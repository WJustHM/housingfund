package com.handge.housingfund.server.controllers.account.api;

import com.handge.housingfund.common.service.account.model.Login;
import com.handge.housingfund.common.service.account.model.LoginCA;
import com.handge.housingfund.common.service.account.model.ResetPwd;

/**
 * Created by tanyi on 2017/7/10.
 */
public interface IAccountAPI<T> {
    /**
     * 账号密码登录
     *
     * @param login 登录信息
     * @return
     */
    T login(Login login);

    /**
     * CA登录
     *
     * @param loginCA CA信息
     * @return
     */
    T loginByCa(LoginCA loginCA);

    /**
     * 获取系统当前时间
     *
     * @return
     */
    T curtime();

    /**
     * 检查token
     *
     * @param token
     * @return
     */
    T checkToken(String token);

    /**
     * 重置密码发送邮件
     *
     * @param email 账户邮件地址
     * @return
     */
    T resetPwdSendEmail(String email);

    /**
     * 重置密码
     *
     * @param resetPwd 密码信息
     * @return
     */
    T resetPwd(ResetPwd resetPwd);


}
