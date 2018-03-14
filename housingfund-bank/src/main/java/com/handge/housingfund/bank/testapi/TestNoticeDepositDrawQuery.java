package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.NoticeDepositDrawQueryIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class TestNoticeDepositDrawQuery {
    public static NoticeDepositDrawQueryIn getBOC() {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "188708222188"
        );
        noticeDepositDrawQueryIn.setBookNo("001");
        noticeDepositDrawQueryIn.setBookListNo(62);
        noticeDepositDrawQueryIn.setCurrIden("1");

        return noticeDepositDrawQueryIn;
    }
    public static NoticeDepositDrawQueryIn getABC() {
        CenterHeadIn chi = CenterHead.getABCHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "188708222188"
        );
        noticeDepositDrawQueryIn.setBookNo("");
        noticeDepositDrawQueryIn.setBookListNo(0);

        return noticeDepositDrawQueryIn;
    }
    public static NoticeDepositDrawQueryIn getICBC() {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "2406070514200201514"
        );
        noticeDepositDrawQueryIn.setBookNo("930002009");
        noticeDepositDrawQueryIn.setBookListNo(930002009);
        noticeDepositDrawQueryIn.setCurrIden("1");

        return noticeDepositDrawQueryIn;
    }
    public static NoticeDepositDrawQueryIn getCCB() {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "52001694036049888888"
        );
        noticeDepositDrawQueryIn.setBookNo("");
        noticeDepositDrawQueryIn.setBookListNo(10);

        return noticeDepositDrawQueryIn;
    }
    public static NoticeDepositDrawQueryIn getBOCOM() {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "310899999600008158610"
        );
        noticeDepositDrawQueryIn.setBookNo("A0000017");
//        noticeDepositDrawQueryIn.setBookListNo(0);

        return noticeDepositDrawQueryIn;
    }
    public static NoticeDepositDrawQueryIn getGZBC() {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC121");
        NoticeDepositDrawQueryIn noticeDepositDrawQueryIn = new NoticeDepositDrawQueryIn(
                chi,
                "156",
                "0707101900000049"
        );
        noticeDepositDrawQueryIn.setBookNo("A0000017");
//        noticeDepositDrawQueryIn.setBookListNo(0);

        return noticeDepositDrawQueryIn;
    }
}
