package cn.com.boco.dss.subject.towerqs.risk.web;


import cn.com.boco.dss.common.data.DataColumn;
import cn.com.boco.dss.common.data.DataRow;
import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.util.StringUtil;


import cn.com.boco.dss.config.user.service.UserInfoService;
import cn.com.boco.dss.config.xml.ItemInfo;

import cn.com.boco.dss.data.DbHelper;
import cn.com.boco.dss.database.SqlQuery;
import cn.com.boco.dss.framework.SysSettings;

import cn.com.boco.dss.framework.security.configuration.JwtUtil;
import cn.com.boco.dss.framework.security.domain.TokenUser;
import cn.com.boco.dss.framework.security.web.TokenUserUtils;
import cn.com.boco.dss.subject.common.helper.*;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.webcore.data.commondata.CommonData;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Types;
import java.util.*;

@Api(value = "铁塔隐患记录表", description = "铁塔隐患记录表")
@Controller
public class TowerRiskController {
    Logger log = LoggerFactory.getLogger(TowerRiskController.class);

    @Value("${dss.tower.img.save-path}")
    private String savePath;
    @Value("${dss.tower.img.read-path}")
    private String readPath;

    @Autowired
    private TowerRiskService trs;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SqlQuery sqlQuery;

    @RequestMapping(value = "dss/TowerService/Risks/findAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getRisksDataByCondition(HttpServletRequest request) {

        JsonData jd = new JsonData();
        List<TowerRisk> risk = trs.findAll();
        TowerRisk r = new TowerRisk();
        r = risk.get(0);

        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("222", Arrays.asList("2", "222"));
        r.setImageNameListMap(map);
        jd.setData(r);

        return jd;
    }

    @RequestMapping(value = "dss/TowerService/Risks/getRisksDataByCondition", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getRisksDataByCondition(@RequestParam(value = "params") String params, HttpServletRequest request) {
        ConditionObj conditionObj = new Gson().fromJson(params, ConditionObj.class);
        JsonData jd = new JsonData();


        CommonData result = queryData(conditionObj, request);
        jd.setData(result);

        return jd;
    }


    private String getZoneId(HttpServletRequest request) {
        TokenUserUtils tokenUserUtils = new TokenUserUtils();
        TokenUser tokenUser = tokenUserUtils.findUserByToken(request);
        String zoneId = tokenUser.getZoneId();
        return zoneId;
    }

    private CommonData queryData(ConditionObj conditionObj, HttpServletRequest request) {
        CommonData cd = new CommonData();
        List<Object> queryList = new ArrayList<Object>();
        List<Integer> queryTypes = new ArrayList<Integer>();

        List<Object> queryListCheck = new ArrayList<Object>();
        List<Integer> queryTypesCheck = new ArrayList<Integer>();

        StringBuilder sqlDetection = new StringBuilder();
        StringBuilder sqlCheck = new StringBuilder();
        ItemInfo itemInfo = ToolUtil.findItemInfoByXml("riskTableData-detection", "config/risk.xml");
        sqlDetection.append(itemInfo.getValue());
        sqlCheck.append(ToolUtil.findItemInfoByXml("riskTableData-check", "config/risk.xml").getValue());
        String zoneId = getZoneId(request);
        System.out.println(zoneId);
        if (!StringUtil.isEqual(zoneId, "-1") && !StringUtil.isNullOrEmpty(zoneId) && !StringUtil.isEqual(zoneId, "280")) {
            sqlDetection.append(" AND t2.AreaID = ? ");
            sqlCheck.append(" AND t2.AreaID = ? ");

            String areaID = zoneId;
            queryList.add(areaID);
            queryTypes.add(Types.VARCHAR);

            queryListCheck.add(areaID);
            queryTypesCheck.add(Types.VARCHAR);
        }


        for (ParamObj po : conditionObj.getParam1()) {
            if (po.getVals().size() > 0) {
                if (StringUtil.isEqual(po.getKeyType(), "date")) {

                    sqlDetection.append(" AND (");
                    sqlCheck.append(" AND (");
                    for (String str : po.getVals()) {
                        if (str.contains(":")) {
                            sqlDetection.append(" DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') >= DATE_FORMAT(?,'%Y-%m-%d')");
                            sqlCheck.append(" DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') >= DATE_FORMAT(?,'%Y-%m-%d')");


                            queryList.add(str.split(":")[0]);
                            queryTypes.add(Types.VARCHAR);

                            queryListCheck.add(str.split(":")[0]);
                            queryTypesCheck.add(Types.VARCHAR);

                            sqlDetection.append(" AND DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') <= DATE_FORMAT(?,'%Y-%m-%d') OR ");
                            sqlCheck.append(" AND DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') <= DATE_FORMAT(?,'%Y-%m-%d') OR ");

                            queryList.add(str.split(":")[1]);
                            queryTypes.add(Types.VARCHAR);

                            queryListCheck.add(str.split(":")[1]);
                            queryTypesCheck.add(Types.VARCHAR);

                        } else {
                            sqlDetection.append(" DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') = DATE_FORMAT(? ,'%Y-%m-%d') OR ");
                            sqlCheck.append(" DATE_FORMAT(t1." + po.getKey() + ", '%Y-%m-%d') = DATE_FORMAT(? ,'%Y-%m-%d') OR ");


                            queryList.add(str);
                            queryTypes.add(Types.VARCHAR);

                            queryListCheck.add(str);
                            queryTypesCheck.add(Types.VARCHAR);
                        }
                    }
                    sqlDetection.delete(sqlDetection.length() - 3, sqlDetection.length());
                    sqlDetection.append(")");

                    sqlCheck.delete(sqlCheck.length() - 3, sqlCheck.length());
                    sqlCheck.append(")");
                } else if (StringUtil.isEqual(po.getKeyType(), "int")) {

                    sqlDetection.append(" AND (");
                    sqlCheck.append(" AND (");
                    for (String str : po.getVals()) {
                        sqlDetection.append(" t1." + po.getKey() + "= ? OR ");
                        sqlCheck.append(" t1." + po.getKey() + "= ? OR ");
                        queryList.add(Integer.parseInt(str));
                        queryTypes.add(Types.INTEGER);

                        queryListCheck.add(Integer.parseInt(str));
                        queryTypesCheck.add(Types.INTEGER);
                    }
                    sqlDetection.delete(sqlDetection.length() - 3, sqlDetection.length());
                    sqlDetection.append(")");

                    sqlCheck.delete(sqlCheck.length() - 3, sqlCheck.length());
                    sqlCheck.append(")");

                } else if (StringUtil.isEqual(po.getKeyType(), "double")) {
                    sqlDetection.append(" AND (");

                    sqlCheck.append(" AND (");
                    for (String str : po.getVals()) {
                        sqlDetection.append(" t1." + po.getKey() + "= ? OR ");
                        sqlCheck.append(" t1." + po.getKey() + "= ? OR ");
                        queryList.add(Double.parseDouble(str));
                        queryTypes.add(Types.DOUBLE);

                        queryListCheck.add(Double.parseDouble(str));
                        queryTypesCheck.add(Types.DOUBLE);
                    }
                    sqlDetection.delete(sqlDetection.length() - 3, sqlDetection.length());
                    sqlDetection.append(")");

                    sqlCheck.delete(sqlCheck.length() - 3, sqlCheck.length());
                    sqlCheck.append(")");
                } else if (StringUtil.isEqual(po.getKeyType(), "varchar")) {
                    sqlDetection.append(" AND (");
                    sqlCheck.append(" AND (");
                    for (String str : po.getVals()) {
                        sqlDetection.append(" t1." + po.getKey() + "= ? OR ");
                        queryList.add(str);
                        queryTypes.add(Types.VARCHAR);

                        sqlCheck.append(" t1." + po.getKey() + "= ? OR ");
                        queryListCheck.add(str);
                        queryTypesCheck.add(Types.VARCHAR);
                    }
                    sqlDetection.delete(sqlDetection.length() - 3, sqlDetection.length());
                    sqlDetection.append(")");

                    sqlCheck.delete(sqlCheck.length() - 3, sqlCheck.length());
                    sqlCheck.append(")");
                }
            }
        }
        if (!StringUtil.isNullOrEmpty(conditionObj.getSortField())) {
            sqlDetection.append(" ORDER BY ");
            sqlDetection.append(conditionObj.getSortField());
            sqlDetection.append(" " + conditionObj.getSortType());

            sqlCheck.append(" ORDER BY ");
            sqlCheck.append(conditionObj.getSortField());
            sqlCheck.append(" " + conditionObj.getSortType());
        }
        queryList.addAll(queryListCheck);
        queryTypes.addAll(queryTypesCheck);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));

        try {
            int pIndex = 0;
            int pSize = 0;
            if (conditionObj.getPageIndex() != 0 && conditionObj.getPageSize() != 0) {
                pIndex = conditionObj.getPageIndex();
                pSize = conditionObj.getPageSize();
            }

            cd = geCommonDatatBySql(sqlDetection.toString() + " UNION ALL " + sqlCheck, queryList.toArray(), types, pIndex, pSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cd;
    }

    private CommonData geCommonDatatBySql(String sql, Object[] queryParamsArr, int[] queryTypeArr, int pageIndex, int pageSize) throws Exception {
        System.out.println(sql);
        String sqlCount = "SELECT COUNT(*) FROM (" + sql + ") TAB ";
        long count = sqlQuery.getTableCountBySql(sqlCount, queryParamsArr, queryTypeArr);
        DataTable dt = sqlQuery.getDataTableBySql(sql, queryParamsArr, queryTypeArr, pageIndex, pageSize, "riskTableData-detection", "config/risk.xml");

        System.out.println(sql);
        setImgPath(dt);
        CommonData commonData = CommonData.fromDataTable(dt);
        commonData.setPageIndex(pageIndex);
        commonData.setPageSize(pageSize);
        commonData.setTotalCount(count);
        return commonData;
    }

    public DataTable setImgPath(DataTable dt) {
        try {
            for (DataRow dr : dt.getRows()) {
                String path = "";
                String workOrderType = String.valueOf(dr.getValue("workOrderType"));
                FilePath filePath = new FilePath();
                filePath.setProvinceName(String.valueOf(dr.getValue("provinceName")));
                filePath.setAreaName(String.valueOf(dr.getValue("areaName")));
                filePath.setLocation(String.valueOf(dr.getValue("location")));
                filePath.setTowerName(String.valueOf(dr.getValue("towerName")));
                filePath.setAddRessCode(String.valueOf(dr.getValue("addressCode")));
                filePath.setDateTime(String.valueOf(dr.getValue("recordDate")));
                filePath.setConstractCode(String.valueOf(dr.getValue("constractCode")));
                if (StringUtil.isEqual(workOrderType, "check")) {
                    path = FilePathUtil.getFilePath(filePath, FileTypeEnum.check_riskImg_before);
                } else if (StringUtil.isEqual(workOrderType, "detection")) {
                    path = FilePathUtil.getFilePath(filePath, FileTypeEnum.detection_riskImg);
                }
                String path1 = savePath + path;
                String name = String.valueOf(dr.getValue("riskName"));
                List<File> files = ToolUtil.getFileSort(path1, name);
                String rPath = readPath + path;
                if (files.size() > 0) {
                    rPath += files.get(0).getName();

                }
                dr.setValue(0, rPath);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return dt;
    }

//    public DataTable setImgPath(DataTable dt) {
//        for (DataRow dr : dt.getRows()) {
//            StringBuilder sb = new StringBuilder();
//            //sb.append(readPath).append("/");
//            sb.append("通信塔资料").append("/").append("中华人民共和国").append("/");
//            sb.append(dr.getValue("ProvinceName")).append("/");
//            sb.append(dr.getValue("areaName")).append("/");
//            sb.append(dr.getValue("location")).append("/");
//            sb.append(dr.getValue("towerName")).append("+");
//            sb.append(dr.getValue("addressCode")).append("/");
//            sb.append(dr.getValue("towerName")).append("+");
//            sb.append(dr.getValue("addressCode")).append("+");
//            sb.append("运营维护").append("/");
//            String workOrderType = String.valueOf(dr.getValue("workOrderType"));
//            if (StringUtil.isEqual(workOrderType, "check")) {
//                sb.append("代维检修").append("/").append("代维检修+");
//                sb.append(String.valueOf(dr.getValue("recordDate")).replace("-", ""));
//                sb.append("/");
//                sb.append(upLoadFileType.check_riskImg_before);
//                String name = String.valueOf(dr.getValue("riskName"));
//                String path = savePath + sb.toString();
//                List<File> files = ToolUtil.getFileSort(path, name);
//                if (files.size() > 0) {
//                    sb.append(files.get(0).getName());
//                }
//            } else if (StringUtil.isEqual(workOrderType, "detection")) {
//
//                sb.append("安全评估").append("/").append("安全评估+");
//                sb.append(String.valueOf(dr.getValue("constractCode")));
//                sb.append("/");
//                sb.append(upLoadFileType.detection_riskImg);
//                String name = String.valueOf(dr.getValue("riskName"));
//                String path = savePath + sb.toString();
//                List<File> files = ToolUtil.getFileSort(path, name);
//                if (files.size() > 0) {
//                    sb.append(files.get(0).getName());
//                }
//            }
//
//
//            try {
//                dr.setValue(0, URLEncoder.encode(readPath + sb.toString(), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return dt;
//    }

    @ApiOperation(value = "修改RiskStatus", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "towerID", value = "towerID", required = true, dataType = "String", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "dss/TowerService/risk/updateStatusByTowerIds", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateStatusByTowerIds(@RequestParam(value = "towerID") String towerID) {
        JsonData jd = new JsonData();
        List<String> ids = new Gson().fromJson(towerID, new TypeToken<List<String>>() {
        }.getType());
        trs.updateStatusByTowerIds(ids);
        jd.setData("SUCCESS");
        return jd;
    }

    @ApiOperation(value = "获取隐患总数", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "towerID", value = "towerID", required = true, dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "workOrderType", value = "workOrderType", required = true, dataType = "String", paramType = "query", defaultValue = "")

    })
    @RequestMapping(value = "dss/TowerService/risk/findCount", method = RequestMethod.POST)
    @ResponseBody
    public JsonData findCount(@RequestParam(value = "towerID") String towerID, @RequestParam(value = "workOrderType") String workOrderType) {
        JsonData jd = new JsonData();
        if (StringUtil.isEqual(workOrderType, "check")) {
            long solved = trs.findCheckCount(towerID, 1);
            long notSolved = trs.findCheckCount(towerID, 0);
            String result = "{\"solved\":\"" + solved + "\",\"notSolved\":\"" + notSolved + "\"}";
            jd.setData(JSON.parse(result));
        } else if (StringUtil.isEqual(workOrderType, "detection")) {
            long count = trs.findDetectionCount(towerID);
            String result = "{\"count\":\"" + count + "\" }";
            jd.setData(JSON.parse(result));
        } else {
            long count = trs.findRepairCount(towerID);
            String result = "{\"count\":\"" + count + "\" }";
            jd.setData(JSON.parse(result));
        }

        return jd;
    }

    @ApiOperation(value = "获取隐患信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "towerID", value = "towerID", required = true, dataType = "String", paramType = "query", defaultValue = "")

    })
    @RequestMapping(value = "dss/TowerService/risk/findRiskInfo", method = RequestMethod.POST)
    @ResponseBody
    public JsonData findRiskInfo(@RequestParam(value = "towerID") String towerID,
                                 @RequestParam(value = "pageIndex") String pageIndex,
                                 @RequestParam(value = "pageSize") String pageSize) throws Exception {
        JsonData jd = new JsonData();
        List<Object> queryList = new ArrayList<Object>();
        List<Integer> queryTypes = new ArrayList<Integer>();
        String sql = ToolUtil.findItemInfoByXml("riskInfo", "config/risk.xml").getValue();
        queryList.add(towerID);
        queryTypes.add(Types.VARCHAR);
        int[] types = ArrayUtils.toPrimitive(queryTypes.toArray(new Integer[queryTypes.size()]));

        int pIndex = 0;
        int pSize = 0;
        if (!StringUtil.isNullOrEmpty(pageIndex)) {
            pIndex = Integer.parseInt(pageIndex);
        }
        if (!StringUtil.isNullOrEmpty(pageSize)) {
            pSize = Integer.parseInt(pageSize);
        }
        DataTable dt = sqlQuery.getDataTableBySql(sql, queryList.toArray(), types, pIndex, pSize, "riskInfo", "config/risk.xml");

        String sqlCount = "SELECT COUNT(*) FROM (" + sql + ") TAB ";
        long count = sqlQuery.getTableCountBySql(sqlCount, queryList.toArray(), types);

        CommonData cd = CommonData.fromDataTable(dt);
        cd.setTotalCount(count);
        cd.setPageIndex(Integer.parseInt(pageIndex));
        cd.setPageSize(Integer.parseInt(pageSize));
        jd.setData(cd);
        return jd;
    }

}

class ConditionObj {
    private int pageIndex;
    private int pageSize;
    private String sortType;
    private String sortField;
    private List<ParamObj> param1;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public List<ParamObj> getParam1() {
        return param1;
    }

    public void setParam1(List<ParamObj> param1) {
        this.param1 = param1;
    }


}

class ParamObj {
    private String key;
    private List<String> vals;
    private String keyType;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getVals() {
        return this.vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    public String getKeyType() {
        return this.keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}


