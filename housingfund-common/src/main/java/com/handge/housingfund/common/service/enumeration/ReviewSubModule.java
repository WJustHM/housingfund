package com.handge.housingfund.common.service.enumeration;

/**
 * Created by Liujuhao on 2017/9/7.
 */
public enum ReviewSubModule {

    归集_个人("GR", "个人"),
    归集_单位("DW", "单位"),
    归集_缴存("JC", "缴存"),
    归集_提取("TQ", "提取"),
    归集_转入("ZR", "转入"),
    归集_转出("ZC", "转出"),

    贷款_放款("DK", "贷款"),
    贷款_还款("HK", "还款"),
    贷款_房开("FK", "房开"),
    贷款_楼盘("LP", "楼盘"),
    贷款_合同("HT", "合同"),

    财务_日常("RC", "日常"),
    财务_定期("DQ", "定期")
    ;

    private String code;

    private String name;

    ReviewSubModule(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
