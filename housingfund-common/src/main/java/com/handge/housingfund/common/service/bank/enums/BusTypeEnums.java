package com.handge.housingfund.common.service.bank.enums;

/**
 * 业务类型
 * Created by gxy on 17-11-8.
 */
public enum BusTypeEnums {
    汇补缴("01","汇补缴"),
    部分提取("02","部分提取"),
    销户提取("03","销户提取"),
    划拨委贷基金("04","划拨委贷基金"),
    贷款扣款("05","贷款扣款"),
    贷款本息分解("06","贷款本息分解"),
    外部转出("07","外部转出"),
    购买国债("08","购买国债"),
    定期存款存入("09","定期存款存入"),
    定期存款支取("10","定期存款支取"),
    通知存款存入("11","通知存款存入"),
    通知存款支取("12","通知存款支取"),
    手续费支出("13","手续费支出"),
    结算费支出("14","结算费支出"),
    支付管理费用("15","支付管理费用"),
    支付风险准备金("16","支付风险准备金"),
    跨区资金转出("17","跨区资金转出"),
    资金划转("18","资金划转"),
    公租房投资("19","公租房投资"),
    其他业务("20","其他业务"),
    其他支付("2713","其他支付"),
    逾期还款("2715","逾期还款");

    private String code;
    private String desc;

    BusTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
