package com.handge.housingfund.common.service.account.enums;

/**
 * Created by 向超 on 2017/9/20.
 */
public enum RateEnum {
    五年以下公积金贷款利率("01","RateOfInterest"),
    五年以上公积金贷款利率("02","RateOfInterest"),
    公积金贷款利率("03","RateOfInterest"),
    活期存款利率("04","RateOfInterest"),
    三个月定期存款利率("05","RateOfInterest"),
    六个月定期存款利率("06","RateOfInterest"),
    一年定期存款利率("07","RateOfInterest"),
    两年定期存款利率("08","RateOfInterest"),
    三年定期存款利率("09","RateOfInterest"),
    五年定期存款利率("10","RateOfInterest"),
    一天通知存款利率("11","RateOfInterest"),
    七天通知存款利率("12","RateOfInterest"),
    协定存款利率("13","RateOfInterest"),
    其他("99","RateOfInterest");

    private String code;
    private String type;
    RateEnum(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
