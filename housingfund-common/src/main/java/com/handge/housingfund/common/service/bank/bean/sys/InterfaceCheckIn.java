package com.handge.housingfund.common.service.bank.bean.sys;

import com.handge.housingfund.common.service.bank.bean.head.SysHeadIn;

import java.io.Serializable;

/**
 * 接口探测(SYS600)输入格式,数据应用系统发起
 */
public class InterfaceCheckIn implements Serializable {
    private static final long serialVersionUID = 158528860480043703L;
    /**
     * SysHeadIn(required)
     */
    private SysHeadIn sysHeadIn;

    public InterfaceCheckIn() {
    }

    public InterfaceCheckIn(SysHeadIn sysHeadIn) {
        this.sysHeadIn = sysHeadIn;
    }

    public SysHeadIn getSysHeadIn() {
        return sysHeadIn;
    }

    public void setSysHeadIn(SysHeadIn sysHeadIn) {
        this.sysHeadIn = sysHeadIn;
    }

    @Override
    public String toString() {
        return "InterfaceCheckIn{" +
                "sysHeadIn=" + sysHeadIn +
                '}';
    }
}
