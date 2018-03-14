package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.CRFCenterCodeQueryIn;

/**
 * Created by gxy on 17-12-6.
 */
public class TestCRFCenterCodeQuery {
    public static CRFCenterCodeQueryIn getCenterCode(String code, String name, String receiveNode) {
        CenterHeadIn centerHeadIn = CenterHead.getTransHead("BDC909", receiveNode);
        CRFCenterCodeQueryIn crfCenterCodeQueryIn = new CRFCenterCodeQueryIn();
        crfCenterCodeQueryIn.setCenterHeadIn(centerHeadIn);
        crfCenterCodeQueryIn.setUnitNo(code);
        crfCenterCodeQueryIn.setUnitName(name);

        return crfCenterCodeQueryIn;
    }
}
