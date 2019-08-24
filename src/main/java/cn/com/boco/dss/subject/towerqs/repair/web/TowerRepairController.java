package cn.com.boco.dss.subject.towerqs.repair.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.common.gsonadapter.GsonUtil;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.common.helper.FilePathUtil;
import cn.com.boco.dss.subject.common.helper.FileTypeEnum;
import cn.com.boco.dss.subject.towerqs.repair.domain.TowerRepair;
import cn.com.boco.dss.subject.towerqs.repair.service.TowerRepairService;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;
import io.swagger.annotations.Api;

@Api(value = "整治结果提交", description = "整治结果提交")
@Controller
public class TowerRepairController {

    @Value("${dss.tower.img.save-path}")
    private String savePath;
    @Value("${dss.tower.img.read-path}")
    private String readPath;

    @Autowired
    private TowerRepairService trs;

    @Autowired
    private TowerRiskService riskService;

    @Autowired
    private TowerService towerService;

    @RequestMapping(value = "dss/TowerService/repair/repairSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonData repairSubmit(@RequestParam(value = "towerImg") MultipartFile[] towerImg,
                                 @RequestParam(value = "riskBeforeImgs") MultipartFile[] riskBeforeImgs,
                                 @RequestParam(value = "riskIngImgs") MultipartFile[] riskIngImgs,
                                 @RequestParam(value = "riskAfterImgs") MultipartFile[] riskAfterImgs,
                                 @RequestParam(value = "riskReport") MultipartFile[] riskReport,
                                 @RequestParam(value = "repair") String repair,
                                 @RequestParam(value = "risk") String risk
    ) {
        JsonData jd = new JsonData();

        TowerRepair towerRepair = new Gson().fromJson(repair, TowerRepair.class);
        List<TowerRisk> towerRisks = new Gson().fromJson(risk, new TypeToken<List<TowerRisk>>() {
        }.getType());

        FilePath filePath = trs.insertAndUpdate(towerRepair, towerRisks);
        upLoadFile(filePath, towerImg, FileTypeEnum.repair_towerImg);
        upLoadFile(filePath, riskBeforeImgs, FileTypeEnum.repair_riskImg_before);
        upLoadFile(filePath, riskIngImgs, FileTypeEnum.repair_riskImg_ing);
        upLoadFile(filePath, riskAfterImgs, FileTypeEnum.repair_riskImg_after);
        upLoadFile(filePath, riskReport, FileTypeEnum.repair_report);
        jd.setData("SUCCESS");
        return jd;
    }

    private void upLoadFile(FilePath filePath, MultipartFile[] files, FileTypeEnum fileTypeEnum) {
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

    @RequestMapping(value = "dss/TowerService/repair/insertnulldata", method = RequestMethod.POST)
    @ResponseBody
    public JsonData insertnulldata(@RequestParam(value = "riskIds") String  riskIds,
                                 @RequestParam(value = "towerId") String towerId) {
        JsonData jd = new JsonData();
        Gson gson = GsonUtil.buildGson();
        List<String> riskIdList = gson.fromJson(riskIds, new TypeToken<List<String>>() {
        }.getType());

        List<TowerRepair> repairList = new ArrayList<TowerRepair>();
        List<Integer> ids = new ArrayList<Integer>();
        for (String id : riskIdList) {
            ids.add(Integer.parseInt(id));
            TowerRepair repair = new TowerRepair();
            repair.setRiskDataIndex(Integer.parseInt(id));
            repair.setTowerID(towerId);
            repair.setStatus(0);
            repair.setUpdateDate(DateHelper.getDateNow());
            repairList.add(repair);
        }
        trs.saveAll(repairList);
        Tower tower = towerService.findById(towerId);
        tower.setStatus("3");
        towerService.save(tower);
        List<TowerRisk> riskList = riskService.findAllById(ids);
        for (TowerRisk risk : riskList) {
            for (TowerRepair repair : repairList) {
                if (risk.getDataIndex() == repair.getRiskDataIndex()) {
                    risk.setRepairDateIndex(repair.getDataIndex());
                }

            }
        }
        riskService.saveAll(riskList);
        jd.setData("SUCCESS");
        return jd;


    }


    @RequestMapping(value = "dss/TowerService/repair/findall", method = RequestMethod.POST)
    @ResponseBody
    public JsonData repairSubmit(@RequestParam(value = "p") String p, @RequestParam(value = "p1") String p1, @RequestParam(value = "file") MultipartFile[] file, @RequestParam(value = "file1") MultipartFile[] file1, HttpServletRequest request) throws IOException {
        JsonData jd = new JsonData();
        TowerRepair tr = new Gson().fromJson(request.getParameter("p"), TowerRepair.class);
        TowerRisk risk = new Gson().fromJson(request.getParameter("p1"), TowerRisk.class);

//        for (MultipartFile m : file) {
//            String path = "D:/dd2222/" + m.getOriginalFilename();
//            File newFile = new File(path);
//            if (!(newFile.getParentFile().exists() && newFile.getParentFile().isDirectory())) {
//                newFile.getParentFile().mkdirs();
//            }
//           m.transferTo(newFile);
//        }
//
//
//        // riskService.save(risk);
//        new Gson().toJson(trs.findAll());
//        new Gson().toJson(riskService.findAll());
//        jd.setData(trs.findAll());
        //        List<String> strings = new Gson().fromJson(request.getParameter("riskIds"), new TypeToken<List<String>>() {
//        }.getType());
        trs.insertAndUpdate(tr, Arrays.asList(risk));
        return jd;
    }


}
