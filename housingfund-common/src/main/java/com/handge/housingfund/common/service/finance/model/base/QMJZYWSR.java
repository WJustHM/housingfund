package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "期末结转业务收入")
@XmlAccessorType(XmlAccessType.FIELD)
public class QMJZYWSR implements Serializable {

//    @XmlElement(name = "结转起始时间")
    private  String  QSSJ;

//    @XmlElement(name = "结转结束时间")
    private  String  JSSJ;

//    @XmlElement(name = "发生额")
    private  String FSE;

//    @XmlElement(name = "备注")
    private String BZ;

//    @XmlElement(name = "附件资料")
    private List<String> FuJian;

//    @XmlElement(name = "资金详情")
    private List<TableRow> tableRows;

    public QMJZYWSR() {
        this(null,null,null,null,null,null);
    }

    public QMJZYWSR(String QSSJ, String JSSJ, String FSE, String BZ, List<String> fuJian, List<TableRow> tableRows) {
        this.QSSJ = QSSJ;
        this.JSSJ = JSSJ;
        this.FSE = FSE;
        this.BZ = BZ;
        FuJian = fuJian;
        this.tableRows = tableRows;
    }

    public String getQSSJ() {
        return QSSJ;
    }

    public void setQSSJ(String QSSJ) {
        this.QSSJ = QSSJ;
    }

    public String getJSSJ() {
        return JSSJ;
    }

    public void setJSSJ(String JSSJ) {
        this.JSSJ = JSSJ;
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
        return "QMJZYWSR{" +
                "QSSJ='" + QSSJ + '\'' +
                ", JSSJ='" + JSSJ + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
