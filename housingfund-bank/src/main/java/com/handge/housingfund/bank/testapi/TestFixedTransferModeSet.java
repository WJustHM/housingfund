package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.FixedTransferModeSetIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class TestFixedTransferModeSet {
    public static FixedTransferModeSetIn getBOC(){
        CenterHeadIn chi = CenterHead.getBOCHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "187208222208",
                "1"
                );
        fixedTransferModeSetIn.setBookNo("002");
        fixedTransferModeSetIn.setBookListNo(24);

        return fixedTransferModeSetIn;
    }

    public static FixedTransferModeSetIn getABC(){
        CenterHeadIn chi = CenterHead.getABCHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "187208222208",
                "1"
        );

        return fixedTransferModeSetIn;
    }

    public static FixedTransferModeSetIn getICBC(){
        CenterHeadIn chi = CenterHead.getICBCHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "2406070514200201487",
                "1"
        );
        fixedTransferModeSetIn.setBookNo("830002528");
        fixedTransferModeSetIn.setBookListNo(830002528);

        return fixedTransferModeSetIn;
    }

    public static FixedTransferModeSetIn getCCB(){
        CenterHeadIn chi = CenterHead.getCCBHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "52001694036049888888",
                "1"
        );
        fixedTransferModeSetIn.setBookNo("0");
        fixedTransferModeSetIn.setBookListNo(106);

        return fixedTransferModeSetIn;
    }

    public static FixedTransferModeSetIn getBOCOM(){
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "310899999600008158610",
                "1"
        );
        fixedTransferModeSetIn.setBookNo("A0000015");
        fixedTransferModeSetIn.setBookListNo(0);

        return fixedTransferModeSetIn;
    }
    public static FixedTransferModeSetIn getGZBC() {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC116");
        FixedTransferModeSetIn fixedTransferModeSetIn = new FixedTransferModeSetIn(
                chi,
                "156",
                "0707101900000049",
                "1"
        );
//        fixedTransferModeSetIn.setBookNo("A0000015");
//        fixedTransferModeSetIn.setBookListNo(0);

        return fixedTransferModeSetIn;
    }
}
