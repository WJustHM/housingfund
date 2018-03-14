package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_time_task")
@org.hibernate.annotations.Table(appliesTo = "c_time_task", comment = "定时任务日志表")
public class CTimeTask extends Common implements Serializable {

    private static final long serialVersionUID = 6163771137282437221L;

    @Column(name = "module", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '模块'")
    private String module;

    @Column(name = "sub_module", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '子模块'")
    private String subModule;

    @Column(name = "ZXSJ", columnDefinition = "datetime DEFAULT NULL COMMENT '执行时间'")
    private Date ZXSJ;

    @Column(name = "SFCG", columnDefinition = "bit(1) NOT NULL DEFAULT b'0' COMMENT '是否成功'")
    private boolean SFCG;

    @Column(name = "SBYY", columnDefinition = "TEXT DEFAULT NULL COMMENT '失败原因'")
    private String SBYY;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public Date getZXSJ() {
        return ZXSJ;
    }

    public void setZXSJ(Date ZXSJ) {
        this.ZXSJ = ZXSJ;
    }

    public boolean getSFCG() {
        return SFCG;
    }

    public void setSFCG(boolean SFCG) {
        this.SFCG = SFCG;
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }
}
