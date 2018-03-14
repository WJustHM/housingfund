package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.BatchReviewInfo;

public interface IGuaranteeCompanyReviewApi<T> {


    /**
     * 审核房开
     *
     **/
    public T putGuaranteeCompanyReview(TokenContext tokenContext, final BatchReviewInfo batchReviewInfo);


    /**
     * 查询未审核列表
     * @param FKGS
     * @param DDSJKS
     * @param DDSJJS
     * @param pageSize
     * @param pageNo
     * @return
     */
    public T getCompanyReviewList(TokenContext tokenContext, final String CZNR, final String FKGS, final String DDSJKS, final String DDSJJS, final String pageSize, final String pageNo);

    /**
     * 查询已审核列表
     * @param FKGS
     * @param ZhuangTai
     * @param SLSJKS
     * @param SLSJJS
     * @param pageSize
     * @param pageNo
     * @return
     */
    public T getCompanyReviewedList(TokenContext tokenContext, final String YWLX, final String FKGS, final String ZhuangTai, final String SLSJKS, final String SLSJJS, final String pageSize, final String pageNo);


}