package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuefei_wang on 17-9-7.
 */

@XmlRootElement(name = "ReportTable")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportTable implements Serializable{

    private String reportName;

    private String dw;

    private String period;

    private String financeManager;

    private String financeUnit ;

    private String review;

    private String operator;

    private String crateTime;

    private List<Table> table;


    public static class Table implements Serializable{

        private String tableName ;

        public Table(String tableName) {
            this.tableName = tableName;
        }

        public Table() {
           this("未定义");
        }

        private List<HashMap<String , String>> rows = new ArrayList<>();

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<HashMap<String, String>> getRows() {
            return rows;
        }

        public void setRows(List<HashMap<String, String>> rows) {
            this.rows = rows;
        }

        public Table addRow(HashMap<String, String> row){
            this.rows.add(row);
            return this;
        }

        public Table copy(){
            Table t = new Table();
            t.setRows(rows);
            t.setTableName(tableName);
            return t;
        }
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFinanceManager() {
        return financeManager;
    }

    public void setFinanceManager(String financeManager) {
        this.financeManager = financeManager;
    }

    public String getFinanceUnit() {
        return financeUnit;
    }

    public void setFinanceUnit(String financeUnit) {
        this.financeUnit = financeUnit;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(String crateTime) {
        this.crateTime = crateTime;
    }

    public List<Table> getTable() {
        return table;
    }

    public void setTable(List<Table> table) {
        this.table = table;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String toString() {
        return "ReportTable{" +
                "reportName='" + reportName + '\'' +
                ", period='" + period + '\'' +
                ", financeManager='" + financeManager + '\'' +
                ", financeUnit='" + financeUnit + '\'' +
                ", review='" + review + '\'' +
                ", operator='" + operator + '\'' +
                ", crateTime='" + crateTime + '\'' +
                ", table=" + table +
                '}';
    }
}
