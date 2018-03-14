package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/10/14.
 */
@XmlRootElement(name = "LoginUserInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = -8670196424309929042L;

	private String user_id; // 用户ID

	private String XingMing;// 姓名

	private String role_id; // 角色ID

	public String getYWWD() {
		return YWWD;
	}

	public void setYWWD(String yWWD) {
		YWWD = yWWD;
	}

	private String YWWDMC;// 业务网点名称

	private String YWWD;// 网点ID

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getYWWDMC() {
		return YWWDMC;
	}

	public void setYWWDMC(String YWWDMC) {
		this.YWWDMC = YWWDMC;
	}

	public String getXingMing() {
		return XingMing;
	}

	public void setXingMing(String xingMing) {
		XingMing = xingMing;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	@Override
	public String toString() {
		return "LoginUserInfo{" + "  user_id='" + user_id + '\'' + ", XingMing='" + XingMing + '\'' + ", role_id='"
				+ role_id + '\'' + ", YWWDMC='" + YWWDMC + '\'' + '}';
	}
}
