package com.handge.housingfund.common.service.bank.enums;

public enum  BankName {

    交通银行("交通银行","301"),
    中国银行股份有限公司("中国银行股份有限公司","104"),
    中国工商银行股份有限公司("中国工商银行股份有限公司","102"),
    中国农业银行股份有限公司("中国农业银行股份有限公司","103"),
    中国建设银行股份有限公司("中国建设银行股份有限公司","105");

    private String code;
    private String name;

    BankName(String name,String code){
        this.name = name;
        this.code = code;

    }

    public static String BankNameWithCode(String code){

        for(BankName bankName:BankName.values()){
            if(bankName.code.equals(code)){
                return bankName.name;
            }
        }
        return "";
    }
}
