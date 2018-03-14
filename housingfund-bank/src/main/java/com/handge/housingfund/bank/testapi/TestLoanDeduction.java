package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试贷款扣款(BDC105)接口
 * Created by gxy on 17-7-4.
 */
public class TestLoanDeduction {
    /**
     * 中行贷款扣款，对私
     */
    public static LoanDeductionIn getBOC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC105");
        FileList fileList = CreateFileList.getBOCLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "3",
                "181208222212",
                "公积金中心测试账户四",
                "2",
                2,
                new BigDecimal("10.00"),
                fileList
        );

        return loanDeductionIn;
    }

    /**
     * 农行贷款扣款，对私
     */
    public static LoanDeductionIn getABC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC105");
        FileList fileList = CreateFileList.getABCLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "1",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                3,
                new BigDecimal("6.00"),
                fileList
        );

        return loanDeductionIn;
    }

    /**
     * 工行贷款扣款，对私
     */
    public static LoanDeductionIn getICBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC105");
        FileList fileList = CreateFileList.getICBCLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "1",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "2",
                3,
                new BigDecimal("6.00"),
                fileList
        );

        return loanDeductionIn;
    }

    /**
     * 建行贷款扣款，对私
     */
    public static LoanDeductionIn getCCB4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC105");
        FileList fileList = CreateFileList.getCCBLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "1",
                "52001694036050006556",
                "公司九七",
                "2",
                3,
                new BigDecimal("10.00"),
                fileList
        );
        loanDeductionIn.setBatchPrjNo("520830275");

        return loanDeductionIn;
    }

    /**
     * 交行贷款扣款，对私
     */
    public static LoanDeductionIn getBOCOM4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC105");
        FileList fileList = CreateFileList.getBOCOMLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "1",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                2,
                new BigDecimal("10.00"),
                fileList
        );

        return loanDeductionIn;
    }

    /**
     * 贵州银行贷款扣款，对私
     */
    public static LoanDeductionIn getGZBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC105");
        FileList fileList = CreateFileList.getGZBCLoanDeduction4Private(centerHeadIn.getSendSeqNo());
        LoanDeductionIn loanDeductionIn = new LoanDeductionIn(
                centerHeadIn,
                "1",
                "0707001900000045",
                "毕节公积金",
                "2",
                2,
                new BigDecimal("10.00"),
                fileList
        );

        return loanDeductionIn;
    }
}
