package com.handge.housingfund.server.controllers.collection.allochthonous;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferOutApi;
import com.handge.housingfund.server.controllers.ResUtils;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;



@Controller
@Path("/TransferOut")
@SuppressWarnings({"SpringAutowiredFieldsWarningInspection", "JavaDoc", "SpellCheckingInspection"})
public class TransferOutResource{

  @Autowired
  private TransferOutApi<Response> TransferOutApi;

  /**
   * 
   * 处理联系函-转移接续转出
   * @param LXHBH 联系函编号
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param zhuangTai 状态 （0：确认接收联系函 1：账户信息已录未审 2：账户信息审核通过 3：账户信息审核失败 4：转出驳回联系函 5：转账中 6：转账失败 7：转出地已转账 8：正常办结 9：驳回办结 10：失败办结 11：协商中 12：账户信息新建）
   * @param ZRGJJZXMC 转入公积金中心名称
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  @Path("/HandleContactLetter")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response handleContactLetterList(@Context HttpRequest httpRequest,final  @QueryParam("LXHBH")String LXHBH, final  @QueryParam("ZGXM")String ZGXM, final  @QueryParam("ZJHM")String ZJHM, final  @QueryParam("ZhuangTai")String zhuangTai, final  @QueryParam("ZRGJJZXMC")String ZRGJJZXMC, final  @QueryParam("KSSJ")String KSSJ, final  @QueryParam("JSSJ")String JSSJ, final  @QueryParam("pageNo")String pageNo, final  @QueryParam("pageSize")String pageSize){

    System.out.println("处理联系函-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.handleContactLetterList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),LXHBH, ZGXM, ZJHM, zhuangTai, ZRGJJZXMC, KSSJ, JSSJ, pageNo, pageSize));
  }

  /**
   * 
   * 接收联系函-转移接续转出
   * @param LXHBH 联系函编号
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  @Path("/ReceiveContactLetter")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response receiveContactLetterList(@Context HttpRequest httpRequest,final  @QueryParam("LXHBH")String LXHBH, final  @QueryParam("ZGXM")String ZGXM, final  @QueryParam("ZJHM")String ZJHM, final  @QueryParam("KSSJ")String KSSJ, final  @QueryParam("JSSJ")String JSSJ, final  @QueryParam("pageNo")String pageNo, final  @QueryParam("pageSize")String pageSize){

    System.out.println("接收联系函-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.receiveContactLetterList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),LXHBH, ZGXM, ZJHM, KSSJ, JSSJ, pageNo, pageSize));
  }

  /**
   * 
   * 用户信息详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  @Path("/getAccountDetails/{YWLSH}")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response transferOutAccountDetails(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

    System.out.println("用户信息详情-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutAccountDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
  }

  /**
   * 
   * 确认办理-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  @Path("/Confirmation/{YWLSH}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferOutConfirmation(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final  RequestWrapper<TransferOutConfirmationModle> body){

    System.out.println("确认办理-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutConfirmation((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 联系函详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  @Path("/getDetails/{YWLSH}")
  @GET
  @Produces("application/json; charset=utf-8")
  public Response transferOutDetails(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

    System.out.println("联系函详情-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
  }

  /**
   * 
   * 录入用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  @Path("/{type}")
  @POST
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferOutPost(@Context HttpRequest httpRequest,final @PathParam("type") String type, final  RequestWrapper<TransferOutPostModle> body){

    System.out.println("录入用户信息-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutPost((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),type, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 修改用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  @Path("/{type}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferOutPut(@Context HttpRequest httpRequest,final @PathParam("type") String type, final  RequestWrapper<TransferOutPutModle> body){

    System.out.println("修改用户信息-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutPut((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),type, body == null ? null : body.getReq()));
  }

  /**
   * 
   * 驳回-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  @Path("/Reject/{YWLSH}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferOutReject(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final  RequestWrapper<TransferOutRejectModle> body){

    System.out.println("驳回-转移接续转出");

    return ResUtils.wrapEntityIfNeeded(this.TransferOutApi.transferOutReject((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, body == null ? null : body.getReq()));
  }

}
