package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by Liujuhao on 2017/7/10. 内部转移
 */
@Entity
@Table(name = "c_collection_individual_account_transfer_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_individual_account_transfer_vice", comment = "针对“个人内部转移”")
public class CCollectionIndividualAccountTransferVice extends Common implements Serializable {
	private static final long serialVersionUID = 7612472638263544616L;
	@Column(name = "ZCDWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '转出单位账号（仅内部转移业务）'")
	private String zcdwzh;
	@Column(name = "ZCDWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '转出单位名称（仅内部转移业务）'")
	private String zcdwmc;
	@Column(name = "ZYSXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '转移生效年月（仅内部转移业务）'")
	private String zysxny;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "NBZYXQ", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '内部转移详情'")
	private Set<CCollectionIndividualAccountTransferDetailVice> nbzyxq;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public String getZcdwzh() {
		return zcdwzh;
	}

	public void setZcdwzh(String zcdwzh) {
		this.updated_at = new Date();
		this.zcdwzh = zcdwzh;
	}

	public String getZcdwmc() {
		return zcdwmc;
	}

	public void setZcdwmc(String zcdwmc) {
		this.updated_at = new Date();
		this.zcdwmc = zcdwmc;
	}

	public String getZysxny() {
		return zysxny;
	}

	public void setZysxny(String zysxny) {
		this.updated_at = new Date();
		this.zysxny = zysxny;
	}

	public Set<CCollectionIndividualAccountTransferDetailVice> getNbzyxq() {
		return nbzyxq;
	}

	public void setNbzyxq(Set<CCollectionIndividualAccountTransferDetailVice> nbzyxq) {
		this.updated_at = new Date();
		this.nbzyxq = nbzyxq;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public CCollectionIndividualAccountTransferVice() {
		super();

	}

	public CCollectionIndividualAccountTransferVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String zcdwzh, String zcdwmc, String zysxny,
			Set<CCollectionIndividualAccountTransferDetailVice> nbzyxq, StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.zcdwzh = zcdwzh;
		this.zcdwmc = zcdwmc;
		this.zysxny = zysxny;
		this.nbzyxq = nbzyxq;
		this.dwywmx = dwywmx;
	}

	// public void setYwzt(String s) {
	// this.updated_at = new Date();
	// dwywmx.getExtension().setYwzt(s);
	// }
	//
	// public void setStep(String next) {
	// this.updated_at = new Date();
	// // TODO
	// //拓展表无step
	// // /dwywmx.getExtension().sets;
	// }
}
