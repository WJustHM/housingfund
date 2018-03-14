package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-25.
 */
@XmlRootElement(name = "General")
@XmlAccessorType(XmlAccessType.FIELD)
public class General implements Serializable {

    @XmlElement(name = "BZ")
    private String BZ;

    @XmlElement(name = "FuJian")
    private List<Object> FuJian;

    @XmlElement(name = "tableRows")
    private List<TableRow> tableRows;

    @XmlElement(name = "FuJianShuLiang")
    private String FuJianShu;

    public General() {

    }

    public General(String BZ, List<Object> fuJian, List<TableRow> tableRows,String fujianshu) {
        this.BZ = BZ;
        this.FuJian = fuJian;
        this.tableRows = tableRows;
        this.FuJianShu = fujianshu;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public List<Object> getFuJian() {
        return FuJian;
    }

    public void setFuJian(List<Object> fuJian) {
        FuJian = fuJian;
    }

    public List<TableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
    }

    public String getFuJianShu() {
        return FuJianShu;
    }

    public void setFuJianShu(String fuJianShu) {
        FuJianShu = fuJianShu;
    }

    @Override
    public String toString() {
        return "General{" +
                "BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                ", FuJianShu='" + FuJianShu + '\'' +
                '}';
    }
}
