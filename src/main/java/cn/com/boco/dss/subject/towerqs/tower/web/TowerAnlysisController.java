package cn.com.boco.dss.subject.towerqs.tower.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.constant.AxisConstant;
import cn.com.boco.dss.subject.towerqs.tower.domain.ChartData;
import cn.com.boco.dss.subject.towerqs.tower.domain.DbChartData;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerAnlysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: TowerService
 * @description: 封装站点明细相关请求
 * @author: lyj
 * @create: 2019-08-22 16:01
 **/
@Controller
@RequestMapping(value = "dss/TowerService/towerAnalysis")
public class TowerAnlysisController {

    @Autowired
    private TowerAnlysisService towerAnlysisService;

    @RequestMapping(value = "/chartdata/safe", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getSafeChartData(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        List<DbChartData> list = towerAnlysisService.getSafeChartData(params);
        ChartData chartData = getChartData(list);
        jd.setData(chartData);
        return jd;
    }

    @RequestMapping(value = "/chartdata/verticality", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getVerticalityChartData(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        List<DbChartData> list = towerAnlysisService.getVerticalityChartData(params);
        ChartData chartData = getChartData(list);
        jd.setData(chartData);
        return jd;
    }

    @RequestMapping(value = "/chartdata/groundResistance", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getGroundResistanceChartData(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        List<DbChartData> list = towerAnlysisService.getGroundResistance(params);
        ChartData chartData = getChartData(list);
        jd.setData(chartData);
        return jd;
    }

    private ChartData getChartData(List<DbChartData> list) {
        List<String> columns = new ArrayList<>(2);
        columns.add(AxisConstant.AXIS_TIME);
        columns.add(AxisConstant.AXIS_NUM);
        List<Map<String, String>> rows = getRows(list);
        ChartData chartData = new ChartData();
        chartData.setColumns(columns);
        chartData.setRows(rows);
        return chartData;
    }

    private List<Map<String, String>> getRows(List<DbChartData> list) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (DbChartData dbChartData : list) {
            Map<String, String> map = new HashMap<>(2);
            map.put(AxisConstant.AXIS_TIME, dbChartData.getK());
            map.put(AxisConstant.AXIS_NUM, dbChartData.getV());
            mapList.add(map);
        }
        return mapList;
    }


}
