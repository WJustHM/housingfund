package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/11.
 */
@XmlRootElement(name = "LoginCA")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginCA implements Serializable {
    private String username;//用户名
    private String cert;//证书
    private String signinfo;//密文
    private String info;//明文

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getSigninfo() {
        return signinfo;
    }

    public void setSigninfo(String signinfo) {
        this.signinfo = signinfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
