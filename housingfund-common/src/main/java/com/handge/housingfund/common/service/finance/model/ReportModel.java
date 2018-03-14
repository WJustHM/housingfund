package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Liujuhao on 2017/11/8.
 */

/*该Model用于“全市归集贷款情况表”的前端查询*/

@XmlRootElement(name = "ReportModel")
@XmlAccessorType(XmlAccessType.FIELD)

public class ReportModel implements Serializable{

    private BigDecimal total;

    private HashMap<String, HashMap<String, BigDecimal>> results;

    public HashMap<String, HashMap<String, BigDecimal>> getResults() {
        return results;
    }

    public void setResults(HashMap<String, HashMap<String, BigDecimal>> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "total=" + total +
                ", results=" + results +
                '}';
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}

/*class Months {

    private String name;

    private ArrayList<Areas> areas;
}

class Areas {

    private String name;

    private String count;
}*/
