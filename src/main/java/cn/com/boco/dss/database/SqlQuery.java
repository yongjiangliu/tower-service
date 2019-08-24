package cn.com.boco.dss.database;

import cn.com.boco.dss.common.data.DataColumn;
import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.config.xml.ItemInfo;
import cn.com.boco.dss.data.ConnectSettings;
import cn.com.boco.dss.data.DbHelper;
import cn.com.boco.dss.subject.common.helper.ToolUtil;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @program: TowerService
 * @description: 根据sql获取表格的数据:commonData，数据总条数，数据库连接串
 * @author: lyj
 * @create: 2019-08-16 09:34
 **/
@Component
public class SqlQuery {

    @Value("${spring.db.towerqs.jdbc-url}")
    private String url;
    @Value("${spring.db.towerqs.driverClassName}")
    private String driverClassName;
    @Value("${spring.db.towerqs.username}")
    private String userName;
    @Value("${spring.db.towerqs.password}")
    private String password;

    public ConnectSettings getConnectSettings() {
        ConnectSettings cs = new ConnectSettings();
        cs.setUrl(url);
        cs.setDriverClassName(driverClassName);
        cs.setUserName(userName);
        cs.setPassword(password);
        return cs;
    }

    public CommonData getCommonDataBySql(String sql) throws Exception {
        DataTable dt = DbHelper.getDataBySql(sql, getConnectSettings());
        CommonData commonData = CommonData.fromDataTable(dt);
        return commonData;
    }

    public long getTableCountBySql(String sql) throws Exception {
        DataTable dt = DbHelper.getDataBySql(sql, getConnectSettings());
        return Long.parseLong(String.valueOf(dt.getValue(0, 0)));
    }

    /**
     * @param sql:分页sql
     * @param queryParamsArr
     * @param queryTypeArr
     * @return
     * @throws Exception
     */
    public CommonData geCommonDatatBySql(String sql, Object[] queryParamsArr, int[] queryTypeArr) throws Exception {
        DataTable dt = DbHelper.getDataBySql(sql, queryParamsArr, queryTypeArr, getConnectSettings());
        CommonData commonData = CommonData.fromDataTable(dt);
        return commonData;
    }

    /**
     * @param sql：包含count的sql
     * @param queryParamsArr
     * @param queryTypeArr
     * @return
     * @throws Exception
     */
    public long getTableCountBySql(String sql, Object[] queryParamsArr, int[] queryTypeArr) throws Exception {
        DataTable dt = DbHelper.getDataBySql(sql, queryParamsArr, queryTypeArr, getConnectSettings());
        return Long.parseLong(String.valueOf(dt.getValue(0, 0)));
    }

    /**
     * @param sql
     * @param queryParamsArr
     * @param queryTypeArr
     * @param pageIndex
     * @param pageSize
     * @param xmlName
     * @param xmlPath
     * @return
     */
    public DataTable getDataTableBySql(String sql, Object[] queryParamsArr, int[] queryTypeArr,
                                       int pageIndex, int pageSize, String xmlName, String xmlPath) {
        String sqlLimit = sql;
        if (pageIndex != 0 && pageSize != 0) {
            sqlLimit = "SELECT * FROM (" + sql + ") TAB LIMIT " + (pageIndex - 1) * pageSize + "," + pageSize;

        }
        DataTable dt = null;
        try {
            dt = DbHelper.getDataBySql(sqlLimit, queryParamsArr, queryTypeArr, getConnectSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemInfo itemInfo = ToolUtil.findItemInfoByXml(xmlName, xmlPath);
        String colName = itemInfo.getProperty1();
        List<String> arr = Arrays.asList(colName.split(","));
        for (DataColumn col : dt.getColumns()) {
            for (String str : arr) {
                if (StringUtil.isEqual(col.getColumnName(), str.split(":")[0])) {
                    col.setCaptionName(str.split(":")[1]);
                    break;
                }
            }
        }
        return dt;
    }

}
