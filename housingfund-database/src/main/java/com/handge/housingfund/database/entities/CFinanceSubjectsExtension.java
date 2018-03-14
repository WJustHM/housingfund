package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/22.
 */
@Entity
@Table(name = "c_finance_subjects_extension")
@org.hibernate.annotations.Table(appliesTo = "c_finance_subjects_extension", comment = "科目信息 扩展表")
public class CFinanceSubjectsExtension extends Common implements Serializable {

    private static final long serialVersionUID = -4661634492308898666L;

    @Column(name = "SJKM", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '上级科目'")
    private String sjkm;
    @Column(name = "KMJM", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '科目简码'")
    private String kmjm;
    @Column(name = "KMXZ", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目性质'")
    private String kmxz;
    @Column(name = "type", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目属性（01：收入、02：支出）'")
    private String type;
    @Column(name = "CZKZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '赤字控制'")
    private String czkz;
    @Column(name = "SFYSY", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否已使用'")
    private Boolean sfysy;
    @Column(name = "SFMR", columnDefinition = "BIT(1) DEFAULT 1 COMMENT '是否默认'")
    private Boolean sfmr;
    @Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String beizhu;

    public CFinanceSubjectsExtension() {
    }

    public CFinanceSubjectsExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                     String sjkm, String kmjm, String kmxz, String type, String czkz,
                                     String beizhu, Boolean sfysy, Boolean sfmr) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.sjkm = sjkm;
        this.kmjm = kmjm;
        this.kmxz = kmxz;
        this.type = type;
        this.czkz = czkz;
        this.beizhu = beizhu;
        this.sfysy = sfysy;
        this.sfmr = sfmr;
    }

    public String getKmxz() {
        return kmxz;
    }

    public void setKmxz(String kmxz) {
        this.kmxz = kmxz;
    }

    public String getSjkm() {
        return sjkm;
    }

    public void setSjkm(String sjkm) {
        this.sjkm = sjkm;
    }

    public String getKmjm() {
        return kmjm;
    }

    public void setKmjm(String kmjm) {
        this.kmjm = kmjm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCzkz() {
        return czkz;
    }

    public void setCzkz(String czkz) {
        this.czkz = czkz;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public Boolean getSfysy() {
        return sfysy;
    }

    public void setSfysy(Boolean sfysy) {
        this.sfysy = sfysy;
    }

    public Boolean getSfmr() {
        return sfmr;
    }

    public void setSfmr(Boolean sfmr) {
        this.sfmr = sfmr;
    }

}
