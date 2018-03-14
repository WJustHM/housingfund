package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;

/**
 * Created by tanyi on 2017/10/1.
 */
public interface IVoucherAutoService {

    /**
     * 自动处理凭证，无业务流水号
     *
     * @param accChangeNotice
     */
    void checkBusiness(AccChangeNotice accChangeNotice);

    /**
     * 日常财务处理
     *
     * @param accChangeNotice
     */
    void transferAccounts(AccChangeNotice accChangeNotice);


    /**
     * 日常财务处理失败
     *
     * @param accChangeNotice
     */
    void transferAccountsFail(AccChangeNotice accChangeNotice);

    /**
     * 外部转入
     *
     * @param accChangeNotice
     */
    void addexternalTransferAccounts(AccChangeNotice accChangeNotice);

}
