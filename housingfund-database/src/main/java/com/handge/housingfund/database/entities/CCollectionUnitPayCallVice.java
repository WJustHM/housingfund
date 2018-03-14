package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/21. 单位催缴
 */
@Entity
@Table(name = "c_collection_unit_paycall_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_paycall_vice", comment = "单位催缴")
public class CCollectionUnitPayCallVice extends Common implements Serializable {

	private static final long serialVersionUID = 4447691281991000867L;
	@Column(name = "CJCS", columnDefinition = "INTEGER(11) DEFAULT 0  COMMENT '催缴次数'")
	private Integer cjcs;
	@Column(name = "ZDCJXX", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '自动催缴信息'")
	private String zdcjxx;
	@Column(name = "YJNY", columnDefinition = "CHAR(6) DEFAULT NULL COMMENT '应缴年月'")
	private String yjny;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CJXQ", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '催缴详情'")
	private Set<CCollectionUnitPayCallDetailVice> cjxq;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;



	public Integer getCjcs() {
		return cjcs;
	}

	public void setCjcs(Integer cjcs) {
		this.cjcs = cjcs;
	}

	public String getZdcjxx() {
		return zdcjxx;
	}

	public void setZdcjxx(String zdcjxx) {
		this.zdcjxx = zdcjxx;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.dwywmx = dwywmx;
	}

	public String getYjny() {
		return yjny;
	}

	public void setYjny(String yjny) {
		this.yjny = yjny;
	}

	public Set<CCollectionUnitPayCallDetailVice> getCjxq() {
		return cjxq;
	}

	public void setCjxq(Set<CCollectionUnitPayCallDetailVice> cjxq) {
		this.cjxq = cjxq;
	}

	public CCollectionUnitPayCallVice() {
		super();
	}

	public CCollectionUnitPayCallVice(String id, Date created_at, Date updated_at, Date deleted_at,boolean deleted,
									  int cjcs, String zdcjxx,String yjny, StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.cjcs = cjcs;
		this.zdcjxx = zdcjxx;
		this.yjny = yjny;
		this.dwywmx = dwywmx;
	}

	public CCollectionUnitPayCallVice(String id, Date created_at, Date updated_at, Date deleted_at,boolean deleted,
									  int cjcs, String zdcjxx, String yjny, Set<CCollectionUnitPayCallDetailVice> cjxq,
									  StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.cjcs = cjcs;
		this.zdcjxx = zdcjxx;
		this.yjny = yjny;
		this.cjxq = cjxq;
		this.dwywmx = dwywmx;
	}
}
