package cn.com.boco.dss.subject.towerqs.common.type.scene.repository;

import cn.com.boco.dss.subject.towerqs.common.type.scene.domain.TypeScene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lyj
 * @Title: TypeSceneRepository
 */
@Repository
public interface TypeSceneRepository extends JpaRepository<TypeScene, String> {
}
