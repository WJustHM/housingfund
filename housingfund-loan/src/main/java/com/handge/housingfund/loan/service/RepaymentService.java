package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.enumeration.WithDrawalReason;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.*;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.enums.RepaymentMethod;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Funnyboy on 2017/8/9.
 */
@SuppressWarnings("Duplicates")
@Component
public class RepaymentService implements IRepaymentService {

    private static Object lock = new Object();
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    IStHousingOverdueRegistrationDAO housingOverdueRegistrationDAO;
    @Autowired
    ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    IStHousingPersonalAccountDAO housingfundPersonalAccount;
    @Autowired
    IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icloanHousingPersonInformationBasicDAO;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    IUploadImagesService iUploadImagesService;
    @Autowired
    ICommCheckMethod icommCheckMethod;
    @Autowired
    private IStCommonUnitDAO common_unit;
    @Autowired
    private ISMSCommon ismsCommon;
    @Autowired
    @Qualifier("housingCompanyImpl")
    private IHousingCompany iHousingCompany;
    @Autowired
    @Qualifier("housingCompanyAlter")
    private IHousingCompanyAlter ihousingCompanyAlter;
    @Autowired
    @Qualifier("estateProjectImpl")
    private IEstateProject iestateProject;
    @Autowired
    @Qualifier("estateProjectAlter")
    private IEstateProjectAlter iestateProjectAlter;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private ILoanTaskService iloanTaskService;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    IStHousingPersonalLoanDAO isthousingpersonalloandao;

    private final SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat simM = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat simMs = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat simm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat simpleall = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat simy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 还款申请列表
     **/
    @Override
    public final PageRes<HousingRepaymentApplyListGetRes> getHousingRepaymentApplyList(TokenContext tokenContext, String DKZH, String JKRXM, String pageSize, String page, String KSSJ, String JSSJ, String YHDM, String ZJHM) {
        PageResults<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = this.cloanHousingBusinessProcess.listWithPage(new HashMap<String, Object>() {{
                                                                                       this.put("cznr", Arrays.asList(LoanBusinessType.逾期还款.getCode(), LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
                                                                                       if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
                                                                                           this.put("ywwd.id", tokenContext.getUserInfo().getYWWD());
                                                                                       }
                                                                                   }}, !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ),
                    !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ), "created_at", Order.DESC, null, null, Integer.parseInt(page), Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.createAlias("loanApplyRepaymentVice", "loanApplyRepaymentVice");
                            if (StringUtil.notEmpty(DKZH)) {
                                criteria.add(Restrictions.like("loanApplyRepaymentVice.dkzh", "%" + DKZH + "%"));
                            }
                            if (StringUtil.notEmpty(JKRXM)) {
                                criteria.add(Restrictions.like("loanApplyRepaymentVice.jkrxm", "%" + JKRXM + "%"));
                            }
                            if (StringUtil.notEmpty(ZJHM)) {
                                criteria.add(Restrictions.like("loanApplyRepaymentVice.jkrzjhm", "%" + ZJHM + "%"));
                            }
                        }
                    });
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        if (housingBusinessProcess == null) throw new ErrorException("业务数据不存在");
        PageRes<HousingRepaymentApplyListGetRes> pageres = new PageRes<>();
        List<CLoanHousingBusinessProcess> housingBusinessProcesslist = housingBusinessProcess.getResults();
        ArrayList<HousingRepaymentApplyListGetRes> listhousingRepaymentApplyListGetRes = new ArrayList<>();
        HousingRepaymentApplyListGetRes housingRepaymentApplyListGetRes = null;
        StHousingPersonalAccount byDKZH = null;
        for (CLoanHousingBusinessProcess BusinessProcess : housingBusinessProcesslist) {
//            if (LoanBussinessStatus.入账失败.getName().equalsIgnoreCase(BusinessProcess.getStep())) continue;
            byDKZH = housingfundPersonalAccount.getByDkzh(BusinessProcess.getDkzh());
            if (StringUtil.notEmpty(YHDM)) {
                if (byDKZH != null && byDKZH.getStHousingPersonalLoan() != null) {
                    if (StringUtil.isEmpty(byDKZH.getStHousingPersonalLoan().getSwtyhdm()) || !byDKZH.getStHousingPersonalLoan().getSwtyhdm().equals(YHDM))
                        continue;
                }
            }
            housingRepaymentApplyListGetRes = new HousingRepaymentApplyListGetRes();
            try {
                housingRepaymentApplyListGetRes.setDKZH(BusinessProcess.getLoanApplyRepaymentVice().getDkzh());//贷款账号
                housingRepaymentApplyListGetRes.setJKRXM(BusinessProcess.getLoanApplyRepaymentVice().getJkrxm());//经办人姓名
                housingRepaymentApplyListGetRes.setJKRZJHM(BusinessProcess.getLoanApplyRepaymentVice().getJkrzjhm());//证件号码
                housingRepaymentApplyListGetRes.setSQHKLX(BusinessProcess.getLoanApplyRepaymentVice().getHklx() + "");//还款类型
                housingRepaymentApplyListGetRes.setSQHKJE(BusinessProcess.getLoanApplyRepaymentVice().getHkje() + "");//还款金额
                housingRepaymentApplyListGetRes.setZJHM(BusinessProcess.getLoanApplyRepaymentVice().getJkrzjhm());
                housingRepaymentApplyListGetRes.setYDKKRQ(sim.format(BusinessProcess.getLoanApplyRepaymentVice().getYdkkrq()));//约定扣款日期
            } catch (ErrorException e) {
                continue;
            }
            housingRepaymentApplyListGetRes.setSLSJ(simm.format(BusinessProcess.getCreated_at()));//办理时间
            housingRepaymentApplyListGetRes.setYWLSH(BusinessProcess.getYwlsh());//业务流水号
            housingRepaymentApplyListGetRes.setStatus(BusinessProcess.getStep() + "");//状态
            listhousingRepaymentApplyListGetRes.add(housingRepaymentApplyListGetRes);//
        }
        pageres.setResults(listhousingRepaymentApplyListGetRes);
        pageres.setCurrentPage(housingBusinessProcess.getCurrentPage());
        pageres.setNextPageNo(housingBusinessProcess.getPageNo());
        pageres.setPageCount(housingBusinessProcess.getPageCount());
        pageres.setPageSize(housingBusinessProcess.getPageSize());
        pageres.setTotalCount(housingBusinessProcess.getTotalCount());
        return pageres;
    }

    /**
     * 新增还款申请
     **/
    @Override
    public final CommonResponses postRepayment(TokenContext tokenContext, String action, RepaymentApplyPrepaymentPost body) {

        List<CLoanHousingPersonInformationBasic> jkrgjjzh = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(body.getJKRZJHM())) this.put("jkrzjhm", body.getJKRZJHM());
        }}, null, null, null, null, null, null);
        if (jkrgjjzh.size() == 0) throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人证件号码不存在");


        StHousingPersonalAccount hingPersonalAccount = jkrgjjzh.get(0).getPersonalAccount();

        if (hingPersonalAccount.getDkffrq() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款发放日期为空");
        if (hingPersonalAccount.getDkffe() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款发放额为空");
        if (hingPersonalAccount.getStHousingPersonalLoan() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账号合同信息丢失");

        StHousingPersonalAccount stHousingPersonalAccount = hingPersonalAccount;
//        if ("104".equals(stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhdm()) && "102".equals(stHousingPersonalAccount.getStHousingPersonalLoan().getZhkhyhdm())) {
//            throw new ErrorException(ReturnEnumeration.Business_FAILED, "中行、工行停止提前还取贷款业务，恢复时间：2017-12-20");
//        }

        BigDecimal yqsjqc = stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc()
                .add(stHousingPersonalAccount.getDkqs().subtract(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()));
        String doesBusiness = stHousingBusinessDetailsDAO.doesBusiness(stHousingPersonalAccount.getDkzh(), yqsjqc);
        if (doesBusiness != null && !doesBusiness.equals(LoanBussinessStatus.已入账.getName()) && !doesBusiness.equals(LoanBussinessStatus.逾期.getName())) {
            throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号（" + stHousingPersonalAccount.getDkzh() + "）存在未办结的正常还款业务");
        }


        List<CLoanHousingBusinessProcess> anHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("dkzh", hingPersonalAccount.getDkzh());
            this.put("cznr", Arrays.asList(LoanBusinessType.结清.getCode(), LoanBusinessType.提前还款.getCode()));
        }}, null, null, null, null, null, null);

        for (CLoanHousingBusinessProcess hsingBusinessProcess : anHousingBusinessProcess) {
            if (!LoanBussinessStatus.已入账.getName().equals(hsingBusinessProcess.getStep()) && !LoanBussinessStatus.已作废.getName().equals(hsingBusinessProcess.getStep())) {
                throw new ErrorException(ReturnEnumeration.Business_FAILED, "存在尚未办理的还款申请业务");
            }
        }

//        List<StCollectionPersonalBusinessDetails> collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
//            {
//                this.put("grzh", hingPersonalAccount.getStHousingPersonalLoan().getJkrgjjzh());
//                this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
//            }
//        }).getList(new DAOBuilder.ErrorHandler() {
//            @Override
//            public void error(Exception e) {
//            }
//        });
//        List<StCollectionPersonalBusinessDetails> collectionPersonalBusinessgt = null;
//        if (jkrgjjzh.get(0).getCoborrower() != null && "20".equals(jkrgjjzh.get(0).getHyzk())) {
//            collectionPersonalBusinessgt = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
//                {
//
//                    this.put("grzh", jkrgjjzh.get(0).getCoborrower().getGtjkrgjjzh());
//                    this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
//                }
//            }).getList(new DAOBuilder.ErrorHandler() {
//                @Override
//                public void error(Exception e) {
//                }
//            });
//        }
//        if (collectionPersonalBusinessDetails != null) {
//            for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDe : collectionPersonalBusinessDetails) {
//                if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDe.getTqyy())
//                        && !CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDe.getExtension().getStep())
//                        && !CollectionBusinessStatus.已作废.getName().equals(collectionPersonalBusinessDe.getExtension().getStep())) {
//                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "该账户正在办理提取还贷，不能申请还款");
//                }
//            }
//        }
//        if (collectionPersonalBusinessgt != null) {
//            for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDe : collectionPersonalBusinessgt) {
//                if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDe.getTqyy())
//                        && !CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDe.getExtension().getStep())
//                        && !CollectionBusinessStatus.已作废.getName().equals(collectionPersonalBusinessDe.getExtension().getStep())) {
//                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "该账户正在办理提取还贷，不能申请还款");
//                }
//            }
//        }

        //查询逾期记录
        List<StHousingOverdueRegistration> stHousingOverdueRegistration = housingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", hingPersonalAccount.getDkzh());
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);

        String id = null;
        CLoanHousingBusinessProcess loanHousingBusinessProcess = null;
        CLoanApplyRepaymentVice loanApplyRepaymentVice = null;
        if (LoanBusinessType.逾期还款.getCode().equals(body.getHKLX())) {
            throw new ErrorException("此功能已下线，改为自动还款流程");
        } else if (LoanBusinessType.提前还款.getCode().equals(body.getHKLX())) {
            for (StHousingOverdueRegistration hingOverdueRegistration : stHousingOverdueRegistration) {
                if (!LoanBussinessStatus.已入账.getName().equals(hingOverdueRegistration.getcHousingOverdueRegistrationExtension().getYwzt()))
                    throw new ErrorException("您还有逾期款项没有还，请等待系统自动逾期扣划再进入此操作");
            }
            //数据验证
            if (body.getTQBFHKXX() == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "提前部分还款信息为空");
            int dqqc = 0;//当期期次 
            try {
                if (sim.parse(sim.format(hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq())).getTime() >= sim.parse(body.getTQBFHKXX().getYDKKRQ()).getTime())
                    throw new ErrorException("不能在放款日当天或以前还款:" + sim.format(hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq()));
                if (sim.parse(body.getTQBFHKXX().getYDKKRQ()).getTime() >= new Date().getTime() + 172800000)
                    throw new ErrorException("约定扣款日期须在第二天");
                dqqc = CommLoanAlgorithm.currentQS(sim.parse(sim.format(hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq())), sim.parse(body.getTQBFHKXX().getYDKKRQ()));
            } catch (ParseException e) {
                throw new ErrorException(e);
            } catch (Exception e) {
                throw new ErrorException(e);
            }

            HashMap<String, String> tgbf = new HashMap<>();
            tgbf.put("借款人证件号码", body.getTQBFHKXX().getJKRZJHM());
            tgbf.put("约定扣款日期", body.getTQBFHKXX().getYDKKRQ());
            tgbf.put("借款人姓名", body.getTQBFHKXX().getJKRXM());
            tgbf.put("还款方式", body.getTQBFHKXX().getHKFS());
            tgbf.put("新最后还款期限", body.getTQBFHKXX().getXZHHKQX());
            ObjectAttributeCheck.checkObjects(tgbf);

            HashMap<String, String> str = new HashMap<>();
            str.put("SYBJ", body.getTQBFHKXX().getSYBJ());
            str.put("SYLX", body.getTQBFHKXX().getSYLX());
            str.put("SYQS", body.getTQBFHKXX().getSYQS());
            str.put("YSYHKE", body.getTQBFHKXX().getYSYHKE());
            str.put("MYDJ", body.getTQBFHKXX().getMYDJ());
            str.put("GYYCHKE", body.getTQBFHKXX().getGYYCHKE());
            str.put("XYHKE", body.getTQBFHKXX().getXYHKE());
            str.put("XMYDJ", body.getTQBFHKXX().getXMYDJ());
            str.put("JYLX", body.getTQBFHKXX().getJYLX());
            str.put("HKJE", body.getTQBFHKXX().getBCHKJE());
            ObjectAttributeCheck.checkDataType(str);

            //数据入库
            loanHousingBusinessProcess = new CLoanHousingBusinessProcess();
            loanHousingBusinessProcess.setDkzh(hingPersonalAccount.getDkzh());//贷款账号
            loanHousingBusinessProcess.setCznr(LoanBusinessType.提前还款.getCode());
            loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());
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
            loanHousingBusinessProcess.setYwwd(network);
            loanHousingBusinessProcess.setBlsj(new Date());
            loanHousingBusinessProcess.setStep(LoanBussinessStatus.初始状态.getName());
            loanApplyRepaymentVice = new CLoanApplyRepaymentVice();
            try {
                loanApplyRepaymentVice.setHkqc(new BigDecimal(dqqc));//还款期次
                loanApplyRepaymentVice.setDkzh(hingPersonalAccount.getDkzh());//贷款账号
                loanApplyRepaymentVice.setJkrzjhm(body.getJKRZJHM());
                loanApplyRepaymentVice.setJkrxm(body.getTQBFHKXX().getJKRXM());//借款人姓名
                loanApplyRepaymentVice.setHkfs(body.getTQBFHKXX().getHKFS());//还款方式
                loanApplyRepaymentVice.setSybj(new BigDecimal(body.getTQBFHKXX().getSYBJ()));//剩余本金
                loanApplyRepaymentVice.setSylx(new BigDecimal(body.getTQBFHKXX().getSYLX()));//剩余利息
                loanApplyRepaymentVice.setSyqc(new BigDecimal(body.getTQBFHKXX().getSYQS()));//剩余期数
                loanApplyRepaymentVice.setHkje(new BigDecimal(body.getTQBFHKXX().getBCHKJE()));//还款金额
                loanApplyRepaymentVice.setYdkkrq(sim.parse(body.getTQBFHKXX().getYDKKRQ()));//约定扣款日期
                loanApplyRepaymentVice.setYsyhke(new BigDecimal(body.getTQBFHKXX().getYSYHKE()));//原首月还款额
                loanApplyRepaymentVice.setMydj(new BigDecimal(body.getTQBFHKXX().getMYDJ())); //每月递减
                loanApplyRepaymentVice.setYzhhkqx(sim.parse(body.getTQBFHKXX().getYZHHKQX()));//原最后还款期限
                loanApplyRepaymentVice.setGyychke(new BigDecimal(body.getTQBFHKXX().getGYYCHKE()));//该月一次还款额
                loanApplyRepaymentVice.setXyhke(new BigDecimal(body.getTQBFHKXX().getXYHKE()));//新月还款额
                loanApplyRepaymentVice.setXmydj(new BigDecimal(body.getTQBFHKXX().getXMYDJ()));//新每月递减
                loanApplyRepaymentVice.setJylx(new BigDecimal(body.getTQBFHKXX().getJYLX()));//节约利息
                loanApplyRepaymentVice.setXzhhkqx(sim.parse(body.getTQBFHKXX().getXZHHKQX()));//新最后还款期限
                loanApplyRepaymentVice.setHklx(LoanBusinessType.提前还款.getCode());//还款类型
                loanApplyRepaymentVice.setJkrzjhm(hingPersonalAccount.getStHousingPersonalLoan().getJkrzjh());//借款人证件号码

                loanApplyRepaymentVice.setGrywmx(loanHousingBusinessProcess);
                loanHousingBusinessProcess.setLoanApplyRepaymentVice(loanApplyRepaymentVice);
                id = cloanHousingBusinessProcess.save(loanHousingBusinessProcess);//入业务表
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        } else if (LoanBusinessType.结清.getCode().equals(body.getHKLX())) {
            for (StHousingOverdueRegistration hingOverdueRegistration : stHousingOverdueRegistration) {
                if (!LoanBussinessStatus.已入账.getName().equals(hingOverdueRegistration.getcHousingOverdueRegistrationExtension().getYwzt()))
                    throw new ErrorException("您还有逾期款项没有还，请等待系统自动逾期扣划再进入此操作");
            }
            //数据验证
            if (body.getTQJQHKXX() == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "提前部分还款信息为空");
            int dqqc = 0;//当期期次 
            try {
                if (hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq().getTime() >= sim.parse(body.getTQJQHKXX().getYDKKRQ()).getTime())
                    throw new ErrorException("不能在放款日当天或以前还款:" + sim.format(hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq()));
                if (sim.parse(body.getTQJQHKXX().getYDKKRQ()).getTime() >= new Date().getTime() + 172800000)
                    throw new ErrorException("约定扣款日期须在第二天");
                dqqc = CommLoanAlgorithm.currentQS(sim.parse(sim.format(hingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq())), sim.parse(body.getTQJQHKXX().getYDKKRQ()));
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期格式有误yyyy-MM-dd");
            } catch (Exception e) {
                throw new ErrorException(e);
            }
            HashMap<String, String> str = new HashMap<>();
            str.put("SYBJ", body.getTQJQHKXX().getSYBJ());
            str.put("SYLX", body.getTQJQHKXX().getSYLX());
            str.put("SYQS", body.getTQJQHKXX().getSYQS());
            str.put("HKJE", body.getTQJQHKXX().getBCHKJE());
            ObjectAttributeCheck.checkDataType(str);

            //数据入库
            loanHousingBusinessProcess = new CLoanHousingBusinessProcess();
            loanHousingBusinessProcess.setDkzh(hingPersonalAccount.getDkzh());//贷款账号
            loanHousingBusinessProcess.setCznr(LoanBusinessType.结清.getCode());
            loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());
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
            loanHousingBusinessProcess.setYwwd(network);
            loanHousingBusinessProcess.setBlsj(new Date());
            loanHousingBusinessProcess.setStep(LoanBussinessStatus.初始状态.getName());
            loanApplyRepaymentVice = new CLoanApplyRepaymentVice();
            try {
                loanApplyRepaymentVice.setDkzh(hingPersonalAccount.getDkzh());//贷款账号
                loanApplyRepaymentVice.setJkrzjhm(body.getJKRZJHM());
                loanApplyRepaymentVice.setJkrxm(body.getTQJQHKXX().getJKRXM());//借款人姓名
                loanApplyRepaymentVice.setHkfs(body.getTQJQHKXX().getHKFS());//还款方式
                loanApplyRepaymentVice.setHkqc(new BigDecimal(dqqc));
                loanApplyRepaymentVice.setSybj(new BigDecimal(body.getTQJQHKXX().getSYBJ()));//剩余本金
                loanApplyRepaymentVice.setSylx(new BigDecimal(body.getTQJQHKXX().getSYLX()));//剩余利息
                loanApplyRepaymentVice.setSyqc(new BigDecimal(body.getTQJQHKXX().getSYQS()));//剩余期数
                loanApplyRepaymentVice.setHkje(new BigDecimal(body.getTQJQHKXX().getBCHKJE()));//还款金额
                loanApplyRepaymentVice.setYdkkrq(sim.parse(body.getTQJQHKXX().getYDKKRQ()));//约定扣款日期
                loanApplyRepaymentVice.setHklx(LoanBusinessType.结清.getCode());//还款类型
                loanApplyRepaymentVice.setJkrzjhm(hingPersonalAccount.getStHousingPersonalLoan().getJkrzjh());//借款人证件号码

                loanApplyRepaymentVice.setGrywmx(loanHousingBusinessProcess);
                loanHousingBusinessProcess.setLoanApplyRepaymentVice(loanApplyRepaymentVice);
                id = cloanHousingBusinessProcess.save(loanHousingBusinessProcess);//入业务表
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }
        CLoanHousingBusinessProcess canHousingBusinessProcess = cloanHousingBusinessProcess.get(id);

        iSaveAuditHistory.saveNormalBusiness(canHousingBusinessProcess.getYwlsh(), tokenContext, LoanBusinessType.提前还款.getName(), "新建");

        StateMachineUtils.updateState(iStateMachineService, action.equals("0") ? Events.通过.getEvent() : Events.提交.getEvent(),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getYWWD());
                    this.setStatus(canHousingBusinessProcess.getStep());
                    this.setTaskId(canHousingBusinessProcess.getYwlsh());
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.贷款_个人还款申请.getSubType());
                    this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }
                        if (StringUtil.isIntoReview(next, null))
                            canHousingBusinessProcess.setDdsj(new Date());
                        if(next.equals(LoanBussinessStatus.待入账.getName()))
                            doAction(canHousingBusinessProcess.getYwlsh());
                        canHousingBusinessProcess.setStep(next);
                        canHousingBusinessProcess.setBlsj(new Date());
                        cloanHousingBusinessProcess.update(canHousingBusinessProcess);
                    }
                });

        CommonResponses comm = new CommonResponses();
        comm.setId(canHousingBusinessProcess.getYwlsh());
        comm.setState("sucess");
        return comm;
    }

    /**
     * 修改还款申请
     **/
    @Override
    public final CommonResponses putRepayment(TokenContext tokenContext, String action, String YWLSH, RepaymentApplyPrepaymentPost body) {
        //数据验证
        List<CLoanHousingBusinessProcess> cloanHousingBusinessProcesslist = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(YWLSH)) this.put("ywlsh", YWLSH);
            this.put("cznr", Arrays.asList(LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
        }}, null, null, null, null, null, null);
        if (cloanHousingBusinessProcesslist.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在此业务纪录:" + YWLSH);
        }
        if (!cloanHousingBusinessProcesslist.get(0).getLoanApplyRepaymentVice().getJkrzjhm().equals(body.getJKRZJHM())) {
            throw new ErrorException("不能修改证件号码:" + cloanHousingBusinessProcesslist.get(0).getLoanApplyRepaymentVice().getJkrzjhm());
        }
        if (!body.getHKLX().equals(cloanHousingBusinessProcesslist.get(0).getLoanApplyRepaymentVice().getHklx()))
            throw new ErrorException("不能修改还款类型");

        if (!cloanHousingBusinessProcesslist.get(0).getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                !cloanHousingBusinessProcesslist.get(0).getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
            throw new ErrorException("操作员|业务网点不匹配");

        StHousingPersonalAccount byDkzh = housingfundPersonalAccount.getByDkzh(cloanHousingBusinessProcesslist.get(0).getDkzh());
        BigDecimal yqsjqc = byDkzh.getcLoanHousingPersonalAccountExtension().getDqqc()
                .add(byDkzh.getDkqs().subtract(byDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()));
        String doesBusiness = stHousingBusinessDetailsDAO.doesBusiness(byDkzh.getDkzh(), yqsjqc);
        if (doesBusiness != null && !doesBusiness.equals(LoanBussinessStatus.已入账.getName()) && !doesBusiness.equals(LoanBussinessStatus.逾期.getName())) {
            throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号（" + byDkzh.getDkzh() + "）存在未办结的正常还款业务");
        }

        CLoanHousingBusinessProcess loanHousingBusinessProcessGet = cloanHousingBusinessProcesslist.get(0);
        CLoanApplyRepaymentVice loanApplyRepaymentVice = null;
        loanHousingBusinessProcessGet.setCzy(tokenContext.getUserInfo().getCZY());
        if (LoanBusinessType.逾期还款.getCode().equals(body.getHKLX())) {
            throw new ErrorException("此功能已下线，改为自动还款流程");
        } else if (LoanBusinessType.提前还款.getCode().equals(body.getHKLX())) {
            //数据验证
            if (body.getTQBFHKXX() == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "提前部分还款信息为空");
//            ObjectAttributeCheck.checkObject(body.getTQBFHKXX());
            HashMap<String, String> str = new HashMap<>();
            str.put("SYBJ", body.getTQBFHKXX().getSYBJ());
            str.put("SYLX", body.getTQBFHKXX().getSYLX());
            str.put("SYQS", body.getTQBFHKXX().getSYQS());
            str.put("YSYHKE", body.getTQBFHKXX().getYSYHKE());
            str.put("MYDJ", body.getTQBFHKXX().getMYDJ());
            str.put("GYYCHKE", body.getTQBFHKXX().getGYYCHKE());
            str.put("XYHKE", body.getTQBFHKXX().getXYHKE());
            str.put("XMYDJ", body.getTQBFHKXX().getXMYDJ());
            str.put("JYLX", body.getTQBFHKXX().getJYLX());
            str.put("HKJE", body.getTQBFHKXX().getBCHKJE());
            ObjectAttributeCheck.checkDataType(str);

            if (loanHousingBusinessProcessGet.getLoanApplyRepaymentVice() == null)
                throw new ErrorException("业务数据严重丢失，重申请");
            try {
                if (byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq().getTime() >= sim.parse(body.getTQBFHKXX().getYDKKRQ()).getTime())
                    throw new ErrorException("不能在放款日当天或以前还款:" + sim.format(byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq()));
                if (sim.parse(body.getTQBFHKXX().getYDKKRQ()).getTime() >= new Date().getTime() + 172800000)
                    throw new ErrorException("约定扣款日期须在第二天");
            } catch (ParseException e) {
                throw new ErrorException(e);
            } catch (Exception e) {
                throw new ErrorException(e);
            }
            loanApplyRepaymentVice = loanHousingBusinessProcessGet.getLoanApplyRepaymentVice();
            try {
                loanApplyRepaymentVice.setJkrxm(body.getTQBFHKXX().getJKRXM());//借款人姓名
                loanApplyRepaymentVice.setHkfs(body.getTQBFHKXX().getHKFS());//还款方式
                loanApplyRepaymentVice.setHkje(new BigDecimal(body.getTQBFHKXX().getBCHKJE()));//还款金额
                loanApplyRepaymentVice.setSybj(new BigDecimal(body.getTQBFHKXX().getSYBJ()));//剩余本金
                loanApplyRepaymentVice.setSylx(new BigDecimal(body.getTQBFHKXX().getSYLX()));//剩余利息
                loanApplyRepaymentVice.setSyqc(new BigDecimal(body.getTQBFHKXX().getSYQS()));//剩余期数
                loanApplyRepaymentVice.setYdkkrq(sim.parse(body.getTQBFHKXX().getYDKKRQ()));//约定扣款日期
                loanApplyRepaymentVice.setYsyhke(new BigDecimal(body.getTQBFHKXX().getYSYHKE()));//原首月还款额
                loanApplyRepaymentVice.setMydj(new BigDecimal(body.getTQBFHKXX().getMYDJ())); //每月递减
                loanApplyRepaymentVice.setYzhhkqx(sim.parse(body.getTQBFHKXX().getYZHHKQX()));//原最后还款期限
                loanApplyRepaymentVice.setGyychke(new BigDecimal(body.getTQBFHKXX().getGYYCHKE()));//该月一次还款额
                loanApplyRepaymentVice.setXyhke(new BigDecimal(body.getTQBFHKXX().getXYHKE()));//新月还款额
                loanApplyRepaymentVice.setXmydj(new BigDecimal(body.getTQBFHKXX().getXMYDJ()));//新每月递减
                loanApplyRepaymentVice.setJylx(new BigDecimal(body.getTQBFHKXX().getJYLX()));//节约利息
                loanApplyRepaymentVice.setXzhhkqx(sim.parse(body.getTQBFHKXX().getXZHHKQX()));//新最后还款期限
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        } else if (LoanBusinessType.结清.getCode().equals(body.getHKLX())) {
            //数据验证
            if (body.getTQJQHKXX() == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "提前结清还款信息为空");

            HashMap<String, String> tgbf = new HashMap<>();
            tgbf.put("借款人证件号码", body.getTQJQHKXX().getJKRZJHM());
            tgbf.put("约定扣款日期", body.getTQJQHKXX().getYDKKRQ());
            tgbf.put("借款人姓名", body.getTQJQHKXX().getJKRXM());
            tgbf.put("还款方式", body.getTQJQHKXX().getHKFS());
            ObjectAttributeCheck.checkObjects(tgbf);

            HashMap<String, String> str = new HashMap<>();
            str.put("SYBJ", body.getTQJQHKXX().getSYBJ());
            str.put("SYLX", body.getTQJQHKXX().getSYLX());
            str.put("SYQS", body.getTQJQHKXX().getSYQS());
            ObjectAttributeCheck.checkDataType(str);

            if (loanHousingBusinessProcessGet.getLoanApplyRepaymentVice() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务数据丢失，重申请");
            try {
                if (byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq().getTime() >= sim.parse(body.getTQJQHKXX().getYDKKRQ()).getTime())
                    throw new ErrorException("不能在放款日当天或以前还款:" + sim.format(byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq()));
                if (sim.parse(body.getTQJQHKXX().getYDKKRQ()).getTime() >= new Date().getTime() + 172800000)
                    throw new ErrorException("约定扣款日期须在第二天");
            } catch (ParseException e) {
                throw new ErrorException("约定扣款日期格式有误yyyy-MM-dd");
            } catch (Exception e) {
                throw new ErrorException(e);
            }
            loanApplyRepaymentVice = loanHousingBusinessProcessGet.getLoanApplyRepaymentVice();
            try {
                loanApplyRepaymentVice.setJkrxm(body.getTQJQHKXX().getJKRXM());//借款人姓名
                loanApplyRepaymentVice.setHkfs(body.getTQJQHKXX().getHKFS());//还款方式
                loanApplyRepaymentVice.setSybj(new BigDecimal(body.getTQJQHKXX().getSYBJ()));//剩余本金
                loanApplyRepaymentVice.setSylx(new BigDecimal(body.getTQJQHKXX().getSYLX()));//剩余利息
                loanApplyRepaymentVice.setSyqc(new BigDecimal(body.getTQJQHKXX().getSYQS()));//剩余期数
                loanApplyRepaymentVice.setHkje(new BigDecimal(body.getTQJQHKXX().getBCHKJE()));//还款金额
                loanApplyRepaymentVice.setYdkkrq(sim.parse(body.getTQJQHKXX().getYDKKRQ()));//约定扣款日期
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }
        StateMachineUtils.updateState(iStateMachineService, action.equals("0") ? Events.保存.getEvent() : Events.通过.getEvent(),
                new TaskEntity() {{
                    this.setOperator(body.getCZY());
                    this.setStatus(loanHousingBusinessProcessGet.getStep());
                    this.setTaskId(YWLSH);
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.贷款_个人还款申请.getSubType());
                    this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    this.setWorkstation(body.getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }

                        if (succeed) {
                            if (StringUtil.isIntoReview(next, null))
                                loanHousingBusinessProcessGet.setDdsj(new Date());
                            if(next.equals(LoanBussinessStatus.待入账.getName()))
                                doAction(loanHousingBusinessProcessGet.getYwlsh());
                            loanHousingBusinessProcessGet.setStep(next);
                            cloanHousingBusinessProcess.update(loanHousingBusinessProcessGet);
                        }
                    }
                });
        if (action.equals("1")) {
            iSaveAuditHistory.saveNormalBusiness(loanHousingBusinessProcessGet.getYwlsh(), tokenContext, LoanBusinessType.提前还款.getName(), "修改");
        }

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    /**
     * 获取还款申请详情
     **/
    @Override
    public final RepaymentApplyPrepaymentPost getPerpaymentDetails(TokenContext tokenContext, String YWLSH) {
        RepaymentApplyPrepaymentPost repaymentApplyPrepaymentPost = null;
        List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            this.put("cznr", Arrays.asList(LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        if (loanHousingBusinessProcess.size() <= 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在此业务记录");
        }
        CLoanApplyRepaymentVice loanApplyRepaymentVice = loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice();
        if (loanApplyRepaymentVice == null)
            throw new ErrorException("此业务不存在还款记录信息,数据严重缺失");

        repaymentApplyPrepaymentPost = new RepaymentApplyPrepaymentPost();
        repaymentApplyPrepaymentPost.setCZY(loanHousingBusinessProcess.get(0).getCzy());//操作员
        repaymentApplyPrepaymentPost.setYWWD(loanHousingBusinessProcess.get(0).getYwwd().getMingCheng());//业务网点
        repaymentApplyPrepaymentPost.setHKLX(loanApplyRepaymentVice.getHklx());//还款类型
        repaymentApplyPrepaymentPost.setYWLSH(YWLSH);//业务流水号
        repaymentApplyPrepaymentPost.setJKRZJHM(loanApplyRepaymentVice.getJkrzjhm());//贷款账号

        if (LoanBusinessType.提前还款.getCode().equals(loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice().getHklx())) {
            RepaymentApplyPrepaymentPostTQBFHKXX repaymentApplyPrepaymentPostTQBFHKXX = new RepaymentApplyPrepaymentPostTQBFHKXX();
            repaymentApplyPrepaymentPostTQBFHKXX.setJYLX(loanApplyRepaymentVice.getJylx() + "");//节约利息
            repaymentApplyPrepaymentPostTQBFHKXX.setBCHKJE(loanApplyRepaymentVice.getHkje() + "");//还款金额
            repaymentApplyPrepaymentPostTQBFHKXX.setYDKKRQ(sim.format(loanApplyRepaymentVice.getYdkkrq()));//约定扣款日期
            repaymentApplyPrepaymentPostTQBFHKXX.setJKRXM(loanApplyRepaymentVice.getJkrxm());//借款人姓名
            repaymentApplyPrepaymentPostTQBFHKXX.setSYBJ(loanApplyRepaymentVice.getSybj() + "");//贷款余额
            repaymentApplyPrepaymentPostTQBFHKXX.setGYYCHKE(loanApplyRepaymentVice.getGyychke() + "");//该月一次还款额
            repaymentApplyPrepaymentPostTQBFHKXX.setMYDJ(loanApplyRepaymentVice.getMydj() + "");//每月递减
            repaymentApplyPrepaymentPostTQBFHKXX.setSYLX(loanApplyRepaymentVice.getSylx() + "");//剩余利息
            repaymentApplyPrepaymentPostTQBFHKXX.setXYHKE(loanApplyRepaymentVice.getXyhke() + "");//新月还款额
            repaymentApplyPrepaymentPostTQBFHKXX.setJKRZJHM(loanApplyRepaymentVice.getJkrzjhm());//贷款账号
            repaymentApplyPrepaymentPostTQBFHKXX.setYSYHKE(loanApplyRepaymentVice.getYsyhke() + "");//
            repaymentApplyPrepaymentPostTQBFHKXX.setXZHHKQX(sim.format(loanApplyRepaymentVice.getXzhhkqx()));//新最后还款期限
            repaymentApplyPrepaymentPostTQBFHKXX.setYZHHKQX(sim.format(loanApplyRepaymentVice.getYzhhkqx()));//原最后还款期限
            repaymentApplyPrepaymentPostTQBFHKXX.setXMYDJ(loanApplyRepaymentVice.getXmydj() + "");//新每月递减
            repaymentApplyPrepaymentPostTQBFHKXX.setSYQS(loanApplyRepaymentVice.getSyqc() + "");//剩余期数
            repaymentApplyPrepaymentPostTQBFHKXX.setHKFS(loanApplyRepaymentVice.getHkfs() + "");//还款方式
            repaymentApplyPrepaymentPost.setTQBFHKXX(repaymentApplyPrepaymentPostTQBFHKXX);
        } else if (LoanBusinessType.结清.getCode().equals(loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice().getHklx())) {
            RepaymentApplyPrepaymentPostTQJQHKXX repaymentApplyPrepaymentPostTQJQHKXX = new RepaymentApplyPrepaymentPostTQJQHKXX();
            repaymentApplyPrepaymentPostTQJQHKXX.setJKRXM(loanApplyRepaymentVice.getJkrxm());//借款人姓名
            repaymentApplyPrepaymentPostTQJQHKXX.setSYQS(loanApplyRepaymentVice.getSyqc() + "");//剩余期数
            repaymentApplyPrepaymentPostTQJQHKXX.setSYLX(loanApplyRepaymentVice.getSylx() + "");//剩余利息
            repaymentApplyPrepaymentPostTQJQHKXX.setSYBJ(loanApplyRepaymentVice.getSybj() + "");//剩余本金
            repaymentApplyPrepaymentPostTQJQHKXX.setJKRZJHM(loanApplyRepaymentVice.getJkrzjhm());//贷款账号
            repaymentApplyPrepaymentPostTQJQHKXX.setBCHKJE(loanApplyRepaymentVice.getHkje() + "");//还款金额
            repaymentApplyPrepaymentPostTQJQHKXX.setYDKKRQ(sim.format(loanApplyRepaymentVice.getYdkkrq()));//约定扣款日期
            repaymentApplyPrepaymentPostTQJQHKXX.setHKFS(loanApplyRepaymentVice.getHkfs() + "");//还款方式
            repaymentApplyPrepaymentPost.setTQJQHKXX(repaymentApplyPrepaymentPostTQJQHKXX);
        }
        return repaymentApplyPrepaymentPost;
    }

    /**
     * 提交与撤回还款申请
     *
     * @param status 状态（0：提交 1：撤回 2 已作废 ）
     **/
    @Override
    @Deprecated
    public final CommonResponses batchSubmit(TokenContext tokenContext, BatchSubmission body, String status, String ywlx) {
        String subtype = "";
        String subywlx = "";
        String subywName = "";
        boolean Verification = true;
        if (!"1".equals(status)) {
            if (!StringUtil.notEmpty(ywlx)) throw new ErrorException(ReturnEnumeration.Data_MISS, "业务类型不能为空");
            switch (ywlx) {
                case "1":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iHousingCompany.submitHousingCompany(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_房开申请受理.getSubType();
                        subywlx = LoanBusinessType.新建房开.getCode();
                        subywName = LoanBusinessType.新建房开.getName();
                    }
                    break;
                case "2":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            ihousingCompanyAlter.submitHousingCompanyAlter(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_房开变更受理.getSubType();
                        subywlx = LoanBusinessType.房开变更.getCode();
                        subywName = LoanBusinessType.房开变更.getName();
                    }
                    break;
                case "3":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iestateProject.submitEstateProject(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_楼盘申请受理.getSubType();
                        subywlx = LoanBusinessType.新建楼盘.getCode();
                        subywName = LoanBusinessType.新建楼盘.getName();
                    }
                    break;
                case "4":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iestateProjectAlter.submitEstateProjectAlter(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_楼盘变更受理.getSubType();
                        subywlx = LoanBusinessType.楼盘变更.getCode();
                        subywName = LoanBusinessType.楼盘变更.getCode();
                    }
                    break;
                case "5"://已作废
                    subtype = BusinessSubType.贷款_个人贷款申请.getSubType();
                    subywlx = LoanBusinessType.贷款发放.getCode();
                    subywName = LoanBusinessType.贷款发放.getName();
                    break;
                case "6":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.逾期还款.getCode();
                    subywName = LoanBusinessType.逾期还款.getName();
                    break;
                case "7":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.提前还款.getCode();
                    subywName = LoanBusinessType.提前还款.getName();
                    break;
                case "8":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.结清.getCode();
                    subywName = LoanBusinessType.结清.getName();
                    break;
                case "9":
                    subtype = BusinessSubType.贷款_合同变更申请.getSubType();
                    subywlx = LoanBusinessType.合同变更.getCode();
                    subywName = LoanBusinessType.合同变更.getName();
                    break;
                default:
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款_房开申请受理(1),\n" +
                            "贷款_房开变更受理(2),\n" +
                            "贷款_楼盘申请受理(3),\n" +
                            "贷款_楼盘变更受理(4),\n" +
                            "贷款_个人贷款申请(5),\n" +
                            "贷款_逾期还款(6),\n" +
                            "贷款_提前还款(7),\n" +
                            "贷款_结清(8),\n" +
                            "贷款_合同变更申请(9);");
            }
        }

        if (Verification == false) return new CommonResponses() {
            {
                this.setId("");
                this.setState("success");
            }
        };

        if (body != null) {
            if (!"1".equals(status)) {//提交和作废
                for (String YWLSH : body.getYWLSHJH()) {
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("ywlsh", YWLSH);
                    if (!"1".equals(status)) {
                        hash.put("cznr", subywlx);
                    }
                    List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(hash, null, null, null, null, ListDeleted.NOTDELETED, null);
                    if (loanHousingBusinessProcess.size() == 0)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在笔业务记录:" + YWLSH);
                    CLoanHousingBusinessProcess housingBusinessProcess = loanHousingBusinessProcess.get(0);
                    if (housingBusinessProcess.getStep() == null)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "业务状态不能为空,业务数据丢失");
                    if (!loanHousingBusinessProcess.get(0).getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                            !loanHousingBusinessProcess.get(0).getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");

                    if ("0".equals(status)) {
                        if (BusinessSubType.贷款_合同变更申请.getSubType().equals(subtype)) {
                            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                                    UploadFileBusinessType.合同变更.getCode(), loanHousingBusinessProcess.get(0).getLoanHousingPersonInformationVice().getBlzl())) {
                                throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
                            }
                            if ("20".equals(loanHousingBusinessProcess.get(0).getLoanHousingPersonInformationVice().getHyzk())) {
                                if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                                        UploadFileBusinessType.合同变更.getCode(), loanHousingBusinessProcess.get(0).getLoanHousingCoborrowerVice().getBlzl())) {
                                    throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
                                }
                            }
                        }
                    }

                    String event = null;
                    if ("0".equals(status)) {
                        event = Events.通过.getEvent();
                        iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, subywName, "修改");
                    } else {
                        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                            this.put("ywlsh", YWLSH);
                        }}, null, null, null, null, null, null);
                        if (cLoanHousingPersonInformationBasics.size() != 0)
                            icloanHousingPersonInformationBasicDAO.delete(cLoanHousingPersonInformationBasics.get(0));
                        event = Events.作废.getEvent();
                    }

                    if (loanHousingBusinessProcess.get(0).getCzy() == null)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "业务操作员为空");
                    if (loanHousingBusinessProcess.get(0).getStep() == null)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "业务状态为空");
                    if (loanHousingBusinessProcess.get(0).getYwwd() == null)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "业务网点为空");

                    TaskEntity taskenti = new TaskEntity();
                    taskenti.setOperator(loanHousingBusinessProcess.get(0).getCzy());
                    taskenti.setStatus(loanHousingBusinessProcess.get(0).getStep());
                    taskenti.setTaskId(loanHousingBusinessProcess.get(0).getYwlsh());
                    taskenti.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    taskenti.setSubtype(subtype);
                    taskenti.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    taskenti.setWorkstation(loanHousingBusinessProcess.get(0).getYwwd().getId());

                    StateMachineUtils.updateState(iStateMachineService, event, taskenti
                            , new StateMachineUtils.StateChangeHandler() {
                                @Override
                                public void onStateChange(boolean succeed, String next, Exception e) {
                                    if (e != null) {
                                        throw new ErrorException(e);
                                    }
                                    if (!succeed || next == null) {
                                        return;
                                    }
                                    if (succeed) {
                                        if (StringUtil.isIntoReview(next, null)) {
                                            loanHousingBusinessProcess.get(0).setDdsj(new Date());
                                        }
                                        loanHousingBusinessProcess.get(0).setStep(next);
                                        cloanHousingBusinessProcess.update(loanHousingBusinessProcess.get(0));
                                    }
                                }
                            });
                }
            } else {//撤回
                for (String YWLSH : body.getYWLSHJH()) {
                    revokeOperation(tokenContext, YWLSH);
                }
            }
        }
        return new CommonResponses() {
            {
                this.setId("");
                this.setState("success");
            }
        };
    }


    public final void revokeOperation(TokenContext tokenContext, String YWLSH) {
        CLoanHousingBusinessProcess entity = DAOBuilder.instance(cloanHousingBusinessProcess).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }
        });


        String step = entity.getStep();
        if (step == null || !StringUtil.isIntoReview(step, null) /** 还应考虑是否处于审核中**/) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务号为" + YWLSH);
        }
        MultiReviewConfig config = NormalJsonUtils.toObj4Review(entity.getShybh());
        String CaoZuo = "不通过撤回";
        step = checkByReivew(config, tokenContext);

        if (LoanBussinessStatus.新建.getName().equals(step)) {
            if (!entity.getCzy().equals(tokenContext.getUserInfo().getCZY()) || !entity.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是您受理的，不能撤回");
            CaoZuo = "撤回";
        }

        String ywlx = entity.getCznr();
        entity.setStep(step);
        entity.setShybh(NormalJsonUtils.toJson4Review(config));
        iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.getNameByCode(ywlx), CaoZuo);
        cloanHousingBusinessProcess.update(entity);
    }

    //基于审核配置的验证，并返回正确的step状态
    public final String checkByReivew(MultiReviewConfig config, TokenContext tokenContext) {
        String step;
        if (config == null) {
            step = LoanBussinessStatus.新建.getName();
        } else {
            //审核级别（0：普审，1：特审）
            if ("S".equals(config.getSHJB())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已进入特审阶段，不能撤回");
            }
            if (StringUtil.notEmpty(config.getSCSHY())) {
                if (!tokenContext.getUserId().equals(config.getSCSHY())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的上一级审核非您操作，不能撤回");
                }
                step = LoanBussinessStatus.审核不通过.getName();
            } else {
                step = LoanBussinessStatus.新建.getName();
            }
            config.setLSSHYBH(null);
            config.setSHJB(null);
            config.setSCSHY(null);
            config.setDQSHY(null);
            config.setDQXM(null);
        }
        return step;
    }

    /**
     * 还款审核列表//没用
     **/
    @Override
    @Deprecated
    public final HousingRepamentApplyRangeGet getRepaymentReview(TokenContext tokenContext, String status, String JKRXM, String stime, String etime) {
        Date date = new Date();
        if (!StringUtil.notEmpty(stime)) stime = "2017-01-01";
        if (!StringUtil.notEmpty(etime)) etime = sim.format(date);

        HousingRepamentApplyRangeGet housingRepamentApplyRangeGet = null;
        List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = null;
        try {
            loanHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(JKRXM)) this.put("loanApplyRepaymentVice.jkrxm", JKRXM);
                if (status.equals("1")) this.put("step", LoanBussinessStatus.办结.getName());
                this.put("cznr", Arrays.asList(LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
            }}, sim.parse(stime), sim.parse(etime), null, null, null, null);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        housingRepamentApplyRangeGet = new HousingRepamentApplyRangeGet();
        ArrayList<HousingRepamentApplyRangeGetRes> res = new ArrayList<>();
        HousingRepamentApplyRangeGetRes housingRepamentApplyRangeGetRes = null;
        for (CLoanHousingBusinessProcess housingBusinessProcess : loanHousingBusinessProcess) {
            housingRepamentApplyRangeGetRes = new HousingRepamentApplyRangeGetRes();
            housingRepamentApplyRangeGetRes.setYWLSH(housingBusinessProcess.getYwlsh());//业务流水号
            housingRepamentApplyRangeGetRes.setDKZH(housingBusinessProcess.getLoanApplyRepaymentVice().getDkzh());//贷款账号
            housingRepamentApplyRangeGetRes.setJKRXM(housingBusinessProcess.getLoanApplyRepaymentVice().getJkrxm());//借款人姓名
            List<StHousingPersonalAccount> stHousingPersonalAccount = housingfundPersonalAccount.list(new HashMap<String, Object>() {{
                if (housingBusinessProcess.getLoanApplyRepaymentVice().getDkzh() != null)
                    this.put("dkzh", housingBusinessProcess.getLoanApplyRepaymentVice().getDkzh());
            }}, null, null, null, null, ListDeleted.NOTDELETED, null);
            if (stHousingPersonalAccount.size() <= 0) continue;
            housingRepamentApplyRangeGetRes.setJKRZJHM(stHousingPersonalAccount.get(0).getStHousingPersonalLoan().getJkrzjh());//借款人证件号
            housingRepamentApplyRangeGetRes.setSQHKLX(housingBusinessProcess.getLoanApplyRepaymentVice().getHklx() + "");//还款类型
            housingRepamentApplyRangeGetRes.setSQHKJE(housingBusinessProcess.getLoanApplyRepaymentVice().getHkje() + "");//申请还款金额
            housingRepamentApplyRangeGetRes.setStatus(housingBusinessProcess.getStep());//状态
            housingRepamentApplyRangeGetRes.setCZY(housingBusinessProcess.getCzy());//操作员
            housingRepamentApplyRangeGetRes.setYWWD(housingBusinessProcess.getYwwd().getMingCheng());//业务网点
            housingRepamentApplyRangeGetRes.setDDSJ(housingBusinessProcess.getBlsj() + "");//办理时间
            res.add(housingRepamentApplyRangeGetRes);
        }
        housingRepamentApplyRangeGet.setRes(res);//列表
        return housingRepamentApplyRangeGet;
    }

    /**
     * 打印还款申请回执
     */
    @Override
    public final CommonResponses printRepaymentReceipt(TokenContext tokenContext, String YWLSH) {
        List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            this.put("cznr", Arrays.asList(LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        if (loanHousingBusinessProcess.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在此业务记录");
        }
        if (!LoanBussinessStatus.已入账.getName().equals(loanHousingBusinessProcess.get(0).getStep()))
            throw new ErrorException("此业务不能打印回执单");
        CLoanApplyRepaymentVice loanApplyRepaymentVice = loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice();
        if (loanApplyRepaymentVice == null)
            throw new ErrorException("此业务不存在还款记录信息,业务记录严重缺失");
        RepaymentApplyReceipt repaymentApplyReceipt = null;

        if (LoanBusinessType.提前还款.getCode().equals(loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice().getHklx())) {
            //部分提前
            repaymentApplyReceipt = new RepaymentApplyReceipt();
            RepaymentApplyPrepaymentPostTQBFHKXX repaymentApplyPrepaymentPostTQBFHKXX = new RepaymentApplyPrepaymentPostTQBFHKXX();
            repaymentApplyReceipt.setCZY(loanHousingBusinessProcess.get(0).getCzy());//操作员
            repaymentApplyReceipt.setTZRQ(simy.format(new Date()));//填制日期
            repaymentApplyReceipt.setYWLSH(YWLSH);//业务流水号
            repaymentApplyReceipt.setYWWD(loanApplyRepaymentVice.getGrywmx().getYwwd().getMingCheng());
            repaymentApplyReceipt.setHKLX(loanApplyRepaymentVice.getHklx());//还款类型
            repaymentApplyPrepaymentPostTQBFHKXX.setJKRZJHM(loanApplyRepaymentVice.getJkrzjhm());//贷款账号
            repaymentApplyPrepaymentPostTQBFHKXX.setJKRXM(loanApplyRepaymentVice.getJkrxm());//借款人姓名
            if (loanApplyRepaymentVice.getHkfs() != null) {
                SingleDictionaryDetail Hkfs = iDictionaryService.getSingleDetail(loanApplyRepaymentVice.getHkfs(), "LoanPaymentMode");
                repaymentApplyPrepaymentPostTQBFHKXX.setHKFS(Hkfs != null ? Hkfs.getName() : "");//还款方式
            }
            repaymentApplyPrepaymentPostTQBFHKXX.setSYBJ(loanApplyRepaymentVice.getSybj() + "");//贷款余额
            repaymentApplyPrepaymentPostTQBFHKXX.setSYLX(loanApplyRepaymentVice.getSylx() + "");//剩余利息
            repaymentApplyPrepaymentPostTQBFHKXX.setSYQS(loanApplyRepaymentVice.getSyqc() + "");//剩余期数
            repaymentApplyPrepaymentPostTQBFHKXX.setBCHKJE(loanApplyRepaymentVice.getHkje() + "");//还款金额
            repaymentApplyPrepaymentPostTQBFHKXX.setYDKKRQ(sim.format(loanApplyRepaymentVice.getYdkkrq()));//约定扣款日期
            repaymentApplyPrepaymentPostTQBFHKXX.setYSYHKE(loanApplyRepaymentVice.getYsyhke() + "");//原首月还款额
            repaymentApplyPrepaymentPostTQBFHKXX.setMYDJ(loanApplyRepaymentVice.getMydj() + "");//每月递减
            repaymentApplyPrepaymentPostTQBFHKXX.setYZHHKQX(sim.format(loanApplyRepaymentVice.getYzhhkqx()));//原最后还款期限
            repaymentApplyPrepaymentPostTQBFHKXX.setXZHHKQX(sim.format(loanApplyRepaymentVice.getXzhhkqx()));//新最后还款期限
            repaymentApplyPrepaymentPostTQBFHKXX.setGYYCHKE(loanApplyRepaymentVice.getGyychke() + "");//该月一次还款额
            repaymentApplyPrepaymentPostTQBFHKXX.setXYHKE(loanApplyRepaymentVice.getXyhke() + "");//新月还款额
            repaymentApplyPrepaymentPostTQBFHKXX.setXMYDJ(loanApplyRepaymentVice.getXmydj() + "");//新每月递减
            repaymentApplyPrepaymentPostTQBFHKXX.setJYLX(loanApplyRepaymentVice.getJylx() + "");//节约利息
            repaymentApplyReceipt.setTQBFHKXX(repaymentApplyPrepaymentPostTQBFHKXX);
        } else if (LoanBusinessType.结清.getCode().equals(loanHousingBusinessProcess.get(0).getLoanApplyRepaymentVice().getHklx())) {
            //结清
            repaymentApplyReceipt = new RepaymentApplyReceipt();
            RepaymentApplyPrepaymentPostTQJQHKXX repaymentApplyPrepaymentPostTQJQHKXX = new RepaymentApplyPrepaymentPostTQJQHKXX();
            repaymentApplyReceipt.setCZY(loanHousingBusinessProcess.get(0).getCzy());//操作员
            repaymentApplyReceipt.setTZRQ(simy.format(new Date()));//填制日期
            repaymentApplyReceipt.setYWLSH(YWLSH);//业务流水号
            repaymentApplyReceipt.setYWWD(loanApplyRepaymentVice.getGrywmx().getYwwd().getMingCheng());
            repaymentApplyReceipt.setHKLX(loanApplyRepaymentVice.getHklx());//还款类型
            repaymentApplyPrepaymentPostTQJQHKXX.setJKRZJHM(loanApplyRepaymentVice.getJkrzjhm());//贷款账号
            repaymentApplyPrepaymentPostTQJQHKXX.setJKRXM(loanApplyRepaymentVice.getJkrxm());//借款人姓名
            if (loanApplyRepaymentVice.getHkfs() != null) {
                SingleDictionaryDetail Hkfs = iDictionaryService.getSingleDetail(loanApplyRepaymentVice.getHkfs(), "LoanPaymentMode");
                repaymentApplyPrepaymentPostTQJQHKXX.setHKFS(Hkfs != null ? Hkfs.getName() : "");//还款方式
            }
            repaymentApplyPrepaymentPostTQJQHKXX.setSYBJ(loanApplyRepaymentVice.getSybj() + "");//剩余本金
            repaymentApplyPrepaymentPostTQJQHKXX.setSYLX(loanApplyRepaymentVice.getSylx() + "");//剩余利息
            repaymentApplyPrepaymentPostTQJQHKXX.setSYQS(loanApplyRepaymentVice.getSyqc() + "");//剩余期数
            repaymentApplyPrepaymentPostTQJQHKXX.setBCHKJE(loanApplyRepaymentVice.getHkje() + "");//还款金额
            repaymentApplyPrepaymentPostTQJQHKXX.setYDKKRQ(sim.format(loanApplyRepaymentVice.getYdkkrq()));//约定扣款日期
            repaymentApplyReceipt.setTQJQHKXX(repaymentApplyPrepaymentPostTQJQHKXX);
        }
        //审核人，该条记录审核通过的操作员
        CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
                this.put("shjg", "01");
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cAuditHistory != null) {
            repaymentApplyReceipt.setSHR(cAuditHistory.getCzy());
        }

        String id = pdfService.getPrintRepaymentReceipt(repaymentApplyReceipt, loanApplyRepaymentVice.getHklx());
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 查询数据金额2 1 贷款还贷 2 提取还贷
     * gjjzh 0就是借款人，1是共同借款人
     */
    public final RepaymentApplicationInAdvanceGet backRepaymentInfo(TokenContext tokenContext, String hklx, String JKRZJHM, String YDKKRQ, String BCHKJE, String HDLX, String gjjzhlx) {
        Date ydkkrq = null;
        try {
            ydkkrq = sim.parse(YDKKRQ);
            if (HDLX.equals("2")) {
                if (ydkkrq.getTime() < sim.parse(sim.format(new Date())).getTime())
                    throw new ErrorException("约定扣款日期必须大于等于当前日期");
            } else {
                if (ydkkrq.getTime() <= sim.parse(sim.format(new Date())).getTime())
                    throw new ErrorException("约定扣款日期必须大于当前日期");
            }
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "时间格式：yyyy-MM-dd");
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, e.getMessage());
        }
        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(JKRZJHM)) this.put("jkrzjhm", JKRZJHM);
            this.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.呆账.getCode(), LoanAccountType.逾期.getCode()));
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        if (cLoanHousingPersonInformationBasics.size() == 0)
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "借款人证件号码（" + JKRZJHM + "）不存在或账户已结清");
        if (HDLX.equals("2")) {
            if ("0".equals(gjjzhlx)) {
                StCommonPerson commonPersonDAOByGrzh = commonPersonDAO.getByGrzh(cLoanHousingPersonInformationBasics.get(0).getJkrgjjzh());
                if (commonPersonDAOByGrzh == null)
                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号绑定借款人公积金账号丢失（" + cLoanHousingPersonInformationBasics.get(0).getJkrgjjzh() + "）");
                if (!commonPersonDAOByGrzh.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.正常.getCode()) &&
                        !commonPersonDAOByGrzh.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.封存.getCode()))
                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号绑定借款人公积金账号状态异常（" + cLoanHousingPersonInformationBasics.get(0).getJkrgjjzh() + "）");
                if (commonPersonDAOByGrzh.getExtension().getSfdj().equals("02"))
                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "此账号已冻结" + cLoanHousingPersonInformationBasics.get(0).getJkrgjjzh());

            }
            if ("1".equals(gjjzhlx)) {
                if (cLoanHousingPersonInformationBasics.get(0).getCoborrower() != null) {
                    StCommonPerson commonPersonDAOByGrzhcob = commonPersonDAO.getByGrzh(cLoanHousingPersonInformationBasics.get(0).getCoborrower().getGtjkrgjjzh());
                    if (commonPersonDAOByGrzhcob == null)
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号绑定共同借款人公积金账号丢失（" + cLoanHousingPersonInformationBasics.get(0).getCoborrower().getGtjkrgjjzh() + "）");
                    if (!commonPersonDAOByGrzhcob.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.正常.getCode())
                            && !commonPersonDAOByGrzhcob.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.封存.getCode()))
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号绑定共同借款人公积金账号状态异常（" + cLoanHousingPersonInformationBasics.get(0).getCoborrower().getGtjkrgjjzh() + "）");
                    if (commonPersonDAOByGrzhcob.getExtension().getSfdj().equals("02"))
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "此账号已冻结" + cLoanHousingPersonInformationBasics.get(0).getCoborrower().getGtjkrgjjzh());
                } else {
                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号没有共同借款人");
                }
            }
        }
        StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();

        BigDecimal yqsjqc = stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc()
                .add(stHousingPersonalAccount.getDkqs().subtract(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhqs()));
        String doesBusiness = stHousingBusinessDetailsDAO.doesBusiness(stHousingPersonalAccount.getDkzh(), yqsjqc);
        if (doesBusiness != null && !doesBusiness.equals(LoanBussinessStatus.已入账.getName()) && !doesBusiness.equals(LoanBussinessStatus.逾期.getName())) {
            throw new ErrorException(ReturnEnumeration.Business_FAILED, "此贷款账号（" + stHousingPersonalAccount.getDkzh() + "）存在未办结的正常还款业务");
        }

        //数据验证
        Calendar calendar = null;
        String dkhkfs = "";
        BigDecimal dkffe = null;
        Date dkffrq = null;
        try {
            calendar = Calendar.getInstance();
            if (StringUtil.notEmpty(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getTqhksj())) {
                calendar.setTime(simMs.parse(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getTqhksj()));
                calendar.add(Calendar.MONTH, 1);
                long end = calendar.getTimeInMillis();
                if (ydkkrq.getTime() <= end)
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "一月只能提前还款一次,下次提前还款月份为：" + sim.format(end) + " 之后");
            }
            dkffrq = sim.parse(sim.format(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkxffrq()));
            Date kdday = CommLoanAlgorithm.periodOfafterTime(dkffrq, Integer.parseInt(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc().toString()));
            if (ydkkrq.getTime() <= (kdday.getTime() + 172800000) && ydkkrq.getTime() >= (kdday.getTime() - 172800000)) {//前后各两天
                throw new ErrorException(ReturnEnumeration.Business_FAILED, "现在处于正常还款时区，请于： " + sim.format((kdday.getTime() + 172800000)) + " 日来办理此业务（还款日前后两天不办理此业务）");
            }
            if (dkffrq.getTime() >= ydkkrq.getTime())
                throw new ParseException("不能在放款日当天还款，还款日期不能小于贷款发放日期:" + sim.format(dkffrq), 0);
            dkhkfs = stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs();//贷款还款方式
            dkffe = stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhye();//贷款发放额
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, e.getMessage());
        } catch (NullPointerException e) {
            if (stHousingPersonalAccount == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账户丢失");
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账户合同信息不完整");
        }
        if (dkffrq == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款发放日期为空");
        if (!RepaymentMethod.等额本息.getCode().equals(stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs()) && !RepaymentMethod.等额本金.getCode().equals(stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs()))
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, JKRZJHM + "：DKHKFS数据库错误,或者02本金01本息");
        if (stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhye() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账号贷款发放额为空");
        if (stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhqs() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "合同贷款期数不能为空");
        if (stHousingPersonalAccount.getDkye() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账户合同贷款余额为空");
        if (stHousingPersonalAccount.getDkll() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "合同贷款利率为空");

        //进入算法阶段
        BigDecimal dkll = CommLoanAlgorithm.lendingRate(stHousingPersonalAccount.getDkll(), stHousingPersonalAccount.getLlfdbl());//贷款利率
        int dkqs = Integer.parseInt(stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDkgbjhqs().toString());//贷款期数
        BigDecimal dkye = stHousingPersonalAccount.getDkye();//贷款余额
        int dqqc = CommLoanAlgorithm.currentQS(dkffrq, ydkkrq);//根据时间计算的期次
        int dqhkqc = stHousingPersonalAccount.getcLoanHousingPersonalAccountExtension().getDqqc().intValue();//数据库当期应还期次
        int jxts = CommLoanAlgorithm.beforeInterestDays(dkffrq, dqqc, ydkkrq);//计息天数 
        int syqc = dkqs - dqqc;//剩余期数
//        BigDecimal mybj = CommLoanAlgorithm.principalAmount(dkffe, dkqs, dkll, dkhkfs, dqqc);//每月本金//本金、本息
        BigDecimal mybj = stHousingPersonalAccount.getDqyhbj();
//        BigDecimal bqbx = CommLoanAlgorithm.currentBX(dkffe, dkqs, dkhkfs, dkll, dqqc);//本期本息//
        BigDecimal bqbx = stHousingPersonalAccount.getDqyhje();

        //查询逾期记录
        List<StHousingOverdueRegistration> stHousingOverdueRegistration = housingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", stHousingPersonalAccount.getDkzh());
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);

        RepaymentApplicationInAdvanceGet repaymentApplicationInAdvanceGet = new RepaymentApplicationInAdvanceGet();
        repaymentApplicationInAdvanceGet.setHKLX(hklx);
        if (hklx.equals(LoanBusinessType.逾期还款.getCode())) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "此功能已下线，改为自动还款流程");
        } else if (hklx.equals(LoanBusinessType.提前还款.getCode())) {
            for (StHousingOverdueRegistration hingOverdueRegistration : stHousingOverdueRegistration) {
                if (!LoanBussinessStatus.已入账.getName().equals(hingOverdueRegistration.getcHousingOverdueRegistrationExtension().getYwzt()))
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "您还有逾期款项没有还，请充值账户等待系统自动扣划逾期");
            }
            if (dqhkqc == dkqs)
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "当期为最后一期，请选择结清还款选项");
            //时间段判断
//            CommLoanAlgorithm.currentRepamentTime(dkffrq, ydkkrq, dqhkqc);
            Double bchkje = null;
            try {
                bchkje = Double.parseDouble(BCHKJE);
                if (new BigDecimal(bchkje).compareTo(bqbx) == -1)
                    throw new ErrorException("提前还款金额不能小于:" + bqbx.setScale(2, BigDecimal.ROUND_HALF_UP));
            } catch (NumberFormatException e) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "还款金额必传");
            }
            dkye = CommLoanAlgorithm.loanBalanceBefore(dqqc, dkll, dkhkfs, dkqs, dkffe);//不会出现这种情况
            if (new BigDecimal(bchkje).compareTo(dkye) == 1) throw new ErrorException("本次还款额大于贷款余额请选择结清还款选项");
            SettlePartialRepayments settlePartialRepayments = CommLoanAlgorithm.settlePartialRepayments(new BigDecimal(bchkje), bqbx, dkll, jxts);
            BigDecimal tqhbje = settlePartialRepayments.getTQHBJE();
            BigDecimal tqhklx = settlePartialRepayments.getTQHKLX();

            EarlyRepaymentReducemonth earlyRepaymentReducemonth = null;
            if (RepaymentMethod.等额本金.getCode().equals(dkhkfs)) {
                earlyRepaymentReducemonth = new EarlyRepaymentReducemonth();
                earlyRepaymentReducemonth.setJKRZJHM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrzjh());
                earlyRepaymentReducemonth.setJKRXM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm());//姓名
                earlyRepaymentReducemonth.setHKFS(dkhkfs);//贷款还款方式
                earlyRepaymentReducemonth.setSYBJ(stHousingPersonalAccount.getDkye().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//剩余本金
                earlyRepaymentReducemonth.setSYLX(CommLoanAlgorithm.residualInterestPrincipal(dqqc, dkffe, dkll, dkqs, dkhkfs).setScale(2, BigDecimal.ROUND_HALF_UP) + "");//剩余利息
                earlyRepaymentReducemonth.setYSYHKE(CommLoanAlgorithm.currentBX(dkffe, dkqs, dkhkfs, dkll, 1).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//原首月还款额
                earlyRepaymentReducemonth.setMYDJ(CommLoanAlgorithm.principalAmount(dkffe, dkqs, dkll, dkhkfs, 1).multiply(dkll.divide(BigDecimal.valueOf(1200), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//每月递减
                calendar.setTime(dkffrq);
                calendar.add(Calendar.MONTH, dkqs);
                earlyRepaymentReducemonth.setYZHHKQX(sim.format(calendar.getTime()));//原最后还款期限
                earlyRepaymentReducemonth.setGYYCHKE(BCHKJE);//该月一次还款额
                BigDecimal sybj = dkye.subtract(tqhbje).subtract(mybj);//剩余本金
                BigDecimal xyhbj = sybj.divide(new BigDecimal(syqc), 10, BigDecimal.ROUND_HALF_UP);//新月还本金
                BigDecimal xyhke = CommLoanAlgorithm.currentBX(sybj, syqc, dkhkfs, dkll, 1);
                earlyRepaymentReducemonth.setXYHKE(xyhke.setScale(2, BigDecimal.ROUND_HALF_UP).toString());//新月还本金
                earlyRepaymentReducemonth.setXMYDJ(xyhbj.multiply(dkll.divide(BigDecimal.valueOf(1200), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//新月递减
                earlyRepaymentReducemonth.setSYQS(syqc + "");//剩余期数
                earlyRepaymentReducemonth.setJYLX(CommLoanAlgorithm.savingLX(new BigDecimal(BCHKJE), dkll, dkhkfs, syqc).add(tqhklx).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//节约利息
                earlyRepaymentReducemonth.setYDKKRQ(YDKKRQ);
                earlyRepaymentReducemonth.setXZHHKQX(sim.format(calendar.getTime()));//新最后还款期限
                earlyRepaymentReducemonth.setBCHKJE(BCHKJE);
                repaymentApplicationInAdvanceGet.setEarlyRepaymentReducemonth(earlyRepaymentReducemonth);//减少月还款额
                return repaymentApplicationInAdvanceGet;
            }
            if (RepaymentMethod.等额本息.getCode().equals(dkhkfs)) {
                earlyRepaymentReducemonth = new EarlyRepaymentReducemonth();
                earlyRepaymentReducemonth.setJKRXM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm());//姓名
                earlyRepaymentReducemonth.setJKRZJHM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrzjh());
                earlyRepaymentReducemonth.setHKFS(dkhkfs);//贷款还款方式
                earlyRepaymentReducemonth.setSYBJ(dkye.setScale(2, BigDecimal.ROUND_HALF_UP).toString());//贷款余额
                earlyRepaymentReducemonth.setSYLX(CommLoanAlgorithm.residualInterestPrincipal(dqqc, dkffe, dkll, dkqs, dkhkfs).setScale(2, BigDecimal.ROUND_HALF_UP) + "");//剩余利息
                earlyRepaymentReducemonth.setYSYHKE(CommLoanAlgorithm.currentBX(dkffe, dkqs, dkhkfs, dkll, 1).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//每个月一样的
                earlyRepaymentReducemonth.setMYDJ("0");//每月递减
                calendar.setTime(dkffrq);
                calendar.add(Calendar.MONTH, dkqs);
                earlyRepaymentReducemonth.setYZHHKQX(sim.format(calendar.getTime()));//原来最后还款期限
                earlyRepaymentReducemonth.setGYYCHKE(BCHKJE);//该月一次还款金额
                BigDecimal sybj = dkye.subtract(tqhbje).subtract(mybj);//剩余本金 重点(没问题)
                earlyRepaymentReducemonth.setSYQS(syqc + "");//剩余期数
                earlyRepaymentReducemonth.setXYHKE(CommLoanAlgorithm.newCrescentRepayment(sybj, dkll, syqc).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//新月还款额
                earlyRepaymentReducemonth.setXMYDJ("0");//等额本息每月递减为0
                earlyRepaymentReducemonth.setJYLX(CommLoanAlgorithm.savingLX(new BigDecimal(BCHKJE), dkll, dkhkfs, syqc).add(tqhklx).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//节约利息
                earlyRepaymentReducemonth.setXZHHKQX(sim.format(calendar.getTime()));//新最后还款期限不变
                earlyRepaymentReducemonth.setYDKKRQ(YDKKRQ);//约定扣款日期
                earlyRepaymentReducemonth.setBCHKJE(BCHKJE);
                repaymentApplicationInAdvanceGet.setEarlyRepaymentReducemonth(earlyRepaymentReducemonth);//减少月还款额
                return repaymentApplicationInAdvanceGet;
            }
        } else if (hklx.equals(LoanBusinessType.结清.getCode())) {
            for (StHousingOverdueRegistration hingOverdueRegistration : stHousingOverdueRegistration) {
                if (!LoanBussinessStatus.已入账.getName().equals(hingOverdueRegistration.getcHousingOverdueRegistrationExtension().getYwzt()))
                    throw new ErrorException("您还有逾期款项没有还，请先选择逾期还款再进入此操作");
            }
            //结清贷款
//            CommLoanAlgorithm.currentRepamentTime(dkffrq, ydkkrq, dqhkqc);
            dkye = CommLoanAlgorithm.loanBalanceBefore(dqqc, dkll, dkhkfs, dkqs, dkffe).setScale(2, BigDecimal.ROUND_HALF_UP);//不会发生正常还款与提前还款并存情况
            SettleAccounts settleAccounts = CommLoanAlgorithm.settleAccounts(stHousingPersonalAccount.getDkye(), mybj, dkll, bqbx, jxts);
            BigDecimal tqjqdkze = settleAccounts.getTQJQHKZE().setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal sylx = tqjqdkze.subtract(dkye);

            EarlySettlementLoan earlySettlementLoan = new EarlySettlementLoan();
            earlySettlementLoan.setJKRXM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm());//姓名
            earlySettlementLoan.setHKFS(dkhkfs);//贷款还款方式
            earlySettlementLoan.setSYQS("0");//剩余期数
            earlySettlementLoan.setSYBJ(dkye + "");//剩余本金(贷款余额)
            earlySettlementLoan.setSYLX(sylx + "");//提前还款利息
            earlySettlementLoan.setXYHKZE(tqjqdkze + "");//提前结清贷款总额
            earlySettlementLoan.setYDKKRQ(YDKKRQ);
            earlySettlementLoan.setJKRZJHM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrzjh());
            repaymentApplicationInAdvanceGet.setEarlySettlementLoan(earlySettlementLoan);//结清贷款
            return repaymentApplicationInAdvanceGet;
        }
        throw new ErrorException("还款类型： 04逾期还款 03提前部分 06结清贷款");
    }

    /**
     * 办结操作
     */
    @Override
    public final void doAction(String YWLSH) {
        try {
            List<CLoanHousingBusinessProcess> ywlsh = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                this.put("ywlsh", YWLSH);
            }}, null, null, null, null, null, null);
            if (ywlsh.size() == 0) throw new ErrorException("业务不存在:" + YWLSH);
            CLoanApplyRepaymentVice loanApplyRepaymentVice = ywlsh.get(0).getLoanApplyRepaymentVice();
            CLoanHousingPersonInformationBasic byDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(ywlsh.get(0).getDkzh());
            Calendar c = Calendar.getInstance();
            c.setTime(loanApplyRepaymentVice.getYdkkrq());
            ismsCommon.sendSingleSMSWithTemp(byDKZH.getSjhm(),SMSTemp.提前还款业务发起时.getCode(),new ArrayList<String>(){{
                this.add(byDKZH.getJkrxm());
                this.add(loanApplyRepaymentVice.getHkje()+"");
                this.add((c.get(Calendar.YEAR)+"").substring(2));
                this.add(c.get(Calendar.MONTH)+1+"");
                this.add(c.get(Calendar.DATE)+"");
            }});

            if (loanApplyRepaymentVice.getYdkkrq().getTime() <= sim.parse(sim.format(new Date())).getTime()) {
                throw new ErrorException(ReturnEnumeration.Business_FAILED, "必须当天办理，当天审核，请作废重新申请");
            }
            ywlsh.get(0).setBjsj(new Date());
            cloanHousingBusinessProcess.update(ywlsh.get(0));
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 扣款业务失败记录
     */
    @Override
    public final PageRes<FailedBuinessInfo> getFailedBuinessInfo(TokenContext tokenContext, String JKRXM, String DKZH, String stime, String etime, String pageSize, String page, String YHDM, String ZJHM, String HKYWLX,String YWWD) {

        PageResults<StHousingBusinessDetails> stHousingBusinessDetails = null;
        try {
            stHousingBusinessDetails = stHousingBusinessDetailsDAO.listWithPage(new HashMap<String, Object>() {{
                                                                                    if (StringUtil.notEmpty(HKYWLX)) {
                                                                                        this.put("dkywmxlx", HKYWLX);
                                                                                    }
                                                                                    if (StringUtil.notEmpty(YWWD) && !YWWD.equals("1")) {
                                                                                        this.put("cHousingBusinessDetailsExtension.ywwd", YWWD);
                                                                                    }
                                                                                    this.put("cHousingBusinessDetailsExtension.ywzt", TaskBusinessStatus.入账失败.getName());
                                                                                    if (StringUtil.notEmpty(YHDM)) this.put("cHousingBusinessDetailsExtension.zhkhyhdm", YHDM);
                                                                                }}, !StringUtil.notEmpty(stime) ? null : simpleall.parse(stime), !StringUtil.notEmpty(etime) ? null : simpleall.parse(etime), null, null, null, null, StringUtil.notEmpty(page) ? Integer.parseInt(page) : null,
                    StringUtil.notEmpty(pageSize) ? Integer.parseInt(pageSize) : null, new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            if (StringUtil.notEmpty(JKRXM)) {
                                criteria.add(Restrictions.like("cHousingBusinessDetailsExtension.jkrxm", "%" + JKRXM + "%"));
                            }
                            if (StringUtil.notEmpty(DKZH)) {
                                criteria.add(Restrictions.like("dkzh", "%" + DKZH + "%"));
                            }
                        }
                    });
        } catch (ParseException e) {
            throw new ErrorException(e);
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        PageRes<FailedBuinessInfo> pageres = new PageRes<>();
        List<StHousingBusinessDetails> housingBusinessDetailslist = stHousingBusinessDetails.getResults();
        ArrayList<FailedBuinessInfo> failedBuinessInfo = new ArrayList<>();
        FailedBuinessInfo ailedBuinessInfo = null;
        CLoanHousingPersonInformationBasic byDKZH;
        StCommonUnit unit;
        int totalCount = stHousingBusinessDetails.getTotalCount();
        for (StHousingBusinessDetails singBusinessDetails : housingBusinessDetailslist) {
            ailedBuinessInfo = new FailedBuinessInfo();
            byDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(singBusinessDetails.getDkzh());

            ailedBuinessInfo.setID(singBusinessDetails.getId());
            ailedBuinessInfo.setZJHM(byDKZH.getJkrzjhm());
            ailedBuinessInfo.setYWLSH(singBusinessDetails.getYwlsh());
            try {
                ailedBuinessInfo.setJKRXM(singBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm());
                ailedBuinessInfo.setSBYY(singBusinessDetails.getcHousingBusinessDetailsExtension().getSbyy());
                ailedBuinessInfo.setRGCL(singBusinessDetails.getcHousingBusinessDetailsExtension().getRgcl());
                ailedBuinessInfo.setSWTYH(singBusinessDetails.getcHousingBusinessDetailsExtension().getZhkhyhmc());
            } catch (Exception e) {
            }
            ailedBuinessInfo.setDKZH(singBusinessDetails.getDkzh());
            ailedBuinessInfo.setYWLX(singBusinessDetails.getDkywmxlx());
            if (singBusinessDetails.getYwfsrq() != null) {
                ailedBuinessInfo.setYWFSRQ(sim.format(singBusinessDetails.getYwfsrq()));
            }
            ailedBuinessInfo.setFSE(singBusinessDetails.getFse() + "");
            ailedBuinessInfo.setBJJE(singBusinessDetails.getBjje() + "");
            ailedBuinessInfo.setLXJE(singBusinessDetails.getLxje() + "");
            ailedBuinessInfo.setDQQC(singBusinessDetails.getDqqc() + "");
            failedBuinessInfo.add(ailedBuinessInfo);
        }
        pageres.setResults(failedBuinessInfo);
        pageres.setCurrentPage(stHousingBusinessDetails.getCurrentPage());
        pageres.setNextPageNo(stHousingBusinessDetails.getPageNo());
        pageres.setPageCount(stHousingBusinessDetails.getPageCount());
        pageres.setPageSize(stHousingBusinessDetails.getPageSize());
        pageres.setTotalCount(totalCount);

        return pageres;
    }

    /**
     * 重新划扣,修改状态
     */
    @Override
    public final CommonResponses putFailedBuinessSubmit(TokenContext tokenContext, String ID, String CZLX) {
        synchronized (lock) {
            if (!StringUtil.notEmpty(CZLX) || (!CZLX.equals("0") && !CZLX.equals("1") && !CZLX.equals("2")))
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "0实际扣款成功，线下处理  1重新划扣 2作废");
            if (!StringUtil.notEmpty(ID)) throw new ErrorException(ReturnEnumeration.Data_MISS, "参数不能为空(id)");
            StHousingBusinessDetails husingBusinessDetails = stHousingBusinessDetailsDAO.get(ID);
            try {
                if (LoanBusinessType.逾期还款.getCode().equals(husingBusinessDetails.getDkywmxlx())) {
                    List<StHousingOverdueRegistration> stHousingOverdueRegistrations = housingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                        this.put("yqqc", husingBusinessDetails.getDqqc());//逾期期次
                        this.put("dkzh", husingBusinessDetails.getDkzh());//贷款账号
                        this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.入账失败.getName());
                    }}, null, null, null, null, null, null);
                    if (stHousingOverdueRegistrations.size() == 0)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "失败记录不存在");
                    if (CZLX.equals("0")) {//成功
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                        husingBusinessDetails.getGrywmx().setStep(TaskBusinessStatus.已入账.getName());
                        husingBusinessDetails.setJzrq(new Date());
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                        icommCheckMethod.OverdueDunksPlan(husingBusinessDetails, stHousingOverdueRegistrations.get(0));
                    } else if (CZLX.equals("1") || CZLX.equals("2")) {//重划扣
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已作废.getName());
                        husingBusinessDetails.getGrywmx().setStep(TaskBusinessStatus.已作废.getName());
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                        stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.待入账.getName());
                    } else {
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "只能选择重扣划和作废");
                    }
                    housingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                } else if (LoanBusinessType.正常还款.getCode().equals(husingBusinessDetails.getDkywmxlx())) {
                    if (CZLX.equals("0")) {
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                        husingBusinessDetails.getGrywmx().setStep(TaskBusinessStatus.已入账.getName());
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                        husingBusinessDetails.setJzrq(new Date());
                        icommCheckMethod.NormalRepaymentReadjustmentDeduction(husingBusinessDetails);
                    } else
                    if (CZLX.equals("1")) {
//                        StHousingPersonalAccount byDkzh = housingfundPersonalAccount.getByDkzh(husingBusinessDetails.getDkzh());
//                        Date periodOfafterTime = CommLoanAlgorithm.periodOfafterTime(byDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq(), byDkzh.getcLoanHousingPersonalAccountExtension().getDqqc().intValue());
//                        if (sim.parse(sim.format(new Date())).getTime() <= periodOfafterTime.getTime() + 432000000) {//5天
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.待入账.getName());
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                        husingBusinessDetails.getGrywmx().setStep(TaskBusinessStatus.已入账.getName());
//                        }
                    } else {
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "只能选择重扣划");
                    }
                } else {
                    if (CZLX.equals("0")) {
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已入账.getName());
                        husingBusinessDetails.setJzrq(new Date());

                        if (LoanBusinessType.提前还款.getCode().equals(husingBusinessDetails.getDkywmxlx())) {
                            icommCheckMethod.partialRepaymentAccount(husingBusinessDetails);
                        } else if (LoanBusinessType.结清.getCode().equals(husingBusinessDetails.getDkywmxlx())) {
                            icommCheckMethod.partialClearingAccount(husingBusinessDetails);
                        }
                    } else if (CZLX.equals("1")) {
                        if (sim.parse(sim.format(new Date())).getTime() <= sim.parse(sim.format(husingBusinessDetails.getYwfsrq())).getTime()) {
                            husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已作废.getName());
                            husingBusinessDetails.setDeleted(true);
                            husingBusinessDetails.setDeleted_at(new Date());
                            stHousingBusinessDetailsDAO.delete(husingBusinessDetails);

                            CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(husingBusinessDetails.getYwlsh());
                            byYWLSH.setStep(LoanBussinessStatus.待入账.getName());
                            cloanHousingBusinessProcess.update(byYWLSH);

                            return new CommonResponses() {{
                                this.setId(husingBusinessDetails.getYwlsh());
                                this.setState("success");
                            }};
                        } else {
                            husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已作废.getName());
                            husingBusinessDetails.getcHousingBusinessDetailsExtension().setUpdated_at(new Date());
                            husingBusinessDetails.setDeleted(true);
                            husingBusinessDetails.setDeleted_at(new Date());

                            CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(husingBusinessDetails.getYwlsh());
                            byYWLSH.setStep(LoanBussinessStatus.已作废.getName());
//                            byYWLSH.setDeleted(true);
                            cloanHousingBusinessProcess.update(byYWLSH);
                        }
                    } else if (CZLX.equals("2")) {
                        husingBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.已作废.getName());
                        husingBusinessDetails.setDeleted(true);
                        husingBusinessDetails.setDeleted_at(new Date());
                        CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(husingBusinessDetails.getYwlsh());
                        byYWLSH.setStep(LoanBussinessStatus.已作废.getName());
//                        byYWLSH.setDeleted(true);
                        cloanHousingBusinessProcess.update(byYWLSH);
                    }
                }
                stHousingBusinessDetailsDAO.update(husingBusinessDetails);
            } catch (ParseException e) {
                throw new ErrorException(e);
            } catch (Exception e) {
                throw new ErrorException(e);
            }
            return new CommonResponses() {{
                this.setId(husingBusinessDetails.getYwlsh());
                this.setState("success");
            }};
        }
    }

    /**
     * 修改逾期 胡昊林
     */
    @Override
    public final GetOverdueModification getOverdueModification(TokenContext tokenContext, String ywlsh, String DKZH, String YDKKRQ) {
        if (true) throw new ErrorException("此功能已下线，改为自动还款流程");
        Date ydkkrq = null;
        try {
            ydkkrq = sim.parse(YDKKRQ);
            if (ydkkrq.getTime() < sim.parse(sim.format(new Date())).getTime())
                throw new ErrorException("约定扣款日期必须大于等于当前日期");
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式：yyyy-MM-dd");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcesses = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
            this.put("cznr", LoanBusinessType.逾期还款.getCode());
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        if (cLoanHousingBusinessProcesses.size() == 0) throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");
        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DKZH)) this.put("dkzh", DKZH);
            this.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        if (cLoanHousingPersonInformationBasics.size() == 0)
            throw new ErrorException("贷款账号不存在|账户状态异常");
        StHousingPersonalAccount stHousingPersonalAccount = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
        if (stHousingPersonalAccount == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账户信息丢失");
        //需要外部字段
        Calendar calendar = Calendar.getInstance();
        String dkhkfs = "";
        BigDecimal dkffe = null;
        try {
            dkhkfs = stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs();//贷款还款方式
            dkffe = stHousingPersonalAccount.getDkffe();//贷款发放额
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账户合同信息不完整");
        }
        if (!RepaymentMethod.等额本息.getCode().equals(stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs()) && !RepaymentMethod.等额本金.getCode().equals(stHousingPersonalAccount.getStHousingPersonalLoan().getDkhkfs()))
            throw new ErrorException(DKZH + "：DKHKFS数据库错误,或者02本金01本息");
        if (stHousingPersonalAccount.getDkffe() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账号贷款发放额为空");
        if (stHousingPersonalAccount.getStHousingPersonalLoan().getDkqs() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "合同贷款期数不能为空");
        if (stHousingPersonalAccount.getDkffrq() == null || stHousingPersonalAccount.getDkffrq().getTime() > new Date().getTime())
            throw new ErrorException("贷款日期不能为空,并且不大于当前时间");
        if (stHousingPersonalAccount.getDkye() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账户合同贷款余额为空");
        if (stHousingPersonalAccount.getDkll() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "合同贷款利率为空");
        ObjectAttributeCheck.checkDataType(new HashMap<String, String>() {{
            this.put("dkffe", stHousingPersonalAccount.getDkffe().toString());
            this.put("dkqs", stHousingPersonalAccount.getStHousingPersonalLoan().getDkqs().toString());
            this.put("dkye", stHousingPersonalAccount.getDkye().toString());
            this.put("dkll", stHousingPersonalAccount.getDkll().toString());
        }});
        BigDecimal dkll = new BigDecimal(stHousingPersonalAccount.getDkll().doubleValue());//贷款利率
        Date dkffrq = stHousingPersonalAccount.getDkffrq();//贷款发生日期
        List<StHousingOverdueRegistration> stHousingOverdueRegistration = housingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", DKZH);
            this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.待入账.getName());
        }}, null, null, null, null, ListDeleted.NOTDELETED, null);
        GetOverdueModification repaymentGetInfomation = null;
        repaymentGetInfomation = new GetOverdueModification();
        repaymentGetInfomation.setJKRXM(stHousingPersonalAccount.getStHousingPersonalLoan().getJkrxm());//姓名
        ArrayList<GetOverdueModificationList> list = new ArrayList<>();
        GetOverdueModificationList repaymentGetInfomationList = null;
        BigDecimal total = BigDecimal.ZERO;
        if (cLoanHousingBusinessProcesses.get(0).getLoanApplyRepaymentVice() == null)
            throw new ErrorException("还款申请信息数据缺失");
        for (StHousingOverdueRegistration housingOverdueDetails : stHousingOverdueRegistration) {
            repaymentGetInfomationList = new GetOverdueModificationList();
            for (CLoanHousingOverdueVice sousingOverdueVice : cLoanHousingBusinessProcesses.get(0).getLoanApplyRepaymentVice().getcLoanHousingOverdueVices()) {
                if (housingOverdueDetails.getYqqc().equals(sousingOverdueVice.getYqqc())) {
                    repaymentGetInfomationList.setSFXZ("0");
                } else {
                    repaymentGetInfomationList.setSFXZ("1");
                }
            }
            repaymentGetInfomationList.setYQQC(housingOverdueDetails.getYqqc() + "");//逾期期次
            repaymentGetInfomationList.setYQBJ(housingOverdueDetails.getYqbj() + "");//逾期本金
            repaymentGetInfomationList.setYQLX(housingOverdueDetails.getYqlx() + "");//逾期利息
            BigDecimal yqfx = CommLoanAlgorithm.overdueFX(housingOverdueDetails.getYqbj(), housingOverdueDetails.getYqlx(),
                    dkll, dkffrq, housingOverdueDetails.getYqqc().intValue(), ydkkrq);//逾期罚息
            repaymentGetInfomationList.setYQFX(yqfx.setScale(2, BigDecimal.ROUND_HALF_UP) + "");//逾期罚息
            total = total.add(housingOverdueDetails.getYqbj()).add(housingOverdueDetails.getYqlx()).add(yqfx.setScale(2, BigDecimal.ROUND_HALF_UP));
            list.add(repaymentGetInfomationList);
        }
        repaymentGetInfomation.setYQKZJ(total.toString());//逾期款总计
        repaymentGetInfomation.setGetOverdueModificationList(list);//逾期统计集合
        return repaymentGetInfomation;
    }

    @Override
    public PageResNew<FailedBuinessInfo> getFailedBuinessInfonew(TokenContext tokenContext, String JKRXM, String DKZH, String stime, String etime, String pageSize, String marker, String action, String YHDM) {
        PageResults<StHousingBusinessDetails> stHousingBusinessDetails = null;
        try {
            stHousingBusinessDetails = stHousingBusinessDetailsDAO.listWithMarker(new HashMap<String, Object>() {{
//                if (StringUtil.notEmpty(JKRXM)) this.put("cHousingBusinessDetailsExtension.jkrxm", JKRXM);
//                if (StringUtil.notEmpty(DKZH)) this.put("dkzh", DKZH);
                                                                                      this.put("dkywmxlx", Arrays.asList(LoanBusinessType.正常还款.getCode(), LoanBusinessType.逾期还款.getCode(), LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
                                                                                      this.put("cHousingBusinessDetailsExtension.ywzt", TaskBusinessStatus.入账失败.getName());
                                                                                  }}, !StringUtil.notEmpty(stime) ? null : simpleall.parse(stime), !StringUtil.notEmpty(etime) ? null : simpleall.parse(etime), null, null, null, null, marker,
                    StringUtil.notEmpty(pageSize) ? Integer.parseInt(pageSize) : null, ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            if (StringUtil.notEmpty(JKRXM)) {
                                criteria.add(Restrictions.like("cHousingBusinessDetailsExtension.jkrxm", "%" + JKRXM + "%"));
                            }
                            if (StringUtil.notEmpty(DKZH)) {
                                criteria.add(Restrictions.like("dkzh", "%" + DKZH + "%"));
                            }
                        }
                    });
        } catch (ParseException e) {
            throw new ErrorException(e);
        } catch (Exception e) {

        }

        PageResNew<FailedBuinessInfo> pageres = new PageResNew<>();
        List<StHousingBusinessDetails> housingBusinessDetailslist = stHousingBusinessDetails.getResults();
        ArrayList<FailedBuinessInfo> failedBuinessInfo = new ArrayList<>();
        FailedBuinessInfo ailedBuinessInfo = null;
        CLoanHousingPersonInformationBasic byDKZH = null;
        StCommonUnit unit;
        for (StHousingBusinessDetails singBusinessDetails : housingBusinessDetailslist) {
            if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                byDKZH = icloanHousingPersonInformationBasicDAO.getByDKZH(singBusinessDetails.getDkzh());
                if (byDKZH != null) {
                    unit = common_unit.getUnit(byDKZH.getDkzh());
                    if (unit != null) {
                        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) ;
                        continue;
                    }
                }
            }
            if (StringUtil.notEmpty(YHDM)) {
                if (!singBusinessDetails.getDkyhdm().equals(YHDM)) continue;
            }
            ailedBuinessInfo = new FailedBuinessInfo();
            ailedBuinessInfo.setId(singBusinessDetails.getId());
            ailedBuinessInfo.setID(singBusinessDetails.getId());
            ailedBuinessInfo.setYWLSH(singBusinessDetails.getYwlsh());
            try {
                ailedBuinessInfo.setJKRXM(singBusinessDetails.getcHousingBusinessDetailsExtension().getJkrxm());
                ailedBuinessInfo.setSBYY(singBusinessDetails.getcHousingBusinessDetailsExtension().getSbyy());
                ailedBuinessInfo.setRGCL(singBusinessDetails.getcHousingBusinessDetailsExtension().getRgcl());
            } catch (Exception e) {
            }
            ailedBuinessInfo.setDKZH(singBusinessDetails.getDkzh());
            ailedBuinessInfo.setYWLX(singBusinessDetails.getDkywmxlx());
            if (singBusinessDetails.getYwfsrq() != null) {
                ailedBuinessInfo.setYWFSRQ(sim.format(singBusinessDetails.getYwfsrq()));
            }
            ailedBuinessInfo.setFSE(singBusinessDetails.getFse() + "");
            ailedBuinessInfo.setBJJE(singBusinessDetails.getBjje() + "");
            ailedBuinessInfo.setLXJE(singBusinessDetails.getLxje() + "");
            ailedBuinessInfo.setDQQC(singBusinessDetails.getDqqc() + "");
            failedBuinessInfo.add(ailedBuinessInfo);
        }
        pageres.setResults(action, failedBuinessInfo);
        return pageres;
    }

    @Override
    public PageResNew<HousingRepaymentApplyListGetRes> getHousingRepaymentApplyListnew(TokenContext tokenContext, String DKZH, String JKRXM, String pageSize, String marker, String KSSJ, String JSSJ, String action, String YHDM) {
        PageResults<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = this.cloanHousingBusinessProcess.listWithMarker(new HashMap<String, Object>() {{
                                                                                         this.put("cznr", Arrays.asList(LoanBusinessType.逾期还款.getCode(), LoanBusinessType.提前还款.getCode(), LoanBusinessType.结清.getCode()));
                                                                                         if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
                                                                                             this.put("ywwd.id", tokenContext.getUserInfo().getYWWD());
                                                                                         }
                                                                                     }}, !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ),
                    !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ), "created_at", Order.DESC, null, null, marker, Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.createAlias("loanApplyRepaymentVice", "loanApplyRepaymentVice");
                            if (StringUtil.notEmpty(DKZH)) {
                                criteria.add(Restrictions.like("loanApplyRepaymentVice.dkzh", "%" + DKZH + "%"));
                            }
                            if (StringUtil.notEmpty(JKRXM)) {
                                criteria.add(Restrictions.like("loanApplyRepaymentVice.jkrxm", "%" + JKRXM + "%"));
                            }
                        }
                    });
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        if (housingBusinessProcess == null) throw new ErrorException("业务数据不存在");
        PageResNew<HousingRepaymentApplyListGetRes> pageres = new PageResNew<>();
        List<CLoanHousingBusinessProcess> housingBusinessProcesslist = housingBusinessProcess.getResults();
        ArrayList<HousingRepaymentApplyListGetRes> listhousingRepaymentApplyListGetRes = new ArrayList<>();
        HousingRepaymentApplyListGetRes housingRepaymentApplyListGetRes = null;
        StHousingPersonalAccount byDKZH = null;
        for (CLoanHousingBusinessProcess BusinessProcess : housingBusinessProcesslist) {
            if (LoanBussinessStatus.入账失败.getName().equalsIgnoreCase(BusinessProcess.getStep())) continue;
            byDKZH = housingfundPersonalAccount.getByDkzh(BusinessProcess.getDkzh());
            if (StringUtil.notEmpty(YHDM)) {
                if (byDKZH != null && byDKZH.getStHousingPersonalLoan() != null) {
                    if (StringUtil.isEmpty(byDKZH.getStHousingPersonalLoan().getSwtyhdm()) || !byDKZH.getStHousingPersonalLoan().getSwtyhdm().equals(YHDM))
                        continue;
                }
            }
            housingRepaymentApplyListGetRes = new HousingRepaymentApplyListGetRes();
            try {
                housingRepaymentApplyListGetRes.setId(BusinessProcess.getId());
                housingRepaymentApplyListGetRes.setDKZH(BusinessProcess.getLoanApplyRepaymentVice().getDkzh());//贷款账号
                housingRepaymentApplyListGetRes.setJKRXM(BusinessProcess.getLoanApplyRepaymentVice().getJkrxm());//经办人姓名
                housingRepaymentApplyListGetRes.setJKRZJHM(BusinessProcess.getLoanApplyRepaymentVice().getJkrzjhm());//证件号码
                housingRepaymentApplyListGetRes.setSQHKLX(BusinessProcess.getLoanApplyRepaymentVice().getHklx() + "");//还款类型
                housingRepaymentApplyListGetRes.setSQHKJE(BusinessProcess.getLoanApplyRepaymentVice().getHkje() + "");//还款金额
                housingRepaymentApplyListGetRes.setYDKKRQ(sim.format(BusinessProcess.getLoanApplyRepaymentVice().getYdkkrq()));//约定扣款日期
            } catch (ErrorException e) {
                continue;
            }
            housingRepaymentApplyListGetRes.setSLSJ(simm.format(BusinessProcess.getCreated_at()));//办理时间
            housingRepaymentApplyListGetRes.setYWLSH(BusinessProcess.getYwlsh());//业务流水号
            housingRepaymentApplyListGetRes.setStatus(BusinessProcess.getStep() + "");//状态
            listhousingRepaymentApplyListGetRes.add(housingRepaymentApplyListGetRes);//
        }
        pageres.setResults(action, listhousingRepaymentApplyListGetRes);
        return pageres;
    }


    public String[] repamentAllMoney(String jkrzjhm) {
        try {
            List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = icloanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                this.put("jkrzjhm", jkrzjhm);
            }}, null, null, null, null, null, null);
            StHousingPersonalAccount stHousingPersonalAccountByDkzh = cLoanHousingPersonInformationBasics.get(0).getPersonalAccount();
            List<StHousingOverdueRegistration> stHousingOverdueRegistrations = housingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                this.put("dkzh", cLoanHousingPersonInformationBasics.get(0).getDkzh());
                this.put("cHousingOverdueRegistrationExtension.ywzt", Arrays.asList(LoanBussinessStatus.待入账.getName(), LoanBussinessStatus.扣款已发送.getName(), LoanBussinessStatus.入账失败.getName()));
            }}, null, null, null, null, null, null);
            BigDecimal fse = BigDecimal.ZERO;
            BigDecimal yqfxall = BigDecimal.ZERO;
            for (StHousingOverdueRegistration overdueRegistrations : stHousingOverdueRegistrations) {
                BigDecimal dkll = CommLoanAlgorithm.lendingRate(stHousingPersonalAccountByDkzh.getDkll(), stHousingPersonalAccountByDkzh.getLlfdbl());
                BigDecimal yqfx = CommLoanAlgorithm.overdueFX(overdueRegistrations.getYqbj(), overdueRegistrations.getYqlx(),
                        dkll, stHousingPersonalAccountByDkzh.getDkffrq(), overdueRegistrations.getYqqc().intValue(), sim.parse(sim.format(new Date())));//逾期罚息
                fse = overdueRegistrations.getYqbj().add(overdueRegistrations.getYqlx().add(yqfx)).setScale(2, BigDecimal.ROUND_HALF_UP);
                yqfxall = yqfxall.add(yqfx);
            }

            BigDecimal dkll = CommLoanAlgorithm.lendingRate(stHousingPersonalAccountByDkzh.getDkll(), stHousingPersonalAccountByDkzh.getLlfdbl());//贷款利率
            int dkqs = Integer.parseInt(stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhqs().toString());//贷款期数
            BigDecimal dkye = stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye();//贷款余额
            int dqqc = CommLoanAlgorithm.currentQS(stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq(), new Date());//根据时间计算的期次
            int dqhkqc = stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDqqc().intValue();//数据库当期应还期次
            int jxts = CommLoanAlgorithm.beforeInterestDays(stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkxffrq(), dqqc, new Date());//计息天数
            int syqc = dkqs - dqqc;//剩余期数
            BigDecimal mybj = stHousingPersonalAccountByDkzh.getDqyhbj();

            BigDecimal bqbx = stHousingPersonalAccountByDkzh.getDqyhje();
            dkye = CommLoanAlgorithm.loanBalanceBefore(dqqc, dkll, stHousingPersonalAccountByDkzh.getStHousingPersonalLoan().getDkhkfs(), dkqs, stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye()).setScale(2, BigDecimal.ROUND_HALF_UP);//不会发生正常还款与提前还款并存情况
            SettleAccounts settleAccounts = CommLoanAlgorithm.settleAccounts(stHousingPersonalAccountByDkzh.getcLoanHousingPersonalAccountExtension().getDkgbjhye(), mybj, dkll, bqbx, jxts);
            BigDecimal tqjqdkze = settleAccounts.getTQJQHKZE().setScale(2, BigDecimal.ROUND_HALF_UP);


            return new String[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
