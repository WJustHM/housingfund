package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_information")
@org.hibernate.annotations.Table(appliesTo = "st_project_information", comment = "建设项目信息 表7.0.2")
public class StProjectInformation extends Common implements java.io.Serializable {

	private static final long serialVersionUID = -7741985717124185421L;
	@Column(name = "XMBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '项目编号'")
	private String xmbh;
	@Column(name = "XMLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '项目类型'")
	private String xmlx;
	@Column(name = "XMMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '项目名称'")
	private String xmmc;
	@Column(name = "SSCSDM", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '所属城市代码'")
	private BigDecimal sscsdm = BigDecimal.ZERO;
	@Column(name = "TZGM", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '投资规模'")
	private BigDecimal tzgm = BigDecimal.ZERO;
	@Column(name = "DKED", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款额度'")
	private BigDecimal dked = BigDecimal.ZERO;
	@Column(name = "JSGM", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '建设规模'")
	private BigDecimal jsgm = BigDecimal.ZERO;
	@Column(name = "XMFLJB", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '项目分类级别'")
	private String xmfljb;
	@Column(name = "XMPC", columnDefinition = "VARCHAR(10) DEFAULT NULL COMMENT '项目批次'")
	private String xmpc;
	@Column(name = "LXPFWH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '立项批复文号'")
	private String lxpfwh;
	@Column(name = "JSYDGHXKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '建设用地规划许可证号'")
	private String jsydghxkzh;
	@Column(name = "GYTDSYZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '国有土地使用证号'")
	private String gytdsyzh;
	@Column(name = "JSGCGHXKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '建设工程规划许可证号'")
	private String jsgcghxkzh;
	@Column(name = "JZGCSGXKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '建筑工程施工许可证号'")
	private String jzgcsgxkzh;

	public StProjectInformation() {
	}

	public StProjectInformation(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String xmbh, String xmlx, String xmmc, BigDecimal sscsdm, BigDecimal tzgm, BigDecimal dked, BigDecimal jsgm,
			String xmfljb, String xmpc, String lxpfwh, String jsydghxkzh, String gytdsyzh, String jsgcghxkzh,
			String jzgcsgxkzh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.xmbh = xmbh;
		this.xmlx = xmlx;
		this.xmmc = xmmc;
		this.sscsdm = sscsdm;
		this.tzgm = tzgm;
		this.dked = dked;
		this.jsgm = jsgm;
		this.xmfljb = xmfljb;
		this.xmpc = xmpc;
		this.lxpfwh = lxpfwh;
		this.jsydghxkzh = jsydghxkzh;
		this.gytdsyzh = gytdsyzh;
		this.jsgcghxkzh = jsgcghxkzh;
		this.jzgcsgxkzh = jzgcsgxkzh;
	}

	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.updated_at = new Date();
		this.xmbh = xmbh;
	}

	public String getXmlx() {
		return this.xmlx;
	}

	public void setXmlx(String xmlx) {
		this.updated_at = new Date();
		this.xmlx = xmlx;
	}

	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.updated_at = new Date();
		this.xmmc = xmmc;
	}

	public BigDecimal getSscsdm() {
		return this.sscsdm;
	}

	public void setSscsdm(BigDecimal sscsdm) {
		this.updated_at = new Date();
		this.sscsdm = sscsdm;
	}

	public BigDecimal getTzgm() {
		return this.tzgm;
	}

	public void setTzgm(BigDecimal tzgm) {
		this.updated_at = new Date();
		this.tzgm = tzgm;
	}

	public BigDecimal getDked() {
		return this.dked;
	}

	public void setDked(BigDecimal dked) {
		this.updated_at = new Date();
		this.dked = dked;
	}

	public BigDecimal getJsgm() {
		return this.jsgm;
	}

	public void setJsgm(BigDecimal jsgm) {
		this.updated_at = new Date();
		this.jsgm = jsgm;
	}

	public String getXmfljb() {
		return this.xmfljb;
	}

	public void setXmfljb(String xmfljb) {
		this.updated_at = new Date();
		this.xmfljb = xmfljb;
	}

	public String getXmpc() {
		return this.xmpc;
	}

	public void setXmpc(String xmpc) {
		this.updated_at = new Date();
		this.xmpc = xmpc;
	}

	public String getLxpfwh() {
		return this.lxpfwh;
	}

	public void setLxpfwh(String lxpfwh) {
		this.updated_at = new Date();
		this.lxpfwh = lxpfwh;
	}

	public String getJsydghxkzh() {
		return this.jsydghxkzh;
	}

	public void setJsydghxkzh(String jsydghxkzh) {
		this.updated_at = new Date();
		this.jsydghxkzh = jsydghxkzh;
	}

	public String getGytdsyzh() {
		return this.gytdsyzh;
	}

	public void setGytdsyzh(String gytdsyzh) {
		this.updated_at = new Date();
		this.gytdsyzh = gytdsyzh;
	}

	public String getJsgcghxkzh() {
		return this.jsgcghxkzh;
	}

	public void setJsgcghxkzh(String jsgcghxkzh) {
		this.updated_at = new Date();
		this.jsgcghxkzh = jsgcghxkzh;
	}

	public String getJzgcsgxkzh() {
		return jzgcsgxkzh;
	}

	public void setJzgcsgxkzh(String jzgcsgxkzh) {
		this.updated_at = new Date();
		this.jzgcsgxkzh = jzgcsgxkzh;
	}

}
