package cn.com.boco.dss.subject.docmanage.controller;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.subject.docmanage.service.DocManageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yxy on 2019/08/13 14:58
 * Version 1.0.0
 * Description 铁塔质量管理平台-档案中心
 */
@Controller
@RequestMapping("/dss/TowerService/doc")
public class DocManageController {
    @Autowired
    private DocManageService docManageService;


    /**
     * 获取文件列表
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    private JsonData getDocList(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        return docManageService.getDocList(jsonObject, request);
    }

    /**
     * 获取文件列表(优化版本)
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/doclist")
    @ResponseBody
    private JsonData getDocListWithUserZoneCache(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        return docManageService.getDocListWithUserZoneCache(jsonObject, request);
    }

    /**
     * 根据指定的文件名称搜索文件
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    private JsonData search(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        return docManageService.search(jsonObject, request);
    }


    /**
     * 下载文件 同时支持单个或多个文件下载，如果是多个文件则进行打包下载zip格式，并返回zip的路径
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/download")
    @ResponseBody
    private String downloadFiles(HttpServletRequest request, HttpServletResponse response) {
        JsonData jd = new JsonData();
        try {
            String docDownloadPackageNamePath = docManageService.downloadFiles(request, response);
            if (!StringUtil.isNullOrEmpty(docDownloadPackageNamePath)) {
                jd.setData(docDownloadPackageNamePath);
                jd.setStatus("0");
            }
            else {
                jd.setData("下载成功");
                jd.setStatus("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String resultStr = StringUtil.isNullOrEmpty(e.getMessage()) ? "下载失败" : "下载失败，" + e.getMessage();
            jd.setData(resultStr);
            jd.setStatus("-1");
        }
        return JSON.toJSONString(jd);
    }

    /**
     * 下载zip文件
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/downloadzip")
    @ResponseBody
    private String downloadZip(HttpServletRequest request, HttpServletResponse response) {
        JsonData jd = new JsonData();
        try {
            docManageService.downloadZip(request, response);
            jd.setData("下载成功");
            jd.setStatus("0");
        } catch (Exception e) {
            e.printStackTrace();
            String resultStr = StringUtil.isNullOrEmpty(e.getMessage()) ? "下载失败" : "下载失败，" + e.getMessage();
            jd.setData(resultStr);
            jd.setStatus("-1");
        }
        return JSON.toJSONString(jd);
    }

    /**
     * 删除目录及其目录下的所有文件
     *
     * @param fileList 待删除的文件目录
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    private JsonData delFileOrDirectory(@RequestBody JSONArray fileList) {
        return docManageService.delFileOrDirectory(fileList);
    }

    /**
     * 创建文件夹
     *
     * @param jsonObject 待创建的文件夹路径
     * @return
     */
    @PostMapping("/mkdir")
    @ResponseBody
    public JsonData makeDir(@RequestBody JSONObject jsonObject) {
        return docManageService.makeDir(jsonObject);
    }


    /**
     * 修改文件夹和文件名
     *
     * @param jsonObject 文件夹或文件路径
     * @return
     */
    @PostMapping("/rename")
    @ResponseBody
    public JsonData rename(@RequestBody JSONObject jsonObject) {

        return docManageService.rename(jsonObject);
    }


    /**
     * 文件上传（支持批量上传）
     *
     * @param request request对象
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public JsonData upload(HttpServletRequest request) {
        return docManageService.upload(request);
    }

}
