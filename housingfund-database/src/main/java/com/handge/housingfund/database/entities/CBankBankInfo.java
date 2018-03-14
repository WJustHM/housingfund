package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/1.
 */
@Entity
@Table(name = "c_bank_bankinfo", indexes = { @Index(name = "INDEX_chgno", columnList = "chgno", unique = true) })
@org.hibernate.annotations.Table(appliesTo = "c_bank_bankinfo", comment = "结算平台-银行信息表")
public class CBankBankInfo extends Common implements Serializable {

	private static final long serialVersionUID = 8438938612009639793L;

	@Column(name = "bank_name", columnDefinition = "VARCHAR(60) DEFAULT NULL COMMENT '银行全称'")
	private String bank_name;

	@Column(name = "chgno", columnDefinition = "VARCHAR(12) DEFAULT NULL COMMENT '联行号'")
	private String chgno;

	@Column(name = "node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行节点号'")
	private String node;

	@Column(name = "code", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '银行代码，联行号前三位'")
	private String code;

	@Column(name = "area_code", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '地区编码'")
	private String area_code;

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.updated_at = new Date();
		this.bank_name = bank_name;
	}

	public String getChgno() {
		return chgno;
	}

	public void setChgno(String chgno) {
		this.updated_at = new Date();
		this.chgno = chgno;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.updated_at = new Date();
		this.node = node;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.updated_at = new Date();
		this.code = code;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.updated_at = new Date();
		this.area_code = area_code;
	}
}
