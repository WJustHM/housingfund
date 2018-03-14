package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位催缴
 */
@Entity
@Table(name = "c_collection_unit_paycall_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_paycall_detail_vice", comment = "单位催缴详情")
public class CCollectionUnitPayCallDetailVice extends Common implements Serializable {

	private static final long serialVersionUID = 4447691281991000867L;
	@Column(name = "CJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '催缴时间'")
	private Date cjsj;
	@Column(name = "CJR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '催缴人'")
	private String cjr;
	@Column(name = "CJFS", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '催缴方式'")
	private String cjfs;
	@Column(name = "ZDCJ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '自动催缴'")
	private String zdcj;

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.updated_at = new Date();
		this.cjsj = cjsj;
	}

	public String getCjr() {
		return cjr;
	}

	public void setCjr(String cjr) {
		this.updated_at = new Date();
		this.cjr = cjr;
	}

	public String getCjfs() {
		return cjfs;
	}

	public void setCjfs(String cjfs) {
		this.updated_at = new Date();
		this.cjfs = cjfs;
	}

	public String getZdcj() {
		return zdcj;
	}

	public void setZdcj(String zdcj) {
		this.updated_at = new Date();
		this.zdcj = zdcj;
	}

	public CCollectionUnitPayCallDetailVice() {
		super();

	}

	public CCollectionUnitPayCallDetailVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                            Date cjsj, String cjr, String cjfs, String zdcj) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.cjsj = cjsj;
		this.cjr = cjr;
		this.cjfs = cjfs;
		this.zdcj = zdcj;
	}
}
