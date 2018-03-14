package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;

/**
 * 联行号列表
 */
public class ChgNoList implements Serializable {
    private static final long serialVersionUID = -579467453639233468L;
    /**
     * 联行号(required)
     */
    private String ChgNo;
    /**
     * 银行名称(required)
     */
    private String ChgName;

    public ChgNoList() {
    }

    public ChgNoList(String chgNo, String chgName) {
        ChgNo = chgNo;
        ChgName = chgName;
    }

    public String getChgNo() {
        return ChgNo;
    }

    public void setChgNo(String chgNo) {
        ChgNo = chgNo;
    }

    public String getChgName() {
        return ChgName;
    }

    public void setChgName(String chgName) {
        ChgName = chgName;
    }

    @Override
    public String toString() {
        return "ChgNoList{" +
                "ChgNo='" + ChgNo + '\'' +
                ", ChgName='" + ChgName + '\'' +
                '}';
    }
}
