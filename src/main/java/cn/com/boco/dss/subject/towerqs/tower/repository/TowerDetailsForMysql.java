package cn.com.boco.dss.subject.towerqs.tower.repository;

import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.data.sql.SqlHelper;
import cn.com.boco.dss.database.SqlQuery;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-21 19:18
 **/
@Repository
public class TowerDetailsForMysql {
    @Autowired
    private SqlQuery sqlQuery;

    private static final Logger log = LoggerFactory.getLogger(TowerDetailsForMysql.class);

    private String towerCheckSql(String towerID, List<Object> queryList, List<Integer> queryTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" 	m.ResourceCode 资源编号,");
        sb.append(" 	m.AddressCode 站址编号, ");
        sb.append(" 	m.ConstractCode 合同号,");
        sb.append(" 	m.CheckerName 巡检单位, ");
        sb.append(" 	m.CheckerPhone 联系电话,");
        sb.append(" 	m.CheckDate 巡检日期, ");
        sb.append(" 	m.TowerHeight 塔高, ");
        sb.append(" 	m.AntennaCount 天线数量,");
        sb.append(" 	m.AntennaCount5G 5G天线数量,");
        sb.append(" 	m.AntennaPloEmptyCount 空余支架数量 ");
        sb.append(" FROM");
        sb.append(" 	dw_tower_check m");
        sb.append(" where m.TowerID = ? ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        return sb.toString();
    }

    private String towerDetectionSql(String towerID, List<Object> queryList, List<Integer> queryTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" 	m.ResourceID 资源编码,");
        sb.append(" 	m.AddressCode 站址编码, ");
        sb.append(" 	m.ConstractCode 合同号, ");
        sb.append(" 	m.DetectionCompany 检测单位,");
        sb.append(" 	m.ContactName 联系人, ");
        sb.append(" 	m.ContactPhone 联系电话,");
        sb.append(" 	m.DetectionDate 检测日期, ");
        sb.append(" 	m.Address 检测地点, ");
        sb.append(" 	n.TypeName 工作场景,");
        sb.append(" 	p.TypeName 塔型,");
        sb.append(" 	m.TowerHeight 塔高, ");
        sb.append(" 	m.AntennaCount 天线数量,");
        sb.append(" 	m.AntennaCount5G 5G天线数量,");
        sb.append(" 	m.AntennaPloEmptyCount 空余支架数量 ");
        sb.append(" FROM");
        sb.append(" 	dw_tower_detection m");
        sb.append(" LEFT JOIN dim_type_scene n ON m.SceneType = n.TypeID");
        sb.append(" LEFT JOIN dim_type_tower_shape p ON m.SceneType = p.TypeID");
        sb.append(" where m.TowerID = ? ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        return sb.toString();
    }


    private String towerRepairSql(String towerID, List<Object> queryList, List<Integer> queryTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" 	m.ResourceCode 资源编码,");
        sb.append(" 	m.AddressCode 站址编码, ");
        sb.append(" 	m.ConstractCode 合同号, ");
        sb.append(" 	m.CompanName 施工单位,");
        sb.append(" 	m.IsMaintenanceCompany 是否是巡检单位,");
        sb.append(" 	m.ContactName 联系人, ");
        sb.append(" 	m.ContactPhone 联系电话,");
        sb.append(" 	m.ConstractionDate 施工日期,");
        sb.append(" 	m. STATUS 状态, ");
        sb.append(" 	m.UpdateDate 数据更新日期 ");
        sb.append(" FROM");
        sb.append(" 	dw_tower_repair m ");
        sb.append(" where m.TowerID = ? ");
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        return sb.toString();
    }


    public long queryCheckCount(String towerID) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerCheckSql(towerID, queryList, queryTypes);
        sql = SqlHelper.GetRecordCountSql(sql);
        log.info("queryCheckCountSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        long count = sqlQuery.getTableCountBySql(sql, queryList.toArray(), types);
        return count;
    }

    public long queryDetectionCount(String towerID) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerDetectionSql(towerID, queryList, queryTypes);
        sql = SqlHelper.GetRecordCountSql(sql);
        log.info("queryDetectionCountSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        long count = sqlQuery.getTableCountBySql(sql, queryList.toArray(), types);
        return count;
    }

    public long queryRepairCount(String towerID) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerRepairSql(towerID, queryList, queryTypes);
        sql = SqlHelper.GetRecordCountSql(sql);
        log.info("queryRepairCountSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        long count = sqlQuery.getTableCountBySql(sql, queryList.toArray(), types);
        return count;
    }

    public CommonData queryCheckData(String towerID, int pageIndex, int pageSize, int sortColIndex, SortType sortType) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerCheckSql(towerID, queryList, queryTypes);
        sql = SqlHelper.PageSql(sql, sqlQuery.getConnectSettings(), pageIndex, pageSize, sortColIndex, SortType.DESC.equals(sortType));
        log.info("queryCheckDataSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        CommonData commonData = sqlQuery.geCommonDatatBySql(sql, queryList.toArray(), types);
        return commonData;
    }

    public CommonData queryRepairData(String towerID, int pageIndex, int pageSize, int sortColIndex, SortType sortType) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerRepairSql(towerID, queryList, queryTypes);
        sql = SqlHelper.PageSql(sql, sqlQuery.getConnectSettings(), pageIndex, pageSize, sortColIndex, SortType.DESC.equals(sortType));
        log.info("queryRepairDataSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        CommonData commonData = sqlQuery.geCommonDatatBySql(sql, queryList.toArray(), types);
        return commonData;
    }

    public CommonData queryDetectionData(String towerID, int pageIndex, int pageSize, int sortColIndex, SortType sortType) throws Exception {
        List<Object> queryList = new ArrayList<>();
        List<Integer> queryTypes = new ArrayList<>();
        String sql = towerDetectionSql(towerID, queryList, queryTypes);
        sql = SqlHelper.PageSql(sql, sqlQuery.getConnectSettings(), pageIndex, pageSize, sortColIndex, SortType.DESC.equals(sortType));
        log.info("queryDetectionDataSql: " + sql);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));
        CommonData commonData = sqlQuery.geCommonDatatBySql(sql, queryList.toArray(), types);
        return commonData;
    }
}
