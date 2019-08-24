package cn.com.boco.dss.subject.towerqs.common.geo.area.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @program: TowerService
 * @description: 地市实体类
 * @author: lyj
 * @create: 2019-08-15 20:31
 **/
@Entity
@Table(name = "dim_geo_area")
public class Area implements Serializable {

    @Id
    @Column(name = "AreaID")
    private String areaID;

    @Column(name = "AreaName")
    private String areaName;

    @Column(name = "ProvinceID")
    private String provinceID;

    @Column(name = "ProvinceName")
    private String provinceName;

    public Area() {
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "Area{" +
                "areaID='" + areaID + '\'' +
                ", areaName='" + areaName + '\'' +
                ", provinceID='" + provinceID + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
