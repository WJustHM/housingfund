package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.model.unit.HeadUnitAcctActionRes;
import com.handge.housingfund.common.service.collection.model.unit.HeadUnitAcctBasicRes;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.ReceiptReturn;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlRecordsReturn;
import com.handge.housingfund.common.service.finance.model.VoucherManager;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyi on 2017/8/9.
 */
public interface IPdfService {

    /**
     * 生成审批表
     *
     * @param response
     * @return
     */
    String getReviewTable(GetApplicantResponse response);

    /**
     * 审批action
     *
     * @param id   原审批表文件ID
     * @param reviews 审核记录
     * @return
     */
    String putReviewTable(String id, List<Review> reviews);

    /**
     * 生成单位补缴通知单
     * @param result 通知单数据
     * @return id
     */
    String getUnitPayBackNoticePdf(HeadUnitPayBackNoticeRes result);

    /**
     * 生成单位补缴回执单
     * @param result 通知单数据
     * @return id
     */
    String getUnitPaybackReceipt(HeadUnitPayBackReceiptRes result);

    /**
     * 生成单位缓缴回执单
     * @param result 缓缴回执单数据
     * @return id
     */
    String getUnitPayholdReceiptPdf(HeadUnitPayHoldReceiptRes result);

    /**
     * 生成单位汇缴通知单
     * @param result 汇缴通知单数据
     * @return id
     */
    String getUnitRemittanceNoticePdf(UnitRemittanceNotice result);

    /**
     * 生成单位缴存比例回执单
     * @param result 比例数据
     * @return id
     */
    String getUnitDepositRatioReceiptPdf(HeadUnitDepositRatioReceiptRes result);

    /**
     * 毕节市住房公积金单位封存回执单
     * @param result 封存回执单数据
     * @return id
     */
    String getUnitAcctSealReceiptPdf(HeadUnitAcctActionRes result);

    /**
     * 毕节市住房公积金单位启封回执单
     * @param result 启封回执单数据
     * @return id
     */
    String getUnitAcctUnsealReceiptPdf(HeadUnitAcctActionRes result);

    /**
     * 毕节市住房公积金单位销户回执单
     * @param result 销户回执单数据
     * @return id
     */
    String getUnitAcctDropReceiptPdf(HeadUnitAcctActionRes result);

    /**
     * 毕节市住房公积金单位缴存登记或变更回执单
     * @param result 回执单数据
     * @return id
     */
    String getUnitAcctsSetReceiptPdf(HeadUnitAcctBasicRes result,String code);

    /**
     * 个人账户设立回执单
     * @param result 设立更数据
     * @return id
     */
    String getIndiAcctSetReceiptPdf(HeadIndiAcctSetRes result);
    /**
     * 个人账户变更回执单
     * @param result 变更数据
     * @return id
     */
    String getIndiAcctAlterReceiptPdf(HeadAcctAlterRes result);
    /**
     * 个人账户Action（封存、启封、冻结、解冻、托管）回执单
     * @param result 数据
     * @return id
     */
    String getIndiAcctActionPdf(HeadIndiAcctActionRes result,String code);
    /**
     * 提取回执单
     * @param result 数据
     * @return id
     */
    String getWithdrawlReceiptPdf(ReceiptReturn result);
    /**
     * 单位催缴
     * @param result 数据
     * @return id
     */
    String getUnitPayCallPdf(HeadUnitPayCallReceiptRes result);

    /**
     * 单位错缴
     * @param result 数据
     * @return id
     */
    String getUnitPayWrongPdf(HeadUnitPayWrongReceiptRes result);
    /**
     * 单位汇缴回执单
     * @param result 数据
     * @return id
     */
    String getUnitRemittanceReceiptPdf(HeadUnitRemittanceReceiptRes result);

    /**
     * 个人基数调整回执单
     * @param result 数据
     * @return id
     */
    String getPersonRadixPdf(HeadPersonRadixRes result);
    /**
     * 清册确认单 01:未办结；02：已办结
     * @param result 数据
     * @return id
     */
    String getRemittanceInventoryReceiptPdf(HeadRemittanceInventoryRes result);
    /**
     * 提取记录
     * @param result 数据
     * @return id
     */
    String getWithdrawlsRecords(WithdrawlRecordsReturn result);
    /**
     * 生成凭证
     * @param result 数据
     * @return id
     */
    String getVoucherGetDetailPdf(VoucherManager result);

    /**
     * 房开回执单
     * @param result 数据
     * @param code 房开新建72，变更76
     * @return id
     */
    String getApplyHousingCompanyReceipt(ApplyHousingCompanyReceipt result,String code);
    /**
     * 还款回执单
     * @param result 数据
     * @param hklx 类型（0逾期还款，1提前部分还款，2提前结清还款）
     * @return id
     */
    String getPrintRepaymentReceipt(RepaymentApplyReceipt result, String hklx);

    /**
     * 楼盘回执单
     * @param estateIdGet 数据
     * @param code 类型（01 新增楼盘回执单 02变更楼盘回执单）
     * @return
     */
    String getEstateProjectAlterReceipt(EstateIdGet estateIdGet,String code);
    /**
     * 借款合同
     * @param loanContractPDFResponse 数据
     * @return
     */
    String getContractRecepit(LoanContractPDFResponse loanContractPDFResponse);

    /**
     * 合并转移回执单
     */
    String getMergeTransferReceiptPdf(TransferListGet transferListGet);

    /**
     * 合同变更回执单
     */
    String getContractAlterPdf(HeadLoanContract result);
    /**
     * 合并多个凭证pdf
     */
    String getMergerVoucherMorePdf(ArrayList<VoucherManager> VoucherInfoArray);
    /**
     * 个人缴存明细
     */
    String getPersonDepositPdf(IndiAcctDepositDetails depositDetails);
    /**
     * 结清回执单
     */
    String getSquareReceiptPdf(AccountSquarepdfInformation accountSquarepdfInformation);
    /**
     * 还款计划pdf
     */
    String getHousingfundPlanPdf(HousingfundAccountPlanGet planGet);

    /**
     * 异地贷款缴存证明pdf
     */
    String getDiffTerritoryLoadProvePdf(ForeignLoanProof foreignLoanProof);

    /**
     * 委托扣划协议PDF
     */
    String getEntrustDeductPdf(EntrustDeductInfos entrustDeductInfos);
}
