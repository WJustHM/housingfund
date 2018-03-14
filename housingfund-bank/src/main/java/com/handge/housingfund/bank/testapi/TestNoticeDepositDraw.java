package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.NoticeDepositDrawIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试通知存款支取(BDC118)接口
 * Created by gxy on 17-7-4.
 */
public class TestNoticeDepositDraw {
    /**
     * 中行通知存款支取
     */
    public static NoticeDepositDrawIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "188708222188",
                "公积金中心测试账户九",
                new BigDecimal("500000.00"),
                "0",
                "20170801",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawIn.setActivedAcctNo("184208222254");
        noticeDepositDrawIn.setActivedAcctName("公积金中心测试账户一");
        noticeDepositDrawIn.setBookNo("001");
        noticeDepositDrawIn.setBookListNo(54);
        noticeDepositDrawIn.setNoticeSetSerialNo("10000");
        noticeDepositDrawIn.setNoticeDrawDate("20170801");
        noticeDepositDrawIn.setNoticeDrawSetDate("20170801");
        noticeDepositDrawIn.setNoticeDrawAmt(new BigDecimal("100000.00"));

        return noticeDepositDrawIn;
    }
    /**
     * 农行通知存款支取
     */
    public static NoticeDepositDrawIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "22401001040003017",
                "公积金对公测试账户一",
                new BigDecimal("500000.00"),
                "1",
                "20170104",
                new BigDecimal("500000.00"),
                "6"
        );
//        noticeDepositDrawIn.setActivedAcctNo("2406070529200005732");
//        noticeDepositDrawIn.setActivedAcctName("翁盯申漾专件覆巾害注谬植");
//        noticeDepositDrawIn.setBookNo("");
//        noticeDepositDrawIn.setBookListNo(69);

        return noticeDepositDrawIn;
    }
    /**
     * 工行通知存款支取
     */
    public static NoticeDepositDrawIn getICBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "2406070514200201514",
                "翁盯申漾专件覆巾害注谬植",
                new BigDecimal("500000.00"),
                "1",
                "20170825",
                new BigDecimal("500000.00"),
                "7"
        );
        noticeDepositDrawIn.setActivedAcctNo("2406070529200005732");
        noticeDepositDrawIn.setActivedAcctName("翁盯申漾专件覆巾害注谬植");
        noticeDepositDrawIn.setBookNo("930002008");
        noticeDepositDrawIn.setBookListNo(930002008);

        return noticeDepositDrawIn;
    }
    /**
     * 建行通知存款支取
     */
    public static NoticeDepositDrawIn getCCB4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "52001694036049888888",
                "毕节市住房公积金管理中心",
                new BigDecimal("500000.00"),
                "1",
                "20180805",
                new BigDecimal("500000.00"),
                "6"
        );
        noticeDepositDrawIn.setActivedAcctNo("52001694036050006556");
        noticeDepositDrawIn.setActivedAcctName("毕节市住房公积金管理中心");
        noticeDepositDrawIn.setBookNo("0");
        noticeDepositDrawIn.setBookListNo(104);

        return noticeDepositDrawIn;
    }

    /**
     * 交行通知存款支取
     */
    public static NoticeDepositDrawIn getBOCOM4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCOMHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "310899999600008158610",
                "公积金中心定期账户67",
                new BigDecimal("500000.00"),
                "1",
                "20181201",
                new BigDecimal("500000.00"),
                "7"
        );
        noticeDepositDrawIn.setActivedAcctNo("310899991010008580716");
        noticeDepositDrawIn.setActivedAcctName("公积金中心结算账户67");
        noticeDepositDrawIn.setBookNo("A0000013");
        noticeDepositDrawIn.setBookListNo(0);

        return noticeDepositDrawIn;
    }

    /**
     * 贵行通知存款支取
     */
    public static NoticeDepositDrawIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC118");
        NoticeDepositDrawIn noticeDepositDrawIn = new NoticeDepositDrawIn(
                centerHeadIn,
                "156",
                "0707101900000049",
                "毕节公积金",
                new BigDecimal("500000.00"),
                "1",
                "20181201",
                new BigDecimal("500000.00"),
                "7"
        );
        noticeDepositDrawIn.setActivedAcctNo("0707001900000045");
        noticeDepositDrawIn.setActivedAcctName("毕节公积金");
        noticeDepositDrawIn.setBookNo("A0000013");
        noticeDepositDrawIn.setBookListNo(0);

        return noticeDepositDrawIn;
    }
}
