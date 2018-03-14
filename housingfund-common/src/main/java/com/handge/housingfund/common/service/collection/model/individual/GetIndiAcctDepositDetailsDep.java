package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 向超 on 2017/10/8.
 */
@XmlRootElement(name = "GetIndiAcctDepositDetailsDep")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctDepositDetailsDep implements java.io.Serializable{

    private String JZRQ;  //记账日期

    private String YWLX;  //业务类型

    private String DWFSE;  //单位发生额

    private String GRFSE;  //个人发生额

    private String FSE;  //发生额

    private String HJNY;  //汇缴年月

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String GRZHYE;  //个人账户状态

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getDWFSE() {
        return DWFSE;
    }

    public void setDWFSE(String DWFSE) {
        this.DWFSE = DWFSE;
    }

    public String getGRFSE() {
        return GRFSE;
    }

    public void setGRFSE(String GRFSE) {
        this.GRFSE = GRFSE;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getHJNY() {
        return HJNY;
    }

    public void setHJNY(String HJNY) {
        this.HJNY = HJNY;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }



    @Override
    public String toString() {
        return "GetIndiAcctDepositDetailsDep{" +
                "JZRQ='" + JZRQ + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", DWFSE='" + DWFSE + '\'' +
                ", GRFSE='" + GRFSE + '\'' +
                ", FSE='" + FSE + '\'' +
                ", HJNY='" + HJNY + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                '}';
    }
}
