package com.handge.housingfund.server.controllers.others;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

public class FileUploadForm {
	public FileUploadForm() {
	}

	private byte[] fileData;//文件数据
	private String fileName;//文件名

	public String getFileName() {
		return fileName;
	}

	@FormParam("filename")
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	@FormParam("file")
	@PartType("application/octet-stream")
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}
