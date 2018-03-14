package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 结算平台-公积金中心信息表
 * Created by gxy on 17-12-7.
 */
@Entity
@Table(name = "c_bank_centerinfo")
@org.hibernate.annotations.Table(appliesTo = "c_bank_centerinfo", comment = "结算平台-公积金中心信息表")
public class CBankCenterInfo extends Common implements Serializable {

    private static final long serialVersionUID = 6386082310814412569L;

    @Column(name = "unit_no", columnDefinition = "VARCHAR(15) DEFAULT NULL COMMENT '中心机构编码'")
    private String unit_no;

    @Column(name = "unit_name", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '机构名称'")
    private String unit_name;

    @Column(name = "parent_unit_no", columnDefinition = "VARCHAR(15) DEFAULT NULL COMMENT '上级机构编码'")
    private String parent_unit_no;

    @Column(name = "node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '中心节点号'")
    private String node;

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.updated_at = new Date();
        this.unit_no = unit_no;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.updated_at = new Date();
        this.unit_name = unit_name;
    }

    public String getParent_unit_no() {
        return parent_unit_no;
    }

    public void setParent_unit_no(String parent_unit_no) {
        this.updated_at = new Date();
        this.parent_unit_no = parent_unit_no;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.updated_at = new Date();
        this.node = node;
    }
}
