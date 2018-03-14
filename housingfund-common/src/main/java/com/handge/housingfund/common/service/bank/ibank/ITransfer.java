package com.handge.housingfund.common.service.bank.ibank;

import com.handge.housingfund.common.service.bank.bean.transfer.*;

/**
 * 转移接续平台接口
 * Created by gxy on 17-6-30.
 */
public interface ITransfer {
    /**
     * 单笔转入申请发送/接收 (BDC901).
     * 转入地公积金中心受理职工申请，《住房公积金异地转移接续申请表》，转入中心在受理并审核职工资料后，生成《住房公积金异地转移接续联系函》，并通过结算系统转移接续平台发送到转出机构，该接口既是转入中心的请求发送报文，也是转出机构的服务接收报文。
     * @param singleTranInApplIn SingleTransInApplIn对象
     * @return SingleTransInApplOut对象
     * @throws Exception
     */
    SingleTransInApplOut sendMsg(SingleTransInApplIn singleTranInApplIn) throws Exception;

    /**
     * 批量转入申请发送/接收(BDC902).
     * 转入地公积金中心受理职工申请，《住房公积金异地转移接续申请表》，转入中心在受理并审核职工资料后，生成《住房公积金异地转移接续联系函》，并发送转出机构，该接口既是转入中心的请求发送报文，也是转出机构的服务接收报文。
     * @param batchTransInApplIn BatchTransInApplIn对象
     * @return BatchTransInApplOut对象
     * @throws Exception
     */
    BatchTransInApplOut sendMsg(BatchTransInApplIn batchTransInApplIn) throws Exception;

    /**
     * 转入申请撤销发送/接收(BDC903).
     * 只有当联系函在“联系函复核通过”，即是转出公积金中心仍未确认受理时才可撤销，该接口既是转入中心的请求发送报文，也是转出中心的服务接收报文。
     * @param transInApplCancelIn TransInApplCancelIn对象
     * @return TransInApplCancelOut对象
     * @throws Exception
     */
    TransInApplCancelOut sendMsg(TransInApplCancelIn transInApplCancelIn) throws Exception;

    /**
     * 申请进度查询发送/接收(BDC904).
     * 转入中心查询转出机构申请处理进度，该接口既是转入中心的请求发送报文，也是转出机构的服务接收报文。
     * @param applScheduleQueryIn ApplScheduleQueryIn对象
     * @return ApplScheduleQueryOut对象
     * @throws Exception
     */
    ApplScheduleQueryOut sendMsg(ApplScheduleQueryIn applScheduleQueryIn) throws Exception;

    /**
     * 单笔转出信息发送/接收(BDC905).
     * 转出公积金中心接收《住房公积金异地转移接续联系函》后，将转出信息发送转入机构，转入中心接收转入信息，该接口既是转出中心的请求发送报文，也是转入机构的服务接收报文。该接口同时用于转入机构反馈给转出中心的办理转出账户信息的最后结果
     * @param singleTransOutInfoIn SingleTransOutInfo对象
     * @return SingleTransOutInfoOut对象
     * @throws Exception
     */
    SingleTransOutInfoOut sendMsg(SingleTransOutInfoIn singleTransOutInfoIn) throws Exception;

    /**
     * 批量转出信息发送/接收(BDC906).
     * 转出公积金中心接收《住房公积金异地转移接续联系函》后，将转出信息通过转移接续平台发送给转入机构，转入结构接收转入信息，该接口既是转出中心的请求发送报文，也是转入机构的服务接收报文。该接口同时用于转入机构反馈给转出中心的办理转出账户信息的最后结果
     * @param batchTransOutInfoIn BatchTransOutInfoIn对象
     * @return BatchTransOutInfoOut对象
     * @throws Exception
     */
    BatchTransOutInfoOut sendMsg(BatchTransOutInfoIn batchTransOutInfoIn) throws Exception;

    /**
     * 转移接续平台公积金中心代码查询(BDC909).
     * 公积金中心以机构编号、机构名称为条件在转移接续平台模糊查询已接入住建部异地转移接续平台的公积金中心机构。
     * @param crfCenterCodeQueryIn CRFCenterCodeQueryIn对象
     * @return CRFCenterCodeQueryOut对象
     * @throws Exception
     */
    CRFCenterCodeQueryOut sendMsg(CRFCenterCodeQueryIn crfCenterCodeQueryIn) throws Exception;
}
