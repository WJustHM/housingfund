package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 接口探测(SYS600)输入格式
 */
public class CenterInterfaceCheckIn implements Serializable {
    private static final long serialVersionUID = -1295013466675699287L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;

    public CenterInterfaceCheckIn() {
    }

    public CenterInterfaceCheckIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    @Override
    public String toString() {
        return "InterfaceCheckIn{" +
                "centerHeadIn=" + centerHeadIn +
                '}';
    }
}
