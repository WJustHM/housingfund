package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentPut;

import javax.ws.rs.core.Response;

public interface ILoansApi<T> {


    /**
     * 查询进度
     *
     * @param DKZH 贷款账号
     **/
    public T getConfirmResultLoans(TokenContext tokenContext, final String DKZH);


    /**
     * 放款列表
     *
     * @param DKRXM  贷款人姓名
     * @param DKZH   贷款账号
     * @param status 状态（0：所有 1：待放款 2：已发放 3：已到账）
     **/
    public T getLoansList(TokenContext tokenContext,final String DKRXM, final String DKZH, final String status);


    /**
     * 发放贷款详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getConfirmLoans(TokenContext tokenContext,final String YWLSH);


    /**
     * 确认放款
     *
     * @param YWLSH 业务流水号
     **/
    public T putConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH, final ConfirmPaymentPut confirmpaymentput);


    /**
     * 发放贷款详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH);
}