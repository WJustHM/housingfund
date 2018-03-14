package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 向超 on 2017/10/8.
 */
@XmlRootElement(name = "GetIndiAcctDepositDetailsPerson")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctDepositDetailsPerson implements java.io.Serializable{

    private String GRZH;  //个人账号

    private String XingMing;  //姓名

    private String SFDJ;  //是否冻结

    private String ZJLX;  //证件类型

    private String ZJHM;  //证件号码

    private String SJHM;  //手机号码

    private String YouXiang;  //邮箱

    private String GRCKZHKHYHMC;  //个人存款账户开户银行名称

    private String GRCKZHHM;  //个人存款账户号码

    private String GRZHYE;  //个人账户余额

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getSFDJ() {
        return SFDJ;
    }

    public void setSFDJ(String SFDJ) {
        this.SFDJ = SFDJ;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getYouXiang() {
        return YouXiang;
    }

    public void setYouXiang(String youXiang) {
        YouXiang = youXiang;
    }

    public String getGRCKZHKHYHMC() {
        return GRCKZHKHYHMC;
    }

    public void setGRCKZHKHYHMC(String GRCKZHKHYHMC) {
        this.GRCKZHKHYHMC = GRCKZHKHYHMC;
    }

    public String getGRCKZHHM() {
        return GRCKZHHM;
    }

    public void setGRCKZHHM(String GRCKZHHM) {
        this.GRCKZHHM = GRCKZHHM;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    @Override
    public String toString() {
        return "GetIndiAcctDepositDetailsPerson{" +
                "GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", SFDJ='" + SFDJ + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", YouXiang='" + YouXiang + '\'' +
                ", GRCKZHKHYHMC='" + GRCKZHKHYHMC + '\'' +
                ", GRCKZHHM='" + GRCKZHHM + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                '}';
    }
}
