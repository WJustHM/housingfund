package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.SignContractPost;

public interface ISignContractApi<T> {


    /**
     * 贷款合同（待定）
     *
     * @param status 操作（0保存，1提交）
     **/
    public T postPurchaseContract(TokenContext tokenContext, final String status ,final String YWLSH, final SignContractPost signcontractpost);


}