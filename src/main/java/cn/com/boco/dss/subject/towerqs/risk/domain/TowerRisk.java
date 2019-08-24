package cn.com.boco.dss.subject.towerqs.risk.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "dw_tower_risk")
public class TowerRisk {

	@Id
	@Column(name = "DataIndex")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer dataIndex;

	@Column(name = "CheckDataIndex")
	private Integer checkDataIndex;

	@Column(name = "DetectionDataIndex")
	private Integer detectionDataIndex;

	@Column(name = "TowerID")
	private String towerID;

	@Column(name = "ResourceCode")
	private String resourceCode;

	@Column(name = "AddressCode")
	private String addressCode;

	@Column(name = "RiskID")
	private String riskID;

	@Column(name = "RiskLevel")
	private Integer riskLevel;

	@Column(name = "RiskPosition")
	private String riskPosition;

	@Column(name = "RiskDescription")
	private String riskDescription;

	@Column(name = "RiskImageCount")
	private Integer riskImageCount;

	@Column(name = "RecordDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordDate;

	@Column(name = "RepairDateIndex")
	private Integer repairDateIndex;

	@Column(name = "RepairMethod")
	private String repairMethod;

	@Column(name = "RepairDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date repairDate;

	@Column(name = "RepairStatus")
	private Integer repairStatus;

	@Column(name = "RepairResult")
	private Integer repairResult;

	@Column(name = "RiskStatus")
	private Integer riskStatus;

	@Column(name = "RepairPrice")
	private Integer repairPrice;
	
	@Transient
	private String riskName;
	
	@Transient
	private List<String> imageNameList;

	@Transient
	private  HashMap<String, List<String>> imageNameListMap;

	public Integer getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public Integer getCheckDataIndex() {
		return checkDataIndex;
	}

	public void setCheckDataIndex(Integer checkDataIndex) {
		this.checkDataIndex = checkDataIndex;
	}

	public Integer getDetectionDataIndex() {
		return detectionDataIndex;
	}

	public void setDetectionDataIndex(Integer detectionDataIndex) {
		this.detectionDataIndex = detectionDataIndex;
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

	public String getRiskID() {
		return riskID;
	}

	public void setRiskID(String riskID) {
       this. riskID = riskID;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRiskPosition() {
		return riskPosition;
	}

	public void setRiskPosition(String riskPosition) {
		this.riskPosition = riskPosition;
	}

	public String getRiskDescription() {
		return riskDescription;
	}

	public void setRiskDescription(String riskDescription) {
		this.riskDescription = riskDescription;
	}

	public Integer getRiskImageCount() {
		return riskImageCount;
	}

	public void setRiskImageCount(Integer riskImageCount) {
		this.riskImageCount = riskImageCount;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Integer getRepairDateIndex() {
		return repairDateIndex;
	}

	public void setRepairDateIndex(Integer repairDateIndex) {
		this.repairDateIndex = repairDateIndex;
	}

	public String getRepairMethod() {
		return repairMethod;
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public Date getRepairDate() {
		return repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}

	public Integer getRepairStatus() {
		return repairStatus;
	}

	public void setRepairStatus(Integer repairStatus) {
		this.repairStatus = repairStatus;
	}

	public Integer getRepairResult() {
		return repairResult;
	}

	public void setRepairResult(Integer repairResult) {
		this.repairResult = repairResult;
	}

	public Integer getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Integer riskStatus) {
		this.riskStatus = riskStatus;
	}

	public Integer getRepairPrice() {
		return repairPrice;
	}

	public void setRepairPrice(Integer repairPrice) {
		this.repairPrice = repairPrice;
	}

	public List<String> getImageNameList() {
		return imageNameList;
	}

	public void setImageNameList(List<String> imageNameList) {
		this.imageNameList = imageNameList;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}





	public  HashMap<String, List<String>>  getImageNameListMap() {
		return imageNameListMap;
	}

	public void setImageNameListMap( HashMap<String, List<String>> imageNameListMap) {
		this.imageNameListMap = imageNameListMap;
	}


}
