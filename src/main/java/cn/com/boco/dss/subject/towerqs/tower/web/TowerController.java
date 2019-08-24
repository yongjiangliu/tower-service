package cn.com.boco.dss.subject.towerqs.tower.web;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.com.boco.dss.common.SortDirection;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.config.zone.domain.Zone;
import cn.com.boco.dss.config.zone.service.ZoneService;
import cn.com.boco.dss.framework.security.domain.TokenUser;
import cn.com.boco.dss.framework.security.web.TokenUserUtils;
import cn.com.boco.dss.subject.common.gsonadapter.GsonUtil;
import cn.com.boco.dss.subject.towerqs.common.constant.MessageConstant;
import cn.com.boco.dss.subject.towerqs.common.constant.ZoneConstant;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.common.table.TableDelete;
import cn.com.boco.dss.subject.towerqs.common.table.TableExport;
import cn.com.boco.dss.subject.towerqs.common.table.TableQuery;
import cn.com.boco.dss.subject.towerqs.common.table.TableSave;
import cn.com.boco.dss.subject.towerqs.common.table.TableTemplateImport;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.service.TowerService;
import cn.com.boco.dss.webcore.data.commondata.CommonData;
import cn.com.boco.dss.webcore.grid.SmartGridOption;

@Controller
@RequestMapping(value = "dss/TowerService/towerList")
public class TowerController implements TableQuery, TableDelete, TableSave, TableTemplateImport, TableExport {

    private static final Logger logger = LoggerFactory.getLogger(TowerController.class);

    @Autowired
    private TowerService towerService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private TokenUserUtils tokenUserUtils;

    @Autowired
    private ZoneService zoneService;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public String getTableData(HttpServletRequest request) throws Exception {
        JsonData jd = new JsonData();
        SmartGridOption opt = SmartGridOption.parseSmartGridOption(request);
        if (opt.isReturnCount()) {
            long count = queryCount(opt, request);
            jd.setData(count);

        } else {
            CommonData commonData = queryData(opt, request);
            if (opt.getPageIndex() == 0) {
                long count = queryCount(opt, request);
                commonData.setTotalCount(count);
            }
            jd.setData(commonData);
        }
        return new Gson().toJson(jd);
    }

    private long queryCount(SmartGridOption opt, HttpServletRequest request) throws Exception {
        String areaID = getAreaID(request);
        return towerService.queryCount(areaID, opt.getPageIndex(), opt.getPageSize(), opt.getSortColIndex(),
                SortDirection.Desc.equals(opt.getSortDirection()) ? SortType.DESC : SortType.ASC);
    }

    private CommonData queryData(SmartGridOption opt, HttpServletRequest request) throws Exception {
        String areaID = getAreaID(request);
        return towerService.queryData(areaID, opt.getPageIndex(), opt.getPageSize(), opt.getSortColIndex(),
                SortDirection.Desc.equals(opt.getSortDirection()) ? SortType.DESC : SortType.ASC);
    }

    private String getAreaID(HttpServletRequest request) {
        TokenUser tokenUser = tokenUserUtils.findUserByToken(request);
        String zoneId = tokenUser.getZoneId();
        if (!StringUtils.isBlank(zoneId)) {
            Zone zone = zoneService.findZoneByZoneID(Integer.parseInt(zoneId));
            if (zone != null) {
                int level = zone.getLevelId();
                if (level == ZoneConstant.ZONE_LEVEL_AREA) {//地市
                    Area area = areaService.findAreaByAreaName(zone.getName());
                    if (area != null) {
                        return area.getAreaID();
                    }
                }
            }
        }
        return null;
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonData delete(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        List<String> idList = new Gson().fromJson(params, new TypeToken<List<String>>() {
        }.getType());
        try {
            towerService.deleteByIdList(idList);
            jd.setData(MessageConstant.MESSAGE_DELETE_SUCCESS);
        } catch (Exception e) {
            jd.setData(MessageConstant.MESSAGE_DELETE_FAILURE);
        }
        return jd;
    }

    @Override
    @RequestMapping(value = "/findOne", method = RequestMethod.POST)
    @ResponseBody
    public JsonData findOne(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        Tower tower = towerService.findById(params);
        jd.setData(tower);
        return jd;
    }

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonData save(HttpServletRequest request) {
        JsonData jd = new JsonData();
        String params = request.getParameter("params");
        Tower tower = GsonUtil.buildGson().fromJson(params, Tower.class);
        try {
            towerService.save(tower);
            jd.setData(MessageConstant.MESSAGE_SAVE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            jd.setData(MessageConstant.MESSAGE_SAVE_FAILURE);
        }
        return jd;
    }


    @Override
    @RequestMapping(value = "/template/download", method = RequestMethod.POST)
    @ResponseBody
    public void templateDownload(HttpServletRequest request, HttpServletResponse response) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String str = "static/plugin/towerqs/tower/excel/towerTemplate.xlsx";
        String fileName = path + str;
        try (InputStream inputStream = new FileInputStream(fileName)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode("towerTemplate.xlsx", "UTF-8"));
            try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                 OutputStream outputStream = response.getOutputStream()) {
                int length = 0;
                byte[] temp = new byte[1 * 1024 * 10];
                while ((length = bis.read(temp)) != -1) {
                    outputStream.write(temp, 0, length);
                }
                outputStream.flush();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public void templateImport(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public void tableDataExport(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
