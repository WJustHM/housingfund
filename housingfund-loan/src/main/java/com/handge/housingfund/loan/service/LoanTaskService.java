package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.LoanWithdrawl;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionFileOther;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionFileSelf;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionIn;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionOut;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.JKRAndGTJKR;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlLoan;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.IClearingBank;
import com.handge.housingfund.common.service.loan.IExceptionMethod;
import com.handge.housingfund.common.service.loan.ILoanTaskService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.schedule.ITimeTask;
import com.handge.housingfund.common.service.schedule.enums.TimeTaskType;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.TaskBusinessStatus;
import com.handge.housingfund.database.enums.TaskBusinessType;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by Funnyboy on 2017/9/29.
 */

@Component
public class LoanTaskService implements ILoanTaskService {
    private static Logger logger = LogManager.getLogger(LoanTaskService.class);
    //    new Logger.
    private static Object lock = new Object();
    @Autowired
    ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    ICommCheckMethod icommCheckMethod;
    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    private IStHousingPersonalAccountDAO stHousingPersonalAccount;
    @Autowired
    private IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;
    @Autowired
    private IStHousingOverdueRegistrationDAO stHousingOverdueRegistrationDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icloanHousingPersonInformationBasicDAO;
    @Autowired
    private WithdrawlTasks withdrawlTasks;
    @Autowired
    private IBank iBank;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IStCommonPersonDAO stCommonPerson;
    @Autowired
    private ISMSCommon ismsCommon;
    @Autowired
    private IExceptionMethod exceptionMethod;
    @Autowired
    private IClearingBank iclearingBank;
    private final SimpleDateFormat simMs = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private HibernateTransactionManager txManager;
    private final SimpleDateFormat simM = new SimpleDateFormat("yyyy-MM");
    @Autowired
    private IVoucherManagerService voucherManagerService;
    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simData = new SimpleDateFormat("yyyyMMdd");

    //date=2017-12-10 kkr
    public boolean range(Date gdyf, Date zhydkkr, Date overTime) {
        try {
            return DateUtil.dateStringtoStringDate(overTime, "yyyy-MM-dd").getTime() >= DateUtil.dateStringtoStringDate(zhydkkr, "yyyy-MM-dd").getTime() && DateUtil.dateStringtoStringDate(zhydkkr, "yyyy-MM-dd").getTime() >= DateUtil.dateStringtoStringDate(gdyf, "yyyy-MM-dd").getTime();
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public CLoanHousingPersonInformationBasic binSearch(List<CLoanHousingPersonInformationBasic> cloanHousingPersonInformationBasic, String dkzh) {
        int mid = cloanHousingPersonInformationBasic.size() / 2;
        if (dkzh.equals(cloanHousingPersonInformationBasic.get(mid).getDkzh())) {
            return cloanHousingPersonInformationBasic.get(mid);
        }

        int start = 0;
        int end = cloanHousingPersonInformationBasic.size() - 1;
        while (start <= end) {
            mid = (end - start) / 2 + start;
            if (dkzh.compareTo(cloanHousingPersonInformationBasic.get(mid).getDkzh()) < 0) {
                end = mid - 1;
            } else if (dkzh.compareTo(cloanHousingPersonInformationBasic.get(mid).getDkzh()) > 0) {
                start = mid + 1;
            } else {
                return cloanHousingPersonInformationBasic.get(mid);
            }
        }
        return null;
    }


    @Override
    public void rehuankuanjihua() {
        synchronized (lock) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<Object[]> list = icloanHousingPersonInformationBasicDAO.getLoan2();
            BigDecimal bigDecimal = null;
            BigDecimal currentBX = null;
            BigDecimal overdueThisPeriodLX = null;
            BigDecimal bjje = null;
            int count = 0;
            for (Object[] obj : list) {
                String id = (String) obj[0];
                BigDecimal dkll = (BigDecimal) obj[1];
                BigDecimal llfdbl = (BigDecimal) obj[2];

                BigDecimal dkgbjhqs = (BigDecimal) obj[3];
                BigDecimal dkgbjhye = (BigDecimal) obj[4];
                BigDecimal dqqc = (BigDecimal) obj[5];

                String dkhkfs = (String) obj[6];

                if (dkgbjhqs.compareTo(BigDecimal.ZERO) > 0) {
                    bigDecimal = CommLoanAlgorithm.lendingRate(dkll, llfdbl);
                    currentBX = CommLoanAlgorithm.currentBX(dkgbjhye, Integer.parseInt(dkgbjhqs.toString()), dkhkfs, bigDecimal, Integer.parseInt(dqqc.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    overdueThisPeriodLX = CommLoanAlgorithm.overdueThisPeriodLX(dkgbjhye, Integer.parseInt(dqqc.toString()), dkhkfs, bigDecimal, Integer.parseInt(dkgbjhqs.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    bjje = currentBX.subtract(overdueThisPeriodLX);

                    icloanHousingPersonInformationBasicDAO.updateLoan(id, currentBX, bjje, overdueThisPeriodLX, currentBX, overdueThisPeriodLX, bjje);
                    count++;
                    if (count % 500 == 0) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                icloanHousingPersonInformationBasicDAO.flush();
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void repayment(String yhdm, Date kkyf, Date overTime) {
        synchronized (lock) {
            try {
                Calendar instancenew = Calendar.getInstance();
                instancenew.setTime(new Date());
                instancenew.add(Calendar.DAY_OF_MONTH, -1);
                kkyf = sim.parse("2017-12-01");
                overTime = sim.parse(sim.format(instancenew.getTime()));
                yhdm = null;
            } catch (Exception e) {
                throw new ErrorException(e);
            }

            while (true) {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
                TransactionStatus status = txManager.getTransaction(def); // 获得事务状态

                int totalNumber = 1;
                BigDecimal totalMoney = BigDecimal.ZERO;
                String yhzhmc = null;
                String yhkhdm = null;
                boolean first = true;//是否是第一次
                String ywlsh = null;
                String id = null;
                try {
                    //业务表
                    List<StHousingBusinessDetails> gBusinessDetails = new ArrayList<>();//如业务记录表
                    List<LoanDeductionFileSelf> list2 = new ArrayList<>();//结算平台批量明细

                    ArrayList<BackBasicInfomation> backBasicInfomations = icloanHousingPersonInformationBasicDAO.repamentNor(yhdm, kkyf, overTime);
                    if (backBasicInfomations.size() == 0) return;
                    //逾期信息记录
                    List<OvderDueRecord> overduelinklist = stHousingOverdueRegistrationDAO.searchRecord();
                    //业务明细记录
                    List<OvderDueRecord> detailslinklist = stHousingBusinessDetailsDAO.searchRecord();
                    List<Object> stringList = cloanHousingBusinessProcess.countRepament();

                    //查看每个账号业务是否产生，details过滤，本期业务是否全部产生
                    boolean checkaccount = true;
                    int count = 0;
                    List<BackBasicInfomation> detailsbasic = new ArrayList<>();
                    BigDecimal yqsjqc = null;
                    for (BackBasicInfomation basic : backBasicInfomations) {
                        if (basic.getDQYHJE().compareTo(BigDecimal.ZERO) == 0) continue;
                        for (Object dkzh : stringList) {
                            if (basic.getDKZH().equals(dkzh)) continue;
                        }
                        yqsjqc = basic.getDQQC().add(basic.getDKQS().subtract(basic.getDKGBJHQS()));
                        //循环details查看业务明细记录（正常还款）
                        boolean min = false;
                        BigDecimal tailsqc = BigDecimal.ZERO;
                        for (OvderDueRecord tails : detailslinklist) {
                            //正常还款生成了，但是失败了继续扣
                            if (tails.getDKZH().equals(basic.getDKZH())) {
                                if ((tails.getDQQC().compareTo(yqsjqc))<=0) {
                                    if (tails.getDKZH().equals(basic.getDKZH()) && tails.getDQQC().compareTo(yqsjqc) == 0) {
                                        count++;
                                        //没发生
                                        if (LoanBussinessStatus.待入账.getName().equals(tails.getYWZT())) {
                                            checkaccount = false;
                                            Session session = this.txManager.getSessionFactory().getCurrentSession();
                                            stHousingBusinessDetailsDAO.deleteBackLoanDuction(session, tails.getId());
                                            detailsbasic.add(basic);
                                            break;
                                        }
                                        break;
                                    }
                                } else {
                                    min = true;
                                    tailsqc = tails.getDQQC();
                                    break;
                                }
                            }
                        }
                        if (min) {
                            logger.error("正常还款期次小于当前业务明细最大期次：" + basic.getDKZH() + ", yqsjqc: " + yqsjqc + ", tailsqc: " + tailsqc);
                            continue;
                        }
                        //没发生
                        if (count == 0) {
                            detailsbasic.add(basic);
                        }
                        count = 0;
                    }
                    //全部
                    if (detailsbasic.size() != 0) checkaccount = false;
                    if (checkaccount == true) return;


                    //总的业务记录表
                    CLoanHousingBusinessProcess housingBusinessProcess = new CLoanHousingBusinessProcess();
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
                    housingBusinessProcess.setYwwd(network);
                    housingBusinessProcess.setCznr(LoanBusinessType.正常还款.getCode());
                    housingBusinessProcess.setStep(TaskBusinessStatus.扣款已发送.getName());
                    housingBusinessProcess.setBlsj(new Date());

                    int number = 0;
                    for (BackBasicInfomation housingPersonInformationBasic : detailsbasic) {
                        if (number == 300) break;
                        number += 1;
                        //验证数据格式问题
                        if (housingPersonInformationBasic.getDQYHBJ() == null) continue;//当期应还本金
                        if (housingPersonInformationBasic.getDQYHLX() == null) continue;//当期应还利息
                        if (housingPersonInformationBasic.getDKFFE() == null) continue;//合同贷款金额
                        if (housingPersonInformationBasic.getDKQS() == null) continue;//贷款期数
                        if (housingPersonInformationBasic.getDKLL() == null) continue;//贷款利率
                        if (housingPersonInformationBasic.getDKGBJHYE() == null)
                            continue;
                        if (housingPersonInformationBasic.getDKGBJHQS() == null)
                            continue;
                        if (housingPersonInformationBasic.getDKXFFRQ() == null)
                            continue;
                        if (housingPersonInformationBasic.getDQQC() == null)
                            continue;
                        if (housingPersonInformationBasic.getDKFFRQ() == null) continue;//贷款发放日期
                        if (housingPersonInformationBasic.getDQYHJE() == null) continue;//当期应还金额
                        if (housingPersonInformationBasic.getDKHKFS() == null) continue;
                        if (housingPersonInformationBasic.getDKQS() == null || housingPersonInformationBasic.getDKQS().compareTo(BigDecimal.ZERO) == 0)
                            continue;


                        //获取当期的结束时间
                        Date periodOfafterTime = CommLoanAlgorithm.periodOfafterTime(housingPersonInformationBasic.getDKXFFRQ(), housingPersonInformationBasic.getDQQC().intValue());
                        BigDecimal sjqc = housingPersonInformationBasic.getDQQC().add(housingPersonInformationBasic.getDKQS().subtract(housingPersonInformationBasic.getDKGBJHQS()));

                        //先换逾期记录,判断是否有逾期记录
                        boolean yqxx = true;
                        boolean ywsffs = false;
                        for (OvderDueRecord overdueRegistrations : overduelinklist) {
                            if (overdueRegistrations.getDKZH().equals(housingPersonInformationBasic.getDKZH())) {
                                if (overdueRegistrations.getDQQC().equals(sjqc)) {
                                    //先查看本期的逾期是否已经写入
                                    ywsffs = true;
                                    //如果有没有入账的,
                                    yqxx = false;
                                    break;
                                }
                            }
                        }

                        //------------逾期-----------
                        //当期逾期记录,基本不可能进来的,继续逾期---
                        if (ywsffs == false && yqxx == false) {
                            //到了还款日(数据库数据)
                            CLoanHousingBusinessProcess housingBusinessProcessnew = new CLoanHousingBusinessProcess();
                            housingBusinessProcessnew.setYwwd(network);
                            housingBusinessProcessnew.setCznr(TaskBusinessType.正常还款.getCode());//还款类型
                            housingBusinessProcessnew.setStep(TaskBusinessStatus.逾期.getName());//入账状态
                            housingBusinessProcessnew.setBlsj(new Date());
                            housingBusinessProcessnew.setDkzh(housingPersonInformationBasic.getDKZH());

                            BigDecimal dkffe = housingPersonInformationBasic.getDKGBJHYE();//合同贷款金额
                            int dkqs = Integer.parseInt(housingPersonInformationBasic.getDKGBJHQS().toString());//贷款期数
                            String dkhkfs = housingPersonInformationBasic.getDKHKFS();//贷款还款方式
                            BigDecimal dkll = CommLoanAlgorithm.lendingRate(housingPersonInformationBasic.getDKLL(), housingPersonInformationBasic.getLLFDBL());//贷款利率
                            Date dkffrq = housingPersonInformationBasic.getDKXFFRQ();//贷款发放日期
                            int dqqc = Integer.parseInt(housingPersonInformationBasic.getDQQC().toString());

                            LinkedList<HousingfundAccountPlanGetInformation> housingfundAccountPlanGetInformation = CommLoanAlgorithm.repaymentPlan(dkffe, dkqs, dkhkfs, dkll, sim.format(dkffrq));//还款计划
                            HousingfundAccountPlanGetInformation fundAccountPlanGetInformation = null;
                            if (dqqc != dkqs) {
                                fundAccountPlanGetInformation = housingfundAccountPlanGetInformation.get(housingPersonInformationBasic.getDQQC().intValue());//下期计划,从零开始的
                            }

                            //生成逾期记录
                            StHousingOverdueRegistration husingOverdueRegistration = new StHousingOverdueRegistration();
                            husingOverdueRegistration.setDkzh(housingPersonInformationBasic.getDKZH());//贷款账号
                            husingOverdueRegistration.setYqqc(sjqc);//逾期期次
                            husingOverdueRegistration.setYqbj(housingPersonInformationBasic.getDQYHBJ());//逾期本金
                            husingOverdueRegistration.setYqlx(housingPersonInformationBasic.getDQYHLX());//逾期利息
                            CHousingOverdueRegistrationExtension housingOverdueRegistrationExtension = new CHousingOverdueRegistrationExtension();
                            housingOverdueRegistrationExtension.setYwzt(TaskBusinessStatus.待入账.getName());//是
                            husingOverdueRegistration.setcHousingOverdueRegistrationExtension(housingOverdueRegistrationExtension);
                            stHousingOverdueRegistrationDAO.save(husingOverdueRegistration);

                            //业务明细记录
                            List<StHousingBusinessDetails> lists = new ArrayList<>();
                            StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                            chousingBusinessDetails.setDkzh(housingPersonInformationBasic.getDKZH());//贷款账号
                            chousingBusinessDetails.setDkywmxlx(TaskBusinessType.正常还款.getCode());//贷款业务类型
                            chousingBusinessDetails.setYwfsrq(periodOfafterTime);//业务发生日期
                            chousingBusinessDetails.setFse(housingPersonInformationBasic.getDQYHBJ().add(housingPersonInformationBasic.getDQYHLX()));//发生额
                            chousingBusinessDetails.setBjje(housingPersonInformationBasic.getDQYHBJ());//本金金额
                            chousingBusinessDetails.setLxje(housingPersonInformationBasic.getDQYHLX());//利息金额
//                            chousingBusinessDetails.setJzrq(periodOfafterTime);//记账日期
                            chousingBusinessDetails.setDkyhdm(housingPersonInformationBasic.getZHKHYHDM());
                            chousingBusinessDetails.setDqqc(sjqc);//当期期次
                            CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                            stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.逾期.getName());//业务状态
                            stHousingBusinessDetailsExtension.setZhkhyhdm(housingPersonInformationBasic.getZHKHYHDM());
                            stHousingBusinessDetailsExtension.setZhkhyhmc(housingPersonInformationBasic.getZHKHYHMC());
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
                            stHousingBusinessDetailsExtension.setJkrxm(housingPersonInformationBasic.getJKRXM());//借款人姓名
                            stHousingBusinessDetailsExtension.setHkzh(housingPersonInformationBasic.getHKZH());//还款账号
                            stHousingBusinessDetailsExtension.setYwwd(housingPersonInformationBasic.getYWWD());
                            chousingBusinessDetails.setGrywmx(housingBusinessProcessnew);
                            chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                            lists.add(chousingBusinessDetails);
                            housingBusinessProcessnew.setStHousingBusinessDetails(lists);
                            String ids = cloanHousingBusinessProcess.save(housingBusinessProcessnew);//更新业务状态
                            CLoanHousingBusinessProcess cLoanHousingBusinessProcess = cloanHousingBusinessProcess.get(ids);
                            cLoanHousingBusinessProcess.getStHousingBusinessDetails().get(0).setYwlsh(cLoanHousingBusinessProcess.getYwlsh());
                            cloanHousingBusinessProcess.update(cLoanHousingBusinessProcess);

                            //结清，最后一期
                            if (housingPersonInformationBasic.getDQQC().equals(housingPersonInformationBasic.getDKGBJHQS())) {
                                stHousingPersonalAccount.updateOver(housingPersonInformationBasic.getCLHPIBID(), LoanAccountType.逾期.getCode()
                                        , BigDecimal.ZERO
                                        , BigDecimal.ZERO
                                        , BigDecimal.ZERO
                                        , BigDecimal.ZERO
                                        , BigDecimal.ZERO
                                        , BigDecimal.ZERO
                                        , housingPersonInformationBasic.getLJYQQS().add(new BigDecimal(1))
                                        , housingPersonInformationBasic.getYQBJZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKBJJE()))
                                        , housingPersonInformationBasic.getYQLXZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKLXJE()))
                                        , housingPersonInformationBasic.getDKGBJHQS()
                                        , BigDecimal.ZERO);
                            } else {
                                //正常
                                stHousingPersonalAccount.updateOver(housingPersonInformationBasic.getCLHPIBID(), LoanAccountType.逾期.getCode()
                                        , new BigDecimal(fundAccountPlanGetInformation.getFSE())
                                        , new BigDecimal(fundAccountPlanGetInformation.getHKBJJE())
                                        , new BigDecimal(fundAccountPlanGetInformation.getHKLXJE())
                                        , new BigDecimal(fundAccountPlanGetInformation.getFSE())
                                        , new BigDecimal(fundAccountPlanGetInformation.getHKBJJE())
                                        , new BigDecimal(fundAccountPlanGetInformation.getHKLXJE())
                                        , housingPersonInformationBasic.getLJYQQS().add(new BigDecimal(1))
                                        , housingPersonInformationBasic.getYQBJZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKBJJE()))
                                        , housingPersonInformationBasic.getYQLXZE().add(new BigDecimal(housingfundAccountPlanGetInformation.get(dqqc - 1).getHKLXJE()))
                                        , new BigDecimal(dqqc + 1)
                                        , new BigDecimal(fundAccountPlanGetInformation.getDKYE()));
                            }
                        }
                        if (yqxx == false) continue;

                        //验证数据唯一性,此次业务是否发生
                        if (stHousingBusinessDetailsDAO.getBuinessCount(housingPersonInformationBasic.getDKZH(), sjqc).compareTo(BigInteger.ZERO) > 0)
                            continue;

                        //开始正常还款流程
                        if (first) {
//                            if ((periodOfafterTime.getTime() <= DateUtil.dateStringtoStringDate(new Date()).getTime() && DateUtil.dateStringtoStringDate(new Date()).getTime() <= (periodOfafterTime.getTime() + 172800000))) {//判断当前时间是否等于约定还款时间5
                            yhzhmc = housingPersonInformationBasic.getZHKHYHMC();//银行账户名称
                            yhkhdm = housingPersonInformationBasic.getZHKHYHDM();
                            if (yhzhmc == null) continue;
                            first = false;
                            HashMap<String, Object> obj = icommCheckMethod.firstCehck(housingPersonInformationBasic, periodOfafterTime, housingBusinessProcess, gBusinessDetails, list2, totalMoney, totalNumber);
                            totalMoney = (BigDecimal) obj.get("money");
                            totalNumber = (int) obj.get("num");
//                            }
                        } else {
//                            if (periodOfafterTime.getTime() <= DateUtil.dateStringtoStringDate(new Date()).getTime() && DateUtil.dateStringtoStringDate(new Date()).getTime() <= (periodOfafterTime.getTime() + 172800000)) {//判断当前时间是否等于约定还款时间5
                            if (yhkhdm.equals(housingPersonInformationBasic.getZHKHYHDM())) {
                                HashMap<String, Object> obj1 = icommCheckMethod.firstCehck(housingPersonInformationBasic, periodOfafterTime, housingBusinessProcess, gBusinessDetails, list2, totalMoney, totalNumber);
                                totalMoney = (BigDecimal) (obj1.get("money"));
                                totalNumber = (int) (obj1.get("num"));
                            }
                        }
//                        }
                    }
                    stHousingPersonalAccount.flush();
                    //调用结算平台
                    try {
                        if (list2.size() > 0) {
                            id = cloanHousingBusinessProcess.save(housingBusinessProcess);
                            CLoanHousingBusinessProcess businessProcess = cloanHousingBusinessProcess.get(id);
                            ywlsh = businessProcess.getYwlsh();
                            stHousingBusinessDetailsDAO.updateBatch(gBusinessDetails, ywlsh, id);
                        }
                        //错就回滚，不影响业务
                        txManager.commit(status);
                    } catch (Exception e) {
                        logger.error(LogUtil.getTrace(e));
                        txManager.rollback(status);
                        throw new ErrorException(e);
                    }
                    if (list2.size() > 0) {
                        LoanDeductionIn loandeductionIn = iclearingBank.sendLoaneMessage(ywlsh, yhzhmc, totalMoney, totalNumber);
                        LoanDeductionOut loanDeductionOut = iBank.sendMsg(loandeductionIn, list2);
                        if ("1".equals(loanDeductionOut.getCenterHeadOut().getTxStatus())) {//入账失败
                            throw new Exception("入账失败--:" + loanDeductionOut.getCenterHeadOut().getRtnMessage());
                        }
                    }
                } catch (Exception e) {
                    TransactionStatus status2 = txManager.getTransaction(def); // 获得事务状态
                    logger.error(LogUtil.getTrace(e));
                    e.printStackTrace();
                    try {
                        exceptionMethod.exceptionSelf(e, id, ywlsh, yhzhmc);
                        try {
                            //绝对不会错
                            txManager.commit(status2);
                        } catch (Exception e2) {
                            txManager.rollback(status2);
                        }
                    } catch (Exception ex) {
                        logger.error(LogUtil.getTrace(ex));
                        throw new ErrorException(ex);
                    }
                }
            }
        }
    }


    /**
     * 正常还款入账失败余额不足转逾期
     */
    @Override
    public void overdueRepaymentChange() {
        synchronized (lock) {
            List<StHousingBusinessDetails> stsingBusinessDetails = stHousingBusinessDetailsDAO.list(
                    new HashMap<String, Object>() {{
                        this.put("dkywmxlx", LoanBusinessType.正常还款.getCode());
                        this.put("cHousingBusinessDetailsExtension.ywzt", Arrays.asList(TaskBusinessStatus.入账失败.getName()));
                    }}, null, null, null, null, null, null);
            StHousingOverdueRegistration husingOverdueRegistration = null;
            int number = 0;
            for (StHousingBusinessDetails BusinessDetails : stsingBusinessDetails) {
                if (!BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("余额不足")
                        && !BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("没有足够可用资金"))
                    continue;
                number++;
                //更新账户信息表
                List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                    this.put("dkzh", BusinessDetails.getDkzh());
                }}, null, null, null, null, null, null);

                StHousingPersonalAccount stHousingPersonalAccounts = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
                if (new Date().getTime() >= (BusinessDetails.getYwfsrq().getTime() + 259200000)) {//2
                    husingOverdueRegistration = new StHousingOverdueRegistration();
                    husingOverdueRegistration.setDkzh(BusinessDetails.getDkzh());//贷款账号
                    husingOverdueRegistration.setYqqc(BusinessDetails.getDqqc());//逾期期次
                    husingOverdueRegistration.setYqbj(BusinessDetails.getBjje());//逾期本金
                    husingOverdueRegistration.setYqlx(BusinessDetails.getLxje());//逾期利息
                    CHousingOverdueRegistrationExtension housingOverdueRegistrationExtension = new CHousingOverdueRegistrationExtension();
                    housingOverdueRegistrationExtension.setYwzt(TaskBusinessStatus.待入账.getName());//是
                    husingOverdueRegistration.setcHousingOverdueRegistrationExtension(housingOverdueRegistrationExtension);
                    List<StHousingOverdueRegistration> list = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                        this.put("dkzh", BusinessDetails.getDkzh());
                        this.put("yqqc", BusinessDetails.getDqqc());
                    }}, null, null, null, null, null, null);
                    if (list.size() == 0) {
                        stHousingOverdueRegistrationDAO.save(husingOverdueRegistration);
                    } else {
                        BusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.逾期.getName());
                        BusinessDetails.getcHousingBusinessDetailsExtension().setYwwd(cLoanHousingPersonInformationBasics.get(0).getYwwd());
                        stHousingBusinessDetailsDAO.update(BusinessDetails);
                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.逾期.getCode());
                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));
                        continue;
                    }
                    BusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.逾期.getName());
                    BusinessDetails.getcHousingBusinessDetailsExtension().setYwwd(cLoanHousingPersonInformationBasics.get(0).getYwwd());
                    BusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                    stHousingBusinessDetailsDAO.update(BusinessDetails);

                    BigDecimal sjqc = stHousingPersonalAccounts.getDkqs().subtract(stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()).add(stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().getDqqc());
                    //结清，最后一期
                    if (stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().getDqqc().equals(
                            stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().getDkgbjhqs())) {
                        stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqjhhkje(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqjhghbj(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqjhghlx(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqyhje(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqyhlx(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setDqyhbj(BigDecimal.ZERO);
                        stHousingPersonalAccounts.setLjyqqs(stHousingPersonalAccounts.getLjyqqs().add(new BigDecimal(1)));
                        stHousingPersonalAccounts.setYqbjze(stHousingPersonalAccounts.getYqbjze().add(BusinessDetails.getBjje()));
                        stHousingPersonalAccounts.setYqlxze(stHousingPersonalAccounts.getYqlxze().add(BusinessDetails.getLxje()));
                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.逾期.getCode());
                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));
                    } else if (sjqc.equals(BusinessDetails.getDqqc())) {
                        //正常
                        stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye());//贷款余额
                        stHousingPersonalAccounts.setDqyhje(BusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());//发生额
                        stHousingPersonalAccounts.setDqyhbj(BusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje()); //还款本金
                        stHousingPersonalAccounts.setDqyhlx(BusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());//利息金额
                        stHousingPersonalAccounts.setDqjhhkje(BusinessDetails.getcHousingBusinessDetailsExtension().getXqfse());
                        stHousingPersonalAccounts.setDqjhghlx(BusinessDetails.getcHousingBusinessDetailsExtension().getXqlxje());
                        stHousingPersonalAccounts.setDqjhghbj(BusinessDetails.getcHousingBusinessDetailsExtension().getXqbjje());
                        stHousingPersonalAccounts.setLjyqqs(stHousingPersonalAccounts.getLjyqqs().add(new BigDecimal(1)));
                        stHousingPersonalAccounts.setYqbjze(stHousingPersonalAccounts.getYqbjze().add(BusinessDetails.getBjje()));
                        stHousingPersonalAccounts.setYqlxze(stHousingPersonalAccounts.getYqlxze().add(BusinessDetails.getLxje()));
                        cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.逾期.getCode());
                        stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().setDqqc(stHousingPersonalAccounts.getcLoanHousingPersonalAccountExtension().getDqqc().add(new BigDecimal(1)));
                        icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));
                    }
                    //短信
                    try {
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(cLoanHousingPersonInformationBasics.get(0).getJkrxm());
                        arr.add(stHousingPersonalAccounts.getDkzh().substring(stHousingPersonalAccounts.getDkzh().length() - 4));
                        arr.add(husingOverdueRegistration.getYqqc()+"");
                        arr.add(husingOverdueRegistration.getYqbj().add(husingOverdueRegistration.getYqlx()) + "");
                        ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasics.get(0).getSjhm(), SMSTemp.贷款逾期催收.getCode(), arr);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 入账失败余额不足处理,重扣划
     */
    @Override
    public void debitSend() {
        synchronized (lock) {
            List<StHousingBusinessDetails> stsingBusinessDetails = stHousingBusinessDetailsDAO.list(
                    new HashMap<String, Object>() {{
                        this.put("dkywmxlx", Arrays.asList(LoanBusinessType.正常还款.getCode()));
                        this.put("cHousingBusinessDetailsExtension.ywzt", TaskBusinessStatus.入账失败.getName());
                    }}, null, null, null, null, null, null);
            for (StHousingBusinessDetails BusinessDetails : stsingBusinessDetails) {
                if (BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("余额不足") ||
                        BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("没有足够可用资金")
                        || BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("批次被撤销")) {
                    BusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.待入账.getName());
                    BusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                    BusinessDetails.getGrywmx().setStep(TaskBusinessStatus.入账失败.getName());
                    stHousingBusinessDetailsDAO.update(BusinessDetails);
                }
            }

            //逾期扣划失败重扣划
            List<StHousingBusinessDetails> stsingBusinessDetailsover = stHousingBusinessDetailsDAO.list(
                    new HashMap<String, Object>() {{
                        this.put("dkywmxlx", Arrays.asList(LoanBusinessType.逾期还款.getCode()));
                        this.put("cHousingBusinessDetailsExtension.ywzt", TaskBusinessStatus.入账失败.getName());
                    }}, null, null, null, null, null, null);
            for (StHousingBusinessDetails BusinessDetails : stsingBusinessDetailsover) {
                if (BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("余额不足") ||
                        BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("没有足够可用资金")
                        || BusinessDetails.getcHousingBusinessDetailsExtension().getSbyy().contains("批次被撤销")) {
                    List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                        this.put("yqqc", BusinessDetails.getDqqc());//逾期期次
                        this.put("dkzh", BusinessDetails.getDkzh());//贷款账号
                        this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.入账失败.getName());
                    }}, null, null, null, null, null, null);
                    BusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已作废.getName());
                    BusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                    BusinessDetails.getGrywmx().setStep(TaskBusinessStatus.已作废.getName());
                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.待入账.getName());
                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                    stHousingBusinessDetailsDAO.update(BusinessDetails);
                }
            }


            //还款申请
//            List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcesses = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
//                this.put("cznr", Arrays.asList(LoanBusinessType.结清.getCode(), LoanBusinessType.提前还款.getCode()));
//                this.put("step", LoanBussinessStatus.扣款已发送.getName());
//                this.put("loanApplyRepaymentVice.hklx", Arrays.asList(LoanBusinessType.结清.getCode(), LoanBusinessType.提前还款.getCode()));
//            }}, null, null, null, null, null, null);
//            StHousingBusinessDetails housingBusinessDetails = null;
//            for (CLoanHousingBusinessProcess cLoanHousingBusiness : cLoanHousingBusinessProcesses) {
//                if (new Date().getTime() > cLoanHousingBusiness.getBlsj().getTime() + 172800000) {//2天
//                    StHousingBusinessDetails ngBusinessDetails = stHousingBusinessDetailsDAO.getByYWLSH(cLoanHousingBusiness.getYwlsh());
//
//                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
//                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy("未收到到账通知,请核对扣款信息手动处理");//失败原因
//                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
//                    ngBusinessDetails.setUpdated_at(new Date());
//                    ngBusinessDetails.getGrywmx().setStep(LoanBussinessStatus.入账失败.getName());
//                    stHousingBusinessDetailsDAO.update(ngBusinessDetails);
//
//                    CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(ngBusinessDetails.getYwlsh());
//                    byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
//                    cloanHousingBusinessProcess.update(byYWLSH);
//                }
//            }
        }
    }


    /**
     * 以还期数
     */
    @Override
    public void remainingPeriod() {
        synchronized (lock) {
            icloanHousingPersonInformationBasicDAO.getInfo();
        }
    }


    /**
     * 还款申请定时发送
     */
    @Override
    public void overdueRepaymenTiming() {
        synchronized (lock) {
            while (true) {
                List<CLoanHousingBusinessProcess> list = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                    this.put("cznr", Arrays.asList(LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
                    this.put("step", TaskBusinessStatus.待入账.getName());
                }}, null, null, null, null, null, null);
                if (list.size() == 0) return;

                try {
                    int i = 0;
                    for (CLoanHousingBusinessProcess clhousingBusinessProcess : list) {
                        String ydk = simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq());
                        if (simData.parse(simData.format(new Date())).getTime() == simData.parse(simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq())).getTime()) {
                            i++;
                        }
                    }
                    if (i == 0) return;
                } catch (Exception e) {
                    logger.error(LogUtil.getTrace(e));
                    e.printStackTrace();
                }
                boolean first = true;
                String khyhmc = null;
                String khyhdm = null;
                int totalNumber = 1;
                BigDecimal totalMoney = BigDecimal.ZERO;
                String id = null;
                String ywlsh = null;
                List<StHousingBusinessDetails> gBusinessDetails = new ArrayList<>();
                List<LoanDeductionFileSelf> self = new ArrayList<>();

                try {
                    CLoanHousingBusinessProcess housingBusinessProcess = new CLoanHousingBusinessProcess();
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
                    housingBusinessProcess.setYwwd(network);
                    housingBusinessProcess.setCznr(LoanBusinessType.提前还款.getCode());
                    housingBusinessProcess.setStep(TaskBusinessStatus.扣款已发送.getName());
                    housingBusinessProcess.setBlsj(new Date());

                    for (CLoanHousingBusinessProcess clhousingBusinessProcess : list) {
                        CLoanHousingPersonInformationBasic icloanHousingPersonInformationBasicDAOByDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(clhousingBusinessProcess.getDkzh());
                        StHousingPersonalAccount byDkzh = icloanHousingPersonInformationBasicDAOByDKZH.getPersonalAccount();
                        BigDecimal sjqc = byDkzh.getcLoanHousingPersonalAccountExtension().getDqqc().add(byDkzh.getDkqs().subtract(byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()));
                        String zhkhyhdm = byDkzh.getStHousingPersonalLoan().getZhkhyhdm();
                        if (LoanBusinessType.提前还款.getCode().equals(clhousingBusinessProcess.getCznr())) {
                            if (first) {
                                if (simData.parse(simData.format(new Date())).getTime() == simData.parse(simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq())).getTime()) {
                                    first = false;
                                    khyhmc = byDkzh.getStHousingPersonalLoan().getZhkhyhmc();
                                    khyhdm = byDkzh.getStHousingPersonalLoan().getZhkhyhdm();
                                    CLoanApplyRepaymentVice applyRepaymentVice = clhousingBusinessProcess.getLoanApplyRepaymentVice();
                                    StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                                    chousingBusinessDetails.setYwlsh(clhousingBusinessProcess.getYwlsh());
                                    chousingBusinessDetails.setDkzh(clhousingBusinessProcess.getDkzh());//贷款账号
                                    chousingBusinessDetails.setDkywmxlx(LoanBusinessType.提前还款.getCode());//贷款业务类型
                                    chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                                    chousingBusinessDetails.setFse(applyRepaymentVice.getHkje());//发生额
                                    BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
//                                    BigDecimal bqbx = CommLoanAlgorithm.currentBX(byDkzh.getDkffe(), byDkzh.getDkqs().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, applyRepaymentVice.getHkqc().intValue());
//                                    BigDecimal bqlx = CommLoanAlgorithm.overdueThisPeriodLX(byDkzh.getDkffe(), applyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, byDkzh.getDkqs().intValue());
                                    BigDecimal bqbx = byDkzh.getDqyhje();
                                    BigDecimal bqlx = byDkzh.getDqyhlx();
                                    BigDecimal tqhkje = CommLoanAlgorithm.prepaymentKAmount(applyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录 //
                                    BigDecimal bjje = bqbx.subtract(bqlx);
                                    int jxts = CommLoanAlgorithm.beforeInterestDays(byDkzh.getDkffrq(), applyRepaymentVice.getHkqc().intValue(), applyRepaymentVice.getYdkkrq());//计息天数
                                    BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                    chousingBusinessDetails.setBjje(tqhbje.add(bjje).setScale(2, BigDecimal.ROUND_HALF_UP));//本金金额
                                    chousingBusinessDetails.setLxje(applyRepaymentVice.getHkje().subtract(tqhbje.add(bjje)).setScale(2, BigDecimal.ROUND_HALF_UP));//利息金额
                                    chousingBusinessDetails.setDqqc(sjqc);//当期期次
                                    chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                    CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                                    stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                                    stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                                    stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                                    stHousingBusinessDetailsExtension.setZhkhyhmc(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                                    stHousingBusinessDetailsExtension.setZhkhyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                    stHousingBusinessDetailsExtension.setYwwd(clhousingBusinessProcess.getYwwd().getId());
                                    stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                                    chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                                    chousingBusinessDetails.setGrywmx(housingBusinessProcess);
                                    //原来的
                                    clhousingBusinessProcess.setStep(LoanBussinessStatus.扣款已发送.getName());
                                    cloanHousingBusinessProcess.update(clhousingBusinessProcess);

                                    if ("104".equals(zhkhyhdm)) {
                                        LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                        loanDeductionFileOther.setNo(totalNumber + "");
                                        loanDeductionFileOther.setAmt(applyRepaymentVice.getHkje());
                                        loanDeductionFileOther.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                        loanDeductionFileOther.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                        loanDeductionFileOther.setDeChgNo("104100000004");
                                        loanDeductionFileOther.setFullMark("0");
                                        loanDeductionFileOther.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                        totalNumber += 1;
                                        totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                        self.add(loanDeductionFileOther);
                                    } else {
                                        LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                        loanDeductionFilSelf.setNo(totalNumber + "");
                                        loanDeductionFilSelf.setAmt(applyRepaymentVice.getHkje());
                                        loanDeductionFilSelf.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                        loanDeductionFilSelf.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                        loanDeductionFilSelf.setFullMark("0");
                                        loanDeductionFilSelf.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                        totalNumber += 1;
                                        totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                        self.add(loanDeductionFilSelf);
                                    }
                                    gBusinessDetails.add(chousingBusinessDetails);
                                }
                            } else {
                                if (zhkhyhdm.equals(khyhdm)) {
                                    if (simData.parse(simData.format(new Date())).getTime() == simData.parse(simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq())).getTime()) {
                                        CLoanApplyRepaymentVice applyRepaymentVice = clhousingBusinessProcess.getLoanApplyRepaymentVice();
                                        StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                                        chousingBusinessDetails.setYwlsh(clhousingBusinessProcess.getYwlsh());
                                        chousingBusinessDetails.setDkzh(clhousingBusinessProcess.getDkzh());//贷款账号
                                        chousingBusinessDetails.setDkywmxlx(LoanBusinessType.提前还款.getCode());//贷款业务类型
                                        chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                                        chousingBusinessDetails.setFse(applyRepaymentVice.getHkje());//发生额
                                        BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
//                                        BigDecimal bqbx = CommLoanAlgorithm.currentBX(byDkzh.getDkffe(), byDkzh.getDkqs().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, applyRepaymentVice.getHkqc().intValue());
//                                        BigDecimal bqlx = CommLoanAlgorithm.overdueThisPeriodLX(byDkzh.getDkffe(), applyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, byDkzh.getDkqs().intValue());
                                        BigDecimal bqbx = byDkzh.getDqyhje();
                                        BigDecimal bqlx = byDkzh.getDqyhlx();
                                        BigDecimal tqhkje = CommLoanAlgorithm.prepaymentKAmount(applyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录 //
                                        BigDecimal bjje = bqbx.subtract(bqlx);
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(byDkzh.getDkffrq(), applyRepaymentVice.getHkqc().intValue(), applyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                        chousingBusinessDetails.setBjje(tqhbje.add(bjje).setScale(2, BigDecimal.ROUND_HALF_UP));//本金金额
                                        chousingBusinessDetails.setLxje(applyRepaymentVice.getHkje().subtract(tqhbje.add(bjje)).setScale(2, BigDecimal.ROUND_HALF_UP));//利息金额
                                        chousingBusinessDetails.setDqqc(sjqc);//当期期次
                                        chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                        CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                                        stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                                        stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                                        stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                                        stHousingBusinessDetailsExtension.setZhkhyhmc(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                                        stHousingBusinessDetailsExtension.setZhkhyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                        stHousingBusinessDetailsExtension.setYwwd(clhousingBusinessProcess.getYwwd().getId());
                                        stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                                        chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                                        chousingBusinessDetails.setGrywmx(housingBusinessProcess);
                                        //原来的
                                        clhousingBusinessProcess.setStep(LoanBussinessStatus.扣款已发送.getName());
                                        cloanHousingBusinessProcess.update(clhousingBusinessProcess);

                                        if ("104".equals(zhkhyhdm)) {
                                            LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                            loanDeductionFileOther.setNo(totalNumber + "");
                                            loanDeductionFileOther.setAmt(applyRepaymentVice.getHkje());
                                            loanDeductionFileOther.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                            loanDeductionFileOther.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                            loanDeductionFileOther.setDeChgNo("104100000004");
                                            loanDeductionFileOther.setFullMark("0");
                                            loanDeductionFileOther.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                            totalNumber += 1;
                                            totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                            self.add(loanDeductionFileOther);
                                        } else {
                                            LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                            loanDeductionFilSelf.setNo(totalNumber + "");
                                            loanDeductionFilSelf.setAmt(applyRepaymentVice.getHkje());
                                            loanDeductionFilSelf.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                            loanDeductionFilSelf.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                            loanDeductionFilSelf.setFullMark("0");
                                            loanDeductionFilSelf.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                            totalNumber += 1;
                                            totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                            self.add(loanDeductionFilSelf);
                                        }
                                        gBusinessDetails.add(chousingBusinessDetails);

                                    }
                                }
                            }

                        } else if (LoanBusinessType.结清.getCode().equals(clhousingBusinessProcess.getCznr())) {
                            if (first) {
                                if (simData.parse(simData.format(new Date())).getTime() == simData.parse(simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq())).getTime()) {
                                    first = false;
                                    khyhmc = byDkzh.getStHousingPersonalLoan().getZhkhyhmc();
                                    khyhdm = byDkzh.getStHousingPersonalLoan().getZhkhyhdm();
                                    CLoanApplyRepaymentVice applyRepaymentVice = clhousingBusinessProcess.getLoanApplyRepaymentVice();
                                    StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                                    chousingBusinessDetails.setYwlsh(clhousingBusinessProcess.getYwlsh());
                                    chousingBusinessDetails.setDkzh(clhousingBusinessProcess.getDkzh());//贷款账号
                                    chousingBusinessDetails.setDkywmxlx(LoanBusinessType.结清.getCode());//贷款业务类型
                                    chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                                    chousingBusinessDetails.setFse(applyRepaymentVice.getHkje());//发生额
                                    BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
//                                    BigDecimal bqbx = CommLoanAlgorithm.currentBX(byDkzh.getDkffe(), byDkzh.getDkqs().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, applyRepaymentVice.getHkqc().intValue());
//                                    BigDecimal bqlx = CommLoanAlgorithm.overdueThisPeriodLX(byDkzh.getDkffe(), applyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, byDkzh.getDkqs().intValue());
                                    BigDecimal bqbx = byDkzh.getDqyhje();
                                    BigDecimal bqlx = byDkzh.getDqyhlx();
                                    BigDecimal tqhkje = CommLoanAlgorithm.prepaymentKAmount(applyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录 //
                                    BigDecimal bjje = bqbx.subtract(bqlx);
                                    int jxts = CommLoanAlgorithm.beforeInterestDays(byDkzh.getDkffrq(), applyRepaymentVice.getHkqc().intValue(), applyRepaymentVice.getYdkkrq());//计息天数
                                    BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                    chousingBusinessDetails.setBjje(applyRepaymentVice.getSybj());//本金金额/贷款余额
                                    chousingBusinessDetails.setLxje(applyRepaymentVice.getSylx());//利息金额、减的
                                    chousingBusinessDetails.setDqqc(sjqc);//当期期次
                                    chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                    CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                                    stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                                    stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                                    stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                                    stHousingBusinessDetailsExtension.setZhkhyhmc(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                                    stHousingBusinessDetailsExtension.setZhkhyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                    stHousingBusinessDetailsExtension.setYwwd(clhousingBusinessProcess.getYwwd().getId());
                                    stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                                    chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                                    chousingBusinessDetails.setGrywmx(housingBusinessProcess);
                                    //原来的
                                    clhousingBusinessProcess.setStep(LoanBussinessStatus.扣款已发送.getName());
                                    cloanHousingBusinessProcess.update(clhousingBusinessProcess);

                                    if ("104".equals(zhkhyhdm)) {
                                        LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                        loanDeductionFileOther.setNo(totalNumber + "");
                                        loanDeductionFileOther.setAmt(applyRepaymentVice.getHkje());
                                        loanDeductionFileOther.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                        loanDeductionFileOther.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                        loanDeductionFileOther.setDeChgNo("104100000004");
                                        loanDeductionFileOther.setFullMark("0");
                                        loanDeductionFileOther.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                        totalNumber += 1;
                                        totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                        self.add(loanDeductionFileOther);
                                    } else {
                                        LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                        loanDeductionFilSelf.setNo(totalNumber + "");
                                        loanDeductionFilSelf.setAmt(applyRepaymentVice.getHkje());
                                        loanDeductionFilSelf.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                        loanDeductionFilSelf.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                        loanDeductionFilSelf.setFullMark("0");
                                        loanDeductionFilSelf.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                        totalNumber += 1;
                                        totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                        self.add(loanDeductionFilSelf);
                                    }
                                    gBusinessDetails.add(chousingBusinessDetails);
                                }
                            } else {
                                if (zhkhyhdm.equals(khyhdm)) {
                                    if (simData.parse(simData.format(new Date())).getTime() == simData.parse(simData.format(clhousingBusinessProcess.getLoanApplyRepaymentVice().getYdkkrq())).getTime()) {
                                        CLoanApplyRepaymentVice applyRepaymentVice = clhousingBusinessProcess.getLoanApplyRepaymentVice();
                                        StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                                        chousingBusinessDetails.setYwlsh(clhousingBusinessProcess.getYwlsh());
                                        chousingBusinessDetails.setDkzh(clhousingBusinessProcess.getDkzh());//贷款账号
                                        chousingBusinessDetails.setDkywmxlx(LoanBusinessType.结清.getCode());//贷款业务类型
                                        chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                                        chousingBusinessDetails.setFse(applyRepaymentVice.getHkje());//发生额
                                        BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
//                                        BigDecimal bqbx = CommLoanAlgorithm.currentBX(byDkzh.getDkffe(), byDkzh.getDkqs().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, applyRepaymentVice.getHkqc().intValue());
//                                        BigDecimal bqlx = CommLoanAlgorithm.overdueThisPeriodLX(byDkzh.getDkffe(), applyRepaymentVice.getHkqc().intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, byDkzh.getDkqs().intValue());
                                        BigDecimal bqbx = byDkzh.getDqyhje();
                                        BigDecimal bqlx = byDkzh.getDqyhlx();
                                        BigDecimal tqhkje = CommLoanAlgorithm.prepaymentKAmount(applyRepaymentVice.getHkje(), bqbx);//提前还款金额，没有逾期记录 //
                                        BigDecimal bjje = bqbx.subtract(bqlx);
                                        int jxts = CommLoanAlgorithm.beforeInterestDays(byDkzh.getDkffrq(), applyRepaymentVice.getHkqc().intValue(), applyRepaymentVice.getYdkkrq());//计息天数
                                        BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                                        chousingBusinessDetails.setBjje(applyRepaymentVice.getSybj());//本金金额/贷款余额
                                        chousingBusinessDetails.setLxje(applyRepaymentVice.getSylx());//利息金额、减的
                                        chousingBusinessDetails.setDqqc(sjqc);//当期期次
                                        chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                        CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                                        stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                                        stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                                        stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                                        stHousingBusinessDetailsExtension.setZhkhyhmc(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                                        stHousingBusinessDetailsExtension.setZhkhyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                        stHousingBusinessDetailsExtension.setYwwd(clhousingBusinessProcess.getYwwd().getId());
                                        stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                                        chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                                        chousingBusinessDetails.setGrywmx(housingBusinessProcess);
                                        //原来的
                                        clhousingBusinessProcess.setStep(LoanBussinessStatus.扣款已发送.getName());
                                        cloanHousingBusinessProcess.update(clhousingBusinessProcess);

                                        if ("104".equals(zhkhyhdm)) {
                                            LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                            loanDeductionFileOther.setNo(totalNumber + "");
                                            loanDeductionFileOther.setAmt(applyRepaymentVice.getHkje());
                                            loanDeductionFileOther.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                            loanDeductionFileOther.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                            loanDeductionFileOther.setDeChgNo("104100000004");
                                            loanDeductionFileOther.setFullMark("0");
                                            loanDeductionFileOther.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                            totalNumber += 1;
                                            totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                            self.add(loanDeductionFileOther);
                                        } else {
                                            LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                            loanDeductionFilSelf.setNo(totalNumber + "");
                                            loanDeductionFilSelf.setAmt(applyRepaymentVice.getHkje());
                                            loanDeductionFilSelf.setDeAcctNo(byDkzh.getStHousingPersonalLoan().getHkzh());
                                            loanDeductionFilSelf.setDeAcctName(byDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                            loanDeductionFilSelf.setFullMark("0");
                                            loanDeductionFilSelf.setSummary(byDkzh.getStHousingPersonalLoan().getJkrxm() + "贷款扣划");
                                            totalNumber += 1;
                                            totalMoney = totalMoney.add(applyRepaymentVice.getHkje());
                                            self.add(loanDeductionFilSelf);
                                        }
                                        gBusinessDetails.add(chousingBusinessDetails);
                                    }
                                }
                            }
                        }
                    }

                    if (self.size() > 0) {
                        //新的
                        housingBusinessProcess.setStHousingBusinessDetails(gBusinessDetails);
                        id = cloanHousingBusinessProcess.save(housingBusinessProcess);
                        ywlsh = cloanHousingBusinessProcess.get(id).getYwlsh();
                        LoanDeductionIn loandeductionIn = iclearingBank.sendLoaneMessage(ywlsh, khyhmc, totalMoney, totalNumber);
                        LoanDeductionOut loanDeductionOut = iBank.sendMsg(loandeductionIn, self);
                        if ("1".equals(loanDeductionOut.getCenterHeadOut().getTxStatus())) {//入账失败
                            throw new Exception("入账失败--:" + loanDeductionOut.getCenterHeadOut().getRtnMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error(LogUtil.getTrace(e));
                    e.printStackTrace();
                    try {
                        exceptionMethod.exceptionAbnormal(e, id, ywlsh, khyhmc);
                    } catch (Exception ex) {
                        logger.error(LogUtil.getTrace(ex));
                        throw new ErrorException(ex);
                    }
                }
            }
        }
    }

    /**
     * 自动逾期记录扣划
     */
    @Override
    public void overdueAutomatic(String yhdm) throws Exception {
        synchronized (lock) {
            yhdm = "";
            while (true) {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
                TransactionStatus status = txManager.getTransaction(def); // 获得事务状态

                List<StHousingOverdueRegistration> housingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                    this.put("cHousingOverdueRegistrationExtension.ywzt", TaskBusinessStatus.待入账.getName());
                }}, null, null, null, null, null, null);
                if (housingOverdueRegistrations.size() == 0) return;

                //查看逾期还款业务明细记录
                boolean first = true;
                String khyhzhmc = null;
                String khyhzhdm = null;
                if (StringUtil.notEmpty(yhdm)) {
                    khyhzhdm = yhdm;
                    first = false;
                }
                String id = null;
                String ywlsh = null;
                int totalNumber = 1;
                BigDecimal fse = BigDecimal.ZERO;
                BigDecimal totalMoney = BigDecimal.ZERO;
                StHousingPersonalAccount stHousingPersonalAccountByDkzh = null;
                ArrayList<StHousingBusinessDetails> gBusinessDetails = new ArrayList<>();
                List<LoanDeductionFileSelf> self = new ArrayList<>();
                try {
                    CLoanHousingBusinessProcess housingBusinessProcess = new CLoanHousingBusinessProcess();
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
                    housingBusinessProcess.setYwwd(network);
                    housingBusinessProcess.setCznr(LoanBusinessType.逾期还款.getCode());//还款类型
                    housingBusinessProcess.setStep(TaskBusinessStatus.扣款已发送.getName());//入账状态
                    housingBusinessProcess.setBlsj(new Date());

                    int number = 0;
                    for (StHousingOverdueRegistration overdueRegistrations : housingOverdueRegistrations) {
                        System.out.println("---" + number);
                        if (number == 300) break;
                        if (first) {
                            CLoanHousingPersonInformationBasic icloanHousingPersonInformationBasicDAOByDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(overdueRegistrations.getDkzh());
                            stHousingPersonalAccountByDkzh = icloanHousingPersonInformationBasicDAOByDKZH.getPersonalAccount();
                            khyhzhmc = stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhmc();
                            khyhzhdm = stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm();
                            BigDecimal dkll = CommLoanAlgorithm.lendingRate(stHousingPersonalAccountByDkzh.getDkll(), stHousingPersonalAccountByDkzh.getLlfdbl());
                            BigDecimal yqfx = CommLoanAlgorithm.overdueFX(overdueRegistrations.getYqbj(), overdueRegistrations.getYqlx(),
                                    dkll, stHousingPersonalAccountByDkzh.getDkffrq(), overdueRegistrations.getYqqc().intValue(), sim.parse(sim.format(new Date())));//逾期罚息
                            fse = overdueRegistrations.getYqbj().add(overdueRegistrations.getYqlx().add(yqfx)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            if (overdueRegistrations.getYqbj().compareTo(BigDecimal.ZERO) == 0 && overdueRegistrations.getYqlx().compareTo(BigDecimal.ZERO) == 0) {
                                overdueRegistrations.getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                stHousingOverdueRegistrationDAO.update(overdueRegistrations);
                                continue;
                            } else {
                                overdueRegistrations.getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.扣款已发送.getName());
                            }
                            stHousingOverdueRegistrationDAO.update(overdueRegistrations);

                            StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                            chousingBusinessDetails.setDkyhdm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                            chousingBusinessDetails.setDkzh(stHousingPersonalAccountByDkzh.getDkzh());//贷款账号
                            chousingBusinessDetails.setDkywmxlx(LoanBusinessType.逾期还款.getCode());//贷款业务类型
                            chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                            chousingBusinessDetails.setFse(fse);//发生额
                            chousingBusinessDetails.setBjje(overdueRegistrations.getYqbj());//本金金额
                            chousingBusinessDetails.setLxje(overdueRegistrations.getYqlx());//利息金额
                            chousingBusinessDetails.setDqqc(overdueRegistrations.getYqqc());//当期期次
                            chousingBusinessDetails.setFxje(yqfx);//逾期罚息
                            CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                            stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                            stHousingBusinessDetailsExtension.setXqdkye(stHousingPersonalAccountByDkzh.getDkye());
                            stHousingBusinessDetailsExtension.setHkzh(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                            stHousingBusinessDetailsExtension.setJkrxm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                            stHousingBusinessDetailsExtension.setZhkhyhdm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                            stHousingBusinessDetailsExtension.setZhkhyhmc(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                            stHousingBusinessDetailsExtension.setYwwd(icloanHousingPersonInformationBasicDAOByDKZH.getYwwd());
                            stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                            chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                            if ("104".equals(khyhzhdm)) {
                                LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                loanDeductionFileOther.setNo(totalNumber + "");
                                loanDeductionFileOther.setAmt(fse);
                                loanDeductionFileOther.setDeAcctNo(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());
                                loanDeductionFileOther.setDeAcctName(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                loanDeductionFileOther.setDeChgNo("104100000004");
                                loanDeductionFileOther.setFullMark("0");
                                loanDeductionFileOther.setSummary("公积金贷款扣划");
                                self.add(loanDeductionFileOther);
                            } else {
                                LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                loanDeductionFilSelf.setNo(totalNumber + "");
                                loanDeductionFilSelf.setAmt(fse);
                                loanDeductionFilSelf.setDeAcctNo(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());
                                loanDeductionFilSelf.setDeAcctName(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                loanDeductionFilSelf.setFullMark("0");
                                loanDeductionFilSelf.setSummary("公积金贷款扣划");
                                self.add(loanDeductionFilSelf);
                            }
                            totalNumber += 1;
                            number++;
                            totalMoney = totalMoney.add(fse);
                            gBusinessDetails.add(chousingBusinessDetails);
                            first = false;
                        } else {
                            CLoanHousingPersonInformationBasic icloanHousingPersonInformationBasicDAOByDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(overdueRegistrations.getDkzh());
                            stHousingPersonalAccountByDkzh = icloanHousingPersonInformationBasicDAOByDKZH.getPersonalAccount();
                            if (khyhzhdm.equals(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm())) {
                                khyhzhmc = stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhmc();
                                BigDecimal dkll = CommLoanAlgorithm.lendingRate(stHousingPersonalAccountByDkzh.getDkll(), stHousingPersonalAccountByDkzh.getLlfdbl());
                                BigDecimal yqfx = CommLoanAlgorithm.overdueFX(overdueRegistrations.getYqbj(), overdueRegistrations.getYqlx(),
                                        dkll, stHousingPersonalAccountByDkzh.getDkffrq(), overdueRegistrations.getYqqc().intValue(), sim.parse(sim.format(new Date())));//逾期罚息
                                fse = overdueRegistrations.getYqbj().add(overdueRegistrations.getYqlx().add(yqfx)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                if (overdueRegistrations.getYqbj().compareTo(BigDecimal.ZERO) == 0 && overdueRegistrations.getYqlx().compareTo(BigDecimal.ZERO) == 0) {
                                    overdueRegistrations.getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                    stHousingOverdueRegistrationDAO.update(overdueRegistrations);
                                    continue;
                                } else {
                                    overdueRegistrations.getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.扣款已发送.getName());
                                }
                                stHousingOverdueRegistrationDAO.update(overdueRegistrations);

                                StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                                chousingBusinessDetails.setDkyhdm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                chousingBusinessDetails.setDkzh(stHousingPersonalAccountByDkzh.getDkzh());//贷款账号
                                chousingBusinessDetails.setDkywmxlx(LoanBusinessType.逾期还款.getCode());//贷款业务类型
                                chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                                chousingBusinessDetails.setFse(fse);//发生额
                                chousingBusinessDetails.setBjje(overdueRegistrations.getYqbj());//本金金额
                                chousingBusinessDetails.setLxje(overdueRegistrations.getYqlx());//利息金额
                                chousingBusinessDetails.setDqqc(overdueRegistrations.getYqqc());//当期期次
                                chousingBusinessDetails.setFxje(yqfx);//逾期罚息
                                CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                                stHousingBusinessDetailsExtension.setYwzt(TaskBusinessStatus.扣款已发送.getName());//业务状态
                                stHousingBusinessDetailsExtension.setXqdkye(stHousingPersonalAccountByDkzh.getDkye());
                                stHousingBusinessDetailsExtension.setHkzh(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                                stHousingBusinessDetailsExtension.setJkrxm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());//借款人姓名
                                stHousingBusinessDetailsExtension.setZhkhyhdm(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                                stHousingBusinessDetailsExtension.setZhkhyhmc(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                                stHousingBusinessDetailsExtension.setYwwd(icloanHousingPersonInformationBasicDAOByDKZH.getYwwd());
                                stHousingBusinessDetailsExtension.setHkxh(totalNumber + "");
                                chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                                if ("104".equals(khyhzhdm)) {
                                    LoanDeductionFileOther loanDeductionFileOther = new LoanDeductionFileOther();
                                    loanDeductionFileOther.setNo(totalNumber + "");
                                    loanDeductionFileOther.setAmt(fse);
                                    loanDeductionFileOther.setDeAcctNo(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());
                                    loanDeductionFileOther.setDeAcctName(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                    loanDeductionFileOther.setDeChgNo("104100000004");
                                    loanDeductionFileOther.setFullMark("0");
                                    loanDeductionFileOther.setSummary("公积金贷款扣划");
                                    self.add(loanDeductionFileOther);
                                } else {
                                    LoanDeductionFileSelf loanDeductionFilSelf = new LoanDeductionFileSelf();
                                    loanDeductionFilSelf.setNo(totalNumber + "");
                                    loanDeductionFilSelf.setAmt(fse);
                                    loanDeductionFilSelf.setDeAcctNo(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getHkzh());
                                    loanDeductionFilSelf.setDeAcctName(stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                                    loanDeductionFilSelf.setFullMark("0");
                                    loanDeductionFilSelf.setSummary("公积金贷款扣划");
                                    self.add(loanDeductionFilSelf);
                                }
                                totalNumber += 1;
                                number++;
                                totalMoney = totalMoney.add(fse);
                                gBusinessDetails.add(chousingBusinessDetails);
                            }
                        }
                    }

                    if (self.size() > 0) {
                        id = cloanHousingBusinessProcess.save(housingBusinessProcess);
                        CLoanHousingBusinessProcess businessProcess = cloanHousingBusinessProcess.get(id);
                        ywlsh = businessProcess.getYwlsh();
                        stHousingBusinessDetailsDAO.updateBatch(gBusinessDetails, ywlsh, id);
                        List<LoanDeductionFileSelf> deleteself = new LinkedList<>();

                        List<StHousingBusinessDetails> housingBusinessDetails = cloanHousingBusinessProcess.get(id).getStHousingBusinessDetails();
                        CLoanHousingPersonInformationBasic byDkzh = null;
                        for (StHousingBusinessDetails sngBusinessDetails : housingBusinessDetails) {
                            try {
                                byDkzh = icloanHousingPersonInformationBasicDAO.getByDKZH(sngBusinessDetails.getDkzh());
                                String str = byDkzh.getJkrgjjzh();

                                StCommonPerson stCommonPersonByGrzh = stCommonPerson.getByGrzh(str);
                                BigDecimal sub = BigDecimal.ZERO;
                                StCollectionPersonalAccount jkrcollectionpersonalAccounts = null;

                                if (byDkzh.getFunds() != null && byDkzh.getFunds().getWtkhyjce() == true) {
                                    if (stCommonPersonByGrzh != null) {
                                        jkrcollectionpersonalAccounts = stCommonPersonByGrzh.getCollectionPersonalAccount();
                                        if (jkrcollectionpersonalAccounts != null) {
                                            if (jkrcollectionpersonalAccounts.getGrzhye() != null) {
                                                sub = jkrcollectionpersonalAccounts.getGrzhye().subtract(sngBusinessDetails.getFse().add(new BigDecimal(0.01)));//公积金余额-还款额
                                            }
                                        }
                                    }
                                    StCollectionPersonalAccount gtjkrcollectionpersonalAccounts = null;
                                    BigDecimal gtsub = BigDecimal.ZERO;
                                    if (sub.compareTo(BigDecimal.ZERO) == -1) {
                                        if (byDkzh.getHyzk().equals("20") && byDkzh.getCoborrower() != null) {
                                            String gtjkrgjjzh = byDkzh.getCoborrower().getGtjkrgjjzh();
                                            StCommonPerson byGrzh = stCommonPerson.getByGrzh(gtjkrgjjzh);
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

                                    if (sub.compareTo(BigDecimal.ZERO) == 1) {
                                        //提取业务
                                        ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getPersonalAccount().getStHousingPersonalLoan().getZhkhyhmc());
                                        LoanWithdrawl loanWithdrawl = new LoanWithdrawl();
                                        loanWithdrawl.setJkrgrzh(jkrcollectionpersonalAccounts.getGrzh());
                                        loanWithdrawl.setJkrfse(sngBusinessDetails.getFse());
                                        for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                                            if (cterAccountInfo.getZHXZ().equals("01")) {
                                                loanWithdrawl.setYhzhhm(cterAccountInfo.getYHZHHM());
                                            }
                                        }
                                        loanWithdrawl.setYwwd(stCommonPersonByGrzh.getUnit().getExtension().getKhwd());
                                        loanWithdrawl.setLixi(sngBusinessDetails.getFse().subtract(sngBusinessDetails.getBjje()));
                                        loanWithdrawl.setBenjin(sngBusinessDetails.getBjje());
                                        loanWithdrawl.setYhdm(byDkzh.getPersonalAccount().getStHousingPersonalLoan().getZhkhyhdm());
                                        addWithdrawlrepament(loanWithdrawl);

                                    } else if (gtsub.compareTo(BigDecimal.ZERO) == 1) {
                                        ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getPersonalAccount().getStHousingPersonalLoan().getZhkhyhmc());
                                        LoanWithdrawl loanWithdrawl = new LoanWithdrawl();
                                        loanWithdrawl.setJkrgrzh(jkrcollectionpersonalAccounts.getGrzh());
                                        loanWithdrawl.setGtjkrgrzh(gtjkrcollectionpersonalAccounts.getGrzh());
                                        if (jkrcollectionpersonalAccounts.getGrzhye().compareTo(new BigDecimal(0.01)) == 1) {
                                            loanWithdrawl.setJkrfse(jkrcollectionpersonalAccounts.getGrzhye().subtract(new BigDecimal(0.01)));
                                        } else {
                                            loanWithdrawl.setJkrfse(BigDecimal.ZERO);
                                        }
                                        loanWithdrawl.setGtjkrfse(sngBusinessDetails.getFse().subtract(loanWithdrawl.getJkrfse()));
                                        for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                                            if (cterAccountInfo.getZHXZ().equals("01")) {
                                                loanWithdrawl.setYhzhhm(cterAccountInfo.getYHZHHM());
                                            }
                                        }
                                        loanWithdrawl.setYwwd(stCommonPersonByGrzh.getUnit().getExtension().getKhwd());
                                        loanWithdrawl.setLixi(sngBusinessDetails.getFse().subtract(sngBusinessDetails.getBjje()));
                                        loanWithdrawl.setBenjin(sngBusinessDetails.getBjje());
                                        loanWithdrawl.setYhdm(byDkzh.getPersonalAccount().getStHousingPersonalLoan().getZhkhyhdm());
                                        addWithdrawlrepament(loanWithdrawl);
                                    }

                                    if (sub.compareTo(BigDecimal.ZERO) == 1 || gtsub.compareTo(BigDecimal.ZERO) == 1) {
                                        for (LoanDeductionFileSelf buness : self) {
                                            if (buness.getNo().equals(sngBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh())) {
                                                totalMoney = totalMoney.subtract(sngBusinessDetails.getFse());
                                                deleteself.add(buness);
                                            }
                                        }

                                        int currentQS = CommLoanAlgorithm.currentQS(stHousingPersonalAccountByDkzh.getDkffrq(), new Date());
                                        sngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                        sngBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                                        sngBusinessDetails.setJzrq(new Date());
                                        stHousingBusinessDetailsDAO.update(sngBusinessDetails);

                                        BigDecimal yqbjze = sngBusinessDetails.getBjje();
                                        BigDecimal yqfxze = sngBusinessDetails.getFxje();
                                        BigDecimal yqlxze = sngBusinessDetails.getLxje();
                                        List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                                            this.put("dkzh", sngBusinessDetails.getDkzh());
                                            this.put("yqqc", sngBusinessDetails.getDqqc());
                                            this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                                        }}, null, null, null, null, null, null);
                                        stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                                        stHousingOverdueRegistrations.get(0).setHkqc(new BigDecimal(currentQS));
                                        stHousingOverdueRegistrations.get(0).setSsyqfxje(sngBusinessDetails.getFxje());
                                        stHousingOverdueRegistrations.get(0).setSsyqbjje(sngBusinessDetails.getBjje());
                                        stHousingOverdueRegistrations.get(0).setSsyqlxje(sngBusinessDetails.getLxje());
                                        stHousingOverdueRegistrations.get(0).setSsrq(new Date());
                                        stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));

                                        stHousingPersonalAccountByDkzh.setYqlxze(stHousingPersonalAccountByDkzh.getYqlxze().add(yqlxze));
                                        stHousingPersonalAccountByDkzh.setYqbjze(stHousingPersonalAccountByDkzh.getYqbjze().add(yqbjze));
                                        stHousingPersonalAccountByDkzh.setHsbjze(stHousingPersonalAccountByDkzh.getYqlxze().add(yqlxze));
                                        stHousingPersonalAccountByDkzh.setHslxze(stHousingPersonalAccountByDkzh.getYqbjze().add(yqbjze));
                                        stHousingPersonalAccountByDkzh.setDkye(stHousingPersonalAccountByDkzh.getDkye().subtract(sngBusinessDetails.getBjje()));
                                        stHousingPersonalAccountByDkzh.setLjyqqs(stHousingPersonalAccountByDkzh.getLjyqqs().add(BigDecimal.valueOf(1)));
                                        stHousingPersonalAccountByDkzh.setFxze(stHousingPersonalAccountByDkzh.getFxze().add(yqfxze));
                                        icloanHousingPersonInformationBasicDAO.update(byDkzh);
                                        totalNumber--;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                logger.error(LogUtil.getTrace(ex));
                                try {
                                    if (ex.getMessage().contains("入账失败")) continue;
                                    for (LoanDeductionFileSelf buness : self) {
                                        if (buness.getNo().equals(sngBusinessDetails.getcHousingBusinessDetailsExtension().getHkxh())) {
                                            totalMoney = totalMoney.subtract(sngBusinessDetails.getFse());
                                            deleteself.add(buness);
                                        }
                                    }
                                    //接收任何异常
                                    exceptionMethod.exceptionOverdueAutomaticSingle(ex, sngBusinessDetails.getId());
                                } catch (Exception e) {
                                    logger.error(LogUtil.getTrace(e));
                                    throw new ErrorException(e);
                                }
                            }
                        }

                        Iterator<LoanDeductionFileSelf> iterator = self.iterator();
                        while (iterator.hasNext()) {
                            LoanDeductionFileSelf next = iterator.next();
                            Iterator<LoanDeductionFileSelf> iterator1 = deleteself.iterator();
                            while (iterator1.hasNext()) {
                                LoanDeductionFileSelf next1 = iterator1.next();
                                if (next.equals(next1)) {
                                    iterator.remove();
                                }
                            }
                        }

                        if (self.size() > 0) {
                            try {
                                txManager.commit(status);
                            } catch (Exception ee) {
                                logger.error(LogUtil.getTrace(ee));
                                txManager.rollback(status);
                                throw new ErrorException(ee);
                            }
                            LoanDeductionIn loandeductionIn = iclearingBank.sendLoaneMessage(ywlsh, khyhzhmc, totalMoney, totalNumber);
                            LoanDeductionOut loanDeductionOut = iBank.sendMsg(loandeductionIn, self);
                            if ("1".equals(loanDeductionOut.getCenterHeadOut().getTxStatus())) {//入账失败
                                throw new Exception("入账失败--:" + loanDeductionOut.getCenterHeadOut().getRtnMessage());
                            }
                        } else {
                            try {
                                businessProcess.setStep(LoanBussinessStatus.已入账.getName());
                                businessProcess.setBjsj(new Date());
                                cloanHousingBusinessProcess.update(businessProcess);
                                txManager.commit(status);
                            } catch (Exception e) {
                                logger.error(LogUtil.getTrace(e));
                                txManager.rollback(status);
                            }
                        }
                    }
                } catch (Exception ex) {
                    TransactionStatus status2 = txManager.getTransaction(def); // 获得事务状态
                    logger.error(LogUtil.getTrace(ex));
                    ex.printStackTrace();
                    try {
                        exceptionMethod.exceptionOverdueAutomatic(ex, id, ywlsh, khyhzhmc);
                        try {
                            //绝对不会错
                            txManager.commit(status2);
                        } catch (Exception e2) {
                            txManager.rollback(status2);
                        }
                    } catch (Exception e) {
                        logger.error(LogUtil.getTrace(e));
                        throw new ErrorException(e);
                    }
                }
            }
        }
    }


    @Override
    public String providentFundWithdrawal(String dkzh, BigDecimal bchkje, String hklx, String czy, String
            shy, String ywlsh, String pch) {
        synchronized (lock) {
            List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", dkzh);
            }}, null, null, null, Order.DESC, null, null);
            if (cLoanHousingPersonInformationBasics.size() == 0) throw new ErrorException("贷款账号不存在");
//            if (!LoanAccountType.正常.getCode().equals(cLoanHousingPersonInformationBasics.get(0).getDkzhzt()) && !LoanAccountType.逾期.getCode().equals(cLoanHousingPersonInformationBasics.get(0).getDkzhzt())) {
//                throw new ErrorException("账户状态：" + LoanAccountType.所有.getcase(cLoanHousingPersonInformationBasics.get(0).getDkzhzt()));
//            }
            StHousingPersonalAccount byDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
            List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", byDkzh.getDkzh());
            }}, null, null, null, null, null, null);
            for (StHousingOverdueRegistration overdue : stHousingOverdueRegistrations) {
                if (!overdue.getcHousingOverdueRegistrationExtension().getYwzt().equals(LoanBussinessStatus.已入账.getName())) {
                    throw new ErrorException("您还有逾期款项没有还，请等待系统自动逾期扣划再进入此操作");
                }
            }
            BigDecimal sjqc = byDkzh.getcLoanHousingPersonalAccountExtension().getDqqc().add(byDkzh.getDkqs().subtract(byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()));

            if (LoanBusinessType.提前还款.getCode().equals(hklx)) {
                try {
                    BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
                    BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
                    Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
                    BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
                    String dkhkfs = byDkzh.getStHousingPersonalLoan().getDkhkfs();
                    if (bchkje.compareTo(byDkzh.getDkye()) == 1)
                        throw new ErrorException("本次还款金额大于贷款余额，请选择结清贷款（贷款余额：" + byDkzh.getDkye() + "）");
                    if (byDkzh.getcLoanHousingPersonalAccountExtension().getDqqc().compareTo(dkqs) == 0)
                        throw new ErrorException("当期为最后一期，只支持结清还款，不支持部分提取");
                    Calendar calendar = Calendar.getInstance();
                    if (StringUtil.notEmpty(byDkzh.getcLoanHousingPersonalAccountExtension().getTqhksj())) {
                        calendar.setTime(simMs.parse(byDkzh.getcLoanHousingPersonalAccountExtension().getTqhksj()));
                        calendar.add(Calendar.MONTH, 1);
                        long end = calendar.getTimeInMillis();
                        if (new Date().getTime() <= end)
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "一月只能提取还款一次,下次提取还款月份为：" + sim.format(end) + " 之后");
                    }

                    int dqqc = CommLoanAlgorithm.currentQS(dkffrq, new Date());//当期期次
//                    CommLoanAlgorithm.currentRepamentTime(dkffrq, new Date(), dqqc);
                    CurrentPeriodRange currentPeriodRange = CommLoanAlgorithm.theTimePeriod(DateUtil.dateStringtoStringDate(dkffrq)
                            , Integer.parseInt(dkqs.toString()), new Date()); //期数，这期时间，下期时间
                    BigDecimal bjje = null;
                    BigDecimal bqbx = null;
                    BigDecimal bqlx = null;
                    BigDecimal tqhkje = null;
                    BigDecimal syqc = BigDecimal.ZERO;
                    if (sim.parse(sim.format(new Date())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                        bqbx = BigDecimal.ZERO;
                        bqlx = BigDecimal.ZERO;
                        bjje = bqbx.subtract(bqlx);
                        tqhkje = CommLoanAlgorithm.prepaymentKAmount(bchkje, bqbx);//提前还款金额，没有逾期记录
                        syqc = dkqs;
                    } else {
//                        bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs.intValue(), byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dqqc);
//                        bqlx = CommLoanAlgorithm.overdueThisPeriodLX(dkffe, dqqc, byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, dkqs.intValue());
//                        bjje = bqbx.subtract(bqlx)
                        bqbx = byDkzh.getDqyhje();
                        bqlx = byDkzh.getDqyhlx();
                        bjje = byDkzh.getDqyhbj();
                        tqhkje = CommLoanAlgorithm.prepaymentKAmount(bchkje, bqbx);//提前还款金额，没有逾期记录
                        syqc = dkqs.subtract(new BigDecimal(dqqc));
                    }
                    int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, dqqc, new Date());//计息天数
                    BigDecimal tqhbje = CommLoanAlgorithm.prepaymentBAmount(tqhkje, dkll, jxts);//提前还本金额
                    BigDecimal tqhklx = tqhkje.subtract(tqhbje);
                    BigDecimal sybj = byDkzh.getDkye().subtract(tqhbje).subtract(bjje);

                    //新的还款计划，按照新的来计算
                    BigDecimal newbqbx = CommLoanAlgorithm.currentBX(sybj, syqc.intValue(), dkhkfs, dkll, 1).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));//本期本息//
                    BigDecimal newbqlx = CommLoanAlgorithm.overdueThisPeriodLX(sybj, 1, byDkzh.getStHousingPersonalLoan().getDkhkfs(), dkll, syqc.intValue()).add(sybj.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-jxts).divide(new BigDecimal(30), 6, BigDecimal.ROUND_HALF_UP)));
                    BigDecimal newbjje = newbqbx.subtract(newbqlx);

                    //更新的重要的参数
                    byDkzh.setDkye(sybj);
                    byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(sybj);

                    //更新的新的周期的还款计划
                    byDkzh.setDqjhhkje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setDqjhghbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setDqjhghlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setDqyhje(newbqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setDqyhbj(newbjje.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setDqyhlx(newbqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setHslxze(byDkzh.getHslxze().add(bqlx.add(tqhklx)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setHsbjze(byDkzh.getDkffe().subtract(sybj).setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                    CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension = byDkzh.getcLoanHousingPersonalAccountExtension();
                    cLoanHousingPersonalAccountExtension.setDqqc(new BigDecimal(1));
                    cLoanHousingPersonalAccountExtension.setDkgbjhye(sybj.setScale(2, BigDecimal.ROUND_HALF_UP));
                    cLoanHousingPersonalAccountExtension.setDkgbjhqs(syqc.setScale(2, BigDecimal.ROUND_HALF_UP));
                    cLoanHousingPersonalAccountExtension.setTqhksj(simMs.format(new Date()));
                    if (currentPeriodRange.getCurrentPeriod() == 0) {
//                        cLoanHousingPersonalAccountExtension.setDkxffrq(byDkzh.getDkffrq());
                    } else {
                        cLoanHousingPersonalAccountExtension.setDkxffrq(currentPeriodRange.getAfterTime());
                    }
                    icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

                    //业务记录表
                    CLoanHousingBusinessProcess hsingBusinessProcess = new CLoanHousingBusinessProcess();
                    CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("id", cLoanHousingPersonInformationBasics.get(0).getYwwd());
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    hsingBusinessProcess.setYwwd(network);
                    hsingBusinessProcess.setStep(LoanBussinessStatus.已入账.getName());
                    hsingBusinessProcess.setCznr(LoanBusinessType.提前还款.getCode());
                    hsingBusinessProcess.setBjsj(new Date());
                    StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                    chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                    chousingBusinessDetails.setDkzh(dkzh);//贷款账号
                    chousingBusinessDetails.setDkywmxlx(LoanBusinessType.提前还款.getCode());//贷款业务类型
                    chousingBusinessDetails.setYwfsrq(new Date());//业务发生日期
                    chousingBusinessDetails.setFse(bchkje.setScale(2, BigDecimal.ROUND_HALF_UP));//发生额
                    chousingBusinessDetails.setBjje(tqhbje.add(bjje).setScale(2, BigDecimal.ROUND_HALF_UP));//本金金额
                    chousingBusinessDetails.setJzrq(new Date());
                    chousingBusinessDetails.setLxje(bchkje.subtract(tqhbje.add(bjje)).setScale(2, BigDecimal.ROUND_HALF_UP));//利息金额
                    chousingBusinessDetails.setDqqc(sjqc);//当期期次
                    CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                    stHousingBusinessDetailsExtension.setYwzt(LoanBussinessStatus.已入账.getName());//业务状态
                    stHousingBusinessDetailsExtension.setXqdkye(sybj);
                    stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                    stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getJkrxm());//借款人姓名
                    stHousingBusinessDetailsExtension.setYwwd(cLoanHousingPersonInformationBasics.get(0).getYwwd());
                    stHousingBusinessDetailsExtension.setPch(pch);
                    chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                    chousingBusinessDetails.setGrywmx(hsingBusinessProcess);
                    String id = stHousingBusinessDetailsDAO.save(chousingBusinessDetails);
                    StHousingBusinessDetails stHousingBusinessDetails = stHousingBusinessDetailsDAO.get(id);
                    stHousingBusinessDetails.setYwlsh(stHousingBusinessDetails.getGrywmx().getYwlsh());


                    try {
                        ismsCommon.sendSingleSMSWithTemp(cLoanHousingPersonInformationBasics.get(0).getSjhm(), SMSTemp.提前还款扣款后.getCode(), new ArrayList<String>() {{
                            this.add(cLoanHousingPersonInformationBasics.get(0).getJkrxm());
                            this.add(bchkje.setScale(2, BigDecimal.ROUND_HALF_UP) + "");
                            this.add(sybj + "");
                        }});
                    } catch (Exception e) {
                    }

                    //region 生成记账凭证
                    int djsl = 1;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,部分还贷");
                    voucherAmount.setJinE(chousingBusinessDetails.getFse());
                    JFSJ.add(voucherAmount);

                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(chousingBusinessDetails.getBjje());
                    voucherAmounts.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,部分还贷");
                    DFSJ.add(voucherAmounts);

                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(chousingBusinessDetails.getFse().subtract(chousingBusinessDetails.getBjje()));
                    oucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,部分还贷");
                    DFSJ.add(oucherAmount);

                    ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                    String yhzhhm = null;
                    for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                        if (cterAccountInfo.getZHXZ().equals("01")) {
                            yhzhhm = cterAccountInfo.getYHZHHM();
                        }
                    }
                    // TODO: 2017/12/6 新增入账失败原因
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", czy,
                            czy, "", "管理员",
                            VoucherBusinessType.月缴存额还款.getCode(), VoucherBusinessType.月缴存额还款.getCode(),
                            ywlsh, JFSJ, DFSJ, String.valueOf(djsl), yhzhhm, byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                    if (StringUtil.isEmpty(voucherRes.getJZPZH()))
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, voucherRes.getMSG());
                    stHousingBusinessDetails.getGrywmx().setJzpzh(voucherRes.getJZPZH());
                    stHousingBusinessDetailsDAO.update(stHousingBusinessDetails);
                    return voucherRes.getJZPZH();
                } catch (Exception e) {
                    logger.error(LogUtil.getTrace(e));
                    throw new ErrorException(e);
                }

            } else if (LoanBusinessType.结清.getCode().equals(hklx)) {
                try {
                    BigDecimal dkffe = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();
                    BigDecimal dkqs = byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs();
                    Date dkffrq = byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq();
                    BigDecimal dkll = CommLoanAlgorithm.lendingRate(byDkzh.getDkll(), byDkzh.getLlfdbl());
                    int dqqc = CommLoanAlgorithm.currentQS(dkffrq, new Date());//当期期次
//                    CommLoanAlgorithm.currentRepamentTime(dkffrq, new Date(), dqqc);
                    BigDecimal bjje = null;
                    BigDecimal bqbx = null;
                    BigDecimal bqlx = null;
                    if (sim.parse(sim.format(new Date())).getTime() == sim.parse(sim.format(dkffrq)).getTime()) {
                        bqbx = BigDecimal.ZERO;
                        bqlx = BigDecimal.ZERO;
                        bjje = bqbx.subtract(bqlx);
                    } else {
                        bqlx = byDkzh.getDqyhlx();
                        bqbx = byDkzh.getDqyhje();
                        bjje = byDkzh.getDqyhbj();
                    }
                    int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, dqqc, new Date());//计息天数
                    SettleAccounts settleAccounts = CommLoanAlgorithm.settleAccounts(byDkzh.getDkye(), bjje, dkll, bqbx, jxts);
                    BigDecimal tqhbje = settleAccounts.getTQHBJE();
                    BigDecimal tqhklx = settleAccounts.getTQHKLX();//提前还本金额
                    BigDecimal tqjqdkze = settleAccounts.getTQJQHKZE();

//                    if (bchkje.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(tqjqdkze.setScale(2, BigDecimal.ROUND_HALF_UP)) != 0)
//                        throw new ErrorException("还款金额小于结清款总计，请核对金额（结清款总计：" + tqjqdkze.setScale(2, BigDecimal.ROUND_HALF_UP) + "）");

                    //业务记录表
                    CLoanHousingBusinessProcess hsingBusinessProcess = new CLoanHousingBusinessProcess();
                    CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("id", cLoanHousingPersonInformationBasics.get(0).getYwwd());
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    hsingBusinessProcess.setYwwd(network);
                    hsingBusinessProcess.setStep("已入账");
                    hsingBusinessProcess.setCznr(LoanBusinessType.结清.getCode());
                    hsingBusinessProcess.setBjsj(new Date());
                    StHousingBusinessDetails chousingBusinessDetails = new StHousingBusinessDetails();
                    chousingBusinessDetails.setDkyhdm(byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                    chousingBusinessDetails.setDkzh(byDkzh.getDkzh());//贷款账号
                    chousingBusinessDetails.setDkywmxlx(LoanBusinessType.结清.getCode());//贷款业务类型
                    chousingBusinessDetails.setYwfsrq(hsingBusinessProcess.getBjsj());//业务发生日期
                    chousingBusinessDetails.setFse(bchkje.setScale(2, BigDecimal.ROUND_HALF_UP));//发生额
                    chousingBusinessDetails.setBjje(byDkzh.getDkye().setScale(2, BigDecimal.ROUND_HALF_UP));//本金金额
                    chousingBusinessDetails.setJzrq(new Date());
                    chousingBusinessDetails.setLxje(bchkje.subtract(byDkzh.getDkye()).setScale(2, BigDecimal.ROUND_HALF_UP));//利息金额
                    chousingBusinessDetails.setDqqc(sjqc);//当期期次
                    CHousingBusinessDetailsExtension stHousingBusinessDetailsExtension = new CHousingBusinessDetailsExtension();
                    stHousingBusinessDetailsExtension.setYwzt(LoanBussinessStatus.已入账.getName());//业务状态
                    stHousingBusinessDetailsExtension.setXqdkye(BigDecimal.ZERO);
                    stHousingBusinessDetailsExtension.setHkzh(byDkzh.getStHousingPersonalLoan().getHkzh());//还款账号
                    stHousingBusinessDetailsExtension.setJkrxm(byDkzh.getStHousingPersonalLoan().getJkrxm());//借款人姓名
                    stHousingBusinessDetailsExtension.setYwwd(cLoanHousingPersonInformationBasics.get(0).getYwwd());
                    stHousingBusinessDetailsExtension.setPch(pch);
                    chousingBusinessDetails.setcHousingBusinessDetailsExtension(stHousingBusinessDetailsExtension);
                    chousingBusinessDetails.setGrywmx(hsingBusinessProcess);
                    String id = stHousingBusinessDetailsDAO.save(chousingBusinessDetails);
                    StHousingBusinessDetails stHousingBusinessDetails = stHousingBusinessDetailsDAO.get(id);
                    stHousingBusinessDetails.setYwlsh(stHousingBusinessDetails.getGrywmx().getYwlsh());


                    byDkzh.setDkye(BigDecimal.ZERO);
                    byDkzh.getcLoanHousingPersonalAccountExtension().setDkyezcbj(BigDecimal.ZERO);
                    byDkzh.setDqjhhkje(BigDecimal.ZERO);
                    byDkzh.setDqjhghbj(BigDecimal.ZERO);
                    byDkzh.setDqjhghlx(BigDecimal.ZERO);
                    byDkzh.setDqyhje(BigDecimal.ZERO);
                    byDkzh.setDqyhlx(BigDecimal.ZERO);
                    byDkzh.setDqyhbj(BigDecimal.ZERO);
                    byDkzh.setDkjqrq(new Date());
                    byDkzh.setHslxze(byDkzh.getHslxze().add(tqhklx.add(bqlx)));
                    byDkzh.setHsbjze(byDkzh.getDkffe().setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.setTqghbjze(byDkzh.getTqghbjze().add(tqhbje).setScale(2, BigDecimal.ROUND_HALF_UP));
                    byDkzh.getcLoanHousingPersonalAccountExtension().setDqqc(new BigDecimal(1));
                    byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhqs(new BigDecimal(0));
                    byDkzh.getcLoanHousingPersonalAccountExtension().setDkgbjhye(new BigDecimal(0));
                    cLoanHousingPersonInformationBasics.get(0).setYhqs(byDkzh.getDkqs());
                    cLoanHousingPersonInformationBasics.get(0).setDkzhzt(LoanAccountType.已结清.getCode());
                    icloanHousingPersonInformationBasicDAO.update(cLoanHousingPersonInformationBasics.get(0));

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

                    //region 生成记账凭证
                    int djsl = 1;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                    VoucherAmount voucherAmount = new VoucherAmount();
                    voucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,结清还贷");
                    voucherAmount.setJinE(chousingBusinessDetails.getFse());
                    JFSJ.add(voucherAmount);

                    VoucherAmount voucherAmounts = new VoucherAmount();
                    voucherAmounts.setJinE(chousingBusinessDetails.getBjje());
                    voucherAmounts.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,结清还贷");
                    DFSJ.add(voucherAmounts);

                    VoucherAmount oucherAmount = new VoucherAmount();
                    oucherAmount.setJinE(chousingBusinessDetails.getFse().subtract(chousingBusinessDetails.getBjje()));
                    oucherAmount.setZhaiYao(byDkzh.getStHousingPersonalLoan().getJkrxm() + "公积金部分提取,结清还贷");
                    DFSJ.add(oucherAmount);


                    ArrayList<CenterAccountInfo> iSettlementSpecialBankAccountManageServiceSpecialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(byDkzh.getStHousingPersonalLoan().getZhkhyhmc());
                    String yhzhhm = null;
                    for (CenterAccountInfo cterAccountInfo : iSettlementSpecialBankAccountManageServiceSpecialAccount) {
                        if (cterAccountInfo.getZHXZ().equals("01")) {
                            yhzhhm = cterAccountInfo.getYHZHHM();
                        }
                    }
                    // TODO: 2017/12/6  新增入账失败原因
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", czy,
                            shy, "", "管理员",
                            VoucherBusinessType.月缴存额还款.getCode(), VoucherBusinessType.月缴存额还款.getCode(),
                            ywlsh, JFSJ, DFSJ, String.valueOf(djsl), yhzhhm, byDkzh.getStHousingPersonalLoan().getZhkhyhdm());
                    if (StringUtil.isEmpty(voucherRes.getJZPZH()))
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, voucherRes.getMSG());
                    stHousingBusinessDetails.getGrywmx().setJzpzh(voucherRes.getJZPZH());
                    stHousingBusinessDetailsDAO.update(stHousingBusinessDetails);
                    return voucherRes.getJZPZH();
                } catch (Exception e) {
                    logger.error(LogUtil.getTrace(e));
                    throw new ErrorException(e);
                }
            }
            return null;
        }
    }

    /**
     * 贷款调用
     *
     * @param loanWithdrawl
     * @return
     */
    @Override
    public CommonReturn addWithdrawlrepament(LoanWithdrawl loanWithdrawl) {
        CommonReturn commonReturn = new CommonReturn();
        try {
            List<WithdrawlLoan> withdrawlLoanList = new ArrayList<>();
            BigDecimal lv = loanWithdrawl.getBenjin().divide(loanWithdrawl.getBenjin().add(loanWithdrawl.getLixi()), 10, BigDecimal.ROUND_HALF_UP);

            if (!StringUtil.isEmpty(loanWithdrawl.getGtjkrgrzh())) {
                WithdrawlLoan withdrawlLoan1 = new WithdrawlLoan();
                withdrawlLoan1.setGrzh(loanWithdrawl.getJkrgrzh());
                if (loanWithdrawl.getJkrfse().compareTo(BigDecimal.ZERO) == 1) {
                    withdrawlLoan1.setFse(loanWithdrawl.getJkrfse().multiply(lv));
                } else {
                    withdrawlLoan1.setFse(BigDecimal.ZERO);
                }
                withdrawlLoan1.setFslxe(loanWithdrawl.getJkrfse().subtract(withdrawlLoan1.getFse()));

                WithdrawlLoan withdrawlLoan2 = new WithdrawlLoan();
                withdrawlLoan2.setGrzh(loanWithdrawl.getGtjkrgrzh());
                withdrawlLoan2.setFse(loanWithdrawl.getBenjin().subtract(withdrawlLoan1.getFse()));
                withdrawlLoan2.setFslxe(loanWithdrawl.getGtjkrfse().subtract(withdrawlLoan2.getFse()));
                withdrawlLoanList.add(withdrawlLoan1);
                withdrawlLoanList.add(withdrawlLoan2);
            } else {
                WithdrawlLoan withdrawlLoan1 = new WithdrawlLoan();
                withdrawlLoan1.setGrzh(loanWithdrawl.getJkrgrzh());
                withdrawlLoan1.setFse(loanWithdrawl.getBenjin());
                withdrawlLoan1.setFslxe(loanWithdrawl.getLixi());
                withdrawlLoanList.add(withdrawlLoan1);
            }

            for (WithdrawlLoan withdrawlLoan : withdrawlLoanList) {
                if (withdrawlLoan.getFse().compareTo(BigDecimal.ZERO) == 0) continue;
                //region 计算累计提取金额
                List<StCollectionPersonalBusinessDetails> collectionDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("grzh", withdrawlLoan.getGrzh());
                        this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
                    }
                }).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException("入账失败--:" + e.getMessage());
                    }
                });
                BigDecimal LJTQJE = new BigDecimal(0);
                if (collectionDetails.size() > 0) {
                    for (StCollectionPersonalBusinessDetails collectionDetail : collectionDetails) {
                        if (collectionDetail.getExtension() != null && (CollectionBusinessStatus.已入账.getName()).equals(collectionDetail.getExtension().getStep())) {
                            if (collectionDetail.getFse() == null) {
                                collectionDetail.setFse(new BigDecimal(0));
                            }
                            LJTQJE = LJTQJE.add(collectionDetail.getFse().abs());
                        }
                    }
                }
                //endregion

                StCommonPerson person = commonPersonDAO.getByGrzh(withdrawlLoan.getGrzh());
                BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();
                StCollectionPersonalBusinessDetails details = new StCollectionPersonalBusinessDetails();
                //保存后的对象，用于生成业务流水号
                StCollectionPersonalBusinessDetails savedDetails = instance(collectionPersonalBusinessDetailsDAO).entity(details).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException("入账失败--:" + e.getMessage());
                    }
                });


                //region写入业务明细表

                savedDetails.setGrzh(withdrawlLoan.getGrzh());//个人账号
                savedDetails.setGjhtqywlx(CollectionBusinessType.部分提取.getCode());//业务明细类型
                savedDetails.setTqyy(WithDrawalReason.REASON_6.getCode());//提取原因为偿还贷款本息
                savedDetails.setTqfs(WithdrawlMethod.转账提取.getCode());//转账提取
                savedDetails.setJzrq(new Date());//记账日期
                savedDetails.setCzbz(CommonFieldType.非冲账.getCode());//非冲账
                savedDetails.setFslxe(new BigDecimal(0.00));
                //endregion

                //region业务明细扩展表
                CCollectionPersonalBusinessDetailsExtension detailExtension = new CCollectionPersonalBusinessDetailsExtension();
                detailExtension.setCzmc(CollectionBusinessType.部分提取.getCode());
                detailExtension.setBlr("01");//本人
                detailExtension.setCzy("系统");
                CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("id", loanWithdrawl.getYwwd());
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                detailExtension.setYwwd(network);
                detailExtension.setHuming(person.getXingMing());
                detailExtension.setGrckzhhm(person.getCollectionPersonalAccount().getGrckzhhm());
                detailExtension.setGrckzhkhyhmc(person.getCollectionPersonalAccount().getGrckzhkhyhmc());
                detailExtension.setSlsj(new Date());
                detailExtension.setStep(CollectionBusinessStatus.已入账.getName());//更新业务状态
                detailExtension.setLjtqje(LJTQJE.add(withdrawlLoan.getFse()));
                detailExtension.setBjsj(new Date());//设置办结时间
                detailExtension.setJqe(new BigDecimal(-1.00));//
                //更新个人账户余额
                if (person.getCollectionPersonalAccount().getGrzhye().subtract(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe())).compareTo(new BigDecimal(0.01)) == -1) {
                    throw new ErrorException("入账失败--:" + "提取后个人账户余额不足0");
                }
                //endregion
                savedDetails.setExtension(detailExtension);
                savedDetails.setPerson(person);
                savedDetails.setUnit(person.getUnit());

                //region提取业务扩展表
                CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = new CCollectionWithdrawlBusinessExtension();
                withdrawlBussinessExtension.setZongE(savedDetails.getFse().abs());
                withdrawlBussinessExtension.setStCollectionPersonalBusinessDetails(savedDetails);
                savedDetails.setWithdrawlBusinessExtension(withdrawlBussinessExtension);
                //endregion

                //region 单位业务明细表
                StCollectionUnitBusinessDetails collectionUnitDetails = new StCollectionUnitBusinessDetails();

                collectionUnitDetails.setDwzh(savedDetails.getUnit().getDwzh());//单位账号
                collectionUnitDetails.setFse(savedDetails.getFse());
                collectionUnitDetails.setCzbz(savedDetails.getCzbz());
                collectionUnitDetails.setFsrs(new BigDecimal(1));
                collectionUnitDetails.setUnit(savedDetails.getUnit());
                collectionUnitDetails.setYwmxlx(savedDetails.getGjhtqywlx());
                collectionUnitDetails.setJzrq(savedDetails.getJzrq());
                collectionUnitDetails.setFslxe(new BigDecimal(0.00));
                //endregion

                //region 单位扩展表
                CCollectionUnitBusinessDetailsExtension unitExtension = new CCollectionUnitBusinessDetailsExtension();
                unitExtension.setStep(savedDetails.getExtension().getStep());
                unitExtension.setCzmc(savedDetails.getExtension().getCzmc());
                unitExtension.setBjsj(savedDetails.getExtension().getBjsj());
//            unitExtension.setJzpzh(details.getExtension().getJzpzh());
                unitExtension.setYwwd(savedDetails.getExtension().getYwwd());
                collectionUnitDetails.setExtension(unitExtension);
                StCollectionUnitBusinessDetails savedUnitDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException("入账失败--:" + e.getMessage());
                    }
                });
                //endregion

                //region 生成记账凭证
                int djsl = 1;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                VoucherAmount voucherAmount = new VoucherAmount();
                voucherAmount.setZhaiYao("公积金已分摊");
                voucherAmount.setJinE(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe()));
                JFSJ.add(voucherAmount);

                VoucherAmount voucherAmounts = new VoucherAmount();
                voucherAmounts.setJinE(withdrawlLoan.getFse());
                voucherAmounts.setZhaiYao("个人委托贷款");
                DFSJ.add(voucherAmounts);

                VoucherAmount oucherAmount = new VoucherAmount();
                oucherAmount.setJinE(withdrawlLoan.getFslxe());
                oucherAmount.setZhaiYao("委托贷款利息收入");
                DFSJ.add(oucherAmount);
                VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", savedDetails.getExtension().getCzy(), savedDetails.getExtension().getCzy()
                        , "", "管理员", VoucherBusinessType.月缴存额还款.getCode(),
                        VoucherBusinessType.月缴存额还款.getCode(), savedDetails.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl),
                        loanWithdrawl.getYhzhhm(), loanWithdrawl.getYhdm());
                if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                    CCollectionWithdrawlVice withdrawlVice = new CCollectionWithdrawlVice();
                    withdrawlVice.setFse(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe()).negate());
                    savedDetails.setWithdrawlVice(withdrawlVice);
                    withdrawlVice.setGrywmx(savedDetails);
                    savedDetails.setFse(new BigDecimal(0.00));//发生额
                    detailExtension.setStep(CollectionBusinessStatus.入账失败.getName());
                    detailExtension.setSbyy(voucherRes.getMSG());
                } else {

                    savedDetails.setFse(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe()).negate());//发生额
                    detailExtension.setJzpzh(voucherRes.getJZPZH());
                }

                person.getCollectionPersonalAccount().setGrzhye(person.getCollectionPersonalAccount().getGrzhye().subtract(savedDetails.getFse().abs()));
                savedUnitDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionUnitDetails.getUnit().getCollectionUnitAccount().getDwzhye().subtract(savedDetails.getFse().abs()));
                detailExtension.setDqye(person.getCollectionPersonalAccount().getGrzhye());

                try {
                    Calendar c = Calendar.getInstance();
                    ismsCommon.sendSingleSMSWithTemp(person.getSjhm(), SMSTemp.提取.getCode(), new ArrayList<String>() {{
                        this.add(c.get(Calendar.MONTH) + 1 + "");
                        this.add(c.get(Calendar.DATE) + "");
                        this.add("委托扣划提取还贷");
                        this.add(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe())+"");
                        this.add("0");
                        this.add(person.getCollectionPersonalAccount().getGrzhye().setScale(2,BigDecimal.ROUND_HALF_UP)+"");
                    }});
                } catch (Exception e) {
                }
                //endregion

                savedDetails.getExtension().setZcdw(savedUnitDetails.getYwlsh());
                collectionPersonalBusinessDetailsDAO.update(savedDetails);
                collectionUnitBusinessDetailsDAO.update(savedUnitDetails);
                commonPersonDAO.update(person);
                commonReturn.setStatus("success");
            }

        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            throw new ErrorException("入账失败--:" + e.getMessage());
        }
        return commonReturn;
    }

    @Override
    public void SMSRepament() {
        try {
            ArrayList<SMSBackBasicInfomation> backBasicInfomations = icloanHousingPersonInformationBasicDAO.SMSrepamentNor();
            if (backBasicInfomations.size() == 0) return;
            //业务明细记录
            List<OvderDueRecord> detailslinklist = stHousingBusinessDetailsDAO.searchRecord();
            List<Object> stringList = cloanHousingBusinessProcess.countRepament();

            //查看每个账号业务是否产生，details过滤，本期业务是否全部产生
            boolean checkaccount = true;
            int count = 0;
            List<SMSBackBasicInfomation> detailsbasic = new ArrayList<>();
            BigDecimal yqsjqc = null;
            for (SMSBackBasicInfomation basic : backBasicInfomations) {
                if (basic.getDQYHJE().compareTo(BigDecimal.ZERO) == 0) continue;
                for (Object dkzh : stringList) {
                    if (basic.getDKZH().equals(dkzh)) continue;
                }
                yqsjqc = basic.getDQQC().add(basic.getDKQS().subtract(basic.getDKGBJHQS()));
                //循环details查看业务明细记录（正常还款）
                boolean min = false;
                BigDecimal tailsqc = BigDecimal.ZERO;
                for (OvderDueRecord tails : detailslinklist) {
                    //正常还款生成了，但是失败了继续扣
                    if (tails.getDKZH().equals(basic.getDKZH())) {
                        if ((tails.getDQQC().compareTo(yqsjqc))<=0) {
                            if (tails.getDKZH().equals(basic.getDKZH()) && tails.getDQQC().compareTo(yqsjqc) == 0) {
                                count++;
                                //没发生
                                if (LoanBussinessStatus.待入账.getName().equals(tails.getYWZT())) {
                                    checkaccount = false;
                                    Session session = this.txManager.getSessionFactory().getCurrentSession();
                                    stHousingBusinessDetailsDAO.deleteBackLoanDuction(session, tails.getId());
                                    detailsbasic.add(basic);
                                    break;
                                }
                                break;
                            }
                        } else {
                            min = true;
                            tailsqc = tails.getDQQC();
                            break;
                        }
                    }
                }
                if (min) {
                    logger.error("正常还款期次小于当前业务明细最大期次：" + basic.getDKZH() + ", yqsjqc: " + yqsjqc + ", tailsqc: " + tailsqc);
                    continue;
                }
                //没发生
                if (count == 0) {
                    detailsbasic.add(basic);
                }
                count = 0;
            }
            //全部
            if (detailsbasic.size() != 0) checkaccount = false;
            if (checkaccount == true) return;

            Calendar calsms = Calendar.getInstance();
            calsms.add(Calendar.DATE,3);
            for (SMSBackBasicInfomation housingPersonInformationBasic : detailsbasic) {
                String str = housingPersonInformationBasic.getJKRGJJZH();
                BigDecimal currentJine = housingPersonInformationBasic.getDQYHJE();//当期应还金额

                StCommonPerson stCommonPersonByGrzh = null;
                BigDecimal sub = BigDecimal.ZERO;

                StCollectionPersonalAccount gtjkrcollectionpersonalAccounts = null;
                BigDecimal gtsub = BigDecimal.ZERO;

                StCollectionPersonalAccount housingfundpersonalAccounts = null;
                if ((housingPersonInformationBasic.isWTKHYJCE() == true)) {
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
                        if (housingPersonInformationBasic.getHYZK().equals("20") && housingPersonInformationBasic.getGTJKRGJJZH() != null) {
                            StCommonPerson byGrzh = stCommonPerson.getByGrzh(housingPersonInformationBasic.getGTJKRGJJZH());
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
                if ((housingPersonInformationBasic.isWTKHYJCE() == true)) {
                    if ((sub.compareTo(BigDecimal.ZERO) == -1 && gtsub.compareTo(BigDecimal.ZERO) == -1)) {
                        //委托扣划余额不足
                        ismsCommon.sendSingleSMSWithTemp(housingPersonInformationBasic.getSJHM(), SMSTemp.委扣余额不足.getCode(), new ArrayList<String>() {{
                            this.add(housingPersonInformationBasic.getJKRXM());
                            this.add(housingPersonInformationBasic.getDKZH().substring(housingPersonInformationBasic.getDKZH().length() - 4));
                            this.add(calsms.get(Calendar.MONTH) + 1 + "");
                            this.add(calsms.get(Calendar.DATE) + "");
                            this.add(housingPersonInformationBasic.getDQYHJE() + "");
                            this.add(calsms.get(Calendar.MONTH) + 1 + "");
                            this.add(calsms.get(Calendar.DATE) + "");
                        }});
                    } else {
                        //委托扣划
                        ismsCommon.sendSingleSMSWithTemp(housingPersonInformationBasic.getSJHM(), SMSTemp.委扣余额充足.getCode(), new ArrayList<String>() {{
                            this.add(housingPersonInformationBasic.getJKRXM());
                            this.add(housingPersonInformationBasic.getDKZH().substring(housingPersonInformationBasic.getDKZH().length() - 4));
                            this.add(calsms.get(Calendar.MONTH) + 1 + "");
                            this.add(calsms.get(Calendar.DATE) + "");
                            this.add(housingPersonInformationBasic.getDQYHJE() + "");
                        }});
                    }
                } else {
                    //非委托扣划
                    ismsCommon.sendSingleSMSWithTemp(housingPersonInformationBasic.getSJHM(), SMSTemp.非委扣.getCode(), new ArrayList<String>() {{
                        this.add(housingPersonInformationBasic.getJKRXM());
                        this.add(housingPersonInformationBasic.getDKZH().substring(housingPersonInformationBasic.getDKZH().length() - 4));
                        this.add(calsms.get(Calendar.MONTH) + 1 + "");
                        this.add(calsms.get(Calendar.DATE) + "");
                        this.add(housingPersonInformationBasic.getDQYHJE() + "");
                    }});
                }
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


}
