package com.handge.housingfund.server.controllers.finance.api;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
public interface IBusinessSetAPI {

    /**
     * 获取业务类型列表
     * @param name
     * @return
     */
    public Response getBusinessList(String name, int pageNo, int pageSize);

    /**
     * 删除业务凭证
     * @param id
     * @return
     */
    public Response delBusiness(String id);

    /**
     * 批量删除业务类型
     * @param delList
     * @return
     */
    public Response delBusinesses(ArrayList<String> delList);
}
