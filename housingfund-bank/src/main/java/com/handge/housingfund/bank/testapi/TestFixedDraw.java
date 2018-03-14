package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.FixedDrawIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.math.BigDecimal;

/**
 * 测试定期支取(BDC115)接口
 * Created by gxy on 17-7-4.
 */
public class TestFixedDraw {
    /**
     * 中行定期支取
     */
    public static FixedDrawIn getBOC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getBOCHead("BDC115");
        FixedDrawIn fixedDrawIn = new FixedDrawIn(
                centerHeadIn,
                "156",
                "187208222208",
                "公积金中心测试账户八",
                "20170727",
                "20190727",
                new BigDecimal("100000.00")
        );
        fixedDrawIn.setActivedAcctNo("184208222254");
        fixedDrawIn.setActivedAcctName("公积金中心测试账户一");
        fixedDrawIn.setBookNo("002");
        fixedDrawIn.setBookListNo(23);
        fixedDrawIn.setDepostiAmt(new BigDecimal("100000"));
        fixedDrawIn.setDepositPeriod("3");

        return fixedDrawIn;
    }

    /**
     * 农行定期支取
     */
    public static FixedDrawIn getABC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getABCHead("BDC115");
        FixedDrawIn fixedDrawIn = new FixedDrawIn(
                centerHeadIn,
                "156",
                "22401001140000335",
                "眉水叔亵冯含记玲雎莲彬修",
                "20170104",
                "20190104",
                new BigDecimal("100000.00")
        );
        fixedDrawIn.setActivedAcctNo("22401001040003017");
        fixedDrawIn.setActivedAcctName("公积金对公测试账户一");
        fixedDrawIn.setBookNo("");
        fixedDrawIn.setBookListNo(67);
        fixedDrawIn.setDepositPeriod("3");

        return fixedDrawIn;
    }
    /**
     * 工行定期支取
     */
    public static FixedDrawIn getICBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getICBCHead("BDC115");
        FixedDrawIn fixedDrawIn = new FixedDrawIn(
                centerHeadIn,
                "156",
                "2406070514200201487",
                "翁盯申漾专件覆巾害注谬植",
                "20170728",
                "20190728",
                new BigDecimal("50000.00")
        );
        fixedDrawIn.setActivedAcctNo("2406070529200005732");
        fixedDrawIn.setActivedAcctName("翁盯申漾专件覆巾害注谬植");
        fixedDrawIn.setBookNo("830002526");
        fixedDrawIn.setDepositPeriod("3");

        return fixedDrawIn;
    }
    /**
     * 建行定期支取
     */
    public static FixedDrawIn getCCB4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getCCBHead("BDC115");
        FixedDrawIn fixedDrawIn = new FixedDrawIn(
                centerHeadIn,
                "156",
                "52001694036049888888",
                "毕节市住房公积金管理中心",
                "20170805",
                "20190805",
                new BigDecimal("100000.00")
        );
        fixedDrawIn.setActivedAcctNo("52001694036050006556");
        fixedDrawIn.setActivedAcctName("毕节市住房公积金管理中心");
        fixedDrawIn.setBookNo("0");
        fixedDrawIn.setBookListNo(102);
        fixedDrawIn.setDepositPeriod("3");

        return fixedDrawIn;
    }

    /**
     * 贵行定期支取
     */
    public static FixedDrawIn getGZBC4Common() {
        CenterHeadIn centerHeadIn = CenterHead.getGZBCHead("BDC115");
        FixedDrawIn fixedDrawIn = new FixedDrawIn(
                centerHeadIn,
                "156",
                "0707101900000049",
                "毕节公积金",
                "20181201",
                "20201201",
                new BigDecimal("10000.00")
        );
        fixedDrawIn.setActivedAcctNo("0707001900000045");
        fixedDrawIn.setActivedAcctName("毕节公积金");
//        fixedDrawIn.setBookNo("A0000014");
//        fixedDrawIn.setBookListNo(0);
        fixedDrawIn.setDepositPeriod("3");

        return fixedDrawIn;
    }
}
