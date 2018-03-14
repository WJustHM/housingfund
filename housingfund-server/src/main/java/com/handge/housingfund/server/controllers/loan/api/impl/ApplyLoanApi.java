package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.IApplyLoanService;
import com.handge.housingfund.common.service.loan.model.ApplicantPost;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IApplyLoanApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@SuppressWarnings({"serial", "SpringAutowiredFieldsWarningInspection"})
@Component
public class ApplyLoanApi implements IApplyLoanApi<Response> {

    @Autowired
    private IApplyLoanService applyLoanService;

    /**
     * 修改页面
     *
     * @param YWLSH 业务流水号
     **/
    public Response putModifyApplication(TokenContext tokenContext, final String status, final String YWLSH, final ApplicantPost body) {


        System.out.println("修改页面");

        if (YWLSH == null || !StringUtil.notEmpty(status)) {

            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.applyLoanService.putModifyApplication(tokenContext,status, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }


    /**
     * 申请贷款详情页面
     *
     * @param YWLSH 业务流水号
     **/
    public Response getApplyDetails(TokenContext tokenContext,final String YWLSH) {


        System.out.println("申请贷款详情页面");

        if (YWLSH == null) {

            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.applyLoanService.getApplyDetails(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }



    /**
     * 获取审批表id
     *
     * @param YWLSH 业务流水号
     **/
    public Response getSPBId(TokenContext tokenContext,final String YWLSH) {


        System.out.println("获取审批表id");

        if (YWLSH == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.applyLoanService.getSPBId(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }


    /**
     * 新增申请贷款
     *
     * @param status 0:保存 1:提交
     **/
    public Response postApplyLoan(TokenContext tokenContext,final String status, final ApplicantPost body) {


        System.out.println("新增申请贷款");

        if (status == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.applyLoanService.postApplyLoan(tokenContext,status, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }
    /**
     * 贷款申请列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    public Response getApplyLoanList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status, String SKYHDM,String pageSize,String page,String KSSJ,String JSSJ) {


        System.out.println("贷款申请列表");

        try {

            return Response.status(200).entity(this.applyLoanService.getApplyLoanList(tokenContext,YWWD,JKRXM, JKRZJHM, status,SKYHDM, pageSize,page,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }
    /**
     * 贷款申请列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    public Response getApplyLoanList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status, String SKYHDM,String pageSize,String marker,String action,String KSSJ,String JSSJ) {


        System.out.println("贷款申请列表");

        try {

            return Response.status(200).entity(this.applyLoanService.getApplyLoanList(tokenContext,YWWD,JKRXM, JKRZJHM, status,SKYHDM, pageSize,marker,action,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    /**
     * 单独修改状态（提交与撤回）
     *
     * @param YWLSH  业务流水号
     * @param status 状态（0：提交 1：撤回）
     **/
    public Response putApplyLoanStatus(TokenContext tokenContext,final String YWLSH, final String status) {


        System.out.println("单独修改状态（提交与撤回）");

        if (YWLSH == null) {

            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.applyLoanService.putApplyLoanStatus(tokenContext,YWLSH, status)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    /**
     * 根据个人账号获取贷款个人信息
     *
     * @param GRZH 个人账号
     **/
    public Response getPersonalAccountInfo(TokenContext tokenContext,String GRZH){

        System.out.println("获取个人账号信息）");

        if (GRZH == null) {

            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.applyLoanService.getPersonalAccountInfo(tokenContext,GRZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

}