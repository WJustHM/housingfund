package com.handge.housingfund.server.controllers.account.api;

import com.handge.housingfund.common.service.account.model.SettlementSpecialBankAccount;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/5.
 */
public interface ISettlementSpecialBankAccountManageAPI {

    /**
     * 获取银行专户列表
     * @param yhmc
     * @param yhzhhm
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getSpecialAccountList(String yhmc,
                                          String yhzhhm,
                                          String status,
                                          int pageNo,
                                          int pageSize);

    /**
     * 获取专户详情
     * @param id
     * @return
     */
    Response getSpecialAccountById(String id);

    /**
     * 新增银行专户
     * @param settlementSpecialBankAccount
     * @return
     */
    Response addSpecialAccount(SettlementSpecialBankAccount settlementSpecialBankAccount);

    /**
     * 修改银行专户信息
     * @param id
     * @param settlementSpecialBankAccount
     * @return
     */
    Response updateSpecialAccount(String id, SettlementSpecialBankAccount settlementSpecialBankAccount);

    /**
     * 删除银行专户
     * @param id
     * @return
     */
    Response delSpecialAccount(String id);

    /**
     * 批量删除银行专户
     * @param ids
     * @return
     */
    Response delSpecialAccounts(ArrayList<String> ids);

    /**
     * 银行专户销户
     * @param id
     * @return
     */
    Response cancelSpecialAccount(String id);

    /**
     * 根据对手方银行获取本公积金中心的对应的银行账户
     * @param yhmc
     * @return
     */
    Response getSpecialAccount(String yhmc);

}
