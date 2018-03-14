package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.others.model.File;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IFileService {

	public String saveFile(File file);

	public void updateFile(File File);

	public void deleteFile(String id);

	public File getFileMetadata(String id);


}
