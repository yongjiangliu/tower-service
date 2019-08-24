package cn.com.boco.dss.subject.towerqs.common.type.baseposition.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-16 18:09
 **/
@Entity
@Table(name = "dim_type_baseposition")
public class TypeBasePosition implements Serializable {

    @Id
    @Column(name = "TypeID")
    private String typeID;

    @Column(name = "TypeGroup")
    private String typeGroup;

    @Column(name = "TypeName")
    private String typeName;

    @Column(name = "TypeDescription")
    private String typeDescription;

    @Column(name = "ParentID")
    private String parentID;

    @Column(name = "SortOrder")
    private String sortOrder;

    @Column(name = "Closed")
    private String closed;

    public TypeBasePosition() {
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "TypeBasePosition{" +
                "typeID='" + typeID + '\'' +
                ", typeGroup='" + typeGroup + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", parentID='" + parentID + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", closed='" + closed + '\'' +
                '}';
    }
}
