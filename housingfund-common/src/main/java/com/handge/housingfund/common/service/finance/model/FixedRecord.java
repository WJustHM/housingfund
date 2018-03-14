package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
@XmlRootElement(name = "定期账户详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class FixedRecord implements Serializable {
    private static final long serialVersionUID = 1297942893625449400L;

    private String id;
    //开户银行名称
    private String khyhmc;
    //账号
    private String acct_no;
    //户名
    private String acct_name;
    //册号
    private String book_no;
    //笔号
    private int book_list_no;
    //存入日期
    private String deposit_begin_date;
    //到期日期
    private String deposit_end_date;
    //开户金额
    private String beg_amt;
    //实际金额
    private String draw_amt;
    //存期
    private String deposit_period;
    //利率
    private String interest_rate;
    //结清利息
    private String interest;
    //冻结状态 0:正常 1:冻结 3:部分冻结 4:其它
    private String freeze_type;
    //挂失标志 0:正常 1:挂失
    private String loss_flag;
    //账户状态 0:未支取 1:已支取 2:支取中
    private String acct_status;
    //定期存款编号
    private String dqckbh;
    //操作员
    private String czy;
    //受理日期
    private String slrq;
    //备注
    private String beizhu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDeposit_begin_date() {
        return deposit_begin_date;
    }

    public void setDeposit_begin_date(String deposit_begin_date) {
        this.deposit_begin_date = deposit_begin_date;
    }

    public String getDeposit_end_date() {
        return deposit_end_date;
    }

    public void setDeposit_end_date(String deposit_end_date) {
        this.deposit_end_date = deposit_end_date;
    }

    public String getBeg_amt() {
        return beg_amt;
    }

    public void setBeg_amt(String beg_amt) {
        this.beg_amt = beg_amt;
    }

    public String getDraw_amt() {
        return draw_amt;
    }

    public void setDraw_amt(String draw_amt) {
        this.draw_amt = draw_amt;
    }

    public String getDeposit_period() {
        return deposit_period;
    }

    public void setDeposit_period(String deposit_period) {
        this.deposit_period = deposit_period;
    }

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
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

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getSlrq() {
        return slrq;
    }

    public void setSlrq(String slrq) {
        this.slrq = slrq;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
