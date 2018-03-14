package com.handge.housingfund.common.service.loan.enums;

public enum LoanRiskStatus {

    所有("00"),
    正常("01"),
    关注("02"),
    次级("03"),
    可疑("04"),
    损失("05"),
    其他("99");

    private String code;

    LoanRiskStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

