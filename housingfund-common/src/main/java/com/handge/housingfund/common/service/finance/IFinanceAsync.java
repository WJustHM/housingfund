package com.handge.housingfund.common.service.finance;

public interface IFinanceAsync {

    /**
     * 获取对账结果，异步操作
     * @param date
     */
    public void getReconciliationAsync(String date);


}
