package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;

public interface ILoanAccountApi<T> {


    /**
     * 贷款账户查询
     *
     * @param DKZH   贷款账号
     * @param JKRXM  借款人姓名
     * @param status 状态（0:正常 1:逾期 2:呆账 3:转出 4:结清）
     **/
    public T getHousingFundAccount(final String DKZH, final String JKRXM, final String status, final String DKFXDJ, final String pageSize, final String page,
                                   final String YWWD, final String SWTYH, final String ZJHM);

    /**
     * 贷款账户业务清单
     *
     * @param DKZH 贷款账号
     **/
    public T getHousingfundAccountBusinessList(final String DKZH, final String pageSize, final String page);


    /**
     * 贷款账户逾期信息
     *
     * @param DKZH 贷款账号
     **/
    public T getHousingfundAccountOverdueList(final String DKZH, final String pageSize, final String page);


    /**
     * 提前还款申请书(待定)
     *
     * @param DKZH 贷款账号
     **/
    public T postOverdueApply(final String DKZH);


    /**
     * 还款记录
     *
     * @param DKZH 贷款账号
     **/
    public T getHousingRecordList(final String DKZH, final String pageSize, final String page);


    /**
     * 打印还款记录
     *
     * @param DKZH 贷款账号
     **/
    public T getHousingRecordPintList(final String DKZH);


    /**
     * 还款计划
     *
     * @param DKZH 贷款账号
     **/
    public T getHousingfundPlanList(final String DKZH);


    /**
     * 贷款账户详情
     *
     * @param DKZH 贷款账户
     **/
    public T getHousingDkzh(final String DKZH);


    /**
     * 转为呆账
     *
     * @param DKZH   贷款账号
     **/
    public T putbadDebts(final String DKZH);

    /**
     * 根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号
     * @param DKZH
     * @return
     */
    public T getLoanAccountBorrowerInfo(final String DKZH);

    /**
     * 贷款风险等级修改
     * @param DKZH
     * @param DKFXDJ
     * @return
     */
    public T putLoanRiskAssessment(final String DKZH, final String DKFXDJ);
    /**
     * 贷款账号结清pdf打印
     * @param DKZH
     * @return
     */
    public T headSquareReceipt(final String DKZH);
    /**
     * 还款计划pdf打印
     * @param DKZH
     * @return
     */
    public T  HeadHousingfundPlan(TokenContext tokenContext,String DKZH, String HKRQS, String HKRQE);
}