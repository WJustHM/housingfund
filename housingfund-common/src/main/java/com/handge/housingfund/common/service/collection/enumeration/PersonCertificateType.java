package com.handge.housingfund.common.service.collection.enumeration;

public enum  PersonCertificateType {

    身份证("身份证","01"),
    军官证("军官证","02"),
    外国人永久居留证("外国人永久居留证","04"),
    护照("护照","99");

    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    PersonCertificateType(String name, String code) {
        this.code = code;

        this.name = name;
    }

}
