package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.model.*;

/**
 * Created by Funnyboy on 2017/8/16.
 */
public interface ILoanContractService {

    /**
     * 合同变更申请列表
     *
     */
    public PageRes<LoanContractListResponseRes> getLoanContractReviewList(TokenContext tokenContext,String YWWD, final String JKRXM, final String JKRZJHM, final String status,String JKHTBH, String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM);

    /**
     * 新增申请
     * // completed
     * */
    public  CommonResponses putBorrowerInformationApplication(TokenContext tokenContext,String action,GetBorrowerInformation body);

    /**
     * 借款人信息(共同借款人)详情2
     * // completed
     */
    public GetBorrowerInformation getBorrowerInformation(TokenContext tokenContext, final String JKHTBH);

    /**
     * 借款人信息(共同借款人)变更
     * // completed
     */
    public CommonResponses putBorrowerInformation(TokenContext tokenContext,String action,String YWLSH, GetBorrowerInformation getborr);

    /**
     * 借款人信息(共同借款人)查询详情
     * // completed
     */
    public GetBorrowerInformation getBorrowerInformationChange(TokenContext tokenContext,final String YWLSH);

    /**
     * 审核列表
     *
     * */
    public PageRes<ContractAlterReviewRes> getRepaymentReview(TokenContext tokenContext,String status, String JKTHBH, String stime, String etime);

    /**
     * 办结操作
     *
     * */
    public ResponseEntity doAction(TokenContext tokenContext,String YWLSH);

    /**
     * 回执单
     * */
    public CommonResponses  headLoanContract(TokenContext tokenContext,String ywlsh);

    public PageResNew<LoanContractListResponseRes> getLoanContractReviewListnew(TokenContext tokenContext, String jkrxm, String jkrzjhm, String status, String jkhtbh, String pageSize, String marker, String kssj, String jssj, String action,String YHDM);

    public CommonResponses getLoanEntrustDeductPdf(TokenContext tokenContext, String ywlsh);
}
