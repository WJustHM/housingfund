package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.finance.model.AccountBookModel;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/8/23.
 */
public interface IAccountBookAPI {
    /**
     * 获取账套
     * @return
     */
    public Response getAccountBookList();

    /**
     * 更新账套
     * @param accountBookModel
     * @return
     */
    public Response updateAccountBook(AccountBookModel accountBookModel);

    /**
     * 获取会计期间列表
     * @param KJND
     * @return
     */
    public Response getAccountPeriodList(String KJND);

    /**
     * 添加会计年度
     * @param KJND
     * @return
     */
    public Response addAccountPeriod(String KJND);
}
