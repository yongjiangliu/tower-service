package cn.com.boco.dss.subject.towerqs.index.controller;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.framework.security.web.TokenUserUtils;
import cn.com.boco.dss.subject.towerqs.index.service.TowerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yxy on 2019/08/19 9:20
 * Version 1.0.0
 * Description 铁塔质量管理平台首页
 */
@Controller
@RequestMapping("/dss/TowerService/index")
public class IndexController {
    @Autowired
    private TowerInfoService towerInfoService;
    @Autowired
    private TokenUserUtils tokenUserUtils;

    @RequestMapping("/risklevel")
    @ResponseBody
    private JsonData findRiskNumGroupByRiskLevel(HttpServletRequest request) {
        return towerInfoService.findRiskNumGroupByRiskLevel(request);
    }

    @RequestMapping("/riskposition")
    @ResponseBody
    private JsonData findRiskNumGroupByRiskPosition(HttpServletRequest request) {
        return towerInfoService.findRiskNumGroupByRiskPosition(request);
    }

    @RequestMapping("/map")
    @ResponseBody
    private JsonData findMapData(HttpServletRequest request) {
        return towerInfoService.findMapData(request);
    }

    @RequestMapping("/location")
    @ResponseBody
    private JsonData findTowerNumOfLocation(HttpServletRequest request) {
        return towerInfoService.findTowerNumOfLocation(request);
    }

    @RequestMapping("/status")
    @ResponseBody
    private JsonData findTowerNumGroupByStatus(HttpServletRequest request) {
        return towerInfoService.findTowerNumGroupByStatus(request);
    }

}
