package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量收款(BDC104)输入格式
 */
public class BatchCollectionIn implements Serializable {
    private static final long serialVersionUID = -7889277587550527558L;
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
     * 收方账号
     */
    private String CrAcctNo;
    /**
     * 收方户名
     */
    private String CrAcctName;
    /**
     * 收方账户类别
     */
    private String CrAcctClass;
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

    public BatchCollectionIn() {
    }

    public BatchCollectionIn(CenterHeadIn centerHeadIn, String fileType, String busType, int batchTotalNum, BigDecimal batchTotalAmt, FileList fileList) {
        this.centerHeadIn = centerHeadIn;
        FileType = fileType;
        BusType = busType;
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

    public String getCrAcctNo() {
        return CrAcctNo;
    }

    public void setCrAcctNo(String crAcctNo) {
        CrAcctNo = crAcctNo;
    }

    public String getCrAcctName() {
        return CrAcctName;
    }

    public void setCrAcctName(String crAcctName) {
        CrAcctName = crAcctName;
    }

    public String getCrAcctClass() {
        return CrAcctClass;
    }

    public void setCrAcctClass(String crAcctClass) {
        CrAcctClass = crAcctClass;
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
        return "BatchCollectionIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", FileType='" + FileType + '\'' +
                ", BusType='" + BusType + '\'' +
                ", BatchPrjNo=" + BatchPrjNo +
                ", CrAcctNo='" + CrAcctNo + '\'' +
                ", CrAcctName='" + CrAcctName + '\'' +
                ", CrAcctClass='" + CrAcctClass + '\'' +
                ", BatchTotalNum=" + BatchTotalNum +
                ", BatchTotalAmt='" + BatchTotalAmt + '\'' +
                ", fileList=" + fileList +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
