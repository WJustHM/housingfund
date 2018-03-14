package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.MultipleTestRes;

/**
 * Created by tanyi on 2017/9/8.
 */
public interface ICommonService {


    /**
     * 删除申请
     *
     * @param YWWD 业务网点
     * @param CZY  操作员
     **/
    public CommonResponses deleteApplication(TokenContext tokenContext,final String YWWD, final String CZY, final DelList body) ;


    /**
     * 仅测试使用
     * @param role
     * @param type
     * @param subModule
     * @param ywwd
     * @return
     */
    public MultipleTestRes multipleReviewTest(String role, String type, String subModule, String ywwd);


    /**
     * 提交与撤回还款申请
     * //TODO djj
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
    @Deprecated
    public CommonResponses batchSubmit(TokenContext tokenContext, final BatchSubmission body, final String status, final String ywlx);
}
