package com.handge.housingfund.server.controllers.collection.api;

import com.handge.housingfund.common.service.TokenContext;

import java.util.List;

/**
 * Created by Liujuhao on 2017/8/29.
 */
public interface CommonApi<T> {

    /**
     * 删除，归集业务
     *
     * @param YWLSHs 业务流水号
     * @param YWMK 业务模块(01个人，02单位)
     **/
    public T deleteOperation(TokenContext tokenContext, final List<String> YWLSHs, final String YWMK);

    /**
     * 撤回，归集业务
     *
     * @param YWLSH 业务流水号
     * @param YWMK 业务模块(01个人，02单位)
     **/
    public T revokeOperation(TokenContext tokenContext, final String YWLSH, final String YWMK);

    public T getRecordHistory(final String ZhangHao, final String YWMK, final String pageSize, final String pageNo);

    public T getReviewInfos(String YWLSH);

    T accountRecord(String ywlsh, String ywlx, String code);
}
