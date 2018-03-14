package com.handge.housingfund.collection.service.withdrawl;

import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.LoanWithdrawl;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.individual.ComMessage;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.*;
import com.handge.housingfund.common.service.collection.service.common.CommonOps;
import com.handge.housingfund.common.service.collection.service.common.ICalculateInterest;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSeal;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionWithdrawlTrader;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.enumeration.ErrorEnumeration;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.ILoanTaskService;
import com.handge.housingfund.common.service.loan.IRepaymentService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.dao.impl.StHousingBusinessDetailsDAO;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/22.
 * 描述
 */
@SuppressWarnings("Duplicates")
@Service
public class WithdrawlTasksImpl implements WithdrawlTasks {
    private static Logger logger = LogManager.getLogger(WithdrawlTasksImpl.class);
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStateMachineService iStateMachineService;
    @Autowired
    private ICCollectionWithdrawViceDAO withdrawlViceDAO;
    @Autowired
    private ICAuditHistoryDAO iAuditHistoryDAO;
    @Autowired
    private IBank iBank;
    @Autowired
    private ICollectionWithdrawlTrader collectionWithdrawlTrader;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;
    @Autowired
    private ICalculateInterest calculateInterest;

    @Autowired
    private CommonOps commonOps;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IRepaymentService repaymentService;
    @Autowired
    private ILoanTaskService loanTaskService;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    private IBankInfoService iBankInfoService;
    @Autowired
    private ICCollectionWithdrawlBusinessExtensionDAO withdrawlBusinessExtensionDAO;

    @Autowired
    private IStHousingBusinessDetailsDAO housingBusinessDetailsDAO;

    @Autowired
    private IndiAcctSeal indiAcctSeal;

    @Autowired
    private ISMSCommon ismsCommon;

    private static String format = "yyyy-MM-dd";

    private static String xctqrqFormat = "yyyyMMdd";

    private static String searchFormat = "yyyy-MM-dd HH:mm";

    private static String df = "yyyy-MM-dd HH:mm:ss";


    public WithdrawlsDetailInfo getWithdrawlDetail(TokenContext tokenContext, String taskId) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        if (StringUtil.isEmpty(taskId)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", taskId);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        if (collectionPersonalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "提取业务不存在");
        }
        StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();
        CCollectionWithdrawlVice withdrawlVice = collectionPersonalBusinessDetails.getWithdrawlVice();
        CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = instance(withdrawlBusinessExtensionDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("stCollectionPersonalBusinessDetails", collectionPersonalBusinessDetails);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });

        WithdrawlsDetailInfo detailInfo = new WithdrawlsDetailInfo();
        try {
            detailInfo.setIndiAcctInfo(new IndiAcctInfo() {
                {
                    this.setYwlsh(taskId);//业务流水号
                    this.setGrzh(commonPerson.getGrzh());//个人账号
                    this.setXingMing(commonPerson.getXingMing());//姓名
                    this.setZjlx(commonPerson.getZjlx());//证件类型"01" "02" "03" "04" "99"
                    this.setZjhm(commonPerson.getZjhm());//证件号码
                    this.setDwmc(commonPerson.getUnit().getDwmc());//单位名称
                    this.setJzny(DateUtil.str2str(commonPerson.getExtension().getGrjzny(), 6));// 缴至年月===============================
                    this.setGrzhzt(commonPerson.getCollectionPersonalAccount().getGrzhzt());/// 个人账户状态
                    this.setGrzhye(!Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName()).contains(collectionPersonalBusinessDetails.getExtension().getStep()) ? commonPerson.getCollectionPersonalAccount().getGrzhye().toString() :
                            collectionPersonalBusinessDetails.getExtension().getDqye().toString());// 个人账户余额
                }
            });
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
        }
        try {
            detailInfo.setWithdrawlsInfo(new WithdrawlsInfo() {
                {
//            审核状态(00所有 01新建 02待审核 03待入账 04已入账 05审核不通过)
                    this.setDkzjhm(collectionPersonalBusinessDetails.getExtension().getJbrzjhm());//这里保存的是借款人证件号码
                    this.setGrckzhhm(collectionPersonalBusinessDetails.getExtension().getGrckzhhm());//个人存款账户号码
                    this.setHuMing(collectionPersonalBusinessDetails.getExtension().getHuming());//户名
                    this.setGrckzhkhyhmc(collectionPersonalBusinessDetails.getExtension().getGrckzhkhyhmc());//个人存款账户开户银行名称
                    this.setJqe(collectionPersonalBusinessDetails.getExtension().getJqe().toString());//结清额
                    if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDetails.getTqyy()) && collectionPersonalBusinessDetails.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                        this.setHkfs(collectionPersonalBusinessDetails.getExtension().getHkfs());//还款方式

                    }
//                    if (CollectionBusinessStatus.新建.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ||
//                        StringUtil.isIntoReview(collectionPersonalBusinessDetails.getExtension().getStep(),null) ||
//                        CollectionBusinessStatus.审核不通过.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep())
//                        ) {
////                        从副表获取数据
//                        this.setTqyy(withdrawlVice.getTqyy());//提取原因
//                        this.setYhke(withdrawlVice.getYhke());//月还款额
//                        this.setXctqrq(DateUtil.date2Str(withdrawlVice.getXctqrq(),format));//下次提取日期
//                        this.setTqfs(withdrawlVice.getTqfs());//提取方式
//                        this.setYwmxlx(withdrawlVice.getYwmxlx());//业务明细类型  11部分提取  12销户提取
//                        this.setXhyy(withdrawlVice.getXhyy());//销户原因
//                        this.setBlr(withdrawlVice.getBlr());//办理人（本人/代理人）
//                        this.setDlrxm(withdrawlVice.getDlrxm());//代理人姓名
//                        this.setDlrzjlx(withdrawlVice.getDlrzjlx());//代理人类型
//                        this.setDlrzjhm(withdrawlVice.getDlrzjhm());//代理人号码
//                        this.setFse(withdrawlVice.getFse().abs());//发生额
//                        if (CollectionBusinessType.销户提取.getCode().equals(this.getYwmxlx())) {//销户提取获取发生利息额
//                            this.setFslxe(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe());
//                        } else {
//                            this.setFslxe(null);
//                        }
//                    } else {//从主表获取
                    this.setTqyy(collectionPersonalBusinessDetails.getTqyy());//提取原因
                    if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDetails.getTqyy())
                            && collectionPersonalBusinessDetails.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) == 0 && !"系统".equals(collectionPersonalBusinessDetails.getExtension().getCzy())) {
                        this.setYhke(collectionPersonalBusinessDetails.getExtension().getYhke().toString());//月还款额
                        this.setXctqrq(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getXctqrq(), format));//下次提取日期
                    }
                    this.setTqfs(collectionPersonalBusinessDetails.getTqfs());//提取方式
                    this.setYwmxlx(collectionPersonalBusinessDetails.getGjhtqywlx());//业务明细类型 11部分提取  12销户提取
                    this.setXhyy(commonPerson.getCollectionPersonalAccount().getXhyy());//销户原因
                    this.setBlr(collectionPersonalBusinessDetails.getExtension().getBlr());//办理人（本人/代理人）

                    if (collectionPersonalBusinessDetails.getExtension().getBlr().equals("02")) {
                        this.setDlrxm(collectionPersonalBusinessDetails.getExtension().getDlrxm());//代理人姓名
                        this.setDlrzjlx(collectionPersonalBusinessDetails.getExtension().getDlrzjlx());//代理人类型
                        this.setDlrzjhm(collectionPersonalBusinessDetails.getExtension().getDlrzjhm());//代理人号码
                    }
                    this.setFse(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? withdrawlVice.getFse().abs().toString()
                            : collectionPersonalBusinessDetails.getFse().abs().toString());//发生额
                    if (CollectionBusinessType.销户提取.getCode().equals(this.getYwmxlx())) {//销户提取或系统自动发起的贷款获取发生利息额
                        this.setFslxe(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs().toString()
                                : collectionPersonalBusinessDetails.getFslxe().abs().toString());
                    }
                    if (CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep())) {
                        this.setJzrq(DateUtil.date2Str(collectionPersonalBusinessDetails.getJzrq(), format));//记账日期
                    }
                    this.setQttqbz(withdrawlBussinessExtension != null ? withdrawlBussinessExtension.getQttqbz() : null);
//                    }
                }
            });
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "提取信息");
        }
        try {
            List<CAuditHistory> cAuditHistories = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("ywlsh", taskId);
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    exceptions.add(e);
                }
            });
            ArrayList<ReviewInfo> reviewInfoList = new ArrayList<>();
            for (CAuditHistory cAuditHistory : cAuditHistories) {
                if (!StringUtil.isEmpty(cAuditHistory.getShjg())) {
                    ReviewInfo reviewInfo = new ReviewInfo();
                    reviewInfo.setYWLSH(taskId);
                    reviewInfo.setSHJG(cAuditHistory.getShjg());
                    reviewInfo.setYYYJ(cAuditHistory.getYyyj());
                    reviewInfo.setCZY(cAuditHistory.getCzy());
                    reviewInfo.setYWWD(cAuditHistory.getYwwd());
                    reviewInfo.setZhiWu(cAuditHistory.getZhiwu());
                    reviewInfo.setCZQD(cAuditHistory.getCzqd());
                    reviewInfo.setBeiZhu(cAuditHistory.getBeiZhu());
                    reviewInfoList.add(reviewInfo);
                }
            }
            detailInfo.setReviewInfos(reviewInfoList);
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "审核信息");
        }
        try {
            detailInfo.setWithdrawlReceiBankInfo(new WithdrawlReceiBankInfo() {{
                this.setYhzhhm(withdrawlBussinessExtension != null ? withdrawlBussinessExtension.getSkyhzhhm() : null);
                this.setYhhm(withdrawlBussinessExtension != null ? withdrawlBussinessExtension.getSkyhhm() : null);
                this.setYhmc(withdrawlBussinessExtension != null ? withdrawlBussinessExtension.getSkyhmc() : null);
            }});
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "本次收款银行账户信息");
        }

        try {
            detailInfo.setYwlsh(collectionPersonalBusinessDetails.getYwlsh());
            detailInfo.setBlzl(collectionPersonalBusinessDetails.getExtension().getBlzl());//办理资料
            detailInfo.setCzy(collectionPersonalBusinessDetails.getExtension().getCzy());
            detailInfo.setYwwd(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());
            List<String> lshs = collectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(taskId);
            BigDecimal tqhj = new BigDecimal(0);
            for (String lsh : lshs) {

                StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails2 = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", lsh);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });
                BigDecimal fse = new BigDecimal(0);
                BigDecimal fslxe = new BigDecimal(0);
                fse = !CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails2.getExtension().getStep()) ? collectionPersonalBusinessDetails2.getWithdrawlVice().getFse().abs()
                        : collectionPersonalBusinessDetails2.getFse().abs();
                fslxe = !CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails2.getExtension().getStep()) ? collectionPersonalBusinessDetails2.getExtension().getXhtqfslxe().abs()
                        : collectionPersonalBusinessDetails2.getFslxe().abs();
                tqhj = tqhj.add(fse).add(fslxe);
            }

            detailInfo.setTqhj(tqhj.toString());
//            }
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号、办理资料、操作员或业务网点");
        }
        return detailInfo;
    }

    @Override
    public ArrayList<WithdrawlsDetailInfo> getWithdrawlDetails(TokenContext tokenContext, String taskId) {
        ArrayList<WithdrawlsDetailInfo> infos = new ArrayList<>();
        String pch = collectionPersonalBusinessDetailsDAO.getByYwlsh(taskId).getExtension().getPch();
        if (pch == null) {
            infos.add(getWithdrawlDetail(tokenContext, taskId));
        } else {
            List<String> ywlshs = collectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(taskId);
            for (String lsh : ywlshs) {
                infos.add(getWithdrawlDetail(tokenContext, lsh));
            }
        }
        return infos;
    }

    public void update(TokenContext tokenContext, String op, ReWithdrawlsInfo info) {
        Long crtime = System.currentTimeMillis();
        //region验证
        if (StringUtil.isEmpty(op)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "操作类型（保存或提交）不能为空");
        }
        if (!Arrays.asList("0", "1").contains(op)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型（保存或提交）错误");
        }

        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getTqyy())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "提取原因不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getTqfs())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "提取方式不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getYwmxlx())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务明细类型不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getFse().toString()) || !StringUtil.isMoney(info.getWithdrawlsInfo().getFse())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "发生额不能为空或有误");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getGrckzhhm())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "个人存款账户号码不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getBlr())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "办理人不能为空");
        }
        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.提取.getCode(),
                new HashMap<String, String>() {{
                    this.put(WithDrawalReason.REASON_1.getCode(), UploadFileBusinessType.购买住房.getCode());
                    this.put(WithDrawalReason.REASON_2.getCode(), UploadFileBusinessType.建造翻建大修住房.getCode());
                    this.put(WithDrawalReason.REASON_3.getCode(), UploadFileBusinessType.离休退休.getCode());
                    this.put(WithDrawalReason.REASON_4.getCode(), UploadFileBusinessType.完全丧失劳动能力并与单位终止劳动合同.getCode());
                    this.put(WithDrawalReason.REASON_5.getCode(), UploadFileBusinessType.户口迁出所在市县或出境定居.getCode());
                    this.put(WithDrawalReason.REASON_6.getCode(), UploadFileBusinessType.偿还购房贷款本息.getCode());
                    this.put(WithDrawalReason.REASON_7.getCode(), UploadFileBusinessType.房租超出家庭工资收入的规定比例.getCode());
                    this.put(WithDrawalReason.REASON_8.getCode(), UploadFileBusinessType.死亡.getCode());
                    this.put(WithDrawalReason.REASON_9.getCode(), UploadFileBusinessType.大病医疗.getCode());
                    this.put(WithDrawalReason.REASON_10.getCode(), UploadFileBusinessType.其他.getCode());
                }}.get(info.getWithdrawlsInfo().getTqyy()),
                info.getBlzl())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料不完整");
        }
        //验证开户银行名称
        if (!(WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && !"-1".equals(info.getWithdrawlsInfo().getJqe()))) {
            try {
                iBankInfoService.getBankInfo(StringUtil.isEmpty(info.getWithdrawlReceiBankInfo().getYhmc()) ? info.getWithdrawlsInfo().getGrckzhkhyhmc() : info.getWithdrawlReceiBankInfo().getYhmc()).getChgno();
            } catch (ErrorException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "本次提取收款银行名称有误，请更正");
            }
        }
        ArrayList<Exception> exceptions = new ArrayList<>();

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", info.getYwlsh());
                this.put("cCollectionPersonalBusinessDetailsExtension.czy", tokenContext.getUserInfo().getCZY());
                this.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        if (collectionPersonalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "不能修改该提取业务信息");
        }
        CCollectionWithdrawlVice withdrawlVice = collectionPersonalBusinessDetails.getWithdrawlVice();

        //判断个人账户状态是否能提取
        String grzhzt = collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhzt();
        if (grzhzt.equals(PersonAccountStatus.合并销户.getCode()) || grzhzt.equals(PersonAccountStatus.外部转出销户.getCode()) ||
                grzhzt.equals(PersonAccountStatus.提取销户.getCode())) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前状态不能申请提取");
        }
        if (!Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.审核不通过.getName()).contains(collectionPersonalBusinessDetails.getExtension().getStep())) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "当前业务状态不能修改");
        }
//      添加限制条件 发生额不能大于个人账户余额
        BigDecimal GRZHYE = collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye();
        if (new BigDecimal(info.getWithdrawlsInfo().getFse().toString()).compareTo(GRZHYE) == 1) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "发生额大于个人账户余额");
        }
        if ((CollectionBusinessType.销户提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {//若为销户提取，添加销户原因
            if (!Arrays.asList(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_5.getCode(),
                    WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_10.getCode()).contains(info.getWithdrawlsInfo().getTqyy())) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前提取原因不能销户");
            }

            //region销户提取时汇补缴在途验证
            BusUtils.personDepositDoingCheck(collectionPersonalBusinessDetails.getGrzh(), collectionPersonalBusinessDetails.getPerson().getUnit().getDwzh());
            //endregion
        } else {
            if (GRZHYE.compareTo(new BigDecimal(0.01)) != 1) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前账户余额不足0，只能销户提取");
            }
            if (new BigDecimal(info.getWithdrawlsInfo().getFse()).compareTo(GRZHYE.subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "提取后个人账户余额为0，请修改发生额");
            }
            if (Arrays.asList(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_5.getCode(),
                    WithDrawalReason.REASON_8.getCode()).contains(info.getWithdrawlsInfo().getTqyy())) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前提取原因只能销户提取");
            }
        }
        try {
            if ("01".equals(info.getWithdrawlsInfo().getHkfs())) {
                //region 验证发生额能否偿还贷款
                repaymentService.backRepaymentInfo(new TokenContext(), LoanBusinessType.提前还款.getCode(), info.getWithdrawlsInfo().getDkzjhm(), DateUtil.date2Str(new Date(), format),
                        info.getWithdrawlsInfo().getFse(), "2", collectionPersonalBusinessDetails.getPerson().getZjhm().equals(info.getWithdrawlsInfo().getDkzjhm()) ? "0" : "1").getEarlyRepaymentReducemonth().getBCHKJE();
            }
        } catch (ErrorException e) {
            throw e;
        }
//        if(collectionPersonalBusinessDetails.getExtension().getJqe() != new BigDecimal(-1.00) && !WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy())){
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"当前账号有公积金贷款，提取原因只能为偿还贷款本息");
//        }
        //endregion
        try {
            //region个人业务明细表信息

            collectionPersonalBusinessDetails.setTqyy(info.getWithdrawlsInfo().getTqyy());//提取原因
            collectionPersonalBusinessDetails.setTqfs(info.getWithdrawlsInfo().getTqfs());//提取方式
            collectionPersonalBusinessDetails.setGjhtqywlx(info.getWithdrawlsInfo().getYwmxlx());//业务明细类型  11部分提取  12销户提取

            if ((CollectionBusinessType.销户提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {
//                collectionPersonalBusinessDetails.setFslxe(new BigDecimal(info.getWithdrawlsInfo().getFslxe().toString()));//成功办结后才写
            }
            //endregion


            //region更新副表
            withdrawlVice.setTqyy(info.getWithdrawlsInfo().getTqyy());//提取原因
            withdrawlVice.setTqfs(info.getWithdrawlsInfo().getTqfs());//提取方式
            withdrawlVice.setYwmxlx(info.getWithdrawlsInfo().getYwmxlx());//业务明细类型  11部分提取  12销户提取
//          若为销户提取，添加销户原因
            if ((CollectionBusinessType.销户提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {
                withdrawlVice.setXhyy(info.getWithdrawlsInfo().getTqyy());//销户原因
            }
            withdrawlVice.setBlr(info.getWithdrawlsInfo().getBlr());//办理人
            withdrawlVice.setDlrxm(info.getWithdrawlsInfo().getDlrxm());//代理人姓名
            withdrawlVice.setDlrzjlx(info.getWithdrawlsInfo().getDlrzjlx());//代理人证件类型
            withdrawlVice.setDlrzjhm(info.getWithdrawlsInfo().getDlrzjhm());//代理人证件号码

            withdrawlVice.setBlzl(info.getBlzl());//办理资料
            withdrawlVice.setYhke(new BigDecimal(0.00));//月还款额不能为空
            //偿还购房贷款本息且没有公积金贷款时计算下次提取日期
            withdrawlVice.setJqe(new BigDecimal(info.getWithdrawlsInfo().getJqe()));
            if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && "-1.00".equals(info.getWithdrawlsInfo().getJqe())) {
                try {
                    withdrawlVice.setYhke(new BigDecimal(info.getWithdrawlsInfo().getYhke().toString()));//月还款额
                    withdrawlVice.setXctqrq(DateUtil.str2Date(xctqrqFormat, DateUtil.str2str(info.getWithdrawlsInfo().getXctqrq(), -1)));//下次提取日期
                } catch (Exception e) {
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "‘月还款额’'下次提取日期'");
                }
            }
            //偿还公积金贷款本息，设置还款方式和结清额
            if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && "-1.00".equals(info.getWithdrawlsInfo().getJqe())) {

                withdrawlVice.setHkfs(info.getWithdrawlsInfo().getHkfs());
            }
            //修改时，提取原因不为06，则重置副表的下次提取日期和月还款额
            if (!WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy())) {
                try {
                    withdrawlVice.setXctqrq(null);
                    withdrawlVice.setYhke(new BigDecimal(0.00));
                } catch (Exception r) {
                    r.printStackTrace();
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "重置下次提取日期出错");
                }
            }
            collectionPersonalBusinessDetails.setWithdrawlVice(withdrawlVice);
            //endregion

            //region更新扩展表
            CCollectionPersonalBusinessDetailsExtension collectionExtension = collectionPersonalBusinessDetails.getExtension();
            collectionExtension.setBlr(info.getWithdrawlsInfo().getBlr());//办理人
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
            collectionExtension.setYwwd(network);
            collectionExtension.setCzy(tokenContext.getUserInfo().getCZY());
            collectionExtension.setGrckzhhm(info.getWithdrawlsInfo().getGrckzhhm());//个人存款账户号码
            collectionExtension.setGrckzhkhyhmc(info.getWithdrawlsInfo().getGrckzhkhyhmc());//个人存款账户开户银行名称
            if (("02").equals(info.getWithdrawlsInfo().getBlr())) {
                collectionExtension.setDlrxm(info.getWithdrawlsInfo().getDlrxm());
                collectionExtension.setDlrzjlx(info.getWithdrawlsInfo().getDlrzjlx());
                collectionExtension.setDlrzjhm(info.getWithdrawlsInfo().getDlrzjhm());
            }
            collectionExtension.setHuming(info.getWithdrawlsInfo().getHuMing());//户名
            collectionExtension.setCzmc(info.getWithdrawlsInfo().getYwmxlx());//操作名称 根据业务明细类型获取，11部分提取，12销户提取
            //更改累计提取金额，原累计提取金额 - 原发生额 + 新发生额 ，注意：发生额在此之后修改
            collectionExtension.setLjtqje(collectionExtension.getLjtqje().subtract(withdrawlVice.getFse().abs()).add(new BigDecimal(info.getWithdrawlsInfo().getFse().toString())));
            collectionPersonalBusinessDetails.setFse(new BigDecimal(0.00));//发生额在办结是才录入
            //累计提取金额=扩展表累计提取金额-原发生额+新发生额
            withdrawlVice.setLjtqje(withdrawlVice.getLjtqje().subtract(withdrawlVice.getFse().abs()).add(withdrawlVice.getFse().abs()));
            withdrawlVice.setFse(new BigDecimal(info.getWithdrawlsInfo().getFse().toString()).negate()); // 发生额
            collectionExtension.setYhke(new BigDecimal(0.00));//月还款额
            collectionExtension.setBlzl(info.getBlzl());
            collectionExtension.setXhtqfslxe(StringUtil.notEmpty(info.getWithdrawlsInfo().getFslxe()) ? new BigDecimal(info.getWithdrawlsInfo().getFslxe().toString()) : new BigDecimal(0));//设置销户提取发生利息额，供查询使用

            //偿还公积金贷款本息，设置还款方式和结清额
            collectionExtension.setJqe(new BigDecimal(info.getWithdrawlsInfo().getJqe()));
            if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && !"-1.00".equals(info.getWithdrawlsInfo().getJqe())) {

                collectionExtension.setHkfs(info.getWithdrawlsInfo().getHkfs());
            }
            //商贷偿还购房贷款本息时计算下次提取日期
            if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && "-1.00".equals(info.getWithdrawlsInfo().getJqe())) {
                try {
                    collectionExtension.setYhke(new BigDecimal(info.getWithdrawlsInfo().getYhke().toString()));//月还款额
                    collectionExtension.setXctqrq(DateUtil.str2Date(xctqrqFormat, DateUtil.str2str(info.getWithdrawlsInfo().getXctqrq(), -1)));//下次提取日期
                } catch (Exception e) {
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "‘月还款额’'下次提取日期'");
                }
            }
            //修改时，提取原因不为06，则重置副表的下次提取日期和月还款额
            if (!WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy())) {
                try {
                    collectionExtension.setXctqrq(null);
                    collectionExtension.setYhke(new BigDecimal(0.00));
                } catch (Exception r) {
                    r.printStackTrace();
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "重置下次提取日期出错");
                }
            }
            collectionPersonalBusinessDetails.setExtension(collectionExtension);
            withdrawlVice.setGrywmx(collectionPersonalBusinessDetails);


            //endregion
            //region 提取扩展表
            CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = collectionPersonalBusinessDetails.getWithdrawlBusinessExtension();
            withdrawlBussinessExtension.setSkyhmc(info.getWithdrawlReceiBankInfo().getYhmc());
            withdrawlBussinessExtension.setSkyhhm(info.getWithdrawlReceiBankInfo().getYhhm());
            withdrawlBussinessExtension.setSkyhzhhm(info.getWithdrawlReceiBankInfo().getYhzhhm());
            withdrawlBussinessExtension.setQttqbz(info.getWithdrawlsInfo().getQttqbz());
            withdrawlBussinessExtension.setZongE(new BigDecimal(info.getWithdrawlsInfo().getFse()).add(StringUtil.notEmpty(info.getWithdrawlsInfo().getFslxe()) ? new BigDecimal(info.getWithdrawlsInfo().getFslxe()) : new BigDecimal(0)));
            withdrawlBussinessExtension.setStCollectionPersonalBusinessDetails(collectionPersonalBusinessDetails);

            collectionPersonalBusinessDetails.setWithdrawlBusinessExtension(withdrawlBussinessExtension);

            //endregion
            //region状态机
            StateMachineUtils.updateState(this.iStateMachineService,
                    new HashMap<String, String>() {{
                        this.put("0", Events.保存.getEvent());
                        this.put("1", Events.通过.getEvent());
                    }}.get(op),
                    new TaskEntity() {{
                        this.setStatus(StringUtil.isEmpty(withdrawlVice.getGrywmx().getExtension().getStep()) ? CollectionBusinessStatus.初始状态.getName() : withdrawlVice.getGrywmx().getExtension().getStep());
                        this.setTaskId(collectionPersonalBusinessDetails.getYwlsh());
                        this.setRole(tokenContext.getRoleList().get(0));
                        this.setOperator(tokenContext.getUserInfo().getCZY());
                        this.setNote("修改提取任务");
                        this.setType(BusinessType.WithDrawl);
                        this.setSubtype(BusinessSubType.归集_提取.getSubType());
                        this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                    }}, new StateMachineUtils.StateChangeHandler() {
                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {
                            if (e != null) {
                                throw new ErrorException(e);
                            }
                            if (!succeed || StringUtil.isEmpty(next)) {
                                return;
                            }
                            collectionExtension.setStep(next);
                            if (StringUtil.isIntoReview(next, null)) {
                                collectionExtension.setDdsj(new Date());
                                collectionPersonalBusinessDetails.setExtension(collectionExtension);
//                                collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                            }
                            //提交，跳过审核后的操作
                            if ("1".equals(op)) {
                                if (!StringUtil.isIntoReview(next, null)) {
                                    collectionExtension.setStep(CollectionBusinessStatus.待入账.getName());
                                    withdrawlVice.setGrywmx(collectionPersonalBusinessDetails);
                                    //region偿还公积金贷款，调用贷款操作
                                    if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDetails.getTqyy()) && collectionPersonalBusinessDetails.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                                        String pch = collectionPersonalBusinessDetails.getExtension().getPch();
                                        // 验证同一批次号贷款是否已处理
                                        if (StringUtil.isEmpty(housingBusinessDetailsDAO.getPch(pch))) {
                                            //同一批次号应在同一事务
                                            bulkReviewPassed(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getCzy());
                                        } else {
                                            bulkDoFinal(collectionPersonalBusinessDetails.getYwlsh());
                                        }
                                    }
                                    //endregion
                                    //region 0余额 0利息销户提取，不走结算平台
                                    else if (CollectionBusinessType.销户提取.getCode().equals(collectionPersonalBusinessDetails.getGjhtqywlx()) && collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0)) <= 0
                                            && collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().compareTo(new BigDecimal(0)) <= 0) {
                                        //生成记账凭证
                                        //审核人，该条记录审核通过的操作员
                                        List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                                            {
                                                this.put("ywlsh", collectionPersonalBusinessDetails.getYwlsh());
                                                this.put("shjg", "01");
                                            }
                                        }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                                            @Override
                                            public void error(Exception e) {
//                    exceptions.add(e);
                                            }
                                        });

                                        int djsl = 2;
                                        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                                        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                                        String ZhaiYao = getZhaiYao(collectionPersonalBusinessDetails, "2");
                                        VoucherAmount jfsj = new VoucherAmount();
                                        jfsj.setZhaiYao(ZhaiYao);
                                        jfsj.setJinE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs());
                                        JFSJ.add(jfsj);

                                        VoucherAmount jfsj1 = new VoucherAmount();
                                        jfsj1.setZhaiYao(ZhaiYao);
                                        jfsj1.setJinE(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs());
                                        JFSJ.add(jfsj1);

                                        VoucherAmount dfsj = new VoucherAmount();
                                        dfsj.setJinE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().add(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs()));
                                        dfsj.setZhaiYao(ZhaiYao);
                                        DFSJ.add(dfsj);
                                        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", collectionPersonalBusinessDetails.getExtension().getCzy(), cAuditHistory.size() == 0 ? collectionPersonalBusinessDetails.getExtension().getCzy() : cAuditHistory.get(0).getCzy(),
                                                "", "管理员", VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(), collectionPersonalBusinessDetails.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), (String) null, null);

                                        if (!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep())) {
                                            if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                                                collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                                                collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy()) ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG()
                                                        : DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                                            } else {
                                                //提取办结操作
                                                doFinal(collectionPersonalBusinessDetails.getYwlsh());
                                                collectionPersonalBusinessDetails.getExtension().setJzpzh(voucherRes.getJZPZH());
                                            }
                                            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                                        }
                                    }
                                    //endregion
                                    //region发往结算平台
                                    else {

                                        if (!iBank.checkYWLSH(collectionPersonalBusinessDetails.getYwlsh())) {
                                            throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                                        }
                                        CommonReturn isSuccess = collectionWithdrawlTrader.sendMsg(collectionPersonalBusinessDetails.getYwlsh());
                                        if (!("success").equals(isSuccess.getStatus())) {
//                                        collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
//                                        collectionPersonalBusinessDetails.getExtension().setSbyy(DateUtil.date2Str(new Date(),df) + isSuccess.getStatus() + "请手动验证");
                                        }
                                    }
                                    //endregion
                                }
                            }

                        }
                    });
            //endregion
            withdrawlBusinessExtensionDAO.update(withdrawlBussinessExtension);
            withdrawlViceDAO.update(withdrawlVice);
            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
            if (op.equals("1")) {
                //写入历史信息表
                saveAuditHistory.saveNormalBusiness(collectionPersonalBusinessDetails.getYwlsh(), tokenContext,
                        CollectionBusinessType.销户提取.getCode().equals(info.getWithdrawlsInfo().getYwmxlx()) ? CollectionBusinessType.销户提取.getName() : CollectionBusinessType.部分提取.getName(), "修改");
            }
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
    }

    @Override
    public CommonReturn updateWithdrawl(TokenContext tokenContext, String op, String taskId, ArrayList<ReWithdrawlsInfo> reInfos) {
        for (ReWithdrawlsInfo reInfo : reInfos) {
            update(tokenContext, op, reInfo);
        }
        return new CommonReturn() {{
            this.setStatus("success");
        }};
    }


    public String saveOrSubmit(TokenContext tokenContext, String op, WithdrawlsDetailInfo info, String pch, int size, boolean first) {
        //region验证
        if (StringUtil.isEmpty(op)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "操作类型（保存或提交）不能为空");
        }
        if (!Arrays.asList("0", "1").contains(op)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型（保存或提交）错误");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getTqyy())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "提取原因不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getTqfs())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "提取方式不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getYwmxlx())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务明细类型不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getFse().toString()) || !StringUtil.isMoney(info.getWithdrawlsInfo().getFse())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "发生额不能为空或有误");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getGrckzhhm())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "个人存款账户号码不能为空");
        }
        if (StringUtil.isEmpty(info.getWithdrawlsInfo().getBlr())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "办理人不能为空");
        }
        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.提取.getCode(),
                new HashMap<String, String>() {{
                    this.put(WithDrawalReason.REASON_1.getCode(), UploadFileBusinessType.购买住房.getCode());
                    this.put(WithDrawalReason.REASON_2.getCode(), UploadFileBusinessType.建造翻建大修住房.getCode());
                    this.put(WithDrawalReason.REASON_3.getCode(), UploadFileBusinessType.离休退休.getCode());
                    this.put(WithDrawalReason.REASON_4.getCode(), UploadFileBusinessType.完全丧失劳动能力并与单位终止劳动合同.getCode());
                    this.put(WithDrawalReason.REASON_5.getCode(), UploadFileBusinessType.户口迁出所在市县或出境定居.getCode());
                    this.put(WithDrawalReason.REASON_6.getCode(), UploadFileBusinessType.偿还购房贷款本息.getCode());
                    this.put(WithDrawalReason.REASON_7.getCode(), UploadFileBusinessType.房租超出家庭工资收入的规定比例.getCode());
                    this.put(WithDrawalReason.REASON_8.getCode(), UploadFileBusinessType.死亡.getCode());
                    this.put(WithDrawalReason.REASON_9.getCode(), UploadFileBusinessType.大病医疗.getCode());
                    this.put(WithDrawalReason.REASON_10.getCode(), UploadFileBusinessType.其他.getCode());
                }}.get(info.getWithdrawlsInfo().getTqyy()),
                info.getBlzl())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料不完整");
        }


        // region验证有没有贷款还贷
        String gtjkrzjhm = "";
        if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && !"-1".equals(info.getWithdrawlsInfo().getJqe())) {
            CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
                {

                    this.put("jkrzjhm", info.getWithdrawlsInfo().getDkzjhm());
                    this.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
//                exceptions.add(e);
                }
            });
            if (loanHousingPersonInformationBasic != null) {
                gtjkrzjhm = loanHousingPersonInformationBasic.getCoborrower() == null ? "" : loanHousingPersonInformationBasic.getCoborrower().getGtjkrzjhm();
                List<CLoanHousingBusinessProcess> anHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                    this.put("dkzh", loanHousingPersonInformationBasic.getDkzh());
                    this.put("cznr", Arrays.asList(LoanBusinessType.结清.getCode(), LoanBusinessType.提前还款.getCode()));
                }}, null, null, null, null, null, null);

                for (CLoanHousingBusinessProcess hsingBusinessProcess : anHousingBusinessProcess) {
                    if (!LoanBussinessStatus.已入账.getName().equals(hsingBusinessProcess.getStep()) && !LoanBussinessStatus.已作废.getName().equals(hsingBusinessProcess.getStep())) {
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "存在尚未办理的还款申请业务");
                    }
                }
            }
        } else {
            //验证开户银行名称
            try {
                iBankInfoService.getBankInfo(StringUtil.isEmpty(info.getWithdrawlReceiBankInfo().getYhmc()) ? info.getWithdrawlsInfo().getGrckzhkhyhmc() : info.getWithdrawlReceiBankInfo().getYhmc()).getChgno();
            } catch (ErrorException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "本次提取收款银行名称有误，请更正");
            }
        }
        //endregion


        // region验证有没有提取还贷
        if (WithDrawalReason.REASON_6.getCode().equals(info.getWithdrawlsInfo().getTqyy()) && !"-1".equals(info.getWithdrawlsInfo().getJqe())) {
            if (size == 1) {//只有一个时
                //共同借款人还贷时，验证借款人是否存在还贷申请
                if (!(info.getWithdrawlsInfo().getDkzjhm().equals(info.getIndiAcctInfo().getZjhm()))) {

                    List<StCollectionPersonalBusinessDetails> collectionDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("person.zjhm", info.getWithdrawlsInfo().getDkzjhm());
                            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
                        }
                    }).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
//                exceptions.add(e);
                        }
                    });
                    for (StCollectionPersonalBusinessDetails collectionDetail : collectionDetails) {
                        if (collectionDetail.getPerson().getZjhm().equals(info.getWithdrawlsInfo().getDkzjhm())
                                && !(Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName())).contains(collectionDetail.getExtension().getStep())) {
                            throw new ErrorException(ReturnEnumeration.Business_In_Process, "该贷款账户存在未完成的提取还贷业务");
                        }
                    }
                } else if (StringUtil.notEmpty(gtjkrzjhm)) {
                    //借款人还贷时，验证共同借款人是否存在还贷申请
                    String finalGtjkrzjhm = gtjkrzjhm;
                    List<StCollectionPersonalBusinessDetails> collectionDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("person.zjhm", finalGtjkrzjhm);
                            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.部分提取.getCode());
                        }
                    }).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
//                exceptions.add(e);
                        }
                    });
                    for (StCollectionPersonalBusinessDetails collectionDetail : collectionDetails) {
                        if (collectionDetail.getPerson().getZjhm().equals(finalGtjkrzjhm)
                                && !(Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName())).contains(collectionDetail.getExtension().getStep())) {
                            throw new ErrorException(ReturnEnumeration.Business_In_Process, "该贷款账户存在未完成的提取还贷业务");
                        }
                    }
                }
            }
        }
        //endregion


        //region 验证发生额能否偿还贷款
        try {
            if ("01".equals(info.getWithdrawlsInfo().getHkfs())) {
                //region 验证发生额能否偿还贷款
                List<CLoanHousingPersonInformationBasic> jkrzjhm = loanHousingPersonInformationBasicDAO.list(new HashMap<String, Object>() {{
                    this.put("jkrzjhm", info.getWithdrawlsInfo().getDkzjhm());
                }}, null, null, null, null, null, null);
//                if (jkrzjhm.size() != 0 && "104".equals(jkrzjhm.get(0).getLoanContract().getZhkhyhdm()) && "102".equals(jkrzjhm.get(0).getLoanContract().getZhkhyhdm())) {
//                    throw new ErrorException(ReturnEnumeration.Business_FAILED, "中行、工行停止提取贷款业务，恢复时间：2017-12-20");
//                }

//                if(jkrzjhm.size()==0||jkrzjhm.get(0).getLoanContract().getZhkhyhdm().equals("104")||jkrzjhm.get(0).getLoanContract().getZhkhyhdm().equals("105"))
//                throw new ErrorException(ReturnEnumeration.Business_FAILED,"停止提取贷款业务，恢复时间：2017-12-16");
//                System.out.println("--------------Save ZJHM" + StringUtil.isEmpty( info.getWithdrawlsInfo().getJkrzjhm()) ? info.getIndiAcctInfo().getZjhm() : info.getWithdrawlsInfo().getJkrzjhm() );
                repaymentService.backRepaymentInfo(new TokenContext(), LoanBusinessType.提前还款.getCode(), info.getWithdrawlsInfo().getDkzjhm(), DateUtil.date2Str(new Date(), format),
                        info.getWithdrawlsInfo().getFse(), "2", info.getIndiAcctInfo().getZjhm().equals(info.getWithdrawlsInfo().getDkzjhm()) ? "0" : "1").getEarlyRepaymentReducemonth().getBCHKJE();
            }
        } catch (ErrorException e) {
            throw e;
        }
        //endregion

        ArrayList<Exception> exceptions = new ArrayList<>();
        StCommonPerson commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", info.getIndiAcctInfo().getGrzh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到账户");
        }

        //region销户提取时
        String DWZH = commonPersonDAO.getByGrzh(info.getIndiAcctInfo().getGrzh()).getUnit().getDwzh();
        if (info.getWithdrawlsInfo().getYwmxlx().equals(CollectionBusinessType.销户提取.getCode())) {
            //封存销户提取时验证是否缴至封存年月
            if (PersonAccountStatus.封存.getCode().equals(commonPerson.getCollectionPersonalAccount().getGrzhzt())) {

                ComMessage comMessage = indiAcctSeal.getPersonSealMsgForTQ(info.getIndiAcctInfo().getGrzh());
                if ("01".equals(comMessage.getCode())) {
                    throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, comMessage.getMessage());
                }

            }
            //汇补缴在途验证
            BusUtils.personDepositDoingCheck(info.getIndiAcctInfo().getGrzh(), DWZH);
        }
        //endregion

        //判断个人账户状态是否能提取
        String grzhzt = commonPerson.getCollectionPersonalAccount().getGrzhzt();
        if (grzhzt.equals(PersonAccountStatus.合并销户.getCode()) || grzhzt.equals(PersonAccountStatus.外部转出销户.getCode()) ||
                grzhzt.equals(PersonAccountStatus.提取销户.getCode())) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该账户当前状态不能申请提取");
        }
        //判断是否已存在提取业务，若不存在，则计算累计提取金额
        List<StCollectionPersonalBusinessDetails> collectionDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", info.getIndiAcctInfo().getGrzh());
                this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        BigDecimal LJTQJE = new BigDecimal(0);
        if (collectionDetails.size() > 0) {
            for (StCollectionPersonalBusinessDetails collectionDetail : collectionDetails) {

                if (collectionDetail.getExtension() != null && !(Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName())).contains(collectionDetail.getExtension().getStep())) {
                    throw new ErrorException(ReturnEnumeration.Business_In_Process, "该账户存在未完成的提取业务");
                }
                if (collectionDetail.getExtension() != null && (CollectionBusinessStatus.已入账.getName()).equals(collectionDetail.getExtension().getStep())) {
                    if (collectionDetail.getFse() == null) {
                        collectionDetail.setFse(new BigDecimal(0));
                    }
                    LJTQJE = LJTQJE.add(collectionDetail.getFse().abs());
                }
            }
        }
        //判断是否大于下次提取日期
        if (commonPerson.getExtension().getXctqrq() != null && DateUtil.getLongCompare(DateUtil.date2Str(commonPerson.getExtension().getXctqrq(), format)) < 0) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该账户还未到下次提取日期");
        }
        // 发生额不能大于个人账户余额
        BigDecimal GRZHYE = commonPerson.getCollectionPersonalAccount().getGrzhye();
        if (new BigDecimal(info.getWithdrawlsInfo().getFse()).compareTo(GRZHYE) == 1) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "发生额大于个人账户余额");
        }

        if ((CollectionBusinessType.销户提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {//若为销户提取，添加销户原因
            if (!Arrays.asList(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_5.getCode(),
                    WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_10.getCode()).contains(info.getWithdrawlsInfo().getTqyy())) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前提取原因不能销户");
            }
        }
        if ((CollectionBusinessType.部分提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {
            if (GRZHYE.compareTo(new BigDecimal(0.01)) != 1) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前账户余额不足0，只能销户提取");
            }
            if (new BigDecimal(info.getWithdrawlsInfo().getFse()).compareTo(GRZHYE.subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "提取后个人账户余额为0，请修改发生额");
            }
            if (Arrays.asList(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_5.getCode(),
                    WithDrawalReason.REASON_8.getCode()).contains(info.getWithdrawlsInfo().getTqyy())) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "当前提取原因只能销户提取");
            }
        }
        //endregion

        //附表信息
        CCollectionWithdrawlVice withdrawlVice = new CCollectionWithdrawlVice();
        try {
            //region个人业务明细表信息
            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();
            collectionPersonalBusinessDetails.setGrzh(info.getIndiAcctInfo().getGrzh());//个人账号
            collectionPersonalBusinessDetails.setTqyy(info.getWithdrawlsInfo().getTqyy());//提取原因
            collectionPersonalBusinessDetails.setTqfs(info.getWithdrawlsInfo().getTqfs());//提取方式
            collectionPersonalBusinessDetails.setGjhtqywlx(info.getWithdrawlsInfo().getYwmxlx());//业务明细类型  11部分提取  12销户提取
            collectionPersonalBusinessDetails.setFse(new BigDecimal(0.00));//发生额在办结是才录入
            collectionPersonalBusinessDetails.setCreated_at(new Date());//创建时间
            collectionPersonalBusinessDetails.setPerson(commonPerson);//个人信息
            collectionPersonalBusinessDetails.setUnit(commonPerson.getUnit());//单位信息
            collectionPersonalBusinessDetails.setFslxe(new BigDecimal(0.00));
            if (CollectionBusinessType.销户提取.getCode().equals(info.getWithdrawlsInfo().getYwmxlx())) {
//                collectionPersonalBusinessDetails.setFslxe(new BigDecimal(info.getWithdrawlsInfo().getFslxe().toString()));   //成功办结后才写
            }
            //endregion

            //region写入副表
            withdrawlVice.setGrzh(info.getIndiAcctInfo().getGrzh()); //个人账号
            withdrawlVice.setTqyy(info.getWithdrawlsInfo().getTqyy());//提取原因
            withdrawlVice.setTqfs(info.getWithdrawlsInfo().getTqfs());//提取方式
            withdrawlVice.setYwmxlx(info.getWithdrawlsInfo().getYwmxlx());//业务明细类型  11部分提取  12销户提取
            if ((CollectionBusinessType.销户提取.getCode()).equals(info.getWithdrawlsInfo().getYwmxlx())) {//若为销户提取，添加销户原因
                withdrawlVice.setXhyy(info.getWithdrawlsInfo().getTqyy());//销户原因
            }
            withdrawlVice.setBlr(info.getWithdrawlsInfo().getBlr());//办理人
            withdrawlVice.setDlrxm(info.getWithdrawlsInfo().getDlrxm());//代理人姓名
            withdrawlVice.setDlrzjlx(info.getWithdrawlsInfo().getDlrzjlx());//代理人证件类型
            withdrawlVice.setDlrzjhm(info.getWithdrawlsInfo().getDlrzjhm());//代理人证件号码
            withdrawlVice.setFse(new BigDecimal(info.getWithdrawlsInfo().getFse().toString()).negate());
            withdrawlVice.setCzy(tokenContext.getUserInfo().getCZY());//操作员
            withdrawlVice.setYwwd(tokenContext.getUserInfo().getYWWD());//业务网点
            withdrawlVice.setSlsj(collectionPersonalBusinessDetails.getCreated_at());//受理时间
            withdrawlVice.setBlzl(info.getBlzl());//办理资料
            withdrawlVice.setCreated_at(new Date());
            withdrawlVice.setLjtqje(LJTQJE.add(new BigDecimal(info.getWithdrawlsInfo().getFse().toString())));   //累计提取金额
            withdrawlVice.setJqe(new BigDecimal(info.getWithdrawlsInfo().getJqe()));
            withdrawlVice.setYhke(new BigDecimal(0.00));//月还款额不能为空
            if ((WithDrawalReason.REASON_6.getCode()).equals(info.getWithdrawlsInfo().getTqyy())) {
                //商业贷款偿还购房贷款本息时
                if ("-1".equals(info.getWithdrawlsInfo().getJqe())) {
                    try {
                        withdrawlVice.setYhke(new BigDecimal(info.getWithdrawlsInfo().getYhke().toString()));//月还款额
                        withdrawlVice.setXctqrq(DateUtil.str2Date(xctqrqFormat, DateUtil.str2str(info.getWithdrawlsInfo().getXctqrq(), -1)));//下次提取日期
                    } catch (Exception e) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "‘月还款额’、'下次提取日期'");
                    }
                } else {//偿还公积金贷款
                    withdrawlVice.setHkfs(info.getWithdrawlsInfo().getHkfs());
                }
            }

            //endregion

            //region写入个人扩展表
            CCollectionPersonalBusinessDetailsExtension collectionExtension = new CCollectionPersonalBusinessDetailsExtension();
            collectionExtension.setBlr(info.getWithdrawlsInfo().getBlr());//办理人
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
            //保存借款人证件号码，用于查询贷款记录
            collectionExtension.setJbrzjhm(info.getWithdrawlsInfo().getDkzjhm());
            collectionExtension.setYwwd(network);
            collectionExtension.setCzy(tokenContext.getUserInfo().getCZY());
            collectionExtension.setGrckzhhm(info.getWithdrawlsInfo().getGrckzhhm());//个人存款账户号码
            collectionExtension.setGrckzhkhyhmc(info.getWithdrawlsInfo().getGrckzhkhyhmc());//个人存款账户开户银行名称
            if (("02").equals(info.getWithdrawlsInfo().getBlr())) {
                collectionExtension.setDlrxm(info.getWithdrawlsInfo().getDlrxm());
                collectionExtension.setDlrzjlx(info.getWithdrawlsInfo().getDlrzjlx());
                collectionExtension.setDlrzjhm(info.getWithdrawlsInfo().getDlrzjhm());
            }
            collectionExtension.setHuming(info.getWithdrawlsInfo().getHuMing());//户名
            collectionExtension.setCzmc(info.getWithdrawlsInfo().getYwmxlx());//操作名称 根据业务明细类型获取，11部分提取，12销户提取

            collectionExtension.setLjtqje(LJTQJE.add(new BigDecimal(info.getWithdrawlsInfo().getFse().toString())));   //累计提取金额
            collectionExtension.setBlzl(info.getBlzl());
            collectionExtension.setJqe(new BigDecimal(info.getWithdrawlsInfo().getJqe()));
            collectionExtension.setYhke(new BigDecimal(0.00));//月还款额不能为空
            if (!first) {
                collectionExtension.setPch(pch);
            }

            if ((WithDrawalReason.REASON_6.getCode()).equals(info.getWithdrawlsInfo().getTqyy())) {
                if ("-1".equals(info.getWithdrawlsInfo().getJqe())) {//结清额=-1，没有公积金贷款，是商业贷款偿还购房贷款本息时
                    try {
                        collectionExtension.setYhke(new BigDecimal(info.getWithdrawlsInfo().getYhke()));//月还款额
                        collectionExtension.setXctqrq(DateUtil.str2Date(xctqrqFormat, DateUtil.str2str(info.getWithdrawlsInfo().getXctqrq(), -1)));//下次提取日期
                    } catch (Exception e) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "‘月还款额’、'下次提取日期'");
                    }
                } else {//公积金贷款偿还购房贷款本息时

                    collectionExtension.setHkfs(info.getWithdrawlsInfo().getHkfs());
                }
            }

            collectionExtension.setXhtqfslxe(StringUtil.notEmpty(info.getWithdrawlsInfo().getFslxe()) ? new BigDecimal(info.getWithdrawlsInfo().getFslxe().toString()) : new BigDecimal(0));//设置销户提取发生利息额，供查询使用


            collectionPersonalBusinessDetails.setExtension(collectionExtension);
            //保存后的对象，用于生成业务流水号
            StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    exceptions.add(e);
                }
            });
            collectionExtension.setSlsj(stCollectionPersonalBusinessDetails.getCreated_at());
            stCollectionPersonalBusinessDetails.setWithdrawlVice(withdrawlVice);
            //endregion

            //region状态机
            withdrawlVice.setGrywmx(stCollectionPersonalBusinessDetails);
            withdrawlVice.setYwlsh(stCollectionPersonalBusinessDetails.getYwlsh());// 业务流水号
            stCollectionPersonalBusinessDetails.setExtension(collectionExtension);
            stCollectionPersonalBusinessDetails.setWithdrawlVice(withdrawlVice);

            StateMachineUtils.updateState(this.iStateMachineService,
                    new HashMap<String, String>() {{
                        this.put("0", Events.通过.getEvent());
                        this.put("1", Events.提交.getEvent());
                    }}.get(op),
                    new TaskEntity() {{
                        this.setStatus(StringUtil.isEmpty(stCollectionPersonalBusinessDetails.getExtension().getStep()) ? "初始状态" : stCollectionPersonalBusinessDetails.getExtension().getStep());
                        this.setTaskId(stCollectionPersonalBusinessDetails.getYwlsh());
                        this.setRole(tokenContext.getRoleList().get(0));
                        this.setOperator(tokenContext.getUserInfo().getCZY());
                        this.setNote("保存提取任务");
                        this.setType(BusinessType.WithDrawl);
                        this.setSubtype(BusinessSubType.归集_提取.getSubType());
                        this.setWorkstation(tokenContext.getUserInfo().getYWWD());
//                        this.setWorkstation(stCollectionPersonalBusinessDetails.getUnit().getExtension().getKhwd());//用户所在单位的开户网点
                    }}, new StateMachineUtils.StateChangeHandler() {
                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {
                            if (e != null) {
                                throw new ErrorException(e);
                            }
                            if (!succeed || StringUtil.isEmpty(next)) {
                                return;
                            }
                            collectionExtension.setStep(next);
                            if (StringUtil.isIntoReview(next, null)) {
                                collectionExtension.setDdsj(new Date());
                                withdrawlVice.setGrywmx(stCollectionPersonalBusinessDetails);
                            }
                            //跳过审核后的操作
                            if ("1".equals(op)) {
                                if (!StringUtil.isIntoReview(next, null)) {
                                    collectionExtension.setStep(CollectionBusinessStatus.待入账.getName());
                                    withdrawlVice.setGrywmx(collectionPersonalBusinessDetails);
                                    //region偿还公积金贷款，调用贷款操作
                                    if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDetails.getTqyy()) && collectionPersonalBusinessDetails.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                                        String pch = collectionPersonalBusinessDetails.getExtension().getPch();
                                        // 验证同一批次号贷款是否已处理
                                        if (StringUtil.isEmpty(housingBusinessDetailsDAO.getPch(pch))) {
                                            //同一批次号应在同一事务
                                            bulkReviewPassed(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getCzy());
                                        } else {
                                            bulkDoFinal(collectionPersonalBusinessDetails.getYwlsh());
                                        }
                                    }
                                    //endregion
                                    //region 0余额 0利息销户提取，不走结算平台
                                    else if (CollectionBusinessType.销户提取.getCode().equals(collectionPersonalBusinessDetails.getGjhtqywlx()) && collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0)) <= 0
                                            && collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().compareTo(new BigDecimal(0)) <= 0) {
                                        //生成记账凭证
                                        //审核人，该条记录审核通过的操作员
                                        List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                                            {
                                                this.put("ywlsh", collectionPersonalBusinessDetails.getYwlsh());
                                                this.put("shjg", "01");
                                            }
                                        }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                                            @Override
                                            public void error(Exception e) {
//                    exceptions.add(e);
                                            }
                                        });

                                        int djsl = 2;
                                        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                                        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                                        String ZhaiYao = getZhaiYao(collectionPersonalBusinessDetails, "2");
                                        VoucherAmount jfsj = new VoucherAmount();
                                        jfsj.setZhaiYao(ZhaiYao);
                                        jfsj.setJinE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs());
                                        JFSJ.add(jfsj);

                                        VoucherAmount jfsj1 = new VoucherAmount();
                                        jfsj1.setZhaiYao(ZhaiYao);
                                        jfsj1.setJinE(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs());
                                        JFSJ.add(jfsj1);

                                        VoucherAmount dfsj = new VoucherAmount();
                                        dfsj.setJinE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().add(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs()));
                                        dfsj.setZhaiYao(ZhaiYao);
                                        DFSJ.add(dfsj);
                                        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", collectionPersonalBusinessDetails.getExtension().getCzy(), cAuditHistory.size() == 0 ? collectionPersonalBusinessDetails.getExtension().getCzy() : cAuditHistory.get(0).getCzy(),
                                                "", "管理员", VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(), collectionPersonalBusinessDetails.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), (String) null, null);

                                        if (!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep())) {
                                            if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                                                collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                                                collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy()) ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG()
                                                        : DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                                            } else {
                                                //提取办结操作
                                                doFinal(collectionPersonalBusinessDetails.getYwlsh());
                                                collectionPersonalBusinessDetails.getExtension().setJzpzh(voucherRes.getJZPZH());
                                            }
                                            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                                        }
                                    }
                                    //endregion
                                    //region发往结算平台
                                    else {

                                        if (!iBank.checkYWLSH(collectionPersonalBusinessDetails.getYwlsh())) {
                                            throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                                        }
                                        CommonReturn isSuccess = collectionWithdrawlTrader.sendMsg(collectionPersonalBusinessDetails.getYwlsh());
                                        if (!("success").equals(isSuccess.getStatus())) {
//                                        collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
//                                        collectionPersonalBusinessDetails.getExtension().setSbyy(DateUtil.date2Str(new Date(),df) + isSuccess.getStatus() + "请手动验证");
                                        }
                                    }
                                    //endregion
                                }
                            }
                        }
                    });

            //endregion

            //region 提取扩展表
            CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = new CCollectionWithdrawlBusinessExtension();
            withdrawlBussinessExtension.setQttqbz(info.getWithdrawlsInfo().getQttqbz());//替他提取备注
            withdrawlBussinessExtension.setSkyhzhhm(info.getWithdrawlReceiBankInfo().getYhzhhm());//收款银行账户号码
            withdrawlBussinessExtension.setSkyhhm(info.getWithdrawlReceiBankInfo().getYhhm());//收款银行户名
            withdrawlBussinessExtension.setSkyhmc(info.getWithdrawlReceiBankInfo().getYhmc());//收款银行名称
            withdrawlBussinessExtension.setStCollectionPersonalBusinessDetails(stCollectionPersonalBusinessDetails);
            withdrawlBussinessExtension.setZongE(new BigDecimal(info.getWithdrawlsInfo().getFse()).add(StringUtil.notEmpty(info.getWithdrawlsInfo().getFslxe()) ? new BigDecimal(info.getWithdrawlsInfo().getFslxe()) : new BigDecimal(0)));
            withdrawlBusinessExtensionDAO.save(withdrawlBussinessExtension);
            //endregion


            //写入历史信息表
            saveAuditHistory.saveNormalBusiness(stCollectionPersonalBusinessDetails.getYwlsh(), tokenContext,
                    CollectionBusinessType.销户提取.getCode().equals(info.getWithdrawlsInfo().getYwmxlx()) ? CollectionBusinessType.销户提取.getName() : CollectionBusinessType.部分提取.getName(), "新建");
            DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    exceptions.add(e);
                }
            });

            return stCollectionPersonalBusinessDetails.getExtension().getPch();
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }

    }

    @Override
    public CommonReturn saveOrSubmitWithdrawl(TokenContext tokenContext, String op, ArrayList<WithdrawlsDetailInfo> infos) {
        boolean first = true;
        String pch = "";

        for (WithdrawlsDetailInfo info : infos) {
            if (first) {
                pch = saveOrSubmit(tokenContext, op, info, null, infos.size(), first);
                first = false;
            } else {
                saveOrSubmit(tokenContext, op, info, pch, infos.size(), first);
            }
        }
        return new CommonReturn() {
            {
                this.setStatus("success");
            }
        };
    }

    //打印提取回执(财务模块调用，非前端调用)
    @Override
    public ReceiptReturn printWithdrawlReceipt(String taskId) {
        if (StringUtil.isEmpty(taskId)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号不能为空");
        }
        ArrayList<Exception> exceptions = new ArrayList<>();
        StCollectionPersonalBusinessDetails collectionDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", taskId);
                this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        if (collectionDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该提取业务明细信息不存在");
        }
//        if (!Arrays.asList(CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.待入账.getName(),CollectionBusinessStatus.待某人审核.getName()).contains(collectionDetails.getExtension().getStep())) {
//            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该提取业务状态不能打印提取回执");
//        }


        ReceiptReturn receiptReturn = new ReceiptReturn();
        try {
//                            this.setYZM();// 验证码
            receiptReturn.setTZRQ(DateUtil.date2Str(new Date(), format));//填制日期
            receiptReturn.setYWLSH(taskId);
            receiptReturn.setYWWD(collectionDetails.getExtension().getYwwd().getMingCheng());
            receiptReturn.setCZY(collectionDetails.getExtension().getCzy());
            //如果操作员是系统，则审核人为系统
            if (("系统").equals(collectionDetails.getExtension().getCzy())) {
                receiptReturn.setSHR("系统");
            } else {
                //审核人，该条记录审核通过的操作员
                List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", taskId);
                        this.put("shjg", "01");
                    }
                }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });
                receiptReturn.setSHR(cAuditHistory.size() == 0 ? null : cAuditHistory.get(0).getCzy());
            }
            receiptReturn.setReceiptIndiAcctInfo(new ReceiptIndiAcctInfo() {
                {
                    this.setGrzh(collectionDetails.getGrzh());
                    this.setXingMing(collectionDetails.getPerson().getXingMing());
                    this.setDwmc(collectionDetails.getPerson().getUnit().getDwmc());
                    this.setDwzh(collectionDetails.getPerson().getUnit().getDwzh());
                }
            });
            receiptReturn.setReceiptWithdrawlsInfo(new ReceiptWithdrawlsInfo() {
                {
                    this.setTqyy(collectionDetails.getTqyy());
                    if (CollectionBusinessType.销户提取.getCode().equals(collectionDetails.getGjhtqywlx())) {
                        this.setSfxh("是");
//                        this.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe()).toString()
//                                : collectionDetails.getWithdrawlVice().getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe()).toString());

                    } else {
                        this.setSfxh("否");
//                        this.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().toString()
//                                : collectionDetails.getWithdrawlVice().getFse().abs().toString());
                    }
                    this.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().toString()
                            : collectionDetails.getWithdrawlVice().getFse().abs().toString());

                    if ("01".equals(collectionDetails.getExtension().getBlr())) {
                        this.setBlr(collectionDetails.getPerson().getXingMing());
                    } else {
                        this.setBlr(collectionDetails.getExtension().getDlrxm());
                    }
                    this.setZjhm(collectionDetails.getPerson().getZjhm());
                    this.setDqye(!Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName()).contains(collectionDetails.getExtension().getStep()) ? collectionDetails.getPerson().getCollectionPersonalAccount().getGrzhye().toString()
                            : collectionDetails.getExtension().getDqye().toString());
                    this.setSkyhkh(collectionDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());//收款银行卡号
                    this.setSkyh(collectionDetails.getPerson().getCollectionPersonalAccount().getGrckzhkhyhmc());//收款银行
                    this.setBlsj(DateUtil.date2Str(collectionDetails.getExtension().getSlsj(), format));//办理时间
                    if (collectionDetails.getExtension().getXctqrq() != null) {
                        this.setXctqrq(DateUtil.date2Str(collectionDetails.getExtension().getXctqrq(), format));
                    }
                    this.setZongE(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().add(collectionDetails.getFslxe().abs()).toString()
                            : collectionDetails.getWithdrawlVice().getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe().abs()).toString());
                    this.setFslxe(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFslxe().abs().toString() : collectionDetails.getExtension().getXhtqfslxe().abs().toString());
                }
            });
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        return receiptReturn;
    }

    @Override
    public PageRes<Withdrawl> searchWithdrawl(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt, String ywwd, String yhdm, String begain, String end, String pageNo, String pageSize, String zjhm, String ZongE, String tqyy) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        Date begainDate = null;
        Date endDate = null;
//        if(begain == null ){
//
//        }
//        if(end == null){
//            endDate = new Date();
//        }
        if (!StringUtil.isEmpty(begain)) {
            try {
                begainDate = DateUtil.str2Date(searchFormat, begain);

            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "起始日期参数异常");
            }
        }
        if (!StringUtil.isEmpty(end)) {
            try {
                endDate = DateUtil.str2Date(searchFormat, end);

            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束日期参数异常");
            }
        }
        PageRes pageRes = new PageRes();
        Date finalBegainDate = begainDate;
        Date finalEndDate = endDate;
        List<StCollectionPersonalBusinessDetails> list = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        if (!StringUtil.isEmpty(dwmc)) {
                            this.put("unit.dwmc", dwmc);
                        }
                        if (!StringUtil.isEmpty(xm)) {
                            this.put("person.xingMing", xm);
                        }
                        if (!StringUtil.isEmpty(grzh)) {
                            this.put("grzh", grzh);
                        }
                        if (!StringUtil.isEmpty(zjhm)) {
                            this.put("person.zjhm", zjhm);
                        }
                        this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.销户提取.getCode(), CollectionBusinessType.部分提取.getCode()));
                    }
                }).extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {


                        criteria.add(Restrictions.between(
                                "cCollectionPersonalBusinessDetailsExtension.created_at",
                                finalBegainDate,
                                finalEndDate));
                        if (!StringUtil.isEmpty(ywzt) && !CollectionBusinessStatus.所有.getName().equals(ywzt) && !CollectionBusinessStatus.待审核.getName().equals(ywzt)) {
                            criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ywzt));
                        }
                        if (CollectionBusinessStatus.待审核.getName().equals(ywzt)) {

                            criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                        }

                        //非中心网点才验证
                        if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                            criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");
                            if (StringUtil.isEmpty(dwmc)) {
                                criteria.createAlias("unit", "unit");

                            }
                            criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");
                            //选择全部时，查询用户的网点或者该开户网点的业务
                            if (StringUtil.isEmpty(ywwd)) {
                                criteria.add(Restrictions.or(

                                        Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()),

                                        Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
                                ));
                            } else if (ywwd.equals(tokenContext.getUserInfo().getYWWD())) {
                                //该网点的人选择该网点过滤时，只查出该网点办理的所有业务
                                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", ywwd));
                            } else {
                                //选择网点时，查询该网点且该开户网点的业务
                                criteria.add(Restrictions.and(

                                        Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", ywwd),

                                        Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
                                ));
                            }
                        } else {
                            if (StringUtil.notEmpty(ywwd)) {
                                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", ywwd));
                            }
                        }
                        //此处经办人姓名字段用来存储个人账户银行所对应的的公积金中心银行的代码
                        if (StringUtil.notEmpty(yhdm)) {
                            criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.jbrxm", yhdm));
                        }
                        //todo zonge
                        if (StringUtil.notEmpty(ZongE)) {
                            criteria.createAlias("withdrawlBusinessExtension", "withdrawlBusinessExtension");

                            criteria.add(Restrictions.eq("withdrawlBusinessExtension.zongE", new BigDecimal(ZongE)));
                        }

                        if (StringUtil.notEmpty(tqyy)) {
                            criteria.add(Restrictions.eq("tqyy", tqyy));
                        }
                    }
                }).betweenDate(begainDate, endDate).searchOption(SearchOption.FUZZY).pageOption(pageRes, !StringUtil.isEmpty(pageSize) ? Integer.parseInt(pageSize) : 10, !StringUtil.isEmpty(pageNo) ? Integer.parseInt(pageNo) : 1).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });

        return new PageRes<Withdrawl>() {{
            this.setResults(new ArrayList<Withdrawl>() {
                {
                    for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list) {
                        StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();
                        if (commonPerson == null)
                            continue;
                        Withdrawl withdrawl = new Withdrawl();
                        try {
                            withdrawl.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());//业务流水号
                            withdrawl.setGRZH(collectionPersonalBusinessDetails.getGrzh());//
                            withdrawl.setXingMing(collectionPersonalBusinessDetails.getPerson().getXingMing());//
                            withdrawl.setTQYY(collectionPersonalBusinessDetails.getTqyy());
                            withdrawl.setGRZHYE(!Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName()).contains(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().toString()
                                    : collectionPersonalBusinessDetails.getExtension().getDqye().toString());
                            withdrawl.setZhuangTai(collectionPersonalBusinessDetails.getExtension().getStep());
                            withdrawl.setJZPZH(collectionPersonalBusinessDetails.getExtension().getJzpzh());//记账凭证号
                            withdrawl.setDWMC(collectionPersonalBusinessDetails.getPerson().getUnit().getDwmc());
                            withdrawl.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getSlsj(), df));
//                                    状态为新建或待审核时，从副表获取数据
//                            if (CollectionBusinessStatus.新建.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ||
//                                    StringUtil.isIntoReview(collectionPersonalBusinessDetails.getExtension().getStep(),null) ) {
//                                withdrawl.setFSE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs());
//                                withdrawl.setCZY(collectionPersonalBusinessDetails.getWithdrawlVice().getCzy());
//                                withdrawl.setYWWD(collectionPersonalBusinessDetails.getWithdrawlVice().getYwwd());
//                            } else {
                            withdrawl.setZongE(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().add(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs()).toString()
                                    : collectionPersonalBusinessDetails.getFse().abs().add(collectionPersonalBusinessDetails.getFslxe().abs()).toString());
                            withdrawl.setFSE(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().toString()
                                    : collectionPersonalBusinessDetails.getFse().abs().toString());
                            withdrawl.setFSLXE(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getExtension().getXhtqfslxe().abs().toString()
                                    : collectionPersonalBusinessDetails.getFslxe().abs().toString());
                            withdrawl.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());
                            withdrawl.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());
                            withdrawl.setSBYY(collectionPersonalBusinessDetails.getExtension().getSbyy());
                            withdrawl.setZJHM(collectionPersonalBusinessDetails.getPerson().getZjhm());
                            collectionPersonalBusinessDetails.getPerson().getZjhm();
//                            }
                        } catch (NullPointerException e) {
                            ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Parameter_MISS);
                        }
                        this.add(withdrawl);
                    }
                }
            });
            this.setCurrentPage(pageRes.getCurrentPage());
            this.setNextPageNo(pageRes.getNextPageNo());
            this.setPageSize(pageRes.getPageSize());
            this.setTotalCount(pageRes.getTotalCount());
            this.setPageCount(pageRes.getPageCount());
        }};
    }

    @Override
    public PageResNew<Withdrawl> searchWithdrawlNew(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt, String ywwd, String yhmc, String begain, String end, String action, String marker, String pagesize, String zjhm, String ZongE) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        int pagesize_size = 0;

        try {

            if (pagesize != null) {
                pagesize_size = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        Date begainDate = null;
        Date endDate = null;
//        if(begain == null ){
//
//        }
//        if(end == null){
//            endDate = new Date();
//        }
        if (!StringUtil.isEmpty(begain)) {
            try {
                begainDate = DateUtil.str2Date(searchFormat, begain);

            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "起始日期参数异常");
            }
        }
        if (!StringUtil.isEmpty(end)) {
            try {
                endDate = DateUtil.str2Date(searchFormat, end);

            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束日期参数异常");
            }
        }
        PageRes pageRes = new PageRes();
        Date finalBegainDate = begainDate;
        Date finalEndDate = endDate;
        List<StCollectionPersonalBusinessDetails> list = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        if (!StringUtil.isEmpty(dwmc)) {
                            this.put("unit.dwmc", dwmc);
                        }
                        if (!StringUtil.isEmpty(xm)) {
                            this.put("person.xingMing", xm);
                        }
                        if (!StringUtil.isEmpty(grzh)) {
                            this.put("grzh", grzh);
                        }


                        this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.销户提取.getCode(), CollectionBusinessType.部分提取.getCode()));
                    }
                }).extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {


                        criteria.add(Restrictions.between(
                                "cCollectionPersonalBusinessDetailsExtension.created_at",
                                finalBegainDate,
                                finalEndDate));

                        if (!StringUtil.isEmpty(ywzt) && !CollectionBusinessStatus.所有.getName().equals(ywzt) && !CollectionBusinessStatus.待审核.getName().equals(ywzt)) {
                            criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ywzt));
                        }

                        if (CollectionBusinessStatus.待审核.getName().equals(ywzt)) {

                            criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                        }


                        //非中心网点才验证
                        if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                            criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");


                            if (StringUtil.isEmpty(dwmc)) {
                                criteria.createAlias("unit", "unit");

                            }
                            criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");

                            criteria.add(Restrictions.or(
                                    Restrictions.and(
                                            Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()),
                                            Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czy", tokenContext.getUserInfo().getCZY())),
                                    Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
                            ));
                        }

                        if ("1".equals(tokenContext.getUserInfo().getYWWD())) {
                            if (StringUtil.notEmpty(ywwd)) {
                                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", ywwd));
                            }
                        }
                        //此处经办人姓名字段用来存储个人账户银行所对应的的公积金中心银行的名称
                        if (StringUtil.notEmpty(yhmc)) {
                            criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.jbrxm", yhmc));
                        }


                    }
                }).betweenDate(begainDate, endDate).searchOption(SearchOption.FUZZY).pageOption(marker, action, pagesize_size).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });
        return new PageResNew<Withdrawl>() {{
            this.setResults(action, new ArrayList<Withdrawl>() {
                {
                    for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list) {
                        StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();
                        if (commonPerson == null)
                            continue;
                        Withdrawl withdrawl = new Withdrawl();
                        try {
                            withdrawl.setId(collectionPersonalBusinessDetails.getId());
                            withdrawl.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());//业务流水号
                            withdrawl.setGRZH(collectionPersonalBusinessDetails.getGrzh());//
                            withdrawl.setXingMing(collectionPersonalBusinessDetails.getPerson().getXingMing());//
                            withdrawl.setTQYY(collectionPersonalBusinessDetails.getTqyy());
                            withdrawl.setGRZHYE(!Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName()).contains(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().toString()
                                    : collectionPersonalBusinessDetails.getExtension().getDqye().toString());
                            withdrawl.setZhuangTai(collectionPersonalBusinessDetails.getExtension().getStep());
                            withdrawl.setJZPZH(collectionPersonalBusinessDetails.getExtension().getJzpzh());//记账凭证号
                            withdrawl.setDWMC(collectionPersonalBusinessDetails.getPerson().getUnit().getDwmc());
                            withdrawl.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getSlsj(), df));
//                                    状态为新建或待审核时，从副表获取数据
//                            if (CollectionBusinessStatus.新建.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ||
//                                    StringUtil.isIntoReview(collectionPersonalBusinessDetails.getExtension().getStep(),null) ) {
//                                withdrawl.setFSE(collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs());
//                                withdrawl.setCZY(collectionPersonalBusinessDetails.getWithdrawlVice().getCzy());
//                                withdrawl.setYWWD(collectionPersonalBusinessDetails.getWithdrawlVice().getYwwd());
//                            } else {
                            withdrawl.setFSE(!CollectionBusinessStatus.已入账.getName().equals(collectionPersonalBusinessDetails.getExtension().getStep()) ? collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().toString()
                                    : collectionPersonalBusinessDetails.getFse().abs().toString());
                            withdrawl.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());
                            withdrawl.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());
                            withdrawl.setSBYY(collectionPersonalBusinessDetails.getExtension().getSbyy());
                            withdrawl.setZJHM(collectionPersonalBusinessDetails.getPerson().getZjhm());
                            collectionPersonalBusinessDetails.getPerson().getZjhm();
//                            }
                        } catch (NullPointerException e) {
                            ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Parameter_MISS);
                        }
                        this.add(withdrawl);
                    }
                }
            });
        }};
    }

    @Override
    public ReadOnly getWithdrawlsReadOnly(TokenContext tokenContext, String zjhm, String type) {

        if (StringUtil.isEmpty(zjhm) && StringUtil.isEmpty(type)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "个人账号或类型不能为空或有误");
        }
        ArrayList<Exception> exceptions = new ArrayList<>();
        List<StCommonPerson> commonPersons = DAOBuilder.instance(commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("zjhm", zjhm);
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });
        //region 账户唯一性
        if (commonPersons.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该账户不存在");
        }
        StCommonPerson commonPerson = null;

        if (commonPersons.size() == 1) {
            commonPerson = commonPersons.get(0);
        }

        //当有多条数据是，取正常或封存状态的账户，不可能同时存在正常和封存
        if (commonPersons.size() > 1) {
            for (StCommonPerson person : commonPersons) {
                if (Arrays.asList(PersonAccountStatus.封存.getCode(), PersonAccountStatus.正常.getCode()).contains(person.getCollectionPersonalAccount().getGrzhzt())) {
                    commonPerson = person;
                }
            }
        }
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该账户不存在或处于不可提取状态");
        }
        //endregion
        //region 启封封存的在途验证
        StCommonPerson finalCommonPerson = commonPerson;
        StCollectionPersonalBusinessDetails details = instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("individualAccountActionVice.grzh", finalCommonPerson.getGrzh());
                this.put("individualAccountActionVice.czmc", Arrays.asList(CollectionBusinessType.封存.getCode(), CollectionBusinessType.启封.getCode()));
                this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("individualAccountActionVice.deleted", false);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (details != null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process, "该账户有未完成的启封或封存业务");
        }

        //endregion
        //转移的在途验证
        //借款人
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("jkrzjhm", zjhm);
        filter.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });

        //共同借款人
        HashMap<String, Object> filter2 = new HashMap<>();
        filter2.put("coborrower.gtjkrzjhm", zjhm);
        filter2.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
        filter2.put("hyzk", "20");//婚姻状况
        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic2 = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(filter2).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        //冻结状态下，只能通过委托扣划时办理提取，贷款调用方法里，其他情况不允许
        if ("02".equals(commonPerson.getExtension().getSfdj()) && loanHousingPersonInformationBasic == null && loanHousingPersonInformationBasic2 == null) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该账户处于冻结状态，不能办理提取");
        }

        String JQE = "0";
        String HKFS = "";
        String jkrzjhm = "";
        String jkrXingMing = "";
        String dkzjhm = "";
        BigDecimal FSE = null;
        String JKRORGT = "0";//0 借款人 1 共同借款人
        if (loanHousingPersonInformationBasic != null || loanHousingPersonInformationBasic2 != null) {
            if (loanHousingPersonInformationBasic == null && loanHousingPersonInformationBasic2 != null) {
                JKRORGT = "1";
                //判断借款人状态是否正常
                StCommonPerson person = commonPersonDAO.getByGrzh(loanHousingPersonInformationBasic2.getJkrgjjzh());
                if (Arrays.asList(PersonAccountStatus.封存.getCode(), PersonAccountStatus.正常.getCode()).contains(person.getCollectionPersonalAccount().getGrzhzt())
                        && !"02".equals(person.getExtension().getSfdj())) {
                    jkrzjhm = loanHousingPersonInformationBasic2.getJkrzjhm();
                    jkrXingMing = loanHousingPersonInformationBasic2.getJkrxm();
                }
            } else {
                //判断共同借款人状态是否正常
                if (loanHousingPersonInformationBasic.getCoborrower() != null && StringUtil.notEmpty(loanHousingPersonInformationBasic.getCoborrower().getGtjkrgjjzh())) {
                    StCommonPerson person = commonPersonDAO.getByGrzh(loanHousingPersonInformationBasic.getCoborrower().getGtjkrgjjzh());
                    if (person != null && Arrays.asList(PersonAccountStatus.封存.getCode(), PersonAccountStatus.正常.getCode()).contains(person.getCollectionPersonalAccount().getGrzhzt())
                            && !"02".equals(person.getExtension().getSfdj())) {
                        jkrzjhm = loanHousingPersonInformationBasic.getCoborrower().getGtjkrzjhm();
                        jkrXingMing = loanHousingPersonInformationBasic.getCoborrower().getGtjkrxm();
                    }
                }
            }
            CLoanHousingPersonInformationBasic basic = loanHousingPersonInformationBasic != null ? loanHousingPersonInformationBasic : loanHousingPersonInformationBasic2;
            dkzjhm = basic.getJkrzjhm();
            if (commonPerson.getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0)) != 1) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "该账户有公积金贷款，且余额不足0，不能办理偿还贷款提取");
            }
            try {
                // 结清额 = 贷款本息+ 逾息＋罚息,返回0，
                JQE = repaymentService.backRepaymentInfo(new TokenContext(), LoanBusinessType.结清.getCode(), basic.getJkrzjhm(), DateUtil.date2Str(new Date(), format), "", "2", JKRORGT).getEarlySettlementLoan().getXYHKZE();
            } catch (ErrorException e) {
                if (e.getMsg().contains("账户已结清")) {
                    JQE = "0";
                } else {
                    throw e;
                }
            }
            if (!"0".equals(JQE)) {
                //有委托扣划，只能做偿还购房贷款本息的结清提取，不能做其他提取；
                if (basic.getFunds() != null && basic.getFunds().getWtkhyjce()) {
                    if (commonPerson.getCollectionPersonalAccount().getGrzhye().subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(JQE)) == -1) {
                        throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该账户在本公积金中心有贷款委托扣划业务，且账户余额小于结清额，不能办理结清提取");
                    } else {
                        HKFS = "02";
                        FSE = new BigDecimal(JQE);
                    }
                } else {
                    //没有委托扣划，可办理部分或结清，
                    // ① 结清额>个人账户余额-0.01；发生额=个人账户余额-0.01（保证个人账户余额至少有0.01），还款方式=提前部分；（贷款余额如果大于0.00，账户上的0.01只能销户提取）
                    // ② 结清额<=个人账户余额-0.01：发生额=结清额（保证个人账户余额至少有0.01），还款方式=提前还清；
                    if (new BigDecimal(JQE).compareTo(commonPerson.getCollectionPersonalAccount().getGrzhye().subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
                        HKFS = "01";
                        FSE = commonPerson.getCollectionPersonalAccount().getGrzhye().subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    } else if (new BigDecimal(JQE).compareTo(commonPerson.getCollectionPersonalAccount().getGrzhye().subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP)) != 1) {
                        HKFS = "02";
                        FSE = new BigDecimal(JQE);
                    }
                }
            }
        }

        String finalJQE = JQE;
        String finalHKFS = HKFS;
        BigDecimal finalFSE = FSE;
        ReadOnly readOnly = new ReadOnly();
        readOnly.setXingMing(commonPerson.getXingMing());
        readOnly.setZjlx(commonPerson.getZjlx());
        readOnly.setZjhm(commonPerson.getZjhm());
        readOnly.setDwmc(commonPerson.getUnit().getDwmc());
        readOnly.setJzny(DateUtil.str2str(commonPerson.getExtension().getGrjzny(), 6));
        readOnly.setGrzhye(commonPerson.getCollectionPersonalAccount().getGrzhye().toString());
        readOnly.setGrzhzt(commonPerson.getCollectionPersonalAccount().getGrzhzt());
        readOnly.setGrckzhhm(commonPerson.getCollectionPersonalAccount().getGrckzhhm());
        readOnly.setHuMing(commonPerson.getXingMing());
        readOnly.setGrckzhkhyhmc(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhmc());
        readOnly.setJqe("0".equals(finalJQE) ? "-1" : finalJQE);
        readOnly.setHkfs(finalHKFS);//01部分还款02结清还款
        readOnly.setFse(finalFSE == null ? "" : finalFSE.toString());
        readOnly.setGrzh(commonPerson.getGrzh());
        readOnly.setJkrzjhm("0".equals(type) ? jkrzjhm : "");
        readOnly.setJkrxm("0".equals(type) ? jkrXingMing : "");
        readOnly.setDkzjhm(dkzjhm);
        return readOnly;
    }

    @Override
    public BatchWithdrawlsReturn batchOpWithdrawls(TokenContext tokenContext, BatchWithdrawlsInfo batchWithdrawlsInfo) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        BatchWithdrawlsReturn batchWithdrawlsReturn = new BatchWithdrawlsReturn();
        batchWithdrawlsReturn.setStatus("success");
        ArrayList<ReceiptReturn> receiptReturns = new ArrayList<>();
        if (batchWithdrawlsInfo == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务内容");
        }
        if (!Arrays.asList("0", "1", "2").contains(batchWithdrawlsInfo.getAction())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "请检查action参数");
        }
        //region批量删除
        if ("1".equals(batchWithdrawlsInfo.getAction())) {
            try {
                this.commonOps.deleteOperation(tokenContext, batchWithdrawlsInfo.getIds(), "01");
            } catch (ErrorException e) {
                throw e;
            }
        }
        //endregion
        //region批量提交
        else if (("0").equals(batchWithdrawlsInfo.getAction())) {
            ArrayList<String> pchList = new ArrayList<>();
            for (String taskId : batchWithdrawlsInfo.getIds()) {

                StCollectionPersonalBusinessDetails collectionDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", taskId);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });
                if (collectionDetails == null) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务信息不存在");
                }
                //region提交验证
                //            审核状态(00所有 01新建 02待审核 03待入账 04已入账 05审核不通过)
                if (!collectionDetails.getExtension().getStep().equals(CollectionBusinessStatus.新建.getName()) &&
                        !collectionDetails.getExtension().getStep().equals(CollectionBusinessStatus.审核不通过.getName())) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "当前提取业务状态不能提交");
                }
                // TODO: 2017/9/30 提交时验证
                if (StringUtil.isEmpty(collectionDetails.getGrzh())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务信息的个人账号为空");
                }
                if (StringUtil.isEmpty(collectionDetails.getTqyy())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务信息的提取原因为空");
                }
                if (StringUtil.isEmpty(collectionDetails.getTqfs())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务的提取方式为空");
                }
                if (StringUtil.isEmpty(collectionDetails.getGjhtqywlx())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条业务的业务明细类型为空");
                }
                if (StringUtil.isEmpty(collectionDetails.getFse().toString())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务信息的发生额为空");
                }
                if (StringUtil.isEmpty(collectionDetails.getExtension().getBlr())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该条提取业务信息的办理人为空");
                }
                if (!iUploadImagesService.validateUploadFile(UploadFileModleType.提取.getCode(),
                        new HashMap<String, String>() {{
                            this.put(WithDrawalReason.REASON_1.getCode(), UploadFileBusinessType.购买住房.getCode());
                            this.put(WithDrawalReason.REASON_2.getCode(), UploadFileBusinessType.建造翻建大修住房.getCode());
                            this.put(WithDrawalReason.REASON_3.getCode(), UploadFileBusinessType.离休退休.getCode());
                            this.put(WithDrawalReason.REASON_4.getCode(), UploadFileBusinessType.完全丧失劳动能力并与单位终止劳动合同.getCode());
                            this.put(WithDrawalReason.REASON_5.getCode(), UploadFileBusinessType.户口迁出所在市县或出境定居.getCode());
                            this.put(WithDrawalReason.REASON_6.getCode(), UploadFileBusinessType.偿还购房贷款本息.getCode());
                            this.put(WithDrawalReason.REASON_7.getCode(), UploadFileBusinessType.房租超出家庭工资收入的规定比例.getCode());
                            this.put(WithDrawalReason.REASON_8.getCode(), UploadFileBusinessType.死亡.getCode());
                            this.put(WithDrawalReason.REASON_9.getCode(), UploadFileBusinessType.大病医疗.getCode());
                            this.put(WithDrawalReason.REASON_10.getCode(), UploadFileBusinessType.其他.getCode());
                        }}.get(collectionDetails.getTqyy()),
                        collectionDetails.getExtension().getBlzl())) {
                    throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料不完整");
                }

                if (!(tokenContext.getUserInfo().getCZY().equals(collectionDetails.getExtension().getCzy()) && tokenContext.getUserInfo().getYWWD().equals(collectionDetails.getExtension().getYwwd().getId()))) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + taskId + ")不是由您受理的，不能提交");
                }
                //endregion
                if (!pchList.contains(collectionDetails.getExtension().getPch())) {
                    pchList.add(collectionDetails.getExtension().getPch());
                } else {
                    continue;
                }
                List<String> lshs = collectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(taskId);
                for (String lsh : lshs) {
                    StCollectionPersonalBusinessDetails collectionDetails2 = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", lsh);
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            exceptions.add(e);
                        }
                    });
                    CCollectionWithdrawlVice withdrawlVice = collectionDetails2.getWithdrawlVice();
                    StateMachineUtils.updateState(this.iStateMachineService,
                            Events.通过.getEvent(),
                            new TaskEntity() {{
                                this.setStatus(collectionDetails2.getExtension().getStep());
                                this.setTaskId(collectionDetails2.getYwlsh());
                                this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                                this.setOperator(tokenContext.getUserInfo().getCZY());
                                this.setNote("提交提取任务");
                                this.setType(BusinessType.WithDrawl);
                                this.setSubtype(BusinessSubType.归集_提取.getSubType());
                                this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                            }}, new StateMachineUtils.StateChangeHandler() {
                                @Override
                                public void onStateChange(boolean succeed, String next, Exception e) {
                                    if (e != null) {
                                        throw new ErrorException(e);
                                    }
                                    if (!succeed || StringUtil.isEmpty(next)) {
                                        return;
                                    }
                                    collectionDetails2.getExtension().setStep(next);
                                    if (StringUtil.isIntoReview(next, null)) {
                                        collectionDetails2.getExtension().setDdsj(new Date());
                                        withdrawlVice.setGrywmx(collectionDetails2);
                                        collectionPersonalBusinessDetailsDAO.update(collectionDetails2);
                                        withdrawlViceDAO.update(withdrawlVice);
                                    }
                                    //跳过审核后的操作
                                    else {
                                        collectionDetails2.getExtension().setStep(CollectionBusinessStatus.待入账.getName());
                                        withdrawlVice.setGrywmx(collectionDetails2);
                                        //region偿还公积金贷款，调用贷款操作
                                        if (WithDrawalReason.REASON_6.getCode().equals(collectionDetails2.getTqyy()) && collectionDetails2.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                                            String pch = collectionDetails2.getExtension().getPch();
                                            // 验证同一批次号贷款是否已处理
                                            if (StringUtil.isEmpty(housingBusinessDetailsDAO.getPch(pch))) {
                                                //同一批次号应在同一事务
                                                bulkReviewPassed(collectionDetails2.getYwlsh(), collectionDetails2.getExtension().getCzy());
                                            } else {
                                                bulkDoFinal(collectionDetails2.getYwlsh());
                                            }
                                        }
                                        //endregion
                                        //region 0余额 0利息销户提取，不走结算平台
                                        else if (CollectionBusinessType.销户提取.getCode().equals(collectionDetails2.getGjhtqywlx()) && collectionDetails2.getPerson().getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0)) <= 0
                                                && collectionDetails2.getExtension().getXhtqfslxe().compareTo(new BigDecimal(0)) <= 0) {
                                            //生成记账凭证
                                            //审核人，该条记录审核通过的操作员
                                            List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                                                {
                                                    this.put("ywlsh", collectionDetails2.getYwlsh());
                                                    this.put("shjg", "01");
                                                }
                                            }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                                                @Override
                                                public void error(Exception e) {
//                    exceptions.add(e);
                                                }
                                            });

                                            int djsl = 2;
                                            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                                            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                                            String ZhaiYao = getZhaiYao(collectionDetails2, "2");
                                            VoucherAmount jfsj = new VoucherAmount();
                                            jfsj.setZhaiYao(ZhaiYao);
                                            jfsj.setJinE(collectionDetails2.getWithdrawlVice().getFse().abs());
                                            JFSJ.add(jfsj);

                                            VoucherAmount jfsj1 = new VoucherAmount();
                                            jfsj1.setZhaiYao(ZhaiYao);
                                            jfsj1.setJinE(collectionDetails2.getExtension().getXhtqfslxe().abs());
                                            JFSJ.add(jfsj1);

                                            VoucherAmount dfsj = new VoucherAmount();
                                            dfsj.setJinE(collectionDetails2.getWithdrawlVice().getFse().abs().add(collectionDetails2.getExtension().getXhtqfslxe().abs()));
                                            dfsj.setZhaiYao(ZhaiYao);
                                            DFSJ.add(dfsj);
                                            VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", collectionDetails2.getExtension().getCzy(), cAuditHistory.size() == 0 ? collectionDetails2.getExtension().getCzy() : cAuditHistory.get(0).getCzy(),
                                                    "", "管理员", VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(), collectionDetails2.getYwlsh(), JFSJ, DFSJ, String.valueOf(djsl), (String) null, null);

                                            if (!CollectionBusinessStatus.已入账.getName().equals(collectionDetails2.getExtension().getStep())) {
                                                if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                                                    collectionDetails2.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                                                    collectionDetails2.getExtension().setSbyy(StringUtil.notEmpty(collectionDetails2.getExtension().getSbyy()) ? collectionDetails2.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG()
                                                            : DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                                                } else {
                                                    //提取办结操作
                                                    doFinal(collectionDetails2.getYwlsh());
                                                    collectionDetails2.getExtension().setJzpzh(voucherRes.getJZPZH());
                                                }
                                                collectionPersonalBusinessDetailsDAO.update(collectionDetails2);
                                            }
                                        }
                                        //endregion
                                        //region发往结算平台
                                        else {

                                            if (!iBank.checkYWLSH(collectionDetails2.getYwlsh())) {
                                                throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                                            }
                                            CommonReturn isSuccess = collectionWithdrawlTrader.sendMsg(collectionDetails2.getYwlsh());
                                            if (!("success").equals(isSuccess.getStatus())) {
                                                batchWithdrawlsReturn.setStatus(isSuccess.getStatus());
                                            }
                                        }
                                        //endregion
                                        withdrawlViceDAO.update(withdrawlVice);
                                    }
                                }
                            });
                    //写入历史信息表
                    saveAuditHistory.saveNormalBusiness(collectionDetails2.getYwlsh(), tokenContext,
                            CollectionBusinessType.销户提取.getCode().equals(collectionDetails2.getExtension().getCzmc()) ? CollectionBusinessType.销户提取.getName() : CollectionBusinessType.部分提取.getName(), "修改");
                }
            }
        }
        //endregion
        //region 批量打印回执
        else if (("2").equals(batchWithdrawlsInfo.getAction())) {
            ArrayList<String> idsArray = new ArrayList<>();
            for (String taskId : batchWithdrawlsInfo.getIds()) {
                StCollectionPersonalBusinessDetails collectionDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", taskId);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });
                CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = instance(withdrawlBusinessExtensionDAO).searchFilter(new HashMap<String, Object>() {{
                    this.put("stCollectionPersonalBusinessDetails", collectionDetails);
                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {

                    }
                });

                ReceiptReturn receiptReturn = new ReceiptReturn();
                try {
                    receiptReturn.setTZRQ(DateUtil.date2Str(new Date(), format));//填制日期
                    receiptReturn.setYWLSH(taskId);
                    receiptReturn.setYWWD(collectionDetails.getExtension().getYwwd().getMingCheng());
                    receiptReturn.setCZY(collectionDetails.getExtension().getCzy());
                    //如果操作员是系统，则审核人为系统
                    if ("系统".equals(collectionDetails.getExtension().getCzy())) {
                        receiptReturn.setSHR("系统");
                    } else {
                        //审核人，该条记录审核通过的操作员
                        List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                            {
                                this.put("ywlsh", taskId);
                                this.put("shjg", "01");
                            }
                        }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                exceptions.add(e);
                            }
                        });
                        receiptReturn.setSHR(cAuditHistory.size() == 0 ? null : cAuditHistory.get(0).getCzy());
                    }
                    ReceiptIndiAcctInfo receiptIndiAcctInfo = new ReceiptIndiAcctInfo();
                    receiptIndiAcctInfo.setGrzh(collectionDetails.getGrzh());
                    receiptIndiAcctInfo.setXingMing(collectionDetails.getPerson().getXingMing());
                    receiptIndiAcctInfo.setDwmc(collectionDetails.getPerson().getUnit().getDwmc());
                    receiptIndiAcctInfo.setDwzh(collectionDetails.getPerson().getUnit().getDwzh());
                    receiptReturn.setReceiptIndiAcctInfo(receiptIndiAcctInfo);
                    ReceiptWithdrawlsInfo receiptWithdrawlsInfo = new ReceiptWithdrawlsInfo();
                    if (collectionDetails.getTqyy() != null) {
                        SingleDictionaryDetail TqyyInfo = iDictionaryService.getSingleDetail(collectionDetails.getTqyy(), "ExtractReason");
                        receiptWithdrawlsInfo.setTqyy(TqyyInfo != null ? TqyyInfo.getName() : "");
                    }
                    if (CollectionBusinessType.销户提取.getCode().equals(collectionDetails.getGjhtqywlx())) {
                        receiptWithdrawlsInfo.setSfxh("是");
//                        this.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe()).toString()
//                                : collectionDetails.getWithdrawlVice().getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe()).toString());

                    } else {
                        receiptWithdrawlsInfo.setSfxh("否");
//                        this.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().toString()
//                                : collectionDetails.getWithdrawlVice().getFse().abs().toString());
                    }
                    receiptWithdrawlsInfo.setTqje(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().toString()
                            : collectionDetails.getWithdrawlVice().getFse().abs().toString());
                    if ("01".equals(collectionDetails.getExtension().getBlr())) {
                        receiptWithdrawlsInfo.setBlr(collectionDetails.getPerson().getXingMing());
                    } else {
                        receiptWithdrawlsInfo.setBlr(collectionDetails.getExtension().getDlrxm());
                    }
                    receiptWithdrawlsInfo.setZjhm(collectionDetails.getPerson().getZjhm());

                    receiptWithdrawlsInfo.setDqye(!Arrays.asList(CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已作废.getName()).contains(collectionDetails.getExtension().getStep()) ? collectionDetails.getPerson().getCollectionPersonalAccount().getGrzhye().toString()
                            : collectionDetails.getExtension().getDqye().toString());

                    receiptWithdrawlsInfo.setSkyhkh(withdrawlBussinessExtension != null && StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) && Arrays.asList(WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_10.getCode())
                            .contains(collectionDetails.getTqyy()) ? withdrawlBussinessExtension.getSkyhzhhm() :
                            collectionDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());//收款银行卡号

                    receiptWithdrawlsInfo.setSkyh(withdrawlBussinessExtension != null && StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) && Arrays.asList(WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_10.getCode())
                            .contains(collectionDetails.getTqyy()) ? withdrawlBussinessExtension.getSkyhmc() :
                            collectionDetails.getPerson().getCollectionPersonalAccount().getGrckzhkhyhmc());//收款银行

                    receiptWithdrawlsInfo.setBlsj(DateUtil.date2Str(collectionDetails.getExtension().getSlsj(), format));//办理时间
                    if (collectionDetails.getExtension().getXctqrq() != null) {
                        receiptWithdrawlsInfo.setXctqrq(DateUtil.date2Str(collectionDetails.getExtension().getXctqrq(), format));
                    }
                    receiptWithdrawlsInfo.setZongE(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFse().abs().add(collectionDetails.getFslxe().abs()).toString()
                            : collectionDetails.getWithdrawlVice().getFse().abs().add(collectionDetails.getExtension().getXhtqfslxe().abs()).toString());
                    receiptWithdrawlsInfo.setFslxe(CollectionBusinessStatus.已入账.getName().equals(collectionDetails.getExtension().getStep()) ? collectionDetails.getFslxe().abs().toString() : collectionDetails.getExtension().getXhtqfslxe().abs().toString());
                    receiptReturn.setReceiptWithdrawlsInfo(receiptWithdrawlsInfo);
                    receiptReturns.add(receiptReturn);

                    String id = pdfService.getWithdrawlReceiptPdf(receiptReturn);
                    System.out.println("生成id的值：" + id);
                    idsArray.add(id);
                    batchWithdrawlsReturn.setIdlist(idsArray);
                } catch (NullPointerException e) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS);
                }
            }
        }
        //endregion

        batchWithdrawlsReturn.setIds(batchWithdrawlsInfo.getIds());
        batchWithdrawlsReturn.setRespList(receiptReturns);
        return batchWithdrawlsReturn;
    }

    @Override
    public CommonResponses printWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize, String grzh, String XingMing) {
        if (StringUtil.isEmpty(zjhm) && StringUtil.isEmpty(grzh)) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "查询条件为空或有误");
        }
        Date begainDate = null;
        Date endDate = null;
        if (!StringUtil.isEmpty(begain)) {
            try {
                begainDate = DateUtil.str2Date(searchFormat, begain);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "起始时间异常");
            }
        }
        if (!StringUtil.isEmpty(end)) {
            try {
                endDate = DateUtil.str2Date(searchFormat, end);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "结束时间异常");
            }
        }
        ArrayList<Exception> exceptions = new ArrayList<>();
        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
            if (!StringUtil.isEmpty(zjhm)) this.put("zjhm", zjhm);
            if (!StringUtil.isEmpty(grzh)) this.put("grzh", grzh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "找不到该账户");
        }
//        审核状态(00所有 01新建 02待审核 03待入账 04已入账 05审核不通过)
        PageRes pageRes = new PageRes();
        List<StCollectionPersonalBusinessDetails> list = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        if (!StringUtil.isEmpty(zjhm)) this.put("person.zjhm", zjhm);
                        if (!StringUtil.isEmpty(grzh)) this.put("grzh", grzh);
                        if (!StringUtil.isEmpty(XingMing)) this.put("person.xingMing", XingMing);
                        this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.销户提取.getCode(), CollectionBusinessType.部分提取.getCode()));
                        this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.已入账.getName());
                    }
                }).orderOption("created_at", Order.ASC).betweenDate(begainDate, endDate).searchOption(SearchOption.REFINED).pageOption(pageRes, !StringUtil.isEmpty(pageSize) ? Integer.parseInt(pageSize) : 10, !StringUtil.isEmpty(pageNo) ? Integer.parseInt(pageNo) : 1).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });

        BigDecimal ljtqje = new BigDecimal(0);
        ArrayList<Record> records = new ArrayList<>();
        try {
            for (StCollectionPersonalBusinessDetails details : list) {
                ljtqje = details.getFse().abs().add(ljtqje);
                Record record = new Record();
                record.setTQRQ(DateUtil.date2Str(details.getCreated_at(), format));
                record.setBCTQJE(details.getFse().abs().toString());//本次提取金额
//                record.setLJTQJE(details.getExtension().getLjtqje().toString());//累计提取金额
                record.setLJTQJE(ljtqje.toString());//累计提取金额
                if ("01".equals(details.getExtension().getBlr())) {
                    record.setBLR(details.getPerson().getXingMing());//办理人为本人姓名
                } else {
                    record.setBLR(details.getExtension().getDlrxm());//办理人为代理人姓名
                }
                record.setCZY(details.getExtension().getCzy());//操作员
                record.setYWWD(details.getExtension().getYwwd().getMingCheng());//业务网点
                records.add(record);
            }
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }

        ArrayList<Record> sortedRecords = new ArrayList<>();
        for (int index = records.size() - 1; index >= 0; index--) {
            sortedRecords.add(records.get(index));
        }

        pageRes.setResults(sortedRecords);
        pageRes.setCurrentPage(pageRes.getCurrentPage());
        pageRes.setNextPageNo(pageRes.getNextPageNo());
        pageRes.setTotalCount(pageRes.getTotalCount());
        pageRes.setPageSize(pageRes.getPageSize());
        pageRes.setPageCount(pageRes.getPageCount());
        WithdrawlRecordsReturn withdrawlRecordsReturn = new WithdrawlRecordsReturn();
        IndiAcctInfo indiAcctInfo = new IndiAcctInfo();
        withdrawlRecordsReturn.setTZRQ(DateUtil.date2Str(new Date(), format));//填制日期
        indiAcctInfo.setGrzh(commonPerson.getGrzh());//个人账号
        indiAcctInfo.setXingMing(commonPerson.getXingMing());//姓名
        indiAcctInfo.setDwmc(commonPerson.getUnit().getDwmc());//单位名称
        withdrawlRecordsReturn.setIndiAcctInfo(indiAcctInfo);
        withdrawlRecordsReturn.setRecordsList(pageRes);//提取记录
        withdrawlRecordsReturn.setCZY(tokenContext.getUserInfo().getCZY());
        withdrawlRecordsReturn.setYWWD(tokenContext.getUserInfo().getYWWDMC());
        String id = pdfService.getWithdrawlsRecords(withdrawlRecordsReturn);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public PageRes<Record> searchWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize, String grzh, String XingMing) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        Date begainDate = null;
        Date endDate = null;
// TODO: 2017/8/8 默认起止时间设定
//         if(begain == null ){
//
//        }
//        if(end == null){
//            endDate = new Date();
//        }
        if (StringUtil.isEmpty(zjhm) && StringUtil.isEmpty(grzh)) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "查询条件为空或有误");
        }
        if (!StringUtil.isEmpty(begain)) {
            try {
                begainDate = DateUtil.str2Date(searchFormat, begain);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "起始日期参数异常");
            }
        }
        if (!StringUtil.isEmpty(end)) {
            try {
                endDate = DateUtil.str2Date(searchFormat, end);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束日期参数异常");
            }
        }
        PageRes pageRes = new PageRes();
        List<StCollectionPersonalBusinessDetails> list = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        if (!StringUtil.isEmpty(zjhm)) this.put("person.zjhm", zjhm);
                        if (!StringUtil.isEmpty(grzh)) this.put("grzh", grzh);
                        if (!StringUtil.isEmpty(XingMing)) this.put("person.xingMing", XingMing);
                        this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
                        this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.已入账.getName());
                    }
                }).orderOption("created_at", Order.ASC).betweenDate(begainDate, endDate).searchOption(SearchOption.REFINED).pageOption(pageRes, !StringUtil.isEmpty(pageSize) ? Integer.parseInt(pageSize) : 10, !StringUtil.isEmpty(pageNo) ? Integer.parseInt(pageNo) : 1).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        exceptions.add(e);
                    }
                });

        BigDecimal ljtqje = new BigDecimal(0);

        ArrayList<Record> records = new ArrayList<>();
        try {
            for (StCollectionPersonalBusinessDetails detail : list) {
                ljtqje = detail.getFse().abs().add(ljtqje);
                Record record = new Record();
                record.setTQRQ(DateUtil.date2Str(detail.getJzrq(), searchFormat));
                record.setBCTQJE(detail.getFse().abs().toString());//本次提取金额
//                record.setLJTQJE(detail.getExtension().getLjtqje().toString());//累计提取金额
                record.setLJTQJE(ljtqje.toString());//累计提取金额
                if ("01".equals(detail.getExtension().getBlr())) {
                    record.setBLR(detail.getPerson().getXingMing());//办理人为本人姓名
                } else {
                    record.setBLR(detail.getExtension().getDlrxm());//办理人为代理人姓名
                }
                record.setCZY(detail.getExtension().getCzy());//操作员
                record.setYWWD(detail.getExtension().getYwwd().getMingCheng());//业务网点
                records.add(record);
            }
        } catch (NullPointerException e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        ArrayList<Record> sortedRecords = new ArrayList<>();
        for (int index = records.size() - 1; index >= 0; index--) {
            sortedRecords.add(records.get(index));
        }
        return new PageRes<Record>() {{
            this.setResults(sortedRecords);
            this.setCurrentPage(pageRes.getCurrentPage());
            this.setNextPageNo(pageRes.getNextPageNo());
            this.setPageSize(pageRes.getPageSize());
            this.setTotalCount(pageRes.getTotalCount());
            this.setPageCount(pageRes.getPageCount());
        }};
    }

    @Override
    public NextDate getNextDate(TokenContext tokenContext, String fse, String yhke) {
        if (StringUtil.isEmpty(yhke) || StringUtil.isEmpty(fse) || !StringUtil.isMoney(fse) || !StringUtil.isMoney(yhke) || ("0").equals(yhke)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月还款额/发生额有误");
        }

        //计算月份
        int month = (int) new BigDecimal(fse).divideToIntegralValue(new BigDecimal(yhke)).doubleValue();
        return new NextDate() {{
            this.setNextDate(DateUtil.getNextDate(new Date(), format, month));
        }};
    }

    @Override
    public FSLXE getFslxe(TokenContext tokenContext, String grzh) {
        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在该账户");
        }
        Date qsDate = null;
        String KHRQ = DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), df);//开户日期

        String currentDate = DateUtil.date2Str(new Date(), df);//当前时间
        String date1 = DateUtil.getYear(DateUtil.date2Str(new Date(), df)) + "-06-30 00:00:00";//当年结息日期
        String date2 = DateUtil.getYear(DateUtil.date2Str(new Date(), df)) - 1 + "-06-30 00:00:00";//前一年结息日期

        Date currentJxDate = null;
        Date beforeJxDate = null;
        try {
            currentJxDate = DateUtil.str2Date(df, date1);
            beforeJxDate = DateUtil.str2Date(df, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //若开户日期大于本年-06-30，则传开户日期
        if (DateUtil.compare_date(KHRQ, date1)) {
            try {
                qsDate = DateUtil.str2Date(df, KHRQ);
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
        return new FSLXE() {
            {
                this.setFSLXE(calculateInterest.calculateInterestByGrzh(grzh, finalQsDate, new Date()).getId());
            }
        };
    }

    /**
     * 审核通过后的操作
     *
     * @param YWLSH
     * @return
     */
    @Override
    @Deprecated
    public void doWithdrawl(String YWLSH) {
        //      将附表数据移到主表
        HashMap<String, Object> filter = new HashMap();
        filter.put("ywlsh", YWLSH);
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO)
                .searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
//                exceptions.add(e);
                    }
                });
        try {
            collectionPersonalBusinessDetails.setFse(collectionPersonalBusinessDetails.getWithdrawlVice().getFse());
            collectionPersonalBusinessDetails.setGjhtqywlx(collectionPersonalBusinessDetails.getWithdrawlVice().getYwmxlx());//归集和提取业务类型
            //region调用计算利息
            if (CollectionBusinessType.销户提取.getCode().equals(collectionPersonalBusinessDetails.getWithdrawlVice().getYwmxlx())) {//若为销户提取
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
                collectionPersonalBusinessDetails.getExtension().setXhtqfslxe(new BigDecimal(calculateInterest.calculateInterestByGrzh(collectionPersonalBusinessDetails.getPerson().getGrzh(), finalQsDate, new Date()).getId()));//销户提取发生利息额
                collectionPersonalBusinessDetails.setFslxe(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe());
            }
            //endregion
            collectionPersonalBusinessDetails.setTqyy(collectionPersonalBusinessDetails.getWithdrawlVice().getTqyy());
            collectionPersonalBusinessDetails.getExtension().setYhke(new BigDecimal(0.00));//月还款额
            if (WithDrawalReason.REASON_6.getCode().equals(collectionPersonalBusinessDetails.getWithdrawlVice().getTqyy())) {
                collectionPersonalBusinessDetails.getExtension().setYhke(collectionPersonalBusinessDetails.getWithdrawlVice().getYhke());//月还款额
                collectionPersonalBusinessDetails.getExtension().setXctqrq(collectionPersonalBusinessDetails.getWithdrawlVice().getXctqrq());//个人业务明细表-下次提取日期
                collectionPersonalBusinessDetails.getPerson().getExtension().setXctqrq(collectionPersonalBusinessDetails.getWithdrawlVice().getXctqrq());//个人基础信息表-下次提取日期
            }
            collectionPersonalBusinessDetails.setTqfs(collectionPersonalBusinessDetails.getWithdrawlVice().getTqfs());//提取方式
            collectionPersonalBusinessDetails.getExtension().setBlr(collectionPersonalBusinessDetails.getWithdrawlVice().getBlr());
            if ("02".equals(collectionPersonalBusinessDetails.getWithdrawlVice().getBlr())) {//办理人 ：01本人 02代理人
                collectionPersonalBusinessDetails.getExtension().setDlrxm(collectionPersonalBusinessDetails.getWithdrawlVice().getDlrxm());
                collectionPersonalBusinessDetails.getExtension().setDlrzjlx(collectionPersonalBusinessDetails.getWithdrawlVice().getDlrzjlx());
                collectionPersonalBusinessDetails.getExtension().setDlrzjhm(collectionPersonalBusinessDetails.getWithdrawlVice().getDlrzjhm());
            }
            collectionPersonalBusinessDetails.getExtension().setLjtqje(collectionPersonalBusinessDetails.getWithdrawlVice().getLjtqje());
            collectionPersonalBusinessDetails.getExtension().setBlzl(collectionPersonalBusinessDetails.getWithdrawlVice().getBlzl());
            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "数据移动失败");
        }
    }

    /**
     * 提取办结后执行的操作
     *
     * @param YWLSH
     */
    @Override
    public void doFinal(String YWLSH) {
        StCollectionPersonalBusinessDetails details = collectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH);

        try {
            StCommonPerson person = commonPersonDAO.getByGrzh(details.getGrzh());
            BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();
            if (grzhye.compareTo(details.getWithdrawlVice().getFse().abs()) < 0) {
                details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                details.getExtension().setSbyy(StringUtil.notEmpty(details.getExtension().getSbyy()) ?
                        details.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 提取办结操作失败，发生额大于个人账户余额，请作废并重新申请" :
                        DateUtil.date2Str(new Date(), df) + " 提取办结操作失败，发生额大于个人账户余额，请作废并重新申请");
            } else {
                //更新业务状态
                details.getExtension().setStep(CollectionBusinessStatus.已入账.getName());
                details.setFse(details.getWithdrawlVice().getFse());
                details.setFslxe(details.getExtension().getXhtqfslxe());    //新增 by ljh 12-02
                //更新个人账户余额
                BigDecimal fse = details.getFse().abs();
                BigDecimal fslxe = details.getFslxe();

                if (fse == null) {
                    fse = new BigDecimal(0);
                }
                if (fslxe == null) {
                    fslxe = new BigDecimal(0);
                }
                //将单位明细流水号和个人账户状态存入个人表，银行处理失败时，可恢复到当前数据
                details.getExtension().setBeizhu(person.getCollectionPersonalAccount().getGrzhzt());
                //若为销户提取，更新个人账户
                if (CollectionBusinessType.销户提取.getCode().equals(details.getGjhtqywlx())) {
//                //销户提取时结一次利息
//                calculateInterest.balanceInterest(person.getGrzh(), fslxe);
                    person.getCollectionPersonalAccount().setXhyy(details.getTqyy());//销户原因
                    person.getCollectionPersonalAccount().setXhrq(details.getCreated_at());//销户日期
                    person.getCollectionPersonalAccount().setGrzhzt(PersonAccountStatus.提取销户.getCode());
                }

                person.getCollectionPersonalAccount().setGrzhye(person.getCollectionPersonalAccount().getGrzhye().subtract(fse));
                person.getExtension().setXctqrq(details.getExtension().getXctqrq());
                details.getExtension().setBjsj(new Date());//设置办结时间
                details.getExtension().setDqye(person.getCollectionPersonalAccount().getGrzhye());
                details.setJzrq(new Date());//记账日期
                details.setCzbz(CommonFieldType.非冲账.getCode());//非冲账
                //没有单位明细才生成，避免重复生成
                if (details.getExtension().getZcdw() == null) {

                    //region 单位业务明细表
                    StCollectionUnitBusinessDetails collectionUnitDetails = new StCollectionUnitBusinessDetails();

                    collectionUnitDetails.setDwzh(details.getUnit().getDwzh());//单位账号
                    collectionUnitDetails.setFse(details.getFse());
                    collectionUnitDetails.setCzbz(details.getCzbz());
                    collectionUnitDetails.setFsrs(new BigDecimal(1));
                    collectionUnitDetails.setUnit(details.getUnit());
                    collectionUnitDetails.setYwmxlx(details.getGjhtqywlx());
                    collectionUnitDetails.setFslxe(details.getFslxe());
                    collectionUnitDetails.setJzrq(details.getJzrq());
                    //endregion
                    //region 单位扩展表
                    CCollectionUnitBusinessDetailsExtension unitExtension = new CCollectionUnitBusinessDetailsExtension();
                    unitExtension.setStep(details.getExtension().getStep());
                    unitExtension.setCzmc(details.getExtension().getCzmc());
                    unitExtension.setBjsj(details.getExtension().getBjsj());
//            unitExtension.setJzpzh(details.getExtension().getJzpzh());
                    unitExtension.setYwwd(details.getExtension().getYwwd());
                    collectionUnitDetails.setExtension(unitExtension);
                    StCollectionUnitBusinessDetails savedUnitDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {

                        }
                    });
                    savedUnitDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionUnitDetails.getUnit().getCollectionUnitAccount().getDwzhye().subtract(details.getFse().abs()));

                    details.getExtension().setZcdw(savedUnitDetails.getYwlsh());
                    collectionUnitBusinessDetailsDAO.update(savedUnitDetails);
                    //endregion
                }
            }

            //region发送短信
            try {
                if (StringUtil.notEmpty(details.getPerson().getSjhm())) {
                    Calendar c = Calendar.getInstance();
                    ismsCommon.sendSingleSMSWithTemp(details.getPerson().getSjhm(), SMSTemp.提取.getCode(),
                            new ArrayList<String>() {{
                                this.add(details.getPerson().getXingMing());
                                this.add(String.valueOf(c.get(Calendar.MONTH) + 1));
                                this.add(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
                                this.add(getZhaiYao(details, "2"));
                                this.add(details.getFse().abs().toString());
                                this.add(details.getFslxe().abs().toString());
                                this.add(details.getExtension().getDqye().toString());
                            }}
                    );
                }
            }catch (Exception e){
                logger.info("提取短信发送失败");
            }
            //endregion
            commonPersonDAO.update(person);
            collectionPersonalBusinessDetailsDAO.update(details);
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
            details.getExtension().setSbyy(StringUtil.notEmpty(details.getExtension().getSbyy()) ?
                    details.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 提取办结操作失败，请手动验证" :
                    DateUtil.date2Str(new Date(), df) + " 提取办结操作失败，请手动验证");
            collectionPersonalBusinessDetailsDAO.update(details);
//            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "提取办结操作失败");
        }
    }


    /**
     * 贷款调用
     *
     * @param loanWithdrawl
     * @return
     */
    @Override
    public CommonReturn addWithdrawl(LoanWithdrawl loanWithdrawl) {
        CommonReturn commonReturn = new CommonReturn();
        List<WithdrawlLoan> withdrawlLoanList = new ArrayList<>();

        if (!StringUtil.isEmpty(loanWithdrawl.getGtjkrgrzh())) {
            JKRAndGTJKR results = compute(loanWithdrawl.getJkrfse(), loanWithdrawl.getGtjkrfse(), loanWithdrawl.getBenjin(), loanWithdrawl.getLixi());
            WithdrawlLoan withdrawlLoan1 = new WithdrawlLoan();
            withdrawlLoan1.setGrzh(loanWithdrawl.getJkrgrzh());
            withdrawlLoan1.setFse(results.getJkrfse());
            withdrawlLoan1.setFslxe(results.getJkrfslxe());
            WithdrawlLoan withdrawlLoan2 = new WithdrawlLoan();
            withdrawlLoan2.setGrzh(loanWithdrawl.getGtjkrgrzh());
            withdrawlLoan2.setFse(results.getGtjkrfse());
            withdrawlLoan2.setFslxe(results.getGtjkrfslxe());
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
            //region 计算累计提取金额
            List<StCollectionPersonalBusinessDetails> collectionDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("grzh", withdrawlLoan.getGrzh());
                    this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
//                exceptions.add(e);
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
//                exceptions.add(e);
                }
            });

            try {
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
                if (person.getCollectionPersonalAccount().getGrzhye().subtract(withdrawlLoan.getFse()).compareTo(new BigDecimal(0.01)) == -1) {
                    throw new ErrorException("入账失败--:" + "提取后个人账户余额不足0");
                }
                //endregion
                savedDetails.setExtension(detailExtension);
                savedDetails.setPerson(person);
                savedDetails.setUnit(person.getUnit());
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
                    detailExtension.setSbyy(StringUtil.notEmpty(detailExtension.getSbyy()) ?
                            detailExtension.getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG()
                            : DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                } else {

                    savedDetails.setFse(withdrawlLoan.getFse().add(withdrawlLoan.getFslxe()).negate());//发生额
                    detailExtension.setJzpzh(voucherRes.getJZPZH());
                }

                person.getCollectionPersonalAccount().setGrzhye(person.getCollectionPersonalAccount().getGrzhye().subtract(savedDetails.getFse().abs()));
                detailExtension.setDqye(person.getCollectionPersonalAccount().getGrzhye());

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

                    }
                });
                savedUnitDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionUnitDetails.getUnit().getCollectionUnitAccount().getDwzhye().subtract(savedDetails.getFse().abs()));
                //endregion
                savedDetails.getExtension().setZcdw(savedUnitDetails.getYwlsh());
                collectionPersonalBusinessDetailsDAO.update(savedDetails);
                collectionUnitBusinessDetailsDAO.update(savedUnitDetails);
                commonPersonDAO.update(person);
                commonReturn.setStatus("success");
            } catch (Exception e) {
                throw new ErrorException("入账失败--:" + e.getMessage());
            }

        }
        return commonReturn;
    }

    @Override
    public void doAfterPassed(String YWLSH, String shy) {
        try {
            StCollectionPersonalBusinessDetails obj = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("ywlsh", YWLSH);
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });


            ArrayList<String> SBYWLSHs = new ArrayList<>();
            //偿还公积金贷款，调用贷款操作
            if (WithDrawalReason.REASON_6.getCode().equals(obj.getTqyy()) && obj.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                if (!DateUtil.isSameDate(obj.getExtension().getCreated_at(), new Date())) {
                    obj.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                    obj.getExtension().setSbyy(StringUtil.notEmpty(obj.getExtension().getSbyy()) ? obj.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 申请时间与当前时间不是同一天，请作废重新办理"
                            : DateUtil.date2Str(new Date(), df) + " 申请时间与当前时间不是同一天，请作废重新办理");
                    collectionPersonalBusinessDetailsDAO.update(obj);
                } else {
                    CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("jkrzjhm", obj.getPerson().getZjhm());
                            this.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
//                exceptions.add(e);
                        }
                    });
                    //共同借款人
                    HashMap<String, Object> filter2 = new HashMap<>();
                    filter2.put("coborrower.gtjkrzjhm", obj.getPerson().getZjhm());
                    filter2.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
                    CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic2 = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(filter2).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
//                        exceptions.add(e);
                        }
                    });


                    if (loanHousingPersonInformationBasic == null && loanHousingPersonInformationBasic2 == null)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "该公积金账号未查询到贷款记录");

                    doFinal(YWLSH);
                    StCollectionPersonalBusinessDetails details = collectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH);

//                调用贷款模块
//                String dkzh = loanHousingPersonInformationBasic.getDkzh();
//                BigDecimal fse = details.getFse();
//                fse.abs();
//                CCollectionPersonalBusinessDetailsExtension extension = details.getExtension();
//                extension.getHkfs();
                    CLoanHousingPersonInformationBasic basic = loanHousingPersonInformationBasic != null ? loanHousingPersonInformationBasic : loanHousingPersonInformationBasic2;
//                    String jzpzh = loanTaskService.providentFundWithdrawal(basic.getDkzh(), details.getWithdrawlVice().getFse().abs(), "01".equals(details.getExtension().getHkfs()) ? LoanBusinessType.提前还款.getCode() : LoanBusinessType.结清.getCode(), details.getExtension().getCzy(), shy, YWLSH);
//                    details.getExtension().setJzpzh(jzpzh);
                    collectionPersonalBusinessDetailsDAO.update(details);
                }

            } else {
                if (!iBank.checkYWLSH(obj.getYwlsh())) {
                    throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                }
                CommonReturn isSuccess = collectionWithdrawlTrader.sendMsg(obj.getYwlsh());
                if (!("success").equals(isSuccess.getStatus())) {
                    SBYWLSHs.add(obj.getYwlsh() + "," + isSuccess.getStatus());
                }
            }
        } catch (ErrorException e) {
            throw e;
        }
    }

    //新审核通过时调用接口
    @Override
    public String doAfterPassed(ArrayList<String> YWLSHs, String shy) {
        if (YWLSHs.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号为空");
        }
        try {
            List<String> pchs = new ArrayList<>();
            for (String ywlsh : YWLSHs) {
                StCollectionPersonalBusinessDetails obj = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", ywlsh);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                //region偿还公积金贷款，调用贷款操作
                if (WithDrawalReason.REASON_6.getCode().equals(obj.getTqyy()) && obj.getExtension().getJqe().compareTo(new BigDecimal(-1.00)) != 0) {
                    String pch = collectionPersonalBusinessDetailsDAO.getByYwlsh(ywlsh).getExtension().getPch();

                    //同一批次号只处理一次
                    if (!pchs.contains(pch)) {
                        pchs.add(pch);
                    } else {
                        continue;
                    }
                    // 验证同一批次号贷款是否已处理
                    if (StringUtil.isEmpty(housingBusinessDetailsDAO.getPch(pch))) {
                        //同一批次号应在同一事务
                        bulkReviewPassed(ywlsh, shy);
                    } else {
                        bulkDoFinal(ywlsh);
                    }
                }
                //endregion
                //region 0余额 0利息销户提取，不走结算平台
                else if (CollectionBusinessType.销户提取.getCode().equals(obj.getGjhtqywlx()) && obj.getPerson().getCollectionPersonalAccount().getGrzhye().compareTo(new BigDecimal(0)) <= 0
                        && obj.getExtension().getXhtqfslxe().compareTo(new BigDecimal(0)) <= 0) {
                    //生成记账凭证
                    //审核人，该条记录审核通过的操作员
                    List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", ywlsh);
                            this.put("shjg", "01");
                        }
                    }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
//                    exceptions.add(e);
                        }
                    });

                    int djsl = 2;
                    List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                    List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                    String ZhaiYao = getZhaiYao(obj,"1");
                    VoucherAmount jfsj = new VoucherAmount();
                    jfsj.setZhaiYao(ZhaiYao);
                    jfsj.setJinE(obj.getWithdrawlVice().getFse().abs());
                    JFSJ.add(jfsj);

                    VoucherAmount jfsj1 = new VoucherAmount();
                    jfsj1.setZhaiYao(ZhaiYao);
                    jfsj1.setJinE(obj.getExtension().getXhtqfslxe().abs());
                    JFSJ.add(jfsj1);

                    VoucherAmount dfsj = new VoucherAmount();
                    dfsj.setJinE(obj.getWithdrawlVice().getFse().abs().add(obj.getExtension().getXhtqfslxe().abs()));
                    dfsj.setZhaiYao(ZhaiYao);
                    DFSJ.add(dfsj);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", obj.getExtension().getCzy(), cAuditHistory.size() == 0 ? obj.getExtension().getCzy() : cAuditHistory.get(0).getCzy(),
                            "", "管理员", VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(), ywlsh, JFSJ, DFSJ, String.valueOf(djsl), (String) null, null);

                    if (!CollectionBusinessStatus.已入账.getName().equals(obj.getExtension().getStep())) {
                        if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                            obj.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                            obj.getExtension().setSbyy(StringUtil.notEmpty(obj.getExtension().getSbyy()) ? obj.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG()
                                    : DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                        } else {
                            //提取办结操作
                            doFinal(ywlsh);
                            obj.getExtension().setJzpzh(voucherRes.getJZPZH());
                        }
                        collectionPersonalBusinessDetailsDAO.update(obj);
                    }
                }
                //endregion
                //region 调用结算平台
                else {
                    if (!iBank.checkYWLSH(obj.getYwlsh())) {
                        throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                    }
                    ArrayList<String> SBYWLSHs = new ArrayList<>();
                    CommonReturn isSuccess = collectionWithdrawlTrader.sendMsg(obj.getYwlsh());
                    if (!("success").equals(isSuccess.getStatus())) {
                        SBYWLSHs.add(obj.getYwlsh() + "," + isSuccess.getStatus());
                    }
                }
                //endregion
            }
        } catch (ErrorException e) {
            throw e;
        }
        return "aaa";
    }

    //处理同一批次号的审核通过
    public void bulkReviewPassed(String ywlsh, String shy) {
        List<String> lshs = collectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(ywlsh);
        String pch = collectionPersonalBusinessDetailsDAO.getByYwlsh(ywlsh).getExtension().getPch();
        BigDecimal HKZE = new BigDecimal(0);
        String DKZH = null;
        BigDecimal JQE = null;
        String czy = null;

        StCollectionPersonalBusinessDetails obj = collectionPersonalBusinessDetailsDAO.getByYwlsh(ywlsh);
        if (!DateUtil.isSameDate(obj.getExtension().getCreated_at(), new Date())) {
            for (String lsh : lshs) {
                StCollectionPersonalBusinessDetails obj2 = collectionPersonalBusinessDetailsDAO.getByYwlsh(lsh);
                obj2.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                obj2.getExtension().setSbyy(StringUtil.notEmpty(obj2.getExtension().getSbyy()) ? obj2.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 申请时间与当前时间不是同一天，请作废重新办理"
                        : DateUtil.date2Str(new Date(), df) + " 申请时间与当前时间不是同一天，请作废重新办理");
                collectionPersonalBusinessDetailsDAO.update(obj2);
            }
        } else {
            for (String lsh : lshs) {
                StCollectionPersonalBusinessDetails obj3 = collectionPersonalBusinessDetailsDAO.getByYwlsh(lsh);
                CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("jkrzjhm", obj3.getPerson().getZjhm());
                        this.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
//                exceptions.add(e);
                    }
                });
                //共同借款人
                HashMap<String, Object> filter2 = new HashMap<>();
                filter2.put("coborrower.gtjkrzjhm", obj3.getPerson().getZjhm());
                filter2.put("dkzhzt", Arrays.asList(LoanAccountType.正常.getCode(), LoanAccountType.逾期.getCode()));
                CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic2 = DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(filter2).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
//                        exceptions.add(e);
                    }
                });
                if (loanHousingPersonInformationBasic == null && loanHousingPersonInformationBasic2 == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该公积金账号未查询到贷款记录");
                CLoanHousingPersonInformationBasic basic = loanHousingPersonInformationBasic != null ? loanHousingPersonInformationBasic : loanHousingPersonInformationBasic2;
                DKZH = basic.getDkzh();
                HKZE = HKZE.add(obj3.getWithdrawlVice().getFse().abs());
                JQE = obj3.getExtension().getJqe();
                czy = obj3.getExtension().getCzy();
                doFinal(lsh);
            }
            String hkfs = null;
            if (HKZE.compareTo(JQE) == 0) {
                hkfs = LoanBusinessType.结清.getCode();
            } else if (HKZE.compareTo(JQE) < 0) {
                hkfs = LoanBusinessType.提前还款.getCode();
            }
            String jzpzh = loanTaskService.providentFundWithdrawal(DKZH, HKZE, hkfs, czy, shy, ywlsh, pch);

            for (String lsh : lshs) {
                StCollectionPersonalBusinessDetails details = collectionPersonalBusinessDetailsDAO.getByYwlsh(lsh);
                details.getExtension().setJzpzh(jzpzh);
                collectionPersonalBusinessDetailsDAO.update(details);
            }
        }
    }

    //贷款已做记录，提取记录直接办结
    public void bulkDoFinal(String ywlsh) {
        List<String> lshs = collectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(ywlsh);
        String pch = collectionPersonalBusinessDetailsDAO.getByYwlsh(ywlsh).getExtension().getPch();
        String JZPZH = housingBusinessDetailsDAO.getJzph(pch);
        for (String lsh : lshs) {
            doFinal(lsh);
            StCollectionPersonalBusinessDetails details = collectionPersonalBusinessDetailsDAO.getByYwlsh(lsh);
            details.getExtension().setJzpzh(JZPZH);
            collectionPersonalBusinessDetailsDAO.update(details);
        }

    }

    @Override
    public void withdrawlTask(String ywlsh) {
        List<StCollectionPersonalBusinessDetails> details = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("gjhtqywlx", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));
            this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待入账.getName());
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });
        for (StCollectionPersonalBusinessDetails detail : details) {
            if (new Date().getTime() > (detail.getExtension().getShsj().getTime() + 432000000)) {
                detail.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                detail.getExtension().setSbyy(StringUtil.notEmpty(detail.getExtension().getSbyy()) ? detail.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 未收到到账通知，请验证后手动处理"
                        : DateUtil.date2Str(new Date(), df) + " 未收到到账通知，请验证后手动处理");
                collectionPersonalBusinessDetailsDAO.update(detail);
            }
        }
    }

    /**
     * 手动修改入账失败的提取
     *
     * @param tokenContext
     * @param ywlsh
     * @param operation    0入账成功 1作废
     * @return
     */
    @Override
    public CommonReturn doFailedWithdrawl(TokenContext tokenContext, String ywlsh, String operation) {
        if (StringUtil.isEmpty(ywlsh)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号不能为空");
        }
        if (StringUtil.isEmpty(operation) || !Arrays.asList("0", "1").contains(operation)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "操作类型不能为空");
        }
        StCollectionPersonalBusinessDetails details = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });
        if (!tokenContext.getUserInfo().getCZY().equals(details.getExtension().getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不是由您受理的");
        }
        if (!CollectionBusinessStatus.入账失败.getName().equals(details.getExtension().getStep())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务当前状态不能手动处理");
        }
        //审核人，该条记录审核通过的操作员
        List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", ywlsh);
                this.put("shjg", "01");
            }
        }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
//                    exceptions.add(e);
            }
        });

        String ZhaiYao = getZhaiYao(details,"1");
        if ("0".equals(operation)) {
            VoucherRes voucherRes = new VoucherRes();
            if (CollectionBusinessType.部分提取.getCode().equals(details.getGjhtqywlx())) {
                voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", details.getExtension().getCzy(), cAuditHistory.size() == 0 ? details.getExtension().getCzy() : cAuditHistory.get(0).getCzy(), "", "管理员",
                        VoucherBusinessType.部分提取.getCode(), VoucherBusinessType.部分提取.getCode(), ywlsh, ZhaiYao, details.getWithdrawlVice().getFse().abs(), null, null);
            } else {
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                VoucherAmount jfsj = new VoucherAmount();
                jfsj.setZhaiYao(ZhaiYao);
                jfsj.setJinE(details.getWithdrawlVice().getFse().abs());
                JFSJ.add(jfsj);

                VoucherAmount jfsj1 = new VoucherAmount();
                jfsj1.setZhaiYao(ZhaiYao);
                jfsj1.setJinE(details.getExtension().getXhtqfslxe().abs());
                JFSJ.add(jfsj1);

                VoucherAmount dfsj = new VoucherAmount();
                dfsj.setJinE(details.getWithdrawlVice().getFse().abs().add(details.getExtension().getXhtqfslxe().abs()));
                dfsj.setZhaiYao(ZhaiYao);
                DFSJ.add(dfsj);
                voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", details.getExtension().getCzy(), cAuditHistory.size() == 0 ? details.getExtension().getCzy() : cAuditHistory.get(0).getCzy(),
                        "", "管理员", VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(), ywlsh, JFSJ, DFSJ, String.valueOf(djsl), (String) null, null);
            }

            if (voucherRes.getJZPZH() != null) {
                details.getExtension().setJzpzh(voucherRes.getJZPZH());
                doFinal(ywlsh);
            } else {
                details.getExtension().setSbyy(StringUtil.notEmpty(details.getExtension().getSbyy()) ? details.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + voucherRes.getMSG() :
                        DateUtil.date2Str(new Date(), df) + voucherRes.getMSG());
                details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
            }
            collectionPersonalBusinessDetailsDAO.update(details);
        } else {
            details.getPerson().getExtension().setXctqrq(null);
            details.getExtension().setStep(CollectionBusinessStatus.已作废.getName());
            details.getExtension().setDqye(details.getPerson().getCollectionPersonalAccount().getGrzhye());
        }
        return new CommonReturn() {{
            this.setStatus("success");
        }};

    }


    public JKRAndGTJKR compute(BigDecimal jkrje, BigDecimal gtjkrje, BigDecimal bjje, BigDecimal lxe) {
        try {
            JKRAndGTJKR result = new JKRAndGTJKR();
            result.setJkrfse(bjje.multiply(jkrje.divide(jkrje.add(gtjkrje), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.setJkrfslxe(lxe.multiply(jkrje.divide(jkrje.add(gtjkrje), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.setGtjkrfse(bjje.subtract(result.getJkrfse()));
            result.setGtjkrfslxe(lxe.subtract(result.getJkrfslxe()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理已入账但银行返回失败的记录
     *
     * @param ywlsh
     */
    @Override
    public void doRecoverWithdrawl(String ywlsh) {

        StCollectionPersonalBusinessDetails details = collectionPersonalBusinessDetailsDAO.getByYwlsh(ywlsh);
        StCommonPerson person = commonPersonDAO.getByGrzh(details.getGrzh());
        //region 恢复个人账户余额，更新业务的发生额,恢复个人账户状态
        person.getCollectionPersonalAccount().setGrzhye(person.getCollectionPersonalAccount().getGrzhye().add(details.getFse().abs()));
        details.setFse(new BigDecimal(0));
        details.setFslxe(new BigDecimal(0));
        details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
//        details.getExtension().setDqye();
        details.getExtension().setSbyy(StringUtil.notEmpty(details.getExtension().getSbyy()) ?
                details.getExtension().getSbyy() + DateUtil.date2Str(new Date(), df) + " 银行处理失败，请作废并重新办理该业务"
                : DateUtil.date2Str(new Date(), df) + " 银行处理失败，请作废并重新办理该业务");
        details.getExtension().setJzpzh(null);
        if (CollectionBusinessType.销户提取.getCode().equals(details.getGjhtqywlx())) {
            person.getCollectionPersonalAccount().setXhyy(null);//销户原因
            person.getCollectionPersonalAccount().setXhrq(null);//销户日期
            person.getCollectionPersonalAccount().setGrzhzt(details.getExtension().getBeizhu());
        }
        person.getExtension().setXctqrq(null);

        //endregion

        //region 单位业务表
        StCollectionUnitBusinessDetails collectionUnitDetails = collectionUnitBusinessDetailsDAO.getByYwlsh(details.getExtension().getZcdw());
        collectionUnitDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionUnitDetails.getUnit().getCollectionUnitAccount().getDwzhye().add(collectionUnitDetails.getFse().abs()));
        collectionUnitDetails.setFse(details.getFse());
        collectionUnitDetails.setFslxe(details.getFslxe());
        collectionUnitDetails.getExtension().setStep(details.getExtension().getStep());
        //endregion

        //删除凭证
        voucherManagerService.rehedgeVoucher(ywlsh);

        commonPersonDAO.update(person);
        collectionPersonalBusinessDetailsDAO.update(details);
        collectionUnitBusinessDetailsDAO.update(collectionUnitDetails);
    }


    /**
     * 生成摘要
     */
    public String getZhaiYao(StCollectionPersonalBusinessDetails details,String model) {
        HashMap<String, String> reasonMap = new HashMap<String, String>();
        reasonMap.put(WithDrawalReason.REASON_1.getCode(), WithDrawalReason.REASON_1.getName());
        reasonMap.put(WithDrawalReason.REASON_2.getCode(), WithDrawalReason.REASON_2.getName());
        reasonMap.put(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_3.getName());
        reasonMap.put(WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_4.getName());
        reasonMap.put(WithDrawalReason.REASON_5.getCode(), WithDrawalReason.REASON_5.getName());
        reasonMap.put(WithDrawalReason.REASON_6.getCode(), WithDrawalReason.REASON_6.getName());
        reasonMap.put(WithDrawalReason.REASON_7.getCode(), WithDrawalReason.REASON_7.getName());
        reasonMap.put(WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_8.getName());
        reasonMap.put(WithDrawalReason.REASON_9.getCode(), WithDrawalReason.REASON_9.getName());
        reasonMap.put(WithDrawalReason.REASON_10.getCode(), WithDrawalReason.REASON_10.getName());

        if(model.equals("1")){
            String ZhaiYao = "";
            if (CollectionBusinessType.部分提取.getCode().equals(details.getGjhtqywlx())) {
                ZhaiYao = details.getPerson().getXingMing() + reasonMap.get(details.getTqyy()) + " 提取公积金";
            } else {
                ZhaiYao = details.getPerson().getXingMing() + reasonMap.get(details.getTqyy()) + " 销户提取";
                if (WithDrawalReason.REASON_5.equals(details.getTqyy())) {
                    ZhaiYao = details.getPerson().getXingMing() + reasonMap.get(details.getTqyy()) + " 外部转出";
                }
            }
            return ZhaiYao;
        }else{
            return reasonMap.get(details.getTqyy());
        }

    }

}
