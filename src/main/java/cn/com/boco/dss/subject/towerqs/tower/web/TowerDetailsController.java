package cn.com.boco.dss.subject.towerqs.tower.web;

import cn.com.boco.dss.common.SortDirection;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerDetailsService;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import cn.com.boco.dss.webcore.grid.SmartGridOption;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: TowerService
 * @description: 站点明细页面的相关请求
 * @author: lyj
 * @create: 2019-08-21 11:19
 **/
@Controller
@RequestMapping(value = "dss/TowerService/towerDetails")
public class TowerDetailsController {

    @Autowired
    private TowerDetailsService towerDetailsService;

    @Autowired
    private TowerRiskService towerRiskService;

    @RequestMapping(value = "/pageHead", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getPageHead(HttpServletRequest request) throws IllegalAccessException {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        Object obj = towerDetailsService.getPageHead(params);
        jd.setData(obj);
        return jd;
    }

    @RequestMapping(value = "/pageBody", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getPageBody(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        Object obj = towerDetailsService.getPageBody(params);
        jd.setData(obj);
        return jd;
    }

    @RequestMapping(value = "/check/list", method = RequestMethod.POST)
    @ResponseBody
    public String getCheckList(HttpServletRequest request) throws Exception {
        JsonData jd = new JsonData();
        SmartGridOption opt = SmartGridOption.parseSmartGridOption(request);
        if (opt.isReturnCount()) {
            long count = queryCheckCount(opt);
            jd.setData(count);
        } else {
            CommonData commonData = queryCheckData(opt);
            if (opt.getPageIndex() == 0) {
                long count = queryCheckCount(opt);
                commonData.setTotalCount(count);
            }
            jd.setData(commonData);
        }
        return new Gson().toJson(jd);
    }

    @RequestMapping(value = "/detection/list", method = RequestMethod.POST)
    @ResponseBody
    public String getDetectionList(HttpServletRequest request) throws Exception {
        JsonData jd = new JsonData();
        SmartGridOption opt = SmartGridOption.parseSmartGridOption(request);
        if (opt.isReturnCount()) {
            long count = queryDetectionCount(opt);
            jd.setData(count);
        } else {
            CommonData commonData = queryDetectionData(opt);
            if (opt.getPageIndex() == 0) {
                long count = queryDetectionCount(opt);
                commonData.setTotalCount(count);
            }
            jd.setData(commonData);
        }
        return new Gson().toJson(jd);
    }

    @RequestMapping(value = "/repair/list", method = RequestMethod.POST)
    @ResponseBody
    public String getRepairList(HttpServletRequest request) throws Exception {
        JsonData jd = new JsonData();
        SmartGridOption opt = SmartGridOption.parseSmartGridOption(request);
        if (opt.isReturnCount()) {
            long count = queryRepairCount(opt);
            jd.setData(count);
        } else {
            CommonData commonData = queryRepairData(opt);
            if (opt.getPageIndex() == 0) {
                long count = queryRepairCount(opt);
                commonData.setTotalCount(count);
            }
            jd.setData(commonData);
        }
        return new Gson().toJson(jd);
    }

    @ResponseBody
    @RequestMapping(value = "/risk/history", method = RequestMethod.POST)
    public JsonData getRiskHistory(HttpServletRequest request) {
        JsonData jsonData = new JsonData();
        String params = request.getParameter("params");
        List<TowerRisk> riskList = towerRiskService.findByTowerID(params);
        jsonData.setData(riskList);
        return jsonData;
    }

    private CommonData queryCheckData(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryCheckData(opt.getParam1(), opt.getPageIndex(), opt.getPageSize(), opt.getSortColIndex(),
                SortDirection.Desc.equals(opt.getSortDirection()) ? SortType.DESC : SortType.ASC);
    }

    private long queryCheckCount(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryCheckCount(opt.getParam1());
    }

    private CommonData queryDetectionData(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryDetectionData(opt.getParam1(), opt.getPageIndex(), opt.getPageSize(), opt.getSortColIndex(),
                SortDirection.Desc.equals(opt.getSortDirection()) ? SortType.DESC : SortType.ASC);
    }

    private long queryDetectionCount(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryDetectionCount(opt.getParam1());
    }

    private CommonData queryRepairData(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryRepairData(opt.getParam1(), opt.getPageIndex(), opt.getPageSize(), opt.getSortColIndex(),
                SortDirection.Desc.equals(opt.getSortDirection()) ? SortType.DESC : SortType.ASC);
    }

    private long queryRepairCount(SmartGridOption opt) throws Exception {
        return towerDetailsService.queryRepairCount(opt.getParam1());
    }


}
