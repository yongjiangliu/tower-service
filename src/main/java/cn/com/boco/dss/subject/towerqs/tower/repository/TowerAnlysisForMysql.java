package cn.com.boco.dss.subject.towerqs.tower.repository;

import cn.com.boco.dss.database.DataSourceTowerConfig;
import cn.com.boco.dss.subject.towerqs.tower.domain.DbChartData;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-22 19:38
 **/
@Repository
public class TowerAnlysisForMysql {

    @Resource(name = DataSourceTowerConfig.C_JDBCTEMPLATE_NAME)
    private JdbcTemplate jdbcTemplate;

    public List<DbChartData> getSafeChartData(String towerID) {
        StringBuilder sb = new StringBuilder();
        List<String> queryList = new ArrayList<String>();
        List<Integer> queryTypes = new ArrayList<Integer>();
        sb.append(" SELECT ");
        sb.append(" 	year(m.RecordDate) k, ");
        sb.append(" 	100-SUM(n.SafePoint)  v ");
        sb.append(" FROM ");
        sb.append(" 	dw_tower_risk m ");
        sb.append(" LEFT JOIN dim_risk n ON m.RiskID = n.RiskID ");
        sb.append(" WHERE ");
        sb.append(" 	m.TowerID = ? ");
        sb.append(" group by m.TowerID,year(m.RecordDate) ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        return jdbcTemplate.query(sb.toString(), queryList.toArray(), types, new BeanPropertyRowMapper<DbChartData>(DbChartData.class));
    }

    public List<DbChartData> getVerticalityChartData(String towerID) {
        StringBuilder sb = new StringBuilder();
        List<String> queryList = new ArrayList<String>();
        List<Integer> queryTypes = new ArrayList<Integer>();
        sb.append(" SELECT  ");
        sb.append(" 	YEAR (m.DetectionDate) k, ");
        sb.append(" 	m.Verticality  v ");
        sb.append(" FROM ");
        sb.append(" 	dw_tower_detection m ");
        sb.append(" WHERE ");
        sb.append(" 	m.TowerID = ? ");
        sb.append(" ORDER BY m.DetectionDate desc ");
        sb.append(" limit 0,1 ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        return jdbcTemplate.query(sb.toString(), queryList.toArray(), types, new BeanPropertyRowMapper<DbChartData>(DbChartData.class));
    }

    public List<DbChartData> getGroundResistance(String towerID) {
        StringBuilder sb = new StringBuilder();
        List<String> queryList = new ArrayList<String>();
        List<Integer> queryTypes = new ArrayList<Integer>();
        sb.append(" SELECT  ");
        sb.append(" 	YEAR (m.DetectionDate) k, ");
        sb.append(" 	m.GroundResistance v ");
        sb.append(" FROM ");
        sb.append(" 	dw_tower_detection m ");
        sb.append(" WHERE ");
        sb.append(" 	m.TowerID = ? ");
        sb.append(" ORDER BY m.DetectionDate desc ");
        sb.append(" limit 0,1 ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        return jdbcTemplate.query(sb.toString(), queryList.toArray(), types, new BeanPropertyRowMapper<DbChartData>(DbChartData.class));
    }
}
