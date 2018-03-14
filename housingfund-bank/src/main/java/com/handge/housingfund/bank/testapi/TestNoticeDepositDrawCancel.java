package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.NoticeDepositDrawCancelIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class TestNoticeDepositDrawCancel {
    public static NoticeDepositDrawCancelIn getBOC(){
        CenterHeadIn chi = CenterHead.getBOCHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "188708222188",
                "公积金中心测试账户九",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }

    public static NoticeDepositDrawCancelIn getABC(){
        CenterHeadIn chi = CenterHead.getABCHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "188708222188",
                "公积金中心测试账户九",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }
    public static NoticeDepositDrawCancelIn getICBC(){
        CenterHeadIn chi = CenterHead.getICBCHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "2406070514200201514",
                "2406070514200201514",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }
    public static NoticeDepositDrawCancelIn getCCB(){
        CenterHeadIn chi = CenterHead.getCCBHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "52001694036049888888",
                "毕节市住房公积金管理中心",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }
    public static NoticeDepositDrawCancelIn getBOCOM(){
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "310899999600008158610",
                "公积金中心定期账户67",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }
    public static NoticeDepositDrawCancelIn getGZBC(){
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC120");
        NoticeDepositDrawCancelIn noticeDepositDrawCancelIn = new NoticeDepositDrawCancelIn(
                chi,
                "156" ,
                "0707101900000049",
                "毕节公积金",
                "",
                "1",
                "",
                "",
                new BigDecimal("100000.00"),
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawCancelIn.setBookNo("");
        noticeDepositDrawCancelIn.setBookListNo(0);

        return noticeDepositDrawCancelIn;
    }
}
