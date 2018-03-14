package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 接口探测(SYS600)输出格式
 */
public class CenterInterfaceCheckOut implements Serializable {
    private static final long serialVersionUID = 3926670518392066886L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;

    public CenterInterfaceCheckOut() {
    }

    public CenterInterfaceCheckOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "InterfaceCheckOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
