package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.others.model.UploadFile;
import com.handge.housingfund.common.service.others.model.UploadFilewithID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyi on 2017/8/17.
 */
public interface IUploadImagesService {

    /**
     * 获取资料设置
     *
     * @param modle 模块
     * @param business
     * @return
     */
    List<UploadFilewithID> getUploadFile(String modle, String business);

    /**
     * 验证传入资料
     *
     * @param modle
     * @param business
     * @param fileJson
     * @return
     */
    boolean validateUploadFile(String modle, String business, String fileJson);

    /**
     * 添加资料设置
     *
     * @param uploadFile
     * @return
     */
    String addFileSet(UploadFile uploadFile);

    /**
     * 修改资料设置
     *
     * @param id
     * @param uploadFile
     * @return
     */
    String putFileSet(String id, UploadFile uploadFile);

    /**
     * 删除资料设置
     *
     * @param id
     * @return
     */
    String deleteFileSet(String id);


    /**
     * 批量删除资料设置
     *
     * @param ids
     * @return
     */
    void deleteFilesSet(ArrayList<String> ids);
}
