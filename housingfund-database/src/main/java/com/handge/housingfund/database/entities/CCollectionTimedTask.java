package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 凡 on 2017/9/9.
 * 针对归集业务：
 * 启封、封存、调基调比每月(?)执行一次
 * 催缴每天执行,不计该表
 */
@Entity
@Table(name = "c_collection_timed_task")
@org.hibernate.annotations.Table(appliesTo = "c_collection_timed_task", comment = "归集定时任务表")
public class CCollectionTimedTask extends Common{

    private static final long serialVersionUID = 1L;

    @Column(name = "YWLX", columnDefinition = "char(2) DEFAULT NULL COMMENT '业务类型:启封04、封存05、调基10、调比75'")
    private String ywlx;
    @Column(name = "YWLSH", columnDefinition = "varchar(20) DEFAULT NULL COMMENT '业务流水号'")
    private String ywlsh;
    @Column(name = "YWMS", columnDefinition = "varchar(100) DEFAULT NULL COMMENT '业务描述'")
    private String ywms;
    @Column(name = "ZXSJ", columnDefinition = "varchar(20) DEFAULT NULL COMMENT '执行时间'")
    private String zxsj;
    @Column(name = "ZXZT", columnDefinition = "char(2) DEFAULT NULL COMMENT '执行状态,00未执行，01执行成功，02执行失败'")
    private String zxzt;
    @Column(name = "ZXCS", columnDefinition = "integer(11) DEFAULT 0 COMMENT '执行次数'")
    private int zxcs;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public String getYwms() {
        return ywms;
    }

    public void setYwms(String ywms) {
        this.ywms = ywms;
    }

    public String getZxsj() {
        return zxsj;
    }

    public void setZxsj(String zxsj) {
        this.zxsj = zxsj;
    }

    public String getZxzt() {
        return zxzt;
    }

    public void setZxzt(String zxzt) {
        this.zxzt = zxzt;
    }

    public int getZxcs() {
        return zxcs;
    }

    public void setZxcs(int zxcs) {
        this.zxcs = zxcs;
    }

    public CCollectionTimedTask() {
    }

    public CCollectionTimedTask(String id, Date createdAt, boolean deleted, Date deletedAt, Date updatedAt,
                                String ywlx, String ywlsh, String ywms, String zxsj, String zxzt, int zxcs) {
        this.id = id;
        this.created_at = createdAt;
        this.deleted = deleted;
        this.deleted_at = deletedAt;
        this.updated_at = updatedAt;
        this.ywlx = ywlx;
        this.ywlsh = ywlsh;
        this.ywms = ywms;
        this.zxsj = zxsj;
        this.zxzt = zxzt;
        this.zxcs = zxcs;
    }
}
