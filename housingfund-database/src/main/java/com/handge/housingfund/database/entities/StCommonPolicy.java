package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_common_policy")
@org.hibernate.annotations.Table(appliesTo = "st_common_policy", comment = "政策信息 表4.0.4")
public class StCommonPolicy extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 4791288811733104770L;
	@Column(name = "DWJCBLSX", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '单位缴存比例上限'")
	private BigDecimal dwjcblsx = BigDecimal.ZERO;
	@Column(name = "DWJCBLXX", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '单位缴存比例下限'")
	private BigDecimal dwjcblxx = BigDecimal.ZERO;
	@Column(name = "GRJCBLSX", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '个人缴存比例上限'")
	private BigDecimal grjcblsx = BigDecimal.ZERO;
	@Column(name = "GRJCBLXX", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '个人缴存比例下限'")
	private BigDecimal grjcblxx = BigDecimal.ZERO;
	@Column(name = "YJCESX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月缴存额上限'")
	private BigDecimal yjcesx = BigDecimal.ZERO;
	@Column(name = "YJCEXX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月缴存额下限'")
	private BigDecimal yjcexx = BigDecimal.ZERO;
	@Column(name = "GRZFDKZCNX", columnDefinition = "NUMERIC(2,0) DEFAULT NULL COMMENT '个人住房贷款最长年限'")
	private BigDecimal grzfdkzcnx = BigDecimal.ZERO;
	@Column(name = "GRZFDKZGED", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人住房贷款最高额度'")
	private BigDecimal grzfdkzged = BigDecimal.ZERO;
	@Column(name = "GRZFDKZGDKBL", columnDefinition = "NUMERIC(3,2) DEFAULT NULL COMMENT '个人住房贷款最高贷款比例'")
	private BigDecimal grzfdkzgdkbl = BigDecimal.ZERO;
	@Column(name = "LLLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '利率类型'")
	private String lllx;
	@Column(name = "ZXLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '执行利率'")
	private BigDecimal zxll = BigDecimal.ZERO;
	@Column(name = "XMDKZCNX", columnDefinition = "NUMERIC(2,0) DEFAULT NULL COMMENT '项目贷款最长年限'")
	private BigDecimal xmdkzcnx = BigDecimal.ZERO;
	@Column(name = "XMDKZGDKBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '项目贷款最高贷款比例'")
	private BigDecimal xmdkzgdkbl = BigDecimal.ZERO;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '政策信息 扩展表'")
    private CCommonPolicyExtension cCommonPolicyExtension;


	public StCommonPolicy() {
		super();

	}

	public StCommonPolicy(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                          BigDecimal dwjcblsx, BigDecimal dwjcblxx, BigDecimal grjcblsx, BigDecimal grjcblxx,
                          BigDecimal yjcesx, BigDecimal yjcexx, BigDecimal grzfdkzcnx, BigDecimal grzfdkzged,
                          BigDecimal grzfdkzgdkbl, String lllx, BigDecimal zxll, BigDecimal xmdkzcnx,
                          BigDecimal xmdkzgdkbl, CCommonPolicyExtension cCommonPolicyExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dwjcblsx = dwjcblsx;
		this.dwjcblxx = dwjcblxx;
		this.grjcblsx = grjcblsx;
		this.grjcblxx = grjcblxx;
		this.yjcesx = yjcesx;
		this.yjcexx = yjcexx;
		this.grzfdkzcnx = grzfdkzcnx;
		this.grzfdkzged = grzfdkzged;
		this.grzfdkzgdkbl = grzfdkzgdkbl;
		this.lllx = lllx;
		this.zxll = zxll;
		this.xmdkzcnx = xmdkzcnx;
		this.xmdkzgdkbl = xmdkzgdkbl;
        this.cCommonPolicyExtension = cCommonPolicyExtension;
    }

    public CCommonPolicyExtension getcCommonPolicyExtension() {
        return cCommonPolicyExtension;
    }

    public void setcCommonPolicyExtension(CCommonPolicyExtension cCommonPolicyExtension) {
        this.updated_at = new Date();
        this.cCommonPolicyExtension = cCommonPolicyExtension;
	}

	public BigDecimal getDwjcblsx() {
		return this.dwjcblsx;
	}

	public void setDwjcblsx(BigDecimal dwjcblsx) {
		this.updated_at = new Date();
		this.dwjcblsx = dwjcblsx;
	}

	public BigDecimal getDwjcblxx() {
		return this.dwjcblxx;
	}

	public void setDwjcblxx(BigDecimal dwjcblxx) {
		this.updated_at = new Date();
		this.dwjcblxx = dwjcblxx;
	}

	public BigDecimal getGrjcblsx() {
		return this.grjcblsx;
	}

	public void setGrjcblsx(BigDecimal grjcblsx) {
		this.updated_at = new Date();
		this.grjcblsx = grjcblsx;
	}

	public BigDecimal getGrjcblxx() {
		return this.grjcblxx;
	}

	public void setGrjcblxx(BigDecimal grjcblxx) {
		this.updated_at = new Date();
		this.grjcblxx = grjcblxx;
	}

	public BigDecimal getYjcesx() {
		return this.yjcesx;
	}

	public void setYjcesx(BigDecimal yjcesx) {
		this.updated_at = new Date();
		this.yjcesx = yjcesx;
	}

	public BigDecimal getYjcexx() {
		return this.yjcexx;
	}

	public void setYjcexx(BigDecimal yjcexx) {
		this.updated_at = new Date();
		this.yjcexx = yjcexx;
	}

	public BigDecimal getGrzfdkzcnx() {
		return this.grzfdkzcnx;
	}

	public void setGrzfdkzcnx(BigDecimal grzfdkzcnx) {
		this.updated_at = new Date();
		this.grzfdkzcnx = grzfdkzcnx;
	}

	public BigDecimal getGrzfdkzged() {
		return this.grzfdkzged;
	}

	public void setGrzfdkzged(BigDecimal grzfdkzged) {
		this.updated_at = new Date();
		this.grzfdkzged = grzfdkzged;
	}

	public BigDecimal getGrzfdkzgdkbl() {
		return this.grzfdkzgdkbl;
	}

	public void setGrzfdkzgdkbl(BigDecimal grzfdkzgdkbl) {
		this.updated_at = new Date();
		this.grzfdkzgdkbl = grzfdkzgdkbl;
	}

	public String getLllx() {
		return this.lllx;
	}

	public void setLllx(String lllx) {
		this.updated_at = new Date();
		this.lllx = lllx;
	}

	public BigDecimal getZxll() {
		return this.zxll;
	}

	public void setZxll(BigDecimal zxll) {
		this.updated_at = new Date();
		this.zxll = zxll;
	}

	public BigDecimal getXmdkzcnx() {
		return this.xmdkzcnx;
	}

	public void setXmdkzcnx(BigDecimal xmdkzcnx) {
		this.updated_at = new Date();
		this.xmdkzcnx = xmdkzcnx;
	}

	public BigDecimal getXmdkzgdkbl() {
		return this.xmdkzgdkbl;
	}

	public void setXmdkzgdkbl(BigDecimal xmdkzgdkbl) {
		this.updated_at = new Date();
		this.xmdkzgdkbl = xmdkzgdkbl;
	}

}
