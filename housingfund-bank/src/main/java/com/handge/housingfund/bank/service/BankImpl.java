package com.handge.housingfund.bank.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.dao.impl.CBankSendSeqNoDAO;
import com.handge.housingfund.database.entities.CBankBusiness;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.tienon.util.FileFieldConv;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 结算平台接口
 * Created by gxy on 17-6-20.
 */
@SuppressWarnings("Duplicates")
@Component
public class BankImpl implements IBank {
    private static Logger logger = LogManager.getLogger(BankImpl.class);

    @Autowired
    private ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Override
    public CenterInterfaceCheckOut sendMsg(CenterInterfaceCheckIn interfaceCheckIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(interfaceCheckIn.getCenterHeadIn(), "SYS600");
        interfaceCheckIn.setCenterHeadIn(centerHeadIn);
        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        interfaceCheckIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + interfaceCheckIn);
        CenterInterfaceCheckOut centerInterfaceCheckOut = (CenterInterfaceCheckOut) transferAndSend(interfaceCheckIn, "CenterInterfaceCheckOut");
        logger.info("返回给业务系统的数据:\n" + centerInterfaceCheckOut);

        String status = centerInterfaceCheckOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = centerInterfaceCheckOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return centerInterfaceCheckOut;
    }

    @Override
    public LoginOut sendMsg(LoginIn loginIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(loginIn.getCenterHeadIn(), "BDC001");
        loginIn.setCenterHeadIn(centerHeadIn);
        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        loginIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + loginIn);
        LoginOut loginOut = (LoginOut) transferAndSend(loginIn, "LoginOut");
        logger.info("返回给业务系统的数据:\n" + loginOut);

        String status = loginOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = loginOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return loginOut;
    }

    @Override
    public LogoutOut sendMsg(LogoutIn logoutIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(logoutIn.getCenterHeadIn(), "BDC002");
        logoutIn.setCenterHeadIn(centerHeadIn);
        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        logoutIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + logoutIn);
        LogoutOut logoutOut = (LogoutOut) transferAndSend(logoutIn, "LogoutOut");
        logger.info("返回给业务系统的数据:\n" + logoutOut);

        if ("0".equals(logoutOut.getCenterHeadOut().getTxStatus()))
            FileUtil.delete(Configure.getInstance().getConfiguration("bank").getString("sessionKey"));

        String status = logoutOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = logoutOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return logoutOut;
    }

    @Override
    public SinglePaymentOut sendMsg(SinglePaymentIn singlePaymentIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singlePaymentIn.getCenterHeadIn(), "BDC101");
        singlePaymentIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singlePaymentIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = singlePaymentIn.getCrAcctNo();
        String crAcctName = singlePaymentIn.getCrAcctName();
        String deAcctNo = singlePaymentIn.getDeAcctNo();
        String deAcctName = singlePaymentIn.getDeAcctName();
        BigDecimal amt = singlePaymentIn.getAmt().negate();

        //映射国标里的贷款发放业务类型
        if ("04".equals(busType)) {
            busType = "01";
        }
        String sendSeqNo = this.addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        singlePaymentIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singlePaymentIn);
        SinglePaymentOut singlePaymentOut = (SinglePaymentOut) transferAndSend(singlePaymentIn, "SinglePaymentOut");
        logger.info("返回给业务系统的数据:\n" + singlePaymentOut);

        String hostSeqNo = singlePaymentOut.getCapHostSeqNo();
        String intHostSeqNo = singlePaymentOut.getIntHostSeqNo();
        String status = singlePaymentOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = singlePaymentOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, intHostSeqNo,"","", status, rtnMessage, "single");

        return singlePaymentOut;
    }

    @Override
    public SingleCollectionOut sendMsg(SingleCollectionIn singleCollectionIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singleCollectionIn.getCenterHeadIn(), "BDC102");
        singleCollectionIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singleCollectionIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = singleCollectionIn.getCrAcctNo();
        String crAcctName = singleCollectionIn.getCrAcctName();
        String deAcctNo = singleCollectionIn.getDeAcctNo();
        String deAcctName = singleCollectionIn.getDeAcctName();
        BigDecimal amt = singleCollectionIn.getAmt();

        String sendSeqNo =  addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        singleCollectionIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singleCollectionIn);
        SingleCollectionOut singleCollectionOut = (SingleCollectionOut) transferAndSend(singleCollectionIn, "SingleCollectionOut");
        logger.info("返回给业务系统的数据:\n" + singleCollectionOut);

        String hostSeqNo = singleCollectionOut.getHostSeqNo();
        String status = singleCollectionOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = singleCollectionOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "","","", status, rtnMessage, "single");

        return singleCollectionOut;
    }

    @Override
    public void sendMsgNotToBDC(SingleCollectionIn singleCollectionIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singleCollectionIn.getCenterHeadIn(), "BDC102");
        singleCollectionIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singleCollectionIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = singleCollectionIn.getCrAcctNo();
        String crAcctName = singleCollectionIn.getCrAcctName();
        String deAcctNo = singleCollectionIn.getDeAcctNo();
        String deAcctName = singleCollectionIn.getDeAcctName();
        BigDecimal amt = singleCollectionIn.getAmt();

        String sendSeqNo =  addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        singleCollectionIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singleCollectionIn);

        if("true".equals(Configure.getInstance().getConfiguration("bank").getString("simulation"))){
            transfer1AndSend(singleCollectionIn, "SingleCollectionOut", businessSeqNo);
        }
    }

    @Override
    public BatchPaymentOut sendMsg(BatchPaymentIn batchPaymentIn, List<BatchPaymentFileSelf> batchFile) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchPaymentIn.getCenterHeadIn(), "BDC103");
        batchPaymentIn.setCenterHeadIn(centerHeadIn);

        String batchSeqNo = centerHeadIn.getSendSeqNo();
        String busType = batchPaymentIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = null;
        String crAcctName = null;
        String deAcctNo = batchPaymentIn.getDeAcctNo();
        String deAcctName = batchPaymentIn.getDeAcctName();
        BigDecimal amt = batchPaymentIn.getBatchTotalAmt().negate();

        List<CBankBusiness> list = new ArrayList<>();

        BatchPaymentFileSelf batchPaymentFile;
        for (Object per : batchFile) {
            CBankBusiness cBankBusiness = new CBankBusiness();
            if (per instanceof BatchPaymentFileSelf)
                batchPaymentFile = (BatchPaymentFileSelf) per;
            else
                batchPaymentFile = (BatchPaymentFileOther) per;
            cBankBusiness.setNo(batchPaymentFile.getNo());
            cBankBusiness.setBusiness_seq_no(batchPaymentFile.getRefSeqNo1());
            list.add(cBankBusiness);
        }
        String sendSeqNo = addSendMsg(busType, txCode, receiveNode, custNo, sendDate, batchSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt, list.toArray());

        batchPaymentIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        batchPaymentIn.setFileList(TransactionFileFactory.getFileList(sendSeqNo, batchFile));

        logger.info("从业务系统收到的数据:\n" + batchPaymentIn);
        BatchPaymentOut batchPaymentOut = (BatchPaymentOut) transferAndSend(batchPaymentIn, "BatchPaymentOut");
        logger.info("返回给业务系统的数据:\n" + batchPaymentOut);

        String batchNo = batchPaymentOut.getBatchNo();
        String status = batchPaymentOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = batchPaymentOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, batchNo,"","","", status, rtnMessage, "batch");

        return batchPaymentOut;
    }

    @Override
    public BatchCollectionOut sendMsg(BatchCollectionIn batchCollectionIn, List<BatchCollectionFileSelf> batchFile) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchCollectionIn.getCenterHeadIn(), "BDC104");
        batchCollectionIn.setCenterHeadIn(centerHeadIn);

        String batchSeqNo = centerHeadIn.getSendSeqNo();
        String busType = batchCollectionIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = batchCollectionIn.getCrAcctNo();
        String crAcctName = batchCollectionIn.getCrAcctName();
        String deAcctNo = null;
        String deAcctName = null;
        BigDecimal amt = batchCollectionIn.getBatchTotalAmt();

        List<CBankBusiness> list = new ArrayList<>();

        BatchCollectionFileSelf collectionFile;
        for (Object per : batchFile) {
            CBankBusiness cBankBusiness = new CBankBusiness();
            if (per instanceof BatchCollectionFileSelf)
                collectionFile = (BatchCollectionFileSelf) per;
            else
                collectionFile = (BatchCollectionFileOther) per;
            cBankBusiness.setNo(collectionFile.getNo());
            cBankBusiness.setBusiness_seq_no(collectionFile.getRefSeqNo());
            list.add(cBankBusiness);
        }
        String sendSeqNo = addSendMsg(busType, txCode, receiveNode, custNo, sendDate, batchSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt, list.toArray());

        batchCollectionIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        batchCollectionIn.setFileList(TransactionFileFactory.getFileList(sendSeqNo, batchFile));

        logger.info("从业务系统收到的数据:\n" + batchCollectionIn);
        BatchCollectionOut batchCollectionOut = (BatchCollectionOut) transferAndSend(batchCollectionIn, "BatchCollectionOut");
        logger.info("返回给业务系统的数据:\n" + batchCollectionOut);

        String batchNo = batchCollectionOut.getBatchNo();
        String status = batchCollectionOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = batchCollectionOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, batchNo,"","","", status, rtnMessage, "batch");

        return batchCollectionOut;
    }

    @Override
    public LoanDeductionOut sendMsg(LoanDeductionIn loanDeductionIn, List<LoanDeductionFileSelf> batchFile) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(loanDeductionIn.getCenterHeadIn(), "BDC105");
        loanDeductionIn.setCenterHeadIn(centerHeadIn);

        String batchSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = loanDeductionIn.getCrAcctNo();
        String crAcctName = loanDeductionIn.getCrAcctName();
        String deAcctNo = null;
        String deAcctName = null;
        BigDecimal amt = loanDeductionIn.getBatchTotalAmt();

        List<CBankBusiness> list = new ArrayList<>();

        LoanDeductionFileSelf loanDeductionFile;
        for (Object per : batchFile) {
            CBankBusiness cBankBusiness = new CBankBusiness();
            if (per instanceof LoanDeductionFileSelf)
                loanDeductionFile = (LoanDeductionFileSelf) per;
            else
                loanDeductionFile = (LoanDeductionFileOther) per;
            cBankBusiness.setNo(loanDeductionFile.getNo());
            cBankBusiness.setBusiness_seq_no(loanDeductionFile.getSummary());
            list.add(cBankBusiness);
        }
        String sendSeqNo = addSendMsg("05", txCode, receiveNode, custNo, sendDate, batchSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt, list.toArray());

        loanDeductionIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        loanDeductionIn.setFileList(TransactionFileFactory.getFileList(sendSeqNo, batchFile));

        logger.info("从业务系统收到的数据:\n" + loanDeductionIn);
        LoanDeductionOut loanDeductionOut = (LoanDeductionOut) transferAndSend(loanDeductionIn, "LoanDeductionOut");
        logger.info("返回给业务系统的数据:\n" + loanDeductionOut);

        String batchNo = loanDeductionOut.getBatchNo();
        String status = loanDeductionOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = loanDeductionOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, batchNo,"","","", status, rtnMessage, "batch");

        return loanDeductionOut;
    }

    @Override
    public LoanCapIntDecOut sendMsg(LoanCapIntDecIn loanCapIntDecIn, List<LoanCapIntDecFile> batchFile) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(loanCapIntDecIn.getCenterHeadIn(), "BDC106");
        loanCapIntDecIn.setCenterHeadIn(centerHeadIn);

        String batchSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = loanCapIntDecIn.getLoanCapCrAcctNo();
        String crAcctName = loanCapIntDecIn.getLoanCapCrAcctName();
        String deAcctNo = null;
        String deAcctName = null;
        BigDecimal amt = loanCapIntDecIn.getBkAmt();

        List<CBankBusiness> list = new ArrayList<>();

        LoanCapIntDecFile loanCapIntDecFile;
        for (Object per : batchFile) {
            CBankBusiness cBankBusiness = new CBankBusiness();
            loanCapIntDecFile = (LoanCapIntDecFile) per;
            cBankBusiness.setNo(loanCapIntDecFile.getNo());
            cBankBusiness.setBusiness_seq_no(loanCapIntDecFile.getSummary());
            list.add(cBankBusiness);
        }
        String sendSeqNo = addSendMsg("06", txCode, receiveNode, custNo, sendDate, batchSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt, list.toArray());

        loanCapIntDecIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        loanCapIntDecIn.setFileList(TransactionFileFactory.getFileList(sendSeqNo, batchFile));

        logger.info("从业务系统收到的数据:\n" + loanCapIntDecIn);
        LoanCapIntDecOut loanCapIntDecOut = (LoanCapIntDecOut) transferAndSend(loanCapIntDecIn, "LoanCapIntDecOut");
        logger.info("返回给业务系统的数据:\n" + loanCapIntDecOut);

        String capHostSeqNo = loanCapIntDecOut.getCapHostSeqNo();
        String intHostSeqNo = loanCapIntDecOut.getIntHostSeqNo();
        String penHostSeqNo = loanCapIntDecOut.getPenHostSeqNo();
        String fineHostSeqNo = loanCapIntDecOut.getFineHostSeqNo();
        String status = loanCapIntDecOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = loanCapIntDecOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, capHostSeqNo, intHostSeqNo, penHostSeqNo, fineHostSeqNo, status, rtnMessage, "single");

        return loanCapIntDecOut;
    }

    @Override
    public BatchResultQueryOut sendMsg(BatchResultQueryIn batchResultQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchResultQueryIn.getCenterHeadIn(), "BDC107");
        batchResultQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);

        batchResultQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        batchResultQueryIn.setBatchNo(getOldSendSeqNo(batchResultQueryIn.getBatchNo()).get("BatchNo"));

        logger.info("从业务系统收到的数据:\n" + batchResultQueryIn);
        BatchResultQueryOut batchResultQueryOut = (BatchResultQueryOut) transferAndSend(batchResultQueryIn, "BatchResultQueryOut");
        logger.info("返回给业务系统的数据:\n" + batchResultQueryOut);

        String status = batchResultQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = batchResultQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return batchResultQueryOut;
    }

    @Override
    public BatchResultDownloadOut sendMsg(BatchResultDownloadIn batchResultDownloadIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(batchResultDownloadIn.getCenterHeadIn(), "BDC108");
        batchResultDownloadIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        batchResultDownloadIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        batchResultDownloadIn.setBatchNo(getOldSendSeqNo(batchResultDownloadIn.getBatchNo()).get("BatchNo"));

        logger.info("从业务系统收到的数据:\n" + batchResultDownloadIn);
        BatchResultDownloadOut batchResultDownloadOut = (BatchResultDownloadOut) transferAndSend(batchResultDownloadIn, "BatchResultDownloadOut");
        logger.info("返回给业务系统的数据:\n" + batchResultDownloadOut);

        String status = batchResultDownloadOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = batchResultDownloadOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return batchResultDownloadOut;
    }

    @Override
    public SingleTransferAccountOut sendMsg(SingleTransferAccountIn singleTransferAccountIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(singleTransferAccountIn.getCenterHeadIn(), "BDC109");
        singleTransferAccountIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String busType = singleTransferAccountIn.getBusType();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = singleTransferAccountIn.getCrAcctNo();
        String crAcctName = singleTransferAccountIn.getCrAcctName();
        String deAcctNo = singleTransferAccountIn.getDeAcctNo();
        String deAcctName = singleTransferAccountIn.getDeAcctName();
        BigDecimal amt = singleTransferAccountIn.getAmt().negate();

        String sendSeqNo = addSendMsg(busType, txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        singleTransferAccountIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + singleTransferAccountIn);
        SingleTransferAccountOut singleTransferAccountOut = (SingleTransferAccountOut) transferAndSend(singleTransferAccountIn, "SingleTransferAccountOut");
        logger.info("返回给业务系统的数据:\n" + singleTransferAccountOut);

        String hostSeqNo = singleTransferAccountOut.getHostSeqNo();
        String status = singleTransferAccountOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = singleTransferAccountOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "","","", status, rtnMessage, "single");

        return singleTransferAccountOut;
    }

    @Override
    public TransactionResultQueryOut sendMsg(TransactionResultQueryIn transactionResultQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(transactionResultQueryIn.getCenterHeadIn(), "BDC110");
        transactionResultQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        transactionResultQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);
        HashMap<String, String> field = getOldSendSeqNo(transactionResultQueryIn.getTxSeqNo());
        transactionResultQueryIn.setTxSeqNo(field.get("SendSeqNo"));
        transactionResultQueryIn.setTxDate(field.get("SendDate"));

        logger.info("从业务系统收到的数据:\n" + transactionResultQueryIn);
        TransactionResultQueryOut transactionResultQueryOut = (TransactionResultQueryOut) transferAndSend(transactionResultQueryIn, "TransactionResultQueryOut");
        logger.info("返回给业务系统的数据:\n" + transactionResultQueryOut);

        String status = transactionResultQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = transactionResultQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return transactionResultQueryOut;
    }

    @Override
    public ChgNoQueryOut sendMsg(ChgNoQueryIn chgNoQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(chgNoQueryIn.getCenterHeadIn(), "BDC112");
        chgNoQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        chgNoQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + chgNoQueryIn);
        ChgNoQueryOut chgNoQueryOut = (ChgNoQueryOut) transferAndSend(chgNoQueryIn, "ChgNoQueryOut");
        logger.info("返回给业务系统的数据:\n" + chgNoQueryOut);

        String status = chgNoQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = chgNoQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return chgNoQueryOut;
    }

    @Override
    public AccTransDetailQueryOut sendMsg(AccTransDetailQueryIn accTransDetailQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(accTransDetailQueryIn.getCenterHeadIn(), "BDC113");
        accTransDetailQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        accTransDetailQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + accTransDetailQueryIn);
        AccTransDetailQueryOut accTransDetailQueryOut = (AccTransDetailQueryOut) transferAndSend(accTransDetailQueryIn, "AccTransDetailQueryOut");
        logger.info("返回给业务系统的数据:\n" + accTransDetailQueryOut);

        String status = accTransDetailQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = accTransDetailQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return accTransDetailQueryOut;
    }

    @Override
    public Actived2FixedOut sendMsg(Actived2FixedIn actived2FixedIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(actived2FixedIn.getCenterHeadIn(), "BDC114");
        actived2FixedIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = actived2FixedIn.getFixedAcctNo();
        String crAcctName = actived2FixedIn.getFixedAcctName();
        String deAcctNo = actived2FixedIn.getActivedAcctNo();
        String deAcctName = actived2FixedIn.getActivedAcctName();
        BigDecimal amt = actived2FixedIn.getAmt().negate();

        String sendSeqNo =  addSendMsg("09", txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        actived2FixedIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + actived2FixedIn);
        Actived2FixedOut actived2FixedOut = (Actived2FixedOut) transferAndSend(actived2FixedIn, "Actived2FixedOut");
        logger.info("返回给业务系统的数据:\n" + actived2FixedOut);

        String hostSeqNo = actived2FixedOut.getHostSeqNo();
        String status = actived2FixedOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = actived2FixedOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "", "", "", status, rtnMessage, "single");

        return actived2FixedOut;
    }

    @Override
    public FixedDrawOut sendMsg(FixedDrawIn fixedDrawIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(fixedDrawIn.getCenterHeadIn(), "BDC115");
        fixedDrawIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = fixedDrawIn.getActivedAcctNo();
        String crAcctName = fixedDrawIn.getActivedAcctName();
        String deAcctNo = fixedDrawIn.getFixedAcctNo();
        String deAcctName = fixedDrawIn.getFixedAcctName();
        BigDecimal amt = fixedDrawIn.getDrawAmt().negate();

        String sendSeqNo = addSendMsg("10", txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        fixedDrawIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + fixedDrawIn);
        FixedDrawOut fixedDrawOut = (FixedDrawOut) transferAndSend(fixedDrawIn, "FixedDrawOut");
        logger.info("返回给业务系统的数据:\n" + fixedDrawOut);

        String hostSeqNo = fixedDrawOut.getHostSeqNo();
        String status = fixedDrawOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = fixedDrawOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "", "", "", status, rtnMessage, "single");

        return fixedDrawOut;
    }

    @Override
    public FixedTransferModeSetOut sendMsg(FixedTransferModeSetIn fixedTransferModeSetIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(fixedTransferModeSetIn.getCenterHeadIn(), "BDC116");
        fixedTransferModeSetIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        fixedTransferModeSetIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + fixedTransferModeSetIn);
        FixedTransferModeSetOut fixedTransferModeSetOut = (FixedTransferModeSetOut) transferAndSend(fixedTransferModeSetIn, "FixedTransferModeSetOut");
        logger.info("返回给业务系统的数据:\n" + fixedTransferModeSetOut);

        String status = fixedTransferModeSetOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = fixedTransferModeSetOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return fixedTransferModeSetOut;
    }

    @Override
    public Actived2NoticeDepositOut sendMsg(Actived2NoticeDepositIn actived2NoticeDepositIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(actived2NoticeDepositIn.getCenterHeadIn(), "BDC117");
        actived2NoticeDepositIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = actived2NoticeDepositIn.getFixedAcctNo();
        String crAcctName = actived2NoticeDepositIn.getFixedAcctName();
        String deAcctNo = actived2NoticeDepositIn.getActivedAcctNo();
        String deAcctName = actived2NoticeDepositIn.getActivedAcctName();
        BigDecimal amt = actived2NoticeDepositIn.getNoticeDepositAmt().negate();

        String sendSeqNo = addSendMsg("11", txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        actived2NoticeDepositIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + actived2NoticeDepositIn);
        Actived2NoticeDepositOut actived2NoticeDepositOut = (Actived2NoticeDepositOut) transferAndSend(actived2NoticeDepositIn, "Actived2NoticeDepositOut");
        logger.info("返回给业务系统的数据:\n" + actived2NoticeDepositOut);

        String hostSeqNo = actived2NoticeDepositOut.getHostSeqNo();
        String status = actived2NoticeDepositOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = actived2NoticeDepositOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "", "", "", status, rtnMessage, "single");

        return actived2NoticeDepositOut;
    }

    @Override
    public NoticeDepositDrawOut sendMsg(NoticeDepositDrawIn noticeDepositDrawIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(noticeDepositDrawIn.getCenterHeadIn(), "BDC118");
        noticeDepositDrawIn.setCenterHeadIn(centerHeadIn);

        String businessSeqNo = centerHeadIn.getSendSeqNo();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();
        String sendDate = centerHeadIn.getSendDate();
        String crAcctNo = noticeDepositDrawIn.getActivedAcctNo();
        String crAcctName = noticeDepositDrawIn.getActivedAcctName();
        String deAcctNo = noticeDepositDrawIn.getFixedAcctNo();
        String deAcctName = noticeDepositDrawIn.getFixedAcctName();
        BigDecimal amt = noticeDepositDrawIn.getDrawAmt().negate();

        String sendSeqNo = addSendMsg("12", txCode, receiveNode, custNo, sendDate, businessSeqNo, crAcctNo, crAcctName, deAcctNo, deAcctName, amt);
        noticeDepositDrawIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + noticeDepositDrawIn);
        NoticeDepositDrawOut noticeDepositDrawOut = (NoticeDepositDrawOut) transferAndSend(noticeDepositDrawIn, "NoticeDepositDrawOut");
        logger.info("返回给业务系统的数据:\n" + noticeDepositDrawOut);

        String hostSeqNo = noticeDepositDrawOut.getHostSeqNo();
        String status = noticeDepositDrawOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = noticeDepositDrawOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, hostSeqNo, "", "", "", status, rtnMessage, "single");

        return noticeDepositDrawOut;
    }

    @Override
    public NoticeDepositDrawSetOut sendMsg(NoticeDepositDrawSetIn noticeDepositDrawSetIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(noticeDepositDrawSetIn.getCenterHeadIn(), "BDC119");
        noticeDepositDrawSetIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        noticeDepositDrawSetIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + noticeDepositDrawSetIn);
        NoticeDepositDrawSetOut noticeDepositDrawSetOut = (NoticeDepositDrawSetOut) transferAndSend(noticeDepositDrawSetIn, "NoticeDepositDrawSetOut");
        logger.info("返回给业务系统的数据:\n" + noticeDepositDrawSetOut);

        String status = noticeDepositDrawSetOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = noticeDepositDrawSetOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return noticeDepositDrawSetOut;
    }

    @Override
    public NoticeDepositDrawCancelOut sendMsg(NoticeDepositDrawCancelIn noticeDepositDrawCancelIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(noticeDepositDrawCancelIn.getCenterHeadIn(), "BDC120");
        noticeDepositDrawCancelIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        noticeDepositDrawCancelIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + noticeDepositDrawCancelIn);
        NoticeDepositDrawCancelOut noticeDepositDrawCancelOut = (NoticeDepositDrawCancelOut) transferAndSend(noticeDepositDrawCancelIn, "NoticeDepositDrawCancelOut");
        logger.info("返回给业务系统的数据:\n" + noticeDepositDrawCancelOut);

        String status = noticeDepositDrawCancelOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = noticeDepositDrawCancelOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return noticeDepositDrawCancelOut;
    }

    @Override
    public NoticeDepositDrawQueryOut sendMsg(NoticeDepositDrawQueryIn noticeDepositDrawQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(noticeDepositDrawQueryIn.getCenterHeadIn(), "BDC121");
        noticeDepositDrawQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        noticeDepositDrawQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + noticeDepositDrawQueryIn);
        NoticeDepositDrawQueryOut noticeDepositDrawQueryOut = (NoticeDepositDrawQueryOut) transferAndSend(noticeDepositDrawQueryIn, "NoticeDepositDrawQueryOut");
        logger.info("返回给业务系统的数据:\n" + noticeDepositDrawQueryOut);

        String status = noticeDepositDrawQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = noticeDepositDrawQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return noticeDepositDrawQueryOut;
    }

    @Override
    public FixedAccBalanceQueryOut sendMsg(FixedAccBalanceQueryIn fixedAccBalanceQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(fixedAccBalanceQueryIn.getCenterHeadIn(), "BDC122");
        fixedAccBalanceQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        fixedAccBalanceQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + fixedAccBalanceQueryIn);
        FixedAccBalanceQueryOut fixedAccBalanceQueryOut = (FixedAccBalanceQueryOut) transferAndSend(fixedAccBalanceQueryIn, "FixedAccBalanceQueryOut");
        logger.info("返回给业务系统的数据:\n" + fixedAccBalanceQueryOut);

        String status = fixedAccBalanceQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = fixedAccBalanceQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return fixedAccBalanceQueryOut;
    }

    @Override
    public ActivedAccBalanceQueryOut sendMsg(ActivedAccBalanceQueryIn activedAccBalanceQueryIn) throws Exception {
        CenterHeadIn centerHeadIn = getCenterHeadIn(activedAccBalanceQueryIn.getCenterHeadIn(), "BDC123");
        activedAccBalanceQueryIn.setCenterHeadIn(centerHeadIn);

        String sendDate = centerHeadIn.getSendDate();
        String txCode = centerHeadIn.getTxCode();
        String receiveNode = centerHeadIn.getReceiveNode();
        String custNo = centerHeadIn.getCustNo();

        String sendSeqNo = addSendMsg(null, txCode, receiveNode, custNo, sendDate, null, null, null, null, null, null);
        activedAccBalanceQueryIn.getCenterHeadIn().setSendSeqNo(sendSeqNo);

        logger.info("从业务系统收到的数据:\n" + activedAccBalanceQueryIn);
        ActivedAccBalanceQueryOut activedAccBalanceQueryOut = (ActivedAccBalanceQueryOut) transferAndSend(activedAccBalanceQueryIn, "ActivedAccBalanceQueryOut");
        logger.info("返回给业务系统的数据:\n" + activedAccBalanceQueryOut);

        String status = activedAccBalanceQueryOut.getCenterHeadOut().getTxStatus();
        String rtnMessage = activedAccBalanceQueryOut.getCenterHeadOut().getRtnMessage();

        this.addReceiveMsg(sendSeqNo, "", "", "", "", status, rtnMessage, "");

        return activedAccBalanceQueryOut;
    }

    @Override
    public boolean checkYWLSH(String ywlsh) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("business_seq_no", ywlsh);

        CBankSendSeqNo isExist = DAOBuilder.instance(icBankSendSeqNoDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                logger.error(LogUtil.getTrace(e));
            }
        });

        return isExist == null;
    }

    /**
     * 对象和xml报文的相互转换,并与住建部进行通信
     * @param obj 对象
     * @param returnType 返回的对象类型
     * @return 返回类型的对象
     * @throws Exception 异常
     */
    private static Object transferAndSend(Object obj, String returnType) throws Exception {

        if("true".equals(Configure.getInstance().getConfiguration("bank").getString("simulation"))){

            return BankImpl.transfer1AndSend(obj,returnType);
        }
        Message respMsg = null;
        Object respObj = null;
        Sender sender = new Sender();
        String respXml;
        String xml = null;

        try {
            com.handge.housingfund.common.service.bank.xmlbean.Message message = new ParmBean2XMLBean().transfer(obj);
            xml = JAXBUtil.toXML(message);
        } catch (Exception e) {
            logger.error("reqObj: " + obj + "\nreqXml: " + xml);
            logger.error(LogUtil.getTrace(e));
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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

//    模拟,不走结算平台
    private static Object transfer1AndSend(Object obj, String returnType, String ...businessSeqNo) throws Exception {
        Object respObj = null;
        String sendSeqNo = "";
        String[] datetime = DateUtil.getDatetime();
        BigDecimal amt = new BigDecimal("0.0");
        String account = "";
        String acctName = "";
        String opAccount = "";
        String opName = "";
        String summary = "摘要";
        int booolListNo = Math.abs((int)System.currentTimeMillis());
        boolean isKH = false; //是否跨行

        CenterHeadOut centerHeadOut = new CenterHeadOut();
        centerHeadOut.setTxStatus("0");
        centerHeadOut.setRtnCode("00000");
        centerHeadOut.setRtnMessage("success");

        if ("SinglePaymentOut".equals(returnType)){
            SinglePaymentIn singlePaymentIn = (SinglePaymentIn) obj;
            amt = singlePaymentIn.getAmt().negate();
            account = singlePaymentIn.getDeAcctNo();
            opAccount = singlePaymentIn.getCrAcctNo();
            opName = singlePaymentIn.getCrAcctName();
            sendSeqNo = singlePaymentIn.getCenterHeadIn().getSendSeqNo();
            summary = singlePaymentIn.getSummary();

            SinglePaymentOut singlePaymentOut = new SinglePaymentOut();
            singlePaymentOut.setCenterHeadOut(centerHeadOut);
            singlePaymentOut.setHostStatus("0");
            singlePaymentOut.setCapHostSeqNo(sendSeqNo);
            respObj = singlePaymentOut;
        } else if ("SingleCollectionOut".equals(returnType)) {
            SingleCollectionIn singleCollectionIn = (SingleCollectionIn) obj;
            amt = singleCollectionIn.getAmt();
            account = singleCollectionIn.getCrAcctNo();
            opAccount = singleCollectionIn.getDeAcctNo();
            opName = singleCollectionIn.getDeAcctName();
            sendSeqNo = singleCollectionIn.getCenterHeadIn().getSendSeqNo();
            summary = singleCollectionIn.getSummary();

            SingleCollectionOut singleCollectionOut = new SingleCollectionOut();
            singleCollectionOut.setCenterHeadOut(centerHeadOut);
            singleCollectionOut.setHostStatus("0");
            singleCollectionOut.setHostSeqNo(sendSeqNo);
            respObj = singleCollectionOut;
        } else if ("BatchCollectionOut".equals(returnType)) {
            BatchCollectionIn batchCollectionIn = (BatchCollectionIn) obj;
            amt = batchCollectionIn.getBatchTotalAmt();
            account = "123456789";
            opAccount = batchCollectionIn.getCrAcctNo();
            opName = batchCollectionIn.getCrAcctName();
            sendSeqNo = batchCollectionIn.getCenterHeadIn().getSendSeqNo();

            BatchCollectionOut batchCollectionOut = new BatchCollectionOut();
            batchCollectionOut.setBatchNo("5512");
            batchCollectionOut.setCenterHeadOut(centerHeadOut);
            respObj = batchCollectionOut;
        } else if ("Actived2FixedOut".equals(returnType)) {
            Actived2FixedIn actived2FixedIn = (Actived2FixedIn) obj;
            amt = actived2FixedIn.getAmt();
            account = actived2FixedIn.getActivedAcctNo();
            opAccount = actived2FixedIn.getFixedAcctNo();
            opName = actived2FixedIn.getFixedAcctName();
            sendSeqNo = actived2FixedIn.getCenterHeadIn().getSendSeqNo();

            Actived2FixedOut actived2FixedOut = new Actived2FixedOut();
            actived2FixedOut.setCenterHeadOut(centerHeadOut);
            actived2FixedOut.setHostSeqNo(sendSeqNo);
            actived2FixedOut.setBookNo("100");
            actived2FixedOut.setBookListNo(booolListNo);
            respObj = actived2FixedOut;
        } else if ("FixedDrawOut".equals(returnType)) {
            FixedDrawIn fixedDrawIn = (FixedDrawIn) obj;
            amt = fixedDrawIn.getDrawAmt();
            account = fixedDrawIn.getFixedAcctNo();
            opAccount = fixedDrawIn.getActivedAcctNo();
            opName = fixedDrawIn.getActivedAcctName();
            sendSeqNo = fixedDrawIn.getCenterHeadIn().getSendSeqNo();

            FixedDrawOut fixedDrawOut = new FixedDrawOut();
            fixedDrawOut.setCenterHeadOut(centerHeadOut);
            fixedDrawOut.setHostSeqNo(sendSeqNo);
            respObj = fixedDrawOut;
        } else if ("LoanDeductionOut".equals(returnType)) {
            LoanDeductionIn loanDeductionIn = (LoanDeductionIn) obj;
            amt = loanDeductionIn.getBatchTotalAmt();
            account = "123456789";
            opAccount = loanDeductionIn.getCrAcctNo();
            opName = loanDeductionIn.getCrAcctName();
            sendSeqNo = loanDeductionIn.getCenterHeadIn().getSendSeqNo();

            LoanDeductionOut loanDeductionOut = new LoanDeductionOut();
            loanDeductionOut.setCenterHeadOut(centerHeadOut);
            loanDeductionOut.setBatchNo(sendSeqNo);
            respObj = loanDeductionOut;
        } else if ("SingleTransferAccountOut".equals(returnType)) {
            SingleTransferAccountIn singleTransferAccountIn = (SingleTransferAccountIn) obj;
            amt = singleTransferAccountIn.getAmt().negate();
            account = singleTransferAccountIn.getDeAcctNo();
            acctName = singleTransferAccountIn.getDeAcctName();
            opAccount = singleTransferAccountIn.getCrAcctNo();
            opName = singleTransferAccountIn.getCrAcctName();
            sendSeqNo = singleTransferAccountIn.getCenterHeadIn().getSendSeqNo();
            summary = singleTransferAccountIn.getSummary();
            if ("1".equals(singleTransferAccountIn.getCrBankClass())) isKH = true;

            SingleTransferAccountOut singleTransferAccountOut = new SingleTransferAccountOut();
            singleTransferAccountOut.setCenterHeadOut(centerHeadOut);
            singleTransferAccountOut.setHostStatus("0");
            singleTransferAccountOut.setHostSeqNo(sendSeqNo);
            respObj = singleTransferAccountOut;
        } else return null;

        AccChangeNoticeFile accChangeNoticeFile = new AccChangeNoticeFile();
        accChangeNoticeFile.setAcct(account);
        accChangeNoticeFile.setAmt(amt);
        accChangeNoticeFile.setOpponentAcct(opAccount);
        accChangeNoticeFile.setOpponentName(opName);
        accChangeNoticeFile.setDate(datetime[0]);
        accChangeNoticeFile.setTime(datetime[1]);
        accChangeNoticeFile.setNo(sendSeqNo);
        accChangeNoticeFile.setBookNo("56");
        accChangeNoticeFile.setBookListNo(String.valueOf(booolListNo));
        accChangeNoticeFile.setHostSeqNo(sendSeqNo);
        accChangeNoticeFile.setCurrIden("1");
        accChangeNoticeFile.setCurrNo("156");
        accChangeNoticeFile.setBalance(new BigDecimal("1000000"));
        accChangeNoticeFile.setSummary(summary);
        accChangeNoticeFile.setRemark(businessSeqNo.length > 0 ? businessSeqNo[0] : "备注");

        String data = FileFieldConv.fieldASCtoBCD(TransactionFileFactory.getFileContent(Arrays.asList(accChangeNoticeFile)),"GBK");

        String finalSendSeqNo = sendSeqNo;
        new Thread(() -> handler("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<message> \n" +
                "  <body> \n" +
                "    <field-list name=\"FILE_LIST\"> \n" +
                "      <field-list name=\"0\"> \n" +
                "        <field name=\"NAME\">BDC_BAL_NTF_"+ System.currentTimeMillis() +".act</field>  \n" +
                "        <field name=\"DATA\">"+ data +"</field> \n" +
                "      </field-list> \n" +
                "    </field-list> \n" +
                "  </body>  \n" +
                "  <head> \n" +
                "    <field name=\"SendDate\">"+ datetime[0] +"</field>  \n" +
                "    <field name=\"SendTime\">"+ datetime[1] +"</field>  \n" +
                "    <field name=\"SendSeqNo\">" + finalSendSeqNo +"</field>  \n" +
                "    <field name=\"SendNode\">D00000</field>  \n" +
                "    <field name=\"TxCode\">SBDC100</field>  \n" +
                "    <field name=\"ReceiveNode\">C52240</field>  \n" +
                "    <field name=\"BDCDate\">"+ datetime[0] +"</field>  \n" +
                "    <field name=\"BDCTime\">"+ datetime[1] +"</field>  \n" +
                "    <field name=\"BDCSeqNo\">" + finalSendSeqNo +"</field> \n" +
                "  </head> \n" +
                "</message>\n", false)).start();

        if ("SingleTransferAccountOut".equals(returnType)) {
            AccChangeNoticeFile accChangeNoticeFile2 = new AccChangeNoticeFile();
            accChangeNoticeFile2.setAcct(opAccount);
            accChangeNoticeFile2.setAmt(amt.negate());
            accChangeNoticeFile2.setOpponentAcct(account);
            accChangeNoticeFile2.setOpponentName(acctName);
            accChangeNoticeFile2.setDate(datetime[0]);
            accChangeNoticeFile2.setTime(datetime[1]);
            accChangeNoticeFile2.setNo(sendSeqNo);
            accChangeNoticeFile2.setBookNo("56");
            accChangeNoticeFile2.setBookListNo(String.valueOf(booolListNo));
            if (isKH) accChangeNoticeFile2.setHostSeqNo(sendSeqNo + "KH");
            else accChangeNoticeFile2.setHostSeqNo(sendSeqNo);
            accChangeNoticeFile2.setCurrIden("1");
            accChangeNoticeFile2.setCurrNo("156");
            accChangeNoticeFile2.setBalance(new BigDecimal("1000000"));
            accChangeNoticeFile2.setSummary(summary);
            accChangeNoticeFile2.setRemark(businessSeqNo.length > 0 ? businessSeqNo[0] : "备注");

            String data2 = FileFieldConv.fieldASCtoBCD(TransactionFileFactory.getFileContent(Arrays.asList(accChangeNoticeFile2)),"GBK");

            String finalSendSeqNo2 = sendSeqNo;
            boolean finalIsKH = isKH;
            new Thread(() -> handler("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                    "<message> \n" +
                    "  <body> \n" +
                    "    <field-list name=\"FILE_LIST\"> \n" +
                    "      <field-list name=\"0\"> \n" +
                    "        <field name=\"NAME\">BDC_BAL_NTF_"+ System.currentTimeMillis() +".act</field>  \n" +
                    "        <field name=\"DATA\">"+ data2 +"</field> \n" +
                    "      </field-list> \n" +
                    "    </field-list> \n" +
                    "  </body>  \n" +
                    "  <head> \n" +
                    "    <field name=\"SendDate\">"+ datetime[0] +"</field>  \n" +
                    "    <field name=\"SendTime\">"+ datetime[1] +"</field>  \n" +
                    "    <field name=\"SendSeqNo\">" + finalSendSeqNo2 +"</field>  \n" +
                    "    <field name=\"SendNode\">D00000</field>  \n" +
                    "    <field name=\"TxCode\">SBDC100</field>  \n" +
                    "    <field name=\"ReceiveNode\">C52240</field>  \n" +
                    "    <field name=\"BDCDate\">"+ datetime[0] +"</field>  \n" +
                    "    <field name=\"BDCTime\">"+ datetime[1] +"</field>  \n" +
                    "    <field name=\"BDCSeqNo\">" + finalSendSeqNo2 +"</field> \n" +
                    "  </head> \n" +
                    "</message>\n", finalIsKH)).start();
        }

        return respObj;
    }

    private static void handler(String accChangeNoticeOutXml, boolean isKH) {
        try {
            Thread.sleep(10000L);
            if (isKH) Thread.sleep(290000L);
            logger.info("到账通知回复： " + new Sender().invoke2(accChangeNoticeOutXml));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private String addSendMsg(String busType, String txCode, String receiveNode, String custNo,
                              String sendDate, String busSeqNo, String crAcctNo, String crAcctName,
                              String deAcctNo, String deAcctName, BigDecimal amt, Object ...cBankBusinessList) {

        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setBus_type(busType);
        cBankSendSeqNo.setTxCode(txCode);
        cBankSendSeqNo.setReceive_node(receiveNode);
        cBankSendSeqNo.setCust_no(custNo);
        cBankSendSeqNo.setCr_acct_no(crAcctNo);
        cBankSendSeqNo.setCr_acct_name(crAcctName);
        cBankSendSeqNo.setDe_acct_no(deAcctNo);
        cBankSendSeqNo.setDe_acct_name(deAcctName);
        cBankSendSeqNo.setAmt(amt);

        try {
            cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd",sendDate));
        } catch (ParseException e) {
            throw new RuntimeException("发送日期格式有误");
        }
        cBankSendSeqNo.setBusiness_seq_no(busSeqNo);

        //默认设置TYPE为1或3，防止结算平台超时导致无法入账问题
        if (StringUtil.notEmpty(busSeqNo)) {
            if (cBankBusinessList.length == 0) {
                if (!"BDC102".equals(txCode)) cBankSendSeqNo.setType("1");
            }
            else cBankSendSeqNo.setType("3");
        }

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
     * @param rtnNo 主机流水号或批次编号
     * @param intHostSeqNo 利息银行主机流水号
     * @param penHostSeqNo 罚息银行主机流水号
     * @param fineHostSeqNo 违约金银行主机流水号
     * @param txStatus 交易状态
     * @param rtnMessage 交易返回信息
     * @param type 交易类型,single|batch
     */
    private void addReceiveMsg(String sendSeqNo, String rtnNo, String intHostSeqNo, String penHostSeqNo, String fineHostSeqNo, String txStatus, String rtnMessage, String type) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("send_seq_no", sendSeqNo);
        try {
            CBankSendSeqNo cBankSendSeqNo = icBankSendSeqNoDAO.list(filter, null, null,null, null, null, null).get(0);
            if ("single".equals(type)) {
                if (rtnNo == null || "".equals(rtnNo)){
                    cBankSendSeqNo.setType("1");
                } else {
                    cBankSendSeqNo.setHost_seq_no(rtnNo);
                    cBankSendSeqNo.setType("2");
                }
                cBankSendSeqNo.setInt_host_seq_no(intHostSeqNo);
                cBankSendSeqNo.setPen_host_seq_no(penHostSeqNo);
                cBankSendSeqNo.setFine_host_seq_no(fineHostSeqNo);
            }
            if ("batch".equals(type)) {
                if ("0".equals(txStatus)) cBankSendSeqNo.setType("3");
                cBankSendSeqNo.setBatch_no(rtnNo);
                cBankSendSeqNo.setHost_seq_no(rtnNo);//测试用，伪造到账通知
            }
            cBankSendSeqNo.setRtn_message(rtnMessage);
            cBankSendSeqNo.setTx_status(txStatus);

            icBankSendSeqNoDAO.update(cBankSendSeqNo);
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    private HashMap<String, String> getOldSendSeqNo(String oldBusSeqNo) throws Exception {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("business_seq_no", oldBusSeqNo);
        List<CBankSendSeqNo> cBankSendSeqNoList = icBankSendSeqNoDAO.list(filter, null, null,null, null, null, null);

        if (cBankSendSeqNoList.size() <= 0) throw new Exception("未查询到原业务流水号：" + oldBusSeqNo + "的相关信息.");
        HashMap<String, String> result = new HashMap<>();
        for (CBankSendSeqNo cBankSendSeqNo : cBankSendSeqNoList) {
            String oldSendSeqNo = cBankSendSeqNo.getSend_seq_no();
            String batchNo = cBankSendSeqNo.getBatch_no();
            String sendDate = DateUtil.date2Str(cBankSendSeqNo.getSend_date(),"yyyyMMdd");
            result.put("SendSeqNo", oldSendSeqNo);
            result.put("BatchNo", batchNo);
            result.put("SendDate", sendDate);
        }

        return result;
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
