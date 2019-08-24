package cn.com.boco.dss.subject.towerqs.tower.domain;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-21 10:58
 **/

import java.io.Serializable;

/**
 * 存放TowerInfo表中所有Status以上的字段
 */
public class TowerDetails implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String towerID;

    private String resourceCode;

    private String addressCode;

    private String towerName;

    private String address;

    private String contactName;

    private String contactPhone;

    private String location;

    private String coordinateE;

    private String coordinateN;

    private String sceneType;

    private String areaID;

    private String towerShapeType;

    private Double towerHeight;

    private Double elevationHeight;

    private Integer hasLightningConductor;

    private Double windPressure;

    private Double baseHeight;

    private String basePositionType;

    private String createCompany;

    private String maintenanceCompany;

    private String detectionCompany;

    private String supervisoryCompany;

    private String designCompany;

    private String constructionCompany;

    private Integer antennaCount;

    private Integer antennaCount5G;

    private Integer antennaPlotCount;

    private Integer antennaPlotUsedCount;

    private Integer antennaPloEmptyCount;

    private Integer platformCount;

    private Double platformDestance1;

    private Double platformDestance2;

    private Double platformDestance3;

    private Double platformDestance4;

    private Double loadWeight;

    private String memo;

    private Double verticality;

    private Double groundResistance;

    private Double antirustThikness;

    private String lineFixedType;

    private String mastFixedType;

    private String mast1;

    private String mast2;

    private String mast3;

    private String mast4;

    private String mast5;

    private String mast6;

    private String mastBlockCount;

    private String attachments;

    public TowerDetails() {
    }

    public TowerDetails(Tower tower) {
        this.towerID = tower.getTowerID();
        this.resourceCode = tower.getResourceCode();
        this.addressCode = tower.getAddressCode();
        this.towerName = tower.getTowerName();
        this.address = tower.getAddress();
        this.contactName = tower.getContactName();
        this.contactPhone = tower.getContactPhone();
        this.location = tower.getLocation();
        this.coordinateE = tower.getCoordinateE();
        this.coordinateN = tower.getCoordinateN();
        this.sceneType = tower.getSceneType();
        this.areaID = tower.getAreaID();
        this.towerShapeType = tower.getTowerShapeType();
        this.towerHeight = tower.getTowerHeight();
        this.elevationHeight = tower.getElevationHeight();
        this.hasLightningConductor = tower.getHasLightningConductor();
        this.windPressure = tower.getWindPressure();
        this.baseHeight = tower.getBaseHeight();
        this.basePositionType = tower.getBasePositionType();
        this.createCompany = tower.getCreateCompany();
        this.maintenanceCompany = tower.getMaintenanceCompany();
        this.detectionCompany = tower.getDetectionCompany();
        this.supervisoryCompany = tower.getSupervisoryCompany();
        this.designCompany = tower.getDesignCompany();
        this.constructionCompany = tower.getConstructionCompany();
        this.antennaCount = tower.getAntennaCount();
        this.antennaCount5G = tower.getAntennaCount5G();
        this.antennaPlotCount = tower.getAntennaPlotCount();
        this.antennaPlotUsedCount = tower.getAntennaPlotUsedCount();
        this.antennaPloEmptyCount = tower.getAntennaPloEmptyCount();
        this.platformCount = tower.getPlatformCount();
        this.platformDestance1 = tower.getPlatformDestance1();
        this.platformDestance2 = tower.getPlatformDestance2();
        this.platformDestance3 = tower.getPlatformDestance3();
        this.platformDestance4 = tower.getPlatformDestance4();
        this.loadWeight = tower.getLoadWeight();
        this.memo = tower.getMemo();
        this.verticality = tower.getVerticality();
        this.groundResistance = tower.getGroundResistance();
        this.antirustThikness = tower.getAntirustThikness();
        this.lineFixedType = tower.getLineFixedType();
        this.mastFixedType = tower.getMastFixedType();
        this.mast1 = tower.getMast1();
        this.mast2 = tower.getMast2();
        this.mast3 = tower.getMast3();
        this.mast4 = tower.getMast4();
        this.mast5 = tower.getMast5();
        this.mast6 = tower.getMast6();
        this.mastBlockCount = tower.getMastBlockCount();
        this.attachments = tower.getAttachments();
    }

    public String getTowerID() {
        return towerID;
    }

    public void setTowerID(String towerID) {
        this.towerID = towerID;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordinateE() {
        return coordinateE;
    }

    public void setCoordinateE(String coordinateE) {
        this.coordinateE = coordinateE;
    }

    public String getCoordinateN() {
        return coordinateN;
    }

    public void setCoordinateN(String coordinateN) {
        this.coordinateN = coordinateN;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getTowerShapeType() {
        return towerShapeType;
    }

    public void setTowerShapeType(String towerShapeType) {
        this.towerShapeType = towerShapeType;
    }

    public Double getTowerHeight() {
        return towerHeight;
    }

    public void setTowerHeight(Double towerHeight) {
        this.towerHeight = towerHeight;
    }

    public Double getElevationHeight() {
        return elevationHeight;
    }

    public void setElevationHeight(Double elevationHeight) {
        this.elevationHeight = elevationHeight;
    }

    public Integer getHasLightningConductor() {
        return hasLightningConductor;
    }

    public void setHasLightningConductor(Integer hasLightningConductor) {
        this.hasLightningConductor = hasLightningConductor;
    }

    public Double getWindPressure() {
        return windPressure;
    }

    public void setWindPressure(Double windPressure) {
        this.windPressure = windPressure;
    }

    public Double getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(Double baseHeight) {
        this.baseHeight = baseHeight;
    }

    public String getBasePositionType() {
        return basePositionType;
    }

    public void setBasePositionType(String basePositionType) {
        this.basePositionType = basePositionType;
    }

    public String getCreateCompany() {
        return createCompany;
    }

    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    public String getMaintenanceCompany() {
        return maintenanceCompany;
    }

    public void setMaintenanceCompany(String maintenanceCompany) {
        this.maintenanceCompany = maintenanceCompany;
    }

    public String getDetectionCompany() {
        return detectionCompany;
    }

    public void setDetectionCompany(String detectionCompany) {
        this.detectionCompany = detectionCompany;
    }

    public String getSupervisoryCompany() {
        return supervisoryCompany;
    }

    public void setSupervisoryCompany(String supervisoryCompany) {
        this.supervisoryCompany = supervisoryCompany;
    }

    public String getDesignCompany() {
        return designCompany;
    }

    public void setDesignCompany(String designCompany) {
        this.designCompany = designCompany;
    }

    public String getConstructionCompany() {
        return constructionCompany;
    }

    public void setConstructionCompany(String constructionCompany) {
        this.constructionCompany = constructionCompany;
    }

    public Integer getAntennaCount() {
        return antennaCount;
    }

    public void setAntennaCount(Integer antennaCount) {
        this.antennaCount = antennaCount;
    }

    public Integer getAntennaCount5G() {
        return antennaCount5G;
    }

    public void setAntennaCount5G(Integer antennaCount5G) {
        this.antennaCount5G = antennaCount5G;
    }

    public Integer getAntennaPlotCount() {
        return antennaPlotCount;
    }

    public void setAntennaPlotCount(Integer antennaPlotCount) {
        this.antennaPlotCount = antennaPlotCount;
    }

    public Integer getAntennaPlotUsedCount() {
        return antennaPlotUsedCount;
    }

    public void setAntennaPlotUsedCount(Integer antennaPlotUsedCount) {
        this.antennaPlotUsedCount = antennaPlotUsedCount;
    }

    public Integer getAntennaPloEmptyCount() {
        return antennaPloEmptyCount;
    }

    public void setAntennaPloEmptyCount(Integer antennaPloEmptyCount) {
        this.antennaPloEmptyCount = antennaPloEmptyCount;
    }

    public Integer getPlatformCount() {
        return platformCount;
    }

    public void setPlatformCount(Integer platformCount) {
        this.platformCount = platformCount;
    }

    public Double getPlatformDestance1() {
        return platformDestance1;
    }

    public void setPlatformDestance1(Double platformDestance1) {
        this.platformDestance1 = platformDestance1;
    }

    public Double getPlatformDestance2() {
        return platformDestance2;
    }

    public void setPlatformDestance2(Double platformDestance2) {
        this.platformDestance2 = platformDestance2;
    }

    public Double getPlatformDestance3() {
        return platformDestance3;
    }

    public void setPlatformDestance3(Double platformDestance3) {
        this.platformDestance3 = platformDestance3;
    }

    public Double getPlatformDestance4() {
        return platformDestance4;
    }

    public void setPlatformDestance4(Double platformDestance4) {
        this.platformDestance4 = platformDestance4;
    }

    public Double getLoadWeight() {
        return loadWeight;
    }

    public void setLoadWeight(Double loadWeight) {
        this.loadWeight = loadWeight;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getVerticality() {
        return verticality;
    }

    public void setVerticality(Double verticality) {
        this.verticality = verticality;
    }

    public Double getGroundResistance() {
        return groundResistance;
    }

    public void setGroundResistance(Double groundResistance) {
        this.groundResistance = groundResistance;
    }

    public Double getAntirustThikness() {
        return antirustThikness;
    }

    public void setAntirustThikness(Double antirustThikness) {
        this.antirustThikness = antirustThikness;
    }

    public String getLineFixedType() {
        return lineFixedType;
    }

    public void setLineFixedType(String lineFixedType) {
        this.lineFixedType = lineFixedType;
    }

    public String getMastFixedType() {
        return mastFixedType;
    }

    public void setMastFixedType(String mastFixedType) {
        this.mastFixedType = mastFixedType;
    }

    public String getMast1() {
        return mast1;
    }

    public void setMast1(String mast1) {
        this.mast1 = mast1;
    }

    public String getMast2() {
        return mast2;
    }

    public void setMast2(String mast2) {
        this.mast2 = mast2;
    }

    public String getMast3() {
        return mast3;
    }

    public void setMast3(String mast3) {
        this.mast3 = mast3;
    }

    public String getMast4() {
        return mast4;
    }

    public void setMast4(String mast4) {
        this.mast4 = mast4;
    }

    public String getMast5() {
        return mast5;
    }

    public void setMast5(String mast5) {
        this.mast5 = mast5;
    }

    public String getMast6() {
        return mast6;
    }

    public void setMast6(String mast6) {
        this.mast6 = mast6;
    }

    public String getMastBlockCount() {
        return mastBlockCount;
    }

    public void setMastBlockCount(String mastBlockCount) {
        this.mastBlockCount = mastBlockCount;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "TowerDetails{" +
                ", towerID='" + towerID + '\'' +
                ", resourceCode='" + resourceCode + '\'' +
                ", addressCode='" + addressCode + '\'' +
                ", towerName='" + towerName + '\'' +
                ", address='" + address + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", location='" + location + '\'' +
                ", coordinateE='" + coordinateE + '\'' +
                ", coordinateN='" + coordinateN + '\'' +
                ", sceneType='" + sceneType + '\'' +
                ", areaID='" + areaID + '\'' +
                ", towerShapeType='" + towerShapeType + '\'' +
                ", towerHeight='" + towerHeight + '\'' +
                ", elevationHeight='" + elevationHeight + '\'' +
                ", hasLightningConductor='" + hasLightningConductor + '\'' +
                ", windPressure='" + windPressure + '\'' +
                ", baseHeight='" + baseHeight + '\'' +
                ", basePositionType='" + basePositionType + '\'' +
                ", createCompany='" + createCompany + '\'' +
                ", maintenanceCompany='" + maintenanceCompany + '\'' +
                ", detectionCompany='" + detectionCompany + '\'' +
                ", supervisoryCompany='" + supervisoryCompany + '\'' +
                ", designCompany='" + designCompany + '\'' +
                ", constructionCompany='" + constructionCompany + '\'' +
                ", antennaCount='" + antennaCount + '\'' +
                ", antennaCount5G='" + antennaCount5G + '\'' +
                ", antennaPlotCount='" + antennaPlotCount + '\'' +
                ", antennaPlotUsedCount='" + antennaPlotUsedCount + '\'' +
                ", antennaPloEmptyCount='" + antennaPloEmptyCount + '\'' +
                ", platformCount='" + platformCount + '\'' +
                ", platformDestance1='" + platformDestance1 + '\'' +
                ", platformDestance2='" + platformDestance2 + '\'' +
                ", platformDestance3='" + platformDestance3 + '\'' +
                ", platformDestance4='" + platformDestance4 + '\'' +
                ", loadWeight='" + loadWeight + '\'' +
                ", memo='" + memo + '\'' +
                ", verticality='" + verticality + '\'' +
                ", groundResistance='" + groundResistance + '\'' +
                ", antirustThikness='" + antirustThikness + '\'' +
                ", lineFixedType='" + lineFixedType + '\'' +
                ", mastFixedType='" + mastFixedType + '\'' +
                ", mast1='" + mast1 + '\'' +
                ", mast2='" + mast2 + '\'' +
                ", mast3='" + mast3 + '\'' +
                ", mast4='" + mast4 + '\'' +
                ", mast5='" + mast5 + '\'' +
                ", mast6='" + mast6 + '\'' +
                ", mastBlockCount='" + mastBlockCount + '\'' +
                ", attachments='" + attachments + '\'' +
                '}';
    }

}
