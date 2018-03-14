package com.handge.housingfund.server.controllers.loan.api;

public interface ILoanRecordApi<T> {


    /**
     * 贷款详情页面
     *
     * @param DKZH 贷款账号
     **/
    public T getLoanRecordDetails(final String DKZH);


    /**
     * 贷款记录列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public T getLoanRecordList(final String JKRXM, final String JKRZJHM, final String DKYT, final String pageSize, final String page, final String Module,
                               final String YWWD, final String SWTYH, final String DKZH, final String HTJE);


    /**
     * 贷款业务历史记录表（单条业务的审核流程）
     *
     * @param DKZH
     * @param pageSize
     * @param pageNo
     * @return
     */
    public T getLoanRecordHistory(final String DKZH, final String pageSize, final String pageNo);

}