package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.model.GetCommonHistory;
import com.handge.housingfund.common.service.loan.model.GetLoanContractRes;
import com.handge.housingfund.common.service.loan.model.GetLoanRecordDetailsResponses;
import com.handge.housingfund.common.service.loan.model.LoanRecord;

/**
 * Created by Liujuhao on 2017/8/9.
 */
public interface ILoanRecordService {

    /**
     * 贷款详情页面
     *
     * @param DKZH 贷款账号
     **/
    public GetLoanRecordDetailsResponses getLoanDetails(final String DKZH);

    /**
     * 贷款记录列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public PageRes<LoanRecord> listLoanRecord(final String JKRXM, final String JKRZJHM, final String DKYT, final String pageSize, final String page, final String Module,
                                              final String YWWD, final String SWTYH, final String DKZH, final String HTJE);

    /**
     * 签订贷款合同-变更
     * @param DKZH 贷款账号
     */
//    public void AlterLoanContract(final String DKZH, final );


    /**
     * 获取贷款账号相关的贷款合同信息
     *
     * @param DKZH
     */
    public GetLoanContractRes getLoanContract(final String DKZH);


    /**
     * 查看审批表
     */

    public void GetLoanReviewPDF();

    /**
     * 贷款业务历史记录（审核流程）
     * @param DKZH
     * @param pageSize
     * @param pageNo
     * @return
     */
    public PageRes<GetCommonHistory> getLoanRecordHistory(String DKZH, String pageSize, String pageNo);
}
