package com.handge.housingfund.common.service.collection.allochthonous.service;


import com.handge.housingfund.common.service.collection.allochthonous.model.*;
import com.handge.housingfund.common.service.TokenContext;

import java.util.*;
import java.io.*;

@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
public interface TransferInCancelInterface{

  /**
   * 
   * 转入-撤销（只能在“联系函复合通过-01”的状态）
   * @param YWLSH 业务流水号
   * @param body 
   */
  public CommonResponses transferInCancel(TokenContext tokenContext, final String YWLSH, final TransferInCancelModle body);


}
