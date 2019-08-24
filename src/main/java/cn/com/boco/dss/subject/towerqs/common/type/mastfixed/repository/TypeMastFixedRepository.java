package cn.com.boco.dss.subject.towerqs.common.type.mastfixed.repository;

import cn.com.boco.dss.subject.towerqs.common.type.mastfixed.domain.TypeMastFixed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 15:34
 **/
@Repository
public interface TypeMastFixedRepository  extends JpaRepository<TypeMastFixed,String> {
}
