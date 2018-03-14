package com.handge.housingfund.common.service;

import com.handge.housingfund.common.service.account.model.UserInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-29.
 */
@XmlRootElement(name = "TokenContext")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenContext implements Serializable {

	private static final long serialVersionUID = 5603218314151328561L;

	private String userType;

	private String userId;

	private UserInfo userInfo;

	private List<String> roleList;

	private String channel;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
