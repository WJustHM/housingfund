package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryIn;
import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.enums.BankExceptionEnums;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Administrator on 2017/9/20.
 */
public class SettlementHandler {
    private static Logger logger = LogManager.getLogger(SettlementHandler.class);

    private IBank iBank;
    private CenterHeadIn centerHeadIn;
    private CenterHeadOut centerHeadOut;
    private Success success;
    private Fail fail;
    private ManualProcess manualProcess;
    private SendException sendException;

    private SettlementHandler(IBank iBank) {
        this.iBank = iBank;
    }
    public static SettlementHandler instance(IBank iBank) {
        return new SettlementHandler(iBank);
    }

    public SettlementHandler setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
        return this;
    }

    public SettlementHandler setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
        return this;
    }
    public SettlementHandler setSuccess(Success success) {
        this.success = success;
        return this;
    }
    public SettlementHandler setFail(Fail fail) {
        this.fail = fail;
        return this;
    }
    public SettlementHandler setManualProcess(ManualProcess manualProcess) {
        this.manualProcess = manualProcess;
        return this;
    }
    public SettlementHandler setSendException(SendException sendException) {
        this.sendException = sendException;
        return this;
    }

    public void handle() {

        if ("0".equals(centerHeadOut.getTxStatus())) {
            success.handle();
        } else {
            transactionResultQuery();
        }
    }

    /**
     * 1. 发送报文出错,调用SendException
     * 2. 回复报文出错，调用回查接口
     * */
    public void handleException(Exception e) {
        String msg = e.getMessage();
        if (msg.contains(BankExceptionEnums.发送请求出错.getDesc()) || msg.contains(BankExceptionEnums.转换发送报文出错.getDesc())) {
            sendException.handle("发送请求失败");
        } else if (msg.contains(BankExceptionEnums.接收回复出错.getDesc()) || msg.contains(BankExceptionEnums.转换回复报文出错.getDesc())) {
            transactionResultQuery();
        } else {
            throw new ErrorException("结算平台中间件未启动");
        }
    }

    /**
     * 1. 异常,调用ManualProcess,线下处理
     * 2. 返回失败,调用ManualProcess,线下处理
     * 3. 返回成功,但原交易状态失败，调用Fail
     * 4. 返回成功,且原交易状态成功，调用Success
     * 5. 返回成功,但原交易状态未知, 调用ManualProcess,线下处理
     * */
    private void transactionResultQuery() {
        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setSendSeqNo("");
        centerHeadIn.setReceiveNode(this.centerHeadIn.getReceiveNode());
        centerHeadIn.setOperNo(this.centerHeadIn.getOperNo());
        centerHeadIn.setCustNo(this.centerHeadIn.getCustNo());

        TransactionResultQueryIn transactionResultQueryIn = new TransactionResultQueryIn();
        transactionResultQueryIn.setCenterHeadIn(centerHeadIn);
        transactionResultQueryIn.setTxSeqNo(this.centerHeadIn.getSendSeqNo());

        try {
            TransactionResultQueryOut transactionResultQueryOut = iBank.sendMsg(transactionResultQueryIn);
            logger.info("-----------------------------------------------------------");
            logger.info(transactionResultQueryOut);
            logger.info("-----------------------------------------------------------");
            if (!"0".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {
                manualProcess.handle();
            }
            if ("0".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {
                if ("1".equals(transactionResultQueryOut.getOldTxStatus())) {
                    fail.handle(this.centerHeadOut != null ? this.centerHeadOut.getRtnMessage() : "");
                } else if ("0".equals(transactionResultQueryOut.getOldTxStatus())) {
                    success.handle();
                } else {
                    manualProcess.handle();
                }
            }
        } catch (Exception e) {
            manualProcess.handle();
        }
    }

    //结算平台返回成功处理方法
    public interface Success {
        void handle();
    }
    //结算平台返回失败处理方法
    public interface Fail {
        void handle(String sbyy);
    }
    //人工处理方法
    public interface ManualProcess {
        void handle();
    }
    //发送时异常,报文尚未发送到结算平台处理方法
    public interface SendException {
        void handle(String sbyy);
    }
}
