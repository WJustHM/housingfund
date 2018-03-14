package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "RepaymentApplyPrepaymentGetYQHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentGetYQHKXX  implements Serializable {

    private String CZY;  //操作员

    private ArrayList<RepaymentApplyPrepaymentGetYQHKXXYQXX> YQXX;  //

    private String YQKZJ;  //逾期款总计

    private String YWWD;  //业务网点

    private String DKZH;  //贷款账号

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String JKRXM;  //借款人姓名

    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public ArrayList<RepaymentApplyPrepaymentGetYQHKXXYQXX> getYQXX() {

        return this.YQXX;

    }


    public void setYQXX(ArrayList<RepaymentApplyPrepaymentGetYQHKXXYQXX> YQXX) {

        this.YQXX = YQXX;

    }


    public String getYQKZJ() {

        return this.YQKZJ;

    }


    public void setYQKZJ(String YQKZJ) {

        this.YQKZJ = YQKZJ;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getHKJE() {

        return this.HKJE;

    }


    public void setHKJE(String HKJE) {

        this.HKJE = HKJE;

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

        return "RepaymentApplyPrepaymentGetYQHKXX{" +

                "CZY='" + this.CZY + '\'' + "," +
                "YQXX='" + this.YQXX + '\'' + "," +
                "YQKZJ='" + this.YQKZJ + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}