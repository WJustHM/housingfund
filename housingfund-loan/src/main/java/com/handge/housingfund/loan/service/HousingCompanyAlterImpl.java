package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.loan.IHousingCompanyAlter;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
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
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;
import static com.handge.housingfund.database.enums.BusinessSubType.贷款_房开变更受理;
import static com.handge.housingfund.database.enums.BusinessType.Loan;
import static com.handge.housingfund.database.enums.Events.通过;
import static com.handge.housingfund.database.utils.DAOBuilder.ErrorHandler;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;
import static com.handge.housingfund.loan.utils.StateMachineUtils.StateChangeHandler;
import static com.handge.housingfund.loan.utils.StateMachineUtils.updateState;

/**
 * Created by 向超 on 2017/8/24.
 */
@Component
public class HousingCompanyAlterImpl implements IHousingCompanyAlter {
    @Autowired
    private ICLoanHousingCompanyBasicExtentionDAO icLoanHousingCompanyBasicExtentionDAO;
    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;
    @Autowired
    private ICLoanHousingCompanyBasicDAO icLoanHousingCompanyBasicDAO;
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

    String format = "yyyy-MM-dd HH:mm";


    @Override
    public CommonResponses addHousingCompanyAlter(TokenContext tokenContext, String CZLX, String FKZH, HousingCompanyPost housingCompanyInfo) {
        System.out.println("添加房开变更信息");
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.房开公司变更.getCode(), housingCompanyInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        CLoanHousingCompanyBasic cLoanHousingCompanyBasics1 = instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", FKZH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingCompanyBasics1 == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开账号");
        }
        if (!cLoanHousingCompanyBasics1.getYwwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "业务不在该网点受理范围内");
        }


        CLoanHousingCompanyVice cLoanHousingCompanyVice = new CLoanHousingCompanyVice();//构造房开公司副表
        cLoanHousingCompanyVice.setFkgs(housingCompanyInfo.getFKGS());//房开公司
        cLoanHousingCompanyVice.setFkgszh(FKZH); //房开账号
        cLoanHousingCompanyVice.setSjflb(housingCompanyInfo.getSJFLB());//售建房类别
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
        cLoanHousingCompanyVice.setSfbg(true);//是否变更


        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = new CLoanHousingBusinessProcess();//构造业务表
        cLoanHousingBusinessProcess.setLoanHousingCompanyVice(cLoanHousingCompanyVice);
        cLoanHousingCompanyVice.setGrywmx(cLoanHousingBusinessProcess);
        cLoanHousingBusinessProcess.setCznr(LoanBusinessType.房开变更.getCode());
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
        List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.ne("fkgszh", cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgszh()));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
            if (cLoanHousingCompanyBasic.getZzjgdm().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码重复");
            }
            if (cLoanHousingCompanyBasic.getFkgs().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "房开公司名称重复");
            }
        }
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", Events.通过.getEvent());
            this.put("1", Events.提交.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess1.getYwlsh(), cLoanHousingBusinessProcess1.getStep(),new HashSet<String>(tokenContext.getRoleList()) ,
                cLoanHousingBusinessProcess1.getCzy(), cLoanHousingCompanyVice.getBeiZhu(), BusinessSubType.贷款_房开变更受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, cLoanHousingBusinessProcess1.getYwwd().getId()), new StateMachineUtils.StateChangeHandler() {
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
                        doHousingCompanyAlter(cLoanHousingBusinessProcess1.getYwlsh());
                }
            }
        });
        if (CZLX.equals("1")) {
            checkParam(cLoanHousingBusinessProcess1);
        }
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs",housingCompanyInfo.getFKGS());
                this.put("loanHousingCompanyVice.fkgszh", FKZH);
                this.put("cznr", LoanBusinessType.房开变更.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("step", LoanBussinessStatus.办结.getName()));
                    criteria.add(Restrictions.ne("ywlsh", cLoanHousingBusinessProcess1.getYwlsh()));
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


        saveAuditHistory.saveNormalBusiness(cLoanHousingBusinessProcess1.getYwlsh(), tokenContext, LoanBusinessType.房开变更.getName(), "新建");

        return new CommonResponses() {{
            this.setId(cLoanHousingBusinessProcess1.getYwlsh());
            this.setState("success");
        }};
    }

    @Override
    public CommonResponses reHousingCompanyAlterInfo(TokenContext tokenContext, String YWLSH, String CZLX, HousingCompanyPut housingCompanyInfo) {
        System.out.println("修改房开变更信息");

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
            throw new ErrorException(ReturnEnumeration.User_Defined, "该业务(" + YWLSH + ")不是由您受理的，不能修改");
        }
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.房开公司变更.getCode(), housingCompanyInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();

        DAOBuilder.instance(cloanHousingCompanyViceExtensionDAO).entities(cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        cLoanHousingCompanyVice.setFkgs(housingCompanyInfo.getFKGS());//房开公司
        // cLoanHousingCompanyVice.setFkgszh(housingCompanyInfo.getFKGSZH());// 房开公司账号
        cLoanHousingCompanyVice.setSjflb(String.valueOf(housingCompanyInfo.getSJFLB()));//售建房类别   类型不对应
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
        cLoanHousingBusinessProcess.setYwwd(network); //业务网点
        cLoanHousingBusinessProcess.setLoanHousingCompanyVice(cLoanHousingCompanyVice);

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 = instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if(CZLX.equals("1")){
            //验证组织机构唯一性
            List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("fkgszh", cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgszh()));
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
                if (cLoanHousingCompanyBasic.getZzjgdm().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码重复");
                }
                if (cLoanHousingCompanyBasic.getFkgs().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "房开公司名称重复");
                }
            }
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", Events.保存.getEvent());
            this.put("1", Events.通过.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess1.getYwlsh(), cLoanHousingBusinessProcess1.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                tokenContext.getUserInfo().getCZY(), cLoanHousingCompanyVice.getBeiZhu(), BusinessSubType.贷款_房开变更受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, tokenContext.getUserInfo().getYWWD()), new StateMachineUtils.StateChangeHandler() {
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
                    if (next.equals(LoanBussinessStatus.办结.getName())) doHousingCompanyAlter(YWLSH);
                }
            }
        });
        if (CZLX.equals("1")) {
            checkParam(cLoanHousingBusinessProcess1);
            saveAuditHistory.saveNormalBusiness(cLoanHousingBusinessProcess1.getYwlsh(), tokenContext, LoanBusinessType.房开变更.getName(), "修改");
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs",housingCompanyInfo.getFKGS());
                this.put("loanHousingCompanyVice.fkgszh", cLoanHousingCompanyVice.getFkgszh());
                this.put("cznr", LoanBusinessType.房开变更.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("step", LoanBussinessStatus.办结.getName()));
                    criteria.add(Restrictions.ne("ywlsh", cLoanHousingBusinessProcess1.getYwlsh()));
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

    @Override
    public HousingIdGet showHousingCompanyAlterInfo(String YWLSH) {
        System.out.println("房开变更详情");

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
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = null;

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
        ArrayList<String> arrayList = showDiff(cLoanHousingBusinessProcess);
        return new HousingIdGet() {{
            this.setDELTA(arrayList);

            this.setZCZJ(cLoanHousingCompanyVice.getZczj() == null ? "0" : cLoanHousingCompanyVice.getZczj().toPlainString());//注册资金

            this.setZZDJ(cLoanHousingCompanyVice.getZzdj());//资质等级（0：一级 1：二级 2：三级 3：四级）

            this.setYWLSH(cLoanHousingBusinessProcess.getYwlsh());//业务流水号


            this.setFKGSZH(cLoanHousingCompanyVice.getFkgszh());//房开公司账号

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

    private ArrayList<String> showDiff(CLoanHousingBusinessProcess cLoanHousingBusinessProcess){
        ArrayList<String> arrayList = new ArrayList<String>();
        String fkgszh = cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgszh();
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", fkgszh);
        }}).getObject(new ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        CLoanHousingCompanyVice cLoanHousingCompanyVice = cLoanHousingBusinessProcess.getLoanHousingCompanyVice();
        //List<CLoanHousingCompanyViceExtension> loanHousingCompanyViceExtensions = cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions();
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getFkgs(),cLoanHousingCompanyBasic.getFkgs())) arrayList.add("FKGS");//房开公司
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getSjflb(),cLoanHousingCompanyBasic.getSjflb())) arrayList.add("SJFLB");//售建房类别（0：开发商 1：个人 2：其他）
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getZzdj(),cLoanHousingCompanyBasic.getZzdj())) arrayList.add("ZZDJ");//资质等级（0：一级 1：二级 2：三级 3：四级）
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getDwdz(),cLoanHousingCompanyBasic.getDwdz())) arrayList.add("DWDZ");//单位地址
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getZcdz(),cLoanHousingCompanyBasic.getZcdz())) arrayList.add("ZCDZ");//注册地址
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getZczj(),cLoanHousingCompanyBasic.getZczj())) arrayList.add("ZCZJ");//注册资金
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getFrdb(),cLoanHousingCompanyBasic.getFrdb())) arrayList.add("FRDB");//法人代表
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getFrdbzjlx(),cLoanHousingCompanyBasic.getFrdbzjlx())) arrayList.add("FRDBZJLX");//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getFrdbzjhm(),cLoanHousingCompanyBasic.getFrdbzjhm())) arrayList.add("FRDBZJHM"); //法人代表证件号码
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getLxr(),cLoanHousingCompanyBasic.getLxr())) arrayList.add("LXR");//联系人
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getLxdh(),cLoanHousingCompanyBasic.getLxdh())) arrayList.add("LXDH");//联系电话
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getZzjgdm(),cLoanHousingCompanyBasic.getZzjgdm())) arrayList.add("ZZJGDM");//组织机构代码
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getBzjzhkhh(),cLoanHousingCompanyBasic.getBzjzhkhh())) arrayList.add("BZJZHKHH");//保证金账户开户行
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getBzjzh(),cLoanHousingCompanyBasic.getBzjzh())) arrayList.add("BZJZH");//保证金账户
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getBzjkhm(),cLoanHousingCompanyBasic.getBzjkhm())) arrayList.add("BZJKHM");//保证金开户名
        if(!StringUtil.stringEquals(cLoanHousingCompanyVice.getBeiZhu(),cLoanHousingCompanyBasic.getBeiZhu())) arrayList.add("BeiZhu");//备注

        int i = 0;
        for(CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension : cLoanHousingCompanyVice.getLoanHousingCompanyViceExtensions()){
            if(cLoanHousingCompanyViceExtension.isDeleted()){
                i++;
            }
        }
        int f = 0;
        for(CLoanHousingCompanyBasicExtension cLoanHousingCompanyBasicExtension : cLoanHousingCompanyBasic.getcLoanHousingCompanyBasicExtensions()){
            if(!cLoanHousingCompanyBasicExtension.isDeleted()){
                f++;
            }
        }
        if(i!=f) arrayList.add("housingCompanyInfoSales");//售房人信息
        return arrayList;
    }

    @Override
    public PageRes getHousingCompanyAlterInfoAccept(TokenContext tokenContext, String FKZH, String FKGS, String ZHUANGTAI, String KSSJ,String JSSJ ,String pageNo, String pageSize) {
        System.out.println("查询受理房开变更信息");
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
            if (StringUtil.notEmpty(FKZH)) this.put("loanHousingCompanyVice.fkgszh", FKZH);
            if (StringUtil.notEmpty(FKGS)) this.put("loanHousingCompanyVice.fkgs", FKGS);
            if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                this.put("step", ZHUANGTAI);
            this.put("cznr", LoanBusinessType.房开变更.getCode());
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
                        this.setFKZH(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgszh());//房开公司账号
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
    public PageResNew getHousingCompanyAlterInfoAcceptNew(TokenContext tokenContext, String FKZH, String FKGS, String ZHUANGTAI, String KSSJ, String JSSJ , String marker, String pageSize, String action) {
        System.out.println("查询受理房开变更信息");
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(FKZH)) this.put("loanHousingCompanyVice.fkgszh", FKZH);
            if (StringUtil.notEmpty(FKGS)) this.put("loanHousingCompanyVice.fkgs", FKGS);
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                    criteria.add(Restrictions.eq("step", ZHUANGTAI));
                criteria.add(Restrictions.eq("cznr", LoanBusinessType.房开变更.getCode()));
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
                        this.setFKZH(cLoanHousingBusinessProcess1.getLoanHousingCompanyVice().getFkgszh());//房开公司账号
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

    @Override
    public CommonResponses receiptHousingCompanyAlter(String YWLSH) {
        System.out.println("房开变更回执单");

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
        ApplyHousingCompanyReceiptUnitInfo ReceiptUnitInfo = new ApplyHousingCompanyReceiptUnitInfo();

        CompanyReceipt.setYWLSH(YWLSH);//业务无流水号
        CompanyReceipt.setCZY(cLoanHousingBusinessProcess.getCzy());//操作员
        // this.setTZRQ(new Date());//填制日期
        // this.setYZM();//验证码
        if (cLoanHousingCompanyVice.getZczj() != null)
            ReceiptUnitInfo.setZCZJ(cLoanHousingCompanyVice.getZczj().toPlainString());//注册资金


        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getZzdj())) {
            SingleDictionaryDetail ZZDJ = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getZzdj(), "LoanQualificationLevel");
            ReceiptUnitInfo.setZZDJ(ZZDJ != null ? ZZDJ.getName() : "");//资质等级（0：一级 1：二级 2：三级 3：四级）
        }
        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getFrdbzjlx())) {
            SingleDictionaryDetail FRDBZJLX = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getFrdbzjlx(), "PersonalCertificate");
            ReceiptUnitInfo.setFRDBZJLX(FRDBZJLX != null ? FRDBZJLX.getName() : "");//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）
        }

        ReceiptUnitInfo.setLXR(cLoanHousingCompanyVice.getLxr());//联系人

        ReceiptUnitInfo.setHousingCompanyInfoSales(housingCompanyInfoSale);

        ReceiptUnitInfo.setLXDH(cLoanHousingCompanyVice.getLxdh());//联系电话

        ReceiptUnitInfo.setFRDBZJHM(cLoanHousingCompanyVice.getFrdbzjhm());//法人代表证件号码

        ReceiptUnitInfo.setZZJGDM(cLoanHousingCompanyVice.getZzjgdm());//组织机构代码

        ReceiptUnitInfo.setZCDZ(cLoanHousingCompanyVice.getZcdz());//注册地址

        ReceiptUnitInfo.setFRDB(cLoanHousingCompanyVice.getFrdb());//法人代表

        ReceiptUnitInfo.setBeiZhu(cLoanHousingCompanyVice.getBeiZhu());//备注

        ReceiptUnitInfo.setDWDZ(cLoanHousingCompanyVice.getDwdz());//单位地址

        if (!StringUtil.isEmpty(cLoanHousingCompanyVice.getSjflb())) {
            SingleDictionaryDetail sjflb = iDictionaryService.getSingleDetail(cLoanHousingCompanyVice.getSjflb(), "LoanHouseType");
            ReceiptUnitInfo.setSJFLB(sjflb != null ? sjflb.getName() : "");//售建房类别（0：开发商 1：个人 2：其他）
        }

        ReceiptUnitInfo.setFKGS(cLoanHousingCompanyVice.getFkgs());//房开公司

        ReceiptUnitInfo.setFKZH(cLoanHousingCompanyVice.getFkgszh());//房开账号

        ReceiptUnitInfo.setBZJZH(cLoanHousingCompanyVice.getBzjzh());//保证金账户

        ReceiptUnitInfo.setBZJZHKHH(cLoanHousingCompanyVice.getBzjzhkhh());//保证金账户开户行

        ReceiptUnitInfo.setBZJKHM(cLoanHousingCompanyVice.getBzjkhm());//保证金开户名

        ReceiptUnitInfo.setYWWD(cLoanHousingBusinessProcess.getYwwd().getMingCheng());//业务网点

        CompanyReceipt.setUnitInfo(ReceiptUnitInfo);
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

        String id = pdfService.getApplyHousingCompanyReceipt(CompanyReceipt, LoanBusinessType.房开变更.getCode());
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public void doHousingCompanyAlter(String YWLSH) {
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
        String fkzh = cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgszh();
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", fkzh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingCompanyBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开公司为空");
        }
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

        //删除历史信息
        DAOBuilder.instance(icLoanHousingCompanyBasicExtentionDAO).entities(cLoanHousingCompanyBasic.getcLoanHousingCompanyBasicExtensions()).delete(new ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

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

//        cLoanHousingCompanyBasic.setSfqy(true);//是否启用

        cLoanHousingBusinessProcess.setBjsj(new Date());//办结时间

        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        instance(icLoanHousingCompanyBasicDAO).entity(cLoanHousingCompanyBasic).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(YWLSH, LoanBusinessType.房开变更.getName(), "办结");
    }

    @Override
    public CommonResponses submitHousingCompanyAlter(TokenContext tokenContext, List<String> YWLSHS) {
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
                throw new ErrorException(Data_MISS, "业务流水号为" + YWLSH);
            }
            if (!cLoanHousingBusinessProcess.getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                    !cLoanHousingBusinessProcess.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            checkParam(cLoanHousingBusinessProcess);
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                    UploadFileBusinessType.房开公司变更.getCode(), cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            if (cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getDwdz().length()<10){
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "楼盘地址不能小于10个字");
            }
            //验证组织机构唯一性
            List<CLoanHousingCompanyBasic> cLoanHousingCompanyBasics = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("fkgszh", cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgszh()));
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CLoanHousingCompanyBasic cLoanHousingCompanyBasic : cLoanHousingCompanyBasics) {
                if (cLoanHousingCompanyBasic.getZzjgdm().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getZzjgdm())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "组织机构代码不能重复 ");
                }
                if (cLoanHousingCompanyBasic.getFkgs().equals(cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgs())) {
                    throw new ErrorException(ReturnEnumeration.User_Defined, "单位名称不能重复 ");
                }
            }
            updateState(this.iStateMachineService, 通过.getEvent(), new TaskEntity(cLoanHousingBusinessProcess.getYwlsh(), cLoanHousingBusinessProcess.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                    tokenContext.getUserInfo().getCZY(), cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getBeiZhu(), 贷款_房开变更受理.getSubType(), Loan, tokenContext.getUserInfo().getYWWD()), new StateChangeHandler() {
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
                        if (next.equals(LoanBussinessStatus.办结.getName())) doHousingCompanyAlter(YWLSH);
                    }
                }
            });
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.房开变更.getName(), "修改");
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanHousingCompanyVice.zzjgdm", housingCompanyInfo.getZZJGDM());
                // this.put("loanHousingCompanyVice.fkgs",housingCompanyInfo.getFKGS());
                this.put("loanHousingCompanyVice.fkgszh", cLoanHousingBusinessProcess.getLoanHousingCompanyVice().getFkgszh());
                this.put("cznr", LoanBusinessType.房开变更.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("step", LoanBussinessStatus.办结.getName()));
                    criteria.add(Restrictions.ne("ywlsh", YWLSH));
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
            this.setState("success");
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
            throw new ErrorException(Data_MISS, "联系电话格式不正确");
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
        }
    }


}
