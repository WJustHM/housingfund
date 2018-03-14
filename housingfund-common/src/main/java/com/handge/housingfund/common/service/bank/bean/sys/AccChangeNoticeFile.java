package com.handge.housingfund.common.service.bank.bean.sys;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 到账通知文件,可用于解析账户交易明细查询(BDC113)和账户变动通知(SBDC100)的文件内容
 * 文件格式: 序号|帐号|银行主机流水号交易代码|交易对手账号|交易对手户名|金额|交易日期|交易时间|可用金额|开户机构号|备注|币别|钞汇鉴别|余额|可透支余额|凭证种类|凭证号码|交易对手行号|摘要|冲正标识|笔号|册号|
 * Created by gxy on 17-8-4.
 */
public class AccChangeNoticeFile implements Serializable {

    private static final long serialVersionUID = -880345084962022066L;
    /**
     * 序号/业务流水号
     */
    private String no;
    /**
     * 账号
     */
    private String acct;
    /**
     * 银行主机流水号
     */
    private String hostSeqNo;
    /**
     * 交易代码
     */
    private String txCode;
    /**
     * 交易对手账号
     */
    private String opponentAcct;
    /**
     * 交易对手户名
     */
    private String opponentName;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 交易日期
     */
    private String date;
    /**
     * 交易时间
     */
    private String time;
    /**
     * 可用金额
     */
    private BigDecimal availableAmt;
    /**
     * 开户机构号
     */
    private String openBankNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 币种
     */
    private String CurrNo;
    /**
     * 钞汇
     */
    private String CurrIden;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 可透支余额
     */
    private BigDecimal overdraft;
    /**
     * 凭证种类
     */
    private String voucherType;
    /**
     * 凭证号码
     */
    private String voucherNo;
    /**
     * 交易对手行号
     */
    private String opponentBankNo;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 冲正标识
     */
    private String redo;
    /**
     * 笔号
     */
    private String bookListNo;
    /**
     * 册号
     */
    private String bookNo;

    private String smwj;//扫描文件

    public AccChangeNoticeFile() {
    }

    public String getSmwj() {
        return smwj;
    }

    public void setSmwj(String smwj) {
        this.smwj = smwj;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getHostSeqNo() {
        return hostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        this.hostSeqNo = hostSeqNo;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public String getOpponentAcct() {
        return opponentAcct;
    }

    public void setOpponentAcct(String opponentAcct) {
        this.opponentAcct = opponentAcct;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(BigDecimal availableAmt) {
        this.availableAmt = availableAmt;
    }

    public String getOpenBankNo() {
        return openBankNo;
    }

    public void setOpenBankNo(String openBankNo) {
        this.openBankNo = openBankNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getOpponentBankNo() {
        return opponentBankNo;
    }

    public void setOpponentBankNo(String opponentBankNo) {
        this.opponentBankNo = opponentBankNo;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRedo() {
        return redo;
    }

    public void setRedo(String redo) {
        this.redo = redo;
    }

    public String getBookListNo() {
        return bookListNo;
    }

    public void setBookListNo(String bookListNo) {
        this.bookListNo = bookListNo;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

//    @Override
//    public String toString() {
//        return "ArrivalNoticeFile{" +
//                "no='" + no + '\'' +
//                ", acct='" + acct + '\'' +
//                ", hostSeqNo='" + hostSeqNo + '\'' +
//                ", txCode='" + txCode + '\'' +
//                ", opponentAcct='" + opponentAcct + '\'' +
//                ", opponentName='" + opponentName + '\'' +
//                ", amt=" + amt +
//                ", date='" + date + '\'' +
//                ", time='" + time + '\'' +
//                ", availableAmt=" + availableAmt +
//                ", openBankNo='" + openBankNo + '\'' +
//                ", remark='" + remark + '\'' +
//                ", CurrNo='" + CurrNo + '\'' +
//                ", CurrIden='" + CurrIden + '\'' +
//                ", balance=" + balance +
//                ", overdraft=" + overdraft +
//                ", voucherType='" + voucherType + '\'' +
//                ", voucherNo='" + voucherNo + '\'' +
//                ", opponentBankNo='" + opponentBankNo + '\'' +
//                ", summary='" + summary + '\'' +
//                ", redo='" + redo + '\'' +
//                ", bookListNo='" + bookListNo + '\'' +
//                ", bookNo='" + bookNo + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return no + "|" +
                acct + "|" +
                hostSeqNo + "|" +
                txCode + "|" +
                opponentAcct + "|" +
                opponentName + "|" +
                amt + "|" +
                date + "|" +
                time + "|" +
                availableAmt + "|" +
                openBankNo + "|" +
                remark + "|" +
                CurrNo + "|" +
                CurrIden + "|" +
                balance + "|" +
                overdraft + "|" +
                voucherType + "|" +
                voucherNo + "|" +
                opponentBankNo + "|" +
                summary + "|" +
                redo + "|" +
                bookListNo + "|" +
                bookNo + "|";
    }
}
