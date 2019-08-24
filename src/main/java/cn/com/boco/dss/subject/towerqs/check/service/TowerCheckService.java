package cn.com.boco.dss.subject.towerqs.check.service;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.database.JpaTowerConfig;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.common.helper.FilePathUtil;
import cn.com.boco.dss.subject.common.helper.ToolUtil;
import cn.com.boco.dss.subject.towerqs.check.domain.TowerCheck;
import cn.com.boco.dss.subject.towerqs.check.repository.TowerCheckRepository;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.repair.domain.TowerRepair;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TowerCheckService {
    @Autowired
    private TowerCheckRepository towerCheckRepository;
    @Autowired
    private TowerService ts;

    @Autowired
    private AreaService as;

    @Autowired
    private TowerRiskService trs;

    public TowerCheck save(TowerCheck towerCheck) {
      return   towerCheckRepository.save(towerCheck);
    }


    public List<TowerCheck> findAll() {
        List<TowerCheck> tcList = new ArrayList<TowerCheck>();
        tcList = towerCheckRepository.findAll();
        return tcList;
    }

    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
    public void updateStatusById(Integer status, Integer id) {
        towerCheckRepository.updateStatusById(status, id);
    }

    //@Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
//    public FilePath insertAndUpdate(TowerCheck check, List<TowerRisk> riskList) {
//        try {
//            Tower tower = ts.findById(check.getTowerID());
//            Area area = as.findById(tower.getAreaID());
//            check.setResourceCode(tower.getResourceCode());
//            check.setAddressCode(tower.getAddressCode());
//
//            setTowerByCheck(check, tower);
//            setTowerRiskCount(tower);
//            ts.save(tower);
//            check.setStatus(1);// 1：已下巡检测工单
//            save(check);
//            for (TowerRisk risk : riskList) {
//                risk.setCheckDataIndex(check.getDataIndex());
//                risk.setTowerID(check.getTowerID());
//                risk.setRecordDate(new Date());
//                risk.setRepairStatus(0);//施工状态，0已计划（已下单），1施工中，2结束
//                risk.setRiskStatus(0);//隐患状态，0未解决，1-整治中，2-已解决
//                risk.setRepairResult(0);//整治结果，0未解决，1-解决，2-暂停
//                risk.setAddressCode(tower.getAddressCode());
//                risk.setResourceCode(tower.getResourceCode());
//            }
//            trs.saveAll(riskList);
//            FilePath filePath = setFilePath(area, tower, check, "save");
//            return filePath;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void setTowerRiskCount(Tower tower) {
//        long checkCount = trs.findCheckCountByLevel(tower.getTowerID(), Arrays.asList(0), 0);
//        long checkCountA = trs.findCheckCountByLevel(tower.getTowerID(), Arrays.asList(0), 1);
//        long detectionCount = trs.findDetectionCountByLevel(tower.getTowerID(), Arrays.asList(0), 0);
//        long detectionCountA = trs.findDetectionCountByLevel(tower.getTowerID(), Arrays.asList(0), 1);
//        Integer level = 0;
//        //风险评级，如果以上隐患为0则此值为0，A类不为0，则此值为1，如果A类为0但隐患数量>=5，此值为2，隐患数量<5此值为3
//        if (checkCount == 0 && checkCountA == 0 && detectionCount == 0 && detectionCountA == 0) {
//            level = 0;
//        } else if (checkCountA != 0 && detectionCountA != 0) {
//            level = 1;
//        } else if (checkCountA == 0 && detectionCountA == 0 && checkCount + detectionCount >= 5) {
//            level = 2;
//        } else if (checkCountA == 0 && detectionCountA == 0 && checkCount + detectionCount < 5) {
//            level = 3;
//        }
//        tower.setCheckRiskCount(String.valueOf(checkCount));
//        tower.setCheckRiskCountA(String.valueOf(checkCountA));
//        tower.setDetectionRiskCount(String.valueOf(detectionCount));
//        tower.setDetectionRiskCountA(String.valueOf(detectionCountA));
//        tower.setRiskLevel(String.valueOf(level));
//    }
//
//    private void setTowerByCheck(TowerCheck check, Tower tower) {
//        try {
//            Field[] checkFields = check.getClass().getDeclaredFields();
//            Field[] towerFields = tower.getClass().getDeclaredFields();
//            for (Field checkField : checkFields) {
//                checkField.setAccessible(true);
//                Object checkFieldObject = checkField.get(check);
//                System.out.println(checkField.getName());
//                System.out.println(checkFieldObject);
//                System.out.println(checkField.getType());
//                boolean isSetField = false;
//                if (checkFieldObject != null) {
//                    if (String.class == checkField.getType()) {
//                        boolean isNullOrEmpty = StringUtil.isNullOrEmpty(checkFieldObject.toString());
//                        boolean isNULL = StringUtil.isEqual(checkFieldObject.toString(), "NULL");
//                        if (!isNullOrEmpty && !isNULL) {
//                            isSetField = true;
//                        }
//                    } else if (Integer.class == checkField.getType()) {
//                        if (!StringUtil.isEqual(checkFieldObject.toString(), "0")) {
//                            isSetField = true;
//                        }
//                    } else if (int.class == checkField.getType()) {
//                        if (!StringUtil.isEqual(checkFieldObject.toString(), "0")) {
//                            isSetField = true;
//                        }
//                    } else if (double.class == checkField.getType()) {
//                        if (!StringUtil.isEqual(checkFieldObject.toString(), "0.0")) {
//                            isSetField = true;
//                        }
//                    } else if (Date.class == checkField.getType()) {
//
//                        isSetField = true;
//                    }
//                }
//                if (isSetField) {
//                    for (Field towerField : towerFields) {
//                        if (StringUtil.isEqual(towerField.getName(), checkField.getName())) {
//                            towerField.setAccessible(true);
//                            towerField.set(tower, checkFieldObject.toString());
//                            break;
//                        }
//                    }
//                }
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static FilePath setFilePath(Area area, Tower tower, TowerCheck check, String type) {
//
//        FilePath filePath = new FilePath();
//        filePath.setProvinceName(area.getProvinceName());
//        filePath.setAreaName(area.getAreaName());
//        filePath.setLocation(tower.getLocation());
//        filePath.setTowerName(tower.getTowerName());
//        filePath.setAddRessCode(tower.getAddressCode());
//        // filePath.setType(type);
//        filePath.setDateTime(DateHelper.dateToString(DateHelper.getDateNow(), "yyyyMMdd"));
//        filePath.setConstractCode(check.getConstractCode());
//        return filePath;
//    }

}
