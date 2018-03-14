package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctsInfo;
import com.handge.housingfund.common.service.loan.IApplyLoanService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.ForeignLoanProof;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/*
 * Created by Liujuhao on 2017/7/1.
 */

@SuppressWarnings({"SpringAutowiredFieldsWarningInspection", "Convert2Lambda", "Anonymous2MethodRef", "Duplicates", "unchecked", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class IndiAcctsInfoImpl implements IndiAcctsInfo {


    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private ICBankBankInfoDAO bankBankInfoDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private IApplyLoanService applyLoanService;

    private static String format = "yyyy-MM-dd";
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    private static String formatNY = "yyyy-MM";


    // completed 已测试 - 正常
    @Override
    public PageRes<ListIndiAcctsResRes> getAcctsInfo(TokenContext tokenContext, final String DWMC, final String GRZH, final String XingMing, final String ZJHM, final String GRZHZT, String YWWD, String SFDJ, String startTime, String endTime, String page, String pagesize) {

        //region //参数检查
        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pagesize != null) {
                pagesize_number = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCommonPerson> list_commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) {
                this.put("unit.dwmc", DWMC);
            }

            if (StringUtil.notEmpty(GRZH)) {
                this.put("grzh", GRZH);
            }

            if (StringUtil.notEmpty(XingMing)) {
                this.put("xingMing", XingMing);
            }

            if (StringUtil.notEmpty(ZJHM)) {
                this.put("zjhm", ZJHM);
            }

            if (StringUtil.notEmpty(SFDJ) && !"00".equals(SFDJ)) {
                this.put("cCommonPersonExtension.sfdj", SFDJ);
            }

            if (StringUtil.notEmpty(GRZHZT) && !GRZHZT.equals(PersonAccountStatus.所有.getCode())) {
                this.put("collectionPersonalAccount.grzhzt", GRZHZT);
            }


        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if (StringUtil.notEmpty(YWWD)) {

                    if (!StringUtil.notEmpty(DWMC)) {

                        criteria.createAlias("unit", "unit");
                    }

                    criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");

                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd", YWWD));
                }

                if (DateUtil.safeStr2Date(formatNYRSF, startTime) != null) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, startTime)));
                }

                if (DateUtil.safeStr2Date(formatNYRSF, endTime) != null) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, endTime)));
                }

            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CAccountNetwork> list_network = list_commonPerson.size() == 0 ? new ArrayList<>() : DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{


            this.put("id", CollectionUtils.flatmap(list_commonPerson, new CollectionUtils.Transformer<StCommonPerson, String>() {
                @Override
                public String tansform(StCommonPerson var1) {
                    return var1.getUnit().getExtension().getKhwd();
                }

            }));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });
//        List<StCollectionPersonalBusinessDetails> list_collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{
//
//
//            this.put("grzh", CollectionUtils.flatmap(list_commonPerson, new CollectionUtils.Transformer<StCommonPerson, String>() {
//                @Override
//                public String tansform(StCommonPerson var1) { return var1.getGrzh(); }
//            }));
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());
//
//        }}).getList(new DAOBuilder.ErrorHandler() {
//
//            @Override
//            public void error(Exception e) { throw new ErrorException(e);}
//
//        });
        //endregion

        return new PageRes<ListIndiAcctsResRes>() {{

            this.setResults(new ArrayList<ListIndiAcctsResRes>() {{

                for (StCommonPerson commonPerson : list_commonPerson) {

                    this.add(new ListIndiAcctsResRes() {{

                        this.setId(commonPerson.getId());
                        this.setXingMing(commonPerson.getXingMing());

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRJCJS(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrjcjs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (commonPerson.getExtension() != null) {

                            this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));
                        }

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRZHYE(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrzhye().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        }

//                        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils.find(list_collectionPersonalBusinessDetails, new CollectionUtils.Predicate<StCollectionPersonalBusinessDetails>() {
//                            @Override
//                            public boolean evaluate(StCollectionPersonalBusinessDetails var1) {
//                                return commonPerson.getGrzh()!=null&&commonPerson.getGrzh().equals(var1.getGrzh());
//                            }
//                        });

//                        if (collectionPersonalBusinessDetails!=null&&collectionPersonalBusinessDetails.getExtension()!=null) {

                        this.setSLSJ(DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), formatNYRSF));
//                        }

                        this.setZJHM(commonPerson.getZjhm());

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());

                            if (commonPerson.getCollectionPersonalAccount().getDwyjce() != null && commonPerson.getCollectionPersonalAccount().getGryjce() != null) {

                                this.setYJCE(String.valueOf(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce())
                                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                            }
                        }

                        if (commonPerson.getUnit() != null) {

                            this.setDWMC(commonPerson.getUnit().getDwmc());
                        }
                        this.setGRZH(commonPerson.getGrzh());

                        this.setSFDJ(commonPerson.getExtension().getSfdj());

                        CAccountNetwork network = CollectionUtils.find(list_network, new CollectionUtils.Predicate<CAccountNetwork>() {
                            @Override
                            public boolean evaluate(CAccountNetwork var1) {

                                return var1.getId() != null && var1.getId().equals(commonPerson.getUnit().getExtension().getKhwd());
                            }
                        });
                        this.setYWWD(network==null?"":network.getMingCheng());
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
    public PageResNew<ListIndiAcctsResRes> getAcctsInfo(TokenContext tokenContext, String DWMC, String GRZH, String XingMing, String ZJHM, String GRZHZT, String YWWD, String SFDJ, String startTime, String endTime, String marker, String action, String pagesize) {
        //region //参数检查

        int pagesize_number = 0;

        try {

            if (pagesize != null) { pagesize_number = Integer.parseInt(pagesize); }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCommonPerson> list_commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) {
                this.put("unit.dwmc", DWMC);
            }

            if (StringUtil.notEmpty(GRZH)) {
                this.put("grzh", GRZH);
            }

            if (StringUtil.notEmpty(XingMing)) {
                this.put("xingMing", XingMing);
            }

            if (StringUtil.notEmpty(ZJHM)) {
                this.put("zjhm", ZJHM);
            }

            if (StringUtil.notEmpty(SFDJ) && !"00".equals(SFDJ)) {
                this.put("cCommonPersonExtension.sfdj", SFDJ);
            }

            if (StringUtil.notEmpty(GRZHZT) && !GRZHZT.equals(PersonAccountStatus.所有.getCode())) {
                this.put("collectionPersonalAccount.grzhzt", GRZHZT);
            }


        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if (StringUtil.notEmpty(YWWD)) {

                    if (!StringUtil.notEmpty(DWMC)) {

                        criteria.createAlias("unit", "unit");
                    }

                    criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");

                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd", YWWD));
                }

                if (DateUtil.safeStr2Date(formatNYRSF, startTime) != null) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, startTime)));
                }

                if (DateUtil.safeStr2Date(formatNYRSF, endTime) != null) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, endTime)));
                }

            }
        }).pageOption(marker,action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CAccountNetwork> list_network = list_commonPerson.size() == 0 ? new ArrayList<>() : DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{


            this.put("id", CollectionUtils.flatmap(list_commonPerson, new CollectionUtils.Transformer<StCommonPerson, String>() {
                @Override
                public String tansform(StCommonPerson var1) {
                    return var1.getUnit().getExtension().getKhwd();
                }

            }));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });
//        List<StCollectionPersonalBusinessDetails> list_collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{
//
//
//            this.put("grzh", CollectionUtils.flatmap(list_commonPerson, new CollectionUtils.Transformer<StCommonPerson, String>() {
//                @Override
//                public String tansform(StCommonPerson var1) { return var1.getGrzh(); }
//            }));
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());
//
//        }}).getList(new DAOBuilder.ErrorHandler() {
//
//            @Override
//            public void error(Exception e) { throw new ErrorException(e);}
//
//        });
        //endregion

        return new PageResNew<ListIndiAcctsResRes>() {{

            this.setResults(action,new ArrayList<ListIndiAcctsResRes>() {{

                for (StCommonPerson commonPerson : list_commonPerson) {

                    this.add(new ListIndiAcctsResRes() {{

                        this.setId(commonPerson.getId());
                        this.setXingMing(commonPerson.getXingMing());

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRJCJS(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrjcjs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (commonPerson.getExtension() != null) {

                            this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));
                        }

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRZHYE(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrzhye().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        }

//                        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils.find(list_collectionPersonalBusinessDetails, new CollectionUtils.Predicate<StCollectionPersonalBusinessDetails>() {
//                            @Override
//                            public boolean evaluate(StCollectionPersonalBusinessDetails var1) {
//                                return commonPerson.getGrzh()!=null&&commonPerson.getGrzh().equals(var1.getGrzh());
//                            }
//                        });

//                        if (collectionPersonalBusinessDetails!=null&&collectionPersonalBusinessDetails.getExtension()!=null) {

                        this.setSLSJ(DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), formatNYRSF));
//                        }

                        this.setZJHM(commonPerson.getZjhm());

                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());

                            if (commonPerson.getCollectionPersonalAccount().getDwyjce() != null && commonPerson.getCollectionPersonalAccount().getGryjce() != null) {

                                this.setYJCE(String.valueOf(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce())
                                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                            }
                        }

                        if (commonPerson.getUnit() != null) {

                            this.setDWMC(commonPerson.getUnit().getDwmc());
                        }
                        this.setGRZH(commonPerson.getGrzh());

                        this.setSFDJ(commonPerson.getExtension().getSfdj());

                        CAccountNetwork network = CollectionUtils.find(list_network, new CollectionUtils.Predicate<CAccountNetwork>() {
                            @Override
                            public boolean evaluate(CAccountNetwork var1) {

                                return var1.getId() != null && var1.getId().equals(commonPerson.getUnit().getExtension().getKhwd());
                            }
                        });
                        this.setYWWD(network==null?"":network.getMingCheng());
                    }});
                }
            }});


        }};
    }

    public GetAcctsInfoDetailsRes getAcctsInfoDetails(TokenContext tokenContext, final String GRZH) {


        //region //参数检查

        if (!StringUtil.notEmpty(GRZH)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "个人账号");
        }

        //endregion

        //region //必要字段查询&完整性验证
        StCommonPerson commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", GRZH);

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (commonPerson == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
        }

//        List<StCommonPerson> list_commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
//
//            this.put("grzh", GRZH);
//
//        }}).getList(new DAOBuilder.ErrorHandler() {
//
//            @Override
//            public void error(Exception e) { throw new ErrorException(e); }
//        });

//        List<StCollectionPersonalBusinessDetails> list_business = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
//
//            if (StringUtil.notEmpty(GRZH)) { this.put("grzh", GRZH); }
//
//
//        }}).extension(new IBaseDAO.CriteriaExtension() {
//            @Override
//            public void extend(Criteria criteria) {
//
//                criteria.add(Restrictions.eq("gjhtqywlx", CollectionBusinessType.内部转移.getCode()));
//                criteria.createAlias("individualAccountTransferNewVice","individualAccountTransferNewVice");
//                criteria.createAlias("individualAccountTransferNewVice.zrdw","zrdw");
//                criteria.add(Restrictions.ne("individualAccountTransferNewVice.zrdw.dwzh", commonPerson.getUnit().getDwzh()));
//            }
//        }).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
//
//            @Override
//            public void error(Exceptionzzzzzzzzzzzz e) { throw new ErrorException(e); }
//        });


        List<StCollectionPersonalBusinessDetails> list_business_withdrawl = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode()));

            this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.已入账.getName());

            this.put("grzh", commonPerson.getGrzh());


        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });


        List<CLoanHousingPersonInformationBasic> list_loanHousingPersonInformationBasic = instance(this.loanHousingPersonInformationBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("coborrower","coborrower", JoinType.LEFT_OUTER_JOIN);

                criteria.add(Restrictions.or(
                        Restrictions.and(
                                Restrictions.isNull("coborrower"),
                                Restrictions.and(
                                        Restrictions.eq("jkrxm",commonPerson.getXingMing()),Restrictions.eq("jkrzjhm",commonPerson.getZjhm())
                                )
                        ),

                        Restrictions.and(
                                Restrictions.isNotNull("coborrower"),
                                Restrictions.or(
                                        Restrictions.and(Restrictions.eq("jkrxm",commonPerson.getXingMing()),Restrictions.eq("jkrzjhm",commonPerson.getZjhm())),
                                        Restrictions.and(Restrictions.eq("coborrower.gtjkrxm",commonPerson.getXingMing()),Restrictions.eq("coborrower.gtjkrzjhm",commonPerson.getZjhm()))
                                )
                        )
                ));
                criteria.add(Restrictions.isNotNull("loanContract"));
                criteria.add(Restrictions.isNotNull("personalAccount"));
                criteria.add(Restrictions.ne("dkzhzt",LoanAccountType.已作废.getCode()));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });
        //endregion

        return new GetAcctsInfoDetailsRes() {
            {
                GetIndiAcctsInfoDetailsResJBXX jbxxObjct = new GetIndiAcctsInfoDetailsResJBXX();
                jbxxObjct.setGRZH(GRZH);
                jbxxObjct.setSFDJ(commonPerson.getExtension().getSfdj());   //新增“是否冻结”1/0
                jbxxObjct.setXingMing(commonPerson.getXingMing());
                jbxxObjct.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getCsny(), formatNY));
                jbxxObjct.setZJLX(commonPerson.getZjlx());
                jbxxObjct.setZJHM(commonPerson.getZjhm());
                jbxxObjct.setSJHM(commonPerson.getSjhm());

                if (commonPerson.getExtension() != null) {

                    jbxxObjct.setYouXiang(commonPerson.getExtension().getDzyx());
                }

                if (commonPerson.getCollectionPersonalAccount() != null) {

                    jbxxObjct.setGRCKZHKHYHMC(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhmc());

                    jbxxObjct.setGRCKZHHM(commonPerson.getCollectionPersonalAccount().getGrckzhhm());
                }
                jbxxObjct.setXMQP(commonPerson.getXmqp());

                this.setJBXX(jbxxObjct);

                ArrayList<GetIndiAcctsInfoDetailsResJCXX> jcxxArrayList = new ArrayList();


                if (commonPerson.getUnit() != null) {
                    GetIndiAcctsInfoDetailsResJCXX resJCXX = new GetIndiAcctsInfoDetailsResJCXX();
                    resJCXX.setDWMC(commonPerson.getUnit().getDwmc());

                    resJCXX.setDWZH(commonPerson.getUnit().getDwzh());


                    if (commonPerson.getCollectionPersonalAccount() != null) {

                        resJCXX.setGRJCJS("" + commonPerson.getCollectionPersonalAccount().getGrjcjs());

                        resJCXX.setGRJCBL("" + (commonPerson.getUnit().getCollectionUnitAccount().getGrjcbl() == null ? 0 : commonPerson.getUnit().getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100"))));

                        resJCXX.setDWJCBL("" + (commonPerson.getUnit().getCollectionUnitAccount().getDwjcbl() == null ? 0 : commonPerson.getUnit().getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))));

                        if (commonPerson.getCollectionPersonalAccount().getGryjce() != null) {


                            if (commonPerson.getUnit() != null && commonPerson.getUnit().getCollectionUnitAccount() != null) {

                                if (commonPerson.getCollectionPersonalAccount().getDwyjce() != null && commonPerson.getCollectionPersonalAccount().getGryjce() != null) {

                                    resJCXX.setDWYJCE("" + commonPerson.getCollectionPersonalAccount().getDwyjce());

                                    resJCXX.setGRYJCE("" + commonPerson.getCollectionPersonalAccount().getGryjce());

                                    resJCXX.setYJCE("" + commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce()));

                                }

                                resJCXX.setDWZHZT(commonPerson.getUnit().getCollectionUnitAccount().getDwzhzt());

                                resJCXX.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));

                            }
                        }

                        jcxxArrayList.add(resJCXX); }

            }
//            for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {
//
//                if (collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice()!= null) {
//                        GetIndiAcctsInfoDetailsResJCXX resJCXX = new GetIndiAcctsInfoDetailsResJCXX();
//                        resJCXX.setDWMC(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwmc());
//                        resJCXX.setDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwzh());
//                        resJCXX.setGRJCJS(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgrjcjs());
//                        resJCXX.setGRJCBL(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgryjce()).divide(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgrjcjs()),2,BigDecimal.ROUND_HALF_UP)+"");
//                        resJCXX.setDWJCBL(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYdwyjce()).divide(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgrjcjs()),2,BigDecimal.ROUND_HALF_UP)+"");
//                        resJCXX.setDWYJCE(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYdwyjce());
//                        resJCXX.setGRYJCE(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgryjce());
//                        resJCXX.setYJCE(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYdwyjce()).add(StringUtil.safeBigDecimal(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getYgryjce())).setScale(2,BigDecimal.ROUND_HALF_UP)+"");
//                        resJCXX.setDWZHZT(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getCollectionUnitAccount().getDwzhzt());
//                        resJCXX.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));
//                        jcxxArrayList.add(resJCXX);
//                }
//            }

            this.setJCXX(jcxxArrayList);
            BigDecimal LJTQJE = new BigDecimal(0);
            ArrayList<GetIndiAcctsInfoDetailsResTQXX> tqxxesarrayList = new ArrayList();
            for (StCollectionPersonalBusinessDetails business_withdrawl_part : list_business_withdrawl) {
                {
                    GetIndiAcctsInfoDetailsResTQXX resTQXX = new GetIndiAcctsInfoDetailsResTQXX();

                    if (business_withdrawl_part.getFse() != null) {

                        LJTQJE = LJTQJE.add(business_withdrawl_part.getFse());
                    }
                    CCollectionPersonalBusinessDetailsExtension extension = business_withdrawl_part.getExtension();
                    if (extension != null) {

                        resTQXX.setTQSJ(DateUtil.date2Str(extension.getSlsj(), formatNYRSF));
                    }
                    resTQXX.setBCTQJE(""+business_withdrawl_part.getFse().abs());
                    resTQXX.setLJTQJE(""+LJTQJE.abs());
                    resTQXX.setBLR(extension.getBlr());
                    resTQXX.setCZY(extension.getCzy());
                    resTQXX.setYWWD(extension.getYwwd().getMingCheng());
                    tqxxesarrayList.add(resTQXX);
                }
            }
            this.setTQXX(tqxxesarrayList);
            //贷款信息暂无
            this.setDKXX(new ArrayList<GetIndiAcctsInfoDetailsResDKXX>() {{

                for (CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic : list_loanHousingPersonInformationBasic) {

                    this.add(new GetIndiAcctsInfoDetailsResDKXX() {{
                        this.setJKHTBH(loanHousingPersonInformationBasic.getLoanContract().getJkhtbh()/*借款合同编号*/);
                        this.setDKZH(loanHousingPersonInformationBasic.getDkzh()/*贷款账号*/);
                        this.setHTDKJ(loanHousingPersonInformationBasic.getLoanContract().getHtdkje() + ""/*合同贷款金（元）*/);
                        this.setDKQS(loanHousingPersonInformationBasic.getPersonalAccount().getDkqs() + ""/*贷款期数*/);
                        this.setDKYE(loanHousingPersonInformationBasic.getPersonalAccount().getDkye() + ""/*贷款余额（元）*/);
                        this.setSYQS(loanHousingPersonInformationBasic.getPersonalAccount().getDkqs().subtract(loanHousingPersonInformationBasic.getYhqs()) + ""/*剩余期数*/);
                        this.setHSLXZE(loanHousingPersonInformationBasic.getPersonalAccount().getHslxze() + ""/*回收利息总额（元）*/);
                        this.setYQBJ((loanHousingPersonInformationBasic.getPersonalAccount().getYqbjze() == null ? "0.00" : (loanHousingPersonInformationBasic.getPersonalAccount().getYqbjze() + "")/*逾期本金（元）*/));
                        this.setYQLX(loanHousingPersonInformationBasic.getPersonalAccount().getYqlxze() + ""/*逾期利息（元）*/);
                        this.setYQFX(loanHousingPersonInformationBasic.getPersonalAccount().getFxze() + ""/*逾期罚息（元）*/);
                        this.setGRZHZT(loanHousingPersonInformationBasic.getDkzhzt()/*个人账户状态*/);
                        this.setHKRQ(loanHousingPersonInformationBasic.getLoanContract() == null ? null : loanHousingPersonInformationBasic.getLoanContract().getYdhkr()/*还款日期*/);
                        this.setXingMing(loanHousingPersonInformationBasic.getJkrxm());
                        this.setGTJKRXM(loanHousingPersonInformationBasic.getCoborrower() == null? null:loanHousingPersonInformationBasic.getCoborrower().getGtjkrxm());
                        this.setGTJKRZJHM(loanHousingPersonInformationBasic.getCoborrower() == null? null:loanHousingPersonInformationBasic.getCoborrower().getGtjkrzjhm());
                    }});
                }
            }});

            this.setBLZL(commonPerson.getExtension().getGrzl());
        }};

    }

    public PageRes<Object> getBanks(TokenContext tokenContext, final String Code, final String Name, final String pageNo, final String pageSize) {

        //region //参数检查

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (pageNo != null) {
                page_number = Integer.parseInt(pageNo);
            }

            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion
        PageRes pageRes = new PageRes();

        List<CBankBankInfo> list_bankInfo = instance(this.bankBankInfoDAO).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.eq("area_code", Code));

                criteria.add(Restrictions.like("bank_name", "%" + Name + "%"));

                criteria.add(Restrictions.isNotNull("code"));
            }
        }).pageOption(pageRes, pagesize_number, page_number).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        return new PageRes<Object>() {{

            this.setResults(new ArrayList<>(list_bankInfo));

            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
//        return new ArrayList(CollectionUtils.flatmap(, new CollectionUtils.Transformer<CBankBankInfo, Object>() {
//
//            @Override
//            public Object tansform(CBankBankInfo var1) { return var1; }
//
//        }));
    }

    @Override
    public PageResNew<Object> getBanks(TokenContext tokenContext, String Code, String Name, String marker, String action, String pageSize) {

        //region //参数检查

        int pagesize_number = 0;

        try {

            if (pageSize != null) { pagesize_number = Integer.parseInt(pageSize); }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        PageRes pageRes = new PageRes();

        List<CBankBankInfo> list_bankInfo = instance(this.bankBankInfoDAO).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.eq("area_code", Code));

                criteria.add(Restrictions.like("bank_name", "%" + Name + "%"));

                criteria.add(Restrictions.isNotNull("code"));
            }
        }).pageOption(marker,action, pagesize_number).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        return new PageResNew<Object>() {{

            this.setResults(action,new ArrayList<>(list_bankInfo));

        }};
//        return new ArrayList(CollectionUtils.flatmap(, new CollectionUtils.Transformer<CBankBankInfo, Object>() {
//
//            @Override
//            public Object tansform(CBankBankInfo var1) { return var1; }
//
//        }));
    }

    @Override
    public IndiAcctDepositDetailsNew<GetIndiAcctDepositDetailsDep> getPersonDepositDetails(TokenContext tokenContext, String grzh, String pageNo, String pageSize) {
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (pageNo != null) {
                page_number = Integer.parseInt(pageNo);
            }
            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(CollectionBusinessType.汇缴.getCode(), CollectionBusinessType.汇缴.getName());
        hashMap.put(CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.部分提取.getName());
        hashMap.put(CollectionBusinessType.销户提取.getCode(), CollectionBusinessType.销户提取.getName());
        hashMap.put(CollectionBusinessType.年终结息.getCode(), CollectionBusinessType.年终结息.getName());
        hashMap.put(CollectionBusinessType.错缴更正.getCode(), CollectionBusinessType.错缴更正.getName());
        hashMap.put(CollectionBusinessType.补缴.getCode(), CollectionBusinessType.补缴.getName());
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("grzh", grzh);
            this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.已入账分摊.getName(), CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.办结.getName()));
            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.汇缴.getCode(), CollectionBusinessType.部分提取.getCode(), CollectionBusinessType.销户提取.getCode(), CollectionBusinessType.年终结息.getCode(), CollectionBusinessType.错缴更正.getCode(), CollectionBusinessType.补缴.getCode()));
        }}).orderOption("jzrq", Order.DESC).pageOption(pageRes, pagesize_number, page_number).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.addOrder(org.hibernate.criterion.Order.desc("cCollectionPersonalBusinessDetailsExtension.fsny"));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        PageRes<GetIndiAcctDepositDetailsDep> pageRes1 = new PageRes<>();
        ArrayList<GetIndiAcctDepositDetailsDep> getIndiAcctDepositDetailsRes = new ArrayList<>();
        GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson = null;
        if(stCollectionPersonalBusinessDetails.size()!=0) {
            getIndiAcctDepositDetailsPerson = new GetIndiAcctDepositDetailsPerson();

            getIndiAcctDepositDetailsPerson.setGRZH(stCollectionPersonalBusinessDetails.get(0).getGrzh());  //个人账号

            getIndiAcctDepositDetailsPerson.setXingMing(stCollectionPersonalBusinessDetails.get(0).getPerson().getXingMing());  //姓名

            getIndiAcctDepositDetailsPerson.setSFDJ(stCollectionPersonalBusinessDetails.get(0).getPerson().getExtension().getSfdj());  //是否冻结

            getIndiAcctDepositDetailsPerson.setZJLX(stCollectionPersonalBusinessDetails.get(0).getPerson().getZjlx());  //证件类型

            getIndiAcctDepositDetailsPerson.setZJHM(stCollectionPersonalBusinessDetails.get(0).getPerson().getZjhm());  //证件号码

            getIndiAcctDepositDetailsPerson.setSJHM(stCollectionPersonalBusinessDetails.get(0).getPerson().getSjhm());  //手机号码

            getIndiAcctDepositDetailsPerson.setYouXiang(stCollectionPersonalBusinessDetails.get(0).getPerson().getExtension().getDzyx());  //邮箱

            getIndiAcctDepositDetailsPerson.setGRCKZHKHYHMC(stCollectionPersonalBusinessDetails.get(0).getPerson().getCollectionPersonalAccount().getGrckzhkhyhmc());  //个人存款账户开户银行名称

            getIndiAcctDepositDetailsPerson.setGRCKZHHM(stCollectionPersonalBusinessDetails.get(0).getPerson().getCollectionPersonalAccount().getGrckzhhm());  //个人存款账户号码

            getIndiAcctDepositDetailsPerson.setGRZHYE(stCollectionPersonalBusinessDetails.get(0).getPerson().getCollectionPersonalAccount().getGrzhye().toString());  //个人账户余额
        }
        for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 : stCollectionPersonalBusinessDetails) {

            GetIndiAcctDepositDetailsDep getIndiAcctDepositDetailsDep = new GetIndiAcctDepositDetailsDep();
            getIndiAcctDepositDetailsDep.setJZRQ(DateUtil.date2Str(stCollectionPersonalBusinessDetails1.getJzrq(), format));  //记账日期

            getIndiAcctDepositDetailsDep.setYWLX(hashMap.get(stCollectionPersonalBusinessDetails1.getExtension().getCzmc()));  //业务类型

            if (CollectionBusinessType.汇缴.getCode().equals(stCollectionPersonalBusinessDetails1.getExtension().getCzmc()) || CollectionBusinessType.错缴更正.getCode().equals(stCollectionPersonalBusinessDetails1.getExtension().getCzmc()) || CollectionBusinessType.补缴.getCode().equals(stCollectionPersonalBusinessDetails1.getExtension().getCzmc())) {
                if (stCollectionPersonalBusinessDetails1.getExtension().getDwfse() != null)
                    getIndiAcctDepositDetailsDep.setDWFSE(stCollectionPersonalBusinessDetails1.getExtension().getDwfse().toPlainString());
                if (stCollectionPersonalBusinessDetails1.getExtension().getGrfse() != null)
                    getIndiAcctDepositDetailsDep.setGRFSE(stCollectionPersonalBusinessDetails1.getExtension().getGrfse().toPlainString());
                if (StringUtil.notEmpty(stCollectionPersonalBusinessDetails1.getExtension().getFsny()))
                    getIndiAcctDepositDetailsDep.setHJNY(DateUtil.str2str(stCollectionPersonalBusinessDetails1.getExtension().getFsny(), 6));
            }
            getIndiAcctDepositDetailsDep.setFSE(stCollectionPersonalBusinessDetails1.getFse().abs().toString());  //发生额

            if (stCollectionPersonalBusinessDetails1.getExtension().getDqye() != null)
                getIndiAcctDepositDetailsDep.setGRZHYE(stCollectionPersonalBusinessDetails1.getExtension().getDqye().toPlainString());  //个人账户余额（元）

            getIndiAcctDepositDetailsDep.setDWMC(stCollectionPersonalBusinessDetails1.getUnit().getDwmc());  //单位名称

            getIndiAcctDepositDetailsDep.setDWZH(stCollectionPersonalBusinessDetails1.getUnit().getDwzh());  //单位账号



            getIndiAcctDepositDetailsRes.add(getIndiAcctDepositDetailsDep);
        }

        pageRes1.setResults(getIndiAcctDepositDetailsRes);
        pageRes1.setCurrentPage(pageRes.getCurrentPage());

        pageRes1.setNextPageNo(pageRes.getNextPageNo());

        pageRes1.setPageCount(pageRes.getPageCount());

        pageRes1.setTotalCount(pageRes.getTotalCount());

        pageRes1.setPageSize(pageRes.getPageSize());

        IndiAcctDepositDetailsNew<GetIndiAcctDepositDetailsDep> indiAcctDepositDetailsNew =new IndiAcctDepositDetailsNew();
        indiAcctDepositDetailsNew.setGetIndiAcctDepositDetailsPerson(getIndiAcctDepositDetailsPerson);
        indiAcctDepositDetailsNew.setPageRes(pageRes1);

        return indiAcctDepositDetailsNew;
    }

    public ArrayList<PersonAccountsGet> getAccounts(TokenContext tokenContext, String zjhm,String GLXH) {

        List<StCommonPerson> list_commonperson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", zjhm);

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if("1".equals(GLXH)){
                    criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
                    criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
                }
            }
        }).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        return new ArrayList<PersonAccountsGet>() {{

            for (StCommonPerson commonPerson : list_commonperson) {

                this.add(new PersonAccountsGet() {{

                    this.setGRZH(commonPerson.getGrzh());
                    this.setXimgMing(commonPerson.getXingMing());
                    this.setGRYHCKZHHM(commonPerson.getCollectionPersonalAccount().getGrckzhhm());
                    this.setGRYHCKKHYHMC(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhmc());
                    this.setZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
                    this.setZHYE((commonPerson.getCollectionPersonalAccount() == null || commonPerson.getCollectionPersonalAccount().getGrzhye() == null) ? null : commonPerson.getCollectionPersonalAccount().getGrzhye().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() + "");
                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,commonPerson.getExtension().getGrjzny(),formatNY));
                    this.setDWMC(commonPerson.getUnit().getDwmc());
                    this.setDWZH(commonPerson.getUnit().getDwzh());
                    this.setSJHM(commonPerson.getSjhm());
                    this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs().setScale(2, RoundingMode.HALF_UP)+"");
                }});
            }
        }};
    }

    public PageRes<TransferListGet> getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM, String page, String pagesize, String KSSJ, String JSSJ) {

        //region //参数检查
        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pagesize != null) {
                pagesize_number = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCollectionPersonalBusinessDetails> list_business = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(Xingming)) {
                this.put("person.xingMing", Xingming);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                this.put("person.zjhm", ZJHM);
            }
            if (StringUtil.notEmpty(GRZH)) {
                this.put("grzh", GRZH);
            }

            if (StringUtil.notEmpty(ZRDWM)) {
                this.put("individualAccountTransferNewVice.zrdw.dwmc", ZRDWM);
            }

            if (StringUtil.notEmpty(ZCDWM)) {
                this.put("individualAccountTransferNewVice.zcdw.dwmc", ZCDWM);
            }

        }}).searchOption(SearchOption.FUZZY).pageOption(pageRes, pagesize_number, page_number).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                }

                criteria.add(Restrictions.eq("gjhtqywlx", CollectionBusinessType.内部转移.getCode()));
            }
        }).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        //endregion

        return new PageRes<TransferListGet>() {{

            this.setResults(new ArrayList<TransferListGet>() {{

                for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

                    this.add(new TransferListGet() {{

                        this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

                        this.setGRZH(collectionPersonalBusinessDetails.getPerson().getGrzh());

                        this.setXingMing(collectionPersonalBusinessDetails.getPerson().getXingMing());

                        this.setZYJE(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZysgrzhye());

                        this.setGRYHCKZHHM(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());

                        this.setZHYE(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().setScale(2,BigDecimal.ROUND_HALF_UP)+"");

                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionPersonalBusinessDetails.getPerson().getExtension().getGrjzny(), formatNY));

                        this.setZCDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwzh());

                        this.setZCDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwmc());

                        this.setZRDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwzh());

                        this.setZRDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwmc());

                        this.setHBSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getBjsj(), formatNYRSF));

                        this.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());

                        this.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());

                        this.setZJHM(collectionPersonalBusinessDetails.getPerson() == null ? null : collectionPersonalBusinessDetails.getPerson().getZjhm());
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
    public PageResNew<TransferListGet> getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM,String marker, String action, String pagesize, String KSSJ, String JSSJ) {
        //region //参数检查

        int pagesize_number = 0;

        try {


            if (pagesize != null) { pagesize_number = Integer.parseInt(pagesize); }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        //region //必要字段查询

        List<StCollectionPersonalBusinessDetails> list_business = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(Xingming)) {
                this.put("person.xingming", Xingming);
            }
            if (StringUtil.notEmpty(ZJHM)) {
                this.put("person.zjhm", ZJHM);
            }
            if (StringUtil.notEmpty(GRZH)) {
                this.put("grzh", GRZH);
            }

            if (StringUtil.notEmpty(ZRDWM)) {
                this.put("individualAccountTransferNewVice.zrdw.dwmc", ZRDWM);
            }

            if (StringUtil.notEmpty(ZCDWM)) {
                this.put("individualAccountTransferNewVice.zcdw.dwmc", ZCDWM);
            }

        }}).searchOption(SearchOption.FUZZY).pageOption(marker,action, pagesize_number).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                }

                criteria.add(Restrictions.eq("gjhtqywlx", CollectionBusinessType.内部转移.getCode()));
            }
        }).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        //endregion

        return new PageResNew<TransferListGet>() {{

            this.setResults(action,new ArrayList<TransferListGet>() {{

                for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

                    this.add(new TransferListGet() {{

                        this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

                        this.setGRZH(collectionPersonalBusinessDetails.getPerson().getGrzh());

                        this.setXingMing(collectionPersonalBusinessDetails.getPerson().getXingMing());

                        this.setZYJE(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZysgrzhye());

                        this.setGRYHCKZHHM(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());

                        this.setZHYE(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().setScale(2,BigDecimal.ROUND_HALF_UP)+"");

                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionPersonalBusinessDetails.getPerson().getExtension().getGrjzny(), formatNY));

                        this.setZCDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwzh());

                        this.setZCDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwmc());

                        this.setZRDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwzh());

                        this.setZRDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwmc());

                        this.setHBSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getBjsj(), formatNYRSF));

                        this.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());

                        this.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());

                    }});
                }
            }});

        }};

    }

    public CommonResponses getTransferDetails(TokenContext tokenContext, String ywlsh) {


        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", ywlsh);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (collectionPersonalBusinessDetails == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        TransferListGet transferListGet = new TransferListGet();

        transferListGet.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

        transferListGet.setGRZH(collectionPersonalBusinessDetails.getPerson().getGrzh());

        transferListGet.setXingMing(collectionPersonalBusinessDetails.getPerson().getXingMing());

        transferListGet.setZYJE(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZysgrzhye());

        transferListGet.setGRYHCKZHHM(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());

        transferListGet.setZHYE(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye().setScale(2,BigDecimal.ROUND_HALF_UP) + "");

        transferListGet.setJZNY(DateUtil.str2str(collectionPersonalBusinessDetails.getPerson().getExtension().getGrjzny(), 6));

        transferListGet.setZCDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwzh());

        transferListGet.setZCDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZcdw().getDwmc());

        transferListGet.setZRDWZH(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwzh());

        transferListGet.setZRDWM(collectionPersonalBusinessDetails.getIndividualAccountTransferNewVice().getZrdw().getDwmc());

        transferListGet.setHBSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getExtension().getBjsj(), formatNYRSF));

        transferListGet.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());

        transferListGet.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());

        //审核人，该条记录审核通过的操作员
        CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", ywlsh);
                this.put("shjg", "01");
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cAuditHistory != null) {
            transferListGet.setSHR(cAuditHistory.getCzy());
        }

        String id = pdfService.getMergeTransferReceiptPdf(transferListGet);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public CommonResponses getPersonDepositPdfDetails(TokenContext tokenContext,String grzh,String hjnys,String hjnye) {

        IndiAcctDepositDetails depositDetails = new IndiAcctDepositDetails();

        //个人基础信息
        GetIndiAcctDepositDetailsPerson personDetail = getpersonDetail(grzh);
        //个人缴存明细信息
        ArrayList<GetIndiAcctDepositDetailsDep> list = getJcmx(grzh,hjnys,hjnye);

        depositDetails.setGetIndiAcctDepositDetailsPerson(personDetail);
        depositDetails.setList(list);

        String id = pdfService.getPersonDepositPdf(depositDetails);

        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    private ArrayList<GetIndiAcctDepositDetailsDep> getJcmx(String grzh,String hjnys, String hjnye) {
        ArrayList<GetIndiAcctDepositDetailsDep> list = new ArrayList<>();

        List<StCollectionPersonalBusinessDetails> personDeposits = collectionPersonalBusinessDetailsDAO.getPersonDepositsChange(grzh);

        for(StCollectionPersonalBusinessDetails grywmx : personDeposits){
            GetIndiAcctDepositDetailsDep rowDetail = new GetIndiAcctDepositDetailsDep();

            CCollectionPersonalBusinessDetailsExtension extension = grywmx.getExtension();

            rowDetail.setJZRQ(DateUtil.date2Str(grywmx.getJzrq(), format));  //记账日期
            rowDetail.setYWLX(CollectionBusinessType.getNameByCode(extension.getCzmc()));  //业务类型
            if (CollectionBusinessType.汇缴.getCode().equals(extension.getCzmc()) || CollectionBusinessType.错缴更正.getCode().equals(extension.getCzmc()) || CollectionBusinessType.补缴.getCode().equals(extension.getCzmc())) {
                rowDetail.setDWFSE(ComUtils.moneyFormat(extension.getDwfse()));
                rowDetail.setGRFSE(ComUtils.moneyFormat(extension.getGrfse()));
            }
            if(hjnys!=null&&hjnye!=null) {
                rowDetail.setHJNY(DateUtil.str2str(extension.getFsny(), 6));
                String hjny = rowDetail.getHJNY();
                if(hjny==null){
                    hjny = DateUtil.date2Str(grywmx.getJzrq(), formatNY);
                }
                int res_l = hjny.compareTo(hjnys);
                int res_r = hjny.compareTo(hjnye);
                if (res_l==0){//"a=b"
                }else if(res_l < 0){ //"a<b"
                    continue;
                }else if(res_l>0 && res_r<=0){
                }else if(res_l>0 && res_r>0){//"a>b"
                    continue;
                }

            }
            rowDetail.setFSE(ComUtils.moneyFormat(grywmx.getFse().abs()));  //发生额
            rowDetail.setGRZHYE(ComUtils.moneyFormat(extension.getDqye()));  //个人账户余额（元）
            rowDetail.setDWMC(grywmx.getUnit().getDwmc());  //单位名称
            rowDetail.setDWZH(grywmx.getUnit().getDwzh());  //单位账号

            list.add(rowDetail);
        }

        return list;
    }

    private GetIndiAcctDepositDetailsPerson getpersonDetail(String grzh) {
        GetIndiAcctDepositDetailsPerson personDetail = new GetIndiAcctDepositDetailsPerson();

        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        if(person==null){
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人账号");
        }
        personDetail.setGRZH(person.getGrzh());
        personDetail.setSJHM(person.getSjhm());
        personDetail.setXingMing(person.getXingMing());
        personDetail.setZJHM(person.getZjhm());
        if (person.getZjlx() != null) {
            SingleDictionaryDetail ZJLX = iDictionaryService.getSingleDetail(person.getZjlx(), "PersonalCertificate");
            personDetail.setZJLX(ZJLX != null ? ZJLX.getName() : "");// 证件类型
        }
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();

        personDetail.setGRZHYE(personalAccount.getGrzhye().toString());
        personDetail.setGRCKZHHM(personalAccount.getGrckzhhm());
        personDetail.setGRCKZHKHYHMC(personalAccount.getGrckzhkhyhmc());

        CCommonPersonExtension extension = person.getExtension();
        String Sfdj = null;
        personDetail.setYouXiang(extension.getDzyx());
        if(extension.getSfdj()!=null&&extension.getSfdj().equals("01")){
             Sfdj = "否";
        }else{
             Sfdj = "是";
        }
        personDetail.setSFDJ(Sfdj);

        return personDetail;
    }
    public CommonResponses getDiffTerritoryLoadProvePdf(TokenContext tokenContext,String grzh) {
        ForeignLoanProof foreignLoanProof= applyLoanService.getForeignLoanProof(tokenContext,grzh);
        String id = pdfService.getDiffTerritoryLoadProvePdf(foreignLoanProof);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }
}
