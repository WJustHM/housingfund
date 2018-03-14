package com.handge.housingfund.server.controllers.collection.allochthonous;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferIntoApi;
import com.handge.housingfund.server.controllers.ResUtils;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;



@Controller
@Path("/TransferInto")
@SuppressWarnings({"SpringAutowiredFieldsWarningInspection", "JavaDoc", "SpellCheckingInspection"})
public class TransferIntoResource{

  @Autowired
  private TransferIntoApi<Response> TransferIntoApi;

  /**
   * 
   * 办理转移-转移接续转入
   * @param LXHBH 联系函编号
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZhuangTai 状态 （0：账户信息审核通过 1：账户信息审核不通过 2：协商中 3：系统处理中 4：正常办结 5：账户信息已录）
   * @param ZCGJJZXMC 转出公积金中心名称
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  @Path("/HandleTransfer")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response handleTransferList(@Context HttpRequest httpRequest,final  @QueryParam("LXHBH")String LXHBH, final  @QueryParam("ZGXM")String ZGXM, final  @QueryParam("ZJHM")String ZJHM, final  @QueryParam("ZhuangTai")String ZhuangTai, final  @QueryParam("ZCGJJZXMC")String ZCGJJZXMC, final  @QueryParam("KSSJ")String KSSJ, final  @QueryParam("JSSJ")String JSSJ, final  @QueryParam("pageNo")String pageNo, final  @QueryParam("pageSize")String pageSize){

    System.out.println("办理转移-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.handleTransferList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),LXHBH, ZGXM, ZJHM, ZhuangTai, ZCGJJZXMC, KSSJ, JSSJ, pageNo, pageSize));
  }

  /**
   * 
   * 业务流程详情-转移接续转入
   * @param YWLSH 业务流水号
   */
  @Path("/getBusinessDetails/{YWLSH}")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response transferIntoBusinessDetails(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

    System.out.println("业务流程详情-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoBusinessDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
  }

  /**
   * 
   * 联系函详情-转移接续转入
   * @param YWLSH 业务流水号
   */
  @Path("/getDetails/{YWLSH}")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response transferIntoDetails(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

    System.out.println("联系函详情-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
  }

  /**
   * 
   * 办结-转移接续转入
   * @param YWLSH 业务流水号
   * @param body 
   */
  @Path("/Finish/{YWLSH}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferIntoFinish(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final  RequestWrapper<TransferIntoFinishModle> body){

    System.out.println("办结-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoFinish((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 申请受理-转移接续转入
   * @param LXHBH 联系函编号
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZCGJJZXMC 转出公积金中心名称
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  @Path("")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response transferIntoList(@Context HttpRequest httpRequest,final  @QueryParam("LXHBH")String LXHBH, final  @QueryParam("ZGXM")String ZGXM, final  @QueryParam("ZJHM")String ZJHM, final  @QueryParam("ZCGJJZXMC")String ZCGJJZXMC,final @QueryParam("ZhuangTai")String ZhuangTai, final  @QueryParam("KSSJ")String KSSJ, final  @QueryParam("JSSJ")String JSSJ, final  @QueryParam("pageNo")String pageNo, final  @QueryParam("pageSize")String pageSize){

    System.out.println("申请受理-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),LXHBH, ZGXM, ZJHM, ZCGJJZXMC,ZhuangTai, KSSJ, JSSJ, pageNo, pageSize));
  }

  /**
   * 
   * 协商-转移接续转入
   * @param YWLSH 业务流水号
   * @param body 
   */
  @Path("/Negotiation/{YWLSH}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferIntoNegotiation(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final  RequestWrapper<TransferIntoNegotiationModle> body){

    System.out.println("协商-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoNegotiation((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 录入联系函-转移接续转入
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  @Path("/{type}")
  @POST
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferIntoPost(@Context HttpRequest httpRequest,final @PathParam("type") String type, final  RequestWrapper<TransferIntoPostModle> body){

    System.out.println("录入联系函-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoPost((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),type, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 申请修改-转移接续转入
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  @Path("/{type}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferIntoPut(@Context HttpRequest httpRequest,final @PathParam("type") String type, final  RequestWrapper<TransferIntoPutModle> body){

    System.out.println("申请修改-转移接续转入");

    return ResUtils.wrapEntityIfNeeded(this.TransferIntoApi.transferIntoPut((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),type, body == null ? null : body.getReq()));
  }

}
