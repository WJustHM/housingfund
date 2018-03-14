package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.AccTransDetailQueryIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class TestAccTransDetailQuery {
    public static AccTransDetailQueryIn getBOC(String begin, String end) {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
    public static AccTransDetailQueryIn getABC(String begin, String end) {
        CenterHeadIn chi = CenterHead.getABCHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
    public static AccTransDetailQueryIn getICBC(String begin, String end) {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
    public static AccTransDetailQueryIn getCCB(String begin, String end) {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
    public static AccTransDetailQueryIn getBOCOM(String begin, String end) {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
    public static AccTransDetailQueryIn getGZBC(String begin, String end) {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC113");
        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn(chi, begin, end);

        return accTransDetailQueryIn;
    }
}
