package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.account.IPolicyService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.IPolicyAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * Created by 向超 on 2017/9/20.
 */
@Component
public class PolicyAPI implements IPolicyAPI<Response>{
    @Autowired
    private IPolicyService policyService;
    @Override
    public Response updatePolicyInfo(String id, String xgz, String sxrq) {
        if(!StringUtil.notEmpty(id)||!StringUtil.notEmpty(xgz)||!StringUtil.notEmpty(sxrq))
           return ResUtils.buildParametersMissErrorResponse();
        if(!StringUtil.isDigits(xgz)){
          return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Data_NOT_MATCH,"修改值");
        }
        try {
            return Response.status(200).entity(policyService.updatePolicyInfo(id,xgz,sxrq)).build();
        }catch (ErrorException e){
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getPolicy() {
        try {
            return Response.status(200).entity(policyService.getPolicy()).build();
        }catch (ErrorException e){
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(e.getCode());
            }}).build();
        }
     }
}
