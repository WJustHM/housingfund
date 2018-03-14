package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 账户交易明细查询(BDC113)输出格式
 */
public class AccTransDetailQueryOut implements Serializable {
    private static final long serialVersionUID = -6171575671934110682L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 文件列表(required)
     */
    private FileList filelist ;

    public AccTransDetailQueryOut() {
    }

    public AccTransDetailQueryOut(CenterHeadOut centerHeadOut, FileList filelist) {
        this.centerHeadOut = centerHeadOut;
        this.filelist = filelist;
    }
    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public FileList getFilelist() {
        return filelist;
    }

    public void setFilelist(FileList filelist) {
        this.filelist = filelist;
    }



    @Override
    public String toString() {
        return "AccTransDetailQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", filelist=" + filelist +
                '}';
    }
}
