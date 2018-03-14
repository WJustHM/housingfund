package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款本息分解(BDC106)输入格式
 */
public class LoanCapIntDecIn implements Serializable {
    private static final long serialVersionUID = 2176631764763794640L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 批量编号(required)
     */
    private String BatchNo;
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
    /**
     * 扣款交易还贷金额(required)
     */
    private BigDecimal BkAmt;
    /**
     * 非扣款交易还贷金额
     */
    private BigDecimal RefAmt;
    /**
     * 非扣款交易贷款本金发生额
     */
    private BigDecimal RefCapAmt;
    /**
     * 非扣款交易贷款利息发生额
     */
    private BigDecimal RefIntAmt;
    /**
     * 非扣款交易贷款罚息发生额
     */
    private BigDecimal RePenAmt;
    /**
     * 非扣款交易贷款违约金发生额
     */
    private BigDecimal ReFineAmt;
    /**
     * 银行扣款资金内部过渡户(required)
     */
    private String BankAcctNo;
    /**
     * 银行委托贷款本金账号
     */
    private String BkRefLoanCapNo;
    /**
     * 银行贷款本金内部过渡户
     */
    private String BkLoanCapInAcctNo;
    /**
     * 银行贷款利息内部过渡户
     */
    private String BkLoanIntInAcctNo;
    /**
     * 贷款本金收方账号(required)
     */
    private String LoanCapCrAcctNo;
    /**
     * 贷款本金收方户名(required)
     */
    private String LoanCapCrAcctName;
    /**
     * 贷款本金收方账户类别(required)
     */
    private String LoanCapCrAcctClass;
    /**
     * 贷款本金收方账户行名(required)
     */
    private String LoanCapCrAcctBkName;
    /**
     * 银行扣款贷款本金发生额(required)
     */
    private BigDecimal LoanCapAmt;
    /**
     * 贷款利息收方账号(required)
     */
    private String LoanIntCrAcctNo;
    /**
     * 贷款利息收方户名(required)
     */
    private String LoanIntCrAcctName;
    /**
     * 贷款利息收方账户类别(required)
     */
    private String LoanIntCrAcctClass;
    /**
     * 贷款利息收方账户行名(required)
     */
    private String LoanIntCrAcctBkName;
    /**
     * 银行扣款贷款利息发生额(required)
     */
    private BigDecimal LoanIntAmt;
    /**
     * 贷款罚息收方账号
     */
    private String LoanPenCrAcctNo;
    /**
     * 贷款罚息收方户名
     */
    private String LoanPenCrAcctName;
    /**
     * 贷款罚息收方账户类别
     */
    private String LoanPenCrAcctClass;
    /**
     * 贷款罚息收方账户行名
     */
    private String LoanPenCrAcctBkName;
    /**
     * 银行扣款贷款罚息发生额
     */
    private BigDecimal LoanPenAmt;
    /**
     * 贷款违约金收方账号
     */
    private String LoanFineCrAcctNo;
    /**
     * 贷款违约金收方户名
     */
    private String LoanFineCrAcctName;
    /**
     * 贷款违约金收方账户类别
     */
    private String LoanFineCrAcctClass;
    /**
     * 贷款违约金收方账户行名
     */
    private String LoanFineCrAcctBkName;
    /**
     * 银行扣款贷款违约金发生额
     */
    private BigDecimal LoanFineAmt;

    public LoanCapIntDecIn() {
    }

    public LoanCapIntDecIn(CenterHeadIn centerHeadIn, String batchNo, int batchTotalNum, BigDecimal batchTotalAmt, FileList fileList, BigDecimal bkAmt, String bankAcctNo, String loanCapCrAcctNo, String loanCapCrAcctName, String loanCapCrAcctClass, String loanCapCrAcctBkName, BigDecimal loanCapAmt, String loanIntCrAcctNo, String loanIntCrAcctName, String loanIntCrAcctClass, String loanIntCrAcctBkName, BigDecimal loanIntAmt) {
        this.centerHeadIn = centerHeadIn;
        BatchNo = batchNo;
        BatchTotalNum = batchTotalNum;
        BatchTotalAmt = batchTotalAmt;
        this.fileList = fileList;
        BkAmt = bkAmt;
        BankAcctNo = bankAcctNo;
        LoanCapCrAcctNo = loanCapCrAcctNo;
        LoanCapCrAcctName = loanCapCrAcctName;
        LoanCapCrAcctClass = loanCapCrAcctClass;
        LoanCapCrAcctBkName = loanCapCrAcctBkName;
        LoanCapAmt = loanCapAmt;
        LoanIntCrAcctNo = loanIntCrAcctNo;
        LoanIntCrAcctName = loanIntCrAcctName;
        LoanIntCrAcctClass = loanIntCrAcctClass;
        LoanIntCrAcctBkName = loanIntCrAcctBkName;
        LoanIntAmt = loanIntAmt;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
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

    public BigDecimal getBkAmt() {
        return BkAmt;
    }

    public void setBkAmt(BigDecimal bkAmt) {
        BkAmt = bkAmt;
    }

    public BigDecimal getRefAmt() {
        return RefAmt;
    }

    public void setRefAmt(BigDecimal refAmt) {
        RefAmt = refAmt;
    }

    public BigDecimal getRefCapAmt() {
        return RefCapAmt;
    }

    public void setRefCapAmt(BigDecimal refCapAmt) {
        RefCapAmt = refCapAmt;
    }

    public BigDecimal getRefIntAmt() {
        return RefIntAmt;
    }

    public void setRefIntAmt(BigDecimal refIntAmt) {
        RefIntAmt = refIntAmt;
    }

    public BigDecimal getRePenAmt() {
        return RePenAmt;
    }

    public void setRePenAmt(BigDecimal rePenAmt) {
        RePenAmt = rePenAmt;
    }

    public BigDecimal getReFineAmt() {
        return ReFineAmt;
    }

    public void setReFineAmt(BigDecimal reFineAmt) {
        ReFineAmt = reFineAmt;
    }

    public String getBankAcctNo() {
        return BankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        BankAcctNo = bankAcctNo;
    }

    public String getBkRefLoanCapNo() {
        return BkRefLoanCapNo;
    }

    public void setBkRefLoanCapNo(String bkRefLoanCapNo) {
        BkRefLoanCapNo = bkRefLoanCapNo;
    }

    public String getBkLoanCapInAcctNo() {
        return BkLoanCapInAcctNo;
    }

    public void setBkLoanCapInAcctNo(String bkLoanCapInAcctNo) {
        BkLoanCapInAcctNo = bkLoanCapInAcctNo;
    }

    public String getBkLoanIntInAcctNo() {
        return BkLoanIntInAcctNo;
    }

    public void setBkLoanIntInAcctNo(String bkLoanIntInAcctNo) {
        BkLoanIntInAcctNo = bkLoanIntInAcctNo;
    }

    public String getLoanCapCrAcctNo() {
        return LoanCapCrAcctNo;
    }

    public void setLoanCapCrAcctNo(String loanCapCrAcctNo) {
        LoanCapCrAcctNo = loanCapCrAcctNo;
    }

    public String getLoanCapCrAcctName() {
        return LoanCapCrAcctName;
    }

    public void setLoanCapCrAcctName(String loanCapCrAcctName) {
        LoanCapCrAcctName = loanCapCrAcctName;
    }

    public String getLoanCapCrAcctClass() {
        return LoanCapCrAcctClass;
    }

    public void setLoanCapCrAcctClass(String loanCapCrAcctClass) {
        LoanCapCrAcctClass = loanCapCrAcctClass;
    }

    public String getLoanCapCrAcctBkName() {
        return LoanCapCrAcctBkName;
    }

    public void setLoanCapCrAcctBkName(String loanCapCrAcctBkName) {
        LoanCapCrAcctBkName = loanCapCrAcctBkName;
    }

    public BigDecimal getLoanCapAmt() {
        return LoanCapAmt;
    }

    public void setLoanCapAmt(BigDecimal loanCapAmt) {
        LoanCapAmt = loanCapAmt;
    }

    public String getLoanIntCrAcctNo() {
        return LoanIntCrAcctNo;
    }

    public void setLoanIntCrAcctNo(String loanIntCrAcctNo) {
        LoanIntCrAcctNo = loanIntCrAcctNo;
    }

    public String getLoanIntCrAcctName() {
        return LoanIntCrAcctName;
    }

    public void setLoanIntCrAcctName(String loanIntCrAcctName) {
        LoanIntCrAcctName = loanIntCrAcctName;
    }

    public String getLoanIntCrAcctClass() {
        return LoanIntCrAcctClass;
    }

    public void setLoanIntCrAcctClass(String loanIntCrAcctClass) {
        LoanIntCrAcctClass = loanIntCrAcctClass;
    }

    public String getLoanIntCrAcctBkName() {
        return LoanIntCrAcctBkName;
    }

    public void setLoanIntCrAcctBkName(String loanIntCrAcctBkName) {
        LoanIntCrAcctBkName = loanIntCrAcctBkName;
    }

    public BigDecimal getLoanIntAmt() {
        return LoanIntAmt;
    }

    public void setLoanIntAmt(BigDecimal loanIntAmt) {
        LoanIntAmt = loanIntAmt;
    }

    public String getLoanPenCrAcctNo() {
        return LoanPenCrAcctNo;
    }

    public void setLoanPenCrAcctNo(String loanPenCrAcctNo) {
        LoanPenCrAcctNo = loanPenCrAcctNo;
    }

    public String getLoanPenCrAcctName() {
        return LoanPenCrAcctName;
    }

    public void setLoanPenCrAcctName(String loanPenCrAcctName) {
        LoanPenCrAcctName = loanPenCrAcctName;
    }

    public String getLoanPenCrAcctClass() {
        return LoanPenCrAcctClass;
    }

    public void setLoanPenCrAcctClass(String loanPenCrAcctClass) {
        LoanPenCrAcctClass = loanPenCrAcctClass;
    }

    public String getLoanPenCrAcctBkName() {
        return LoanPenCrAcctBkName;
    }

    public void setLoanPenCrAcctBkName(String loanPenCrAcctBkName) {
        LoanPenCrAcctBkName = loanPenCrAcctBkName;
    }

    public BigDecimal getLoanPenAmt() {
        return LoanPenAmt;
    }

    public void setLoanPenAmt(BigDecimal loanPenAmt) {
        LoanPenAmt = loanPenAmt;
    }

    public String getLoanFineCrAcctNo() {
        return LoanFineCrAcctNo;
    }

    public void setLoanFineCrAcctNo(String loanFineCrAcctNo) {
        LoanFineCrAcctNo = loanFineCrAcctNo;
    }

    public String getLoanFineCrAcctName() {
        return LoanFineCrAcctName;
    }

    public void setLoanFineCrAcctName(String loanFineCrAcctName) {
        LoanFineCrAcctName = loanFineCrAcctName;
    }

    public String getLoanFineCrAcctClass() {
        return LoanFineCrAcctClass;
    }

    public void setLoanFineCrAcctClass(String loanFineCrAcctClass) {
        LoanFineCrAcctClass = loanFineCrAcctClass;
    }

    public String getLoanFineCrAcctBkName() {
        return LoanFineCrAcctBkName;
    }

    public void setLoanFineCrAcctBkName(String loanFineCrAcctBkName) {
        LoanFineCrAcctBkName = loanFineCrAcctBkName;
    }

    public BigDecimal getLoanFineAmt() {
        return LoanFineAmt;
    }

    public void setLoanFineAmt(BigDecimal loanFineAmt) {
        LoanFineAmt = loanFineAmt;
    }

    @Override
    public String toString() {
        return "LoanCapIntDecIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", BatchNo='" + BatchNo + '\'' +
                ", BatchTotalNum=" + BatchTotalNum +
                ", BatchTotalAmt=" + BatchTotalAmt +
                ", fileList=" + fileList +
                ", Remark='" + Remark + '\'' +
                ", BkAmt=" + BkAmt +
                ", RefAmt=" + RefAmt +
                ", RefCapAmt=" + RefCapAmt +
                ", RefIntAmt=" + RefIntAmt +
                ", RePenAmt=" + RePenAmt +
                ", ReFineAmt=" + ReFineAmt +
                ", BankAcctNo='" + BankAcctNo + '\'' +
                ", BkRefLoanCapNo='" + BkRefLoanCapNo + '\'' +
                ", BkLoanCapInAcctNo='" + BkLoanCapInAcctNo + '\'' +
                ", BkLoanIntInAcctNo='" + BkLoanIntInAcctNo + '\'' +
                ", LoanCapCrAcctNo='" + LoanCapCrAcctNo + '\'' +
                ", LoanCapCrAcctName='" + LoanCapCrAcctName + '\'' +
                ", LoanCapCrAcctClass='" + LoanCapCrAcctClass + '\'' +
                ", LoanCapCrAcctBkName='" + LoanCapCrAcctBkName + '\'' +
                ", LoanCapAmt=" + LoanCapAmt +
                ", LoanIntCrAcctNo='" + LoanIntCrAcctNo + '\'' +
                ", LoanIntCrAcctName='" + LoanIntCrAcctName + '\'' +
                ", LoanIntCrAcctClass='" + LoanIntCrAcctClass + '\'' +
                ", LoanIntCrAcctBkName='" + LoanIntCrAcctBkName + '\'' +
                ", LoanIntAmt=" + LoanIntAmt +
                ", LoanPenCrAcctNo='" + LoanPenCrAcctNo + '\'' +
                ", LoanPenCrAcctName='" + LoanPenCrAcctName + '\'' +
                ", LoanPenCrAcctClass='" + LoanPenCrAcctClass + '\'' +
                ", LoanPenCrAcctBkName='" + LoanPenCrAcctBkName + '\'' +
                ", LoanPenAmt=" + LoanPenAmt +
                ", LoanFineCrAcctNo='" + LoanFineCrAcctNo + '\'' +
                ", LoanFineCrAcctName='" + LoanFineCrAcctName + '\'' +
                ", LoanFineCrAcctClass='" + LoanFineCrAcctClass + '\'' +
                ", LoanFineCrAcctBkName='" + LoanFineCrAcctBkName + '\'' +
                ", LoanFineAmt=" + LoanFineAmt +
                '}';
    }
}
