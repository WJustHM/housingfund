package com.handge.housingfund.common.service.bank.bean.transfer;

import java.io.Serializable;

/**
 * 中心信息
 * 文件格式： 序号|机构代码|机构名称|上级机构代码|中心节点号|
 * Created by gxy on 17-12-8.
 */
public class CenterInfoFile implements Serializable {

    private static final long serialVersionUID = 556648679410579791L;


    /**
     * 序号
     */
    private String no;
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

    public CenterInfoFile() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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
