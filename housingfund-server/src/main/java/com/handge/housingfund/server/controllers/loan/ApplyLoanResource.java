package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.ApplicantPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IApplyLoanApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * 1 级功能，6
 */
@Path("/loan/applyLoan")
@Controller
public class ApplyLoanResource {

    @Autowired
    private IApplyLoanApi<Response> service;

    /**
     * 修改页面
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putModifyApplication(@Context HttpRequest httpRequest, final @QueryParam("status") String status, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<ApplicantPost> applicantpost) {

        System.out.println("修改页面");

        return ResUtils.wrapEntityIfNeeded(this.service.putModifyApplication((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),status, YWLSH, applicantpost == null ? null : applicantpost.getReq()));
    }


    /**
     * 申请贷款详情页面
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApplyDetails(@Context HttpRequest httpRequest, final @PathParam("YWLSH") String YWLSH) {

        System.out.println("申请贷款详情页面");

        return ResUtils.wrapEntityIfNeeded(this.service.getApplyDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 获取审批表id
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/spwj/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response postApplyLoan(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){
        System.out.println("获取审批表id");

        return ResUtils.wrapEntityIfNeeded(this.service.getSPBId((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));

    }
    /**
     * 新增申请贷款
     *
     * @param status 0:保存 1:提交
     **/

    @Path("")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postApplyLoan(@Context HttpRequest httpRequest, final @QueryParam("status") String status, final RequestWrapper<ApplicantPost> applicantpost) {

        System.out.println("新增申请贷款");

        return ResUtils.wrapEntityIfNeeded(this.service.postApplyLoan((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),status, applicantpost == null ? null : applicantpost.getReq()));
    }


    /**
     * 贷款申请列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApplyLoanList(@Context HttpRequest httpRequest,@QueryParam("YWWD")String YWWD,final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("status") String status, @QueryParam("SWTYH") String SKYHDM,@QueryParam("pageSize") String pagesize,@QueryParam("pageNo") String page, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("贷款申请列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getApplyLoanList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWWD,JKRXM, JKRZJHM, status,SKYHDM,pagesize,page,KSSJ,JSSJ));
    }


    /**
     * 贷款申请列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    @Path("/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApplyLoanList(@Context HttpRequest httpRequest,@QueryParam("YWWD")String YWWD,final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("status") String status, @QueryParam("SKYHDM") String SKYHDM,@QueryParam("pageSize") String pagesize,final @QueryParam("marker")String marker,final @QueryParam("action") String action, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("贷款申请列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getApplyLoanList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWWD,JKRXM, JKRZJHM, status,SKYHDM,pagesize,marker,action,KSSJ,JSSJ));
    }
    /**
     * 根据个人账号获取贷款个人信息
     *
     * @param GRZH 个人账号
     **/
    @Path("/account")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonalAccountInfo(@Context HttpRequest httpRequest,@QueryParam("GRZH") String GRZH){

        System.out.println("根据个人账号获取贷款个人信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getPersonalAccountInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),GRZH));

    }

    /**
     * 单独修改状态（提交与撤回）
     *
     * @param YWLSH  贷款id
     * @param status 状态（0：提交 1：撤回 2:作废）
     **/
//    @Path("/{YWLSH}/state/{status}")
//    @PUT
//    @Produces("application/json; charset=utf-8")
//    public Response putApplyLoanStatus(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final @PathParam("status") String status) {
//
//        System.out.println("单独修改状态（提交与撤回）");
//
//        return ResUtils.wrapEntityIfNeeded(this.service.putApplyLoanStatus((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, status));
//    }


}