package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseHouseInformationOverhaulInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseHouseInformationOverhaulInformation  implements Serializable {

    private  String    DXGCYS;  //大修工程预算

    private  String    GRSKYHZH;  //个人收款银行账号

    private  String    FWZJBGJGMCJBH;  //房屋质检报告机关名称及编号

    private  String    FXHCS;  //土地使用证号

    private  String    FWZL;  //房屋坐落

    private  String    YJZMJ;  //原建筑面积

    private  String    KHYHMC;  //开户银行名称

    private  String    BLZL;   //办理资料  //

    private  String    YBDCZH;  //原不动产证号

    private  String    JHJGRQ;  //计划竣工日期

    private  String    JHKGRQ;  //计划开工日期

    private  String    YHKHM;  //银行开户名

    public String getDXGCYS() {
        return DXGCYS;
    }

    public void setDXGCYS(String DXGCYS) {
        this.DXGCYS = DXGCYS;
    }

    public String getGRSKYHZH() {
        return GRSKYHZH;
    }

    public void setGRSKYHZH(String GRSKYHZH) {
        this.GRSKYHZH = GRSKYHZH;
    }

    public String getFWZJBGJGMCJBH() {
        return FWZJBGJGMCJBH;
    }

    public void setFWZJBGJGMCJBH(String FWZJBGJGMCJBH) {
        this.FWZJBGJGMCJBH = FWZJBGJGMCJBH;
    }

    public String getFXHCS() {
        return FXHCS;
    }

    public void setFXHCS(String FXHCS) {
        this.FXHCS = FXHCS;
    }

    public String getFWZL() {
        return FWZL;
    }

    public void setFWZL(String FWZL) {
        this.FWZL = FWZL;
    }

    public String getYJZMJ() {
        return YJZMJ;
    }

    public void setYJZMJ(String YJZMJ) {
        this.YJZMJ = YJZMJ;
    }

    public String getKHYHMC() {
        return KHYHMC;
    }

    public void setKHYHMC(String KHYHMC) {
        this.KHYHMC = KHYHMC;
    }

    public String getSCZL() {
        return BLZL;
    }

    public void setSCZL(String SCZL) {
        this.BLZL = SCZL;
    }

    public String getYBDCZH() {
        return YBDCZH;
    }

    public void setYBDCZH(String YBDCZH) {
        this.YBDCZH = YBDCZH;
    }

    public String getJHJGRQ() {
        return JHJGRQ;
    }

    public void setJHJGRQ(String JHJGRQ) {
        this.JHJGRQ = JHJGRQ;
    }

    public String getJHKGRQ() {
        return JHKGRQ;
    }

    public void setJHKGRQ(String JHKGRQ) {
        this.JHKGRQ = JHKGRQ;
    }

    public String getYHKHM() {
        return YHKHM;
    }

    public void setYHKHM(String YHKHM) {
        this.YHKHM = YHKHM;
    }

    @Override
    public String toString() {
        return "GetApplicantResponseHouseInformationOverhaulInformation{" +
                "DXGCYS='" + DXGCYS + '\'' +
                ", GRSKYHZH='" + GRSKYHZH + '\'' +
                ", FWZJBGJGMCJBH='" + FWZJBGJGMCJBH + '\'' +
                ", FXHCS='" + FXHCS + '\'' +
                ", FWZL='" + FWZL + '\'' +
                ", YJZMJ='" + YJZMJ + '\'' +
                ", KHYHMC='" + KHYHMC + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", YBDCZH='" + YBDCZH + '\'' +
                ", JHJGRQ='" + JHJGRQ + '\'' +
                ", JHKGRQ='" + JHKGRQ + '\'' +
                ", YHKHM='" + YHKHM + '\'' +
                '}';
    }
}