package cn.com.boco.dss.subject.towerqs.risk.service;

import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.database.JpaTowerConfig;
import cn.com.boco.dss.subject.towerqs.check.service.TowerCheckService;
import cn.com.boco.dss.subject.towerqs.detection.service.TowerDetectionService;
import cn.com.boco.dss.subject.towerqs.repair.domain.TowerRepair;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.repository.TowerRiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TowerRiskService {

    @Autowired
    private TowerRiskRepository trr;

    @Autowired
    private TowerCheckService tcs;

    @Autowired
    private TowerDetectionService tds;


    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
    public void updateStatusByTowerIds(List<String> towerId) {
        trr.updateStatusByTowerIds(towerId);
    }

    public long findCheckCount(String towerId, Integer type) {
        return trr.findCheckCount(towerId, type);
    }

    public long findDetectionCount(String towerId) {
        return trr.findDetectionCount(towerId);
    }

    public long findRepairCount(String towerId) {
        return trr.findRepairCount(towerId);
    }

    public void save(TowerRisk tr) {
        trr.save(tr);
    }

    public void saveAll(List<TowerRisk> trList) {
        trr.saveAll(trList);
    }

    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
    public List<TowerRisk> findAll() {
        List<TowerRisk> tr = trr.findAll();
        return tr;
    }

    public TowerRisk findById(Integer id) {

        return trr.findById(id).orElse(null);
    }

    public List<TowerRisk> findAllById(List<Integer> ids) {

        return trr.findAllById(ids);
    }


    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
    public void updateRiskByIds(List<TowerRisk> riskList, List<TowerRepair> trList, List<Integer> ids) {
        List<TowerRisk> risks = trr.findAllById(ids);

        for (TowerRisk risk : risks) {
            for (TowerRisk tRisk : riskList) {
                if (risk.getDataIndex() == tRisk.getDataIndex()) {

                    risk.setRepairDateIndex(getRepairIdByRiskId(trList, tRisk.getDataIndex()));
                    risk.setRepairDate(tRisk.getRepairDate());
                    risk.setRepairStatus(tRisk.getRepairStatus());
                    risk.setRepairResult(tRisk.getRepairResult());
                    risk.setRiskStatus(tRisk.getRiskStatus());
                    if (!StringUtil.isNullOrEmpty(tRisk.getRepairMethod())) {
                        risk.setRepairMethod(tRisk.getRepairMethod());
                    } else if (tRisk.getRepairPrice() != null) {
                        risk.setRepairPrice(tRisk.getRepairPrice());
                    }
                }
            }
            if (risk.getCheckDataIndex() != null) {
                tcs.updateStatusById(0, risk.getCheckDataIndex());
            } else if (risk.getCheckDataIndex() != null) {
                tds.updateStatusById(0, risk.getDetectionDataIndex());
            }
        }


        trr.saveAll(risks);
    }

    private Integer getRepairIdByRiskId(List<TowerRepair> trList, Integer riskId) {
        TowerRepair repair = trList.stream().filter(p -> p.getRiskDataIndex() == riskId).findFirst().orElse(null);
        return repair.getDataIndex();
    }

    public List<TowerRisk> findByTowerID(String towerID) {
        return trr.findByTowerID(towerID);
    }

    public long findCheckCountByLevel(String towerId,List<Integer> level, List<Integer> type  ) {
        return trr.findCheckCountByLevel(towerId, level, type);
    }

    public long findDetectionCountByLevel(String towerId, List<Integer> level, List<Integer> type) {
        return trr.findDetectionCountByLevel(towerId, level, type);
    }
}
