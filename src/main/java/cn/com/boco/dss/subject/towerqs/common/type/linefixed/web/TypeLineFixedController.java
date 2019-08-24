package cn.com.boco.dss.subject.towerqs.common.type.linefixed.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.type.linefixed.domain.TypeLineFixed;
import cn.com.boco.dss.subject.towerqs.common.type.linefixed.service.TypeLineFixedService;
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
 * @create: 2019-08-19 15:32
 **/
@Controller
@RequestMapping(value = "dss/TowerService/common/type/linefixed")
public class TypeLineFixedController {

    @Autowired
    private TypeLineFixedService typeLineFixedService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData findAll(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<TypeLineFixed> typeLineFixedList = typeLineFixedService.findAll();
        jd.setData(typeLineFixedList);
        return jd;
    }


}
