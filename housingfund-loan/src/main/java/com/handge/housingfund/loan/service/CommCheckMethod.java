package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.LoanWithdrawl;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionFileOther;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionFileSelf;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.IExceptionMethod;
import com.handge.housingfund.common.service.loan.ILoanTaskService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.BackBasicInfomation;
import com.handge.housingfund.common.service.loan.model.CurrentPeriodRange;
import com.handge.housingfund.common.service.loan.model.HousingfundAccountPlanGetInformation;
import com.handge.housingfund.common.service.util.CommLoanAlgorithm;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.LogUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.TaskBusinessStatus;
import com.handge.housingfund.database.enums.TaskBusinessType;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Funnyboy on 2017/9/14.
 */
@Component
public class CommCheckMethod implements ICommCheckMethod {
    private static Logger logger = LogManager.getLogger(LoanTaskService.class);
    @Autowired
    private ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    private IStHousingPersonalAccountDAO stHousingPersonalAccount;
    @Autowired
    private IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icLoanHousingPersonInformationBasicDAO;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private IStCommonPersonDAO stCommonPerson;
    @Autowired
    private WithdrawlTasks withdrawlTasks;
    @Autowired
    private IStHousingOverdueRegistrationDAO stHousingOverdueRegistrationDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icloanHousingPersonInformationBasicDAO;
    @Autowired
    ICAuditHistoryDAO auditHistoryDAO;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IExceptionMethod exceptionMethod;
    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    ILoanTaskService loanTaskService;
    private final SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simM = new SimpleDateFormat("yyyyMMdd");


    public HashMap<String, Object> firstCehck(BackBasicInfomation housingPersonInformationBasic, Date periodOfafterTime, CLoanHousingBusinessProcess housingBusinessProcess,
                                              List<StHousingBusinessDetails> gBusinessDetails, List<LoanDeductionFileSelf> list2, BigDecimal totalMoney, int totalNumber) throws Exception {
        BackBasicInfomation personalAccount = housingPersonInformationBasic;
        HashMap<String, Object> obj = new HashMap<>();
        //到了还款日(数据库数据)
        BigDecimal dkffe = personalAccount.getDKGBJHYE();//合同贷款金额
        int dkqs = Integer.parseInt(personalAccount.getDKGBJHQS().toString());//贷款期数
        String dkhkfs = personalAccount.getDKHKFS();//贷款还款方式
        BigDecimal dkll = CommLoanAlgorithm.lendingRate(personalAccount.getDKLL(), personalAccount.getLLFDBL());//贷款利率
        Date dkffrq = personalAccount.getDKXFFRQ();//贷款发放日期
        int dqqc = Integer.parseInt(personalAccount.getDQQC().toString());
        BigDecimal sjqc = personalAccount.getDQQC().add(personalAccount.getDKQS().subtract(personalAccount.getDKGBJHQS()));
        BigDecimal currentJine = personalAccount.getDQYHJE();//当期应还金额

        String str = personalAccount.getJKRGJJZH();

        LinkedList<HousingfundAccountPlanGetInformation> housingfundAccountPlanGetInformation = CommLoanAlgorithm.repaymentPlan(dkffe, dkqs, dkhkfs, dkll, sim.format(dkffrq));//还款计划
        HousingfundAccountPlanGetInformation fundAccountPlanGetInformation = null;
        if (dqqc != dkqs) {
            fundAccountPlanGetInformation = housingfundAccountPlanGetInformation.get(personalAccount.getDQQC().intValue());//下期计划,从零开始的
        }

        StCommonPerson stCommonPersonByGrzh = null;
        BigDecimal sub = BigDecimal.ZERO;

        StCollectionPersonalAccount gtjkrcollectionpersonalAccounts = null;
        BigDecimal gtsub = BigDecimal.ZERO;

        StCollectionPersonalAccount housingfundpersonalAccounts = null;
        if ((personalAccount.isWTKHYJCE() == true)) {
            stCommonPersonByGrzh = stCommonPerson.getByGrzh(str);
            if (stCommonPersonByGrzh != null) {
                housingfundpersonalAccounts = stCommonPersonByGrzh.getCollectionPersonalAccount();
                if (housingfundpersonalAccounts != null) {
                    if (housingfundpersonalAccounts.getGrzhye() != null) {
                        sub = housingfundpersonalAccounts.getGrzhye().subtract(currentJine.add(new BigDecimal(0.01)));//公积金余额-还款额
                    }
                }
            }

            if (sub.compareTo(BigDecimal.ZERO) == -1) {
                if (personalAccount.getHYZK().equals("20") && personalAccount.getGTJKRGJJZH() != null) {
                    StCommonPerson byGrzh = stCommonPerson.getByGrzh(personalAccount.getGTJKRGJJZH());
                    if (byGrzh != null) {
                        gtjkrcollectionpersonalAccounts = byGrzh.getCollectionPersonalAccount();
                        if (gtjkrcollectionpersonalAccounts != null) {
                            if (gtjkrcollectionpersonalAccounts.getGrzhye() != null) {
                                gtsub = gtjkrcollectionpersonalAccounts.getGrzhye().subtract(sub.abs().add(new BigDecimal(0.01)));//公积金余额-还款额
                            }
                        }
                    }
                }
            }
        }

        if ((sub.compareTo(BigDecimal.ZERO) == 1 || gtsub.compareTo(BigDecimal.ZERO) == 1)) {
            try {
                if (sub.compareTo(BigDecimal.ZERO) == 1) {
                    //提取业务
                    ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(personalAccount.getZHKHYHMC());
                    LoanWithdrawl loanWithdrawl = new LoanWithdrawl();
                    loanWithdrawl.setJkrgrzh(str);
                    loanWithdrawl.setJkrfse(currentJine);
                    for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                        if (cterAccountInfo.getZHXZ().equals("01")) {
                            loanWithdrawl.setYhzhhm(cterAccountInfo.getYHZHHM());
                        }
                    }
                    loanWithdrawl.setYwwd(stCommonPersonByGrzh.getUnit().getExtension().getKhwd());
                    loanWithdrawl.setLixi(personalAccount.getDQYHLX());
                    loanWithdrawl.setBenjin(personalAccount.getDQYHBJ());
                    loanWithdrawl.setYhdm(personalAccount.getZHKHYHDM());
                    loanTaskService.addWithdrawlrepament(loanWithdrawl);
                } else if (gtsub.compareTo(BigDecimal.ZERO) == 1) {
                    ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(personalAccount.getZHKHYHMC());
                    LoanWithdrawl loanWithdrawl = new LoanWithdrawl();
                    loanWithdrawl.setJkrgrzh(str);
                    loanWithdrawl.setGtjkrgrzh(gtjkrcollectionpersonalAccounts.getGrzh());
                    if (stCommonPersonByGrzh.getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0.01)) == 1) {
                        loanWithdrawl.setJkrfse(stCommonPersonByGrzh.getCollectionPersonalAccount().getGrzhye().subtract(new BigDecimal(0.01)));
                    } else {
                        loanWithdrawl.setJkrfse(BigDecimal.ZERO);
                    }
                    loanWithdrawl.setGtjkrfse(currentJine.subtract(loanWithdrawl.getJkrfse()));
                    for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                        if (cterAccountInfo.getZHXZ().equals("01")) {
                            loanWithdrawl.setYhzhhm(cterAccountInfo.getYHZHHM());
                        }
                    }
                    loanWithdrawl.setYwwd(stCommonPersonByGrzh.getUnit().getExtension().getKhwd());
                    loanWithdrawl.setLixi(personalAccount.getDQYHLX());
                    loanWithdrawl.setBenjin(personalAccount.getDQYHBJ());
                    loanWithdrawl.setYhdm(personalAccount.getZHKHYHDM());
                    loanTaskService.addWithdrawlrepament(loanWithdrawl);
                }

                CLoanHousingBusinessProcess housingBusinessProcessnew = new CLoanHousingBusinessProcess();
                CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("id", "1");
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                housingBusinessProcessnew.setYwwd(network);
                housingBusinessProcessnew.setCznr(TaskBusinessType.正常还款.getCode());//还款类型
                housingBusinessProcessnew.setStep(TaskBusinessStatus.已入账.getName());//入账状态
                housingBusinessProcessnew.setBlsj(new Date());
                housingBusinessProcessnew.setDkzh(personalAccount.getDKZH());

                //业务明细记录
                List<StHousingBusinessDetails> list = new ArrayList<>();
                StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                chousingBusinessDetails.setDkzh(personalAccount.getDKZH());//贷款账号
                chousingBusinessDetails.setDkywmxlx(TaskBusinessType.正常还款.getCode());//贷款业务类型
                chousingBusinessDetails.setYwfsrq(periodOfafterTime);//业务发生日期
                chousingBusinessDetails.setFse(currentJine);//发生额
                chousingBusinessDetails.setBjje(personalAccount.getDQYHBJ());//本金金额
                chousingBusinessDetails.setLxje(personalAccount.getDQYHLX());//利息金额
                chousingBusinessDetails.setJzrq(new Date());//记账日期
                chousingBusinessDetails.setDkyhdm(personalAccount.getZHKHYHDM());
                chousingBusinessDetails.setDqqc(sjqc);//当期期次
                CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.已入账.getName());//业务状态
                if (fundAccountPlanGetInformation != null) {
                    stHousingBusinessDetailsExtension.setXqdkye(new BigDecimal(fundAccountPlanGetInformation.getDKYE()));//贷款余额
                    stHousingBusinessDetailsExtension.setXqbjje(new BigDecimal(fundAccountPlanGetInformation.getHKBJJE()));//下期本金
                    stHousingBusinessDetailsExtension.setXqfse(new BigDecimal(fundAccountPlanGetInformation.getFSE()));//下期发生额
                    stHousingBusinessDetailsExtension.setXqlxje(new BigDecimal(fundAccountPlanGetInformation.getHKLXJE()));//下期利息金额
                } else {
                    stHousingBusinessDetailsExtension.setXqdkye(BigDecimal.ZERO);//贷款余额
                    stHousingBusinessDetailsExtension.setXqbjje(BigDecimal.ZERO);//下期本金
                    stHousingBusinessDetailsExtension.setXqfse(BigDecimal.ZERO);//下期发生额
                    stHousingBusinessDetailsExtension.setXqlxje(BigDecimal.ZERO);//下期利息金额
                }
                stHousingBusinessDetailsExtension.setJkrxm(personalAccount.getJKRXM());//借款人姓名
                stHousingBusinessDetailsExtension.setHkzh(personalAccount.getHKZH());//还款账号
                stHousingBusinessDetailsExtension.setZhkhyhmc(personalAccount.getZHKHYHMC());
                stHousingBusinessDetailsExtension.setZhkhyhdm(personalAccount.getZHKHYHDM());
                stHousingBusinessDetailsExtension.setYwwd(personalAccount.getYWWD());
                chousingBusinessDetails.setGrywmx(housingBusinessProcessnew);
                chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                list.add(chousingBusinessDetails);
                housingBusinessProcessnew.setStHousingBusinessDetails(list);
                String id = cloanHousingBusinessProcess.save(housingBusinessProcessnew);//更新业务状态
                CLoanHousingBusinessProcess cLoanHousingBusinessProcess = cloanHousingBusinessProcess.get(id);
                cLoanHousingBusinessProcess.getStHousingBusinessDetails().get(0).setYwlsh(cLoanHousingBusinessProcess.getYwlsh());
                cloanHousingBusinessProcess.update(cLoanHousingBusinessProcess);


                //修改账户余额，生成下一期的还款计划
                if (personalAccount.getDQQC().equals(personalAccount.getDKGBJHQS())) {
                    stHousingPersonalAccount.updateDecution(housingPersonInformationBasic.getCLHPIBID(), LoanAccountType.已结清.getCode()
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , housingPersonInformationBasic.getHSLXZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKLXJE()))
                            , new Date()
                            , housingPersonInformationBasic.getHSBJZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKBJJE()))
                            , housingPersonInformationBasic.getDKGBJHQS()
                            , BigDecimal.ZERO);
                } else {
                    stHousingPersonalAccount.updateDecution(housingPersonInformationBasic.getCLHPIBID(), LoanAccountType.正常.getCode()
                            , new BigDecimal(fundAccountPlanGetInformation.getDKYE())
                            , new BigDecimal(fundAccountPlanGetInformation.getFSE())
                            , new BigDecimal(fundAccountPlanGetInformation.getHKBJJE())
                            , new BigDecimal(fundAccountPlanGetInformation.getHKLXJE())
                            , new BigDecimal(fundAccountPlanGetInformation.getFSE())
                            , new BigDecimal(fundAccountPlanGetInformation.getHKBJJE())
                            , new BigDecimal(fundAccountPlanGetInformation.getHKLXJE())
                            , housingPersonInformationBasic.getHSLXZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKLXJE()))
                            , new Date()
                            , housingPersonInformationBasic.getHSBJZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKBJJE()))
                            , new BigDecimal(fundAccountPlanGetInformation.getHKQC())
                            , new BigDecimal(fundAccountPlanGetInformation.getDKYE()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(LogUtil.getTrace(e));
                try {
                    if (e.getMessage().contains("入账失败")) {
                        //业务明细记录
                        StHousingBusinessDetails chousingBusinessDetailsnew = new StHousingBusinessDetails();
                        chousingBusinessDetailsnew.setDkyhdm(personalAccount.getZHKHYHDM());
                        chousingBusinessDetailsnew.setDkzh(personalAccount.getDKZH());//贷款账号
                        chousingBusinessDetailsnew.setDkywmxlx(TaskBusinessType.正常还款.getCode());//贷款业务类型
                        chousingBusinessDetailsnew.setYwfsrq(periodOfafterTime);//业务发生日期
                        chousingBusinessDetailsnew.setFse(personalAccount.getDQYHJE());//发生额
                        chousingBusinessDetailsnew.setBjje(personalAccount.getDQYHBJ());//本金金额
                        chousingBusinessDetailsnew.setLxje(personalAccount.getDQYHLX());//利息金额
                        chousingBusinessDetailsnew.setDqqc(sjqc);//当期期次
                        CHousingBusinessDetailsExtension stHousingBusinessDetailsExtensionnew = new CHousingBusinessDetailsExtension();
                        stHousingBusinessDetailsExtensionnew.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                        if (fundAccountPlanGetInformation != null) {
                            stHousingBusinessDetailsExtensionnew.setXqdkye(new BigDecimal(fundAccountPlanGetInformation.getDKYE()));//贷款余额
                            stHousingBusinessDetailsExtensionnew.setXqbjje(new BigDecimal(fundAccountPlanGetInformation.getHKBJJE()));//下期本金
                            stHousingBusinessDetailsExtensionnew.setXqfse(new BigDecimal(fundAccountPlanGetInformation.getFSE()));//下期发生额
                            stHousingBusinessDetailsExtensionnew.setXqlxje(new BigDecimal(fundAccountPlanGetInformation.getHKLXJE()));//下期利息金额
                        } else {
                            stHousingBusinessDetailsExtensionnew.setXqdkye(BigDecimal.ZERO);//贷款余额
                            stHousingBusinessDetailsExtensionnew.setXqbjje(BigDecimal.ZERO);//下期本金
                            stHousingBusinessDetailsExtensionnew.setXqfse(BigDecimal.ZERO);//下期发生额
                            stHousingBusinessDetailsExtensionnew.setXqlxje(BigDecimal.ZERO);//下期利息金额
                        }
                        stHousingBusinessDetailsExtensionnew.setHkzh(personalAccount.getHKZH());//还款账号
                        stHousingBusinessDetailsExtensionnew.setJkrxm(personalAccount.getJKRXM());//借款人姓名
                        stHousingBusinessDetailsExtensionnew.setZhkhyhmc(personalAccount.getZHKHYHMC());
                        stHousingBusinessDetailsExtensionnew.setZhkhyhdm(personalAccount.getZHKHYHDM());
                        stHousingBusinessDetailsExtensionnew.setYwwd(personalAccount.getYWWD());
                        stHousingBusinessDetailsExtensionnew.setHkxh(totalNumber + "");
                        chousingBusinessDetailsnew.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtensionnew);

                        String zhkhyhdm = personalAccount.getZHKHYHDM();
                        if ("104".equals(zhkhyhdm)) {
                            LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                            loanDeductionFileOther.setNo(totalNumber + "");
                            loanDeductionFileOther.setAmt(currentJine);
                            loanDeductionFileOther.setDeAcctNo(personalAccount.getHKZH());
                            loanDeductionFileOther.setDeAcctName(personalAccount.getJKRXM());
                            loanDeductionFileOther.setDeChgNo("104100000004");
                            loanDeductionFileOther.setFullMark("0");
                            loanDeductionFileOther.setSummary(housingPersonInformationBasic.getJKRXM() + periodOfafterTime + "公积金贷款扣划");
                            list2.add(loanDeductionFileOther);
                        } else {
                            LoanDeductionFileSelf loanDeductionFileSlef = new LoanDeductionFileSelf();
                            loanDeductionFileSlef.setNo(totalNumber + "");
                            loanDeductionFileSlef.setAmt(currentJine);
                            loanDeductionFileSlef.setDeAcctNo(personalAccount.getHKZH());
                            loanDeductionFileSlef.setDeAcctName(personalAccount.getJKRXM());
                            loanDeductionFileSlef.setFullMark("0");
                            loanDeductionFileSlef.setSummary(housingPersonInformationBasic.getJKRXM() + periodOfafterTime + "公积金贷款扣划");
                            list2.add(loanDeductionFileSlef);//入结算平台
                        }
                        gBusinessDetails.add(chousingBusinessDetailsnew);
                        totalNumber += 1;
                        totalMoney = totalMoney.add(currentJine);
                    } else {
                        throw new ErrorException(e);
                    }
                } catch (Exception ex) {
                    logger.error(LogUtil.getTrace(ex));
                    throw new ErrorException(ex);
                }
            }
        } else {
            //业务明细记录
            StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
            chousingBusinessDetails.setDkyhdm(personalAccount.getZHKHYHDM());
            chousingBusinessDetails.setDkzh(personalAccount.getDKZH());//贷款账号
            chousingBusinessDetails.setDkywmxlx(TaskBusinessType.正常还款.getCode());//贷款业务类型
            chousingBusinessDetails.setYwfsrq(periodOfafterTime);//业务发生日期
            chousingBusinessDetails.setFse(personalAccount.getDQYHJE());//发生额
            chousingBusinessDetails.setBjje(personalAccount.getDQYHBJ());//本金金额
            chousingBusinessDetails.setLxje(personalAccount.getDQYHLX());//利息金额
            chousingBusinessDetails.setDqqc(sjqc);//当期期次
            CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
            stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
            if (fundAccountPlanGetInformation != null) {
                stHousingBusinessDetailsExtension.setXqdkye(new BigDecimal(fundAccountPlanGetInformation.getDKYE()));//贷款余额
                stHousingBusinessDetailsExtension.setXqbjje(new BigDecimal(fundAccountPlanGetInformation.getHKBJJE()));//下期本金
                stHousingBusinessDetailsExtension.setXqfse(new BigDecimal(fundAccountPlanGetInformation.getFSE()));//下期发生额
                stHousingBusinessDetailsExtension.setXqlxje(new BigDecimal(fundAccountPlanGetInformation.getHKLXJE()));//下期利息金额
            } else {
                stHousingBusinessDetailsExtension.setXqdkye(BigDecimal.ZERO);//贷款余额
                stHousingBusinessDetailsExtension.setXqbjje(BigDecimal.ZERO);//下期本金
                stHousingBusinessDetailsExtension.setXqfse(BigDecimal.ZERO);//下期发生额
                stHousingBusinessDetailsExtension.setXqlxje(BigDecimal.ZERO);//下期利息金额
            }
            stHousingBusinessDetailsExtension.setHkzh(personalAccount.getHKZH());//还款账号
            stHousingBusinessDetailsExtension.setJkrxm(personalAccount.getJKRXM());//借款人姓名
            stHousingBusinessDetailsExtension.setZhkhyhmc(personalAccount.getZHKHYHMC());
            stHousingBusinessDetailsExtension.setZhkhyhdm(personalAccount.getZHKHYHDM());
            stHousingBusinessDetailsExtension.setYwwd(personalAccount.getYWWD());
            stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
            chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);

            String zhkhyhdm = personalAccount.getZHKHYHDM();
            if ("104".equals(zhkhyhdm)) {
                LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                loanDeductionFileOther.setNo(totalNumber + "");
                loanDeductionFileOther.setAmt(currentJine);
                loanDeductionFileOther.setDeAcctNo(personalAccount.getHKZH());
                loanDeductionFileOther.setDeAcctName(personalAccount.getJKRXM());
                loanDeductionFileOther.setDeChgNo("104100000004");
                loanDeductionFileOther.setFullMark("0");
                loanDeductionFileOther.setSummary(housingPersonInformationBasic.getJKRXM() + periodOfafterTime + "公积金贷款扣划");
                list2.add(loanDeductionFileOther);
            } else {
                LoanDeductionFileSelf loanDeductionFileSlef = new LoanDeductionFileSelf();
                loanDeductionFileSlef.setNo(totalNumber + "");
                loanDeductionFileSlef.setAmt(currentJine);
                loanDeductionFileSlef.setDeAcctNo(personalAccount.getHKZH());
                loanDeductionFileSlef.setDeAcctName(personalAccount.getJKRXM());
                loanDeductionFileSlef.setFullMark("0");
                loanDeductionFileSlef.setSummary(housingPersonInformationBasic.getJKRXM() + periodOfafterTime + "公积金贷款扣划");
                list2.add(loanDeductionFileSlef);//入结算平台
            }
            gBusinessDetails.add(chousingBusinessDetails);
            totalNumber += 1;
            totalMoney = totalMoney.add(currentJine);
        }

        obj.put("money", totalMoney);
        obj.put("num", totalNumber);
        return obj;
    }

    //部分
    public void partialRepaymentAccount(StHousingBusinessDetails chousingBusinessDetails) {
        try {
            CLoanHousingBusinessProcess hsingBusinessProcess = cloanHousingBusinessProcess.getByYWLSH(chousingBusinessDetails.getYwlsh());
            hsingBusinessProcess.setBjsj(new Date());

            CLoanApplyRepaymentVice loanApplyRepaymentVice = hsingBusinessProcess.getLoanApplyRepaymentVice();
            List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icLoanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", hsingBusinessProcess.getDkzh());
            }}, null, null, null, Order.DESC, null, null);

            //贷款账号
            StHousingPersonalAccount byDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
            BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
            BigDecimal dkff = byDkzh.getDkffe();
            BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
            Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
            BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
            String dkhkfs = byDkzh.getStHousingPersonalLoan().getDkhkfs();

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
//                bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, loanApplyRepaymentVice.getHkqc().intValue());
//                bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, loanApplyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
                bqbx = byDkzh.getDqyhje();
                bqlx = byDkzh.getDqyhlx();
                bjje = bqbx.subtract(bqlx);
                tqhkje = CommLoanAlgorithm.prepaymentKAmount(loanApplyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录
                syqc = dkqs.subtract(new BigDecimal(dqqc));
            }
            int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, loanApplyRepaymentVice.getHkqc().intValue(), loanApplyRepaymentVice.getYdkkrq());//计息天数
            BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
            BigDecimal tqhklx = tqhkje.subtract(tqhbje);
            BigDecimal sybj = byDkzh.getDkye().subtract(tqhbje).subtract(bjje);

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
            byDkzh.setHslxze(byDkzh.getHslxze().add(chousingBusinessDetails.getLxje()));
            byDkzh.setHsbjze(byDkzh.getDkffe().subtract(sybj));
            byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
            CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension = byDkzh.getcLoanHousingPersonalAccountExtension();
            cLoanHousingPersonalAccountExtension.setDqqc(new BigDecimal(1));
            cLoanHousingPersonalAccountExtension.setDkgbjhye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
            cLoanHousingPersonalAccountExtension.setDkgbjhqs(syqc.setScale(2, BigDecimal.ROUND_HALF_UP));
            if (currentPeriodRange.getCurrentPeriod() == 0) {
//                cLoanHousingPersonalAccountExtension.setDkxffrq(byDkzh.getDkffrq());
            } else {
                cLoanHousingPersonalAccountExtension.setDkxffrq(currentPeriodRange.getAfterTime());
            }
            cLoanHousingPersonalAccountExtension.setTqhksj(simM.format(loanApplyRepaymentVice.getYdkkrq()));//提前还款月份
            icLoanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

            iSaveAuditHistory.saveNormalBusiness(hsingBusinessProcess.getYwlsh(), LoanBusinessType.提前还款.getName(), "办结");

            //region 生成记账凭证
            int djsl = 2;
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

            VoucherAmount voucherAmount = new VoucherAmount();
            voucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "部分还款扣款");
            voucherAmount.setJinE(chousingBusinessDetails.getFse());
            JFSJ.add(voucherAmount);

            VoucherAmount voucherAmounts = new VoucherAmount();
            voucherAmounts.setJinE(chousingBusinessDetails.getBjje());
            voucherAmounts.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "部分还款扣款");
            DFSJ.add(voucherAmounts);

            VoucherAmount oucherAmount = new VoucherAmount();
            oucherAmount.setJinE(chousingBusinessDetails.getFse().subtract(chousingBusinessDetails.getBjje()));
            oucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "部分还款扣款");
            DFSJ.add(oucherAmount);
            ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
            for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                if (cterAccountInfo.getZHXZ().equals("01")) {
                    List<CAuditHistory> cAuditHistories = auditHistoryDAO.list(new HashMap<String, Object>() {{
                        this.put("ywlsh", hsingBusinessProcess.getYwlsh());
                    }}, null, null, null, Order.DESC, null, null);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", hsingBusinessProcess.getCzy(), cAuditHistories == null ? hsingBusinessProcess.getCzy() : cAuditHistories.get(0).getCzy(),
                            "", "管理员", VoucherBusinessType.提前还款.getCode(), VoucherBusinessType.提前还款.getCode(),
                            hsingBusinessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), cterAccountInfo.getYHZHHM(), byDkzh.getStHousingPersonalLoan().getZhkhyhdm());

                    if (voucherRes.getJZPZH() == null) {
                        hsingBusinessProcess.setSbyy(voucherRes.getMSG());
                        hsingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        hsingBusinessProcess.setJzpzh(voucherRes.getJZPZH());
                        hsingBusinessProcess.setStep(LoanBussinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(hsingBusinessProcess);
                }
            }

        } catch (ParseException e) {
            throw new ErrorException(e);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    //结清

    public void partialClearingAccount(StHousingBusinessDetails chousingBusinessDetails) {
        try {
            CLoanHousingBusinessProcess hsingBusinessProcess = cloanHousingBusinessProcess.getByYWLSH(chousingBusinessDetails.getYwlsh());
            hsingBusinessProcess.setStep(LoanBussinessStatus.已入账.getName());
            hsingBusinessProcess.setBjsj(new Date());
            CLoanApplyRepaymentVice loanApplyRepaymentVice = hsingBusinessProcess.getLoanApplyRepaymentVice();
            List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icLoanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", hsingBusinessProcess.getDkzh());
            }}, null, null, null, Order.DESC, null, null);

            cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
            //贷款账号
            StHousingPersonalAccount byDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
            BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
            Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
            BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
            BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());

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
            BigDecimal hkze = loanApplyRepaymentVice.getHkje();

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
            icLoanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

            iSaveAuditHistory.saveNormalBusiness(hsingBusinessProcess.getYwlsh(), LoanBusinessType.结清.getName(), "办结");

            //region 生成记账凭证
            int djsl = 2;
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

            VoucherAmount voucherAmount = new VoucherAmount();
            voucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "结清还款扣款");
            voucherAmount.setJinE(chousingBusinessDetails.getFse());
            JFSJ.add(voucherAmount);

            VoucherAmount voucherAmounts = new VoucherAmount();
            voucherAmounts.setJinE(chousingBusinessDetails.getBjje());
            voucherAmounts.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "结清还款扣款");
            DFSJ.add(voucherAmounts);

            VoucherAmount oucherAmount = new VoucherAmount();
            oucherAmount.setJinE(chousingBusinessDetails.getFse().subtract(chousingBusinessDetails.getBjje()));
            oucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "结清还款扣款");
            DFSJ.add(oucherAmount);
            ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
            for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                if (cterAccountInfo.getZHXZ().equals("01")) {
                    List<CAuditHistory> cAuditHistories = auditHistoryDAO.list(new HashMap<String, Object>() {{
                        this.put("ywlsh", hsingBusinessProcess.getYwlsh());
                    }}, null, null, null, Order.DESC, null, null);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", hsingBusinessProcess.getCzy(), cAuditHistories == null ? hsingBusinessProcess.getCzy() : cAuditHistories.get(0).getCzy(),
                            "", "管理员", VoucherBusinessType.提前还清.getCode(), VoucherBusinessType.提前还清.getCode(),
                            hsingBusinessProcess.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), cterAccountInfo.getYHZHHM(), byDkzh.getStHousingPersonalLoan().getZhkhyhdm());

                    if (voucherRes.getJZPZH() == null) {
                        hsingBusinessProcess.setSbyy(voucherRes.getMSG());
                        hsingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        hsingBusinessProcess.setJzpzh(voucherRes.getJZPZH());
                        hsingBusinessProcess.setStep(LoanBussinessStatus.已入账.getName());
                    }
                    cloanHousingBusinessProcess.update(hsingBusinessProcess);
                }
            }
        } catch (ParseException e) {
            throw new ErrorException(e);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public void NormalRepaymentReadjustmentDeduction(StHousingBusinessDetails hsingBusinessDetails) {
        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icLoanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", hsingBusinessDetails.getDkzh());
        }}, null, null, null, null, null, null);

        StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
        BigDecimal sjqc = stHousingPersonalAccount.getDkqs().subtract(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()).add(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc());
        if (hsingBusinessDetails.getDqqc().equals(stHousingPersonalAccount.getDkqs())) {
            stHousingPersonalAccount.setDkye(BigDecimal.ZERO);
            stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqjhhkje(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqjhghbj(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqjhghlx(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqyhje(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqyhlx(BigDecimal.ZERO);
            stHousingPersonalAccount.setDqyhbj(BigDecimal.ZERO);
            stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
            cLoanHousingPersonInformationBasics.get(0).setYhqs(stHousingPersonalAccount.getDkqs());
            List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", stHousingPersonalAccount.getDkzh());
                this.put("cHousingOverdueRegistrationExtension.ywzt", Arrays.asList(LoanBussinessStatus.待入账.getCode()
                        , LoanBussinessStatus.扣款已发送.getCode(), LoanBussinessStatus.入账失败.getCode()));
            }}, null, null, null, null, null, null);
            if (stHousingOverdueRegistrations.size() == 0) {
                stHousingPersonalAccount.setDkjqrq(new Date());
                stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getDkffe());
                cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
            } else {
                stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getHsbjze().add(hsingBusinessDetails.getBjje()));
            }
        } else {
            stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().setDkyezcbj(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());
            stHousingPersonalAccount.setDkye(hsingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());//贷款余额
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

        try {
            //region 生成记账凭证
            int djsl = 2;
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
            VoucherAmount voucherAmount = new VoucherAmount();
            voucherAmount.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "月缴存额还款");
            voucherAmount.setJinE(hsingBusinessDetails.getFse());
            JFSJ.add(voucherAmount);
            VoucherAmount voucherAmounts = new VoucherAmount();
            voucherAmounts.setJinE(hsingBusinessDetails.getBjje());
            voucherAmounts.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "月缴存额还款");
            DFSJ.add(voucherAmounts);
            VoucherAmount oucherAmount = new VoucherAmount();
            oucherAmount.setJinE(hsingBusinessDetails.getFse().subtract(hsingBusinessDetails.getBjje()));
            oucherAmount.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "月缴存额还款");
            DFSJ.add(oucherAmount);
            ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhmc());
            for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                if (cterAccountInfo.getZHXZ().equals("01")) {
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                            "", "管理员", VoucherBusinessType.正常还款.getCode(), VoucherBusinessType.正常还款.getCode(),
                            hsingBusinessDetails.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), cterAccountInfo.getYHZHHM(), stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhdm());
                    if (voucherRes.getJZPZH() == null) {
                        hsingBusinessDetails.getGrywmx().setSbyy(voucherRes.getMSG());
                        hsingBusinessDetails.getGrywmx().setStep(LoanBussinessStatus.入账失败.getName());
                    } else {
                        hsingBusinessDetails.getGrywmx().setJzpzh(voucherRes.getJZPZH());
                        hsingBusinessDetails.setJzrq(new Date());
                        hsingBusinessDetails.getGrywmx().setStep(LoanBussinessStatus.已入账.getName());
                    }
                    stHousingBusinessDetailsDAO.update(hsingBusinessDetails);
                }
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    public void OverdueDunksPlan(StHousingBusinessDetails hsingBusinessDetails, StHousingOverdueRegistration stHousingOverdueRegistration) {
        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icLoanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", hsingBusinessDetails.getDkzh());
        }}, null, null, null, null, null, null);
        StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
        stHousingPersonalAccount.setYqlxze(stHousingPersonalAccount.getYqlxze().add(hsingBusinessDetails.getLxje()).setScale(2, BigDecimal.ROUND_HALF_UP));
        stHousingPersonalAccount.setYqbjze(stHousingPersonalAccount.getYqbjze().add(hsingBusinessDetails.getBjje()).setScale(2, BigDecimal.ROUND_HALF_UP));
        stHousingPersonalAccount.setHsbjze(stHousingPersonalAccount.getHsbjze().add(hsingBusinessDetails.getBjje()));
        stHousingPersonalAccount.setHslxze(stHousingPersonalAccount.getHslxze().add(hsingBusinessDetails.getLxje()));
        stHousingPersonalAccount.setFxze(stHousingPersonalAccount.getFxze().add(hsingBusinessDetails.getFxje()).setScale(2, BigDecimal.ROUND_HALF_UP));
        stHousingPersonalAccount.setLjyqqs(stHousingPersonalAccount.getLjyqqs().add(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
        stHousingPersonalAccount.setDkye(stHousingPersonalAccount.getDkye().subtract(hsingBusinessDetails.getBjje()));
        List<StHousingOverdueRegistration> list = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
            //数据唯一性验证  重扣划是作废
            this.put("dkzh", hsingBusinessDetails.getDkzh());
        }}, null, null, null, null, null, null);
        int i = 0;
        for (StHousingOverdueRegistration stHousingOverdue : list) {
            if (!TaskBusinessStatus.已入账.getName().equals(stHousingOverdue.getcHousingOverdueRegistrationExtension().getYwzt())) {
                i++;
            }
        }
        if (i == 0) cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.正常.getCode());
        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

        hsingBusinessDetails.getcHousingBusinessDetailsExtension().setXqdkye(stHousingPersonalAccount.getDkye());

        stHousingOverdueRegistration.setSsrq(new Date());
        stHousingOverdueRegistration.setSsyqfxje(hsingBusinessDetails.getFxje());
        stHousingOverdueRegistration.setSsyqlxje(hsingBusinessDetails.getLxje());
        stHousingOverdueRegistration.setSsyqbjje(hsingBusinessDetails.getBjje());
        stHousingOverdueRegistration.getcHousingOverdueRegistrationExtension().setYwzt(LoanBussinessStatus.已入账.getName());
        stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistration);

        try {
            //region 生成记账凭证
            int djsl = 2;
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
            VoucherAmount voucherAmount = new VoucherAmount();
            voucherAmount.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "逾期还款申请扣款");
            voucherAmount.setJinE(hsingBusinessDetails.getFse());
            JFSJ.add(voucherAmount);
            VoucherAmount voucherAmounts = new VoucherAmount();
            voucherAmounts.setJinE(hsingBusinessDetails.getBjje());
            voucherAmounts.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "逾期还款申请扣款");
            DFSJ.add(voucherAmounts);
            VoucherAmount oucherAmount = new VoucherAmount();
            oucherAmount.setJinE(hsingBusinessDetails.getFse().subtract(hsingBusinessDetails.getBjje()));
            oucherAmount.setZhaiYao(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm() + "逾期还款申请扣款");
            DFSJ.add(oucherAmount);
            ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhmc());
            for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                if (cterAccountInfo.getZHXZ().equals("01")) {
                    voucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                            "", "管理员", VoucherBusinessType.提前还款.getCode(), VoucherBusinessType.提前还款.getCode(),
                            hsingBusinessDetails.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), cterAccountInfo.getYHZHHM(), stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhdm());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}