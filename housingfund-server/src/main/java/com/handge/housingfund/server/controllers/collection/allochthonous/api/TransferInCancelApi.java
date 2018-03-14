package com.handge.housingfund.server.controllers.collection.allochthonous.api;

import com.handge.housingfund.common.service.TokenContext;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;


import java.util.Map;
import java.util.HashMap;
import java.io.File;

@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "UnnecessaryInterfaceModifier"})
public interface TransferInCancelApi<T>{


  /**
   * 
   * 转入-撤销（只能在“联系函复合通过-01”的状态）
   * @param YWLSH 业务流水号
   * @param body 
   */
  public T transferInCancel(TokenContext tokenContext, final String YWLSH, final TransferInCancelModle body);

}
