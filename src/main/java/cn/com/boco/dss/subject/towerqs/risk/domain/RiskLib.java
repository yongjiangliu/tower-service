package cn.com.boco.dss.subject.towerqs.risk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dim_risk")
public class RiskLib {

	@Id
	@Column(name = "RiskID")
	private String riskID;
	
	@Column(name = "RiskName")
	private String riskName;
	
	@Column(name = "RiskDescription")
	private String riskDescription;
	
	@Column(name = "RiskLevel")
	private Integer riskLevel;
	
	@Column(name = "ParentID")
	private String parentID;

	public String getRiskID() {
		return riskID;
	}

	public void setRiskID(String riskID) {
		this.riskID = riskID;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getRiskDescription() {
		return riskDescription;
	}

	public void setRiskDescription(String riskDescription) {
		this.riskDescription = riskDescription;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	
}
