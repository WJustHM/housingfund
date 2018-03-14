package com.handge.housingfund.common.service.loan.model;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/8.
 * 描述 还款申请回执单
 */
public class RepaymentApplyReceipt  implements Serializable {
    private  String YZM;//验证码

    private String YWLSH;//业务流水号

    private String TZRQ;//填制日期

    private RepaymentApplyPrepaymentPostYQHKXX YQHKXX;  //逾期还款信息

    private RepaymentApplyPrepaymentPostTQBFHKXX TQBFHKXX;  //提前部分还款信息

    private String HKLX;  //类型（0逾期还款，1提前部分还款，2提前结清还款）

    private RepaymentApplyPrepaymentPostTQJQHKXX TQJQHKXX;  //提前结清还款信息

    private String CZY;//操作员

    private String SHR; //审核人

    private String YWWD; //业务网点

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }
    public String getYZM() {
        return YZM;
    }

    public void setYZM(String YZM) {
        this.YZM = YZM;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public RepaymentApplyPrepaymentPostYQHKXX getYQHKXX() {
        return YQHKXX;
    }

    public void setYQHKXX(RepaymentApplyPrepaymentPostYQHKXX YQHKXX) {
        this.YQHKXX = YQHKXX;
    }

    public RepaymentApplyPrepaymentPostTQBFHKXX getTQBFHKXX() {
        return TQBFHKXX;
    }

    public void setTQBFHKXX(RepaymentApplyPrepaymentPostTQBFHKXX TQBFHKXX) {
        this.TQBFHKXX = TQBFHKXX;
    }

    public String getHKLX() {
        return HKLX;
    }

    public void setHKLX(String HKLX) {
        this.HKLX = HKLX;
    }

    public RepaymentApplyPrepaymentPostTQJQHKXX getTQJQHKXX() {
        return TQJQHKXX;
    }

    public void setTQJQHKXX(RepaymentApplyPrepaymentPostTQJQHKXX TQJQHKXX) {
        this.TQJQHKXX = TQJQHKXX;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    @Override
    public String toString() {
        return "RepaymentApplyReceipt{" +
                "YZM='" + YZM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", YQHKXX=" + YQHKXX +
                ", TQBFHKXX=" + TQBFHKXX +
                ", HKLX='" + HKLX + '\'' +
                ", TQJQHKXX=" + TQJQHKXX +
                ", CZY='" + CZY + '\'' +
                ", SHR='" + SHR + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}
