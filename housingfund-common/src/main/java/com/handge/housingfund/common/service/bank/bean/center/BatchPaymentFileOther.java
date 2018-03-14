package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量付款文件,跨行和混合
 * 文件格式:序号@|$金额@|$收方账号@|$收方户名@|$收方账户联行号@|$收款人开户行联行号@|$收款人地址@|$摘要@|$业务明细账号@|$业务明细流水号 1@|$业务明细流水号 2@|$本金发生额@|$利息发生额@|$
 * Created by gxy on 17-8-2.
 */
public class BatchPaymentFileOther extends BatchPaymentFileSelf implements Serializable {

    private static final long serialVersionUID = 7986892558232374087L;
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

    public BatchPaymentFileOther() {
    }

    public BatchPaymentFileOther(String no, BigDecimal amt, String crAcctNo, String crAcctName, String crChgNo, String summary, String refSeqNo1, BigDecimal capAmt) {
        super(no, amt, crAcctNo, crAcctName, summary, refSeqNo1, capAmt);
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

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                crAcctNo + "@|$" +
                crAcctName + "@|$" +
                crChgNo + "@|$" +
                crBankChgNo + "@|$" +
                address + "@|$" +
                summary + "@|$" +
                refAcctNo + "@|$" +
                refSeqNo1 + "@|$" +
                refSeqNo2 + "@|$" +
                getString(capAmt) + "@|$" +
                getString(intAmt) + "@|$";
    }
}
