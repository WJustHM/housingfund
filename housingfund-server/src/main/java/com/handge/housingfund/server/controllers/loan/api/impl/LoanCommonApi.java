package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.ICommonService;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanCommonApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;


/**
 * Created by Liujuhao on 2017/9/1.
 */
@Component
public class LoanCommonApi implements ILoanCommonApi {

    @Autowired
    private ICommonService commonService;

    /**
     * 删除申请
     *
     * @param YWWD 业务网点
     * @param CZY  操作员
     **/
    public Response deleteApplication(TokenContext tokenContext,final String YWWD, final String CZY, final DelList body) {

        System.out.println("删除申请");

        if (YWWD == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.commonService.deleteApplication(tokenContext,YWWD, CZY, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    public Response multipleReviewTest(String role, String type, String subModule, String ywwd) {

        System.out.println("多级审核测试接口");

        try {

            return Response.status(200).entity(this.commonService.multipleReviewTest(role, type, subModule, ywwd)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 提交与撤回还款申请
     * //TODO djj
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
    public Response putEstateStatusSubmit(TokenContext tokenContext, final BatchSubmission body, final String status, String ywlx) {

        System.out.println("提交与撤回还款申请");

        if (!StringUtil.notEmpty(status) || (!"0".equals(status) && !"1".equals(status) && !"2".equals(status)))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "状态编码有误(0-提交、1-撤回、2-作废)");

        try {
            return Response.status(200).entity(this.commonService.batchSubmit(tokenContext, body, status, ywlx)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

}
