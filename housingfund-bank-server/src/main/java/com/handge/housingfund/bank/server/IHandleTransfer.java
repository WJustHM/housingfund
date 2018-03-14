package com.handge.housingfund.bank.server;

import com.handge.housingfund.common.service.bank.bean.transfer.*;

/**
 * Created by gxy on 17-12-6.
 */
public interface IHandleTransfer {

    void handler(SingleTransInApplIn singleTransInApplIn);

    void handler(TransInApplCancelIn transInApplCancelIn);

    ApplScheduleQueryOut handler(ApplScheduleQueryIn applScheduleQueryIn);

    void handler(SingleTransOutInfoIn singleTransOutInfoIn);
}
