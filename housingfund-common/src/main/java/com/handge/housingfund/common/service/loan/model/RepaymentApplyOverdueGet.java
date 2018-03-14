package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyOverdueGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyOverdueGet  implements Serializable {

    private String LiXi;  //利息

    private String JKRQ;  //逾期日期

    private String QXTS;  //期限天数

    private String BenJin;  //本金

    private String FaXi;  //罚息

    private String JKZJE;  //借款总金额

    private String YQTS;  //逾期天数

    private String RiQi;  //日期

    private String JKRXM;  //借款人姓名

    public String getLiXi() {

        return this.LiXi;

    }


    public void setLiXi(String LiXi) {

        this.LiXi = LiXi;

    }


    public String getJKRQ() {

        return this.JKRQ;

    }


    public void setJKRQ(String JKRQ) {

        this.JKRQ = JKRQ;

    }


    public String getQXTS() {

        return this.QXTS;

    }


    public void setQXTS(String QXTS) {

        this.QXTS = QXTS;

    }


    public String getBenJin() {

        return this.BenJin;

    }


    public void setBenJin(String BenJin) {

        this.BenJin = BenJin;

    }


    public String getFaXi() {

        return this.FaXi;

    }


    public void setFaXi(String FaXi) {

        this.FaXi = FaXi;

    }


    public String getJKZJE() {

        return this.JKZJE;

    }


    public void setJKZJE(String JKZJE) {

        this.JKZJE = JKZJE;

    }


    public String getYQTS() {

        return this.YQTS;

    }


    public void setYQTS(String YQTS) {

        this.YQTS = YQTS;

    }


    public String getRiQi() {

        return this.RiQi;

    }


    public void setRiQi(String RiQi) {

        this.RiQi = RiQi;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "RepaymentApplyOverdueGet{" +

                "LiXi='" + this.LiXi + '\'' + "," +
                "JKRQ='" + this.JKRQ + '\'' + "," +
                "QXTS='" + this.QXTS + '\'' + "," +
                "BenJin='" + this.BenJin + '\'' + "," +
                "FaXi='" + this.FaXi + '\'' + "," +
                "JKZJE='" + this.JKZJE + '\'' + "," +
                "YQTS='" + this.YQTS + '\'' + "," +
                "RiQi='" + this.RiQi + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}