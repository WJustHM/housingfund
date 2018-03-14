package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.model.DelList;

/**
 * Created by Liujuhao on 2017/9/1.
 */

public interface ILoanCommonApi<T> {

    /**
     * 删除申请
     *
     * @param YWWD 业务网点
     * @param CZY  操作员
     **/
    public T deleteApplication(TokenContext tokenContext,final String YWWD, final String CZY, final DelList dellist);


    /**
     * 多级审核测试
     * @param role
     * @param type
     * @param subModule
     * @param ywwd
     * @return
     */
    public T multipleReviewTest(String role, String type, String subModule, String ywwd);

    /**
     * 提交与撤回还款申请
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
    public T putEstateStatusSubmit(TokenContext tokenContext,final BatchSubmission body, final String status, final  String ywlx);

}
