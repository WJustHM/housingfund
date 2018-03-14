package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 活期转通知存款(BDC117)输出格式
 */
public class Actived2NoticeDepositOut implements Serializable {
    private static final long serialVersionUID = 7928809293459667556L;
    /**
     * CenterHeadOut
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 通知存款账号(required)
     */
    private String AcctNo;
    /**
     * 通知账户户名(required)
     */
    private String AcctName;
    /**
     * 册号
     */
    private String BookNo;
    /**
     * 笔号
     */
    private int BookListNo;
    /**
     * 银行主机流水号(required)
     */
    private String HostSeqNo;

    public Actived2NoticeDepositOut() {
    }

    public Actived2NoticeDepositOut(CenterHeadOut centerHeadOut, String AcctNo, String AcctName, String HostSeqNo){
        this.centerHeadOut = centerHeadOut;
        this.AcctNo = AcctNo;
        this.AcctName = AcctName;
        this.HostSeqNo = HostSeqNo;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
    }

    public String getAcctName() {
        return AcctName;
    }

    public void setAcctName(String acctName) {
        AcctName = acctName;
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

    public String getHostSeqNo() {
        return HostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        HostSeqNo = hostSeqNo;
    }

    @Override
    public String toString() {
        return "Actived2NoticeDepositOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", AcctNo='" + AcctNo + '\'' +
                ", AcctName='" + AcctName + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
