package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.BatchReviewInfo;

public interface IEstateProjectReviewApi<T> {


    /**
     * 审核楼盘
     *
     **/
    public T putEstateReView(TokenContext tokenContext, final BatchReviewInfo batchReviewInfo);


    /**
     * 楼盘审核查询
     *
     * @param status    状态（0：待处理 1：已处理）
     * @param LPMC      楼盘名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     **/
    public T getEstateProjectReview(final String status, final String LPMC, final String startTime, final String endTime);


    /**
     * 查询未审核列表
     * @param LPMC
     * @param DDSJKS
     * @param DDSJJS
     * @param pageSize
     * @param pageNo
     * @return
     */

    public T getEstateReviewList(TokenContext tokenContext, final String CZNR, final String LPMC, final String DDSJKS, final String DDSJJS, final String pageSize, final String pageNo);

    /**
     * 查询已审核列表
     * @param LPMC
     * @param ZhuangTai
     * @param SLSJKS
     * @param SLSJJS
     * @param pageSize
     * @param pageNo
     * @return
     */
    public T getEstateReviewedList(TokenContext tokenContext, final String YWLX, final String LPMC, final String ZhuangTai, final String SLSJKS, final String SLSJJS, final String pageSize, final String pageNo);
}