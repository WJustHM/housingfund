package com.handge.housingfund.common.service.bank.bean.sys;

import com.handge.housingfund.common.service.bank.bean.head.SysHeadOut;

import java.io.Serializable;

/**
 * Created by gxy on 17-8-11.
 */
public class AccChangeNotice implements Serializable {
    private static final long serialVersionUID = 8188148381971548175L;
    /**
     * SysHeadOut(required)
     */
    private SysHeadOut sysHeadOut;
    /**
     * 账户变动详情
     */
    private AccChangeNoticeFile accChangeNoticeFile;

    public AccChangeNotice() {
    }

    public SysHeadOut getSysHeadOut() {
        return sysHeadOut;
    }

    public void setSysHeadOut(SysHeadOut sysHeadOut) {
        this.sysHeadOut = sysHeadOut;
    }

    public AccChangeNoticeFile getAccChangeNoticeFile() {
        return accChangeNoticeFile;
    }

    public void setAccChangeNoticeFile(AccChangeNoticeFile accChangeNoticeFile) {
        this.accChangeNoticeFile = accChangeNoticeFile;
    }

    @Override
    public String toString() {
        return "AccChangeNotice{" +
                "sysHeadOut=" + sysHeadOut +
                ", accChangeNoticeFile=" + accChangeNoticeFile +
                '}';
    }
}
