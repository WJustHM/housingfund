package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentPut;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoansApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，4
 */
@Path("/loan/loans")
@Controller
public class LoansResource {

    @Autowired
    private ILoansApi<Response> service;

    /**
     * 查询进度
     *
     * @param DKZH 贷款账号
     **/
    @Path("/status/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getConfirmResultLoans(@Context HttpRequest httpRequest, final @PathParam("DKZH") String DKZH) {

        System.out.println("查询进度");

        return ResUtils.wrapEntityIfNeeded(this.service.getConfirmResultLoans((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DKZH));
    }


    /**
     * 放款列表
     *
     * @param DKRXM  贷款人姓名
     * @param DKZH   贷款账号
     * @param status 状态（0：所有 1：待放款 2：已发放 3：已到账）
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getLoansList(@Context HttpRequest httpRequest,final @QueryParam("DKRXM") String DKRXM, final @QueryParam("DKZH") String DKZH, final @QueryParam("status") String status) {

        System.out.println("放款列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoansList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DKRXM, DKZH, status));
    }


    /**
     * 发放贷款信息
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getConfirmLoans(@Context HttpRequest httpRequest, final @PathParam("YWLSH") String YWLSH) {

        System.out.println("发放贷款信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getConfirmLoans((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 确认放款
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putConfirmLoansSubmit(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<ConfirmPaymentPut> confirmpaymentput) {

        System.out.println("确认放款");

        return ResUtils.wrapEntityIfNeeded(this.service.putConfirmLoansSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, confirmpaymentput == null ? null : confirmpaymentput.getReq()));
    }


    /**
     * 发放贷款详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/loans/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getConfirmLoansSubmit(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("发放贷款详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getConfirmLoansSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }
}