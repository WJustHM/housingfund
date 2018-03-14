package com.handge.housingfund.common.service.others.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class File implements Serializable {

	private static final long serialVersionUID = 6104193219361195598L;
	private String id;
	private Date created_at;
	private Date updated_at;
	private Date deleted_at;
	private boolean deleted;
	private String name;
	private FileType type;
	private String SHA1;
	private BigDecimal size;
	private String path;
	private BigDecimal count;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Date getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(Date deleted_at) {
		this.deleted_at = deleted_at;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public String getSHA1() {
		return SHA1;
	}

	public void setSHA1(String sHA1) {
		SHA1 = sHA1;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}


	public File(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String name,
			FileType type, String sHA1, BigDecimal size, String path, BigDecimal count) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.name = name;
		this.type = type;
		SHA1 = sHA1;
		this.size = size;
		this.path = path;
		this.count = count;
	}

	public File() {

	}

	@Override
	public String toString() {
		return "File{" + "id='" + id + '\'' + ", created_at=" + created_at + ", updated_at=" + updated_at
				+ ", deleted_at=" + deleted_at + ", deleted=" + deleted + ", name='" + name + '\'' + ", type=" + type
				+ ", SHA1='" + SHA1 + '\'' + ", size=" + size + ", path='" + path + '\'' + ", count=" + count + '}';
	}
}
