package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionFileSelf;
import com.handge.housingfund.common.service.loan.model.BackBasicInfomation;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.CLoanHousingPersonInformationBasic;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import com.handge.housingfund.database.entities.StHousingOverdueRegistration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Funnyboy on 2017/9/29.
 */
public interface ICommCheckMethod {

    //正常
    HashMap<String,Object> firstCehck(BackBasicInfomation housingPersonInformationBasic, Date periodOfafterTime, CLoanHousingBusinessProcess housingBusinessProcess
            , List<StHousingBusinessDetails> gBusinessDetails, List<LoanDeductionFileSelf> list2, BigDecimal totalMoney, int totalNumber) throws Exception;
    //部分
    void partialRepaymentAccount(StHousingBusinessDetails chousingBusinessDetails);
    //结清
    void partialClearingAccount(StHousingBusinessDetails chousingBusinessDetails);
    //正常重扣划
    void NormalRepaymentReadjustmentDeduction(StHousingBusinessDetails chousingBusinessDetails);
    //逾期
    void OverdueDunksPlan(StHousingBusinessDetails chousingBusinessDetails, StHousingOverdueRegistration stHousingOverdueRegistration);
}
