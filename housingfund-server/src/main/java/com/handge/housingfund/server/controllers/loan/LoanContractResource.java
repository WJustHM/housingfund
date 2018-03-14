package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.AccountChangePost;
import com.handge.housingfund.common.service.loan.model.BorrowerChangePost;
import com.handge.housingfund.common.service.loan.model.GetBorrowerInformation;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanContractApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，4+1
 */
@Path("/loan/loanContract")
@Controller
public class LoanContractResource {

    @Autowired
    private ILoanContractApi<Response> service;

    /**
     * 合同信息列表(待定)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getLoanContractList(final @Context HttpRequest request, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("status") String status) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("合同信息列表(待定)");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanContractList(tokenContext, JKRXM, JKRZJHM, status));
    }



/*-----------------------------------------------------------------------------------------------------------*/

    /**
     * 合同变更repaymentApplyReceipt(合同变更申请列表)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    @Path("/ReviewList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getLoanContractReviewList(final @Context HttpRequest request,@QueryParam("YWWD")String YWWD, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("status") String status,
                                              final @QueryParam("JKHTBH") String JKHTBH
            , final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ
    , final @QueryParam("SWTYH") String YHDM,
                                              final @QueryParam("ZJHM")        String ZJHM) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("合同变更repaymentApplyReceipt");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanContractReviewList(tokenContext, YWWD, JKRXM, JKRZJHM, status,JKHTBH, pageSize, pageNo,KSSJ,JSSJ,YHDM,ZJHM));
    }


    /**
     * 合同变更repaymentApplyReceipt(合同变更申请列表)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    @Path("/ReviewList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getLoanContractReviewListnew(final @Context HttpRequest request, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("status") String status,
                                              final @QueryParam("JKHTBH") String JKHTBH
            , final @QueryParam("pageSize") String pageSize, final @QueryParam("marker") String marker,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ,final @QueryParam("action") String action
            , final @QueryParam("SWTYH") String YHDM) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("合同变更repaymentApplyReceipt");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanContractReviewListnew(tokenContext, JKRXM, JKRZJHM, status,JKHTBH, pageSize, marker,KSSJ,JSSJ,action,YHDM));
    }

    /**
     * 新增申请
     */
    @Path("/BorrowerInformationApplication/{action}")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response putBorrowerInformationApplication(final @Context HttpRequest request, final @PathParam("action") String action
            , RequestWrapper<GetBorrowerInformation> body) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新增申请");

        return ResUtils.wrapEntityIfNeeded(this.service.putBorrowerInformationApplication(tokenContext, action, body == null ? null : body.getReq()));
    }

    /**
     * 借款人信息(共同借款人)详情2
     */
    @Path("/BorrowerInformation/{JKHTBH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getBorrowerInformation(final @Context HttpRequest request, final @PathParam("JKHTBH") String JKHTBH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("借款人信息(共同借款人)详情2");

        return ResUtils.wrapEntityIfNeeded(this.service.getBorrowerInformation(tokenContext, JKHTBH));
    }

    /**
     * 借款人信息(共同借款人)变更
     */
    @Path("/BorrowerInformation/{action}/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putBorrowerInformation(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH,
                                           final @PathParam("action") String action, final RequestWrapper<GetBorrowerInformation> body) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("借款人信息(共同借款人)变更");

        return ResUtils.wrapEntityIfNeeded(this.service.putBorrowerInformation(tokenContext, action, YWLSH, body == null ? null : body.getReq()));
    }

    /**
     * 借款人信息(共同借款人)查询详情
     */
    @Path("/BorrowerInformationChange/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getBorrowerInformationChange(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("借款人信息(共同借款人)查询详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getBorrowerInformationChange(tokenContext, YWLSH));
    }

    /**
     * 借款人信息(共同借款人)查询详情
     */
    @Path("/headLoanContract/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headLoanContract(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("合同变更回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headLoanContract(tokenContext, YWLSH));
    }


    /*-----------------------------------------------------------------------------------------------------------*/

    /**
     * 划款账号或方式变更
     **/
    @Path("/accountCharge")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAccountCharge(final @Context HttpRequest request, final RequestWrapper<AccountChangePost> accountchangepost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("划款账号或方式变更");

        return ResUtils.wrapEntityIfNeeded(this.service.postAccountChange(tokenContext, accountchangepost == null ? null : accountchangepost.getReq()));
    }


    /**
     * 借款人变更
     **/
    @Path("/borrowerCharge")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBorrowerCharge(final @Context HttpRequest request, final RequestWrapper<BorrowerChangePost> borrowerchangepost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("借款人变更");

        return ResUtils.wrapEntityIfNeeded(this.service.postBorrowerChange(tokenContext, borrowerchangepost == null ? null : borrowerchangepost.getReq()));
    }

    /**
     * 打印委托扣划协议
     */
    @Path("/entrustDeduct/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoanEntrustDeductPdf(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);

        System.out.println("打印委托扣划协议");

        return ResUtils.wrapEntityIfNeeded(this.service.getLoanEntrustDeductPdf(tokenContext, YWLSH));
    }


}