package cn.com.boco.dss.subject.towerqs.common.geo.area.service;

import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-15 20:42
 **/
@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public Area findById(String areaID) {
        return areaRepository.findById(areaID).orElse(null);
    }

    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    public Area findAreaByAreaName(String areaName) {
        return areaRepository.findAreaByAreaName(areaName);
    }

}
