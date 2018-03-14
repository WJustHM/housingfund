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
public class ZFBXDKLX implements Serializable {

//    @XmlElement(name = "支付年月")
    private  String  ZFNY;

//    @XmlElement(name = "发生额")
    private  String FSE;

//    @XmlElement(name = "备注")
    private String BZ;

//    @XmlElement(name = "附件资料")
    private List<String> FuJian;

//    @XmlElement(name = "资金详情")
    private List<TableRow> tableRows;

    public ZFBXDKLX() {
    }

    public ZFBXDKLX(String ZFNY, String FSE, String BZ, List<String> fuJian, List<TableRow> tableRows) {
        this.ZFNY = ZFNY;
        this.FSE = FSE;
        this.BZ = BZ;
        FuJian = fuJian;
        this.tableRows = tableRows;
    }



    public String getZFNY() {
        return ZFNY;
    }

    public void setZFNY(String ZFNY) {
        this.ZFNY = ZFNY;
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
        return "ZFBXDKLX{" +
                "ZFNY='" + ZFNY + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BZ='" + BZ + '\'' +
                ", FuJian=" + FuJian +
                ", tableRows=" + tableRows +
                '}';
    }
}
