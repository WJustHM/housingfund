package com.handge.housingfund.bank.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.entities.CBankBusiness;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;

import static com.handge.housingfund.common.service.util.JAXBUtil.formatXml;

/**
 * 转移接续平台接口
 * Created by gxy on 17-6-30.
 */
@SuppressWarnings("Duplicates")
@Service
public class TransferImpl implements ITransfer {
    private static Logger logger = LogManager.getLogger(TransferImpl.class);

    @Autowired
    private ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Override
    public SingleTransInApplOut sendMsg(SingleTransInApplIn singleTranInApplIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singleTranInApplIn.getCenterHeadIn(), "BDC901");
        singleTranInApplIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singleTranInApplIn.getTxFunc();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();

        String sendSeqNo = this.addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo);
        singleTranInApplIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singleTranInApplIn);
        SingleTransInApplOut singleTransInApplOut = (SingleTransInApplOut) transferAndSend(singleTranInApplIn, "SingleTransInApplOut");
        logger.info("返回给业务系统的数据:\n" + singleTransInApplOut);

        String status = singleTransInApplOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = singleTransInApplOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, singleTranInApplIn.getConNum(), status, rtnMessage);

        return singleTransInApplOut;
    }

    //暂不使用
    @Override
    public BatchTransInApplOut sendMsg(BatchTransInApplIn batchTransInApplIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchTransInApplIn.getCenterHeadIn(), "BDC902");
        logger.info("从业务系统收到的数据:\n" + batchTransInApplIn);
        BatchTransInApplOut batchTransInApplOut = (BatchTransInApplOut) transferAndSend(batchTransInApplIn, "BatchTransInApplOut");
        logger.info("返回给业务系统的数据:\n" + batchTransInApplOut);

        return batchTransInApplOut;
    }

    @Override
    public TransInApplCancelOut sendMsg(TransInApplCancelIn transInApplCancelIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(transInApplCancelIn.getCenterHeadIn(), "BDC903");
        transInApplCancelIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null);
        transInApplCancelIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + transInApplCancelIn);
        TransInApplCancelOut transInApplCancelOut = (TransInApplCancelOut) transferAndSend(transInApplCancelIn, "TransInApplCancelOut");
        logger.info("返回给业务系统的数据:\n" + transInApplCancelOut);

        String status = transInApplCancelOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = transInApplCancelOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", status, rtnMessage);

        return transInApplCancelOut;
    }

    @Override
    public ApplScheduleQueryOut sendMsg(ApplScheduleQueryIn applScheduleQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(applScheduleQueryIn.getCenterHeadIn(), "BDC904");
        applScheduleQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null);
        applScheduleQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + applScheduleQueryIn);
        ApplScheduleQueryOut applScheduleQueryOut = (ApplScheduleQueryOut) transferAndSend(applScheduleQueryIn, "ApplScheduleQueryOut");
        logger.info("返回给业务系统的数据:\n" + applScheduleQueryOut);

        String status = applScheduleQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = applScheduleQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", status, rtnMessage);

        return applScheduleQueryOut;
    }

    @Override
    public SingleTransOutInfoOut sendMsg(SingleTransOutInfoIn singleTransOutInfoIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singleTransOutInfoIn.getCenterHeadIn(), "BDC905");
        singleTransOutInfoIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singleTransOutInfoIn.getTxFunc();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();

        String sendSeqNo = this.addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo);
        singleTransOutInfoIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singleTransOutInfoIn);
        SingleTransOutInfoOut singleTransOutInfoOut = (SingleTransOutInfoOut) transferAndSend(singleTransOutInfoIn, "SingleTransOutInfoOut");
        logger.info("返回给业务系统的数据:\n" + singleTransOutInfoOut);

        String status = singleTransOutInfoOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = singleTransOutInfoOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, singleTransOutInfoIn.getOrConNum(), status, rtnMessage);

        return singleTransOutInfoOut;
    }

    //暂不使用
    @Override
    public BatchTransOutInfoOut sendMsg(BatchTransOutInfoIn batchTransOutInfoIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchTransOutInfoIn.getCenterHeadIn(), "BDC906");

        logger.info("从业务系统收到的数据:\n" + batchTransOutInfoIn);
        BatchTransOutInfoOut batchTransOutInfoOut = (BatchTransOutInfoOut) transferAndSend(batchTransOutInfoIn, "BatchTransOutInfoOut");
        logger.info("返回给业务系统的数据:\n" + batchTransOutInfoOut);

        return batchTransOutInfoOut;
    }

    @Override
    public CRFCenterCodeQueryOut sendMsg(CRFCenterCodeQueryIn crfCenterCodeQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(crfCenterCodeQueryIn.getCenterHeadIn(), "BDC909");
        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null);
        crfCenterCodeQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + crfCenterCodeQueryIn);
        CRFCenterCodeQueryOut crfCenterCodeQueryOut = (CRFCenterCodeQueryOut) transferAndSend(crfCenterCodeQueryIn, "CRFCenterCodeQueryOut");
        logger.info("返回给业务系统的数据:\n" + crfCenterCodeQueryOut);

        String status = crfCenterCodeQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = crfCenterCodeQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", status, rtnMessage);

        return crfCenterCodeQueryOut;
    }

    /**
     * 对象和xml报文的相互转换,并与转移接续平台进行通信
     * @param obj
     * @parm returnType
     * @return 返回类型的对象
     * @throws Exception
     */
    private static Object transferAndSend(Object obj, String returnType) throws Exception {
        Message respMsg = null;
        Object respObj = null;
        Sender sender = new Sender();
        String respXml;
        String xml = null;

        try {
            Message message = new ParmBean2XMLBean().transfer(obj);
            xml = JAXBUtil.toXML(message);
        } catch (Exception e) {
            logger.error("reqObj: " + obj + "\nreqXml: " + xml);
            logger.error(LogUtil.getTrace(e));
            throw new RuntimeException("转换发送报文出错. " + e.getMessage());
        }
        try {
            logger.info("发送到住建部的报文:\n" + JAXBUtil.formatXml(xml, "send"));
        } catch (Exception e) {
            logger.error("reqXml: \n" + xml);
            logger.error(LogUtil.getTrace(e));
        }

        if ("CenterInterfaceCheckOut".equals(returnType)) {
            try {
                respXml = sender.interfaceCheck(xml);
            } catch (Exception e) {
                logger.error(LogUtil.getTrace(e));
                if (e.getMessage().contains("网络连接出错") || e.getMessage().contains("发送请求出错")) {
                    throw new RuntimeException("发送请求出错. " + LogUtil.getTrace(e));
                } else
                    throw new Exception("接收回复出错. " + LogUtil.getTrace(e));
            }
        } else if ("LoginOut".equals(returnType)) {
            try {
                respXml = sender.login(xml);
            } catch (Exception e) {
                logger.error(LogUtil.getTrace(e));
                if (e.getMessage().contains("网络连接出错") || e.getMessage().contains("发送请求出错")) {
                    throw new RuntimeException("发送请求出错. " + LogUtil.getTrace(e));
                } else
                    throw new Exception("接收回复出错. " + LogUtil.getTrace(e));
            }
        } else {
            try {
                respXml = sender.invoke(xml);
            } catch (Exception e) {
                logger.error(LogUtil.getTrace(e));
                if (e.getMessage().contains("网络连接出错") ||
                        e.getMessage().contains("发送请求出错") ||
                        e.getMessage().contains("加密报文异常") ||
                        e.getMessage().contains("读文件异常") ||
                        e.getMessage().contains("签到出错")) {
                    throw new RuntimeException("发送请求出错.");
                } else
                    throw new Exception("接收回复出错. " + LogUtil.getTrace(e));
            }
        }

        try {
            logger.info("从住建部收到的报文:\n" + JAXBUtil.formatXml(respXml, "receive"));
        } catch (Exception e) {
            logger.error("respXml: \n" + respXml);
            logger.error(LogUtil.getTrace(e));
        }
        try {
            respMsg = JAXBUtil.createInstanceFromXML(respXml);
            respObj = new XMLBean2ParmBean().transfer(respMsg, returnType);
        } catch (Exception e) {
            logger.error("respMsg: " + respMsg + "\nrespObj: " + respObj + "\nreturnType: " + returnType);
            logger.error(LogUtil.getTrace(e));
            throw new Exception("转换回复报文出错. " + e.getLocalizedMessage());
        }

        return respObj;
    }

    /**
     * 插入发送报文的相关信息,sendSeqNo采用触发器生成,保证全局唯一
     * @param busType 交易类型
     * @param txCode 交易代码
     * @param receiveNode 接收方节点号
     * @param sendDate 发送日期
     * @param busSeqNo 业务流水号
     * @param cBankBusinessList 批量业务集合
     */
    private String addSendMsg(String busType, String txCode, String receiveNode, String custNo, String sendDate, String busSeqNo, Object ...cBankBusinessList) {

        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setBus_type(busType);
        cBankSendSeqNo.setTxCode(txCode);
        cBankSendSeqNo.setReceive_node(receiveNode);
        cBankSendSeqNo.setCust_no(custNo);
        try {
            cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd",sendDate));
        } catch (ParseException e) {
            throw new RuntimeException("发送日期格式有误");
        }
        cBankSendSeqNo.setBusiness_seq_no(busSeqNo);

        for (Object cbb: cBankBusinessList) {
            cBankSendSeqNo.addBankBusinesses((CBankBusiness)cbb);
        }

        String sendSeqNo;
        try {
            String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
            sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
//            sendSeqNo = icBankSendSeqNoDAO.saveWithoutTrigger(cBankSendSeqNo);
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            throw new RuntimeException("获取发送发流水号失败. " + e.getLocalizedMessage());
        }

        return sendSeqNo;
    }

    /**
     * 插入回复报文的相关信息
     * @param sendSeqNo 发送防流水号
     * @param conNum 联系函编号
     * @param txStatus 交易状态
     * @param rtnMessage 交易返回信息
     */
    private void addReceiveMsg(String sendSeqNo, String conNum, String txStatus, String rtnMessage) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("send_seq_no", sendSeqNo);
        try {
            CBankSendSeqNo cBankSendSeqNo = icBankSendSeqNoDAO.list(filter, null, null,null, null, null, null).get(0);
            cBankSendSeqNo.setHost_seq_no(conNum);
            cBankSendSeqNo.setRtn_message(rtnMessage);
            cBankSendSeqNo.setTx_status(txStatus);

            icBankSendSeqNoDAO.update(cBankSendSeqNo);
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    private CenterHeadIn getCenterHeadIn(CenterHeadIn centerHeadIn, String txCode) {
        Configuration cfg = Configure.getInstance().getConfiguration("bank");
        String[] datetime = DateUtil.getDatetime();
        centerHeadIn.setSendDate(datetime[0]);
        centerHeadIn.setSendTime(datetime[1]);
        centerHeadIn.setTxUnitNo(cfg.getString("txUnitNo"));
        centerHeadIn.setSendNode(cfg.getString("nodeNo"));
        centerHeadIn.setTxCode(txCode);

        return centerHeadIn;
    }
}