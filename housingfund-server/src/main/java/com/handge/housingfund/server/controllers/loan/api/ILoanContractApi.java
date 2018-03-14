package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.AccountChangePost;
import com.handge.housingfund.common.service.loan.model.BorrowerChangePost;
import com.handge.housingfund.common.service.loan.model.CommissionedDebitChange;
import com.handge.housingfund.common.service.loan.model.GetBorrowerInformation;

public interface ILoanContractApi<T> {


    /**
     * 合同信息列表(待定)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    public T getLoanContractList(TokenContext tokenContext, final String JKRXM, final String JKRZJHM, final String status);




/*------------------------------------------------------------------------------------------------*/

    /**
     * 合同变更repaymentApplyReceipt(合同变更申请列表)
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param status  状态（0：新建、待审核、审核不通过、办结 1：办结、已撤销）
     **/
    public T getLoanContractReviewList(TokenContext tokenContext,String YWWD,final String JKRXM, final String JKRZJHM, final String status,String JKHTBH,String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM);

    T getLoanContractReviewListnew(TokenContext tokenContext, String jkrxm, String jkrzjhm, String status, String jkhtbh, String pageSize, String marker, String kssj, String jssj, String action,String YHDM);

    /**
     * 新增申请
     * */
    public T putBorrowerInformationApplication(TokenContext tokenContext,String action,GetBorrowerInformation body);

    /**
     * 借款人信息(共同借款人)详情2
     */
    public T getBorrowerInformation(TokenContext tokenContext, final String JKHTBH);

    /**
     * 借款人信息(共同借款人)变更
     */
    public T putBorrowerInformation(TokenContext tokenContext,String action,String YWLSH,GetBorrowerInformation body);

    /**
     * 借款人信息(共同借款人)查询详情
     */
    public T getBorrowerInformationChange(TokenContext tokenContext,final String YWLSH);

    /**
     * 回执单
     * */
    public T  headLoanContract(TokenContext tokenContext,String ywlsh);


  /*------------------------------------------------------------------------------------------------*/
    /**
     * 还款账号变更
     **/
    public T postAccountChange(TokenContext tokenContext,final AccountChangePost accountchargepost);


    /**
     * 借款人变更
     **/
    public T postBorrowerChange(TokenContext tokenContext,final BorrowerChangePost borrowerchargepost);

    /**
     * 委托扣划变更
     **/
    public T postCommissionedDebitChange(TokenContext tokenContext,final CommissionedDebitChange commissionedDebitChange);

    /**
     * 打印委托扣划协议
     */
    public T getLoanEntrustDeductPdf(TokenContext tokenContext, String dkzh);

}