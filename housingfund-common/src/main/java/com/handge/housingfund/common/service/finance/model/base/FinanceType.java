package com.handge.housingfund.common.service.finance.model.base;

import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-22.
 */
public enum FinanceType implements Serializable {
    JTGJJLX("计提公积金利息"),
    JSGJJLX("结算公积金利息"),
    SDYHLX("收到银行利息"),
    SDZS("收到暂收"),
    QMJZYWSR("期末结转业务收入"),
    QMJZYWZC("期末结转业务支出"),
    THZZ("同行转账"),
    KHZZ("跨行转账"),
    JTSXF("计提手续费"),
    ZFSXF("支付手续费"),
    ZFQTYFK("支付其它应付款"),
    ZFBXDKLX("支付补息贷款利息"),
    NDJZZZSY("年度结转增值收益"),
    ZZSYFP("增值收益分配"),
    QTYW("其它业务");
    private String  name;
    FinanceType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FinanceType{" +
                "name='" + name + '\'' +
                '}';
    }
}
