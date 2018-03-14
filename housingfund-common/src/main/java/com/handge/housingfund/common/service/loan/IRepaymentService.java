package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.model.*;

/**
 * Created by Funnyboy on 2017/8/9.
 */
public interface IRepaymentService {
    /**
     * 还款repaymentApplyReceipt
     * //TODO completed over
     *
     * @param DKZH  贷款账号
     * @param JKRXM 借款人姓名
     **/
    public PageRes<HousingRepaymentApplyListGetRes> getHousingRepaymentApplyList(TokenContext tokenContext, final String DKZH, final String JKRXM, String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM);

    /**
     * 新增还款申请
     * //TODO completed over
     *
     * @param action (0保存 1提交)
     **/
    public CommonResponses postRepayment(TokenContext tokenContext,final String action,  final RepaymentApplyPrepaymentPost body);

    /**
     * 修改还款申请
     * //TODO djj
     *
     * @param action (0保存 1提交)
     * @param YWLSH  业务流水号
     **/
    public CommonResponses putRepayment(TokenContext tokenContext,final String action,  final String YWLSH, final RepaymentApplyPrepaymentPost body);


    /**
     * 获取还款申请详情
     * //TODO djj
     *
     * @param YWLSH 业务流水号
     **/
    public RepaymentApplyPrepaymentPost getPerpaymentDetails(TokenContext tokenContext,final String YWLSH);

    /**
     * 提交与撤回
     * //TODO djj
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
//    @Deprecated
    public CommonResponses batchSubmit(TokenContext tokenContext,final BatchSubmission body, final String status,  final String ywlx);

    /**
     * 还款审核列表
     * //TODO completed over
     *
     * @param status 状态（0：待处理 1：已处理）
     * @param JKRXM  借款人姓名
     * @param stime  开始时间
     * @param etime  结束时间
     **/
    public HousingRepamentApplyRangeGet getRepaymentReview(TokenContext tokenContext,final String status, final String JKRXM, final String stime, final String etime);

    /**
     * 打印还款申请回执 djjtest
     * //TODO completed over
     *
     * @param YWLSH 业务流水号
     * @return
     */
    public CommonResponses printRepaymentReceipt(TokenContext tokenContext, String YWLSH);


    /**
     * 提前还款2
     *  //TODO completed over
     */
    public RepaymentApplicationInAdvanceGet backRepaymentInfo(TokenContext tokenContext,String hklx, String DKZH, String YDKKRQ, String BCHKJE,String HDLX,String gjjzhlx);

    /**
     * 办结操作
     * */
    public void doAction(String YWLSH);


    /**
     * 扣款业务失败记录
     * */
    public PageRes<FailedBuinessInfo> getFailedBuinessInfo(TokenContext tokenContext,String JKRXM,String DKZH,String stime,String etime,String pageSize, String page,String YHDM,String ZJHM,String HKYWLX,String YWWD);

    /**
     * 重新划扣,修改状态
     * @param CZLX  0实际扣款成功，线下处理  1重新划扣
     * */
    public CommonResponses putFailedBuinessSubmit(TokenContext tokenContext,String ID,String CZLX);


    /**
     *
     *
     * */
    public GetOverdueModification getOverdueModification(TokenContext tokenContext,String ywlsh,String dkzh,String ydkkrq);

    public PageResNew<FailedBuinessInfo> getFailedBuinessInfonew(TokenContext tokenContext, String jkrxm, String dkzh, String stime, String etime, String pageSize, String marker, String action,String YHDM);

    public PageResNew<HousingRepaymentApplyListGetRes> getHousingRepaymentApplyListnew(TokenContext tokenContext, String dkzh, String jkrxm, String pageSize, String marker, String kssj, String jssj, String action,String YHDM);
}
