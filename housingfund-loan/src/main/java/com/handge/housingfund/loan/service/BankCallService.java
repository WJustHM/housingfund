package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.BatchResultDownloadIn;
import com.handge.housingfund.common.service.bank.bean.center.BatchResultDownloadOut;
import com.handge.housingfund.common.service.bank.bean.center.BatchResultFileDetail;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.IBankCallService;
import com.handge.housingfund.common.service.loan.IExceptionMethod;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CurrentPeriodRange;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.enums.TaskBusinessStatus;
import com.handge.housingfund.database.enums.TaskBusinessType;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tanyi on 2017/8/9.
 */
@SuppressWarnings({"Convert2Lambda", "Duplicates", "deprecation"})
@Component
public class BankCallService implements IBankCallService, Serializable {
    private static Logger logger = LogManager.getLogger(BankCallService.class);
    private static Object lock = new Object();

    @Autowired
    private ICLoanHousingLoanDAO loanHousingLoanDAO;
    //银行
    @Autowired
    private IBank iBank;
    //个人贷款业务流程表
    @Autowired
    private ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    //个人贷款账户
    @Autowired
    private IStHousingPersonalAccountDAO stHousingPersonalAccount;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icLoanHousingPersonInformationBasicDAO;
    @Autowired
    private IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;
    //逾期记录
    @Autowired
    private IStHousingOverdueRegistrationDAO stHousingOverdueRegistrationDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icloanHousingPersonInformationBasicDAO;
    @Autowired
    private IExceptionMethod exceptionMethod;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    ICAuditHistoryDAO auditHistoryDAO;
    @Autowired
    private ISMSCommon ismsCommon;

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simM = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void putLoan(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }
        String BDCSeqNo = accChangeNoticeFile.getNo();
        String ReceiveDate = accChangeNoticeFile.getDate();
        String summary = accChangeNoticeFile.getSummary();

        if (!DateUtil.isFollowFormat(ReceiveDate, "yyyyMMdd", false)) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "交易日期");
        }

        CLoanHousingLoan cLoanHousingLoans = DAOBuilder.instance(this.loanHousingLoanDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", BDCSeqNo);
            this.put("state", 0);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });
        if (cLoanHousingLoans == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "发放贷款记录不存在");
        }

        CLoanHousingPersonInformationBasic cLoanHousingPersonInformationBasic = DAOBuilder.instance(icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("dkzh", cLoanHousingLoans.getDkzh());
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingPersonInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账户基础信息不存在");
        }
        cLoanHousingPersonInformationBasic.setDkzhzt(LoanAccountType.正常.getCode());
        cLoanHousingLoans.setState(1);

        cLoanHousingLoans.setDkffrq(DateUtil.safeStr2Date("yyyyMMdd", ReceiveDate));

        StHousingPersonalAccount personalAccount = cLoanHousingPersonInformationBasic.getPersonalAccount();

        if (personalAccount.getcLoanHousingPersonalAccountExtension() == null) {

            personalAccount.setcLoanHousingPersonalAccountExtension(new CLoanHousingPersonalAccountExtension());
        }
        personalAccount.getcLoanHousingPersonalAccountExtension().setDkgbjhye(new BigDecimal(cLoanHousingLoans.getDkffe()));
        personalAccount.getcLoanHousingPersonalAccountExtension().setDkgbjhqs(personalAccount.getDkqs());
        personalAccount.getcLoanHousingPersonalAccountExtension().setDkxffrq(cLoanHousingLoans.getDkffrq());
        personalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(new BigDecimal(cLoanHousingLoans.getDkffe()));
        personalAccount.getcLoanHousingPersonalAccountExtension().setDqqc(BigDecimal.ONE);
        personalAccount.setDkffrq(DateUtil.safeStr2Date("yyyyMMdd", ReceiveDate));
        personalAccount.setDkjqrq(CommLoanAlgorithm.settlementDate(personalAccount.getDkffrq(), personalAccount.getDkqs().intValue()));

        if (cLoanHousingPersonInformationBasic.getLoanContract() != null) {

            cLoanHousingPersonInformationBasic.getLoanContract().setYdfkrq(cLoanHousingLoans.getDkffrq());
            Calendar cal = Calendar.getInstance();
            cal.setTime(cLoanHousingLoans.getDkffrq());
            cLoanHousingPersonInformationBasic.getLoanContract().setYdhkr(cal.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + cal.get(Calendar.DAY_OF_MONTH)) : (cal.get(Calendar.DAY_OF_MONTH) + ""));
            cLoanHousingPersonInformationBasic.getLoanContract().setYddqrq(DateUtils.addMonths(cLoanHousingLoans.getDkffrq(), cLoanHousingPersonInformationBasic.getLoanContract().getDkqs().intValue()));
        }

        DAOBuilder.instance(this.stHousingPersonalAccount).entity(personalAccount).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).entity(cLoanHousingPersonInformationBasic).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        DAOBuilder.instance(this.loanHousingLoanDAO).entity(cLoanHousingLoans).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = DAOBuilder.instance(cloanHousingBusinessProcess).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", cLoanHousingLoans.getYwlsh());

        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        cLoanHousingBusinessProcess.setStep(LoanBussinessStatus.已入账.getName());
        cLoanHousingBusinessProcess.setBjsj(new Date());
        this.iSaveAuditHistory.saveNormalBusiness(cLoanHousingLoans.getYwlsh(), LoanBusinessType.贷款发放.getName(), "办结");

        DAOBuilder.instance(cloanHousingBusinessProcess).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        StHousingBusinessDetails housingBusinessDetails_search = DAOBuilder.instance(this.stHousingBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", cLoanHousingLoans.getYwlsh());
            this.put("dkzh", cLoanHousingLoans.getDkzh());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        StHousingBusinessDetails stHousingBusinessDetails = housingBusinessDetails_search != null ? housingBusinessDetails_search : new StHousingBusinessDetails();
        stHousingBusinessDetails.setDkzh(cLoanHousingLoans.getDkzh());
        stHousingBusinessDetails.setYwlsh(cLoanHousingLoans.getYwlsh());
        stHousingBusinessDetails.setDkywmxlx(LoanBusinessType.贷款发放.getCode());
        stHousingBusinessDetails.setYwfsrq(cLoanHousingBusinessProcess.getBlsj());
        CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(accChangeNoticeFile.getAcct());
        String dkyhdm = centerAccountInfo.getCode();
        stHousingBusinessDetails.setDkyhdm(dkyhdm);//根据银行名称获取数据库
        stHousingBusinessDetails.setFse(new BigDecimal(cLoanHousingLoans.getDkffe()));
        stHousingBusinessDetails.setBjje(new BigDecimal(cLoanHousingLoans.getDkffe()));
        stHousingBusinessDetails.setLxje(new BigDecimal("0.00"));
        stHousingBusinessDetails.setFxje(new BigDecimal("0.00"));
        stHousingBusinessDetails.setJzrq(new Date());
        stHousingBusinessDetails.setGrywmx(cLoanHousingBusinessProcess);

        DAOBuilder.instance(stHousingBusinessDetailsDAO).entity(stHousingBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        try {
            this.ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasic.getSjhm(), SMSTemp.贷款发放.getCode(),
                    new ArrayList<String>() {{
                        this.add(cLoanHousingPersonInformationBasic.getLoanContract().getJkrxm());
                        this.add(cLoanHousingLoans.getDkffe() + "");
                        this.add(cLoanHousingPersonInformationBasic.getLoanContract().getYdhkr());
                        this.add(personalAccount.getDqyhje().setScale(2, RoundingMode.HALF_UP) + "");
                    }}
            );
        } catch (Exception e) {
            LogManager.getLogger(this.getClass()).info("贷款发放短信发送失败:" + e.getMessage());
        }

        //region 生成记账凭证
        if (accChangeNotice.getAccChangeNoticeFile() != null) {

            accChangeNotice.getAccChangeNoticeFile().setSummary(cLoanHousingLoans.getJkrxm() + " 贷款发放");
        }

        CAuditHistory cAuditHistory = DAOBuilder.instance(this.auditHistoryDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", cLoanHousingBusinessProcess.getYwlsh());
        }}).orderOption("created_at", Order.DESC).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", cLoanHousingBusinessProcess.getCzy(),
                cAuditHistory == null ? cLoanHousingBusinessProcess.getCzy() : cAuditHistory.getCzy(), "", "管理员",
                VoucherBusinessType.贷款发放.getCode(), VoucherBusinessType.贷款发放.getCode(), accChangeNotice, personalAccount.getStHousingPersonalLoan().getZhkhyhdm());
        if (voucherRes.getJZPZH() != null) {
            cLoanHousingBusinessProcess.setJzpzh(voucherRes.getJZPZH());
        } else {
            cLoanHousingBusinessProcess.setJzpzh(null);
            cLoanHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
            cLoanHousingLoans.setSbyy(voucherRes.getMSG());

            DAOBuilder.instance(this.loanHousingLoanDAO).entity(cLoanHousingLoans).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
        DAOBuilder.instance(cloanHousingBusinessProcess).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

    }


    @Override
    public void putrepayment(AccChangeNotice accChangeNotice) {

        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }
        String summary = accChangeNoticeFile.getSummary();
        BigDecimal jzbjje = BigDecimal.ZERO;
        BigDecimal jzfse = BigDecimal.ZERO;
        BigDecimal jzlxje = BigDecimal.ZERO;
        String yhdm = null;

        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(accChangeNotice.getAccChangeNoticeFile().getNo()))
                    this.put("ywlsh", accChangeNotice.getAccChangeNoticeFile().getNo());
                this.put("cznr", TaskBusinessType.正常还款.getCode());
            }}, null, null, null, null, null, null);
            yhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {

                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(accChangeNotice.getAccChangeNoticeFile().getNo(), zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(accChangeNotice.getAccChangeNoticeFile().getNo());
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });

                //处理本地业务明细表记录
                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (yhdm == null) {
                            yhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo()) &&
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                                    this.put("dkzh", hsingBusinessDetails.getDkzh());
                                }}, null, null, null, Order.DESC, null, null);
                                StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
                                //成功
                                if (resultFileDetail.getRtnCode().equals("00000")) {
                                    jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                    jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                    jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                    if (hsingBusinessDetails.getDqqc().equals(stHousingPersonalAccount.getDkqs())) {
                                        stHousingPersonalAccount.setDkye(BigDecimal.ZERO);
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhhkje(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhghbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhghlx(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhje(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhlx(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDkjqrq(new Date());
                                        stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getDkffe().setScale(2, BigDecimal.ROUND_HALF_UP));
                                        cLoanHousingPersonInformationBasics.get(0).setYhqs(stHousingPersonalAccount.getDkqs());
                                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
                                    } else {
                                        stHousingPersonalAccount.setDkye(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());//贷款余额
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());
                                        stHousingPersonalAccount.setDqyhje(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());//发生额
                                        stHousingPersonalAccount.setDqyhbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje()); //还款本金
                                        stHousingPersonalAccount.setDqyhlx(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());//利息金额
                                        stHousingPersonalAccount.setDqjhhkje(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());
                                        stHousingPersonalAccount.setDqjhghlx(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());
                                        stHousingPersonalAccount.setDqjhghbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje());
                                        stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getHsbjze().add(hsingBusinessDetails.getBjje()));
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDqqc(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc().add(new BigDecimal(1)));
                                    }
                                    icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                                    //修改业务表
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                    hsingBusinessDetails.setJzrq(new Date());//记账日期
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());

                                } else {
                                    //业务扣款失败
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
//                                hsingBusinessDetails.setJzrq(new Date());//记账日期
                                }
                            }
                        }
                    }

                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                    voucherAmount.setJinE(jzfse);
                    JFSJ.add(voucherAmount);
                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(jzbjje);
                    voucherAmounts.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                    DFSJ.add(voucherAmounts);
                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(jzfse.subtract(jzbjje));
                    oucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                    DFSJ.add(oucherAmount);


                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员",
                            VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                            businessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, yhdm);
                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setBjsj(new Date());
                        businessProcess.setStep(TaskBusinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                voucherAmount.setJinE(jzfse);
                JFSJ.add(voucherAmount);
                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(jzbjje);
                voucherAmounts.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                DFSJ.add(voucherAmounts);
                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(jzfse.subtract(jzbjje));
                oucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " " + summary);
                DFSJ.add(oucherAmount);


                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员",
                        VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                        housingBusinessProcess.get(0).getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, yhdm);
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            e.printStackTrace();
            try {
                exceptionMethod.exceptionBatch(e, housingBusinessProcess.get(0).getId());
            } catch (Exception ex) {
                logger.error(LogUtil.getTrace(ex));
                throw new ErrorException(ex.getMessage());
            }
        }

    }

    public CenterHeadIn centerInfo(String ywlsh, String yhmc) {
        try {
            ArrayList<CenterAccountInfo> specialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(yhmc);
            CenterHeadIn centerHeadIn = new CenterHeadIn();
            for (CenterAccountInfo specialAccou : specialAccount) {
                if (specialAccou.getZHXZ().equals("01")) {
                    centerHeadIn.setSendSeqNo(ywlsh);
                    centerHeadIn.setReceiveNode(specialAccou.getNode());
                    centerHeadIn.setOperNo("system");
                    centerHeadIn.setCustNo(specialAccou.getKHBH());
                }
            }
            return centerHeadIn;
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            throw new ErrorException("single" + e.getMessage());
        }
    }

    /**
     * @param accChangeNotice 回调结果查询
     */
    @Override
    public void putLoanApply(AccChangeNotice accChangeNotice) {

        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }
        String summary = accChangeNoticeFile.getSummary();

        BigDecimal jzbjje = BigDecimal.ZERO;
        BigDecimal jzfse = BigDecimal.ZERO;
        BigDecimal jzlxje = BigDecimal.ZERO;
        String yhdm = null;
        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(accChangeNotice.getAccChangeNoticeFile().getNo()))
                    this.put("ywlsh", accChangeNotice.getAccChangeNoticeFile().getNo());
                this.put("cznr", LoanBusinessType.提前还款.getCode());
            }}, null, null, null, null, null, null);
            yhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {
                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(accChangeNotice.getAccChangeNoticeFile().getNo(), zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(accChangeNotice.getAccChangeNoticeFile().getNo());
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });

                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (yhdm == null) {
                            yhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(hsingBusinessDetails.getYwlsh());
                                CLoanApplyRepaymentVice loanApplyRepaymentVice = byYWLSH.getLoanApplyRepaymentVice();
                                List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                                    this.put("dkzh", byYWLSH.getDkzh());
                                }}, null, null, null, null, null, null);

                                StHousingPersonalAccount byDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
                                BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
                                BigDecimal dkff = byDkzh.getDkffe();
                                BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
                                Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
                                BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
                                String dkhkfs = byDkzh.getStHousingPersonalLoan().getDkhkfs();

                                if (byYWLSH.getCznr().equals(LoanBusinessType.提前还款.getCode())) {
                                    if (resultFileDetail.getRtnCode().equals("00000")) {
                                        jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                        jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                        jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                        int dqqc = CommLoanAlgorithm.currentQS(dkffrq, loanApplyRepaymentVice.getYdkkrq());//当期期次
                                        CurrentPeriodRange currentPeriodRange = CommLoanAlgorithm.theTimePeriod(DateUtil.dateStringtoStringDate(dkffrq)
                                                , Integer.parseInt(dkqs.toString()), loanApplyRepaymentVice.getYdkkrq()); //期数，这期时间，下期时间
                                        BigDecimal bjje = null;
                                        BigDecimal bqbx = null;
                                        BigDecimal bqlx = null;
                                        BigDecimal tqhkje = null;
                                        BigDecimal syqc = BigDecimal.ZERO;
                                        if (sim.parse(sim.format(loanApplyRepaymentVice.getYdkkrq())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                                            bqbx = BigDecimal.ZERO;
                                            bqlx = BigDecimal.ZERO;
                                            bjje = bqbx.subtract(bqlx);
                                            tqhkje = CommLoanAlgorithm.prepaymentKAmount(loanApplyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录
                                            syqc = dkqs;
                                        } else {
//                                        bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, loanApplyRepaymentVice.getHkqc().intValue());
//                                        bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, loanApplyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
                                            bqbx = byDkzh.getDqyhje();
                                            bqlx = byDkzh.getDqyhlx();
                                            bjje = bqbx.subtract(bqlx);
                                            tqhkje = CommLoanAlgorithm.prepaymentKAmount(loanApplyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录
                                            syqc = dkqs.subtract(new BigDecimal(dqqc));
                                        }
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, loanApplyRepaymentVice.getHkqc().intValue(), loanApplyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                        BigDecimal tqhklx = tqhkje.subtract(tqhbje);
//                                        BigDecimal sybj = byDkzh.getDkye().subtract(tqhbje).subtract(bjje);
                                        BigDecimal sybj = byDkzh.getDkye().subtract(hsingBusinessDetails.getBjje());

                                        //新的还款计划，按照新的来计算
                                        BigDecimal newbqbx = CommLoanAlgorithm.currentBX(sybj, syqc.intValue(), dkhkfs, dkll, 1).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));//本期本息//
                                        BigDecimal newbqlx = CommLoanAlgorithm.overdueThisPeriodLX(sybj, 1, byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, syqc.intValue()).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));
                                        BigDecimal newbjje = newbqbx.subtract(newbqlx);


                                        //更新的重要的参数
                                        byDkzh.setDkye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));

                                        //更新的新的周期的还款计划
                                        byDkzh.setDqjhhkje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqjhghbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqjhghlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setHslxze(byDkzh.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        byDkzh.setHsbjze(byDkzh.getDkffe().subtract(sybj));
                                        byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension = byDkzh.getcLoanHousingPersonalAccountExtension();
                                        cLoanHousingPersonalAccountExtension.setDqqc(new BigDecimal(1));
                                        cLoanHousingPersonalAccountExtension.setDkgbjhye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        cLoanHousingPersonalAccountExtension.setDkgbjhqs(syqc.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        if (currentPeriodRange.getCurrentPeriod() == 0) {
//                                        cLoanHousingPersonalAccountExtension.setDkxffrq(byDkzh.getDkffrq());
                                        } else {
                                            cLoanHousingPersonalAccountExtension.setDkxffrq(currentPeriodRange.getAfterTime());
                                        }
                                        cLoanHousingPersonalAccountExtension.setTqhksj(simM.format(byYWLSH.getLoanApplyRepaymentVice().getYdkkrq()));//提前还款月份
                                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                                        //process表更新
                                        byYWLSH.setStep(LoanBussinessStatus.已入账.getName());
                                        byYWLSH.setBjsj(new Date());
                                        hsingBusinessDetails.setJzrq(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(sybj);
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());

                                        iSaveAuditHistory.saveNormalBusiness(hsingBusinessDetails.getYwlsh(), LoanBusinessType.提前还款.getName(), "办结");

                                    } else {
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                                    }
                                    cloanHousingBusinessProcess.update(byYWLSH);
                                } else if (byYWLSH.getCznr().equals(LoanBusinessType.结清.getCode())) {
                                    if (resultFileDetail.getRtnCode().equals("00000")) {
                                        jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                        jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                        jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                        int dqqc = CommLoanAlgorithm.currentQS(dkffrq, loanApplyRepaymentVice.getYdkkrq());//当期期次
                                        BigDecimal bjje = null;
                                        BigDecimal bqbx = null;
                                        BigDecimal bqlx = null;
                                        if (sim.parse(sim.format(loanApplyRepaymentVice.getYdkkrq())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                                            bqbx = BigDecimal.ZERO;
                                            bqlx = BigDecimal.ZERO;
                                            bjje = bqbx.subtract(bqlx);
                                        } else {
                                            bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, loanApplyRepaymentVice.getHkqc().intValue());
                                            bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, loanApplyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
                                            bjje = bqbx.subtract(bqlx);
                                        }
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, loanApplyRepaymentVice.getHkqc().intValue(), loanApplyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = byDkzh.getDkye().subtract(bjje);
                                        BigDecimal tqhklx = tqhbje.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(jxts)).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP);//提前还本金额

                                        byDkzh.setDkye(BigDecimal.ZERO);
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                                        byDkzh.setDqjhhkje(BigDecimal.ZERO);
                                        byDkzh.setDqjhghbj(BigDecimal.ZERO);
                                        byDkzh.setDqjhghlx(BigDecimal.ZERO);
                                        byDkzh.setDqyhje(BigDecimal.ZERO);
                                        byDkzh.setDqyhlx(BigDecimal.ZERO);
                                        byDkzh.setDqyhbj(BigDecimal.ZERO);
                                        byDkzh.setDkjqrq(new Date());
                                        byDkzh.setHslxze(byDkzh.getHslxze().add(loanApplyRepaymentVice.getSylx()));
                                        byDkzh.setHsbjze(byDkzh.getDkffe().setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhye(new BigDecimal(0));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDqqc(new BigDecimal(1));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhqs(new BigDecimal(0));
                                        cLoanHousingPersonInformationBasics.get(0).setYhqs(byDkzh.getDkqs());
                                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
                                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                                        byYWLSH.setStep(LoanBussinessStatus.已入账.getName());
                                        byYWLSH.setBjsj(new Date());
                                        hsingBusinessDetails.setJzrq(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(BigDecimal.ZERO);
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());

                                        iSaveAuditHistory.saveNormalBusiness(byYWLSH.getYwlsh(), LoanBusinessType.结清.getName(), "办结");

                                    } else {
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                                    }
                                    cloanHousingBusinessProcess.update(byYWLSH);
                                }
                            }
                        }
                    }
                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                    voucherAmount.setJinE(jzfse);
                    JFSJ.add(voucherAmount);

                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(jzbjje);
                    voucherAmounts.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                    DFSJ.add(voucherAmounts);

                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(jzfse.subtract(jzbjje));
                    oucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                    DFSJ.add(oucherAmount);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员", VoucherBusinessType.提前还清.getCode(),
                            VoucherBusinessType.提前还清.getCode(), accChangeNotice.getAccChangeNoticeFile().getNo(), JFSJ, DFSJ,
                            String.valueOf(djsl), accChangeNotice, yhdm);

                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setStep(LoanBussinessStatus.已入账.getName());
                    }

                    businessProcess.setBjsj(new Date());
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                voucherAmount.setJinE(jzfse);
                JFSJ.add(voucherAmount);

                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(jzbjje);
                voucherAmounts.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                DFSJ.add(voucherAmounts);

                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(jzfse.subtract(jzbjje));
                oucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm + "," + summary);
                DFSJ.add(oucherAmount);
                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员", VoucherBusinessType.提前还清.getCode(),
                        VoucherBusinessType.提前还清.getCode(), accChangeNotice.getAccChangeNoticeFile().getNo(), JFSJ, DFSJ,
                        String.valueOf(djsl), accChangeNotice, yhdm);
            }
        } catch (Exception ex) {
            logger.error(LogUtil.getTrace(ex));
            ex.printStackTrace();
            if (ex.getMessage().contains("入账失败--:")) {
                List<StHousingBusinessDetails> ousingBusinessDetails = housingBusinessProcess.get(0).getStHousingBusinessDetails();
                for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(ex.getMessage());//失败原因
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                    //原来的
                    CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(ngBusinessDetails.getYwlsh());
                    byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(byYWLSH);
                }
                //新的
                housingBusinessProcess.get(0).setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(housingBusinessProcess.get(0));
            } else {
                throw new ErrorException(ex);
            }
        }
    }


    /***
     *
     * 逾期自动扣划,贷款扣款
     * */
    @Override
    public void overdueAutomatic(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }

        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            BigDecimal yqbjze = BigDecimal.ZERO;
            BigDecimal totalMoney = BigDecimal.ZERO;
            String dkyhdm = null;
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(accChangeNotice.getAccChangeNoticeFile().getNo()))
                    this.put("ywlsh", accChangeNotice.getAccChangeNoticeFile().getNo());
                this.put("cznr", LoanBusinessType.逾期还款.getCode());
            }}, null, null, null, null, null, null);
            dkyhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {

                //处理业务完成
                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(accChangeNotice.getAccChangeNoticeFile().getNo(), zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(accChangeNotice.getAccChangeNoticeFile().getNo());
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });


                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (dkyhdm == null) {
                            dkyhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                CLoanHousingPersonInformationBasic icLoanHousingPersonInformationBasicDAOByDKZH = icLoanHousingPersonInformationBasicDAO.getByDKZH(hsingBusinessDetails.getDkzh());
                                StHousingPersonalAccount stHousingPersonalAccountByDkzh = icLoanHousingPersonInformationBasicDAOByDKZH.getPersonalAccount();
                                if (resultFileDetail.getRtnCode().equals("00000")) {
                                    totalMoney = totalMoney.add(hsingBusinessDetails.getFse());
                                    yqbjze = yqbjze.add(hsingBusinessDetails.getBjje());
                                    List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                        this.put("dkzh", hsingBusinessDetails.getDkzh());
                                        this.put("yqqc", hsingBusinessDetails.getDqqc());
                                        this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                                    }}, null, null, null, null, null, null);
                                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                    stHousingOverdueRegistrations.get(0).setSsrq(new Date());
                                    stHousingOverdueRegistrations.get(0).setSsyqbjje(hsingBusinessDetails.getBjje());
                                    stHousingOverdueRegistrations.get(0).setSsyqfxje(hsingBusinessDetails.getFxje());
                                    stHousingOverdueRegistrations.get(0).setSsyqlxje(hsingBusinessDetails.getLxje());
                                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));

                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                    hsingBusinessDetails.setJzrq(new Date());

                                    stHousingPersonalAccountByDkzh.setYqlxze(stHousingPersonalAccountByDkzh.getYqlxze().add(hsingBusinessDetails.getLxje().add(hsingBusinessDetails.getFxje())).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    stHousingPersonalAccountByDkzh.setYqbjze(stHousingPersonalAccountByDkzh.getYqbjze().add(hsingBusinessDetails.getBjje()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    stHousingPersonalAccountByDkzh.setHsbjze(stHousingPersonalAccountByDkzh.getHsbjze().add(hsingBusinessDetails.getBjje()));
                                    stHousingPersonalAccountByDkzh.setHslxze(stHousingPersonalAccountByDkzh.getHslxze().add(hsingBusinessDetails.getLxje()));
                                    stHousingPersonalAccountByDkzh.setDkye(stHousingPersonalAccountByDkzh.getDkye().subtract(hsingBusinessDetails.getBjje()));
                                    stHousingPersonalAccountByDkzh.setLjyqqs(stHousingPersonalAccountByDkzh.getLjyqqs().add(new BigDecimal(1)));
                                    stHousingPersonalAccountByDkzh.setFxze(stHousingPersonalAccountByDkzh.getFxze().add(hsingBusinessDetails.getFxje()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(stHousingPersonalAccountByDkzh.getDkye());
                                    icloanHousingPersonInformationBasicDAO.update(icLoanHousingPersonInformationBasicDAOByDKZH);
                                } else {
                                    CHousingBusinessDetailsExtension singBusinessDetailsExtension = hsingBusinessDetails.getcHousingBusinessDetailsExtension();
                                    singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
                                    singBusinessDetailsExtension.setSbyy(resultFileDetail.getRtnMsg());//失败原因
                                    singBusinessDetailsExtension.setUpdated_at(new Date());
                                    singBusinessDetailsExtension.setRgcl("0");
                                    List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                        this.put("dkzh", hsingBusinessDetails.getDkzh());
                                        this.put("yqqc", hsingBusinessDetails.getDqqc());
                                        this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                                    }}, null, null, null, null, null, null);
                                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                                }
                            }
                        }
                    }


                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    voucherAmount.setJinE(totalMoney);
                    JFSJ.add(voucherAmount);
                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(yqbjze);
                    voucherAmounts.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    DFSJ.add(voucherAmounts);
                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(totalMoney.subtract(yqbjze));
                    oucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    DFSJ.add(oucherAmount);

                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员",
                            VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                            businessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, dkyhdm);
                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setBjsj(new Date());
                        businessProcess.setStep(TaskBusinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                voucherAmount.setJinE(totalMoney);
                JFSJ.add(voucherAmount);
                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(yqbjze);
                voucherAmounts.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                DFSJ.add(voucherAmounts);
                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(totalMoney.subtract(yqbjze));
                oucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                DFSJ.add(oucherAmount);

                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员",
                        VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                        housingBusinessProcess.get(0).getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, dkyhdm);
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            if (e.getMessage().contains("入账失败--:")) {
                try {
                    List<StHousingBusinessDetails> ousingBusinessDetails = housingBusinessProcess.get(0).getStHousingBusinessDetails();
                    for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                        if (ngBusinessDetails.getcHousingBusinessDetailsExtension().getYwzt().equals(LoanBussinessStatus.扣款已发送.getName())) {
                            CHousingBusinessDetailsExtension singBusinessDetailsExtension = ngBusinessDetails.getcHousingBusinessDetailsExtension();
                            singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
                            singBusinessDetailsExtension.setSbyy(e.getMessage());//失败原因
                            singBusinessDetailsExtension.setRgcl("0");
                            List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                this.put("dkzh", ngBusinessDetails.getDkzh());
                                this.put("yqqc", ngBusinessDetails.getDqqc());
                                this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                            }}, null, null, null, null, null, null);
                            stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                            stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                        }
                    }
                    housingBusinessProcess.get(0).setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(housingBusinessProcess.get(0));
                } catch (Exception ex) {
                    logger.error(LogUtil.getTrace(ex));
                    throw new ErrorException(ex.getMessage());
                }
            } else {
                throw new ErrorException(e.getMessage());
            }
        }

    }

    @Override
    public void putrepaymentYwlsh(String ywlsh, String yhzhh) {
        BigDecimal jzbjje = BigDecimal.ZERO;
        BigDecimal jzfse = BigDecimal.ZERO;
        BigDecimal jzlxje = BigDecimal.ZERO;
        String yhdm = null;

        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                this.put("ywlsh", ywlsh);
                this.put("cznr", TaskBusinessType.正常还款.getCode());
            }}, null, null, null, null, null, null);
            yhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {

                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(ywlsh, zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(ywlsh);
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });

                //处理本地业务明细表记录
                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (yhdm == null) {
                            yhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo()) &&
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                                    this.put("dkzh", hsingBusinessDetails.getDkzh());
                                }}, null, null, null, Order.DESC, null, null);
                                StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
                                //成功
                                if (resultFileDetail.getRtnCode().equals("00000")) {
                                    jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                    jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                    jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                    if (hsingBusinessDetails.getDqqc().equals(stHousingPersonalAccount.getDkqs())) {
                                        stHousingPersonalAccount.setDkye(BigDecimal.ZERO);
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhhkje(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhghbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqjhghlx(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhje(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhlx(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDqyhbj(BigDecimal.ZERO);
                                        stHousingPersonalAccount.setDkjqrq(new Date());
                                        stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getDkffe().setScale(2, BigDecimal.ROUND_HALF_UP));
                                        cLoanHousingPersonInformationBasics.get(0).setYhqs(stHousingPersonalAccount.getDkqs());
                                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
                                    } else {
                                        stHousingPersonalAccount.setDkye(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());//贷款余额
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());
                                        stHousingPersonalAccount.setDqyhje(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());//发生额
                                        stHousingPersonalAccount.setDqyhbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje()); //还款本金
                                        stHousingPersonalAccount.setDqyhlx(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());//利息金额
                                        stHousingPersonalAccount.setDqjhhkje(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());
                                        stHousingPersonalAccount.setDqjhghlx(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());
                                        stHousingPersonalAccount.setDqjhghbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje());
                                        stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getHsbjze().add(hsingBusinessDetails.getBjje()));
                                        stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDqqc(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc().add(new BigDecimal(1)));
                                    }
                                    icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                                    //修改业务表
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                    hsingBusinessDetails.setJzrq(new Date());//记账日期

                                    try {
                                        Calendar c = Calendar.getInstance();
                                        ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasics.get(0).getSjhm(), SMSTemp.正常还款后.getCode(), new ArrayList<String>() {{
                                            this.add(cLoanHousingPersonInformationBasics.get(0).getJkrxm());
                                            this.add(stHousingPersonalAccount.getDkzh().substring(stHousingPersonalAccount.getDkzh().length() - 4));
                                            this.add(c.get(Calendar.MONTH) + 1 + "");
                                            this.add(c.get(Calendar.DATE) + "");
                                            this.add(hsingBusinessDetails.getBjje() + "");
                                            this.add(hsingBusinessDetails.getLxje() + "");
                                            this.add("0");
                                            this.add("0");
                                            this.add(stHousingPersonalAccount.getDkye() + "");
                                        }});
                                    } catch (Exception e) {
                                    }

                                } else {
                                    //业务扣款失败
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                }
                            }
                        }
                    }


                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                    voucherAmount.setJinE(jzfse);
                    JFSJ.add(voucherAmount);
                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(jzbjje);
                    voucherAmounts.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                    DFSJ.add(voucherAmounts);
                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(jzfse.subtract(jzbjje));
                    oucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                    DFSJ.add(oucherAmount);

                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员",
                            VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                            businessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), yhzhh, yhdm);
                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setBjsj(new Date());
                        businessProcess.setStep(TaskBusinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                voucherAmount.setJinE(jzfse);
                JFSJ.add(voucherAmount);
                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(jzbjje);
                voucherAmounts.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                DFSJ.add(voucherAmounts);
                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(jzfse.subtract(jzbjje));
                oucherAmount.setZhaiYao("正常个贷银行代码：" + yhdm + " ");
                DFSJ.add(oucherAmount);

                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员",
                        VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                        housingBusinessProcess.get(0).getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), yhzhh, yhdm);
            }

        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            e.printStackTrace();
            try {
                exceptionMethod.exceptionBatch(e, housingBusinessProcess.get(0).getId());
            } catch (Exception ex) {
                logger.error(LogUtil.getTrace(ex));
                throw new ErrorException(ex.getMessage());
            }
        }
    }


    @Override
    public void putLoanApplyYwlsh(String ywlsh, String yhzhh) {
        BigDecimal jzbjje = BigDecimal.ZERO;
        BigDecimal jzfse = BigDecimal.ZERO;
        BigDecimal jzlxje = BigDecimal.ZERO;
        String yhdm = null;
        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                this.put("ywlsh", ywlsh);
                this.put("cznr", LoanBusinessType.提前还款.getCode());
            }}, null, null, null, null, null, null);
            yhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {
                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(ywlsh, zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(ywlsh);
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });

                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (yhdm == null) {
                            yhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(hsingBusinessDetails.getYwlsh());
                                CLoanApplyRepaymentVice loanApplyRepaymentVice = byYWLSH.getLoanApplyRepaymentVice();
                                List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                                    this.put("dkzh", byYWLSH.getDkzh());
                                }}, null, null, null, null, null, null);

                                StHousingPersonalAccount byDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
                                BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
                                BigDecimal dkff = byDkzh.getDkffe();
                                BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
                                Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
                                BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
                                String dkhkfs = byDkzh.getStHousingPersonalLoan().getDkhkfs();

                                if (byYWLSH.getCznr().equals(LoanBusinessType.提前还款.getCode())) {
                                    if (resultFileDetail.getRtnCode().equals("00000")) {
                                        jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                        jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                        jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                        int dqqc = CommLoanAlgorithm.currentQS(dkffrq, loanApplyRepaymentVice.getYdkkrq());//当期期次
                                        CurrentPeriodRange currentPeriodRange = CommLoanAlgorithm.theTimePeriod(DateUtil.dateStringtoStringDate(dkffrq)
                                                , Integer.parseInt(dkqs.toString()), loanApplyRepaymentVice.getYdkkrq()); //期数，这期时间，下期时间
                                        BigDecimal bjje = null;
                                        BigDecimal bqbx = null;
                                        BigDecimal bqlx = null;
                                        BigDecimal tqhkje = null;
                                        BigDecimal syqc = BigDecimal.ZERO;
                                        if (sim.parse(sim.format(loanApplyRepaymentVice.getYdkkrq())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                                            bqbx = BigDecimal.ZERO;
                                            bqlx = BigDecimal.ZERO;
                                            bjje = bqbx.subtract(bqlx);
                                            tqhkje = CommLoanAlgorithm.prepaymentKAmount(loanApplyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录
                                            syqc = dkqs;
                                        } else {
//                                        bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, loanApplyRepaymentVice.getHkqc().intValue());
//                                        bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, loanApplyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
                                            bqbx = byDkzh.getDqyhje();
                                            bqlx = byDkzh.getDqyhlx();
                                            bjje = bqbx.subtract(bqlx);
                                            tqhkje = CommLoanAlgorithm.prepaymentKAmount(loanApplyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录
                                            syqc = dkqs.subtract(new BigDecimal(dqqc));
                                        }
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, loanApplyRepaymentVice.getHkqc().intValue(), loanApplyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                        BigDecimal tqhklx = tqhkje.subtract(tqhbje);
//                                        BigDecimal sybj = byDkzh.getDkye().subtract(tqhbje).subtract(bjje);
                                        BigDecimal sybj = byDkzh.getDkye().subtract(hsingBusinessDetails.getBjje());

                                        //新的还款计划，按照新的来计算
                                        BigDecimal newbqbx = CommLoanAlgorithm.currentBX(sybj, syqc.intValue(), dkhkfs, dkll, 1).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));//本期本息//
                                        BigDecimal newbqlx = CommLoanAlgorithm.overdueThisPeriodLX(sybj, 1, byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, syqc.intValue()).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));
                                        BigDecimal newbjje = newbqbx.subtract(newbqlx);


                                        //更新的重要的参数
                                        byDkzh.setDkye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));

                                        //更新的新的周期的还款计划
                                        byDkzh.setDqjhhkje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqjhghbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqjhghlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setDqyhlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setHslxze(byDkzh.getHslxze().add(hsingBusinessDetails.getLxje()));
                                        byDkzh.setHsbjze(byDkzh.getDkffe().subtract(sybj));
                                        byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension = byDkzh.getcLoanHousingPersonalAccountExtension();
                                        cLoanHousingPersonalAccountExtension.setDqqc(new BigDecimal(1));
                                        cLoanHousingPersonalAccountExtension.setDkgbjhye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        cLoanHousingPersonalAccountExtension.setDkgbjhqs(syqc.setScale(2, BigDecimal.ROUND_HALF_UP));
                                        if (currentPeriodRange.getCurrentPeriod() == 0) {
//                                        cLoanHousingPersonalAccountExtension.setDkxffrq(byDkzh.getDkffrq());
                                        } else {
                                            cLoanHousingPersonalAccountExtension.setDkxffrq(currentPeriodRange.getAfterTime());
                                        }
                                        cLoanHousingPersonalAccountExtension.setTqhksj(simM.format(byYWLSH.getLoanApplyRepaymentVice().getYdkkrq()));//提前还款月份
                                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));
                                        try {
                                            ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasics.get(0).getSjhm(), SMSTemp.提前还款扣款后.getCode(), new ArrayList<String>() {{
                                                this.add(cLoanHousingPersonInformationBasics.get(0).getJkrxm());
                                                this.add(loanApplyRepaymentVice.getHkje() + "");
                                                this.add(sybj + "");
                                            }});
                                        } catch (Exception e) {
                                        }
                                        //process表更新
                                        byYWLSH.setStep(LoanBussinessStatus.已入账.getName());
                                        byYWLSH.setBjsj(new Date());
                                        hsingBusinessDetails.setJzrq(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(sybj);

                                        iSaveAuditHistory.saveNormalBusiness(hsingBusinessDetails.getYwlsh(), LoanBusinessType.提前还款.getName(), "办结");

                                    } else {
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                        byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                                    }
                                    cloanHousingBusinessProcess.update(byYWLSH);
                                } else if (byYWLSH.getCznr().equals(LoanBusinessType.结清.getCode())) {
                                    if (resultFileDetail.getRtnCode().equals("00000")) {
                                        jzfse = jzfse.add(hsingBusinessDetails.getFse());
                                        jzbjje = jzbjje.add(hsingBusinessDetails.getBjje());
                                        jzlxje = jzlxje.add(hsingBusinessDetails.getLxje());
                                        int dqqc = CommLoanAlgorithm.currentQS(dkffrq, loanApplyRepaymentVice.getYdkkrq());//当期期次
                                        BigDecimal bjje = null;
                                        BigDecimal bqbx = null;
                                        BigDecimal bqlx = null;
                                        if (sim.parse(sim.format(loanApplyRepaymentVice.getYdkkrq())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                                            bqbx = BigDecimal.ZERO;
                                            bqlx = BigDecimal.ZERO;
                                            bjje = bqbx.subtract(bqlx);
                                        } else {
                                            bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, loanApplyRepaymentVice.getHkqc().intValue());
                                            bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, loanApplyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
                                            bjje = bqbx.subtract(bqlx);
                                        }
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, loanApplyRepaymentVice.getHkqc().intValue(), loanApplyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = byDkzh.getDkye().subtract(bjje);
                                        BigDecimal tqhklx = tqhbje.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(jxts)).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP);//提前还本金额

                                        byDkzh.setDkye(BigDecimal.ZERO);
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                                        byDkzh.setDqjhhkje(BigDecimal.ZERO);
                                        byDkzh.setDqjhghbj(BigDecimal.ZERO);
                                        byDkzh.setDqjhghlx(BigDecimal.ZERO);
                                        byDkzh.setDqyhje(BigDecimal.ZERO);
                                        byDkzh.setDqyhlx(BigDecimal.ZERO);
                                        byDkzh.setDqyhbj(BigDecimal.ZERO);
                                        byDkzh.setDkjqrq(new Date());
                                        byDkzh.setHslxze(byDkzh.getHslxze().add(loanApplyRepaymentVice.getSylx()));
                                        byDkzh.setHsbjze(byDkzh.getDkffe().setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhye(new BigDecimal(0));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDqqc(new BigDecimal(1));
                                        byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhqs(new BigDecimal(0));
                                        cLoanHousingPersonInformationBasics.get(0).setYhqs(byDkzh.getDkqs());
                                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
                                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                                        byYWLSH.setStep(LoanBussinessStatus.已入账.getName());
                                        byYWLSH.setBjsj(new Date());
                                        hsingBusinessDetails.setJzrq(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(BigDecimal.ZERO);
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        try {
                                            Calendar c = Calendar.getInstance();
                                            ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasics.get(0).getSjhm(), SMSTemp.贷款结清.getCode(), new ArrayList<String>() {{
                                                this.add(cLoanHousingPersonInformationBasics.get(0).getJkrxm());
                                                this.add(byDkzh.getDkzh().substring(byDkzh.getDkzh().length() - 4));
                                                this.add(c.get(Calendar.YEAR) + "");
                                                this.add(c.get(Calendar.MONTH) + 1 + "");
                                                this.add(c.get(Calendar.DATE) + "");
                                            }});
                                        } catch (Exception e) {
                                        }

                                        iSaveAuditHistory.saveNormalBusiness(byYWLSH.getYwlsh(), LoanBusinessType.结清.getName(), "办结");

                                    } else {
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(resultFileDetail.getRtnMsg());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                                        byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                                    }
                                    cloanHousingBusinessProcess.update(byYWLSH);
                                }
                            }
                        }
                    }
                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm);
                    voucherAmount.setJinE(jzfse);
                    JFSJ.add(voucherAmount);

                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(jzbjje);
                    voucherAmounts.setZhaiYao("提前个贷银行代码：" + yhdm);
                    DFSJ.add(voucherAmounts);

                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(jzfse.subtract(jzbjje));
                    oucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm);
                    DFSJ.add(oucherAmount);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员", VoucherBusinessType.提前还清.getCode(),
                            VoucherBusinessType.提前还清.getCode(), ywlsh, JFSJ, DFSJ,
                            String.valueOf(djsl), yhzhh, yhdm);

                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setStep(LoanBussinessStatus.已入账.getName());
                    }

                    businessProcess.setBjsj(new Date());
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm);
                voucherAmount.setJinE(jzfse);
                JFSJ.add(voucherAmount);

                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(jzbjje);
                voucherAmounts.setZhaiYao("提前个贷银行代码：" + yhdm);
                DFSJ.add(voucherAmounts);

                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(jzfse.subtract(jzbjje));
                oucherAmount.setZhaiYao("提前个贷银行代码：" + yhdm);
                DFSJ.add(oucherAmount);
                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员", VoucherBusinessType.提前还清.getCode(),
                        VoucherBusinessType.提前还清.getCode(), ywlsh, JFSJ, DFSJ,
                        String.valueOf(djsl), yhzhh, yhdm);
            }
        } catch (Exception ex) {
            logger.error(LogUtil.getTrace(ex));
            ex.printStackTrace();
            if (ex.getMessage().contains("入账失败--:")) {
                List<StHousingBusinessDetails> ousingBusinessDetails = housingBusinessProcess.get(0).getStHousingBusinessDetails();
                for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(ex.getMessage());//失败原因
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                    //原来的
                    CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(ngBusinessDetails.getYwlsh());
                    byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(byYWLSH);
                }
                //新的
                housingBusinessProcess.get(0).setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(housingBusinessProcess.get(0));
            } else {
                throw new ErrorException(ex);
            }
        }
    }

    @Override
    public void overdueAutomaticYwlsh(String ywlsh, String yhzhh) {
        List<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            BigDecimal yqbjze = BigDecimal.ZERO;
            BigDecimal totalMoney = BigDecimal.ZERO;
            String dkyhdm = null;
            housingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                this.put("ywlsh", ywlsh);
                this.put("cznr", LoanBusinessType.逾期还款.getCode());
            }}, null, null, null, null, null, null);
            dkyhdm = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhdm();
            if (housingBusinessProcess.get(0).getStep().equals(LoanBussinessStatus.扣款已发送.getName())) {

                //处理业务完成
                String zhkhyhmc = housingBusinessProcess.get(0).getStHousingBusinessDetails().get(0).getcHousingBusinessDetailsExtension().getZhkhyhmc();
                CenterHeadIn centerHeadIn = centerInfo(ywlsh, zhkhyhmc);
                //处理业务完成
                BatchResultDownloadIn batchResultDownloadIn = new BatchResultDownloadIn();
                batchResultDownloadIn.setBatchNo(ywlsh);
                batchResultDownloadIn.setCenterHeadIn(centerHeadIn);
                batchResultDownloadIn.setTxCodeSub("3");//结果数据下载
                BatchResultDownloadOut batchResultDownloadOut = iBank.sendMsg(batchResultDownloadIn);
                if ("1".equals(batchResultDownloadOut.getCenterHeadOut().getTxStatus())) {//入账失败
                    throw new Exception("入账失败--:" + batchResultDownloadOut.getCenterHeadOut().getRtnMessage());
                }

                List<BatchResultFileDetail> batchResultFileDetail = CollectionUtils.flatmap(TransactionFileFactory.getObjFromFile(batchResultDownloadOut.getFileList().getDATA(), BatchResultFileDetail.class.getName()),
                        new CollectionUtils.Transformer<Object, BatchResultFileDetail>() {
                            @Override
                            public BatchResultFileDetail tansform(Object var1) {
                                return (BatchResultFileDetail) var1;
                            }
                        });


                for (CLoanHousingBusinessProcess businessProcess : housingBusinessProcess) {
                    List<StHousingBusinessDetails> housingBusinessDetails = businessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails hsingBusinessDetails : housingBusinessDetails) {
                        if (dkyhdm == null) {
                            dkyhdm = hsingBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhdm();
                        }
                        for (BatchResultFileDetail resultFileDetail : batchResultFileDetail) {
                            if (hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkzh().equals(resultFileDetail.getDeAcctNo())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm().equals(resultFileDetail.getDeAcctName())
                                    && hsingBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh().equals(resultFileDetail.getNo())) {
                                CLoanHousingPersonInformationBasic icLoanHousingPersonInformationBasicDAOByDKZH = icLoanHousingPersonInformationBasicDAO.getByDKZH(hsingBusinessDetails.getDkzh());
                                StHousingPersonalAccount stHousingPersonalAccountByDkzh = icLoanHousingPersonInformationBasicDAOByDKZH.getPersonalAccount();

                                List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                    this.put("dkzh", hsingBusinessDetails.getDkzh());
                                    this.put("yqqc", hsingBusinessDetails.getDqqc());
                                    this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                                }}, null, null, null, null, null, null);

                                if (resultFileDetail.getRtnCode().equals("00000")) {
                                    totalMoney = totalMoney.add(hsingBusinessDetails.getFse());
                                    yqbjze = yqbjze.add(hsingBusinessDetails.getBjje());
                                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                    stHousingOverdueRegistrations.get(0).setSsrq(new Date());
                                    stHousingOverdueRegistrations.get(0).setSsyqbjje(hsingBusinessDetails.getBjje());
                                    stHousingOverdueRegistrations.get(0).setSsyqfxje(hsingBusinessDetails.getFxje());
                                    stHousingOverdueRegistrations.get(0).setSsyqlxje(hsingBusinessDetails.getLxje());
                                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));

                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(LoanBussinessStatus.已入账.getName());
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                    hsingBusinessDetails.setJzrq(new Date());

                                    stHousingPersonalAccountByDkzh.setYqlxze(stHousingPersonalAccountByDkzh.getYqlxze().add(hsingBusinessDetails.getLxje().add(hsingBusinessDetails.getFxje())).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    stHousingPersonalAccountByDkzh.setYqbjze(stHousingPersonalAccountByDkzh.getYqbjze().add(hsingBusinessDetails.getBjje()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    stHousingPersonalAccountByDkzh.setHsbjze(stHousingPersonalAccountByDkzh.getHsbjze().add(hsingBusinessDetails.getBjje()));
                                    stHousingPersonalAccountByDkzh.setHslxze(stHousingPersonalAccountByDkzh.getHslxze().add(hsingBusinessDetails.getLxje()));
                                    stHousingPersonalAccountByDkzh.setDkye(stHousingPersonalAccountByDkzh.getDkye().subtract(hsingBusinessDetails.getBjje()));
                                    stHousingPersonalAccountByDkzh.setLjyqqs(stHousingPersonalAccountByDkzh.getLjyqqs().add(new BigDecimal(1)));
                                    stHousingPersonalAccountByDkzh.setFxze(stHousingPersonalAccountByDkzh.getFxze().add(hsingBusinessDetails.getFxje()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(stHousingPersonalAccountByDkzh.getDkye());
                                    icloanHousingPersonInformationBasicDAO.update(icLoanHousingPersonInformationBasicDAOByDKZH);
                                } else {
                                    CHousingBusinessDetailsExtension singBusinessDetailsExtension = hsingBusinessDetails.getcHousingBusinessDetailsExtension();
                                    singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
                                    singBusinessDetailsExtension.setSbyy(resultFileDetail.getRtnMsg());//失败原因
                                    singBusinessDetailsExtension.setRgcl("0");
                                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                                    singBusinessDetailsExtension.setUpdated_at(new Date());
                                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                                }
                            }
                        }
                    }

                    //region 生成记账凭证
                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    voucherAmount.setJinE(totalMoney);
                    JFSJ.add(voucherAmount);
                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(yqbjze);
                    voucherAmounts.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    DFSJ.add(voucherAmounts);
                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(totalMoney.subtract(yqbjze));
                    oucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                    DFSJ.add(oucherAmount);

                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                            "自动", "", "管理员",
                            VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                            businessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), yhzhh, dkyhdm);
                    if (voucherRes.getJZPZH() == null) {
                        businessProcess.setSbyy(voucherRes.getMSG());
                        businessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        businessProcess.setJzpzh(voucherRes.getJZPZH());
                        businessProcess.setBjsj(new Date());
                        businessProcess.setStep(TaskBusinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(businessProcess);
                }
            } else {
                //region 生成记账凭证
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                voucherAmount.setJinE(totalMoney);
                JFSJ.add(voucherAmount);
                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(yqbjze);
                voucherAmounts.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                DFSJ.add(voucherAmounts);
                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(totalMoney.subtract(yqbjze));
                oucherAmount.setZhaiYao("逾期个贷银行代码：" + dkyhdm);
                DFSJ.add(oucherAmount);

                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动",
                        "自动", "", "管理员",
                        VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                        ywlsh, JFSJ, DFSJ, String.valueOf(djsl), yhzhh, dkyhdm);
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            logger.error(e.getMessage());
            if (e.getMessage().contains("入账失败--:")) {
                try {
                    List<StHousingBusinessDetails> ousingBusinessDetails = housingBusinessProcess.get(0).getStHousingBusinessDetails();
                    for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                        if (ngBusinessDetails.getcHousingBusinessDetailsExtension().getYwzt().equals(LoanBussinessStatus.扣款已发送.getName())) {
                            CHousingBusinessDetailsExtension singBusinessDetailsExtension = ngBusinessDetails.getcHousingBusinessDetailsExtension();
                            singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
                            singBusinessDetailsExtension.setSbyy(e.getMessage());//失败原因
                            singBusinessDetailsExtension.setRgcl("0");
                            List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                this.put("dkzh", ngBusinessDetails.getDkzh());
                                this.put("yqqc", ngBusinessDetails.getDqqc());
                                this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                            }}, null, null, null, null, null, null);
                            stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                            stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                        }
                    }
                    housingBusinessProcess.get(0).setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(housingBusinessProcess.get(0));
                } catch (Exception ex) {
                    logger.error(LogUtil.getTrace(ex));
                    logger.error(ex.getMessage());
                    throw new ErrorException(ex.getMessage());
                }
            } else {
                throw new ErrorException(e.getMessage());
            }
        }
    }
}

