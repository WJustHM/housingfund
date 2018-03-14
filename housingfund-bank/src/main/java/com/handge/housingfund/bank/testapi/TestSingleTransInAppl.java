package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplIn;

/**
 * Created by gxy on 17-12-7.
 */
public class TestSingleTransInAppl {
    public static SingleTransInApplIn getSingleTransInApplIn(String receiveNode) {
        CenterHeadIn centerHeadIn = CenterHead.getTransHead("BDC901", receiveNode);
        SingleTransInApplIn singleTransInApplIn = new SingleTransInApplIn(
                centerHeadIn,
                "5224001712131000",
                "0",
                "522700000000000",
                "黔南布依族苗族自治州住房公积金管理中心",
                "夜神月",
                "99",
                "123456789",
                "深度学习",
                "5468545",
                "5551468615454",
                "嘎嘎嘎",
                "中国银行",
                "5224235",
                "20171207"
        );

        return singleTransInApplIn;
    }
}
