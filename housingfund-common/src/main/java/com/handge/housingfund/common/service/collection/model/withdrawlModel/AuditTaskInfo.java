package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 周君 on 2017/7/27.
 */
@XmlRootElement(name = "AuditTaskInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditTaskInfo implements Serializable {
    /**
     * 状态
     */
    private String ZhuangTai;
    /**
     * 业务流水号
     */
    private String YWLSH;
    /**
     * 个人账号
     */
    private String GRZH;
    /**
     * 姓名
     */
    private String XingMing;
    /**
     * 单位名称
     */
    private String DWMC;
    /**
     * 提取原因
     */
    private String TQYY;
    /**
     * 操作员
     */
    private String CZY;
    /**
     * 业务网点
     */
    private String YWWD;
    /**
     * 到达时间
     */
    private String DDSJ;
    /*受理时间*/
    private String SLSJ;

    private String SCSHY;   //上次审核员

    private String SFTS; //是否特审

    private String DQSHY;

    private String DQXM;

    private String id;

    private String ZJHM;

    @Override
    public String toString() {
        return "AuditTaskInfo{" +
                "ZhuangTai='" + ZhuangTai + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", TQYY='" + TQYY + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", id='" + id + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                '}';
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuditTaskInfo() {
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

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

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

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getTQYY() {
        return TQYY;
    }

    public void setTQYY(String TQYY) {
        this.TQYY = TQYY;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getDDSJ() {
        return DDSJ;
    }

    public void setDDSJ(String DDSJ) {
        this.DDSJ = DDSJ;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

}
