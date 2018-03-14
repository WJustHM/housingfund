package com.handge.housingfund.collection.service.indiacctmanage;


import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.CommonMessage;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSeal;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSet;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctUnseal;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayback;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

/*
 * Created by Liujuhao on 2017/7/1.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiringInspection", "serial", "SpringJavaAutowiredMembersInspection", "ConstantConditions"})
@com.alibaba.dubbo.config.annotation.Service
public class IndiAcctSetImpl implements IndiAcctSet {

    @Autowired
    private ICCollectionIndividualAccountActionViceDAO collectionIndividualAccountActionViceDAO;

    @Autowired
    private ICCollectionIndividualAccountBasicViceDAO collectionIndividualAccountBasicViceDAO;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private com.handge.housingfund.statemachineV2.IStateMachineService stateMachineService;
    @Autowired
    private ICCollectionIndividualAccountTransferNewViceDAO collectionIndividualAccountTransferNewViceDAO;
    @Autowired
    private AccountRpcService accountRpcService;
    @Autowired
    private UnitPayback unitPayback;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private IndiAcctSeal indiAcctSeal;
    @Autowired
    private IndiAcctUnseal indiAcctUnseal;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IStCommonPolicyDAO commonPolicyDAO;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private IBankInfoService iBankInfoService;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

    private static String format = "yyyy-MM-dd";
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    private static String formatNY = "yyyy-MM";
    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    // completed 已测试 - 正常
    @Override
    public PageRes<ListOperationAcctsResRes> getAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String ZhuangTai, final String XingMing, final String ZJHM, final String CZMC, String page, String pagesize, String KSSJ, String JSSJ) {

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

        List<StCollectionPersonalBusinessDetails> list_business = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) {
                this.put("unit.dwmc", DWMC);
            }

            if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {
                this.put("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai);
            }

            if (StringUtil.notEmpty(XingMing)) {
                this.put("individualAccountBasicVice.xingMing", XingMing);
            }

            if (StringUtil.notEmpty(ZJHM)) {
                this.put("individualAccountBasicVice.zjhm", ZJHM);
            }

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {


                if (!(StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                }

                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }

                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()));

                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));

                }
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");

                if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        return new PageRes<ListOperationAcctsResRes>() {{

            this.setResults(new ArrayList<ListOperationAcctsResRes>() {{

                for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

                    CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = collectionPersonalBusinessDetails.getIndividualAccountBasicVice() == null ? new CCollectionIndividualAccountBasicVice() : collectionPersonalBusinessDetails.getIndividualAccountBasicVice();

                    StCommonUnit commonUnit = collectionPersonalBusinessDetails.getUnit() == null ? new StCommonUnit() : collectionPersonalBusinessDetails.getUnit();

                    CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension() == null ? new CCollectionPersonalBusinessDetailsExtension() : collectionPersonalBusinessDetails.getExtension();


                    this.add(new ListOperationAcctsResRes() {{

						/* 构造返回值 */

                        this.setId(collectionPersonalBusinessDetails.getId());
                        this.setXingming(collectionIndividualAccountBasicVice.getXingMing());

                        this.setGRJCJS("" + collectionIndividualAccountBasicVice.getGrjcjs());

                        this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

                        this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getCreated_at(), formatNYRSF));

                        this.setZhuangTai(collectionPersonalBusinessDetailsExtension.getStep());

                        this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

                        this.setGRZHZT(this.getGRZHZT()/* 开户没有个人账号状态 */);

                        if (commonUnit.getCollectionUnitAccount() != null && collectionIndividualAccountBasicVice.getGrjcjs() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null) {

                            this.setYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().multiply(commonUnit.getCollectionUnitAccount().getGrjcbl().add(commonUnit.getCollectionUnitAccount().getDwjcbl())));
                        }

                        this.setDWMC(commonUnit.getDwmc());

                        this.setGRZH(collectionPersonalBusinessDetails.getGrzh());

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
    public PageResNew<ListOperationAcctsResRes> getAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String ZhuangTai, final String XingMing, final String ZJHM, final String CZMC, String marker, String action, String pagesize, String KSSJ, String JSSJ) {

        //region //参数检查

        int pagesize_number = 0;

        try {

            if (pagesize != null) {
                pagesize_number = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCollectionPersonalBusinessDetails> list_business = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) {
                this.put("unit.dwmc", DWMC);
            }

            if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {
                this.put("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai);
            }

            if (StringUtil.notEmpty(XingMing)) {
                this.put("individualAccountBasicVice.xingMing", XingMing);
            }

            if (StringUtil.notEmpty(ZJHM)) {
                this.put("individualAccountBasicVice.zjhm", ZJHM);
            }

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {


                if (!(StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))) {

                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                }

                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }

                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()));

                if (DateUtil.isFollowFormat(KSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ, formatNYRSF, false)) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at", DateUtil.safeStr2Date(formatNYRSF, JSSJ)));

                }
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");

                if (!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).pageOption(marker, action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        return new PageResNew<ListOperationAcctsResRes>() {{

            this.setResults(action,new ArrayList<ListOperationAcctsResRes>() {{

                for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

                    CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = collectionPersonalBusinessDetails.getIndividualAccountBasicVice() == null ? new CCollectionIndividualAccountBasicVice() : collectionPersonalBusinessDetails.getIndividualAccountBasicVice();

                    StCommonUnit commonUnit = collectionPersonalBusinessDetails.getUnit() == null ? new StCommonUnit() : collectionPersonalBusinessDetails.getUnit();

                    CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension() == null ? new CCollectionPersonalBusinessDetailsExtension() : collectionPersonalBusinessDetails.getExtension();


                    this.add(new ListOperationAcctsResRes() {{

						/* 构造返回值 */

                        this.setId(collectionPersonalBusinessDetails.getId());
                        this.setXingming(collectionIndividualAccountBasicVice.getXingMing());

                        this.setGRJCJS("" + collectionIndividualAccountBasicVice.getGrjcjs());

                        this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

                        this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getCreated_at(), formatNYRSF));

                        this.setZhuangTai(collectionPersonalBusinessDetailsExtension.getStep());

                        this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

                        this.setGRZHZT(this.getGRZHZT()/* 开户没有个人账号状态 */);

                        if (commonUnit.getCollectionUnitAccount() != null && collectionIndividualAccountBasicVice.getGrjcjs() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null) {

                            this.setYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().multiply(commonUnit.getCollectionUnitAccount().getGrjcbl().add(commonUnit.getCollectionUnitAccount().getDwjcbl())));
                        }

                        this.setDWMC(commonUnit.getDwmc());

                        this.setGRZH(collectionPersonalBusinessDetails.getGrzh());

                    }});
                }
            }});

        }};

    }


    // completed 已测试 - 正常
    @Override
    public AddIndiAcctSetRes addAcctSet(TokenContext tokenContext, IndiAcctSetPost addIndiAcctSet) {

        //region //参数检查

        boolean allowNull = "0".equals(addIndiAcctSet.getCZLX());

        if (!DateUtil.isFollowFormat(addIndiAcctSet.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "公积金首次汇缴年月");
        }

        if (!DateUtil.isFollowFormat(addIndiAcctSet.getGRXX().getCSNY()/*出生年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!StringUtil.isDigits(addIndiAcctSet.getGRZHXX().getGRJCJS() + ""/*个人缴存基数*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.isDigits(addIndiAcctSet.getGRXX().getJTYSR()/*家庭月收入*/ + "", true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "家庭月收入");
        }

        if (!DateUtil.isFollowFormat(addIndiAcctSet.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "公积金首次汇缴年月");
        }

        if (!DateUtil.isFollowFormat(addIndiAcctSet.getGRXX().getCSNY()/*出生年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!StringUtil.isDigits(addIndiAcctSet.getGRZHXX().getGRJCJS() + ""/*个人缴存基数*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人存款账户号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getJBRZJLX()/*经办人证件类型*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件类型");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getJBRXM()/*经办人姓名*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人姓名");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getHYZK()/*婚姻状况*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "婚姻状况");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getZJLX()/*证件类型*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件类型");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getSJHM()/*手机号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getZJHM()/*证件号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getXMQP()/*姓名全拼*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名全拼");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getXingBie()/*性别*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "性别");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getGRXX().getXingMing()/*姓名*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getJBRZJHM()/*经办人证件号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSet.getDWXX().getDWZH()/*单位账号*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位账号");
        }

        //endregion

        //region //必要参数声明&关系配置
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("id", tokenContext.getUserInfo().getYWWD());
        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //验证：个人公积金首次汇缴年月，必须小于单位首次汇缴年月
        //addCheck(addIndiAcctSet);

        CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = new CCollectionIndividualAccountBasicVice();

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();

        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);

        collectionIndividualAccountBasicVice.setGrywmx(collectionPersonalBusinessDetails);


        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", addIndiAcctSet.getDWXX().getDWZH());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (commonUnit == null || commonUnit.getExtension() == null || commonUnit.getCollectionUnitAccount() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息");
        }

        if (!network.getId().equals(commonUnit.getExtension().getKhwd())) {

            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }

        if (UnitAccountStatus.封存.getCode().equals(commonUnit.getCollectionUnitAccount().getDwzhzt())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "单位已封存");
        }
        //endregion

        //region  //字段填充
        // COMMON
        collectionPersonalBusinessDetails.setYwlsh(collectionPersonalBusinessDetails.getYwlsh()/* 不填 数据库自动生成 */);
        collectionPersonalBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode()/* todo 冲账标识 */);
        collectionPersonalBusinessDetails.setDngjfse(BigDecimal.ZERO/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setFse(BigDecimal.ZERO/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setFslxe(BigDecimal.ZERO/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.开户.getCode());
        collectionPersonalBusinessDetails.setGrzh(collectionPersonalBusinessDetails.getGrzh()/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setJzrq(collectionPersonalBusinessDetails.getJzrq()/* todo 记账日期 */);
        collectionPersonalBusinessDetails.setSnjzfse(BigDecimal.ZERO/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setTqyy(collectionPersonalBusinessDetails.getTqyy()/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setTqfs(collectionPersonalBusinessDetails.getTqfs()/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setUnit(commonUnit);
        collectionPersonalBusinessDetails.setPerson(collectionPersonalBusinessDetails.getPerson()/* 开户此字段不填 */);
        collectionPersonalBusinessDetails.setCreated_at(new Date());

        collectionPersonalBusinessDetailsExtension.setSlsj(new Date()/* todo 受理时间 */);
        collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.开户.getCode());
        collectionPersonalBusinessDetailsExtension.setDjje(BigDecimal.ZERO/* 开户此字段不填 */);
        collectionPersonalBusinessDetailsExtension.setBeizhu(collectionPersonalBusinessDetailsExtension.getBeizhu()/* 开户此字段不填 */);
        collectionPersonalBusinessDetailsExtension.setZcdw(collectionPersonalBusinessDetailsExtension.getZcdw()/* 开户此字段不填 */);
        collectionPersonalBusinessDetailsExtension.setCzyy(collectionPersonalBusinessDetailsExtension.getCzyy()/*不填 操作原因*/);

        collectionIndividualAccountBasicVice.setYwlsh(collectionIndividualAccountBasicVice.getYwlsh()/* 不填 数据库自动生成 */);
        collectionIndividualAccountBasicVice.setGjhtqywlx(CollectionBusinessType.开户.getCode());
        collectionIndividualAccountBasicVice.setSlsj(new Date()/* todo 受理时间 */);
        collectionIndividualAccountBasicVice.setGrzh(collectionIndividualAccountBasicVice.getGrzh()/* 开户此字段不填 */);
        collectionIndividualAccountBasicVice.setGrzhye(BigDecimal.ZERO/* 开户此字段不填 */);


        // GRZHXX
        collectionIndividualAccountBasicVice.setGjjschjny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSet.getGRZHXX().getGJJSCHJNY(), DateUtil.dbformatYear_Month)/*公积金首次汇缴年月*/);
        collectionIndividualAccountBasicVice.setGrckzhhm(addIndiAcctSet.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/);
        collectionIndividualAccountBasicVice.setGrjcjs(StringUtil.safeBigDecimal(addIndiAcctSet.getGRZHXX().getGRJCJS() + "")/*个人缴存基数*/);
        collectionIndividualAccountBasicVice.setGrckzhkhyhdm(addIndiAcctSet.getGRZHXX().getGRCKZHKHYHDM()/*个人存款账户开户银行代码*/);
        collectionIndividualAccountBasicVice.setGrckzhkhyhmc(addIndiAcctSet.getGRZHXX().getGRCKZHKHYHMC()/*个人存款账户开银行名称*/);


        // JBRZJLX
        collectionPersonalBusinessDetailsExtension.setJbrzjlx(addIndiAcctSet.getJBRZJLX()/*经办人证件类型*/);

        // BLZL
        collectionIndividualAccountBasicVice.setBlzl(addIndiAcctSet.getBLZL()/*办理资料*/);
        collectionPersonalBusinessDetailsExtension.setBlzl(addIndiAcctSet.getBLZL()/*办理资料*/);

        // JBRXM
        collectionPersonalBusinessDetailsExtension.setJbrxm(addIndiAcctSet.getJBRXM()/*经办人姓名*/);

        // GRXX
        collectionIndividualAccountBasicVice.setHyzk(addIndiAcctSet.getGRXX().getHYZK()/*婚姻状况*/);
        collectionIndividualAccountBasicVice.setJtzz(addIndiAcctSet.getGRXX().getJTZZ()/*家庭住址*/);
        collectionIndividualAccountBasicVice.setCsny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSet.getGRXX().getCSNY()/*出生年月*/, DateUtil.dbformatYear_Month));
        collectionIndividualAccountBasicVice.setZjlx(addIndiAcctSet.getGRXX().getZJLX()/*证件类型*/);
        collectionIndividualAccountBasicVice.setZhiYe(addIndiAcctSet.getGRXX().getZhiYe()/*职业*/);
        collectionIndividualAccountBasicVice.setYzbm(addIndiAcctSet.getGRXX().getYZBM()/*邮政编码*/);
        collectionIndividualAccountBasicVice.setZhiCheng(addIndiAcctSet.getGRXX().getZhiCheng()/*职称*/);
        collectionIndividualAccountBasicVice.setSjhm(addIndiAcctSet.getGRXX().getSJHM()/*手机号码*/);
        collectionIndividualAccountBasicVice.setZjhm(addIndiAcctSet.getGRXX().getZJHM()/*证件号码*/);
        collectionIndividualAccountBasicVice.setGddhhm(addIndiAcctSet.getGRXX().getGDDHHM()/*固定电话号码*/);
        collectionIndividualAccountBasicVice.setXmqp(addIndiAcctSet.getGRXX().getXMQP()/*姓名全拼*/);
        collectionIndividualAccountBasicVice.setXingBie(addIndiAcctSet.getGRXX().getXingBie()/*性别*/);
        collectionIndividualAccountBasicVice.setJtysr(StringUtil.safeBigDecimal(addIndiAcctSet.getGRXX().getJTYSR()/*家庭月收入*/ + ""));
        collectionIndividualAccountBasicVice.setXueLi(addIndiAcctSet.getGRXX().getXueLi()/*学历*/);
        collectionIndividualAccountBasicVice.setXingMing(addIndiAcctSet.getGRXX().getXingMing()/*姓名*/);
        collectionIndividualAccountBasicVice.setZhiWu(addIndiAcctSet.getGRXX().getZhiWu()/*职务*/);
        collectionIndividualAccountBasicVice.setDzyx(addIndiAcctSet.getGRXX().getYouXiang()/*邮箱*/);

        // YWWD
        collectionIndividualAccountBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);


        collectionPersonalBusinessDetailsExtension.setYwwd(network/*业务网点*/);

        // JBRZJHM
        collectionPersonalBusinessDetailsExtension.setJbrzjhm(addIndiAcctSet.getJBRZJHM()/*经办人证件号码*/);

        // DWXX
        collectionIndividualAccountBasicVice.setDwzh(addIndiAcctSet.getDWXX().getDWZH()/*单位账号*/);

        // CZY
        collectionIndividualAccountBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
        collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        //endregion

        //region//唯一性验证

        if (addIndiAcctSet.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", collectionIndividualAccountBasicVice.getZjhm());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) {
                throw new ErrorException(e);
            }
        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "证件号码");
        }

        if (addIndiAcctSet.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("sjhm", collectionIndividualAccountBasicVice.getSjhm());

            //this.put("xingMing",collectionIndividualAccountBasicVice.getXingMing());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) { throw new ErrorException(e); }

        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "手机号码");
        }

        if (addIndiAcctSet.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("collectionPersonalAccount.grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "个人存款账户号码");
        }

        //endregion

        //region //修改状态
        CCollectionIndividualAccountBasicVice savedVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        if (savedVice == null || savedVice.getGrywmx() == null) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务记录");
        }


        StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.通过.getEvent());

            this.put("1", Events.提交.getEvent());

        }}.get(addIndiAcctSet.getCZLX()), new TaskEntity() {{

            this.setStatus(savedVice.getGrywmx().getExtension().getStep() == null ? "初始状态" : savedVice.getGrywmx().getExtension().getStep());
            this.setTaskId(savedVice.getGrywmx().getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            ;
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_个人账户设立.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(savedVice.getCzy());
            this.setWorkstation(savedVice.getYwwd());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) { throw new ErrorException(e); }

                if (!succeed || next == null) { return; }

                collectionPersonalBusinessDetailsExtension.setStep(next);


                if (StringUtil.isIntoReview(next, null)) {

                    collectionPersonalBusinessDetailsExtension.setDdsj(new Date());

                }

                DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(savedVice).save(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }

                });

                if (next.equals(CollectionBusinessStatus.办结.getName())) {

                    doAcctSet(tokenContext, savedVice.getGrywmx().getYwlsh());
                }
            }
        });

        //endregion

        //region //在途验证
        BusUtils.checkRemittanceDoing(addIndiAcctSet.getDWXX().getDWZH());

        if (addIndiAcctSet.getCZLX().equals("1") && !DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria, "grywmx.cCollectionPersonalBusinessDetailsExtension.step"), (Collection) CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

                    @Override
                    public String tansform(CollectionBusinessStatus var1) {
                        return var1.getName();
                    }

                }))));
                criteria.add(Restrictions.or(
                    Restrictions.eq("zjhm", collectionIndividualAccountBasicVice.getZjhm()),
                    Restrictions.eq("sjhm", collectionIndividualAccountBasicVice.getSjhm()),
                    Restrictions.eq("grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm())
                ));
                criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode(),CollectionBusinessType.开户.getCode()));
                criteria.add(Restrictions.ne("grywmx.ywlsh", savedVice.getGrywmx().getYwlsh()));
            }

        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        })) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process, "已有人正在使用相同的 证件号码 或 手机号码 或 个人存款账户号码 办理开户或变更业务");
        }

        if (addIndiAcctSet.getCZLX().equals("1")) {

            this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);

        }
        this.iSaveAuditHistory.saveNormalBusiness(savedVice.getGrywmx().getYwlsh(), tokenContext, CollectionBusinessType.开户.getName(), "新建");

        //endregion

        return new AddIndiAcctSetRes() {{

            this.setYWLSH(savedVice.getGrywmx().getYwlsh());

            this.setStatus("success");

            this.setGRZH(this.getGRZH()/* 开户没有个人账号 */);
        }};
    }


    // completed 未测试 - 待定
    @Override
    public CommonResponses headAcctSet(TokenContext tokenContext, String YWLSH) {

        //region // 检查参数
        if (YWLSH == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号");
        }

        //endregion

        //region // 必要字段查询&完整性验证
        CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grywmx.ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null || collectionIndividualAccountBasicVice.getGrywmx().getExtension() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        if (!CollectionBusinessStatus.办结.getName().equals(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务状态");
        }

        final StCommonPerson person_exist;

        // 对应个人账户(开户成功的情况下)
        if (collectionIndividualAccountBasicVice.getGrzh() != null) {

            StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grzh", collectionIndividualAccountBasicVice.getGrzh());

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }

            });

            if (commonPerson == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
            }

            person_exist = commonPerson;

        } else {

            person_exist = null;
        }


        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", collectionIndividualAccountBasicVice.getDwzh());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion
        HeadIndiAcctSetRes result = new HeadIndiAcctSetRes();


        // 构建返回值
        result.setGRJCJS("" + collectionIndividualAccountBasicVice.getGrjcjs());

        result.setZhiWu(collectionIndividualAccountBasicVice.getZhiWu());

        result.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getCsny(), formatNY));

        result.setGGJSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGjjschjny(), formatNY));

        result.setZJLX(collectionIndividualAccountBasicVice.getZjlx());

        if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && collectionIndividualAccountBasicVice.getGrjcjs() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null) {
            Float gryjce = collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue());
            result.setGRYJCE("" + ComUtils.moneyFormat(new BigDecimal(gryjce.toString())));
        }

        result.setGRZH(collectionIndividualAccountBasicVice.getGrzh());

        result.setRiQi(DateUtil.date2Str(new Date(), format));

        result.setZhiYe(collectionIndividualAccountBasicVice.getZhiYe());

        result.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGjjschjny(), formatNY));

        result.setJTYSR(collectionIndividualAccountBasicVice.getJtysr() + "");

        if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null) {
            Float dwyjce = collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue());
            result.setDWYJCE("" + ComUtils.moneyFormat(new BigDecimal(dwyjce.toString())));
        }
        result.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

        if (person_exist != null && person_exist.getCollectionPersonalAccount() != null) {

            SingleDictionaryDetail dictionaryDetail3 = iDictionaryService.getSingleDetail(person_exist.getCollectionPersonalAccount().getGrzhzt(), "PersonalAccountState");
            if (dictionaryDetail3 != null) {
                result.setGRZHZT(dictionaryDetail3 != null ? dictionaryDetail3.getName() : "");
            }
        }
        SingleDictionaryDetail dictionaryDetail = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getXingBie(), "Gender");
        if (dictionaryDetail != null) {
            result.setXingBie(dictionaryDetail != null ? dictionaryDetail.getName() : "");
        }

        if (commonUnit != null) {

            result.setDWMC(commonUnit.getDwmc());
        }
        result.setDWZH(collectionIndividualAccountBasicVice.getDwzh());

        SingleDictionaryDetail dictionaryDetail4 = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getHyzk(), "MaritalStatus");
        result.setHYZK(dictionaryDetail4 != null ? dictionaryDetail4.getName() : "");

//		result.setHYZK(collectionIndividualAccountBasicVice.getHyzk());

        result.setYWLSH(collectionIndividualAccountBasicVice.getGrywmx().getYwlsh());

        result.setYWWD(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getYwwd().getMingCheng());

        result.setGRCKZHKHYHMC(collectionIndividualAccountBasicVice.getGrckzhkhyhmc());

        result.setJTZZ(collectionIndividualAccountBasicVice.getJtzz());

        result.setCZY(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy());

        if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null) {
            Float yjce = collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue() + commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue());
            result.setYJCE("" + ComUtils.moneyFormat(new BigDecimal(yjce.toString())));
        }
        result.setYZBM(collectionIndividualAccountBasicVice.getYzbm());

        if (commonUnit != null) {

            result.setGRJCBL("" + (commonUnit.getCollectionUnitAccount().getGrjcbl() == null ? 0 : commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100"))));

        }
        result.setZhiCheng(collectionIndividualAccountBasicVice.getZhiCheng());

        result.setSJHM(collectionIndividualAccountBasicVice.getSjhm());

        if (person_exist != null && person_exist.getCollectionPersonalAccount() != null) {
            result.setKHRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month_Day, person_exist.getCollectionPersonalAccount().getKhrq(), format));
        }

        result.setGDDHHM(collectionIndividualAccountBasicVice.getGddhhm());

        if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null) {

            result.setDWJCBL("" + (commonUnit.getCollectionUnitAccount().getDwjcbl() == null ? 0 : commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))));
        }

        result.setXMQP(collectionIndividualAccountBasicVice.getXmqp());

        result.setYouXiang(collectionIndividualAccountBasicVice.getDzyx());


        if (collectionIndividualAccountBasicVice.getXueLi() != null) {

            SingleDictionaryDetail dictionaryDetail2 = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getXueLi(), "Educational");

            result.setXueLi(dictionaryDetail2 != null ? dictionaryDetail2.getName() : "");
        }

        result.setXingMing(collectionIndividualAccountBasicVice.getXingMing());

        result.setGRCKZHHM(collectionIndividualAccountBasicVice.getGrckzhhm());

        result.setGRCKZHKHYHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

        result.setDWJBR(collectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getJbrxm());
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
            result.setSHR(cAuditHistory.getCzy());
        }
        String id = pdfService.getIndiAcctSetReceiptPdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;

    }

    // completed 已测试 - 正常
    @Override
    public ReIndiAcctSetRes reAcctSet(TokenContext tokenContext, String YWLSH, IndiAcctSetPut addIndiAcctSetPut) {

        //region //参数检查

        boolean allowNull = addIndiAcctSetPut.getCZLX().equals("0");

        if (!DateUtil.isFollowFormat(addIndiAcctSetPut.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "公积金首次汇缴年月");
        }

        if (!DateUtil.isFollowFormat(addIndiAcctSetPut.getGRXX().getCSNY()/*出生年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!StringUtil.isDigits(addIndiAcctSetPut.getGRZHXX().getGRJCJS() + ""/*个人缴存基数*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.isDigits(addIndiAcctSetPut.getGRXX().getJTYSR()/*家庭月收入*/ + "", true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "家庭月收入");
        }
        ;

        if (!DateUtil.isFollowFormat(addIndiAcctSetPut.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "公积金首次汇缴年月");
        }

        if (!DateUtil.isFollowFormat(addIndiAcctSetPut.getGRXX().getCSNY()/*出生年月*/, formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!StringUtil.isDigits(addIndiAcctSetPut.getGRZHXX().getGRJCJS() + ""/*个人缴存基数*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人存款账户号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getJBRZJLX()/*经办人证件类型*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件类型");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getJBRXM()/*经办人姓名*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人姓名");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getHYZK()/*婚姻状况*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "婚姻状况");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getZJLX()/*证件类型*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件类型");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getSJHM()/*手机号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getZJHM()/*证件号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getXMQP()/*姓名全拼*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名全拼");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getXingBie()/*性别*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "性别");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getGRXX().getXingMing()/*姓名*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getJBRZJHM()/*经办人证件号码*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件号码");
        }

        if (!StringUtil.notEmpty(addIndiAcctSetPut.getDWXX().getDWZH()/*单位账号*/, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位账号");
        }

        //endregion

        //region //必要参数声明&关系配置
        CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grywmx.ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null || collectionIndividualAccountBasicVice.getGrywmx().getExtension() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }


        if (!tokenContext.getUserInfo().getCZY().equals(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy())) {

            throw new ErrorException(ReturnEnumeration.Permission_Denied);

        }

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = collectionIndividualAccountBasicVice.getGrywmx();

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

        //endregion

        //region  //字段填充

        // GRZHXX
        collectionIndividualAccountBasicVice.setGjjschjny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSetPut.getGRZHXX().getGJJSCHJNY(), DateUtil.dbformatYear_Month)/*公积金首次汇缴年月*/);
        collectionIndividualAccountBasicVice.setGrckzhhm(addIndiAcctSetPut.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/);
        collectionIndividualAccountBasicVice.setGrjcjs(StringUtil.safeBigDecimal(addIndiAcctSetPut.getGRZHXX().getGRJCJS() + "")/*个人缴存基数*/);
        collectionIndividualAccountBasicVice.setGrckzhkhyhdm(addIndiAcctSetPut.getGRZHXX().getGRCKZHKHYHDM()/*个人存款账户开户银行代码*/);
        collectionIndividualAccountBasicVice.setGrckzhkhyhmc(addIndiAcctSetPut.getGRZHXX().getGRCKZHKHYHMC()/*个人存款账户开银行名称*/);


        // JBRZJLX
        collectionPersonalBusinessDetailsExtension.setJbrzjlx(addIndiAcctSetPut.getJBRZJLX()/*经办人证件类型*/);

        // BLZL
        collectionIndividualAccountBasicVice.setBlzl(addIndiAcctSetPut.getBLZL()/*办理资料*/);
        collectionPersonalBusinessDetailsExtension.setBlzl(addIndiAcctSetPut.getBLZL()/*办理资料*/);

        // JBRXM
        collectionPersonalBusinessDetailsExtension.setJbrxm(addIndiAcctSetPut.getJBRXM()/*经办人姓名*/);

        // GRXX
        collectionIndividualAccountBasicVice.setHyzk(addIndiAcctSetPut.getGRXX().getHYZK()/*婚姻状况*/);
        collectionIndividualAccountBasicVice.setJtzz(addIndiAcctSetPut.getGRXX().getJTZZ()/*家庭住址*/);
        collectionIndividualAccountBasicVice.setCsny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSetPut.getGRXX().getCSNY(), DateUtil.dbformatYear_Month/*出生年月*/));
        collectionIndividualAccountBasicVice.setZjlx(addIndiAcctSetPut.getGRXX().getZJLX()/*证件类型*/);
        collectionIndividualAccountBasicVice.setZhiYe(addIndiAcctSetPut.getGRXX().getZhiYe()/*职业*/);
        collectionIndividualAccountBasicVice.setYzbm(addIndiAcctSetPut.getGRXX().getYZBM()/*邮政编码*/);
        collectionIndividualAccountBasicVice.setZhiCheng(addIndiAcctSetPut.getGRXX().getZhiCheng()/*职称*/);
        collectionIndividualAccountBasicVice.setSjhm(addIndiAcctSetPut.getGRXX().getSJHM()/*手机号码*/);
        collectionIndividualAccountBasicVice.setZjhm(addIndiAcctSetPut.getGRXX().getZJHM()/*证件号码*/);
        collectionIndividualAccountBasicVice.setGddhhm(addIndiAcctSetPut.getGRXX().getGDDHHM()/*固定电话号码*/);
        collectionIndividualAccountBasicVice.setXmqp(addIndiAcctSetPut.getGRXX().getXMQP()/*姓名全拼*/);
        collectionIndividualAccountBasicVice.setXingBie(addIndiAcctSetPut.getGRXX().getXingBie()/*性别*/);
        collectionIndividualAccountBasicVice.setJtysr(StringUtil.safeBigDecimal(addIndiAcctSetPut.getGRXX().getJTYSR()/*家庭月收入*/ + ""));
        collectionIndividualAccountBasicVice.setXueLi(addIndiAcctSetPut.getGRXX().getXueLi()/*学历*/);
        collectionIndividualAccountBasicVice.setXingMing(addIndiAcctSetPut.getGRXX().getXingMing()/*姓名*/);
        collectionIndividualAccountBasicVice.setZhiWu(addIndiAcctSetPut.getGRXX().getZhiWu()/*职务*/);
        collectionIndividualAccountBasicVice.setDzyx(addIndiAcctSetPut.getGRXX().getYouXiang()/*邮箱*/);

        // YWWD
        collectionIndividualAccountBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);

        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("id", tokenContext.getUserInfo().getYWWD());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        collectionPersonalBusinessDetailsExtension.setYwwd(network/*业务网点*/);

        // JBRZJHM
        collectionPersonalBusinessDetailsExtension.setJbrzjhm(addIndiAcctSetPut.getJBRZJHM()/*经办人证件号码*/);

        // DWXX
        collectionIndividualAccountBasicVice.setDwzh(addIndiAcctSetPut.getDWXX().getDWZH()/*单位账号*/);

        // CZY
        collectionIndividualAccountBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
        collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        //endregion

        //region//唯一性验证

        if (addIndiAcctSetPut.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", collectionIndividualAccountBasicVice.getZjhm());

            //this.put("xingMing",collectionIndividualAccountBasicVice.getXingMing());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) { throw new ErrorException(e); }

        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "证件号码");
        }

        if (addIndiAcctSetPut.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("sjhm", collectionIndividualAccountBasicVice.getSjhm());

            //this.put("xingMing",collectionIndividualAccountBasicVice.getXingMing());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) { throw new ErrorException(e); }

        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "手机号码");
        }

        if (addIndiAcctSetPut.getCZLX().equals("1") && DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("collectionPersonalAccount.grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) { throw new ErrorException(e); }

        }) != null) {

            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "个人存款账户号码");
        }
        //endregion

        //region //修改状态
        CCollectionIndividualAccountBasicVice savedVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        if (savedVice == null || savedVice.getGrywmx() == null) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务记录");
        }


        StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.保存.getEvent());

            this.put("1", Events.通过.getEvent());

        }}.get(addIndiAcctSetPut.getCZLX()), new TaskEntity() {{

            this.setStatus(savedVice.getGrywmx().getExtension().getStep() == null ? "初始状态" : savedVice.getGrywmx().getExtension().getStep());
            this.setTaskId(YWLSH);
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            ;
            this.setNote("");
            this.setSubtype(BusinessSubType.归集_个人账户设立.getSubType());
            this.setType(BusinessType.Collection);
            this.setOperator(savedVice.getCzy());
            this.setWorkstation(savedVice.getYwwd());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) { throw new ErrorException(e); }

                if (!succeed || next == null) { return; }

                collectionPersonalBusinessDetailsExtension.setStep(next);


                if (StringUtil.isIntoReview(next, null)) {

                    collectionPersonalBusinessDetailsExtension.setDdsj(new Date());

                }

                DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(savedVice).save(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) { throw new ErrorException(e); }

                });

                if (next.equals(CollectionBusinessStatus.办结.getName())) {

                    doAcctSet(tokenContext, savedVice.getGrywmx().getYwlsh());
                }
            }
        });

        //endregion

        //region //在途验证

        if ((addIndiAcctSetPut.getCZLX().equals("1") && !DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria, "grywmx.cCollectionPersonalBusinessDetailsExtension.step"), (Collection) CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

                    @Override
                    public String tansform(CollectionBusinessStatus var1) {
                        return var1.getName();
                    }

                }))));

                criteria.add(Restrictions.or(
                        Restrictions.eq("zjhm", collectionIndividualAccountBasicVice.getZjhm()),
                        Restrictions.eq("sjhm", collectionIndividualAccountBasicVice.getSjhm()),
                        Restrictions.eq("grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm())
                ));
                criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode(),CollectionBusinessType.开户.getCode()));
                criteria.add(Restrictions.ne("grywmx.ywlsh", savedVice.getGrywmx().getYwlsh()));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        }))) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process, "已有人正在使用相同的 证件号码 或 手机号码 或 个人存款账户号码 办理开户或变更业务");
        }

        if (addIndiAcctSetPut.getCZLX().equals("1")) {

            this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);

            this.iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.开户.getName(), "修改");
        }

        //endregion

        return new ReIndiAcctSetRes() {{

            this.setYWLSH(YWLSH);

            this.setStatus("success");

        }};
    }

    // completed 已测试 - 正常
    @Override
    public GetIndiAcctSetRes showAcctSet(TokenContext tokenContext, String YWLSH) {

        //region // 检查参数
        if (YWLSH == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号");
        }

        //endregion

        //region // 必要字段查询&完整性验证
        CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grywmx.ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (collectionIndividualAccountBasicVice == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        final StCommonPerson person_exist;

        // 对应个人账户(开户成功的情况下)
        if (collectionIndividualAccountBasicVice.getGrzh() != null) {

            StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grzh", collectionIndividualAccountBasicVice.getGrzh());

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }

            });

            if (commonPerson == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
            }

            person_exist = commonPerson;

        } else {

            person_exist = null;
        }


        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", collectionIndividualAccountBasicVice.getDwzh());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CAuditHistory> list_auditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        return new GetIndiAcctSetRes() {{


            // 构建返回值
            this.setBLZL(collectionIndividualAccountBasicVice.getBlzl());

            if (commonUnit != null) {

                this.setJBRZJLX(commonUnit.getJbrzjlx());

                this.setJBRXM(commonUnit.getJbrxm());
            }

            this.setGRXX(new GetIndiAcctSetResGRXX() {{

                this.setHYZK(collectionIndividualAccountBasicVice.getHyzk());

                this.setJTZZ(collectionIndividualAccountBasicVice.getJtzz());

                this.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getCsny(), formatNY));

                this.setZJLX(collectionIndividualAccountBasicVice.getZjlx());

                this.setZhiYe(collectionIndividualAccountBasicVice.getZhiYe());

                this.setYZBM(collectionIndividualAccountBasicVice.getYzbm());

                this.setZhiCheng(collectionIndividualAccountBasicVice.getZhiCheng());

                this.setSJHM(collectionIndividualAccountBasicVice.getSjhm());

                this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

                this.setGDDHHM(collectionIndividualAccountBasicVice.getGddhhm());

                this.setXMQP(collectionIndividualAccountBasicVice.getXmqp());

                this.setXingBie(collectionIndividualAccountBasicVice.getXingBie());

                this.setJTYSR(collectionIndividualAccountBasicVice.getJtysr() + "");

                this.setXueLi(collectionIndividualAccountBasicVice.getXueLi());

                this.setXingMing(collectionIndividualAccountBasicVice.getXingMing());

                this.setZhiWu(collectionIndividualAccountBasicVice.getZhiWu());

                this.setYouXiang(collectionIndividualAccountBasicVice.getDzyx());
            }});

            this.setYWWD(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getYwwd().getMingCheng());

            if (commonUnit != null) {

                this.setJBRZJHM(commonUnit.getJbrzjhm());
            }

            this.setDWXX(new GetIndiAcctSetResDWXX() {{

                this.setYWLSH(YWLSH);

                this.setDWZH(collectionIndividualAccountBasicVice.getDwzh());

                if (commonUnit != null) {

                    this.setDWMC(commonUnit.getDwmc());

                    if (commonUnit.getCollectionUnitAccount() != null) {

                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonUnit.getCollectionUnitAccount().getJzny(), formatNY));
                    }
                }
            }});

            this.setGRZHXX(new GetIndiAcctSetResGRZHXX() {{

                this.setGRJCJS("" + collectionIndividualAccountBasicVice.getGrjcjs());

                this.setGJJSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGjjschjny(), formatNY));

                this.setGRCKZHKHYHMC(collectionIndividualAccountBasicVice.getGrckzhkhyhmc());

                if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && collectionIndividualAccountBasicVice.getGrjcjs() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null) {

                    this.setGRYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue()));
                }

                this.setGRZH(collectionIndividualAccountBasicVice.getGrzh());

                if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && collectionIndividualAccountBasicVice.getGrjcjs() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null) {

                    this.setGRJCBL("" + (commonUnit.getCollectionUnitAccount().getGrjcbl() == null ? 0 : commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")).doubleValue()));

                    this.setGRYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue()));

                    this.setDWJCBL("" + (commonUnit.getCollectionUnitAccount().getDwjcbl() == null ? 0 : commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))));

                    this.setDWYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue()));

                    this.setYJCE("" + collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue()) + collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue()));
                }
                if (person_exist != null && person_exist.getCollectionPersonalAccount() != null) {

                    this.setKHRQ(DateUtil.date2Str(person_exist.getCollectionPersonalAccount().getKhrq(), formatNY));

                    this.setGRZHZT(person_exist.getCollectionPersonalAccount().getGrzhzt());
                }

                this.setGRCKZHKHYHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

                this.setGRCKZHHM(collectionIndividualAccountBasicVice.getGrckzhhm());

            }});

            this.setCZY(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy());

            this.setReviewInfos(new ArrayList<>(CollectionUtils.flatmap(list_auditHistory, new CollectionUtils.Transformer<CAuditHistory, ReviewInfo>() {
                @Override
                public ReviewInfo tansform(CAuditHistory cAuditHistory) {

                    return new ReviewInfo() {{

                        this.setYWLSH(YWLSH);
                        this.setSHJG(cAuditHistory.getShjg());
                        this.setYYYJ(cAuditHistory.getYyyj());
                        this.setCZY(cAuditHistory.getCzy());
                        this.setYWWD(cAuditHistory.getYwwd());
                        this.setZhiWu(cAuditHistory.getZhiwu());
                        this.setCZQD(cAuditHistory.getCzqd());
                        this.setBeiZhu(cAuditHistory.getBeiZhu());

                    }};
                }
            })));
        }};

    }

    @Override
    public CommonResponses doInnerTransfer(TokenContext tokenContext, InnerTransferPost innerTransferPost) {

        //region //必要字段查询&正确性验证

        if (!DateUtil.isFollowFormat(innerTransferPost.getGJJSCHJNY(), "yyyy-MM", false)) {

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "公积金首次汇缴年月");
        }

        if (!StringUtil.isDigits(innerTransferPost.getGRJCJS(), false)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }
        StCommonUnit unit_current = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", innerTransferPost.getDWZH());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (unit_current == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息");
        }


        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", innerTransferPost.getZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");
                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (commonPerson == null || commonPerson.getZjhm() == null || commonPerson.getCollectionPersonalAccount() == null || commonPerson.getCollectionPersonalAccount().getGrckzhhm() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
        }

        if (commonPerson.getUnit().getDwzh().equals(innerTransferPost.getDWZH())) {

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位账号不能与原单位账号一致");
        }

        if (!commonPerson.getXingMing().equals(innerTransferPost.getXingMing()) || !commonPerson.getCollectionPersonalAccount().getGrckzhhm().equals(innerTransferPost.getGRCKZHHM())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "姓名或个人存款账户号码不一致");
        }

        if (!PersonAccountStatus.封存.getCode().equals(commonPerson.getCollectionPersonalAccount().getGrzhzt())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "原单位账号未封存");

        }

        List<StCommonPolicy> list_commonPolicy = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", Arrays.asList("YJCESX", "YJCEXX"));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        StCommonPolicy commonPolicy = new StCommonPolicy();

        list_commonPolicy.forEach(new Consumer<StCommonPolicy>() {
            @Override
            public void accept(StCommonPolicy stCommonPolicy) {

                if ("YJCESX".equals(stCommonPolicy.getId())) {

                    commonPolicy.setYjcesx(stCommonPolicy.getYjcesx() == null ? BigDecimal.ZERO : stCommonPolicy.getYjcesx());
                }

                if ("YJCEXX".equals(stCommonPolicy.getId())) {

                    commonPolicy.setYjcexx(stCommonPolicy.getYjcexx() == null ? BigDecimal.ZERO : stCommonPolicy.getYjcexx());
                }
            }
        });
        BigDecimal yjce = unit_current.getCollectionUnitAccount().getGrjcbl().multiply(StringUtil.safeBigDecimal(innerTransferPost.getGRJCJS())).add(unit_current.getCollectionUnitAccount().getDwjcbl().multiply(StringUtil.safeBigDecimal(innerTransferPost.getGRJCJS())));

        if (yjce.compareTo(commonPolicy.getYjcesx()) > 0 || yjce.compareTo(commonPolicy.getYjcexx()) < 0) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "当前月缴存额" + yjce.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "不在" + commonPolicy.getYjcexx().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "~" + commonPolicy.getYjcesx().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "区间内 请重新调整个人缴存基数");
        }

        if (DateUtil.safeStr2Date("yyyy-MM", innerTransferPost.getGJJSCHJNY()) != null && DateUtil.safeStr2Date("yyyy-MM", innerTransferPost.getGJJSCHJNY()).getTime() < DateUtil.safeStr2Date(DateUtil.dbformatYear_Month, unit_current.getExtension().getDwschjny()).getTime()) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人在新单位首次汇缴年月不能小于单位首次汇缴年月 "+DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,unit_current.getExtension().getDwschjny(),formatNY));
        }


        CAccountNetwork YWWD = DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", tokenContext.getUserInfo().getYWWD());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        //region //在途验证
        if(!DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.eq("grzh",commonPerson.getGrzh()));
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", (Collection) Arrays.asList(CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode())));
                criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.已作废.getName())));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        })){

            throw new ErrorException(ReturnEnumeration.Business_In_Process,"证件号码"+commonPerson.getZjhm()+"正在办理提取业务");
        }
        //endregion

        //region //必要字段声明&关系配置
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
        collectionPersonalBusinessDetails.setPerson(commonPerson);

        StCommonUnit unit_original = commonPerson.getUnit();
        StCollectionPersonalAccount personalAccount = commonPerson.getCollectionPersonalAccount();

        CCollectionIndividualAccountTransferNewVice collectionIndividualAccountTransferNewVice = new CCollectionIndividualAccountTransferNewVice();
        collectionPersonalBusinessDetails.setIndividualAccountTransferNewVice(collectionIndividualAccountTransferNewVice);
        collectionIndividualAccountTransferNewVice.setGrywmx(collectionPersonalBusinessDetails);
        //endregion

        //region //转移业务
        collectionPersonalBusinessDetails.setFse(BigDecimal.ZERO);
        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.内部转移.getCode());
        collectionPersonalBusinessDetails.setGrzh(commonPerson.getGrzh());
        collectionPersonalBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());
        collectionPersonalBusinessDetailsExtension.setBjsj(new Date());
        collectionPersonalBusinessDetailsExtension.setSlsj(new Date());
        collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());
        collectionPersonalBusinessDetailsExtension.setYwwd(YWWD);
        collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.内部转移.getCode());


        collectionIndividualAccountTransferNewVice.setGrzh(commonPerson.getGrzh());  //职工个人账号
        collectionIndividualAccountTransferNewVice.setZcdw(unit_original/*转出单位账号*/);
        collectionIndividualAccountTransferNewVice.setZrdw(unit_current);  //转入单位账号
        collectionIndividualAccountTransferNewVice.setYgrjcjs(personalAccount.getGrjcjs() + ""/*原单位下个人月缴存基数*/);
        collectionIndividualAccountTransferNewVice.setYdwyjce(personalAccount.getDwyjce() + ""/*原单位下单位月缴存额*/);
        collectionIndividualAccountTransferNewVice.setYgryjce(personalAccount.getGryjce() + ""/*原单位下个人月缴存额*/);
        collectionIndividualAccountTransferNewVice.setZysgrzhye(personalAccount.getGrzhye() + ""/*转移时个人账户余额*/);
        collectionIndividualAccountTransferNewVice.setZysdngjye(personalAccount.getGrzhdngjye() + ""/*转移时个人账户当年归集余额*/);
        collectionIndividualAccountTransferNewVice.setZyssnjzye(personalAccount.getGrzhsnjzye()/*转移时个人账户上年结转余额）*/);
        collectionIndividualAccountTransferNewVice.setSxny(getTransferSxny(commonPerson.getGrzh(), unit_original.getDwzh()));


        StCollectionPersonalBusinessDetails savedBusiness = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        //endregion

        //region //转移单位

        CCommonPersonExtension personExtension = commonPerson.getExtension();
        personExtension.setGjjschjny(ComUtils.parseToYYYYMM(innerTransferPost.getGJJSCHJNY()));
        personalAccount.setGrjcjs(StringUtil.safeBigDecimal(innerTransferPost.getGRJCJS()));
        personalAccount.setGryjce(BusUtils.computeDeposit(personalAccount.getGrjcjs(), unit_current.getCollectionUnitAccount().getGrjcbl()));
        personalAccount.setDwyjce(BusUtils.computeDeposit(personalAccount.getGrjcjs(), unit_current.getCollectionUnitAccount().getDwjcbl()));
        personalAccount.setGrzhzt(PersonAccountStatus.正常.getCode());
        commonPerson.setUnit(unit_current);
        commonPersonDAO.update(commonPerson);

        //暂时加入限制：转移不产生补缴的情况
        checkGjjschjny2(commonPerson, innerTransferPost.getGJJSCHJNY());
        //endregion

        //region //原单位业务
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails_original = new StCollectionUnitBusinessDetails();

        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension_original = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetails_original.setExtension(collectionUnitBusinessDetailsExtension_original);

        collectionUnitBusinessDetails_original.setDwzh(unit_original.getDwzh());
        collectionUnitBusinessDetails_original.setUnit(unit_original);
        collectionUnitBusinessDetails_original.setYwmxlx(CollectionBusinessType.内部转移.getCode());
        collectionUnitBusinessDetails_original.setFse(commonPerson.getCollectionPersonalAccount().getGrzhye().multiply(new BigDecimal("-1")));
        collectionUnitBusinessDetails_original.setFslxe(BigDecimal.ZERO);
        collectionUnitBusinessDetails_original.setCzbz(CommonFieldType.非冲账.getCode());
        collectionUnitBusinessDetails_original.setJzrq(new Date());


        collectionUnitBusinessDetailsExtension_original.setBjsj(new Date());
        collectionUnitBusinessDetailsExtension_original.setStep(CollectionBusinessStatus.办结.getName());
        collectionUnitBusinessDetailsExtension_original.setSlsj(new Date());
        collectionUnitBusinessDetailsExtension_original.setCzy(tokenContext.getUserInfo().getCZY());
        collectionUnitBusinessDetailsExtension_original.setYwwd(YWWD);
        collectionUnitBusinessDetailsExtension_original.setBeizhu("内部转移-" + commonPerson.getGrzh());
        collectionUnitBusinessDetailsExtension_original.setCzmc(CollectionBusinessType.内部转移.getCode());

        collectionUnitBusinessDetails_original.getUnit().getCollectionUnitAccount().setDwzhye(unit_original.getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails_original.getFse()));

        DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails_original).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //现单位业务
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails_current = new StCollectionUnitBusinessDetails();

        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension_current = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetails_current.setExtension(collectionUnitBusinessDetailsExtension_current);

        collectionUnitBusinessDetails_current.setDwzh(unit_current.getDwzh());
        collectionUnitBusinessDetails_current.setUnit(unit_current);
        collectionUnitBusinessDetails_current.setYwmxlx(CollectionBusinessType.内部转移.getCode());
        collectionUnitBusinessDetails_current.setFse(commonPerson.getCollectionPersonalAccount().getGrzhye());
        collectionUnitBusinessDetails_current.setFslxe(BigDecimal.ZERO);
        collectionUnitBusinessDetails_current.setCzbz(CommonFieldType.非冲账.getCode());
        collectionUnitBusinessDetails_current.setJzrq(new Date());


        collectionUnitBusinessDetailsExtension_current.setBjsj(new Date());
        collectionUnitBusinessDetailsExtension_current.setStep(CollectionBusinessStatus.办结.getName());
        collectionUnitBusinessDetailsExtension_current.setSlsj(new Date());
        collectionUnitBusinessDetailsExtension_current.setCzy(tokenContext.getUserInfo().getCZY());
        collectionUnitBusinessDetailsExtension_current.setYwwd(YWWD);
        collectionUnitBusinessDetailsExtension_current.setBeizhu("内部转移-" + commonPerson.getGrzh());
        collectionUnitBusinessDetailsExtension_current.setCzmc(CollectionBusinessType.内部转移.getCode());

        unit_current.getCollectionUnitAccount().setDwzhye(unit_current.getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails_current.getFse()));

        DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails_current).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        //endregion

        //region //补缴检测
        Date gjjschjny = ComUtils.parseToDate(innerTransferPost.getGJJSCHJNY(),"yyyyMM");
        String dwzh = innerTransferPost.getDWZH();
        CCollectionPersonalBusinessDetailsExtension extension = savedBusiness.getExtension();
        Map map = new HashMap();
        map.put("ywwd",extension.getYwwd());
        map.put("czy",extension.getCzy());
        map.put("token",tokenContext);
        doAcctSetRelation(commonPerson,dwzh,gjjschjny,map);

        //endregion

        //region //财务入账

        this.voucherManagerService.addVoucher("毕节市住房公积金管理中心", tokenContext.getUserInfo().getCZY(), tokenContext.getUserInfo().getCZY(), "", "",
            VoucherBusinessType.转移合户.getCode(),
            VoucherBusinessType.转移合户.getCode(), savedBusiness.getYwlsh(), unit_original.getDwmc() + commonPerson.getXingMing() + "转移到" + unit_current.getDwmc(),
            commonPerson.getCollectionPersonalAccount().getGrzhye(), null, null);
        this.iSaveAuditHistory.saveNormalBusiness(savedBusiness.getYwlsh(), tokenContext, CollectionBusinessType.内部转移.getName(), "办结");
        //endregion

        return new CommonResponses() {{

            this.setId(savedBusiness.getYwlsh());
            this.setState("success");
        }};

    }

    private void checkGjjschjny2(StCommonPerson commonPerson, String gjjschjny) {
        Date grschjny = ComUtils.parseToDate(gjjschjny, "yyyy-MM");
        String grjznyStr = commonPerson.getExtension().getGrjzny();
        Date grjzny = ComUtils.parseToDate(grjznyStr, "yyyyMM");
        String dwjznyStr = commonPerson.getUnit().getCollectionUnitAccount().getJzny();
        Date dwjzny = ComUtils.parseToDate(dwjznyStr, "yyyyMM");

        if (!ComUtils.isEmpty(grjzny) && grschjny.compareTo(grjzny) <= 0) {
            throw new ErrorException("个人在新单位的首次汇缴年月必须大于个人的缴至年月"+DateUtil.date2Str(grjzny,formatNY));
        }
        /*if (!ComUtils.isEmpty(dwjzny) && grschjny.compareTo(dwjzny) <= 0) {
            throw new ErrorException("个人在新单位的首次汇缴年月在必须大于单位的缴至年月"+DateUtil.date2Str(dwjzny,formatNY));
        }*/

    }

    /**
     * 查看个人在该单位下的最后一次封存的生效年月
     */
    private String getTransferSxny(String grzh, String zcdw) {
        CCollectionIndividualAccountActionVice vice = collectionIndividualAccountActionViceDAO.getNearlySeal(grzh, zcdw);
        if (!ComUtils.isEmpty(vice)) {
            return vice.getSxny();
        }
        //该人没有封存信息,数据有误
        throw new ErrorException("该人转移前没有封存信息,数据有误!");
    }

    @Override
    public void doAcctSet(TokenContext tokenContext, String ywlsh) {

        CCollectionIndividualAccountBasicVice personSet = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grywmx.ywlsh", ywlsh);

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personSet == null || personSet.getGrywmx() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");

        }
        String dwzh = personSet.getDwzh();
        StCommonUnit commonUnit = commonUnitDAO.getUnit(dwzh);

        String zjhm = personSet.getZjhm();
        String zjlx = personSet.getZjlx();

        //先保存个人基础信息
        StCommonPerson commonPerson = new StCommonPerson();
        commonPerson.setGrzh(zjhm);
        commonPerson.setXingMing(personSet.getXingMing());
        commonPerson.setXmqp(personSet.getXmqp());
        commonPerson.setXingBie(personSet.getXingBie().toCharArray()[0]);
        commonPerson.setGddhhm(personSet.getGddhhm());
        commonPerson.setSjhm(personSet.getSjhm());
        commonPerson.setZjlx(zjlx);
        commonPerson.setZjhm(zjhm);
        commonPerson.setCsny(personSet.getCsny());
        commonPerson.setHyzk(personSet.getHyzk());
        commonPerson.setZhiYe(personSet.getZhiYe());
        commonPerson.setZhiChen(personSet.getZhiCheng());
        commonPerson.setZhiWu(personSet.getZhiWu());
        commonPerson.setXueLi(personSet.getXueLi());
        commonPerson.setYzbm(personSet.getYzbm());
        commonPerson.setJtzz(personSet.getJtzz());
        commonPerson.setJtysr(personSet.getJtysr());
        commonPerson.setUnit(commonUnit);
        //个人账户信息
        StCollectionPersonalAccount personalAccount = new StCollectionPersonalAccount();
        personalAccount.setGrzh(personalAccount.getGrzh());
        BigDecimal grjcjs = personSet.getGrjcjs();

        personalAccount.setGrjcjs(grjcjs != null ? grjcjs : new BigDecimal("0"));
        personalAccount.setGrzhzt(personalAccount.getGrzhzt() != null ? personalAccount.getGrzhzt() : PersonAccountStatus.正常.getCode());
        personalAccount.setKhrq(new Date());
        personalAccount.setGrzhye(new BigDecimal(0 + ""));
        personalAccount.setGrzhsnjzye(new BigDecimal(0 + ""));
        personalAccount.setGrzhdngjye(new BigDecimal(0 + ""));

        BigDecimal dwjcbl = commonUnit.getCollectionUnitAccount().getDwjcbl();
        BigDecimal grjcbl = commonUnit.getCollectionUnitAccount().getGrjcbl();
        AssertUtils.notEmpty(dwjcbl, "单位缴存比例意外为空");
        AssertUtils.notEmpty(grjcbl, "个人缴存比例意外为空");

        personalAccount.setGryjce(BusUtils.computeDeposit(grjcjs, grjcbl));
        personalAccount.setDwyjce(BusUtils.computeDeposit(grjcjs, dwjcbl));

        personalAccount.setXhrq(personalAccount.getXhrq()/*开户不填*/);
        personalAccount.setXhyy(personalAccount.getXhyy()/*开户不填*/);
        personalAccount.setGrckzhhm(personSet.getGrckzhhm());
        personalAccount.setGrckzhkhyhmc(personSet.getGrckzhkhyhmc());
        personalAccount.setGrckzhkhyhdm(personSet.getGrckzhkhyhdm());
        commonPerson.setCollectionPersonalAccount(personalAccount);
        //个人拓展信息
        CCommonPersonExtension commonPersonExtension = new CCommonPersonExtension();
        commonPersonExtension.setGjjschjny(personSet.getGjjschjny());
        commonPersonExtension.setDzyx(personSet.getDzyx());
        commonPersonExtension.setSfdj("01");
        commonPersonExtension.setGrzl(personSet.getBlzl());
        commonPerson.setExtension(commonPersonExtension);
        commonPersonDAO.save(commonPerson);

        commonPerson.setGrzh(personalAccount.getGrzh());//触发器生成个人账号

        commonPersonDAO.update(commonPerson);

        StCollectionPersonalBusinessDetails grywmx = personSet.getGrywmx();
        grywmx.setPerson(commonPerson);
        grywmx.setGrzh(personalAccount.getGrzh());
        CCollectionPersonalBusinessDetailsExtension busExtension = grywmx.getExtension();
        busExtension.setBjsj(new Date());
        personSet.setGrzh(personalAccount.getGrzh());    //??

        collectionIndividualAccountBasicViceDAO.update(personSet);

        Date gjjschjny = ComUtils.parseToDate(personSet.getGjjschjny(), "yyyyMM");
        CCollectionPersonalBusinessDetailsExtension extension = personSet.getGrywmx().getExtension();
        Map map = new HashMap();
        map.put("ywwd", extension.getYwwd());
        map.put("czy", extension.getCzy());
        map.put("token", tokenContext);
        doAcctSetRelation(commonPerson, dwzh, gjjschjny, map);

        RpcAuth rpcAuth = new RpcAuth();
        rpcAuth.setEmail(commonPerson.getExtension().getDzyx());
        rpcAuth.setPassword(zjhm.length() <= 8 ? zjhm : zjhm.substring(zjhm.length() - 8));
        rpcAuth.setType(1);
        rpcAuth.setUser_id(commonPerson.getCollectionPersonalAccount().getId());
        rpcAuth.setUsername(zjhm);
        rpcAuth.setState(1);

        this.iSaveAuditHistory.saveNormalBusiness(ywlsh, tokenContext, CollectionBusinessType.开户.getName(), "办结");
        /*if(true){
            throw new ErrorException("test！");
		}*/

        Msg rpcMsg = accountRpcService.registerAuth(ResUtils.noneAdductionValue(RpcAuth.class, rpcAuth));
        if (ReturnCode.Success != rpcMsg.getCode()) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, rpcMsg.getMsg());
        }


    }

    private void doAcctSetRelation(StCommonPerson commonPerson, String DWZh, Date gjjschjny, Map map) {
        //公积金首次汇缴年月与单位的应汇缴年月对比,前置条件:单位汇缴过程中不能进行开户操作
        //3种情况，1、产生补缴 2、无操作 3、封存启封操作
//		String gjjschjnyStr = commonPerson.getExtension().getGjjschjny();// accSet.getGjjschjny();
//		Date gjjschjny = ComUtils.parseToDate(gjjschjnyStr,"yyyyMM");
        BusUtils.checkRemittanceDoing(DWZh);
        String yhjnyStr = BusUtils.getDWYHJNY(DWZh);
        Date yhjny = ComUtils.parseToDate(yhjnyStr, "yyyy-MM");
        //falg -1补缴 0无操作 1启封再封存
        int flag = gjjschjny.compareTo(yhjny);
        if (flag < 0) {
            //开户业务可能产生补缴数据 杨凡 2017-8-17
            this.unitPayback.checkPersonAccSet(commonPerson.getGrzh(), map);
        }
        //更新单位账户信息
        BusUtils.refreshUnitAcount(DWZh);
    }

    private void isCollectionIndividualAccountBasicViceAvailable(CCollectionIndividualAccountBasicVice
                                                                     collectionIndividualAccountBasicVice) {

        List<StCommonPolicy> list_commonPolicy = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", Arrays.asList("YJCESX", "YJCEXX"));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        StCommonPolicy commonPolicy = new StCommonPolicy();

        list_commonPolicy.forEach(new Consumer<StCommonPolicy>() {
            @Override
            public void accept(StCommonPolicy stCommonPolicy) {

                if ("YJCESX".equals(stCommonPolicy.getId())) {

                    commonPolicy.setYjcesx(stCommonPolicy.getYjcesx() == null ? BigDecimal.ZERO : stCommonPolicy.getYjcesx());
                }

                if ("YJCEXX".equals(stCommonPolicy.getId())) {

                    commonPolicy.setYjcexx(stCommonPolicy.getYjcexx() == null ? BigDecimal.ZERO : stCommonPolicy.getYjcexx());
                }
            }
        });

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionIndividualAccountBasicVice.getGrywmx().getExtension();

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGjjschjny())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "公积金首次汇缴年月");
        }

        if (DateUtil.safeStr2Date(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGjjschjny()).compareTo(DateUtil.safeStr2Date(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGrywmx().getUnit().getExtension().getDwschjny())) < 0) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人的首次汇缴年月不能小于单位首次汇缴年月:" + DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionIndividualAccountBasicVice.getGrywmx().getUnit().getExtension().getDwschjny(), formatNY));
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhhm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人存款账户号码");
        }

        if (collectionIndividualAccountBasicVice.getGrjcjs() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人缴存基数");


        } else {

            BigDecimal yjce = collectionIndividualAccountBasicVice.getGrywmx().getUnit().getCollectionUnitAccount().getGrjcbl().multiply(collectionIndividualAccountBasicVice.getGrjcjs()).add(collectionIndividualAccountBasicVice.getGrywmx().getUnit().getCollectionUnitAccount().getDwjcbl().multiply(collectionIndividualAccountBasicVice.getGrjcjs()));

            if (yjce.compareTo(commonPolicy.getYjcesx()) > 0 || yjce.compareTo(commonPolicy.getYjcexx()) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "当前月缴存额" + yjce.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "不在" + commonPolicy.getYjcexx().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "~" + commonPolicy.getYjcesx().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "区间内 请重新调整个人缴存基数");
            }
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhkhyhdm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人存款账户开户银行代码");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhkhyhmc())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人存款账户开银行名称");
        }

        if (!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrzjlx())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "经办人证件类型");
        }

        if (!this.iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.个人账户设立.getCode(), collectionIndividualAccountBasicVice.getBlzl())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "办理资料");
        }
        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getBlzl())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");}

		/*if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getBlzl())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");}*/

        if (!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrxm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "经办人姓名");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getHyzk())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "婚姻状况");
        }

        if (!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("MaritalStatus"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) {
                return var1.getCode();
            }

        }).contains(collectionIndividualAccountBasicVice.getHyzk())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "婚姻状况");
        }

        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getJtzz())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"家庭住址");}

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getCsny())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "出生年月");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZjlx())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "证件类型");
        }

        if (!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) {
                return var1.getCode();
            }

        }).contains(collectionIndividualAccountBasicVice.getZjlx())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "证件类型");
        }
        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiYe())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "职业");
        }

        if (!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Vocation"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) {
                return var1.getCode();
            }

        }).contains(collectionIndividualAccountBasicVice.getZhiYe())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "职业");
        }
        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getYzbm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"邮政编码");}

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiCheng())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "职称");
        }

        if (collectionIndividualAccountBasicVice.getZhiCheng().length() != 3) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "职称");
        }
        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getSjhm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "手机号码");
        }

        if (collectionIndividualAccountBasicVice.getSjhm().length() != 11) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "手机号码");
        }
        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZjhm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "证件号码");
        }

        if (PersonCertificateType.身份证.getCode().equals(collectionIndividualAccountBasicVice.getZjlx()) && !IdcardValidator.isValidatedAllIdcard(collectionIndividualAccountBasicVice.getZjhm())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "身份证号码");
        }

        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGddhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"固定电话号码");}

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXmqp())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "姓名全拼");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXingBie())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "性别");
        }

        if (!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Gender"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) {
                return var1.getCode();
            }

        }).contains(collectionIndividualAccountBasicVice.getXingBie())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "性别");
        }
        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getJtysr())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"家庭月收入");}

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXueLi())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "学历");
        }

        if (collectionIndividualAccountBasicVice.getXueLi().length() != 3 && !CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Educational"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) {
                return var1.getCode();
            }

        }).contains(collectionIndividualAccountBasicVice.getXueLi())) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "学历");
        }
        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXingMing())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "姓名");
        }

        if (collectionIndividualAccountBasicVice.getXingMing().contains(" ")) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "姓名不能包含空格");
        }
        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiWu())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "职务");
        }

        if (collectionIndividualAccountBasicVice.getZhiWu().length() != 4) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "职务");
        }
        //if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getDzyx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"邮箱");}

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getYwwd())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务网点");
        }

//		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

        if (!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrzjhm())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "经办人证件号码");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getDwzh())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号");
        }

        if (!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getCzy())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "操作员");
        }

        if (!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getCzy())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "操作员");
        }
    }


    //region //过时
    @Override
    public PersonBaseMessage getIndiAcctSetCheck(TokenContext tokenContext, String xingMing, String zjlx, String
        zjhm) {
        return getIndiAcctSetCheck(xingMing, zjlx, zjhm);
    }

    private PersonBaseMessage getIndiAcctSetCheck(String xingMing, String zjlx, String zjhm) {

        //1、验证证件号码格式
        AssertUtils.isIdentification(zjhm);
        //2、查询系统是否已存在该身份证件号码、证件类型、姓名是否匹配
        StCommonPerson person = commonPersonDAO.getPerson(zjhm, zjlx);
        if (person == null) {        //不存在，正常开户
            return null;
        }
        //3、对姓名验证
        String name = person.getXingMing();
        if (!name.equals(xingMing)) {
            throw new ErrorException("当前输入的姓名与该人的已存在数据中的姓名不匹配！");
        }

        //4、验证是否为封存状态，如果已存在，个人账户状态是正常，抛出错误提示，不允许办理
        String grzhzt = person.getCollectionPersonalAccount().getGrzhzt();
        if ("01".equals(grzhzt)) {
            throw new ErrorException("当前的职工在单位[" + person.getUnit().getDwzh() + "]下已存在账户，个人账户状态为正常，当前不能开户转移！");
        }
        //5、封存状态，能办理，开户分支：内部转移；反显账户信息
        PersonBaseMessage basePerson = new PersonBaseMessage();
        setBasePerson(basePerson, person);
        return basePerson;
    }

    private void setBasePerson(PersonBaseMessage basePerson, StCommonPerson person) {
        basePerson.setXingMing(person.getXingMing());
        basePerson.setGRZH(person.getGrzh());
        basePerson.setZJHM(person.getZjhm());
        basePerson.setZJLX(person.getZjlx());

        basePerson.setCSNY(person.getCsny());
        basePerson.setGDDHHM(person.getGddhhm());
        basePerson.setHYZK(person.getHyzk());
        basePerson.setJTYSR("" + person.getJtysr());
        basePerson.setJTZZ(person.getJtzz());
        basePerson.setSJHM(person.getSjhm());
        basePerson.setXingBie(person.getXingBie().toString());
        basePerson.setXMQP(person.getXmqp());
        basePerson.setXueLi(person.getXueLi());
        basePerson.setYouXiang(person.getExtension().getDzyx());
        basePerson.setYZBM(person.getYzbm());
        basePerson.setZhiCheng(person.getZhiChen());
        basePerson.setZhiWu(person.getZhiWu());
        basePerson.setZhiYe(person.getZhiYe());
    }

    @Deprecated
    private boolean isInnerTransferAvailable(String zjhm, String ximgming, String dqdwzh) {

        //身份验证
        if (!DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", zjhm);
            this.put("xingMing", ximgming);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {


                criteria.add(Restrictions.ne(CriteriaUtils.addAlias(criteria, "collectionPersonalAccount.grzhzt"), PersonAccountStatus.封存.getCode()));
                criteria.add(Restrictions.ne(CriteriaUtils.addAlias(criteria, "unit.dwzh"), dqdwzh));
            }

        }).isUnique(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        })) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "在其他单位不为封存状态");
        }

        return true;
    }

    @Deprecated
    private void addCheck(IndiAcctSetPost addIndiAcctSet) {
        String gjjschjny = addIndiAcctSet.getGRZHXX().getGJJSCHJNY();

        if (!StringUtil.notEmpty(gjjschjny)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "个人公积金首次汇缴年月");
        }

        String dwzh = addIndiAcctSet.getDWXX().getDWZH();
        StCommonUnit unit = commonUnitDAO.getUnit(dwzh);
        String dwschjny = unit.getExtension().getDwschjny();

        Date grschj = ComUtils.parseToDate(gjjschjny, "yyyy-MM");
        Date dwschj = ComUtils.parseToDate(dwschjny, "yyyyMM");

        if (grschj.compareTo(dwschj) < 0) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人的首次汇缴年月：" + gjjschjny + "不能小于单位首次汇缴年月:" + dwschjny);
        }
    }

    @Deprecated
    private void addAcctSetparamCheck(IndiAcctSetPost addIndiAcctSet) {
        if (ComUtils.isEmpty(addIndiAcctSet)) {
            throw new ErrorException("传入的参数不能为空！");
        }
        if (ComUtils.isEmpty(addIndiAcctSet.getCZLX())) {
            throw new ErrorException("操作类型不能为空！");
        }
        //个人缴存信息
        IndiAcctSetPostGRZHXX grjcxx = addIndiAcctSet.getGRZHXX();
        if (ComUtils.isEmpty(grjcxx)) {
            throw new ErrorException("个人缴存信息不能为空！");
        } else if (ComUtils.isEmpty(grjcxx.getGRJCJS())) {
            throw new ErrorException("个人缴存基数不能为空！");
        } else if (ComUtils.isEmpty(grjcxx.getGRCKZHHM())) {
            throw new ErrorException("个人存款账户号码不能为空！");
        } else if (ComUtils.isEmpty(grjcxx.getGJJSCHJNY())) {
            throw new ErrorException("公积金首次汇缴年月不能为空！");
        }
        //个人基本信息
        IndiAcctSetPostGRXX grxx = addIndiAcctSet.getGRXX();
        if (ComUtils.isEmpty(grxx)) {
            throw new ErrorException("个人信息不能为空！");
        } else if (ComUtils.isEmpty(grxx.getXingMing())) {
            throw new ErrorException("个人姓名不能为空！");
        } else if (ComUtils.isEmpty(grxx.getXMQP())) {
            throw new ErrorException("个人姓名全拼不能为空！");
        } else if (ComUtils.isEmpty(grxx.getZJLX())) {
            throw new ErrorException("证件类型不能为空！");
        } else if (ComUtils.isEmpty(grxx.getZJHM())) {
            throw new ErrorException("证件号码不能为空！");
        } else if (ComUtils.isEmpty(grxx.getXingBie())) {
            throw new ErrorException("性别不能为空！");
        } else if (ComUtils.isEmpty(grxx.getCSNY())) {
            throw new ErrorException("出生年月不能为空！");
        } else if (ComUtils.isEmpty(grxx.getHYZK())) {
            throw new ErrorException("婚姻状况不能为空！");
        } else if (ComUtils.isEmpty(grxx.getSJHM())) {
            throw new ErrorException("手机号码不能为空！");
        }
        //单位账号
        if (ComUtils.isEmpty(addIndiAcctSet.getDWXX())) {
            throw new ErrorException("单位信息不能为空！");
        } else if (ComUtils.isEmpty(addIndiAcctSet.getDWXX().getDWZH())) {
            throw new ErrorException("单位账号不能为空！");
        }

    }

    @Deprecated
    private void updateAuth(String zjhm) {
        StCommonPerson person = commonPersonDAO.getByGrzh(zjhm);
        String id = person.getCollectionPersonalAccount().getId();

        RpcAuth rpcAuth = new RpcAuth();
        //rpcAuth.setEmail(person.getExtension().getDzyx());
        rpcAuth.setType(1);
        rpcAuth.setUser_id(id);
        rpcAuth.setUsername(zjhm);
        rpcAuth.setState(1);

        Msg rpcMsg = accountRpcService.updateAuth(id, ResUtils.noneAdductionValue(RpcAuth.class, rpcAuth));
        if (ReturnCode.Success != rpcMsg.getCode()) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, rpcMsg.getMsg());
        }
    }

    @Deprecated
    @Override
    public SubmitIndiAcctSetRes submitAcctSet(TokenContext tokenContext, List<String> YWLSHJH) {


        if (true) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "开户批量提交功能已取消");

        }
        //region // 检查参数

        if (YWLSHJH == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        //endregion

        ArrayList<Exception> exceptions = new ArrayList<>();

        for (String YWLSH : YWLSHJH) {

            //region //必要数据查询&完整性验证
            CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grywmx.ywlsh", YWLSH);

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    exceptions.add(e);
                }

            });

            if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null || collectionIndividualAccountBasicVice.getGrywmx().getExtension() == null) {


                return new SubmitIndiAcctSetRes() {{

                    this.setSBYWLSH(YWLSH);

                    this.setStatus("fail");
                }};
            }

            try {

                this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);
                this.iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.开户.getName(), "修改");
            } catch (Exception e) {

                return new SubmitIndiAcctSetRes() {{

                    this.setSBYWLSH(YWLSH);

                    this.setStatus("fail");
                }};
            }

            //endregion

            //region//唯一性验证
            StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("zjhm", collectionIndividualAccountBasicVice.getZjhm());

                this.put("xingMing", collectionIndividualAccountBasicVice.getXingMing());

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override

                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            if (commonPerson != null && (commonPerson.getUnit() != null && commonPerson.getUnit().getDwzh() != null && (commonPerson.getUnit().getDwzh() + "").equals(collectionIndividualAccountBasicVice.getDwzh() + ""))) {

                throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "个人账号");
            }
            //endregion

            //region //修改状态
            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = collectionIndividualAccountBasicVice.getGrywmx();

            CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

            StateMachineUtils.updateState(this.stateMachineService, Events.通过.getEvent(), new TaskEntity() {{

                this.setStatus(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep() == null ? "初始状态" : collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep());
                this.setTaskId(YWLSH);
                this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                ;
                this.setNote("");
                this.setSubtype(BusinessSubType.归集_个人账户设立.getSubType());
                this.setType(BusinessType.Collection);
                this.setOperator(collectionIndividualAccountBasicVice.getCzy());
                this.setWorkstation(collectionIndividualAccountBasicVice.getYwwd());

            }}, new StateMachineUtils.StateChangeHandler() {

                @Override
                public void onStateChange(boolean succeed, String next, Exception e) {

                    if (e != null) {
                        exceptions.add(e);
                    }

                    if (!succeed || next == null || e != null) {
                        return;
                    }

                    collectionPersonalBusinessDetailsExtension.setStep(next);


                    if (StringUtil.isIntoReview(next, null)) {

                        collectionPersonalBusinessDetailsExtension.setDdsj(new Date());

                    }

                    DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).save(new DAOBuilder.ErrorHandler() {

                        @Override
                        public void error(Exception e) {
                            exceptions.add(e);
                        }
                    });

                    if (next.equals(CollectionBusinessStatus.办结.getName())) {

                        try {

                            doAcctSet(tokenContext, collectionPersonalBusinessDetails.getYwlsh());

                        } catch (Exception ex) {

                            exceptions.add(ex);
                        }
                    }
                }
            });

            if (exceptions.size() != 0) {

                return new SubmitIndiAcctSetRes() {{

                    this.setSBYWLSH(YWLSH);
                    this.setStatus("fail");
                }};
            }
            //endregion

            //region //在途验证

            if (this.isInnerTransferAvailable(collectionIndividualAccountBasicVice.getZjhm(), collectionIndividualAccountBasicVice.getXingMing(), collectionIndividualAccountBasicVice.getDwzh()) && !DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("zjhm", collectionIndividualAccountBasicVice.getZjhm());

            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria, "grywmx.cCollectionPersonalBusinessDetailsExtension.step"), (Collection) CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

                        @Override
                        public String tansform(CollectionBusinessStatus var1) {
                            return var1.getName();
                        }

                    }))));
                    criteria.add(Restrictions.ne("grywmx.ywlsh", YWLSH));
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()));
                }
            }).isUnique(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    exceptions.add(e);
                }

            })) {

                return new SubmitIndiAcctSetRes() {{

                    this.setSBYWLSH(YWLSH);

                    this.setStatus("fail");
                }};
            }
            //endregion

        }
        return new SubmitIndiAcctSetRes() {{

            this.setSBYWLSH(this.getSBYWLSH());

            this.setStatus("success");

        }};

    }
    //endregion

    public static void main(String[] args) {

        Date date = ComUtils.parseToDate("2017-00", "yyyy-MM");
        System.out.print(date);

    }

    public CommonMessage saveImportIndiAcctInfo(TokenContext tokenContext, IndiAcctSetPost
        addIndiAcctSet, StCommonUnit commonUnit, CAccountNetwork network) {

        CommonMessage commonMessage = new CommonMessage();
        //endregion
        //验证：个人公积金首次汇缴年月，必须小于单位首次汇缴年月
        //addCheck(addIndiAcctSet);

        CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = new CCollectionIndividualAccountBasicVice();

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
        try {
            collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);

            collectionIndividualAccountBasicVice.setGrywmx(collectionPersonalBusinessDetails);
            //region  //字段填充
            // COMMON
            collectionPersonalBusinessDetails.setYwlsh(collectionPersonalBusinessDetails.getYwlsh()/* 不填 数据库自动生成 */);
            collectionPersonalBusinessDetails.setCzbz(collectionPersonalBusinessDetails.getCzbz()/* todo 冲账标识 */);
            collectionPersonalBusinessDetails.setDngjfse(BigDecimal.ZERO/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setFse(BigDecimal.ZERO/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setFslxe(BigDecimal.ZERO/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.开户.getCode());
            collectionPersonalBusinessDetails.setGrzh(collectionPersonalBusinessDetails.getGrzh()/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setJzrq(collectionPersonalBusinessDetails.getJzrq()/* todo 记账日期 */);
            collectionPersonalBusinessDetails.setSnjzfse(BigDecimal.ZERO/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setTqyy(collectionPersonalBusinessDetails.getTqyy()/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setTqfs(collectionPersonalBusinessDetails.getTqfs()/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setUnit(commonUnit);
            collectionPersonalBusinessDetails.setPerson(collectionPersonalBusinessDetails.getPerson()/* 开户此字段不填 */);
            collectionPersonalBusinessDetails.setCreated_at(new Date());

            collectionPersonalBusinessDetailsExtension.setSlsj(new Date()/* todo 受理时间 */);
            collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.开户.getCode());
            collectionPersonalBusinessDetailsExtension.setDjje(BigDecimal.ZERO/* 开户此字段不填 */);
            collectionPersonalBusinessDetailsExtension.setBeizhu(collectionPersonalBusinessDetailsExtension.getBeizhu()/* 开户此字段不填 */);
            collectionPersonalBusinessDetailsExtension.setZcdw(collectionPersonalBusinessDetailsExtension.getZcdw()/* 开户此字段不填 */);
            collectionPersonalBusinessDetailsExtension.setCzyy(collectionPersonalBusinessDetailsExtension.getCzyy()/*不填 操作原因*/);

            collectionIndividualAccountBasicVice.setYwlsh(collectionIndividualAccountBasicVice.getYwlsh()/* 不填 数据库自动生成 */);
            collectionIndividualAccountBasicVice.setGjhtqywlx(CollectionBusinessType.开户.getCode());
            collectionIndividualAccountBasicVice.setSlsj(new Date()/* todo 受理时间 */);
            collectionIndividualAccountBasicVice.setGrzh(collectionIndividualAccountBasicVice.getGrzh()/* 开户此字段不填 */);
            collectionIndividualAccountBasicVice.setGrzhye(BigDecimal.ZERO/* 开户此字段不填 */);
            // GRZHXX
            collectionIndividualAccountBasicVice.setGjjschjny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSet.getGRZHXX().getGJJSCHJNY(), DateUtil.dbformatYear_Month)/*公积金首次汇缴年月*/);
            collectionIndividualAccountBasicVice.setGrckzhhm(addIndiAcctSet.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/);
            collectionIndividualAccountBasicVice.setGrjcjs(StringUtil.safeBigDecimal(addIndiAcctSet.getGRZHXX().getGRJCJS() + "")/*个人缴存基数*/);
            collectionIndividualAccountBasicVice.setGrckzhkhyhdm(addIndiAcctSet.getGRZHXX().getGRCKZHKHYHDM()/*个人存款账户开户银行代码*/);
            collectionIndividualAccountBasicVice.setGrckzhkhyhmc(addIndiAcctSet.getGRZHXX().getGRCKZHKHYHMC()/*个人存款账户开银行名称*/);
            // JBRZJLX
            collectionPersonalBusinessDetailsExtension.setJbrzjlx(addIndiAcctSet.getJBRZJLX()/*经办人证件类型*/);


            // JBRXM
            collectionPersonalBusinessDetailsExtension.setJbrxm(addIndiAcctSet.getJBRXM()/*经办人姓名*/);
            collectionPersonalBusinessDetailsExtension.setStep("新建");
            // GRXX
            collectionIndividualAccountBasicVice.setHyzk(addIndiAcctSet.getGRXX().getHYZK()/*婚姻状况*/);

            collectionIndividualAccountBasicVice.setZjlx(addIndiAcctSet.getGRXX().getZJLX()/*证件类型*/);
            collectionIndividualAccountBasicVice.setZhiYe(addIndiAcctSet.getGRXX().getZhiYe()/*职业*/);
            collectionIndividualAccountBasicVice.setZhiCheng(addIndiAcctSet.getGRXX().getZhiCheng()/*职称*/);
            collectionIndividualAccountBasicVice.setZhiWu("999Y");//其他
            collectionIndividualAccountBasicVice.setXueLi("999");//其他
            collectionIndividualAccountBasicVice.setSjhm(addIndiAcctSet.getGRXX().getSJHM()/*手机号码*/);
            collectionIndividualAccountBasicVice.setZjhm(addIndiAcctSet.getGRXX().getZJHM()/*证件号码*/);
            collectionIndividualAccountBasicVice.setXingMing(addIndiAcctSet.getGRXX().getXingMing()/*姓名*/);
            collectionIndividualAccountBasicVice.setYzbm(addIndiAcctSet.getGRXX().getYZBM()/*邮政编码*/);
            collectionIndividualAccountBasicVice.setXmqp(addIndiAcctSet.getGRXX().getXMQP()/*姓名全拼*/);
            collectionIndividualAccountBasicVice.setXingBie(addIndiAcctSet.getGRXX().getXingBie()/*性别*/);
            collectionIndividualAccountBasicVice.setCsny(DateUtil.safeStr2DBDate(formatNY, addIndiAcctSet.getGRXX().getCSNY()/*出生年月*/, DateUtil.dbformatYear_Month));
            collectionIndividualAccountBasicVice.setJtysr(StringUtil.safeBigDecimal(addIndiAcctSet.getGRXX().getJTYSR()/*家庭月收入*/ + ""));
            // YWWD
            collectionIndividualAccountBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);

            collectionPersonalBusinessDetailsExtension.setYwwd(network/*业务网点*/);

            // JBRZJHM
            collectionPersonalBusinessDetailsExtension.setJbrzjhm(addIndiAcctSet.getJBRZJHM()/*经办人证件号码*/);
            // DWXX
            collectionIndividualAccountBasicVice.setDwzh(addIndiAcctSet.getDWXX().getDWZH()/*单位账号*/);
            // CZY
            collectionIndividualAccountBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
            collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
            collectionIndividualAccountBasicViceDAO.saveNoflush(collectionIndividualAccountBasicVice);
        } catch (Exception e) {
            commonMessage.setMessage(e.getMessage());
            commonMessage.setCode("错误原因:");
            return commonMessage;
        }
        commonMessage.setMessage("Success");
        commonMessage.setCode("01");
        return commonMessage;
    }
    public ImportExcelRes addImportIndiAcctInfo(String id,TokenContext
        tokenContext, Map<Integer, Map<Integer, Object>> map) {
        HashMap<String, String> KeyValue = new HashMap<>();
        ArrayList arrayList = new ArrayList();
        ImportExcelRes importExcelRes = new ImportExcelRes();
        ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
        KeyValue.put("DWMC", String.valueOf(map.get(2).get(2)).trim());
        KeyValue.put("DWZH", String.valueOf(map.get(2).get(7)).replace(" ", ""));
        if (KeyValue.get("DWZH") == null) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "单位账号参数异常");
        }
        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", KeyValue.get("DWZH"));

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonUnit != null && !KeyValue.get("DWMC").equals(commonUnit.getDwmc())) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "单位名称与单位账号不匹配");
        }
        if (commonUnit == null) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "单位名称或者单位账号不存在");
        }
        if (commonUnit == null || commonUnit.getExtension() == null || commonUnit.getCollectionUnitAccount() == null) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "单位信息" + ReturnEnumeration.Data_MISS.getMessage());
        }
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("id", tokenContext.getUserInfo().getYWWD());
        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (!network.getId().equals(commonUnit.getExtension().getKhwd())) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "该业务不在当前网点受理范围内" + ReturnEnumeration.Permission_Denied.getMessage());
        }

        if (UnitAccountStatus.封存.getCode().equals(commonUnit.getCollectionUnitAccount().getDwzhzt())) {
            return ExcelUtil.ReturnNotice(String.valueOf(map.get(2).get(2)), "单位已封存" + ReturnEnumeration.Data_NOT_MATCH.getMessage());
        }
        Integer F = 0;
        Integer C = 0;

        String zjhm = null;
        for (int i = 4; i <= map.size(); i++) {
            String v = String.valueOf(map.get(i).get(1));
            if (v == null || v.length() <= 0) {
                C = 1;
                break;
            }
            if ((map.get(i).get(1) == null || map.get(i).get(1).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "姓名数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(2) == null || map.get(i).get(2).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "身份证号码数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if(map.get(i).get(2).toString().replace(" ", "").length()!=15&&map.get(i).get(2).toString().replace(" ", "").length()!=18){
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "身份证号码长度为18或15位");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if(!IdcardValidator.isValidatedAllIdcard(map.get(i).get(2).toString().replace(" ", ""))){
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "身份证号码不正确");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            zjhm = map.get(i).get(2).toString().replace(" ", "");
            String finalZjhm = zjhm;
            if (DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("zjhm", finalZjhm);

            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");
                    criteria.add(Restrictions.and(Restrictions.ne("collectionPersonalAccount.grzhzt","03"),
                            Restrictions.ne("collectionPersonalAccount.grzhzt","04"),
                            Restrictions.ne("collectionPersonalAccount.grzhzt","05")));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {

                @Override

                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            }) != null) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "证件号码已存在");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if (DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("zjhm", finalZjhm);
                this.put("grywmx.cCollectionPersonalBusinessDetailsExtension.step", "新建");

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override

                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            }) != null) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "证件号码已存在");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }

            if ((map.get(i).get(3) == null || map.get(i).get(3).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "联名卡开户银行全称数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(4) == null || map.get(i).get(4).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "联名卡卡号数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(5) == null || map.get(i).get(5).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "手机号码数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            BigDecimal sjhm = new BigDecimal(map.get(i).get(5).toString().replace(" ", ""));
            if(sjhm.toPlainString().length()!=11){
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "手机号码长度该为11位");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(6) == null || map.get(i).get(6).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "婚姻状况数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(7) == null || map.get(i).get(7).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "职业数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(8) == null || map.get(i).get(8).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "职称数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(9) == null || map.get(i).get(9).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "缴存基数数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(10) == null || map.get(i).get(10).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "开户日期数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if (map.get(i).get(3)!= null) {
                PageRes<BankInfoModel> bankInfoList = iBankInfoService.getBankInfoList(map.get(i).get(3).toString(), "", 1, 9999);
                if (bankInfoList.getResults() != null && bankInfoList.getResults().size() > 0) {
                } else {
                    excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "联名卡开户银行全称数据异常");
                    arrayList.add(excelErrorListRes);
                    F++;
                    break;
                }
            }
        }
        if (C == 1 && F != 0) {
            F = 1;
            excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第5行", "姓名数据缺失");
            arrayList.add(excelErrorListRes);
        }
        if(F==0&&excelErrorListRes.getMes()==null){
            excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("数据", "正在导入");
            arrayList.add(excelErrorListRes);
        }
        importExcelRes.setImportExcelErrorListRes(arrayList);
        importExcelRes.setSuccess_num("0");
        importExcelRes.setFail_num(F.toString());
        if(F==0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    asyncaddIndiAcctInfo(id, tokenContext, map, commonUnit, network);
                }
            }).start();
        }
        return importExcelRes;
    }
    public void asyncaddIndiAcctInfo(String id ,TokenContext tokenContext, Map<Integer, Map<Integer, Object>> map, StCommonUnit commonUnit,CAccountNetwork network ){
        HashMap<String, String> KeyValue = new HashMap<>();
        KeyValue.put("DWMC", String.valueOf(map.get(2).get(2)).trim());
        KeyValue.put("DWZH", String.valueOf(map.get(2).get(7)).replace(" ", ""));
        Integer S = 0;
        String TotalNum = "IndiAcct_TotalNum"+id;
        String SucNum = "IndiAcct_SucNum"+id;
        String Mes = "IndiAcct_Mes"+id;
        JedisCluster redis = null;
        try {
            redis = redisCache.getJedisCluster();
            for (int i = 4; i <= map.size(); i++) {
                String v = String.valueOf(map.get(i).get(1));
                if (v == null || v.length() <= 0) {
                    break;
                }
                List<CommonDictionary> HYZK_list = iDictionaryService.getDictionaryByType("MaritalStatus");
                List<CommonDictionary> ZhiYe_list = iDictionaryService.getDictionaryByType("Vocation");
                List<CommonDictionary> ZhiCheng_list = iDictionaryService.getDictionaryByType("TechnicalTitle");
                KeyValue.put("XingMing", String.valueOf(map.get(i).get(1)).trim());
                KeyValue.put("SFZHM", String.valueOf(map.get(i).get(2)).replace(" ", ""));
                KeyValue.put("LMKKHXHMC", String.valueOf(map.get(i).get(3)).trim());
                KeyValue.put("LMKKH", String.valueOf(map.get(i).get(4)).replace(" ", ""));
                BigDecimal sjhm = new BigDecimal(map.get(i).get(5).toString().replace(" ", ""));
                KeyValue.put("SJHM", String.valueOf(sjhm.toPlainString()));
                if (map.get(i).get(6) != null) {
                    for (CommonDictionary hyzk : HYZK_list) {
                        if (hyzk.getName().equals(map.get(i).get(6))) {
                            KeyValue.put("HYZK", hyzk.getCode());
                            break;
                        }
                    }
                }
                if (map.get(i).get(7) != null) {
                    for (CommonDictionary ZhiYe : ZhiYe_list) {
                        if (ZhiYe.getName().equals(map.get(i).get(7))) {
                            KeyValue.put("ZhiYe", ZhiYe.getCode());
                            break;
                        }
                    }
                }
                if (map.get(i).get(8) != null) {
                    for (CommonDictionary ZhiCheng : ZhiCheng_list) {
                        if (ZhiCheng.getName().equals(map.get(i).get(8))) {
                            KeyValue.put("ZhiCheng", ZhiCheng.getCode());
                            break;
                        }
                    }
                }
                KeyValue.put("JCJS", String.valueOf(map.get(i).get(9)));
                SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
                String KHRQ = String.valueOf(map.get(i).get(10));
                if (!KHRQ.equals("")) {
                    KeyValue.put("KHRQ", ym.format(new Date(KHRQ)));
                }
                IndiAcctSetPost indiAcctSetPost = new IndiAcctSetPost();
                IndiAcctSetPostDWXX DWXX = new IndiAcctSetPostDWXX();
                IndiAcctSetPostGRXX GRXX = new IndiAcctSetPostGRXX();
                IndiAcctSetPostGRZHXX GRZHXX = new IndiAcctSetPostGRZHXX();
                DWXX.setDWZH(KeyValue.get("DWZH"));
                indiAcctSetPost.setDWXX(DWXX);
                GRXX.setXingMing(KeyValue.get("XingMing"));
                GRXX.setZJLX("01");
                GRXX.setZJHM(KeyValue.get("SFZHM"));
                Map<String, String> carInfo = ExcelUtil.getCarInfo(KeyValue.get("SFZHM").toString());
                GRXX.setCSNY(carInfo.get("CSNY"));
                GRXX.setXingBie(carInfo.get("sexcode"));
                GRXX.setHYZK(KeyValue.get("HYZK"));
                GRXX.setZhiYe(KeyValue.get("ZhiYe"));
                GRXX.setZhiCheng(KeyValue.get("ZhiCheng"));
                GRXX.setXMQP(ExcelUtil.convertHanzi2Pinyin(KeyValue.get("XingMing"), true));
                GRXX.setSJHM(KeyValue.get("SJHM"));
                GRXX.setYZBM(Postalcode.getCode(tokenContext.getUserInfo().getYWWDMC()));
                if (KeyValue.get("JCJS") != null && !KeyValue.get("JCJS").equals("")) {
                    String JCJS = KeyValue.get("JCJS");
                    GRZHXX.setGRJCJS(JCJS);
                } else {
                    GRZHXX.setGRJCJS("0.00");
                }
                GRXX.setJTYSR(GRZHXX.getGRJCJS());
                indiAcctSetPost.setGRXX(GRXX);
                GRZHXX.setGRCKZHKHYHMC(KeyValue.get("LMKKHXHMC"));
                GRZHXX.setGJJSCHJNY(KeyValue.get("KHRQ"));
                if (KeyValue.get("LMKKHXHMC") != null) {
                    PageRes<BankInfoModel> bankInfoList = iBankInfoService.getBankInfoList(KeyValue.get("LMKKHXHMC"), "", 1, 9999);
                    if (bankInfoList.getResults() != null && bankInfoList.getResults().size() > 0) {
                        GRZHXX.setGRCKZHKHYHDM(bankInfoList.getResults().get(0).getCode());
                    } else {
                        break;
                    }
                }
                GRZHXX.setGRCKZHHM(KeyValue.get("LMKKH"));
                if (KeyValue.get("JCJS") != null && !KeyValue.get("JCJS").equals("")) {
                    Number JCJS = new BigDecimal(KeyValue.get("JCJS"));
                    GRZHXX.setGRJCJS("" + JCJS);
                } else {
                    GRZHXX.setGRJCJS("0.00");
                }
                indiAcctSetPost.setGRZHXX(GRZHXX);
                indiAcctSetPost.setCZLX("0");
                CommonMessage result = saveImportIndiAcctInfo(tokenContext, indiAcctSetPost, commonUnit, network);
                if (result != null && result.getMessage().equals("Success") && result.getCode() != null) {
                    S++;
                    redis.setex(SucNum,3600,S.toString());
                    redis.setex(Mes,3600,"正在导入");
                }else{
                    redis.setex(Mes,3600,"导入失败");
                }
            }
            redis.setex(TotalNum,3600,S.toString());
            redis.setex(Mes,3600,"导入成功");
            redis.close();
        }catch (Exception e){
            redis.setex(Mes,3600,"导入失败");
        }
    }
    public ImportExcelRes ReturnNotice(String Name, String Mes) {
        ArrayList arrayList = new ArrayList();
        ImportExcelRes importExcelRes = new ImportExcelRes();
        ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
        excelErrorListRes.setStatus("doing");
        excelErrorListRes.setMes(Mes);
        excelErrorListRes.setName(Name);
        arrayList.add(excelErrorListRes);
        importExcelRes.setImportExcelErrorListRes(arrayList);
        importExcelRes.setSuccess_num("0");
        importExcelRes.setFail_num("1");
        return importExcelRes;
    }
}
