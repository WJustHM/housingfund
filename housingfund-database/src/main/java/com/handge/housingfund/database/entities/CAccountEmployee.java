package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/12.
 */
@Entity
@Table(name = "c_account_employee")
@org.hibernate.annotations.Table(appliesTo = "c_account_employee", comment = "账户管理-员工表")
public class CAccountEmployee extends Common implements Serializable {

	private static final long serialVersionUID = 2154417261521778296L;

	@Column(name = "XingMing", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '姓名'")
	private String xingMing;

	@Column(name = "ZhangHao", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT  '账号'")
	private String zhangHao;

	@Column(name = "XingBie", nullable = false, columnDefinition = "VARCHAR(1) NOT NULL COMMENT  '性别'")
	private String xingBie;

	@Column(name = "YHTX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '用户头像'")
	private String yhtx;

	@Column(name = "RZSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '入职时间'")
	private Date rzsj;

	@Column(name = "XueLi", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '学历'")
	private String xueLi;

	@Column(name = "YouXiang", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '邮箱'")
	private String youXiang;

	@Column(name = "LXDH", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '联系电话'")
	private String lxdh;

	@Column(name = "QQH", columnDefinition = "VARCHAR(12) DEFAULT NULL COMMENT 'QQ号'")
	private String qqh;

	@Column(name = "WXH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '微信号'")
	private String wxh;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @JoinColumn(name = "cAccountDepartment_id", columnDefinition =
	// "varchar(32) DEFAULT NULL")
	private CAccountDepartment cAccountDepartment;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @JoinColumn(name = "cAccountNetwork_id", columnDefinition = "varchar(32)
	// DEFAULT NULL")
	private CAccountNetwork cAccountNetwork;

	public String getXingMing() {
		return xingMing;
	}

	public void setXingMing(String xingMing) {
		this.updated_at = new Date();
		this.xingMing = xingMing;
	}

	public String getZhangHao() {
		return zhangHao;
	}

	public void setZhangHao(String zhangHao) {
		this.updated_at = new Date();
		this.zhangHao = zhangHao;
	}

	public String getXingBie() {
		return xingBie;
	}

	public void setXingBie(String xingBie) {
		this.updated_at = new Date();
		this.xingBie = xingBie;
	}

	public String getYhtx() {
		return yhtx;
	}

	public void setYhtx(String yhtx) {
		this.updated_at = new Date();
		this.yhtx = yhtx;
	}

	public Date getRzsj() {
		return rzsj;
	}

	public void setRzsj(Date rzsj) {
		this.updated_at = new Date();
		this.rzsj = rzsj;
	}

	public String getXueLi() {
		return xueLi;
	}

	public void setXueLi(String xueLi) {
		this.updated_at = new Date();
		this.xueLi = xueLi;
	}

	public String getYouXiang() {
		return youXiang;
	}

	public void setYouXiang(String youXiang) {
		this.updated_at = new Date();
		this.youXiang = youXiang;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.updated_at = new Date();
		this.lxdh = lxdh;
	}

	public String getQqh() {
		return qqh;
	}

	public void setQqh(String qqh) {
		this.updated_at = new Date();
		this.qqh = qqh;
	}

	public String getWxh() {
		return wxh;
	}

	public void setWxh(String wxh) {
		this.updated_at = new Date();
		this.wxh = wxh;
	}

	public CAccountDepartment getcAccountDepartment() {
		return cAccountDepartment;
	}

	public void setcAccountDepartment(CAccountDepartment cAccountDepartment) {
		this.updated_at = new Date();
		this.cAccountDepartment = cAccountDepartment;
	}

	public CAccountEmployee() {
		super();

	}

	public CAccountNetwork getcAccountNetwork() {
		return cAccountNetwork;
	}

	public void setcAccountNetwork(CAccountNetwork cAccountNetwork) {
		this.updated_at = new Date();
		this.cAccountNetwork = cAccountNetwork;
	}

	public CAccountEmployee(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
			String xingMing, String zhangHao, String xingBie, String yhtx, Date rzsj, String xueLi, String youXiang,
			String lxdh, String qqh, String wxh) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.xingMing = xingMing;
		this.zhangHao = zhangHao;
		this.xingBie = xingBie;
		this.yhtx = yhtx;
		this.rzsj = rzsj;
		this.xueLi = xueLi;
		this.youXiang = youXiang;
		this.lxdh = lxdh;
		this.qqh = qqh;
		this.wxh = wxh;
	}

}
