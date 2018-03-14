package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-12-8.
 */
@XmlRootElement(name = "中心信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class CenterInfoModel implements Serializable {

    private static final long serialVersionUID = 2743671456845806194L;

    private String id;

    /**
     * 中心机构编码
     */
    private String unit_no;
    /**
     * 机构名称
     */
    private String unit_name;
    /**
     * 上级机构编码
     */
    private String parent_unit_no;
    /**
     * 中心节点号
     */
    private String node;

    public CenterInfoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getParent_unit_no() {
        return parent_unit_no;
    }

    public void setParent_unit_no(String parent_unit_no) {
        this.parent_unit_no = parent_unit_no;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
