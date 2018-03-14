package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "收到暂收")
@XmlAccessorType(XmlAccessType.FIELD)
public class SDZS implements Serializable {

//    @XmlElement(name = "银行名称")
    private String YHMC;

//    @XmlElement(name = "银行通知单")
    private String YHTZD;

//    @XmlElement(name = "银行账号")
    private String YHZH;

//    @XmlElement(name = "发生时间")
    private String FSSJ;

//    @XmlElement(name = "发生额")
    private  String FSE;

//    @XmlElement(name = "备注")
    private String BZ;

//    @XmlElement(name = "附件资料")
    private List<String> FuJian;

//    @XmlElement(name = "资金详情")
    private List<TableRow> tableRows;

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getYHTZD() {
        return YHTZD;
    }

    public void setYHTZD(String YHTZD) {
        this.YHTZD = YHTZD;
    }

    public String getYHZH() {
        return YHZH;
    }

    public void setYHZH(String YHZH) {
        this.YHZH = YHZH;
    }

    public String getFSSJ() {
        return FSSJ;
    }

    public void setFSSJ(String FSSJ) {
        this.FSSJ = FSSJ;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public List<String> getFuJian() {
        return FuJian;
    }

    public void setFuJian(List<String> fuJian) {
        FuJian = fuJian;
    }

    public List<TableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
    }

    @Override
    public String toString() {
        return "SDZS{" +
                "YHMC='" + YHMC + '\'' +
                ", YHTZD='" + YHTZD + '\'' +
                ", YHZH='" + YHZH + '\'' +
                ", FSSJ='" + FSSJ + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
