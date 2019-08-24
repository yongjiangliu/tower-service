package cn.com.boco.dss.subject.towerqs.common.type.linefixed.service;

import cn.com.boco.dss.subject.towerqs.common.type.linefixed.domain.TypeLineFixed;
import cn.com.boco.dss.subject.towerqs.common.type.linefixed.repository.TypeLineFixedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 15:30
 **/
@Service
public class TypeLineFixedService {

    @Autowired
    private TypeLineFixedRepository typeLineFixedRepository;

    public List<TypeLineFixed> findAll() {
        return typeLineFixedRepository.findAll();
    }
}
