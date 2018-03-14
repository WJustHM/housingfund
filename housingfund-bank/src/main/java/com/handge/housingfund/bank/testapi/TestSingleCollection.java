package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.SingleCollectionIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试单笔收款(BDC102)接口
 * Created by gxy on 17-7-4.
 */
public class TestSingleCollection {
    /**
     * 中国银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getBOCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "181208222212",
                "公积金中心测试账户四",
                "2",
                "182708222217",
                "公积金中心测试账户五",
                "2",
                "0",
                new BigDecimal("1.00"),
                "中国银行单笔收款(本行),对公",
                "中国银行单笔收款(本行),对公"
        );
        singleCollectionIn.setDeBankName("中国银行");
        singleCollectionIn.setDeChgNo("104362004010");

        return singleCollectionIn;
    }

    /**
     * 中国银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getBOCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "181208222212",
                "公积金中心测试账户四",
                "2",
                "176708222268",
                "王小一",
                "1",
                "0",
                new BigDecimal("1.00"),
                "中国银行单笔收款(本行),对私",
                "中国银行单笔收款(本行),对私"
        );
        singleCollectionIn.setDeBankName("中国银行");
        singleCollectionIn.setDeChgNo("104362004010");

        return singleCollectionIn;
    }

    /**
     * 中国银行单笔收款(他行),对公
     */
    public static SingleCollectionIn getBOCOther4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "3",
                "01",
                "181208222212",
                "公积金中心测试账户四",
                "2",
                "789541254121",
                "任意",
                "2",
                "1",
                new BigDecimal("1.00"),
                "中国银行单笔收款(他行),对公",
                "中国银行单笔收款(他行),对公"
        );
        singleCollectionIn.setDeBankName("工商银行");
        singleCollectionIn.setDeChgNo("104362004010");
        singleCollectionIn.setConAgrNo("102247000204");

        return singleCollectionIn;
    }

    /**
     * 中国银行单笔收款(他行),对私
     */
    public static SingleCollectionIn getBOCOther4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "2",
                "01",
                "181208222212",
                "公积金中心测试账户四",
                "1",
                "12541523652",
                "任意",
                "1",
                "1",
                new BigDecimal("1.00"),
                "",
                ""
        );
        singleCollectionIn.setDeBankName("工商银行");
        singleCollectionIn.setDeChgNo("104362004010");
        singleCollectionIn.setConAgrNo("102247000204");

        return singleCollectionIn;
    }

    /**
     * 农业银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getABCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                "22401001040000245",
                "四淳师盟士首踏珊军准菇承于慊官送",
                "2",
                "0",
                new BigDecimal("1.00"),
                "农业银行单笔收款(本行),对公",
                "农业银行单笔收款(本行),对公"
        );

        return singleCollectionIn;
    }

    /**
     * 农业银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getABCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                "44026100460000043",
                "石平平",
                "1",
                "0",
                new BigDecimal("1.00"),
                "农业银行单笔收款(本行),对私",
                "农业银行单笔收款(本行),对私"
        );

        return singleCollectionIn;
    }

    /**
     * 交通银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getBOCOMSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                "310899991010003089831",
                "对公测试01",
                "2",
                "0",
                new BigDecimal("1.00"),
                "交通银行单笔收款(本行),对公",
                "交通银行单笔收款(本行),对公"
        );

        return singleCollectionIn;
    }

    /**
     * 交通银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getBOCOMSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                "310972090000300338202",
                "李合宽",
                "1",
                "0",
                new BigDecimal("1.00"),
                "交通银行单笔收款(本行),对私",
                "交通银行单笔收款(本行),对私"
        );

        return singleCollectionIn;
    }

    /**
     * 工商银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getICBCSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "2",
                "2406075029200001081",
                "翁盯申漾专件覆巾害注谬植巾铀支害注酷",
                "2",
                "0",
                new BigDecimal("1.00"),
                "工商银行单笔收款(本行),对公",
                "工商银行单笔收款(本行),对公"
        );

        return singleCollectionIn;
    }

    /**
     * 工商银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getICBCSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "2",
                "6212262406000426368",
                "玷鑫",
                "1",
                "0",
                new BigDecimal("1.00"),
                "工商银行单笔收款(本行),对私",
                "工商银行单笔收款(本行),对私"
        );

        return singleCollectionIn;
    }

    /**
     * 建设银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getCCBSelf4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "52001694036050006556",
                "公司九七",
                "2",
//                "52001696636052500124",
//                "公司九七",
//                "6217007140007209373",
//                "王六八",
                "6222807102671087149",
                "吴一一",
                "2",
                "0",
                new BigDecimal("1.00"),
                "建设银行单笔收款(本行),对公",
                "建设银行单笔收款(本行),对公"
        );

        return singleCollectionIn;
    }

    /**
     * 建设银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getCCBSelf4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "52001694036050006556",
                "公司九七",
                "2",
//                "6217007140007209373",
//                "王六八",
                "6222807102671087149",
                "吴一一",
                "1",
                "0",
                new BigDecimal("15.00"),
                "建设银行单笔收款(本行),对私",
                "建设银行单笔收款(本行),对私"
        );

        return singleCollectionIn;
    }

    /**
     * 贵州银行单笔收款(本行),对公
     */
    public static SingleCollectionIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "0707001900000045",
                "毕节公积金",
                "2",
                "0707001500000047",
                "毕节公积金测试对公账户二",
                "2",
                "0",
                new BigDecimal("10000000.00"),
                "贵州银行单笔收款(本行),对公",
                "贵州银行单笔收款(本行),对公"
        );

        return singleCollectionIn;
    }

    /**
     * 贵州银行单笔收款(本行),对私
     */
    public static SingleCollectionIn getGZBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC102");
        SingleCollectionIn singleCollectionIn = new SingleCollectionIn(
                centerHeadIn,
                "1",
                "01",
                "0707001900000045",
                "毕节公积金",
                "2",
                "6214600180002921984",
                "余堃",
                "1",
                "0",
                new BigDecimal("10.00"),
                "贵州银行单笔收款(本行),对私",
                "贵州银行单笔收款(本行),对私"
        );

        return singleCollectionIn;
    }
}
