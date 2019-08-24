package cn.com.boco.dss.subject.towerqs.tower.service;

import cn.com.boco.dss.subject.towerqs.tower.domain.DbChartData;
import cn.com.boco.dss.subject.towerqs.tower.repository.TowerAnlysisForMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-22 16:26
 **/
@Service
public class TowerAnlysisService {
    @Autowired
    private TowerAnlysisForMysql towerAnlysisForMysql;

    public List<DbChartData> getSafeChartData(String towerID) {
        return towerAnlysisForMysql.getSafeChartData(towerID);
    }

    public List<DbChartData> getVerticalityChartData(String towerID) {
        return towerAnlysisForMysql.getVerticalityChartData(towerID);
    }

    public List<DbChartData> getGroundResistance(String towerID) {
        return towerAnlysisForMysql.getGroundResistance(towerID);
    }
}
