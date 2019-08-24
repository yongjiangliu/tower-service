package cn.com.boco.dss.subject.towerqs.detection.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.subject.common.gsonadapter.GsonUtil;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.common.helper.FilePathUtil;
import cn.com.boco.dss.subject.common.helper.FileTypeEnum;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.detection.domain.TowerDetection;
import cn.com.boco.dss.subject.towerqs.detection.service.TowerDetectionService;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;
import io.swagger.annotations.Api;

@Api(value = "检测记录")
@Controller
public class TowerDetectionController {

	private static Logger logger = LoggerFactory.getLogger(TowerDetectionController.class);

	@Value("${dss.tower.img.save-path}")
	private String savePath;

	@Autowired
	private TowerService towerService;

	@Autowired
	private TowerDetectionService towerDetectionService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private TowerRiskService towerRiskService;
	
	@PostMapping(value = "dss/TowerService/detection/addinitdata")
	@ResponseBody
	public JsonData addInitDetectionList(@RequestParam(value = "towerIds", required = true) String towerIds) {
		Gson gson = GsonUtil.buildGson();
		List<String> towerIdList = gson.fromJson(towerIds, new TypeToken<List<String>>() {}.getType());
		List<Tower> towerList = towerService.findAll(towerIdList);
		List<TowerDetection> detectionList = new LinkedList<TowerDetection>();
		for(Tower tower : towerList) {
			tower.setStatus("2");// 2：已下发检测工单
			TowerDetection detection = new TowerDetection();
			detection.setTowerID(tower.getTowerID());
			detection.setResourceID(tower.getResourceCode());
			detection.setAddressCode(tower.getAddressCode());
			detection.setStatus(2);
			detectionList.add(detection);
		}
		towerDetectionService.saveAll(detectionList);
		towerService.saveAll(towerList);
		JsonData jd = new JsonData();
		jd.setData("Add succeed.");
		return jd;
	}

	@PostMapping(value = "dss/TowerService/detection/saveresult")
	@ResponseBody
	public JsonData saveDetectionResult(
			@RequestParam(value = "towerImgFiles", required = false) MultipartFile[] towerImgFiles,
			@RequestParam(value = "riskImgFiles", required = false) MultipartFile[] riskImgFiles,
			@RequestParam(value = "reportFiles", required = false) MultipartFile[] reportFiles,
			@RequestParam(value = "recoverImgFiles", required = false) MultipartFile[] recoverImgFiles,
			@RequestParam(value = "detection", required = true) String detection,
			@RequestParam(value = "risks", required = false) String risks) {
		JsonData jd = new JsonData();
		try {
			Gson gson = GsonUtil.buildGson();
			TowerDetection towerDetection = gson.fromJson(detection, TowerDetection.class);
			List<TowerRisk> riskList = gson.fromJson(risks, new TypeToken<List<TowerRisk>>() {
			}.getType());
			Tower tower = towerService.findById(towerDetection.getTowerID());
			Area area = areaService.findById(tower.getAreaID());
			towerDetection = saveDetectionAndTowerRisks(riskList, towerDetection,tower);
			FilePath filePath = createFilePath(towerDetection, tower, area);
			saveTowerImgFiles(towerImgFiles, filePath);
			saveRiskImgFiles(riskImgFiles, filePath, riskList);
			saveReportImgFiles(reportFiles, filePath);
			saveRecoverImgFiles(recoverImgFiles, filePath);
			jd.setData("Save succeed.");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			jd.setStatus("100");
			jd.setData("Save failed.");
		}
		return jd;
	}

	private TowerDetection saveDetectionAndTowerRisks(List<TowerRisk> riskList, TowerDetection towerDetection, Tower tower) {
		towerDetection.setStatus(2);// 2：已下发检测工单
		towerDetection = towerDetectionService.save(towerDetection);
		for (TowerRisk risk : riskList) {
			risk.setDetectionDataIndex(towerDetection.getDataIndex());
			risk.setTowerID(towerDetection.getTowerID());
			risk.setRecordDate(new Date());
			risk.setRepairStatus(0);//施工状态，0已计划（已下单），1施工中，2结束
			risk.setRiskStatus(0);//隐患状态，0未解决，1-整治中，2-已解决
			risk.setRepairResult(0);//整治结果，0未解决，1-解决，2-暂停
			risk.setAddressCode(StringUtil.isNullOrEmpty(towerDetection.getAddressCode())?tower.getAddressCode():towerDetection.getAddressCode());
			risk.setResourceCode(StringUtil.isNullOrEmpty(towerDetection.getResourceID())?tower.getResourceCode():towerDetection.getResourceID());
		}
		towerRiskService.saveAll(riskList);
		backfillTower(tower,towerDetection,riskList);
		towerService.save(tower);
		return towerDetection;
	}
	
	private void backfillTower(Tower tower,TowerDetection detection, List<TowerRisk> riskList) {
		if(StringUtil.isBlank(detection.getCoordinateE())){
			tower.setCoordinateE(detection.getCoordinateE());
		}
		if(StringUtil.isBlank(detection.getCoordinateN())) {
			tower.setCoordinateN(detection.getCoordinateN());
		}
		if(detection.getTowerHeight()!=null) {
			tower.setTowerHeight(detection.getTowerHeight());
		}
		if(detection.getElevationHeight()!=null) {
			tower.setElevationHeight(detection.getElevationHeight());
		}
		if(detection.getAntennaCount()!=null) {
			tower.setAntennaCount(detection.getAntennaCount());
		}
		if(detection.getAntennaCount5G()!=null) {
			tower.setAntennaCount5G(detection.getAntennaCount5G());
		}
		if(detection.getAntennaPloEmptyCount()!=null) {
			tower.setAntennaPloEmptyCount(detection.getAntennaPloEmptyCount());
		}
		if(detection.getAntennaPlotCount()!=null) {
			tower.setAntennaPlotCount(detection.getAntennaPlotCount());
		}
		if(detection.getAntennaPlotUsedCount()!=null) {
			tower.setAntennaPlotUsedCount(detection.getAntennaPlotUsedCount());
		}
		if(detection.getAntirustThikness()!=null) {
			tower.setAntirustThikness(detection.getAntirustThikness());
		}
		int detectionRiskCount = 0;
		int detectionRiskCountA = 0;
		for(TowerRisk risk : riskList) {
			if(risk.getRiskLevel()!=null&&risk.getRiskLevel().equals(1)) {
				detectionRiskCountA++;
			}
			detectionRiskCount++;
		}
		if(tower.getDetectionRiskCount()==null) {
			tower.setDetectionRiskCount(detectionRiskCount);
		}
		else {
			tower.setDetectionRiskCount(detectionRiskCount+tower.getDetectionRiskCount());
		}
		if(tower.getDetectionRiskCountA()==null) {
			tower.setDetectionRiskCountA(detectionRiskCountA);
		}
		else {
			tower.setDetectionRiskCountA(detectionRiskCountA+tower.getDetectionRiskCountA());
		}
	}

	private void saveTowerImgFiles(MultipartFile[] towerImgFiles, FilePath filePath) {
		String folderPath = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_towerImg);
		saveFiles(towerImgFiles, folderPath);
		
		folderPath = FilePathUtil.getFilePath(filePath, FileTypeEnum.towerImg);
		saveFiles(towerImgFiles, folderPath);
	}

	private void saveRiskImgFiles(MultipartFile[] towerImgFiles, FilePath filePath, List<TowerRisk> riskList) {
		String folderPath = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_riskImg);
		saveRiskImageFiles(towerImgFiles, folderPath, riskList);
	}

	private void saveReportImgFiles(MultipartFile[] towerImgFiles, FilePath filePath) {
		String folderPath = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_report);
		saveFiles(towerImgFiles, folderPath);
	}

	private void saveRecoverImgFiles(MultipartFile[] towerImgFiles, FilePath filePath) {
		String folderPath = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_drawingImg);
		saveFiles(towerImgFiles, folderPath);
	}

	private FilePath createFilePath(TowerDetection towerDetection, Tower tower, Area area) {
		FilePath filePath = new FilePath();
		filePath.setAddRessCode(tower.getAddressCode());
		filePath.setAreaName(area.getAreaName());
		filePath.setProvinceName(area.getProvinceName());
		filePath.setConstractCode(towerDetection.getConstractCode());
		filePath.setLocation(tower.getLocation());
		filePath.setDateTime(DateHelper.dateToString(towerDetection.getDetectionDate(), "yyyyMMdd"));
		filePath.setTowerName(tower.getTowerName());
		return filePath;
	}

	private void saveFile(MultipartFile file, String folderPath,String fileName) {
		BufferedOutputStream bufferedOutputStream = null;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				File newFile = new File(savePath + folderPath + fileName);
				newFile.getParentFile().mkdirs();
				bufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(newFile));
				bufferedOutputStream.write(bytes);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (bufferedOutputStream != null) {
					try {
						bufferedOutputStream.close();
					} catch (IOException e) {
					}
				}
			}

		}
	}

	private void saveRiskImageFiles(MultipartFile[] multipartFiles, String folderPath, List<TowerRisk> riskList) {

		for (TowerRisk risk : riskList) {
			if (risk.getImageNameList() == null) {
				continue;
			}
			for (int i = 0; i < risk.getImageNameList().size(); i++) {
				String imageName = risk.getImageNameList().get(i);
				for (MultipartFile file : multipartFiles) {
					if (file.getOriginalFilename().equals(imageName)) {
						String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
						String fileName = risk.getRiskPosition() + "-" + risk.getRiskName() + "-" + i+filetype;
						saveFile(file, folderPath ,fileName);
						break;
					}
				}
			}
		}

	}

	private void saveFiles(MultipartFile[] multipartFiles, String folderPath) {
		for (MultipartFile file : multipartFiles) {
			saveFile(file, folderPath,file.getOriginalFilename());
		}
	}
}
