package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingRepaymentApplyListGetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRepaymentApplyListGetRes  implements Serializable {

    private String Status;  //状态

    private String YWLSH;  //业务流水号

    private String SLSJ;  //受理时间

    private String SQHKLX;  //申请还款类型

    private String JKRZJHM;  //借款人证件号码

    private String SQHKJE;  //申请还款金额

    private String DKZH;  //贷款账号

    private String YDKKRQ;  //约定扣款日期

    private String JKRXM;  //借款人姓名

    private String ZJHM;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {

        return this.Status;

    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public void setStatus(String Status) {

        this.Status = Status;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getSQHKLX() {

        return this.SQHKLX;

    }


    public void setSQHKLX(String SQHKLX) {

        this.SQHKLX = SQHKLX;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getSQHKJE() {

        return this.SQHKJE;

    }


    public void setSQHKJE(String SQHKJE) {

        this.SQHKJE = SQHKJE;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getYDKKRQ() {

        return this.YDKKRQ;

    }


    public void setYDKKRQ(String YDKKRQ) {

        this.YDKKRQ = YDKKRQ;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "HousingRepaymentApplyListGetRes{" +

                "Status='" + this.Status + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "SLSJ='" + this.SLSJ + '\'' + "," +
                "SQHKLX='" + this.SQHKLX + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "SQHKJE='" + this.SQHKJE + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}