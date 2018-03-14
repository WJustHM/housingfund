package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.Actived2FixedIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试活期转定期(BDC114)接口
 * Created by gxy on 17-7-4.
 */
public class TestActived2Fixed {
    /**
     * 中行活转定，对公
     */
    public static Actived2FixedIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "184208222254",
                "公积金中心测试账户一",
                "3",
                new BigDecimal("100000.00"),
                "0"
        );
        actived2FixedIn.setFixedAcctNo("187208222208");
        actived2FixedIn.setFixedAcctName("公积金中心测试账户八");

        return actived2FixedIn;
    }

    /**
     * 农行活转定，对公
     */
    public static Actived2FixedIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "22401001040003017",
                "公积金对公测试账户一",
                "3",
                new BigDecimal("100000.00"),
                "0"
        );
        actived2FixedIn.setFixedAcctNo("22401001140000335");
        actived2FixedIn.setFixedAcctName("眉水叔亵冯含记玲雎莲彬修");

        return actived2FixedIn;
    }

    /**
     * 工行活转定，对公
     */
    public static Actived2FixedIn getICBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "3",
                new BigDecimal("100000.00"),
                "0"
        );
        actived2FixedIn.setFixedAcctNo("2406070514200201487");
        actived2FixedIn.setFixedAcctName("翁盯申漾专件覆巾害注谬植");

        return actived2FixedIn;
    }

    /**
     * 建行活转定，对公
     */
    public static Actived2FixedIn getCCB4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "52001694036050006556",
                "毕节市住房公积金管理中心",
                "3",
                new BigDecimal("100000.00"),
                "1"
        );
        actived2FixedIn.setFixedAcctNo("52001694036049888888");
        actived2FixedIn.setFixedAcctName("毕节市住房公积金管理中心");

        return actived2FixedIn;
    }

    /**
     * 交行活转定，对公
     */
    public static Actived2FixedIn getBOCOM4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "310899991010008580716",
                "公积金中心结算账户67",
                "3",
                new BigDecimal("100000.00"),
                "0"
        );
        actived2FixedIn.setFixedAcctNo("310899999600008158610");
        actived2FixedIn.setFixedAcctName("公积金中心定期账户67");

        return actived2FixedIn;
    }

    /**
     * 贵州银行活转定，对公
     */
    public static Actived2FixedIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC114");
        Actived2FixedIn actived2FixedIn = new Actived2FixedIn(
                centerHeadIn,
                "156",
                "0707001900000045",
                "毕节公积金",
                "3",
                new BigDecimal("10000.00"),
                "0"
        );
        actived2FixedIn.setFixedAcctNo("0707101900000049");
        actived2FixedIn.setFixedAcctName("毕节公积金");

        return actived2FixedIn;
    }
}
