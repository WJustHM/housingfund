package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试单笔付款(BDC101)接口
 * Created by gxy on 17-7-3.
 */
public class TestSinglePayment {
    /**
     * 中国银行单笔付款对公
     */
    public static SinglePaymentIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "184208222254",
                "公积金中心测试账户一",
                "2",
                new BigDecimal("1.00"),
                "182708222217",
                "公积金中心测试账户五",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        singlePaymentIn.setCrChgNo("104100000004");
        return singlePaymentIn;
    }

    /**
     * 中国银行单笔付款对私
     */
    public static SinglePaymentIn getBOC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "184208222254",
                "公积金中心测试账户一",
                "2",
                new BigDecimal("1.00"),
                "176708222268",
                "王小一",
                "1",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        singlePaymentIn.setCrChgNo("104362004010");
        return singlePaymentIn;
    }

    /**
     * 建设银行单笔付款对公
     */
    public static SinglePaymentIn getCCB4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "52001694036050006556",
                "公司九七",
                "2",
                new BigDecimal("1.00"),
                "52001696636052500124",
                "公司九七",
//                "6217007140007209373",
//                "王六八",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 建设银行单笔付款对私
     */
    public static SinglePaymentIn getCCB4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "52001694036050006556",
                "公司九七",
                "2",
                new BigDecimal("1.00"),
                "52001696636052500124",
                "公司九七",
//                "6217007140007209373",
//                "王六八",
                "1",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 农业银行单笔付款对公
     */
    public static SinglePaymentIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                new BigDecimal("1.00"),
                "22401001040000245",
                "四淳师盟士首踏珊军准菇承于慊官送",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 农业银行单笔付款对私
     */
    public static SinglePaymentIn getABC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                new BigDecimal("1.00"),
                "44026100460000043",
                "石平平",
                "1",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 工商银行单笔付款对公
     */
    public static SinglePaymentIn getICBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "2",
                new BigDecimal("1.00"),
                "2406075029200001081",
                "翁盯申漾专件覆巾害注谬植巾铀支害注酷",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 工商银行单笔付款对私
     */
    public static SinglePaymentIn getICBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "1",
                new BigDecimal("1.00"),
                "2406075029200001081",
                "翁盯申漾专件覆巾害注谬植巾铀支害注酷",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 交通银行单笔付款对公
     */
    public static SinglePaymentIn getBOCOM4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                new BigDecimal("1.00"),
                "310899991010003089831",
                "对公测试01",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 交通银行单笔付款对私
     */
    public static SinglePaymentIn getBOCOM4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                new BigDecimal("1.00"),
                "310972090000300338202",
                "李合宽",
                "1",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }

    /**
     * 贵州银行单笔付款对公
     */
    public static SinglePaymentIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "0707001900000045",
                "毕节公积金",
                "2",
                new BigDecimal("1.00"),
                "0707001700000046",
                "毕节公积金测试对公账户一",
                "2",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }
    /**
     * 贵州银行单笔付款对私
     */
    public static SinglePaymentIn getGZBC4Private() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC101");
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                centerHeadIn,
                "1",
                "02",
                "0707001900000045",
                "毕节公积金",
                "2",
                new BigDecimal("1.00"),
                "6214600180002921976",
                "余辛",
                "1",
                "0",
                new BigDecimal("1.00"),
                CenterHead.random,
                "部分提取,金额1",
                "部分提取,金额1"
        );
        return singlePaymentIn;
    }
}