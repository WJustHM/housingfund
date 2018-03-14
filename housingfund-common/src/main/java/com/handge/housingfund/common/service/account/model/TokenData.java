package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/7/12.
 */
public class TokenData implements Serializable {

    private Integer Code;//1：成功，0：失败
    private String Issuer;
    private Date IssuedAt;
    private String userid;
    private Integer type;
    private Date ExpiresAt;
    private String Msg = "";

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public String getIssuer() {
        return Issuer;
    }

    public void setIssuer(String issuer) {
        Issuer = issuer;
    }

    public Date getIssuedAt() {
        return IssuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        IssuedAt = issuedAt;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        ExpiresAt = expiresAt;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    @Override
    public String toString() {
        return "TokenData{" +
                "Code=" + Code +
                ", Issuer='" + Issuer + '\'' +
                ", IssuedAt=" + IssuedAt +
                ", userid='" + userid + '\'' +
                ", type=" + type +
                ", ExpiresAt=" + ExpiresAt +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
