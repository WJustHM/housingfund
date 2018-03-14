package com.handge.housingfund.review.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.allochthonous.model.*;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferFinalInterface;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.WithDrawalReason;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositReviewResRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositReviewedResRes;
import com.handge.housingfund.common.service.collection.model.individual.ListIndiAcctReviewedResRes;
import com.handge.housingfund.common.service.collection.model.individual.ListReviewIndiAcctResRes;
import com.handge.housingfund.common.service.collection.model.unit.ListUnitAcctReviewResRes;
import com.handge.housingfund.common.service.collection.model.unit.ListUnitAcctReviewedResRes;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.AuditTaskInfo;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.*;
import com.handge.housingfund.common.service.collection.service.unitdeposit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.*;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.entities.StCommonPerson;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.review.util.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Liujuhao on 2017/9/25.
 */
@SuppressWarnings("Duplicates")
@Component(value = "review.collection")
public class CollectionReview extends CommonReview {

    public static String format = "yyyy-MM-dd HH:mm";

    //个人账户业务
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO iStCollectionPersonalBusinessDetailsDAO;
    @Autowired
    private IndiAcctAlter indiAcctAlter;
    @Autowired
    private IndiAcctSet indiAcctSet;
    @Autowired
    private IndiAcctFreeze indiAcctFreeze;
    @Autowired
    private IndiAcctUnfreeze indiAcctUnFreeze;
    @Autowired
    private IndiAcctSeal indiAcctSeal;
    @Autowired
    private IndiAcctUnseal indiAcctUnseal;
    @Autowired
    private WithdrawlTasks withdrawlTasks;
    @Autowired
    @Qualifier(value = "final.out")
    private TransferFinalInterface transferFinalout;
    @Autowired
    @Qualifier(value = "final.final")
    private TransferFinalInterface transferFinalfinal;

    //单位账户业务
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO iStCollectionUnitBusinessDetailsDAO;
    @Autowired
    private UnitAcctSet unitAcctSet;
    @Autowired
    private UnitAcctAlter unitAcctAlter;
    @Autowired
    private UnitAcctSeal unitAcctSeal;
    @Autowired
    private UnitAcctUnseal unitAcctUnseal;
    @Autowired
    private UnitAcctDrop unitAcctDrop;

    //单位缴存业务
    @Autowired
    private PersonRadix personRadix;
    @Autowired
    private UnitDepositRatio unitDepositRatio;
    @Autowired
    private UnitPayhold unitPayhold;
    @Autowired
    private UnitPayWrong unitPayWrong;
    @Autowired
    private UnitRemittance unitRemittance;
    @Autowired
    private UnitPayback unitPayback;

    //状态机DAO
    @Autowired
    private ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    /**
     * 归集--未审核列表
     *
     * @param tokenContext
     * @param conditions
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

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        String CZY = conditions.get("CZY");
        String YWLX = conditions.get("YWLX");
        String XingMing = conditions.get("XingMing");
        String DWMC = conditions.get("DWMC");
        String GRZH = conditions.get("GRZH");
        String TQYY = conditions.get("TQYY");
        String ZJHM = conditions.get("ZJHM");    //证件号码
        String ZCZXMC = conditions.get("ZCZXMC");   //转出中心名称
        String ZRZXMC = conditions.get("ZRZXMC");   //转入中心名称
        String DWZH = conditions.get("DWZH");

        if (ReviewSubModule.归集_个人.getCode().equals(type)) {

            results = new ArrayList<ListReviewIndiAcctResRes>();

            ywlx_subtype.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_个人账户设立.getSubType());
            ywlx_subtype.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_个人账户信息变更.getSubType());
            ywlx_subtype.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_个人账户启封.getSubType());
            ywlx_subtype.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_个人账户封存.getSubType());
            ywlx_subtype.put(CollectionBusinessType.解冻.getCode(), BusinessSubType.归集_解冻个人账户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.冻结.getCode(), BusinessSubType.归集_冻结个人账户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.内部转移.getCode(), BusinessSubType.归集_个人账户内部转移.getSubType());
            ywlx_subtype.put(CollectionBusinessType.合并.getCode(), BusinessSubType.归集_合并个人账户.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_个人.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_个人.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionPersonalBusinessDetailsExtension.czy", CZY);
            }

            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            }

            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.createAlias("person", "person", JoinType.LEFT_OUTER_JOIN);
                    if (StringUtil.notEmpty(XingMing)) {
                        criteria.createAlias("individualAccountBasicVice", "individualAccountBasicVice", JoinType.LEFT_OUTER_JOIN);
                        criteria.add(Restrictions.or(
                                Restrictions.and(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("individualAccountBasicVice.xingMing", "%" + XingMing + "%")),
                                Restrictions.and(Restrictions.ne("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("person.xingMing", "%" + XingMing + "%"))));
                    }
                    if (StringUtil.notEmpty(ZJHM)) {
                        criteria.add(Restrictions.like("person.zjhm", "%" + ZJHM + "%"));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_单位.getCode().equals(type)) {

            results = new ArrayList<ListUnitAcctReviewResRes>();

            ywlx_subtype.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_单位开户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_单位账户启封.getSubType());
            ywlx_subtype.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_单位账户封存.getSubType());
            ywlx_subtype.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_单位账户变更.getSubType());
            ywlx_subtype.put(CollectionBusinessType.销户.getCode(), BusinessSubType.归集_单位账户销户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_单位.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_单位.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionUnitBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
            }
            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionUnitBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionUnitBusinessDetailsExtension.czmc", ywlxs);
            }
            //基于网点的验证
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (StringUtil.notEmpty(DWMC)) {
                        criteria.createAlias("unitInformationBasicVice", "unitInformationBasicVice", JoinType.LEFT_OUTER_JOIN);
                        criteria.createAlias("unit", "unit", JoinType.LEFT_OUTER_JOIN);
                        criteria.add(Restrictions.or(
                                Restrictions.and(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("unitInformationBasicVice.dwmc", "%" + DWMC + "%")),
                                Restrictions.and(Restrictions.ne("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("unit.dwmc", "%" + DWMC + "%"))));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

            results = new ArrayList<ListUnitDepositReviewResRes>();

            ywlx_subtype.put(CollectionBusinessType.汇缴.getCode(), BusinessSubType.归集_汇缴记录.getSubType());
            ywlx_subtype.put(CollectionBusinessType.补缴.getCode(), BusinessSubType.归集_补缴记录.getSubType());
            ywlx_subtype.put(CollectionBusinessType.基数调整.getCode(), BusinessSubType.归集_个人基数调整.getSubType());
            ywlx_subtype.put(CollectionBusinessType.缓缴处理.getCode(), BusinessSubType.归集_单位缓缴申请.getSubType());
            ywlx_subtype.put(CollectionBusinessType.错缴更正.getCode(), BusinessSubType.归集_错缴更正.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_缴存.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_缴存.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionUnitBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
            }
            if (StringUtil.notEmpty(DWZH)) {
                filter.put("dwzh", DWZH);
            }
            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionUnitBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionUnitBusinessDetailsExtension.czmc", ywlxs);
            }
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

            results = new ArrayList<AuditTaskInfo>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.WithDrawl,
                    ReviewSubModule.归集_提取.getName(),
                    Arrays.asList(
                            BusinessSubType.归集_提取.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.WithDrawl,
                    ReviewSubModule.归集_提取.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(TQYY) && !TQYY.equals(WithDrawalReason.REASON_0.getCode()))
                filter.put("withdrawlVice.tqyy", TQYY);
            if (StringUtil.notEmpty(DWMC)) filter.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(XingMing)) filter.put("person.xingMing", XingMing);
            if (StringUtil.notEmpty(GRZH)) filter.put("grzh", GRZH);
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("person.zjhm", ZJHM);
            }
            filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            //提取审核要求在归属网点进行
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };
        } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

            results = new ArrayList<>();

            ywlx_subtype.put(CollectionBusinessType.外部转入.getCode(), BusinessSubType.归集_转入个人接续.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_转入.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_转入.getName());

            if (ywlxs.isEmpty())
                return res;

            // TODO: 2017/12/9 添加filter和extend
            if (StringUtil.notEmpty(ZCZXMC)) {
                filter.put("allochthounousTransferVice.ZCGJJZXMC", ZCZXMC);
            }
            if (StringUtil.notEmpty(XingMing)) {
                filter.put("allochthounousTransferVice.ZGXM", XingMing);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("allochthounousTransferVice.ZJHM", ZJHM);
            }
            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                filter.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.isEmpty(ZCZXMC) && StringUtil.isEmpty(XingMing) && StringUtil.isEmpty(ZJHM)) {
                        criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

            results = new ArrayList<>();

            ywlx_subtype.put(CollectionBusinessType.外部转出.getCode(), BusinessSubType.归集_转出个人接续.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_转出.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_转出.getName());

            if (ywlxs.isEmpty())
                return res;

            // TODO: 2017/12/9 添加filter和extend
            if (StringUtil.notEmpty(ZRZXMC)) {
                filter.put("allochthounousTransferVice.ZRGJJZXMC", ZRZXMC);
            }
            if (StringUtil.notEmpty(XingMing)) {
                filter.put("allochthounousTransferVice.ZGXM", XingMing);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("allochthounousTransferVice.ZJHM", ZJHM);
            }
            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                filter.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    if (StringUtil.isEmpty(ZRZXMC) && StringUtil.isEmpty(XingMing) && StringUtil.isEmpty(ZJHM)) {
                        criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, res);

        for (Object entity : list) {

            if (entity instanceof StCollectionPersonalBusinessDetails) {

                StCollectionPersonalBusinessDetails details = (StCollectionPersonalBusinessDetails) entity;

                if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                    boolean isSpecial;
                    if (details.getExtension().getCzmc().equals(CollectionBusinessType.变更.getCode())) {
                        isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getUnit().getExtension().getKhwd());
                    } else {
                        isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    }
                    StCommonPerson commonPerson = details.getPerson();

                    ListReviewIndiAcctResRes obj = new ListReviewIndiAcctResRes();
                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setGRZH(details.getGrzh());/*此处可能GRZH为空（开户业务）*/
                    if (commonPerson == null) {
                        obj.setXingMing(details.getIndividualAccountBasicVice().getXingMing());
                        obj.setZJHM(details.getIndividualAccountBasicVice().getZjhm());
                    } else {
                        obj.setZJHM(commonPerson.getZjhm());
                        obj.setXingMing(commonPerson.getXingMing());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    if (details.getUnit() != null)
                        obj.setDWMC(details.getUnit().getDwmc());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);

                } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), BusinessSubType.归集_提取.getSubType(), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    AuditTaskInfo obj = new AuditTaskInfo();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setGRZH(details.getGrzh());
                    obj.setXingMing(details.getPerson().getXingMing());
                    obj.setZJHM(details.getPerson().getZjhm());
                    obj.setDWMC(details.getPerson().getUnit().getDwmc());
                    obj.setTQYY(details.getWithdrawlVice().getTqyy());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");    //1是0不是
                    results.add(obj);

                } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());

                    TransferIntoNotReviewedListModleResults obj = new TransferIntoNotReviewedListModleResults();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setLXHBH(details.getAllochthounousTransferVice().getLXHBH());
                    obj.setZGXM(details.getAllochthounousTransferVice().getZGXM());
                    obj.setZJHM(details.getAllochthounousTransferVice().getZJHM());
                    obj.setZCGJJZXMC(details.getAllochthounousTransferVice().getZCGJJZXMC());
                    obj.setYGZDWMC(details.getAllochthounousTransferVice().getYGZDWMC());
                    obj.setXGRZFGJJZH(details.getAllochthounousTransferVice().getXZFGJJZH());
                    obj.setXDWMC(details.getAllochthounousTransferVice().getXGZDWMC());
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    results.add(obj);

                } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    TransferOutNotReviewedListModleResults obj = new TransferOutNotReviewedListModleResults();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setLXHBH(details.getAllochthounousTransferVice().getLXHBH());
                    obj.setZRGJJZXMC(details.getAllochthounousTransferVice().getZRGJJZXMC());
                    obj.setZGXM(details.getAllochthounousTransferVice().getZGXM());
                    obj.setZJLX(details.getAllochthounousTransferVice().getZJLX());
                    obj.setZJHM(details.getAllochthounousTransferVice().getZJHM());
                    obj.setZYJE(details.getAllochthounousTransferVice().getZYJE() == null ? null : details.getAllochthounousTransferVice().getZYJE() + "");
                    obj.setYZHBJJE(details.getAllochthounousTransferVice().getYZHBJJE() == null ? null : details.getAllochthounousTransferVice().getYZHBJJE() + "");
                    obj.setBNDLX(details.getAllochthounousTransferVice().getBNDLX() == null ? null : details.getAllochthounousTransferVice().getBNDLX() + "");
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    results.add(obj);
                }
            }

            if (entity instanceof StCollectionUnitBusinessDetails) {

                StCollectionUnitBusinessDetails details = (StCollectionUnitBusinessDetails) entity;

                if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitAcctReviewResRes obj = new ListUnitAcctReviewResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    if (commonUnit == null) {
                        obj.setDWMC(details.getUnitInformationBasicVice().getDwmc());
                        obj.setDWLB(details.getUnitInformationBasicVice().getDwlb());
                    } else {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));

                    results.add(obj);

                } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitDepositReviewResRes obj = new ListUnitDepositReviewResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    if (commonUnit != null) {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWZT(details.getExtension().getStep());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /**
     * 归集--已审核列表
     *
     * @param tokenContext
     * @param conditions
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

        res.setCurrentPage(pageNo);
        res.setPageSize(pageSize);

        String CZY = conditions.get("CZY");
        String YWLX = conditions.get("YWLX");
        String XingMing = conditions.get("XingMing");
        String DWMC = conditions.get("DWMC");
        String GRZH = conditions.get("GRZH");
        String TQYY = conditions.get("TQYY");
        String ZhuangTai = conditions.get("ZhuangTai");
        String ZJHM = conditions.get("ZJHM");
        String ZCZXMC = conditions.get("ZCZXMC");
        String ZRZXMC = conditions.get("ZRZXMC");
        String DWZH = conditions.get("DWZH");

        if (ReviewSubModule.归集_个人.getCode().equals(type)) {

            results = new ArrayList<ListIndiAcctReviewedResRes>();

            if (StringUtil.notEmpty(XingMing)) {
                filter.put("person.xingMing", XingMing);
            }

            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("person.zjhm", ZJHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                                CollectionBusinessType.开户.getCode(),
                                CollectionBusinessType.变更.getCode(),
                                CollectionBusinessType.启封.getCode(),
                                CollectionBusinessType.封存.getCode(),
                                CollectionBusinessType.冻结.getCode(),
//                        CollectionBusinessType.基数调整.getCode(),
                                CollectionBusinessType.解冻.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));

                }
            };

        } else if (ReviewSubModule.归集_单位.getCode().equals(type)) {

            results = new ArrayList<ListUnitAcctReviewedResRes>();

            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc", Arrays.asList(
                                CollectionBusinessType.比例调整.getCode(),  /*从缴存模块新增的*/
                                CollectionBusinessType.开户.getCode(),
                                CollectionBusinessType.变更.getCode(),
                                CollectionBusinessType.封存.getCode(),
                                CollectionBusinessType.启封.getCode(),
                                CollectionBusinessType.销户.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.shsj", KSSJ));
                    }

                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName())),
                                Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

            results = new ArrayList<ListUnitDepositReviewedResRes>();

            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }

            if (StringUtil.notEmpty(DWZH)) {
                filter.put("dwzh", DWZH);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc",
                                Arrays.asList(
                                        CollectionBusinessType.催缴.getCode(),
                                        CollectionBusinessType.补缴.getCode(),
                                        CollectionBusinessType.汇缴.getCode(),
                                        CollectionBusinessType.基数调整.getCode(),
//                                        CollectionBusinessType.比例调整.getCode(),    /*转移至单位业务审核*/
                                        CollectionBusinessType.缓缴处理.getCode(),
                                        CollectionBusinessType.错缴更正.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName(),
                                        CollectionBusinessStatus.待入账.getName(),
                                        CollectionBusinessStatus.已入账分摊.getName(),
                                        CollectionBusinessStatus.已入账.getName())),
                                Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

            results = new ArrayList<AuditTaskInfo>();

            if (StringUtil.notEmpty(DWMC)) filter.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(XingMing)) filter.put("person.xingMing", XingMing);
            if (StringUtil.notEmpty(GRZH)) filter.put("grzh", GRZH);
            if (StringUtil.notEmpty(ZJHM)) filter.put("person.zjhm", ZJHM);
            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.notEmpty(TQYY) && !TQYY.equals(WithDrawalReason.REASON_0.getCode())) {
                        criteria.createAlias("withdrawlVice", "withdrawlVice");
                        criteria.add(Restrictions.eq("withdrawlVice.tqyy", TQYY));
                    }

                    criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                            CollectionBusinessType.部分提取.getCode(),
                            CollectionBusinessType.销户提取.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName(),
                                        CollectionBusinessStatus.待入账.getName(),
                                        CollectionBusinessStatus.已入账.getName(),
                                        CollectionBusinessStatus.入账失败.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));

                }
            };
        } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

            results = new ArrayList<>();

            if (StringUtil.notEmpty(ZCZXMC)) {
                filter.put("allochthounousTransferVice.ZCGJJZXMC", ZCZXMC);
            }
            if (StringUtil.notEmpty(XingMing)) {
                filter.put("allochthounousTransferVice.ZGXM", XingMing);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("allochthounousTransferVice.ZJHM", ZJHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.isEmpty(ZCZXMC) && StringUtil.isEmpty(XingMing) && StringUtil.isEmpty(ZJHM)) {
                        criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                    }

                    criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                            CollectionBusinessType.外部转入.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.正常办结.getName(),
                                        CollectionBusinessStatus.协商中.getName(),
                                        CollectionBusinessStatus.联系函审核通过.getName(),
                                        CollectionBusinessStatus.联系函确认接收.getName(),
                                        CollectionBusinessStatus.账户信息审核通过.getName(),
                                        CollectionBusinessStatus.转入撤销业务办结.getName(),
                                        CollectionBusinessStatus.转出审核不通过.getName(),
                                        CollectionBusinessStatus.转出失败业务办结.getName(),
                                        CollectionBusinessStatus.协商后业务办结.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

            results = new ArrayList<>();

            if (StringUtil.notEmpty(ZRZXMC)) {
                filter.put("allochthounousTransferVice.ZRGJJZXMC", ZRZXMC);
            }
            if (StringUtil.notEmpty(XingMing)) {
                filter.put("allochthounousTransferVice.ZGXM", XingMing);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                filter.put("allochthounousTransferVice.ZJHM", ZJHM);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.isEmpty(ZRZXMC) && StringUtil.isEmpty(XingMing) && StringUtil.isEmpty(ZJHM)) {
                        criteria.createAlias("allochthounousTransferVice", "allochthounousTransferVice");
                    }

                    criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                            CollectionBusinessType.外部转出.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.正常办结.getName(),
                                        CollectionBusinessStatus.协商中.getName(),
                                        CollectionBusinessStatus.转账中.getName(),
                                        CollectionBusinessStatus.联系函审核通过.getName(),
                                        CollectionBusinessStatus.联系函确认接收.getName(),
                                        CollectionBusinessStatus.账户信息审核通过.getName(),
                                        CollectionBusinessStatus.转出地已转账.getName(),
                                        CollectionBusinessStatus.转入撤销业务办结.getName(),
                                        CollectionBusinessStatus.转出审核不通过.getName(),
                                        CollectionBusinessStatus.转出失败业务办结.getName(),
                                        CollectionBusinessStatus.协商后业务办结.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, res);

        for (Object entity : list) {

            if (entity instanceof StCollectionPersonalBusinessDetails) {

                StCollectionPersonalBusinessDetails details = (StCollectionPersonalBusinessDetails) entity;

                if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                    StCommonPerson commonPerson = details.getPerson();

                    ListIndiAcctReviewedResRes obj = new ListIndiAcctReviewedResRes();
                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setGRZH(details.getGrzh());/*此处可能GRZH为空（开户业务）*/
                    if (commonPerson == null) {
                        obj.setXingMing(details.getIndividualAccountBasicVice().getXingMing());
                        obj.setZJHM(details.getIndividualAccountBasicVice().getZjhm());
                    } else {
                        obj.setXingMing(commonPerson.getXingMing());
                        obj.setZJHM(commonPerson.getZjhm());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    obj.setZhuangTai(details.getExtension().getStep());
                    if (details.getUnit() != null)
                        obj.setDWMC(details.getUnit().getDwmc());

                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    AuditTaskInfo obj = new AuditTaskInfo();

                    obj.setZJHM(details.getPerson().getZjhm());
                    obj.setZhuangTai(details.getExtension().getStep());
                    obj.setYWLSH(details.getYwlsh());
                    obj.setGRZH(details.getGrzh());
                    obj.setXingMing(details.getPerson().getXingMing());
                    obj.setDWMC(details.getPerson().getUnit().getDwmc());
                    obj.setTQYY(details.getTqyy());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

                    TransferIntoAuditedListModleResults obj = new TransferIntoAuditedListModleResults();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setLXHBH(details.getAllochthounousTransferVice().getLXHBH());
                    obj.setZGXM(details.getAllochthounousTransferVice().getZGXM());
                    obj.setZJHM(details.getAllochthounousTransferVice().getZJHM());
                    obj.setZCGJJZXMC(details.getAllochthounousTransferVice().getZCGJJZXMC());
                    obj.setZhuangTai(details.getExtension().getStep());
                    obj.setYGZDWMC(details.getAllochthounousTransferVice().getYGZDWMC());
                    obj.setXGRZFGJJZH(details.getAllochthounousTransferVice().getXZFGJJZH());
                    obj.setXDWMC(details.getAllochthounousTransferVice().getXGZDWMC());
                    obj.setLXDSCRQ(DateUtil.date2Str(details.getAllochthounousTransferVice().getLXDSCRQ(), format));
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());

                    results.add(obj);

                } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

                    TransferOutAuditedListModleResults obj = new TransferOutAuditedListModleResults();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setLXHBH(details.getAllochthounousTransferVice().getLXHBH());
                    obj.setZGXM(details.getAllochthounousTransferVice().getZGXM());
                    obj.setZJHM(details.getAllochthounousTransferVice().getZJHM());
                    obj.setZRGJJZXMC(details.getAllochthounousTransferVice().getZRGJJZXMC());
                    obj.setZhuangTai(details.getExtension().getStep());
                    obj.setYDWMC(details.getAllochthounousTransferVice().getYGZDWMC());
                    obj.setXGRZFGJJZH(details.getAllochthounousTransferVice().getXZFGJJZH()); //废弃
                    obj.setXDWMC(details.getAllochthounousTransferVice().getXGZDWMC());   //废弃
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());

                    results.add(obj);
                }
            }

            if (entity instanceof StCollectionUnitBusinessDetails) {

                StCollectionUnitBusinessDetails details = (StCollectionUnitBusinessDetails) entity;

                if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitAcctReviewedResRes obj = new ListUnitAcctReviewedResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    obj.setYWZT(details.getExtension().getStep());
                    if (commonUnit == null) {
                        obj.setDWMC(details.getUnitInformationBasicVice().getDwmc());
                        obj.setDWLB(details.getUnitInformationBasicVice().getDwlb());

                    } else {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));

                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);

                } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitDepositReviewedResRes obj = new ListUnitDepositReviewedResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    obj.setYWZT(details.getExtension().getStep());
                    if (commonUnit != null) {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());

                    results.add(obj);
                }
            }
        }
        res.setResults(results);
        return res;
    }

    /**
     * 归集--审核操作
     *
     * @param tokenContext
     * @param YWLSH
     * @param auditInfo
     * @param pass
     */
    @Override
    public void doReview(TokenContext tokenContext, String YWLSH, AuditInfo auditInfo, boolean pass, String module, String type) {

        Object entity = getEntityByModule(module, YWLSH);

        List<String> nexts = new ArrayList<>();

        if (entity instanceof StCollectionPersonalBusinessDetails) {

            StCollectionPersonalBusinessDetails obj = (StCollectionPersonalBusinessDetails) entity;

            String czmc = obj.getExtension().getCzmc();
            String step = obj.getExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            MultiReviewConfig check = NormalJsonUtils.toObj4Review(obj.getExtension().getShybh());

            if (check != null && check.getDQSHY() != null)
                if (!check.getDQSHY().equals(tokenContext.getUserId()))
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已被审核员 " + check.getDQXM() + "锁定");

            if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Collection,
                        ReviewSubModule.归集_个人.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        CollectionBusinessType.开户.getCode(),
                        CollectionBusinessType.启封.getCode(),
                        CollectionBusinessType.封存.getCode(),
                        CollectionBusinessType.内部转移.getCode(),
                        CollectionBusinessType.冻结.getCode(),
                        CollectionBusinessType.解冻.getCode(),
                        CollectionBusinessType.变更.getCode(),
//                        CollectionBusinessType.基数调整.getCode(),
                        CollectionBusinessType.合并.getCode()).contains(czmc)) {
                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                String khwd = obj.getUnit().getExtension().getKhwd();

                if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                    if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {{
                    this.setStatus(step);
                    this.setTaskId(YWLSH);
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setNote(auditInfo.getNote());
                    this.setSubtype(new HashMap<String, String>() {{

                        this.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_个人账户设立.getSubType());
                        this.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_个人账户信息变更.getSubType());
                        this.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_个人账户封存.getSubType());
                        this.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_个人账户启封.getSubType());
                        this.put(CollectionBusinessType.冻结.getCode(), BusinessSubType.归集_冻结个人账户.getSubType());
                        this.put(CollectionBusinessType.解冻.getCode(), BusinessSubType.归集_解冻个人账户.getSubType());
                        this.put(CollectionBusinessType.内部转移.getCode(), BusinessSubType.归集_个人账户内部转移.getSubType());
                        this.put(CollectionBusinessType.合并.getCode(), BusinessSubType.归集_合并个人账户.getSubType());
                    }}.get(czmc));

                    this.setType(BusinessType.Collection);
                    this.setOperator(czy);
                    this.setWorkstation(ywwd);

                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {

                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        try {
                            if (pass) {
                                if (czmc.equals(CollectionBusinessType.开户.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.开户);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctSet.doAcctSet(tokenContext, YWLSH);

                                }
                                if (czmc.equals(CollectionBusinessType.变更.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.变更);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctAlter.doAcctAlter(tokenContext, YWLSH);

                                }
                                if (czmc.equals(CollectionBusinessType.封存.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.封存);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctSeal.doAcctAction(tokenContext, YWLSH);

                                }
                                if (czmc.equals(CollectionBusinessType.启封.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.启封);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctUnseal.doAcctAction(tokenContext, YWLSH);

                                }
                                if (czmc.equals(CollectionBusinessType.冻结.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.冻结);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctFreeze.doAcctAction(tokenContext, YWLSH);

                                }
                                if (czmc.equals(CollectionBusinessType.解冻.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.解冻);
                                    if (!StringUtil.isIntoReview(next, null))
                                        indiAcctUnFreeze.doAcctAction(tokenContext, YWLSH);

                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.归集_转入.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Collection,
                        ReviewSubModule.归集_转入.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        CollectionBusinessType.外部转入.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.外部转入.getCode(), BusinessSubType.归集_转入个人接续.getSubType());
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
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        try {
                            if (pass) {
                                next = TranslateUtils.step2Status(next, CollectionBusinessType.外部转入);
                                if (!StringUtil.isIntoReview(next, null)) {
                                    // TODO: 2017/12/11 办结操作
                                    transferFinalfinal.intoFinal(tokenContext, YWLSH);
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.归集_转出.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Collection,
                        ReviewSubModule.归集_转出.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        CollectionBusinessType.外部转出.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                    if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
                        this.setSubtype(new HashMap<String, String>() {
                            {
                                this.put(CollectionBusinessType.外部转出.getCode(), BusinessSubType.归集_转出个人接续.getSubType());
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
                            throw new ErrorException(e);
                        }

                        if (!succeed || next == null) return;

                        try {
                            if (pass) {
                                next = TranslateUtils.step2Status(next, CollectionBusinessType.外部转出);
                                if (!StringUtil.isIntoReview(next, null)) {
                                    // TODO: 2017/12/11 办结操作
                                    transferFinalout.outFinal(tokenContext, YWLSH, auditInfo.getYYYJ());
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });
            }

            obj = (StCollectionPersonalBusinessDetails) getEntityByModule(module, YWLSH);

            if (nexts.size() != 1) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getExtension().getStep().equals(step))
                obj.getExtension().setStep(nexts.get(0));

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                obj.getExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), false, pass);

            obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getExtension().setShsj(new Date());

            iStCollectionPersonalBusinessDetailsDAO.update(obj);

            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);

        } else if (entity instanceof StCollectionUnitBusinessDetails) {

            StCollectionUnitBusinessDetails obj = (StCollectionUnitBusinessDetails) entity;

            String czmc = obj.getExtension().getCzmc();
            String step = obj.getExtension().getStep();
            String czy = tokenContext.getUserInfo().getCZY();
            String ywwd = tokenContext.getUserInfo().getYWWD();

            MultiReviewConfig check = NormalJsonUtils.toObj4Review(obj.getExtension().getShybh());

            if (check != null && check.getDQSHY() != null)
                if (!check.getDQSHY().equals(tokenContext.getUserId()))
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已被审核员 " + check.getDQXM() + "锁定");

            if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Collection,
                        ReviewSubModule.归集_单位.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
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

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                    {
                        this.setStatus(step);
                        this.setTaskId(YWLSH);
                        this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                        this.setNote(auditInfo.getNote());
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

                        this.setType(BusinessType.Collection);
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

                        try {
                            if (pass) {
                                if (czmc.equals(CollectionBusinessType.开户.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.开户);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitAcctSet.doUnitAcctSet(tokenContext, YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.启封.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.启封);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitAcctUnseal.doUnitAcctUnseal(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.封存.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.封存);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitAcctSeal.doUnitAcctSeal(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.变更.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.变更);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitAcctAlter.doUnitAcctAlter(tokenContext, YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.销户.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.销户);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitAcctDrop.doUnitAcctDrop(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.比例调整.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.比例调整);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitDepositRatio.doActionDepositRatio(tokenContext, YWLSH);
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });

            } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                ArrayList<String> steps = icStateMachineConfigurationDAO.getReviewSources(
                        tokenContext.getRoleList().get(0),
                        BusinessType.Collection,
                        ReviewSubModule.归集_缴存.getName(),
                        null,
                        tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                if (!steps.contains(step)) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                }

                if (czmc == null || !Arrays.asList(
                        CollectionBusinessType.汇缴.getCode(),
                        CollectionBusinessType.补缴.getCode(),
                        CollectionBusinessType.年终结息.getCode(),
                        CollectionBusinessType.基数调整.getCode(),
                        CollectionBusinessType.缓缴处理.getCode(),
//                        CollectionBusinessType.比例调整.getCode(),
//                        CollectionBusinessType.单位清册.getCode(),
                        CollectionBusinessType.错缴更正.getCode()).contains(czmc)) {

                    throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                }

                if (!tokenContext.getUserInfo().getYWWD().equals("1")) {
                    if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                    }
                }

                StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
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

                        try {
                            if (pass) {
                                if (czmc.equals(CollectionBusinessType.汇缴.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.汇缴);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitRemittance.doFinal(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.补缴.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.补缴);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitPayback.doFinal(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.基数调整.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.基数调整);
                                    if (!StringUtil.isIntoReview(next, null))
                                        personRadix.doPersonRadix(tokenContext, YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.缓缴处理.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.缓缴处理);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitPayhold.doUnitPayhold(YWLSH);
                                }
                                if (czmc.equals(CollectionBusinessType.错缴更正.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.错缴更正);
                                    if (!StringUtil.isIntoReview(next, null))
                                        unitPayWrong.doFinal(YWLSH);
                                }
                            }
                        } catch (Exception ee) {
                            throw new ErrorException(ee, "办结操作失败 ");
                        }
                        nexts.add(next);
                    }
                });
            }

            obj = (StCollectionUnitBusinessDetails) getEntityByModule(module, YWLSH);
            if (nexts.size() != 1) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getExtension().getStep().equals(step))
                obj.getExtension().setStep(nexts.get(0));

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                obj.getExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), false, pass);
            obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getExtension().setShsj(new Date());

            iStCollectionUnitBusinessDetailsDAO.update(obj);

            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);
        }
    }

    /**
     * 归集--批量操作
     *
     * @param tokenContext
     * @param YWLSHs
     * @param auditInfo
     * @param pass
     */
    @Override
    public void doBulks(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type) {

        List<String> pchList = new ArrayList();

        for (String YWLSH : YWLSHs) {

            if (ReviewSubModule.归集_提取.getCode().equals(type)) {
                List<String> TQ_YWLSHs = iStCollectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(YWLSH);
                String currentPch = iStCollectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getPch();
                if (pchList.contains(currentPch)) {
                    continue;
                }
                try {
                    doWholeReview(tokenContext, TQ_YWLSHs, auditInfo, pass, module, type);
                } catch (ErrorException e) {
                    throw e;
                }
                pchList.add(currentPch);

            } else {

                try {
                    doReview(tokenContext, YWLSH, auditInfo, pass, module, type);

                } catch (ErrorException e) {
                    e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                    throw e;
                }
            }
        }
    }

    public void doWholeReview(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type) {

        WholeReivewStatus wholeReivewStatus = validateReviewProcess(tokenContext, YWLSHs, auditInfo, pass, module, type);

        excuteFinalOperation(wholeReivewStatus, YWLSHs, tokenContext);

        alterReviewStatus(tokenContext, YWLSHs, auditInfo, pass, module, wholeReivewStatus);

    }

    public WholeReivewStatus validateReviewProcess(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, String type) {

        final int[] wholeCount = {0};

        List<String> nexts = new ArrayList<>();

        List<String> steps = new ArrayList<>();

        for (String YWLSH : YWLSHs) {

            Object entity = getEntityByModule(module, YWLSH);

            if (entity instanceof StCollectionPersonalBusinessDetails) {

                StCollectionPersonalBusinessDetails obj = (StCollectionPersonalBusinessDetails) entity;

                String czmc = obj.getExtension().getCzmc();
                String step = obj.getExtension().getStep();
                String czy = tokenContext.getUserInfo().getCZY();
                String ywwd = obj.getExtension().getYwwd().getId();

                MultiReviewConfig check = NormalJsonUtils.toObj4Review(obj.getExtension().getShybh());

                if (check != null && check.getDQSHY() != null)
                    if (!check.getDQSHY().equals(tokenContext.getUserId()))
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已被审核员 " + check.getDQXM() + "锁定");

                if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    ArrayList<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                            tokenContext.getRoleList().get(0),
                            BusinessType.WithDrawl,
                            ReviewSubModule.归集_提取.getName(),
                            null,
                            tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

                    if (!sources.contains(step)) {

                        throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务可能已被其他人审核或撤回");
                    }

                    if (czmc == null || !Arrays.asList(
                            CollectionBusinessType.部分提取.getCode(),
                            CollectionBusinessType.销户提取.getCode()).contains(czmc)) {

                        throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
                    }

                    if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                        if (!obj.getExtension().getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD())) {
                            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的受理网点和您所在网点不匹配");
                        }

                    String khwd = obj.getUnit().getExtension().getKhwd();

                    StateMachineUtils.updateState(this.iStateMachineService, pass ? Events.通过.getEvent() : Events.不通过.getEvent(), new TaskEntity() {
                        {
                            this.setStatus(step);
                            this.setTaskId(YWLSH);
                            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                            this.setNote(auditInfo.getNote());
                            this.setSubtype(new HashMap<String, String>() {
                                {
                                    this.put(CollectionBusinessType.部分提取.getCode(), BusinessSubType.归集_提取.getSubType());
                                    this.put(CollectionBusinessType.销户提取.getCode(), BusinessSubType.归集_提取.getSubType());
                                }
                            }.get(czmc));
                            this.setType(BusinessType.WithDrawl);
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


                            if (pass) {
                                if (czmc.equals(CollectionBusinessType.部分提取.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.部分提取);
                                } else if (czmc.equals(CollectionBusinessType.销户提取.getCode())) {
                                    next = TranslateUtils.step2Status(next, CollectionBusinessType.销户提取);
                                }
                                if (CollectionBusinessStatus.待入账.getName().equals(next)) {
                                    wholeCount[0] += 1;
                                }
                            }

                            nexts.add(next);
                            steps.add(step);
                        }
                    });
                }
            }
        }
        WholeReivewStatus wholeReivewStatus = new WholeReivewStatus();
        wholeReivewStatus.setCount(wholeCount[0]);
        wholeReivewStatus.setNexts(nexts);
        wholeReivewStatus.setSteps(steps);

        return wholeReivewStatus;
    }

    public void excuteFinalOperation(WholeReivewStatus wholeReivewStatus, List<String> YWLSHs, TokenContext tokenContext) {
        try {
            int wholeCount = wholeReivewStatus.getCount();
            if (wholeCount == YWLSHs.size()) {
                ArrayList<String> castYWLSHs = (ArrayList) YWLSHs;
                withdrawlTasks.doAfterPassed(castYWLSHs, tokenContext.getUserInfo().getCZY());
            }

        } catch (ErrorException e) {
            throw new ErrorException(e, "办结操作失败 ");
        }
    }

    public void alterReviewStatus(TokenContext tokenContext, List<String> YWLSHs, AuditInfo auditInfo, boolean pass, String module, WholeReivewStatus wholeReivewStatus) {

        List<String> nexts = wholeReivewStatus.getNexts();
        List<String> steps = wholeReivewStatus.getSteps();

        for (int i = 0; i < YWLSHs.size(); i++) {
            String YWLSH = YWLSHs.get(i);
            String next = nexts.get(i);
            String step = steps.get(i);
            StCollectionPersonalBusinessDetails obj = (StCollectionPersonalBusinessDetails) getEntityByModule(module, YWLSH);

            if (nexts.size() != YWLSHs.size()) throw new ErrorException("获取业务下一步状态失败");
            if (obj.getExtension().getStep().equals(step))
                obj.getExtension().setStep(next);

            ReviewInfo reviewInfo = generateReviewInfo(tokenContext, auditInfo, obj);

            if (StringUtil.isIntoReview(obj.getExtension().getStep(), null)) {
                obj.getExtension().setDdsj(new Date());
            }

            MultiReviewConfig config = generateConfig(tokenContext.getUserId(), obj.getExtension().getShybh(), false, pass);

            obj.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            obj.getExtension().setShsj(new Date());

            iStCollectionPersonalBusinessDetailsDAO.update(obj);

            iSaveAuditHistory.saveAuditHistory(YWLSH, reviewInfo);
        }
    }

    /**
     * 归集--未审核列表New
     *
     * @param tokenContext
     * @param conditions
     * @return
     */
    @Override
    public PageResNew getReviewList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        HashMap<String, String> ywlx_subtype = new HashMap<>();

        IBaseDAO.CriteriaExtension extension = null;

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String CZY = conditions.get("CZY");
        String YWLX = conditions.get("YWLX");
        String XingMing = conditions.get("XingMing");
        String DWMC = conditions.get("DWMC");
        String GRZH = conditions.get("GRZH");
        String TQYY = conditions.get("TQYY");
        String marker = conditions.get("marker");

        if (ReviewSubModule.归集_个人.getCode().equals(type)) {

            results = new ArrayList<ListReviewIndiAcctResRes>();

            ywlx_subtype.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_个人账户设立.getSubType());
            ywlx_subtype.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_个人账户信息变更.getSubType());
            ywlx_subtype.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_个人账户启封.getSubType());
            ywlx_subtype.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_个人账户封存.getSubType());
            ywlx_subtype.put(CollectionBusinessType.解冻.getCode(), BusinessSubType.归集_解冻个人账户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.冻结.getCode(), BusinessSubType.归集_冻结个人账户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.内部转移.getCode(), BusinessSubType.归集_个人账户内部转移.getSubType());
            ywlx_subtype.put(CollectionBusinessType.合并.getCode(), BusinessSubType.归集_合并个人账户.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_个人.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_个人.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionPersonalBusinessDetailsExtension.czy", CZY);
            }
            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            }

            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("unit.cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (StringUtil.notEmpty(XingMing)) {
                        criteria.createAlias("individualAccountBasicVice", "individualAccountBasicVice", JoinType.LEFT_OUTER_JOIN);
                        criteria.createAlias("person", "person", JoinType.LEFT_OUTER_JOIN);
                        criteria.add(Restrictions.or(
                                Restrictions.and(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("individualAccountBasicVice.xingMing", "%" + XingMing + "%")),
                                Restrictions.and(Restrictions.ne("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("person.xingMing", "%" + XingMing + "%"))));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_单位.getCode().equals(type)) {

            results = new ArrayList<ListUnitAcctReviewResRes>();

            ywlx_subtype.put(CollectionBusinessType.开户.getCode(), BusinessSubType.归集_单位开户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.启封.getCode(), BusinessSubType.归集_单位账户启封.getSubType());
            ywlx_subtype.put(CollectionBusinessType.封存.getCode(), BusinessSubType.归集_单位账户封存.getSubType());
            ywlx_subtype.put(CollectionBusinessType.变更.getCode(), BusinessSubType.归集_单位账户变更.getSubType());
            ywlx_subtype.put(CollectionBusinessType.销户.getCode(), BusinessSubType.归集_单位账户销户.getSubType());
            ywlx_subtype.put(CollectionBusinessType.比例调整.getCode(), BusinessSubType.归集_单位缴存比例调整.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_单位.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_单位.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionUnitBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
            }
            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionUnitBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionUnitBusinessDetailsExtension.czmc", ywlxs);
            }
            //基于网点的验证
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (StringUtil.notEmpty(DWMC)) {
                        criteria.createAlias("unitInformationBasicVice", "unitInformationBasicVice", JoinType.LEFT_OUTER_JOIN);
                        criteria.createAlias("unit", "unit", JoinType.LEFT_OUTER_JOIN);
                        criteria.add(Restrictions.or(
                                Restrictions.and(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("unitInformationBasicVice.dwmc", "%" + DWMC + "%")),
                                Restrictions.and(Restrictions.ne("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()), Restrictions.like("unit.dwmc", "%" + DWMC + "%"))));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

            results = new ArrayList<ListUnitDepositReviewResRes>();

            ywlx_subtype.put(CollectionBusinessType.汇缴.getCode(), BusinessSubType.归集_汇缴记录.getSubType());
            ywlx_subtype.put(CollectionBusinessType.补缴.getCode(), BusinessSubType.归集_补缴记录.getSubType());
            ywlx_subtype.put(CollectionBusinessType.基数调整.getCode(), BusinessSubType.归集_个人基数调整.getSubType());
            ywlx_subtype.put(CollectionBusinessType.缓缴处理.getCode(), BusinessSubType.归集_单位缓缴申请.getSubType());
            ywlx_subtype.put(CollectionBusinessType.错缴更正.getCode(), BusinessSubType.归集_错缴更正.getSubType());

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_缴存.getName(),
                    ywlx_subtype.values(),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.Collection,
                    ReviewSubModule.归集_缴存.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionUnitBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }
            if (StringUtil.notEmpty(CZY)) {
                filter.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
            }
            if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                if (ywlxs.contains(YWLX)) {
                    filter.put("cCollectionUnitBusinessDetailsExtension.czmc", YWLX);
                } else {
                    return res;
                }
            } else {
                filter.put("cCollectionUnitBusinessDetailsExtension.czmc", ywlxs);
            }
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };

        } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

            results = new ArrayList<AuditTaskInfo>();

            List<String> sources = icStateMachineConfigurationDAO.getReviewSources(
                    tokenContext.getRoleList().get(0),
                    BusinessType.WithDrawl,
                    ReviewSubModule.归集_提取.getName(),
                    Arrays.asList(
                            BusinessSubType.归集_提取.getSubType()
                    ),
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD());

            if (sources == null || sources.isEmpty())
                return res;

            ArrayList<String> ywlxs = getYWLX(
                    tokenContext.getUserInfo().getYWWD().equals("1") ? null : tokenContext.getUserInfo().getYWWD(),
                    tokenContext.getRoleList().get(0),
                    BusinessType.WithDrawl,
                    ReviewSubModule.归集_提取.getName());

            if (ywlxs.isEmpty())
                return res;

            filter.put("cCollectionPersonalBusinessDetailsExtension.step", sources);
            if (StringUtil.notEmpty(TQYY) && !TQYY.equals(WithDrawalReason.REASON_0.getCode()))
                filter.put("withdrawlVice.tqyy", TQYY);
            if (StringUtil.notEmpty(DWMC)) filter.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(XingMing)) filter.put("person.xingMing", XingMing);
            if (StringUtil.notEmpty(GRZH)) filter.put("grzh", GRZH);
            filter.put("cCollectionPersonalBusinessDetailsExtension.czmc", ywlxs);
            //提取审核要求在归属网点进行
            if (!tokenContext.getUserInfo().getYWWD().equals("1"))
                filter.put("unit.cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD());
            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.ddsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.ddsj", JSSJ));
                    }
                }
            };
        }

        List list = getEntitiesByModule(false, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {

            if (entity instanceof StCollectionPersonalBusinessDetails) {

                StCollectionPersonalBusinessDetails details = (StCollectionPersonalBusinessDetails) entity;

                if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                    boolean isSpecial;
                    if (details.getExtension().getCzmc().equals(CollectionBusinessType.变更.getCode())) {
                        isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getUnit().getExtension().getKhwd());
                    } else {
                        isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    }
                    StCommonPerson commonPerson = details.getPerson();

                    ListReviewIndiAcctResRes obj = new ListReviewIndiAcctResRes();
                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setGRZH(details.getGrzh());/*此处可能GRZH为空（开户业务）*/
                    if (commonPerson == null) {
                        obj.setXingMing(details.getIndividualAccountBasicVice().getXingMing());
                        obj.setZJHM(details.getIndividualAccountBasicVice().getZjhm());
                    } else {
                        obj.setZJHM(commonPerson.getZjhm());
                        obj.setXingMing(commonPerson.getXingMing());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    if (details.getUnit() != null)
                        obj.setDWMC(details.getUnit().getDwmc());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), BusinessSubType.归集_提取.getSubType(), tokenContext.getRoleList().get(0), details.getUnit().getExtension().getKhwd());
                    AuditTaskInfo obj = new AuditTaskInfo();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setGRZH(details.getGrzh());
                    obj.setXingMing(details.getPerson().getXingMing());
                    obj.setDWMC(details.getPerson().getUnit().getDwmc());
                    obj.setTQYY(details.getWithdrawlVice().getTqyy());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setSFTS(isSpecial ? "1" : "0");    //1是0不是
                    obj.setId(details.getId());

                    results.add(obj);

                }
            }

            if (entity instanceof StCollectionUnitBusinessDetails) {

                StCollectionUnitBusinessDetails details = (StCollectionUnitBusinessDetails) entity;

                if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());
                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitAcctReviewResRes obj = new ListUnitAcctReviewResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    if (commonUnit == null) {
                        obj.setDWMC(details.getUnitInformationBasicVice().getDwmc());
                        obj.setDWLB(details.getUnitInformationBasicVice().getDwlb());
                    } else {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setSFTS(isSpecial ? "1" : "0");
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                    boolean isSpecial = icStateMachineConfigurationDAO.isSpecialReview(details.getExtension().getStep(), ywlx_subtype.get(details.getExtension().getCzmc()), tokenContext.getRoleList().get(0), details.getExtension().getYwwd().getId());

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitDepositReviewResRes obj = new ListUnitDepositReviewResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    if (commonUnit != null) {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWZT(details.getExtension().getStep());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    MultiReviewConfig shyxx = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    if (shyxx != null) {
                        obj.setDQSHY(shyxx.getDQSHY());
                        obj.setDQXM(shyxx.getDQXM());
                    }
                    obj.setDDSJ(DateUtil.date2Str(details.getExtension().getDdsj(), format));
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
     * 归集--已审核列表New
     *
     * @param tokenContext
     * @param conditions
     * @return
     */
    @Override
    public PageResNew getReviewedList(TokenContext tokenContext, HashMap<String, String> conditions, String action, String module, String type) {

        PageResNew res = new PageResNew<>();

        ArrayList results = null;

        HashMap<String, Object> filter = new HashMap();

        IBaseDAO.CriteriaExtension extension = null;

        int pageSize = StringUtil.isEmpty(conditions.get("pageSize")) ? 30 : Integer.parseInt(conditions.get("pageSize"));

        Date KSSJ = DateUtil.safeStr2Date(format, conditions.get("KSSJ"));

        Date JSSJ = DateUtil.safeStr2Date(format, conditions.get("JSSJ"));

        String CZY = conditions.get("CZY");
        String YWLX = conditions.get("YWLX");
        String XingMing = conditions.get("XingMing");
        String DWMC = conditions.get("DWMC");
        String GRZH = conditions.get("GRZH");
        String TQYY = conditions.get("TQYY");
        String ZhuangTai = conditions.get("ZhuangTai");
        String marker = conditions.get("marker");

        if (ReviewSubModule.归集_个人.getCode().equals(type)) {

            results = new ArrayList<ListIndiAcctReviewedResRes>();

            if (StringUtil.notEmpty(XingMing)) {
                filter.put("person.xingMing", XingMing);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                                CollectionBusinessType.开户.getCode(),
                                CollectionBusinessType.变更.getCode(),
                                CollectionBusinessType.启封.getCode(),
                                CollectionBusinessType.封存.getCode(),
                                CollectionBusinessType.冻结.getCode(),
//                        CollectionBusinessType.基数调整.getCode(),
                                CollectionBusinessType.解冻.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));

                }
            };

        } else if (ReviewSubModule.归集_单位.getCode().equals(type)) {

            results = new ArrayList<ListUnitAcctReviewedResRes>();

            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc", Arrays.asList(
                                CollectionBusinessType.比例调整.getCode(),  /*从缴存模块新增的*/
                                CollectionBusinessType.开户.getCode(),
                                CollectionBusinessType.变更.getCode(),
                                CollectionBusinessType.封存.getCode(),
                                CollectionBusinessType.启封.getCode(),
                                CollectionBusinessType.销户.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.shsj", KSSJ));
                    }

                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName())),
                                Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

            results = new ArrayList<ListUnitDepositReviewedResRes>();

            if (StringUtil.notEmpty(DWMC)) {
                filter.put("unit.dwmc", DWMC);
            }

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");

                    if (StringUtil.notEmpty(CZY)) {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + CZY + "%"));
                    }

                    if (StringUtil.notEmpty(YWLX) && !YWLX.equals(CollectionBusinessType.所有.getCode())) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", YWLX));
                    } else {
                        criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc",
                                Arrays.asList(
                                        CollectionBusinessType.催缴.getCode(),
                                        CollectionBusinessType.补缴.getCode(),
                                        CollectionBusinessType.汇缴.getCode(),
                                        CollectionBusinessType.基数调整.getCode(),
//                                        CollectionBusinessType.比例调整.getCode(),    /*转移至单位业务审核*/
                                        CollectionBusinessType.缓缴处理.getCode(),
                                        CollectionBusinessType.错缴更正.getCode())));
                    }

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionUnitBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionUnitBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName(),
                                        CollectionBusinessStatus.待入账.getName(),
                                        CollectionBusinessStatus.已入账分摊.getName(),
                                        CollectionBusinessStatus.已入账.getName())),
                                Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));
                }
            };

        } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

            results = new ArrayList<AuditTaskInfo>();

            if (StringUtil.notEmpty(DWMC)) filter.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(XingMing)) filter.put("person.xingMing", XingMing);
            if (StringUtil.notEmpty(GRZH)) filter.put("grzh", GRZH);

            extension = new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

                    if (StringUtil.notEmpty(TQYY) && !TQYY.equals(WithDrawalReason.REASON_0.getCode())) {
                        criteria.createAlias("withdrawlVice", "withdrawlVice");
                        criteria.add(Restrictions.eq("withdrawlVice.tqyy", TQYY));
                    }

                    criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(
                            CollectionBusinessType.部分提取.getCode(),
                            CollectionBusinessType.销户提取.getCode())));

                    if (KSSJ != null) {
                        Calendar leftdate = Calendar.getInstance();
                        leftdate.setTime(KSSJ);
                        leftdate.add(Calendar.MONTH, -1);
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", leftdate.getTime()));
                        criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.shsj", KSSJ));
                    }
                    if (JSSJ != null) {
                        Calendar rightdate = Calendar.getInstance();
                        rightdate.setTime(JSSJ);
                        rightdate.add(Calendar.MONTH, 1);
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.created_at", rightdate.getTime()));
                        criteria.add(Restrictions.le("cCollectionPersonalBusinessDetailsExtension.shsj", JSSJ));
                    }

                    if (StringUtil.isEmpty(ZhuangTai) || CollectionBusinessStatus.所有.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.or(
                                Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                                        CollectionBusinessStatus.审核不通过.getName(),
                                        CollectionBusinessStatus.办结.getName(),
                                        CollectionBusinessStatus.待入账.getName(),
                                        CollectionBusinessStatus.已入账.getName(),
                                        CollectionBusinessStatus.入账失败.getName())),
                                Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName())));
                    } else if (!CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                        criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                    } else {
                        criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.待某人审核.getName()));
                    }

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.shybh", "%[%\"" + tokenContext.getUserId() + "\"%]%"));

//                    criteria.add(Restrictions.sqlRestriction("locate( "+ "\"" + tokenContext.getUserId() + "\"" +  ", cCollectionPersonalBusinessDetailsExtension.shybh) > 0"));
                }
            };
        }

        List list = getEntitiesByModule(true, module, filter, extension, pageSize, marker, action);

        for (Object entity : list) {

            if (entity instanceof StCollectionPersonalBusinessDetails) {

                StCollectionPersonalBusinessDetails details = (StCollectionPersonalBusinessDetails) entity;

                if (ReviewSubModule.归集_个人.getCode().equals(type)) {

                    StCommonPerson commonPerson = details.getPerson();

                    ListIndiAcctReviewedResRes obj = new ListIndiAcctReviewedResRes();
                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setGRZH(details.getGrzh());/*此处可能GRZH为空（开户业务）*/
                    if (commonPerson == null) {
                        obj.setXingMing(details.getIndividualAccountBasicVice().getXingMing());
                    } else {
                        obj.setXingMing(commonPerson.getXingMing());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    obj.setZhuangTai(details.getExtension().getStep());
                    if (details.getUnit() != null)
                        obj.setDWMC(details.getUnit().getDwmc());

                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.归集_提取.getCode().equals(type)) {

                    AuditTaskInfo obj = new AuditTaskInfo();

                    obj.setZhuangTai(details.getExtension().getStep());
                    obj.setYWLSH(details.getYwlsh());
                    obj.setGRZH(details.getGrzh());
                    obj.setXingMing(details.getPerson().getXingMing());
                    obj.setDWMC(details.getPerson().getUnit().getDwmc());
                    obj.setTQYY(details.getTqyy());
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                }
            }

            if (entity instanceof StCollectionUnitBusinessDetails) {

                StCollectionUnitBusinessDetails details = (StCollectionUnitBusinessDetails) entity;

                if (ReviewSubModule.归集_单位.getCode().equals(type)) {

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitAcctReviewedResRes obj = new ListUnitAcctReviewedResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    obj.setYWZT(details.getExtension().getStep());
                    if (commonUnit == null) {
                        obj.setDWMC(details.getUnitInformationBasicVice().getDwmc());
                        obj.setDWLB(details.getUnitInformationBasicVice().getDwlb());

                    } else {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));

                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
                    obj.setSCSHY(config.getSCSHY());
                    obj.setId(details.getId());

                    results.add(obj);

                } else if (ReviewSubModule.归集_缴存.getCode().equals(type)) {

                    StCommonUnit commonUnit = details.getUnit();

                    ListUnitDepositReviewedResRes obj = new ListUnitDepositReviewedResRes();

                    obj.setYWLSH(details.getYwlsh());
                    obj.setYWMXLX(details.getExtension().getCzmc());
                    obj.setDWZH(details.getDwzh());
                    obj.setYWZT(details.getExtension().getStep());
                    if (commonUnit != null) {
                        obj.setDWMC(commonUnit.getDwmc());
                        obj.setDWLB(commonUnit.getExtension().getDwlb());
                    }
                    obj.setCZY(details.getExtension().getCzy());
                    obj.setYWWD(details.getExtension().getYwwd().getMingCheng());
                    obj.setSLSJ(DateUtil.date2Str(details.getExtension().getShsj(), format));
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(details.getExtension().getShybh());
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

    class WholeReivewStatus {
        int count = 0;
        List<String> steps;
        List<String> nexts;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<String> getSteps() {
            return steps;
        }

        public void setSteps(List<String> steps) {
            this.steps = steps;
        }

        public List<String> getNexts() {
            return nexts;
        }

        public void setNexts(List<String> nexts) {
            this.nexts = nexts;
        }
    }
}
