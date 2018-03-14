package com.handge.housingfund.review.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.loan.*;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.model.Review;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.review.util.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Liujuhao on 2017/9/25.
 */
@SuppressWarnings("Duplicates")
@Component(value = "review.loanl")
public class LoanReview extends CommonReview {

    //贷款业务流水表副表
    @Autowired
    protected ICLoanHousingBusinessProcessDAO cLoanHousingBusinessProcessDAO;

    //状态机DAO
    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    //合同变更
    @Autowired
    private ILoanContractService iLoanContractService;

    //楼盘
    @Autowired
    private IEstateProject iEstateProject;
    @Autowired
    private IEstateProjectAlter estateProjectAlter;

    //房开
    @Autowired
    private IHousingCompany iHousingCompany;
    @Autowired
    private IHousingCompanyAlter housingCompanyAlter;

    //贷款发放
    @Autowired
    private IPdfService iPdfService;
    @Autowired
    private ILoanReviewService iLoanReviewService;

    //还款申请
    @Autowired
    private IRepaymentService iRepaymentService;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    @Autowired
    private ICAccountEmployeeDAO accountEmployeeDAO;

    /**
     * 贷款-未审核列表
     *
     * @param tokenContext
     * @param conditions
     * @param module
     * @param type
     * @return
     */
    @Override
    public PageRes getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type) {

        PageRes res = new PageRes<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        HashMap<String, String> ywlx_subtype = new HashMap<>();

        IBaseDAO.CriteriaExtension extension = null;

        int pageNo = StringUtil.isEmpty(conditions.get("pageNo")) ? 1 : Integer.parseInt(conditions.get("pageNo"));

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String FKGS = conditions.get("FKGS");
        String YWLX = conditions.get("YWLX");
        String JKRXM = conditions.get("JKRXM");
        String LPMC = conditions.get("LPMC");
        String HKLX = conditions.get("HKLX");
        String JKHTBH = conditions.get("JKHTBH");
        String ZJHM = conditions.get("ZJHM");

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

            results = new ArrayList<HousingCompanyReviewResHousingCompanyReview>();

            ywlx_subtype.put(LoanBusinessType.新建房开.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
            ywlx_subtype.put(LoanBusinessType.房开变更.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_房开.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_房开.getName());

            if (ywlxs.isEmpty())
                return res;

            if (StringUtil.notEmpty(YWLX) && !LoanBusinessType.所有.getCode().equals(YWLX)) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cznr", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            filter.put("step", sources);
            if (StringUtil.notEmpty(FKGS)) {
                filter.put("loanHousingCompanyVice.fkgs", FKGS);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

            results = new ArrayList<EstateProjectReviewRes>();

            ywlx_subtype.put(LoanBusinessType.新建楼盘.getCode(), BusinessSubType.贷款_楼盘申请受理.getSubType());
            ywlx_subtype.put(LoanBusinessType.楼盘变更.getCode(), BusinessSubType.贷款_楼盘变更受理.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_楼盘.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_楼盘.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
            if (StringUtil.notEmpty(YWLX) && !LoanBusinessType.所有.getCode().equals(YWLX)) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cznr", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            if (StringUtil.notEmpty(LPMC)) {
                filter.put("loanEatateProjectVice.lpmc", LPMC);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

            results = new ArrayList<LoanReviewListResponse>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_放款.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_个人贷款申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_放款.getName());

            if (ywlxs.isEmpty())
                return res;

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("loanHousingPersonInformationVice.jkrzjhm", ZJHM);
            }
            filter.put("step", sources);
            filter.put("cznr", ywlxs);
            //放款业务的审核，只有受理网点人员可见
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }

                }
            };

        } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

            results = new ArrayList<HousingRepamentApplyRangeGetRes>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_还款.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_个人还款申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_还款.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanApplyRepaymentVice.jkrxm", JKRXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("loanApplyRepaymentVice.jkrzjhm", ZJHM);
            }
            if (StringUtil.notEmpty(HKLX) && !LoanBusinessType.所有.getCode().equals(HKLX)) {
                if (ywlxs.contains(HKLX)) {
                    filter.put("cznr", HKLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

            results = new ArrayList<ContractAlterReviewRes>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_合同.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_合同变更申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_合同.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
/*            if (StringUtil.notEmpty(JKHTBH)) {
                filter.put("loanContract.jkhtbh", JKHTBH);
            }*/
            filter.put("cznr", ywlxs);

            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(JKHTBH)) {
                        criteria.createAlias("loanContract", "loanContract");
                        criteria.add(Restrictions.eq("loanContract.jkhtbh", JKHTBH));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, res);

        for (Object entity : list) {

            if (entity instanceof CLoanHousingBusinessProcess) {

                CLoanHousingBusinessProcess details = (CLoanHousingBusinessProcess) entity;

                if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ywlx_subtype.get(details.getCznr()), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    HousingCompanyReviewResHousingCompanyReview obj = new HousingCompanyReviewResHousingCompanyReview();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingCompanyVice() != null) {
                        obj.setFKGS(details.getLoanHousingCompanyVice().getFkgs());//房开公司
                        obj.setSJFLB(details.getLoanHousingCompanyVice().getSjflb());//售建房类别
                    }
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setYWLX(details.getCznr());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);

                } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ywlx_subtype.get(details.getCznr()), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    EstateProjectReviewRes obj = new EstateProjectReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanEatateProjectVice() != null) {
                        obj.setLPDZ(details.getLoanEatateProjectVice().getLpdz());//楼盘地址
                        obj.setLPMC(details.getLoanEatateProjectVice().getLpmc());//楼盘名称
                    }
                    obj.setZhuangTai(details.getStep());//状态
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWLX(details.getCznr());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);

                } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                    LoanReviewListResponse obj = new LoanReviewListResponse();

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_个人贷款申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());

                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        obj.setJKRZJHM(details.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                    }
                    obj.setStatus(details.getStep());
                    obj.setYWLSH(details.getYwlsh()/*业务流水号*/);
                    if (details.getLoanFundsVice() != null) {
                        obj.setHTDKJE(details.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format)/*到达时间*/);
                    obj.setYWWD(details.getYwwd().getMingCheng()/*业务网点*/);
                    obj.setCZY(details.getCzy()/*操作员*/);
                    obj.setSFTS(isSpecial ? "1" : "0");    //1能0不能
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                        if (shyxx.getSCSHY() != null) {
                            obj.setSCSHY(accountEmployeeDAO.get(shyxx.getSCSHY()) == null ? null : accountEmployeeDAO.get(shyxx.getSCSHY()).getXingMing());
                        }
                    }
                    obj.setSPBWJ(details.getLoanHousingPersonInformationVice() == null ? null : details.getLoanHousingPersonInformationVice().getSpbwj());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_个人还款申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());

                    HousingRepamentApplyRangeGetRes obj = new HousingRepamentApplyRangeGetRes();

                    obj.setYWLSH(details.getYwlsh());
                    if (details.getLoanApplyRepaymentVice() != null) {
                        obj.setDKZH(details.getLoanApplyRepaymentVice().getDkzh());
                        obj.setJKRXM(details.getLoanApplyRepaymentVice().getJkrxm());
                        obj.setJKRZJHM(details.getLoanApplyRepaymentVice().getJkrzjhm());
                        obj.setSQHKLX(details.getLoanApplyRepaymentVice().getHklx());
                        obj.setSQHKJE(details.getLoanApplyRepaymentVice().getHkje() + "");
                    }
                    obj.setCZY(details.getCzy());
                    obj.setYWWD(details.getYwwd().getMingCheng());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);

                } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_合同变更申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    ContractAlterReviewRes obj = new ContractAlterReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKHTBH(details.getLoanHousingPersonInformationVice().getJkhtbh());//借款合同编号
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /**
     * 贷款-已审核列表
     *
     * @param tokenContext
     * @param conditions
     * @param module
     * @param type
     * @return
     */
    @Override
    public PageRes getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type) {

        PageRes res = new PageRes<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;

        int pageNo = StringUtil.isEmpty(conditions.get("pageNo")) ? 1 : Integer.parseInt(conditions.get("pageNo"));

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String FKGS = conditions.get("FKGS");
        String ZhuangTai = conditions.get("ZhuangTai");
        String YWLX = conditions.get("YWLX");
        String LPMC = conditions.get("LPMC");
        String JKRXM = conditions.get("JKRXM");
        String HKLX = conditions.get("HKLX");
        String JKHTBH = conditions.get("JKHTBH");
        String ZJHM = conditions.get("ZJHM");

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

            results = new ArrayList<HousingCompanyReviewResHousingCompanyReview>();

            if (StringUtil.notEmpty(FKGS)) {
                filter.put("loanHousingCompanyVice.fkgs", FKGS);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWLX)) {
                        criteria.add(Restrictions.eq("cznr", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.新建房开.getCode(),
                                LoanBusinessType.房开变更.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.办结.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

            results = new ArrayList<EstateProjectReviewRes>();

            if (StringUtil.notEmpty(LPMC)) {
                filter.put("loanEatateProjectVice.lpmc", LPMC);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWLX)) {
                        criteria.add(Restrictions.eq("cznr", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.新建楼盘.getCode(),
                                LoanBusinessType.楼盘变更.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.办结.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

            results = new ArrayList<LoanReviewListResponse>();

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("loanHousingPersonInformationVice.jkrzjhm", ZJHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.add(Restrictions.in("cznr", Arrays.asList(LoanBusinessType.贷款发放.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.待签合同.getName(),
                                        LoanBussinessStatus.待入账.getName(),
                                        LoanBussinessStatus.已入账.getName(),
                                        LoanBussinessStatus.入账失败.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

            results = new ArrayList<HousingRepamentApplyRangeGetRes>();

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanApplyRepaymentVice.jkrxm", JKRXM);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("loanApplyRepaymentVice.jkrzjhm", ZJHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(HKLX) && !LoanBusinessType.所有.getCode().equals(HKLX)) {
                        criteria.add(Restrictions.eq("cznr", HKLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.逾期还款.getCode(),
                                LoanBusinessType.提前还款.getCode(),
                                LoanBusinessType.公积金提取还款.getCode(),
                                LoanBusinessType.结清.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.待入账.getName(),
                                        LoanBussinessStatus.扣款已发送.getName(),
                                        LoanBussinessStatus.入账失败.getName(),
                                        LoanBussinessStatus.已入账.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

            results = new ArrayList<ContractAlterReviewRes>();

            if (StringUtil.notEmpty(JKHTBH)) {
                filter.put("loanContract.jkhtbh", JKHTBH);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.add(Restrictions.in("cznr", Arrays.asList(LoanBusinessType.合同变更.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.办结.getName(),
                                        LoanBussinessStatus.审核不通过.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, res);

        for (Object entity : list) {

            if (entity instanceof CLoanHousingBusinessProcess) {

                CLoanHousingBusinessProcess details = (CLoanHousingBusinessProcess) entity;

                if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                    HousingCompanyReviewResHousingCompanyReview obj = new HousingCompanyReviewResHousingCompanyReview();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingCompanyVice() != null) {
                        obj.setFKGS(details.getLoanHousingCompanyVice().getFkgs());//房开公司
                        obj.setSJFLB(details.getLoanHousingCompanyVice().getSjflb());//售建房类别
                    }
                    obj.setCZY(details.getCzy());//操作员
                    obj.setZhuangtai(details.getStep());
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setYWLX(details.getCznr());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//（审核）受理时间
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                    EstateProjectReviewRes obj = new EstateProjectReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanEatateProjectVice() != null) {
                        obj.setLPMC(details.getLoanEatateProjectVice().getLpmc());//楼盘名称
                        obj.setLPDZ(details.getLoanEatateProjectVice().getLpdz());//楼盘地址
                    }
                    obj.setZhuangTai(details.getStep());//状态
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//（审核）受理时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWLX(details.getCznr());
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                    LoanReviewListResponse obj = new LoanReviewListResponse();

                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        obj.setJKRZJHM(details.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                    }
                    obj.setStatus(details.getStep()/*状态*/);
                    obj.setYWLSH(details.getYwlsh()/*业务流水号*/);
                    if (details.getLoanFundsVice() != null) {
                        obj.setHTDKJE(details.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                    }
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format)/*（审核）受理时间*/);
                    obj.setYWWD(details.getYwwd().getMingCheng()/*业务网点*/);
                    obj.setCZY(details.getCzy()/*操作员*/);
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

                    HousingRepamentApplyRangeGetRes obj = new HousingRepamentApplyRangeGetRes();

                    obj.setYWLSH(details.getYwlsh());
                    if (details.getLoanApplyRepaymentVice() != null) {
                        obj.setDKZH(details.getLoanApplyRepaymentVice().getDkzh());
                        obj.setJKRXM(details.getLoanApplyRepaymentVice().getJkrxm());
                        obj.setJKRZJHM(details.getLoanApplyRepaymentVice().getJkrzjhm());
                        obj.setSQHKLX(details.getLoanApplyRepaymentVice().getHklx());
                        obj.setSQHKJE(details.getLoanApplyRepaymentVice().getHkje() + "");
                    }
                    obj.setStatus(details.getStep());
                    obj.setCZY(details.getCzy());
                    obj.setYWWD(details.getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                    ContractAlterReviewRes obj = new ContractAlterReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKHTBH(details.getLoanHousingPersonInformationVice().getJkhtbh());//借款合同编号
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm());
                    }
                    obj.setZhuangTai(details.getStep());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//受理时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /**
     * 贷款--审核操作
     *
     * @param tokenContext
     * @param YWLSH
     * @param auditInfo
     * @param pass
     * @param module
     * @param type
     */
    @Override
    public void doReview(TokenContext tokenContext, String YWLSH, AuditInfo auditInfo, boolean pass, String module, String type) {

        Object entity = getEntityByModule(module, YWLSH);

        List<String> nexts = new ArrayList<>();

        if (entity instanceof CLoanHousingBusinessProcess) {

            CLoanHousingBusinessProcess obj = (CLoanHousingBusinessProcess) entity;

            String czmc = obj.getCznr();
            String step = obj.getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            MultiReviewConfig check = NormalJsonUtils.toObj4Review(obj.getShybh());

            if (check != null && check.getDQSHY() != null)
                if (!check.getDQSHY().equals(tokenContext.getUserId()))
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已被审核员 " + check.getDQXM() + "锁定");

            if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Loan,
                        ReviewSubModule.贷款_房开.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");

                }

                if (!Arrays.asList(
                        LoanBusinessType.新建房开.getCode(),
                        LoanBusinessType.房开变更.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的归属网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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
                                this.put(LoanBusinessType.新建房开.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
                                this.put(LoanBusinessType.房开变更.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());
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

                        try {
                            if (pass) {
                                if (czmc.equals(LoanBusinessType.新建房开.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.新建房开);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iHousingCompany.doHousingCompany(YWLSH);

                                }
                                if (czmc.equals(LoanBusinessType.房开变更.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.房开变更);
                                    if (!StringUtil.isIntoReview(next, null))
                                        housingCompanyAlter.doHousingCompanyAlter(YWLSH);

                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Loan,
                        ReviewSubModule.贷款_楼盘.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (!Arrays.asList(
                        LoanBusinessType.新建楼盘.getCode(),
                        LoanBusinessType.楼盘变更.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的归属网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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
                                this.put(LoanBusinessType.新建楼盘.getCode(), BusinessSubType.贷款_楼盘申请受理.getSubType());
                                this.put(LoanBusinessType.楼盘变更.getCode(), BusinessSubType.贷款_楼盘变更受理.getSubType());
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

                        try {
                            if (pass) {
                                if (czmc.equals(LoanBusinessType.新建楼盘.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.新建楼盘);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iEstateProject.doEstateProject(YWLSH);

                                }
                                if (czmc.equals(LoanBusinessType.楼盘变更.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.楼盘变更);
                                    if (!StringUtil.isIntoReview(next, null))
                                        estateProjectAlter.doEstateProjectAlter(YWLSH);

                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Loan,
                        ReviewSubModule.贷款_放款.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (!Arrays.asList(
                        LoanBusinessType.贷款发放.getCode()).contains(czmc)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                //放款业务的审批要验证“受理网点”
                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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

                        try {
                            if (pass) {
                            /*多级审核的最后一步时才调用“办结”操作*/

                                if (czmc.equals(LoanBusinessType.贷款发放.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.贷款发放);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iLoanReviewService.postLoanReviewReason(tokenContext, YWLSH);
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        generateReviewTable(YWLSH, tokenContext, auditInfo);
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Loan,
                        ReviewSubModule.贷款_还款.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的归属网点和您所在网点不匹配");
                    }

                if (!Arrays.asList(
                        LoanBusinessType.提前还款.getCode(),
                        LoanBusinessType.逾期还款.getCode(),
                        LoanBusinessType.公积金提取还款.getCode(),
                        LoanBusinessType.结清.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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
                                this.put(LoanBusinessType.逾期还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                                this.put(LoanBusinessType.公积金提取还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                                this.put(LoanBusinessType.提前还款.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
                                this.put(LoanBusinessType.结清.getCode(), BusinessSubType.贷款_个人还款申请.getSubType());
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

                        try {
                            if (pass) {
                                if (czmc.equals(LoanBusinessType.逾期还款.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.逾期还款);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iRepaymentService.doAction(YWLSH);

                                }
                                if (czmc.equals(LoanBusinessType.提前还款.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.提前还款);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iRepaymentService.doAction(YWLSH);

                                }
                                if (czmc.equals(LoanBusinessType.结清.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.结清);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iRepaymentService.doAction(YWLSH);

                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Loan,
                        ReviewSubModule.贷款_合同.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (!Arrays.asList(
                        LoanBusinessType.合同变更.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的归属网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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

                        try {
                            if (pass) {
                                if (czmc.equals(LoanBusinessType.合同变更.getCode())) {
                                    next = TranslateUtils.step2Status(next, LoanBusinessType.合同变更);
                                    if (!StringUtil.isIntoReview(next, null))
                                        iLoanContractService.doAction(tokenContext, YWLSH);
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });
            }

            obj = (CLoanHousingBusinessProcess) getEntityByModule(module, YWLSH);
            if (nexts.size() != 1) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getStep().equals(step))
                obj.setStep(nexts.get(0));

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getStep(), null)) {
                obj.setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getShybh(), false, pass);
            obj.setShybh(NormalJsonUtils.toJson4Review(config));
            obj.setShsj(new Date());

            cLoanHousingBusinessProcessDAO.update(obj);

            //贷款申请的历史记录在审批表调用时写入，此处跳过
            if (ReviewSubModule.贷款_放款.getCode().equals(type))
                return;
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);
        }
    }

    /**
     * 贷款--批量操作
     *
     * @param tokenContext
     * @param YWLSHs
     * @param auditInfo
     * @param pass
     * @param module
     * @param type
     */
    @Override
    public void doBulks(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type) {

        for (String YWLSH : YWLSHs) {

            try {
                doReview(tokenContext, YWLSH, auditInfo, pass, module, type);

            } catch (ErrorException e) {
                e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                throw e;
            }
        }
    }

    /**
     * 贷款--生成审批表
     *
     * @param YWLSH
     * @return
     */
    public void generateReviewTable(String YWLSH, TokenContext tokenContext, AuditInfo auditInfo) {


        CLoanHousingBusinessProcess obj = DAOBuilder.instance(cLoanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        obj = cLoanHousingBusinessProcessDAO.refresh(obj);

        ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

        iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        List<ReviewInfo> reviewInfos = iSaveAuditHistory.getAuditHistoryList(YWLSH);

        if (obj.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款用户基础信息不存在");
        }
        String id = obj.getLoanHousingPersonInformationVice().getSpbwj();

        List<Review> reviews = new ArrayList<>();

        for (int i = 0; i < reviewInfos.size(); i++) {
            ReviewInfo reviewInfo1 = reviewInfos.get(i);
            Review review = new Review();
            review.setAction(reviewInfo1.getCaoZuo());
            review.setCZQD(reviewInfo1.getCZQD());
            review.setCZY(reviewInfo1.getCZY());
            review.setSLR(reviewInfo1.getCZY());
            review.setSLRYJ(reviewInfo1.getYYYJ());
            review.setSPSJ(DateUtil.date2Str(reviewInfo1.getSHSJ(), "yyyy-MM-dd HH:mm:ss"));
            review.setType(i);
            String ywwdmc = icAccountNetworkDAO.get(reviewInfo1.getYWWD()).getMingCheng();
            review.setYWWD(ywwdmc);
            reviews.add(review);
        }

        // TODO: 2017/10/20  putReviewTable
        id = iPdfService.putReviewTable(id, reviews);

        if (id != null && obj.getLoanHousingPersonInformationVice() != null) {
            obj.getLoanHousingPersonInformationVice().setSpbwj(id);

            CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("ywlsh", YWLSH);

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }

            });

            if (loanHousingPersonInformationBasic != null) {
                loanHousingPersonInformationBasic.setSpbwj(id);

                DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(loanHousingPersonInformationBasic).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
            }
        }
        cLoanHousingBusinessProcessDAO.update(obj);
    }

    /**
     * @param YWLSH
     * @return
     */
    public GetApplicantResponse getApplyDetails(String YWLSH) {

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.cLoanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }


        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //endregion

        //region //必要数据声明

        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice();

        CLoanHousePurchasingVice loanHousePurchasingVice = loanHousingBusinessProcess.getLoanHousePurchasingVice();

        CLoanHouseBuildVice loanHouseBuildVice = loanHousingBusinessProcess.getLoanHouseBuildVice();

        CLoanFundsVice loanFundsVice = loanHousingBusinessProcess.getLoanFundsVice();

        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice();

        CLoanHouseOverhaulVice loanHouseOverhaulVice = loanHousingBusinessProcess.getLoanHouseOverhaulVice();

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice();

        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = loanHousingGuaranteeContractVice.getGuaranteeMortgageVices() == null ? new ArrayList<CLoanGuaranteeMortgageVice>() : loanHousingGuaranteeContractVice.getGuaranteeMortgageVices();

        List<CLoanGuaranteeVice> list_loanGuaranteeVice = loanHousingGuaranteeContractVice.getGuaranteeVices() == null ? new ArrayList<CLoanGuaranteeVice>() : loanHousingGuaranteeContractVice.getGuaranteeVices();

        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = loanHousingGuaranteeContractVice.getGuaranteePledgeVices() == null ? new ArrayList<CLoanGuaranteePledgeVice>() : loanHousingGuaranteeContractVice.getGuaranteePledgeVices();

        //endregion

        return new GetApplicantResponse() {{

            this.setCommonBorrowerInformation(loanHousingCoborrowerVice == null ? new GetApplicantResponseCommonBorrowerInformation() : new GetApplicantResponseCommonBorrowerInformation() {{

                this.setHKSZD(loanHousingCoborrowerVice.getHkszd()/*户口所在地*/);
                this.setSCZL(loanHousingCoborrowerVice.getBlzl()/*提交资料*/);
                this.setGTJKRGJJZH(loanHousingCoborrowerVice.getGtjkrgjjzh()/*共同借款人公积金账号*/);
                this.setCHGX(loanHousingCoborrowerVice.getCdgx()/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他）*/);
                this.setSJHM(loanHousingCoborrowerVice.getSjhm()/*手机号码*/);
                this.setJCD(loanHousingCoborrowerVice.getJcd()/*缴存地*/);
                this.setGDDHHM(loanHousingCoborrowerVice.getGddhhm()/*固定电话号码*/);
                this.setGTJKRXM(loanHousingCoborrowerVice.getGtjkrxm()/*共同借款人姓名*/);
                this.setGTJKRZJLX(loanHousingCoborrowerVice.getGtjkrzjlx()/*共同借款人证件类型*/);
                this.setYSR(loanHousingCoborrowerVice.getYsr() + ""/*月收入*/);
                this.setGTJKRZJHM(loanHousingCoborrowerVice.getGtjkrzjhm()/*共同借款人证件号码*/);

                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingCoborrowerVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingCoborrowerVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingCoborrowerVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingCoborrowerVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingCoborrowerVice.getDwdz()/*单位地址*/);
                }});

                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingCoborrowerVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(loanHousingCoborrowerVice.getJzny()/*缴至年月*/);
                    this.setYJCE(loanHousingCoborrowerVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingCoborrowerVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingCoborrowerVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS(loanHousingCoborrowerVice.getLxzcjcys() + ""/*连续正常缴存月数*/);
                }});
            }});

            this.setSCZL(loanHousingBusinessProcess.getBlzl()/*提交资料*/);

            this.setSQSJ(DateUtil.date2Str(loanHousingBusinessProcess.getBlsj(), format)/*申请时间*/);

            this.setHouseInformation(new GetApplicantResponseHouseInformation() {{

                this.setPurchaseSecondInformation((loanHousePurchasingVice == null || !loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : (loanHousingCoborrowerVice == null ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : new GetApplicantResponseHouseInformationPurchaseSecondInformation() {{

                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setGRSKYHZH(loanHousePurchasingVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setKHYHMC(loanHousePurchasingVice.getKhyhmc()/*开户银行名称*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setYHKHM(loanHousePurchasingVice.getYhkhm()/*银行开户名*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + "");
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSFK(loanHousePurchasingVice.getSfk() + ""/*首付款*/);
                }}));

                this.setDKYT(loanHousingBusinessProcess.getDkyt() + ""/*贷款用途*/);

                this.setSFWESF(loanHousePurchasingVice == null ? null : loanHousePurchasingVice.getSfwesf() ? "1" : "0"/*是否为二手房*/);

                this.setPurchaseFirstInformation((loanHousePurchasingVice == null || loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : (loanHousePurchasingVice == null ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : new GetApplicantResponseHouseInformationPurchaseFirstInformation() {{
                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人开户银行名称*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setSFRZHHM(loanHousePurchasingVice.getSfrzhhm()/*售房人账户号码*/);
                    this.setLPMC(loanHousePurchasingVice.getLpmc()/*楼盘名称*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + ""/*单价*/);
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSPFYSXKBH(loanHousePurchasingVice.getSpfysxkbh()/*商品房预售许可编号*/);
                    this.setSFK(loanHousePurchasingVice.getSfk() + ""/*首付款*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人银行开户名*/);
                }}));

                this.setOverhaulInformation(loanHouseOverhaulVice == null ? new GetApplicantResponseHouseInformationOverhaulInformation() : new GetApplicantResponseHouseInformationOverhaulInformation() {{
                    this.setDXGCYS(loanHouseOverhaulVice.getDxgcys() + ""/*大修工程预算*/);
                    this.setGRSKYHZH(loanHouseOverhaulVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZJBGJGMCJBH(loanHouseOverhaulVice.getFwzjbgjgmcjbh(/*房屋质检报告机关名称及编号*/));
                    this.setFXHCS(loanHouseOverhaulVice.getTdsyzh()/*土地使用证号*/);
                    this.setFWZL(loanHouseOverhaulVice.getFwzl()/*房屋坐落*/);
                    this.setYJZMJ(loanHouseOverhaulVice.getYjzmj() + ""/*原建筑面积*/);
                    this.setKHYHMC(loanHouseOverhaulVice.getKhyhmc()/*开户银行名称*/);
                    this.setSCZL(loanHouseOverhaulVice.getBlzl()/*提交资料*/);
                    this.setYBDCZH(loanHouseOverhaulVice.getYbdczh()/*原不动产证号*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setYHKHM(loanHouseOverhaulVice.getYhkhm()/*银行开户名*/);
                }});

                this.setBuildInformation(loanHouseBuildVice == null ? new GetApplicantResponseHouseInformationBuildInformation() : new GetApplicantResponseHouseInformationBuildInformation() {{

                    this.setGRSKYHZH(loanHouseBuildVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWXZ(loanHouseBuildVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWJG(loanHouseBuildVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setFWZLDZ(loanHouseBuildVice.getFwzl()/*房屋坐落地址*/);
                    this.setTDSYZH(loanHouseBuildVice.getTdsyzh()/*土地使用证号*/);
                    this.setGRSYZJ(loanHouseBuildVice.getGrsyzj() + ""/*个人使用资金*/);
                    this.setJZCS(loanHouseBuildVice.getJzcs()/*建造层数*/);
                    this.setSCZL(loanHouseBuildVice.getBlzl()/*提交资料*/);
                    this.setYHKHM(loanHouseBuildVice.getYhkhm()/*银行开户名*/);
                    this.setJZYDGHXKZH(loanHouseBuildVice.getJzydghxkzh()/*建造用地规划许可证号*/);
                    this.setPZJGWH(loanHouseBuildVice.getPzjgwh()/*批准机关文号*/);
                    this.setJHJZFY(loanHouseBuildVice.getJhjzfy() + ""/*计划建造费用*/);
                    this.setKHHYHMC(loanHouseBuildVice.getKhhyhmc()/*开户行银行名称*/);
                    this.setJZGCGHXKZH(loanHouseBuildVice.getJzgcghxkzh()/*建造工程规划许可证号*/);
                    this.setJCHJZMJ(loanHouseBuildVice.getJchjzmj() + ""/*建成后建造面积*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setJSGCSGXKZH(loanHouseBuildVice.getJsgcsgxkzh()/*建设工程施工许可证号*/);
                }});
            }});

            this.setYWLSH(YWLSH/*业务流水号*/);

            this.setreviewInformation(new ArrayList<GetApplicantResponseReviewInformation>() {{//审核信息

            }});

            this.setCollateralInformation(new GetApplicantResponseCollateralInformation() {{

                this.setMortgageInformation(new ArrayList<GetApplicantResponseCollateralInformationMortgageInformation>() {{

                    for (CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice : list_loanGuaranteeMortgageVice) {

                        this.add(new GetApplicantResponseCollateralInformationMortgageInformation() {{
                            this.setDYWSYQRSFZHM(loanGuaranteeMortgageVice.getDywsyqrsfzhm()/*抵押物所有权人身份证号码*/);
                            this.setDYFWXS(loanGuaranteeMortgageVice.getDyfwxs() + ""/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/);
                            this.setDYWFWZL(loanGuaranteeMortgageVice.getDywfwzl()/*抵押物房屋坐落*/);
                            this.setDYWMC(loanGuaranteeMortgageVice.getDywmc()/*抵押物名称*/);
                            this.setFWJG(loanGuaranteeMortgageVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/);
                            this.setDYWGYQRLXDH(loanGuaranteeMortgageVice.getDywgyqrlxdh()/*抵押物共有权人联系电话*/);
                            this.setDYWPGJZ(loanGuaranteeMortgageVice.getDywpgjz() + ""/*抵押物评估价值*/);
                            this.setDYWGYQRSFZHM(loanGuaranteeMortgageVice.getDywgyqrsfzhm()/*抵押物共有权人身份证号码*/);
                            this.setDYWSYQRXM(loanGuaranteeMortgageVice.getDywsyqrxm()/*抵押物所有权人姓名*/);
                            this.setDYWSYQRLXDH(loanGuaranteeMortgageVice.getDywsyqrlxdh()/*抵押物所有权人联系电话*/);
                            this.setFWMJ(loanGuaranteeMortgageVice.getFwmj() + ""/*房屋面积1*/);
                            this.setQSZSBH(loanGuaranteeMortgageVice.getQszsbh()/*权属证书编号*/);
                            this.setDYWGYQRXM(loanGuaranteeMortgageVice.getDywgyqrxm()/*抵押物共有权人姓名*/);
                        }});

                    }
                }});

                this.setPledgeInformation(new ArrayList<GetApplicantResponseCollateralInformationPledgeInformation>() {{

                    for (CLoanGuaranteePledgeVice loanGuaranteePledgeVice : list_loanGuaranteePledgeVice) {

                        this.add(new GetApplicantResponseCollateralInformationPledgeInformation() {{
                            this.setZYWSYQRSFZHM(loanGuaranteePledgeVice.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);
                            this.setZYWSYQRXM(loanGuaranteePledgeVice.getZywsyqrxm()/*质押物所有权人姓名*/);
                            this.setZYWJZ(loanGuaranteePledgeVice.getZywjz() + ""/*质押物价值*/);
                            this.setZYWSYQRLXDH(loanGuaranteePledgeVice.getZywsyqrlxdh()/*质押物所有权人联系电话*/);
                            this.setZYWMC(loanGuaranteePledgeVice.getZywmc()/*质押物名称*/);
                        }});

                    }
                }});

                this.setDKDBLX(loanHousingBusinessProcess.getDkdblx()/*贷款担保类型*/);

                this.setGuaranteeInformation(new ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation>() {{

                    for (CLoanGuaranteeVice loanGuaranteeVice : list_loanGuaranteeVice) {

                        this.add(new GetApplicantResponseCollateralInformationGuaranteeInformation() {{
                            this.setYZBM(loanGuaranteeVice.getYzbm()/*邮政编码*/);
                            this.setBZRXJZDZ(loanGuaranteeVice.getFrdbxjzdz()/*保证人现居住地址*/);
                            this.setBZRSFZHM(loanGuaranteeVice.getFrdbsfzhm()/*保证人身份证号码*/);
                            this.setBZRXM(loanGuaranteeVice.getFrdbxm()/*保证人姓名*/);
                            this.setBZRLXDH(loanGuaranteeVice.getFrdblxdh()/*保证人联系电话*/);
                            this.setTXDZ(loanGuaranteeVice.getTxdz()/*通讯地址*/);
                            this.setBZFLX(loanGuaranteeVice.getBzflx()/*保证方类型（0：个人 1：机构）*/);
                        }});

                    }
                }});

                this.setSCZL(loanHousingGuaranteeContractVice.getBlzl()/*提交资料*/);
            }});

            this.setCapitalInformation(new GetApplicantResponseCapitalInformation() {{
                this.setHTDKJEDX(loanFundsVice.getHtdkjedx()/*合同贷款金额大写*/);
                this.setDKLX(loanFundsVice.getDklx() + ""/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/);
                this.setDKQS(loanFundsVice.getDkqs() + ""/*贷款期数*/);
                this.setHKFS(loanFundsVice.getHkfs() + ""/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/);
                this.setHTDKJE(loanFundsVice.getHtdkje() + ""/*合同贷款金额*/);
                this.setLLFSBL(loanFundsVice.getLlfsbl() + ""/*利率浮动比例*/);
                this.setJKHTLL(loanFundsVice.getJkhtll() + ""/*借款合同利率*/);
                //this.setZXLL(loanFundsVice.getZxll() + ""/*执行利率*/);
                this.setWTKHYJCE(loanFundsVice.getWtkhyjce() ? "1" : "0"/*委托扣划月缴存额*/);
                this.setDKDBLX(loanFundsVice.getDkdblx() + ""/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/);
                this.setFWTS(loanFundsVice.getFwts() + ""/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/);
            }});

            this.setApplicantInformation(new GetApplicantResponseApplicantInformation() {{
                this.setBorrowerInformation(new GetApplicantResponseApplicantInformationBorrowerInformation() {{
                    this.setHYZK(loanHousingPersonInformationVice.getHyzk() + ""/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/);
                    this.setNianLing(loanHousingPersonInformationVice.getNianLing()/*年龄*/);
                    this.setJTZZ(loanHousingPersonInformationVice.getJtzz()/*家庭住址*/);
                    this.setCSNY(loanHousingPersonInformationVice.getCsny()/*出生年月*/);
                    this.setJKRZJHM(loanHousingPersonInformationVice.getJkrzjhm()/*借款人证件号码*/);
                    this.setYGXZ(loanHousingPersonInformationVice.getYgxz() + ""/*用工性质（0：正式职工 1：合同制 2：聘用制）*/);
                    this.setYSR(loanHousingPersonInformationVice.getYsr() + ""/*月收入*/);
                    this.setJKRXM(loanHousingPersonInformationVice.getJkrxm()/*借款人姓名*/);
                    this.setHKSZD(loanHousingPersonInformationVice.getHkszd()/*户口所在地*/);
                    this.setZhiCheng(loanHousingPersonInformationVice.getZhiCheng()/*职称*/);
                    this.setJKRZJLX(loanHousingPersonInformationVice.getJkrzjlx() + ""/*借款人证件类型*/);
                    this.setSJHM(loanHousingPersonInformationVice.getSjhm()/*手机号码*/);
                    this.setJKZK(loanHousingPersonInformationVice.getJkzk() + ""/*健康状态（0：良好 1：一般 2：差）*/);
                    this.setGDDHHM(loanHousingPersonInformationVice.getGddhhm()/*固定电话号码*/);
                    this.setZYJJLY(loanHousingPersonInformationVice.getZyjjly() + ""/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/);
                    this.setXingBie(loanHousingPersonInformationVice.getXingBie() + ""/*性别*/);
                    this.setJTYSR(loanHousingPersonInformationVice.getJtysr() + ""/*家庭月收入*/);
                    this.setXueLi(loanHousingPersonInformationVice.getXueLi()/*学历*/);
                    this.setZhiWu(loanHousingPersonInformationVice.getZhiWu()/*职务*/);
                }});
                this.setJKRGJJZH(loanHousingPersonInformationVice.getJkrgjjzh()/*借款人公积金账号*/);
                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingPersonInformationVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingPersonInformationVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingPersonInformationVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingPersonInformationVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingPersonInformationVice.getDwdz()/*单位地址*/);
                }});
                this.setJCD(loanHousingPersonInformationVice.getJcd()/*缴存地*/);
                this.setSCZL(loanHousingPersonInformationVice.getBlzl()/* 办理资料*/);
                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingPersonInformationVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(loanHousingPersonInformationVice.getJzny()/*缴至年月*/);
                    this.setYJCE(loanHousingPersonInformationVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingPersonInformationVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingPersonInformationVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS(loanHousingPersonInformationVice.getLxzcjcys() + ""/*连续正常缴存月数*/);
                }});
            }});
        }};
    }

    /**
     * 贷款-未审核列表New
     *
     * @param tokenContext
     * @param conditions
     * @param module
     * @param type
     * @return
     */
    @Override
    public PageResNew getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        HashMap<String, String> ywlx_subtype = new HashMap<>();

        IBaseDAO.CriteriaExtension extension = null;

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String FKGS = conditions.get("FKGS");
        String YWLX = conditions.get("YWLX");
        String JKRXM = conditions.get("JKRXM");
        String LPMC = conditions.get("LPMC");
        String HKLX = conditions.get("HKLX");
        String JKHTBH = conditions.get("JKHTBH");
        String marker = conditions.get("marker");

        if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

            results = new ArrayList<HousingCompanyReviewResHousingCompanyReview>();

            ywlx_subtype.put(LoanBusinessType.新建房开.getCode(), BusinessSubType.贷款_房开申请受理.getSubType());
            ywlx_subtype.put(LoanBusinessType.房开变更.getCode(), BusinessSubType.贷款_房开变更受理.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_房开.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_房开.getName());

            if (ywlxs.isEmpty())
                return res;

            if (StringUtil.notEmpty(YWLX) && !LoanBusinessType.所有.getCode().equals(YWLX)) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cznr", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            filter.put("step", sources);
            if (StringUtil.notEmpty(FKGS)) {
                filter.put("loanHousingCompanyVice.fkgs", FKGS);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

            results = new ArrayList<EstateProjectReviewRes>();

            ywlx_subtype.put(LoanBusinessType.新建楼盘.getCode(), BusinessSubType.贷款_楼盘申请受理.getSubType());
            ywlx_subtype.put(LoanBusinessType.楼盘变更.getCode(), BusinessSubType.贷款_楼盘变更受理.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_楼盘.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_楼盘.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
            if (StringUtil.notEmpty(YWLX) && !LoanBusinessType.所有.getCode().equals(YWLX)) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cznr", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            if (StringUtil.notEmpty(LPMC)) {
                filter.put("loanEatateProjectVice.lpmc", LPMC);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

            results = new ArrayList<LoanReviewListResponse>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_放款.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_个人贷款申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_放款.getName());

            if (ywlxs.isEmpty())
                return res;

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }
            filter.put("step", sources);
            filter.put("cznr", ywlxs);
            //放款业务的审核，只有受理网点人员可见
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }

                }
            };

        } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

            results = new ArrayList<HousingRepamentApplyRangeGetRes>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_还款.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_个人还款申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_还款.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanApplyRepaymentVice.jkrxm", JKRXM);
            }
            if (StringUtil.notEmpty(HKLX) && !LoanBusinessType.所有.getCode().equals(HKLX)) {
                if (ywlxs.contains(HKLX)) {
                    filter.put("cznr", HKLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cznr", ywlxs);
            }
            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

            results = new ArrayList<ContractAlterReviewRes>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_合同.getName(),
                    Arrays.asList(
                            BusinessSubType.贷款_合同变更申请.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Loan,
                    ReviewSubModule.贷款_合同.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("step", sources);
/*            if (StringUtil.notEmpty(JKHTBH)) {
                filter.put("loanContract.jkhtbh", JKHTBH);
            }*/
            filter.put("cznr", ywlxs);

            //基于受理网点的过滤
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(JKHTBH)) {
                        criteria.createAlias("loanContract", "loanContract");
                        criteria.add(Restrictions.eq("loanContract.jkhtbh", JKHTBH));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {

            if (entity instanceof CLoanHousingBusinessProcess) {

                CLoanHousingBusinessProcess details = (CLoanHousingBusinessProcess) entity;

                if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ywlx_subtype.get(details.getCznr()), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    HousingCompanyReviewResHousingCompanyReview obj = new HousingCompanyReviewResHousingCompanyReview();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingCompanyVice() != null) {
                        obj.setFKGS(details.getLoanHousingCompanyVice().getFkgs());//房开公司
                        obj.setSJFLB(details.getLoanHousingCompanyVice().getSjflb());//售建房类别
                    }
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setYWLX(details.getCznr());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ywlx_subtype.get(details.getCznr()), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    EstateProjectReviewRes obj = new EstateProjectReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanEatateProjectVice() != null) {
                        obj.setLPDZ(details.getLoanEatateProjectVice().getLpdz());//楼盘地址
                        obj.setLPMC(details.getLoanEatateProjectVice().getLpmc());//楼盘名称
                    }
                    obj.setZhuangTai(details.getStep());//状态
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWLX(details.getCznr());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                    LoanReviewListResponse obj = new LoanReviewListResponse();

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_个人贷款申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());

                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        obj.setJKRZJHM(details.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                    }
                    obj.setStatus(details.getStep());
                    obj.setYWLSH(details.getYwlsh()/*业务流水号*/);
                    if (details.getLoanFundsVice() != null) {
                        obj.setHTDKJE(details.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format)/*到达时间*/);
                    obj.setYWWD(details.getYwwd().getMingCheng()/*业务网点*/);
                    obj.setCZY(details.getCzy()/*操作员*/);
                    obj.setSFTS(isSpecial ? "1" : "0");    //1能0不能
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSPBWJ(details.getLoanHousingPersonInformationVice() == null ? null : details.getLoanHousingPersonInformationVice().getSpbwj());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_个人还款申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());

                    HousingRepamentApplyRangeGetRes obj = new HousingRepamentApplyRangeGetRes();

                    obj.setYWLSH(details.getYwlsh());
                    if (details.getLoanApplyRepaymentVice() != null) {
                        obj.setDKZH(details.getLoanApplyRepaymentVice().getDkzh());
                        obj.setJKRXM(details.getLoanApplyRepaymentVice().getJkrxm());
                        obj.setJKRZJHM(details.getLoanApplyRepaymentVice().getJkrzjhm());
                        obj.setSQHKLX(details.getLoanApplyRepaymentVice().getHklx());
                        obj.setSQHKJE(details.getLoanApplyRepaymentVice().getHkje() + "");
                    }
                    obj.setCZY(details.getCzy());
                    obj.setYWWD(details.getYwwd().getMingCheng());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), BusinessSubType.贷款_合同变更申请.getSubType(), tokenContext.getRoleList().get(0), details.getYwwd().getId());
                    ContractAlterReviewRes obj = new ContractAlterReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKHTBH(details.getLoanHousingPersonInformationVice().getJkhtbh());//借款合同编号
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));//到达时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setId(details.getId());

                    results.add(obj);
                }
            }
        }
        if (results.size() == 0) {
            if (action.equals(ListAction.Next.getCode())) {
                res.setTag("L");
            } else if (action.equals(ListAction.Previous.getCode())) {
                res.setTag("F");
            }
        }
        res.setResults(action, results);
        return res;
    }

    /**
     * 贷款-已审核列表New
     *
     * @param tokenContext
     * @param conditions
     * @param module
     * @param type
     * @return
     */
    @Override
    public PageResNew getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String FKGS = conditions.get("FKGS");
        String ZhuangTai = conditions.get("ZhuangTai");
        String YWLX = conditions.get("YWLX");
        String LPMC = conditions.get("LPMC");
        String JKRXM = conditions.get("JKRXM");
        String HKLX = conditions.get("HKLX");
        String JKHTBH = conditions.get("JKHTBH");
        String marker = conditions.get("marker");

        if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

            results = new ArrayList<HousingCompanyReviewResHousingCompanyReview>();

            if (StringUtil.notEmpty(FKGS)) {
                filter.put("loanHousingCompanyVice.fkgs", FKGS);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWLX)) {
                        criteria.add(Restrictions.eq("cznr", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.新建房开.getCode(),
                                LoanBusinessType.房开变更.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.办结.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

            results = new ArrayList<EstateProjectReviewRes>();

            if (StringUtil.notEmpty(LPMC)) {
                filter.put("loanEatateProjectVice.lpmc", LPMC);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWLX)) {
                        criteria.add(Restrictions.eq("cznr", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.新建楼盘.getCode(),
                                LoanBusinessType.楼盘变更.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.办结.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

            results = new ArrayList<LoanReviewListResponse>();

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.add(Restrictions.in("cznr", Arrays.asList(LoanBusinessType.贷款发放.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.待签合同.getName(),
                                        LoanBussinessStatus.待入账.getName(),
                                        LoanBussinessStatus.已入账.getName(),
                                        LoanBussinessStatus.入账失败.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

            results = new ArrayList<HousingRepamentApplyRangeGetRes>();

            if (StringUtil.notEmpty(JKRXM)) {
                filter.put("loanApplyRepaymentVice.jkrxm", JKRXM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(HKLX) && !LoanBusinessType.所有.getCode().equals(HKLX)) {
                        criteria.add(Restrictions.eq("cznr", HKLX));
                    } else {
                        criteria.add(Restrictions.in("cznr", Arrays.asList(
                                LoanBusinessType.逾期还款.getCode(),
                                LoanBusinessType.提前还款.getCode(),
                                LoanBusinessType.公积金提取还款.getCode(),
                                LoanBusinessType.结清.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.审核不通过.getName(),
                                        LoanBussinessStatus.待入账.getName(),
                                        LoanBussinessStatus.扣款已发送.getName(),
                                        LoanBussinessStatus.入账失败.getName(),
                                        LoanBussinessStatus.已入账.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

            results = new ArrayList<ContractAlterReviewRes>();

            if (StringUtil.notEmpty(JKHTBH)) {
                filter.put("loanContract.jkhtbh", JKHTBH);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.add(Restrictions.in("cznr", Arrays.asList(LoanBusinessType.合同变更.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }
                    if (StringUtil.isEmpty(ZhuangTai) || LoanBussinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        LoanBussinessStatus.办结.getName(),
                                        LoanBussinessStatus.审核不通过.getName())),
                                Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));
                    } else if (!LoanBussinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {

            if (entity instanceof CLoanHousingBusinessProcess) {

                CLoanHousingBusinessProcess details = (CLoanHousingBusinessProcess) entity;

                if (ReviewSubModule.贷款_房开.getCode().equals(type)) {

                    HousingCompanyReviewResHousingCompanyReview obj = new HousingCompanyReviewResHousingCompanyReview();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingCompanyVice() != null) {
                        obj.setFKGS(details.getLoanHousingCompanyVice().getFkgs());//房开公司
                        obj.setSJFLB(details.getLoanHousingCompanyVice().getSjflb());//售建房类别
                    }
                    obj.setCZY(details.getCzy());//操作员
                    obj.setZhuangtai(details.getStep());
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setYWLX(details.getCznr());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//（审核）受理时间
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_楼盘.getCode().equals(type)) {

                    EstateProjectReviewRes obj = new EstateProjectReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanEatateProjectVice() != null) {
                        obj.setLPMC(details.getLoanEatateProjectVice().getLpmc());//楼盘名称
                        obj.setLPDZ(details.getLoanEatateProjectVice().getLpdz());//楼盘地址
                    }
                    obj.setZhuangTai(details.getStep());//状态
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//（审核）受理时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    obj.setYWLX(details.getCznr());
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_放款.getCode().equals(type)) {

                    LoanReviewListResponse obj = new LoanReviewListResponse();

                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKRXM(details.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        obj.setJKRZJHM(details.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                    }
                    obj.setStatus(details.getStep()/*状态*/);
                    obj.setYWLSH(details.getYwlsh()/*业务流水号*/);
                    if (details.getLoanFundsVice() != null) {
                        obj.setHTDKJE(details.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                    }
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format)/*（审核）受理时间*/);
                    obj.setYWWD(details.getYwwd().getMingCheng()/*业务网点*/);
                    obj.setCZY(details.getCzy()/*操作员*/);
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_还款.getCode().equals(type)) {

                    HousingRepamentApplyRangeGetRes obj = new HousingRepamentApplyRangeGetRes();

                    obj.setYWLSH(details.getYwlsh());
                    if (details.getLoanApplyRepaymentVice() != null) {
                        obj.setDKZH(details.getLoanApplyRepaymentVice().getDkzh());
                        obj.setJKRXM(details.getLoanApplyRepaymentVice().getJkrxm());
                        obj.setJKRZJHM(details.getLoanApplyRepaymentVice().getJkrzjhm());
                        obj.setSQHKLX(details.getLoanApplyRepaymentVice().getHklx());
                        obj.setSQHKJE(details.getLoanApplyRepaymentVice().getHkje() + "");
                    }
                    obj.setStatus(details.getStep());
                    obj.setCZY(details.getCzy());
                    obj.setYWWD(details.getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.贷款_合同.getCode().equals(type)) {

                    ContractAlterReviewRes obj = new ContractAlterReviewRes();

                    obj.setYWLSH(details.getYwlsh());//业务流水号
                    if (details.getLoanHousingPersonInformationVice() != null) {
                        obj.setJKHTBH(details.getLoanHousingPersonInformationVice().getJkhtbh());//借款合同编号
                    }
                    obj.setZhuangTai(details.getStep());
                    obj.setSLSJ(DateUtil.date2Str(details.getShsj(), format));//受理时间
                    obj.setYWWD(details.getYwwd().getMingCheng());//业务网点
                    obj.setCZY(details.getCzy());//操作员
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);
                }
            }
        }
        if (results.size() == 0) {
            if (action.equals(ListAction.Next.getCode())) {
                res.setTag("L");
            } else if (action.equals(ListAction.Previous.getCode())) {
                res.setTag("F");
            }
        }
        res.setResults(action, results);
        return res;
    }
}
