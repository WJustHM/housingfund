package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.SingleTransferAccountIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class TestSingleTransferAccount {
    public static SingleTransferAccountIn getBOC(){
        CenterHeadIn chi = CenterHead.getBOCHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                chi,
                "1",
                "18",
                "184208222254",
                "公积金中心测试账户一",
                "2",
                "181208222212",
                "公积金中心测试账户四",
                "2",
                "0",
                new BigDecimal("1.00"),
                "中国银行单笔转账(本行),对公",
                "中国银行单笔转账(本行),对公"
        );
        singleTransferAccountIn.setCrBankName("中国银行");
        singleTransferAccountIn.setCrChgNo("104100000004");

        return  singleTransferAccountIn;
    }
    /**
     * 农业银行单笔转账(本行),对公
     */
    public static SingleTransferAccountIn getABC() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                centerHeadIn,
                "1",
                "18",
                "22401001040003017",
                "公积金对公测试账户一",
                "2",
                "22401001040000245",
                "四淳师盟士首踏珊军准菇承于慊官送",
                "2",
                "0",
                new BigDecimal("1.00"),
                "农业银行单笔转账(本行),对公",
                "农业银行单笔转账(本行),对公"
        );

        return singleTransferAccountIn;
    }
    /**
     * 工商银行单笔转账(本行),对公
     */
    public static SingleTransferAccountIn getICBC() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                centerHeadIn,
                "1",
                "18",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "2",
                "2406075029200001081",
                "翁盯申漾专件覆巾害注谬植巾铀支害注酷",
                "2",
                "0",
                new BigDecimal("100.00"),
                "工商银行单笔转账(本行),对公",
                "工商银行单笔转账(本行),对公"
        );

        return singleTransferAccountIn;
    }
    /**
     * 建设银行单笔转账(本行),对公
     */
    public static SingleTransferAccountIn getCCB() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                centerHeadIn,
                "1",
                "18",
                "52001694036050006556",
                "公司九七",
                "2",
                "52050168363600000152",
                "公司零八",
                "2",
                "0",
                new BigDecimal("1.00"),
                "建设银行单笔转账(本行),对公",
                "建设银行单笔转账(本行),对公"
        );

        return singleTransferAccountIn;
    }

    /**
     * 交通银行单笔转账(本行),对公
     */
    public static SingleTransferAccountIn getBOCOM() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                centerHeadIn,
                "1",
                "18",
                "310899991010008580716",
                "公积金中心结算账户67",
                "2",
                "310899991010003089831",
                "对公测试01",
                "2",
                "0",
                new BigDecimal("1.00"),
                "交通银行单笔转账(本行),对公",
                "交通银行单笔转账(本行),对公"
        );

        return singleTransferAccountIn;
    }

    /**
     * 贵州银行单笔转账(本行),对公
     */
    public static SingleTransferAccountIn getGZBC() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC109");
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn(
                centerHeadIn,
                "1",
                "18",
                "0707001900000045",
                "毕节公积金",
                "2",
                "0707001300000048",
                "毕节公积金测试对公账户三",
                "2",
                "0",
                new BigDecimal("1.00"),
                "贵州银行单笔转账(本行),对公",
                "贵州银行单笔转账(本行),对公"
        );

        return singleTransferAccountIn;
    }
}
