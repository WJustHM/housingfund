package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位错缴详情
 */
@Entity
@Table(name = "c_collection_unit_paywrong_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_paywrong_detail_vice", comment = "单位错缴详情")
public class CCollectionUnitPayWrongDetailVice extends Common implements Serializable {

	private static final long serialVersionUID = -2430929235623756964L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "DWCJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位错缴金额'")
	private BigDecimal dwcjje = BigDecimal.ZERO;
	@Column(name = "GRCJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人错缴金额'")
	private BigDecimal grcjje = BigDecimal.ZERO;
	@Column(name = "CJYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '错缴原因'")
	private String cjyy;

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public BigDecimal getDwcjje() {
		return dwcjje;
	}

	public void setDwcjje(BigDecimal dwcjje) {
		this.updated_at = new Date();
		this.dwcjje = dwcjje;
	}

	public BigDecimal getGrcjje() {
		return grcjje;
	}

	public void setGrcjje(BigDecimal grcjje) {
		this.updated_at = new Date();
		this.grcjje = grcjje;
	}

	public String getCjyy() {
		return cjyy;
	}

	public void setCjyy(String cjyy) {
		this.updated_at = new Date();
		this.cjyy = cjyy;
	}

	public CCollectionUnitPayWrongDetailVice() {
		super();

	}

	public CCollectionUnitPayWrongDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, BigDecimal dwcjje, BigDecimal grcjje, String cjyy) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.dwcjje = dwcjje;
		this.grcjje = grcjje;
		this.cjyy = cjyy;
	}
}
