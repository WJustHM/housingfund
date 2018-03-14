package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 通知存款支取通知查询(BDC121)输入格式
 */
public class NoticeDepositDrawQueryIn implements Serializable {
    private static final long serialVersionUID = 8679760306727900232L;
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
     * 通知存款账号(required)
     */
    private String AcctNo;
    /**
     * 册号
     */
    private String BookNo;
    /**
     * 笔号
     */
    private int BookListNo;

    public NoticeDepositDrawQueryIn() {
    }

    public NoticeDepositDrawQueryIn(CenterHeadIn centerHeadIn, String currNo, String acctNo) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        AcctNo = acctNo;
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

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
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

    @Override
    public String toString() {
        return "NoticeDepositDrawQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", AcctNo='" + AcctNo + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                '}';
    }
}
