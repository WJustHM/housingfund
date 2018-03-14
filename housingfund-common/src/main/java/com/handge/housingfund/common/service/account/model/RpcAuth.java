package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/13.
 */
@XmlRootElement(name = "RpcAuth")
@XmlAccessorType(XmlAccessType.FIELD)
public class RpcAuth implements Serializable {
    private String user_id;//资料表用户ID
    private int type;//账户类型，对应不同资料表（1：个人账户，2：员工账户，3：单位账户，4：房开商账号）
    private String username;//用户名
    private String password;//密码
    private String role_id;//角色ID
    private String email;//邮箱
    private int state;//状态1:启用，0:禁用

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
