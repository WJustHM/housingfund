package com.handge.housingfund.common.service.collection.enumeration;

/**
 * Created by Liujuhao on 2017/10/10.
 */

/*归集模块部分通用字段的枚举*/

public enum CommonFieldType {

    /*冲账标识*/
    非冲账("非冲账", "01"),
    冲账("冲账", "02")
    ;

    private String name;
    private String code;

    CommonFieldType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
