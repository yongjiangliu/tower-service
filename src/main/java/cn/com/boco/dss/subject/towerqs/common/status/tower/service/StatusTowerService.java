package cn.com.boco.dss.subject.towerqs.common.status.tower.service;

import cn.com.boco.dss.subject.towerqs.common.status.tower.domain.StatusTower;
import cn.com.boco.dss.subject.towerqs.common.status.tower.repository.StatusTowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-21 09:43
 **/
@Service
public class StatusTowerService {

    @Autowired
    private StatusTowerRepository statusTowerRepository;


    public List<StatusTower> findAll() {
        return statusTowerRepository.findAll();
    }
}
