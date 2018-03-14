package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.others.IFileService;
import com.handge.housingfund.common.service.others.model.File;
import com.handge.housingfund.common.service.others.model.FileType;
import com.handge.housingfund.common.service.util.FileUtil;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.UploadImagesUtil;
import org.apache.commons.configuration2.Configuration;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("restriction")
@Controller
@Path(value = "/file")
public class FileController {

    @Autowired
    IFileService fileService;

	static Configuration config = Configure.getInstance().getConfiguration("pdf");
	String filePath = config.getString("file_path");

    /**
     * 通过表单上传文件
     *
     * @param form 文件表单
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
	@POST
	@Path("")
	@Consumes("multipart/form-data")
	@Produces("application/json; charset=utf-8")
	public Response uploadFile(@MultipartForm FileUploadForm form,@Context HttpRequest httpRequest) throws IOException, NoSuchAlgorithmException {
		TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
		String fileName = URLDecoder.decode((form.getFileName() == null ? "Unknown" : form.getFileName()), "UTF-8");
		com.handge.housingfund.common.service.others.model.File file = new com.handge.housingfund.common.service.others.model.File();
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		file.setName(name);
		if (extension.equals("jpg")) {
			file.setType(FileType.JPG);
		} else if (extension.equals("gif")) {
			file.setType(FileType.GIF);
		} else if (extension.equals("png")) {
			file.setType(FileType.PNG);
		} else if (extension.equals("pdf")) {
			file.setType(FileType.PDF);
		} else if (extension.equals("doc")) {
			file.setType(FileType.DOC);
		} else if (extension.equals("docs")) {
			file.setType(FileType.DOCS);
		} else if (extension.equals("xlsx")) {
			file.setType(FileType.XLSX);
		} else if (extension.equals("xls")) {
			file.setType(FileType.XLS);
		} else {
			return Response.status(500).entity("Not support this file type").build();
		}
		file.setCount(new BigDecimal(0));
		file.setSize(new BigDecimal(0));
		String id = fileService.saveFile(file);
		String savePath = filePath + config.getString("image") + "/" + id;
		this.writeFile(form.getFileData(), savePath);
//		if (extension.equals("jpg")||extension.equals("png"))
//		UploadImagesUtil.addWaterMark(savePath,savePath,"仅限毕节市住房公积金管理中心使用 上传者:"+tokenContext.getUserInfo().getCZY(), new Color(255,255,255,32));
		String SHA1 = FileUtil.getCheckCode(savePath, "SHA-1");
		BigDecimal size = FileUtil.getFileSize(savePath);
		file.setId(id);
		file.setPath(config.getString("image"));
		file.setSHA1(SHA1);
		file.setSize(size);
		fileService.updateFile(file);
		return Response.status(200).entity(file).build();
	}

    /**
     * 通过文本base64上传文件
     *
     * @param text base64
     * @return
     */
	@POST
	@Path("")
	@Consumes(MediaType.TEXT_PLAIN + ";charset=utf-8")
	@Produces("application/json; charset=utf-8")
	public Response uploadBase64(String text,@Context HttpRequest httpRequest) {
		TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
		String[] textSplits = text.split(",");
		if (textSplits.length != 2) {
			return Response.status(500).entity("Error body").build();
		}
		String filename = textSplits[0];
		com.handge.housingfund.common.service.others.model.File file = new com.handge.housingfund.common.service.others.model.File();
		try {
			String name = filename.substring(0, filename.lastIndexOf("."));
			String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			file.setName(name);
			if (extension.equals("jpg")) {
				file.setType(FileType.JPG);
			} else if (extension.equals("gif")) {
				file.setType(FileType.GIF);
			} else if (extension.equals("png")) {
				file.setType(FileType.PNG);
			} else if (extension.equals("pdf")) {
				file.setType(FileType.PDF);
			} else if (extension.equals("doc")) {
				file.setType(FileType.DOC);
			} else if (extension.equals("docs")) {
				file.setType(FileType.DOCS);
			} else if (extension.equals("xlsx")) {
				file.setType(FileType.XLSX);
			} else if (extension.equals("xls")) {
				file.setType(FileType.XLS);
			} else {
				return Response.status(500).entity("Not support this file type").build();
			}
			byte[] buffer = new BASE64Decoder().decodeBuffer(textSplits[1]);
			file.setCount(new BigDecimal(0));
			file.setSize(new BigDecimal(0));
			String id = fileService.saveFile(file);
			String savePath = filePath + config.getString("image") + "/" + id;
			this.writeFile(buffer, savePath);
//			if (extension.equals("jpg")||extension.equals("png"))
//			UploadImagesUtil.addWaterMark(savePath,savePath,"仅限毕节市住房公积金管理中心使用 上传者:"+tokenContext.getUserInfo().getCZY(), new Color(255,255,255,32));
			String SHA1 = FileUtil.getCheckCode(savePath, "SHA-1");
			BigDecimal size = FileUtil.getFileSize(savePath);
			file.setId(id);
			file.setPath(config.getString("image"));
			file.setSHA1(SHA1);
			file.setSize(size);
			fileService.updateFile(file);
			return Response.status(200).entity(file).build();
		} catch (Exception e) {
			return Response.status(500).entity("Error body").build();
		}
	}

    /**
     * 获取文件Base64
     *
     * @param id 文件ID
     * @return
     * @throws IOException
     */
	@GET
	@Path("/{id}/base64")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public Response getBase64(@PathParam("id") String id) {
		com.handge.housingfund.common.service.others.model.File file = fileService.getFileMetadata(id);
		if (file == null) {
			return Response.status(200).entity("File not exists").build();
		}
		String filename = file.getName() + "." + file.getType().toString().toLowerCase();
		StringBuffer base64Str = new StringBuffer();
		switch (file.getType().toString()) {
		case "JPG":
			base64Str.append("data:image/jpg;base64;");
			break;
		case "PDF":
			base64Str.append("data:application/pdf;base64;");
			break;
		case "PNG":
			base64Str.append("data:image/png;base64;");
			break;
		default:
			break;
		}
		try{
		String Path = config.getString("file_path") + file.getPath() + "/" + file.getId();
		java.io.File localFile = new java.io.File(Path);
		FileInputStream inputFile = new FileInputStream(localFile);
		byte[] buffer = new byte[(int) localFile.length()];
		inputFile.read(buffer);
		inputFile.close();
		base64Str.append(filename + "," + new BASE64Encoder().encode(buffer));
		} catch (IOException e){
			return Response.status(200).entity("File not exists").build();
		}
		return Response.status(200).entity(base64Str.toString()).build();
	}

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return
     * @throws UnsupportedEncodingException
     */
	@GET
	@Path("/{id}")
	@Produces("application/json; charset=utf-8")
	public Response downloadFile(@PathParam("id") String id) throws UnsupportedEncodingException {
		com.handge.housingfund.common.service.others.model.File file = fileService.getFileMetadata(id);
		if (file == null) {
			return Response.status(200).entity(new Error(){{this.setCode(ReturnEnumeration.User_Defined.getCode());this.setMsg(ReturnEnumeration.User_Defined.getMessage()+"文件不存在");}}).build();
		}
		ResponseBuilder responseBuilder = Response.status(200);
		String filename = file.getName() + "." + file.getType().toString().toLowerCase();
		responseBuilder.header("Content-Disposition", "filename=" + URLEncoder.encode(filename, "UTF-8"));
		switch (file.getType().toString()) {
		case "JPG":
			responseBuilder.type("image/jpg");
			break;
		case "PNG":
			responseBuilder.type("image/png");
			break;
		case "PDF":
			responseBuilder.type("application/pdf");
			break;
		case "XLSX":
			responseBuilder.type("application/ms-excel");
			break;
		default:
			break;
		}
		byte[] buffer =null;
		try{
			String Path = config.getString("file_path") + file.getPath() + "/" + file.getId();
			java.io.File localFile = new java.io.File(Path);
			FileInputStream inputFile = new FileInputStream(localFile);
			buffer = new byte[(int) localFile.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (IOException e){
			return Response.status(200).entity("File not exists").build();
		}
		return responseBuilder.entity(buffer).build();
	}

    /**
     * 获取文件元数据
     *
     * @param id 文件ID
     * @return
     * @throws UnsupportedEncodingException
     */
	@HEAD
	@Path("/{id}")
	public Response getFileMetadata(@PathParam("id") String id) throws UnsupportedEncodingException {
		com.handge.housingfund.common.service.others.model.File file = fileService.getFileMetadata(id);
		if (file == null) {
			return Response.status(200).entity("File not exists").build();
		}
		ResponseBuilder builder =Response.status(200); 
		String filename = file.getName() + "." + file.getType().toString().toLowerCase();
		builder.header("filename", URLEncoder.encode(filename, "UTF-8"));
		builder.header("size", file.getSize());
		builder.header("SHA-1", file.getSHA1());
		builder.header("id", file.getId());
		return builder.build();
	}

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return
     */
	@DELETE
	@Path("/{id}")
	public Response deleteFile(@PathParam("id") String id) {
		File cfile= fileService.getFileMetadata(id);
		if (cfile != null) {
			String path = filePath + cfile.getPath() + "/" + cfile.getId();
			java.io.File file = new java.io.File(path);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			fileService.deleteFile(id);
		}
		return Response.status(200).build();
	}

	private void writeFile(byte[] content, String filename) throws IOException {
		java.io.File file = new java.io.File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}
}
