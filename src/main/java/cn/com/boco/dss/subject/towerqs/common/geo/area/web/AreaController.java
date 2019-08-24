package cn.com.boco.dss.subject.towerqs.common.geo.area.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: TowerService
 * @author: lyj
 * @create: 2019-08-16 15:35
 **/
@Controller
@RequestMapping(value = "dss/TowerService/common/geo/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData getAreaList(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<Area> list = areaService.findAll();
        jd.setData(list);
        return jd;
    }

}
