package com.handge.housingfund.server.controllers.loan.api;

public interface ILoanMonitorApi<T> {


    /**
     * 监控发放贷款、还款自动扣款、还款时是否扣款成功并进行后续操作
     *
     * @param DKZH 贷款账号
     **/
    public T getloanMonitor(final String DKZH);


}