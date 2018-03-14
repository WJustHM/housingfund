package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.account.model.RpcAuth;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.loan.IHousingCompany;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.*;
import static com.handge.housingfund.common.service.util.StringUtil.isIntoReview;
import static com.handge.housingfund.common.service.util.StringUtil.stringEquals;
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;
import static com.handge.housingfund.database.enums.BusinessSubType.贷款_房开申请受理;
import static com.handge.housingfund.database.enums.BusinessType.Loan;
import static com.handge.housingfund.database.enums.Events.通过;
import static com.handge.housingfund.database.utils.DAOBuilder.ErrorHandler;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;
import static com.handge.housingfund.loan.utils.StateMachineUtils.StateChangeHandler;
import static com.handge.housingfund.loan.utils.StateMachineUtils.updateState;

/**
 * Created by 向超 on 2017/8/9.
 */
@SuppressWarnings("Duplicates")
@Component
public class HousingCompanyImpl implements IHousingCompany {
    @Autowired
    ICAuditHistoryDAO auditHistoryDAO;
    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;
    @Autowired
    private ICLoanHousingCompanyBasicDAO icLoanHousingCompanyBasicDAO;
    @Autowired
    private ICLoanEatateProjectBasicDAO loanEatateProjectBasicDAO;
    @Autowired
    private ICloanHousingCompanyViceExtensionDAO cloanHousingCompanyViceExtensionDAO;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private AccountRpcService accountRpcService;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    private IDictionaryService iDictionaryService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd HH:mm";

    //测试通过
    @Override
    public PageRes<HousingCompaniesListRes> getHousingCompanyInfo(TokenContext tokenContext, String FKGS, String FKZH, Boolean SFQY, String kssj, String jssj, String pageNo, String pageSize) {
        System.out.println("房开信息");
        Date start = null;
        Date end = null;
        try {
            if (StringUtil.notEmpty(kssj)) start = DateUtil.str2Date(format, kssj);
            if (StringUtil.notEmpty(jssj)) end = DateUtil.str2Date(format, jssj);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "时间格式不正确,正确格式为yyyy-MM-dd HH:mm");
        }
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).betweenDate(start, end).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(FKGS)) this.put("fkgs", FKGS);
            if (StringUtil.notEmpty(FKZH)) this.put("fkgszh", FKZH);
            if (SFQY != null) this.put("sfqy", SFQY);
        }}).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<HousingCompaniesListRes>() {{
            this.setResults(new ArrayList<HousingCompaniesListRes>() {{
                for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
                    CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("id", cLoanHousingCompanyBasic.getYwwd());
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    this.add(new HousingCompaniesListRes() {{
                        this.setDWDZ(cLoanHousingCompanyBasic.getDwdz());//单位地址

                        this.setLXR(cLoanHousingCompanyBasic.getLxr());//联系人

                        this.setFKGS(cLoanHousingCompanyBasic.getFkgs());//房开公司
                        if (cLoanHousingCompanyBasic.getSfqy() != null) {
                            if (cLoanHousingCompanyBasic.getSfqy()) {
                                this.setSFQY("是");//是否启用
                            } else {
                                this.setSFQY("否");//是否启用
                            }
                        }

                        this.setFKZH(cLoanHousingCompanyBasic.getFkgszh());//房开账号

                        this.setYWWD(network == null ? "" : network.getMingCheng());//业务网点

                        this.setLXDH(cLoanHousingCompanyBasic.getLxdh());//联系电话

                        this.setCZY(cLoanHousingCompanyBasic.getCzy());//操作员

                        this.setZZJGDM(cLoanHousingCompanyBasic.getZzjgdm());//组织机构代码

                        this.setSLSJ(DateUtil.date2Str(cLoanHousingCompanyBasic.getCreated_at(), format));//受理时间
                    }});

                }

            }});
            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};

    }

    @Override
    public PageResNew<HousingCompaniesListRes> getHousingCompanyInfoNew(TokenContext tokenContext, String FKGS, String FKZH, Boolean SFQY, String kssj, String jssj, String marker, String pageSize,String action) {
        System.out.println("房开信息");
        Date start = null;
        Date end = null;
        try {
            if (StringUtil.notEmpty(kssj)) start = DateUtil.str2Date(format, kssj);
            if (StringUtil.notEmpty(jssj)) end = DateUtil.str2Date(format, jssj);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "时间格式不正确,正确格式为yyyy-MM-dd HH:mm");
        }
        int pagesize_number = 10;
        try {

            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).betweenDate(start, end).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(FKGS)) this.put("fkgs", FKGS);
            if (StringUtil.notEmpty(FKZH)) this.put("fkgszh", FKZH);
        }}).pageOption(marker, action, pagesize_number).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (SFQY != null) criteria.add(Restrictions.eq("sfqy", SFQY));
            }
        }).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageResNew<HousingCompaniesListRes>() {{
            this.setResults(action,new ArrayList<HousingCompaniesListRes>() {{
                for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
                    CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                        {
                            this.put("id", cLoanHousingCompanyBasic.getYwwd());
                        }
                    }).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    this.add(new HousingCompaniesListRes() {{
                        this.setId(cLoanHousingCompanyBasic.getId());
                        this.setDWDZ(cLoanHousingCompanyBasic.getDwdz());//单位地址

                        this.setLXR(cLoanHousingCompanyBasic.getLxr());//联系人

                        this.setFKGS(cLoanHousingCompanyBasic.getFkgs());//房开公司
                        if (cLoanHousingCompanyBasic.getSfqy() != null) {
                            if (cLoanHousingCompanyBasic.getSfqy()) {
                                this.setSFQY("是");//是否启用
                            } else {
                                this.setSFQY("否");//是否启用
                            }
                        }

                        this.setFKZH(cLoanHousingCompanyBasic.getFkgszh());//房开账号

                        this.setYWWD(network == null ? "" : network.getMingCheng());//业务网点

                        this.setLXDH(cLoanHousingCompanyBasic.getLxdh());//联系电话

                        this.setCZY(cLoanHousingCompanyBasic.getCzy());//操作员

                        this.setZZJGDM(cLoanHousingCompanyBasic.getZzjgdm());//组织机构代码

                        this.setSLSJ(DateUtil.date2Str(cLoanHousingCompanyBasic.getCreated_at(), format));//受理时间
                    }});
                }
            }});
        }};
    }

    //测试通过xc
    @Override
    public CommonResponses addHousingCompany(TokenContext tokenContext, String CZLX, HousingCompanyPost housingCompanyInfo) {
        System.out.println("添加房开信息");
        //检查上传资料
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.房开公司申请.getCode(), housingCompanyInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        if(StringUtil.notEmpty(housingCompanyInfo.getZCZJ())&&!StringUtil.isDigits(housingCompanyInfo.getZCZJ())){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"注册资金");
        }
        CLoanHousingCompanyVice cLoanHousingCompanyVice = new CLoanHousingCompanyVice();//构造房开公司副表
        cLoanHousingCompanyVice.setFkgs(housingCompanyInfo.getFKGS());//房开公司

        cLoanHousingCompanyVice.setSjflb(housingCompanyInfo.getSJFLB());//售建房类别
        cLoanHousingCompanyVice.setZzdj(housingCompanyInfo.getZZDJ());//资质等级
        cLoanHousingCompanyVice.setDwdz(housingCompanyInfo.getDWDZ());//单位地址
        cLoanHousingCompanyVice.setZcdz(housingCompanyInfo.getZCDZ());//注册地址
        cLoanHousingCompanyVice.setZczj(new BigDecimal(StringUtil.isEmpty(housingCompanyInfo.getZCZJ()) ? "0" : housingCompanyInfo.getZCZJ()));//注册资金
        cLoanHousingCompanyVice.setFrdb(housingCompanyInfo.getFRDB());//法人代表
        cLoanHousingCompanyVice.setFrdbzjlx(housingCompanyInfo.getFRDBZJLX());//法人代表证件类型
        cLoanHousingCompanyVice.setFrdbzjhm(housingCompanyInfo.getFRDBZJHM());//法人代表证件号码
        ArrayList<CLoanHousingCompanyViceExtension> cLoanHousingCompanyViceExtensions = new ArrayList<>();
        for (HousingCompanyInfoSale housingCompanyInfoSale : housingCompanyInfo.getHousingCompanyInfoSaleList()) {
            CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension = new CLoanHousingCompanyViceExtension();
            cLoanHousingCompanyViceExtension.setSFRKHYHKHM(housingCompanyInfoSale.getSFRKHYHKHM());////售房人开户银行开户名
            cLoanHousingCompanyViceExtension.setSFRZHHM(housingCompanyInfoSale.getSFRZHHM());////售房人账户号码
            cLoanHousingCompanyViceExtension.setSFRKHYHMC(housingCompanyInfoSale.getSFRKHYHMC());//售房人开户银行名称
            cLoanHousingCompanyViceExtension.setFkgsfb(cLoanHousingCompanyVice);
            cLoanHousingCompanyViceExtensions.add(cLoanHousingCompanyViceExtension);
        }
        cLoanHousingCompanyVice.setLoanHousingCompanyViceExtensions(cLoanHousingCompanyViceExtensions);

        cLoanHousingCompanyVice.setLxr(housingCompanyInfo.getLXR());//联系人
        cLoanHousingCompanyVice.setLxdh(housingCompanyInfo.getLXDH());//联系人电话
        cLoanHousingCompanyVice.setZzjgdm(housingCompanyInfo.getZZJGDM());//组织机构代码
        cLoanHousingCompanyVice.setBzjzh(housingCompanyInfo.getBZJZH());//保证金账户
        cLoanHousingCompanyVice.setBzjzhkhh(housingCompanyInfo.getBZJZHKHH());//保证金账户开户行
        cLoanHousingCompanyVice.setBzjkhm(housingCompanyInfo.getBZJKHM());//保证金开户名
        cLoanHousingCompanyVice.setBeiZhu(housingCompanyInfo.getBeiZhu());//备注
        cLoanHousingCompanyVice.setBlzl(housingCompanyInfo.getBLZL());//办理资料
        // cLoanHousingCompanyVice.setSfbg();//是否变更


        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = new CLoanHousingBusinessProcess();//构造业务表
        cLoanHousingBusinessProcess.setLoanHousingCompanyVice(cLoanHousingCompanyVice);
        cLoanHousingCompanyVice.setGrywmx(cLoanHousingBusinessProcess);
        cLoanHousingBusinessProcess.setCznr(LoanBusinessType.新建房开.getCode());
        cLoanHousingBusinessProcess.setBlzl(housingCompanyInfo.getBLZL());
        cLoanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());//操作员
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
        cLoanHousingBusinessProcess.setYwwd(network);//业务网点
        cLoanHousingBusinessProcess.setStep(LoanBussinessStatus.初始状态.getName());
        if (!Arrays.asList("0", "1").contains(CZLX))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 = instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if(CZLX.equals("1")){
        //验证组织机构唯一性
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
            if (cLoanHousingCompanyBasic.getZzjgdm().equals(housingCompanyInfo.getZZJGDM())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码不能重复");
            }
            if (cLoanHousingCompanyBasic.getFkgs().equals(housingCompanyInfo.getFKGS())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "房开公司名称不能重复");
            }
        }
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", Events.通过.getEvent());
            this.put("1", Events.提交.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess1.getYwlsh(), cLoanHousingBusinessProcess1.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                cLoanHousingBusinessProcess1.getCzy(), cLoanHousingCompanyVice.getBeiZhu(), 贷款_房开申请受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, cLoanHousingBusinessProcess1.getYwwd().getId()), new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {
                if (e != null) {
                    throw new ErrorException(e);
                }
                if (!StringUtil.notEmpty(next) || e != null) {
                    return;
                }

                if (succeed) {
                    //缺少step
                    cLoanHousingBusinessProcess.setStep(next);

                    if (StringUtil.isIntoReview(next, null)) cLoanHousingBusinessProcess.setDdsj(new Date());

                    instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    if (next.equals(LoanBussinessStatus.办结.getName()))
                        doHousingCompany(cLoanHousingBusinessProcess1.getYwlsh());
                }
            }
        });
        if (CZLX.equals("1")) {
            checkParam(cLoanHousingBusinessProcess1);//参数完整性验证
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs",housingCompanyInfo.getFKGS());
                this.put("cznr", LoanBusinessType.新建房开.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", cLoanHousingBusinessProcess1.getYwlsh()));
                    criteria.createAlias("loanHousingCompanyVice", "loanHousingCompanyVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM()), Restrictions.eq("loanHousingCompanyVice.fkgs", housingCompanyInfo.getFKGS())));
                    criteria.add(Restrictions.like("step",LoanBussinessStatus.待某人审核.getName()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process);
            }
        }
        saveAuditHistory.saveNormalBusiness(cLoanHousingBusinessProcess.getYwlsh(), tokenContext, LoanBusinessType.新建房开.getName(), "新建");

        return new CommonResponses() {{
            this.setId(cLoanHousingBusinessProcess1.getYwlsh());
            this.setState("success");
        }};
    }

    //测试成功
    @Override
    public CommonResponses reHousingCompanyInfo(TokenContext tokenContext, String YWLSH, String CZLX, HousingCompanyPost housingCompanyInfo) {
        System.out.println("修改房开信息");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanHousingCompanyVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }
        if (!tokenContext.getUserInfo().getCZY().equals(cLoanHousingBusinessProcess.getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied);
        }
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.房开公司申请.getCode(), housingCompanyInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料缺失");
        }
        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();

        DAOBuilder.instance(cloanHousingCompanyViceExtensionDAO).entities(cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        cLoanHousingCompanyVice.setFkgs(housingCompanyInfo.getFKGS());//房开公司
        cLoanHousingCompanyVice.setSjflb(housingCompanyInfo.getSJFLB());//售建房类别   类型不对应
        cLoanHousingCompanyVice.setZzdj(housingCompanyInfo.getZZDJ());//资质等级
        cLoanHousingCompanyVice.setDwdz(housingCompanyInfo.getDWDZ());//单位地址
        cLoanHousingCompanyVice.setZcdz(housingCompanyInfo.getZCDZ());//注册地址
        cLoanHousingCompanyVice.setZczj(new BigDecimal(StringUtil.notEmpty(housingCompanyInfo.getZCZJ()) ? housingCompanyInfo.getZCZJ() : "0"));//注册资金
        cLoanHousingCompanyVice.setFrdb(housingCompanyInfo.getFRDB());//法人代表
        cLoanHousingCompanyVice.setFrdbzjlx(housingCompanyInfo.getFRDBZJLX());//法人代表证件类型
        cLoanHousingCompanyVice.setFrdbzjhm(housingCompanyInfo.getFRDBZJHM());//法人代表证件号码

        ArrayList<CLoanHousingCompanyViceExtension> cLoanHousingCompanyViceExtensions = new ArrayList<>();
        for (HousingCompanyInfoSale housingCompanyInfoSale : housingCompanyInfo.getHousingCompanyInfoSaleList()) {
            CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension = new CLoanHousingCompanyViceExtension();
            cLoanHousingCompanyViceExtension.setSFRKHYHKHM(housingCompanyInfoSale.getSFRKHYHKHM());////售房人开户银行开户名
            cLoanHousingCompanyViceExtension.setSFRZHHM(housingCompanyInfoSale.getSFRZHHM());////售房人账户号码
            cLoanHousingCompanyViceExtension.setSFRKHYHMC(housingCompanyInfoSale.getSFRKHYHMC());//售房人开户银行名称
            cLoanHousingCompanyViceExtension.setFkgsfb(cLoanHousingCompanyVice);
            cLoanHousingCompanyViceExtensions.add(cLoanHousingCompanyViceExtension);
        }
        cLoanHousingCompanyVice.setLoanHousingCompanyViceExtensions(cLoanHousingCompanyViceExtensions);

        cLoanHousingCompanyVice.setLxr(housingCompanyInfo.getLXR());//联系人
        cLoanHousingCompanyVice.setLxdh(housingCompanyInfo.getLXDH());//联系人电话
        cLoanHousingCompanyVice.setZzjgdm(housingCompanyInfo.getZZJGDM());//组织机构代码
        cLoanHousingCompanyVice.setBzjzh(housingCompanyInfo.getBZJZH());//保证金账户
        cLoanHousingCompanyVice.setBzjzhkhh(housingCompanyInfo.getBZJZHKHH());//保证金账户开户行
        cLoanHousingCompanyVice.setBzjkhm(housingCompanyInfo.getBZJKHM());//保证金开户名
        cLoanHousingCompanyVice.setBeiZhu(housingCompanyInfo.getBeiZhu());//备注
        cLoanHousingCompanyVice.setBlzl(housingCompanyInfo.getBLZL());//办理资料
        // cLoanHousingCompanyVice.setSfbg();//是否变更
        cLoanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        cLoanHousingBusinessProcess.setBlzl(housingCompanyInfo.getBLZL());
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
        cLoanHousingBusinessProcess.setYwwd(network);//业务网点
        cLoanHousingBusinessProcess.setLoanHousingCompanyVice(cLoanHousingCompanyVice);


        CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 = instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
       if(CZLX.equals("1")){
        //组织机构代码唯一性验证
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
            if (cLoanHousingCompanyBasic.getZzjgdm().equals(housingCompanyInfo.getZZJGDM())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码不能重复");
            }
            if (cLoanHousingCompanyBasic.getFkgs().equals(housingCompanyInfo.getFKGS())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "房开公司名称不能重复");
            }
        }
       }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", Events.保存.getEvent());
            this.put("1", Events.通过.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess1.getYwlsh(), cLoanHousingBusinessProcess1.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                tokenContext.getUserInfo().getCZY(), cLoanHousingCompanyVice.getBeiZhu(), BusinessSubType.贷款_房开申请受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, tokenContext.getUserInfo().getYWWD()), new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {
                if (e != null) {
                    throw new ErrorException(e);
                }
                if (!StringUtil.notEmpty(next) || e != null) {
                    return;
                }

                if (succeed) {
                    //缺少step
                    cLoanHousingBusinessProcess.setStep(next);
                    if (StringUtil.isIntoReview(next, null)) cLoanHousingBusinessProcess.setDdsj(new Date());
                    instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    if (next.equals(LoanBussinessStatus.办结.getName())) doHousingCompany(YWLSH);
                }
            }
        });
        if (CZLX.equals("1")) {
            //参数完整性验证
            checkParam(cLoanHousingBusinessProcess1);
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.新建房开.getName(), "修改");
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs", housingCompanyInfo.getFKGS());
                this.put("cznr", LoanBusinessType.新建房开.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", YWLSH));
                    criteria.createAlias("loanHousingCompanyVice", "loanHousingCompanyVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM()), Restrictions.eq("loanHousingCompanyVice.fkgs", housingCompanyInfo.getFKGS())));
                    criteria.add(Restrictions.like("step",LoanBussinessStatus.待某人审核.getName()));
                }

            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process);
            }
        }

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    //测试通过xc
    @Override
    public HousingIdGet showHousingCompanyInfo(String YWLSH, String type) {
        System.out.println("房开详情");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            // if (type.equals("1"))
            //     this.put("step", Arrays.asList(LoanBussinessStatus.新建.getName(), LoanBussinessStatus.待审核.getName(), LoanBussinessStatus.审核不通过.getName()));
            if (type.equals("2")) this.put("step", LoanBussinessStatus.办结.getName());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if(type.equals("1"))
                criteria.add(Restrictions.ne("step",LoanBussinessStatus.办结.getName()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanHousingCompanyVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();
        ArrayList<HousingCompanyInfoSale> housingCompanyInfoSale = new ArrayList<>();
        for (CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension : cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()) {
            if (!cLoanHousingCompanyViceExtension.isDeleted()) {
                HousingCompanyInfoSale housingCompanyInfoSale1 = new HousingCompanyInfoSale();
                housingCompanyInfoSale1.setSFRKHYHMC(cLoanHousingCompanyViceExtension.getSFRKHYHMC());//售房人开户银行名称
                housingCompanyInfoSale1.setSFRZHHM(cLoanHousingCompanyViceExtension.getSFRZHHM()); //售房人账户号码
                housingCompanyInfoSale1.setSFRKHYHKHM(cLoanHousingCompanyViceExtension.getSFRKHYHKHM()); //售房人开户银行开户名
                housingCompanyInfoSale.add(housingCompanyInfoSale1);
            }
        }

        return new HousingIdGet() {{
            this.setZCZJ(cLoanHousingCompanyVice.getZczj() == null ? "0" : cLoanHousingCompanyVice.getZczj().toPlainString());//注册资金

            this.setZZDJ(cLoanHousingCompanyVice.getZzdj());//资质等级（0：一级 1：二级 2：三级 3：四级）

            this.setYWLSH(cLoanHousingBusinessProcess.getYwlsh());//业务流水号


            this.setFKGSZH(cLoanHousingCompanyVice.getFkgszh());  //房开公司账号

            this.setFRDBZJLX(cLoanHousingCompanyVice.getFrdbzjlx());//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）


            this.setLXR(cLoanHousingCompanyVice.getLxr());//联系人


            this.setHousingCompanyInfoSales(housingCompanyInfoSale);

            this.setManagerInformation(new HousingIdGetManagerInformation() {{
                this.setCZY(cLoanHousingBusinessProcess.getCzy());
                this.setYWWD(cLoanHousingBusinessProcess.getYwwd().getMingCheng());
            }});

            this.setLXDH(cLoanHousingCompanyVice.getLxdh());//联系电话

            this.setFRDBZJHM(cLoanHousingCompanyVice.getFrdbzjhm());//法人代表证件号码

            this.setZZJGDM(cLoanHousingCompanyVice.getZzjgdm());//组织机构代码

            this.setZCDZ(cLoanHousingCompanyVice.getZcdz());//注册地址

            this.setFRDB(cLoanHousingCompanyVice.getFrdb());//法人代表

            this.setBeiZhu(cLoanHousingCompanyVice.getBeiZhu());//备注

            this.setDWDZ(cLoanHousingCompanyVice.getDwdz());//单位地址

            this.setFKGS(cLoanHousingCompanyVice.getFkgs());//房开公司

            this.setSJFLB(cLoanHousingCompanyVice.getSjflb());//售建房类别（0：开发商 1：个人 2：其他）

            this.setBLZL(StringUtil.notEmpty(cLoanHousingCompanyVice.getBlzl()) ? cLoanHousingCompanyVice.getBlzl() : "");//办理资料

            this.setBZJZH(cLoanHousingCompanyVice.getBzjzh());//保证金账户

            this.setBZJKHH(cLoanHousingCompanyVice.getBzjzhkhh());//保证金账户开户行

            this.setBZJKHM(cLoanHousingCompanyVice.getBzjkhm());//保证金开户名

        }};
    }

    //测试通过xc
    @Override
    public PageRes getHousingCompanyInfoAccept(TokenContext tokenContext, String FKGS, String ZHUANGTAI, String KSSJ,String JSSJ ,String pageNo, String pageSize) {
        System.out.println("查询过程中房开信息");
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                this.put("step", ZHUANGTAI);
            if (StringUtil.notEmpty(FKGS)) this.put("loanHousingCompanyVice.fkgs", FKGS);
            this.put("cznr", LoanBusinessType.新建房开.getCode());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (CollectionBusinessStatus.待审核.getName().equals(ZHUANGTAI)) {
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                criteria.createAlias("ywwd", "ywwd");

                criteria.add(Restrictions.eq("ywwd.id", tokenContext.getUserInfo().getYWWD()));
            }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageRes<HousingCompanyRecordsRes>() {{
            this.setResults(new ArrayList<HousingCompanyRecordsRes>() {{
                for (CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 : cLoanHousingBusinessProcess) {
                    this.add(new HousingCompanyRecordsRes() {{
                        this.setYWLSH(cLoanHousingBusinessProcess1.getYwlsh());//业务流水号
                        this.setFKGS(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgs());//房开公司
                        this.setZZJGDM(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getZzjgdm());//组织机构代码
                        this.setDWDZ(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getDwdz());//单位地址
                        this.setZhuangTai(cLoanHousingBusinessProcess1.getStep());//状态
                        this.setCZY(cLoanHousingBusinessProcess1.getCzy());//操作员
                        this.setYWWD(cLoanHousingBusinessProcess1.getYwwd().getMingCheng());//业务网点
                        this.setSLSJ(DateUtil.date2Str(cLoanHousingBusinessProcess1.getCreated_at(), format));//受理时间
                    }});
                }
            }});
            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }

    @Override
    public PageResNew getHousingCompanyInfoAcceptNew(TokenContext tokenContext, String FKGS, String ZHUANGTAI, String KSSJ, String JSSJ , String marker, String pageSize,String action) {
        System.out.println("查询过程中房开信息");
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(FKGS)) this.put("loanHousingCompanyVice.fkgs", FKGS);
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.eq("cznr", LoanBusinessType.新建房开.getCode()));
                if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                    criteria.add(Restrictions.eq("step", ZHUANGTAI));
                if (CollectionBusinessStatus.待审核.getName().equals(ZHUANGTAI)) {
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                    criteria.createAlias("ywwd", "ywwd");

                    criteria.add(Restrictions.eq("ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(marker, action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageResNew<HousingCompanyRecordsRes>() {{
            this.setResults(action,new ArrayList<HousingCompanyRecordsRes>() {{
                for (CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 : cLoanHousingBusinessProcess) {
                    this.add(new HousingCompanyRecordsRes() {{
                        this.setId(cLoanHousingBusinessProcess1.getId());
                        this.setYWLSH(cLoanHousingBusinessProcess1.getYwlsh());//业务流水号
                        this.setFKGS(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgs());//房开公司
                        this.setZZJGDM(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getZzjgdm());//组织机构代码
                        this.setDWDZ(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getDwdz());//单位地址
                        this.setZhuangTai(cLoanHousingBusinessProcess1.getStep());//状态
                        this.setCZY(cLoanHousingBusinessProcess1.getCzy());//操作员
                        this.setYWWD(cLoanHousingBusinessProcess1.getYwwd().getMingCheng());//业务网点
                        this.setSLSJ(DateUtil.date2Str(cLoanHousingBusinessProcess1.getCreated_at(), format));//受理时间
                    }});
                }
            }});
        }};
    }


    //测试通过
    @Override
    public CommonResponses receiptHousingCompany(String YWLSH) {
        System.out.println("房开回执单");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanHousingCompanyVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }
        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();
        ArrayList<HousingCompanyInfoSale> housingCompanyInfoSale = new ArrayList<HousingCompanyInfoSale>();
        for (CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension : cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()) {
            if (!cLoanHousingCompanyViceExtension.isDeleted()) {
                HousingCompanyInfoSale housingCompanyInfoSale1 = new HousingCompanyInfoSale();
                housingCompanyInfoSale1.setSFRKHYHMC(cLoanHousingCompanyViceExtension.getSFRKHYHMC());//售房人开户银行名称
                housingCompanyInfoSale1.setSFRZHHM(cLoanHousingCompanyViceExtension.getSFRZHHM()); //售房人账户号码
                housingCompanyInfoSale1.setSFRKHYHKHM(cLoanHousingCompanyViceExtension.getSFRKHYHKHM()); //售房人开户银行开户名
                housingCompanyInfoSale.add(housingCompanyInfoSale1);
            }
        }
        ApplyHousingCompanyReceipt CompanyReceipt = new ApplyHousingCompanyReceipt();
        ApplyHousingCompanyReceiptUnitInfo applyReceipt = new ApplyHousingCompanyReceiptUnitInfo();

        CompanyReceipt.setYWLSH(YWLSH);//业务无流水号
        CompanyReceipt.setCZY(cLoanHousingBusinessProcess.getCzy());//操作员
        // this.setTZRQ(new Date());//填制日期
        // this.setYZM();//验证码
        if (cLoanHousingCompanyVice.getZczj() != null) {
            applyReceipt.setZCZJ(cLoanHousingCompanyVice.getZczj().toPlainString());//注册资金
        }
        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getZzdj())) {
            SingleDictionaryDetail ZZDJ = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getZzdj(), "LoanQualificationLevel");
            applyReceipt.setZZDJ(ZZDJ != null ? ZZDJ.getName() : "");//资质等级（0：一级 1：二级 2：三级 3：四级）
        }
        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getFrdbzjlx())) {
            SingleDictionaryDetail FRDBZJLX = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getFrdbzjlx(), "PersonalCertificate");
            applyReceipt.setFRDBZJLX(FRDBZJLX != null ? FRDBZJLX.getName() : "");//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）
        }


        applyReceipt.setLXR(cLoanHousingCompanyVice.getLxr());//联系人

        applyReceipt.setHousingCompanyInfoSales(housingCompanyInfoSale);

        applyReceipt.setLXDH(cLoanHousingCompanyVice.getLxdh());//联系电话

        applyReceipt.setFRDBZJHM(cLoanHousingCompanyVice.getFrdbzjhm());//法人代表证件号码

        applyReceipt.setZZJGDM(cLoanHousingCompanyVice.getZzjgdm());//组织机构代码

        applyReceipt.setZCDZ(cLoanHousingCompanyVice.getZcdz());//注册地址

        applyReceipt.setFRDB(cLoanHousingCompanyVice.getFrdb());//法人代表

        applyReceipt.setBeiZhu(cLoanHousingCompanyVice.getBeiZhu());//备注

        applyReceipt.setDWDZ(cLoanHousingCompanyVice.getDwdz());//单位地址
        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getSjflb())) {
            SingleDictionaryDetail sjflb = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getSjflb(), "LoanHouseType");
            applyReceipt.setSJFLB(sjflb != null ? sjflb.getName() : "");//售建房类别（0：开发商 1：个人 2：其他）
        }
        applyReceipt.setFKGS(cLoanHousingCompanyVice.getFkgs());//房开公司

        applyReceipt.setFKZH(cLoanHousingCompanyVice.getFkgszh());//房开账号

        applyReceipt.setBZJZH(cLoanHousingCompanyVice.getBzjzh());//保证金账户

        applyReceipt.setBZJZHKHH(cLoanHousingCompanyVice.getBzjzhkhh());//保证金账户开户行

        applyReceipt.setBZJKHM(cLoanHousingCompanyVice.getBzjkhm());//保证金开户名

        applyReceipt.setYWWD(cLoanHousingBusinessProcess.getYwwd().getMingCheng());

        CompanyReceipt.setUnitInfo(applyReceipt);
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
            CompanyReceipt.setSHR(cAuditHistory.getCzy());
        }

        String id = pdfService.getApplyHousingCompanyReceipt(CompanyReceipt, LoanBusinessType.新建房开.getCode());
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    //测试通过xc
    @Override
    public void doHousingCompany(String YWLSH) {
        System.out.println("审核通过后添加房开信息");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanHousingCompanyVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = new CLoanHousingCompanyBasic();
        cLoanHousingCompanyBasic.setFkgs(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs());//房开公司
        cLoanHousingCompanyBasic.setFkgszh(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgszh());//房开公司账号
        cLoanHousingCompanyBasic.setSjflb(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getSjflb());//售建房类别   类型不对应
        cLoanHousingCompanyBasic.setZzdj(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzdj());//资质等级
        cLoanHousingCompanyBasic.setDwdz(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getDwdz());//单位地址
        cLoanHousingCompanyBasic.setZcdz(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZcdz());//注册地址
        cLoanHousingCompanyBasic.setZczj(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZczj());//注册资金
        cLoanHousingCompanyBasic.setFrdb(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFrdb());//法人代表
        cLoanHousingCompanyBasic.setFrdbzjlx(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFrdbzjlx());//法人代表证件类型
        cLoanHousingCompanyBasic.setFrdbzjhm(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFrdbzjhm());//法人代表证件号码


        ArrayList<CLoanHousingCompanyBasicExtension> loanHousingCompanyBasicExtentions = new ArrayList<>();
        for (CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension : cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getLoanHousingCompanyViceExtensions()) {
            if (!cLoanHousingCompanyViceExtension.isDeleted()) {
                CLoanHousingCompanyBasicExtension cLoanHousingCompanyBasicExtension = new CLoanHousingCompanyBasicExtension();
                cLoanHousingCompanyBasicExtension.setSFRKHYHKHM(cLoanHousingCompanyViceExtension.getSFRKHYHKHM());//售房人开户银行开户名
                cLoanHousingCompanyBasicExtension.setSFRKHYHMC(cLoanHousingCompanyViceExtension.getSFRKHYHMC());//售房人开户银行名称
                cLoanHousingCompanyBasicExtension.setSFRZHHM(cLoanHousingCompanyViceExtension.getSFRZHHM());//售房人账户号码
                cLoanHousingCompanyBasicExtension.setFkgszb(cLoanHousingCompanyBasic);
                loanHousingCompanyBasicExtentions.add(cLoanHousingCompanyBasicExtension);
            }
        }

        cLoanHousingCompanyBasic.setcLoanHousingCompanyBasicExtensions(loanHousingCompanyBasicExtentions);
        cLoanHousingCompanyBasic.setLxr(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getLxr());//联系人
        cLoanHousingCompanyBasic.setLxdh(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getLxdh());//联系人电话
        cLoanHousingCompanyBasic.setZzjgdm(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm());//组织机构代码
        cLoanHousingCompanyBasic.setBzjzh(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBzjzh());//保证金账户
        cLoanHousingCompanyBasic.setBzjzhkhh(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBzjzhkhh());//保证金账户开户行
        cLoanHousingCompanyBasic.setBzjkhm(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBzjkhm());//保证金开户名
        cLoanHousingCompanyBasic.setBeiZhu(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBeiZhu());//备注
        cLoanHousingCompanyBasic.setBlzl(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBlzl());//办理资料
        cLoanHousingCompanyBasic.setSfbg(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getSfbg());//是否变更
        cLoanHousingCompanyBasic.setYwwd(cLoanHousingBusinessProcess.getYwwd().getId()); //业务网点
        cLoanHousingCompanyBasic.setCzy(cLoanHousingBusinessProcess.getCzy());  //操作员

        cLoanHousingCompanyBasic.setSfqy(true);//是否启用

        cLoanHousingBusinessProcess.setBjsj(new Date());//办结时间


        CLoanHousingCompanyBasic cLoanHousingCompanyBasic1 = instance(icLoanHousingCompanyBasicDAO).entity(cLoanHousingCompanyBasic).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        String fkgszh = cLoanHousingCompanyBasic1.getFkgszh();
        cLoanHousingBusinessProcess.getLoanHousingCompanyVice().setFkgszh(fkgszh);
        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(YWLSH, LoanBusinessType.新建房开.getName(), "办结");
        //设置房开公司账号密码
        RpcAuth rpcAuth = new RpcAuth();
        rpcAuth.setUser_id(cLoanHousingCompanyBasic1.getId());
        rpcAuth.setState(1);
        rpcAuth.setType(4);
        rpcAuth.setUsername(cLoanHousingCompanyBasic1.getFkgszh());
        rpcAuth.setPassword(null);
        accountRpcService.registerAuth(rpcAuth);

    }

    @Override
    public CommonResponses submitHousingCompany(TokenContext tokenContext, List<String> YWLSHS) {
        System.out.println("批量提交");
        ArrayList<Exception> exceptions = new ArrayList<>();
        for (String YWLSH : YWLSHS) {
            if (!StringUtil.notEmpty(YWLSH)) {
                throw new ErrorException(Parameter_MISS);
            }
            CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywlsh", YWLSH);
            }}).getObject(new ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcess == null) {
                throw new ErrorException(Data_MISS, "业务流水号不存在：" + YWLSH);
            }
            if (!cLoanHousingBusinessProcess.getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                    !cLoanHousingBusinessProcess.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");

            checkParam(cLoanHousingBusinessProcess);//参数完整性验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                    UploadFileBusinessType.房开公司申请.getCode(), cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            if (cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getDwdz().length()<10){
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位地址不能小于10个字");
            }
            //验证组织机构唯一性
            List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
                if (cLoanHousingCompanyBasic.getZzjgdm().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码不能重复 " + YWLSH);
                }
                if (cLoanHousingCompanyBasic.getFkgs().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "房开公司名称不能重复 "+YWLSH);
                }
            }
            updateState(this.iStateMachineService, 通过.getEvent(), new TaskEntity(cLoanHousingBusinessProcess.getYwlsh(), cLoanHousingBusinessProcess.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                    tokenContext.getUserInfo().getCZY(), cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBeiZhu(), 贷款_房开申请受理.getSubType(), Loan, tokenContext.getUserInfo().getYWWD()), new StateChangeHandler() {
                @Override
                public void onStateChange(boolean succeed, String next, Exception e) {
                    if (e != null) {
                        throw new ErrorException(e);
                    }
                    if (!StringUtil.notEmpty(next) || e != null) {
                        return;
                    }

                    if (succeed) {
                        //缺少step
                        cLoanHousingBusinessProcess.setStep(next);
                        if (isIntoReview(next, null)) cLoanHousingBusinessProcess.setDdsj(new Date());
                        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }
                        });
                        if (next.equals(LoanBussinessStatus.办结.getName())) doHousingCompany(YWLSH);
                    }
                }
            });
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.新建房开.getName(), "修改");
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs",housingCompanyInfo.getFKGS());
                this.put("cznr", LoanBusinessType.新建房开.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", cLoanHousingBusinessProcess.getYwlsh()));
                    criteria.createAlias("loanHousingCompanyVice", "loanHousingCompanyVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanHousingCompanyVice.zzjgdm", cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm()), Restrictions.eq("loanHousingCompanyVice.fkgs", cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs())));
                    criteria.add(Restrictions.like("step",LoanBussinessStatus.待某人审核.getName()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process, YWLSH);
            }
        }
        return new CommonResponses() {{
            this.setState("success");
        }};
    }

    @Override
    public PageRes getHousingCompany(String FKGS, String FKZH,String YWWD, String pageNo, String pageSize) {
        System.out.println("根据房开公司与房开账号查询房开信息");
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(FKGS)) this.put("fkgs", FKGS);
            if (StringUtil.notEmpty(FKZH)) this.put("fkgszh", FKZH);
            this.put("sfqy", true);
        }}).pageOption(pageRes, pagesize_number, page_number).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if(StringUtil.notEmpty(YWWD)){
                criteria.add(Restrictions.eq("ywwd",YWWD));
                }
            }
        }).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        ArrayList<GetHousingCompanyBasicInfoByCriteria> companyBasicInfoByCriterias = new ArrayList<>();
        for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
            GetHousingCompanyBasicInfoByCriteria getHousingCompanyBasicInfoByCriteria = new GetHousingCompanyBasicInfoByCriteria();
            getHousingCompanyBasicInfoByCriteria.setFKZH(cLoanHousingCompanyBasic.getFkgszh());//房开账号
            getHousingCompanyBasicInfoByCriteria.setFKGS(cLoanHousingCompanyBasic.getFkgs());//房开公司
            getHousingCompanyBasicInfoByCriteria.setSJFLB(cLoanHousingCompanyBasic.getSjflb());//售建房类别
            getHousingCompanyBasicInfoByCriteria.setDWDZ(cLoanHousingCompanyBasic.getDwdz());//单位地址
            getHousingCompanyBasicInfoByCriteria.setYWWD(cAccountNetworkDAO.get(cLoanHousingCompanyBasic.getYwwd()).getMingCheng());
            companyBasicInfoByCriterias.add(getHousingCompanyBasicInfoByCriteria);
        }
        return new PageRes<GetHousingCompanyBasicInfoByCriteria>() {{
            this.setResults(companyBasicInfoByCriterias);
            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }


    @Override
    public CommonResponses reHousingCompanySFQY(TokenContext tokenContext,String FKGSZH, String SFQY) {
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", FKGSZH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingCompanyBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开公司账号不存在");
        }
        if(!cLoanHousingCompanyBasic.getYwwd().equals(tokenContext.getUserInfo().getYWWD())){
            throw new ErrorException(ReturnEnumeration.User_Defined,"没有操作权限");
        }
        List<CLoanEatateProjectBasic> cLoanEatateProjectBasics = instance(loanEatateProjectBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("cLoanHousingCompanyBasic.fkgszh", FKGSZH);
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (SFQY.equals("0")) {
            if (cLoanHousingCompanyBasic.getSfqy()) {
                cLoanHousingCompanyBasic.setSfqy(false);
                for (CLoanEatateProjectBasic cLoanEatateProjectBasic : cLoanEatateProjectBasics) {
                    cLoanEatateProjectBasic.setSfqy(false);
                    loanEatateProjectBasicDAO.update(cLoanEatateProjectBasic);
                }
            } else {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该房开公司已为禁用状态");
            }
        } else if (SFQY.equals("1")) {
            if (!cLoanHousingCompanyBasic.getSfqy()) {
                cLoanHousingCompanyBasic.setSfqy(true);
            } else {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该房开公司已为启用状态");
            }
        }


        instance(icLoanHousingCompanyBasicDAO).entity(cLoanHousingCompanyBasic).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        // RpcAuth rpcAuth = new RpcAuth();
        // if (SFQY.equals("0"))
        //     rpcAuth.setState(0);
        // else
        //     rpcAuth.setState(1);
        // accountRpcService.updateAuth(cLoanHousingCompanyBasic.getId(), rpcAuth);
        return new CommonResponses() {{
            this.setId(cLoanHousingCompanyBasic.getFkgszh());
            this.setState("success");
        }};
    }

    @Override
    public PageRes getCompanyHistory(String ZZJGDM, String pageNo, String pageSize) {
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("loanHousingCompanyVice.zzjgdm", ZZJGDM);
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        ArrayList<GetCommonHistory> getCommonHistories = new ArrayList<>();
        List<CAuditHistory> auditHistoryList = DAOBuilder.instance(this.auditHistoryDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", (Collection) CollectionUtils.flatmap(cLoanHousingBusinessProcess, new CollectionUtils.Transformer<CLoanHousingBusinessProcess, String>() {
                            @Override
                            public String tansform(CLoanHousingBusinessProcess var1) {
                                return var1.getYwlsh();
                            }
                        }));
                    }
                }).pageOption(pageRes, pagesize_number, page_number).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                });
        for (CAuditHistory cAuditHistory : auditHistoryList) {
            GetCommonHistory getCommonHistory = new GetCommonHistory();
            try {
                String ywwdmc = cAccountNetworkDAO.get(cAuditHistory.getYwwd()).getMingCheng();
                getCommonHistory.setYWWD(ywwdmc);
            } catch (Exception e) {
                getCommonHistory.setYWWD("缺失");
            }
            getCommonHistory.setYWLX(cAuditHistory.getYwlx());
            getCommonHistory.setCZNR(cAuditHistory.getCaoZuo());
            getCommonHistory.setCZY(cAuditHistory.getCzy());
            getCommonHistory.setSLSJ(DateUtil.date2Str(cAuditHistory.getShsj() == null ? cAuditHistory.getDdsj() : cAuditHistory.getShsj(), format));
            getCommonHistory.setCZQD(cAuditHistory.getCzqd());
            getCommonHistory.setYWLSH(cAuditHistory.getYwlsh());
            getCommonHistories.add(getCommonHistory);
        }

        return new PageRes<GetCommonHistory>() {{
            this.setResults(getCommonHistories);
            this.setCurrentPage(pageRes.getCurrentPage());
            this.setNextPageNo(pageRes.getNextPageNo());
            this.setPageCount(pageRes.getPageCount());
            this.setPageSize(pageRes.getPageSize());
            this.setTotalCount(pageRes.getTotalCount());
        }};
    }

    @Override
    public HousingIdGet getCompanyInfoByFKZH(String fkzh) {
        System.out.println("根据房开账号查找房开信息");
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", fkzh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException();
            }
        });
        if (cLoanHousingCompanyBasic == null || cLoanHousingCompanyBasic.getcLoanHousingCompanyBasicExtensions() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开账号不存在");
        }
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", cLoanHousingCompanyBasic.getYwwd());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<CLoanHousingCompanyBasicExtension> cLoanHousingCompanyBasicExtensions = cLoanHousingCompanyBasic.getcLoanHousingCompanyBasicExtensions();

        ArrayList<HousingCompanyInfoSale> housingCompanyInfoSale = new ArrayList<>();
        for (CLoanHousingCompanyBasicExtension cLoanHousingCompanyBasicExtension : cLoanHousingCompanyBasicExtensions) {
            if (!cLoanHousingCompanyBasicExtension.isDeleted()) {
                HousingCompanyInfoSale housingCompanyInfoSale1 = new HousingCompanyInfoSale();
                housingCompanyInfoSale1.setSFRKHYHMC(cLoanHousingCompanyBasicExtension.getSFRKHYHMC());//售房人开户银行名称
                housingCompanyInfoSale1.setSFRZHHM(cLoanHousingCompanyBasicExtension.getSFRZHHM()); //售房人账户号码
                housingCompanyInfoSale1.setSFRKHYHKHM(cLoanHousingCompanyBasicExtension.getSFRKHYHKHM()); //售房人开户银行开户名
                housingCompanyInfoSale.add(housingCompanyInfoSale1);
            }
        }
        return new HousingIdGet() {{
            if (cLoanHousingCompanyBasic.getZczj() != null)
                this.setZCZJ(cLoanHousingCompanyBasic.getZczj().toPlainString());//注册资金

            this.setZZDJ(cLoanHousingCompanyBasic.getZzdj());//资质等级（0：一级 1：二级 2：三级 3：四级）


            this.setFKGSZH(cLoanHousingCompanyBasic.getFkgszh());//房开公司账号

            this.setFRDBZJLX(cLoanHousingCompanyBasic.getFrdbzjlx());//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）


            this.setLXR(cLoanHousingCompanyBasic.getLxr());//联系人


            this.setHousingCompanyInfoSales(housingCompanyInfoSale);

            this.setManagerInformation(new HousingIdGetManagerInformation() {{
                this.setCZY(cLoanHousingCompanyBasic.getCzy());
                this.setYWWD(network.getMingCheng());
            }});

            this.setLXDH(cLoanHousingCompanyBasic.getLxdh());//联系电话

            this.setFRDBZJHM(cLoanHousingCompanyBasic.getFrdbzjhm());//法人代表证件号码

            this.setZZJGDM(cLoanHousingCompanyBasic.getZzjgdm());//组织机构代码

            this.setZCDZ(cLoanHousingCompanyBasic.getZcdz());//注册地址

            this.setFRDB(cLoanHousingCompanyBasic.getFrdb());//法人代表

            this.setBeiZhu(cLoanHousingCompanyBasic.getBeiZhu());//备注

            this.setDWDZ(cLoanHousingCompanyBasic.getDwdz());//单位地址

            this.setFKGS(cLoanHousingCompanyBasic.getFkgs());//房开公司

            this.setSJFLB(cLoanHousingCompanyBasic.getSjflb());//售建房类别（0：开发商 1：个人 2：其他）

            this.setBLZL(cLoanHousingCompanyBasic.getBlzl());//办理资料

            this.setBZJZH(cLoanHousingCompanyBasic.getBzjzh());//保证金账户

            this.setBZJKHH(cLoanHousingCompanyBasic.getBzjzhkhh());//保证金账户开户行

            this.setBZJKHM(cLoanHousingCompanyBasic.getBzjkhm());//保证金开户名

        }};
    }

    /*
      参数完整性验证
     */
    private void checkParam(CLoanHousingBusinessProcess cLoanHousingBusinessProcess) {
        if (cLoanHousingBusinessProcess.getLoanHousingCompanyVice() == null || cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getLoanHousingCompanyViceExtensions() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getFkgs())) {
            throw new ErrorException(Data_MISS, "房开公司名称");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getSjflb())) {
            throw new ErrorException(Data_MISS, "售建房类别");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getZzdj())) {
            throw new ErrorException(Data_MISS, "资质等级");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getDwdz())) {
            throw new ErrorException(Data_MISS, "单位地址");
        }
        if (cLoanHousingCompanyVice.getDwdz().length()<10) {
            throw new ErrorException(Data_NOT_MATCH, "单位地址长度不能小于10");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getFrdb())) {
            throw new ErrorException(Data_MISS, "法人代表");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getFrdbzjlx())) {
            throw new ErrorException(Data_MISS, "法人代表证件类型");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getFrdbzjhm())) {
            throw new ErrorException(Data_MISS, "法人代表证件号码");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getLxr())) {
            throw new ErrorException(Data_MISS, "联系人");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getLxdh())) {
            throw new ErrorException(Data_MISS, "联系电话");
        }
        if (!StringUtil.isMobile(cLoanHousingCompanyVice.getLxdh()) && !StringUtil.isPhone(cLoanHousingCompanyVice.getLxdh())) {
            throw new ErrorException(Data_NOT_MATCH, "联系电话格式不正确");
        }
        if (!StringUtil.notEmpty(cLoanHousingCompanyVice.getZzjgdm())) {
            throw new ErrorException(Data_MISS, "组织机构代码");
        }
        if (cLoanHousingCompanyVice.getZzjgdm().length()<6||cLoanHousingCompanyVice.getZzjgdm().length()>20) {
            throw new ErrorException(Data_NOT_MATCH, "组织机构代码必须在6-20位之间");
        }
        for (CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension : cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()) {

            if (cLoanHousingCompanyViceExtension.isDeleted())
                continue;
            if (!StringUtil.notEmpty(cLoanHousingCompanyViceExtension.getSFRKHYHMC())) {
                throw new ErrorException(Data_MISS, "售房人开户银行名称");
            }
            if (!StringUtil.notEmpty(cLoanHousingCompanyViceExtension.getSFRKHYHKHM())) {
                throw new ErrorException(Data_MISS, "售房人开户银行开户名");
            }
            if (!StringUtil.notEmpty(cLoanHousingCompanyViceExtension.getSFRZHHM())) {
                throw new ErrorException(Data_MISS, "售房人账户号码");
            }
            if (!StringUtil.isNum(cLoanHousingCompanyViceExtension.getSFRZHHM())) {
                throw new ErrorException(Data_NOT_MATCH, "售房人账户号码");
            }
            if (cLoanHousingCompanyViceExtension.getSFRZHHM().length()<4) {
                throw new ErrorException(Data_NOT_MATCH, "售房人账户号码长度必须大于4");
            }
        }
    }
}

