package com.handge.housingfund.server.controllers.account.api;

import com.handge.housingfund.common.service.account.model.BankContactReq;
import com.handge.housingfund.common.service.loan.model.DelList;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/6.
 */
public interface IBankInfoAPI {

    /**
     * 通过银行名称或代码获取银行列表
     *
     * @param yhmc
     * @param code
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getBankInfoList(String yhmc, String code, int pageNo, int pageSize);

    /**
     * 通过银行名称获取签约银行列表
     *
     * @param YHMC
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getBankContactList(String YHMC, int pageNo, int pageSize);

    /**
     * 添加签约银行
     *
     * @param bankContactReq
     * @return
     */
    Response addBankContact(BankContactReq bankContactReq);

    /**
     * 获取签约银行详情
     *
     * @param id
     * @return
     */
    Response getBankContactDetail(String id);

    /**
     * 修改签约银行
     *
     * @param id
     * @param bankContactReq
     * @return
     */
    Response putBankContact(String id, BankContactReq bankContactReq);

    /**
     * 删除签约银行
     *
     * @param delList
     * @return
     */
    Response delBankContact(DelList delList);
}
