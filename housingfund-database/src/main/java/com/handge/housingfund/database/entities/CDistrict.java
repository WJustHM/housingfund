package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by sjw on 2017/10/11.
 */
@Entity
@Table(name = "c_district")
@org.hibernate.annotations.Table(appliesTo = "c_district", comment = "缴存地")
public class CDistrict extends Common implements Serializable {

    private static final long serialVersionUID = 2887989130474747250L;

    @Column(name = "id", columnDefinition = "VARCHAR(32) NOT NULL COMMENT '编码'")
    private String id;
    @Column(name = "parent", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '父编码'")
    private String parent;
    @Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '名称'")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
    public CDistrict() {
        super();

    }
    public CDistrict(String id, Date created_at, Date updated_at, Date deleted_at,boolean deleted, String parent, String name) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.parent = parent;
        this.name = name;

    }
}
