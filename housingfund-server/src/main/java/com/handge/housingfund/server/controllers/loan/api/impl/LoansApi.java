package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ILoanService;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentPut;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.LoansResponse;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.loan.api.ILoansApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@SuppressWarnings({"serial", "SpringAutowiredFieldsWarningInspection"})
@Component
public class LoansApi implements ILoansApi<Response> {


    @Autowired
    private ILoanService loanService;

    /**
     * 查询进度
     *
     * @param DKZH 贷款账号
     **/
    public Response getConfirmResultLoans(TokenContext tokenContext, final String DKZH) {

        System.out.println("查询进度");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 放款列表
     *
     * @param DKRXM  贷款人姓名
     * @param DKZH   贷款账号
     * @param status 状态（0：所有 1：待放款 2：已发放 3：已到账）
     **/
    public Response getLoansList(TokenContext tokenContext,final String DKRXM, final String DKZH, final String status) {

        System.out.println("放款列表");

        return Response.status(200).entity(new LoansResponse() {{


        }}).build();
    }


    /**
     * 发放贷款信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response getConfirmLoans(TokenContext tokenContext,final String YWLSH) {

        System.out.println("发放贷款详情");


        try {

            return Response.status(200).entity(this.loanService.getConfirmLoans(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    /**
     * 确认放款
     *
     * @param YWLSH 业务流水号
     **/
    public Response putConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH, final ConfirmPaymentPut body) {

        System.out.println("确认放款");

        try {

            return Response.status(200).entity(this.loanService.putConfirmLoansSubmit(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    /**
     * 发放贷款详情
     *
     * @param YWLSH 业务流水号
     **/
    @Override
    public Response getConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH){

        System.out.println("确认放款");

        try {

            return Response.status(200).entity(this.loanService.getConfirmLoansSubmit(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }
}