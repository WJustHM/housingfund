package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPost;

public interface IRepaymentApi<T> {


    /**
     * 还款repaymentApplyReceipt
     *
     * @param DKZH  贷款账号
     * @param JKRXM 借款人姓名
     **/
    public T getHousingRepaymentApplyList(TokenContext tokenContext, final String DKZH, final String JKRXM, String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM);

    T getHousingRepaymentApplyListnew(TokenContext tokenContext, String dkzh, String jkrxm, String pageSize, String marker, String kssj, String jssj, String action,String YHDM);


    /**
     * 新增还款申请
     *
     * @param action (0保存 1提交)
     **/
    public T postRepayment(TokenContext tokenContext,final String action, final RepaymentApplyPrepaymentPost repaymentapplyprepaymentpost);


    /**
     * 修改还款申请
     *
     * @param YWLSH  业务流水号
     * @param action (0保存 1提交)
     **/
    public T putRepayment(TokenContext tokenContext,final String action,  final String YWLSH, final RepaymentApplyPrepaymentPost repaymentapplyprepaymentpost);


    /**
     * 删除还款申请
     **/
    public T deleteHousingRepaymentApply(TokenContext tokenContext,final DelList dellist);


    /**
     * 获取还款申请详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getPerpaymentDetails(TokenContext tokenContext,final String YWLSH);


    /**
     * 提交与撤回还款申请
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
//    @Deprecated
    public T putEstateStatusSubmit(TokenContext tokenContext,final BatchSubmission body, final String status, final  String ywlx);


    /**
     * 打印还款申请回执单
     *
     * @param YWLSH 业务流水号
     * @return
     */
    public T printRepaymentReceipt(TokenContext tokenContextfinal, String YWLSH);

    /**
     * 提前查询2
     */
    public T backRepaymentInfo(TokenContext tokenContext,final String hklx, final String DKZH, final String YDKKRQ, final String BCHKJE);

    /**
     * 办结操作
     */
    public T doAction(TokenContext tokenContext,final String YWLSH);


    /**
     * 还款失败操作
     */
    public T getFailedBuinessInfo(TokenContext tokenContext,String JKRXM, String DKZH, String stime, String etime, String pageSize, String page,String YHDM,String ZJHM,String HKYWLX,String YWWD);

    T getFailedBuinessInfonew(TokenContext tokenContext, String jkrxm, String dkzh, String stime, String etime, String pageSize, String marker, String action,String YHDM);


    /**
     * 重新划扣
     */
    public T putFailedBuinessSubmit(TokenContext tokenContext,String ID, String CZLX);

    /**
     *
     * 逾期修改查询
     * */
    public T getOverdueModification(TokenContext tokenContext,String ywlsh,String dkzh,String ydkkrq);

}