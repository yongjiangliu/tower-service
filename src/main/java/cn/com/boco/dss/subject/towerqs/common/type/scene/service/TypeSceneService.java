package cn.com.boco.dss.subject.towerqs.common.type.scene.service;

import cn.com.boco.dss.subject.towerqs.common.type.scene.domain.TypeScene;
import cn.com.boco.dss.subject.towerqs.common.type.scene.repository.TypeSceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyj
 * @Title: TypeSceneService
 */
@Service
public class TypeSceneService {
    @Autowired
    private TypeSceneRepository typeSceneRepository;

    public List<TypeScene> findAll() {
        return typeSceneRepository.findAll();
    }
}
