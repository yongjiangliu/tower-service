package cn.com.boco.dss.subject.towerqs.tower.repository;

import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TowerRepository extends JpaRepository<Tower, String> {

    @Modifying
    @Query("delete from Tower  where towerID in (?1)")
    void deleteByIdList(List<String> idList);

    @Query("select t.towerID from Tower t where t.areaID = ?1  order by t.towerID desc ")
    Page<String> findAllTowerId(String areaID, Pageable pageable);
}
