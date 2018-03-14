package com.handge.housingfund.review.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.enumeration.BusinessDetailsModule;
import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.review.IBaseReview;
import com.handge.housingfund.common.service.review.model.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.NormalJsonUtils;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.review.util.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Liujuhao on 2017/7/25.
 */

@SuppressWarnings({"unchecked", "Convert2Lambda", "Duplicates"})
@Component
public abstract class CommonReview implements IBaseReview {

    @Autowired
    @Resource(name = "stateMachineServiceV2")
    protected com.handge.housingfund.statemachineV2.IStateMachineService iStateMachineService;

    //全平台审核历史表
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    protected IStCollectionPersonalBusinessDetailsDAO iStCollectionPersonalBusinessDetailsDAO;

    @Autowired
    protected IStCollectionUnitBusinessDetailsDAO iStCollectionUnitBusinessDetailsDAO;

    @Autowired
    protected ICLoanHousingBusinessProcessDAO cLoanHousingBusinessProcessDAO;

    @Autowired
    protected ICFinanceBusinessProcessDAO icFinanceBusinessProcessDAO;

    @Autowired
    protected ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;

    @Autowired
    protected ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;

    @Autowired
    protected ICAccountNetworkDAO icAccountNetworkDAO;

    //状态机DAO
    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    public static String format = "yyyy-MM-dd HH:mm";

    public abstract PageRes getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type);

    public abstract PageRes getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type);

    public abstract PageResNew getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type);

    public abstract PageResNew getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type);

    public abstract void doReview(TokenContext tokenContext, String YWLSH, AuditInfo auditInfo, boolean pass, String module, String type);

    public abstract void doBulks(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type);

    public List getEntitiesByModule(boolean reviewed, String module, HashMap<String, Object> filters, IBaseDAO.CriteriaExtension extension, PageRes pageRes) {

        IBaseDAO dao;
        String orderby;

        if (BusinessDetailsModule.Collection_person.getCode().equals(module)) {
            dao = iStCollectionPersonalBusinessDetailsDAO;
            if (reviewed)
                orderby = "cCollectionPersonalBusinessDetailsExtension.shsj";
            else
                orderby = "cCollectionPersonalBusinessDetailsExtension.ddsj";

        } else if (BusinessDetailsModule.Collection_unit.getCode().equals(module)) {
            dao = iStCollectionUnitBusinessDetailsDAO;
            if (reviewed)
                orderby = "cCollectionUnitBusinessDetailsExtension.shsj";
            else
                orderby = "cCollectionUnitBusinessDetailsExtension.ddsj";

        } else if (BusinessDetailsModule.Loan_person.getCode().equals(module)) {
            dao = cLoanHousingBusinessProcessDAO;
            if (reviewed)
                orderby = "shsj";
            else
                orderby = "ddsj";
        } else if (BusinessDetailsModule.Finance_common.getCode().equals(module)) {
            dao = icFinanceBusinessProcessDAO;
            if (reviewed)
                orderby = "shsj";
            else
                orderby = "ddsj";
        } else if (BusinessDetailsModule.Finance_currentToPeriodic.getCode().equals(module)) {
            dao = icFinanceActived2FixedDAO;
            if (reviewed)
                orderby = "cFinanceManageFinanceExtension.shsj";
            else
                orderby = "cFinanceManageFinanceExtension.ddsj";
        } else if (BusinessDetailsModule.Finance_periodicWithdraw.getCode().equals(module)) {
            dao = icFinanceFixedDrawDAO;
            if (reviewed)
                orderby = "cFinanceManageFinanceExtension.shsj";
            else
                orderby = "cFinanceManageFinanceExtension.ddsj";
        } else {
            throw new ErrorException("无效的业务模块");
        }

        List list = DAOBuilder.instance(dao)
                .searchFilter(filters)
                .extension(extension)
                .orderOption(orderby, Order.DESC)
                .pageOption(pageRes, pageRes.getPageSize(), pageRes.getCurrentPage())
                .searchOption(SearchOption.FUZZY)
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        return list;
    }

    public List getEntitiesByModule(boolean reviewed, String module, HashMap<String, Object> filters, IBaseDAO.CriteriaExtension extension, int pageSize, String marker, String action) {

        IBaseDAO dao;
        String orderby;

        if (BusinessDetailsModule.Collection_person.getCode().equals(module)) {
            dao = iStCollectionPersonalBusinessDetailsDAO;
            if (reviewed)
                orderby = "cCollectionPersonalBusinessDetailsExtension.shsj";
            else
                orderby = "cCollectionPersonalBusinessDetailsExtension.ddsj";

        } else if (BusinessDetailsModule.Collection_unit.getCode().equals(module)) {
            dao = iStCollectionUnitBusinessDetailsDAO;
            if (reviewed)
                orderby = "cCollectionUnitBusinessDetailsExtension.shsj";
            else
                orderby = "cCollectionUnitBusinessDetailsExtension.ddsj";

        } else if (BusinessDetailsModule.Loan_person.getCode().equals(module)) {
            dao = cLoanHousingBusinessProcessDAO;
            if (reviewed)
                orderby = "shsj";
            else
                orderby = "ddsj";
        } else if (BusinessDetailsModule.Finance_common.getCode().equals(module)) {
            dao = icFinanceBusinessProcessDAO;
            if (reviewed)
                orderby = "shsj";
            else
                orderby = "ddsj";
        } else if (BusinessDetailsModule.Finance_currentToPeriodic.getCode().equals(module)) {
            dao = icFinanceActived2FixedDAO;
            if (reviewed)
                orderby = "cFinanceManageFinanceExtension.shsj";
            else
                orderby = "cFinanceManageFinanceExtension.ddsj";
        } else if (BusinessDetailsModule.Finance_periodicWithdraw.getCode().equals(module)) {
            dao = icFinanceFixedDrawDAO;
            if (reviewed)
                orderby = "cFinanceManageFinanceExtension.shsj";
            else
                orderby = "cFinanceManageFinanceExtension.ddsj";
        } else {
            throw new ErrorException("无效的业务模块");
        }


        List list = DAOBuilder.instance(dao)
                .searchFilter(filters)
                .extension(extension)
                .orderOption(orderby, Order.DESC)
                .pageOption(marker, action, pageSize)
                .searchOption(SearchOption.FUZZY)
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        return list;
    }

    public Common getEntityByModule(String module, String YWLSH) {

        IBaseDAO dao;
        if (BusinessDetailsModule.Collection_person.getCode().equals(module)) {
            dao = iStCollectionPersonalBusinessDetailsDAO;
        } else if (BusinessDetailsModule.Collection_unit.getCode().equals(module)) {
            dao = iStCollectionUnitBusinessDetailsDAO;
        } else if (BusinessDetailsModule.Loan_person.getCode().equals(module)) {
            dao = cLoanHousingBusinessProcessDAO;
        } else if (BusinessDetailsModule.Finance_common.getCode().equals(module)) {
            dao = icFinanceBusinessProcessDAO;
        } else if (BusinessDetailsModule.Finance_periodicWithdraw.getCode().equals(module)) {
            dao = icFinanceFixedDrawDAO;
        } else if (BusinessDetailsModule.Finance_currentToPeriodic.getCode().equals(module)) {
            dao = icFinanceActived2FixedDAO;
        } else {
            throw new ErrorException("无效的业务模块");
        }

        Common entity = DAOBuilder.instance(dao)
                .searchFilter(new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                }})
                .getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        if (entity == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
        }

        entity = (Common) dao.refresh(entity);

        return entity;
    }

    public ReviewInfo generateReviewInfo(TokenContext tokenContext, AuditInfo auditInfo, Common entity) {

        Date DDSJ;
        String YWLX;

        if (entity instanceof StCollectionPersonalBusinessDetails) {
            DDSJ = ((StCollectionPersonalBusinessDetails) entity).getExtension().getDdsj();
            YWLX = CollectionBusinessType.getNameByCode(((StCollectionPersonalBusinessDetails) entity).getExtension().getCzmc());
        } else if (entity instanceof StCollectionUnitBusinessDetails) {
            DDSJ = ((StCollectionUnitBusinessDetails) entity).getExtension().getDdsj();
            YWLX = CollectionBusinessType.getNameByCode(((StCollectionUnitBusinessDetails) entity).getExtension().getCzmc());
        } else if (entity instanceof CLoanHousingBusinessProcess) {
            DDSJ = ((CLoanHousingBusinessProcess) entity).getDdsj();
            YWLX = LoanBusinessType.getNameByCode(((CLoanHousingBusinessProcess) entity).getCznr());
        } else if (entity instanceof CFinanceBusinessProcess) {
            DDSJ = ((CFinanceBusinessProcess) entity).getDdsj();
            YWLX = FinanceBusinessType.getNameByCode(((CFinanceBusinessProcess) entity).getCznr());
        } else if (entity instanceof CFinanceActived2Fixed) {
            DDSJ = ((CFinanceActived2Fixed) entity).getcFinanceManageFinanceExtension().getDdsj();
            YWLX = FinanceBusinessType.getNameByCode(((CFinanceActived2Fixed) entity).getcFinanceManageFinanceExtension().getYwlx());
        } else if (entity instanceof CFinanceFixedDraw) {
            DDSJ = ((CFinanceFixedDraw) entity).getcFinanceManageFinanceExtension().getDdsj();
            YWLX = FinanceBusinessType.getNameByCode(((CFinanceFixedDraw) entity).getcFinanceManageFinanceExtension().getYwlx());
        } else {
            throw new ErrorException("数据库中没有匹配的业务表");
        }

        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.setDDSJ(DDSJ);
        Calendar t1 = Calendar.getInstance();
        t1.add(Calendar.SECOND, -5); //当前秒数-5
        reviewInfo.setSHSJ(t1.getTime());
        reviewInfo.setYWLX(YWLX);
        reviewInfo.setBeiZhu(auditInfo.getNote());
        reviewInfo.setSHJG(auditInfo.getEvent());
        reviewInfo.setCaoZuo(tokenContext.getRoleList().get(0) + "审核");
        reviewInfo.setCZQD(tokenContext.getChannel());
        reviewInfo.setCZY(tokenContext.getUserInfo().getCZY());
        reviewInfo.setQZSJ(null);
        reviewInfo.setSHYBH(tokenContext.getUserId());
        reviewInfo.setYWWD(tokenContext.getUserInfo().getYWWD());
        reviewInfo.setYYYJ(auditInfo.getYYYJ());
        reviewInfo.setZhiWu(tokenContext.getRoleList().get(0));

        return reviewInfo;
    }

    public MultiReviewConfig generateConfig(String userId, String json, boolean sfts/*是否特审*/, boolean pass) {

        MultiReviewConfig config = NormalJsonUtils.toObj4Review(json);

        config.getLSSHYBH().add(userId);
        if (pass) {
            config.setSHJB(sfts ? "S" : "C");
            config.setSCSHY(config.getDQSHY());
        } else {
//            config.setLSSHYBH(null);/*目前审核不通过时，不清空历史审核员记录*/
            config.setSHJB(null);
            config.setSCSHY(null);
        }
        config.setDQSHY(null);
        config.setDQXM(null);

        return config;
    }

    @Override
    public PageRes getReviewInfo(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type) {

        PageRes pageRes;

        try {

            pageRes = getReviewList(tokenContext, conditions, module, type);

        } catch (Exception e) {

            throw new ErrorException(e);
        }

        return pageRes;
    }

    @Override
    public PageRes getReviewedInfo(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type) {

        PageRes pageRes;

        try {

            pageRes = getReviewedList(tokenContext, conditions, module, type);

        } catch (Exception e) {

            throw new ErrorException(e);
        }

        return pageRes;
    }

    @Override
    public ReviewRes postBusinessReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type) {

        for (String YWLSH : YWLSHs) {

            if (YWLSH == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号");
            }
        }

        try {

            doBulks(tokenContext, YWLSHs, auditInfo, pass, module, type);

        } catch (ErrorException e) {

            throw e;
        }

        ReviewRes reviewRes = new ReviewRes();
        reviewRes.setStatus("success");
        return reviewRes;
    }

    @Override
    public ReviewCheckRes checkIsReviewing(TokenContext tokenContext, ArrayList<String> YWLSHs, String module, String type) {

        for (String YWLSH : YWLSHs) {

            boolean isReviewing;

            String DQXM;

            if (module.equals(BusinessDetailsModule.Collection_person.getCode())) {

                StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.iStCollectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {

                        this.put("ywlsh", YWLSH);

                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                String wd = collectionPersonalBusinessDetails.getExtension().getYwwd().getId();

                // 加锁前权限验证
                {
                    String source = collectionPersonalBusinessDetails.getExtension().getStep();
                    String subType = new HashMap<String, String>() {
                        {
                            this.put(CollectionBusinessType.部分提取.getCode(), BusinessSubType.归集_提取.getSubType());
                            this.put(CollectionBusinessType.销户提取.getCode(), BusinessSubType.归集_提取.getSubType());
                            this.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_个人账户设立.getSubType());
                            this.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_个人账户信息变更.getSubType());
                            this.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_个人账户封存.getSubType());
                            this.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_个人账户启封.getSubType());
                            this.put(CollectionBusinessType.冻结.getCode(), BusinessSubType.归集_冻结个人账户.getSubType());
                            this.put(CollectionBusinessType.解冻.getCode(), BusinessSubType.归集_解冻个人账户.getSubType());
                            this.put(CollectionBusinessType.内部转移.getCode(), BusinessSubType.归集_个人账户内部转移.getSubType());
                            this.put(CollectionBusinessType.合并.getCode(), BusinessSubType.归集_合并个人账户.getSubType());
                            this.put(CollectionBusinessType.外部转出.getCode(), BusinessSubType.归集_转出个人接续.getSubType());
                            this.put(CollectionBusinessType.外部转入.getCode(), BusinessSubType.归集_转入个人接续.getSubType());
                        }
                    }.get(collectionPersonalBusinessDetails.getExtension().getCzmc());
                    boolean access = icStateMachineConfigurationDAO.checkIsPermission(wd, tokenContext.getRoleList().get(0), subType, source);
                    if (!access) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务可能已被其他人审核或撤回，请返回或刷新页面");
                    }
                }

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(collectionPersonalBusinessDetails.getExtension().getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                collectionPersonalBusinessDetails.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
                this.iStCollectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);

            } else if (module.equals(BusinessDetailsModule.Collection_unit.getCode())) {

                StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder.instance(this.iStCollectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                String wd = collectionUnitBusinessDetails.getExtension().getYwwd().getId();

                // 加锁前权限验证
                {
                    String source = collectionUnitBusinessDetails.getExtension().getStep();
                    String subType = new HashMap<String, String>() {
                        {
                            this.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_单位开户.getSubType());
                            this.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_单位账户启封.getSubType());
                            this.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_单位账户封存.getSubType());
                            this.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_单位账户变更.getSubType());
                            this.put(CollectionBusinessType.销户.getCode(), BusinessSubType.归集_单位账户销户.getSubType());
                            this.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());
                            this.put(CollectionBusinessType.汇缴.getCode(), BusinessSubType.归集_汇缴记录.getSubType());
                            this.put(CollectionBusinessType.补缴.getCode(), BusinessSubType.归集_补缴记录.getSubType());
                            this.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());
                            this.put(CollectionBusinessType.基数调整.getCode(), BusinessSubType.归集_个人基数调整.getSubType());
                            this.put(CollectionBusinessType.缓缴处理.getCode(), BusinessSubType.归集_单位缓缴申请.getSubType());
                            this.put(CollectionBusinessType.错缴更正.getCode(), BusinessSubType.归集_错缴更正.getSubType());
                        }
                    }.get(collectionUnitBusinessDetails.getExtension().getCzmc());
                    boolean access = icStateMachineConfigurationDAO.checkIsPermission(wd, tokenContext.getRoleList().get(0), subType, source);
                    if (!access) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务可能已被其他人审核或撤回，请返回或刷新页面");
                    }
                }

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(collectionUnitBusinessDetails.getExtension().getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                collectionUnitBusinessDetails.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
                this.iStCollectionPersonalBusinessDetailsDAO.update(collectionUnitBusinessDetails);

            } else if (module.equals(BusinessDetailsModule.Loan_person.getCode())) {

                CLoanHousingBusinessProcess cLoanHousingBusinessProcess = DAOBuilder.instance(this.cLoanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                String wd = cLoanHousingBusinessProcess.getYwwd().getId();
                // 加锁前权限验证
                {
                    String source = cLoanHousingBusinessProcess.getStep();
                    String subType = new HashMap<String, String>() {
                        {
                            this.put(LoanBusinessType.新建房开.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
                            this.put(LoanBusinessType.房开变更.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());
                            this.put(LoanBusinessType.新建楼盘.getCode(), BusinessSubType.贷款_楼盘申请受理.getSubType());
                            this.put(LoanBusinessType.楼盘变更.getCode(), BusinessSubType.贷款_楼盘变更受理.getSubType());
                            this.put(LoanBusinessType.贷款发放.getCode(), BusinessSubType.贷款_个人贷款申请.getSubType());
                            this.put(LoanBusinessType.逾期还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                            this.put(LoanBusinessType.公积金提取还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                            this.put(LoanBusinessType.提前还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                            this.put(LoanBusinessType.结清.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                            this.put(LoanBusinessType.合同变更.getCode(), BusinessSubType.贷款_合同变更申请.getSubType());
                        }
                    }.get(cLoanHousingBusinessProcess.getCznr());
                    boolean access = icStateMachineConfigurationDAO.checkIsPermission(wd, tokenContext.getRoleList().get(0), subType, source);
                    if (!access) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务可能已被其他人审核或撤回，请返回或刷新页面");
                    }
                }

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(cLoanHousingBusinessProcess.getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                cLoanHousingBusinessProcess.setShybh(NormalJsonUtils.toJson4Review(config));
                this.cLoanHousingBusinessProcessDAO.update(cLoanHousingBusinessProcess);

            } else if (module.equals(BusinessDetailsModule.Finance_common.getCode())) {

                CFinanceBusinessProcess financeBusinessProcess = DAOBuilder.instance(this.icFinanceBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                String wd = financeBusinessProcess.getYwwd();

                // 加锁前权限验证
                {
                    String source = financeBusinessProcess.getStep();
                    String subType = ReviewSubModule.财务_日常.getName() + financeBusinessProcess.getcFinanceDailyBusinessVice().getZjywlx();
                    boolean access = icStateMachineConfigurationDAO.checkIsPermission(wd, tokenContext.getRoleList().get(0), subType, source);
                    if (!access) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务可能已被其他人审核或撤回，请返回或刷新页面");
                    }
                }

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(financeBusinessProcess.getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                financeBusinessProcess.setShybh(NormalJsonUtils.toJson4Review(config));
                this.icFinanceBusinessProcessDAO.update(financeBusinessProcess);

            } else if (module.equals(BusinessDetailsModule.Finance_currentToPeriodic.getCode())) {

                CFinanceActived2Fixed financeActived2Fixed = DAOBuilder.instance(this.icFinanceActived2FixedDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(financeActived2Fixed.getcFinanceManageFinanceExtension().getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                financeActived2Fixed.getcFinanceManageFinanceExtension().setShybh(NormalJsonUtils.toJson4Review(config));
                this.icFinanceActived2FixedDAO.update(financeActived2Fixed);

            } else if (module.equals(BusinessDetailsModule.Finance_periodicWithdraw.getCode())) {

                CFinanceFixedDraw financeFixedDraw = DAOBuilder.instance(this.icFinanceFixedDrawDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(financeFixedDraw.getcFinanceManageFinanceExtension().getShybh());

                DQXM = config.getDQXM();
                isReviewing = rebuildConfig(config, tokenContext);

                financeFixedDraw.getcFinanceManageFinanceExtension().setShybh(NormalJsonUtils.toJson4Review(config));
                this.icFinanceFixedDrawDAO.update(financeFixedDraw);

            } else {

                throw new ErrorException("fail", "未知业务模块:" + YWLSH);
            }
            if (isReviewing) {

                throw new ErrorException("fail", YWLSH + "正在被审核，审核员为" + DQXM/*当前审核员*/);
            }
        }

        ReviewCheckRes reviewCheckRes = new ReviewCheckRes();
        reviewCheckRes.setCode("success");
        return reviewCheckRes;
    }

    public boolean rebuildConfig(MultiReviewConfig config, TokenContext tokenContext) {
        if (config != null) {
            if (StringUtil.notEmpty(config.getDQSHY())) {
                return !config.getDQSHY().equals(tokenContext.getUserId());
            } else {
                config.setDQSHY(tokenContext.getUserId());
                config.setDQXM(tokenContext.getUserInfo().getCZY());
            }
            return false;
        } else {
            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "解析审核配置出错");
        }
    }

    /**
     * 特审操作，要求该API被调用时必须被子类所重写，否则无效
     *
     * @param YWLSHs
     * @param auditInfo
     * @return
     */
    @Override
    public ReviewRes postSpecialReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, String module, String type) {

        List<String> pchList = new ArrayList();

        for (String YWLSH : YWLSHs) {

            if (ReviewSubModule.归集_提取.getCode().equals(type)) {
                List<String> TQ_YWLSHs = iStCollectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(YWLSH);
                String currentPch = iStCollectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getPch();
                if (pchList.contains(currentPch)) {
                    continue;
                }
                try {
                    wholeSpecialReview(tokenContext, TQ_YWLSHs, auditInfo, module, type);
                } catch (ErrorException e) {
                    throw e;
                }
                pchList.add(currentPch);

            } else {

                try {
                    singleSpecialReivew(tokenContext, YWLSH, auditInfo, module, type);

                } catch (ErrorException e) {
                    e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                    throw e;
                }
            }
        }

        ReviewRes reviewRes = new ReviewRes();
        reviewRes.setStatus("success");
        return reviewRes;
    }

    public void singleSpecialReivew(TokenContext tokenContext, String YWLSH, AuditInfo auditInfo, String module, String type) {

        if (StringUtil.isEmpty(YWLSH))
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");

        if (BusinessDetailsModule.Loan_person.getCode().equals(module)) {

            Object entity = getEntityByModule(module, YWLSH);

            CLoanHousingBusinessProcess obj = (CLoanHousingBusinessProcess) entity;

            if (obj == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            String czmc = obj.getCznr();
            String step = obj.getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = obj.getYwwd().getId();

            if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                if (!Arrays.asList(
                        LoanBusinessType.贷款发放.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Loan);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(LoanBusinessType.贷款发放.getCode(), BusinessSubType.贷款_个人贷款申请.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.setStep(next);
                    }
                });
            } else if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                if (!Arrays.asList(
                        LoanBusinessType.房开变更.getCode(),
                        LoanBusinessType.新建房开.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Loan);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(LoanBusinessType.房开变更.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());
                                this.put(LoanBusinessType.新建房开.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.setStep(next);
                    }
                });
            } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                if (!Arrays.asList(
                        LoanBusinessType.新建楼盘.getCode(),
                        LoanBusinessType.楼盘变更.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Loan);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(LoanBusinessType.新建楼盘.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());
                                this.put(LoanBusinessType.楼盘变更.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.setStep(next);
                    }
                });
            } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                if (!Arrays.asList(
                        LoanBusinessType.合同变更.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Loan);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(LoanBusinessType.合同变更.getCode(), BusinessSubType.贷款_合同变更申请.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.setStep(next);
                    }
                });
            }

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);
            reviewInfo.setCaoZuo("提交特审");
            reviewInfo.setSHJG("03");//用03来表示“发起特审”

            if (StringUtil.isIntoReview(obj.getStep(), null)) {
                obj.setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getShybh(), true, true);
            obj.setShybh(NormalJsonUtils.toJson4Review(config));
            obj.setShsj(new Date());

            cLoanHousingBusinessProcessDAO.update(obj);
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (BusinessDetailsModule.Collection_person.getCode().equals(module)) {

            StCollectionPersonalBusinessDetails obj = DAOBuilder.instance(iStCollectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("ywlsh", YWLSH);

            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            if (obj == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            String czmc = obj.getExtension().getCzmc();
            String step = obj.getExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = obj.getExtension().getYwwd().getId();

            if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                if (!icStateMachineConfigurationDAO.getSpecialReviewSource(BusinessSubType.归集_提取.getSubType()).contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH);
                }

                if (!Arrays.asList(
                        CollectionBusinessType.部分提取.getCode(),
                        CollectionBusinessType.销户提取.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                String khwd = obj.getUnit().getExtension().getKhwd();

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.WithDrawl);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.部分提取.getCode(), BusinessSubType.归集_提取.getSubType());
                                this.put(CollectionBusinessType.销户提取.getCode(), BusinessSubType.归集_提取.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.getExtension().setStep(next);
                    }
                });

            } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

                if (!icStateMachineConfigurationDAO.getSpecialReviewSource(BusinessSubType.归集_转出个人接续.getSubType()).contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH);
                }

                if (!Arrays.asList(
                        CollectionBusinessType.外部转出.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Collection);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.外部转出.getCode(), BusinessSubType.归集_转出个人接续.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.getExtension().setStep(next);
                    }
                });

            } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

                if (!icStateMachineConfigurationDAO.getSpecialReviewSource(BusinessSubType.归集_转入个人接续.getSubType()).contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH);
                }

                if (!Arrays.asList(
                        CollectionBusinessType.外部转入.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Collection);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.外部转入.getCode(), BusinessSubType.归集_转入个人接续.getSubType());
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.getExtension().setStep(next);
                    }
                });

            } else if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                if (!Arrays.asList(
                        CollectionBusinessType.开户.getCode(),
                        CollectionBusinessType.启封.getCode(),
                        CollectionBusinessType.封存.getCode(),
                        CollectionBusinessType.内部转移.getCode(),
                        CollectionBusinessType.冻结.getCode(),
                        CollectionBusinessType.解冻.getCode(),
                        CollectionBusinessType.变更.getCode(),
                        CollectionBusinessType.合并.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                String khwd = obj.getUnit().getExtension().getKhwd();

                    if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                        if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                        }
                    }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Collection);
                        this.setOperator(czy);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_个人账户设立.getSubType());
                                this.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_个人账户信息变更.getSubType());
                                this.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_个人账户封存.getSubType());
                                this.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_个人账户启封.getSubType());
                                this.put(CollectionBusinessType.冻结.getCode(), BusinessSubType.归集_冻结个人账户.getSubType());
                                this.put(CollectionBusinessType.解冻.getCode(), BusinessSubType.归集_解冻个人账户.getSubType());
                                this.put(CollectionBusinessType.内部转移.getCode(), BusinessSubType.归集_个人账户内部转移.getSubType());
                                this.put(CollectionBusinessType.合并.getCode(), BusinessSubType.归集_合并个人账户.getSubType());
                            }
                        }.get(czmc));

                            this.setOperator(czy);
                            this.setWorkstation(ywwd);
                        }
                    }, new StateMachineUtils.StateChangeHandler() {
                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {
                            if (e != null) {
                                throw new ErrorException(e);
                            }

                        if (!succeed || next == null) return;

                        obj.getExtension().setStep(next);

                    }
                });
            }
            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);
            reviewInfo.setCaoZuo("提交特审");
            reviewInfo.setSHJG("03");//用03来表示“发起特审”

            if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                obj.getExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), true, true);
            obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getExtension().setShsj(new Date());

            iStCollectionPersonalBusinessDetailsDAO.update(obj);
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (BusinessDetailsModule.Collection_unit.getCode().equals(module)) {

            StCollectionUnitBusinessDetails obj = DAOBuilder.instance(iStCollectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("ywlsh", YWLSH);

            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            if (obj == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            String czmc = obj.getExtension().getCzmc();
            String step = obj.getExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = obj.getExtension().getYwwd().getId();

            if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                if (!Arrays.asList(
                        CollectionBusinessType.开户.getCode(),
                        CollectionBusinessType.启封.getCode(),
                        CollectionBusinessType.封存.getCode(),
                        CollectionBusinessType.变更.getCode(),
                        CollectionBusinessType.比例调整.getCode(),
                        CollectionBusinessType.销户.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                    if (!CollectionBusinessType.开户.getCode().equals(czmc)) {
                        if (!obj.getUnit().getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
                            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的归属网点和您所在网点不匹配");
                        }
                    } else {
                        if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                        }
                    }
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Collection);
                        this.setOperator(czy);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_单位开户.getSubType());
                                this.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_单位账户启封.getSubType());
                                this.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_单位账户封存.getSubType());
                                this.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_单位账户变更.getSubType());
                                this.put(CollectionBusinessType.销户.getCode(), BusinessSubType.归集_单位账户销户.getSubType());
                                this.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());
                            }
                        }.get(czmc));

                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.getExtension().setStep(next);
                    }
                });
            } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                if (czmc == null || !Arrays.asList(
                        CollectionBusinessType.汇缴.getCode(),
                        CollectionBusinessType.补缴.getCode(),
                        CollectionBusinessType.年终结息.getCode(),
                        CollectionBusinessType.基数调整.getCode(),
                        CollectionBusinessType.缓缴处理.getCode(),
                        CollectionBusinessType.错缴更正.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);

                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                    if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.汇缴.getCode(), BusinessSubType.归集_汇缴记录.getSubType());
                                this.put(CollectionBusinessType.补缴.getCode(), BusinessSubType.归集_补缴记录.getSubType());
                                this.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());
                                this.put(CollectionBusinessType.基数调整.getCode(), BusinessSubType.归集_个人基数调整.getSubType());
                                this.put(CollectionBusinessType.缓缴处理.getCode(), BusinessSubType.归集_单位缓缴申请.getSubType());
                                this.put(CollectionBusinessType.错缴更正.getCode(), BusinessSubType.归集_错缴更正.getSubType());
                            }
                        }.get(czmc));

                        this.setType(BusinessType.Collection);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {

                        if (e != null) {
                            e.printStackTrace();
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;
                    }
                });
            }
            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);
            reviewInfo.setCaoZuo("提交特审");
            reviewInfo.setSHJG("03");//用03来表示“发起特审”

            if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                obj.getExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), true, true);
            obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getExtension().setShsj(new Date());

            iStCollectionUnitBusinessDetailsDAO.update(obj);
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (BusinessDetailsModule.Finance_common.getCode().equals(module)) {

            CFinanceBusinessProcess obj = DAOBuilder.instance(icFinanceBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("ywlsh", YWLSH);

            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            if (obj == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            String czmc = obj.getCznr();
            String step = obj.getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = obj.getYwwd();
            String subType = ReviewSubModule.财务_日常.getName() + obj.getcFinanceDailyBusinessVice().getZjywlx();

            if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                if (!Arrays.asList(
                        FinanceBusinessType.日常财务处理.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                ArrayList<String> zjywlxs = icStateMachineConfigurationDAO.getSubTypesByAuth(null, tokenContext.getRoleList().get(0), BusinessType.Finance, ReviewSubModule.财务_日常.getName());

                if (czmc == null || !zjywlxs.contains(subType)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Finance);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(FinanceBusinessType.日常财务处理.getCode(), subType);
                            }
                        }.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        obj.setStep(next);
                    }
                });
            }
            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);
            reviewInfo.setCaoZuo("提交特审");
            reviewInfo.setSHJG("03");//用03来表示“发起特审”

            if (StringUtil.isIntoReview(obj.getStep(), null)) {
                obj.setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getShybh(), true, true);
            obj.setShybh(NormalJsonUtils.toJson4Review(config));
            obj.setShsj(new Date());

            icFinanceBusinessProcessDAO.update(obj);
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else {

            throw new ErrorException("没有匹配的实现接口，请联系管理员");
        }

//        ReviewRes reviewRes = new ReviewRes();
//        reviewRes.setStatus("success");
//        return reviewRes;
    }

    public void wholeSpecialReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, String module, String type) {

        for (String YWLSH : YWLSHs) {

            if (StringUtil.isEmpty(YWLSH))
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");

            if (BusinessDetailsModule.Collection_person.getCode().equals(module)) {

                StCollectionPersonalBusinessDetails obj = DAOBuilder.instance(iStCollectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

                    this.put("ywlsh", YWLSH);

                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {

                        throw new ErrorException(e);
                    }
                });

                if (obj == null) {

                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
                }

                String czmc = obj.getExtension().getCzmc();
                String step = obj.getExtension().getStep();
                String czy = tokenContext.getUserInfo().getCZY();
                String ywwd = obj.getExtension().getYwwd().getId();

                if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    if (!icStateMachineConfigurationDAO.getSpecialReviewSource(BusinessSubType.归集_提取.getSubType()).contains(step)) {
                        throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH);
                    }

                    if (!Arrays.asList(
                            CollectionBusinessType.部分提取.getCode(),
                            CollectionBusinessType.销户提取.getCode()).contains(czmc)) {

                        throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                    }

                    String khwd = obj.getUnit().getExtension().getKhwd();

                    StateMachineUtils.updateState(this.iStateMachineService, Events.特审.getEvent(), new TaskEntity() {
                        {
                            this.setStatus(step);
                            this.setTaskId(YWLSH);
                            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                            this.setNote(auditInfo.getNote());
                            this.setType(BusinessType.WithDrawl);
                            this.setOperator(czy);
                            this.setWorkstation(ywwd);
                            this.setSubtype(new HashMap<String, String>() {
                                {
                                    this.put(CollectionBusinessType.部分提取.getCode(), BusinessSubType.归集_提取.getSubType());
                                    this.put(CollectionBusinessType.销户提取.getCode(), BusinessSubType.归集_提取.getSubType());
                                }
                            }.get(czmc));
                        }
                    }, new StateMachineUtils.StateChangeHandler() {
                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {
                            if (e != null) {
                                throw new ErrorException(e);
                            }

                            if (!succeed || next == null) return;

                            obj.getExtension().setStep(next);
                        }
                    });
                }

                ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);
                reviewInfo.setCaoZuo("提交特审");
                reviewInfo.setSHJG("03");//用03来表示“发起特审”

                if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                    obj.getExtension().setDdsj(new Date());
                }

                MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), true, true);
                obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
                obj.getExtension().setShsj(new Date());

                iStCollectionPersonalBusinessDetailsDAO.update(obj);
                iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);
            }
        }
    }


    public ArrayList<String> getYWLX(String ywwd, String role, BusinessType type, String prefix) {

        ArrayList<String> subtypes = icStateMachineConfigurationDAO.getSubTypesByAuth(ywwd, role, type, prefix);

        ArrayList<String> ywlxs = new ArrayList<>();

        for (String subtype : subtypes) {
            if (BusinessSubType.归集_提取.getSubType().equals(subtype)) {
                ywlxs.add(CollectionBusinessType.部分提取.getCode());
                ywlxs.add(CollectionBusinessType.销户提取.getCode());
            } else if (BusinessSubType.贷款_个人还款申请.getSubType().equals(subtype)) {
                ywlxs.add(LoanBusinessType.提前还款.getCode());
                ywlxs.add(LoanBusinessType.结清.getCode());
                ywlxs.add(LoanBusinessType.逾期还款.getCode());
                ywlxs.add(LoanBusinessType.公积金提取还款.getCode());
            } else {
                ywlxs.add(BusinessSubType.getTypeCodeBySubType(subtype));
            }
        }
        return ywlxs;
    }

    @Override
    public PageResNew getReviewInfo(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew pageRes;

        try {

            pageRes = getReviewList(tokenContext, conditions, action, module, type);

        } catch (Exception e) {

            throw new ErrorException(e);
        }

        return pageRes;
    }

    @Override
    public PageResNew getReviewedInfo(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew pageRes;

        try {

            pageRes = getReviewedList(tokenContext, conditions, action, module, type);

        } catch (Exception e) {

            throw new ErrorException(e);
        }

        return pageRes;
    }
}
