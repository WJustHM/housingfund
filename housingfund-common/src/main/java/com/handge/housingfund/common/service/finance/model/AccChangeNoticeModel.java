package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/12.
 */
@XmlRootElement(name = "账户变动通知")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccChangeNoticeModel implements Serializable {

    private static final long serialVersionUID = -7093163413197237374L;
    private String id;
    //通知编号
    private String notice_no;

    //账户账号(ZHHM)
    private String acct;

    //银行名称(YHMC)
    private String yhmc;

    //银行主机流水号
    private String host_seq_no;

    //银行交易代码
    private String tx_code;

    //交易对手账号(DSZH)
    private String opponent_acct;

    //交易对手户名
    private String opponent_name;

    //交易金额
    private String amt;

    //交易日期(JYRQ),格式:YYYYMMDD
    private String date;

    //交易时间,格式:HHMMSS
    private String time;

    //可用金额
    private String available_amt;

    //开户机构号
    private String open_bank_no;

    //备注
    private String remark;

    //币种
    private String curr_no;

    //钞汇
    private String curr_iden;

    //余额
    private String balance;

    //可透支余额
    private String overdraft;

    //凭证种类
    private String voucher_type;

    //凭证号码
    private String voucher_no;

    //交易对手行号
    private String opponent_bank_no;

    //摘要
    private String summary;

    //冲正标识
    private String redo;

    //笔号
    private String book_list_no;

    //册号
    private String book_no;

    //是否已做账(SFZZ): 0:否,1:是
    private String is_make_acc;

    //业务流水号
    private String bus_seq_no;

    //记账凭证号
    private String jzpzh;

    private String smwj;//扫描文件

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotice_no() {
        return notice_no;
    }

    public void setNotice_no(String notice_no) {
        this.notice_no = notice_no;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }

    public String getHost_seq_no() {
        return host_seq_no;
    }

    public void setHost_seq_no(String host_seq_no) {
        this.host_seq_no = host_seq_no;
    }

    public String getTx_code() {
        return tx_code;
    }

    public void setTx_code(String tx_code) {
        this.tx_code = tx_code;
    }

    public String getOpponent_acct() {
        return opponent_acct;
    }

    public void setOpponent_acct(String opponent_acct) {
        this.opponent_acct = opponent_acct;
    }

    public String getOpponent_name() {
        return opponent_name;
    }

    public void setOpponent_name(String opponent_name) {
        this.opponent_name = opponent_name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
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

    public String getAvailable_amt() {
        return available_amt;
    }

    public void setAvailable_amt(String available_amt) {
        this.available_amt = available_amt;
    }

    public String getOpen_bank_no() {
        return open_bank_no;
    }

    public void setOpen_bank_no(String open_bank_no) {
        this.open_bank_no = open_bank_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurr_no() {
        return curr_no;
    }

    public void setCurr_no(String curr_no) {
        this.curr_no = curr_no;
    }

    public String getCurr_iden() {
        return curr_iden;
    }

    public void setCurr_iden(String curr_iden) {
        this.curr_iden = curr_iden;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(String overdraft) {
        this.overdraft = overdraft;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type = voucher_type;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public void setVoucher_no(String voucher_no) {
        this.voucher_no = voucher_no;
    }

    public String getOpponent_bank_no() {
        return opponent_bank_no;
    }

    public void setOpponent_bank_no(String opponent_bank_no) {
        this.opponent_bank_no = opponent_bank_no;
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

    public String getBook_list_no() {
        return book_list_no;
    }

    public void setBook_list_no(String book_list_no) {
        this.book_list_no = book_list_no;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getIs_make_acc() {
        return is_make_acc;
    }

    public void setIs_make_acc(String is_make_acc) {
        this.is_make_acc = is_make_acc;
    }

    public String getBus_seq_no() {
        return bus_seq_no;
    }

    public void setBus_seq_no(String bus_seq_no) {
        this.bus_seq_no = bus_seq_no;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.jzpzh = jzpzh;
    }

    public String getSmwj() {
        return smwj;
    }

    public void setSmwj(String smwj) {
        this.smwj = smwj;
    }
}
