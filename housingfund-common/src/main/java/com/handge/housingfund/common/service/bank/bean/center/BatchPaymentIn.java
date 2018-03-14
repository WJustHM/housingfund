package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量付款(BDC103)输入格式
 */
public class BatchPaymentIn implements Serializable {
    private static final long serialVersionUID = -3797246973858983642L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 文件类型(required)
     */
    private String FileType;
    /**
     * 业务类型(required)
     */
    private String BusType;
    /**
     * 批量项目编号
     */
    private String BatchPrjNo;
    /**
     * 付方账号
     */
    private String DeAcctNo;
    /**
     * 付方户名
     */
    private String DeAcctName;
    /**
     * 付方账户类别
     */
    private String DeAcctClass;
    /**
     * 付款本金金额(required)
     */
    private BigDecimal BatchCapAmt;
    /**
     * 付方利息户账号
     */
    private String DeIntAcctNo;
    /**
     * 付方利息户户名
     */
    private String DeIntAcctName;
    /**
     * 付方利息户类别
     */
    private String DeIntAcctClass;
    /**
     * 利息收方账号
     */
    private String DeIntCrAcct; 
    /**
     * 总利息金额
     */
    private BigDecimal BatchIntAmt;
    /**
     * 总笔数(required)
     */
    private int BatchTotalNum;
    /**
     * 总金额(required)
     */
    private BigDecimal BatchTotalAmt;
    /**
     * 文件列表(required)
     */
    private FileList fileList;
    /**
     * 备注
     */
    private String Remark;

    public BatchPaymentIn() {
    }

    public BatchPaymentIn(CenterHeadIn centerHeadIn, String fileType, String busType, BigDecimal batchCapAmt, int batchTotalNum, BigDecimal batchTotalAmt, FileList fileList) {
        this.centerHeadIn = centerHeadIn;
        FileType = fileType;
        BusType = busType;
        BatchCapAmt = batchCapAmt;
        BatchTotalNum = batchTotalNum;
        BatchTotalAmt = batchTotalAmt;
        this.fileList = fileList;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        BusType = busType;
    }

    public String getBatchPrjNo() {
        return BatchPrjNo;
    }

    public void setBatchPrjNo(String batchPrjNo) {
        BatchPrjNo = batchPrjNo;
    }

    public String getDeAcctNo() {
        return DeAcctNo;
    }

    public void setDeAcctNo(String deAcctNo) {
        DeAcctNo = deAcctNo;
    }

    public String getDeAcctName() {
        return DeAcctName;
    }

    public void setDeAcctName(String deAcctName) {
        DeAcctName = deAcctName;
    }

    public String getDeAcctClass() {
        return DeAcctClass;
    }

    public void setDeAcctClass(String deAcctClass) {
        DeAcctClass = deAcctClass;
    }

    public BigDecimal getBatchCapAmt() {
        return BatchCapAmt;
    }

    public void setBatchCapAmt(BigDecimal batchCapAmt) {
        BatchCapAmt = batchCapAmt;
    }

    public String getDeIntAcctNo() {
        return DeIntAcctNo;
    }

    public void setDeIntAcctNo(String deIntAcctNo) {
        DeIntAcctNo = deIntAcctNo;
    }

    public String getDeIntAcctName() {
        return DeIntAcctName;
    }

    public void setDeIntAcctName(String deIntAcctName) {
        DeIntAcctName = deIntAcctName;
    }

    public String getDeIntAcctClass() {
        return DeIntAcctClass;
    }

    public void setDeIntAcctClass(String deIntAcctClass) {
        DeIntAcctClass = deIntAcctClass;
    }

    public String getDeIntCrAcct() {
        return DeIntCrAcct;
    }

    public void setDeIntCrAcct(String deIntCrAcct) {
        DeIntCrAcct = deIntCrAcct;
    }

    public BigDecimal getBatchIntAmt() {
        return BatchIntAmt;
    }

    public void setBatchIntAmt(BigDecimal batchIntAmt) {
        BatchIntAmt = batchIntAmt;
    }

    public int getBatchTotalNum() {
        return BatchTotalNum;
    }

    public void setBatchTotalNum(int batchTotalNum) {
        BatchTotalNum = batchTotalNum;
    }

    public BigDecimal getBatchTotalAmt() {
        return BatchTotalAmt;
    }

    public void setBatchTotalAmt(BigDecimal batchTotalAmt) {
        BatchTotalAmt = batchTotalAmt;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return "BatchPaymentIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", FileType='" + FileType + '\'' +
                ", BusType='" + BusType + '\'' +
                ", BatchPrjNo=" + BatchPrjNo +
                ", DeAcctNo='" + DeAcctNo + '\'' +
                ", DeAcctName='" + DeAcctName + '\'' +
                ", DeAcctClass='" + DeAcctClass + '\'' +
                ", BatchCapAmt=" + BatchCapAmt +
                ", DeIntAcctNo='" + DeIntAcctNo + '\'' +
                ", DeIntAcctName='" + DeIntAcctName + '\'' +
                ", DeIntAcctClass='" + DeIntAcctClass + '\'' +
                ", DeIntCrAcct='" + DeIntCrAcct + '\'' +
                ", BatchIntAmt=" + BatchIntAmt +
                ", BatchTotalNum=" + BatchTotalNum +
                ", BatchTotalAmt=" + BatchTotalAmt +
                ", fileList=" + fileList +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
