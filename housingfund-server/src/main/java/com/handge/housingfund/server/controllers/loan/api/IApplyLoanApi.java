package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.ApplicantPost;

import javax.ws.rs.core.Response;

public interface IApplyLoanApi<T> {


    /**
     * 修改页面
     *
     * @param YWLSH 业务流水号
     **/
    public T putModifyApplication(TokenContext tokenContext, String status, final String YWLSH, final ApplicantPost applicantpost);


    /**
     * 申请贷款详情页面
     *
     * @param YWLSH 业务流水号
     **/
    public T getApplyDetails(TokenContext tokenContext,final String YWLSH);



    /**
     * 获取审批表id
     *
     * @param YWLSH 业务流水号
     **/
    public T getSPBId(TokenContext tokenContext,final String YWLSH);

    /**
     * 新增申请贷款
     *
     * @param status 0:保存 1:提交
     **/
    public T postApplyLoan(TokenContext tokenContext,final String status, final ApplicantPost applicantpost);


    /**
     * 贷款repaymentApplyReceipt
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    public T getApplyLoanList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status, String SKYHDM,String pageSize,String page,String KSSJ,String JSSJ);


    /**
     * 贷款repaymentApplyReceipt
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：所有 1：新建 2：待审核 3：审核不通过 4：待签合同）
     **/
    public T getApplyLoanList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status, String SKYHDM,String pageSize,String marker,String action,String KSSJ,String JSSJ);

    /**
     * 单独修改状态（提交与撤回）
     *
     * @param loan_id 贷款id
     * @param status  状态（0：提交 1：撤回）
     **/
    public T putApplyLoanStatus(TokenContext tokenContext,final String loan_id, final String status);

    /**
     * 根据个人账号获取贷款个人信息
     *
     * @param GRZH 个人账号
     **/
    public Response getPersonalAccountInfo(TokenContext tokenContext,String GRZH);

}