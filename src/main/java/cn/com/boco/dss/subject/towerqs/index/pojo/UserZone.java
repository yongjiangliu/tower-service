package cn.com.boco.dss.subject.towerqs.index.pojo;

import cn.com.boco.dss.config.zone.domain.Zone;

/**
 * Created by yxy on 2019/08/16 17:46
 * Version 1.0.0
 * Description 登陆用户的归属
 */

public class UserZone {

    /**
     * 检查登陆用户归属的区域 -1：全部数据；1：省端用户；:2：地市端用户；3：区县；999：是其他用户组；
     */
    private Integer userStatus = -1;
    private Zone zone;
    /**
     * 当前登陆用户应该扫描的路径 有权限控制
     * 路径是“中国人民共和国/四川/成都/.......”,配置的路径最后面会带“/”
     */
    private String userDocUrl;


    public Integer getUserStatus() {
        return userStatus;
    }


    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getUserDocUrl() {
        return userDocUrl;
    }

    public void setUserDocUrl(String userDocUrl) {
        this.userDocUrl = userDocUrl;
    }
}
