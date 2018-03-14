package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 字典表 Created by gxy on 17-7-5.
 */

@XmlRootElement(name = "CommonDictionary")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonDictionary implements Serializable {
	private static final long serialVersionUID = -125991586954847409L;
	protected BigDecimal no;
	protected String code;
	protected String name;

	public BigDecimal getNo() {
		return no;
	}

	public void setNo(BigDecimal no) {
		this.no = no;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommonDictionary() {

	}

	public CommonDictionary(BigDecimal no, String code, String name) {
		this.no = no;
		this.code = code;
		this.name = name;
	}
}
