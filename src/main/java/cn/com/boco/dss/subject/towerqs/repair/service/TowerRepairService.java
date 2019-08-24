package cn.com.boco.dss.subject.towerqs.repair.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.database.JpaTowerConfig;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.repair.domain.TowerRepair;
import cn.com.boco.dss.subject.towerqs.repair.repository.TowerRepairRepository;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;

@Service
public class TowerRepairService {

    @Autowired
    private TowerRepairRepository trr;

    @Autowired
    private TowerService ts;

    @Autowired
    private TowerRiskService trs;

    @Autowired
    private AreaService as;

    public TowerRepair save(TowerRepair tr) {
        return trr.save(tr);
    }


    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME,rollbackFor = Exception.class)
    public List<TowerRepair> saveAll(List<TowerRepair> trList) {

        return trr.saveAll(trList);
    }


    public List<TowerRepair> findAll() {
        List<TowerRepair> trList = trr.findAll();
        return trList;
    }

    /**
     * @param repair
     * @param riskList
     */
    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME,rollbackFor = Exception.class)
    public FilePath insertAndUpdate(TowerRepair repair, List<TowerRisk> riskList) {
        Tower tower = ts.findById(repair.getTowerID());
        Area area =as.findById(tower.getAreaID());// as.getOne(tower.getAreaID());
        repair.setResourceCode(tower.getResourceCode());
        repair.setAddressCode(tower.getAddressCode());
        repair.setUpdateDate(DateHelper.getDateNow());
        repair.setStatus(2);


        List<TowerRepair> trList = new ArrayList<TowerRepair>();
        for (Integer id : repair.getRiskIds()) {
            TowerRepair repairNew = new TowerRepair();
            BeanUtils.copyProperties(repair, repairNew);
            repairNew.setRiskDataIndex(id);
            trList.add(repairNew);
        }
        saveAll(trList);

        trs.updateRiskByIds(riskList, trList, repair.getRiskIds());
        tower.setStatus("0");
        tower.setCheckRiskCount(0);
        tower.setCheckRiskCountA(0);
        tower.setDetectionRiskCount(0);
        tower.setDetectionRiskCountA(0);
        tower.setRiskLevel(0);
        ts.save(tower);

        FilePath filePath = setFilePath(area, tower,repair);


        return filePath;

    }


    private FilePath setFilePath(Area area, Tower tower, TowerRepair repair) {

        FilePath filePath = new FilePath();
        filePath.setProvinceName(area.getProvinceName());
        filePath.setAreaName(area.getAreaName());
        filePath.setLocation(tower.getLocation());
        filePath.setTowerName(tower.getTowerName());
        filePath.setAddRessCode(tower.getAddressCode());
        //filePath.setType("save");
        filePath.setDateTime(DateHelper.dateToString(DateHelper.getDateNow(), "yyyyMMdd"));
        filePath.setConstractCode(repair.getConstractCode());
        return filePath;
    }


}
