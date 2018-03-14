package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ILoanReviewService;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanReviewApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class LoanReviewApi implements ILoanReviewApi<Response> {

    @Autowired
    private ILoanReviewService loanReviewService;

    /**
     * 贷款审核详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getLoanReviewDetails(TokenContext tokenContext,final String YWLSH) {


        System.out.println("贷款审核详情");

        if (YWLSH == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(this.loanReviewService.getLoanReviewDetails(tokenContext,YWLSH)).build();
    }

}