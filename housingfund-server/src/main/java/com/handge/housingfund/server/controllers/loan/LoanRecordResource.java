package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanRecordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * 1 级功能，2
 */
@Path("/loan/loanRecord")
@Controller
public class LoanRecordResource {

    @Autowired
    private ILoanRecordApi<Response> service;

    /**
     * 贷款详情页面
     *
     * @param DKZH 贷款账号
     **/
    @Path("/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoanRecordDetails(final @PathParam("DKZH") String DKZH) {

        System.out.println("贷款详情页面");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanRecordDetails(DKZH));
    }


    /**
     * 贷款记录列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    @GET
    @Path("")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoanRecordList(final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("DKYT") String DKYT,
                                      final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo, final @QueryParam("Module") String Module,
                                      final @QueryParam("YWWD") String YWWD, final @QueryParam("SWTYH") String SWTYH,
                                      final @QueryParam("DKZH") String DKZH, final @QueryParam("HTJE") String HTJE) {

        System.out.println("贷款记录列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanRecordList(JKRXM, JKRZJHM, DKYT, pageSize, pageNo, Module, YWWD, SWTYH, DKZH, HTJE));
    }

    /**
     * 查询，贷款业务历史记录（审核流程）
     *
     * @param DKZH
     * @param pageSize
     * @param pageNo
     * @return
     */
    @Path("/history/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecordHistory(final @PathParam("DKZH") String DKZH,
                                     final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo) {

        System.out.println("查询，贷款业务历史记录（审核流程）");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanRecordHistory(DKZH, pageSize, pageNo));
    }

}