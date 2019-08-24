package cn.com.boco.dss.subject.towerqs.index.service;

import cn.com.boco.dss.common.data.DataRow;
import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.common.helper.DssHelper;
import cn.com.boco.dss.subject.towerqs.index.pojo.CheckUserZoneUtil;
import cn.com.boco.dss.subject.towerqs.index.pojo.UserZone;
import cn.com.boco.dss.subject.towerqs.index.respository.TowerInfoRespository;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2019/08/16 16:20
 * Version 1.0.0
 * Description 铁塔质量管理平台首页
 */
@Service
public class TowerInfoService {

    @Autowired
    private TowerInfoRespository towerInfoRespository;

    @Autowired
    private CheckUserZoneUtil checkUserZoneUtil;


    /**
     * 获取AB两级隐患每个级别对应的隐患数量
     *
     * @return
     */
    public JsonData findRiskNumGroupByRiskLevel(HttpServletRequest request) {
        String[] arrColNames = {"A类隐患", "B类隐患"};
        String msgIfException = "获取AB级患数量异常，请联系管理员。";
        return getData(arrColNames, "risklevel", msgIfException, request);

    }


    /**
     * 获取每个隐患部位的隐患数量
     *
     * @return
     */
    public JsonData findRiskNumGroupByRiskPosition(HttpServletRequest request) {
        String[] arrColNames = {"隐患部位", "隐患数量"};
        String msgIfException = "获取隐患部位数量异常，请联系管理员。";
        return getData(arrColNames, "riskposition", msgIfException, request);

    }


    /**
     * 首页地图
     *
     * @return
     */
    public JsonData findMapData(HttpServletRequest request) {
        String[] arrColNames = {"铁塔名称", "经度", "维度", "隐患级别", "巡检发现隐患数量", "巡检发现A类隐患数量", "检测发现隐患数量", "检测发现A类隐患数量"};
        String msgIfException = "获取首页地图数据异常，请联系管理员。";
        return getData(arrColNames, "map", msgIfException, request);

    }

    /**
     * 获取省份/地市下的AB类隐患数量
     *
     * @param request
     * @return
     */
    public JsonData findTowerNumOfLocation(HttpServletRequest request) {
        String[] arrColNames = {"区域", "A类隐患数量", "B类隐患数量"};
        String msgIfException = "获取省份/地市下的AB类隐患数量，请联系管理员。";
        return getData(arrColNames, "location", msgIfException, request);
    }

    /**
     * 获取每个处理状态下的铁塔数量
     *
     * @return
     */
    public JsonData findTowerNumGroupByStatus(HttpServletRequest request) {
        String[] arrColNames = {"处理状态", "数量"};
        String msgIfException = "获取处理状态下的铁塔数量异常，请联系管理员。";
        return getData(arrColNames, "status", msgIfException, request);
    }


    /**
     * 公共方法
     *
     * @param arrColNames    列表头
     * @param bussType       业务类型
     * @param msgIfException 如果发生错误，错误信息checkUserZoneUtil
     * @param request        request对象
     * @return
     */
    private JsonData getData(String[] arrColNames, String bussType, String msgIfException, HttpServletRequest request) {
        JsonData jd = new JsonData();
        try {
            UserZone userZone = checkUserZoneUtil.checkUserZone(request);
            int userStatus = userZone.getUserStatus();
            CommonData commonData = null;
            //省端用户；说明：userStatus=-1 本来是全部数据，为了保证首页有值，这里默认为省端用户即可；
            if (userStatus == -1 || userStatus == 1) {
                Integer provinceId = userZone.getZone().getZoneId();
                List<Object> rowsProvince = new ArrayList<>();
                switch (bussType) {
                    case "risklevel":
                        rowsProvince = towerInfoRespository.findRiskNumGroupByRiskLevelOfArea(provinceId);
                        break;
                    case "riskposition":
                        rowsProvince = towerInfoRespository.findRiskNumGroupByRiskPositionOfArea(provinceId);
                        break;
                    case "map":
                        rowsProvince = towerInfoRespository.findMapDataOfArea(provinceId);
                        break;
                    case "location":
                        rowsProvince = towerInfoRespository.findTowerNumOfArea(provinceId);
                        break;
                    case "status":
                        rowsProvince = towerInfoRespository.findTowerNumGroupByStatusOfArea(provinceId);
                        break;
                }
                commonData = convertToCommonData(arrColNames, rowsProvince);
            }
            //地市用户
            else if (userStatus == 2) {
                Integer parentZoneId = userZone.getZone().getZoneId();
                List<String> cityNames = checkUserZoneUtil.findZoneNamesByParentZoneId(parentZoneId);
                List<Object> rowsCities = new ArrayList<>();
                switch (bussType) {
                    case "risklevel":
                        rowsCities = towerInfoRespository.findRiskNumGroupByRiskLevelOfCity(cityNames);
                        break;
                    case "riskposition":
                        rowsCities = towerInfoRespository.findRiskNumGroupByRiskPositionOfCity(cityNames);
                        break;
                    case "map":
                        rowsCities = towerInfoRespository.findMapDataOfCity(cityNames);
                        break;
                    case "location":
                        rowsCities = towerInfoRespository.findTowerNumOfCity(cityNames);
                        break;
                    case "status":
                        rowsCities = towerInfoRespository.findTowerNumGroupByStatusOfCity(cityNames);
                        break;
                }
                commonData = convertToCommonData(arrColNames, rowsCities);
            }
            //其他用户
            else {
                DataTable dt = DssHelper.getDataTableByColNamesAndTableName(arrColNames, "");
                commonData = CommonData.fromDataTable(dt);
            }
            jd.setData(commonData);
        } catch (Exception e) {
            e.printStackTrace();
            jd.setStatus("-1");
            jd.setData(msgIfException + e.getMessage());
        }
        return jd;
    }


    /**
     * 把List<Object>类型 转为CommonData类型
     *
     * @param arrColNames
     * @param rows
     * @return
     */
    private CommonData convertToCommonData(String[] arrColNames, List<Object> rows) {
        CommonData commonData = null;
        try {
            DataTable dt = DssHelper.getDataTableByColNamesAndTableName(arrColNames, "");
            for (int i = 0; i < rows.size(); i++) {
                Object row = rows.get(i);
                Object[] cells = (Object[]) row;
                DataRow dr = dt.newRow();
                for (int j = 0; j < cells.length; j++) {
                    dr.setValue(j, cells[j]);
                }
                dt.addRow(dr);
            }
            commonData = CommonData.fromDataTable(dt);
        } catch (Exception e) {
            DssHelper.log("铁塔质量管理平台首页功能--异常信息-列信息：" + arrColNames.toString() + "，" + e.getMessage(), this.getClass());
            //报错时 commodata置为空 防止页面出现问问题
            DataTable dtOriginal = DssHelper.getDataTableByColNamesAndTableName(arrColNames, "");
            commonData = CommonData.fromDataTable(dtOriginal);
        }
        return commonData;
    }


}
