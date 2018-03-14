package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/28.
 * 描述
 */
public class WithdrawlRecordsListReturn implements Serializable {
    ArrayList<Record> recordsList;//提取记录


    public ArrayList<Record> getRecordsList() {
        return recordsList;
    }

    public void setRecordsList(ArrayList<Record> recordsList) {
        this.recordsList = recordsList;
    }

    @Override
    public String toString() {
        return "WithdrawlRecordsReturn{" +
                ", recordsList=" + recordsList +
                '}';
    }
}
