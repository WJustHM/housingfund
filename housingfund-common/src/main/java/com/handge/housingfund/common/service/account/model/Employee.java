package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee implements Serializable {

    private String XingMing;//姓名
    private String XingBie;//性别
    private String YHTX;//用户头像
    private String WangDian;//网点ID
    private String BuMen;//部门ID
    private long RZSJ;//入职时间
    private String XueLi;//学历
    private String YouXiang;//邮箱
    private String LXDH;//联系电话
    private String QQH;//qq号
    private String WXH;//微信号
    private String JueSe;//角色
    private String state;//状态


    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getXingBie() {
        return XingBie;
    }

    public void setXingBie(String xingBie) {
        XingBie = xingBie;
    }

    public String getYHTX() {
        return YHTX;
    }

    public void setYHTX(String YHTX) {
        this.YHTX = YHTX;
    }

    public String getWangDian() {
        return WangDian;
    }

    public void setWangDian(String wangDian) {
        WangDian = wangDian;
    }

    public String getBuMen() {
        return BuMen;
    }

    public void setBuMen(String buMen) {
        BuMen = buMen;
    }

    public long getRZSJ() {
        return RZSJ;
    }

    public void setRZSJ(long RZSJ) {
        this.RZSJ = RZSJ;
    }

    public String getXueLi() {
        return XueLi;
    }

    public void setXueLi(String xueLi) {
        XueLi = xueLi;
    }

    public String getYouXiang() {
        return YouXiang;
    }

    public void setYouXiang(String youXiang) {
        YouXiang = youXiang;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getQQH() {
        return QQH;
    }

    public void setQQH(String QQH) {
        this.QQH = QQH;
    }

    public String getWXH() {
        return WXH;
    }

    public void setWXH(String WXH) {
        this.WXH = WXH;
    }

    public String getJueSe() {
        return JueSe;
    }

    public void setJueSe(String jueSe) {
        JueSe = jueSe;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
