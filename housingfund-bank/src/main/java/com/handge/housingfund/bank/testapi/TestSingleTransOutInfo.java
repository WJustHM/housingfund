package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransOutInfoIn;

/**
 * Created by gxy on 17-12-7.
 */
public class TestSingleTransOutInfo {
    public static SingleTransOutInfoIn getSingleTransOutInfoIn(String receiveNode) {
        CenterHeadIn centerHeadIn = CenterHead.getTransHead("BDC905", receiveNode);
        SingleTransOutInfoIn singleTransOutInfoIn = new SingleTransOutInfoIn(
                centerHeadIn,
                "5227001702160004",
                "0",
                "阿道夫",
                "01",
                "522731190006120035",
                "10",
                "5",
                "5",
                "20171213",
                "201712",
                "1",
                "1",
                "1",
                "5221235"
        );
        singleTransOutInfoIn.setFrLoanFlag("2");

        return singleTransOutInfoIn;
    }
}
