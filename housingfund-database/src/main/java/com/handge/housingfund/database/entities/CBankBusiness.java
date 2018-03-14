package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by gxy on 17-8-9.
 */
@Entity
@Table(name = "c_bank_business")
@org.hibernate.annotations.Table(appliesTo = "c_bank_business", comment = "结算平台-业务表")
public class CBankBusiness extends Common implements Serializable {
	private static final long serialVersionUID = -4245478384401837518L;

	@Column(name = "business_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流水号'")
	private String business_seq_no;

	@Column(name = "no", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '批量文件中的序号'")
	private String no;

	@Column(name = "rtn_code", columnDefinition = "VARCHAR(50) DEFAULT NULL COMMENT '响应信息（00000：成功，其他：失败）'")
	private String rtn_code;

	@Column(name = "rtn_message", columnDefinition = "TEXT DEFAULT NULL COMMENT '报文信息'")
	private String rtn_message;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CBankSendSeqNo cBankSendSeqNo;

	public CBankBusiness() {
		super();

	}

	public String getBusiness_seq_no() {
		return business_seq_no;
	}

	public void setBusiness_seq_no(String business_seq_no) {
		this.updated_at = new Date();
		this.business_seq_no = business_seq_no;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.updated_at = new Date();
		this.no = no;
	}

	public String getRtn_code() {
		return rtn_code;
	}

	public void setRtn_code(String rtn_code) {
		this.updated_at = new Date();
		this.rtn_code = rtn_code;
	}

	public String getRtn_message() {
		return rtn_message;
	}

	public void setRtn_message(String rtn_message) {
		this.updated_at = new Date();
		this.rtn_message = rtn_message;
	}

	public CBankSendSeqNo getcBankSendSeqNo() {
		return cBankSendSeqNo;
	}

	public void setcBankSendSeqNo(CBankSendSeqNo cBankSendSeqNo) {
		this.updated_at = new Date();
		this.cBankSendSeqNo = cBankSendSeqNo;
	}
}
