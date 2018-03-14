package com.handge.housingfund.common.service;

import com.handge.housingfund.common.service.review.model.ReviewInfo;

import java.util.List;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/15.
 * 描述
 */
public interface ISaveAuditHistory<T> {

    /**
     * 保存业务一般流程信息
     * @param YWLSH
     * @param tokenContext
     * @param YWLX
     * @param CaoZuo
     */
    public void saveNormalBusiness(String YWLSH, TokenContext tokenContext, String YWLX, String CaoZuo);

    /**
     * 保存业务一般流程信息(后台)
     * @param YWLSH
     * @param YWLX
     * @param CaoZuo
     */
    public void saveNormalBusiness(String YWLSH, String YWLX, String CaoZuo);

    /**
     * 保存审核信息
     * @param YWLSH
     * @param reviewInfo
     * @return
     */
    public void saveAuditHistory(String YWLSH, ReviewInfo reviewInfo);

    /**
     * 获取审核历史信息
     * @param YWLSH
     * @return
     */
    public List<ReviewInfo> getAuditHistoryList(String YWLSH);
}
