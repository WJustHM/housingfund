package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;
import java.util.List;

/**
 * 联行号查询(BDC112)输出格式
 */
public class ChgNoQueryOut implements Serializable {
    private static final long serialVersionUID = 9091382511431538281L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 联行号列表(repeated)
     */
    private List<ChgNoList> chgNoList;

    public ChgNoQueryOut() {
    }

    public ChgNoQueryOut(CenterHeadOut centerHeadOut, List<ChgNoList> chgNoList) {
        this.centerHeadOut = centerHeadOut;
        this.chgNoList = chgNoList;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public List<ChgNoList> getChgNoList() {
        return chgNoList;
    }

    public void setChgNoList(List<ChgNoList> chgNoList) {
        this.chgNoList = chgNoList;
    }

    @Override
    public String toString() {
        return "ChgNoQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", chgNoList=" + chgNoList +
                '}';
    }
}
