package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.others.IFileService;
import com.handge.housingfund.common.service.others.model.File;
import com.handge.housingfund.database.dao.ICFileDAO;
import com.handge.housingfund.database.entities.CFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FileServiceImpl implements IFileService {

	@Autowired
	ICFileDAO fileDAO;

	public String saveFile(File file) {
		CFile cFile = this.converToCFile(file);
		String id = fileDAO.save(cFile);
		return id;
	}

	public void updateFile(File file) {
		CFile cFile = this.fileDAO.get(file.getId());
		cFile.setCount(file.getCount());
		cFile.setSHA1(file.getSHA1());
		cFile.setSize(file.getSize());
		cFile.setPath(file.getPath());
		this.fileDAO.update(cFile);
	}

	public void deleteFile(String id) {
		CFile cfile = fileDAO.get(id);
		if (cfile != null) {
			fileDAO.delete(cfile);
		}
	}

	private File converToFile(CFile cFile) {
		if (cFile == null) {
			return null;
		}
		File file = new File(cFile.getId(), cFile.getCreated_at(), cFile.getUpdated_at(), cFile.getDeleted_at(),
				cFile.isDeleted(), cFile.getName(), cFile.getType(), cFile.getSHA1(), cFile.getSize(), cFile.getPath(),
				cFile.getCount());
		return file;
	}

	private CFile converToCFile(File file) {
		if (file == null) {
			return null;
		}
		CFile cFile = new CFile(file.getId(), file.getCreated_at(), file.getUpdated_at(), file.getDeleted_at(),
				file.isDeleted(), file.getName(), file.getType(), file.getSHA1(), file.getSize(), file.getPath(),
				file.getCount());
		return cFile;
	}

	@Override
	public File getFileMetadata(String id) {
		CFile cFile = fileDAO.get(id);
		if (cFile != null) {
			cFile.setCount(cFile.getCount().add(new BigDecimal(1)));
			this.fileDAO.update(cFile);
			return this.converToFile(cFile);
		} else {
			return null;
		}
	}

}
