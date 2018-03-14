package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 转移接续平台公积金中心代码查询(BDC909)输入格式
 */
public class CRFCenterCodeQueryIn implements Serializable {
    private static final long serialVersionUID = -8128113451405646937L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 公积金中心机构编号, 参见公积金中心代码表的机构代码
     */
    private String UnitNo;
    /**
     * 公积金中心机构名称
     */
    private String UnitName;

    public CRFCenterCodeQueryIn() {
    }

    public CRFCenterCodeQueryIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    @Override
    public String toString() {
        return "CRFCenterCodeQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", UnitNo='" + UnitNo + '\'' +
                ", UnitName='" + UnitName + '\'' +
                '}';
    }
}
