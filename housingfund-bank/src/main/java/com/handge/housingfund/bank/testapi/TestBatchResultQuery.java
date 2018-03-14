package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.BatchResultQueryIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class TestBatchResultQuery {

    public static BatchResultQueryIn getBOC(String batchNo) {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }
    public static BatchResultQueryIn getABC(String batchNo) {
        CenterHeadIn chi = CenterHead.getABCHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }
    public static BatchResultQueryIn getICBC(String batchNo) {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }
    public static BatchResultQueryIn getCCB(String batchNo) {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }
    public static BatchResultQueryIn getBOCOM(String batchNo) {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }

    public static BatchResultQueryIn getGZBC(String batchNo) {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC107");
        BatchResultQueryIn batchResultQueryIn = new BatchResultQueryIn(chi, batchNo);
        return batchResultQueryIn;
    }
}
