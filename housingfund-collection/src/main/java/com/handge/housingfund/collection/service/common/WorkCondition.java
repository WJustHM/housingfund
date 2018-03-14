package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.database.enums.BusinessType;

/**
 * Created by 凡 on 2017/8/3.
 */
public class WorkCondition {

    //业务流水号
    private String ywlsh;

    //主体类型，表示办理业务的是单位：1，还是个人：2
    private String ztlx;

    //当前状态
    private String status;

    //事件
    private String event;

    //模块类型
    private BusinessType type;

    //具体的业务类型
    private String subType;

    //操作员
    private String czy;

    //操作员的网点
    private String czwd;

    //操作事件
    private String czsj;

    //是否审核
    private boolean sfsh;

    //是否通过
    private boolean sftg;

    //审核备注
    private String beizhu;

    //审核不通过原因
    private String shbtgyy;

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public String getZtlx() {
        return ztlx;
    }

    public void setZtlx(String ztlx) {
        this.ztlx = ztlx;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public BusinessType getType() {
        return type;
    }

    public void setType(BusinessType type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getCzwd() {
        return czwd;
    }

    public void setCzwd(String czwd) {
        this.czwd = czwd;
    }

    public String getCzsj() {
        return czsj;
    }

    public void setCzsj(String czsj) {
        this.czsj = czsj;
    }

    public boolean isSfsh() {
        return sfsh;
    }

    public void setSfsh(boolean sfsh) {
        this.sfsh = sfsh;
    }

    public boolean isSftg() {
        return sftg;
    }

    public void setSftg(boolean sftg) {
        this.sftg = sftg;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getShbtgyy() {
        return shbtgyy;
    }

    public void setShbtgyy(String shbtgyy) {
        this.shbtgyy = shbtgyy;
    }
}
