package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.BatchResultDownloadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class TestBatchResultDownload {

    public static BatchResultDownloadIn getBOC(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
    public static BatchResultDownloadIn getABC(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getABCHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
    public static BatchResultDownloadIn getICBC(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
    public static BatchResultDownloadIn getCCB(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
    public static BatchResultDownloadIn getBOCOM(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
    public static BatchResultDownloadIn getGZBC(String txCodeSub, String batchNo) {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC108");
        BatchResultDownloadIn brdi = new BatchResultDownloadIn(chi, txCodeSub, batchNo);

        return brdi;
    }
}
