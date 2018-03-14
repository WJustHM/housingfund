package com.handge.housingfund.common.service.collection.enumeration;

/**
 * Created by Liujuhao on 2017/8/29.
 */
public enum WithDrawalReason {

    REASON_0("00", "所有"),
    REASON_1("01", "购买住房"),
    REASON_2("02", "建造、翻修、大修住房"),
    REASON_3("03", "离休、退休"),
    REASON_4("04", "完全丧失劳动能力，并与单位终止劳动合同关系"),
    REASON_5("05", "户口迁出所在市、县或出境定居"),
    REASON_6("06", "偿还购房贷款本息"),
    REASON_7("07", "房租超过家庭工资收入的规定比例"),
    REASON_8("08", "死亡"),
    REASON_9("09", "大病医疗"),
    REASON_10("99" ,"其他"),
    ;
    
    private String code;

    private String name;

    WithDrawalReason(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
