package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/4.
 */
@Entity
@Table(name = "c_bank_nodeinfo")
@org.hibernate.annotations.Table(appliesTo = "c_bank_nodeinfo", comment = "结算平台-银行节点信息表")
public class CBankNodeInfo extends Common implements Serializable {

    private static final long serialVersionUID = -2856312931972646348L;

    @Column(name = "bank_name", columnDefinition = "VARCHAR(60) DEFAULT NULL COMMENT '银行名称'")
    private String bank_name;

    @Column(name = "code", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行编码'")
    private String code;

    @Column(name = "node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行节点号'")
    private String node;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
       this.updated_at = new Date();
        this.bank_name = bank_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
       this.updated_at = new Date();
        this.code = code;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
       this.updated_at = new Date();
        this.node = node;
    }
}
