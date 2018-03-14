package com.handge.housingfund.common.service.collection.service.trader;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;

import java.util.HashMap;

/**
 * Created by 凡 on 2017/8/10.
 */
public interface ICollectionTrader {

    /**
     * 汇补缴，发送委托收款请求
     * BDC102 单笔收款
     */
    public void sendRemittanceMSg(HashMap map,String code);

    /**
     * 错缴发送委托收款请求
     * BDC101 单笔付款
     */
    public void sendPayWrongMSg(HashMap map);

    /**
     * 住建部向业务系统发送账户变动通知，
     * 该接口用于向归集汇补缴业务发送入账通知
     */
    public void sendPaymentNotice(AccChangeNotice accChangeNotice,String id);

    /**
     *
     * 该接口用于向归集错缴业务发送入账通知
     */
    public void sendPayWrongNotice(AccChangeNotice accChangeNotice);
}
