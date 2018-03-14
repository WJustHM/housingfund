package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/12/9.
 */
@Entity
@Table(name = "c_change_notice_to_unit")
@org.hibernate.annotations.Table(appliesTo = "c_change_notice_to_unit", comment = "单位未分摊余额记录表")
public class CChangeNoticeToUnit extends Common implements Serializable {

    private static final long serialVersionUID = -1895046026269527630L;

    @Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
    private String dwzh;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "notice", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '账户资金变动通知表'")
    private CBankAccChangeNotice cBankAccChangeNotice;

    public CChangeNoticeToUnit() {
    }

    public CChangeNoticeToUnit(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                               String dwzh, CBankAccChangeNotice cBankAccChangeNotice) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.dwzh = dwzh;
        this.cBankAccChangeNotice = cBankAccChangeNotice;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.updated_at = new Date();
        this.dwzh = dwzh;
    }

    public CBankAccChangeNotice getcBankAccChangeNotice() {
        return cBankAccChangeNotice;
    }

    public void setcBankAccChangeNotice(CBankAccChangeNotice cBankAccChangeNotice) {
        this.updated_at = new Date();
        this.cBankAccChangeNotice = cBankAccChangeNotice;
    }
}
