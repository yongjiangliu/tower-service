package cn.com.boco.dss.subject.towerqs.common.geo.area.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import org.springframework.stereotype.Repository;

/**
 * @program: TowerService
 * @author: lyj
 * @create: 2019-08-15 20:38
 **/
@Repository
public interface AreaRepository extends JpaRepository<Area, String> {

    Area findAreaByAreaName(String areaName);
}
