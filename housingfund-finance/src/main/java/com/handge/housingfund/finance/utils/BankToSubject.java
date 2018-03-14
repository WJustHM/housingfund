package com.handge.housingfund.finance.utils;

/**
 * Created by tanyi on 2017/10/30.
 */
public enum BankToSubject {

    交通银行("301", "1210105", "1220105","2190405","2190505"),
    中国银行("104", "1210103", "1220103","2190403","2190503"),
    工商银行("102", "1210101", "1220101","2190401","2190501"),
    农业银行("103", "1210102", "1220102","2190402","2190502"),
    建设银行("105", "1210104", "1220104","2190404","2190504");


    private String bankcode;//银行代码
    private String grwtcode;//个人委托贷款代码
    private String gryqcode;//个人逾期贷款代码
    private String gjsxf; //归集手续费
    private String wdsxf ;//委贷手续费

    BankToSubject(String bankcode, String grwtcode, String gryqcode, String gjsxf, String wdsxf) {
        this.bankcode = bankcode;
        this.grwtcode = grwtcode;
        this.gryqcode = gryqcode;
        this.gjsxf = gjsxf;
        this.wdsxf = wdsxf;
    }

    public String getBankcode() {
        return bankcode;
    }

    public String getGrwtcode() {
        return grwtcode;
    }

    public String getGryqcode() {
        return gryqcode;
    }

    public String getGjsxf() {
        return gjsxf;
    }

    public String getWdsxf() {
        return wdsxf;
    }

    public static BankToSubject getSubject(String bankcode) {
        for (BankToSubject subject : BankToSubject.values()) {
            if (subject.bankcode.equals(bankcode)) {
                return subject;
            }
        }
        return null;
    }

}
