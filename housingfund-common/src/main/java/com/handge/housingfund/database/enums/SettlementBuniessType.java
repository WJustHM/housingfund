package com.handge.housingfund.database.enums;

/**
 * Created by Funnyboy on 2017/9/17.
 */
public enum SettlementBuniessType {

    汇补缴("汇补缴", "01"),
    部分提取("部分提取", "02"),
    销户提取("销户提取", "03"),
    划拨委贷基金("划拨委贷基金", "04"),
    贷款扣款("贷款扣款", "05"),
    贷款本息分解("贷款本息分解", "06"),
    外部转出("外部转出", "07"),
    购买国债("购买国债", "08"),
    定期存款存入("定期存款存入", "09"),
    定期存款支取("定期存款支取", "10"),
    通知存款存入("通知存款存入", "11"),
    通知存款支取("通知存款支取", "12"),
    手续费支出("手续费支出", "13"),
    结算费支出("结算费支出", "14"),
    支付管理费用("支付管理费用", "15"),
    支付风险准备金("支付风险准备金", "16"),
    跨区资金转出("跨区资金转出", "17"),
    资金划转("资金划转", "18"),
    公租房投资("公租房投资", "19"),
    逾期扣款("逾期扣款","3599"),
    其他业务("其他业务", "20");

    private String name;

    private String code;

    SettlementBuniessType(String name, String code) {
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
