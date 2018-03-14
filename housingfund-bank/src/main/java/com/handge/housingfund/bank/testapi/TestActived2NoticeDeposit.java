package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.Actived2NoticeDepositIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试活期转通知存款(BDC117)接口
 * Created by gxy on 17-7-4.
 */
public class TestActived2NoticeDeposit {
    /**
     * 中行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("500000.00"),
                "184208222254",
                "公积金中心测试账户一",
                "6"
        );
        actived2NoticeDepositIn.setFixedAcctNo("188708222188");
        actived2NoticeDepositIn.setFixedAcctName("公积金中心测试账户九");

        return actived2NoticeDepositIn;
    }
    /**
     * 农行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("500000.00"),
                "22401001040003017",
                "公积金对公测试账户一",
                "6"
        );
//        actived2NoticeDepositIn.setFixedAcctNo("22401001040010426");
//        actived2NoticeDepositIn.setFixedAcctName("公积金对公测试账户二");

        return actived2NoticeDepositIn;
    }

    /**
     * 工行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getICBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("500000.00"),
                "2406070529200005732",
                "翁盯申漾专件覆巾害注谬植",
                "6"
        );
        actived2NoticeDepositIn.setFixedAcctNo("2406070514200201514");
        actived2NoticeDepositIn.setFixedAcctName("翁盯申漾专件覆巾害注谬植");

        return actived2NoticeDepositIn;
    }

    /**
     * 建行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getCCB4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("500000.00"),
                "52001694036050006556",
                "毕节市住房公积金管理中心",
                "6"
        );
        actived2NoticeDepositIn.setFixedAcctNo("52001694036049888888");
        actived2NoticeDepositIn.setFixedAcctName("毕节市住房公积金管理中心");

        return actived2NoticeDepositIn;
    }
    /**
     * 交行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getBOCOM4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("500000.00"),
                "310899991010008580716",
                "公积金中心结算账户67",
                "6"
        );
        actived2NoticeDepositIn.setFixedAcctNo("310899999600008158610");
        actived2NoticeDepositIn.setFixedAcctName("公积金中心定期账户67");

        return actived2NoticeDepositIn;
    }
    /**
     * 贵行活期转通知存款，对公
     */
    public static Actived2NoticeDepositIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC117");
        Actived2NoticeDepositIn actived2NoticeDepositIn = new Actived2NoticeDepositIn(
                centerHeadIn,
                "156",
                new BigDecimal("10000.00"),
                "0707001900000045",
                "毕节公积金",
                "6"
        );
        actived2NoticeDepositIn.setFixedAcctNo("0707101900000049");
        actived2NoticeDepositIn.setFixedAcctName("毕节公积金");

        return actived2NoticeDepositIn;
    }
}
