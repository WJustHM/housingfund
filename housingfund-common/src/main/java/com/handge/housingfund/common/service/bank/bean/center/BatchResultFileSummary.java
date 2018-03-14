package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量业务结果文件汇总行
 * 汇总行:$日期|交易机构号|批号|类别|类型|币种|钞汇|总笔数|总金额|成功笔数|成功金额|失败笔数|失败金额|摘要|
 * Created by gxy on 17-8-3.
 */
public class BatchResultFileSummary implements Serializable {
    private static final long serialVersionUID = -8569968036370615934L;
    /**
     * 日期
     */
    private String date;
    /**
     * 交易机构号
     */
    private String txUnitNo;
    /**
     * 批号
     */
    private String batchNo;
    /**
     * 类别
     */
    private String category;
    /**
     * 类型
     */
    private String type;
    /**
     * 币种
     */
    private String currNo;
    /**
     * 钞汇
     */
    private String currIden;
    /**
     * 总笔数
     */
    private String batchTotalNum;
    /**
     * 总金额
     */
    private BigDecimal batchTotalAmt;
    /**
     * 成功笔数
     */
    private String successTotal;
    /**
     * 成功金额
     */
    private BigDecimal successAmt;
    /**
     * 失败笔数
     */
    private String failedTotal;
    /**
     * 失败金额
     */
    private BigDecimal failedAmt;
    /**
     * 摘要
     */
    private String summary;

    public BatchResultFileSummary() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date.substring(1);
    }

    public String getTxUnitNo() {
        return txUnitNo;
    }

    public void setTxUnitNo(String txUnitNo) {
        this.txUnitNo = txUnitNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrNo() {
        return currNo;
    }

    public void setCurrNo(String currNo) {
        this.currNo = currNo;
    }

    public String getCurrIden() {
        return currIden;
    }

    public void setCurrIden(String currIden) {
        this.currIden = currIden;
    }

    public String getBatchTotalNum() {
        return batchTotalNum;
    }

    public void setBatchTotalNum(String batchTotalNum) {
        this.batchTotalNum = batchTotalNum;
    }

    public BigDecimal getBatchTotalAmt() {
        return batchTotalAmt;
    }

    public void setBatchTotalAmt(BigDecimal batchTotalAmt) {
        this.batchTotalAmt = batchTotalAmt;
    }

    public String getSuccessTotal() {
        return successTotal;
    }

    public void setSuccessTotal(String successTotal) {
        this.successTotal = successTotal;
    }

    public BigDecimal getSuccessAmt() {
        return successAmt;
    }

    public void setSuccessAmt(BigDecimal successAmt) {
        this.successAmt = successAmt;
    }

    public String getFailedTotal() {
        return failedTotal;
    }

    public void setFailedTotal(String failedTotal) {
        this.failedTotal = failedTotal;
    }

    public BigDecimal getFailedAmt() {
        return failedAmt;
    }

    public void setFailedAmt(BigDecimal failedAmt) {
        this.failedAmt = failedAmt;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BatchResultFileSummary{" +
                "date='" + date + '\'' +
                ", txUnitNo='" + txUnitNo + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", currNo='" + currNo + '\'' +
                ", currIden='" + currIden + '\'' +
                ", batchTotalNum='" + batchTotalNum + '\'' +
                ", batchTotalAmt=" + batchTotalAmt +
                ", successTotal='" + successTotal + '\'' +
                ", successAmt=" + successAmt +
                ", failedTotal='" + failedTotal + '\'' +
                ", failedAmt=" + failedAmt +
                ", summary='" + summary + '\'' +
                '}';
    }
}
