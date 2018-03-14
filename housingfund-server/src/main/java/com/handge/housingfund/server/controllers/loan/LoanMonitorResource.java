package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanMonitorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * 待定
 */
@Path("/loan/loanMonitor")
@Controller
public class LoanMonitorResource {

    @Autowired
    private ILoanMonitorApi<Response> service;

    /**
     * 监控发放贷款、还款自动扣款、还款时是否扣款成功并进行后续操作
     *
     * @param DKZH 贷款账号
     **/
    @Path("/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getloanMonitor(final @PathParam("DKZH") String DKZH) {

        System.out.println("监控发放贷款、还款自动扣款、还款时是否扣款成功并进行后续操作");

        return ResUtils.wrapEntityIfNeeded(this.service.getloanMonitor(DKZH));
    }


}