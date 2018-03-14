package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 单笔转出信息(BDC905)输入格式
 */
public class SingleTransOutInfoIn implements Serializable {
    private static final long serialVersionUID = 8788715444017623531L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 原联系函编号(required), 要求使用转入申请的联系函编号
     */
    private String OrConNum;
    /**
     * 交易类型(required)
     * 0-新增信息转出
     * 1-修改转出信息
     * 2-转入确认办结
     * 0新增是用于新增的转移接续【转出发起】
     * 1修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】
     * 2转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】
     */
    private String TxFunc;
    /**
     * 职工姓名(required)
     */
    private String EmpName;
    /**
     * 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他
     */
    private String DocType;
    /**
     * 职工证件号码(required)
     */
    private String IdNum;
    /**
     * 转移金额(required), 转移金额=本金金额+利息金额 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String TranAmt;
    /**
     * 本金金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String OrAcctAmt;
    /**
     * 利息金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String Yinter;
    /**
     * 开户日期(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String OAcctDate;
    /**
     * 缴至年月(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String PayEndDate;
    /**
     * 缴至月份之前6个月是否连续缴存(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String SixMconFlag;
    /**
     * 在转出地使用住房公积金贷款次数(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String LoanNum;
    /**
     * 是否有未结清的公积金贷款(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String LoanEndFlag;
    /**
     * 是否存在骗提骗贷行为, 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空
     */
    private String FrLoanFlag;
    /**
     * 反馈信息代码, 0-已完成 1-失败 2-处理中 当交易类型为2-反馈结果时, 该项必填, 其他可为空
     */
    private String TranRstCode;
    /**
     * 反馈信息
     */
    private String TranRstMsg;
    /**
     * 业务办理联系电话(required), 固定电话：区号-电话号码 手机：11位号码
     */
    private String ContPhone;
    /**
     * 备用
     */
    private String Bak1;
    /**
     * 备用
     */
    private String Bak2;

    public SingleTransOutInfoIn() {
    }

    public SingleTransOutInfoIn(CenterHeadIn centerHeadIn, String orConNum, String txFunc, String empName, String docType, String idNum, String tranAmt, String orAcctAmt, String yinter, String OAcctDate, String payEndDate, String sixMconFlag, String loanNum, String loanEndFlag, String contPhone) {
        this.centerHeadIn = centerHeadIn;
        OrConNum = orConNum;
        TxFunc = txFunc;
        EmpName = empName;
        DocType = docType;
        IdNum = idNum;
        TranAmt = tranAmt;
        OrAcctAmt = orAcctAmt;
        Yinter = yinter;
        this.OAcctDate = OAcctDate;
        PayEndDate = payEndDate;
        SixMconFlag = sixMconFlag;
        LoanNum = loanNum;
        LoanEndFlag = loanEndFlag;
        ContPhone = contPhone;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getOrConNum() {
        return OrConNum;
    }

    public void setOrConNum(String orConNum) {
        OrConNum = orConNum;
    }

    public String getTxFunc() {
        return TxFunc;
    }

    public void setTxFunc(String txFunc) {
        TxFunc = txFunc;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    public String getIdNum() {
        return IdNum;
    }

    public void setIdNum(String idNum) {
        IdNum = idNum;
    }

    public String getTranAmt() {
        return TranAmt;
    }

    public void setTranAmt(String tranAmt) {
        TranAmt = tranAmt;
    }

    public String getOrAcctAmt() {
        return OrAcctAmt;
    }

    public void setOrAcctAmt(String orAcctAmt) {
        OrAcctAmt = orAcctAmt;
    }

    public String getYinter() {
        return Yinter;
    }

    public void setYinter(String yinter) {
        Yinter = yinter;
    }

    public String getOAcctDate() {
        return OAcctDate;
    }

    public void setOAcctDate(String OAcctDate) {
        this.OAcctDate = OAcctDate;
    }

    public String getPayEndDate() {
        return PayEndDate;
    }

    public void setPayEndDate(String payEndDate) {
        PayEndDate = payEndDate;
    }

    public String getSixMconFlag() {
        return SixMconFlag;
    }

    public void setSixMconFlag(String sixMconFlag) {
        SixMconFlag = sixMconFlag;
    }

    public String getLoanNum() {
        return LoanNum;
    }

    public void setLoanNum(String loanNum) {
        LoanNum = loanNum;
    }

    public String getLoanEndFlag() {
        return LoanEndFlag;
    }

    public void setLoanEndFlag(String loanEndFlag) {
        LoanEndFlag = loanEndFlag;
    }

    public String getFrLoanFlag() {
        return FrLoanFlag;
    }

    public void setFrLoanFlag(String frLoanFlag) {
        FrLoanFlag = frLoanFlag;
    }

    public String getTranRstCode() {
        return TranRstCode;
    }

    public void setTranRstCode(String tranRstCode) {
        TranRstCode = tranRstCode;
    }

    public String getTranRstMsg() {
        return TranRstMsg;
    }

    public void setTranRstMsg(String tranRstMsg) {
        TranRstMsg = tranRstMsg;
    }

    public String getContPhone() {
        return ContPhone;
    }

    public void setContPhone(String contPhone) {
        ContPhone = contPhone;
    }

    public String getBak1() {
        return Bak1;
    }

    public void setBak1(String bak1) {
        Bak1 = bak1;
    }

    public String getBak2() {
        return Bak2;
    }

    public void setBak2(String bak2) {
        Bak2 = bak2;
    }

    @Override
    public String toString() {
        return "SingleTranOutApplIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", OrConNum='" + OrConNum + '\'' +
                ", TxFunc='" + TxFunc + '\'' +
                ", EmpName='" + EmpName + '\'' +
                ", DocType='" + DocType + '\'' +
                ", IdNum='" + IdNum + '\'' +
                ", TranAmt='" + TranAmt + '\'' +
                ", OrAcctAmt='" + OrAcctAmt + '\'' +
                ", Yinter='" + Yinter + '\'' +
                ", OAcctDate='" + OAcctDate + '\'' +
                ", PayEndDate='" + PayEndDate + '\'' +
                ", SixMconFlag='" + SixMconFlag + '\'' +
                ", LoanNum='" + LoanNum + '\'' +
                ", LoanEndFlag='" + LoanEndFlag + '\'' +
                ", FrLoanFlag='" + FrLoanFlag + '\'' +
                ", TranRstCode='" + TranRstCode + '\'' +
                ", TranRstMsg='" + TranRstMsg + '\'' +
                ", ContPhone='" + ContPhone + '\'' +
                ", Bak1='" + Bak1 + '\'' +
                ", Bak2='" + Bak2 + '\'' +
                '}';
    }
}
