package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryIn;

import java.math.BigDecimal;

/**
 * Created by Funnyboy on 2017/9/17.
 */
public interface IClearingBank {
    /**
     * 单笔收款(结清，部分)
     */
     <T> T sendSingleMessage(String ywlsh, String hkzh, String jkrxm,String yhmc, BigDecimal totalMoney) throws Exception;

    /**
     * 单笔收款(逾期还款)
     */
    <T> T sendOverdueMessage(String ywlsh, String hkzh, String jkrxm,String yhmc, BigDecimal totalMoney) throws Exception;

    /**
     * 贷款扣款
     */
    <T> T sendLoaneMessage(String ywlsh,String yhmc, BigDecimal totalMoney,int totalNubmer) throws Exception;

    /**
     * 回查接口
     */
    TransactionResultQueryIn transResultQuery(String ywlsh,String yhmc) throws Exception;

}
