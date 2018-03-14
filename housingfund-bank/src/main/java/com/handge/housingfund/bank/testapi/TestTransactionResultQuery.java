package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class TestTransactionResultQuery {
    public static TransactionResultQueryIn getBOC(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getBOCHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }

    public static TransactionResultQueryIn getABC(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getABCHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }
    public static TransactionResultQueryIn getICBC(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getICBCHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }
    public static TransactionResultQueryIn getCCB(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getCCBHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }
    public static TransactionResultQueryIn getBOCOM(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getBOCOMHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }
    public static TransactionResultQueryIn getGZBC(String date, String seqNo) {
        CenterHeadIn chi = CenterHead.getGZBCHead("BDC110");
        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn(chi, date, seqNo);

        return transactionResultQueryIn;
    }
}
