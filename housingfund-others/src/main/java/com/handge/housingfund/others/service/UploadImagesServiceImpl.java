package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.model.UFile;
import com.handge.housingfund.common.service.others.model.UploadFile;
import com.handge.housingfund.common.service.others.model.UploadFilewithID;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.common.service.util.UploadImagesUtil;
import com.handge.housingfund.database.dao.ICUploadImagesDAO;
import com.handge.housingfund.database.entities.CUploadImages;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanyi on 2017/8/17.
 */
@SuppressWarnings({"Convert2Lambda", "SpringAutowiredFieldsWarningInspection"})
@Component
public class UploadImagesServiceImpl implements IUploadImagesService {

    @Autowired
    private ICUploadImagesDAO uploadImagesDAO;


    @Override
    public List<UploadFilewithID> getUploadFile(String modle, String business) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(modle)) {
            filter.put("modle", modle);
        }
        if (StringUtil.notEmpty(business)) {
            filter.put("business", business);
        }
        List<CUploadImages> cUploadImages = uploadImagesDAO.list(filter, null, null, "created_at", Order.ASC, null, SearchOption.REFINED);
        List<UploadFilewithID> uploadFiles = new ArrayList<>();
        for (CUploadImages cUploadImage : cUploadImages) {
            UploadFilewithID file = new UploadFilewithID();
            file.setBusiness(cUploadImage.getBusiness());
            file.setCertificateName(cUploadImage.getCertificateName());
            file.setId(cUploadImage.getId());
            file.setModle(cUploadImage.getModle());
            file.setRequired(cUploadImage.getRequired());
            uploadFiles.add(file);
        }
        return uploadFiles;
    }

    @Override
    public boolean validateUploadFile(String modle, String business, String fileJson) {
        System.out.println(fileJson);
        if (!StringUtil.notEmpty(modle)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "模块编码");
        }
        if (!StringUtil.notEmpty(business)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务编码");
        }
        List<UploadFilewithID> fileRes = getUploadFile(modle, business);
        List<UFile> jsonFiles = UploadImagesUtil.getFileByJson(fileJson);

        if (jsonFiles == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "上传资料格式有误");
        }
        for (int i = 0; i < fileRes.size(); i++) {
            UploadFilewithID file = fileRes.get(i);
            if (file.getRequired()) {
                int isfind = -1;
                for (int j = 0; j < jsonFiles.size(); j++) {
                    UFile uFile = jsonFiles.get(j);
                    if (uFile.getName().equals(file.getCertificateName())) {
                        isfind = j;
                    }
                }
                if (isfind != -1) {
                    if (jsonFiles.get(i).getData().length <= 0) {
                        return false;
                    }
                } else {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public String addFileSet(UploadFile uploadFile) {
        CUploadImages cUploadImages = new CUploadImages();
        cUploadImages.setBusiness(uploadFile.getBusiness());
        cUploadImages.setCertificateName(uploadFile.getCertificateName());
        cUploadImages.setModle(uploadFile.getModle());
        cUploadImages.setRequired(uploadFile.getRequired());
        String id = uploadImagesDAO.save(cUploadImages);
        return id;
    }

    @Override
    public String putFileSet(String id, UploadFile uploadFile) {
        CUploadImages cUploadImages = uploadImagesDAO.get(id);
        cUploadImages.setBusiness(uploadFile.getBusiness());
        cUploadImages.setCertificateName(uploadFile.getCertificateName());
        cUploadImages.setModle(uploadFile.getModle());
        cUploadImages.setRequired(uploadFile.getRequired());
        uploadImagesDAO.update(cUploadImages);
        return id;
    }

    @Override
    public String deleteFileSet(String id) {
        CUploadImages cUploadImages = uploadImagesDAO.get(id);
        uploadImagesDAO.delete(cUploadImages);
        return id;
    }

    @Override
    public void deleteFilesSet(ArrayList<String> ids) {

        DAOBuilder.instance(this.uploadImagesDAO).entities(DAOBuilder.instance(this.uploadImagesDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", ids);

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        })).delete(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }
}
