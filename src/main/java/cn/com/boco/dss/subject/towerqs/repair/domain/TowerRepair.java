package cn.com.boco.dss.subject.towerqs.repair.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "dw_tower_repair")
public class TowerRepair {

    @Id
    @Column(name = "DateIndex" )
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer dataIndex;

    @Column(name = "RiskDataIndex")
    private Integer riskDataIndex;

    @Transient
    private List<Integer> riskIds;

    @Column(name = "TowerID")
    private String towerID;

    @Column(name = "ResourceCode")
    private String resourceCode;

    @Column(name = "AddressCode")
    private String addressCode;

    @Column(name = "ConstractCode")
    private String constractCode;

    @Column(name = "CompanName")
    private String companName;

    @Column(name = "IsMaintenanceCompany")
    private Integer isMaintenanceCompany;

    @Column(name = "ContactName")
    private String contactName;

    @Column(name = "ContactPhone")
    private String contactPhone;

    @Column(name = "ConstractionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date constractionDate;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "UpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public Integer getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Integer getRiskDataIndex() {
        return riskDataIndex;
    }

    public void setRiskDataIndex(Integer riskDataIndex) {
        this.riskDataIndex = riskDataIndex;
    }

    public  List<Integer> getRiskIds(){
        return  riskIds;
    }

    public  void setRiskIds(List<Integer> riskIds){
        this.riskIds=riskIds;
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

    public String getConstractCode() {
        return constractCode;
    }

    public void setConstractCode(String constractCode) {
        this.constractCode = constractCode;
    }

    public String getCompanName() {
        return companName;
    }

    public void setCompanName(String companName) {
        this.companName = companName;
    }

    public Integer getIsMaintenanceCompany() {
        return isMaintenanceCompany;
    }

    public void setIsMaintenanceCompany(Integer isMaintenanceCompany) {
        this.isMaintenanceCompany = isMaintenanceCompany;
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

    public Date getConstractionDate() {
        return constractionDate;
    }

    public void setConstractionDate(Date constractionDate) {
        this.constractionDate = constractionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
