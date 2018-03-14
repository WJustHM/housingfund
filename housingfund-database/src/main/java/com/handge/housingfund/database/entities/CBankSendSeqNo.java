package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gxy on 17-8-7.
 */
@Entity
@Table(name = "c_bank_sendseqno")
@org.hibernate.annotations.Table(appliesTo = "c_bank_sendseqno", comment = "结算平台-发送方流水表")
public class CBankSendSeqNo extends Common implements Serializable {

	private static final long serialVersionUID = -179078466521324282L;

	@Column(name = "business_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流水号'")
	private String business_seq_no;

	@Column(name = "send_seq_no", columnDefinition = "VARCHAR(20) NOT NULL UNIQUE COMMENT '发送方流水号'")
	private String send_seq_no;

	@Column(name = "bus_type", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '业务类型'")
	private String bus_type;

	@Column(name = "send_date", columnDefinition = "DATETIME DEFAULT NULL COMMENT '交易日期'")
	private Date send_date;

	@Column(name = "txCode", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '交易代码'")
	private String txCode;

	@Column(name = "receive_node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '接收方节点号'")
	private String receive_node;

	@Column(name = "cust_no", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '客户编号'")
	private String cust_no;

	@Column(name = "host_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '银行主机流水号'")
	private String host_seq_no;

	@Column(name = "pen_host_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '利息银行主机流水号'")
	private String pen_host_seq_no;

	@Column(name = "int_host_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '贷款罚息银行主机流水号'")
	private String int_host_seq_no;

	@Column(name = "fine_host_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '贷款违约金银行主机流水号'")
	private String fine_host_seq_no;

	@Column(name = "batch_no", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '批量编号'")
	private String batch_no;

	@Column(name = "tx_status", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '交易状态（0：成功，1：失败，2：其他,处理中或状态未知）'")
	private String tx_status;

	@Column(name = "rtn_message", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '报文信息'")
	private String rtn_message;

	@Column(name = "type", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '类型(1:单笔交易且无交易结果,2:单笔交易有交易结果,3:批量交易且无交易结果,4:批量交易且有交易结果)'")
	private String type;

	@Column(name = "receive_seq_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '接收方流水号'")
	private String receive_seq_no;

	@Column(name = "cr_acct_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '收方账号'")
	private String cr_acct_no;

	@Column(name = "cr_acct_name", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '收方户名'")
	private String cr_acct_name;

	@Column(name = "de_acct_no", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '付方账号'")
	private String de_acct_no;

	@Column(name = "de_acct_name", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '付方户名'")
	private String de_acct_name;

	@Column(name = "amt", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '交易金额'")
	private BigDecimal amt = BigDecimal.ZERO;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cBankSendSeqNo")
	private List<CBankBusiness> cBankBusinesses = new ArrayList<>();

	public CBankSendSeqNo() {
		super();

	}

	public String getBusiness_seq_no() {
		return business_seq_no;
	}

	public void setBusiness_seq_no(String business_seq_no) {
		this.updated_at = new Date();
		this.business_seq_no = business_seq_no;
	}

	public String getSend_seq_no() {
		return send_seq_no;
	}

	public void setSend_seq_no(String send_seq_no) {
		this.updated_at = new Date();
		this.send_seq_no = send_seq_no;
	}

	public String getBus_type() {
		return bus_type;
	}

	public void setBus_type(String bus_type) {
		this.updated_at = new Date();
		this.bus_type = bus_type;
	}

	public Date getSend_date() {
		return send_date;
	}

	public void setSend_date(Date send_date) {
		this.updated_at = new Date();
		this.send_date = send_date;
	}

	public String getTxCode() {
		return txCode;
	}

	public void setTxCode(String txCode) {
		this.updated_at = new Date();
		this.txCode = txCode;
	}

	public String getReceive_node() {
		return receive_node;
	}

	public void setReceive_node(String receive_node) {
		this.updated_at = new Date();
		this.receive_node = receive_node;
	}

	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.updated_at = new Date();
		this.cust_no = cust_no;
	}

	public String getHost_seq_no() {
		return host_seq_no;
	}

	public void setHost_seq_no(String host_seq_no) {
		this.updated_at = new Date();
		this.host_seq_no = host_seq_no;
	}

	public String getPen_host_seq_no() {
		return pen_host_seq_no;
	}

	public void setPen_host_seq_no(String pen_host_seq_no) {
		this.updated_at = new Date();
		this.pen_host_seq_no = pen_host_seq_no;
	}

	public String getInt_host_seq_no() {
		return int_host_seq_no;
	}

	public void setInt_host_seq_no(String int_host_seq_no) {
		this.updated_at = new Date();
		this.int_host_seq_no = int_host_seq_no;
	}

	public String getFine_host_seq_no() {
		return fine_host_seq_no;
	}

	public void setFine_host_seq_no(String fine_host_seq_no) {
		this.updated_at = new Date();
		this.fine_host_seq_no = fine_host_seq_no;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.updated_at = new Date();
		this.batch_no = batch_no;
	}

	public String getTx_status() {
		return tx_status;
	}

	public void setTx_status(String tx_status) {
		this.updated_at = new Date();
		this.tx_status = tx_status;
	}

	public String getRtn_message() {
		return rtn_message;
	}

	public void setRtn_message(String rtn_message) {
		this.updated_at = new Date();
		this.rtn_message = rtn_message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.updated_at = new Date();
		this.type = type;
	}

	public String getCr_acct_no() {
		return cr_acct_no;
	}

	public void setCr_acct_no(String cr_acct_no) {
		this.updated_at = new Date();
		this.cr_acct_no = cr_acct_no;
	}

	public String getCr_acct_name() {
		return cr_acct_name;
	}

	public void setCr_acct_name(String cr_acct_name) {
		this.updated_at = new Date();
		this.cr_acct_name = cr_acct_name;
	}

	public String getDe_acct_no() {
		return de_acct_no;
	}

	public void setDe_acct_no(String de_acct_no) {
		this.updated_at = new Date();
		this.de_acct_no = de_acct_no;
	}

	public String getDe_acct_name() {
		return de_acct_name;
	}

	public void setDe_acct_name(String de_acct_name) {
		this.updated_at = new Date();
		this.de_acct_name = de_acct_name;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.updated_at = new Date();
		this.amt = amt;
	}

	public List<CBankBusiness> getcBankBusinesses() {
		return cBankBusinesses;
	}

	public void setcBankBusinesses(List<CBankBusiness> cBankBusinesses) {
		this.updated_at = new Date();
		this.cBankBusinesses = cBankBusinesses;
	}

	public String getReceive_seq_no() {
		return receive_seq_no;
	}

	public void setReceive_seq_no(String receive_seq_no) {
		this.updated_at = new Date();
		this.receive_seq_no = receive_seq_no;
	}

	public void addBankBusinesses(CBankBusiness cBankBusiness) {
		cBankBusinesses.add(cBankBusiness);
		cBankBusiness.setcBankSendSeqNo(this);
	}
}
