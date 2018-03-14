package com.handge.housingfund.bank.server.service;

import com.handge.housingfund.bank.server.ICenterTransfer;
import com.handge.housingfund.bank.server.IHandleTransfer;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 中心转移接续接收接口
 * Created by gxy on 17-12-6.
 */
@SuppressWarnings("Duplicates")
@Component
public class CenterTransferImpl implements ICenterTransfer {
    private static Logger logger = LogManager.getLogger(CenterTransferImpl.class);

    @Autowired
    private IHandleTransfer iHandleTransfer;
    @Autowired
    private ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Override
    public SingleTransInApplOut recMsg(SingleTransInApplIn singleTransInApplIn) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        SingleTransInApplOut singleTransInApplOut = new SingleTransInApplOut();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("BDC901");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = singleTransInApplIn.getCenterHeadIn().getSendNode();
        CenterHeadOut centerHeadOut = getCenterHeadOut("0", sendSeqNo, "BDC901", sendNode);
//        CenterHeadOut centerHeadOut = getCenterHeadOut("0", "12314141413", "BDC901", sendNode);

        singleTransInApplOut.setCenterHeadOut(centerHeadOut);
        logger.info("发送到住建部的数据:\n" + singleTransInApplOut);

        new Thread(() -> iHandleTransfer.handler(singleTransInApplIn)).start();

        return singleTransInApplOut;
    }

    @Override
    public TransInApplCancelOut recMsg(TransInApplCancelIn transInApplCancelIn) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        TransInApplCancelOut transInApplCancelOut = new TransInApplCancelOut();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("BDC903");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = transInApplCancelIn.getCenterHeadIn().getSendNode();
        CenterHeadOut centerHeadOut = getCenterHeadOut("0", sendSeqNo, "BDC903", sendNode);
//        CenterHeadOut centerHeadOut = getCenterHeadOut("0", "12314141413", "BDC903", sendNode);

        transInApplCancelOut.setCenterHeadOut(centerHeadOut);
        logger.info("发送到住建部的数据:\n" + transInApplCancelOut);

        new Thread(() -> iHandleTransfer.handler(transInApplCancelIn)).start();

        return transInApplCancelOut;
    }

    @Override
    public ApplScheduleQueryOut recMsg(ApplScheduleQueryIn applScheduleQueryIn) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        ApplScheduleQueryOut applScheduleQueryOut = new ApplScheduleQueryOut();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("BDC904");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = applScheduleQueryIn.getCenterHeadIn().getSendNode();
        CenterHeadOut centerHeadOut = getCenterHeadOut("0", sendSeqNo, "BDC904", sendNode);
//        CenterHeadOut centerHeadOut = getCenterHeadOut("0", "12314141413", "BDC904", sendNode);

        ApplScheduleQueryOut queryOut = iHandleTransfer.handler(applScheduleQueryIn);

        applScheduleQueryOut.setCenterHeadOut(centerHeadOut);
        applScheduleQueryOut.setAppProMessage(queryOut.getAppProMessage());
        applScheduleQueryOut.setAppPrStatus(queryOut.getAppPrStatus());
        applScheduleQueryOut.setContacts(queryOut.getContacts());
        applScheduleQueryOut.setContPhone(queryOut.getContPhone());

        logger.info("发送到住建部的数据:\n" + applScheduleQueryOut);
        return applScheduleQueryOut;
    }

    @Override
    public SingleTransOutInfoOut recMsg(SingleTransOutInfoIn singleTransOutInfoIn) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        SingleTransOutInfoOut singleTransOutInfoOut = new SingleTransOutInfoOut();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("BDC905");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = singleTransOutInfoIn.getCenterHeadIn().getSendNode();
        CenterHeadOut centerHeadOut = getCenterHeadOut("0", sendSeqNo, "BDC905", sendNode);
//        CenterHeadOut centerHeadOut = getCenterHeadOut("0", "12314141413", "BDC905", sendNode);

        singleTransOutInfoOut.setCenterHeadOut(centerHeadOut);
        logger.info("发送到住建部的数据:\n" + singleTransOutInfoOut);

        new Thread(() -> iHandleTransfer.handler(singleTransOutInfoIn)).start();

        return singleTransOutInfoOut;
    }

    public static CenterHeadOut getCenterHeadOut(String txStatus, String sendSeqNo, String txCode, String receiveNode) {
        Configuration cfg = Configure.getInstance().getConfiguration("bank");
        String[] dateTime = DateUtil.getDatetime();
        CenterHeadOut centerHeadOut = new CenterHeadOut();
        centerHeadOut.setTxStatus(txStatus);
        if ("0".equals(txStatus)) {
            centerHeadOut.setRtnCode("00000");
            centerHeadOut.setRtnMessage("成功");
        } else {
            centerHeadOut.setRtnCode("10000");
            centerHeadOut.setRtnMessage("失败");
        }
        centerHeadOut.setReceiveDate(dateTime[0]);
        centerHeadOut.setReceiveTime(dateTime[1]);
        centerHeadOut.setReceiveSeqNo(sendSeqNo);
        centerHeadOut.setReceiveNode(receiveNode);
        centerHeadOut.setTxCode(txCode);
        centerHeadOut.setSendNode(cfg.getString("nodeNo"));

        return centerHeadOut;
    }
}
