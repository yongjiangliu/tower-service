package cn.com.boco.dss.subject.towerqs.common.type.scene.web;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.common.type.scene.domain.TypeScene;
import cn.com.boco.dss.subject.towerqs.common.type.scene.service.TypeSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lyj
 * @Title: TypeSceneController
 * @Description: sceneType维度表的请求操作
 */
@Controller
@RequestMapping(value = "dss/TowerService/common/type/scene")
public class TypeSceneController {

    @Autowired
    private TypeSceneService typeSceneService;

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public JsonData findAll(HttpServletRequest request) {
        JsonData jd = new JsonData();
        List<TypeScene> typeSceneList = typeSceneService.findAll();
        jd.setData(typeSceneList);
        return jd;
    }


}
