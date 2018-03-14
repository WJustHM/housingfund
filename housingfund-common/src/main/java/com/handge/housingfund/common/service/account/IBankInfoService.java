package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.loan.model.DelList;

import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */
public interface IBankInfoService {

    /**
     * 通过银行名称或代码获取银行列表
     *
     * @param yhmc     银行名称
     * @param code     银行代码
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<BankInfoModel> getBankInfoList(String yhmc, String code, int pageNo, int pageSize);

    /**
     *
     * @param yhmc
     * @return
     */
    BankInfoModel getBankInfo(String yhmc);

    /**
     * 通过银行名称获取签约银行列表
     *
     * @param YHMC     银行名称
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<BankContactList> getBankContactList(String YHMC, int pageNo, int pageSize);

    /**
     * 添加签约银行
     *
     * @param bankContactReq 银行信息
     * @return
     */
    Success addBankContact(BankContactReq bankContactReq);

    /**
     * 获取签约银行详情
     *
     * @param id 签约银行ID
     * @return
     */
    BankContactRes getBankContactDetail(String id);

    /**
     * 修改签约银行
     *
     * @param id             签约银行ID
     * @param bankContactReq 银行信息
     * @return
     */
    Success putBankContact(String id, BankContactReq bankContactReq);

    /**
     * 删除签约银行
     *
     * @param delList
     * @return
     */
    List<Delres> delBankContact(DelList delList);

}
