package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.AddIndiAcctActionRes;
import com.handge.housingfund.common.service.collection.model.unit.ReUnitAcctDropRes;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayhold;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Business_In_Process;
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by 向超 on 2017/7/25.
 */
@Component
public class UnitPayholdImpl implements UnitPayhold {
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionUnitPayholdViceDAO collectionUnitPayholdViceDAO;
    @Autowired
    private IStateMachineService iStateMachineService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    private static String format = "yyyy-MM-dd HH:mm";
    private static String formatNY = "yyyy-MM";

    @Override
    public PageRes<ListUnitPayHoldResRes> getUnitPayholdInfo(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String CZY, String YWWD, String KSSJ, String JSSJ, String page, String pageSize) {

        Date kssj = null;
        Date jssj = null;
        try {
            if (StringUtil.notEmpty(KSSJ))
                kssj = new SimpleDateFormat(format).parse(KSSJ);
            if (StringUtil.notEmpty(JSSJ))
                jssj = new SimpleDateFormat(format).parse(JSSJ);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式不正确");
        }
        PageRes pageRes = new PageRes();
        int page_number = 1;

        int pagesize_number = 10;

        try {

            if (StringUtil.notEmpty(page)) {
                page_number = Integer.parseInt(page);
            }

            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }

        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsList = instance(collectionUnitBusinessDetailsDAO).betweenDate(kssj, jssj).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("unit.dwzh", DWZH);
            if (StringUtil.notEmpty(DWMC)) this.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(CZY)) this.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
            if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))
                this.put("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai);
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.缓缴处理.getCode());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(YWWD)&&!"1".equals(YWWD)){
                criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");

                criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id", YWWD));
            }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<ListUnitPayHoldResRes>() {{
            this.setResults(new ArrayList<ListUnitPayHoldResRes>() {{
                for (StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails : stCollectionUnitBusinessDetailsList) {
                    this.add(new ListUnitPayHoldResRes() {{
                        this.setJZNY(DateUtil.dateStrTransform(stCollectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()));//缴至年月

                        this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());//业务流水号

                        this.setSQHJNY(DateUtil.date2Str(stCollectionUnitBusinessDetails.getUnitPayholdVice().getHjjssj(), formatNY));//申请缓缴年月

                        this.setSLSJ(DateUtil.date2Str(stCollectionUnitBusinessDetails.getCreated_at(), format));//受理时间

                        this.setYWWD(stCollectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//业务网点

                        this.setYWZT(stCollectionUnitBusinessDetails.getExtension().getStep());//状态

                        this.setDWMC(stCollectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

                        this.setDWZH(stCollectionUnitBusinessDetails.getUnit().getDwzh());//单位账号

                        this.setCZY(stCollectionUnitBusinessDetails.getExtension().getCzy());//操作员
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
    public PageResNew<ListUnitPayHoldResRes> getUnitPayholdInfoNew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String CZY, String YWWD, String KSSJ, String JSSJ, String marker, String pageSize, String action) {
        Date kssj = null;
        Date jssj = null;
        try {
            if (StringUtil.notEmpty(KSSJ))
                kssj = new SimpleDateFormat(format).parse(KSSJ);
            if (StringUtil.notEmpty(JSSJ))
                jssj = new SimpleDateFormat(format).parse(JSSJ);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式不正确");
        }
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }

        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsList = instance(collectionUnitBusinessDetailsDAO).betweenDate(kssj, jssj).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("unit.dwzh", DWZH);
            if (StringUtil.notEmpty(DWMC)) this.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(CZY)) this.put("cCollectionUnitBusinessDetailsExtension.czy", CZY);
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if(!StringUtil.notEmpty(CZY))
                criteria.createCriteria("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.缓缴处理.getCode()));
                if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))
                    criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(YWWD)&&!"1".equals(YWWD)){
                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id", YWWD));
                }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(marker, action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageResNew<ListUnitPayHoldResRes>() {{
            this.setResults(action,new ArrayList<ListUnitPayHoldResRes>() {{
                for (StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails : stCollectionUnitBusinessDetailsList) {
                    this.add(new ListUnitPayHoldResRes() {{
                        this.setId(stCollectionUnitBusinessDetails.getId());
                        this.setJZNY(DateUtil.dateStrTransform(stCollectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()));//缴至年月

                        this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());//业务流水号

                        this.setSQHJNY(DateUtil.date2Str(stCollectionUnitBusinessDetails.getUnitPayholdVice().getHjjssj(), formatNY));//申请缓缴年月

                        this.setSLSJ(DateUtil.date2Str(stCollectionUnitBusinessDetails.getCreated_at(), format));//受理时间

                        this.setYWWD(stCollectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//业务网点

                        this.setYWZT(stCollectionUnitBusinessDetails.getExtension().getStep());//状态

                        this.setDWMC(stCollectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

                        this.setDWZH(stCollectionUnitBusinessDetails.getUnit().getDwzh());//单位账号

                        this.setCZY(stCollectionUnitBusinessDetails.getExtension().getCzy());//操作员
                    }});
                }
            }});
        }};
    }

    @Override
    public ReUnitDepositRatioRes addUnitPayhold(TokenContext tokenContext, UnitPayHoldPost unitPayHoldPost) {

        //办理资料验证
        if (unitPayHoldPost.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.单位缓缴.getCode(), unitPayHoldPost.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        StCommonUnit commonUnit = instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("dwzh", unitPayHoldPost.getDWZH());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonUnit == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的单位");
        }
        if (!commonUnit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }
        //缓缴唯一验证
        if (commonUnit.getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.缓缴.getCode()))
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前单位账户状态为缓缴，不能再办理缓缴业务");
        Date SQHJKSNY = null;
        Date SQHJJSNY = null;
        if (!StringUtil.notEmpty(unitPayHoldPost.getSQHJKSNY()) || !StringUtil.notEmpty(unitPayHoldPost.getSQHJKSNY())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缓缴开始年月与缓缴结束年月不能为空");
        }
        try {
            SQHJKSNY = new SimpleDateFormat(formatNY).parse(unitPayHoldPost.getSQHJKSNY());
            SQHJJSNY = new SimpleDateFormat(formatNY).parse(unitPayHoldPost.getSQHJJSNY());
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH);
        }
        if (SQHJKSNY.getTime() > SQHJJSNY.getTime()) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能大于结束月份");
        }
        Date jzny = null;
        try {
            jzny = DateUtil.str2Date("yyyyMM", commonUnit.getCollectionUnitAccount().getJzny());
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "缴至年月格式不正确");
        }
//        if (commonUnit.getCollectionUnitAccount().getJzny() != null && jzny != null && jzny.getTime() >= SQHJKSNY.getTime()) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能小于等于缴至年月");
//        }
        if(!StringUtil.notEmpty(commonUnit.getCollectionUnitAccount().getJzny())){
            throw new ErrorException(ReturnEnumeration.Data_MISS,"缴至年月缺失,请确保该单位缴过钱!");
        }
        String yhjny = DateUtil.getNextMonthStr(commonUnit.getCollectionUnitAccount().getJzny());
        try {
            if(DateUtil.str2Date("yyyyMM",yhjny).compareTo(SQHJKSNY)!=0){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "开始月份应等于应汇缴年月");
            }
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "缴至年月格式不正确");
        }

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();//构造单位明细信息
        collectionUnitBusinessDetails.setDwzh(unitPayHoldPost.getDWZH());//单位账号
        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetailsExtension.setBlzl(unitPayHoldPost.getBLZL());//办理资料
        collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.缓缴处理.getCode());//操作名称
        collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        collectionUnitBusinessDetailsExtension.setQtczyy(unitPayHoldPost.getHJYY());// 操作原因
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
        collectionUnitBusinessDetailsExtension.setYwwd(network);//业务网点
        collectionUnitBusinessDetailsExtension.setJbrxm(unitPayHoldPost.getJBRXM());//经办人姓名
        collectionUnitBusinessDetailsExtension.setJbrzjlx(unitPayHoldPost.getJBRZJLX());//经办人证件类型
        collectionUnitBusinessDetailsExtension.setJbrzjhm(unitPayHoldPost.getJBRZJHM());//经办人证件号码
        collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        collectionUnitBusinessDetailsExtension.setSlsj(new Date());
        if (!Arrays.asList("0", "1").contains(unitPayHoldPost.getCZLX()))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型");

        collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);


        CCollectionUnitPayholdVice cCollectionUnitPayholdVice = new CCollectionUnitPayholdVice();
        cCollectionUnitPayholdVice.setHjfssj(SQHJKSNY);//'汇缴开始时间
        cCollectionUnitPayholdVice.setHjjssj(SQHJJSNY);//汇缴结束时间
        cCollectionUnitPayholdVice.setQuNianKS(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQuNianKS())? BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQuNianKS()));//前年亏损
        cCollectionUnitPayholdVice.setQianNianKS(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQianNianKS())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQianNianKS()));//前年亏损
        cCollectionUnitPayholdVice.setQuNianYL(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQuNianYL())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQuNianYL()));//去年盈利
        cCollectionUnitPayholdVice.setQianNianYL(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQianNianYL())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQianNianYL()));//前年盈利
        cCollectionUnitPayholdVice.setQuNianRJGZ(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQuNianRYPJGZ())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQuNianRYPJGZ()));//去年人均工资
        cCollectionUnitPayholdVice.setQianNianRJGZ(!StringUtil.notEmpty(unitPayHoldPost.getQCQR().getQianNianRYPJGZ())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPost.getQCQR().getQianNianRYPJGZ()));//前年人均工资
        cCollectionUnitPayholdVice.setDwywmx(collectionUnitBusinessDetails);

        collectionUnitBusinessDetails.setUnitPayholdVice(cCollectionUnitPayholdVice);
        collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.缓缴处理.getCode());
        collectionUnitBusinessDetails.setUnit(commonUnit);

        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {//插入单位明细表
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.通过.getEvent());

                    this.put("1", Events.提交.getEvent());

                }}.get(unitPayHoldPost.getCZLX()), new TaskEntity(stCollectionUnitBusinessDetails.getYwlsh(), stCollectionUnitBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        stCollectionUnitBusinessDetails.getExtension().getCzy(), stCollectionUnitBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_单位缓缴申请.getSubType(), BusinessType.Collection, stCollectionUnitBusinessDetails.getExtension().getYwwd().getId()),
                new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || !StringUtil.notEmpty(next) || e != null) {
                            return;
                        }

                        if (succeed) {
                            CCollectionUnitBusinessDetailsExtension cCollectionUnitBusinessDetailsExtension = stCollectionUnitBusinessDetails.getExtension();
                            cCollectionUnitBusinessDetailsExtension.setStep(next);

                            stCollectionUnitBusinessDetails.setExtension(cCollectionUnitBusinessDetailsExtension);

                            if (StringUtil.isIntoReview(next, null))
                                stCollectionUnitBusinessDetails.getExtension().setDdsj(new Date());

                            DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(stCollectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName()))
                                doUnitPayhold(stCollectionUnitBusinessDetails.getYwlsh());
                        }
                    }
                });
        checkParam(stCollectionUnitBusinessDetails);
        //在途验证
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails1 = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dwzh", unitPayHoldPost.getDWZH());
                this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.缓缴处理.getCode());
                this.put("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("unitPayholdVice.deleted", false);
            }
        }).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.ne("ywlsh", stCollectionUnitBusinessDetails.getYwlsh()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionUnitBusinessDetails1 != null) {
            throw new ErrorException(Business_In_Process);
        }

        saveAuditHistory.saveNormalBusiness(stCollectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.缓缴处理.getName(), "新建");

        return new ReUnitDepositRatioRes() {{
            this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());
            this.setStatus("sucess");
        }};
    }


    private void checkParam(StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails){
        if(stCollectionUnitBusinessDetails==null||stCollectionUnitBusinessDetails.getExtension()==null||stCollectionUnitBusinessDetails.getUnitPayholdVice()==null){
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        CCollectionUnitPayholdVice cCollectionUnitInformationActionVice = stCollectionUnitBusinessDetails.getUnitPayholdVice();
        if(!StringUtil.notEmpty(stCollectionUnitBusinessDetails.getDwzh())) {throw new ErrorException(ReturnEnumeration.Data_MISS,"单位账号");}
        if(cCollectionUnitInformationActionVice.getHjfssj()==null) {throw new ErrorException(ReturnEnumeration.Data_MISS,"申请缓缴开始年月");}
        if(cCollectionUnitInformationActionVice.getHjjssj()==null) {throw new ErrorException(ReturnEnumeration.Data_MISS,"申请缓缴结束年月");}
        if(!StringUtil.notEmpty(stCollectionUnitBusinessDetails.getExtension().getQtczyy())) {throw new ErrorException(ReturnEnumeration.Data_MISS,"缓缴原因");}
        if(cCollectionUnitInformationActionVice.getQianNianKS().compareTo(new BigDecimal("0"))!=0&&cCollectionUnitInformationActionVice.getQianNianYL().compareTo(new BigDecimal("0"))!=0) {throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"前年亏损与盈利只能一个有值");}
        if(cCollectionUnitInformationActionVice.getQuNianKS().compareTo(new BigDecimal("0"))!=0&&cCollectionUnitInformationActionVice.getQuNianYL().compareTo(new BigDecimal("0"))!=0) {throw new ErrorException(ReturnEnumeration.Data_MISS,"去年亏损与盈利只能一个有值");}
    }

    @Override
    public ReUnitAcctDropRes reUnitPayhold(TokenContext tokenContext, String YWLSH, UnitPayHoldPut unitPayHoldPut) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务("+YWLSH+")不存在");
        }
        StCommonUnit commonUnit = instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("dwzh", unitPayHoldPut.getDWZH());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (!tokenContext.getUserInfo().getCZY().equals(collectionUnitBusinessDetails.getExtension().getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能修改");
        }
        //办理资料验证
        if (unitPayHoldPut.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.单位缓缴.getCode(), unitPayHoldPut.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        Date SQHJKSNY = null;
        Date SQHJJSNY = null;
        if (!StringUtil.notEmpty(unitPayHoldPut.getSQYHJSJKS()) || !StringUtil.notEmpty(unitPayHoldPut.getSQYHJSJJS())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "申请缓缴开始年月或申请缓缴结束年月");
        }
        try {
            SQHJKSNY = new SimpleDateFormat(formatNY).parse(unitPayHoldPut.getSQYHJSJKS());
            SQHJJSNY = new SimpleDateFormat(formatNY).parse(unitPayHoldPut.getSQYHJSJJS());
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH ,"时间格式有误");
        }
        if (SQHJKSNY.getTime() > SQHJJSNY.getTime()) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能大于结束月份");
        }
        Date jzny = null;
        try {
            jzny = DateUtil.str2Date("yyyyMM", commonUnit.getCollectionUnitAccount().getJzny());
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "缴至年月格式不正确");
        }
//        if (commonUnit.getCollectionUnitAccount().getJzny() != null && jzny != null && jzny.getTime() >= SQHJKSNY.getTime()) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能小于等于缴至年月");
//        }
        String yhjny = DateUtil.getNextMonthStr(commonUnit.getCollectionUnitAccount().getJzny());
        try {
            if(DateUtil.str2Date("yyyyMM",yhjny).compareTo(SQHJKSNY)!=0){
                throw new ErrorException(ReturnEnumeration.User_Defined,"开始月份应等于应汇缴年月");
            }
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "缴至年月格式不正确");
        }

        Date SQYHJSJKS = ComUtils.parseToDate(unitPayHoldPut.getSQYHJSJKS(), "yyyy-MM");
        Date SQYHJSJJS = ComUtils.parseToDate(unitPayHoldPut.getSQYHJSJJS(), "yyyy-MM");

        collectionUnitBusinessDetails.getExtension().setCzy(tokenContext.getUserInfo().getCZY());//操作人
        collectionUnitBusinessDetails.getExtension().setBlzl(unitPayHoldPut.getBLZL());//办理资料
        collectionUnitBusinessDetails.getExtension().setQtczyy(unitPayHoldPut.getHJYY());//销户原因
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
        collectionUnitBusinessDetails.getExtension().setYwwd(network);//业务网点
        collectionUnitBusinessDetails.getExtension().setJbrxm(unitPayHoldPut.getJBRXM());//经办人姓名
        collectionUnitBusinessDetails.getExtension().setJbrzjlx(unitPayHoldPut.getJBRZJLX());//经办人证件类型
        collectionUnitBusinessDetails.getExtension().setJbrzjhm(unitPayHoldPut.getJBRZJHM());//经办人证件号码
        UnitPayHoldPutQCQR unitPayHoldPutQCQR = unitPayHoldPut.getQCQR();
        collectionUnitBusinessDetails.getUnitPayholdVice().setHjfssj(SQYHJSJKS);
        collectionUnitBusinessDetails.getUnitPayholdVice().setHjjssj(SQYHJSJJS);
        collectionUnitBusinessDetails.getUnitPayholdVice().setQuNianKS(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQuNianKS())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQuNianKS().toString()));
        collectionUnitBusinessDetails.getUnitPayholdVice().setQianNianKS(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQianNianKS())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQianNianKS().toString()));
        collectionUnitBusinessDetails.getUnitPayholdVice().setQuNianYL(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQuNianYL())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQuNianYL().toString()));
        collectionUnitBusinessDetails.getUnitPayholdVice().setQianNianYL(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQianNianYL())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQianNianYL().toString()));
        collectionUnitBusinessDetails.getUnitPayholdVice().setQuNianRJGZ(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQuNianRYPJGZ())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQuNianRYPJGZ().toString()));
        collectionUnitBusinessDetails.getUnitPayholdVice().setQianNianRJGZ(!StringUtil.notEmpty(unitPayHoldPutQCQR.getQianNianRYPJGZ())?BigDecimal.ZERO:new BigDecimal(unitPayHoldPutQCQR.getQianNianRYPJGZ().toString()));


        StCollectionUnitBusinessDetails collectionUnitBusinessDetails1 = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(unitPayHoldPut.getCZLX()), new TaskEntity(collectionUnitBusinessDetails1.getYwlsh(), collectionUnitBusinessDetails1.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        collectionUnitBusinessDetails1.getExtension().getCzy(), collectionUnitBusinessDetails1.getExtension().getBeizhu(), BusinessSubType.归集_单位缓缴申请.getSubType(), BusinessType.Collection, collectionUnitBusinessDetails1.getExtension().getYwwd().getId()),
                new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }

                        if (!succeed || !StringUtil.notEmpty(next) || e != null) {
                            return;
                        }

                        if (succeed) {
                            collectionUnitBusinessDetails1.getExtension().setStep(next);

                            if (StringUtil.isIntoReview(next, null))
                                collectionUnitBusinessDetails1.getExtension().setDdsj(new Date());

                              DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails1).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName())) doUnitPayhold(YWLSH);
                        }
                    }
                });
        if (unitPayHoldPut.getCZLX().equals("1"))
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.缓缴处理.getName(), "修改");

        checkParam(collectionUnitBusinessDetails1);
        return new ReUnitAcctDropRes() {{
            this.setYWLSH(YWLSH);
            this.setStatus("sucess");
        }};
    }

    @Override
    public GetUnitPayHoldRes showUnitPayhold(String YWLSH) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (collectionUnitBusinessDetails == null || collectionUnitBusinessDetails.getUnitPayholdVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务("+YWLSH+")不存在");
        }
        final CCollectionUnitPayholdVice payhold = collectionUnitBusinessDetails.getUnitPayholdVice();
        return new GetUnitPayHoldRes() {{
            this.setBLZL(StringUtil.notEmpty(collectionUnitBusinessDetails.getExtension().getBlzl()) ? collectionUnitBusinessDetails.getExtension().getBlzl() : "");//办理资料

            this.setJBRZJLX(collectionUnitBusinessDetails.getExtension().getJbrzjlx());//经办人证件类型

            this.setYWLSH(YWLSH);//业务流水号

            this.setSQHJKSNY(DateUtil.date2Str(payhold.getHjfssj(), formatNY));//申请缓缴开始年月

            this.setJBRXM(collectionUnitBusinessDetails.getExtension().getJbrxm());//经办人姓名

            // private  ArrayList<GetUnitPayHoldResQCQR>    QCQR;  //单位前两年经营情况
            this.setQCQR(new GetUnitPayHoldResQCQR() {{
                this.setQianNianKS(payhold.getQianNianKS()+"");
                this.setQianNianRJGZ(payhold.getQianNianRJGZ()+"");
                this.setQianNianYL(payhold.getQianNianYL()+"");

                this.setQuNianKS(payhold.getQuNianKS()+"");
                this.setQuNianRJGZ(payhold.getQuNianRJGZ()+"");
                this.setQuNianYL(payhold.getQuNianYL()+"");
            }});

            this.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//操作网点

            this.setJZNY(DateUtil.dateStrTransform(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()));//缴至年月

            this.setHJYY(collectionUnitBusinessDetails.getExtension().getQtczyy());//缓缴原因

            this.setJBRZJHM(collectionUnitBusinessDetails.getExtension().getJbrzjhm());//经办人证件号码

            this.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

            if (StringUtil.isEmpty(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())) {
                this.setDWYHJNY(DateUtil.str2str(DateUtil.getNextMonth(collectionUnitBusinessDetails.getUnit().getExtension().getDwschjny()), 6));
            } else {
                this.setDWYHJNY(DateUtil.str2str(DateUtil.getNextMonth(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()), 6));//单位应汇缴年月
            }
            this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员

            this.setDWZH(collectionUnitBusinessDetails.getUnit().getDwzh());//单位账号

            this.setSQHJJSNY(DateUtil.date2Str(payhold.getHjjssj(), formatNY));//申请缓缴结束年月

        }};
    }

    @Override
    public CommonResponses headUnitPayhold(String YWLSH) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collectionUnitBusinessDetailsDAO.getByYwlsh(YWLSH);
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务("+YWLSH+")不存在");
        }
        final CCollectionUnitPayholdVice unitPayholdVice = collectionUnitBusinessDetails.getUnitPayholdVice();
        String step = unitPayholdVice.getDwywmx().getExtension().getStep();
        if (!CollectionBusinessStatus.办结.getName().equals(step)) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务未办结，当前业务状态为" + step);
        }
        HeadUnitPayHoldReceiptRes result = new HeadUnitPayHoldReceiptRes();

        result.setJBRZJLX(collectionUnitBusinessDetails.getExtension().getJbrzjlx());//经办人证件类型

        result.setYWLSH(YWLSH);//业务流水号

        result.setSQHJKSNY(DateUtil.date2Str(unitPayholdVice.getHjfssj(), formatNY));//申请缓缴开始年月

        result.setJBRXM(collectionUnitBusinessDetails.getExtension().getJbrxm());//经办人姓名

        result.setJZNY(DateUtil.dateStrTransform(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()));//缴至年月

        result.setHJYY(collectionUnitBusinessDetails.getExtension().getQtczyy());//缓缴原因

        //result.setTZSJ(collectionUnitBusinessDetails.);//填制时间

        // result.setYZM(collectionUnitBusinessDetails.getExtension());//验证码
        result.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());

        result.setJBRZJHM(collectionUnitBusinessDetails.getExtension().getJbrzjhm());//经办人证件号码

        result.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

        if (StringUtil.isEmpty(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())) {
            result.setDWYHJNY(DateUtil.str2str(DateUtil.getNextMonth(collectionUnitBusinessDetails.getUnit().getExtension().getDwschjny()),6));
        } else {
            result.setDWYHJNY(DateUtil.str2str(DateUtil.getNextMonth(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny()),6));//单位应汇缴年月
        }

        result.setDWZH(collectionUnitBusinessDetails.getUnit().getDwzh());//单位账号

        result.setSQHJJSNY(DateUtil.date2Str(unitPayholdVice.getHjjssj(), formatNY));//申请缓缴结束年月

        result.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
        //审核人，该条记录审核通过的操作员
        CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
                this.put("shjg","01");
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if(cAuditHistory!=null){
            result.setSHR(cAuditHistory.getCzy());
        }
        String id = pdfService.getUnitPayholdReceiptPdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public void doUnitPayhold(String YWLSH) {
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务("+YWLSH+")不存在");
        }
        stCollectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().setDwzhzt(UnitAccountStatus.缓缴.getCode());
        DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(stCollectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(YWLSH, CollectionBusinessType.缓缴处理.getName(), "办结");
        doPayHoldTimeTask(YWLSH,DateUtil.getNextMonthStr(DateUtil.date2Str(stCollectionUnitBusinessDetails.getUnitPayholdVice().getHjjssj(),"yyyyMM")));
    }


    @Override
    public AddIndiAcctActionRes submitUnitPayHold(TokenContext tokenContext, ArrayList<String> ywlshs) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        for (String ywlsh : ywlshs) {
            if (!StringUtil.notEmpty(ywlsh)) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS);
            }
            StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywlsh", ywlsh);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (stCollectionUnitBusinessDetails == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应业务");
            }
            if (!tokenContext.getUserInfo().getCZY().equals(stCollectionUnitBusinessDetails.getExtension().getCzy())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + ywlsh + ")不是由您受理的，不能提交");
            }
            checkParam(stCollectionUnitBusinessDetails);
            //办理资料验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.单位缓缴.getCode(), stCollectionUnitBusinessDetails.getExtension().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            StateMachineUtils.updateState(this.iStateMachineService, Events.通过.getEvent(), new TaskEntity(stCollectionUnitBusinessDetails.getYwlsh(), stCollectionUnitBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                            stCollectionUnitBusinessDetails.getExtension().getCzy(), stCollectionUnitBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_单位缓缴申请.getSubType(), BusinessType.Collection, stCollectionUnitBusinessDetails.getExtension().getYwwd().getId()),
                    new StateMachineUtils.StateChangeHandler() {

                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {

                            if (e != null) {
                                throw new ErrorException(e);
                            }

                            if (!succeed || !StringUtil.notEmpty(next) || e != null) {
                                return;
                            }

                            stCollectionUnitBusinessDetails.getExtension().setStep(next);

                            if (StringUtil.isIntoReview(next, null))
                                stCollectionUnitBusinessDetails.getExtension().setDdsj(new Date());

                            DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(stCollectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {

                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName())) doUnitPayhold(ywlsh);
                        }
                    });

        }
        return new AddIndiAcctActionRes() {{
            this.setStatus("success");
        }};
    }

    @Override
    public void doUpdateUnitState(String YWLSH) {
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务("+YWLSH+")不存在");
        }
        if(stCollectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.缓缴.getCode())){
            stCollectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().setDwzhzt(UnitAccountStatus.正常.getCode());
        }
        DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(stCollectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }
    /**
     * 产生定时
     */
    private void doPayHoldTimeTask(String ywlsh, String sxny) {
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx(CollectionBusinessType.缓缴处理.getCode());
        task.setYwms("缓缴业务");
        task.setZxcs(0);
        task.setZxsj(sxny);
        timedTaskDAO.save(task);
    }
}
