package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 批量业务结果下载(BDC108)输出格式
 */
public class BatchResultDownloadOut implements Serializable {
    private static final long serialVersionUID = 8892645538680709027L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 文件列表(required)
     */
    private FileList fileList;

    public BatchResultDownloadOut() {
    }

    public BatchResultDownloadOut(CenterHeadOut centerHeadOut, FileList fileList) {
        this.centerHeadOut = centerHeadOut;
        this.fileList = fileList;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "BatchResultDownloadOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", fileList=" + fileList +
                '}';
    }
}
