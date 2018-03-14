package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 定期转存方式设定(BDC116)输入格式
 */
public class FixedTransferModeSetIn implements Serializable {
    private static final long serialVersionUID = 7858227062337174815L;
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
     * 定期账号(required)
     */
    private String FixedAcctNo;
    /**
     * 册号
     */
    private String BookNo;
    /**
     * 笔号
     */
    private int BookListNo;
    /**
     * 自动转存方式(required)
     */
    private String ExtendDepositType;
    /**
     * 利息转存转入账号
     */
    private String PartExtendDepositAcctNo;

    public FixedTransferModeSetIn() {
    }

    public FixedTransferModeSetIn(CenterHeadIn centerHeadIn, String currNo, String fixedAcctNo, String extendDepositType) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        FixedAcctNo = fixedAcctNo;
        ExtendDepositType = extendDepositType;
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

    public String getFixedAcctNo() {
        return FixedAcctNo;
    }

    public void setFixedAcctNo(String fixedAcctNo) {
        FixedAcctNo = fixedAcctNo;
    }

    public String getBookNo() {
        return BookNo;
    }

    public void setBookNo(String bookNo) {
        BookNo = bookNo;
    }

    public int getBookListNo() {
        return BookListNo;
    }

    public void setBookListNo(int bookListNo) {
        BookListNo = bookListNo;
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

    @Override
    public String toString() {
        return "FixedTransferModeSetIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", ExtendDepositType='" + ExtendDepositType + '\'' +
                ", PartExtendDepositAcctNo='" + PartExtendDepositAcctNo + '\'' +
                '}';
    }
}
