package com.handge.housingfund.bank.server.service;

import com.handge.housingfund.bank.server.IHandleTransfer;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.collection.service.trader.IAllochthounousBackCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 转移接续请求处理
 * Created by gxy on 17-12-6.
 */
@Service
public class HandleTransfer implements IHandleTransfer {

    @Autowired
    private IAllochthounousBackCall iAllochthounousBackCall;

    /**
     * BDC901
     * 收到【转入发起】 0-新增申请
     * 收到【转出发起】 1-转出方确认受理
     * 请实现作为对手方收到发起方的请求后的业务逻辑
     *
     * @param singleTransInApplIn
     */
    @Override
    public void handler(SingleTransInApplIn singleTransInApplIn) {

        if ("1".equals(singleTransInApplIn.getTxFunc())) {

            iAllochthounousBackCall.transferIntoCall(singleTransInApplIn);

        } else if ("0".equals(singleTransInApplIn.getTxFunc())) {

            iAllochthounousBackCall.transferOutInputCallBack(singleTransInApplIn);
        }
    }

    /**
     * BDC903
     * 收到【转入发起】申请撤销
     * 请实现作为对手方收到发起方的请求后的业务逻辑
     *
     * @param transInApplCancelIn
     */
    @Override
    public void handler(TransInApplCancelIn transInApplCancelIn) {
        iAllochthounousBackCall.sendCancelNotice(transInApplCancelIn);
//        System.out.println(transInApplCancelIn);
    }

    /**
     * BDC904
     * 申请进度查询
     *
     * @param applScheduleQueryIn
     * @return
     */
    @Override
    public ApplScheduleQueryOut handler(ApplScheduleQueryIn applScheduleQueryIn) {
        System.out.println(applScheduleQueryIn);
        return new ApplScheduleQueryOut();
    }

    /**
     * BDC905
     * 收到【转出发起】 0-新增信息转出
     * 收到【转入发起】 1-修改转出信息
     * 收到【转入发起】 2-转入确认办结
     * 请实现作为对手方收到发起方的请求后的业务逻辑
     *
     * @param singleTransOutInfoIn
     */
    @Override
    public void handler(SingleTransOutInfoIn singleTransOutInfoIn) {

        if ("2".equals(singleTransOutInfoIn.getTxFunc())) {

            iAllochthounousBackCall.transferOutConfirmCallBack(singleTransOutInfoIn);

        } else if ("0".equals(singleTransOutInfoIn.getTxFunc())) {

            iAllochthounousBackCall.transferOutCall(singleTransOutInfoIn);

        } else if ("1".equals(singleTransOutInfoIn.getTxFunc())) {

            iAllochthounousBackCall.transferOutArrange(singleTransOutInfoIn);

        }
    }
}
