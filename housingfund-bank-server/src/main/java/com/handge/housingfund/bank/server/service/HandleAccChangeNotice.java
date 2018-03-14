package com.handge.housingfund.bank.server.service;

import com.handge.housingfund.bank.server.IHandleAccChangeNotice;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.collection.service.trader.IAllochthounousBackCall;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionWithdrawlTrader;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IVoucherAutoService;
import com.handge.housingfund.common.service.loan.IBankCallService;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICBankAccChangeNoticeDAO;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.dao.ICLoanHousingBusinessProcessDAO;
import com.handge.housingfund.database.entities.CBankAccChangeNotice;
import com.handge.housingfund.database.entities.CBankBusiness;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.ParseException;
import java.util.*;

/**
 * 资金变动通知处理
 * Created by gxy on 17-8-16.
 */
@Service
public class HandleAccChangeNotice implements IHandleAccChangeNotice {
    private static Logger logger = LogManager.getLogger(HandleAccChangeNotice.class);

    private static ApplicationContext ac;

    @Autowired
    private ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Autowired
    private ICBankAccChangeNoticeDAO icBankAccChangeNoticeDAO;

    @Autowired
    private ICLoanHousingBusinessProcessDAO icLoanHousingBusinessProcessDAO;

    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    @Autowired
    private HibernateTransactionManager txManager;

    public static void init(ApplicationContext aC) {
        ac = aC;
    }

    @Override
    public void handler(AccChangeNoticeOut accChangeNoticeOut) {
        try {
            //当变动通知到达时,更新c_bank_sendseqno表,获取已经成功交易的银行处理结果信息
            if(!"true".equals(Configure.getInstance().getConfiguration("bank").getString("simulation"))){

                updateHostSeqNo();
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
        }
        List<Object> accChangeNoticeFiles = new ArrayList<>();
        try {
            accChangeNoticeFiles = TransactionFileFactory.getObjFromFile(accChangeNoticeOut.getFileList().getDATA(), AccChangeNoticeFile.class.getName());
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
        }

        //根据银行主机流水号查询业务流水号,把资金变动通知推送到对应的业务接口上
        for (Object o : accChangeNoticeFiles) {
            AccChangeNoticeFile accChangeNoticeFile = (AccChangeNoticeFile) o;

            //判断此通知是否已经收到过，如果已收到过就不在接收
            HashMap<String, Object> hasExistFilter = new HashMap<>();
            hasExistFilter.put("acct", accChangeNoticeFile.getAcct());
            hasExistFilter.put("amt", accChangeNoticeFile.getAmt());
            hasExistFilter.put("opponent_acct", accChangeNoticeFile.getOpponentAcct());
            hasExistFilter.put("opponent_name", accChangeNoticeFile.getOpponentName());
            hasExistFilter.put("host_seq_no", accChangeNoticeFile.getHostSeqNo());

            CBankAccChangeNotice hasExsitNotice = DAOBuilder.instance(icBankAccChangeNoticeDAO).searchFilter(hasExistFilter)
                    .getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
            if (hasExsitNotice != null) continue;

            //通过主机流水号或摘要备注等匹配业务
            CBankSendSeqNo cBankSendSeqNo = DAOBuilder.instance(icBankSendSeqNoDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getSummary())).length() == 15) {

                        if (StringUtil.notEmpty(accChangeNoticeFile.getRemark()) && StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getRemark())).length() == 15) {
                            criteria.add(Restrictions.or(Restrictions.eq("host_seq_no", accChangeNoticeFile.getHostSeqNo()),
                                    Restrictions.eq("business_seq_no", StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getSummary()))),
                                    Restrictions.eq("business_seq_no", StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getRemark())))));
                        } else {
                            criteria.add(Restrictions.or(Restrictions.eq("host_seq_no", accChangeNoticeFile.getHostSeqNo()),
                                    Restrictions.eq("business_seq_no", StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getSummary())))));
                        }
                    } else {
                        if (StringUtil.notEmpty(accChangeNoticeFile.getRemark()) && StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getRemark())).length() == 15) {
                            criteria.add(Restrictions.or(Restrictions.eq("host_seq_no", accChangeNoticeFile.getHostSeqNo()),
                                    Restrictions.eq("business_seq_no", StringUtil.subStr(StringUtil.toDBC(accChangeNoticeFile.getRemark())))));
                        } else {
                            criteria.add(Restrictions.or(Restrictions.eq("host_seq_no", accChangeNoticeFile.getHostSeqNo())));
                        }
                    }
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    logger.error(LogUtil.getTrace(e));
                }
            });

            CBankAccChangeNotice cBankAccChangeNotice = new CBankAccChangeNotice();
            cBankAccChangeNotice.setAcct(accChangeNoticeFile.getAcct());
            cBankAccChangeNotice.setHost_seq_no(accChangeNoticeFile.getHostSeqNo());
            cBankAccChangeNotice.setTx_code(accChangeNoticeFile.getTxCode());
            cBankAccChangeNotice.setOpponent_acct(accChangeNoticeFile.getOpponentAcct());
            cBankAccChangeNotice.setOpponent_name(accChangeNoticeFile.getOpponentName());
            cBankAccChangeNotice.setAmt(accChangeNoticeFile.getAmt());
            try {
                cBankAccChangeNotice.setDate(DateUtil.str2Date("yyyyMMdd", accChangeNoticeFile.getDate()));
            } catch (ParseException e) {
                logger.error(LogUtil.getTrace(e));
            }
            cBankAccChangeNotice.setTime(accChangeNoticeFile.getTime());
            cBankAccChangeNotice.setAvailable_amt(accChangeNoticeFile.getAvailableAmt());
            cBankAccChangeNotice.setOpen_bank_no(accChangeNoticeFile.getOpenBankNo());
            cBankAccChangeNotice.setRemark(accChangeNoticeFile.getRemark());
            cBankAccChangeNotice.setCurr_no(accChangeNoticeFile.getCurrNo());
            cBankAccChangeNotice.setCurr_iden(accChangeNoticeFile.getCurrIden());
            cBankAccChangeNotice.setBalance(accChangeNoticeFile.getBalance());
            cBankAccChangeNotice.setOverdraft(accChangeNoticeFile.getOverdraft());
            cBankAccChangeNotice.setVoucher_type(accChangeNoticeFile.getVoucherType());
            cBankAccChangeNotice.setVoucher_no(accChangeNoticeFile.getVoucherNo());
            cBankAccChangeNotice.setOpponent_bank_no(accChangeNoticeFile.getOpponentBankNo());
            cBankAccChangeNotice.setSummary(accChangeNoticeFile.getSummary());
            cBankAccChangeNotice.setRedo(accChangeNoticeFile.getRedo());
            cBankAccChangeNotice.setBook_list_no(accChangeNoticeFile.getBookListNo());
            cBankAccChangeNotice.setBook_no(accChangeNoticeFile.getBookNo());
            if (cBankSendSeqNo != null) {
                cBankAccChangeNotice.setBus_seq_no(cBankSendSeqNo.getBusiness_seq_no());
            }
            cBankAccChangeNotice.setIs_make_acc("0");

            //如果是工行的贷款扣款通知，则不调业务回调
            if ("71050".equals(accChangeNoticeFile.getTxCode()) &&
                    "null".equals(accChangeNoticeFile.getOpponentAcct()) &&
                    "".equals(accChangeNoticeFile.getOpponentName())) {
                cBankAccChangeNotice.setIs_make_acc("1");
                cBankAccChangeNotice.setSummary("工行贷款扣款");

                //资金变动通知入库
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
                TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
                String id = icBankAccChangeNoticeDAO.save(cBankAccChangeNotice);
                try {
                    txManager.commit(status);
                } catch (Exception e) {
                    txManager.rollback(status);
                }
                continue;
            }

            //查询此通知是否是失败业务的退回
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("acct", cBankAccChangeNotice.getAcct());
            filter.put("amt", cBankAccChangeNotice.getAmt().negate());
            filter.put("opponent_acct", cBankAccChangeNotice.getOpponent_acct());
            filter.put("opponent_name", cBankAccChangeNotice.getOpponent_name());

            CBankAccChangeNotice cBankAccChangeNoticeP = DAOBuilder.instance(icBankAccChangeNoticeDAO)
                    .searchFilter(filter)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            Date start = null;
                            try {
                                start = DateUtil.str2Date("yyyyMMdd", DateUtil.getDateFromNow(Calendar.DAY_OF_YEAR, -10,"yyyyMMdd"));
                            } catch (ParseException ignored) {
                            }

                            if (start != null) {
                                criteria.add(Restrictions.between("date", start, new Date()));
                            }
                        }
                    }).orderOption("created_at", Order.DESC)
                    .getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

            if (cBankAccChangeNoticeP != null) {
                if (cBankAccChangeNoticeP.getBus_seq_no() != null) {
                    if ("02".equals(cBankAccChangeNoticeP.getBus_seq_no().substring(0,2))) cBankAccChangeNoticeP = null;
                } else
                    cBankAccChangeNoticeP = null;
            }

            if (cBankAccChangeNoticeP != null) {
                cBankAccChangeNotice.setBus_seq_no(cBankAccChangeNoticeP.getBus_seq_no());
            }

            //资金变动通知入库
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
            TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
            String id = icBankAccChangeNoticeDAO.save(cBankAccChangeNotice);
            try {
                txManager.commit(status);
            } catch (Exception e) {
                txManager.rollback(status);
            }

            //判断交易的账户是否都是中心的账户,如果是则必须两笔通知都到才做凭证
            if (cBankAccChangeNoticeP == null &&
                    iSettlementSpecialBankAccountManageService.isSpecialAccountByZHHM(cBankAccChangeNotice.getAcct()) &&
                    iSettlementSpecialBankAccountManageService.isSpecialAccountByZHHM(cBankAccChangeNotice.getOpponent_acct())) {
                filter.clear();
                filter.put("amt", cBankAccChangeNotice.getAmt().negate());
                CBankAccChangeNotice cBankAccChangeNoticeT = DAOBuilder.instance(icBankAccChangeNoticeDAO)
                        .searchFilter(filter)
                        .extension(new IBaseDAO.CriteriaExtension() {
                            @Override
                            public void extend(Criteria criteria) {
                                criteria.add(
                                        Restrictions.or(
                                                Restrictions.eq("host_seq_no", cBankAccChangeNotice.getHost_seq_no()),
                                                Restrictions.eq("bus_seq_no", cBankAccChangeNotice.getBus_seq_no()))
                                );
                            }
                        })
                        .getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }
                        });
                if (cBankAccChangeNoticeT == null) {
                    continue;
                }
            }

            //判断属于哪个业务,并推送到对应的接口上
            String callBackMethod = "";
            AccChangeNotice accChangeNotice = new AccChangeNotice();
            try {
                String module = "未知";
                String busType = "未知";
                String isSuccess = "Success";
                boolean isBack = false;

                if (cBankSendSeqNo != null) {
                    cBankSendSeqNo.setReceive_seq_no(accChangeNoticeOut.getSysHeadOut().getSendSeqNo());
                    try {
                        status = txManager.getTransaction(def); // 获得事务状态
                        icBankSendSeqNoDAO.update(cBankSendSeqNo);
                        txManager.commit(status);
                    } catch (Exception e) {
                        txManager.rollback(status);
                    }

                    String businessSeqNo = cBankSendSeqNo.getBusiness_seq_no();
                    accChangeNoticeFile.setNo(businessSeqNo);
                    module = businessSeqNo.substring(0, 2);
                    busType = cBankSendSeqNo.getBus_type();
                } else if (cBankAccChangeNoticeP != null) { //失败退回
                    String businessSeqNo = cBankAccChangeNoticeP.getBus_seq_no();
                    accChangeNoticeFile.setNo(businessSeqNo);
                    module = businessSeqNo.substring(0, 2);
                    isBack = true;
                } else { //三无通知
                    accChangeNoticeFile.setNo(id);
                }

                accChangeNotice.setSysHeadOut(accChangeNoticeOut.getSysHeadOut());
                accChangeNotice.setAccChangeNoticeFile(accChangeNoticeFile);

                switch (module) {
                    case "01": //提取
                        ICollectionWithdrawlTrader iCollectionWithdrawlTrader = (ICollectionWithdrawlTrader) ac.getBean("iCollectionWithdrawlTrader");
                        switch (busType) {
                            case "02": //部分提取
                                callBackMethod = "iCollectionWithdrawlTrader.sendWithdrawlNotice";
                                iCollectionWithdrawlTrader.sendWithdrawlNotice(accChangeNotice);
                                break;
                            case "03": //销户提取
                                callBackMethod = "iCollectionWithdrawlTrader.sendWithdrawlNotice";
                                iCollectionWithdrawlTrader.sendWithdrawlNotice(accChangeNotice);
                                break;
                            case "07": //外部转出
                                IAllochthounousBackCall iAllochthounousBackCall = (IAllochthounousBackCall) ac.getBean("IAllochthounousBackCall");
                                callBackMethod = "iAllochthounousBackCall.transferOutPayCallBack";
                                iAllochthounousBackCall.transferOutPayCallBack(accChangeNotice);
                                break;
                            default:
                                if (isBack) {
                                    callBackMethod = "iCollectionWithdrawlTrader.sendFailedWithdrawlNotice";
                                    iCollectionWithdrawlTrader.sendFailedWithdrawlNotice(accChangeNotice);
                                }
                        }
                        break;
                    case "02": //归集
                        ICollectionTrader iCollectionTrader = (ICollectionTrader) ac.getBean("iCollectionTrader");
                        switch (busType) {
                            case "01": //汇补缴
                                callBackMethod = "iCollectionTrader.sendPaymentNotice";
                                iCollectionTrader.sendPaymentNotice(accChangeNotice, id);
                                break;
                            case "3382": //错缴
                                callBackMethod = "iCollectionTrader.sendPayWrongNotice";
                                iCollectionTrader.sendPayWrongNotice(accChangeNotice);
                                break;
                        }
                        break;
                    case "05": //贷款
                        IBankCallService iBankCallService = (IBankCallService) ac.getBean("bankCallService");
                        busType = icLoanHousingBusinessProcessDAO.getLoanBuniess(cBankSendSeqNo.getBusiness_seq_no());
                        switch (busType) {
                            case "01": //放款
                                callBackMethod = "iBankCallService.putLoan";
                                iBankCallService.putLoan(accChangeNotice);
                                break;
                            case "02": //正常还款
                                callBackMethod = "iBankCallService.putrepayment";
                                iBankCallService.putrepayment(accChangeNotice);
                                break;
                            case "03": //提前还款
                                callBackMethod = "iBankCallService.putLoanApply";
                                iBankCallService.putLoanApply(accChangeNotice);
                                break;
                            case "04": //逾期还款
                                callBackMethod = "iBankCallService.overdueAutomatic";
                                iBankCallService.overdueAutomatic(accChangeNotice);
                                break;
                        }
                        break;
                    case "08": //日常财务处理
                        IVoucherAutoService iVoucherAutoService = (IVoucherAutoService) ac.getBean("iVoucherAutoService");
                        if (!isBack) { //正常业务
                            callBackMethod = "iVoucherAutoService.transferAccounts";
                            iVoucherAutoService.transferAccounts(accChangeNotice);
                        } else {
                            callBackMethod = "iVoucherAutoService.transferAccountsFail";
                            iVoucherAutoService.transferAccountsFail(accChangeNotice);
                        }
                        break;
                    case "09": //活期转定期
                        IFinanceTrader actived2FixedTrader = (IFinanceTrader) ac.getBean("iFinanceTrader");
                        callBackMethod = "actived2FixedTrader.actived2FixedNotice";
                        isSuccess = actived2FixedTrader.actived2FixedNotice(accChangeNotice);
                        break;
                    case "10": //定期支取
                        IFinanceTrader fixedDrawTrader = (IFinanceTrader) ac.getBean("iFinanceTrader");
                        callBackMethod = "fixedDrawTrader.fixedDrawNotice";
                        isSuccess = fixedDrawTrader.fixedDrawNotice(accChangeNotice);
                        break;
                    default: //天外来物
                        IVoucherAutoService iVoucherAutoService1 = (IVoucherAutoService) ac.getBean("iVoucherAutoService");
                        if (IdcardValidator.isValidatedAllIdcard(accChangeNoticeFile.getSummary())) {
                            callBackMethod = "iVoucherAutoService.addexternalTransferAccounts";
                            iVoucherAutoService1.addexternalTransferAccounts(accChangeNotice);
                        } else {
                            callBackMethod = "iVoucherAutoService.checkBusiness";
                            iVoucherAutoService1.checkBusiness(accChangeNotice);
                        }
                        break;
                }
                logger.info("CallBackMethod: " + callBackMethod + ", " + isSuccess);
                logger.info("Notice: " + accChangeNotice);
            } catch (Exception e) {
                logger.error("CallBackMethod: " + callBackMethod);
                logger.error("Notice: " + accChangeNotice);
                logger.error(LogUtil.getTrace(e));
            }
        }
    }

    private void updateHostSeqNo() throws Exception {

        IBank iBank = (IBank) ac.getBean("iBank");

        List<String> values = new ArrayList<>();
        values.add("1");
        values.add("3");
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("type", values);
        List<CBankSendSeqNo> cBankSendSeqNoList = icBankSendSeqNoDAO.list(
                filter, null, null, null, null, null, null);

        /**
         * type : 类型(1:单笔交易成功且无交易结果,2:单笔交易成功有交易结果,3:批量交易成功且无交易结果,4:批量交易且成功有交易结果,null:交易失败)
         * txStatus: 0:成功 1:失败
         * oldTxStatus: 0:成功 1:失败 2:处理中或状态未知
         * batchTxStatus: 0:处理中 1:处理完成 2:部分处理完成 3:处理失败
         */
        for (CBankSendSeqNo cBankSendSeqNo : cBankSendSeqNoList) {
            String busSeqNo = cBankSendSeqNo.getBusiness_seq_no();
            String txDate = DateUtil.date2Str(cBankSendSeqNo.getSend_date(), "yyyyMMdd");
            String receiveNode = cBankSendSeqNo.getReceive_node();
            String custNo = cBankSendSeqNo.getCust_no();
            //单笔业务,通过交易结果查询(BDC110)接口查询业务结果
            if ("1".equals(cBankSendSeqNo.getType())) {
                CenterHeadIn centerHeadIn = getCenterHeadIn(receiveNode, "BDC110", custNo);

                TransactionResultQueryOut transactionResultQueryOut = iBank.sendMsg(new TransactionResultQueryIn(centerHeadIn, txDate, busSeqNo));
                if ("0".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {
                    cBankSendSeqNo.setHost_seq_no(transactionResultQueryOut.getHostSeqNo());
                    cBankSendSeqNo.setInt_host_seq_no(transactionResultQueryOut.getIntHostSeqN());
                    cBankSendSeqNo.setPen_host_seq_no(transactionResultQueryOut.getPenHostSeqNo());
                    cBankSendSeqNo.setFine_host_seq_no(transactionResultQueryOut.getFineHostSeqNo());
                    cBankSendSeqNo.setBatch_no(transactionResultQueryOut.getOldBatchNo());
                    cBankSendSeqNo.setTx_status(transactionResultQueryOut.getOldTxStatus());
                    if (StringUtil.notEmpty(transactionResultQueryOut.getHostSeqNo())) cBankSendSeqNo.setType("2");
                    if ("1".equals(transactionResultQueryOut.getOldTxStatus())) cBankSendSeqNo.setType(null);
                } else if ("1".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {
                    cBankSendSeqNo.setType(null);
                }

                icBankSendSeqNoDAO.update(cBankSendSeqNo);
            }

            //批量业务,通过批量业务结果下载(BDC108)接口下载批量业务结果
            if ("3".equals(cBankSendSeqNo.getType())) {
                CenterHeadIn centerHeadIn = getCenterHeadIn(receiveNode, "BDC107", custNo);

                BatchResultQueryOut batchResultQueryOut = iBank.sendMsg(new BatchResultQueryIn(centerHeadIn, busSeqNo));
                if ("0".equals(batchResultQueryOut.getCenterHeadOut().getTxStatus())) {
                    if ("1".equals(batchResultQueryOut.getBatchTxStatus())) {
                        centerHeadIn = getCenterHeadIn(receiveNode, "BDC108", custNo);

                        BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(new BatchResultDownloadIn(centerHeadIn, "3", busSeqNo));
                        List<Object> batchResultFileDetails = TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName());
                        HashMap<String, BatchResultFileDetail> batchResultFileDetailHashMap = new HashMap<>();
                        logger.info("????????????????????\n" + batchResultFileDetails);
                        for (Object o : batchResultFileDetails) {
                            BatchResultFileDetail batchResultFileDetail = (BatchResultFileDetail) o;
                            batchResultFileDetailHashMap.put(batchResultFileDetail.getNo(), batchResultFileDetail);
                            if ("00000".equals(batchResultFileDetail.getRtnCode())) {
                                if ("104000".equals(centerHeadIn.getReceiveNode()) && batchResultFileDetail.getHostSeqNo().startsWith("0"))
                                    cBankSendSeqNo.setHost_seq_no(batchResultFileDetail.getHostSeqNo().substring(1));
                                else
                                    cBankSendSeqNo.setHost_seq_no(batchResultFileDetail.getHostSeqNo());
                                break;
                            }
                        }

                        logger.info("-------------------++++++++++++++++++");
                        logger.info(batchResultFileDetailHashMap);

                        for (CBankBusiness cBankBusiness : cBankSendSeqNo.getcBankBusinesses()) {
                            logger.info(cBankBusiness);
                            if (batchResultFileDetailHashMap.get(cBankBusiness.getNo()) != null) {
                                cBankBusiness.setRtn_code(batchResultFileDetailHashMap.get(cBankBusiness.getNo()).getRtnCode());
                                cBankBusiness.setRtn_message(batchResultFileDetailHashMap.get(cBankBusiness.getNo()).getRtnMsg());
                            }
                        }
                        logger.info("++++++++++++++++++--------------------");
                        logger.info(cBankSendSeqNo);

                        cBankSendSeqNo.setType("4");

                        //如果是工行的贷款扣款业务，则在此调业务回调
//                    if ("102000".equals(cBankSendSeqNo.getReceive_node())) {
                        //bug_v1.2.19 修改为所有银行贷款扣款
                        String callBackMethod = "";
                        String ywlsh = cBankSendSeqNo.getBusiness_seq_no();
                        String zhhm = cBankSendSeqNo.getCr_acct_no();
                        try {
                            IBankCallService iBankCallService = (IBankCallService) ac.getBean("bankCallService");
                            String busType = icLoanHousingBusinessProcessDAO.getLoanBuniess(ywlsh);
                            switch (busType) {
                                case "02": //正常还款
                                    callBackMethod = "iBankCallService.putrepaymentYwlsh";
                                    iBankCallService.putrepaymentYwlsh(ywlsh, zhhm);
                                    break;
                                case "03": //提前还款
                                    callBackMethod = "iBankCallService.putLoanApplyYwlsh";
                                    iBankCallService.putLoanApplyYwlsh(ywlsh, zhhm);
                                    break;
                                case "04": //逾期还款
                                    callBackMethod = "iBankCallService.overdueAutomaticYwlsh";
                                    iBankCallService.overdueAutomaticYwlsh(ywlsh, zhhm);
                                    break;
                            }

                            logger.info("CallBackMethod: " + callBackMethod + ", Success");
                            logger.info("ywlsh: " + ywlsh);
                        } catch (Exception e) {
                            logger.error("CallBackMethod: " + callBackMethod);
                            logger.error("ywlsh: " + ywlsh);
                            logger.error(LogUtil.getTrace(e));
                            cBankSendSeqNo.setType("3");
                        }
//                    }
                    } else if ("3".equals(batchResultQueryOut.getBatchTxStatus())) {
                        cBankSendSeqNo.setType(null);
                        cBankSendSeqNo.setRtn_message(batchResultQueryOut.getSummary());
                        cBankSendSeqNo.setTx_status(batchResultQueryOut.getBatchTxStatus());
                    }
                }  else if ("1".equals(batchResultQueryOut.getCenterHeadOut().getTxStatus())) {
                    cBankSendSeqNo.setType(null);
                }

                icBankSendSeqNoDAO.update(cBankSendSeqNo);
            }
        }
    }

    private CenterHeadIn getCenterHeadIn(String receiveNode, String txCode, String custNo) {
        Configuration cfg = Configure.getInstance().getConfiguration("bank");
        String[] datetime = DateUtil.getDatetime();
        CenterHeadIn centerHeadIn = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "",
                cfg.getString("txUnitNo"),
                cfg.getString("nodeNo"),
                txCode,
                receiveNode,
                cfg.getString("operNo")
        );

        centerHeadIn.setCustNo(custNo);
        return centerHeadIn;
    }
}