package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
@XmlRootElement(name = "活期转定期")
@XmlAccessorType(XmlAccessType.FIELD)
public class Actived2Fixed implements Serializable {
    private static final long serialVersionUID = 7528036898407933618L;

    private String id;
    //业务流水号
    private String ywlsh;
    //开户银行名称
    private String khyhmc;
    //账号
    private String acct_no;
    //户名
    private String acct_name;
    //存期 0:三个月 1:半年 2:一年 3:两年 4:三年 5:五年 8:其他
    private String deposit_period;
    //利率
    private String interest_rate;
    //交易金额
    private String Amt;
    //存入日期
    private String deposit_begin_date;
    //到期日期
    private String deposit_end_date;
    //受理日期
    private String slrq;
    //备注
    private String beizhu;
    //状态机状态
    private String step;
    //操作员
    private String czy;
    //失败原因
    private String sbyy;
    //是否人工处理,0:否 1:是
    private String rgcl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
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

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
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

    public String getSbyy() {
        return sbyy;
    }

    public void setSbyy(String sbyy) {
        this.sbyy = sbyy;
    }

    public String getRgcl() {
        return rgcl;
    }

    public void setRgcl(String rgcl) {
        this.rgcl = rgcl;
    }
}
