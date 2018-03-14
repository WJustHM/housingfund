package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
public interface IBusinessClassifySetAPI {
    /**
     * 获取日常业务分类集合
     * @return
     */
    public Response getBusinessClassifys();

    /**
     * 添加日常业务分类
     * @param businessClassifySets
     * @return
     */
    public Response updateBusinessClassify(ArrayList<BusinessClassifySet> businessClassifySets);

//    /**
//     * 删除日常业务分类
//     * @param delList
//     * @return
//     */
//    public Response delBusinessClassify(ArrayList<String> delList);

    /**
     * 添加日常业务
     * @param businessClassifySet
     * @return
     */
    public Response updateBusiness(BusinessClassifySet businessClassifySet);

//    /**
//     * 删除日常业务
//     * @param delList
//     * @return
//     */
//    public Response delBusiness(ArrayList<String> delList);
}
