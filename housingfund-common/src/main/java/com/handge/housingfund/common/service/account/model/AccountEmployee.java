package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
@XmlRootElement(name = "AccountEmployee")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountEmployee implements Serializable{
    private String id;
    private String xingming;//姓名
    private String zhanghao;//账号
    private String xingbie;//性别
    private String yhtx;//用户头像
    private String wangdian;//网点ID
    private String bumen;//部门ID
    private String rzsj;//入职时间
    private String xueli;//学历
    private String youxiang;//邮箱
    private String lxdh;//联系电话
    private String qqh;//qq号
    private String wxh;//微信号
    private String juese;//角色
    private String state;//状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao;
    }

    public String getXingbie() {
        return xingbie;
    }

    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }

    public String getYhtx() {
        return yhtx;
    }

    public void setYhtx(String yhtx) {
        this.yhtx = yhtx;
    }

    public String getWangdian() {
        return wangdian;
    }

    public void setWangdian(String wangdian) {
        this.wangdian = wangdian;
    }

    public String getBumen() {
        return bumen;
    }

    public void setBumen(String bumen) {
        this.bumen = bumen;
    }

    public String getRzsj() {
        return rzsj;
    }

    public void setRzsj(String rzsj) {
        this.rzsj = rzsj;
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getYouxiang() {
        return youxiang;
    }

    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getQqh() {
        return qqh;
    }

    public void setQqh(String qqh) {
        this.qqh = qqh;
    }

    public String getWxh() {
        return wxh;
    }

    public void setWxh(String wxh) {
        this.wxh = wxh;
    }

    public String getJuese() {
        return juese;
    }

    public void setJuese(String juese) {
        this.juese = juese;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
