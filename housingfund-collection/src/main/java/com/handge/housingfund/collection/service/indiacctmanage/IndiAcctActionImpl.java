package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctAction;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by Liujuhao on 2017/7/3.
 */
public abstract class IndiAcctActionImpl implements IndiAcctAction {

    @Autowired
    private ICCollectionIndividualAccountActionViceDAO collectionIndividualAccountActionViceDAO;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private IStCollectionPersonalAccountDAO collectionPersonalAccountDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStateMachineService iStateMachineService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";

    private static String formatNY = "yyyy-MM";

    private static String formatNYR = "yyyy-MM-dd HH:mm";
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";

    /**
     * @param DWMC
     * @param ZhuangTai
     * @param XingMing
     * @param ZJHM
     * @param GRZH
     * @param CZMC
     * @return 个人账户（冻结/解冻/封存/启封）业务列表 @// TODO: 2017/7/3 by liujihao
     * 获取个人账户（冻结/解冻/封存/启封）业务信息
     * 1.根据姓名、证件号码、个人账号、单位名称、状态参数，查询“个人账户信息”、“个人缴存信息”相关的表； 2.异常处理，统一待定
     * 3.封装数据库返回数据 4.返回结果
     */
    // completed 已测试
    public PageRes<ListOperationAcctsResRes> getAcctAcionInfo(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH,
                                                              String CZMC, String CZYY,String KSSJ,String JSSJ ,String pageNo, String pageSize) {
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
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (!StringUtil.isEmpty(XingMing)) this.put("person.xingMing", XingMing);
            if (!StringUtil.isEmpty(GRZH)) this.put("grzh", GRZH);
            if (!StringUtil.isEmpty(ZJHM)) this.put("person.zjhm", ZJHM);
            if (!StringUtil.isEmpty(CZMC)) this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CZMC);
            if (!StringUtil.isEmpty(DWMC)) this.put("unit.dwmc", DWMC);
            if (!StringUtil.isEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()) && !ZhuangTai.equals(PersonAccountStatus.冻结.getName())) {
                this.put("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai);
            }
            if (!StringUtil.isEmpty(ZhuangTai) && ZhuangTai.equals(PersonAccountStatus.冻结.getName())) {
                this.put("person.cCommonPersonExtension.sfdj", "02");
            }
        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(CZYY)) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czyy", CZYY));
                }
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {

                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){
                    criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){
                    criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
                }

                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");

                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).pageOption(pageRes, pagesize_number, page_number).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageRes<ListOperationAcctsResRes>() {{
            {
                this.setResults(new ArrayList<ListOperationAcctsResRes>() {
                    {
                        for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 : stCollectionPersonalBusinessDetails) {
                            this.add(new ListOperationAcctsResRes() {
                                {
                                    this.setYWLSH(stCollectionPersonalBusinessDetails1.getYwlsh());// 业务流水号
                                    this.setGRZH(stCollectionPersonalBusinessDetails1.getGrzh());// 个人账号
                                    StCommonPerson commonPerson = commonPersonDAO.list(new HashMap<String, Object>() {
                                        {
                                            this.put("grzh", stCollectionPersonalBusinessDetails1.getGrzh());
                                        }
                                    }, null, null, null, null, null, null).get(0);// 根据个人账号查询个人基本信息
                                    this.setXingming(commonPerson.getXingMing());// 姓名
                                    this.setZJHM(commonPerson.getZjhm());// 证件号码
                                    this.setZhuangTai(stCollectionPersonalBusinessDetails1.getExtension().getStep());// 业务状态
                                    this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs()+"");// 个人缴存基数
                                    this.setYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGrzhye())+"");// 合计月缴存额
                                    this.setGRZHYE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 个人账户余额
                                    this.setJZNY(DateUtil.dateStrTransform(commonPerson.getExtension().getGrjzny()));//缴至年月
                                    this.setDWMC(commonPerson.getUnit().getDwmc());// 单位名称
                                    this.setSLSJ(DateUtil.date2Str(stCollectionPersonalBusinessDetails1.getCreated_at(), "yyyy-MM-dd HH:mm"));
                                    this.setCZYY(stCollectionPersonalBusinessDetails1.getExtension().getCzyy());
                                }
                            });
                        }
                    }
                });

                this.setCurrentPage(pageRes.getCurrentPage());

                this.setNextPageNo(pageRes.getNextPageNo());

                this.setPageCount(pageRes.getPageCount());

                this.setTotalCount(pageRes.getTotalCount());

                this.setPageSize(pageRes.getPageSize());
            }
        }};
    }

    /**
     * @param DWMC
     * @param ZhuangTai
     * @param XingMing
     * @param ZJHM
     * @param GRZH
     * @param CZMC
     * @return 个人账户（冻结/解冻/封存/启封）业务列表 @// TODO: 2017/7/3 by liujihao
     * 获取个人账户（冻结/解冻/封存/启封）业务信息
     * 1.根据姓名、证件号码、个人账号、单位名称、状态参数，查询“个人账户信息”、“个人缴存信息”相关的表； 2.异常处理，统一待定
     * 3.封装数据库返回数据 4.返回结果
     */
    // completed 已测试
    public PageResNew<ListOperationAcctsResRes> getAcctAcionInfoNew(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH,
                                                                 String CZMC, String CZYY, String KSSJ, String JSSJ , String marker, String pageSize,String action) {
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (!StringUtil.isEmpty(XingMing)) this.put("person.xingMing", XingMing);
            if (!StringUtil.isEmpty(GRZH)) this.put("grzh", GRZH);
            if (!StringUtil.isEmpty(ZJHM)) this.put("person.zjhm", ZJHM);
            if (!StringUtil.isEmpty(DWMC)) this.put("unit.dwmc", DWMC);
        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (!StringUtil.isEmpty(CZMC)) {
                    criteria.createCriteria("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CZMC));
                }
                if (!StringUtil.isEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()) && !ZhuangTai.equals(PersonAccountStatus.冻结.getName())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", ZhuangTai));
                }
                if (!StringUtil.isEmpty(ZhuangTai) && ZhuangTai.equals(PersonAccountStatus.冻结.getName())) {
                    criteria.createCriteria("person","person");
                    criteria.createCriteria("person.cCommonPersonExtension","cCommonPersonExtension");
                    criteria.add(Restrictions.eq("person.cCommonPersonExtension.sfdj", "02"));
                }
                if (StringUtil.notEmpty(CZYY)) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czyy", CZYY));
                }
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){
                    criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
                    criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
                }
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){
                    criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
                    criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
                }

                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).pageOption(marker, action, pagesize_number).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageResNew<ListOperationAcctsResRes>() {{
            {
                this.setResults(action,new ArrayList<ListOperationAcctsResRes>() {
                    {
                        for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 : stCollectionPersonalBusinessDetails) {
                            this.add(new ListOperationAcctsResRes() {
                                {
                                    this.setId(stCollectionPersonalBusinessDetails1.getId());
                                    this.setYWLSH(stCollectionPersonalBusinessDetails1.getYwlsh());// 业务流水号
                                    this.setGRZH(stCollectionPersonalBusinessDetails1.getGrzh());// 个人账号
                                    StCommonPerson commonPerson = commonPersonDAO.list(new HashMap<String, Object>() {
                                        {
                                            this.put("grzh", stCollectionPersonalBusinessDetails1.getGrzh());
                                        }
                                    }, null, null, null, null, null, null).get(0);// 根据个人账号查询个人基本信息
                                    this.setXingming(commonPerson.getXingMing());// 姓名
                                    this.setZJHM(commonPerson.getZjhm());// 证件号码
                                    this.setZhuangTai(stCollectionPersonalBusinessDetails1.getExtension().getStep());// 业务状态
                                    this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs()+"");// 个人缴存基数
                                    this.setYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGrzhye())+"");// 合计月缴存额
                                    this.setGRZHYE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 个人账户余额
                                    this.setJZNY(DateUtil.dateStrTransform(commonPerson.getExtension().getGrjzny()));//缴至年月
                                    this.setDWMC(commonPerson.getUnit().getDwmc());// 单位名称
                                    this.setSLSJ(DateUtil.date2Str(stCollectionPersonalBusinessDetails1.getCreated_at(), "yyyy-MM-dd HH:mm"));
                                    this.setCZYY(stCollectionPersonalBusinessDetails1.getExtension().getCzyy());
                                }
                            });
                        }
                    }
                });
            }
        }};
    }

    /**
     * @param grzh              个人账号（GRZH)
     * @param addIndiAcctAction 封装了个人账户操作（冻结0/解冻1/封存2/启封3）信息的对象
     * @return 成功/失败 @// TODO: 2017/7/3 by liujihao 新增个人账户（冻结/解冻/封存/启封）操作业务
     * 1.接收UID（个人账号）和AddIndiAcctAction对象，获取操作类型（保存/提交）生成状态（新建/待审核），并生成业务流水号
     * 2.获取操作类型（冻结/解冻/封存/启封）和操作原因，其中冻结/解冻操作分别有冻结/解冻金额字段
     * 3.根据个人账号，查询“个人账户信息”相关的表，根据单位账号，查询“单位缴存信息”相关的表
     * 4.获取单位缴存比例、个人缴存比例，并计算单位月缴存额、个人月缴存额、合计月缴存额
     * 5.写入“个人账户信息”、“个人账户业务记录信息”相关的表 6.异常处理，统一待定 7.返回结果
     */
    // completed 已测试 - 正常
    public abstract AddIndiAcctActionRes addAcctAction(TokenContext tokenContext, String grzh, IndiAcctActionPost addIndiAcctAction);


    /**
     * @param UID              业务流水号（YWLSH)
     * @param reIndiAcctAction 对象类型：ReIndiAcctAction 封装了对个人账户操作（冻结/解冻/封存/启封）修改信息的对象
     * @return 成功/失败 @// TODO: 2017/7/3 by liujihao 修改个人账户（冻结/解冻/封存/启封）操作业务
     * 1.接收UID（业务流水号）和ReIndiAcctAction对象，获取操作类型（保存/提交）生成状态（新建/待审核）
     * 2.获取操作类型（冻结/解冻/封存/启封）和操作原因，其中冻结/解冻操作分别有冻结/解冻金额字段
     * 3.根据业务流水号，查询“个人账户业务信息”相关的表，根据单位账号，查询“单位缴存信息”相关的表
     * 4.获取单位缴存比例(DWJCBL)、个人缴存比例（GRJCBL），并计算单位月缴存额、个人月缴存额、合计月缴存额（考虑写成util
     * API）； 5.更新“个人账户信息”、“个人账户业务记录信息”相关的表 6.异常处理，统一待定 7.返回结果
     */
    // completed 已测试 - 正常
    public abstract ReIndiAcctActionRes reAcctAction(TokenContext tokenContext, String UID, IndiAcctActionPut reIndiAcctAction);


    /**
     * @param UID 业务流水号（YWLSH）
     * @return 封装了个人账户操作（冻结/解冻/封存/启封）业务信息的对象 @// TODO: 2017/7/3 by liujihao
     * 查看个人账户（冻结/解冻/封存/启封）业务详情 1.接收UID（业务流水号），查询“个人账户业务信息”相关的表
     * 2.根据单位账号，查询“单位缴存信息”相关的表 3.异常处理，统一待定 4.封装数据库返回数据 5.返回结果
     */
    // completed 已测试 - 正常
    public GetIndiAcctActionRes showAcctAction(String UID) {
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = instance(
                this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", UID);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (collectionPersonalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        StCommonPerson commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", collectionPersonalBusinessDetails.getGrzh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的个人账号");
        }
        return new GetIndiAcctActionRes() {
            {
                this.setDWXX(new GetIndiAcctActionResDWXX() {
                    {
                        this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());// 业务流水号
                        this.setDWMC(commonPerson.getUnit().getDwmc());// 单位名称
                        this.setDWZH(commonPerson.getUnit().getDwzh());// 单位账号
                        this.setJZNY(DateUtil.dateStrTransform(commonPerson.getUnit().getCollectionUnitAccount().getJzny()));// 缴至年月

                    }
                });
                this.setGRZHXX(new GetIndiAcctActionResGRZHXX() {
                    {
                        this.setGRZH(commonPerson.getCollectionPersonalAccount().getGrzh()); // 个人账号
                        this.setXingMing(commonPerson.getXingMing());// 姓名
                        this.setZJLX(commonPerson.getZjlx());// 证件类型
                        this.setZJHM(commonPerson.getZjhm());// 证件号码
                        this.setSXNY(DateUtil.dateStrTransform(collectionPersonalBusinessDetails.getIndividualAccountActionVice().getSxny()));//生效年月
                        this.setGRJZNY(DateUtil.dateStrTransform(commonPerson.getUnit().getCollectionUnitAccount().getJzny()));//个人缴至年月
                        this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());/// 个人账户状态
                        this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs()+"");// 个人缴存基数
                        this.setDWJCBL(commonPerson.getUnit().getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal(100))+"");// 单位缴存比例
                        this.setGRJCBL(commonPerson.getUnit().getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal(100))+"");// 个人缴存比例
                        this.setDWYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce()+"");// 单位月缴存额
                        this.setGRYJCE(commonPerson.getCollectionPersonalAccount().getGryjce()+"");// 个人月缴存额
                        this.setYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce())+"");
                        this.setGRZHYE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 个人账户余额
                        this.setGRZHSNJZYE(commonPerson.getCollectionPersonalAccount().getGrzhsnjzye()+"");// 个人账户上年结转余额
                        this.setGRZHDNGJYE(commonPerson.getCollectionPersonalAccount().getGrzhdngjye()+"");// 个人账户当年归集余额
                        this.setCZMC(collectionPersonalBusinessDetails.getExtension().getCzmc());// 操作名称（01:开户；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移）
                        this.setCZYY(collectionPersonalBusinessDetails.getExtension().getCzyy());// 操作原因
                        this.setDJJE(collectionPersonalBusinessDetails.getIndividualAccountActionVice().getDjje()+"");// 冻结金额
                        this.setBeiZhu(collectionPersonalBusinessDetails.getExtension().getBeizhu());// 备注
                    }
                });
                this.setBLZL(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getBlzl()) ? collectionPersonalBusinessDetails.getExtension().getBlzl() : "");// 办理资料
                this.setJBRXM(commonPerson.getUnit().getJbrxm());// 经办人姓名
                this.setJBRZJLX(commonPerson.getUnit().getJbrzjlx());// 经办人证件类型
                this.setJBRZJHM(commonPerson.getUnit().getJbrzjhm());// 经办人证件号码
                this.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());// 操作员
                this.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());// 业务网点
            }
        };
    }

    /**
     * 创建时，自动获取一些个人账户相关的固定信息（单位缴存信息）
     *
     * @param GRZH
     * @return
     */
    // completed 已测试 - 正常
    public AutoIndiAcctActionRes AutoIndiAcctAction(TokenContext tokenContext, String GRZH) {
        StCommonPerson commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", GRZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的个人账号");
        }

        return new AutoIndiAcctActionRes() {
            {
                this.setDWXX(new AutoIndiAcctActionResDWXX() {
                    {
                        this.setDWMC(commonPerson.getUnit().getDwmc());// 单位名称============================
                        this.setDWZH(commonPerson.getUnit().getDwzh());// 单位账号=============================
                        this.setJZNY(DateUtil.dateStrTransform(commonPerson.getUnit().getCollectionUnitAccount().getJzny()));// 缴至年月===============================
                    }
                });
                this.setGRXX(new GetIndiAcctAlterResGRXX() {{
                    this.setHYZK(commonPerson.getHyzk());//婚姻状况

                    this.setJTZZ(commonPerson.getJtzz());//家庭住址

                    this.setCSNY(DateUtil.dateStrTransform(commonPerson.getCsny()));//出生年月

                    this.setZJLX(commonPerson.getZjlx());//证件类型

                    this.setZhiYe(commonPerson.getZhiYe());//职业

                    this.setGRCKZHHM(commonPerson.getCollectionPersonalAccount().getGrckzhhm());//个人存款账户号码

                    this.setGRCKZHKHHDM(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhdm());//个人存款账户开户行代码

                    this.setGRCKZHKHHMC(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhmc());//个人存款账户开户行名称

                    this.setYZBM(commonPerson.getYzbm());//邮政编码

                    this.setZhiCheng(commonPerson.getZhiChen());//职称

                    this.setSJHM(commonPerson.getSjhm());//手机号码

                    this.setZJHM(commonPerson.getZjhm());//证件号码

                    this.setGDDHHM(commonPerson.getGddhhm());//固定电话号码

                    this.setXMQP(commonPerson.getXmqp());//姓名全拼

                    this.setXingBie(commonPerson.getXingBie().toString());//性别

                    this.setJTYSR(commonPerson.getJtysr().toPlainString());//家庭月收入

                    this.setXueLi(commonPerson.getXueLi());//学历

                    this.setXingMing(commonPerson.getXingMing());//姓名

                    this.setZhiWu(commonPerson.getZhiWu());//职务

                    this.setYouXiang(commonPerson.getExtension().getDzyx());//邮箱
                }});
                this.setGRZHXX(new AutoIndiAcctActionResGRZHXX() {
                    {
                        this.setDWJCBL(commonPerson.getUnit().getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal(100))+"");// 单位缴存比例
                        this.setGRJCBL(commonPerson.getUnit().getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal(100))+"");// 个人缴存比例
                        this.setGRZH(GRZH);// 个人账号
                        this.setXingming(commonPerson.getXingMing());// 姓名
                        this.setZJLX(commonPerson.getZjlx());// 证件类型
                        this.setZJHM(commonPerson.getZjhm());// 证件号码
                        this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());// 个人账户状态
                        this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs().toPlainString());// 个人缴存基数
                        this.setGRJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));//个人缴至年月
                        this.setGRZHYE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 个人账户余额
                        this.setGRZHSNJZYE(commonPerson.getCollectionPersonalAccount().getGrzhsnjzye()+"");// 个人账户上年结转余额
                        this.setGRZHDNGJYE(commonPerson.getCollectionPersonalAccount().getGrzhdngjye()+"");// 个人账户当年归集余额
                        this.setKHRQ(DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), format));
                        this.setGJJSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGjjschjny(), formatNY));
                    }
                });//
                this.setJBRXM(commonPerson.getUnit().getJbrxm());// 经办人姓名
                this.setJBRZJLX(commonPerson.getUnit().getJbrzjlx());// 经办人证件类型
                this.setJBRZJHM(commonPerson.getUnit().getJbrzjhm());// 经办人证件号码
                this.setCZY(tokenContext.getUserInfo().getCZY());
                CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("id", commonPerson.getUnit().getExtension().getKhwd());
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                this.setYWWD(network.getMingCheng());
            }
        };
    }

    // 个人账户Action（封存、启封、冻结、解冻、托管）回执单
    @Override // completed 已测试 - 正常
    public CommonResponses headAcctAction(TokenContext tokenContext, String UID) {
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = instance(
                this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", UID);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (collectionPersonalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        StCommonPerson commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", collectionPersonalBusinessDetails.getGrzh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的个人账号");
        }
        HeadIndiAcctActionRes result = new HeadIndiAcctActionRes();
        result.setRiQi(DateUtil.date2Str(collectionPersonalBusinessDetails.getCreated_at(), formatNYR)); // 日期
        result.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());// 业务流水号
        result.setDWMC(commonPerson.getUnit().getDwmc());// 单位名称
        result.setDWZH(commonPerson.getUnit().getDwzh());// 单位账号
        result.setJZNY(DateUtil.dateStrTransform(commonPerson.getUnit().getCollectionUnitAccount().getJzny()));// 缴至年月
        result.setGRZH(collectionPersonalBusinessDetails.getGrzh());// 个人账号
        result.setXingMing(commonPerson.getXingMing());// 姓名
        result.setZJLX(commonPerson.getZjlx());// 证件类型
        result.setZJHM(commonPerson.getZjhm());// 证件号码
        result.setGRZHZT(PersonAccountStatus.getNameByCode(commonPerson.getCollectionPersonalAccount().getGrzhzt()));// 个人账户状态
        result.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs()+"");// 个人缴存基数（元）
        result.setGRJCBL(commonPerson.getUnit().getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100"))+"");// 个人缴存比例（%）
        result.setDWJCBL(commonPerson.getUnit().getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))+"");// 单位缴存比例（%）
        result.setDWYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce()+"");// 单位月缴存额（元）
        result.setGRYJCE(commonPerson.getCollectionPersonalAccount().getGryjce()+"");// 个人月缴存额（元）
        result.setYJCE(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce())+""); //合计月缴存额（元）
        result.setCZMC(collectionPersonalBusinessDetails.getExtension().getCzmc());
        result.setSXNY(new SimpleDateFormat(formatNY).format(new Date()));//待定
        result.setGRZJNY(DateUtil.dateStrTransform(commonPerson.getUnit().getCollectionUnitAccount().getJzny()));
        result.setGRZHYE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 个人账户余额（元）
        result.setGRZHSNJZYE(commonPerson.getCollectionPersonalAccount().getGrzhsnjzye()+"");// 个人账户上年结转余额（元）
        result.setGRZHDNGJYE(commonPerson.getCollectionPersonalAccount().getGrzhdngjye()+"");// 个人账户当年归集余额（元）
        result.setDJJE(commonPerson.getCollectionPersonalAccount().getGrzhye()+"");// 冻结金额
        if (result.getCZMC().equals(CollectionBusinessType.封存.getCode())) {
            SingleDictionaryDetail CZMC_Info = iDictionaryService.getSingleDetail(collectionPersonalBusinessDetails.getExtension().getCzyy(), "IndividualSequestration");
            result.setCZYY(CZMC_Info != null ? CZMC_Info.getName() : null);// 封存原因
        }
        if (result.getCZMC().equals(CollectionBusinessType.启封.getCode())) {
            SingleDictionaryDetail CZMC_Info = iDictionaryService.getSingleDetail(collectionPersonalBusinessDetails.getExtension().getCzyy(), "IndividualUnsealed");
            result.setCZYY(CZMC_Info != null ? CZMC_Info.getName() : null);// 启封原因
        }
        if (result.getCZMC().equals(CollectionBusinessType.解冻.getCode())) {
            SingleDictionaryDetail CZMC_Info = iDictionaryService.getSingleDetail(collectionPersonalBusinessDetails.getExtension().getCzyy(), "IndividualUnfreeze");
            result.setCZYY(CZMC_Info != null ? CZMC_Info.getName() : null);// 解冻原因
        }
        if (result.getCZMC().equals(CollectionBusinessType.冻结.getCode())) {
            SingleDictionaryDetail CZMC_Info = iDictionaryService.getSingleDetail(collectionPersonalBusinessDetails.getExtension().getCzyy(), "IndividualFreeze");
            result.setCZYY(CZMC_Info != null ? CZMC_Info.getName() : null);// 冻结原因
        }
        result.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());
        result.setBeiZhu(collectionPersonalBusinessDetails.getExtension().getBeizhu());// 备注
        result.setCZY(collectionPersonalBusinessDetails.getExtension().getCzy());
        result.setDWJBR(collectionPersonalBusinessDetails.getExtension().getJbrxm());
        //审核人，该条记录审核通过的操作员
        CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", UID);
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
        String id = pdfService.getIndiAcctActionPdf(result, result.getCZMC());
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    // TODO: 2017/7/24 实现（向超）
    public abstract void doAcctAction(TokenContext tokenContext, String YWLSH);



    @Override
    public abstract SubmitIndiAcctFreezeRes submitIndiAcctAction(TokenContext tokenContext, List<String> YWLSHJH);

    /**
     * 前置条件：汇缴在途时限制(启封、封存、开户)
     * 封存/启封 检查:职工是否符合条件，并给出提示或警告
     * 根据单位的缴至年月、封存的生效月份、以及当前的时间来判断
     * 01正常封存/启封
     * 02未来生效
     * 03错缴/补缴
     */
    @Override
    public ComMessage sealCheck(String grzh, String sxnyStr) {
        Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        Date jzny = ComUtils.parseToDate(unitAccount.getJzny(), "yyyyMM");
        Date currentMonth = ComUtils.getfirstDayOfMonth(new Date());
        //如果缴至年月为空
        if (jzny == null) {
            String dwschjnyStr = unit.getExtension().getDwschjny();
            Date dwschjny = ComUtils.parseToDate(dwschjnyStr, "yyyyMM");
            if (sxny.compareTo(dwschjny) < 0) {
                return new ComMessage("05", "生效年月不能小于单位的应汇缴年月！");
            }
            jzny = ComUtils.getLastMonth(dwschjny);
        }
        //生效年月是否大于缴至年月
        boolean flag = sxny.compareTo(jzny) > 0;
        //生效年月与当前月份的比较
        int falg2 = sxny.compareTo(currentMonth);
        ComMessage result = new ComMessage();
        //1、生效年月大于缴至年月，且生效年月小于等于当前月份
        if (flag && falg2 <= 0) {
            result.setValue("01", "正常");
        } else if (flag && falg2 > 0) {
            result.setValue("02", "未来生效");
        } else if (!flag) {
            result.setValue("03", "错缴或补缴产生");
        }
        return result;
    }

    //检查参数
    protected void checkParams(StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails) {
        if (stCollectionPersonalBusinessDetails.getExtension() == null || stCollectionPersonalBusinessDetails.getIndividualAccountActionVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "数据缺失");
        }
        if (StringUtil.isEmpty(stCollectionPersonalBusinessDetails.getIndividualAccountActionVice().getSxny())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "生效年月");
        }
        if (StringUtil.isEmpty(stCollectionPersonalBusinessDetails.getIndividualAccountActionVice().getCzyy())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "操作原因");
        }
    }
}



