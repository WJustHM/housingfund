package com.handge.housingfund.common.service.bank.bean.sys;

import com.handge.housingfund.common.service.bank.bean.head.SysHeadOut;

import java.io.Serializable;

/**
 * 接口探测(SYS600)输出格式,数据应用系统发起
 */
public class InterfaceCheckOut implements Serializable {
    private static final long serialVersionUID = -2496842246871637979L;
    /**
     * SysHeadOut(required)
     */
    private SysHeadOut sysHeadOut;

    public InterfaceCheckOut() {
    }

    public SysHeadOut getSysHeadOut() {
        return sysHeadOut;
    }

    public void setSysHeadOut(SysHeadOut sysHeadOut) {
        this.sysHeadOut = sysHeadOut;
    }

    @Override
    public String toString() {
        return "InterfaceCheckOut{" +
                "sysHeadOut=" + sysHeadOut +
                '}';
    }
}
