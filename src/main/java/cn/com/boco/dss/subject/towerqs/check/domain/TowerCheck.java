package cn.com.boco.dss.subject.towerqs.check.domain;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "dw_tower_check")
public class TowerCheck {


    @Id
    @Column(name = "DataIndex")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer dataIndex;

    @Column(name = "TowerID")
    private String towerID;

    @Column(name = "ResourceCode")
    private String resourceCode;

    @Column(name = "AddressCode")
    private String addressCode;

    @Column(name = "ConstractCode")
    private String constractCode;

    @Column(name = "CheckerName")
    private String checkerName;

    @Column(name = "CheckerPhone")
    private String checkerPhone;

    @Column(name = "CheckDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDate;

    @Column(name = "TowerHeight")
    private double towerHeight;

    @Column(name = "HasLightningConductor")
    private Integer hasLightningConductor;

    @Column(name = "AntennaCount")
    private Integer antennaCount;

    @Column(name = "AntennaCount5G")
    private Integer antennaCount5G;

    @Column(name = "AntennaPlotCount")
    private Integer antennaPlotCount;

    @Column(name = "AntennaPlotUsedCount")
    private Integer antennaPlotUsedCount;

    @Column(name = "AntennaPloEmptyCount")
    private Integer antennaPloEmptyCount;

    @Column(name = "PlatformCount")
    private Integer platformCount;

    @Column(name = "PlatformDestance1")
    private double platformDestance1;

    @Column(name = "PlatformDestance2")
    private double platformDestance2;

    @Column(name = "PlatformDestance3")
    private double platformDestance3;

    @Column(name = "PlatformDestance4")
    private double platformDestance4;

    @Column(name = "Verticality")
    private double verticality;

    @Column(name = "GroundResistance")
    private double groundResistance;

    @Column(name = "AntirustThikness")
    private double antirustThikness;

    @Column(name = "LineFixedType")
    private String lineFixedType;

    @Column(name = "MastFixedType")
    private String mastFixedType;

    @Column(name = "Mast1")
    private String mast1;

    @Column(name = "Mast2")
    private String mast2;

    @Column(name = "Mast3")
    private String mast3;

    @Column(name = "Mast4")
    private String mast4;

    @Column(name = "Mast5")
    private String mast5;

    @Column(name = "Mast6")
    private String mast6;

    @Column(name = "MastBlockCount")
    private String mastBlockCount;

    @Column(name = "Attachments")
    private String attachments;

    @Column(name = "Status")
    private Integer status;

    public Integer getDataIndex() {
        return dataIndex;
    }


    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getTowerID() {
        return this.towerID;
    }

    public void setTowerID(String towerID) {
        this.towerID = towerID;
    }

    public String getResourceCode() {
        return this.resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getAddressCode() {
        return this.addressCode;
    }


    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getConstractCode() {
        return this.constractCode;
    }


    public void setConstractCode(String constractCode) {
        this.constractCode = constractCode;
    }

    public String getCheckerName() {
        return this.checkerName;
    }


    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getCheckerPhone() {
        return this.checkerPhone;
    }


    public void setCheckerPhone(String checkerPhone) {
        this.checkerPhone = checkerPhone;
    }

    public Date getCheckDate() {
        return this.checkDate;
    }


    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public double getTowerHeight() {
        return this.towerHeight;
    }


    public void setTowerHeight(double towerHeight) {
        this.towerHeight = towerHeight;
    }

    public Integer getHasLightningConductor() {
        return this.hasLightningConductor;
    }


    public void setHasLightningConductor(Integer hasLightningConductor) {
        this.hasLightningConductor = hasLightningConductor;
    }

    public Integer getAntennaCount() {
        return this.antennaCount;
    }


    public void setAntennaCount(Integer antennaCount) {
        this.antennaCount = antennaCount;
    }

    public Integer getAntennaCount5G() {
        return this.antennaCount5G;
    }


    public void setAntennaCount5G(Integer antennaCount5G) {
        this.antennaCount5G = antennaCount5G;
    }

    public Integer getAntennaPlotCount() {
        return this.antennaPlotCount;
    }


    public void setAntennaPlotCount(Integer antennaPlotCount) {
        this.antennaPlotCount = antennaPlotCount;
    }

    public Integer getAntennaPlotUsedCount() {
        return this.antennaPlotUsedCount;
    }


    public void setAntennaPlotUsedCount(Integer antennaPlotUsedCount) {
        this.antennaPlotUsedCount = antennaPlotUsedCount;
    }

    public Integer getAntennaPloEmptyCount() {
        return this.antennaPloEmptyCount;
    }


    public void setAntennaPloEmptyCount(Integer antennaPloEmptyCount) {
        this.antennaPloEmptyCount = antennaPloEmptyCount;
    }

    public Integer getPlatformCount() {
        return this.platformCount;
    }


    public void setPlatformCount(Integer platformCount) {
        this.platformCount = platformCount;
    }

    public double getPlatformDestance1() {
        return this.platformDestance1;
    }


    public void setPlatformDestance1(double platformDestance1) {
        this.platformDestance1 = platformDestance1;
    }

    public double getPlatformDestance2() {
        return this.platformDestance2;
    }


    public void setPlatformDestance2(double platformDestance2) {
        this.platformDestance2 = platformDestance2;
    }

    public double getPlatformDestance3() {
        return this.platformDestance3;
    }


    public void setPlatformDestance3(double platformDestance3) {
        this.platformDestance3 = platformDestance3;
    }

    public double getPlatformDestance4() {
        return this.platformDestance4;
    }


    public void setPlatformDestance4(double platformDestance4) {
        this.platformDestance4 = platformDestance4;
    }

    public double getVerticality() {
        return this.verticality;
    }


    public void setVerticality(double verticality) {
        this.verticality = verticality;
    }

    public double getGroundResistance() {
        return this.groundResistance;
    }


    public void setGroundResistance(double groundResistance) {
        this.groundResistance = groundResistance;
    }

    public double getAntirustThikness() {
        return this.antirustThikness;
    }


    public void setAntirustThikness(double antirustThikness) {
        this.antirustThikness = antirustThikness;
    }

    public String getLineFixedType() {
        return this.lineFixedType;
    }


    public void setLineFixedType(String lineFixedType) {
        this.lineFixedType = lineFixedType;
    }

    public String getMastFixedType() {
        return this.mastFixedType;
    }


    public void setMastFixedType(String mastFixedType) {
        this.mastFixedType = mastFixedType;
    }

    public String getMast1() {
        return this.mast1;
    }


    public void setMast1(String mast1) {
        this.mast1 = mast1;
    }

    public String getMast2() {
        return this.mast2;
    }


    public void setMast2(String mast2) {
        this.mast2 = mast2;
    }

    public String getMast3() {
        return this.mast3;
    }


    public void setMast3(String mast3) {
        this.mast3 = mast3;
    }

    public String getMast4() {
        return this.mast4;
    }


    public void setMast4(String mast4) {
        this.mast4 = mast4;
    }

    public String getMast5() {
        return this.mast5;
    }


    public void setMast5(String mast5) {
        this.mast5 = mast5;
    }

    public String getMast6() {
        return this.mast6;
    }


    public void setMast6(String mast6) {
        this.mast6 = mast6;
    }

    public String getMastBlockCount() {
        return this.mastBlockCount;
    }


    public void setMastBlockCount(String mastBlockCount) {
        this.mastBlockCount = mastBlockCount;
    }

    public String getAttachments() {
        return this.attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
