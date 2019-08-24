package cn.com.boco.dss.subject.towerqs.tower.repository;

import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.data.ConnectSettings;
import cn.com.boco.dss.data.sql.SqlHelper;
import cn.com.boco.dss.database.SqlQuery;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: TowerService
 * @description: 关于tower的sql相关操作
 * @author: lyj
 * @create: 2019-08-16 10:31
 **/
@Repository
public class TowerForMysql {

    @Autowired
    private SqlQuery sqlQuery;

    private static final Logger log = LoggerFactory.getLogger(TowerForMysql.class);

    private String towerTableSql(String areaID, List<Object> queryList, List<Integer> queryTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" m.TowerID towerID, ");
        sb.append(" CASE m.ResourceCode WHEN 'NULL' THEN	'' ELSE m.ResourceCode END 资源编号,");
        sb.append(" n.ProvinceName 省份, ");
        sb.append(" n.AreaName	地市, ");
        sb.append(" CASE m.Location WHEN 'NULL' THEN	'' ELSE m.Location	END 区县, ");
        sb.append(" p.TypeName	塔型, ");
        sb.append(" CASE m.AddressCode WHEN 'NULL' THEN	'' ELSE m.AddressCode END	站址编码, ");
        sb.append(" CASE m.TowerName WHEN 'NULL' THEN	'' ELSE m.TowerName	END 站名, ");
        sb.append(" CASE m.RiskLevel WHEN 1 THEN '严重' WHEN 2 THEN '一般' ELSE '无' END	隐患级别, ");
        sb.append(" CASE m.Status WHEN 1 THEN '巡检中'  WHEN 2 THEN '检测中' WHEN 1 THEN '整治中' ELSE '无' END	状态 ");
        sb.append(" from dim_ne_tower m ");
        sb.append(" LEFT JOIN dim_geo_area n ");
        sb.append(" on m.AreaID =n.AreaID ");
        sb.append(" LEFT JOIN dim_type_tower_shape p ");
        sb.append(" ON m.TowerShapeType = p.TypeID ");
        if (!StringUtils.isBlank(areaID)) {
            sb.append(" where m.AreaID = ? ");
            queryList.add(areaID);
            queryTypes.add(Types.VARCHAR);
        }
        return sb.toString();
    }

    public CommonData queryData(String areaID, int pageIndex, int pageSize, int sortColIndex, SortType sortType) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerTableSql(areaID, queryList, queryTypes);
        ConnectSettings cs = sqlQuery.getConnectSettings();
        sql = SqlHelper.PageSql(sql, cs, pageIndex, pageSize, sortColIndex, SortType.DESC.equals(sortType));
        log.info("towerTableDataSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        CommonData commonData = sqlQuery.geCommonDatatBySql(sql, queryList.toArray(), types);
        return commonData;
    }

    public long queryCount(String areaID, int pageIndex, int pageSize, int sortColIndex, SortType sortType) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerTableSql(areaID, queryList, queryTypes);
        sql = SqlHelper.GetRecordCountSql(sql);
        log.info("towerTableCountSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        long count = sqlQuery.getTableCountBySql(sql, queryList.toArray(), types);
        return count;
    }

}
