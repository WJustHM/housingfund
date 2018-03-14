package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingRepamentApplyRangeGetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRepamentApplyRangeGetRes  implements Serializable {

    private String SLSJ;  //受理时间

    private String YWLSH;  //业务流水号

    private String Status;  //状态

    private String SQHKLX;  //申请还款类型

    private String DDSJ;  //到达时间

    private String JKRZJHM;  //借款人证件号码

    private String YWWD;  //业务网点

    private String SQHKJE;  //申请还款金额

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    private String SCSHY;

    private String DQSHY;

    private String DQXM;

    private String SFTS;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HousingRepamentApplyRangeGetRes{" +
                "SLSJ='" + SLSJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", Status='" + Status + '\'' +
                ", SQHKLX='" + SQHKLX + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SQHKJE='" + SQHKJE + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getStatus() {

        return this.Status;

    }


    public void setStatus(String Status) {

        this.Status = Status;

    }


    public String getSQHKLX() {

        return this.SQHKLX;

    }


    public void setSQHKLX(String SQHKLX) {

        this.SQHKLX = SQHKLX;

    }



    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

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


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }

}