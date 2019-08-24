package cn.com.boco.dss.subject.towerqs.check.repository;

import cn.com.boco.dss.subject.towerqs.check.domain.TowerCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;




public interface TowerCheckRepository extends JpaRepository<TowerCheck,Integer> {

    @Modifying
    @Query("UPDATE TowerCheck f SET f.status =?1 WHERE f.dataIndex=?2")
    public void updateStatusById(Integer status,Integer id );
}
