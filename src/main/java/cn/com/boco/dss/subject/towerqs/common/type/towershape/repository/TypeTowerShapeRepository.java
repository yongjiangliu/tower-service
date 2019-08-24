package cn.com.boco.dss.subject.towerqs.common.type.towershape.repository;

import cn.com.boco.dss.subject.towerqs.common.type.towershape.domain.TypeTowerShape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lyj
 * @Title: TypeTowerShapeRepository
 * @Description: TODO
 */
@Repository
public interface TypeTowerShapeRepository extends JpaRepository<TypeTowerShape,String> {
}
