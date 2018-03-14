package com.handge.housingfund.common.service.finance.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-22.
 */
@XmlRootElement(name = "资金详细")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableRow implements Serializable {

    private String rowName;
    private boolean JFKM;
    private String debitAmount;
    private String creditAmount;

    public TableRow() {
        this(null, true, "0", "0");
    }

    public TableRow(String rowName, boolean JFKM, String debitAmount, String creditAmount) {
        this.rowName = rowName;
        this.JFKM = JFKM;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public boolean isJFKM() {
        return JFKM;
    }

    public void setJFKM(boolean JFKM) {
        this.JFKM = JFKM;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "rowName='" + rowName + '\'' +
                ", JFKM=" + JFKM +
                ", debitAmount='" + debitAmount + '\'' +
                ", creditAmount='" + creditAmount + '\'' +
                '}';
    }
}
