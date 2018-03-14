package com.handge.housingfund.server.controllers.collection.allochthonous.api;

import com.handge.housingfund.common.service.TokenContext;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;


import java.util.Map;
import java.util.HashMap;
import java.io.File;

@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
public interface TransferOutApi<T>{


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
  public T handleContactLetterList(TokenContext tokenContext, final String LXHBH, final String ZGXM, final String ZJHM, final String zhuangTai, final String ZRGJJZXMC, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize);

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
  public T receiveContactLetterList(TokenContext tokenContext, final String LXHBH, final String ZGXM, final String ZJHM, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize);

  /**
   * 
   * 用户信息详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  public T transferOutAccountDetails(TokenContext tokenContext, final String YWLSH);

  /**
   * 
   * 确认办理-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  public T transferOutConfirmation(TokenContext tokenContext, final String YWLSH, final TransferOutConfirmationModle body);

  /**
   * 
   * 联系函详情-转移接续转出
   * @param YWLSH 业务流水号
   */
  public T transferOutDetails(TokenContext tokenContext, final String YWLSH);

  /**
   * 
   * 录入用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public T transferOutPost(TokenContext tokenContext, final String type, final TransferOutPostModle body);

  /**
   * 
   * 修改用户信息-转移接续转出
   * @param type 类型（0：保存 1：提交）
   * @param body 
   */
  public T transferOutPut(TokenContext tokenContext, final String type, final TransferOutPutModle body);

  /**
   * 
   * 驳回-转移接续转出
   * @param YWLSH 业务流水号
   * @param body 
   */
  public T transferOutReject(TokenContext tokenContext, final String YWLSH, final TransferOutRejectModle body);

}
