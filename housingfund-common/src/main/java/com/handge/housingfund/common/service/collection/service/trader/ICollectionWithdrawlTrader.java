package com.handge.housingfund.common.service.collection.service.trader;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/25.
 * 描述
 */
public interface ICollectionWithdrawlTrader {
    /**
     * 向提取业务发送到账通知
     * @param accChangeNotice
     */
    void sendWithdrawlNotice(AccChangeNotice accChangeNotice);

    /**
     * 向提取业务发送失败业务的到账通知
     * @param accChangeNotice
     */
    void sendFailedWithdrawlNotice(AccChangeNotice accChangeNotice);

    /**
     * 封装数据发往结算平台
     * @param YWLSH
     */
    public CommonReturn sendMsg(String YWLSH);
}
