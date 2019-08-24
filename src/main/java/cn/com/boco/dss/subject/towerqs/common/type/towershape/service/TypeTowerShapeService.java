package cn.com.boco.dss.subject.towerqs.common.type.towershape.service;

import cn.com.boco.dss.subject.towerqs.common.type.towershape.domain.TypeTowerShape;
import cn.com.boco.dss.subject.towerqs.common.type.towershape.repository.TypeTowerShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyj
 * @Title: TypeTowerShapeService
 * @Description: TODO
 */
@Service
public class TypeTowerShapeService {

    @Autowired
    private TypeTowerShapeRepository typeTowerShapeRepository;

    public List<TypeTowerShape> findAll() {
        return typeTowerShapeRepository.findAll();
    }
}
