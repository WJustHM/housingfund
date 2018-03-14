package com.handge.housingfund.server.controllers.loan.api;

public interface IHistoryRecordApi<T> {


    /**
     * 历史业务记录
     *
     * @param ZZJGDM 组织机构代码
     *
     **/
    public T getCompanyHistory(final String ZZJGDM);


}