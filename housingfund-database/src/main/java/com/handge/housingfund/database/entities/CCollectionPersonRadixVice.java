package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/21. 个人基数调整
 */
@Entity
@Table(name = "c_collection_person_radix_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_person_radix_vice", comment = "个人基数调整")
public class CCollectionPersonRadixVice extends Common implements Serializable {

	private static final long serialVersionUID = 6201738211196165412L;
	@Column(name = "FSRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT '发生人数'")
	private Long fsrs;
	@Column(name = "DWJCBL", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '单位缴存比例'")
	private BigDecimal dwjcbl;
	@Column(name = "GRJCBL", columnDefinition = "NUMERIC(18,2) DEFAULT 0 COMMENT '个人缴存比例'")
	private BigDecimal grjcbl;
	@Column(name = "SXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '生效年月'")
	private String sxny;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "JSTZXQ", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '基数调整详情'")
	private Set<CCollectionPersonRadixDetailVice> jstzxq;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public Long getFsrs() {
		return fsrs;
	}

	public void setFsrs(Long fsrs) {
		this.updated_at = new Date();
		this.fsrs = fsrs;
	}

	public String getSxny() {
		return sxny;
	}

	public void setSxny(String sxny) {
		this.updated_at = new Date();
		this.sxny = sxny;
	}

	public Set<CCollectionPersonRadixDetailVice> getJstzxq() {
		return jstzxq;
	}

	public void setJstzxq(Set<CCollectionPersonRadixDetailVice> jstzxq) {
		this.updated_at = new Date();
		this.jstzxq = jstzxq;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public BigDecimal getDwjcbl() {
		return dwjcbl;
	}

	public void setDwjcbl(BigDecimal dwjcbl) {
		this.dwjcbl = dwjcbl;
	}

	public BigDecimal getGrjcbl() {
		return grjcbl;
	}

	public void setGrjcbl(BigDecimal grjcbl) {
		this.grjcbl = grjcbl;
	}

	public CCollectionPersonRadixVice() {
		super();

	}

	public CCollectionPersonRadixVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			Long fsrs, String sxny, Set<CCollectionPersonRadixDetailVice> jstzxq,
			StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.fsrs = fsrs;
		this.sxny = sxny;
		this.jstzxq = jstzxq;
		this.dwywmx = dwywmx;
	}
}
