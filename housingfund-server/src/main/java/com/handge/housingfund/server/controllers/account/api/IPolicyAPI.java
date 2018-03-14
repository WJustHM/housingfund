package com.handge.housingfund.server.controllers.account.api;

/**
 * Created by 向超 on 2017/9/20.
 */
public interface IPolicyAPI<T> {

    /**
     *
     * @param id
     * @param xgz 修改值
     * @param sxrq 生效日期
     * @return
     */
    public T updatePolicyInfo(String id, String xgz, String sxrq);


    public T getPolicy();
}
