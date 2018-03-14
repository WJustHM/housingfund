package com.handge.housingfund.server.controllers.collection.allochthonous.api.impl;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;

import com.handge.housingfund.common.service.collection.allochthonous.service.TransferInCancelInterface;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferInCancelApi;

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
public class TransferInCancelService implements TransferInCancelApi<Response>{

  @Autowired
  private TransferInCancelInterface TransferInCancelInterface;

  /**
   * 
   * 转入-撤销（只能在“联系函复合通过-01”的状态）
   * @param YWLSH 业务流水号
   * @param body 
   */
  public Response transferInCancel(TokenContext tokenContext,final String YWLSH, final TransferInCancelModle body){

    System.out.println("转入-撤销（只能在“联系函复合通过-01”的状态）");

    if (!StringUtil.notEmpty(YWLSH)){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号");
    }
    

    if (body==null){

       throw new ErrorException(ReturnEnumeration.Parameter_MISS,"");
    }

    try {

        return Response.status(200).entity(this.TransferInCancelInterface.transferInCancel(tokenContext,YWLSH, body)).build();

    } catch (ErrorException e) {

        return Response.status(200).entity(e.getError()).build();
    }

  }

}
