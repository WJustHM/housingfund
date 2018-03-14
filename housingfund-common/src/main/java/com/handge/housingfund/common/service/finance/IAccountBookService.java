package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.finance.model.AccountBookModel;
import com.handge.housingfund.common.service.finance.model.AccountPeriod;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/23.
 */
public interface IAccountBookService {
    /**
     * 获取账套
     *
     * @return
     */
    AccountBookModel getAccountBookList();

    /**
     * 更新账套
     *
     * @param accountBookModel
     * @return
     */
    void updateAccountBook(AccountBookModel accountBookModel);

    /**
     * 获取会计期间列表
     *
     * @param KJND
     * @return
     */
    ArrayList<AccountPeriod> getAccountPeriodList(String KJND);

    /**
     * 添加会计年度
     *
     * @return
     */
    ArrayList<AccountPeriod> addAccountPeriod();

    /**
     * 会计期间结算
     *
     * @param id   会计期间id
     * @param jzr  结账人
     * @param jzrq 结账日期 yyyy-MM-dd
     **/
    void putAccountPeriodSettle(String id, String jzr, String jzrq);
}
