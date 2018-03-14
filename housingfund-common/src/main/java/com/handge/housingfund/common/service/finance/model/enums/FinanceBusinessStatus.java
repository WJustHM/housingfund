package com.handge.housingfund.common.service.finance.model.enums;

/**
 * Created by Funnyboy on 2017/9/22.
 */
public enum FinanceBusinessStatus {

    所有("所有", "00"),
    初始状态("初始状态", "01"),
    新建("新建", "02"),
    待审核("待审核", "04"),
    审核不通过("审核不通过", "05"),
    待活转定("待活转定", "03"),
    定期存款成功("定期存款成功", "06"),
    定期存款失败("定期存款失败", "07"),
    待定转活("待定转活", "08"),
    定期支取失败("定期支取失败", "09"),
    定期支取成功("定期支取成功", "10"),
    待入账("待入账", "11"),

    待某人审核("待%审核", "04"),

    入账失败("入账失败", "97"),
    已入账("已入账", "98"),
    办结("办结", "99");

    private String name;

    private String code;

    FinanceBusinessStatus(String name, String code) {
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

}
