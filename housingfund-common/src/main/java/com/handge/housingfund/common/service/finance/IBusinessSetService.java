package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.BusinessSet;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
public interface IBusinessSetService {

    /**
     * 获取业务类型列表
     *
     * @param name
     * @return
     */
    PageRes<BusinessSet> getBusinessList(String name, int pageNo, int pageSize);

    /**
     * 删除业务类型
     *
     * @param id
     * @return
     */
    boolean delBusiness(String id);

    /**
     * 批量删除业务类型
     *
     * @param delList
     * @return
     */
    boolean delBusinesses(ArrayList<String> delList);
}
