package com.handge.housingfund.common.service.review;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.review.model.ReviewCheckRes;
import com.handge.housingfund.common.service.review.model.ReviewRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Liujuhao on 2017/9/25.
 */
public interface IBaseReview {

    /**
     * 获取未审核列表
     *
     * @param conditions 查询条件
     * @return
     */
    public PageRes getReviewInfo(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type);

    public PageResNew getReviewInfo(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type);

    /**
     * 获取已审核列表
     *
     * @param conditions 查询条件
     * @return
     */
    public PageRes getReviewedInfo(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type);

    public PageResNew getReviewedInfo(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type);

    /**
     * 审核通过/不通过
     *
     * @param YWLSHs
     * @return
     */
    public ReviewRes postBusinessReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type);


    /**
     * 检查业务是否正在审核中
     *
     * @param YWLSH 业务流水号
     * @param module  业务模块
     * @return
     */
    public ReviewCheckRes checkIsReviewing(TokenContext tokenContext, ArrayList<String> YWLSH, String module, String type);

    /**
     * 特审
     * @param YWLSHs
     * @param auditInfo
     * @return
     */
    public ReviewRes postSpecialReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, String module, String type);
}
