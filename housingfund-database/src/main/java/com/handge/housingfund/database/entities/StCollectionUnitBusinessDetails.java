package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_collection_unit_business_details", indexes = {
		@Index(name = "INDEX_DWYWLSH", columnList = "YWLSH", unique = true) })
@org.hibernate.annotations.Table(appliesTo = "st_collection_unit_business_details", comment = "单位业务明细信息 表5.0.3")
public class StCollectionUnitBusinessDetails extends Common implements java.io.Serializable,Comparable<StCollectionUnitBusinessDetails> {

	private static final long serialVersionUID = 6953749357666849266L;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;
	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;
	@Column(name = "FSLXE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生利息额'")
	private BigDecimal fslxe = BigDecimal.ZERO;
	@Column(name = "FSRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT'发生人数'")
	private BigDecimal fsrs = BigDecimal.ZERO;
	@Column(name = "YWMXLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '业务明细类型'")
	private String ywmxlx;
	@Column(name = "HBJNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '汇补缴年月'")
	private String hbjny;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "CZBZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '冲账标识'")
	private String czbz;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "Unit", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位信息'")
	private StCommonUnit unit;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '缴存单位业务明细扩展'")
	private CCollectionUnitBusinessDetailsExtension cCollectionUnitBusinessDetailsExtension;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitInformationActionVice unitInformationActionVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitInformationBasicVice unitInformationBasicVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitDepositInventoryVice unitDepositInventoryVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitRemittanceVice unitRemittanceVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitPaybackVice unitPaybackVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitPayholdVice unitPayholdVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitPayWrongVice unitPayWrongVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitPayCallVice unitPayCallVices;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionPersonRadixVice personRadixVice;
	@OneToOne(mappedBy = "dwywmx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CCollectionUnitDepositRatioVice unitDepositRatioVice;

	public StCollectionUnitBusinessDetails() {
		super();

	}

	public StCollectionUnitBusinessDetails(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String dwzh, Date jzrq, BigDecimal fse, BigDecimal fslxe, BigDecimal fsrs, String ywmxlx,
			String hbjny, String ywlsh, String czbz, CCollectionUnitBusinessDetailsExtension extension,
			CCollectionUnitInformationBasicVice unitInformationBasicVice,
			CCollectionUnitInformationActionVice unitInformationActionVice,
			CCollectionUnitDepositInventoryVice unitDepositInventoryVice,
			CCollectionUnitRemittanceVice unitRemittanceVice, CCollectionUnitPaybackVice unitPaybackVice,
			CCollectionUnitPayholdVice unitPayholdVice, CCollectionUnitPayWrongVice unitPayWrongVice,
			CCollectionUnitPayCallVice unitPayCallVices, CCollectionPersonRadixVice personRadixVice,
			CCollectionUnitDepositRatioVice unitDepositRatioVice, StCommonUnit unit) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dwzh = dwzh;
		this.jzrq = jzrq;
		this.fse = fse;
		this.fslxe = fslxe;
		this.fsrs = fsrs;
		this.ywmxlx = ywmxlx;
		this.hbjny = hbjny;
		this.ywlsh = ywlsh;
		this.czbz = czbz;
		this.cCollectionUnitBusinessDetailsExtension = extension;
		this.unitInformationBasicVice = unitInformationBasicVice;
		this.unitInformationActionVice = unitInformationActionVice;
		this.unitDepositInventoryVice = unitDepositInventoryVice;
		this.unitRemittanceVice = unitRemittanceVice;
		this.unitPaybackVice = unitPaybackVice;
		this.unitPayholdVice = unitPayholdVice;
		this.unitPayWrongVice = unitPayWrongVice;
		this.unitPayCallVices = unitPayCallVices;
		this.personRadixVice = personRadixVice;
		this.unitDepositRatioVice = unitDepositRatioVice;
		this.unit = unit;
	}

	public String getDwzh() {
		return this.dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public BigDecimal getFse() {
		return this.fse;
	}

	public void setFse(BigDecimal fse) {
		this.updated_at = new Date();
		this.fse = fse;
	}

	public BigDecimal getFslxe() {
		return this.fslxe;
	}

	public void setFslxe(BigDecimal fslxe) {
		this.updated_at = new Date();
		this.fslxe = fslxe;
	}

	public BigDecimal getFsrs() {
		return this.fsrs;
	}

	public void setFsrs(BigDecimal fsrs) {
		this.updated_at = new Date();
		this.fsrs = fsrs;
	}

	public String getYwmxlx() {
		return this.ywmxlx;
	}

	public void setYwmxlx(String ywmxlx) {
		this.updated_at = new Date();
		this.ywmxlx = ywmxlx;
	}

	public String getHbjny() {
		return this.hbjny;
	}

	public void setHbjny(String hbjny) {
		this.updated_at = new Date();
		this.hbjny = hbjny;
	}

	public String getYwlsh() {
		return this.ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getCzbz() {
		return this.czbz;
	}

	public void setCzbz(String czbz) {
		this.updated_at = new Date();
		this.czbz = czbz;
	}

	public CCollectionUnitBusinessDetailsExtension getExtension() {
		return cCollectionUnitBusinessDetailsExtension;
	}

	public void setExtension(CCollectionUnitBusinessDetailsExtension extension) {
		this.updated_at = new Date();
		this.cCollectionUnitBusinessDetailsExtension = extension;
	}

	public CCollectionUnitInformationActionVice getUnitInformationActionVice() {
		return unitInformationActionVice;
	}

	public void setUnitInformationActionVice(CCollectionUnitInformationActionVice unitInformationActionVice) {
		this.updated_at = new Date();
		this.unitInformationActionVice = unitInformationActionVice;
	}

	public CCollectionUnitInformationBasicVice getUnitInformationBasicVice() {
		return unitInformationBasicVice;
	}

	public void setUnitInformationBasicVice(CCollectionUnitInformationBasicVice unitInformationBasicVice) {
		this.updated_at = new Date();
		this.unitInformationBasicVice = unitInformationBasicVice;
	}

	public CCollectionUnitDepositInventoryVice getUnitDepositInventoryVice() {
		return unitDepositInventoryVice;
	}

	public void setUnitDepositInventoryVice(CCollectionUnitDepositInventoryVice unitDepositInventoryVice) {
		this.updated_at = new Date();
		this.unitDepositInventoryVice = unitDepositInventoryVice;
	}

	public CCollectionUnitRemittanceVice getUnitRemittanceVice() {
		return unitRemittanceVice;
	}

	public void setUnitRemittanceVice(CCollectionUnitRemittanceVice unitRemittanceVice) {
		this.updated_at = new Date();
		this.unitRemittanceVice = unitRemittanceVice;
	}

	public CCollectionUnitPaybackVice getUnitPaybackVice() {
		return unitPaybackVice;
	}

	public void setUnitPaybackVice(CCollectionUnitPaybackVice unitPaybackVice) {
		this.updated_at = new Date();
		this.unitPaybackVice = unitPaybackVice;
	}

	public CCollectionUnitPayholdVice getUnitPayholdVice() {
		return unitPayholdVice;
	}

	public void setUnitPayholdVice(CCollectionUnitPayholdVice unitPayholdVice) {
		this.updated_at = new Date();
		this.unitPayholdVice = unitPayholdVice;
	}

	public CCollectionUnitPayWrongVice getUnitPayWrongVice() {
		return unitPayWrongVice;
	}

	public void setUnitPayWrongVice(CCollectionUnitPayWrongVice unitPayWrongVice) {
		this.updated_at = new Date();
		this.unitPayWrongVice = unitPayWrongVice;
	}

	public CCollectionUnitPayCallVice getUnitPayCallVice() {
		return unitPayCallVices;
	}

	public void setUnitPayCallVice(CCollectionUnitPayCallVice unitPayCallVices) {
		this.updated_at = new Date();
		this.unitPayCallVices = unitPayCallVices;
	}

	public CCollectionPersonRadixVice getPersonRadixVice() {
		return personRadixVice;
	}

	public void setPersonRadixVice(CCollectionPersonRadixVice personRadixVice) {
		this.updated_at = new Date();
		this.personRadixVice = personRadixVice;
	}

	public CCollectionUnitDepositRatioVice getUnitDepositRatioVice() {
		return unitDepositRatioVice;
	}

	public void setUnitDepositRatioVice(CCollectionUnitDepositRatioVice unitDepositRatioVice) {
		this.updated_at = new Date();
		this.unitDepositRatioVice = unitDepositRatioVice;
	}

	public StCommonUnit getUnit() {
		return unit;
	}

	public void setUnit(StCommonUnit unit) {
		this.updated_at = new Date();
		this.unit = unit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		StCollectionUnitBusinessDetails that = (StCollectionUnitBusinessDetails) o;

		return dwzh != null ? dwzh.equals(that.dwzh) : that.dwzh == null;
	}

	@Override
	public int hashCode() {
		return dwzh != null ? dwzh.hashCode() : 0;
	}

	@Override
	public int compareTo(StCollectionUnitBusinessDetails o) {
		if(this.getHbjny() != null &&  o.getHbjny() != null){
			int hbjny = Integer.parseInt(this.getHbjny());
			int hbjny2 = Integer.parseInt(o.getHbjny());
			return hbjny2 - hbjny;
		}
		return 0;
	}
}
