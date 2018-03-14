package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "PersonBaseMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonBaseMessage implements Serializable {

    private String GRZH;    //个人账号

    private String XingMing;  //姓名

    private String ZJLX;  //证件类型

    private String ZJHM;  //证件号码

    private String CSNY;  //出生年月

    private String SJHM;  //手机号码

    private String HYZK;  //婚姻状况

    private String JTZZ;  //家庭住址

    private String ZhiYe;  //职业

    private String YZBM;  //邮政编码

    private String ZhiCheng;  //职称

    private String GDDHHM;  //固定电话号码

    private String XMQP;  //姓名全拼

    private String XingBie;  //性别

    private String JTYSR;  //家庭月收入

    private String XueLi;  //学历

    private String ZhiWu;  //职务

    private String YouXiang;  //邮箱

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

    public String getCSNY() {
        return CSNY;
    }

    public void setCSNY(String CSNY) {
        this.CSNY = CSNY;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getHYZK() {
        return HYZK;
    }

    public void setHYZK(String HYZK) {
        this.HYZK = HYZK;
    }

    public String getJTZZ() {
        return JTZZ;
    }

    public void setJTZZ(String JTZZ) {
        this.JTZZ = JTZZ;
    }

    public String getZhiYe() {
        return ZhiYe;
    }

    public void setZhiYe(String zhiYe) {
        ZhiYe = zhiYe;
    }

    public String getYZBM() {
        return YZBM;
    }

    public void setYZBM(String YZBM) {
        this.YZBM = YZBM;
    }

    public String getZhiCheng() {
        return ZhiCheng;
    }

    public void setZhiCheng(String zhiCheng) {
        ZhiCheng = zhiCheng;
    }

    public String getGDDHHM() {
        return GDDHHM;
    }

    public void setGDDHHM(String GDDHHM) {
        this.GDDHHM = GDDHHM;
    }

    public String getXMQP() {
        return XMQP;
    }

    public void setXMQP(String XMQP) {
        this.XMQP = XMQP;
    }

    public String getXingBie() {
        return XingBie;
    }

    public void setXingBie(String xingBie) {
        XingBie = xingBie;
    }

    public String getJTYSR() {
        return JTYSR;
    }

    public void setJTYSR(String JTYSR) {
        this.JTYSR = JTYSR;
    }

    public String getXueLi() {
        return XueLi;
    }

    public void setXueLi(String xueLi) {
        XueLi = xueLi;
    }

    public String getZhiWu() {
        return ZhiWu;
    }

    public void setZhiWu(String zhiWu) {
        ZhiWu = zhiWu;
    }

    public String getYouXiang() {
        return YouXiang;
    }

    public void setYouXiang(String youXiang) {
        YouXiang = youXiang;
    }

    @Override
    public String toString() {
        return "PersonBaseMessage{" +
                "GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", CSNY='" + CSNY + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", HYZK='" + HYZK + '\'' +
                ", JTZZ='" + JTZZ + '\'' +
                ", ZhiYe='" + ZhiYe + '\'' +
                ", YZBM='" + YZBM + '\'' +
                ", ZhiCheng='" + ZhiCheng + '\'' +
                ", GDDHHM='" + GDDHHM + '\'' +
                ", XMQP='" + XMQP + '\'' +
                ", XingBie='" + XingBie + '\'' +
                ", JTYSR=" + JTYSR +
                ", XueLi='" + XueLi + '\'' +
                ", ZhiWu='" + ZhiWu + '\'' +
                ", YouXiang='" + YouXiang + '\'' +
                '}';
    }
}