package com.handge.housingfund.common.service.bank.bean.sys;

import com.handge.housingfund.common.service.bank.bean.head.SysHeadIn;

import java.io.Serializable;

/**
 * 账户变动通知(SBDC100)输入格式
 */
public class AccChangeNoticeIn implements Serializable {
    private static final long serialVersionUID = -2616892368853320062L;
    /**
     * SysHeadIn(required)
     */
    private SysHeadIn sysHeadIn;

    public AccChangeNoticeIn() {
    }

    public AccChangeNoticeIn(SysHeadIn sysHeadIn) {
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
        return "AccChangeNoticeIn{" +
                "sysHeadIn=" + sysHeadIn +
                '}';
    }
}
