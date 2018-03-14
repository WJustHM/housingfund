package com.handge.housingfund.collection.service.allochthonous;

import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.bank.enums.AcctClassEnums;
import com.handge.housingfund.common.service.bank.enums.BusTypeEnums;
import com.handge.housingfund.common.service.bank.enums.SettleTypeEnums;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.collection.allochthonous.model.*;

import com.handge.housingfund.common.service.collection.allochthonous.service.TransferFinalInterface;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferOutInterface;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.collection.service.common.ICalculateInterest;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import com.handge.housingfund.common.service.TokenContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;


@SuppressWarnings({"ConstantConditions", "JavaDoc", "SpellCheckingInspection", "Convert2Lambda", "DanglingJavadoc", "unused", "ConstantIfStatement", "SpringJavaAutowiredMembersInspection", "Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
@Service(value = "final.out")
public class TransferOutImpl implements TransferOutInterface, TransferFinalInterface {

    @Autowired
    private ICCollectionAllochthounousTransferProcessDAO collectionAllochthounousTransferProcessDAO;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionAllochthounousTransferViceDAO collectionAllochthounousTransferViceDAO;
    @Autowired
    private IStateMachineService stateMachineService;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private ITransfer iTransfer;
    @Autowired
    private IBank iBankService;
    @Autowired
    private ISettlementSpecialBankAccountManageService settlementSpecialBankAccountManageService;
    @Autowired
    private ICalculateInterest calculateInterest;
    @Autowired
    private UnitRemittance unitRemittance;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;
    @Autowired
    private ISettlementSpecialBankAccountManageService accountManageService;
    @Autowired
    private IBankInfoService iBankInfoService;
    @Autowired
    private ICBankCenterInfoDAO bankCenterInfoDAO;
    @Autowired
    private IStSettlementSpecialBankAccountDAO settlementSpecialBankAccountDAO;

    private static String format = "yyyy-MM-dd";
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    private static String formatNYRSFM = "yyyy-MM-dd HH:mm:ss";
    private static String formatNY = "yyyy-MM";


    @Override
    public HandleContactLetterListModle handleContactLetterList(TokenContext tokenContext, String LXHBH, String ZGXM, String ZJHM, String zhuangTai, String ZRZXMC, String KSSJ, String JSSJ, String pageNo, String pageSize) {

        if (false) {
            return ModelUtils.randomModel(HandleContactLetterListModle.class);
        }

        //region //必要字段查询&正确性验证
        PageRes pageRes = new PageRes();

        List<StCollectionPersonalBusinessDetails> list_details = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(LXHBH)) {
                this.put("allochthounousTransferVice.LXHBH", LXHBH);
            }
            if (StringUtil.notEmpty(ZGXM)) {
                this.put("allochthounousTransferVice.ZGXM", ZGXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                this.put("allochthounousTransferVice.ZJHM", ZJHM);
            }
            if (StringUtil.notEmpty(ZRZXMC)) {
                this.put("allochthounousTransferVice.ZRGJJZXMC", ZRZXMC);
            }

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (!(StringUtil.notEmpty(LXHBH) || StringUtil.notEmpty(ZGXM) || StringUtil.notEmpty(ZJHM) || StringUtil.notEmpty(ZRZXMC))) {

                    criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                }

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                if (LoanBussinessStatus.待审核.getName().equals(zhuangTai)) {

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));

                } else if (StringUtil.notEmpty(zhuangTai) && !LoanBussinessStatus.所有.getName().equals(zhuangTai)) {

                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", zhuangTai));
                }


                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                }

                criteria.add(Restrictions.ne("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.联系函审核通过.getName()));

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");

                if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }

        }).pageOption(pageRes, StringUtil.safeBigDecimal(pageSize).intValue(), StringUtil.safeBigDecimal(pageNo).intValue()).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        return new HandleContactLetterListModle() {
            {

                this.setNextPageNo(pageRes.getNextPageNo() + ""/*下一页*/);
                this.setCurrentPage(pageRes.getCurrentPage() + ""/*当前页码*/);
                this.setPageSize(pageRes.getPageSize() + ""/*当前页码数据条数*/);
                this.setTotalCount(pageRes.getTotalCount() + ""/*总条数*/);
                this.setPageCount((pageRes.getTotalCount() / (pageRes.getPageSize() == 0 ? 1 : pageRes.getPageSize())) + ""/*总页数*/);
                this.setResults(new ArrayList<HandleContactLetterListModleResults>() {
                    {
                        for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_details) {
                            this.add(new HandleContactLetterListModleResults() {
                                {
                                    CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

                                    this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh()/*业务流水号*/);
                                    this.setLXHBH(collectionAllochthounousTransferVice.getLXHBH()/*联系函编号*/);
                                    this.setZRGJJZXMC(collectionAllochthounousTransferVice.getZRGJJZXMC()/*转入中心名称*/);
                                    this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                                    this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                                    this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                                    this.setZhuangTai(collectionPersonalBusinessDetails.getExtension().getStep()/*状态*/);
                                    this.setZYJE(collectionAllochthounousTransferVice.getZYJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*转移金额*/);
                                    this.setYZHBJJE(collectionAllochthounousTransferVice.getYZHBJJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*原账户本金金额*/);
                                    this.setBNDLX(collectionAllochthounousTransferVice.getBNDLX().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*本年度利息*/);
                                    this.setKHRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getKHRQ(), format)/*开户日期*/);
                                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionAllochthounousTransferVice.getJZNY(), formatNY)/*缴至年月*/);
                                    this.setJZYFZQLGYSFLXJC(collectionAllochthounousTransferVice.getLXJC() ? "1" : "0"/*缴至月份之前6个月是否连续缴存*/);
                                    this.setZZCDSYZFGJJDKCS(collectionAllochthounousTransferVice.getGJJDKCS() + ""/*在转出地使用住房公积金贷款次数*/);
                                    this.setSFYWJQDGJJDK(collectionAllochthounousTransferVice.getJQDK() ? "1" : "0"/*是否有未结清的公积金贷款*/);
                                    this.setSFCZPTPDXW(collectionAllochthounousTransferVice.getSFCZPTPD() ? "1" : "0"/*是否存在骗提骗贷行为*/);
                                    this.setYWBLLXDH(collectionAllochthounousTransferVice.getLXDHHCZ()/*业务办理联系电话*/);
                                }
                            });
                        }
                    }
                }/**/);
            }
        };
    }

    @Override
    public ReceiveContactLetterListModle receiveContactLetterList(TokenContext tokenContext, String LXHBH, String ZGXM, String ZJHM, String KSSJ, String JSSJ, String pageNo, String pageSize) {

        if (false) {
            return ModelUtils.randomModel(ReceiveContactLetterListModle.class);
        }

        //region //必要字段查询&正确性验证
        PageRes pageRes = new PageRes();

        List<StCollectionPersonalBusinessDetails> list_details = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(LXHBH)) {
                this.put("allochthounousTransferVice.LXHBH", LXHBH);
            }
            if (StringUtil.notEmpty(ZGXM)) {
                this.put("allochthounousTransferVice.ZGXM", ZGXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                this.put("allochthounousTransferVice.ZJHM", ZJHM);
            }


        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (!(StringUtil.notEmpty(LXHBH) || StringUtil.notEmpty(ZGXM) || StringUtil.notEmpty(ZJHM))) {

                    criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                }

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                }
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.联系函审核通过.getName()));

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");

                if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }

        }).pageOption(pageRes, StringUtil.safeBigDecimal(pageSize).intValue(), StringUtil.safeBigDecimal(pageNo).intValue()).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        return new ReceiveContactLetterListModle() {
            {

                this.setNextPageNo(pageRes.getNextPageNo() + ""/*下一页*/);
                this.setCurrentPage(pageRes.getCurrentPage() + ""/*当前页码*/);
                this.setPageSize(pageRes.getPageSize() + ""/*当前页码数据条数*/);
                this.setTotalCount(pageRes.getTotalCount() + ""/*总条数*/);
                this.setPageCount((pageRes.getTotalCount() / (pageRes.getPageSize() == 0 ? 1 : pageRes.getPageSize())) + ""/*总页数*/);
                this.setResults(new ArrayList<ReceiveContactLetterListModleResults>() {
                    {
                        for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_details) {

                            this.add(new ReceiveContactLetterListModleResults() {
                                {
                                    CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

                                    this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh()/*业务流水号*/);
                                    this.setLXHBH(collectionAllochthounousTransferVice.getLXHBH()/*联系函编号*/);
                                    this.setZRGJJZXMC(collectionAllochthounousTransferVice.getZRGJJZXMC()/*转入公积金中心名称*/);
                                    this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                                    this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                                    this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                                    this.setYGZDWMC(collectionAllochthounousTransferVice.getYGZDWMC()/*原工作单位名称*/);
                                    this.setYGRZFGJJZH(collectionAllochthounousTransferVice.getYGRZFGJJZH()/*原住房公积金账号*/);
                                    this.setZRZJZH(collectionAllochthounousTransferVice.getZRZJZH()/*转入资金账号*/);
                                    this.setZRZJZHHM(collectionAllochthounousTransferVice.getZRZJZHHM()/*转入资金账号户名*/);
                                    this.setZRZJZHYHMC(collectionAllochthounousTransferVice.getZRZJZHYHMC()/*转入资金账号银行名称*/);
                                    this.setZRGJJZXLXFS(collectionAllochthounousTransferVice.getLXDHHCZ()/*转入公积金中心联系方式*/);
                                    this.setLXDSCRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getLXDSCRQ(), format)/*联系单生成日期*/);
                                    this.setZhuangTai(collectionPersonalBusinessDetails.getExtension().getStep()/*审核状态*/);
                                }
                            });
                        }
                    }
                }/**/);

            }
        };
    }

    @Override
    public TransferOutAccountDetailsModle transferOutAccountDetails(TokenContext tokenContext, final String YWLSH) {

        if (false) {
            return ModelUtils.randomModel(TransferOutAccountDetailsModle.class);
        }

        //region //必要数据查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        return new TransferOutAccountDetailsModle() {
            {
                CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();
                this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh()/*业务流水号*/);
                this.setLXHBH(collectionAllochthounousTransferVice.getLXHBH()/*联系函编号*/);
                boolean isNew = CollectionBusinessStatus.联系函确认接收.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) && collectionPersonalBusinessDetails.getPerson() != null;
                this.setAccountMsg(isNew ? new TransferOutAccountDetailsModleAccountMsg() {

                    {
                        this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                        this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                        this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                        this.setYZHBJYE(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*原账户本金余额*/);
                        this.setBNDLX(getInterest(collectionPersonalBusinessDetails).setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*本年度利息*/);
                        this.setZYJE(new BigDecimal(this.getYZHBJYE()).add(new BigDecimal(this.getBNDLX())).setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*转移金额*/);
                        this.setKHRQ(DateUtil.date2Str(collectionPersonalBusinessDetails.getPerson().getCreated_at(), format)/*开户日期*/);
                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionPersonalBusinessDetails.getPerson().getExtension().getGrjzny(), formatNY)/*缴至年月*/);
                        Integer consecutiveDepositMonths = new BigDecimal(unitRemittance.getConsecutiveDepositMonths(collectionPersonalBusinessDetails.getPerson().getGrzh()).getId()).intValue();
                        this.setLXJC(consecutiveDepositMonths <= 1 ? "0" : "1"/*连续缴存*/);
                        List<CLoanHousingPersonInformationBasic> list_loanHousingPersonInformationBasic = instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

                            this.put("jkrxm", collectionPersonalBusinessDetails.getPerson().getXingMing());
                            this.put("jkrzjhm", collectionPersonalBusinessDetails.getPerson().getZjhm());

                        }}).extension(new IBaseDAO.CriteriaExtension() {
                            @Override
                            public void extend(Criteria criteria) {
                                criteria.add(Restrictions.isNotNull("loanContract"));
                                criteria.add(Restrictions.isNotNull("personalAccount"));
                            }
                        }).getList(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }

                        });
                        this.setGJJDKCS(list_loanHousingPersonInformationBasic.size() + ""/*公积金贷款次数*/);
                        this.setJQDK("1");
                        for (CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic : list_loanHousingPersonInformationBasic) {

                            if ("1".equals(this.getJQDK()) && !(LoanAccountType.已结清.getCode().equals(loanHousingPersonInformationBasic.getDkzhzt())||LoanAccountType.已作废.getCode().equals(loanHousingPersonInformationBasic.getDkzhzt()))) {

                                this.setJQDK("0");
                            }
                        }
                        this.setSFCZPTPD(collectionAllochthounousTransferVice.getSFCZPTPD() ? "1" : "0"/*是否存在骗提骗贷*/);
                        this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getSlsj(), formatNYRSFM)/*受理时间*/);
                    }
                } : new TransferOutAccountDetailsModleAccountMsg() {
                    {
                        this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                        this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                        this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                        this.setYZHBJYE(collectionAllochthounousTransferVice.getYZHBJJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*原账户本金余额*/);
                        this.setBNDLX(collectionAllochthounousTransferVice.getBNDLX().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*本年度利息*/);
                        this.setZYJE(collectionAllochthounousTransferVice.getZYJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*转移金额*/);
                        this.setKHRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getKHRQ(), format)/*开户日期*/);
                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionAllochthounousTransferVice.getJZNY(), formatNY)/*缴至年月*/);
                        this.setLXJC(collectionAllochthounousTransferVice.getLXJC() ? "1" : "0"/*连续缴存*/);
                        this.setGJJDKCS(collectionAllochthounousTransferVice.getGJJDKCS() + ""/*公积金贷款次数*/);
                        this.setJQDK(collectionAllochthounousTransferVice.getJQDK() ? "1" : "0"/*结清贷款*/);
                        this.setSFCZPTPD(collectionAllochthounousTransferVice.getSFCZPTPD() ? "1" : "0"/*是否存在骗提骗贷*/);
                        this.setYWBLLXDH(collectionAllochthounousTransferVice.getYWLXDH()/*业务联系电话*/);
                        this.setFKXX(collectionAllochthounousTransferVice.getFKXX()/*反馈信息*/);
                        this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getSlsj(), formatNYRSFM)/*受理时间*/);
                    }
                }/**/);
                this.setTurnOutFundsAccountMsg(new TransferOutAccountDetailsModleTurnOutFundsAccountMsg() {
                    {
                        this.setFKYH(collectionAllochthounousTransferVice.getFKYHMC()/*付款银行*/);
                        this.setFKZH(collectionAllochthounousTransferVice.getFKZH()/*付款账号*/);
                        this.setYHHM(collectionAllochthounousTransferVice.getFKHM()/*付款户名*/);
                    }
                }/**/);

            }
        };
    }

    @Override
    public TransferOutDetailsModle transferOutDetails(TokenContext tokenContext, final String YWLSH) {

        if (false) {
            return ModelUtils.randomModel(TransferOutDetailsModle.class);
        }

        //region //必要字段查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        return new TransferOutDetailsModle() {
            {
                CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();
                this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh()/*业务流水号*/);
                this.setLXHBH(collectionAllochthounousTransferVice.getLXHBH()/*联系函编号*/);
                this.setZhuangTai(collectionAllochthounousTransferVice.getLXHZT()/*状态*/);
                boolean isNew = CollectionBusinessStatus.联系函确认接收.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) && collectionPersonalBusinessDetails.getPerson() != null;
                this.setAccountMsg(isNew ? new TransferOutDetailsModleAccountMsg() {

                    {
                        this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                        this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                        this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                        this.setYZHBJYE(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*原账户本金余额*/);
                        this.setBNDLX(getInterest(collectionPersonalBusinessDetails).setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*本年度利息*/);
                        this.setZYJE(new BigDecimal(this.getYZHBJYE()).add(new BigDecimal(this.getBNDLX())).setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*转移金额*/);
                        this.setKHRQ(DateUtil.date2Str(collectionPersonalBusinessDetails.getPerson().getCreated_at(), format)/*开户日期*/);
                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionPersonalBusinessDetails.getPerson().getExtension().getGrjzny(), formatNY)/*缴至年月*/);
                        Integer consecutiveDepositMonths = new BigDecimal(unitRemittance.getConsecutiveDepositMonths(collectionPersonalBusinessDetails.getPerson().getGrzh()).getId()).intValue();
                        this.setLXJC(consecutiveDepositMonths <= 1 ? "0" : "1"/*连续缴存*/);
                        List<CLoanHousingPersonInformationBasic> list_loanHousingPersonInformationBasic = instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

                            this.put("jkrxm", collectionPersonalBusinessDetails.getPerson().getXingMing());
                            this.put("jkrzjhm", collectionPersonalBusinessDetails.getPerson().getZjhm());

                        }}).extension(new IBaseDAO.CriteriaExtension() {
                            @Override
                            public void extend(Criteria criteria) {
                                criteria.add(Restrictions.isNotNull("loanContract"));
                                criteria.add(Restrictions.isNotNull("personalAccount"));
                            }
                        }).getList(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }

                        });
                        this.setGJJDKCS(list_loanHousingPersonInformationBasic.size() + ""/*公积金贷款次数*/);
                        this.setJQDK("1");
                        for (CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic : list_loanHousingPersonInformationBasic) {

                            if ("1".equals(this.getJQDK()) && !LoanAccountType.已结清.getCode().equals(loanHousingPersonInformationBasic.getDkzhzt())) {

                                this.setJQDK("0");
                            }
                        }
                        this.setSFCZPTPD(collectionAllochthounousTransferVice.getSFCZPTPD() ? "1" : "0"/*是否存在骗提骗贷*/);
                        this.setYWLXDH(collectionAllochthounousTransferVice.getYWLXDH()/*业务联系电话*/);
                        this.setLXDSCRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getLXDSCRQ(), format)/*联系单生成日期*/);
                    }
                } : new TransferOutDetailsModleAccountMsg() {
                    {
                        this.setZJLX(collectionAllochthounousTransferVice.getZJLX()/*证件类型*/);
                        this.setZJHM(collectionAllochthounousTransferVice.getZJHM()/*证件号码*/);
                        this.setZGXM(collectionAllochthounousTransferVice.getZGXM()/*职工姓名*/);
                        this.setYZHBJYE(collectionAllochthounousTransferVice.getYZHBJJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*原账户本金余额*/);
                        this.setBNDLX(collectionAllochthounousTransferVice.getBNDLX().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*本年度利息*/);
                        this.setZYJE(collectionAllochthounousTransferVice.getZYJE().setScale(2, BigDecimal.ROUND_HALF_UP) + ""/*转移金额*/);
                        this.setKHRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getKHRQ(), format)/*开户日期*/);
                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionAllochthounousTransferVice.getJZNY(), formatNY)/*缴至年月*/);
                        this.setLXJC(collectionAllochthounousTransferVice.getLXJC() ? "1" : "0"/*连续缴存*/);
                        this.setGJJDKCS(collectionAllochthounousTransferVice.getGJJDKCS() + ""/*公积金贷款次数*/);
                        this.setJQDK(collectionAllochthounousTransferVice.getJQDK() ? "1" : "0"/*结清贷款*/);
                        this.setSFCZPTPD(collectionAllochthounousTransferVice.getSFCZPTPD() ? "1" : "0"/*是否存在骗提骗贷*/);
                        this.setYWLXDH(collectionAllochthounousTransferVice.getYWLXDH()/*业务联系电话*/);
                        this.setLXDSCRQ(DateUtil.date2Str(collectionAllochthounousTransferVice.getLXDSCRQ(), format)/*联系单生成日期*/);
                    }
                }/**/);
                this.setTurnOutMechanismMsg(new TransferOutDetailsModleTurnOutMechanismMsg() {
                    {
                        this.setZCJGBH(collectionAllochthounousTransferVice.getZCJGBH()/*转出机构编号*/);
                        this.setZCGJJZXMC(collectionAllochthounousTransferVice.getZCGJJZXMC()/*转出中心名称*/);
                        this.setYGRZFGJJZH(collectionAllochthounousTransferVice.getYGRZFGJJZH()/*原个人住房公积金账号*/);
                        this.setYDWMC(collectionAllochthounousTransferVice.getYGZDWMC()/*原工作单位名称*/);
                        this.setFKYHMC(collectionAllochthounousTransferVice.getFKYHMC()/*付款银行名称*/);
                        this.setFKZH(collectionAllochthounousTransferVice.getFKZH()/*付款账号*/);
                        this.setFKHM(collectionAllochthounousTransferVice.getFKHM()/*付款户名*/);
                    }
                }/**/);
                this.setTurnIntoMechanismMsg(new TransferOutDetailsModleTurnIntoMechanismMsg() {
                    {
                        this.setZRJGBH(collectionAllochthounousTransferVice.getZRJGBH()/*转入机构编号*/);
                        this.setZRGJJZXMC(collectionAllochthounousTransferVice.getZRGJJZXMC()/*转入中心名称*/);
                        this.setXGRZFGJJZH(collectionAllochthounousTransferVice.getXZFGJJZH()/*现个人住房公积金账号*/);
                        this.setXDWMC(collectionAllochthounousTransferVice.getXGZDWMC()/*现工作单位名称*/);
                        this.setZRGJJZXZJZHSSYHMC(collectionAllochthounousTransferVice.getZRZJZHYHMC()/*转入公积金中心资金账户所属银行名称*/);
                        this.setZRGJJZXZJZH(collectionAllochthounousTransferVice.getZRZJZH()/*转入公积金中心资金账号*/);
                        this.setZRGJJZXZJZHHM(collectionAllochthounousTransferVice.getZRZJZHHM()/*转入公积金中心资金账号户名*/);
                        this.setLXDHHCZ(collectionAllochthounousTransferVice.getLXDHHCZ()/*联系电话/传真*/);
                    }
                }/**/);
                this.setBLZL(collectionAllochthounousTransferVice.getBLZL()/*资料信息*/);

            }
        };
    }

    //TODO
    @Override
    public CommonResponses transferOutConfirmation(TokenContext tokenContext, String YWLSH, TransferOutConfirmationModle body) {

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        //region //必要数据查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();
        //endregion

        //region //修改状态
        StateMachineUtils.updateState(this.stateMachineService, Events.通过.getEvent(), new TaskEntity() {{

            this.setStatus(collectionPersonalBusinessDetails.getExtension().getStep() == null ? "初始状态" : collectionPersonalBusinessDetails.getExtension().getStep());
            this.setTaskId(collectionPersonalBusinessDetails.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_转出个人接续.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(tokenContext.getUserInfo().getCZY());
            this.setWorkstation(collectionPersonalBusinessDetails.getExtension().getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null) {
                    return;
                }

                collectionPersonalBusinessDetails.getExtension().setStep(next);
                collectionPersonalBusinessDetails.getExtension().setCzy(tokenContext.getUserInfo().getCZY());
                collectionPersonalBusinessDetails.getExtension().setSlsj(new Date());
                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
            }
        });

        //endregion

        //region //记录流程
        CCollectionAllochthounousTransferProcess collectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

        collectionAllochthounousTransferProcess.setCaoZuo("转出方确认接收联系函");
        collectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
        collectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
        collectionAllochthounousTransferProcess.setCZSJ(new Date());
        collectionAllochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
        collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.联系函确认接收.name());
        collectionAllochthounousTransferProcess.setTransferVice(collectionAllochthounousTransferVice);
        collectionAllochthounousTransferVice.getProcesses().add(collectionAllochthounousTransferProcess);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //转移接续平台--确认
        try {

            SingleTransInApplOut transInApplCancelOut = iTransfer.sendMsg(ResUtils.noneAdductionValue(SingleTransInApplIn.class, new SingleTransInApplIn() {{


                this.setCenterHeadIn(new CenterHeadIn(YWLSH, bankCenterInfoDAO.getCenterNodeByNum(collectionAllochthounousTransferVice.getZRJGBH()), tokenContext.getUserInfo().getCZY()));
                this.setTxFunc("1");
                this.setTranRstCode("0");
                this.setConNum(collectionAllochthounousTransferVice.getLXHBH());
                this.setEmpName(collectionAllochthounousTransferVice.getZGXM(/* 职工姓名(required) */));
                this.setDocType(collectionAllochthounousTransferVice.getZJLX(/* 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他 */));
                this.setIdNum(collectionAllochthounousTransferVice.getZJHM(/* 职工证件号码(required) */));
                this.setTranRefAcctNo(collectionAllochthounousTransferVice.getZRZJZH(/* 转入公积金中心资金账号(required), 转入公积金中心资金接收账户的账号 */));
                this.setTranRefAcctName(collectionAllochthounousTransferVice.getZRZJZHHM(/* 转入公积金中心资金账号户名(required), 转入公积金中心资金接收账户户名 */));
                this.setTranBank(collectionAllochthounousTransferVice.getZRZJZHYHMC(/* 公积金中心委托业务办理银行, 公积金中心委托银行办理异地转移接续业务时填写, 参见银行代码表 */));
                this.setTrenCenInfo(collectionAllochthounousTransferVice.getYWLXDH(/* 转入公积金中心联系方式(required) */));
                this.setGenDate(DateUtil.date2Str(collectionAllochthounousTransferVice.getLXDSCRQ(), "yyyyMMdd"));
                this.setTranOutUnitNo(collectionAllochthounousTransferVice.getZCJGBH(/*转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码*/));
                this.setTranCenName(collectionAllochthounousTransferVice.getZCGJJZXMC(/* 转出公积金中心名称(required)*/));
                this.setSocUnitName(collectionAllochthounousTransferVice.getYGZDWMC(/*原工作单位名称(required)*/));
                this.setSocRefAcctNo(collectionAllochthounousTransferVice.getYGRZFGJJZH(/*原住房公积金账号(required)*/));
                this.setTranRstMsg(body.getReviewInfo().getYYYJ());
            }}));

            if ("0".equals(transInApplCancelOut.getCenterHeadOut().getTxStatus())) {

                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.联系函确认接收.getCode());

                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
                return new CommonResponses() {
                    {
                        this.setStatus(YWLSH/*操作码*/);
                        this.setMsg("success"/*信息描述*/);
                    }
                };
            } else {
                throw new ErrorException(ReturnEnumeration.Business_FAILED, transInApplCancelOut.getCenterHeadOut().getRtnMessage());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        //endregion


    }

    //TODO
    @Override
    public CommonResponses transferOutPost(TokenContext tokenContext, final String type, final TransferOutPostModle body) {

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        //region //必要字段查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", body.getYWLSH());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

        //endregion

        //region //字段填充
        collectionAllochthounousTransferVice.setZJLX(body.getAccountMsg().getZJLX()/*证件类型*/);
        collectionAllochthounousTransferVice.setZJHM(body.getAccountMsg().getZJHM()/*证件号码*/);
        collectionAllochthounousTransferVice.setZGXM(body.getAccountMsg().getZGXM()/*职工姓名*/);
        collectionAllochthounousTransferVice.setYZHBJJE(StringUtil.safeBigDecimal(body.getAccountMsg().getYZHBJYE())/*原账户本金余额*/);
        collectionAllochthounousTransferVice.setBNDLX(StringUtil.safeBigDecimal(body.getAccountMsg().getBNDLX())/*本年度利息*/);
        collectionAllochthounousTransferVice.setZYJE(StringUtil.safeBigDecimal(body.getAccountMsg().getZYJE())/*转移金额*/);
        collectionAllochthounousTransferVice.setKHRQ(DateUtil.safeStr2Date(format, body.getAccountMsg().getKHRQ())/*开户日期*/);
        collectionAllochthounousTransferVice.setJZNY(DateUtil.safeStr2DBDate(formatNY, body.getAccountMsg().getJZNY(), DateUtil.dbformatYear_Month)/*缴至年月*/);
        collectionAllochthounousTransferVice.setLXJC(StringUtil.safeBoolean(body.getAccountMsg().getLXJC())/*连续缴存*/);
        collectionAllochthounousTransferVice.setGJJDKCS(StringUtil.safeBigDecimal(body.getAccountMsg().getGJJDKCS()).intValue()/*公积金贷款次数*/);
        collectionAllochthounousTransferVice.setJQDK(StringUtil.safeBoolean(body.getAccountMsg().getJQDK())/*结清贷款*/);
        collectionAllochthounousTransferVice.setSFCZPTPD(StringUtil.safeBoolean(body.getAccountMsg().getSFCZPTPD())/*是否存在骗提骗贷*/);
        collectionAllochthounousTransferVice.setYWLXDH(body.getAccountMsg().getYWBLLXDH()/*业务联系电话*/);
        collectionAllochthounousTransferVice.setFKXX(body.getAccountMsg().getFKXX()/*反馈信息*/);

        collectionAllochthounousTransferVice.setFKYHMC(body.getTurnOutFundsAccountMsg().getFKYH()/*付款银行*/);
        collectionAllochthounousTransferVice.setFKZH(body.getTurnOutFundsAccountMsg().getFKZH()/*付款账号*/);
        collectionAllochthounousTransferVice.setFKHM(body.getTurnOutFundsAccountMsg().getYHHM()/*付款户名*/);

        collectionPersonalBusinessDetails.getExtension().setCzy(tokenContext.getUserInfo().getCZY());
        this.isCollectionAllochthounousTransferViceAvailable(collectionAllochthounousTransferVice);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //修改状态
        StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.通过.getEvent());

            this.put("1", Events.提交.getEvent());

        }}.get(type), new TaskEntity() {{

            this.setStatus(collectionPersonalBusinessDetails.getExtension().getStep() == null ? "初始状态" : collectionPersonalBusinessDetails.getExtension().getStep());
            this.setTaskId(collectionPersonalBusinessDetails.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_转出个人接续.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(collectionPersonalBusinessDetails.getExtension().getCzy());
            this.setWorkstation(collectionPersonalBusinessDetails.getExtension().getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null) {
                    return;
                }

                collectionPersonalBusinessDetails.getExtension().setStep(next);

                if (StringUtil.isIntoReview(next, null)) {
                    collectionPersonalBusinessDetails.getExtension().setDdsj(new Date());
                }

                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
            }
        });
        //endregion

        //region //历史记录
        this.saveAuditHistory.saveNormalBusiness(collectionPersonalBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.外部转出.getName(), "修改");
        //endregion

        return new CommonResponses() {
            {
                this.setStatus(body.getYWLSH()/*操作码*/);
                this.setMsg("success"/*信息描述*/);
            }
        };
    }

    //TODO
    @Override
    public CommonResponses transferOutPut(TokenContext tokenContext, final String type, final TransferOutPutModle body) {

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        //region //必要字段查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", body.getYWLSH());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

        //endregion

        //region//权限检查
        if (!tokenContext.getUserInfo().getCZY().equals(collectionPersonalBusinessDetails.getExtension().getCzy())) {

            throw new ErrorException(ReturnEnumeration.Permission_Denied,"该业务("+body.getYWLSH()+")不是您受理的,不能进行修改操作");
        }
        //endregion

        //region //字段填充
        collectionAllochthounousTransferVice.setZJLX(body.getAccountMsg().getZJLX()/*证件类型*/);
        collectionAllochthounousTransferVice.setZJHM(body.getAccountMsg().getZJHM()/*证件号码*/);
        collectionAllochthounousTransferVice.setZGXM(body.getAccountMsg().getZGXM()/*职工姓名*/);
        collectionAllochthounousTransferVice.setYZHBJJE(StringUtil.safeBigDecimal(body.getAccountMsg().getYZHBJYE())/*原账户本金余额*/);
        collectionAllochthounousTransferVice.setBNDLX(StringUtil.safeBigDecimal(body.getAccountMsg().getBNDLX())/*本年度利息*/);
        collectionAllochthounousTransferVice.setZYJE(StringUtil.safeBigDecimal(body.getAccountMsg().getZYJE())/*转移金额*/);
        collectionAllochthounousTransferVice.setKHRQ(DateUtil.safeStr2Date(format, body.getAccountMsg().getKHRQ())/*开户日期*/);
        collectionAllochthounousTransferVice.setJZNY(DateUtil.safeStr2DBDate(formatNY, body.getAccountMsg().getJZNY(), DateUtil.dbformatYear_Month)/*缴至年月*/);
        collectionAllochthounousTransferVice.setLXJC(StringUtil.safeBoolean(body.getAccountMsg().getLXJC())/*连续缴存*/);
        collectionAllochthounousTransferVice.setGJJDKCS(StringUtil.safeBigDecimal(body.getAccountMsg().getGJJDKCS()).intValue()/*公积金贷款次数*/);
        collectionAllochthounousTransferVice.setJQDK(StringUtil.safeBoolean(body.getAccountMsg().getJQDK())/*结清贷款*/);
        collectionAllochthounousTransferVice.setSFCZPTPD(StringUtil.safeBoolean(body.getAccountMsg().getSFCZPTPD())/*是否存在骗提骗贷*/);
        collectionAllochthounousTransferVice.setYWLXDH(body.getAccountMsg().getYWBLLXDH()/*业务联系电话*/);
        collectionAllochthounousTransferVice.setFKXX(body.getAccountMsg().getFKXX()/*反馈信息*/);

        collectionAllochthounousTransferVice.setFKYHMC(body.getTurnOutFundsAccountMsg().getFKYH()/*付款银行*/);
        collectionAllochthounousTransferVice.setFKZH(body.getTurnOutFundsAccountMsg().getFKZH()/*付款账号*/);
        collectionAllochthounousTransferVice.setFKHM(body.getTurnOutFundsAccountMsg().getFKHM()/*付款户名*/);

        this.isCollectionAllochthounousTransferViceAvailable(collectionAllochthounousTransferVice);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //修改状态
        StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.保存.getEvent());

            this.put("1", Events.通过.getEvent());

        }}.get(type), new TaskEntity() {{

            this.setStatus(collectionPersonalBusinessDetails.getExtension().getStep() == null ? "初始状态" : collectionPersonalBusinessDetails.getExtension().getStep());
            this.setTaskId(collectionPersonalBusinessDetails.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_转出个人接续.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(collectionPersonalBusinessDetails.getExtension().getCzy());
            this.setWorkstation(collectionPersonalBusinessDetails.getExtension().getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null) {
                    return;
                }

                collectionPersonalBusinessDetails.getExtension().setStep(next);

                if (StringUtil.isIntoReview(next, null)) {
                    collectionPersonalBusinessDetails.getExtension().setDdsj(new Date());
                }

                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
            }
        });

        //endregion

        //region //历史记录
        this.saveAuditHistory.saveNormalBusiness(collectionPersonalBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.外部转出.getName(), "修改");
        //endregion

        return new CommonResponses() {
            {
                this.setStatus(body.getYWLSH()/*操作码*/);
                this.setMsg("success"/*信息描述*/);
            }
        };
    }

    //TODO
    @Override
    public CommonResponses transferOutReject(TokenContext tokenContext, String YWLSH, TransferOutRejectModle body) {

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        //region //必要数据查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

        //endregion

        //region //修改状态
        if (!AllochthonousStatus.联系函确认接收.getCode().equals(collectionAllochthounousTransferVice.getLXHZT())) {

            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "当前联系函状态无法驳回");
        }
        StateMachineUtils.updateState(this.stateMachineService, Events.不通过.getEvent(), new TaskEntity() {{

            this.setStatus(collectionPersonalBusinessDetails.getExtension().getStep() == null ? "初始状态" : collectionPersonalBusinessDetails.getExtension().getStep());
            this.setTaskId(collectionPersonalBusinessDetails.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_转出个人接续.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(collectionPersonalBusinessDetails.getExtension().getCzy());
            this.setWorkstation(collectionPersonalBusinessDetails.getExtension().getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null) {
                    return;
                }

                collectionPersonalBusinessDetails.getExtension().setStep(next);
                collectionPersonalBusinessDetails.getAllochthounousTransferVice().setFKXX(body.getReviewInfo().getYYYJ());
                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
            }
        });

        //endregion

        //region //记录流程
        CCollectionAllochthounousTransferProcess collectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

        collectionAllochthounousTransferProcess.setCaoZuo("转出方驳回联系函");
        collectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
        collectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
        collectionAllochthounousTransferProcess.setCZSJ(new Date());
        collectionAllochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
        collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.转出审核不通过.name());
        collectionAllochthounousTransferProcess.setTransferVice(collectionAllochthounousTransferVice);
        collectionAllochthounousTransferVice.getProcesses().add(collectionAllochthounousTransferProcess);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //转移接续平台--驳回
        try {

            SingleTransOutInfoOut transInApplCancelOut = iTransfer.sendMsg(ResUtils.noneAdductionValue(SingleTransOutInfoIn.class, new SingleTransOutInfoIn() {{


                this.setCenterHeadIn(new CenterHeadIn(YWLSH, bankCenterInfoDAO.getCenterNodeByNum(collectionAllochthounousTransferVice.getZRJGBH()), tokenContext.getUserInfo().getCZY()));
                this.setOrConNum(collectionAllochthounousTransferVice.getLXHBH(/* 原联系函编号(required), 要求使用转入申请的联系函编号*/));
                /**
                 * 交易类型(required)
                 * 0-新增信息转出
                 * 1-修改转出信息
                 * 2-转入确认办结
                 * 0新增是用于新增的转移接续【转出发起】
                 * 1修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】
                 * 2转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】
                 */
                this.setTxFunc("0");


                this.setEmpName(collectionAllochthounousTransferVice.getZGXM(/* 职工姓名(required) */));
                this.setDocType(collectionAllochthounousTransferVice.getZJLX(/* 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他 */));
                this.setIdNum(collectionAllochthounousTransferVice.getZJHM(/* 职工证件号码(required) */));
                this.setTranAmt(null/* 转移金额(required), 转移金额=本金金额+利息金额 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */);
                this.setOrAcctAmt(null/* 本金金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */);
                this.setYinter(null/* 利息金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/);
                if (collectionAllochthounousTransferVice.getGrywmx().getPerson() != null) {
                    this.setOAcctDate(DateUtil.date2Str(collectionAllochthounousTransferVice.getKHRQ(/* 开户日期(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/), "yyyyMM"));
                    this.setPayEndDate(collectionAllochthounousTransferVice.getJZNY(/* 缴至年月(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/));
                    Integer consecutiveDepositMonths = new BigDecimal(unitRemittance.getConsecutiveDepositMonths(collectionPersonalBusinessDetails.getPerson().getGrzh()).getId()).intValue();
                    this.setSixMconFlag(consecutiveDepositMonths <= 1 ? "2" : "1"/*缴至月份之前6个月是否连续缴存(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/);

                    List<CLoanHousingPersonInformationBasic> list_loanHousingPersonInformationBasic = instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

                        this.put("jkrxm", collectionPersonalBusinessDetails.getPerson().getXingMing());
                        this.put("jkrzjhm", collectionPersonalBusinessDetails.getPerson().getZjhm());

                    }}).extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.add(Restrictions.isNotNull("loanContract"));
                            criteria.add(Restrictions.isNotNull("personalAccount"));
                        }
                    }).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }

                    });

                    this.setLoanNum(list_loanHousingPersonInformationBasic.size() + ""/*在转出地使用住房公积金贷款次数(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/);

                    this.setLoanEndFlag("1");

                    for (CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic : list_loanHousingPersonInformationBasic) {

                        if ("1".equals(this.getLoanEndFlag()) && !LoanAccountType.已结清.getCode().equals(loanHousingPersonInformationBasic.getDkzhzt())) {

                            this.setLoanEndFlag("2");
                        }
                    }
                    this.setLoanEndFlag(collectionAllochthounousTransferVice.getJQDK(/*是否有未结清的公积金贷款(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) ? "1" : "0");
                    this.setFrLoanFlag("2"/*是否存在骗提骗贷行为, 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/);
                }

                this.setTranRstCode("1"/*反馈信息代码, 0-已完成 1-失败 2-处理中 当交易类型为2-反馈结果时, 该项必填, 其他可为空*/);

                this.setTranRstMsg(collectionAllochthounousTransferVice.getFKXX(/* 反馈信息*/));
                this.setContPhone(collectionAllochthounousTransferVice.getYWLXDH(/*业务办理联系电话(required), 固定电话：区号-电话号码 手机：11位号码*/));

                this.setBak1(null/*备用*/);

                this.setBak2(null/*备用*/);
            }}));

            if ("0".equals(transInApplCancelOut.getCenterHeadOut().getTxStatus())) {

                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.转出审核不通过.getCode());

                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
                return new CommonResponses() {
                    {
                        this.setStatus("200"/*操作码*/);
                        this.setMsg("success"/*信息描述*/);
                    }
                };
            } else {
                throw new ErrorException(ReturnEnumeration.Business_FAILED, transInApplCancelOut.getCenterHeadOut().getRtnMessage());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        //endregion

    }

    //TODO
    @Override
    public void outFinal(TokenContext tokenContext, String YWLSH, String YYYJ) {

        //region //必要数据查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        if (collectionPersonalBusinessDetails == null || collectionPersonalBusinessDetails.getExtension() == null || collectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");

        }
        //发消息到转入方
        doSendMesg(tokenContext, collectionPersonalBusinessDetails, YYYJ);

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

        StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();

        if (commonPerson == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
        }

        StCommonUnit commonUnit = commonPerson.getUnit();

        if (commonUnit == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息");
        }

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

        CAccountNetwork YWWD = DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", tokenContext.getUserInfo().getYWWD());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        //region //转出业务更新
        collectionPersonalBusinessDetails.setFse(BigDecimal.ZERO);
        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.外部转出.getCode());
        collectionPersonalBusinessDetails.setFslxe(this.getInterest(collectionPersonalBusinessDetails));
        collectionPersonalBusinessDetails.setGrzh(commonPerson.getGrzh());
        collectionPersonalBusinessDetailsExtension.setDqye(BigDecimal.ZERO);
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.转账中.getName());

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        //endregion

        //region //单位业务
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();

        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension_current = new CCollectionUnitBusinessDetailsExtension();

        collectionUnitBusinessDetails.setDwzh(commonUnit.getDwzh());
        collectionUnitBusinessDetails.setUnit(commonUnit);
        collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.外部转出.getCode());
        collectionUnitBusinessDetails.setFse(collectionPersonalBusinessDetails.getFse());

        collectionUnitBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
        collectionUnitBusinessDetails.setJzrq(new Date());


        collectionUnitBusinessDetailsExtension_current.setBjsj(new Date());
        collectionUnitBusinessDetailsExtension_current.setCzmc(CollectionBusinessType.外部转出.getCode());
        collectionUnitBusinessDetailsExtension_current.setStep(CollectionBusinessStatus.转账中.getName());
        collectionUnitBusinessDetailsExtension_current.setBeizhu(collectionPersonalBusinessDetails.getYwlsh());
        collectionUnitBusinessDetailsExtension_current.setSlsj(new Date());
        collectionUnitBusinessDetailsExtension_current.setCzy(tokenContext.getUserInfo().getCZY());
        collectionUnitBusinessDetailsExtension_current.setYwwd(YWWD);

        commonUnit.getCollectionUnitAccount().setDwzhye(commonUnit.getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails.getFse()));

        collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension_current);

        DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        //endregion

        //region //记录流程
        CCollectionAllochthounousTransferProcess collectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

        collectionAllochthounousTransferProcess.setCaoZuo("转出方账户信息复核通过");
        collectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
        collectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
        collectionAllochthounousTransferProcess.setCZSJ(new Date());
        //collectionAllochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
        collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.账户信息复核通过.name());
        collectionAllochthounousTransferProcess.setTransferVice(collectionAllochthounousTransferVice);
        collectionAllochthounousTransferVice.getProcesses().add(collectionAllochthounousTransferProcess);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        //endregion

        //region //结算平台
        StCollectionPersonalAccount collectionPersonalAccount = commonPerson.getCollectionPersonalAccount();
        BigDecimal GRZHYE = BigDecimal.ZERO.add(collectionPersonalAccount.getGrzhye());

        ArrayList<CenterAccountInfo> infos = new ArrayList<>();

        //个人存款账户开户银行名称
        String indiBankName = collectionAllochthounousTransferVice.getZRZJZHYHMC();

        //付方账户，根据个人存款账户所属的受托银行查找公积金中心对应银行的公积金账号
        String deAcctNo = "";
        //付方户名
        String deAcctName = "";
        String indiBankCode = "";

        infos = this.accountManageService.getSpecialAccount(indiBankName);

        boolean hasCKZH = false; //是否有存款专户,没有就默认使用建行存款专户

        for (CenterAccountInfo info : infos) {
            if ("01".equals(info.getZHXZ())) {
                indiBankCode = info.getCode();
                deAcctNo = info.getYHZHHM();
                deAcctName = info.getYHZHMC();
                hasCKZH = true;
            }
        }
        if (!hasCKZH) {
            StSettlementSpecialBankAccount settlementSpecialBankAccount = DAOBuilder.instance(settlementSpecialBankAccountDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("yhdm", "105");
                this.put("zhxz", "01");
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            if (settlementSpecialBankAccount == null) {

                throw new ErrorException(ReturnEnumeration.Business_FAILED,"未找到银行专户");

            } else {

                deAcctName = settlementSpecialBankAccount.getYhzhmc();
                deAcctNo = settlementSpecialBankAccount.getYhzhhm();

            }
        }
        CenterAccountInfo centerAccountInfo = this.settlementSpecialBankAccountManageService.getSpecialAccountByZHHM(deAcctNo);

        SinglePaymentOut singlePaymentOut = null;

        boolean succeed = false;

        SettlementHandler settlementHandler = SettlementHandler.instance(iBankService);

        try {

            String finalDeAcctNo = deAcctNo;
            String finalDeAcctName = deAcctName;
            String finalIndiBankCode = indiBankCode;
            SinglePaymentIn singlePaymentIn = ResUtils.noneAdductionValue(SinglePaymentIn.class, new SinglePaymentIn() {{

                this.setCenterHeadIn(new CenterHeadIn(YWLSH, centerAccountInfo.getNode(), tokenContext.getUserInfo().getCZY()/*CenterHeadIn(required)*/) {{

//          String[] array = DateUtil.getDatetime();
//
//          this.setSendDate(array[0]/*发送日期(required)*/);
//
//          this.setSendTime(array[1]/*发送方时间(required)*/);
//
//          this.setSendSeqNo(YWLSH/*发送方流水号(required)*/);
//
//          this.setTxUnitNo("0"/*交易机构号(required)*/);//*
//
//          this.setSendNode("0"/*发送方节点号(required)*/);//*
//
//          this.setTxCode("0"/*交易代码(required)*/);//*
//
//          this.setCustNo(centerAccountInfo.getKHBH()); //客户编号
//
//          this.setReceiveNode(centerAccountInfo.getNode()/*接收方节点号(required) 根据银行名称查询*/);//*
//
//          this.setOperNo("0"/*操作员编号(required) 待定*/);//*

                }});
                this.setSettleType(StringUtil.notEmpty(finalIndiBankCode) ? SettleTypeEnums.同行.getCode() : SettleTypeEnums.跨行实时.getCode()/*结算模式(required) 1：本行 2：跨行-实时 3：跨行-非实时*/);//*
                this.setBusType(BusTypeEnums.外部转出.getCode()/*业务类型(required) 待定*/);//*
                this.setDeAcctNo(finalDeAcctNo/*付方账号(required)*/);
                this.setDeAcctName(finalDeAcctName/*付方户名(required)*/);
                this.setDeAcctClass(AcctClassEnums.对公.getCode()/*付方账户类别(required)*/);
                this.setCapAmt(GRZHYE.add(collectionPersonalBusinessDetails.getFslxe())/*本金发生额(required)*/);
                this.setCrAcctNo(collectionAllochthounousTransferVice.getZRZJZH()/*收方账号(required)*/);
                this.setCrAcctName(collectionAllochthounousTransferVice.getZRZJZHHM()/*收方户名(required)*/);
                this.setCrAcctClass(AcctClassEnums.对公.getCode()/*收方账户类别(required)*/);
                this.setCrBankClass(StringUtil.notEmpty(finalIndiBankCode) ? "0" : "1"/*收方账户行别(required) 0：本行 1：跨行*/);//(
                this.setAmt(GRZHYE.add(collectionPersonalBusinessDetails.getFslxe())/*金额(required)*/);
                this.setIntAmt(BigDecimal.ZERO);
                this.setRefSeqNo1(YWLSH/*业务明细流水号1(required)*/);
                this.setSummary(CollectionBusinessType.外部转出.getName()/*摘要(required)*/);
                this.setRemark(CollectionBusinessType.外部转出.getName()/*备注(required)*/);
                //设置跨行必输
                if (!SettleTypeEnums.同行.getCode().equals(this.getSettleType())) {
                    this.setCrBankName(collectionPersonalBusinessDetails.getAllochthounousTransferVice().getZRZJZHYHMC());//收方行名
                    this.setCrChgNo(iBankInfoService.getBankInfo(indiBankName).getChgno());//收方联行号
                }
                if ("104".equals(centerAccountInfo.getCode())) {
                    this.setCrChgNo("104100000004");
                }
            }});
            if (!this.iBankService.checkYWLSH(YWLSH)) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process,"请勿重复操作,请刷新页面确认");
            }
            settlementHandler.setCenterHeadIn(singlePaymentIn.getCenterHeadIn())
                    .setSuccess(new SettlementHandler.Success() {
                        @Override
                        public void handle() { System.out.println("外部转出：结算平台请求成功"); }
                    }).setFail(new SettlementHandler.Fail() {
                        @Override
                        public void handle(String sbyy) { updateSBYY(YWLSH, sbyy); }
                    }).setManualProcess(new SettlementHandler.ManualProcess() {
                        @Override
                        public void handle() { updateSBYY(YWLSH, "状态未知,需人工线下查询"); }
                    });

            singlePaymentOut = this.iBankService.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();

            succeed = singlePaymentOut.getCenterHeadOut().getTxStatus().equals("0");
        } catch (Exception e) {

            succeed = false;

            settlementHandler.setSendException(new SettlementHandler.SendException() {
                @Override
                public void handle(String sbyy) { updateSBYY(YWLSH, sbyy); }

            }).handleException(e);

            throw new ErrorException(e);
        }


        //endregion

    }


    //TODO
    private void isCollectionAllochthounousTransferViceAvailable(CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice) {


    }

    private void updateSBYY(String YWLSH, String sbyy) {

        //region //必要数据查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode()));

            }

        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });
        //endregion

        //region //更新
        collectionPersonalBusinessDetails.getExtension().setSbyy(sbyy);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });
        //endregion
    }

    private BigDecimal getInterest(StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails) {
        collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().setUpdated_at(new Date());

        Date qsDate = null;
        String KHRQ = DateUtil.date2Str(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getKhrq(), format);//开户日期

        String currentDate = DateUtil.date2Str(new Date(), format);//当前时间
        String date1 = DateUtil.getYear(DateUtil.date2Str(new Date(), format)) + "-06-30";//当年结息日期
        String date2 = DateUtil.getYear(DateUtil.date2Str(new Date(), format)) - 1 + "-06-30";//前一年结息日期

        Date currentJxDate = null;
        Date beforeJxDate = null;
        try {
            currentJxDate = DateUtil.str2Date(format, date1);
            beforeJxDate = DateUtil.str2Date(format, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //若开户日期大于本年-06-30，则传开户日期
        if (DateUtil.compare_date(KHRQ, date1)) {
            try {
                qsDate = DateUtil.str2Date(format, KHRQ);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {//比较当前时间和当年6-30
            if (DateUtil.compare_date(currentDate, date1)) {
                qsDate = currentJxDate;
            } else {
                qsDate = beforeJxDate;
            }
        }
        Date finalQsDate = qsDate;
        return new BigDecimal(calculateInterest.calculateInterestByGrzh(collectionPersonalBusinessDetails.getPerson().getGrzh(), finalQsDate, new Date()).getId());//销户提取发生利息额

    }

    private void doSendMesg(TokenContext tokenContext, StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails, String YYYJ) {
        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice();
        try {

            SingleTransOutInfoOut transInApplCancelOut = iTransfer.sendMsg(ResUtils.noneAdductionValue(SingleTransOutInfoIn.class, new SingleTransOutInfoIn() {{


                this.setCenterHeadIn(new CenterHeadIn(stCollectionPersonalBusinessDetails.getYwlsh(), bankCenterInfoDAO.getCenterNodeByNum(collectionAllochthounousTransferVice.getZRJGBH()), tokenContext.getUserInfo().getCZY()));
                this.setOrConNum(collectionAllochthounousTransferVice.getLXHBH(/* 原联系函编号(required), 要求使用转入申请的联系函编号*/));
                /**
                 * 交易类型(required)
                 * 0-新增信息转出
                 * 1-修改转出信息
                 * 2-转入确认办结
                 * 0新增是用于新增的转移接续【转出发起】
                 * 1修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】
                 * 2转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】
                 */
                this.setTxFunc("0");

                this.setEmpName(collectionAllochthounousTransferVice.getZGXM(/* 职工姓名(required) */));
                this.setDocType(collectionAllochthounousTransferVice.getZJLX(/* 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他 */));
                this.setIdNum(collectionAllochthounousTransferVice.getZJHM(/* 职工证件号码(required) */));
                this.setTranAmt(collectionAllochthounousTransferVice.getZYJE( /* 转移金额(required), 转移金额=本金金额+利息金额 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */) + "");
                this.setOrAcctAmt(collectionAllochthounousTransferVice.getYZHBJJE(/* 本金金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */) + "");
                this.setYinter(collectionAllochthounousTransferVice.getBNDLX(/* 利息金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) + "");
                this.setOAcctDate(DateUtil.date2Str(collectionAllochthounousTransferVice.getKHRQ(/* 开户日期(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/), "yyyyMM"));
                this.setPayEndDate(collectionAllochthounousTransferVice.getJZNY(/* 缴至年月(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/));
                this.setSixMconFlag(collectionAllochthounousTransferVice.getLXJC(/*缴至月份之前6个月是否连续缴存(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) ? "1" : "0");
                this.setLoanNum(collectionAllochthounousTransferVice.getGJJDKCS(/*在转出地使用住房公积金贷款次数(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) + "");
                this.setLoanEndFlag(collectionAllochthounousTransferVice.getJQDK(/*是否有未结清的公积金贷款(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) ? "1" : "0");
                this.setFrLoanFlag(collectionAllochthounousTransferVice.getSFCZPTPD(/*是否存在骗提骗贷行为, 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/) ? "1" : "0");

                this.setTranRstCode(null/*反馈信息代码, 0-已完成 1-失败 2-处理中 当交易类型为2-反馈结果时, 该项必填, 其他可为空*/);

                this.setTranRstMsg(collectionAllochthounousTransferVice.getFKXX(/* 反馈信息*/));
                this.setTranRstMsg(YYYJ);
                this.setContPhone(collectionAllochthounousTransferVice.getYWLXDH(/*业务办理联系电话(required), 固定电话：区号-电话号码 手机：11位号码*/));

                this.setBak1(null/*备用*/);

                this.setBak2(null/*备用*/);
            }}));

            if ("0".equals(transInApplCancelOut.getCenterHeadOut().getTxStatus())) {

                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.账户信息复核通过.getCode());

                DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });
            } else {
                throw new ErrorException(ReturnEnumeration.Business_FAILED, transInApplCancelOut.getCenterHeadOut().getRtnMessage());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


}
