package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_individual_account_action_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_action_vice", comment = "针对“冻结个人账户”“解冻个人账户”“封存个人账户”“启封个人账户”")
public class CCollectionIndividualAccountActionVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = -4064783642336992580L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	// @Column(name = "YWZT", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '业务状态'")
	// private String ywzt;
	// @Column(name = "STEP", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '审核状态'")
	// private String step;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "CZMC", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	@Column(name = "CZYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作原因'")
	private String czyy;
	@Column(name = "BeiZhu", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '备注'")
	private String beiZhu;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "DJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '冻结金额'")
	private BigDecimal djje = BigDecimal.ZERO;
	@Column(name = "SXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '生效年月'")
	private String sxny;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	private StCollectionPersonalBusinessDetails grywmx;

	public StCollectionPersonalBusinessDetails getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(StCollectionPersonalBusinessDetails grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	// public String getYwzt() {
	// return ywzt;
	// }
	//
	// public void setYwzt(String ywzt) {
	// this.updated_at = new Date();
	// this.ywzt = ywzt;
	// }
	//
	// public String getStep() {
	// return step;
	// }
	//
	// public void setStep(String step) {
	// this.updated_at = new Date();
	// this.step = step;
	// }

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.updated_at = new Date();
		this.czmc = czmc;
	}

	public String getCzyy() {
		return czyy;
	}

	public void setCzyy(String czyy) {
		this.updated_at = new Date();
		this.czyy = czyy;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.updated_at = new Date();
		this.beiZhu = beiZhu;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.updated_at = new Date();
		this.slsj = slsj;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.updated_at = new Date();
		this.czy = czy;
	}

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.updated_at = new Date();
		this.ywwd = ywwd;
	}

	public BigDecimal getDjje() {
		return djje;
	}

	public void setDjje(BigDecimal djje) {
		this.updated_at = new Date();
		this.djje = djje;
	}

	public String getSxny() {
		return sxny;
	}

	public void setSxny(String sxny) {
		this.updated_at = new Date();
		this.sxny = sxny;
	}

	public CCollectionIndividualAccountActionVice() {
		super();

	}

	public CCollectionIndividualAccountActionVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String ywlsh, String grzh, String dwzh,
			/** String ywzt, String step, **/
			String blzl, String czmc, String czyy, String beiZhu, Date slsj, String czy, String ywwd, BigDecimal djje, String sxny, StCollectionPersonalBusinessDetails grywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.grzh = grzh;
		this.dwzh = dwzh;
		// this.ywzt = ywzt;
		// this.step = step;
		this.blzl = blzl;
		this.czmc = czmc;
		this.czyy = czyy;
		this.beiZhu = beiZhu;
		this.slsj = slsj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.djje = djje;
		this.sxny = sxny;
		this.grywmx = grywmx;
	}

}