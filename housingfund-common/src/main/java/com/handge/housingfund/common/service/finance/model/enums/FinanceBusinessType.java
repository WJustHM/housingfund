package com.handge.housingfund.common.service.finance.model.enums;

import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.util.StringUtil;

/**
 * 财务业务操作内容枚举定义
 */
/*09：定期存款 10：定期支取 11：通知存款 12：通知存款支取*/
@SuppressWarnings("NonAsciiCharacters")
public enum FinanceBusinessType {

    所有("所有", "00"),

    定期存款("定期存款", "09"),
    定期支取("定期支取", "10"),
    通知存款("通知存款", "11"),
    通知存款支取("通知存款支取", "12"),
    其他("其他", "99"),

    日常财务处理("日常财务处理", "70");


    private String name;
    private String code;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FinanceBusinessType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static String getNameByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return LoanBusinessType.所有.getName();
        }
        LoanBusinessType[] values = LoanBusinessType.values();
        for (LoanBusinessType businessStatus : values) {
            if (businessStatus.getCode().equals(code)) {
                return businessStatus.getName();
            }
        }
        return code;
    }
}
