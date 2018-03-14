package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 转入申请撤销(BDC903)输入格式
 */
public class TransInApplCancelIn implements Serializable {
    private static final long serialVersionUID = 8997065253781207444L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 原联系函编号或原批量编号(required)
     */
    private String OrConNum;
    /**
     * 原交易类型(required), 1: 单笔 2: 批量
     */
    private String OrTxFunc;
    /**
     * 联系函编号(required), 当选择原交易类型为2时, 申请批量中某一笔撤销就填写对应的申请编号, 如不填, 默认整批撤销: 选择原交易类型为1时, 不填
     */
    private String SubConNum;
    /**
     * 转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码
     */
    private String TranOutUnitNo;
    /**
     * 转出公积金中心名称(required)
     */
    private String TranCenName;
    /**
     * 职工姓名(required)
     */
    private String EmpName;
    /**
     * 职工证件类型(required), 依据贯标标准代码值:01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他
     */
    private String DocType;
    /**
     * 职工证件号码(required)
     */
    private String IdNum;
    /**
     * 备注
     */
    private String Remark;
    /**
     * 备用
     */
    private String Bak1;
    /**
     * 备用
     */
    private String Bak2;

    public TransInApplCancelIn() {
    }

    public TransInApplCancelIn(CenterHeadIn centerHeadIn, String orConNum, String orTxFunc, String tranOutUnitNo, String tranCenName, String empName, String docType, String idNum) {
        this.centerHeadIn = centerHeadIn;
        OrConNum = orConNum;
        OrTxFunc = orTxFunc;
        TranOutUnitNo = tranOutUnitNo;
        TranCenName = tranCenName;
        EmpName = empName;
        DocType = docType;
        IdNum = idNum;
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

    public String getOrTxFunc() {
        return OrTxFunc;
    }

    public void setOrTxFunc(String orTxFunc) {
        OrTxFunc = orTxFunc;
    }

    public String getSubConNum() {
        return SubConNum;
    }

    public void setSubConNum(String subConNum) {
        SubConNum = subConNum;
    }

    public String getTranOutUnitNo() {
        return TranOutUnitNo;
    }

    public void setTranOutUnitNo(String tranOutUnitNo) {
        TranOutUnitNo = tranOutUnitNo;
    }

    public String getTranCenName() {
        return TranCenName;
    }

    public void setTranCenName(String tranCenName) {
        TranCenName = tranCenName;
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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
        return "TranInApplCancelIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", OrConNum='" + OrConNum + '\'' +
                ", OrTxFunc='" + OrTxFunc + '\'' +
                ", SubConNum='" + SubConNum + '\'' +
                ", TranOutUnitNo='" + TranOutUnitNo + '\'' +
                ", TranCenName='" + TranCenName + '\'' +
                ", EmpName='" + EmpName + '\'' +
                ", DocType='" + DocType + '\'' +
                ", IdNum='" + IdNum + '\'' +
                ", Remark='" + Remark + '\'' +
                ", Bak1='" + Bak1 + '\'' +
                ", Bak2='" + Bak2 + '\'' +
                '}';
    }
}
