package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gxy on 17-8-16.
 */
@Entity
@Table(name = "c_bank_acc_change_notice")
@org.hibernate.annotations.Table(appliesTo = "c_bank_acc_change_notice", comment = "结算平台-账户资金变动通知表")
public class CBankAccChangeNotice extends Common implements Serializable {

    private static final long serialVersionUID = -4245528383401197518L;

    @Column(name = "notice_no", columnDefinition = "VARCHAR(30) NOT NULL COMMENT '通知编号'")
    private String notice_no;

    @Column(name = "acct", columnDefinition = "VARCHAR(32) NOT NULL COMMENT '账户账号'")
    private String acct;

    @Column(name = "host_seq_no", columnDefinition = "VARCHAR(32) NOT NULL COMMENT '银行主机流水号'")
    private String host_seq_no;

    @Column(name = "tx_code", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行交易代码'")
    private String tx_code;

    @Column(name = "opponent_acct", columnDefinition = "VARCHAR(30) NOT NULL COMMENT '交易对手账号'")
    private String opponent_acct;

    @Column(name = "opponent_name", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '交易对手户名'")
    private String opponent_name;

    @Column(name = "amt", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '交易金额'")
    private BigDecimal amt = BigDecimal.ZERO;

    @Column(name = "date", columnDefinition = "DATETIME NOT NULL COMMENT '交易日期,格式:YYYYMMDD'")
    private Date date;

    @Column(name = "time", columnDefinition = "VARCHAR(6) NOT NULL COMMENT '交易时间,格式:HHMMSS'")
    private String time;

    @Column(name = "available_amt", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '可用金额'")
    private BigDecimal available_amt = BigDecimal.ZERO;

    @Column(name = "open_bank_no", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '开户机构号'")
    private String open_bank_no;

    @Column(name = "remark", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String remark;

    @Column(name = "curr_no", columnDefinition = "VARCHAR(3) NOT NULL COMMENT '币种'")
    private String curr_no;

    @Column(name = "curr_iden", columnDefinition = "VARCHAR(1) NOT NULL COMMENT '钞汇'")
    private String curr_iden;

    @Column(name = "balance", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '余额'")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "overdraft", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '可透支余额'")
    private BigDecimal overdraft = BigDecimal.ZERO;

    @Column(name = "voucher_type", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '凭证种类'")
    private String voucher_type;

    @Column(name = "voucher_no", columnDefinition = "VARCHAR(16) DEFAULT NULL COMMENT '凭证号码'")
    private String voucher_no;

    @Column(name = "opponent_bank_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '交易对手行号'")
    private String opponent_bank_no;

    @Column(name = "summary", columnDefinition = "TEXT NOT NULL COMMENT '摘要'")
    private String summary;

    @Column(name = "redo", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '冲正标识'")
    private String redo;

    @Column(name = "book_list_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '笔号'")
    private String book_list_no;

    @Column(name = "book_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '册号'")
    private String book_no;

    @Column(name = "is_make_acc", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '是否已做账: 0:否,1:是,null:自动对账业务'")
    private String is_make_acc;

    @Column(name = "bus_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流水号'")
    private String bus_seq_no;

    @Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
    private String jzpzh;

    @Column(name = "SMWJ", columnDefinition = "TEXT DEFAULT NULL COMMENT '扫描文件'")
    private String smwj;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cFinanceRecordingVoucher", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '记账凭证信息'")
    private CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension;

    public CBankAccChangeNotice() {
        super();

    }

    public String getSmwj() {
        return smwj;
    }

    public void setSmwj(String smwj) {
        this.updated_at = new Date();
        this.smwj = smwj;
    }

    public String getNotice_no() {
        return notice_no;
    }

    public void setNotice_no(String notice_no) {
        this.updated_at = new Date();
        this.notice_no = notice_no;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.updated_at = new Date();
        this.acct = acct;
    }

    public String getHost_seq_no() {
        return host_seq_no;
    }

    public void setHost_seq_no(String host_seq_no) {
        this.updated_at = new Date();
        this.host_seq_no = host_seq_no;
    }

    public String getTx_code() {
        return tx_code;
    }

    public void setTx_code(String tx_code) {
        this.updated_at = new Date();
        this.tx_code = tx_code;
    }

    public String getOpponent_acct() {
        return opponent_acct;
    }

    public void setOpponent_acct(String opponent_acct) {
        this.updated_at = new Date();
        this.opponent_acct = opponent_acct;
    }

    public String getOpponent_name() {
        return opponent_name;
    }

    public void setOpponent_name(String opponent_name) {
        this.updated_at = new Date();
        this.opponent_name = opponent_name;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.updated_at = new Date();
        this.amt = amt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.updated_at = new Date();
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.updated_at = new Date();
        this.time = time;
    }

    public BigDecimal getAvailable_amt() {
        return available_amt;
    }

    public void setAvailable_amt(BigDecimal available_amt) {
        this.updated_at = new Date();
        this.available_amt = available_amt;
    }

    public String getOpen_bank_no() {
        return open_bank_no;
    }

    public void setOpen_bank_no(String open_bank_no) {
        this.updated_at = new Date();
        this.open_bank_no = open_bank_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.updated_at = new Date();
        this.remark = remark;
    }

    public String getCurr_no() {
        return curr_no;
    }

    public void setCurr_no(String curr_no) {
        this.updated_at = new Date();
        this.curr_no = curr_no;
    }

    public String getCurr_iden() {
        return curr_iden;
    }

    public void setCurr_iden(String curr_iden) {
        this.updated_at = new Date();
        this.curr_iden = curr_iden;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.updated_at = new Date();
        this.balance = balance;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.updated_at = new Date();
        this.overdraft = overdraft;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.updated_at = new Date();
        this.voucher_type = voucher_type;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public void setVoucher_no(String voucher_no) {
        this.updated_at = new Date();
        this.voucher_no = voucher_no;
    }

    public String getOpponent_bank_no() {
        return opponent_bank_no;
    }

    public void setOpponent_bank_no(String opponent_bank_no) {
        this.updated_at = new Date();
        this.opponent_bank_no = opponent_bank_no;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.updated_at = new Date();
        this.summary = summary;
    }

    public String getRedo() {
        return redo;
    }

    public void setRedo(String redo) {
        this.updated_at = new Date();
        this.redo = redo;
    }

    public String getBook_list_no() {
        return book_list_no;
    }

    public void setBook_list_no(String book_list_no) {
        this.updated_at = new Date();
        this.book_list_no = book_list_no;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.updated_at = new Date();
        this.book_no = book_no;
    }

    public String getIs_make_acc() {
        return is_make_acc;
    }

    public void setIs_make_acc(String is_make_acc) {
        this.updated_at = new Date();
        this.is_make_acc = is_make_acc;
    }

    public String getBus_seq_no() {
        return bus_seq_no;
    }

    public void setBus_seq_no(String bus_seq_no) {
        this.updated_at = new Date();
        this.bus_seq_no = bus_seq_no;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.updated_at = new Date();
        this.jzpzh = jzpzh;
    }

    public CFinanceRecordingVoucherExtension getcFinanceRecordingVoucherExtension() {
        return cFinanceRecordingVoucherExtension;
    }

    public void setcFinanceRecordingVoucherExtension(CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension) {
        this.cFinanceRecordingVoucherExtension = cFinanceRecordingVoucherExtension;
    }
}
