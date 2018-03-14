package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/28.
 */
public interface IBusinessClassifySetService {
    /**
     * 获取日常业务分类集合
     * @return
     */
    public ArrayList<BusinessClassifySet> getBusinessClassifys();

    /**
     * 添加日常业务分类
     * @param businessClassifySets
     * @return
     */
    public boolean updateBusinessClassify(ArrayList<BusinessClassifySet> businessClassifySets);

    /**
     * 添加日常业务
     * @param businessClassifySet
     * @return
     */
    public boolean updateBusiness(BusinessClassifySet businessClassifySet);

    /**
     * 根据业务类型获取对应的资金业务类型
     * @param ywmcid 业务id
     * @return
     */
    public HashMap<String, String> getFundBusinessType(String ywmcid);
}
