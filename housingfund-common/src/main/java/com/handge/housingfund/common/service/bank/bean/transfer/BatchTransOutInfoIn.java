package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 批量转出信息(BDC906)输入格式
 */
public class BatchTransOutInfoIn implements Serializable {
    private static final long serialVersionUID = -5095424540696428588L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 原批量编号(required), 要求使用转入申请的批量编号
     */
    private String OrBatConNum;
    /**
     * 交易类型(required)
     * 0-新增信息转出
     * 1-修改转出信息
     * 2-转入确认办结
     * 0新增是用于新增的转移接续【转出发起】
     * 1修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】
     * 2转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】
     */
    private String TxFunc;
    /**
     * 总笔数(required), 本批次交易总笔数
     */
    private String BatchTotalNum;
    /**
     * 文件列表(required), 明细文件，只支持一个文件
     */
    private FileList fileList;
    /**
     * 业务办理联系电话(required), 固定电话：区号-电话号码 手机：11位号码
     */
    private String ContPhone;
    /**
     * 备注
     */
    private String Remark;
    /**
     * 备用
     */
    private String Bak1;
    /**
     * 备用
     */
    private String Bak2;

    public BatchTransOutInfoIn() {
    }

    public BatchTransOutInfoIn(CenterHeadIn centerHeadIn, String orBatConNum, String txFunc, String batchTotalNum, FileList fileList, String contPhone) {
        this.centerHeadIn = centerHeadIn;
        OrBatConNum = orBatConNum;
        TxFunc = txFunc;
        BatchTotalNum = batchTotalNum;
        this.fileList = fileList;
        ContPhone = contPhone;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getOrBatConNum() {
        return OrBatConNum;
    }

    public void setOrBatConNum(String orBatConNum) {
        OrBatConNum = orBatConNum;
    }

    public String getTxFunc() {
        return TxFunc;
    }

    public void setTxFunc(String txFunc) {
        TxFunc = txFunc;
    }

    public String getBatchTotalNum() {
        return BatchTotalNum;
    }

    public void setBatchTotalNum(String batchTotalNum) {
        BatchTotalNum = batchTotalNum;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

    public String getContPhone() {
        return ContPhone;
    }

    public void setContPhone(String contPhone) {
        ContPhone = contPhone;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getBak1() {
        return Bak1;
    }

    public void setBak1(String bak1) {
        Bak1 = bak1;
    }

    public String getBak2() {
        return Bak2;
    }

    public void setBak2(String bak2) {
        Bak2 = bak2;
    }

    @Override
    public String toString() {
        return "BatchTranOutInfoIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", OrBatConNum='" + OrBatConNum + '\'' +
                ", TxFunc='" + TxFunc + '\'' +
                ", BatchTotalNum='" + BatchTotalNum + '\'' +
                ", fileList=" + fileList +
                ", ContPhone='" + ContPhone + '\'' +
                ", Remark='" + Remark + '\'' +
                ", Bak1='" + Bak1 + '\'' +
                ", Bak2='" + Bak2 + '\'' +
                '}';
    }
}
