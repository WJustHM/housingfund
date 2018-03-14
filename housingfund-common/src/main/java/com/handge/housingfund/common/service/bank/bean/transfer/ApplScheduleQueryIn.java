package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 申请进度查询(BDC904)输入格式
 */
public class ApplScheduleQueryIn implements Serializable {
    private static final long serialVersionUID = -8814748484450964224L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 原联系函编号(required), 要求使用转入申请的联系函编号
     */
    private String OrConNum;
    /**
     * 原交易类型(required), 1-单笔 2-批量
     */
    private String OrTxFunc;
    /**
     * 转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码
     */
    private String TranOutUnitNo;
    /**
     * 备用
     */
    private String Bak1;
    /**
     * 备用
     */
    private String Bak2;

    public ApplScheduleQueryIn() {
    }

    public ApplScheduleQueryIn(CenterHeadIn centerHeadIn, String orConNum, String orTxFunc, String tranOutUnitNo) {
        this.centerHeadIn = centerHeadIn;
        OrConNum = orConNum;
        OrTxFunc = orTxFunc;
        TranOutUnitNo = tranOutUnitNo;
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

    public String getTranOutUnitNo() {
        return TranOutUnitNo;
    }

    public void setTranOutUnitNo(String tranOutUnitNo) {
        TranOutUnitNo = tranOutUnitNo;
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
        return "ApplScheduleQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", OrConNum='" + OrConNum + '\'' +
                ", OrTxFunc='" + OrTxFunc + '\'' +
                ", TranOutUnitNo='" + TranOutUnitNo + '\'' +
                ", Bak1='" + Bak1 + '\'' +
                ", Bak2='" + Bak2 + '\'' +
                '}';
    }
}
