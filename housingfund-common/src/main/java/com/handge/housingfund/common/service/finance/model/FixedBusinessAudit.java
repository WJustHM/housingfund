package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/14.
 */
@XmlRootElement(name = "定期业务审核")
@XmlAccessorType(XmlAccessType.FIELD)
public class FixedBusinessAudit implements Serializable {

    private static final long serialVersionUID = -5805382323442013005L;
    private String id;
    //业务流水号
    private String ywlsh;
    //开户银行名称
    private String khyhmc;
    //账号
    private String acct_no;
    //实际金额
    private String deposti_amt;
    //操作员
    private String czy;
    //业务类型 09：定期存款 10：定期支取 11：通知存款 12：通知存款支取
    private String ywlx;
    //到达时间
    private String ddsj;
    //审核时间
    private String shsj;
    //状态机状态
    private String step;

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

    public String getDeposti_amt() {
        return deposti_amt;
    }

    public void setDeposti_amt(String deposti_amt) {
        this.deposti_amt = deposti_amt;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getDdsj() {
        return ddsj;
    }

    public void setDdsj(String ddsj) {
        this.ddsj = ddsj;
    }

    public String getShsj() {
        return shsj;
    }

    public void setShsj(String shsj) {
        this.shsj = shsj;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
