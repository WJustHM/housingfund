package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanAccountApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/loan/account")
@Controller
public class LoanAccountResource {

    @Autowired
    private ILoanAccountApi<Response> service;

    /**
     * * 贷款账户查询
     *
     * @param DKZH     贷款账号
     * @param JKRXM    借款人姓名
     * @param status   状态（0:正常 1:逾期 2:呆账 3:转出 4:结清）
     * @param DKFXDJ   贷款风险等级
     * @param pageSize
     * @param page
     * @return
     */
    @Path("/search")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingFundAccount(final @QueryParam("DKZH") String DKZH, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("status") String status,
                                          final @QueryParam("DKFXDJ") String DKFXDJ, final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page,
                                          final @QueryParam("YWWD") String YWWD, final @QueryParam("SWTYH") String SWTYH, final @QueryParam("ZJHM") String ZJHM) {

        System.out.println("贷款账户查询");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingFundAccount(DKZH, JKRXM, status, DKFXDJ, pageSize, page, YWWD, SWTYH, ZJHM));
    }

    /**
     * 贷款账户业务清单
     *
     * @param DKZH 贷款账号
     **/
    @Path("/business/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingfundAccountBusinessList(final @PathParam("DKZH") String DKZH, final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page) {

        System.out.println("贷款账户业务清单");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingfundAccountBusinessList(DKZH, pageSize, page));
    }

    /**
     * 贷款账户逾期信息
     *
     * @param DKZH 贷款账号
     **/
    @Path("/Overdue/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingfundAccountOverdueList(final @PathParam("DKZH") String DKZH, final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page) {

        System.out.println("贷款账户逾期信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingfundAccountOverdueList(DKZH, pageSize, page));
    }

    /**
     * 提前还款申请书(待定)
     *
     * @param DKZH 贷款账号
     **/
    @Path("/repamentapply/overdue")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postOverdueApply(final @QueryParam("DKZH") String DKZH) {

        System.out.println("提前还款申请书(待定)");

        return ResUtils.wrapEntityIfNeeded(this.service.postOverdueApply(DKZH));
    }

    /**
     * 还款记录
     *
     * @param DKZH 贷款账号
     **/
    @Path("/record/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingRecordList(final @PathParam("DKZH") String DKZH, final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page) {

        System.out.println("还款记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingRecordList(DKZH, pageSize, page));
    }

    /**
     * 还款计划
     *
     * @param DKZH 贷款账号
     **/
    @Path("/Plan/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingfundPlanList(final @PathParam("DKZH") String DKZH) {

        System.out.println("还款计划");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingfundPlanList(DKZH));
    }


    /**
     * 贷款账户详情
     *
     * @param DKZH 贷款账户
     **/
    @Path("/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingDkzh(final @PathParam("DKZH") String DKZH) {

        System.out.println("贷款账户详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingDkzh(DKZH));
    }


    /**
     * 打印还款记录
     *
     * @param DKZH 贷款账号
     **/
    @Path("/record/print/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHousingRecordPintList(final @PathParam("DKZH") String DKZH) {

        System.out.println("打印还款记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingRecordPintList(DKZH));
    }


    /**
     * 单独修改状态（转为呆账）
     *
     * @param DKZH 贷款账号
     **/
    @Path("/badDebts/{DKZH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putbadDebts(final @PathParam("DKZH") String DKZH) {

        System.out.println("单独修改状态（转为呆账）");

        return ResUtils.wrapEntityIfNeeded(this.service.putbadDebts(DKZH));
    }


    /**
     * 根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号
     *
     * @param DKZH
     * @return
     */
    @Path("/borrowerInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoanAccountBorrowerInfo(final @QueryParam("DKZH") String DKZH) {

        System.out.println("根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanAccountBorrowerInfo(DKZH));
    }

    @Path("/riskAssessment/{DKZH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putLoanRiskAssessment(final @PathParam("DKZH") String DKZH, final @QueryParam("DKFXDJ") String DKFXDJ) {

        System.out.println("贷款风险评估修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putLoanRiskAssessment(DKZH, DKFXDJ));
    }
    /**
     * 贷款账号已结清的回执单
     *
     * @param DKZH 贷款账号
     *
     **/
    @Path("/SquareReceipt/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headSquareReceipt(final @PathParam("DKZH") String DKZH) {

        System.out.println("贷款账号已结清回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headSquareReceipt(DKZH));
    }
    /**
     * 打印还款计划pdf
     *
     * @param DKZH 贷款账号
     *
     **/
    @Path("/housingfundPlanPdf/{DKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response HeadHousingfundPlanPdf(final @Context HttpRequest request, final @PathParam("DKZH") String DKZH, final @QueryParam("HKRQS") String HKRQS, final @QueryParam("HKRQE") String HKRQE) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("打印还款计划pdf");

        return ResUtils.wrapEntityIfNeeded(this.service.HeadHousingfundPlan(tokenContext,DKZH,HKRQS,HKRQE));
    }
}