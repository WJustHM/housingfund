package com.handge.housingfund.database.entities;

import com.handge.housingfund.common.service.others.model.FileType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/7/12.
 */
@Entity
@Table(name = "c_file")
@org.hibernate.annotations.Table(appliesTo = "c_file", comment = "文件")
public class CFile extends Common implements Serializable {

	private static final long serialVersionUID = -5498475272058340247L;

	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '文件名'")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '文件类型'")
	private FileType type;

	@Column(name = "SHA1", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT 'SHA1'")
	private String SHA1;

	@Column(name = "size", nullable = false, columnDefinition = "NUMERIC(20,0) DEFAULT 0 COMMENT '文件大小'")
	private BigDecimal size = BigDecimal.ZERO;

	@Column(name = "path", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '保存路径'")
	private String path;

	@Column(name = "count", nullable = false, columnDefinition = "NUMERIC(10,0) DEFAULT 0 COMMENT '获取次数'")
	private BigDecimal count = BigDecimal.ZERO;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.updated_at = new Date();
		this.name = name;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.updated_at = new Date();
		this.type = type;
	}

	public String getSHA1() {
		return SHA1;
	}

	public void setSHA1(String SHA1) {
		this.updated_at = new Date();
		this.SHA1 = SHA1;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.updated_at = new Date();
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.updated_at = new Date();
		this.path = path;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.updated_at = new Date();
		this.count = count;
	}

	public CFile(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String name,
			FileType type, String SHA1, BigDecimal size, String path, BigDecimal count) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.name = name;
		this.type = type;
		this.SHA1 = SHA1;
		this.size = size;
		this.path = path;
		this.count = count;
	}

	public CFile() {
		super();

	}
}
