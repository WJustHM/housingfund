package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.LoanWithdrawl;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Funnyboy on 2017/9/29.
 */
public interface ILoanTaskService {

    /**
     * 还款计划 djj
     */
    void rehuankuanjihua();


    /**
     * 正常还款，定时任务 djj
     */
    void repayment(String yhdm, Date kkyf,Date overTime);


    /**
     * 正常还款入账失败余额不足转逾期
     */
    void overdueRepaymentChange();


    /**
     * 入账失败余额不足处理,重扣划 --------------
     */
    void debitSend();


    /**
     * 剩余期数  djj
     */
    void remainingPeriod();


    /**
     * 还款申请定时发送 djj
     */
    void overdueRepaymenTiming();

    /**
     * 自动逾期记录扣划 ------------------
     */
    void overdueAutomatic(String yhdm) throws Exception;

    /**
     * 公积金部分提取,结清还贷 djj
     */
    String providentFundWithdrawal(String dkzh, BigDecimal bchkje, String hklx, String czy, String shy,String ywlsh, String pch);

     CommonReturn addWithdrawlrepament(LoanWithdrawl loanWithdrawl);

     void SMSRepament();
}
