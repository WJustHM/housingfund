package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/10/10.
 */
@Entity
@Table(name = "c_finance_report")
@org.hibernate.annotations.Table(appliesTo = "c_finance_report", comment = "财务报表")
public class CFinanceReport extends Common implements Serializable {

    private static final long serialVersionUID = -7183773828147113806L;

    @Column(name = "BBLX", columnDefinition = "VARCHAR(4) NOT NULL COMMENT '报表类型'")
    private String bblx;

    @Column(name = "NAME", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '报表名字'")
    private String name;

    @Column(name = "BBQJ", columnDefinition = "VARCHAR(40) NOT NULL COMMENT '报表期间'")
    private String bbqj;

    @Column(name = "BBSJ", columnDefinition = "TEXT NOT NULL COMMENT '报表数据'")
    private String bbsj;

    public CFinanceReport() {
    }

    public CFinanceReport(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                          String bblx,String name , String bbqj, String bbsj) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.bblx = bblx;
        this.bbqj = bbqj;
        this.bbsj = bbsj;
        this.name = name;
    }

    public String getBblx() {
        return bblx;
    }

    public void setBblx(String bblx) {
        this.updated_at = new Date();
        this.bblx = bblx;
    }

    public String getBbqj() {
        return bbqj;
    }

    public void setBbqj(String bbqj) {
        this.updated_at = new Date();
        this.bbqj = bbqj;
    }

    public String getBbsj() {
        return bbsj;
    }

    public void setBbsj(String bbsj) {
        this.updated_at = new Date();
        this.bbsj = bbsj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
