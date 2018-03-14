package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_unit_information_basic_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_information_basic_vice", comment = "针对“单位账户设立（开户）”“单位账户变更”")
public class CCollectionUnitInformationBasicVice extends Common implements Serializable {
	private static final long serialVersionUID = 3655030956621831534L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "DWLB", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位类别'")
	private String dwlb;
	@Column(name = "KGQK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '控股情况'")
	private String kgqk;
	@Column(name = "XZQY", columnDefinition = "VARCHAR(10) DEFAULT NULL COMMENT '行政区域'")
	private String xzqy;
	@Column(name = "PZJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '批准机关名称'")
	private String pzjgmc;
	@Column(name = "PZJGJB", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '批准机关级别'")
	private String pzjgjb;
	@Column(name = "DJZCH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '登记注册号'")
	private String djzch;
	@Column(name = "DJSYYZ", columnDefinition = "VARCHAR(100) DEFAULT NULL COMMENT '登记使用印章'")
	private String djsyyz;
	@Column(name = "DWLXDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位联系电话'")
	private String dwlxdh;
	@Column(name = "DWCZHM", columnDefinition = "VARCHAR(50) DEFAULT NULL COMMENT '单位传真号码'")
	private String dwczhm;
	@Column(name = "DWFXR", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位发薪日'")
	private String dwfxr;
	@Column(name = "FXZHKHYH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '发薪账户开户银行'")
	private String fxzhkhyh;
	@Column(name = "FXZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '发薪账户'")
	private String fxzh;
	@Column(name = "FXZHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '发薪账户户名'")
	private String fxzhhm;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间 yyyy-MM-dd hh:mm'")
	private Date slsj;
	@Column(name = "DWSCHJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '单位首次汇缴年月'")
	private String dwschjny;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beiZhu;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "DDSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到达时间'")
	private Date ddsj;
	@Column(name = "CZMC", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	// @Column(name = "YWZT", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '业务状态'")
	// private String ywzt;
	// @Column(name = "STEP", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '审核状态'")
	// private String step;
	@Column(name = "DWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位名称'")
	private String dwmc;
	@Column(name = "DWDZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位地址'")
	private String dwdz;
	@Column(name = "DWFRDBXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '单位法人代表姓名'")
	private String dwfrdbxm;
	@Column(name = "DWFRDBZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位法人代表证件类型 01：身份证 02：军官证 03：护照 04：外国人永久居留证 99：其他'")
	private String dwfrdbzjlx;
	@Column(name = "DWFRDBZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '单位法人代表证件号码'")
	private String dwfrdbzjhm;
	@Column(name = "DWLSGX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位隶属关系 参考GBT12404-1997'")
	private String dwlsgx;
	@Column(name = "DWJJLX", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '单位经济类型 参考GB/T 12402-2000'")
	private String dwjjlx;
	@Column(name = "DWSSHY", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '单位所属行业 GB/T 4754'")
	private String dwsshy;
	@Column(name = "DWYB", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '单位邮编 参照：YD/T 603'")
	private String dwyb;
	@Column(name = "ZZJGDM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '组织机构代码 GB11714'")
	private String zzjgdm;
	@Column(name = "DWDZXX", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '单位电子信箱'")
	private String dwdzxx;
	@Column(name = "DWSLRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '单位设立日期 YYYYMMDD GB/T 7408'")
	private Date dwslrq;
	@Column(name = "JBRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '经办人姓名'")
	private String jbrxm;
	@Column(name = "JBRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '经办人证件类型 01：身份证 02：军官证 03：护照 04：外国人永久居留证 99：其他'")
	private String jbrzjlx;
	@Column(name = "JBRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '经办人证件号码 GB11643'")
	private String jbrzjhm;
	@Column(name = "JBRSJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '经办人手机号码'")
	private String jbrsjhm;
	@Column(name = "JBRGDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '经办人固定电话号码'")
	private String jbrgddhhm;
	@Column(name = "STYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '受托银行代码'")
	private String styhdm;
	@Column(name = "STYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '受托银行名称'")
	private String styhmc;
	@Column(name = "YWMXLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '业务明细类型 附A.5'")
	private String ywmxlx;
	@Column(name = "DWJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '单位缴存比例'")
	private BigDecimal dwjcbl = BigDecimal.ZERO;
	@Column(name = "GRJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '个人缴存比例'")
	private BigDecimal grjcbl = BigDecimal.ZERO;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public String getDwlb() {
		return dwlb;
	}

	public void setDwlb(String dwlb) {
		this.updated_at = new Date();
		this.dwlb = dwlb;
	}

	public String getKgqk() {
		return kgqk;
	}

	public void setKgqk(String kgqk) {
		this.updated_at = new Date();
		this.kgqk = kgqk;
	}

	public String getXzqy() {
		return xzqy;
	}

	public void setXzqy(String xzqy) {
		this.updated_at = new Date();
		this.xzqy = xzqy;
	}

	public String getPzjgmc() {
		return pzjgmc;
	}

	public void setPzjgmc(String pzjgmc) {
		this.updated_at = new Date();
		this.pzjgmc = pzjgmc;
	}

	public String getPzjgjb() {
		return pzjgjb;
	}

	public void setPzjgjb(String pzjgjb) {
		this.updated_at = new Date();
		this.pzjgjb = pzjgjb;
	}

	public String getDjzch() {
		return djzch;
	}

	public void setDjzch(String djzch) {
		this.updated_at = new Date();
		this.djzch = djzch;
	}

	public String getDjsyyz() {
		return djsyyz;
	}

	public void setDjsyyz(String djsyyz) {
		this.updated_at = new Date();
		this.djsyyz = djsyyz;
	}

	public String getDwlxdh() {
		return dwlxdh;
	}

	public void setDwlxdh(String dwlxdh) {
		this.updated_at = new Date();
		this.dwlxdh = dwlxdh;
	}

	public String getDwczhm() {
		return dwczhm;
	}

	public void setDwczhm(String dwczhm) {
		this.updated_at = new Date();
		this.dwczhm = dwczhm;
	}

	public String getFxzhkhyh() {
		return fxzhkhyh;
	}

	public void setFxzhkhyh(String fxzhkhyh) {
		this.updated_at = new Date();
		this.fxzhkhyh = fxzhkhyh;
	}

	public String getFxzh() {
		return fxzh;
	}

	public void setFxzh(String fxzh) {
		this.updated_at = new Date();
		this.fxzh = fxzh;
	}

	public String getFxzhhm() {
		return fxzhhm;
	}

	public void setFxzhhm(String fxzhhm) {
		this.updated_at = new Date();
		this.fxzhhm = fxzhhm;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.updated_at = new Date();
		this.slsj = slsj;
	}

	public String getDwschjny() {
		return dwschjny;
	}

	public void setDwschjny(String dwschjny) {
		this.updated_at = new Date();
		this.dwschjny = dwschjny;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.updated_at = new Date();
		this.beiZhu = beiZhu;
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

	public Date getDdsj() {
		return ddsj;
	}

	public void setDdsj(Date ddsj) {
		this.updated_at = new Date();
		this.ddsj = ddsj;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.updated_at = new Date();
		this.czmc = czmc;
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

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.updated_at = new Date();
		this.dwmc = dwmc;
	}

	public String getDwdz() {
		return dwdz;
	}

	public void setDwdz(String dwdz) {
		this.updated_at = new Date();
		this.dwdz = dwdz;
	}

	public String getDwfrdbxm() {
		return dwfrdbxm;
	}

	public void setDwfrdbxm(String dwfrdbxm) {
		this.updated_at = new Date();
		this.dwfrdbxm = dwfrdbxm;
	}

	public String getDwfrdbzjlx() {
		return dwfrdbzjlx;
	}

	public void setDwfrdbzjlx(String dwfrdbzjlx) {
		this.updated_at = new Date();
		this.dwfrdbzjlx = dwfrdbzjlx;
	}

	public String getDwfrdbzjhm() {
		return dwfrdbzjhm;
	}

	public void setDwfrdbzjhm(String dwfrdbzjhm) {
		this.updated_at = new Date();
		this.dwfrdbzjhm = dwfrdbzjhm;
	}

	public String getDwlsgx() {
		return dwlsgx;
	}

	public void setDwlsgx(String dwlsgx) {
		this.updated_at = new Date();
		this.dwlsgx = dwlsgx;
	}

	public String getDwjjlx() {
		return dwjjlx;
	}

	public void setDwjjlx(String dwjjlx) {
		this.updated_at = new Date();
		this.dwjjlx = dwjjlx;
	}

	public String getDwsshy() {
		return dwsshy;
	}

	public void setDwsshy(String dwsshy) {
		this.updated_at = new Date();
		this.dwsshy = dwsshy;
	}

	public String getDwyb() {
		return dwyb;
	}

	public void setDwyb(String dwyb) {
		this.updated_at = new Date();
		this.dwyb = dwyb;
	}

	public String getZzjgdm() {
		return zzjgdm;
	}

	public void setZzjgdm(String zzjgdm) {
		this.updated_at = new Date();
		this.zzjgdm = zzjgdm;
	}

	public String getDwdzxx() {
		return dwdzxx;
	}

	public void setDwdzxx(String dwdzxx) {
		this.updated_at = new Date();
		this.dwdzxx = dwdzxx;
	}

	public Date getDwslrq() {
		return dwslrq;
	}

	public void setDwslrq(Date dwslrq) {
		this.updated_at = new Date();
		this.dwslrq = dwslrq;
	}

	public String getJbrxm() {
		return jbrxm;
	}

	public void setJbrxm(String jbrxm) {
		this.updated_at = new Date();
		this.jbrxm = jbrxm;
	}

	public String getJbrzjlx() {
		return jbrzjlx;
	}

	public void setJbrzjlx(String jbrzjlx) {
		this.updated_at = new Date();
		this.jbrzjlx = jbrzjlx;
	}

	public String getJbrzjhm() {
		return jbrzjhm;
	}

	public void setJbrzjhm(String jbrzjhm) {
		this.updated_at = new Date();
		this.jbrzjhm = jbrzjhm;
	}

	public String getJbrsjhm() {
		return jbrsjhm;
	}

	public void setJbrsjhm(String jbrsjhm) {
		this.updated_at = new Date();
		this.jbrsjhm = jbrsjhm;
	}

	public String getJbrgddhhm() {
		return jbrgddhhm;
	}

	public void setJbrgddhhm(String jbrgddhhm) {
		this.updated_at = new Date();
		this.jbrgddhhm = jbrgddhhm;
	}

	public String getStyhdm() {
		return styhdm;
	}

	public void setStyhdm(String styhdm) {
		this.updated_at = new Date();
		this.styhdm = styhdm;
	}

	public String getStyhmc() {
		return styhmc;
	}

	public void setStyhmc(String styhmc) {
		this.updated_at = new Date();
		this.styhmc = styhmc;
	}

	public String getYwmxlx() {
		return ywmxlx;
	}

	public void setYwmxlx(String ywmxlx) {
		this.updated_at = new Date();
		this.ywmxlx = ywmxlx;
	}

	public BigDecimal getDwjcbl() {
		return dwjcbl;
	}

	public void setDwjcbl(BigDecimal dwjcbl) {
		this.updated_at = new Date();
		this.dwjcbl = dwjcbl;
	}

	public BigDecimal getGrjcbl() {
		return grjcbl;
	}

	public void setGrjcbl(BigDecimal grjcbl) {
		this.updated_at = new Date();
		this.grjcbl = grjcbl;
	}

	public String getDwfxr() {
		return dwfxr;
	}

	public void setDwfxr(String dwfxr) {
		this.updated_at = new Date();
		this.dwfxr = dwfxr;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public CCollectionUnitInformationBasicVice() {
		super();

	}

	public CCollectionUnitInformationBasicVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String ywlsh, String dwzh, String dwlb, String kgqk, String xzqy, String pzjgmc,
			String pzjgjb, String djzch, String djsyyz, String dwlxdh, String dwczhm, String dwfxr, String fxzhkhyh,
			String fxzh, String fxzhhm, Date slsj, String dwschjny, String blzl, String beiZhu, String czy, String ywwd,
			Date ddsj, String czmc, /** String ywzt, String step, **/
			String dwmc, String dwdz, String dwfrdbxm, String dwfrdbzjlx, String dwfrdbzjhm, String dwlsgx, String dwjjlx, String dwsshy, String dwyb, String zzjgdm, String dwdzxx, Date dwslrq, String jbrxm, String jbrzjlx, String jbrzjhm, String jbrsjhm, String jbrgddhhm, String styhdm, String styhmc, String ywmxlx, BigDecimal dwjcbl, BigDecimal grjcbl, StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.dwzh = dwzh;
		this.dwlb = dwlb;
		this.kgqk = kgqk;
		this.xzqy = xzqy;
		this.pzjgmc = pzjgmc;
		this.pzjgjb = pzjgjb;
		this.djzch = djzch;
		this.djsyyz = djsyyz;
		this.dwlxdh = dwlxdh;
		this.dwczhm = dwczhm;
		this.dwfxr = dwfxr;
		this.fxzhkhyh = fxzhkhyh;
		this.fxzh = fxzh;
		this.fxzhhm = fxzhhm;
		this.slsj = slsj;
		this.dwschjny = dwschjny;
		this.blzl = blzl;
		this.beiZhu = beiZhu;
		this.czy = czy;
		this.ywwd = ywwd;
		this.ddsj = ddsj;
		this.czmc = czmc;
		// this.ywzt = ywzt;
		// this.step = step;
		this.dwmc = dwmc;
		this.dwdz = dwdz;
		this.dwfrdbxm = dwfrdbxm;
		this.dwfrdbzjlx = dwfrdbzjlx;
		this.dwfrdbzjhm = dwfrdbzjhm;
		this.dwlsgx = dwlsgx;
		this.dwjjlx = dwjjlx;
		this.dwsshy = dwsshy;
		this.dwyb = dwyb;
		this.zzjgdm = zzjgdm;
		this.dwdzxx = dwdzxx;
		this.dwslrq = dwslrq;
		this.jbrxm = jbrxm;
		this.jbrzjlx = jbrzjlx;
		this.jbrzjhm = jbrzjhm;
		this.jbrsjhm = jbrsjhm;
		this.jbrgddhhm = jbrgddhhm;
		this.styhdm = styhdm;
		this.styhmc = styhmc;
		this.ywmxlx = ywmxlx;
		this.dwjcbl = dwjcbl;
		this.grjcbl = grjcbl;
		this.dwywmx = dwywmx;
	}

}
