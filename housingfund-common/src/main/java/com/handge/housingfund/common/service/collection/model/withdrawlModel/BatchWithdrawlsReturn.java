package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/15.
 * 描述
 */

public class BatchWithdrawlsReturn implements Serializable {
    /**
     * @param ids 业务流水号集合
     * @param status 操作结果(0成功 1失败)
     * @param RespList 打印回执列表
     * @param idlist 生成pdf回执单的uid
     */
    private ArrayList<String> ids ;
    private String status;
    private ArrayList<ReceiptReturn> RespList;

    private ArrayList<String> idlist;

    public ArrayList<ReceiptReturn> getRespList() {
        return RespList;
    }

    public void setRespList(ArrayList<ReceiptReturn> respList) {
        RespList = respList;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getIdlist() {
        return idlist;
    }

    public void setIdlist(ArrayList<String> idlist) {
        this.idlist = idlist;
    }

    @Override
    public String toString() {
        return "BatchWithdrawlsReturn{" +
                "ids=" + ids +
                "idlist=" + idlist +
                ", status='" + status + '\'' +
                '}';
    }
}
