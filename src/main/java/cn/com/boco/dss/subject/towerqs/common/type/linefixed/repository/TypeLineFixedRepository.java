package cn.com.boco.dss.subject.towerqs.common.type.linefixed.repository;

import cn.com.boco.dss.subject.towerqs.common.type.linefixed.domain.TypeLineFixed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 15:29
 **/
@Repository
public interface TypeLineFixedRepository extends JpaRepository<TypeLineFixed, String> {


}
