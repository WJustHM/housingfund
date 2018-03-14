package com.handge.housingfund.server.controllers.collection.allochthonous;

import com.handge.housingfund.common.service.collection.allochthonous.model.*;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.collection.allochthonous.api.TransferInCancelApi;
import com.handge.housingfund.server.controllers.ResUtils;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;



@Controller
@Path("/TransferInCancel")
@SuppressWarnings({"SpringAutowiredFieldsWarningInspection", "JavaDoc", "SpellCheckingInspection"})
public class TransferInCancelResource{

  @Autowired
  private TransferInCancelApi<Response> TransferInCancelApi;

  /**
   * 
   * 转入-撤销（只能在“联系函复合通过-01”的状态）
   * @param YWLSH 业务流水号
   * @param body 
   */
  @Path("/{YWLSH}")
  @PUT
  @Produces("application/json; charset=utf-8")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferInCancel(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final  RequestWrapper<TransferInCancelModle> body){

    System.out.println("转入-撤销（只能在“联系函复合通过-01”的状态）");

    return ResUtils.wrapEntityIfNeeded(this.TransferInCancelApi.transferInCancel((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, body == null ? null : body.getReq()));
  }

}
