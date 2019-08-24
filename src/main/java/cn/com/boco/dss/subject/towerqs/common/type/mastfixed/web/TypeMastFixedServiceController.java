package cn.com.boco.dss.subject.towerqs.common.type.mastfixed.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.type.mastfixed.domain.TypeMastFixed;
import cn.com.boco.dss.subject.towerqs.common.type.mastfixed.service.TypeMastFixedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-19 15:37
 **/
@Controller
@RequestMapping(value = "dss/TowerService/common/type/mastfixed")
public class TypeMastFixedServiceController {

    @Autowired
    private TypeMastFixedService typeMastFixedService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData findAll(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<TypeMastFixed> typeMastFixedList = typeMastFixedService.findAll();
        jd.setData(typeMastFixedList);
        return jd;
    }
}
