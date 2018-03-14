package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "结算公积金利息")
@XmlAccessorType(XmlAccessType.FIELD)
public class JSGJJLX implements Serializable {

    @XmlElement(name = "JSNF")
    private  String  JSNF;

    @XmlElement(name = "FSE")
    private  String FSE;

    @XmlElement(name = "BZ")
    private String BZ;

    @XmlElement(name = "FuJian")
    private List<String> FuJian;

    @XmlElement(name = "tableRows")
    private List<TableRow> tableRows;


    public JSGJJLX() {

    }

    public JSGJJLX(String JSNF, String FSE, String BZ, List<String> fuJian, List<TableRow> tableRows) {
        this.JSNF = JSNF;
        this.FSE = FSE;
        this.BZ = BZ;
        FuJian = fuJian;
        this.tableRows = tableRows;
    }

    public String getJSNF() {
        return JSNF;
    }

    public void setJSNF(String JSNF) {
        this.JSNF = JSNF;
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
        return "JSGJJLX{" +
                "JSNF='" + JSNF + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
