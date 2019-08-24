package cn.com.boco.dss.subject.towerqs.common.type.baseposition.repository;

import cn.com.boco.dss.subject.towerqs.common.type.baseposition.domain.TypeBasePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 11:18
 **/
@Repository
public interface TypeBasePositionRepository extends JpaRepository<TypeBasePosition, String> {
}
