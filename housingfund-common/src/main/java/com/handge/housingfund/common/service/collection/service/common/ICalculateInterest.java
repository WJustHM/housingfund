package com.handge.housingfund.common.service.collection.service.common;

import com.handge.housingfund.common.service.collection.model.BalanceInterestFinalRes;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.math.BigDecimal;
import java.util.Date;

public interface ICalculateInterest {

    /**
     * 计算利息
     *
     * @param grzh 个人账号
     *
     * @param start 开始时间
     *
     * @param end 结束时间
     **/
    public CommonResponses calculateInterestByGrzh(String grzh, Date start, Date end);

    /**
     * 年终结息
     *
     * @param grzh 个人账号
     *
     **/
    public BalanceInterestFinalRes balanceInterestFinal(String grzh);


    /**
     * 结息
     *
     * @param grzh 个人账号
     *
     * @param interest 利息额
     *
     **/
    @Deprecated
    public CommonResponses balanceInterest(String grzh, BigDecimal interest);
}
