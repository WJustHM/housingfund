package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 活期转定期(BDC114)输出格式
 */
public class Actived2FixedOut implements Serializable {
    private static final long serialVersionUID = -3301672298104103991L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 定期账号(required)
     */
    private String FixedAcctNo;
    /**
     * 定期户名(required)
     */
    private String FixedAcctName;
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

    public Actived2FixedOut() {
    }

    public Actived2FixedOut(CenterHeadOut centerHeadOut, String FixedAcctNo, String FixedAcctName, String HostSeqNo){
        this.centerHeadOut = centerHeadOut;
        this.FixedAcctNo = FixedAcctNo;
        this.FixedAcctName = FixedAcctName;
        this.HostSeqNo = HostSeqNo;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
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
        return "Actived2FixedOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
