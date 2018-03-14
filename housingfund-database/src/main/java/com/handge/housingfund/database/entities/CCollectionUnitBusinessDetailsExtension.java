package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_collection_unit_business_details_extension")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_business_details_extension", comment = "缴存单位业务明细扩展表")
public class CCollectionUnitBusinessDetailsExtension extends Common implements Serializable {

	private static final long serialVersionUID = -5318037633377221478L;

	@Column(name = "CZMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	@Column(name = "FCHXHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '封存或销户原因'")
	private String fchxhyy;
	@Column(name = "QTCZYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '其他操作原因（如启封等等）'")
	private String qtczyy;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beizhu;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "YWWD", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务网点对象'")
	private CAccountNetwork ywwd;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "BJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '办结时间'")
	private Date bjsj;
	@Column(name = "SHSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '审核时间'")
	private Date shsj;
	/*
	 * @Column(name = "YWZT", columnDefinition =
	 * "varchar(20) DEFAULT NULL COMMENT '业务状态'") private String ywzt;
	 */
	@Column(name = "STEP", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '状态机状态'")
	private String step;
	@Column(name = "DDSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到达时间'")
	private Date ddsj;
	/*
	 * @Column(name = "YWZT", columnDefinition =
	 * "varchar(20) DEFAULT NULL COMMENT '业务状态'") private String ywzt;
	 */
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
	private String jzpzh;
	@Column(name = "JBRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '经办人姓名'")
	private String jbrxm;
	@Column(name = "JBRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '经办人证件类型'")
	private String jbrzjlx;
	@Column(name = "JBRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '经办人证件号码'")
	private String jbrzjhm;
	@Column(name = "SHBTGYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '审核不通过原因'")
	private String shbtgyy;
	@Column(name = "SXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '生效年月'")
	private String sxny;
	@Column(name = "SHYBH", columnDefinition = "TEXT DEFAULT NULL COMMENT '审核员编号'")
	private String shybh;

	public String getId() {
		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {

		this.czmc = czmc;
	}

	public String getFchxhyy() {
		return fchxhyy;
	}

	public void setFchxhyy(String czyy) {

		this.fchxhyy = czyy;
	}

	public String getQtczyy() {
		return qtczyy;
	}

	public void setQtczyy(String qtczyy) {

		this.qtczyy = qtczyy;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {

		this.beizhu = beizhu;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {

		this.blzl = blzl;
	}

	public CAccountNetwork getYwwd() {
		return ywwd;
	}

	public void setYwwd(CAccountNetwork ywwd) {

		this.ywwd = ywwd;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {

		this.slsj = slsj;
	}

	/*
	 * public String getYwzt() { return ywzt; }
	 * 
	 * public void setYwzt(String ywzt) {
	 * 
	 * this.ywzt = ywzt; }
	 */

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {

		this.czy = czy;
	}

	public String getJzpzh() {
		return jzpzh;
	}

	public void setJzpzh(String jzpzh) {

		this.jzpzh = jzpzh;
	}

	public String getJbrxm() {
		return jbrxm;
	}

	public void setJbrxm(String jbrxm) {

		this.jbrxm = jbrxm;
	}

	public String getJbrzjlx() {
		return jbrzjlx;
	}

	public void setJbrzjlx(String jbrzjlx) {

		this.jbrzjlx = jbrzjlx;
	}

	public String getJbrzjhm() {
		return jbrzjhm;
	}

	public void setJbrzjhm(String jbrzjhm) {

		this.jbrzjhm = jbrzjhm;
	}

	public String getShbtgyy() {
		return shbtgyy;
	}

	public void setShbtgyy(String shbtgyy) {

		this.shbtgyy = shbtgyy;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {

		this.step = step;
	}

	public String getSxny() {
		return sxny;
	}

	public void setSxny(String sxny) {

		this.sxny = sxny;
	}

	public Date getBjsj() {
		return bjsj;
	}

	public void setBjsj(Date bjsj) {

		this.bjsj = bjsj;
	}

	public String getShybh() {
		return shybh;
	}

	public void setShybh(String shybh) {

		this.shybh = shybh;
	}

	public Date getShsj() {
		return shsj;
	}

	public void setShsj(Date shsj) {

		this.shsj = shsj;
	}

	public Date getDdsj() {
		return ddsj;
	}

	public void setDdsj(Date ddsj) {
		this.ddsj = ddsj;
	}

	public CCollectionUnitBusinessDetailsExtension() {
		super();

	}

	public CCollectionUnitBusinessDetailsExtension(
			String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String czmc, String fchxhyy, String qtczyy, String beizhu,
			String blzl, CAccountNetwork ywwd, Date slsj, Date bjsj, Date shsj, Date ddsj,
			/* String ywzt, */ String step, String czy, String jzpzh, String jbrxm, String jbrzjlx, String jbrzjhm,
			String shbtgyy, String sxny, String shybh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.czmc = czmc;
		this.fchxhyy = fchxhyy;
		this.qtczyy = qtczyy;
		this.beizhu = beizhu;
		this.blzl = blzl;
		this.ywwd = ywwd;
		this.slsj = slsj;
		this.bjsj = bjsj;
		this.shsj = shsj;
		/* this.ywzt = ywzt; */
		this.ddsj = ddsj;
		this.step = step;
		this.czy = czy;
		this.jzpzh = jzpzh;
		this.jbrxm = jbrxm;
		this.jbrzjlx = jbrzjlx;
		this.jbrzjhm = jbrzjhm;
		this.shbtgyy = shbtgyy;
		this.sxny = sxny;
		this.shybh = shybh;
	}
}
