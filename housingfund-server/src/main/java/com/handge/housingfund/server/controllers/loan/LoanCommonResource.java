package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanCommonApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Liujuhao on 2017/9/1.
 */

@Controller
@Path("/loan")
public class LoanCommonResource {

    @Autowired
    private ILoanCommonApi<Response> service;

    /**
     * 删除申请(贷款业务所有申请删除操作)
     *
     * @param YWWD 业务网点
     * @param CZY  操作员
     **/

    @Path("/delete")
    @DELETE
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteApplication(final @Context HttpRequest request, final @QueryParam("YWWD") String YWWD, final @QueryParam("CZY") String CZY, final RequestWrapper<DelList> dellist) {

        System.out.println("删除申请");

        return ResUtils.wrapEntityIfNeeded(this.service.deleteApplication((TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY),YWWD, CZY, dellist == null ? null : dellist.getReq()));
    }

    @GET
    @Path("/common/multipleReviewTest")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMultipleReviewTest(final @QueryParam("role") String role, final @QueryParam("type") String type,
                                          final @QueryParam("subModule") String subModule, final @QueryParam("ywwd") String ywwd) {

        System.out.println("多级审核测试接口");

        return ResUtils.wrapEntityIfNeeded(this.service.multipleReviewTest(role, type, subModule, ywwd));
    }

//    /**
//     * 提交与撤回还款申请
//     *
//     * @param action 状态（0：提交 1：撤回 ）
//     **/
//    @Path("/repayment/state/{action}")
//    @PUT
//    @Produces("application/json; charset=utf-8")
//    public Response putEstateStatusSubmit(final @Context HttpRequest request, final @PathParam("action") String action, final @QueryParam("YWLX") String ywlx,
//                                          final RequestWrapper<BatchSubmission> body) {
//        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
//        System.out.println("单独修改状态（提交与撤回,删除）申请受理");
//
//        return ResUtils.wrapEntityIfNeeded(this.service.putEstateStatusSubmit(tokenContext, body == null ? null : body.getReq(), action, ywlx));
//    }
}
