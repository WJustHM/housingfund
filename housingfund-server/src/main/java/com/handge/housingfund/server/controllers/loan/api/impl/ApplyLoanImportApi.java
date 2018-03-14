package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.loan.model.ApplyLoanImportPost;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.server.controllers.loan.api.IApplyLoanImportApi;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class ApplyLoanImportApi implements IApplyLoanImportApi<Response> {


    /**
     * 导入贷款申请
     *
     * @param status 状态（0：导入申请表 1：导入委托申请表）
     **/
    public Response postapplyLoanImport(final String status, final ApplyLoanImportPost body) {

        System.out.println("导入贷款申请");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


}