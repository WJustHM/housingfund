package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活期转定期(BDC114)输入格式
 */
public class Actived2FixedIn implements Serializable {
    private static final long serialVersionUID = 2874205979776161742L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 币种(required)
     */
    private String CurrNo;
    /**
     * 钞汇鉴别
     */
    private String CurrIden;
    /**
     * 活期账号(required)
     */
    private String ActivedAcctNo;
    /**
     * 活期户名(required)
     */
    private String ActivedAcctName;
    /**
     *定期账号
     */
    private String FixedAcctNo;
    /**
     * 定期户名
     */
    private String FixedAcctName;
    /**
     * 存期(required)
     */
    private String DepositPeriod;
    /**
     * 利率
     */
    private BigDecimal InterestRate;
    /**
     * 交易金额(required)
     */
    private BigDecimal Amt;
    /**
     * 转存方式(required)
     */
    private String ExtendDepositType;
    /**
     * 利息转存转入账号
     */
    private String PartExtendDepositAcctNo;
    /**
     * 备注
     */
    private String Remark;

    public Actived2FixedIn() {
    }

    public Actived2FixedIn(CenterHeadIn centerHeadIn, String CurrNo, String ActivedAcctNo, String ActivedAcctName, String DepositPeriod, BigDecimal Amt,
                           String ExtendDepositType){
        this.centerHeadIn = centerHeadIn;
        this.CurrNo = CurrNo;
        this.ActivedAcctNo = ActivedAcctNo;
        this.ActivedAcctName = ActivedAcctName;
        this.DepositPeriod = DepositPeriod;
        this.Amt = Amt;
        this.ExtendDepositType = ExtendDepositType;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getCurrNo() {
        return CurrNo;
    }

    public void setCurrNo(String currNo) {
        CurrNo = currNo;
    }

    public String getCurrIden() {
        return CurrIden;
    }

    public void setCurrIden(String currIden) {
        CurrIden = currIden;
    }

    public String getActivedAcctNo() {
        return ActivedAcctNo;
    }

    public void setActivedAcctNo(String activedAcctNo) {
        ActivedAcctNo = activedAcctNo;
    }

    public String getActivedAcctName() {
        return ActivedAcctName;
    }

    public void setActivedAcctName(String activedAcctName) {
        ActivedAcctName = activedAcctName;
    }

    public String getFixedAcctNo() {
        return FixedAcctNo;
    }

    public void setFixedAcctNo(String fixedAcctNo) {
        FixedAcctNo = fixedAcctNo;
    }

    public String getFixedAcctName() {
        return FixedAcctName;
    }

    public void setFixedAcctName(String fixedAcctName) {
        FixedAcctName = fixedAcctName;
    }

    public String getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    public BigDecimal getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        InterestRate = interestRate;
    }

    public BigDecimal getAmt() {
        return Amt;
    }

    public void setAmt(BigDecimal amt) {
        Amt = amt;
    }

    public String getExtendDepositType() {
        return ExtendDepositType;
    }

    public void setExtendDepositType(String extendDepositType) {
        ExtendDepositType = extendDepositType;
    }

    public String getPartExtendDepositAcctNo() {
        return PartExtendDepositAcctNo;
    }

    public void setPartExtendDepositAcctNo(String partExtendDepositAcctNo) {
        PartExtendDepositAcctNo = partExtendDepositAcctNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return "Actived2FixedIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", ActivedAcctNo='" + ActivedAcctNo + '\'' +
                ", ActivedAcctName='" + ActivedAcctName + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                ", InterestRate=" + InterestRate +
                ", Amt=" + Amt +
                ", ExtendDepositType='" + ExtendDepositType + '\'' +
                ", PartExtendDepositAcctNo='" + PartExtendDepositAcctNo + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
