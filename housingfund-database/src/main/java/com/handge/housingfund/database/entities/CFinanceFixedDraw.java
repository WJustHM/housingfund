package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */
@Entity
@Table(name = "c_finance_fixed_draw")
@org.hibernate.annotations.Table(appliesTo = "c_finance_fixed_draw", comment = "定期支取")
public class CFinanceFixedDraw extends Common implements Serializable {
    private static final long serialVersionUID = 3044978312451302338L;

    @Column(name = "ywlsh", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
    private String ywlsh;
    @Column(name = "khyhmc", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '开户银行名称'")
    private String khyhmc;
    @Column(name = "acct_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '账号'")
    private String acct_no;
    @Column(name = "acct_name", columnDefinition = "VARCHAR(60) DEFAULT NULL COMMENT '户名'")
    private String acct_name;
    @Column(name = "book_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '册号'")
    private String book_no;
    @Column(name = "book_list_no", columnDefinition = "INT(16) DEFAULT NULL COMMENT '笔号'")
    private int book_list_no;
    @Column(name = "deposit_begin_date", columnDefinition = "DATETIME DEFAULT NULL COMMENT '存入日期'")
    private Date deposit_begin_date;
    @Column(name = "deposit_end_date", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到期日期'")
    private Date deposit_end_date;
    @Column(name = "deposti_amt", columnDefinition = "NUMERIC(16,2) DEFAULT NULL COMMENT '实际金额'")
    private BigDecimal deposti_amt = BigDecimal.ZERO;
    @Column(name = "draw_amt", columnDefinition = "NUMERIC(16,2) DEFAULT NULL COMMENT '支取金额'")
    private BigDecimal draw_amt = BigDecimal.ZERO;
    @Column(name = "deposit_period", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '存期 0:三个月 1:半年 2:一年 3:两年 4:三年 5:五年 8:其他'")
    private String deposit_period;
    @Column(name = "interest_rate", columnDefinition = "NUMERIC(8,5) DEFAULT NULL COMMENT '利率'")
    private BigDecimal interest_rate = BigDecimal.ZERO;
    @Column(name = "zqqk", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '支取情况'")
    private String zqqk;
    @Column(name = "DQCKBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '定期存款编号'")
    private String dqckbh;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '理财业务扩展表'")
    private CFinanceManageFinanceExtension cFinanceManageFinanceExtension;

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

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

    public String getAcct_name() {
        return acct_name;
    }

    public void setAcct_name(String acct_name) {
        this.acct_name = acct_name;
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

    public BigDecimal getDeposti_amt() {
        return deposti_amt;
    }

    public void setDeposti_amt(BigDecimal deposti_amt) {
        this.deposti_amt = deposti_amt;
    }

    public BigDecimal getDraw_amt() {
        return draw_amt;
    }

    public void setDraw_amt(BigDecimal draw_amt) {
        this.draw_amt = draw_amt;
    }

    public String getDeposit_period() {
        return deposit_period;
    }

    public void setDeposit_period(String deposit_period) {
        this.deposit_period = deposit_period;
    }

    public BigDecimal getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(BigDecimal interest_rate) {
        this.interest_rate = interest_rate;
    }

    public String getZqqk() {
        return zqqk;
    }

    public void setZqqk(String zqqk) {
        this.zqqk = zqqk;
    }

    public String getDqckbh() {
        return dqckbh;
    }

    public void setDqckbh(String dqckbh) {
        this.dqckbh = dqckbh;
    }

    public CFinanceManageFinanceExtension getcFinanceManageFinanceExtension() {
        return cFinanceManageFinanceExtension;
    }

    public void setcFinanceManageFinanceExtension(CFinanceManageFinanceExtension cFinanceManageFinanceExtension) {
        this.cFinanceManageFinanceExtension = cFinanceManageFinanceExtension;
    }
}
