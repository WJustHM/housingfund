package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.server.controllers.loan.api.ILoanMonitorApi;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;


@Component
public class LoanMonitorApi implements ILoanMonitorApi<Response> {


    /**
     * 监控发放贷款、还款自动扣款、还款时是否扣款成功并进行后续操作
     *
     * @param DKZH 贷款账号
     **/
    public Response getloanMonitor(final String DKZH) {

        System.out.println("监控发放贷款、还款自动扣款、还款时是否扣款成功并进行后续操作");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


}