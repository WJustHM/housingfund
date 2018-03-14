package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/10/25.
 */
@XmlRootElement(name = "余额调节详情基础")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReconciliationBase implements Serializable {

    private static final long serialVersionUID = -2195578740098144886L;
    private String TJRQ; //调节日期yyyy-MM

    private String ZHHM;//专户号码

    private String node;//银行节点号

    private String KHYHM;//开户银行名称

    private String ZXCKYE;//中心存款余额

    private String ZXJIA;//加：银行已收，中心未收款

    private String ZXJ;//减：银行已付，中心未付款

    private String ZXYE;//中心调节后的存款余额

    private String YHDZYE;//银行对账余额

    private String YHJIA;//加：中心已收，银行未收款

    private String YHJ;//减：企业已付，银行未付款

    private String YHYE;//银行调节后的存款余额

    public ReconciliationBase() {
    }

    public ReconciliationBase(String TJRQ, String ZHHM, String node, String ZXCKYE, String ZXJIA, String ZXJ, String ZXYE, String YHDZYE, String YHJIA, String YHJ, String YHYE, String KHYHM) {
        this.TJRQ = TJRQ;
        this.ZHHM = ZHHM;
        this.node = node;
        this.KHYHM = KHYHM;
        this.ZXCKYE = ZXCKYE;
        this.ZXJIA = ZXJIA;
        this.ZXJ = ZXJ;
        this.ZXYE = ZXYE;
        this.YHDZYE = YHDZYE;
        this.YHJIA = YHJIA;
        this.YHJ = YHJ;
        this.YHYE = YHYE;
    }

    public String getTJRQ() {
        return TJRQ;
    }

    public void setTJRQ(String TJRQ) {
        this.TJRQ = TJRQ;
    }

    public String getZHHM() {
        return ZHHM;
    }

    public void setZHHM(String ZHHM) {
        this.ZHHM = ZHHM;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getZXCKYE() {
        return ZXCKYE;
    }

    public void setZXCKYE(String ZXCKYE) {
        this.ZXCKYE = ZXCKYE;
    }

    public String getZXJIA() {
        return ZXJIA;
    }

    public void setZXJIA(String ZXJIA) {
        this.ZXJIA = ZXJIA;
    }

    public String getZXJ() {
        return ZXJ;
    }

    public void setZXJ(String ZXJ) {
        this.ZXJ = ZXJ;
    }

    public String getZXYE() {
        return ZXYE;
    }

    public void setZXYE(String ZXYE) {
        this.ZXYE = ZXYE;
    }

    public String getYHDZYE() {
        return YHDZYE;
    }

    public void setYHDZYE(String YHDZYE) {
        this.YHDZYE = YHDZYE;
    }

    public String getYHJIA() {
        return YHJIA;
    }

    public void setYHJIA(String YHJIA) {
        this.YHJIA = YHJIA;
    }

    public String getYHJ() {
        return YHJ;
    }

    public void setYHJ(String YHJ) {
        this.YHJ = YHJ;
    }

    public String getYHYE() {
        return YHYE;
    }

    public void setYHYE(String YHYE) {
        this.YHYE = YHYE;
    }

    public String getKHYHM() {
        return KHYHM;
    }

    public void setKHYHM(String KHYHM) {
        this.KHYHM = KHYHM;
    }

    @Override
    public String toString() {
        return "ReconciliationBase{" +
                "TJRQ='" + TJRQ + '\'' +
                ", ZHHM='" + ZHHM + '\'' +
                ", node='" + node + '\'' +
                ", KHYHM='" + KHYHM + '\'' +
                ", ZXCKYE='" + ZXCKYE + '\'' +
                ", ZXJIA='" + ZXJIA + '\'' +
                ", ZXJ='" + ZXJ + '\'' +
                ", ZXYE='" + ZXYE + '\'' +
                ", YHDZYE='" + YHDZYE + '\'' +
                ", YHJIA='" + YHJIA + '\'' +
                ", YHJ='" + YHJ + '\'' +
                ", YHYE='" + YHYE + '\'' +
                '}';
    }
}
