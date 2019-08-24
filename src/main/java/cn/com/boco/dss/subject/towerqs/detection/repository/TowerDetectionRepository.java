package cn.com.boco.dss.subject.towerqs.detection.repository;

import cn.com.boco.dss.subject.towerqs.detection.domain.TowerDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TowerDetectionRepository  extends JpaRepository<TowerDetection,Integer> {

    @Modifying
    @Query("UPDATE TowerDetection f SET f.status =?1 WHERE f.dataIndex=?2")
    public void updateStatusById(Integer status,Integer id );
}
