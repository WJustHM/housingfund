package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.LoginCA;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.ResetPwd;
import com.handge.housingfund.common.service.account.model.RpcAuth;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by tanyi on 2017/7/11.
 */
public interface IAccountService {
    /**
     * 生成TOKEN
     *
     * @param userid 用户ID
     * @return
     */
    String getToken(String userid, int type);

    /**
     * 验证CA
     *
     * @param loginCA 登录信息
     * @return
     */
    RpcAuth verifyCa(LoginCA loginCA);

    /**
     * 验证账户密码
     *
     * @param username 用户名
     * @param pwd      密码
     * @return
     */
    RpcAuth verifPwd(String username, String pwd) throws InvalidKeySpecException, NoSuchAlgorithmException;

    /**
     * 重置密码发送邮件
     *
     * @param email 邮件地址
     * @return
     */
    Msg resetPwdSendEmail(String email) throws UnsupportedEncodingException;

    /**
     * 重置密码
     *
     * @param auth_id  认证ID
     * @param resetPwd 新密码信息
     * @return
     */
    Msg resetPwd(String auth_id, ResetPwd resetPwd);


}
