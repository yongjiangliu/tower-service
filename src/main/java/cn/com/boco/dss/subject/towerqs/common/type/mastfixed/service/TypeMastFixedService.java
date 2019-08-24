package cn.com.boco.dss.subject.towerqs.common.type.mastfixed.service;

import cn.com.boco.dss.subject.towerqs.common.type.mastfixed.domain.TypeMastFixed;
import cn.com.boco.dss.subject.towerqs.common.type.mastfixed.repository.TypeMastFixedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 15:35
 **/
@Service
public class TypeMastFixedService {

    @Autowired
    private TypeMastFixedRepository typeMastFixedRepository;

    public List<TypeMastFixed> findAll() {
        return typeMastFixedRepository.findAll();
    }
}
