package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 个人开户，转移账户情况时，记录转移前个人账户信息
 */
@Entity
@Table(name = "c_collection_individual_account_transfer_new_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_transfer_new_vice", comment = "个人开户时，保存个人账户转移前的信息")
public class CCollectionIndividualAccountTransferNewVice extends Common implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '职工个人账号'")
	private String grzh;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ZCDW", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '转出单位账号'")
	private StCommonUnit zcdw;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ZRDW", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '转入单位账号'")
	private StCommonUnit zrdw;

	@Column(name = "YGRJCJS", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '原单位下个人月缴存基数'")
	private String ygrjcjs;
	@Column(name = "YDWYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '原单位下单位月缴存额'")
	private String ydwyjce;
	@Column(name = "YGRYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '原单位下个人月缴存额'")
	private String ygryjce;
	@Column(name = "ZYSGRZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '转移时个人账户余额'")
	private String zysgrzhye;
	@Column(name = "ZYSDNGJYE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '转移时个人账户当年归集余额'")
	private String zysdngjye;
	@Column(name = "ZYSSNJZYE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '转移时个人账户上年结转余额）'")
	private BigDecimal zyssnjzye = BigDecimal.ZERO;
	@Column(name = "SXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '转移时默认的的生效年月，用于清册业务'")
	private String sxny;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	private StCollectionPersonalBusinessDetails grywmx;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public StCommonUnit getZcdw() {
		return zcdw;
	}

	public void setZcdw(StCommonUnit zcdw) {
		this.updated_at = new Date();
		this.zcdw = zcdw;
	}

	public StCommonUnit getZrdw() {
		return zrdw;
	}

	public void setZrdw(StCommonUnit zrdw) {
		this.updated_at = new Date();
		this.zrdw = zrdw;
	}

	public String getYgrjcjs() {
		return ygrjcjs;
	}

	public void setYgrjcjs(String ygrjcjs) {
		this.updated_at = new Date();
		this.ygrjcjs = ygrjcjs;
	}

	public String getYdwyjce() {
		return ydwyjce;
	}

	public void setYdwyjce(String ydwyjce) {
		this.updated_at = new Date();
		this.ydwyjce = ydwyjce;
	}

	public String getYgryjce() {
		return ygryjce;
	}

	public void setYgryjce(String ygryjce) {
		this.updated_at = new Date();
		this.ygryjce = ygryjce;
	}

	public String getZysgrzhye() {
		return zysgrzhye;
	}

	public void setZysgrzhye(String zysgrzhye) {
		this.updated_at = new Date();
		this.zysgrzhye = zysgrzhye;
	}

	public String getZysdngjye() {
		return zysdngjye;
	}

	public void setZysdngjye(String zysdngjye) {
		this.updated_at = new Date();
		this.zysdngjye = zysdngjye;
	}

	public BigDecimal getZyssnjzye() {
		return zyssnjzye;
	}

	public void setZyssnjzye(BigDecimal zyssnjzye) {
		this.updated_at = new Date();
		this.zyssnjzye = zyssnjzye;
	}

	public String getSxny() {
		return sxny;
	}

	public void setSxny(String sxny) {
		this.sxny = sxny;
	}

	public CCollectionIndividualAccountTransferNewVice() {
		super();

	}




	public CCollectionIndividualAccountTransferNewVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, StCommonUnit zcdw, StCommonUnit zrdw, String ygrjcjs, String ydwyjce, String ygryjce,
			String zysgrzhye, String zysdngjye, BigDecimal zyssnjzye) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.zcdw = zcdw;
		this.zrdw = zrdw;
		this.ygrjcjs = ygrjcjs;
		this.ydwyjce = ydwyjce;
		this.ygryjce = ygryjce;
		this.zysgrzhye = zysgrzhye;
		this.zysdngjye = zysdngjye;
		this.zyssnjzye = zyssnjzye;
	}

	public StCollectionPersonalBusinessDetails getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(StCollectionPersonalBusinessDetails grywmx) {
		this.grywmx = grywmx;
	}
}
