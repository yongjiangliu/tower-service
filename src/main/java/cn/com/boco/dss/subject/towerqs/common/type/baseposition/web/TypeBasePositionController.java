package cn.com.boco.dss.subject.towerqs.common.type.baseposition.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.type.baseposition.domain.TypeBasePosition;
import cn.com.boco.dss.subject.towerqs.common.type.baseposition.service.TypeBasePositionService;
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
 * @create: 2019-08-19 11:22
 **/
@Controller
@RequestMapping(value = "dss/TowerService/common/type/baseposition")
public class TypeBasePositionController {

    @Autowired
    private TypeBasePositionService typeBasePositionService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData findAll(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<TypeBasePosition> typeBasePositionList = typeBasePositionService.findAll();
        jd.setData(typeBasePositionList);
        return jd;
    }


}
