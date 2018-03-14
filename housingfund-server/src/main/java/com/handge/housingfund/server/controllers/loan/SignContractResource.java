package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.SignContractPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ISignContractApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，1
 */
@Path("/loan/signContract")
@Controller
public class SignContractResource {

    @Autowired
    private ISignContractApi<Response> service;

    /**
     * 贷款合同
     *
     * @param YWLSH 业务流水号
     **/

    @Path("")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPurchaseContract(@Context HttpRequest httpRequest, final  @QueryParam("status") String status , final @QueryParam("YWLSH") String YWLSH, final RequestWrapper<SignContractPost> signcontractpost) {

        System.out.println("贷款合同");

        return ResUtils.wrapEntityIfNeeded(this.service.postPurchaseContract((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),status,YWLSH, signcontractpost == null ? null : signcontractpost.getReq()));
    }


}