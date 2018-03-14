package com.handge.housingfund.common.service.collection.enumeration;

/**
 * Created by Liujuhao on 2017/12/8.
 */
public enum AllochthonousStatus {

    /*
    00-正常办结
    01-联系函复核通过
    02-联系函确认接收
    05-转入撤销业务办结
    06-协商中
    08-账户信息复核通过
    13-转出审核不通过
    20-协商后业务办结
    40-转出失败业务办结
    */
    正常办结("00", "正常办结"),
    联系函复核通过("01", "联系函复核通过"),
    联系函确认接收("02", "联系函确认接收"),
    转入撤销业务办结("05", "转入撤销业务办结"),
    协商中("06", "协商中"),
    账户信息复核通过("08", "账户信息复核通过"),
    转出审核不通过("13", "转出审核不通过"),
    协商后业务办结("20", "协商后业务办结"),
    转出失败业务办结("40", "转出失败业务办结");

    private String code;
    private String name;

    AllochthonousStatus(String code, String name) {
        this.code = code;
        this.name = name;
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
