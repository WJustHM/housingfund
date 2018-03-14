package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "跨行转账")
@XmlAccessorType(XmlAccessType.FIELD)
public class KHZZ implements Serializable {

    //    @XmlElement(name = "付方银行名称")
    private String FFYHMC;

    //    @XmlElement(name = "付方银行账号")
    private String FFYHZH;

    //    @XmlElement(name = "付方账户户名")
    private String FFZHHM;

    //    @XmlElement(name = "收方银行名称")
    private String SFYHMC;

    //    @XmlElement(name = "收方银行账号")
    private String SFYHZH;

    //    @XmlElement(name = "收方账户户名")
    private String SFZHHM;

    //    @XmlElement(name = "发生额")
    private String FSE;

    //    @XmlElement(name = "备注")
    private String BZ;

    //    @XmlElement(name = "附件资料")
    private List<String> FuJian;

    //    @XmlElement(name = "资金详情")
    private List<TableRow> tableRows;

    public KHZZ() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    public KHZZ(String FFYHMC, String FFYHZH, String FFZHHM, String SFYHMC, String SFYHZH, String SFZHHM, String FSE, String BZ, List<String> fuJian, List<TableRow> tableRows) {
        this.FFYHMC = FFYHMC;
        this.FFYHZH = FFYHZH;
        this.FFZHHM = FFZHHM;
        this.SFYHMC = SFYHMC;
        this.SFYHZH = SFYHZH;
        this.SFZHHM = SFZHHM;
        this.FSE = FSE;
        this.BZ = BZ;
        FuJian = fuJian;
        this.tableRows = tableRows;
    }

    public String getFFYHMC() {
        return FFYHMC;
    }

    public void setFFYHMC(String FFYHMC) {
        this.FFYHMC = FFYHMC;
    }

    public String getFFYHZH() {
        return FFYHZH;
    }

    public void setFFYHZH(String FFYHZH) {
        this.FFYHZH = FFYHZH;
    }

    public String getSFYHMC() {
        return SFYHMC;
    }

    public void setSFYHMC(String SFYHMC) {
        this.SFYHMC = SFYHMC;
    }

    public String getSFYHZH() {
        return SFYHZH;
    }

    public void setSFYHZH(String SFYHZH) {
        this.SFYHZH = SFYHZH;
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

    public String getFFZHHM() {
        return FFZHHM;
    }

    public void setFFZHHM(String FFZHHM) {
        this.FFZHHM = FFZHHM;
    }

    public String getSFZHHM() {
        return SFZHHM;
    }

    public void setSFZHHM(String SFZHHM) {
        this.SFZHHM = SFZHHM;
    }

    @Override
    public String toString() {
        return "KHZZ{" +
                "FFYHMC='" + FFYHMC + '\'' +
                ", FFYHZH='" + FFYHZH + '\'' +
                ", FFZHHM='" + FFZHHM + '\'' +
                ", SFYHMC='" + SFYHMC + '\'' +
                ", SFYHZH='" + SFYHZH + '\'' +
                ", SFZHHM='" + SFZHHM + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
