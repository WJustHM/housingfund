package com.handge.housingfund.common.service.collection.allochthonous.service;


import com.handge.housingfund.common.service.collection.allochthonous.model.*;
import com.handge.housingfund.common.service.TokenContext;

import java.util.*;
import java.io.*;

@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
public interface TransferIntoReviewInterface{

  /**
   * 
   * 已审核-转移接续转入
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZCGJJZXMC 转出公积金中心名称
   * @param zhuangTai 状态
   * @param SLSJ 受理时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  public TransferIntoAuditedListModle transferIntoAuditedList(TokenContext tokenContext, final String ZGXM, final String ZJHM, final String ZCGJJZXMC, final String zhuangTai, final String SLSJ, final String pageNo, final String pageSize);


  /**
   * 
   * 未审核-转移接续转入
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZCGJJZXMC 转出公积金中心名称
   * @param DDSJ 到达时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  public TransferIntoNotReviewedListModle transferIntoNotReviewedList(TokenContext tokenContext, final String ZGXM, final String ZJHM, final String ZCGJJZXMC, final String DDSJ, final String pageNo, final String pageSize);


}
