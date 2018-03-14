package com.handge.housingfund.common.service.enumeration;

/**
 * Created by Liujuhao on 2017/8/30.
 */
public enum BusinessDetailsModule {

    /**
     * 该枚举以数据库（业务）表为单位，进行区分
     */
    Collection_person("归集-个人", "01"),
    Collection_unit("归集-单位", "02"),
    Loan_person("贷款-个人", "03"),
    Loan_project("贷款-项目", "04"),
    Finance_common("财务-日常", "05"), //c_finance_business_process
    Finance_currentToPeriodic("财务-活期转定期", "06"),    //c_finance_actived_2_fixed
    Finance_periodicWithdraw("财务-定期支取", "07");  //c_finance_fixed_draw

    private String name;

    private String code;

    BusinessDetailsModule(String name, String code) {
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
