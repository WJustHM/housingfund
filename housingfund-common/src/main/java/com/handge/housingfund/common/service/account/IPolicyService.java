package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.PolicyCommonRes;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 向超 on 2017/9/19.
 */
public interface IPolicyService {

    /**
     * @param id
     * @param xgz  修改值
     * @param sxrq 生效日期
     * @return
     */
    public CommonResponses updatePolicyInfo(String id, String xgz, String sxrq);


    public ArrayList<PolicyCommonRes> getPolicy();


    /**
     * 根据利率类型获取利率值
     * @param LLLX
     * @return
     */
    public BigDecimal getPolicyRate(String LLLX);

    /**
     * 根据id获取利率值
     * @param id
     * @return
     */
    public HashMap<String, BigDecimal> getPolicyRateById(String id);
    }
