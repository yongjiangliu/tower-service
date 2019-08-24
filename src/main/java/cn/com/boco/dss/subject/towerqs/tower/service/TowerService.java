package cn.com.boco.dss.subject.towerqs.tower.service;

import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.database.JpaTowerConfig;
import cn.com.boco.dss.subject.towerqs.common.constant.NumberConstant;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.repository.TowerForMysql;
import cn.com.boco.dss.subject.towerqs.tower.repository.TowerRepository;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TowerService {

    @Autowired
    private TowerRepository towerRepository;

    @Autowired
    private TowerForMysql towerForMysql;

    public List<Tower> findAll() {
        return towerRepository.findAll();
    }

    public Tower findById(String towerId) {
        return towerRepository.findById(towerId).orElse(null);
    }

   public List<Tower> findAll(List<String> towerIdList){
    	return towerRepository.findAllById(towerIdList);
    }

    public void deleteById(String towerId) {
        towerRepository.deleteById(towerId);
    }

    @Transactional(value = JpaTowerConfig.C_TRANSACTIONMANAGER_NAME, rollbackFor = Exception.class)
    public void deleteByIdList(List<String> idList) {
        towerRepository.deleteByIdList(idList);
    }

    public void delete(Tower tower) {
        towerRepository.delete(tower);
    }

    public void delete(List<Tower> towerList) {
        towerRepository.deleteInBatch(towerList);
    }

    public void save(Tower tower) {
        String towerID = tower.getTowerID();
        String areaID = tower.getAreaID();
        if (StringUtils.isBlank(towerID)) {
            tower.setTowerID(getNewTowerID(areaID, towerID));
            tower.setStatus("0");
        }
        towerRepository.save(tower);
    }

    public void saveAll(List<Tower> towerList) {
        towerRepository.saveAll(towerList);
    }

    public CommonData queryData(String areaID, int pageIndex, int pageSize, int sortColIndex, SortType sortType)
            throws Exception {
        return towerForMysql.queryData(areaID, pageIndex, pageSize, sortColIndex, sortType);
    }

    public long queryCount(String areaID, int pageIndex, int pageSize, int sortColIndex, SortType sortType)
            throws Exception {
        return towerForMysql.queryCount(areaID, pageIndex, pageSize, sortColIndex, sortType);
    }

    public String findMaxTowerId(String areaID) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<String> pageIdList = towerRepository.findAllTowerId(areaID, pageable);
        List<String> idList = pageIdList.getContent();
        if (idList.size() > 0) {
            return idList.get(0);
        }
        return null;
    }

    private String getNewTowerID(String areaID, String towerID) {
        String maxTowerID = findMaxTowerId(areaID);
        StringBuilder sb = new StringBuilder();
        if (maxTowerID == null) {
            sb.append(areaID).append(NumberConstant.NUMBER_START);
        } else {
//            int length = maxTowerID.length();
//            int newSeq = Integer.parseInt(maxTowerID.substring(length - 6)) + 1;
            int length = maxTowerID.length();
            String beforeStr = maxTowerID.substring(0, length - 3);
            String afterStr = String.valueOf(Integer.parseInt(maxTowerID.substring(length - 3)) + 1);
            afterStr = afterStr.length() == 1 ? "00" + afterStr : afterStr.length() == 2 ? "0" + afterStr : afterStr;
            sb.append(beforeStr).append(afterStr);
        }
        return sb.toString();
    }


}
