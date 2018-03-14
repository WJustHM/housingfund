package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "c_common_person_extension")
@org.hibernate.annotations.Table(appliesTo = "c_common_person_extension", comment = "个人信息扩展表")
public class CCommonPersonExtension extends Common implements Serializable {

	private static final long serialVersionUID = -7873994127646558337L;

	@Column(name = "GJJSCHJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '公积金首次汇缴年月'")
	private String gjjschjny;
	@Column(name = "DZYX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '电子邮箱'")
	private String dzyx;
	@Column(name = "BJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本金金额'")
	private BigDecimal bjje = BigDecimal.ZERO;
	@Column(name = "LXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '利息金额'")
	private BigDecimal lxje = BigDecimal.ZERO;
	@Column(name = "GRJZNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '个人缴至年月'")
	private String grjzny;
	// 提取扩展
	@Column(name = "XCTQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '下次提取日期'")
	private Date xctqrq;

	@Column(name = "GRZHZTLX", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账户状态类型'")
	private String grzhztlx;

	@Column(name = "SFDJ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '是否冻结：01正常/02冻结'")
	private String sfdj;

	@Column(name = "GRZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '个人资料'")
	private String grzl;

	public String getSfdj() {
		return sfdj;
	}

	public void setSfdj(String sfdj) {

		this.sfdj = sfdj;
	}

	public String getGrzhztlx() {
		return grzhztlx;
	}

	public void setGrzhztlx(String grzhztlx) {

		this.grzhztlx = grzhztlx;
	}

	public BigDecimal getBjje() {
		return bjje;
	}

	public void setBjje(BigDecimal bjje) {

		this.bjje = bjje;
	}

	public BigDecimal getLxje() {
		return lxje;
	}

	public void setLxje(BigDecimal lxje) {

		this.lxje = lxje;
	}

	public Date getXctqrq() {
		return xctqrq;
	}

	public void setXctqrq(Date xctqrq) {

		this.xctqrq = xctqrq;
	}

	public String getGjjschjny() {
		return gjjschjny;
	}

	public void setGjjschjny(String gjjschjny) {

		this.gjjschjny = gjjschjny;
	}

	public String getDzyx() {
		return dzyx;
	}

	public void setDzyx(String dzyx) {

		this.dzyx = dzyx;
	}

	public String getGrjzny() {
		return grjzny;
	}

	public void setGrjzny(String grjzny) {

		this.grjzny = grjzny;
	}

	public String getGrzl() {
		return grzl;
	}

	public void setGrzl(String grzl) {
		this.grzl = grzl;
	}

	public CCommonPersonExtension() {
		super();

	}

	public CCommonPersonExtension(
			String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String gjjschjny, String dzyx, BigDecimal bjje, BigDecimal lxje,
			Date xctqrq, String grjzny) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.gjjschjny = gjjschjny;
		this.dzyx = dzyx;
		this.bjje = bjje;
		this.lxje = lxje;
		this.xctqrq = xctqrq;
		this.grjzny = grjzny;
	}
}
