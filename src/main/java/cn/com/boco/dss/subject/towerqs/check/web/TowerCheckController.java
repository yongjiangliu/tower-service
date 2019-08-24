package cn.com.boco.dss.subject.towerqs.check.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.common.data.DataColumn;
import cn.com.boco.dss.common.data.DataRow;
import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.config.xml.ItemInfo;
import cn.com.boco.dss.data.DbHelper;
import cn.com.boco.dss.database.SqlQuery;
import cn.com.boco.dss.framework.security.domain.TokenUser;
import cn.com.boco.dss.framework.security.web.TokenUserUtils;
import cn.com.boco.dss.subject.common.gsonadapter.GsonUtil;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.common.helper.FilePathUtil;
import cn.com.boco.dss.subject.common.helper.FileTypeEnum;
import cn.com.boco.dss.subject.common.helper.ToolUtil;
import cn.com.boco.dss.subject.towerqs.check.domain.TowerCheck;
import cn.com.boco.dss.subject.towerqs.check.service.TowerCheckService;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "巡检工单")
@Controller
public class TowerCheckController {
	Logger log = LoggerFactory.getLogger(TowerCheckController.class);

	@Value("${dss.tower.img.save-path}")
	private String savePath;

	@Value("${dss.tower.img.read-path}")
	private String readPath;

	@Autowired
	private TowerCheckService tcs;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TowerService towerService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private TowerRiskService towerRiskService;

	private FilePath filePath = new FilePath();

	@ApiOperation(value = "巡检工单立即下发接口", notes = "")
	@PostMapping(value = "dss/TowerService/check/addinitdata")
	@ResponseBody
	public JsonData addInitCheckList(@RequestParam(value = "towerIds", required = true) String towerIds) {
		JsonData jd = new JsonData();
		try {
			Gson gson = GsonUtil.buildGson();
			List<String> towerIdList = gson.fromJson(towerIds, new TypeToken<List<String>>() {
			}.getType());
			List<Tower> towerList = towerService.findAll(towerIdList);
			List<TowerCheck> checkList = new LinkedList<TowerCheck>();
			for (Tower tower : towerList) {
				tower.setStatus("1");// 1：已下发巡检工单
				TowerCheck check = new TowerCheck();
				check.setTowerID(tower.getTowerID());
				check.setResourceCode(tower.getResourceCode());
				check.setAddressCode(tower.getAddressCode());
				check.setStatus(1);
				checkList.add(check);
			}
			tcs.saveAll(checkList);
			towerService.saveAll(towerList);
			jd.setData("Add succeed.");
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			jd.setStatus("100");
			jd.setData("Add Failed");
		}
		return jd;
	}

	@RequestMapping(value = "dss/TowerService/risks/resultubmit")
	@ResponseBody
	public JsonData resultSubmit(@RequestParam(value = "workOrderType") String workOrderType,
			@RequestParam(value = "pageIndex") String pageIndex, @RequestParam(value = "pageSize") String pageSize,
			HttpServletRequest request) {

		JsonData jd = new JsonData();
		CommonData commonData = getCommonData(getSql(workOrderType), workOrderType, pageIndex, pageSize, request);
		jd.setData(commonData);

		return jd;
	}

	private CommonData getCommonData(StringBuilder sql, String workOrderType, String pageIndex, String pageSize,
			HttpServletRequest request) {
		CommonData cd = new CommonData();
		List<Object> queryList = new ArrayList<Object>();
		List<Integer> queryTypes = new ArrayList<Integer>();
		String zoneId = getZoneId(request);
		if (!StringUtil.isEqual(zoneId, "-1") && !StringUtil.isNullOrEmpty(zoneId)
				&& !StringUtil.isEqual(zoneId, "280")) {
			sql.append(" AND t2.AreaID = ? ");
			queryList.add(zoneId);
			queryTypes.add(Types.VARCHAR);
		}
//		String name = "";
//        if (workOrderType.equals("check")) {
//            name = "grid-check";
//
//        } else if (workOrderType.equals("detection")) {
//            name = "grid-detection";
//
//        } else if (workOrderType.equals("check")) {
//            name = "grid-repair";
//
//        }
		int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));

		try {
			cd = geCommonDatatBySql(sql.toString(), "grid-check", queryList.toArray(), types,
					Integer.parseInt(pageIndex), Integer.parseInt(pageSize), false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cd;
	}

	private StringBuilder getSql(String workOrderType) {
		StringBuilder sql = new StringBuilder();
		if (workOrderType.equals("check")) {
			ItemInfo itemInfo = ToolUtil.findItemInfoByXml("grid-check", "config/risk.xml");
			sql.append(itemInfo.getValue());
		} else if (workOrderType.equals("detection")) {
			ItemInfo itemInfo = ToolUtil.findItemInfoByXml("grid-detection", "config/risk.xml");
			sql.append(itemInfo.getValue());
		} else if (workOrderType.equals("repair")) {
			ItemInfo itemInfo = ToolUtil.findItemInfoByXml("grid-repair", "config/risk.xml");
			sql.append(itemInfo.getValue());
		}
		return sql;
	}

	@RequestMapping(value = "dss/TowerService/risks/findGridData")
	@ResponseBody
	public JsonData findGridData(HttpServletRequest request) throws Exception {

		JsonData jd = new JsonData();
		String param1 = request.getParameter("param1");
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");

		JSONObject param1Obj = JSONObject.parseObject(param1);
		String towerID = param1Obj.getString("towerID");

		List<String> ids = new Gson().fromJson(towerID, new TypeToken<List<String>>() {
		}.getType());
		CommonData result = queryData(ids, Integer.parseInt(pageIndex), Integer.parseInt(pageSize), request);
		jd.setData(result);
		return jd;
	}

	private String getZoneId(HttpServletRequest request) {
		TokenUserUtils tokenUserUtils = new TokenUserUtils();
		TokenUser tokenUser = tokenUserUtils.findUserByToken(request);
		String zoneId = tokenUser.getZoneId();
		return zoneId;
	}

	private CommonData queryData(List<String> towerIds, int pageIndex, int pageSize, HttpServletRequest request)
			throws Exception {

		CommonData cd = new CommonData();
		List<Object> queryList = new ArrayList<Object>();
		List<Integer> queryTypes = new ArrayList<Integer>();
		StringBuilder sqlCheck = new StringBuilder();
		StringBuilder sqlDetection = new StringBuilder();
		sqlDetection.append(ToolUtil.findItemInfoByXml("gridData-detection", "config/risk.xml").getValue());
		sqlCheck.append(ToolUtil.findItemInfoByXml("gridData-check", "config/risk.xml").getValue());
		String zoneId = getZoneId(request);
		if (!StringUtil.isEqual(zoneId, "-1") && !StringUtil.isNullOrEmpty(zoneId)
				&& !StringUtil.isEqual(zoneId, "280")) {
			sqlCheck.append(" AND t2.AreaID = ? ");

			String areaID = zoneId;
			queryList.add(areaID);
			queryTypes.add(Types.VARCHAR);
		}

		if (towerIds.size() > 0) {
			sqlCheck.append(" AND ( ");
			for (String str : towerIds) {
				sqlCheck.append(" t1.TowerID =? OR ");
				queryList.add(str);
				queryTypes.add(Types.VARCHAR);
			}
			sqlCheck.delete(sqlCheck.length() - 3, sqlCheck.length());
			sqlCheck.append(" ) ");
		}
		if (!StringUtil.isEqual(zoneId, "-1") && !StringUtil.isNullOrEmpty(zoneId)
				&& !StringUtil.isEqual(zoneId, "280")) {
			sqlDetection.append(" AND t2.AreaID = ? ");
			String areaID = zoneId;
			queryList.add(areaID);
			queryTypes.add(Types.VARCHAR);
		}

		if (towerIds.size() > 0) {
			sqlDetection.append(" AND ( ");
			for (String str : towerIds) {
				sqlDetection.append(" t1.TowerID =? OR ");
				queryList.add(str);
				queryTypes.add(Types.VARCHAR);
			}
			sqlDetection.delete(sqlDetection.length() - 3, sqlDetection.length());
			sqlDetection.append(" ) ");
		}
		String gridSql = sqlCheck.toString() + " UNION ALL " + sqlDetection.toString();
		int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
		cd = geCommonDatatBySql(gridSql, "gridData-check", queryList.toArray(), types, pageIndex, pageSize, true);
		return cd;
	}

	private CommonData geCommonDatatBySql(String sql, String name, Object[] queryParamsArr, int[] queryTypeArr,
			int pageIndex, int pageSize, boolean isImgPath) throws Exception {
		String sqlCount = "SELECT COUNT(*) FROM (" + sql + ") TAB ";
		DataTable dtCount = DbHelper.getDataBySql(sqlCount, queryParamsArr, queryTypeArr,
				sqlQuery.getConnectSettings());

		String sqlLimit = "SELECT * FROM (" + sql + ") TAB LIMIT " + (pageIndex - 1) * pageSize + "," + pageSize;
		DataTable dt = DbHelper.getDataBySql(sqlLimit, queryParamsArr, queryTypeArr, sqlQuery.getConnectSettings());
		ItemInfo itemInfo = ToolUtil.findItemInfoByXml(name, "config/risk.xml");
		String colName = itemInfo.getProperty1();
		List<String> arr = Arrays.asList(colName.split(","));
		for (DataColumn col : dt.getColumns()) {
			for (String str : arr) {
				if (StringUtil.isEqual(col.getColumnName(), str.split(":")[0])) {
					col.setCaptionName(str.split(":")[1]);
					break;
				}
			}
		}
		if (isImgPath) {
			setImgPath(dt, savePath, readPath, dt.getColumns().size() - 1);
		}
		CommonData commonData = CommonData.fromDataTable(dt);
		commonData.setPageIndex(pageIndex);
		commonData.setPageSize(pageSize);
		commonData.setTotalCount(Long.parseLong(String.valueOf(dtCount.getValue(0, 0))));
		return commonData;
	}

	public DataTable setImgPath(DataTable dt, String savePath, String readPath, int colIndex) {
		try {
			for (DataRow dr : dt.getRows()) {
				String path = "";
				String workOrderType = String.valueOf(dr.getValue("workOrderType"));
				FilePath filePath = new FilePath();
				filePath.setProvinceName(String.valueOf(dr.getValue("ProvinceName")));
				filePath.setAreaName(String.valueOf(dr.getValue("AreaName")));
				filePath.setLocation(String.valueOf(dr.getValue("city")));
				filePath.setTowerName(String.valueOf(dr.getValue("TowerName")));
				filePath.setAddRessCode(String.valueOf(dr.getValue("AddressCode")));
				filePath.setDateTime(String.valueOf(dr.getValue("RiskDate")));
				filePath.setConstractCode(String.valueOf(dr.getValue("constractCode")));
				if (StringUtil.isEqual(workOrderType, "check")) {
					path = FilePathUtil.getFilePath(filePath, FileTypeEnum.check_riskImg_before);
				} else if (StringUtil.isEqual(workOrderType, "detection")) {
					path = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_riskImg);
				} else if (StringUtil.isEqual(workOrderType, "repair")) {
					path = FilePathUtil.getFilePath(filePath, FileTypeEnum.repair_riskImg_before);
				}
				String path1 = savePath + path;
				String name = String.valueOf(dr.getValue("riskName"));
				List<File> files = ToolUtil.getFileSort(path1, name);
				String rPath = readPath + path;
				if (files.size() > 0) {
					rPath += files.get(0).getName();

				}
				dr.setValue(colIndex, rPath);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return dt;
	}

	@RequestMapping(value = "dss/TowerService/check/checkSubmit", method = RequestMethod.POST)
	@ResponseBody
	public JsonData repairSubmit(@RequestParam(value = "towerImg") MultipartFile[] towerImg,
			@RequestParam(value = "riskBeforeImgs") MultipartFile[] riskBeforeImgs,
			@RequestParam(value = "riskIngImgs") MultipartFile[] riskIngImgs,
			@RequestParam(value = "riskAfterImgs") MultipartFile[] riskAfterImgs,
			@RequestParam(value = "riskReport") MultipartFile[] riskReport,
			@RequestParam(value = "questionImg") MultipartFile[] questionImg,
			@RequestParam(value = "check") String check, @RequestParam(value = "risk") String risk) {
		JsonData jd = new JsonData();
		try {
			Gson gson = GsonUtil.buildGson();
			TowerCheck towerCheck = gson.fromJson(check, TowerCheck.class);
			List<TowerRisk> towerRisks = gson.fromJson(risk, new TypeToken<List<TowerRisk>>() {
			}.getType());
			insertAndUpdate(towerCheck, towerRisks);
			upLoadTowerOrReport(filePath, FileTypeEnum.check_towerImg_before, towerImg);
			upLoadRiskImgs(filePath, riskBeforeImgs, FileTypeEnum.check_riskImg_before, towerRisks);
			upLoadRiskImgs(filePath, riskIngImgs, FileTypeEnum.check_riskImg_ing, towerRisks);
			upLoadRiskImgs(filePath, riskAfterImgs, FileTypeEnum.check_riskImg_after, towerRisks);
			upLoadRiskImgs(filePath, questionImg, FileTypeEnum.check_questionImg, towerRisks);
			upLoadTowerOrReport(filePath, FileTypeEnum.check_report, riskReport);

			jd.setData("SUCCESS");
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			jd.setStatus("100");
			jd.setData("FAIL");
		}
		return jd;
	}

	private void insertAndUpdate(TowerCheck check, List<TowerRisk> riskList) {
		try {
			Tower tower = towerService.findById(check.getTowerID());

			// 保存check
			check.setTowerID(tower.getTowerID());
			check.setResourceCode(tower.getResourceCode());
			check.setAddressCode(tower.getAddressCode());
			check.setStatus(1);// 1：已下巡检工单
			tcs.save(check);

			// 更新tower表
			check.setResourceCode(tower.getResourceCode());
			check.setAddressCode(tower.getAddressCode());
			setTowerByCheck(check, tower);
			setTowerRiskCount(tower);
			towerService.save(tower);

			// 保存risk表
			for (TowerRisk risk : riskList) {
				risk.setCheckDataIndex(check.getDataIndex());
				risk.setTowerID(check.getTowerID());
				risk.setResourceCode(tower.getResourceCode());
				risk.setAddressCode(tower.getAddressCode());
				risk.setRiskImageCount(risk.getImageNameList().size());
				risk.setRecordDate(new Date());
				risk.setRepairStatus(0);// 施工状态，0已计划（已下单），1施工中，2结束
				risk.setRiskStatus(risk.getRiskStatus());// 隐患状态，0未解决，1-整治中，2-已解决
				risk.setRepairResult(0);// 整治结果，0未解决，1-解决，2-暂停

				risk.setResourceCode(tower.getResourceCode());
			}
			towerRiskService.saveAll(riskList);

			// 设置目录
			Area area = areaService.findById(tower.getAreaID());
			setFilePath(area, tower, check, "save");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void upLoadTowerOrReport(FilePath filePath, FileTypeEnum fileTypeEnum, MultipartFile[] files) {
		for (MultipartFile file : files) {
			String path = savePath + FilePathUtil.getFilePath(filePath, fileTypeEnum) + file.getOriginalFilename();
			File newFile = new File(path);
			if (!(newFile.getParentFile().exists() && newFile.getParentFile().isDirectory())) {
				newFile.getParentFile().mkdirs();
			}
			try {
				file.transferTo(newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void upLoadRiskImgs(FilePath filePath, MultipartFile[] files, FileTypeEnum fileTypeEnum,
			List<TowerRisk> riskList) {
		if (files.length == 0) {
			return;
		}
		for (TowerRisk risk : riskList) {
			List<String> imgNameList = getImgNames(fileTypeEnum, risk);
			if (imgNameList == null) {
				continue;
			}
			for (int i = 0; i < imgNameList.size(); i++) {
				String imageName = imgNameList.get(i);
				for (MultipartFile file : files) {
					if (file.getOriginalFilename().equals(imageName)) {
						String fileType = file.getOriginalFilename()
								.substring(file.getOriginalFilename().lastIndexOf("."));

						String fileName = risk.getRiskPosition() + "-" + risk.getRiskName() + "-" + i + fileType;
						String path = savePath + FilePathUtil.getFilePath(filePath, fileTypeEnum) + fileName;// file.getOriginalFilename();
						File newFile = new File(path);
						if (!(newFile.getParentFile().exists() && newFile.getParentFile().isDirectory())) {
							newFile.getParentFile().mkdirs();
						}
						try {
							file.transferTo(newFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}

			}
		}

	}

	private List<String> getImgNames(FileTypeEnum fileTypeEnum, TowerRisk risk) {
		List<String> imgNameList = new ArrayList<String>();

		switch (fileTypeEnum) {
		case check_riskImg_before:
			imgNameList = risk.getImageNameListMap().get("imgBefore");
			break;
		case check_riskImg_ing:
			imgNameList = risk.getImageNameListMap().get("imgIng");
			break;
		case check_riskImg_after:
			imgNameList = risk.getImageNameListMap().get("imgAfter");
			break;
		case check_questionImg:
			imgNameList = risk.getImageNameListMap().get("imgQuestion");
			break;
		default:
			break;
		}
		return imgNameList;

	}

	private void setTowerRiskCount(Tower tower) {
		long checkCount = towerRiskService.findCheckCountByLevel(tower.getTowerID(), Arrays.asList(1, 2),
				Arrays.asList(0));
		long checkCountA = towerRiskService.findCheckCountByLevel(tower.getTowerID(), Arrays.asList(1),
				Arrays.asList(0));
//        long detectionCount = towerRiskService.findDetectionCountByLevel(tower.getTowerID(),Arrays.asList(1,2), Arrays.asList(0));
//        long detectionCountA = towerRiskService.findDetectionCountByLevel(tower.getTowerID(), Arrays.asList(1), Arrays.asList(0));

		long detectionCount = Long.parseLong(tower.getDetectionRiskCount());
		long detectionCountA = Long.parseLong(tower.getDetectionRiskCountA());
		Integer level = 0;
		// 风险评级，如果以上隐患为0则此值为0，A类不为0，则此值为1，如果A类为0但隐患数量>=5，此值为2，隐患数量<5此值为3
		if (checkCount == 0 && checkCountA == 0 && detectionCount == 0 && detectionCountA == 0) {
			level = 0;
		} else if (checkCountA != 0 && detectionCountA != 0) {
			level = 1;
		} else if (checkCountA == 0 && detectionCountA == 0 && checkCount + detectionCount >= 5) {
			level = 2;
		} else if (checkCountA == 0 && detectionCountA == 0 && checkCount + detectionCount < 5) {
			level = 3;
		}
		tower.setCheckRiskCount(String.valueOf(checkCount));
		tower.setCheckRiskCountA(String.valueOf(checkCountA));
		tower.setDetectionRiskCount(String.valueOf(detectionCount));
		tower.setDetectionRiskCountA(String.valueOf(detectionCountA));
		tower.setRiskLevel(String.valueOf(level));
	}

	private void setTowerByCheck(TowerCheck check, Tower tower) {
		try {
			Field[] checkFields = check.getClass().getDeclaredFields();
			Field[] towerFields = tower.getClass().getDeclaredFields();
			for (Field checkField : checkFields) {
				checkField.setAccessible(true);
				Object checkFieldObject = checkField.get(check);
				System.out.println(checkField.getName());
				System.out.println(checkFieldObject);
				System.out.println(checkField.getType());
				boolean isSetField = false;
				if (checkFieldObject != null) {
					if (String.class == checkField.getType()) {
						boolean isNullOrEmpty = StringUtil.isNullOrEmpty(checkFieldObject.toString());
						boolean isNULL = StringUtil.isEqual(checkFieldObject.toString(), "NULL");
						if (!isNullOrEmpty && !isNULL) {
							isSetField = true;
						}
					} else if (Integer.class == checkField.getType()) {
						if (!StringUtil.isEqual(checkFieldObject.toString(), "0")) {
							isSetField = true;
						}
					} else if (int.class == checkField.getType()) {
						if (!StringUtil.isEqual(checkFieldObject.toString(), "0")) {
							isSetField = true;
						}
					} else if (double.class == checkField.getType()) {
						if (!StringUtil.isEqual(checkFieldObject.toString(), "0.0")) {
							isSetField = true;
						}
					} else if (Date.class == checkField.getType()) {

						isSetField = true;
					}
				}
				if (isSetField) {
					for (Field towerField : towerFields) {
						if (StringUtil.isEqual(towerField.getName(), checkField.getName())) {
							towerField.setAccessible(true);
							towerField.set(tower, checkFieldObject.toString());
							break;
						}
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private FilePath setFilePath(Area area, Tower tower, TowerCheck check, String type) {

		filePath.setProvinceName(area.getProvinceName());
		filePath.setAreaName(area.getAreaName());
		filePath.setLocation(tower.getLocation());
		filePath.setTowerName(tower.getTowerName());
		filePath.setAddRessCode(tower.getAddressCode());
		// filePath.setType(type);
		filePath.setDateTime(DateHelper.dateToString(DateHelper.getDateNow(), "yyyyMMdd"));
		filePath.setConstractCode(check.getConstractCode());
		return filePath;
	}

	@RequestMapping(value = "dss/TowerService/check/findCkeckById", method = RequestMethod.POST)
	@ResponseBody
	public JsonData findById(HttpServletRequest request) throws Exception {
		JsonData jd = new JsonData();
		List<TowerCheck> check = tcs.findAll();
		jd.setData(check);
		return jd;
	}

}
