package cn.com.boco.dss.subject.common.helper;


public class FilePath {

    /**
     * 省
     */
    private String provinceName;

    /**
     * 地市
     */
    private String areaName;

    /**
     * 区县
     */
    private String location;

    /**
     * 站址名称
     */
    private String towerName;

    /**
     * 站址编码
     */
    private String addRessCode;



    /**
     * YYYYMMDD
     */
    private String dateTime;

    /**
     * 合同号
     */
    private String constractCode;

//    /**
//     * 文件操作类别  save/read
//     */
//    private String type;



    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getAddRessCode() {
        return addRessCode;
    }

    public void setAddRessCode(String addRessCode) {
        this.addRessCode = addRessCode;
    }



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getConstractCode() {
        return constractCode;
    }

    public void setConstractCode(String constractCode) {
        this.constractCode = constractCode;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
