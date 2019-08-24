package cn.com.boco.dss.subject.towerqs.common.status.tower.repository;

import cn.com.boco.dss.subject.towerqs.common.status.tower.domain.StatusTower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-21 09:42
 **/
@Repository
public interface StatusTowerRepository extends JpaRepository<StatusTower, String> {
}
