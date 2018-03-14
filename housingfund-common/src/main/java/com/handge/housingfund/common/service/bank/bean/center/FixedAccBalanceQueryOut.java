package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;
import java.util.List;

/**
 * 定期账户余额查询(BDC122)出输格式
 */
public class FixedAccBalanceQueryOut implements Serializable {
    private static final long serialVersionUID = 6196756123950041865L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 总笔数(required)
     */
    private int Num;
    /**
     * 结果列表(repeated)
     */
    private List<BDC122Summary> SUMMARY;

    public FixedAccBalanceQueryOut() {
    }

    public FixedAccBalanceQueryOut(CenterHeadOut centerHeadOut, int num, List<BDC122Summary> SUMMARY) {
        this.centerHeadOut = centerHeadOut;
        Num = num;
        this.SUMMARY = SUMMARY;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public List<BDC122Summary> getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(List<BDC122Summary> SUMMARY) {
        this.SUMMARY = SUMMARY;
    }

    @Override
    public String toString() {
        return "FixedAccBalanceQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", Num=" + Num +
                ", SUMMARY=" + SUMMARY +
                '}';
    }
}
