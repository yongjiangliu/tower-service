package cn.com.boco.dss.subject.towerqs.common.type.towershape.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.type.towershape.domain.TypeTowerShape;
import cn.com.boco.dss.subject.towerqs.common.type.towershape.service.TypeTowerShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lyj
 * @Title: TypeTowerShapeController
 */
@Controller
@RequestMapping(value = "dss/TowerService/common/type/towerscene")
public class TypeTowerShapeController {

    @Autowired
    private TypeTowerShapeService typeTowerShapeService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData findAll(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<TypeTowerShape> typeTowerShapeList = typeTowerShapeService.findAll();
        jd.setData(typeTowerShapeList);
        return jd;
    }

}
