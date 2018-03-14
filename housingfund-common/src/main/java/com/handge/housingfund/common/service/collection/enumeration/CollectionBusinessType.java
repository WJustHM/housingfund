package com.handge.housingfund.common.service.collection.enumeration;

import com.handge.housingfund.common.service.util.StringUtil;

@SuppressWarnings({"unused", "NonAsciiCharacters"})
public enum CollectionBusinessType {

    所有("所有", "00"),
    /*CZ*/ 汇缴("汇缴", "01"),
    /*CZ*/ 补缴("补缴", "02"),
    开户("开户", "03"),
    启封("启封", "04"),
    封存("封存", "05"),
    内部转移("内部转移", "06"),
    /*CZ*/ 外部转出("外部转出", "07"),
    /*CZ*/ 外部转入("外部转入", "08"),
    /*CZ*/ 年终结息("年终结息", "09"),
    基数调整("基数调整", "10"),
    /*CZ*/ 部分提取("部分提取", "11"),
    /*CZ*/ 销户提取("销户提取", "12"),
    缓缴处理("缓缴处理", "13"),
    冻结("冻结", "14"),
    解冻("解冻", "15"),
    其他("其他", "99"),

    //自定义在此添加...

    变更("变更", "70"), //划分到：其他
    合并("个人合并", "71"), //划分到：内部转移
    销户("销户", "72"), //划分到：其他
    /*CZ*/ 错缴更正("错缴更正", "73"), //划分到：其他
    催缴("催缴", "74"), //划分到：其他（暂时）
    比例调整("比例调整", "75"), //划分到：其他
    单位清册("单位清册", "76"), //划分到：其他（暂时）

    结息("结息", "98"); //去掉（有@deprecated的方法在调用，暂不处理）

    private String name;

    private String code;

    CollectionBusinessType(String name, String code) {

        this.name = name;

        this.code = code;
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


    public static String getNameByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return CollectionBusinessType.所有.getName();
        }
        CollectionBusinessType[] values = CollectionBusinessType.values();
        for (CollectionBusinessType businessStatus : values) {
            if (businessStatus.getCode().equals(code)) {
                return businessStatus.getName();
            }
        }
        return code;
    }
}
