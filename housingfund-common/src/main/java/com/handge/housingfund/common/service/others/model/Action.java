package com.handge.housingfund.common.service.others.model;

import java.io.Serializable;

public class Action implements Serializable {
	private static final long serialVersionUID = -3943098905842302841L;
	private String action_method;
	private String action_url;
	private String action_code;

	public String getAction_code() {
		return action_code;
	}

	public void setAction_code(String action_code) {
		this.action_code = action_code;
	}

	public String getAction_method() {
		return action_method;
	}

	public void setAction_method(String action_method) {
		this.action_method = action_method;
	}

	public String getAction_url() {
		return action_url;
	}

	public void setAction_url(String action_url) {
		this.action_url = action_url;
	}

	public Action() {

	}

	public Action(String action_method, String action_url, String action_code) {
		this.action_method = action_method;
		this.action_url = action_url;
		this.action_code = action_code;
	}

}
