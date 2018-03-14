package com.handge.housingfund.common.service.bank.bean.sys;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.SysHeadOut;

import java.io.Serializable;

/**
 * 账户变动通知(SBDC100)输出格式
 */
public class AccChangeNoticeOut implements Serializable {
    private static final long serialVersionUID = 5053685075501551405L;
    /**
     * SysHeadOut(required)
     */
    private SysHeadOut sysHeadOut;
    /**
     * 文件列表(required)
     */
    private FileList fileList;

    public AccChangeNoticeOut() {
    }

    public SysHeadOut getSysHeadOut() {
        return sysHeadOut;
    }

    public void setSysHeadOut(SysHeadOut sysHeadOut) {
        this.sysHeadOut = sysHeadOut;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "AccChangeNoticeOut{" +
                "sysHeadOut=" + sysHeadOut +
                ", fileList=" + fileList +
                '}';
    }
}
