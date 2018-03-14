package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ILoanContractService;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanContractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class LoanContractApi implements ILoanContractApi<Response> {


    @Autowired
    private ILoanContractService loanContractService;

    /**
     * 合同信息列表(待定)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    public Response getLoanContractList(TokenContext tokenContext, final String JKRXM, final String JKRZJHM, final String status) {


        System.out.println("合同信息列表(待定)");

        if (JKRXM == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new LoanContractListResponse() {{


        }}).build();
    }





/*------------------------------------------------------------------------------------------------*/

    /**
     * 合同变更repaymentApplyReceipt（合同变更申请列表）
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    public Response getLoanContractReviewList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status, String JKHTBH,String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM) {

        System.out.println("合同变更repaymentApplyReceipt");

        if (!StringUtil.notEmpty(page) || !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码或每页条数");

        try {
            return Response.status(200).entity(this.loanContractService.getLoanContractReviewList(tokenContext,YWWD,JKRXM, JKRZJHM, status,JKHTBH, pageSize, page,KSSJ,JSSJ,YHDM,ZJHM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getLoanContractReviewListnew(TokenContext tokenContext, String jkrxm, String jkrzjhm, String status, String jkhtbh, String pageSize, String marker, String kssj, String jssj, String action,String YHDM) {
        System.out.println("合同变更repaymentApplyReceipt");

        if ( !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码或每页条数");

        try {
            return Response.status(200).entity(this.loanContractService.getLoanContractReviewListnew(tokenContext,jkrxm, jkrzjhm, status,jkhtbh, pageSize, marker,kssj,jssj,action,YHDM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 新增申请
     */
    @Override
    public Response putBorrowerInformationApplication(TokenContext tokenContext,String action,  GetBorrowerInformation body) {

        System.out.println("新增申请");

        if (body == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "表单数据");

        if (!StringUtil.notEmpty(action) )
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型(0-保存、1-提交)");

        try {
            return Response.status(200).entity(this.loanContractService.putBorrowerInformationApplication(tokenContext,action,  body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 借款人信息(共同借款人)详情2
     */
    @Override
    public Response getBorrowerInformation(TokenContext tokenContext,String JKHTBH) {

        System.out.println("借款人信息(共同借款人)详情2");

        try {
            return Response.status(200).entity(this.loanContractService.getBorrowerInformation(tokenContext, JKHTBH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 借款人信息(共同借款人)变更
     */
    @Override
    public Response putBorrowerInformation(TokenContext tokenContext,String action,  String YWLSH, GetBorrowerInformation body) {

        System.out.println("借款人信息(共同借款人)变更");

        if (!StringUtil.notEmpty(action)  && !StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "操作类型或业务流水号");

        if (body == null) return ResUtils.buildParametersErrorResponse();

        try {
            return Response.status(200).entity(this.loanContractService.putBorrowerInformation(tokenContext,action,  YWLSH, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 借款人信息(共同借款人)查询详情
     */
    @Override
    public Response getBorrowerInformationChange(TokenContext tokenContext,String YWLSH) {

        System.out.println("借款人信息(共同借款人)查询详情");

        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");

        try {
            return Response.status(200).entity(this.loanContractService.getBorrowerInformationChange(tokenContext,YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 借款人信息(共同借款人)查询详情
     */
    @Override
    public Response headLoanContract(TokenContext tokenContext,String YWLSH) {

        System.out.println("合同变更回执单");

        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");

        try {
            return Response.status(200).entity(this.loanContractService.headLoanContract(tokenContext,YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


/*------------------------------------------------------------------------------------------------*/

    /**
     * 还款账号变更
     **/
    public Response postAccountChange(TokenContext tokenContext,final AccountChangePost body) {


        System.out.println("还款账号变更");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 借款人变更
     **/
    public Response postBorrowerChange(TokenContext tokenContext,final BorrowerChangePost body) {


        System.out.println("借款人变更");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }

    /**
     * 委托扣划变更
     **/
    public Response postCommissionedDebitChange(TokenContext tokenContext,final CommissionedDebitChange body) {


        System.out.println("委托扣划变更");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }

    public Response getLoanEntrustDeductPdf(TokenContext tokenContext, String ywlsh) {

        System.out.println("打印贷款委托扣划协议回执单");

        if (ywlsh == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.loanContractService.getLoanEntrustDeductPdf(tokenContext, ywlsh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }

}