package com.handge.housingfund.database.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class Common {
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	protected String id;
	@Column(name = "created_at")
	protected Date created_at;
	@Column(name = "updated_at")
	protected Date updated_at;
	@Column(name = "deleted_at")
	protected Date deleted_at;
	@Column(name = "deleted")
	protected boolean deleted;

	public Common(){

		this.created_at = new Date();
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
       this.updated_at = new Date();
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
       this.updated_at = new Date();
		this.updated_at = updated_at;
	}

	public Date getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(Date deleted_at) {
       this.updated_at = new Date();
		this.deleted_at = deleted_at;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
       this.updated_at = new Date();
		this.deleted = deleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
       this.updated_at = new Date();
		this.id = id;
	}
}
