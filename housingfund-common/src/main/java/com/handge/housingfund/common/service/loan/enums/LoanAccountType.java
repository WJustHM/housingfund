package com.handge.housingfund.common.service.loan.enums;

/**
 * Created by tanyi on 2017/9/8.
 */

/**
 * 贷款账户状态
 */
@SuppressWarnings("NonAsciiCharacters")
public enum LoanAccountType {

    待签合同("待签合同","0"),
    待放款("待放款","1"),
    正常("正常","2"),
    已结清("已结清","3"),
    呆账("呆账","4"),
    逾期("逾期","5"),
    待确认("待确认","6"),
    所有("所有","7"),
    已作废("已作废", "8");

    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    LoanAccountType(String name, String code) {
        this.code = code;
        this.name=name;
    }

    public  String getcase(String id){
        String str="";
        switch (id){
            case "0":
                str=LoanAccountType.待签合同.getName();
                break;
            case "1":
                str=LoanAccountType.待放款.getName();
                break;
            case "3":
                str=LoanAccountType.已结清.getName();
                break;
            case "4":
                str=LoanAccountType.呆账.getName();
                break;
        }
        return str;
    }

}
