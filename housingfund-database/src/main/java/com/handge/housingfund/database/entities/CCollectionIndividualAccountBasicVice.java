package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_individual_account_basic_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_basic_vice", comment = "针对“设立个人账户““变更个人账户”")
public class CCollectionIndividualAccountBasicVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 8972537611988720037L;

	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "GJJSCHJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '公积金首次汇缴年月'")
	private String gjjschjny;
	// @Column(name = "YWZT", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '业务状态'")
	// private String ywzt;
	// @Column(name = "STEP", columnDefinition = "char(2) DEFAULT NULL COMMENT
	// '审核状态'")
	// private String step;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "GJHTQYWLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '归集和提取业务类型'")
	private String gjhtqywlx;
	@Column(name = "XingMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '姓名'")
	private String xingMing;
	@Column(name = "XMQP", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '姓名全拼'")
	private String xmqp;
	@Column(name = "ZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '证件类型'")
	private String zjlx;
	@Column(name = "ZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '证件号码'")
	private String zjhm;
	@Column(name = "XingBie", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '性别'")
	private String xingBie;
	@Column(name = "CSNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '出生年月 YYYY-MM'")
	private String csny;
	@Column(name = "HYZK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '婚姻状况'")
	private String hyzk;
	@Column(name = "XueLi", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '学历'")
	private String xueLi;
	@Column(name = "ZhiYe", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '职业'")
	private String zhiYe;
	@Column(name = "ZhiCheng", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '职称'")
	private String zhiCheng;
	@Column(name = "ZhiWu", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '职务'")
	private String zhiWu;
	@Column(name = "JTYSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '家庭月收入'")
	private BigDecimal jtysr = BigDecimal.ZERO;
	@Column(name = "SJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '手机号码'")
	private String sjhm;
	@Column(name = "GDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '固定电话号码'")
	private String gddhhm;
	@Column(name = "YZBM", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '邮政编码 YD/T 603'")
	private String yzbm;
	@Column(name = "DZYX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '电子邮箱'")
	private String dzyx;
	@Column(name = "JTZZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '家庭住址'")
	private String jtzz;
	@Column(name = "GRJCJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人缴存基数'")
	private BigDecimal grjcjs = BigDecimal.ZERO;
	@Column(name = "GRCKZHHM", length = 30, columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '个人存款账户号码'")
	private String grckzhhm;
	@Column(name = "GRCKZHKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '个人存款账户开户银行名称'")
	private String grckzhkhyhmc;
	@Column(name = "GRCKZHKHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '个人存款账户开户银行代码。 联行号前三位'")
	private String grckzhkhyhdm;
	@Column(name = "GRZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户余额'")
	private BigDecimal grzhye = BigDecimal.ZERO;
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

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public String getGjjschjny() {
		return gjjschjny;
	}

	public void setGjjschjny(String gjjschjny) {
		this.updated_at = new Date();
		this.gjjschjny = gjjschjny;
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

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getGjhtqywlx() {
		return gjhtqywlx;
	}

	public void setGjhtqywlx(String gjhtqywlx) {
		this.updated_at = new Date();
		this.gjhtqywlx = gjhtqywlx;
	}

	public String getXingMing() {
		return xingMing;
	}

	public void setXingMing(String xingMing) {
		this.updated_at = new Date();
		this.xingMing = xingMing;
	}

	public String getXmqp() {
		return xmqp;
	}

	public void setXmqp(String xmqp) {
		this.updated_at = new Date();
		this.xmqp = xmqp;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.updated_at = new Date();
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.updated_at = new Date();
		this.zjhm = zjhm;
	}

	public String getXingBie() {
		return xingBie;
	}

	public void setXingBie(String xingBie) {
		this.updated_at = new Date();
		this.xingBie = xingBie;
	}

	public String getCsny() {
		return csny;
	}

	public void setCsny(String csny) {
		this.updated_at = new Date();
		this.csny = csny;
	}

	public String getHyzk() {
		return hyzk;
	}

	public void setHyzk(String hyzk) {
		this.updated_at = new Date();
		this.hyzk = hyzk;
	}

	public String getXueLi() {
		return xueLi;
	}

	public void setXueLi(String xueLi) {
		this.updated_at = new Date();
		this.xueLi = xueLi;
	}

	public String getZhiYe() {
		return zhiYe;
	}

	public void setZhiYe(String zhiYe) {
		this.updated_at = new Date();
		this.zhiYe = zhiYe;
	}

	public String getZhiCheng() {
		return zhiCheng;
	}

	public void setZhiCheng(String zhiCheng) {
		this.updated_at = new Date();
		this.zhiCheng = zhiCheng;
	}

	public String getZhiWu() {
		return zhiWu;
	}

	public void setZhiWu(String zhiWu) {
		this.updated_at = new Date();
		this.zhiWu = zhiWu;
	}

	public BigDecimal getJtysr() {
		return jtysr;
	}

	public void setJtysr(BigDecimal jtysr) {
		this.updated_at = new Date();
		this.jtysr = jtysr;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.updated_at = new Date();
		this.sjhm = sjhm;
	}

	public String getGddhhm() {
		return gddhhm;
	}

	public void setGddhhm(String gddhhm) {
		this.updated_at = new Date();
		this.gddhhm = gddhhm;
	}

	public String getYzbm() {
		return yzbm;
	}

	public void setYzbm(String yzbm) {
		this.updated_at = new Date();
		this.yzbm = yzbm;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtzz(String jtzz) {
		this.updated_at = new Date();
		this.jtzz = jtzz;
	}

	public BigDecimal getGrjcjs() {
		return grjcjs;
	}

	public void setGrjcjs(BigDecimal grjcjs) {
		this.updated_at = new Date();
		this.grjcjs = grjcjs;
	}

	public String getGrckzhhm() {
		return grckzhhm;
	}

	public void setGrckzhhm(String grckzhhm) {
		this.updated_at = new Date();
		this.grckzhhm = grckzhhm;
	}

	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}

	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.updated_at = new Date();
		this.grckzhkhyhmc = grckzhkhyhmc;
	}

	public String getGrckzhkhyhdm() {
		return grckzhkhyhdm;
	}

	public void setGrckzhkhyhdm(String grckzhkhyhdm) {
		this.updated_at = new Date();
		this.grckzhkhyhdm = grckzhkhyhdm;
	}

	public BigDecimal getGrzhye() {
		return grzhye;
	}

	public void setGrzhye(BigDecimal grzhye) {
		this.updated_at = new Date();
		this.grzhye = grzhye;
	}

	public String getDzyx() {
		return dzyx;
	}

	public void setDzyx(String dzyx) {
		this.updated_at = new Date();
		this.dzyx = dzyx;
	}

	public CCollectionIndividualAccountBasicVice() {
		super();

	}

	public CCollectionIndividualAccountBasicVice(
			String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String ywlsh, String dwzh, String gjjschjny,
			/** String ywzt, String step, **/
			String blzl, Date slsj, String czy, String ywwd, String grzh, String gjhtqywlx, String xingMing, String xmqp, String zjlx, String zjhm, String xingBie, String csny, String hyzk, String xueLi, String zhiYe, String zhiCheng, String zhiWu, BigDecimal jtysr, String sjhm, String gddhhm, String yzbm, String dzyx, String jtzz, BigDecimal grjcjs, String grckzhhm, String grckzhkhyhmc, String grckzhkhyhdm, BigDecimal grzhye, StCollectionPersonalBusinessDetails grywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.dwzh = dwzh;
		this.gjjschjny = gjjschjny;
		// this.ywzt = ywzt;
		// this.step = step;
		this.blzl = blzl;
		this.slsj = slsj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.grzh = grzh;
		this.gjhtqywlx = gjhtqywlx;
		this.xingMing = xingMing;
		this.xmqp = xmqp;
		this.zjlx = zjlx;
		this.zjhm = zjhm;
		this.xingBie = xingBie;
		this.csny = csny;
		this.hyzk = hyzk;
		this.xueLi = xueLi;
		this.zhiYe = zhiYe;
		this.zhiCheng = zhiCheng;
		this.zhiWu = zhiWu;
		this.jtysr = jtysr;
		this.sjhm = sjhm;
		this.gddhhm = gddhhm;
		this.yzbm = yzbm;
		this.dzyx = dzyx;
		this.jtzz = jtzz;
		this.grjcjs = grjcjs;
		this.grckzhhm = grckzhhm;
		this.grckzhkhyhmc = grckzhkhyhmc;
		this.grckzhkhyhdm = grckzhkhyhdm;
		this.grzhye = grzhye;
		this.grywmx = grywmx;
	}

}
