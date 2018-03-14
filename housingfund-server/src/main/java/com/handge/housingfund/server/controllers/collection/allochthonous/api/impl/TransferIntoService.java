package com.handge.housingfund.server.controllers.collection.allochthonous.api.impl;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;

import com.handge.housingfund.common.service.collection.allochthonous.service.TransferIntoInterface;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferIntoApi;

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
public class TransferIntoService implements TransferIntoApi<Response>{

  @Autowired
  private TransferIntoInterface TransferIntoInterface;

  /**
   * 
   * 办理转移-转移接续转入
   * @param LXHBH 联系函编号
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param zhuangTai 状态 （0：账户信息审核通过 1：账户信息审核不通过 2：协商中 3：系统处理中 4：正常办结 5：账户信息已录）
   * @param ZCGJJZXMC 转出公积金中心名称
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  public Response handleTransferList(TokenContext tokenContext,final String LXHBH, final String ZGXM, final String ZJHM, final String zhuangTai, final String ZCGJJZXMC, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize){

    System.out.println("办理转移-转移接续转入");

    try {

        return Response.status(200).entity(this.TransferIntoInterface.handleTransferList(tokenContext,LXHBH, ZGXM, ZJHM, zhuangTai, ZCGJJZXMC, KSSJ, JSSJ, pageNo, pageSize)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 业务流程详情-转移接续转入
   * @param YWLSH 业务流水号
   */
  public Response transferIntoBusinessDetails(TokenContext tokenContext,final String YWLSH){

    System.out.println("业务流程详情-转移接续转入");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoBusinessDetails(tokenContext,YWLSH)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 联系函详情-转移接续转入
   * @param YWLSH 业务流水号
   */
  public Response transferIntoDetails(TokenContext tokenContext,final String YWLSH){

    System.out.println("联系函详情-转移接续转入");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoDetails(tokenContext,YWLSH)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 办结-转移接续转入
   * @param YWLSH 业务流水号
   * @param body 
   */
  public Response transferIntoFinish(TokenContext tokenContext,final String YWLSH, final TransferIntoFinishModle body){

    System.out.println("办结-转移接续转入");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoFinish(tokenContext,YWLSH, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

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
  public Response transferIntoList(TokenContext tokenContext,final String LXHBH, final String ZGXM, final String ZJHM, final String ZCGJJZXMC,final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize){

    System.out.println("申请受理-转移接续转入");

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoList(tokenContext,LXHBH, ZGXM, ZJHM, ZCGJJZXMC,ZhuangTai, KSSJ, JSSJ, pageNo, pageSize)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 协商-转移接续转入
   * @param YWLSH 业务流水号
   * @param body 
   */
  public Response transferIntoNegotiation(TokenContext tokenContext,final String YWLSH, final TransferIntoNegotiationModle body){

    System.out.println("协商-转移接续转入");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoNegotiation(tokenContext,YWLSH, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 录入联系函-转移接续转入
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public Response transferIntoPost(TokenContext tokenContext,final String type, final TransferIntoPostModle body){

    System.out.println("录入联系函-转移接续转入");

    if (!StringUtil.notEmpty(type)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"类型（0：保存 1：提交）");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoPost(tokenContext,type, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

  /**
   * 
   * 申请修改-转移接续转入
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public Response transferIntoPut(TokenContext tokenContext,final String type, final TransferIntoPutModle body){

    System.out.println("申请修改-转移接续转入");

    if (!StringUtil.notEmpty(type)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"类型（0：保存 1：提交）");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferIntoInterface.transferIntoPut(tokenContext,type, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

}
