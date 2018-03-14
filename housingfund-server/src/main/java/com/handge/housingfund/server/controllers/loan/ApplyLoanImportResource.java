package com.handge.housingfund.server.controllers.loan;


import com.handge.housingfund.common.service.loan.model.ApplyLoanImportPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IApplyLoanImportApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 待定
 */
@Path("/loan/applyLoanImport")
@Controller
public class ApplyLoanImportResource {

    @Autowired
    private IApplyLoanImportApi<Response> service;

    /**
     * 导入贷款申请（待定）
     *
     * @param status 状态（0：导入申请表 1：导入委托申请表）
     **/
    @Path("/{status}")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postapplyLoanImport(final @PathParam("status") String status, final RequestWrapper<ApplyLoanImportPost> applyloanimportpost) {

        System.out.println("导入贷款申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postapplyLoanImport(status, applyloanimportpost == null ? null : applyloanimportpost.getReq()));
    }


}