package cn.com.boco.dss.subject.towerqs.common.type.baseposition.service;

import cn.com.boco.dss.subject.towerqs.common.type.baseposition.domain.TypeBasePosition;
import cn.com.boco.dss.subject.towerqs.common.type.baseposition.repository.TypeBasePositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 11:20
 **/
@Service
public class TypeBasePositionService {

    @Autowired
    private TypeBasePositionRepository typeBasePositionRepository;

    public List<TypeBasePosition> findAll() {
        return typeBasePositionRepository.findAll();
    }
}
