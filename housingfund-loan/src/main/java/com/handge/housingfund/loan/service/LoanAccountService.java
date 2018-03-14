package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.loan.ILoanAccountService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.enums.LoanRiskStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Liujuhao on 2017/8/9.
 */

@Service
public class LoanAccountService implements ILoanAccountService {

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icLoanHousingPersonInformationBasicDAO;

    @Autowired
    private IStHousingBusinessDetailsDAO iStHousingBusinessDetailsDAO;

    @Autowired
    private ICLoanHousingOverdueViceDAO icLoanHousingOverdueViceDAO;

    @Autowired
    private IStHousingOverdueRegistrationDAO iStHousingOverdueRegistrationDAO;

    @Autowired
    private ICLoanApplyRepaymentViceDAO icLoanApplyRepaymentViceDAO;

    @Autowired
    private IStHousingPersonalAccountDAO iStHousingPersonalAccountDAO;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    String format1 = "yyyy-MM-dd";

    // completed: 2017/8/10 贷款账户查询 已完成，待测试
    @Override
    public PageRes<HousingFundAccountGetRes> getHousingFundAccount(String DKZH, String JKRXM, String status, String DKFXDJ, String pageSize, String page, String YWWD, String SWTYH, String ZJHM) {

        PageRes pageRes = new PageRes();

        List<CLoanHousingPersonInformationBasic> loanHousingPersonInformationBasicList = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                if (StringUtil.notEmpty(status)) {
                    this.put("dkzhzt", status);
                }
                if (StringUtil.notEmpty(DKFXDJ) && !DKFXDJ.equals(LoanRiskStatus.所有.getCode())) {
                    this.put("personalAccount.dkfxdj", DKFXDJ);/*贷款风险等级*/
                }
                if (StringUtil.notEmpty(YWWD)) {
                    this.put("ywwd", YWWD);
                }
                if (StringUtil.notEmpty(SWTYH)) {
                    this.put("loanContract.swtyhdm", SWTYH);
                }
            }
        }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(page)).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(JKRXM)) {
                    criteria.add(Restrictions.like("jkrxm", "%" + JKRXM + "%"));
                }
                if (StringUtil.notEmpty(ZJHM)) {
                    criteria.add(Restrictions.like("jkrzjhm", "%" + ZJHM + "%"));
                }
                if (StringUtil.notEmpty(DKZH)) {
                    criteria.add(Restrictions.like("dkzh", "%" + DKZH + "%"));
                }
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        Object[] money = icLoanHousingPersonInformationBasicDAO.getLoanAccountMoneyCount(DKZH,JKRXM,status,DKFXDJ,YWWD,SWTYH,ZJHM);

        return new LoanAccountMoneyCount<HousingFundAccountGetRes>() {
            {
                final BigDecimal[] BYDKYE = {BigDecimal.ZERO};
                final BigDecimal[] BYDKFF = {BigDecimal.ZERO};
                this.setResults(new ArrayList<HousingFundAccountGetRes>() {
                    {
                        for (CLoanHousingPersonInformationBasic informationBasic : loanHousingPersonInformationBasicList) {

                            this.add(new HousingFundAccountGetRes() {
                                {
                                    this.setDKZH(informationBasic.getDkzh());
                                    this.setZhuangTai(informationBasic.getDkzhzt());
                                    this.setJKRXM(informationBasic.getJkrxm());
                                    this.setZJHM(informationBasic.getJkrzjhm());
                                    this.setSWTYH(informationBasic.getLoanContract() == null ? null : informationBasic.getLoanContract().getSwtyhmc());
                                    if (informationBasic.getPersonalAccount() != null) {
                                        if (informationBasic.getPersonalAccount().getDkye() != null) {
                                            this.setDKYE(informationBasic.getPersonalAccount().getDkye() + "");
                                            BYDKYE[0] = BYDKYE[0].add(informationBasic.getPersonalAccount().getDkye());
                                        }
                                        if (informationBasic.getPersonalAccount().getDkqs() != null && informationBasic.getYhqs() != null) {
                                            this.setSYQS(informationBasic.getPersonalAccount().getDkqs().intValue() - informationBasic.getYhqs().intValue());    // 剩余期数 = 贷款期数 - 已还期数
                                        }
                                        this.setHSBJZE(informationBasic.getPersonalAccount().getHsbjze() == null ? null : informationBasic.getPersonalAccount().getHsbjze() + "");
                                        this.setHSLXZE(informationBasic.getPersonalAccount().getHslxze() == null ? null : informationBasic.getPersonalAccount().getHslxze() + "");
                                        this.setFXZE(informationBasic.getPersonalAccount().getFxze() == null ? null : informationBasic.getPersonalAccount().getFxze() + "");
                                        this.setDKFXDJ(informationBasic.getPersonalAccount().getDkfxdj());
                                        if (informationBasic.getPersonalAccount().getDkffe() != null) {
                                            this.setDKFFE(informationBasic.getPersonalAccount().getDkffe() + "");
                                            BYDKFF[0] = BYDKFF[0].add(informationBasic.getPersonalAccount().getDkffe());
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                this.setCurrentPage(pageRes.getCurrentPage());
                this.setNextPageNo(pageRes.getNextPageNo());
                this.setPageCount(pageRes.getPageCount());
                this.setPageSize(pageRes.getPageSize());
                this.setTotalCount(pageRes.getTotalCount());
                this.setDKYEBYHJ(BYDKYE[0] + "");
                this.setDKFFBYHJ(BYDKFF[0] + "");
                this.setDKYEZHJ(money[1] == null ? "0" : money[1] + "");
                this.setDKFFZHJ(money[0] == null ? "0" : money[0] + "");
            }
        };
    }

    // completed: 2017/8/10 业务清单 已完成，待测试
    @Override
    public PageRes<HousingfundAccountBusinessListGetInformation> getHousingfundAccountBusinessList(String DKZH, String pageSize, String page) {

        PageRes pageRes = new PageRes();

        List<StHousingBusinessDetails> housingBusinessDetailsList = DAOBuilder.instance(iStHousingBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(page)).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<HousingfundAccountBusinessListGetInformation>() {
            {
                this.setResults(new ArrayList<HousingfundAccountBusinessListGetInformation>() {
                    {
                        for (StHousingBusinessDetails businessDetails : housingBusinessDetailsList) {
                            this.add(new HousingfundAccountBusinessListGetInformation() {
                                {
                                    this.setYWLSH(businessDetails.getYwlsh());
                                    this.setDKYWMXLX(businessDetails.getDkywmxlx());
                                    if (businessDetails.getYwfsrq() != null) {
                                        this.setYWFSRQ(DateUtil.date2Str(businessDetails.getYwfsrq(), format1));
                                    }
                                    this.setDKYHDM(businessDetails.getDkyhdm());
                                    this.setFSE(businessDetails.getFse() == null ? null : businessDetails.getFse() + "");
                                    this.setBJJE(businessDetails.getBjje() == null ? null : businessDetails.getBjje() + "");
                                    this.setLXJE(businessDetails.getLxje() == null ? null : businessDetails.getLxje() + "");
                                    this.setFXJE(businessDetails.getFxje() == null ? null : businessDetails.getFxje() + "");
                                    if (businessDetails.getDqqc() != null) {
                                        this.setDQQC(businessDetails.getDqqc().intValue());
                                    }
                                    this.setZCZYQBJJE(businessDetails.getZczyqbjje() == null ? null : businessDetails.getZczyqbjje() + "");
                                    this.setYQZZCBJJE(businessDetails.getYqzzcbjje() == null ? null : businessDetails.getYqzzcbjje() + "");
                                    this.setJZRQ(DateUtil.date2Str(businessDetails.getJzrq(), format1));
                                    this.setZhuangtai(businessDetails.getcHousingBusinessDetailsExtension() == null ?
                                            (businessDetails.getGrywmx() == null ? null : businessDetails.getGrywmx().getStep()) :
                                            businessDetails.getcHousingBusinessDetailsExtension().getYwzt());
                                    this.setSBYY(businessDetails.getcHousingBusinessDetailsExtension() == null ? null : businessDetails.getcHousingBusinessDetailsExtension().getSbyy());
                                }
                            });
                        }
                    }
                });
                this.setCurrentPage(pageRes.getCurrentPage());
                this.setNextPageNo(pageRes.getNextPageNo());
                this.setPageCount(pageRes.getPageCount());
                this.setPageSize(pageRes.getPageSize());
                this.setTotalCount(pageRes.getTotalCount());

            }
        };
    }

    // completed: 2017/8/10 逾期信息 已完成，已测试
    @Override
    public PageRes<HousingfundAccountOverdueListGetInformation> getHousingfundAccountOverdueList(String DKZH, String pageSize, String page) {

        PageRes pageRes = new PageRes();

        List<StHousingOverdueRegistration> overdueViceList = DAOBuilder.instance(this.iStHousingOverdueRegistrationDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(page)).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<HousingfundAccountOverdueListGetInformation>() {
            {
                this.setResults(new ArrayList<HousingfundAccountOverdueListGetInformation>() {
                    {
                        for (StHousingOverdueRegistration overdueRegistration : overdueViceList) {
                            this.add(new HousingfundAccountOverdueListGetInformation() {
                                {
                                    this.setYQQC(overdueRegistration.getYqqc().intValue());
                                    this.setYQBJ(overdueRegistration.getYqbj() == null ? null : overdueRegistration.getYqbj() + "");
                                    this.setYQLX(overdueRegistration.getYqlx() == null ? null : overdueRegistration.getYqlx() + "");
                                    this.setYQFX(overdueRegistration.getYqfx() + "");
                                    if (overdueRegistration.getSsrq() != null) {
                                        this.setSSRQ(DateUtil.date2Str(overdueRegistration.getSsrq(), format1));
                                    }
                                    boolean sfhk = (overdueRegistration.getcHousingOverdueRegistrationExtension().getYwzt().equals(LoanBussinessStatus.已入账.getName()) ? true : false);
                                    this.setSFHK(sfhk); //是否还款
                                    if (sfhk) {
                                        this.setHKQC(overdueRegistration.getHkqc() == null ? null : overdueRegistration.getHkqc() + "");
                                        this.setSSYQBJJE(overdueRegistration.getSsyqbjje() == null ? null : overdueRegistration.getSsyqbjje() + "");
                                        this.setSSYQLXJE(overdueRegistration.getSsyqlxje() == null ? null : overdueRegistration.getSsyqlxje() + "");
                                        this.setSSYQFXJE(overdueRegistration.getSsyqfxje() == null ? null : overdueRegistration.getSsyqfxje() + "");
                                    }
                                }
                            });
                        }
                    }
                });
                this.setCurrentPage(pageRes.getCurrentPage());
                this.setNextPageNo(pageRes.getNextPageNo());
                this.setPageCount(pageRes.getPageCount());
                this.setPageSize(pageRes.getPageSize());
                this.setTotalCount(pageRes.getTotalCount());
            }
        };
    }

    // TODO: 2017/8/10 提前还款通知书（待定）
    @Override
    public RepaymentApplyOverdueGet postOverdueApply(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        short yqqc = 10; /*假设当前预期期次*/

        StHousingOverdueRegistration overdueRegistration = DAOBuilder.instance(this.iStHousingOverdueRegistrationDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);

                this.put("yqqc", yqqc);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (overdueRegistration == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的逾期记录");
        }

        return new RepaymentApplyOverdueGet() {
            {
                this.setJKRXM(personInformationBasic.getJkrxm());

                if (personInformationBasic.getPersonalAccount() != null) {

                    this.setJKRQ(DateUtil.date2Str(personInformationBasic.getPersonalAccount().getDkffrq(), format1));
                    this.setJKZJE(personInformationBasic.getPersonalAccount().getDkffe() == null ? null : personInformationBasic.getPersonalAccount().getDkffe() + "");
                    this.setYQTS(CommLoanAlgorithm.interestDays(personInformationBasic.getPersonalAccount().getDkffrq(), yqqc, new Date()) + ""); //逾期天数
                    this.setQXTS(null);/*期限天数，这个如何设置？*/
                    this.setBenJin(overdueRegistration.getYqbj() == null ? null : overdueRegistration.getYqbj() + "");   //未还本金
                    this.setLiXi(overdueRegistration.getYqlx() == null ? null : overdueRegistration.getYqlx() + ""); //未还利息
                    this.setFaXi(overdueRegistration.getYqfx() == null ? null : overdueRegistration.getYqfx() + ""); //罚息金额
                    this.setRiQi(DateUtil.getNowDateTime());
                }
            }
        };
    }

    // completed: 2017/8/10  还款记录 已完成，待测试
    @Override
    public PageRes<HousingRecordListGetInformation> getHousingRecordList(String DKZH, String pageSize, String page) {


        PageRes pageRes = new PageRes();

        List<StHousingBusinessDetails> housingBusinessDetailsList = DAOBuilder.instance(iStHousingBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
                this.put("dkywmxlx", Arrays.asList(
                        LoanBusinessType.正常还款.getCode(),
                        LoanBusinessType.提前还款.getCode(),
                        LoanBusinessType.逾期还款.getCode(),
                        LoanBusinessType.公积金提取还款.getCode(),
                        LoanBusinessType.结清.getCode()));

            }
        }).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("cHousingBusinessDetailsExtension", "cHousingBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cHousingBusinessDetailsExtension.ywzt", LoanBussinessStatus.已入账.getName()));
            }
        }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(page)).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<HousingRecordListGetInformation>() {
            {
                this.setResults(new ArrayList<HousingRecordListGetInformation>() {
                    {
                        for (StHousingBusinessDetails housingBusinessDetails : housingBusinessDetailsList) {
                            this.add(new HousingRecordListGetInformation() {
                                {
                                    this.setYWLSH(housingBusinessDetails.getYwlsh());
                                    this.setDKYWMXLX(housingBusinessDetails.getDkywmxlx());
                                    if (housingBusinessDetails.getYwfsrq() != null) {
                                        this.setYWFSRQ(DateUtil.date2Str(housingBusinessDetails.getYwfsrq(), format1));
                                    }
                                    this.setFSE(housingBusinessDetails.getFse() == null ? null : housingBusinessDetails.getFse() + "");
                                    this.setBJJE(housingBusinessDetails.getBjje() == null ? null : housingBusinessDetails.getBjje() + "");
                                    this.setLXJE(housingBusinessDetails.getLxje() == null ? null : housingBusinessDetails.getLxje() + "");
                                    this.setFXJE(housingBusinessDetails.getFxje() == null ? null : housingBusinessDetails.getFxje() + "");
                                    if (housingBusinessDetails.getDqqc() != null) {
                                        this.setDQQC(housingBusinessDetails.getDqqc().intValue());
                                    }
                                    if (housingBusinessDetails.getJzrq() != null) {
                                        this.setJZRQ(DateUtil.date2Str(housingBusinessDetails.getJzrq(), format1));
                                    }
                                    if (housingBusinessDetails.getcHousingBusinessDetailsExtension() != null) {
                                        this.setDKYE(housingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye() == null ? null : housingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye() + "");
                                    }
                                }
                            });
                        }
                    }
                });
                this.setCurrentPage(pageRes.getCurrentPage());
                this.setNextPageNo(pageRes.getNextPageNo());
                this.setPageCount(pageRes.getPageCount());
                this.setPageSize(pageRes.getPageSize());
                this.setTotalCount(pageRes.getTotalCount());
            }
        };
    }

    // TODO: 2017/8/10 打印还款记录（即还款记录，打印逻辑交由前端；该API不再实现）
    @Override
    public HousingRecordPrintListGet getHousingRecordPintList(String DKZH) {
        return null;
    }

    // completed: 2017/8/10 还款计划 已完成，待测试
    @Override
    public HousingfundAccountPlanGet getHousingfundPlanList(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");

        }

        return new HousingfundAccountPlanGet() {
            {
                this.setJKRXM(personInformationBasic.getJkrxm());
                this.setJKRZJHM(personInformationBasic.getJkrzjhm());
                this.setDKZH(personInformationBasic.getDkzh());
                if (personInformationBasic.getLoanContract() != null && personInformationBasic.getPersonalAccount() != null) {
                    this.setDKFFE(personInformationBasic.getFunds().getHtdkje() == null ? null : personInformationBasic.getFunds().getHtdkje() + "");
                    this.setDKQS(personInformationBasic.getFunds().getDkqs() == null ? null : personInformationBasic.getFunds().getDkqs() + "");
                    this.setDKHKFS(personInformationBasic.getFunds().getHkfs());
                    try {
                        this.setinformation(CommLoanAlgorithm.repaymentPlan(
                                personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkgbjhye(),
                                personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkgbjhqs().intValue(),
                                personInformationBasic.getLoanContract().getDkhkfs(),
                                CommLoanAlgorithm.lendingRate(personInformationBasic.getPersonalAccount().getDkll(), personInformationBasic.getPersonalAccount().getLlfdbl()),
                                DateUtil.date2Str(personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkxffrq(), format1)));
                    } catch (ParseException e) {
                        throw new ErrorException(e);
                    }
                }
            }
        };
    }

    // completed: 2017/8/10 贷款账户详情 已完成，待测试
    @Override
    public HousingDkzhGet getHousingDkzh(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        return new HousingDkzhGet() {
            {
                this.setJKRXM(personInformationBasic.getJkrxm());
                this.setJKRZJLX(personInformationBasic.getJkrzjlx());
                this.setJKRZJHM(personInformationBasic.getJkrzjhm());
                this.setDKZH(personInformationBasic.getDkzh());
                this.setDKZHZT(personInformationBasic.getDkzhzt());
                if (personInformationBasic.getPersonalAccount() != null) {
                    this.setJKHTBH(personInformationBasic.getPersonalAccount().getJkhtbh());
                    this.setDKFXDJ(personInformationBasic.getPersonalAccount().getDkfxdj());
                    this.setStatus(personInformationBasic.getDkzhzt());
                    this.setDKFFE(personInformationBasic.getPersonalAccount().getDkffe() == null ? null : personInformationBasic.getPersonalAccount().getDkffe() + "");
                    if (personInformationBasic.getPersonalAccount().getDkffrq() != null) {
                        this.setDKFFRQ(DateUtil.date2Str(personInformationBasic.getPersonalAccount().getDkffrq(), format1));
                    }
                    this.setDKYE(personInformationBasic.getPersonalAccount().getDkye() == null ? null : personInformationBasic.getPersonalAccount().getDkye() + "");
                    this.setDKLL(personInformationBasic.getPersonalAccount().getDkll() == null ? null : personInformationBasic.getPersonalAccount().getDkll() + "");
                    this.setLLFDBL(personInformationBasic.getPersonalAccount().getLlfdbl() == null ? null : personInformationBasic.getPersonalAccount().getLlfdbl() + "");
                    if (personInformationBasic.getPersonalAccount().getDkqs() != null) {
                        this.setDKQS(personInformationBasic.getPersonalAccount().getDkqs().intValue());
                    }
                    // TODO: 2017/9/8 杜俊杰
                    if (personInformationBasic.getPersonalAccount().getDkqs() != null && personInformationBasic.getYhqs() != null) {
                        this.setSYQS(personInformationBasic.getPersonalAccount().getDkqs().intValue() - personInformationBasic.getYhqs().intValue());
                    } else if (personInformationBasic.getYhqs() == null && personInformationBasic.getPersonalAccount().getDkqs() != null) {
                        this.setSYQS(personInformationBasic.getPersonalAccount().getDkqs().intValue());
                    }

                    if (personInformationBasic.getDqyqqs() != null) {
                        this.setDQYQQS(personInformationBasic.getDqyqqs().intValue());   //当前逾期期数，重点验证
                    }
                    this.setDQJHHKJE(personInformationBasic.getPersonalAccount().getDqjhhkje() == null ? null : personInformationBasic.getPersonalAccount().getDqjhhkje() + ""); //当前计划还款金额 model缺失
                    this.setDQJHGHBJ(personInformationBasic.getPersonalAccount().getDqjhghbj() == null ? null : personInformationBasic.getPersonalAccount().getDqjhghbj() + "");
                    this.setDQJHGHLX(personInformationBasic.getPersonalAccount().getDqjhghlx() == null ? null : personInformationBasic.getPersonalAccount().getDqjhghlx() + "");
                    this.setDQYHJE(personInformationBasic.getPersonalAccount().getDqyhje() == null ? null : personInformationBasic.getPersonalAccount().getDqyhje() + "");
                    this.setDQYHBJ(personInformationBasic.getPersonalAccount().getDqyhbj() == null ? null : personInformationBasic.getPersonalAccount().getDqyhbj() + "");
                    this.setDQYHLX(personInformationBasic.getPersonalAccount().getDqyhlx() == null ? null : personInformationBasic.getPersonalAccount().getDqyhlx() + "");
                    this.setDQYHFX(personInformationBasic.getPersonalAccount().getDqyhfx() == null ? null : personInformationBasic.getPersonalAccount().getDqyhfx() + "");
                    if (personInformationBasic.getPersonalAccount().getLjyqqs() != null) {
                        this.setLJYQQS(personInformationBasic.getPersonalAccount().getLjyqqs().intValue());
                    }
                    this.setHSBJZE(personInformationBasic.getPersonalAccount().getHsbjze() == null ? null : personInformationBasic.getPersonalAccount().getHsbjze() + "");
                    this.setHSLXZE(personInformationBasic.getPersonalAccount().getHslxze() == null ? null : personInformationBasic.getPersonalAccount().getHslxze() + "");
                    this.setTQGHBJZE(personInformationBasic.getPersonalAccount().getTqghbjze() == null ? null : personInformationBasic.getPersonalAccount().getTqghbjze() + "");
                    this.setYQBJZE(personInformationBasic.getPersonalAccount().getYqbjze() == null ? null : personInformationBasic.getPersonalAccount().getYqbjze() + "");
                    this.setFXZE(personInformationBasic.getPersonalAccount().getFxze() == null ? null : personInformationBasic.getPersonalAccount().getFxze() + "");
                    this.setYQLXZE(personInformationBasic.getPersonalAccount().getYqlxze() == null ? null : personInformationBasic.getPersonalAccount().getYqlxze() + "");
                    if (personInformationBasic.getPersonalAccount().getDkjqrq() != null) {
                        this.setDKJQRQ(DateUtil.date2Str(personInformationBasic.getPersonalAccount().getDkjqrq(), format1));
                    }
                }
            }
        };
    }

    /**
     * 根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号
     *
     * @param DKZH
     * @return
     */
    @Override
    public LoanAccountBorrowerInfo getLoanAccountBorrowerInfo(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        return new LoanAccountBorrowerInfo() {
            {
                this.setDKZH(DKZH);
                this.setJKRXM(personInformationBasic.getJkrxm());
                this.setJKRZJHM(personInformationBasic.getJkrzjhm());
                this.setJKRZJLX(personInformationBasic.getJkrzjlx());
                if (personInformationBasic.getLoanContract() != null) {
                    this.setDKFFRQ(personInformationBasic.getLoanContract().getYdfkrq() == null ? null : DateUtil.date2Str(personInformationBasic.getLoanContract().getYdfkrq(), "yyyy-MM-dd"));
                    this.setDKDQRQ(personInformationBasic.getLoanContract().getYddqrq() == null ? null : DateUtil.date2Str(personInformationBasic.getLoanContract().getYddqrq(), "yyyy-MM-dd"));
                }
            }
        };
    }

    // TODO: 2017/8/10 手动修改贷款状态（转为呆账）
    @Override
    public CommonResponses putBadDebts(String DKZH, String status) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        if (!personInformationBasic.getDkzhzt().equals(LoanAccountType.逾期.getCode())) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前贷款账户状态非逾期");
        }

        // TODO: 2017/8/17 最新：呆账不调用财务凭证
//        String jzpzh = voucherManagerService.addVoucher("毕节市住房公积金管理中心", personInformationBasic.getJkrxm(), personInformationBasic.getCzy(), "", "管理员",
//                VoucherBusinessType.发生呆账.getCode(), VoucherBusinessType.发生呆账.getCode(), DKZH, "转为呆账", new BigDecimal("0.00"));

        /*        上面通过后才能执行*/

        personInformationBasic.setDkzhzt(LoanAccountType.呆账.getCode()); // 4表示呆账

        DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).entity(personInformationBasic).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

                throw new ErrorException(e);
            }
        });

        return new CommonResponses() {
            {
                this.setId(DKZH);
                this.setState("success");
            }
        };
    }

    @Override
    public CommonResponses putRiskAssessment(String DKZH, String rank) {

        StHousingPersonalAccount personalAccount = DAOBuilder.instance(this.iStHousingPersonalAccountDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personalAccount == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        personalAccount.setDkfxdj(rank);

        DAOBuilder.instance(this.iStHousingPersonalAccountDAO).entity(personalAccount).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new CommonResponses() {
            {
                this.setId(DKZH);
                this.setState("success");
            }
        };
    }
    @Override
    public CommonResponses getSquareReceipt(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
                this.put("dkzhzt",LoanAccountType.已结清.getCode());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的结清相关信息");
        }
        AccountSquarepdfInformation accountSquarepdfInformation = new AccountSquarepdfInformation();
        accountSquarepdfInformation.setDKZH(DKZH);
        accountSquarepdfInformation.setXingMing(personInformationBasic.getJkrxm());
        accountSquarepdfInformation.setZJHM(personInformationBasic.getJkrzjhm());
        accountSquarepdfInformation.setJKQS(personInformationBasic.getPersonalAccount().getDkqs());
        accountSquarepdfInformation.setJKJE_DX(StringUtil.digitUppercase(personInformationBasic.getPersonalAccount().getDkffe().doubleValue()));
        accountSquarepdfInformation.setJKJE(personInformationBasic.getPersonalAccount().getDkffe());
        accountSquarepdfInformation.setFKR(personInformationBasic.getPersonalAccount().getDkffrq());
        accountSquarepdfInformation.setJQR(personInformationBasic.getPersonalAccount().getDkjqrq());
        String id = pdfService.getSquareReceiptPdf(accountSquarepdfInformation);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public CommonResponses getHousingfundPlan(TokenContext tokenContext,String DKZH, String HKRQS, String HKRQE) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }
        HousingfundAccountPlanGet planGet = new HousingfundAccountPlanGet();
        planGet.setJKRXM(personInformationBasic.getJkrxm());
        planGet.setJKRZJHM(personInformationBasic.getJkrzjhm());
        planGet.setDKZH(personInformationBasic.getDkzh());
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        planGet.setYWWD(network.getMingCheng());
        if (personInformationBasic.getLoanContract() != null && personInformationBasic.getPersonalAccount() != null) {
            planGet.setDKFFE(personInformationBasic.getFunds().getHtdkje() == null ? null : personInformationBasic.getFunds().getHtdkje() + "");
            planGet.setDKQS(personInformationBasic.getFunds().getDkqs() == null ? null : personInformationBasic.getFunds().getDkqs() + "");
            planGet.setDKHKFS(personInformationBasic.getFunds().getHkfs());
            try {
                planGet.setinformation(CommLoanAlgorithm.repaymentPlanTimeFilter(
                        personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkgbjhye(),
                        personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkgbjhqs().intValue(),
                        personInformationBasic.getLoanContract().getDkhkfs(),
                        CommLoanAlgorithm.lendingRate(personInformationBasic.getPersonalAccount().getDkll(), personInformationBasic.getPersonalAccount().getLlfdbl()),
                        DateUtil.date2Str(personInformationBasic.getPersonalAccount().getcLoanHousingPersonalAccountExtension().getDkxffrq(), format1),HKRQS,HKRQE));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }
        String id = pdfService.getHousingfundPlanPdf(planGet);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }
    public PaymentHistoryDataRes getHousingRecordData(TokenContext tokenContext,String DKZH, String HKRQS, String HKRQE) {
        if (!StringUtil.notEmpty(DKZH)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }
        if(!StringUtil.notEmpty(HKRQS) || !StringUtil.notEmpty(HKRQE) ){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "查询时间缺失");
        }
        if(!DateUtil.isFollowFormat(HKRQS,"yyyy-MM" , false)){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "开始时间格式不正确");
        }
        if(!DateUtil.isFollowFormat(HKRQE,"yyyy-MM" , false)){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束时间格式不正确");
        }
        int result = HKRQS.compareTo(HKRQE);

        if (result >0){
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能大于结束月份");
        }

        PaymentHistoryDataRes paymentHistoryDataRes= new PaymentHistoryDataRes();

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }
        paymentHistoryDataRes.setDKZH(personInformationBasic.getDkzh());
        paymentHistoryDataRes.setJKRXM(personInformationBasic.getJkrxm());
        paymentHistoryDataRes.setJKRZJHM(personInformationBasic.getJkrzjhm());
        paymentHistoryDataRes.setJKRZJLX("身份证");
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        paymentHistoryDataRes.setYWWD(network.getMingCheng());
        List<StHousingBusinessDetails> housingBusinessDetailsList = DAOBuilder.instance(iStHousingBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
                this.put("dkywmxlx", Arrays.asList(
                        LoanBusinessType.正常还款.getCode(),
                        LoanBusinessType.提前还款.getCode(),
                        LoanBusinessType.逾期还款.getCode(),
                        LoanBusinessType.公积金提取还款.getCode(),
                        LoanBusinessType.结清.getCode()));

            }
        }).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("cHousingBusinessDetailsExtension", "cHousingBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cHousingBusinessDetailsExtension.ywzt", LoanBussinessStatus.已入账.getName()));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        ArrayList<HousingRecordListGetInformation> RecordInformations = new ArrayList<HousingRecordListGetInformation>();



        for (StHousingBusinessDetails housingBusinessDetails : housingBusinessDetailsList) {
                HousingRecordListGetInformation RecordInformation = new HousingRecordListGetInformation();
                RecordInformation.setYWLSH(housingBusinessDetails.getYwlsh());
                RecordInformation.setDKYWMXLX(LoanBusinessType.getNameByCode(housingBusinessDetails.getDkywmxlx()));
                if (housingBusinessDetails.getYwfsrq() != null) {
                    RecordInformation.setYWFSRQ(DateUtil.date2Str(housingBusinessDetails.getYwfsrq(), format1));
                }
                RecordInformation.setFSE(housingBusinessDetails.getFse() == null ? null : housingBusinessDetails.getFse() + "");
                RecordInformation.setBJJE(housingBusinessDetails.getBjje() == null ? null : housingBusinessDetails.getBjje() + "");
                RecordInformation.setLXJE(housingBusinessDetails.getLxje() == null ? null : housingBusinessDetails.getLxje() + "");
                RecordInformation.setFXJE(housingBusinessDetails.getFxje() == null ? null : housingBusinessDetails.getFxje() + "");
                if (housingBusinessDetails.getDqqc() != null) {
                    RecordInformation.setDQQC(housingBusinessDetails.getDqqc().intValue());
                }
                if (housingBusinessDetails.getJzrq() != null) {
                    RecordInformation.setJZRQ(DateUtil.date2Str(housingBusinessDetails.getJzrq(), format1));
                }
                SimpleDateFormat ny = new SimpleDateFormat("yyyy-MM");
                String JZRQ = ny.format(housingBusinessDetails.getJzrq());
                if(JZRQ!=null && HKRQS!=null && HKRQE!=null){
                    int res_l = JZRQ.compareTo(HKRQS);
                    int res_r = JZRQ.compareTo(HKRQE);
                    if (res_l==0){//"a=b"
                    }else if(res_l < 0){ //"a<b"
                        continue;
                    }else if(res_l>0 && res_r<=0){
                    }else if(res_l>0 && res_r>0){//"a>b"
                        continue;
                    }
                }
                if (housingBusinessDetails.getcHousingBusinessDetailsExtension() != null) {
                    RecordInformation.setDKYE(housingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye() == null ? null : housingBusinessDetails.getcHousingBusinessDetailsExtension().getXqdkye() + "");
                }
            RecordInformations.add(RecordInformation);
        }

        paymentHistoryDataRes.setHousingRecordListGetInformations(RecordInformations);
        return  paymentHistoryDataRes;
    }
}
