package cn.com.boco.dss.subject.towerqs.risk.repository;

import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TowerRiskRepository extends JpaRepository<TowerRisk, Integer> {

    @Modifying
    @Query("UPDATE TowerRisk f SET f.riskStatus =1 WHERE f.towerID in (?1)")
    public void updateStatusByTowerIds(List<String> towerId);

    @Query("SELECT COUNT(*) FROM TowerRisk f WHERE f.towerID  = ?1 AND f.checkDataIndex  <> ''  AND f.riskStatus =?2")
    public long findCheckCount(String towerId, Integer type);

    @Query("SELECT COUNT(*) FROM TowerRisk f WHERE f.towerID  = ?1 AND f.detectionDataIndex  <> '' ")
    public long findDetectionCount(String towerId);

    @Query("SELECT  COUNT(*) FROM TowerRisk f WHERE f.repairResult=0 AND f.riskStatus=1 AND f.towerID  = ?1 AND f.repairDateIndex  is null ")
    public long findRepairCount(String towerId);

    @Query("SELECT f FROM TowerRisk f WHERE f.towerID =  ?1")
    List<TowerRisk> findByTowerID(String towerID);


    @Query("SELECT COUNT(*) FROM TowerRisk f WHERE f.checkDataIndex  IS NOT NULL AND f.checkDataIndex<>0 AND f.towerID = ?1 AND f.riskLevel in ( ?2 ) AND f.riskStatus in (?3)")
    public long findCheckCountByLevel(String towerId, List<Integer> level, List<Integer> status);

    @Query("SELECT COUNT(*) FROM TowerRisk f WHERE f.detectionDataIndex  IS NOT NULL AND f.detectionDataIndex<>0 AND f.towerID = ?1 AND f.riskLevel in ( ?2 ) AND f.riskStatus in (?3) ")
    public long findDetectionCountByLevel(String towerId, List<Integer> level, List<Integer> status);
}
