package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/12/8.
 */

@Entity
@Table(name = "c_collection_allochthounous_tansfer_process")
@org.hibernate.annotations.Table(appliesTo = "c_collection_allochthounous_tansfer_process", comment = "个人转移接续业务流程表")
public class CCollectionAllochthounousTransferProcess extends Common implements Serializable{
    private static final long serialVersionUID = -7084225481941148050L;

    @Column(name = "CaoZuo", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作'")
    String CaoZuo;
    @Column(name = "CZJG", columnDefinition = "VARCHAR(240) DEFAULT NULL COMMENT '操作机构'")
    String CZJG;
    @Column(name = "CZRY", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作人员'")
    String CZRY;
    @Column(name = "CZSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '操作时间'")
    Date CZSJ;
    @Column(name = "CZYJ", columnDefinition = "TEXT DEFAULT NULL COMMENT '操作意见'")
    String CZYJ;
    @Column(name = "CZHZT", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作后状态'")
    String CZHZT;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transferVice", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流程记录'")
    CCollectionAllochthounousTransferVice transferVice;

    public CCollectionAllochthounousTransferProcess() {
    }

    public CCollectionAllochthounousTransferProcess(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                                    String caoZuo, String CZJG, String CZRY, Date CZSJ, String CZYJ, String CZHZT,
                                                    CCollectionAllochthounousTransferVice transferVice) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        CaoZuo = caoZuo;
        this.CZJG = CZJG;
        this.CZRY = CZRY;
        this.CZSJ = CZSJ;
        this.CZYJ = CZYJ;
        this.CZHZT = CZHZT;
        this.transferVice = transferVice;
    }

    public CCollectionAllochthounousTransferVice getTransferVice() {
        return transferVice;
    }

    public void setTransferVice(CCollectionAllochthounousTransferVice transferVice) {
        this.transferVice = transferVice;
    }

    public String getCaoZuo() {
        return CaoZuo;
    }

    public void setCaoZuo(String caoZuo) {
        CaoZuo = caoZuo;
    }

    public String getCZJG() {
        return CZJG;
    }

    public void setCZJG(String CZJG) {
        this.CZJG = CZJG;
    }

    public String getCZRY() {
        return CZRY;
    }

    public void setCZRY(String CZRY) {
        this.CZRY = CZRY;
    }

    public Date getCZSJ() {
        return CZSJ;
    }

    public void setCZSJ(Date CZSJ) {
        this.CZSJ = CZSJ;
    }

    public String getCZYJ() {
        return CZYJ;
    }

    public void setCZYJ(String CZYJ) {
        this.CZYJ = CZYJ;
    }

    public String getCZHZT() {
        return CZHZT;
    }

    public void setCZHZT(String CZHZT) {
        this.CZHZT = CZHZT;
    }
}
