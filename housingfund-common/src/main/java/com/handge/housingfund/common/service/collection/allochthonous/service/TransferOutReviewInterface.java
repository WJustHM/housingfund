package com.handge.housingfund.common.service.collection.allochthonous.service;


import com.handge.housingfund.common.service.collection.allochthonous.model.*;
import com.handge.housingfund.common.service.TokenContext;

import java.util.*;
import java.io.*;

@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
public interface TransferOutReviewInterface{

  /**
   * 
   * 已审核-转移接续转出
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZRGJJZXMC 转入公积金中心名称
   * @param zhuangTai 状态
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  public TransferOutAuditedListModle transferOutAuditedList(TokenContext tokenContext, final String ZGXM, final String ZJHM, final String ZRGJJZXMC, final String zhuangTai, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize);


  /**
   * 
   * 未审核-转移接续转出
   * @param ZGXM 职工姓名
   * @param ZJHM 证件号码
   * @param ZRGJJZXMC 转入公积金中心名称
   * @param KSSJ 开始时间
   * @param JSSJ 结束时间
   * @param pageNo 当前页数
   * @param pageSize 当前页数数据条数
   */
  public TransferOutNotReviewedListModle transferOutNotReviewedList(TokenContext tokenContext, final String ZGXM, final String ZJHM, final String ZRGJJZXMC, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize);


}
