package com.handge.housingfund.database.enums;

/**
 * Created by Funnyboy on 2017/8/23.
 */
public enum TaskBusinessType {
    正常还款("02"),
    提前还款("03"),
    逾期还款("04");

    private String code;

    public String getCode() {
        return code;
    }

    TaskBusinessType(String code) {
        this.code = code;
    }
}
