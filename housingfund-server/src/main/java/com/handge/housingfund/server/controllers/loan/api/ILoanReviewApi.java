package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;

public interface ILoanReviewApi<T> {

    /**
     * 贷款审核详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getLoanReviewDetails(TokenContext tokenContext,final String YWLSH);



}