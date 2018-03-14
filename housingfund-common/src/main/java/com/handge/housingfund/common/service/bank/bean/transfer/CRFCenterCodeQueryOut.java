package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 转移接续平台公积金中心代码查询(BDC909)输出格式
 */
public class CRFCenterCodeQueryOut implements Serializable {
    private static final long serialVersionUID = -686235311103434205L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 文件列表(required), 明细文件，只支持一个文件
     */
    private FileList fileList;

    public CRFCenterCodeQueryOut() {
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
        return "CRFCenterCodeQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", fileList=" + fileList +
                '}';
    }
}
