package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 联行号查询(BDC112)输入格式
 */
public class ChgNoQueryIn implements Serializable {
    private static final long serialVersionUID = -48444269484850726L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 银行编码(required)
     */
    private String BankNo;
    /**
     * 地区编号(required)
     */
    private String AreaNo;

    public ChgNoQueryIn() {
    }

    public ChgNoQueryIn(CenterHeadIn centerHeadIn, String bankNo, String areaNo) {
        this.centerHeadIn = centerHeadIn;
        BankNo = bankNo;
        AreaNo = areaNo;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getBankNo() {
        return BankNo;
    }

    public void setBankNo(String bankNo) {
        BankNo = bankNo;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }

    @Override
    public String toString() {
        return "ChgNoQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", BankNo='" + BankNo + '\'' +
                ", AreaNo='" + AreaNo + '\'' +
                '}';
    }
}
