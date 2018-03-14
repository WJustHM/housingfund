package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.BatchCollectionIn;
import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试批量收款(BDC104)接口
 * Created by gxy on 17-7-4.
 */
public class TestBatchCollection {
    /**
     * 中国银行批量收款(本行),对公
     */
    public static BatchCollectionIn getBOCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC104");
        FileList fileList = CreateFileList.getBOCBatchCollectionSelf4Common(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "3",
                "01",
                2,
                new BigDecimal("4.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("181208222212");
        batchCollectionIn.setCrAcctName("公积金中心测试账户四");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 中国银行批量收款(本行),对私
     */
    public static BatchCollectionIn getBOCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC104");
        FileList fileList = CreateFileList.getBOCBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "3",
                "01",
                2,
                new BigDecimal("2.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("181208222212");
        batchCollectionIn.setCrAcctName("公积金中心测试账户四");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 中国银行批量收款(他行),对公
     */
    public static BatchCollectionIn getBOCOther4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC104");
        FileList fileList = CreateFileList.getBOCBatchCollectionOther4Common(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "2",
                "01",
                1,
                new BigDecimal("1.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("181208222212");
        batchCollectionIn.setCrAcctName("公积金中心测试账户四");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 中国银行批量收款(他行),对私
     */
    public static BatchCollectionIn getBOCOther4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC104");
        FileList fileList = CreateFileList.getBOCBatchCollectionOther4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "2",
                "01",
                1,
                new BigDecimal("1.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("181208222212");
        batchCollectionIn.setCrAcctName("公积金中心测试账户四");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 农行批量收款(本行),对公
     */
    public static BatchCollectionIn getABCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC104");
        FileList fileList = CreateFileList.getABCBatchCollectionSelf4Common(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                2,
                new BigDecimal("4.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("22401001040003017");
        batchCollectionIn.setCrAcctName("公积金对公测试账户一");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 农行批量收款(本行),对私
     */
    public static BatchCollectionIn getABCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC104");
        FileList fileList = CreateFileList.getABCBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                3,
                new BigDecimal("6.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("22401001040003017");
        batchCollectionIn.setCrAcctName("公积金对公测试账户一");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }
    /**
     * 工行批量收款(本行),对私
     */
    public static BatchCollectionIn getICBCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC104");
        FileList fileList = CreateFileList.getICBCBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                3,
                new BigDecimal("6.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("2406070529200005732");
        batchCollectionIn.setCrAcctName("翁盯申漾专件覆巾害注谬植");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 建设银行批量收款(本行),对公
     */
//    public static BatchCollectionIn getCCBSelf4Common() {
//        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC104");
//        FileList fileList = CreateFileList.getCCBBatchCollectionSelf4Common(centerHeadIn.getSendSeqNo());
//        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
//                centerHeadIn,
//                "1",
//                "01",
//                3,
//                new BigDecimal("6.00"),
//                fileList
//        );
//        batchCollectionIn.setCrAcctNo("52001694036050006556");
//        batchCollectionIn.setCrAcctName("毕节市住房公积金管理中心");
//        batchCollectionIn.setCrAcctClass("2");
//
//        return batchCollectionIn;
//    }

    /**
     * 建设银行批量收款(本行),对私
     */
    public static BatchCollectionIn getCCBSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC104");
        FileList fileList = CreateFileList.getCCBBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                3,
                new BigDecimal("15.00"),
                fileList
        );
        batchCollectionIn.setBatchPrjNo("520830275");
        batchCollectionIn.setCrAcctNo("52001694036050006556");
        batchCollectionIn.setCrAcctName("毕节市住房公积金管理中心");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }
    /**
     * 交通银行批量收款(本行),对公
     */
    public static BatchCollectionIn getBOCOMSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC104");
        FileList fileList = CreateFileList.getBOCOMBatchCollectionSelf4Common(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                2,
                new BigDecimal("4.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("310899991010008580716");
        batchCollectionIn.setCrAcctName("公积金中心结算账户67");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 交通银行批量收款(本行),对私
     */
    public static BatchCollectionIn getBOCOMSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC104");
        FileList fileList = CreateFileList.getBOCOMBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                2,
                new BigDecimal("3.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("310899991010008580716");
        batchCollectionIn.setCrAcctName("公积金中心结算账户67");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 贵州银行批量收款(本行),对公
     */
    public static BatchCollectionIn getGZBCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC104");
        FileList fileList = CreateFileList.getGZBCBatchCollectionSelf4Common(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                3,
                new BigDecimal("300000.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("0707001900000045");
        batchCollectionIn.setCrAcctName("毕节公积金");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }

    /**
     * 贵州银行批量收款(本行),对私
     */
    public static BatchCollectionIn getGZBCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC104");
        FileList fileList = CreateFileList.getGZBCBatchCollectionSelf4Private(centerHeadIn.getSendSeqNo());
        BatchCollectionIn batchCollectionIn = new BatchCollectionIn(
                centerHeadIn,
                "1",
                "01",
                2,
                new BigDecimal("550000.00"),
                fileList
        );
        batchCollectionIn.setCrAcctNo("0707001900000045");
        batchCollectionIn.setCrAcctName("毕节公积金");
        batchCollectionIn.setCrAcctClass("2");

        return batchCollectionIn;
    }
}
