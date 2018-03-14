package com.handge.housingfund.common.service.others.enums;

/**
 * 模块名称
 */
public enum UploadFileModleType {

    归集("01"),
    提取("02"),
    贷款("03"),
    财务("04");

    private String code;

    UploadFileModleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
