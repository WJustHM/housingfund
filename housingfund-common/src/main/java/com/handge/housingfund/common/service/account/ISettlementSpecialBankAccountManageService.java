package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.SettlementSpecialBankAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/5.
 */
public interface ISettlementSpecialBankAccountManageService {
    /**
     * 获取银行专户列表
     * @param yhmc
     * @param yhzhhm
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<SettlementSpecialBankAccount> getSpecialAccountList(String yhmc, String yhzhhm, String status, int pageNo, int pageSize);

    /**
     * 获取银行专户详情
     * @param id
     * @return
     */
    SettlementSpecialBankAccount getSpecialAccountById(String id);

    /**
     * 新增银行专户
     * @param settlementSpecialBankAccount
     * @return
     */
    SettlementSpecialBankAccount addSpecialAccount(SettlementSpecialBankAccount settlementSpecialBankAccount);

    /**
     * 修改银行专户信息
     * @param id
     * @param settlementSpecialBankAccount
     * @return
     */
    boolean updateSpecialAccount(String id, SettlementSpecialBankAccount settlementSpecialBankAccount);

    /**
     * 删除银行专户
     * @param id
     * @return
     */
    boolean delSpecialAccount(String id);

    /**
     * 批量删除银行专户
     * @param ids
     * @return
     */
    boolean delSpecialAccounts(ArrayList<String> ids);

    /**
     * 银行专户销户
     * @param id
     * @return
     */
    void cancelSpecialAccount(String id);


    /**
     * 根据对手方银行获取本公积金中心的对应的银行账户
     * @param yhmc
     * @return
     */
    ArrayList<CenterAccountInfo> getSpecialAccount(String yhmc);

    /**
     * 根据专户号码获取节点号
     * @param zhhm
     * @return
     */
    CenterAccountInfo getSpecialAccountByZHHM(String zhhm);

    /**
     * 判断是否是中心的专户
     * @param zhhm
     * @return
     */
    boolean isSpecialAccountByZHHM(String zhhm);
}
