package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.BatchPaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试批量付款(BDC103)接口
 * Created by gxy on 17-7-4.
 */
public class TestBatchPayment {
    /**
     * 中国银行批量付款对公
     */
    public static BatchPaymentIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC103");
//        FileList fileList = CreateFileList.getBOCBatchPayment4Common(centerHeadIn.getSendSeqNo());
        FileList fileList = new FileList();
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "3",
                "02",
                new BigDecimal("10.00"),
                2,
                new BigDecimal("10.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("184208222254");
        batchPaymentIn.setDeAcctName("公积金中心测试账户一");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 中国银行批量付款对私
     */
    public static BatchPaymentIn getBOC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC103");
        FileList fileList = CreateFileList.getBOCBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "3",
                "02",
                new BigDecimal("2.00"),
                2,
                new BigDecimal("2.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("184208222254");
        batchPaymentIn.setDeAcctName("公积金中心测试账户一");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 农业银行批量付款对公
     */
    public static BatchPaymentIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC103");
        FileList fileList = CreateFileList.getABCBatchPayment4Common(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("2.00"),
                2,
                new BigDecimal("2.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("22401001040003017");
        batchPaymentIn.setDeAcctName("公积金对公测试账户一");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 农业银行批量付款对私
     */
    public static BatchPaymentIn getABC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC103");
        FileList fileList = CreateFileList.getABCBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("2.00"),
                2,
                new BigDecimal("2.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("22401001040003017");
        batchPaymentIn.setDeAcctName("公积金对公测试账户一");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 工商银行批量付款对公
     */
//    public static BatchPaymentIn getICBC4Common() {
//        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC103");
//        FileList fileList = CreateFileList.getICBCBatchPayment4Common(centerHeadIn.getSendSeqNo());
//        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
//                centerHeadIn,
//                "1",
//                "02",
//                new BigDecimal("2.00"),
//                2,
//                new BigDecimal("2.00"),
//                fileList
//        );
//        batchPaymentIn.setDeAcctNo("2406070529200005732");
//        batchPaymentIn.setDeAcctName("翁盯申漾专件覆巾害注谬植");
//        batchPaymentIn.setDeAcctClass("2");
//
//        return batchPaymentIn;
//    }

    /**
     * 工商银行批量付款对私
     */
    public static BatchPaymentIn getICBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC103");
        FileList fileList = CreateFileList.getICBCBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("30.00"),
                3,
                new BigDecimal("30.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("2406070529200005732");
        batchPaymentIn.setDeAcctName("翁盯申漾专件覆巾害注谬植");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 建行批量付款，对私
     */
    public static BatchPaymentIn getCCB4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC103");
        FileList fileList = CreateFileList.getCCBBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("6.00"),
                3,
                new BigDecimal("6.00"),
                fileList
        );
        batchPaymentIn.setBatchPrjNo("520830274");
        batchPaymentIn.setDeAcctNo("52001694036050006556");
        batchPaymentIn.setDeAcctName("毕节市住房公积金管理中心");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 交通银行批量付款对公
     */
    public static BatchPaymentIn getBOCOM4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC103");
        FileList fileList = CreateFileList.getBOCOMBatchPayment4Common(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("4.00"),
                2,
                new BigDecimal("4.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("310899991010008580716");
        batchPaymentIn.setDeAcctName("公积金中心结算账户67");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 交通银行批量付款对私
     */
    public static BatchPaymentIn getBOCOM4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC103");
        FileList fileList = CreateFileList.getBOCOMBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("2.00"),
                2,
                new BigDecimal("2.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("310899991010008580716");
        batchPaymentIn.setDeAcctName("公积金中心结算账户67");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 贵州银行批量付款对公
     */
    public static BatchPaymentIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC103");
        FileList fileList = CreateFileList.getGZBCBatchPayment4Common(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("30.00"),
                3,
                new BigDecimal("30.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("0707001900000045");
        batchPaymentIn.setDeAcctName("毕节公积金");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }

    /**
     * 贵州银行批量付款对私
     */
    public static BatchPaymentIn getGZBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC103");
        FileList fileList = CreateFileList.getGZBCBatchPayment4Private(centerHeadIn.getSendSeqNo());
        BatchPaymentIn batchPaymentIn = new BatchPaymentIn(
                centerHeadIn,
                "1",
                "02",
                new BigDecimal("20.00"),
                2,
                new BigDecimal("20.00"),
                fileList
        );
        batchPaymentIn.setDeAcctNo("0707001900000045");
        batchPaymentIn.setDeAcctName("毕节公积金");
        batchPaymentIn.setDeAcctClass("2");

        return batchPaymentIn;
    }
}
