package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 申请进度查询(BDC904)输出格式
 */
public class ApplScheduleQueryOut  implements Serializable {
    private static final long serialVersionUID = 7613439297091595304L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 申请处理状态(required)
     * 00-正常办结【终止状态】
     * 01-联系函复核通过
     * 02-联系函确认接收
     * 05-转入撤销业务办结【终止状态】
     * 06-协商中
     * 08-账户信息复核通过
     * 13-转出审核不通过
     * 20-协商办结【终止状态】
     * 40-失败办结【终止状态】
     */
    private String AppPrStatus;
    /**
     * 处理结果详细信息(required), 比如转出中心审核未通过的原因、转出中心审核通过
     */
    private String AppProMessage;
    /**
     * 转出地联系人(required)
     */
    private String Contacts;
    /**
     * 转出地联系电话(required), 固定电话：区号-电话号码 手机：11位号码
     */
    private String ContPhone;

    public ApplScheduleQueryOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getAppPrStatus() {
        return AppPrStatus;
    }

    public void setAppPrStatus(String appPrStatus) {
        AppPrStatus = appPrStatus;
    }

    public String getAppProMessage() {
        return AppProMessage;
    }

    public void setAppProMessage(String appProMessage) {
        AppProMessage = appProMessage;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String contacts) {
        Contacts = contacts;
    }

    public String getContPhone() {
        return ContPhone;
    }

    public void setContPhone(String contPhone) {
        ContPhone = contPhone;
    }

    @Override
    public String toString() {
        return "ApplScheduleQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", AppPrStatus='" + AppPrStatus + '\'' +
                ", AppProMessage='" + AppProMessage + '\'' +
                ", Contacts='" + Contacts + '\'' +
                ", ContPhone='" + ContPhone + '\'' +
                '}';
    }
}
