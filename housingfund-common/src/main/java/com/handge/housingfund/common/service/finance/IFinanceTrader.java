package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.bank.bean.center.FixedAccBalanceQueryOut;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.finance.model.ActivedBalance;

/**
 * Created by Administrator on 2017/9/15.
 */
public interface IFinanceTrader {

    /**
     * 日常财务，办结操作
     * @param YWLSH
     */
    void doFinanceCommon(String YWLSH);

    /**
     * 活期转定期到账通知
     * @param accChangeNotice
     * @return
     */
    String actived2FixedNotice(AccChangeNotice accChangeNotice);

    /**
     * 定期支取到账通知
     * @param accChangeNotice
     * @return
     */
    String fixedDrawNotice(AccChangeNotice accChangeNotice);

    /**
     * 活期转定期
     * @param id
     */
    void actived2Fixed(String id);

    /**
     * 定期支取
     * @param id
     */
    void fixedDraw(String id);

    /**
     * 获取定期余额
     * @param czy
     * @param acctNo
     * @param dqckbh
     * @param khyhmc
     * @return
     */
    FixedAccBalanceQueryOut getFixedBalance(String czy, String acctNo, String dqckbh, String khyhmc, boolean isSave);

    /**
     * 获取活期余额
     * @param id 活期账户id
     * @return
     */
    ActivedBalance getActivedBalance(String id);
}
