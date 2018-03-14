package com.handge.housingfund.common.service.account;


import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.RpcAuth;

/**
 * Created by tanyi on 2017/7/13.
 */
public interface AccountRpcService {
    /**
     * 注册用户
     *
     * @param rpcAuth 认证信息，所有信息应经过验证
     * @return
     */
    Msg registerAuth(RpcAuth rpcAuth);

    /**
     * 更新用户
     * @param id
     * @param rpcAuth
     * @return
     */
    Msg updateAuth(String id, RpcAuth rpcAuth);
}
