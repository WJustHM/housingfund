package com.handge.housingfund.review.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.finance.IFinanceBaseService;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.model.CommonFinanceReview;
import com.handge.housingfund.common.service.finance.model.FixedBusinessAudit;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessStatus;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessType;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICFinanceActived2FixedDAO;
import com.handge.housingfund.database.dao.ICFinanceBusinessProcessDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedDrawDAO;
import com.handge.housingfund.database.entities.CFinanceActived2Fixed;
import com.handge.housingfund.database.entities.CFinanceBusinessProcess;
import com.handge.housingfund.database.entities.CFinanceFixedDraw;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.review.util.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Liujuhao on 2017/9/25.
 */
@SuppressWarnings("Duplicates")
@Component
@Qualifier(value = "finance")
public class FinanceReview extends CommonReview {

    @Autowired
    private ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;    //1

    @Autowired
    private ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;    //2

    @Autowired
    private ICFinanceBusinessProcessDAO icFinanceBusinessProcessDAO;

    @Autowired
    private IFinanceTrader iFinanceTrader;

    @Autowired
    IFinanceBaseService financeBaseService;

    /*财务--未审核列表*/
    @Override
    public PageRes getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String module, String type) {

        PageRes res = new PageRes<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;

        int pageNo = StringUtil.isEmpty(conditions.get("pageNo")) ? 1 : Integer.parseInt(conditions.get("pageNo"));

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        if (ReviewSubModule.财务_日常.getCode().equals(type)) {

            results = new ArrayList();

            ArrayList<String> zjywlxs = icStateMachineConfigurationDAO.getSubTypesByAuth(null, tokenContext.getRoleList().get(0), BusinessType.Finance, ReviewSubModule.财务_日常.getName());

            if (zjywlxs.isEmpty())
                return res;

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Finance,
                    ReviewSubModule.财务_日常.getName(),
                    zjywlxs,
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            String YWMC = conditions.get("YWMC");
            String CZY = conditions.get("CZY");

            filter.put("step", sources);
            if (StringUtil.notEmpty(YWMC) && !YWMC.equals(FinanceBusinessType.所有.getCode())) {
                if (zjywlxs.contains(ReviewSubModule.财务_日常.getName() + YWMC)) {
                    filter.put("cFinanceDailyBusinessVice.zjywlx", YWMC);
                } else {
                    return res;
                }
            }
            if (StringUtil.notEmpty(CZY)) {
                filter.put("czy", CZY);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };
        }

        if (ReviewSubModule.财务_定期.getCode().equals(type)) {

            results = new ArrayList<FixedBusinessAudit>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Finance,
                    ReviewSubModule.财务_定期.getName(),
                    null,
                    tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            String KHYHMC = conditions.get("KHYHMC");
            String ZHHM = conditions.get("ZHHM");
//            String YWLX = conditions.get("YWLX");

            if (StringUtil.notEmpty(KHYHMC)) {
                filter.put("khyhmc", KHYHMC);
            }
            if (StringUtil.notEmpty(ZHHM)) {
                filter.put("acct_no", ZHHM);
            }
            filter.put("cFinanceManageFinanceExtension.step", sources);

            /*09：定期存款 10：定期支取 11：通知存款 12：通知存款支取*/

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, res);

        for (Object entity : list) {
            if (entity instanceof CFinanceBusinessProcess) {

                CFinanceBusinessProcess details = (CFinanceBusinessProcess) entity;

                if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ReviewSubModule.财务_日常.getName() + details.getcFinanceDailyBusinessVice().getZjywlx(), tokenContext.getRoleList().get(0), null);
                    CommonFinanceReview obj = new CommonFinanceReview();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMC(details.getcFinanceDailyBusinessVice().getZjywlx());
                    obj.setCZY(details.getCzy());
                    obj.setZhuangTai(details.getStep());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }

                    results.add(obj);
                }

            } else if (entity instanceof CFinanceActived2Fixed) {

                CFinanceActived2Fixed details = (CFinanceActived2Fixed) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getAmt() != null ? details.getAmt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setDdsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getDdsj(), format));

                    results.add(obj);
                }
            } else if (entity instanceof CFinanceFixedDraw) {

                CFinanceFixedDraw details = (CFinanceFixedDraw) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getDeposti_amt() != null ? details.getDeposti_amt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setDdsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getDdsj(), format));

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /*财务--已审核列表*/
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

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        if (ReviewSubModule.财务_日常.getCode().equals(type)) {

            String YWMC = conditions.get("YWMC");
            String CZY = conditions.get("CZY");
            String ZhuangTai = conditions.get("ZhuangTai");

            results = new ArrayList();

            if (StringUtil.notEmpty(CZY)) {
                filter.put("czy", CZY);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWMC)) {
                        criteria.createAlias("cFinanceDailyBusinessVice", "cFinanceDailyBusinessVice");
                        criteria.add(Restrictions.eq("cFinanceDailyBusinessVice.zjywlx", YWMC));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || FinanceBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        FinanceBusinessStatus.办结.getName(),
                                        FinanceBusinessStatus.已入账.getName(),
                                        FinanceBusinessStatus.入账失败.getName(),
                                        FinanceBusinessStatus.待入账.getName(),
                                        FinanceBusinessStatus.审核不通过.getName())),
                                Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName())));
                    } else if (!FinanceBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.财务_定期.getCode().equals(type)) {

            String KHYHMC = conditions.get("KHYHMC");
            String ZHHM = conditions.get("ZHHM");
            String ZhuangTai = conditions.get("ZhuangTai");

            results = new ArrayList();

            if (StringUtil.notEmpty(KHYHMC)) {
                filter.put("khyhmc", KHYHMC);
            }
            if (StringUtil.notEmpty(ZHHM)) {
                filter.put("acct_no", ZHHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cFinanceManageFinanceExtension", "cFinanceManageFinanceExtension");

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || FinanceBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cFinanceManageFinanceExtension.step", Arrays.asList(
                                        FinanceBusinessStatus.审核不通过.getName(),
                                        FinanceBusinessStatus.定期存款失败.getName(),
                                        FinanceBusinessStatus.定期存款成功.getName(),
                                        FinanceBusinessStatus.定期支取失败.getName(),
                                        FinanceBusinessStatus.定期支取成功.getName(),
                                        FinanceBusinessStatus.待定转活.getName(),
                                        FinanceBusinessStatus.待活转定.getName())),
                                Restrictions.like("cFinanceManageFinanceExtension.step", FinanceBusinessStatus.待某人审核.getName())));
                    } else if (!FinanceBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cFinanceManageFinanceExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cFinanceManageFinanceExtension.step", FinanceBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cFinanceManageFinanceExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, res);

        for (Object entity : list) {

            if (entity instanceof CFinanceBusinessProcess) {

                CFinanceBusinessProcess details = (CFinanceBusinessProcess) entity;

                if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                    CommonFinanceReview obj = new CommonFinanceReview();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMC(details.getcFinanceDailyBusinessVice().getZjywlx());
                    obj.setZhuangTai(details.getStep());
                    obj.setCZY(details.getCzy());
                    obj.setSHSJ(DateUtil.date2Str(details.getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);
                }

            } else if (entity instanceof CFinanceActived2Fixed) {

                CFinanceActived2Fixed details = (CFinanceActived2Fixed) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getAmt() != null ? details.getAmt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setStep(details.getcFinanceManageFinanceExtension().getStep());
                    obj.setShsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getShsj(), format));

                    results.add(obj);
                }

            } else if (entity instanceof CFinanceFixedDraw) {

                CFinanceFixedDraw details = (CFinanceFixedDraw) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getDeposti_amt() != null ? details.getDeposti_amt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setStep(details.getcFinanceManageFinanceExtension().getStep());
                    obj.setShsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getShsj(), format));

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /*财务--审核操作*/
    @Override
    public void doReview(TokenContext tokenContext, String YWLSH, AuditInfo auditInfo, boolean pass, String module, String type) {

        Object entity = getEntityByModule(module, YWLSH);

        List<String> nexts = new ArrayList<>();

        if (entity instanceof CFinanceBusinessProcess) {

            CFinanceBusinessProcess obj = (CFinanceBusinessProcess) entity;

            String id = obj.getId();
            String czmc = ReviewSubModule.财务_日常.getName() + obj.getcFinanceDailyBusinessVice().getZjywlx();
            String step = obj.getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            MultiReviewConfig check = NormalJsonUtils.toObj4Review(obj.getShybh());

            if (check != null && check.getDQSHY() != null)
                if (!check.getDQSHY().equals(tokenContext.getUserId()))
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已被审核员 " + check.getDQXM() + "锁定");

            if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Finance,
                        ReviewSubModule.财务_日常.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

//                if (czmc == null || !Arrays.asList(
//                        FinanceBusinessType.日常财务处理.getCode()).contains(czmc)) {
//                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
//                }

                ArrayList<String> zjywlxs = icStateMachineConfigurationDAO.getSubTypesByAuth(null, tokenContext.getRoleList().get(0), BusinessType.Finance, ReviewSubModule.财务_日常.getName());

                if (czmc == null || !zjywlxs.contains(czmc)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Finance);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(czmc);
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
                                // completed: 2017/11/10 日常财务只有一种，和资金业务类型无关
                                if (!StringUtil.isIntoReview(next, null))
                                    financeBaseService.financeJobFinished(id, tokenContext);
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });
            }
            obj = (CFinanceBusinessProcess) getEntityByModule(module, YWLSH);
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
            obj.setZhclry(tokenContext.getUserInfo().getCZY());

            icFinanceBusinessProcessDAO.update(obj);
            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (entity instanceof CFinanceFixedDraw) {

            CFinanceFixedDraw obj = (CFinanceFixedDraw) entity;

            String id = obj.getId();
            String czmc = obj.getcFinanceManageFinanceExtension().getYwlx();
            String step = obj.getcFinanceManageFinanceExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Finance,
                        ReviewSubModule.财务_定期.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        FinanceBusinessType.定期支取.getCode()).contains(czmc)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Finance);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {{

                            this.put(FinanceBusinessType.定期支取.getCode(), BusinessSubType.财务_定期支取.getSubType());

                        }}.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {

                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        if (pass) {
                            if (czmc.equals(FinanceBusinessType.定期支取.getCode())) {
                                if (!StringUtil.isIntoReview(next, null))
                                    iFinanceTrader.fixedDraw(id);
                            }
                        }
                        nexts.add(next);
                    }
                });
            }
            obj = (CFinanceFixedDraw) getEntityByModule(module, YWLSH);
            if (nexts.size() != 1) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getcFinanceManageFinanceExtension().getStep().equals(step))
                obj.getcFinanceManageFinanceExtension().setStep(nexts.get(0));

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getcFinanceManageFinanceExtension().getStep(), null)) {
                obj.getcFinanceManageFinanceExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getcFinanceManageFinanceExtension().getShybh(), false, pass);

            obj.getcFinanceManageFinanceExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getcFinanceManageFinanceExtension().setShsj(new Date());

            icFinanceFixedDrawDAO.update(obj);

            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (entity instanceof CFinanceActived2Fixed) {

            CFinanceActived2Fixed obj = (CFinanceActived2Fixed) entity;

            String id = obj.getId();
            String czmc = obj.getcFinanceManageFinanceExtension().getYwlx();
            String step = obj.getcFinanceManageFinanceExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Finance,
                        ReviewSubModule.财务_定期.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        FinanceBusinessType.定期存款.getCode()).contains(czmc)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setType(BusinessType.Finance);
                        this.setOperator(czy);
                        this.setWorkstation(ywwd);
                        this.setSubtype(new HashMap<String, String>() {{

                            this.put(FinanceBusinessType.定期存款.getCode(), BusinessSubType.财务_定期活期转定期.getSubType());

                        }}.get(czmc));
                    }
                }, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {

                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        if (pass) {
                            if (czmc.equals(FinanceBusinessType.定期存款.getCode())) {
                                if (!StringUtil.isIntoReview(next, null))
                                    iFinanceTrader.actived2Fixed(id);
                            }
                        }
                        nexts.add(next);
                    }
                });
            }
            obj = (CFinanceActived2Fixed) getEntityByModule(module, YWLSH);
            if (nexts.size() != 1) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getcFinanceManageFinanceExtension().getStep().equals(step))
                obj.getcFinanceManageFinanceExtension().setStep(nexts.get(0));

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getcFinanceManageFinanceExtension().getStep(), null)) {
                obj.getcFinanceManageFinanceExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getcFinanceManageFinanceExtension().getShybh(), false, pass);

            obj.getcFinanceManageFinanceExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getcFinanceManageFinanceExtension().setShsj(new Date());

            icFinanceActived2FixedDAO.update(obj);

            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);
        }
    }

    /**
     * 财务--批量操作
     *
     * @param tokenContext
     * @param YWLSHs
     * @param auditInfo
     * @param pass
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

    /*财务--未审核列表New*/
    @Override
    public PageResNew getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;
        
        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));
        
        String marker = conditions.get("marker");
        
        if (ReviewSubModule.财务_日常.getCode().equals(type)) {

            results = new ArrayList<CommonFinanceReview>();

            ArrayList<String> zjywlxs = icStateMachineConfigurationDAO.getSubTypesByAuth(null, tokenContext.getRoleList().get(0), BusinessType.Finance, ReviewSubModule.财务_日常.getName());

            if (zjywlxs.isEmpty())
                return res;

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Finance,
                    ReviewSubModule.财务_日常.getName(),
                    zjywlxs,
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            String YWMC = conditions.get("YWMC");
            String CZY = conditions.get("CZY");

            filter.put("step", sources);
            if (StringUtil.notEmpty(YWMC) && !YWMC.equals(FinanceBusinessType.所有.getCode())) {
                if (zjywlxs.contains(ReviewSubModule.财务_日常.getName() + YWMC)) {
                    filter.put("cFinanceDailyBusinessVice.zjywlx", YWMC);
                } else {
                    return res;
                }
            }
            if (StringUtil.notEmpty(CZY)) {
                filter.put("czy", CZY);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("ddsj", JSSJ));
                    }
                }
            };
        }

        if (ReviewSubModule.财务_定期.getCode().equals(type)) {

            results = new ArrayList<FixedBusinessAudit>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Finance,
                    ReviewSubModule.财务_定期.getName(),
                    null,
                    tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            String KHYHMC = conditions.get("KHYHMC");
            String ZHHM = conditions.get("ZHHM");
//            String YWLX = conditions.get("YWLX");

            if (StringUtil.notEmpty(KHYHMC)) {
                filter.put("khyhmc", KHYHMC);
            }
            if (StringUtil.notEmpty(ZHHM)) {
                filter.put("acct_no", ZHHM);
            }
            filter.put("cFinanceManageFinanceExtension.step", sources);

            /*09：定期存款 10：定期支取 11：通知存款 12：通知存款支取*/

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {
            if (entity instanceof CFinanceBusinessProcess) {

                CFinanceBusinessProcess details = (CFinanceBusinessProcess) entity;

                if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getStep(), ReviewSubModule.财务_日常.getName() + details.getcFinanceDailyBusinessVice().getZjywlx(), tokenContext.getRoleList().get(0), null);
                    CommonFinanceReview obj = new CommonFinanceReview();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMC(details.getcFinanceDailyBusinessVice().getZjywlx());
                    obj.setCZY(details.getCzy());
                    obj.setZhuangTai(details.getStep());
                    obj.setDDSJ(DateUtil.date2Str(details.getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setId(details.getId());

                    results.add(obj);
                }

            } else if (entity instanceof CFinanceActived2Fixed) {

                CFinanceActived2Fixed details = (CFinanceActived2Fixed) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getAmt() != null ? details.getAmt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setDdsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getDdsj(), format));
                    obj.setId(details.getId());

                    results.add(obj);
                }
            } else if (entity instanceof CFinanceFixedDraw) {

                CFinanceFixedDraw details = (CFinanceFixedDraw) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getDeposti_amt() != null ? details.getDeposti_amt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setDdsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getDdsj(), format));
                    obj.setId(details.getId());

                    results.add(obj);
                }
            }
        }
        if (results.size() == 0) {
            if (action.equals(ListAction.Next.getCode())) {
                res.setTag("L");
            }
            else if (action.equals(ListAction.Previous.getCode())) {
                res.setTag("F");
            }
        }
        res.setResults(action,results);
        return res;
    }

    /*财务--已审核列表New*/
    @Override
    public PageResNew getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;
        
        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));
        
        String marker = conditions.get("marker");
        
        if (ReviewSubModule.财务_日常.getCode().equals(type)) {

            String YWMC = conditions.get("YWMC");
            String CZY = conditions.get("CZY");
            String ZhuangTai = conditions.get("ZhuangTai");

            results = new ArrayList<CommonFinanceReview>();

            if (StringUtil.notEmpty(CZY)) {
                filter.put("czy", CZY);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.notEmpty(YWMC)) {
                        criteria.createAlias("cFinanceDailyBusinessVice", "cFinanceDailyBusinessVice");
                        criteria.add(Restrictions.eq("cFinanceDailyBusinessVice.zjywlx", YWMC));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || FinanceBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("step", Arrays.asList(
                                        FinanceBusinessStatus.办结.getName(),
                                        FinanceBusinessStatus.已入账.getName(),
                                        FinanceBusinessStatus.入账失败.getName(),
                                        FinanceBusinessStatus.待入账.getName(),
                                        FinanceBusinessStatus.审核不通过.getName())),
                                Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName())));
                    } else if (!FinanceBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.财务_定期.getCode().equals(type)) {

            String KHYHMC = conditions.get("KHYHMC");
            String ZHHM = conditions.get("ZHHM");
            String ZhuangTai = conditions.get("ZhuangTai");

            results = new ArrayList();

            if (StringUtil.notEmpty(KHYHMC)) {
                filter.put("khyhmc", KHYHMC);
            }
            if (StringUtil.notEmpty(ZHHM)) {
                filter.put("acct_no", ZHHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cFinanceManageFinanceExtension", "cFinanceManageFinanceExtension");

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH,  -1);
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH,  1);
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || FinanceBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cFinanceManageFinanceExtension.step", Arrays.asList(
                                        FinanceBusinessStatus.审核不通过.getName(),
                                        FinanceBusinessStatus.定期存款失败.getName(),
                                        FinanceBusinessStatus.定期存款成功.getName(),
                                        FinanceBusinessStatus.定期支取失败.getName(),
                                        FinanceBusinessStatus.定期支取成功.getName(),
                                        FinanceBusinessStatus.待定转活.getName(),
                                        FinanceBusinessStatus.待活转定.getName())),
                                Restrictions.like("cFinanceManageFinanceExtension.step", FinanceBusinessStatus.待某人审核.getName())));
                    } else if (!FinanceBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cFinanceManageFinanceExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cFinanceManageFinanceExtension.step", FinanceBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cFinanceManageFinanceExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {

            if (entity instanceof CFinanceBusinessProcess) {

                CFinanceBusinessProcess details = (CFinanceBusinessProcess) entity;

                if (ReviewSubModule.财务_日常.getCode().equals(type)) {

                    CommonFinanceReview obj = new CommonFinanceReview();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMC(details.getcFinanceDailyBusinessVice().getZjywlx());
                    obj.setZhuangTai(details.getStep());
                    obj.setCZY(details.getCzy());
                    obj.setSHSJ(DateUtil.date2Str(details.getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);
                }

            } else if (entity instanceof CFinanceActived2Fixed) {

                CFinanceActived2Fixed details = (CFinanceActived2Fixed) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getAmt() != null ? details.getAmt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setStep(details.getcFinanceManageFinanceExtension().getStep());
                    obj.setShsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getShsj(), format));
                    obj.setId(details.getId());
                    
                    results.add(obj);
                }

            } else if (entity instanceof CFinanceFixedDraw) {

                CFinanceFixedDraw details = (CFinanceFixedDraw) entity;

                if (ReviewSubModule.财务_定期.getCode().equals(type)) {

                    FixedBusinessAudit obj = new FixedBusinessAudit();

                    obj.setYwlsh(details.getYwlsh());
                    obj.setKhyhmc(details.getKhyhmc());
                    obj.setAcct_no(details.getAcct_no());
                    obj.setDeposti_amt(details.getDeposti_amt() != null ? details.getDeposti_amt().toPlainString() : null);
                    obj.setYwlx(details.getcFinanceManageFinanceExtension().getYwlx());
                    obj.setCzy(details.getcFinanceManageFinanceExtension().getCzy());
                    obj.setStep(details.getcFinanceManageFinanceExtension().getStep());
                    obj.setShsj(DateUtil.date2Str(details.getcFinanceManageFinanceExtension().getShsj(), format));
                    obj.setId(details.getId());
                    
                    results.add(obj);
                }
            }
        }
        if (results.size() == 0) {
            if (action.equals(ListAction.Next.getCode())) {
                res.setTag("L");
            }
            else if (action.equals(ListAction.Previous.getCode())) {
                res.setTag("F");
            }
        }
        res.setResults(action,results);
        return res;
    }
}
