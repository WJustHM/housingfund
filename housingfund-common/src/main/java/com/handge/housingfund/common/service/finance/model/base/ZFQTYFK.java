package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "支付补息贷款利息")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZFQTYFK implements Serializable {


//    @XmlElement(name = "银行名称")
    private String YHMC;


//    @XmlElement(name = "银行账号")
    private String YHZH;


//    @XmlElement(name = "发生额")
    private  String FSE;

//    @XmlElement(name = "备注")
    private String BZ;

//    @XmlElement(name = "附件资料")
    private List<String> FuJian;

//    @XmlElement(name = "资金详情")
    private List<TableRow> tableRows;

    public ZFQTYFK(String YHMC, String YHZH, String FSE, String BZ, List<String> fuJian, List<TableRow> tableRows) {
        this.YHMC = YHMC;
        this.YHZH = YHZH;
        this.FSE = FSE;
        this.BZ = BZ;
        FuJian = fuJian;
        this.tableRows = tableRows;
    }

    public ZFQTYFK(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getYHZH() {
        return YHZH;
    }

    public void setYHZH(String YHZH) {
        this.YHZH = YHZH;
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
        return "ZFQTYFK{" +
                "YHMC='" + YHMC + '\'' +
                ", YHZH='" + YHZH + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
