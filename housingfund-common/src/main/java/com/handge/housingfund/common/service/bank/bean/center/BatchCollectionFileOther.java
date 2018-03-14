package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量收款文件,跨行和混合
 * 文件格式:序号@|$金额@|$付方账号@|$付方户名@|$付方联行号@|$付方开户行联行号@|$付方地址(可选)@|$多方协议号@|$摘要@|$业务明细账号@|$业务明细流水号@|$
 * Created by gxy on 17-8-3.
 */
public class BatchCollectionFileOther extends BatchCollectionFileSelf implements Serializable {

    private static final long serialVersionUID = 8351542472698039420L;
    /**
     * 收方账户联行号(required)
     */
    private String crChgNo = "";
    /**
     * 收款人开户行联行号
     */
    private String crBankChgNo = "";
    /**
     * 收款人地址
     */
    private String address = "";
    /**
     * 多方协议号
     */
    private String conAgrNo = "";

    public BatchCollectionFileOther() {
    }

    public BatchCollectionFileOther(String no, BigDecimal amt, String deAcctNo, String deAcctName, String crChgNo, String summary) {
        super(no, amt, deAcctNo, deAcctName, summary);
        this.crChgNo = crChgNo;
    }

    public String getCrChgNo() {
        return crChgNo;
    }

    public void setCrChgNo(String crChgNo) {
        this.crChgNo = crChgNo;
    }

    public String getCrBankChgNo() {
        return crBankChgNo;
    }

    public void setCrBankChgNo(String crBankChgNo) {
        this.crBankChgNo = crBankChgNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConAgrNo() {
        return conAgrNo;
    }

    public void setConAgrNo(String conAgrNo) {
        this.conAgrNo = conAgrNo;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                deAcctNo + "@|$" +
                deAcctName + "@|$" +
                crChgNo + "@|$" +
                crBankChgNo + "@|$" +
                address + "@|$" +
                conAgrNo + "@|$" +
                summary + "@|$" +
                refAcctNo + "@|$" +
                refSeqNo + "@|$";
    }
}
