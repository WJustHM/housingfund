package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ISignContractListApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，1
 */
@Path("/loan/signContractList")
@Controller
public class SignContractListResource {

    @Autowired
    private ISignContractListApi<Response> service;

    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/

    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getSignContractList(@Context HttpRequest httpRequest,@QueryParam("ZhuangTai")String YWZT,@QueryParam("JinE")String JE, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("DKYT") String DKYT,final @QueryParam("FFDKYH") String YHMC,final @QueryParam("YWWD") String YWWD, @QueryParam("pageSize") String pagesize, @QueryParam("pageNo") String page,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("签订合同列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getSignContractList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWZT,JE,JKRXM, JKRZJHM, DKYT,YHMC,YWWD,pagesize,page,KSSJ,JSSJ));
    }


    @Path("/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getSignContractList(@Context HttpRequest httpRequest, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("JKRZJHM") String JKRZJHM, final @QueryParam("DKYT") String DKYT,final @QueryParam("FFDKYH") String YHMC,final @QueryParam("YWWD") String YWWD, @QueryParam("pageSize") String pagesize, final @QueryParam("marker")String marker,final @QueryParam("action") String action,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("签订合同列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getSignContractList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),JKRXM, JKRZJHM, DKYT,YHMC,YWWD,pagesize,marker,action,KSSJ,JSSJ));
    }

    /**
     * 自动获取合同信息
     *
     * @param YWLSH 业务流水号
     **/

    @Path("/signContractAuto/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getSignContractList(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){
        System.out.println("签订合同列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getContractAuto((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 打印 合同pdf
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/printContractPDF")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response printContractPdf(@Context HttpRequest httpRequest,final @QueryParam("YWLSH") String YWLSH){

        System.out.println("打印 合同pdf");

        return ResUtils.wrapEntityIfNeeded(this.service.printContractPdf((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }
}