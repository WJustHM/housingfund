package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;

/**
 * Created by tanyi on 2017/8/9.
 */

/**
 * 结算平台回调接口
 */
public interface IBankCallService {

    /**
     * 发放贷款回调接口
     *
     * @param accChangeNotice
     * @return
     */
    void putLoan(AccChangeNotice accChangeNotice);

    /**
     * 正常还款、
     *
     * @param accChangeNotice
     * @return
     */
    void putrepayment(AccChangeNotice accChangeNotice);

    /**
     *
     * 还款申请，回调结果查询 okokok
     * */
    void putLoanApply(AccChangeNotice accChangeNotice);

    /***
     *
     * 逾期自动扣划,贷款扣款
     * */
    void overdueAutomatic(AccChangeNotice accChangeNotice);

    /**
     * 正常还款
     *
     * @param
     * @return
     */
    void putrepaymentYwlsh(String ywlsh,String yhzhh);

    /**
     *
     * 还款申请，回调结果查询 okokok
     * */
    void putLoanApplyYwlsh(String ywlsh,String yhzhh);

    /***
     *
     * 逾期自动扣划,贷款扣款
     * */
    void overdueAutomaticYwlsh(String ywlsh,String yhzhh);


}
