package com.handge.housingfund.common.service.collection.service.trader;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransOutInfoIn;

import com.handge.housingfund.common.service.bank.bean.transfer.TransInApplCancelIn;
import com.handge.housingfund.common.service.bank.bean.transfer.TransInApplCancelOut;

/**
 * Created by Liujuhao on 2017/12/8.
 */
public interface IAllochthounousBackCall {

    /**
     * 转出方接收转入方联系函-回调
     * @param singleTransOutInfoIn
     */
    public void transferOutInputCallBack(SingleTransInApplIn singleTransOutInfoIn);

    /**
     * 转出方接收转入方确认办结-回调
     * @param singleTransOutInfoIn
     */
    public void transferOutConfirmCallBack(SingleTransOutInfoIn singleTransOutInfoIn);

    /**
     * 转入申请撤销发送/接收(BDC903)--回调.
     * 只有当联系函在“联系函复核通过”，即是转出公积金中心仍未确认受理时才可撤销，该接口既是转入中心的请求发送报文，也是转出中心的服务接收报文。
     * @param transInApplCancelIn TransInApplCancelIn对象
     * @return TransInApplCancelOut对象
     * @throws Exception
     */
    public void sendCancelNotice(TransInApplCancelIn transInApplCancelIn);

    /**
     *
     * 回调函数-转移接续转入
     * @param singleTransInApplIn
     */
    public void transferIntoCall(SingleTransInApplIn singleTransInApplIn);


    /**
     * 单笔转出信息发送/接收(BDC905).
     * @param singleTransOutInfoIn
     */
    public void transferOutCall(SingleTransOutInfoIn singleTransOutInfoIn);

    /**
     * 单笔付款的回调
     * @param accChangeNotice
     */
    public void transferOutPayCallBack(AccChangeNotice accChangeNotice);

    /**
     * 协商回调 转入方发起
     * @param singleTransOutInfoIn
     */
    public void transferOutArrange(SingleTransOutInfoIn singleTransOutInfoIn);
}
