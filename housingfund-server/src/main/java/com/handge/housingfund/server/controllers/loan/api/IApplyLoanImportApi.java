package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.loan.model.ApplyLoanImportPost;

public interface IApplyLoanImportApi<T> {


    /**
     * 导入贷款申请
     *
     * @param status 状态（0：导入申请表 1：导入委托申请表）
     **/
    public T postapplyLoanImport(final String status, final ApplyLoanImportPost applyloanimportpost);


}