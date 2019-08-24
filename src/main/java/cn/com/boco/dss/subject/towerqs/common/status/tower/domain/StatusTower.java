package cn.com.boco.dss.subject.towerqs.common.status.tower.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @program: TowerService
 * @description: 铁塔状态维护表实体类
 * @author: lyj
 * @create: 2019-08-21 09:38
 **/
@Entity
@Table(name = "dim_status_tower")
public class StatusTower implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "StatusID")
    private String statusID;

    @Column(name = "StatusName")
    private String statusName;

    @Column(name = "StatusDesc")
    private String statusDesc;

    public StatusTower() {
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public String toString() {
        return "StatusTower{" +
                "statusID='" + statusID + '\'' +
                ", statusName='" + statusName + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                '}';
    }
}
