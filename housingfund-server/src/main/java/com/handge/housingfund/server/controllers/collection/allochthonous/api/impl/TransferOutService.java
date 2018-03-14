package com.handge.housingfund.server.controllers.collection.allochthonous.api.impl;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;

import com.handge.housingfund.common.service.collection.allochthonous.service.TransferOutInterface;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferOutApi;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;

@Component
@SuppressWarnings({"SpringAutowiredFieldsWarningInspection","unused","SpellCheckingInspection", "SpringJavaAutowiredMembersInspection", "JavaDoc"})
public class TransferOutService implements TransferOutApi<Response>{

  @Autowired
  private TransferOutInterface TransferOutInterface;

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
  public Response handleContactLetterList(TokenContext tokenContext,final String LXHBH, final String ZGXM, final String ZJHM, final String zhuangTai, final String ZRGJJZXMC, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize){

    System.out.println("处理联系函-转移接续转出");

    try {

        return Response.status(200).entity(this.TransferOutInterface.handleContactLetterList(tokenContext,LXHBH, ZGXM, ZJHM, zhuangTai, ZRGJJZXMC, KSSJ, JSSJ, pageNo, pageSize)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

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
  public Response receiveContactLetterList(TokenContext tokenContext,final String LXHBH, final String ZGXM, final String ZJHM, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize){

    System.out.println("接收联系函-转移接续转出");

    try {

        return Response.status(200).entity(this.TransferOutInterface.receiveContactLetterList(tokenContext,LXHBH, ZGXM, ZJHM, KSSJ, JSSJ, pageNo, pageSize)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 用户信息详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  public Response transferOutAccountDetails(TokenContext tokenContext,final String YWLSH){

    System.out.println("用户信息详情-转移接续转出");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutAccountDetails(tokenContext,YWLSH)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 确认办理-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  public Response transferOutConfirmation(TokenContext tokenContext,final String YWLSH, final TransferOutConfirmationModle body){

    System.out.println("确认办理-转移接续转出");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutConfirmation(tokenContext,YWLSH, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 联系函详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  public Response transferOutDetails(TokenContext tokenContext,final String YWLSH){

    System.out.println("联系函详情-转移接续转出");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutDetails(tokenContext,YWLSH)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 录入用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public Response transferOutPost(TokenContext tokenContext,final String type, final TransferOutPostModle body){

    System.out.println("录入用户信息-转移接续转出");

    if (!StringUtil.notEmpty(type)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"类型（0：保存 1：提交）");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutPost(tokenContext,type, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 修改用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public Response transferOutPut(TokenContext tokenContext,final String type, final TransferOutPutModle body){

    System.out.println("修改用户信息-转移接续转出");

    if (!StringUtil.notEmpty(type)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"类型（0：保存 1：提交）");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutPut(tokenContext,type, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 驳回-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  public Response transferOutReject(TokenContext tokenContext,final String YWLSH, final TransferOutRejectModle body){

    System.out.println("驳回-转移接续转出");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferOutInterface.transferOutReject(tokenContext,YWLSH, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

}
