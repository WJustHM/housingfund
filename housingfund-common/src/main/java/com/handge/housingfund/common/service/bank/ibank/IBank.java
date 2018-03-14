package com.handge.housingfund.common.service.bank.ibank;

import com.handge.housingfund.common.service.bank.bean.center.*;

import java.util.List;

/**
 * 结算平台接口
 * Created by gxy on 17-6-26.
 */
public interface IBank {
    /**
     * 接口探测(SYS600).
     * 用于测试系统服务是否正常.
     * @param interfaceCheckIn InterfaceCheckIn对象
     * @return InterfaceCheckOut对象, 收到返回值, 则表示系统服务正常, 反之亦然.
     * @throws Exception
     */
    CenterInterfaceCheckOut sendMsg(CenterInterfaceCheckIn interfaceCheckIn) throws Exception;

    /**
     * 系统签到(BDC001)
     * 公积金中心系统向数据应用系统签到.
     * @param loginIn LoginIn对象
     * @return LoginOut对象, 在返回的报文体中获取SessionKey.
     * @throws Exception
     */
    LoginOut sendMsg(LoginIn loginIn) throws Exception;

    /**
     * 系统签退(BDC002).
     * 公积金中心系统向数据应用系统签退.
     * @param logoutIn LogoutIn对象
     * @return LogoutOut对象, 收到返回值, 则表签退成功, 反之亦然.
     * @throws Exception
     */
    LogoutOut sendMsg(LogoutIn logoutIn) throws Exception;

    /**
     * 单笔付款(BDC101).
     * 实现公积金缴存人公积金提取, 公积金中心资金转移、付款等支付结算业务, 此支付结算业务需在支付结算银行签约后才能使用.
     * @param singlePaymentIn SinglePaymentIn对象
     * @return SinglePaymentOut对象
     * @throws Exception
     */
    SinglePaymentOut sendMsg(SinglePaymentIn singlePaymentIn) throws Exception;

    /**
     * 单笔收款 (BDC102).
     * 实现公积金中心资金归集、贷款人提前还款等支付结算业务, 此支付结算业务需在支付结算银行签约后才能正常使用.
     * @param singleCollectionIn SingleCollectionIn对象
     * @return SingleCollectionOut对象
     * @throws Exception
     */
    SingleCollectionOut sendMsg(SingleCollectionIn singleCollectionIn) throws Exception;

    /**
     * 单笔收款 (BDC102).
     * 实现公积金中心资金归集、贷款人提前还款等支付结算业务, 此支付结算业务需在支付结算银行签约后才能正常使用.
     * @param singleCollectionIn SingleCollectionIn对象
     * @return SingleCollectionOut对象
     * @throws Exception
     */
    void sendMsgNotToBDC(SingleCollectionIn singleCollectionIn) throws Exception;

    /**
     * 批量付款(BDC103).
     * 实现公积金中心公积金缴存人批量公积金提取, 公积金中心批量资金转移、付款等支付结算业务, 此支付结算业务需在支付结算银行签约后才能正常使用.
     * 此交易为异步交易, 公积金中心发起交易交易所获得数据应用系统的受理结果, 不代表交易结算最终结果. 交易结算最终结果需使用交易结果查询(BDC107)、交易结果下载(BDC108)获得.
     * @param batchPaymentIn BatchPaymentIn对象
     * @return BatchPaymentOut对象
     * @throws Exception
     */
    BatchPaymentOut sendMsg(BatchPaymentIn batchPaymentIn, List<BatchPaymentFileSelf> batchFile) throws Exception;

    /**
     * 批量收款(BDC104).
     * 实现公积金中心资金批量归集、贷款人批量提前还款等支付结算业务. 此支付结算业务需在支付结算银行签约后才能正常使用.
     * 此交易为异步交易, 公积金中心发起交易后获得数据应用系统的交易受理结果, 不代表获得交易结算最终结果, 交易结算结果须使用交易结果查询(BDC107)、交易结果下载(BDC108)获得最终结果.
     * @param batchCollectionIn BatchCollectionIn对象
     * @return BatchCollectionOut对象
     * @throws Exception
     */
    BatchCollectionOut sendMsg(BatchCollectionIn batchCollectionIn, List<BatchCollectionFileSelf> batchFile) throws Exception;

    /**
     * 贷款扣款(BDC105).
     * 实现公积金中心贷款批量回收业务。
     * 此交易为异步交易, 公积金中心发起交易后获得数据应用系统的交易受理结果, 不代表获得交易结算最终结果, 交易结算结果须使用交易结果查询(BDC107)、交易结果下载(BDC108)获得最终结果.
     * @param loanDeductionIn LoanDeductionIn对象
     * @return LoanDeductionOut对象
     * @throws Exception
     */
    LoanDeductionOut sendMsg(LoanDeductionIn loanDeductionIn, List<LoanDeductionFileSelf> batchFile) throws Exception;

    /**
     * 贷款本息分解(BDC106).
     * 本息分解交易需与贷款扣款交易配合使用, 在公积金中心通过贷款扣款交易完成自主贷款回收并且将回收资金扣款至银行内部过渡户后, 才能发起本息分解交易.
     * 本息分解交易主要是解决公积金中心自主核实贷款后, 银行放款后无法获得贷款账户还款明细问题. 公积金中心通过本息分解交易可实现一笔回收的资金按回收资金不同属性(本金、利息、罚息、违约金)划入不同属性银行账户.
     * @param loanCapIntDecIn LoanCapIntDecIn对象
     * @return LoanCapIntDecOut对象
     * @throws Exception
     */
    LoanCapIntDecOut sendMsg(LoanCapIntDecIn loanCapIntDecIn, List<LoanCapIntDecFile> batchFile) throws Exception;

    /**
     * 批量业务结果查询(BDC107).
     * 公积金中心查询中心已发起批量交易的银行处理最终结果.
     * @param batchResultQueryIn BatchResultQueryIn对象
     * @return BatchResultQueryOut对象
     * @throws Exception
     */
    BatchResultQueryOut sendMsg(BatchResultQueryIn batchResultQueryIn) throws Exception;
    /**
     * 批量业务结果查询(BDC108).
     * 公积金中心下载中心已发起批量交易的银行处理最终结果.
     * @param batchResultDownloadIn BatchResultDownloadIn对象
     * @return BatchResultDownloadOut对象
     * @throws Exception
     */
    BatchResultDownloadOut sendMsg(BatchResultDownloadIn batchResultDownloadIn) throws Exception;

    /**
     * 单笔转账(BDC109).
     * 实现公积金中心内部定向资金调拨、付款.
     * @param singleTransferAccountIn SingleTransferAccountIn对象
     * @return SingleTransferAccountOut对象
     * @throws Exception
     */
    SingleTransferAccountOut sendMsg(SingleTransferAccountIn singleTransferAccountIn) throws Exception;

    /**
     * 交易结果查询(BDC110).
     * 公积金中心查询由中心发起交易的交易结果及交易信息.
     * @param transactionResultQueryIn TransactionResultQueryIn对象
     * @return TransactionResultQueryOut对象
     * @throws Exception
     */
    TransactionResultQueryOut sendMsg(TransactionResultQueryIn transactionResultQueryIn) throws Exception;

    /**
     * 联行号查询(BDC112).
     * 公积金中心在发起跨行交易时, 利用此接口查询银行联行号.
     * @param chgNoQueryIn ChgNoQueryIn对象
     * @return ChgNoQueryOut对象
     * @throws Exception
     */
    ChgNoQueryOut sendMsg(ChgNoQueryIn chgNoQueryIn) throws Exception;

    /**
     * 账户交易明细查询(BDC113).
     * 公积金中心以银行、时间段维度查询结算银行已经推送到数据应用系统的到账通知, 每次最多返回最近时间的1000笔.
     * @param accTransDetailQueryIn AccTransDetailQueryIn对象
     * @return AccTransDetailQueryOut对象
     * @throws Exception
     */
    AccTransDetailQueryOut sendMsg(AccTransDetailQueryIn accTransDetailQueryIn) throws Exception;

    /**
     * 活期转定期(BDC114).
     * 实现公积金中心理财, 将活期存款转为定期存款提高收益率.
     * @param actived2FixedIn Actived2FixedIn对象
     * @return Actived2FixedOut对象
     * @throws Exception
     */
    Actived2FixedOut sendMsg(Actived2FixedIn actived2FixedIn) throws Exception;

    /**
     * 定期支取(BDC115).
     * 支取公积金中心定期账户中存款, 本交易需配合定期账户余额查询交易使用.
     * 建议先使用定期账户余额查询(BDC122), 查询到银行的定期存款信息后, 将相关信息组装到定期存款支取报文中, 再发起定期支取交易.
     * @param fixedDrawIn FixedDrawIn对象
     * @return FixedDrawOut对象
     * @throws Exception
     */
    FixedDrawOut sendMsg(FixedDrawIn fixedDrawIn) throws Exception;

    /**
     * 定期转存方式设定(BDC116).
     * 公积金中心对存款的转存方式重新进行设定, 本交易需配合定期账户余额查询交易使用.
     * 建议先使用定期账户余额查询(BDC122), 查询到银行的定期存款信息后, 将相关信息组装到定期转存方式设定报文中, 再发起定期转存方式设定交易.
     * @param fixedTransferModeSetIn FixedTransferModeSetIn对象
     * @return FixedTransferModeSetOut对象
     * @throws Exception
     */
    FixedTransferModeSetOut sendMsg(FixedTransferModeSetIn fixedTransferModeSetIn) throws Exception;

    /**
     * 活期转通知存款(BDC117).
     * 实现公积金中心理财, 将活期存款转为通知存款提高收益率.
     * @param actived2NoticeDepositIn Actived2NoticeDepositIn对象
     * @return Actived2NoticeDepositOut对象
     * @throws Exception
     */
    Actived2NoticeDepositOut sendMsg(Actived2NoticeDepositIn actived2NoticeDepositIn) throws Exception;

    /**
     * 通知存款支取(BDC118).
     * 公积金中心支取通知存款, 本交易需配合定期账户余额查询交易使用(如按设定支取, 需配合通知存款支取通知查询使用).
     * 建议先使用定期账户余额查询(BDC122), 查询到银行的定期存款信息后, 将相关信息组装到通知存款支取报文中, 再发通知存款支取交易.
     * @param noticeDepositDrawIn NoticeDepositDrawIn对象
     * @return NoticeDepositDrawOut对象
     * @throws Exception
     */
    NoticeDepositDrawOut sendMsg(NoticeDepositDrawIn noticeDepositDrawIn) throws Exception;

    /**
     * 通知存款支取设定(BDC119).
     * 公积金支取通知存款前对要支取的通知存款进行设定, 设定为当前日期后支取的时间.
     * 建议先使用定期账户余额查询(BDC122), 查询到银行的定期存款信息后, 将相关信息组装到通知存款支取设定报文中, 再发起通知存款支取设定交易.
     * @param noticeDepositDrawSetIn NoticeDepositDrawSetIn对象
     * @return NoticeDepositDrawSetOut对象
     * @throws Exception
     */
    NoticeDepositDrawSetOut sendMsg(NoticeDepositDrawSetIn noticeDepositDrawSetIn) throws Exception;

    /**
     * 通知存款支取通知取消(BDC120).
     * 公积金中心取消通知存款通知, 本交易需配合通知存款支取查询使用.
     * 建议先使用通知存款支取通知查询(BDC121), 查询到银行的定期存款信息后, 将相关信息组装到通知存款支取通知取消报文中, 再发起通知存款支取通知取消交易.
     * @param noticeDepositDrawCancelIn NoticeDepositDrawCancelIn对象
     * @return NoticeDepositDrawCancelOut对象
     * @throws Exception
     */
    NoticeDepositDrawCancelOut sendMsg(NoticeDepositDrawCancelIn noticeDepositDrawCancelIn) throws Exception;

    /**
     * 通知存款支取通知查询(BDC121).
     * 查询公积金中心已经设定的通知存款支取设定.
     * @param noticeDepositDrawQueryIn NoticeDepositDrawQueryIn对象
     * @return NoticeDepositDrawQueryOut对象
     * @throws Exception
     */
    NoticeDepositDrawQueryOut sendMsg(NoticeDepositDrawQueryIn noticeDepositDrawQueryIn) throws Exception;

    /**
     * 定期账户余额查询(BDC122).
     * 公积金中心查询中心的定期账户余额.
     * @param fixedAccBalanceQueryIn FixedAccBalanceQueryIn对象
     * @return FixedAccBalanceQueryOut对象
     * @throws Exception
     */
    FixedAccBalanceQueryOut sendMsg(FixedAccBalanceQueryIn fixedAccBalanceQueryIn) throws Exception;

    /**
     * 活期账户实时余额查询(BDC123).
     * 公积金中心查询查询中心活期账户余额.
     * @param activedAccBalanceQueryIn ActivedAccBalanceQueryIn对象
     * @return ActivedAccBalanceQueryOut对象
     * @throws Exception
     */
    ActivedAccBalanceQueryOut sendMsg(ActivedAccBalanceQueryIn activedAccBalanceQueryIn) throws Exception;

    /**
     * 检查此业务是否已经发送过，若返回true，表示未发送过，反之亦然
     * @param ywlsh 业务流水号
     * @return
     */
    boolean checkYWLSH(String ywlsh);
}
