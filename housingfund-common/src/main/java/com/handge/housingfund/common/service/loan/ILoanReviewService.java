package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.model.GetApplicantResponse;
import com.handge.housingfund.common.service.loan.model.LoanReviewListResponse;

public interface ILoanReviewService {

    /**
     * 审核操作办结
     *
     * @param YWLSH 业务流水号
     **/
    public void postLoanReviewReason(TokenContext tokenContext, final String YWLSH);


    /**
     * 贷款审核详情
     *
     * @param YWLSH 业务流水号
     **/
    public GetApplicantResponse getLoanReviewDetails(TokenContext tokenContext,final String YWLSH);


    /**
     * 贷款审核列表
     *
     * @param SFCL      是否处理 （0：待处理 1：已处理）
     * @param JKRXM     借款人姓名
     * @param status    状态（0：审核不通过  1：办结 2：待入账）
     * @param startTime 开始时间
     * @param endTime   结束时间
     **/
    public PageRes<LoanReviewListResponse> getLoanReview(TokenContext tokenContext,final String SFCL, final String JKRXM, final String status, final String startTime, final String endTime, String pageSize, String page);
}
