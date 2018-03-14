package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.model.*;


/**
 * Created by Liujuhao on 2017/8/9.
 */
public interface ILoanAccountService {

    /**
     * 贷款账户查询
     *
     * @param DKZH   贷款账号
     * @param JKRXM  借款人姓名
     * @param status 状态（0:正常 1:逾期 2:呆账 3:转出 4:结清）
     **/
    public PageRes<HousingFundAccountGetRes> getHousingFundAccount(final String DKZH, final String JKRXM, final String status, final String DKFXDJ, final String pageSize, final String page,
                                                                   final String YWWD, final String SWTYH, final String ZJHM);

    /**
     * 贷款账户业务清单
     *
     * @param DKZH 贷款账号
     **/
    public PageRes<HousingfundAccountBusinessListGetInformation> getHousingfundAccountBusinessList(final String DKZH, final String pageSize, final String page);

    /**
     * 贷款账户逾期信息
     *
     * @param DKZH 贷款账号
     **/
    public PageRes<HousingfundAccountOverdueListGetInformation> getHousingfundAccountOverdueList(final String DKZH, final String pageSize, final String page);

    /**
     * 提前还款申请书(待定)
     *
     * @param DKZH 贷款账号
     **/
    public RepaymentApplyOverdueGet postOverdueApply(final String DKZH);


    /**
     * 还款记录
     *
     * @param DKZH 贷款账号
     **/
    public PageRes<HousingRecordListGetInformation> getHousingRecordList(final String DKZH, final String pageSize, final String page);

    /**
     * 打印还款记录
     *
     * @param DKZH 贷款账号
     **/
    public HousingRecordPrintListGet getHousingRecordPintList(final String DKZH);

    /**
     * 还款计划
     *
     * @param DKZH 贷款账号
     **/
    public HousingfundAccountPlanGet getHousingfundPlanList(final String DKZH);

    /**
     * 贷款账户详情
     *
     * @param DKZH 贷款账户
     **/
    public HousingDkzhGet getHousingDkzh(final String DKZH);

    /**
     * 根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号
     * @param DKZH
     * @return
     */
    public LoanAccountBorrowerInfo getLoanAccountBorrowerInfo(final String DKZH);

    /**
     * 单独修改状态（转为呆账）
     *
     * @param DKZH   贷款账号
     * @param status 状态（0：呆账）
     **/
    public CommonResponses putBadDebts(final String DKZH, final String status);


    /**
     * 风险评估（修改等级）
     * @param DKZH
     * @param rank
     * @return
     */
    public CommonResponses putRiskAssessment(final String DKZH, final String rank);
    /**
     * 贷款账号
     * @param DKZH
     * @return
     */
    public CommonResponses getSquareReceipt(final String DKZH);
    /**
     * 还款计划pdf
     * @param DKZH
     * @return
     */
    public CommonResponses getHousingfundPlan(TokenContext tokenContext,final String DKZH, String HKRQS, String HKRQE);
    /**
     * 还款记录excel
     * @param DKZH
     * @return
     */
    public PaymentHistoryDataRes getHousingRecordData(TokenContext tokenContext,String DKZH, String HKRQS, String HKRQE);
}
