package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 批量转入申请(BDC902)输入格式
 */
public class BatchTransInApplIn implements Serializable {
    private static final long serialVersionUID = -6057557112078656764L;
    /**
     * CenterHeadIn(required), 公积金中心发起交易输入报文头
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 批量编号(required), 批量联系函编号, 公积金中心全局唯一“PLZYJXH+机构代码(前6位)+YYMMDD+4位顺序号”且不重复, 当交易类型为1时, 必须是原来的批量联系函编号
     */
    private String BatConNum;
    /**
     * 交易类型(required), 0 新增是用于新增的转移接续, 1 转出方确认受理是用于转出方确认受理转入方的联系函, 反馈受理信息给转入方
     */
    private String TxFunc;
    /**
     * 转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码
     */
    private String TranOutUnitNo;
    /**
     * 总笔数(required), 本批次交易总笔数
     */
    private String BatchTotalNum;
    /**
     * 文件列表(required), 明细文件, 只支持一个文件
     */
    private FileList fileList;
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

    public BatchTransInApplIn() {
    }

    public BatchTransInApplIn(CenterHeadIn centerHeadIn, String batConNum, String txFunc, String tranOutUnitNo, String batchTotalNum, FileList fileList) {
        this.centerHeadIn = centerHeadIn;
        BatConNum = batConNum;
        TxFunc = txFunc;
        TranOutUnitNo = tranOutUnitNo;
        BatchTotalNum = batchTotalNum;
        this.fileList = fileList;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getBatConNum() {
        return BatConNum;
    }

    public void setBatConNum(String batConNum) {
        BatConNum = batConNum;
    }

    public String getTxFunc() {
        return TxFunc;
    }

    public void setTxFunc(String txFunc) {
        TxFunc = txFunc;
    }

    public String getTranOutUnitNo() {
        return TranOutUnitNo;
    }

    public void setTranOutUnitNo(String tranOutUnitNo) {
        TranOutUnitNo = tranOutUnitNo;
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
        return "BatchTranInApplIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", BatConNum='" + BatConNum + '\'' +
                ", TxFunc='" + TxFunc + '\'' +
                ", TranOutUnitNo='" + TranOutUnitNo + '\'' +
                ", BatchTotalNum='" + BatchTotalNum + '\'' +
                ", fileList=" + fileList +
                ", Remark='" + Remark + '\'' +
                ", Bak1='" + Bak1 + '\'' +
                ", Bak2='" + Bak2 + '\'' +
                '}';
    }
}
