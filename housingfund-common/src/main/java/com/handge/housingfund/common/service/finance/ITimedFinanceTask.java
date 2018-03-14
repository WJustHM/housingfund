package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.finance.model.TimedTaskInfo;

import java.math.BigDecimal;

/**
 * Created by xuefei_wang on 17-9-27.
 */
public interface ITimedFinanceTask {

    /**
     * 每月月月初触发，计算职工公积金月利息，并生成结算凭证
     *
     * @return {@link TimedTaskInfo}
     * @author xuefei_wang
     */
    public void checkoutMonthInterest();


    /**
     * 每年6月30日　23：59　触发
     * 日常财务 :结算截止到6月30日，金额为中心所有缴存职工截止到6月30日时的金额总和．生成结算凭证
     *
     * @return {@link TimedTaskInfo}
     * @author xuefei_wang
     */
    public TimedTaskInfo checkoutYearInterest(BigDecimal sumYearInterest);


    /**
     * 每年1月1日　05：00　触发
     * 日常财务 :　计算“增值收益”科目的余额．生成结算凭证
     * @return {@link TimedTaskInfo}
     * @author xuefei_wang
     */
    public TimedTaskInfo checkoutBenefits();


     /**
     * 每月月初03：00触发，结账操作，生成科目余额表
     */
    void checkVoucher();

    /**
     * 每月月初04：00触发，在结账之前触发，期末业务收入结转/期末业务支出结转
     */
    void setBusinessEndIncomeAndExpenditure();

    /**
     * 新增会计期间
     * 每年1月1日,00：00触发
     */
    void addAccountPeriod();

    /**
     * 新增住房公积金银行存款
     * 每月月末23:59分触发
     */
    void addHousingfundBankBalance();

    /**
     * 更新定期余额
     * 每天01:00触发
     */
    void updateFixedBalance();

    /**
     * 计提定期利息收入
     * 每月月末触发
     */
    void fixedIntIncome();
}
