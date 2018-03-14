package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.NoticeDepositDrawSetIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class TestNoticeDepositDrawSet {
    public static NoticeDepositDrawSetIn getBOC() {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC119");
        NoticeDepositDrawSetIn noticeDepositDrawSetIn = new NoticeDepositDrawSetIn(
                chi,
                "156",
                "188708222188",
                "公积金中心测试账户九",
                "001",
                62,
                new BigDecimal("500000.00"),
                "20170908",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawSetIn.setCurrIden("1");

        return noticeDepositDrawSetIn;
    }
    public static NoticeDepositDrawSetIn getICBC() {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC119");
        NoticeDepositDrawSetIn noticeDepositDrawSetIn = new NoticeDepositDrawSetIn(
                chi,
                "156",
                "2406070514200201514",
                "翁盯申漾专件覆巾害注谬植",
                "930002009",
                930002009,
                new BigDecimal("500000.00"),
                "20170825",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawSetIn.setCurrIden("1");

        return noticeDepositDrawSetIn;
    }

    public static NoticeDepositDrawSetIn getCCB() {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC119");
        NoticeDepositDrawSetIn noticeDepositDrawSetIn = new NoticeDepositDrawSetIn(
                chi,
                "156",
                "52001694036049888888",
                "毕节市住房公积金管理中心",
                "0",
                108,
                new BigDecimal("500000.00"),
                "20170804",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawSetIn.setCurrIden("1");

        return noticeDepositDrawSetIn;
    }
    public static NoticeDepositDrawSetIn getBOCOM() {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC119");
        NoticeDepositDrawSetIn noticeDepositDrawSetIn = new NoticeDepositDrawSetIn(
                chi,
                "156",
                "310899999600008158610",
                "公积金中心定期账户67",
                "A0000017",
                0,
                new BigDecimal("500000.00"),
                "20190130",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawSetIn.setCurrIden("1");

        return noticeDepositDrawSetIn;
    }
    public static NoticeDepositDrawSetIn getGZBC() {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC119");
        NoticeDepositDrawSetIn noticeDepositDrawSetIn = new NoticeDepositDrawSetIn(
                chi,
                "156",
                "0707101900000049",
                "毕节公积金",
                "A0000017",
                0,
                new BigDecimal("500000.00"),
                "20190130",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawSetIn.setCurrIden("1");

        return noticeDepositDrawSetIn;
    }
}
