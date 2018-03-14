package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentGet;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentPut;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentResponse;

public  interface ILoanService {

    /**
     * 发放贷款信息
     *
     * @param YWLSH 业务流水号
     **/
    public ConfirmPaymentResponse getConfirmLoans(TokenContext tokenContext, final String YWLSH);


    /**
     * 确认放款
     *
     * @param YWLSH 业务流水号
     **/
    public CommonResponses putConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH, final ConfirmPaymentPut body);


    /**
     * 发放贷款详情
     *
     * @param YWLSH 业务流水号
     **/
    public ConfirmPaymentGet getConfirmLoansSubmit(TokenContext tokenContext,final String YWLSH);
}
