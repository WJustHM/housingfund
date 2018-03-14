package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/8/18.
 */
@XmlRootElement(name = "GetIndiAcctsInfoDetailsResJBXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctsInfoDetailsResJBXX  implements Serializable{

    private String SFDJ;    //是否冻结1/0

    private  String    GRZH;  //个人账号

    private  String    CSNY;  //出生年月

    private  String    ZJLX;  //证件类型

    private  String    SJHM;  //手机号码

    private  String    ZJHM;  //证件号码

    private  String    XMQP;  //姓名全拼

    private  String    XingMing;  //姓名

    private  String    YouXiang;  //邮箱

    private  String    GRCKZHHM;  //个人存款账户号码

    private  String    GRCKZHKHYHMC;  //个人存款账户开户银行名称

    public String getSFDJ() {
        return SFDJ;
    }

    public void setSFDJ(String SFDJ) {
        this.SFDJ = SFDJ;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public void setCSNY(String CSNY) {
        this.CSNY = CSNY;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public void setXMQP(String XMQP) {
        this.XMQP = XMQP;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public void setYouXiang(String youXiang) {
        YouXiang = youXiang;
    }

    public void setGRCKZHH(String GRCKZHHM) {
        this.GRCKZHHM = GRCKZHHM;
    }

    public void setGRCKZHKHYHMC(String GRCKZHKHYHMC) {
        this.GRCKZHKHYHMC = GRCKZHKHYHMC;
    }

    public String getGRZH() {
        return GRZH;
    }

    public String getCSNY() {
        return CSNY;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public String getSJHM() {
        return SJHM;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public String getXMQP() {
        return XMQP;
    }

    public String getXingMing() {
        return XingMing;
    }

    public String getYouXiang() {
        return YouXiang;
    }

    public String getGRCKZHHM() {
        return GRCKZHHM;
    }

    public void setGRCKZHHM(String GRCKZHHM) {
        this.GRCKZHHM = GRCKZHHM;
    }

    public String getGRCKZHKHYHMC() {
        return GRCKZHKHYHMC;
    }


    @Override
    public String toString() {
        return "GetIndiAcctsInfoDetailsResJBXX{" +
                "SFDJ='" + SFDJ + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", CSNY='" + CSNY + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", XMQP='" + XMQP + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", YouXiang='" + YouXiang + '\'' +
                ", GRCKZHHM='" + GRCKZHHM + '\'' +
                ", GRCKZHKHYHMC='" + GRCKZHKHYHMC + '\'' +
                '}';
    }
}
