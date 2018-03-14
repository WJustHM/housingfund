package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/10.
 */
@Entity
@Table(name = "c_collection_individual_account_transfer_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_transfer_detail_vice", comment = "针对账户内部转移，每个业务的转移账号列表")
public class CCollectionIndividualAccountTransferDetailVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 5866915959492106287L;

	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "XingMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '姓名'")
	private String xingMing;
	@Column(name = "ZRDWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '转入单位账号'")
	private String zrdwzh;
	@Column(name = "ZRDWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '转入单位名称'")
	private String zrdwmc;
	@Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String beiZhu;

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getXingMing() {
		return xingMing;
	}

	public void setXingMing(String xingMing) {
		this.updated_at = new Date();
		this.xingMing = xingMing;
	}

	public String getZrdwzh() {
		return zrdwzh;
	}

	public void setZrdwzh(String zrdwzh) {
		this.updated_at = new Date();

		this.zrdwzh = zrdwzh;
	}

	public String getZrdwmc() {
		return zrdwmc;
	}

	public void setZrdwmc(String zrdwmc) {
		this.updated_at = new Date();
		this.zrdwmc = zrdwmc;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.updated_at = new Date();
		this.beiZhu = beiZhu;
	}

	public CCollectionIndividualAccountTransferDetailVice() {
		super();

	}

	public CCollectionIndividualAccountTransferDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, String xingMing, String zrdwzh, String zrdwmc, String beiZhu) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.xingMing = xingMing;
		this.zrdwzh = zrdwzh;
		this.zrdwmc = zrdwmc;
		this.beiZhu = beiZhu;
	}

}
