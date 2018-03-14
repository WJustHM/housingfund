package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ISignContractService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.SignContractPost;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ISignContractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class SignContractApi implements ISignContractApi<Response> {


    @Autowired
    private ISignContractService signContractService;

    /**
     * 贷款合同（待定）
     *
     * @param YWLSH 业务流水号
     **/
    public Response postPurchaseContract(TokenContext tokenContext, final String status ,final String YWLSH, final SignContractPost body) {


        System.out.println("贷款合同（待定）");

        if (YWLSH == null) {

            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.signContractService.postPurchaseContract(tokenContext,status,YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }


}