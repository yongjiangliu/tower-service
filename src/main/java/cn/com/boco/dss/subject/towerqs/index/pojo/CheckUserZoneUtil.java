package cn.com.boco.dss.subject.towerqs.index.pojo;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.config.zone.domain.Zone;
import cn.com.boco.dss.config.zone.service.ZoneService;
import cn.com.boco.dss.data.DbHelper;
import cn.com.boco.dss.database.DbSys;
import cn.com.boco.dss.framework.security.domain.TokenUser;
import cn.com.boco.dss.framework.security.web.TokenUserUtils;
import cn.com.boco.dss.subject.towerqs.common.constant.ZoneConstant;

/**
 * Created by yxy on 2019/08/16 18:04
 * Version 1.0.0
 * Description 检查登陆用户归属的区域
 */
@Component
public class CheckUserZoneUtil {

    @Autowired
    private TokenUserUtils tokenUserUtils;
    @Autowired
    private DbSys dbSys;
    @Autowired
    private ZoneService zoneService;

    /**
     * 检查登陆用户归属的区域 -1：全部数据；1：省端用户；:2：地市端用户；3：区县；999：是其他用户组；
     *
     * @param request request对象
     * @return 用户归属的区域标识
     */
    public UserZone checkUserZone(HttpServletRequest request) throws Exception {
        UserZone userZone = new UserZone();
        int userStatus = -1;//全部数据
        String userDocUrl = "";//当前登陆用户应该扫描的路径
        TokenUser tokenUser = tokenUserUtils.findUserByToken(request);
        if (tokenUser != null) {
            int originalZondId = Integer.parseInt(tokenUser.getZoneId());
            //需求要求：如果登陆用户没有设置任何区域归属，则默认到四川省端用户；
            int proviceId = 280;
            int zondId = originalZondId == -1 ? proviceId : originalZondId;
            Zone zone = zoneService.findZoneByZoneID(zondId);
            if (originalZondId == -1) { //全部数据
                userStatus = -1;
            }
            else {
                Integer levelId = zone.getLevelId();//1：省份；2：地市；3：区县；4:xxx
                if (levelId == ZoneConstant.ZONE_LEVEL_PROVINCE) {
                    userStatus = 1;//省端用户
                    Zone zoneProvince = zoneService.findZoneByZoneID(proviceId);
                    userDocUrl = "通信塔资料/中华人民共和国/" + zoneProvince.getName() + "/";
                }
                else if (levelId == ZoneConstant.ZONE_LEVEL_AREA) {
                    userStatus = 2;//某个具体的地市端用户 比如成都市 只能看到成都市下所有的区县
                    Zone zoneProvince = zoneService.findZoneByZoneID(proviceId);
                    userDocUrl = "通信塔资料/中华人民共和国/" + zoneProvince.getName() + "/" + zone.getName() + "/";
                }
                else if (levelId == ZoneConstant.ZONE_LEVEL_COUNTY) {
                    userStatus = 3;//区县
                }
                else {
                    userStatus = 999;//其他
                }
            }
            //赋值用户标识
            userZone.setUserStatus(userStatus);
            //赋值zone对象
            userZone.setZone(zone);
            //赋值扫描路径
            userZone.setUserDocUrl(userDocUrl);
        }
        else {
            throw new Exception("未获取到TokenUser信息");
        }
        return userZone;
    }

    private DataTable getZoneListByParentZoneId(int parentZoneId) {
        DataTable dt = new DataTable();
        try {
            String sql = "SELECT t.zone_id,t.zone_name,t.zone_level FROM sys_fw_zone t where t.parent_zone_id=? ORDER BY t.zone_order asc ";
            Object[] args = {parentZoneId};
            int[] argTypes = {Types.VARCHAR};
            dt = DbHelper.getDataBySql(sql, args, argTypes, dbSys.getConnectSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取区域名称
     *
     * @param parentZoneId
     * @return
     */
    public List<String> findZoneNamesByParentZoneId(int parentZoneId) {
        List<String> list = new ArrayList<>();
        try {
            DataTable dt = getZoneListByParentZoneId(parentZoneId);
            for (int i = 0; i < dt.getRows().size(); i++) {
                String zoneName = dt.getRows().get(i).getString(1);
                list.add(zoneName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (list.size() == 0) {
                list.add("20190820");//随便添加一条，防止jpa执行sql报错；
            }
        }
        return list;
    }


    /**
     * 省份的区域id
     *
     * @param parentZoneId
     * @return
     */
    public Zone findZoneByParentZoneIdAndZoneName(int parentZoneId, String zoneName) {
        Zone zone = new Zone();
        try {
            DataTable dt = getZoneListByParentZoneId(parentZoneId);
            if (dt.getRows().size() > 0) {
                for (int i = 0; i < dt.getRows().size(); i++) {
                    Integer zondIdTemp = dt.getRows().get(i).getInteger(0);
                    String zoneNameTemp = dt.getRows().get(i).getString(1);
                    if (zoneNameTemp.contains(zoneName)) {
                        zone.setZoneId(zondIdTemp);
                        zone.setName(zoneNameTemp);
                        break;
                    }
                }
            }
            else {
                zone.setZoneId(280);
                zone.setName("四川");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zone;
    }
}
