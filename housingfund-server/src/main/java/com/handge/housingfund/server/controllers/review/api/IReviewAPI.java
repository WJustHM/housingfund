package com.handge.housingfund.server.controllers.review.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.BatchReviewCheck;
import com.handge.housingfund.common.service.review.model.BatchReviewInfo;

/**
 * Created by Liujuhao on 2017/9/25.
 */
public interface IReviewAPI<T> {

    /**
     * 获取未审核列表
     *
     * @return
     */
    public T getReviewList(
            TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String pageNo, String module, String type,
            String DWMC, String XingMing, String CZY, String GRZH, String TQYY, String ZJHM, String ZRZXMC, String ZCZXMC, String DWZH,
            String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
            String KHYHMC, String ZHHM, String YWMC);

    public T getReviewList(
            TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String marker/*标记*/, String action/*操作*/, String module, String type,
            String DWMC, String XingMing, String CZY, String GRZH, String TQYY,
            String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
            String KHYHMC, String ZHHM, String YWMC);

    /**
     * 获取已审核列表
     *
     * @return
     */
    public T getReviewedList(
            TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String pageNo, String module, String type, String ZhuangTai,
            String DWMC, String XingMing, String CZY, String GRZH, String TQYY, String ZJHM, String ZRZXMC, String ZCZXMC, String DWZH,
            String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
            String KHYHMC, String ZHHM, String YWMC);

    public T getReviewedList(
            TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String marker/*标记*/, String action/*操作*/, String module, String type, String ZhuangTai,
            String DWMC, String XingMing, String CZY, String GRZH, String TQYY,
            String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
            String KHYHMC, String ZHHM, String YWMC);

    /**
     * 审核操作
     *
     * @return
     */
    public T postBusinessReview(
            TokenContext tokenContext, BatchReviewInfo batchReviewInfo, String module, String type);

    /**
     * 审核业务，在途验证
     *
     * @param batchReviewCheck
     * @return
     */
    public T checkIsReviewing(TokenContext tokenContext, BatchReviewCheck batchReviewCheck, String module, String type);

    /**
     * 特审操作
     *
     * @return
     */
    public T postSpecialReview(TokenContext tokenContext, BatchReviewInfo batchReviewInfo, String module, String type);

}
