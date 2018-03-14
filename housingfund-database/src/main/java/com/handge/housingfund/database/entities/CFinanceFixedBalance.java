package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */
@Entity
@Table(name = "c_finance_fixed_balance")
@org.hibernate.annotations.Table(appliesTo = "c_finance_fixed_balance", comment = "定期余额")
public class CFinanceFixedBalance extends Common implements Serializable {
    private static final long serialVersionUID = -899161821433265080L;

    @Column(name = "khyhmc", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '开户银行名称'")
    private String khyhmc;
    @Column(name = "acct_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '账号'")
    private String acct_no;
    @Column(name = "book_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '册号'")
    private String book_no;
    @Column(name = "book_list_no", columnDefinition = "INT(16) DEFAULT NULL COMMENT '笔号'")
    private int book_list_no;
    @Column(name = "deposit_begin_date", columnDefinition = "DATETIME DEFAULT NULL COMMENT '存入日期'")
    private Date deposit_begin_date;
    @Column(name = "deposit_end_date", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到期日期'")
    private Date deposit_end_date;
    @Column(name = "beg_amt", columnDefinition = "NUMERIC(16,2) DEFAULT NULL COMMENT '开户金额'")
    private BigDecimal beg_amt = BigDecimal.ZERO;
    @Column(name = "draw_amt", columnDefinition = "NUMERIC(16,2) DEFAULT NULL COMMENT '实际金额'")
    private BigDecimal draw_amt = BigDecimal.ZERO;
    @Column(name = "deposit_period", columnDefinition = "NUMERIC(5,0) DEFAULT NULL COMMENT '存期'")
    private BigDecimal deposit_period = BigDecimal.ZERO;
    @Column(name = "interest_rate", columnDefinition = "NUMERIC(8,5) DEFAULT NULL COMMENT '利率'")
    private BigDecimal interest_rate = BigDecimal.ZERO;
    @Column(name = "interest", columnDefinition = "NUMERIC(16,2) DEFAULT NULL COMMENT '结清利息'")
    private BigDecimal interest = BigDecimal.ZERO;
    @Column(name = "freeze_type", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '冻结状态 0:正常 1:冻结 3:部分冻结 4:其它'")
    private String freeze_type;
    @Column(name = "loss_flag", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '挂失标志 0:正常 1:挂失'")
    private String loss_flag;
    @Column(name = "acct_status", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '账户状态 0:正常 1:销户 :没收 3:上缴 9:其他 99:支取中'")
    private String acct_status;
    @Column(name = "DQCKBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '定期存款编号'")
    private String dqckbh;

    public String getKhyhmc() {
        return khyhmc;
    }

    public void setKhyhmc(String khyhmc) {
        this.khyhmc = khyhmc;
    }

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public int getBook_list_no() {
        return book_list_no;
    }

    public void setBook_list_no(int book_list_no) {
        this.book_list_no = book_list_no;
    }

    public Date getDeposit_begin_date() {
        return deposit_begin_date;
    }

    public void setDeposit_begin_date(Date deposit_begin_date) {
        this.deposit_begin_date = deposit_begin_date;
    }

    public Date getDeposit_end_date() {
        return deposit_end_date;
    }

    public void setDeposit_end_date(Date deposit_end_date) {
        this.deposit_end_date = deposit_end_date;
    }

    public BigDecimal getBeg_amt() {
        return beg_amt;
    }

    public void setBeg_amt(BigDecimal beg_amt) {
        this.beg_amt = beg_amt;
    }

    public BigDecimal getDraw_amt() {
        return draw_amt;
    }

    public void setDraw_amt(BigDecimal draw_amt) {
        this.draw_amt = draw_amt;
    }

    public BigDecimal getDeposit_period() {
        return deposit_period;
    }

    public void setDeposit_period(BigDecimal deposit_period) {
        this.deposit_period = deposit_period;
    }

    public BigDecimal getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(BigDecimal interest_rate) {
        this.interest_rate = interest_rate;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public String getFreeze_type() {
        return freeze_type;
    }

    public void setFreeze_type(String freeze_type) {
        this.freeze_type = freeze_type;
    }

    public String getLoss_flag() {
        return loss_flag;
    }

    public void setLoss_flag(String loss_flag) {
        this.loss_flag = loss_flag;
    }

    public String getAcct_status() {
        return acct_status;
    }

    public void setAcct_status(String acct_status) {
        this.acct_status = acct_status;
    }

    public String getDqckbh() {
        return dqckbh;
    }

    public void setDqckbh(String dqckbh) {
        this.dqckbh = dqckbh;
    }
}
