package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "住房公积金银行存款情况表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingBankDepositDetail implements Serializable {

    private String CKJRJG;//存款金融机构
    private String ZHXZ;//专户性质
    private String CKLX;//专户类型
    private String ZJYE;//资金余额
    private String LiLv;//利率

    public HousingBankDepositDetail() {
    }

    public HousingBankDepositDetail(String CKJRJG, String ZHXZ, String CKLX, String ZJYE, String liLv) {
        this.CKJRJG = CKJRJG;
        this.ZHXZ = ZHXZ;
        this.CKLX = CKLX;
        this.ZJYE = ZJYE;
        LiLv = liLv;
    }

    public String getCKJRJG() {
        return CKJRJG;
    }

    public void setCKJRJG(String CKJRJG) {
        this.CKJRJG = CKJRJG;
    }

    public String getZHXZ() {
        return ZHXZ;
    }

    public void setZHXZ(String ZHXZ) {
        this.ZHXZ = ZHXZ;
    }

    public String getCKLX() {
        return CKLX;
    }

    public void setCKLX(String CKLX) {
        this.CKLX = CKLX;
    }

    public String getZJYE() {
        return ZJYE;
    }

    public void setZJYE(String ZJYE) {
        this.ZJYE = ZJYE;
    }

    public String getLiLv() {
        return LiLv;
    }

    public void setLiLv(String liLv) {
        LiLv = liLv;
    }
}
