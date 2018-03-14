package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款本息分解文件
 * 文件格式: 序号@|$业务明细账号@|$业务明细流水号@|$身份证号@|$合同编号@|$姓名@|$还款金额@|$贷款本金发生额@|$贷款利息发生额@|$贷款罚息发生额@|$贷款违约金发生额@|$非扣款还贷金额@|$非扣款贷款本金发生额@|$非扣款贷款利息发生额@|$非扣款贷款违约金发生额@|$非扣款贷款罚息发生额@|$摘要@|$备注@|$
 * Created by gxy on 17-8-3.
 */
public class LoanCapIntDecFile implements Serializable {
    private static final long serialVersionUID = 2288139521723378653L;
    /**
     * 序号
     */
    private String no = "";
    /**
     * 业务明细账号
     */
    private String refAcctNo = "";
    /**
     * 业务明细流水号
     */
    private String refSeqNo = "";
    /**
     * 身份证号
     */
    private String card = "";
    /**
     * 合同编号
     */
    private String contractNo = "";
    /**
     * 姓名
     */
    private String name = "";
    /**
     * 还款金额
     */
    private BigDecimal repaymentAmt;
    /**
     * 贷款本金发生额
     */
    private BigDecimal loanCapAmt;
    /**
     * 贷款利息发生额
     */
    private BigDecimal loanIntAmt;
    /**
     * 贷款罚息发生额
     */
    private BigDecimal loanPenAmt;
    /**
     * 贷款违约金发生额
     */
    private BigDecimal loanFineAmt;
    /**
     * 非扣款还贷金额
     */
    private BigDecimal unDeRepaymentAmt;
    /**
     * 非扣款贷款本金发生额
     */
    private BigDecimal unDeloanCapAmt;
    /**
     * 非扣款贷款利息发生额
     */
    private BigDecimal unDeloanIntAmt;
    /**
     * 非扣款贷款违约金发生额
     */
    private BigDecimal unDeloanFineAmt;
    /**
     * 非扣款贷款罚息发生额
     */
    private BigDecimal unDeloanPenAmt;
    /**
     * 摘要
     */
    private String summary= "";
    /**
     * 备注
     */
    private String remark= "";

    public LoanCapIntDecFile() {
    }

    public LoanCapIntDecFile(String no, String refAcctNo, String refSeqNo, String name, BigDecimal repaymentAmt, BigDecimal loanCapAmt, BigDecimal loanIntAmt, String summary) {
        this.no = no;
        this.refAcctNo = refAcctNo;
        this.refSeqNo = refSeqNo;
        this.name = name;
        this.repaymentAmt = repaymentAmt;
        this.loanCapAmt = loanCapAmt;
        this.loanIntAmt = loanIntAmt;
        this.summary = summary;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRefAcctNo() {
        return refAcctNo;
    }

    public void setRefAcctNo(String refAcctNo) {
        this.refAcctNo = refAcctNo;
    }

    public String getRefSeqNo() {
        return refSeqNo;
    }

    public void setRefSeqNo(String refSeqNo) {
        this.refSeqNo = refSeqNo;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRepaymentAmt() {
        return repaymentAmt;
    }

    public void setRepaymentAmt(BigDecimal repaymentAmt) {
        this.repaymentAmt = repaymentAmt;
    }

    public BigDecimal getLoanCapAmt() {
        return loanCapAmt;
    }

    public void setLoanCapAmt(BigDecimal loanCapAmt) {
        this.loanCapAmt = loanCapAmt;
    }

    public BigDecimal getLoanIntAmt() {
        return loanIntAmt;
    }

    public void setLoanIntAmt(BigDecimal loanIntAmt) {
        this.loanIntAmt = loanIntAmt;
    }

    public BigDecimal getLoanPenAmt() {
        return loanPenAmt;
    }

    public void setLoanPenAmt(BigDecimal loanPenAmt) {
        this.loanPenAmt = loanPenAmt;
    }

    public BigDecimal getLoanFineAmt() {
        return loanFineAmt;
    }

    public void setLoanFineAmt(BigDecimal loanFineAmt) {
        this.loanFineAmt = loanFineAmt;
    }

    public BigDecimal getUnDeRepaymentAmt() {
        return unDeRepaymentAmt;
    }

    public void setUnDeRepaymentAmt(BigDecimal unDeRepaymentAmt) {
        this.unDeRepaymentAmt = unDeRepaymentAmt;
    }

    public BigDecimal getUnDeloanCapAmt() {
        return unDeloanCapAmt;
    }

    public void setUnDeloanCapAmt(BigDecimal unDeloanCapAmt) {
        this.unDeloanCapAmt = unDeloanCapAmt;
    }

    public BigDecimal getUnDeloanIntAmt() {
        return unDeloanIntAmt;
    }

    public void setUnDeloanIntAmt(BigDecimal unDeloanIntAmt) {
        this.unDeloanIntAmt = unDeloanIntAmt;
    }

    public BigDecimal getUnDeloanFineAmt() {
        return unDeloanFineAmt;
    }

    public void setUnDeloanFineAmt(BigDecimal unDeloanFineAmt) {
        this.unDeloanFineAmt = unDeloanFineAmt;
    }

    public BigDecimal getUnDeloanPenAmt() {
        return unDeloanPenAmt;
    }

    public void setUnDeloanPenAmt(BigDecimal unDeloanPenAmt) {
        this.unDeloanPenAmt = unDeloanPenAmt;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                refAcctNo + "@|$" +
                refSeqNo + "@|$" +
                card + "@|$" +
                contractNo + "@|$" +
                name + "@|$" +
                getString(repaymentAmt) + "@|$" +
                getString(loanCapAmt) + "@|$" +
                getString(loanIntAmt) + "@|$" +
                getString(loanPenAmt) + "@|$" +
                getString(loanFineAmt) + "@|$" +
                getString(unDeRepaymentAmt) + "@|$" +
                getString(unDeloanCapAmt) + "@|$" +
                getString(unDeloanIntAmt) + "@|$" +
                getString(unDeloanFineAmt) + "@|$" +
                getString(unDeloanPenAmt) + "@|$" +
                summary + "@|$" +
                remark + "@|$";
    }

    public String getString(BigDecimal decimal) {
        return decimal != null ? decimal.toString() : "";
    }
}
