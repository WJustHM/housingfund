package com.handge.housingfund.collection.service.unitinfomanage;

import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctSeal;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
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

import java.text.SimpleDateFormat;
import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Business_In_Process;
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;

/**
 * Created by 向超 on 2017/7/19.
 */
@Component
public class UnitAcctSealImpl implements UnitAcctSeal {
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;
    @Autowired
    private ICCollectionIndividualAccountActionViceDAO accountActionViceDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionUnitInformationActionViceDAO collectionUnitInformationActionViceDAO;
    @Autowired
    private IStCollectionUnitAccountDAO collectionUnitAccountDAO;
    @Autowired
    private IStateMachineService iStateMachineService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";

    @Autowired
    private IDictionaryService iDictionaryService;

    // completed 状态处理销户类状态、已封存状态不能办理
    @Override
    public AddUnitAcctSealedRes addUnitAcctSealed(TokenContext tokenContext, UnitAcctSealedPost unitSealedPost) {
        //检查上传资料
        if (unitSealedPost.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.单位账号封存.getCode(), unitSealedPost.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        StCommonUnit commonUnit = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("dwzh", unitSealedPost.getDWZH());
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
        if (!commonUnit.getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.正常.getCode()) && !commonUnit.getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.缓缴.getCode())) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前单位的账户状态不能被封存");
        }
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();//构造单位明细信息
        collectionUnitBusinessDetails.setDwzh(unitSealedPost.getDWZH());//单位账号

        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetailsExtension.setBeizhu(unitSealedPost.getBeiZhu());//备注
        collectionUnitBusinessDetailsExtension.setBlzl(unitSealedPost.getBLZL());//办理资料
        collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.封存.getCode());//操作名称
        collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        collectionUnitBusinessDetailsExtension.setFchxhyy(unitSealedPost.getFCHXHYY());// 操作原因
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
        collectionUnitBusinessDetailsExtension.setSlsj(new Date());
        collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        if (!Arrays.asList("0", "1").contains(unitSealedPost.getCZLX()))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型");

        CCollectionUnitInformationActionVice collectionUnitInformationActionVice = new CCollectionUnitInformationActionVice();//构造副表信息
        collectionUnitInformationActionVice.setDwzh(unitSealedPost.getDWZH());//单位账号
        collectionUnitInformationActionVice.setBeiZhu(unitSealedPost.getBeiZhu());//备注
        collectionUnitInformationActionVice.setBlzl(unitSealedPost.getBLZL());//办理资料
        collectionUnitInformationActionVice.setCzmc(CollectionBusinessType.封存.getCode());//操作名称
        collectionUnitInformationActionVice.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        collectionUnitInformationActionVice.setFchxhyy(unitSealedPost.getFCHXHYY());// 操作原因
        collectionUnitInformationActionVice.setYwwd(tokenContext.getUserInfo().getYWWD());//业务网点

        collectionUnitInformationActionVice.setDwywmx(collectionUnitBusinessDetails);
        collectionUnitBusinessDetails.setUnitInformationActionVice(collectionUnitInformationActionVice);
        collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());
        collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);
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

                }}.get(unitSealedPost.getCZLX()), new TaskEntity(stCollectionUnitBusinessDetails.getYwlsh(), stCollectionUnitBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        stCollectionUnitBusinessDetails.getExtension().getCzy(), stCollectionUnitBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_单位账户封存.getSubType(), BusinessType.Collection, stCollectionUnitBusinessDetails.getExtension().getYwwd().getId()),
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
                                doUnitAcctSeal(stCollectionUnitBusinessDetails.getYwlsh());
                        }
                    }
                });
        //在途验证start
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails1 = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("unitInformationActionVice.dwzh", unitSealedPost.getDWZH());
                this.put("unitInformationActionVice.czmc", CollectionBusinessType.封存.getCode());
                this.put("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("unitInformationActionVice.deleted", false);
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
        if (unitSealedPost.getCZLX().equals("1")) {
            checkParams(stCollectionUnitBusinessDetails);
        }
        saveAuditHistory.saveNormalBusiness(stCollectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.封存.getName(), "新建");
        // //在途验证end
        return new AddUnitAcctSealedRes() {
            {
                this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());
                this.setStatus("success");
            }
        };
    }


    @Override
    public ReUnitAcctSealedRes reUnitAcctSealed(TokenContext tokenContext, String YWLSH, UnitAcctSealedPut unitSealedPut) {
        //检查上传资料
        if (unitSealedPut.getDWGJXX().getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.单位账号封存.getCode(), unitSealedPut.getDWGJXX().getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if (!tokenContext.getUserInfo().getCZY().equals(collectionUnitBusinessDetails.getExtension().getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied);
        }
        //更新单位明细表
        collectionUnitBusinessDetails.getExtension().setBeizhu(unitSealedPut.getDWGJXX().getBeiZhu());//备注
        collectionUnitBusinessDetails.getExtension().setCzy(tokenContext.getUserInfo().getCZY());//操作人
        collectionUnitBusinessDetails.getExtension().setBlzl(unitSealedPut.getDWGJXX().getBLZL());//办理资料
        collectionUnitBusinessDetails.getExtension().setFchxhyy(unitSealedPut.getDWGJXX().getFCHXHYY());//封存原因
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
        //更新单位副表
        collectionUnitBusinessDetails.getUnitInformationActionVice().setBeiZhu(unitSealedPut.getDWGJXX().getBeiZhu());//备注
        collectionUnitBusinessDetails.getUnitInformationActionVice().setCzy(tokenContext.getUserInfo().getCZY());//操作人
        collectionUnitBusinessDetails.getUnitInformationActionVice().setBlzl(unitSealedPut.getDWGJXX().getBLZL());//办理资料
        collectionUnitBusinessDetails.getUnitInformationActionVice().setFchxhyy(unitSealedPut.getDWGJXX().getFCHXHYY());//封存原因
        collectionUnitBusinessDetails.getUnitInformationActionVice().setYwwd(tokenContext.getUserInfo().getYWWD());//业务网点

        if (!Arrays.asList("0", "1").contains(unitSealedPut.getDWGJXX().getCZLX())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型不匹配");
        }

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(unitSealedPut.getDWGJXX().getCZLX()), new TaskEntity(collectionUnitBusinessDetails.getYwlsh(), collectionUnitBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        collectionUnitBusinessDetails.getExtension().getCzy(), collectionUnitBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_单位账户封存.getSubType(), BusinessType.Collection, collectionUnitBusinessDetails.getExtension().getYwwd().getId()),
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
                            collectionUnitBusinessDetails.getExtension().setStep(next);

                            if (StringUtil.isIntoReview(next, null))
                                collectionUnitBusinessDetails.getExtension().setDdsj(new Date());

                            DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName())) doUnitAcctSeal(YWLSH);
                        }
                    }
                });
        if (unitSealedPut.getDWGJXX().getCZLX().equals("1")) {
            checkParams(collectionUnitBusinessDetails);
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.封存.getName(), "修改");
        }
        return new ReUnitAcctSealedRes() {
            {
                this.setYWLSH(YWLSH);
                this.setStatus("success");
            }
        };
    }


    @Override
    public GetUnitAcctSealedRes getUnitAcctSealed(String YWLSH) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder
                .instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {// 获取单位明细信息
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        StCommonUnit commonUnit = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {
            {// 获取单位信息
                this.put("dwzh", collectionUnitBusinessDetails.getDwzh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonUnit == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的单位");
        }
        return new GetUnitAcctSealedRes() {
            {
                this.setDWFCXX(new GetUnitAcctSealedResDWFCXX() {
                    {
                        this.setYWLSH(YWLSH);// 业务流水号
                        this.setDWZH(collectionUnitBusinessDetails.getDwzh());// 单位账号
                        this.setDWMC(commonUnit.getDwmc());// 单位名称
                        this.setJZNY(DateUtil.str2str(commonUnit.getCollectionUnitAccount().getJzny(), 6));
                        this.setDWFRDBXM(commonUnit.getDwfrdbxm());// 单位法人代表姓名
                        this.setDWFRDBZJHM(commonUnit.getDwfrdbzjhm());// 单位法人代表证件号码
                        this.setDWLB(commonUnit.getExtension().getDwlb());// 单位类别
                        this.setDJSYYZ(commonUnit.getExtension().getDjsyyz());// 登记使用印章
                        this.setZZJGDM(commonUnit.getZzjgdm());// 组织机构代码
                        this.setJBRXM(commonUnit.getJbrxm());// 经办人姓名
                        this.setJBRZJLX(commonUnit.getJbrzjlx());// 经办人证件类型
                        this.setJBRZJHM(commonUnit.getJbrzjhm());// 经办人证件号码
                        this.setJBRSJHM(commonUnit.getJbrsjhm());// 经办人手机号码
                        this.setJBRGDDHHM(commonUnit.getJbrgddhhm());// 经办人固定电话号码
                        this.setDWFCYY(collectionUnitBusinessDetails.getExtension().getFchxhyy());// 单位封存原因
                        this.setBeiZhu(collectionUnitBusinessDetails.getExtension().getBeizhu());// 备注
                        this.setBLZL(StringUtil.notEmpty(collectionUnitBusinessDetails.getExtension().getBlzl()) ? collectionUnitBusinessDetails.getExtension().getBlzl() : "");// 办理资料
                        this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());// 操作人
                        this.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());// 业务网点
                    }
                });
            }
        };
    }

    // completed 返回model需修改
    @Override
    public PageRes<ListUnitAcctSealedResRes> showUnitAcctsSealed(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai,String KSSJ,String JSSJ, String page, String pageSize) {

        PageRes pageRes = new PageRes();
        int page_number = 1;

        int pagesize_number = 10;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsList = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("unit.dwzh", DWZH);
            if (StringUtil.notEmpty(DWMC)) this.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(DWLB)) this.put("unit.cCommonUnitExtension.dwlb", DWLB);
            if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))
                this.put("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai);
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.封存.getCode());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");

                criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
            }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageRes<ListUnitAcctSealedResRes>() {
            {
                this.setResults(new ArrayList<ListUnitAcctSealedResRes>() {
                    {
                        for (StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails : stCollectionUnitBusinessDetailsList) {
                            this.add(new ListUnitAcctSealedResRes() {
                                {
                                    this.setZZJGDM(stCollectionUnitBusinessDetails.getUnit().getZzjgdm());//组织机构代码

                                    this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());//业务流水号

                                    this.setSLSJ(DateUtil.date2Str(stCollectionUnitBusinessDetails.getCreated_at(), "yyyy-MM-dd HH:mm"));//受理时间

                                    this.setZhuangTai(stCollectionUnitBusinessDetails.getExtension().getStep());//状态

                                    String ywwdmc = stCollectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng();
                                    this.setXZQY(ywwdmc);//改为业务网点

                                    this.setDWMC(stCollectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

                                    this.setDWZH(stCollectionUnitBusinessDetails.getDwzh());//单位账号

                                    this.setCZY(stCollectionUnitBusinessDetails.getExtension().getCzy());//操作员

                                    this.setDWLB(stCollectionUnitBusinessDetails.getUnit().getExtension().getDwlb());//单位类别
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
        };
    }

    @Override
    public PageResNew<ListUnitAcctSealedResRes> showUnitAcctsSealedNew(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai,String KSSJ,String JSSJ, String marker, String pageSize,String action) {

        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsList = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("unit.dwzh", DWZH);
            if (StringUtil.notEmpty(DWMC)) this.put("unit.dwmc", DWMC);
            if (StringUtil.notEmpty(DWLB)) this.put("unit.cCommonUnitExtension.dwlb", DWLB);

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createCriteria("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");
                criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.封存.getCode()));
                if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName()) && !ZhuangTai.equals(CollectionBusinessStatus.待审核.getName()))
                 criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
                if (CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
                }
                if(StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())&&!"1".equals(tokenContext.getUserInfo().getYWWD())){
                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");

                    criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }}
        }).betweenDate(timeTransform(KSSJ,JSSJ)[0],timeTransform(KSSJ,JSSJ)[1]).pageOption(marker, action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageResNew<ListUnitAcctSealedResRes>() {
            {
                this.setResults(action,new ArrayList<ListUnitAcctSealedResRes>() {
                    {
                        for (StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails : stCollectionUnitBusinessDetailsList) {
                            this.add(new ListUnitAcctSealedResRes() {
                                {
                                    this.setId(stCollectionUnitBusinessDetails.getId());
                                    this.setZZJGDM(stCollectionUnitBusinessDetails.getUnit().getZzjgdm());//组织机构代码

                                    this.setYWLSH(stCollectionUnitBusinessDetails.getYwlsh());//业务流水号

                                    this.setSLSJ(DateUtil.date2Str(stCollectionUnitBusinessDetails.getCreated_at(), "yyyy-MM-dd HH:mm"));//受理时间

                                    this.setZhuangTai(stCollectionUnitBusinessDetails.getExtension().getStep());//状态

                                    String ywwdmc = stCollectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng();
                                    this.setXZQY(ywwdmc);//改为业务网点

                                    this.setDWMC(stCollectionUnitBusinessDetails.getUnit().getDwmc());//单位名称

                                    this.setDWZH(stCollectionUnitBusinessDetails.getDwzh());//单位账号

                                    this.setCZY(stCollectionUnitBusinessDetails.getExtension().getCzy());//操作员

                                    this.setDWLB(stCollectionUnitBusinessDetails.getUnit().getExtension().getDwlb());//单位类别
                                }
                            });
                        }
                    }
                });

            }
        };
    }

    @Override
    public CommonResponses headUnitAcctsSealedReceipt(TokenContext tokenContext, String YWLSH) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder
                .instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                    {// 获取单位明细信息
                        this.put("ywlsh", YWLSH);
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        StCommonUnit commonUnit = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {
            {// 获取单位信息
                this.put("dwzh", collectionUnitBusinessDetails.getDwzh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (commonUnit == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的单位");
        }
        HeadUnitAcctActionRes result = new HeadUnitAcctActionRes();
        HeadUnitAcctActionResDWGJXX DWGJXX = new HeadUnitAcctActionResDWGJXX();
        DWGJXX.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd()==null?"未知网点":collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
        DWGJXX.setYWLSH(YWLSH);// 业务流水号
        DWGJXX.setRiQi(new SimpleDateFormat(format).format(new Date()));// 日期
        DWGJXX.setDWZH(collectionUnitBusinessDetails.getDwzh());// 单位账号
        DWGJXX.setDWMC(commonUnit.getDwmc());// 单位名称
        DWGJXX.setJZNY(
                DateUtil.dateStrTransform(commonUnit.getCollectionUnitAccount().getJzny()));// 缴至年月
        DWGJXX.setDWLB(commonUnit.getExtension().getDwlb());// 单位类别
        DWGJXX.setZZJGDM(commonUnit.getZzjgdm());// 组织机构代码
        DWGJXX.setDJSYYZ(commonUnit.getExtension().getDjsyyz());// 登记使用印章
        DWGJXX.setJBRXM(commonUnit.getJbrxm());// 经办人姓名
        DWGJXX.setJBRSJHM(commonUnit.getJbrsjhm());// 经办人手机号码
        DWGJXX.setJBRZJHM(commonUnit.getJbrzjhm());// 经办人证件号码
        DWGJXX.setDWFRDBXM(commonUnit.getDwfrdbxm());// 单位法人代表姓名
        DWGJXX.setDWFRDBZJHM(commonUnit.getDwfrdbzjhm());// 单位法人代表证件号码
        if (collectionUnitBusinessDetails.getExtension().getFchxhyy() != null) {
            SingleDictionaryDetail dictionaryDetail = iDictionaryService.getSingleDetail(collectionUnitBusinessDetails.getExtension().getFchxhyy(), "UnitSealed");
            DWGJXX.setFCHXHYY(dictionaryDetail != null ? dictionaryDetail.getName() : "");
        }
//        DWGJXX.setFCHXHYY(collectionUnitBusinessDetails.getExtension().getFchxhyy());// 单位操作原因（销户、启封、封存）
        DWGJXX.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());
        DWGJXX.setBeiZhu(collectionUnitBusinessDetails.getExtension().getBeizhu());// 备注
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
            DWGJXX.setSHR(cAuditHistory.getCzy());
        }
        result.setDWGJXX(DWGJXX);
        String id = pdfService.getUnitAcctSealReceiptPdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }



    @Override
    public void doUnitAcctSeal(String YWLSH) {

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                e.printStackTrace();
                throw new ErrorException(e);
            }
        });
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if (collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.正常.getCode()) || collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.缓缴.getCode())) {
            List<StCommonPerson> stCommonPersons = DAOBuilder.instance(commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
                   this.put("unit.dwzh",collectionUnitBusinessDetails.getUnit().getDwzh());
            }}).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                }
            });
            for(StCommonPerson commonPerson:stCommonPersons){
                if(commonPerson.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.正常.getCode())){
                   String jzny = collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny();
                    try {
                        if(DateUtil.str2Date("yyyy-MM",BusUtils.getDWYHJNY(collectionUnitBusinessDetails.getUnit().getDwzh())).compareTo(new Date())>0){
                            HashMap<String,String> hashMap = doSealDirect(commonPerson,collectionUnitBusinessDetails.getExtension().getCzy(),collectionUnitBusinessDetails.getExtension().getYwwd().getId());
                            doSaveTimer(hashMap.get("ywlsh"),hashMap.get("dwzh"));
                        }else if(DateUtil.str2Date("yyyy-MM",BusUtils.getDWYHJNY(collectionUnitBusinessDetails.getUnit().getDwzh())).compareTo(new Date())<=0){
                            doSealDirect(commonPerson,collectionUnitBusinessDetails.getExtension().getCzy(),collectionUnitBusinessDetails.getExtension().getYwwd().getId());
                            commonPerson.getCollectionPersonalAccount().setGrzhzt(PersonAccountStatus.封存.getCode());
                            commonPersonDAO.save(commonPerson);
                        }
                    } catch (Exception e) {
                        throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"缴至年月异常");
                    }
                }
            }
            collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().setDwzhzt(UnitAccountStatus.封存.getCode());//设置单位账户状态为封存
            collectionUnitBusinessDetails.getExtension().setBjsj(new Date());
        } else {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前单位的账户状态不能被封存");
        }
        DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(YWLSH, CollectionBusinessType.封存.getName(), "办结");

    }
    //直接做一笔个人封存业务
    private HashMap<String,String> doSealDirect(StCommonPerson commonPerson, String czy, String ywwd) {

        StCommonPerson person = commonPersonDAO.getByGrzh(commonPerson.getGrzh());
        StCommonUnit unit = person.getUnit();
        String dwzh = unit.getDwzh();
        CCollectionIndividualAccountActionVice seal = new CCollectionIndividualAccountActionVice();
        seal.setGrzh(commonPerson.getGrzh());
        seal.setCzmc(CollectionBusinessType.封存.getCode());
        seal.setDwzh(dwzh);
        // 设置副表信息
        seal.setBlzl("");// 办理资料
        seal.setSxny(DateUtil.str2str(BusUtils.getDWYHJNY(dwzh),-1));//生效年月
        seal.setCzyy("99");// 操作原因
        seal.setBeiZhu("单位封存导致的个人封存");// 备注
        seal.setSlsj(new Date());// 受理时间-------------等待审核阶段传入
        seal.setCzy(czy);// 操作员

        StCollectionPersonalBusinessDetails grywmx = new StCollectionPersonalBusinessDetails();
        grywmx.setGrzh(commonPerson.getGrzh());
        grywmx.setGjhtqywlx(CollectionBusinessType.封存.getCode());
        grywmx.setPerson(person);
        grywmx.setUnit(unit);
        grywmx.setCreated_at(new Date());
        grywmx.setIndividualAccountActionVice(seal);

        CCollectionPersonalBusinessDetailsExtension extention = new CCollectionPersonalBusinessDetailsExtension();
        extention.setStep(CollectionBusinessStatus.办结.getName());
        extention.setCzmc(CollectionBusinessType.封存.getCode());    //封存
        extention.setBjsj(new Date());
        extention.setSlsj(new Date());

        extention.setCzy(czy);// 操作员
        extention.setCzyy("99");// 操作原因
        CAccountNetwork network = cAccountNetworkDAO.get(ywwd);

        extention.setYwwd(network);// 业务网点
        extention.setBeizhu("单位封存导致的个人封存");//备注
        extention.setJbrxm(unit.getJbrxm()); //经办人姓名
        extention.setJbrzjhm(unit.getJbrzjhm()); //经办人证件号码
        extention.setJbrzjlx(unit.getJbrzjlx()); //经办人类型
        extention.setBlzl("");//办理资料
        grywmx.setExtension(extention);

        seal.setGrywmx(grywmx);

        CCollectionIndividualAccountActionVice cCollectionIndividualAccountActionVice = DAOBuilder.instance(accountActionViceDAO).entity(seal).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        HashMap<String,String> hashMap =new HashMap<String,String>();
        hashMap.put("ywlsh",cCollectionIndividualAccountActionVice.getGrywmx().getYwlsh());
        hashMap.put("dwzh",dwzh);
        return hashMap;
    }

    private void doSaveTimer(String ywlsh,String dwzh){
        //插入定时任务表
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx(CollectionBusinessType.封存.getCode());
        task.setYwms("封存业务");
        task.setZxcs(0);
        task.setZxsj(DateUtil.str2str(BusUtils.getDWYHJNY(dwzh),-1));
        timedTaskDAO.save(task);
    }

    @Override
    public PostUnitAcctSealedSubmitRes submitUnitAcctSeal(TokenContext tokenContext, List<String> YWLSHs) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        for (String YWLSH : YWLSHs) {
            if (YWLSH == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
            }
            StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywlsh", YWLSH);
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
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            }
            //检查上传资料
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.单位账号封存.getCode(), stCollectionUnitBusinessDetails.getExtension().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            StateMachineUtils.updateState(this.iStateMachineService, Events.通过.getEvent(), new TaskEntity(stCollectionUnitBusinessDetails.getYwlsh(), stCollectionUnitBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                            tokenContext.getUserInfo().getCZY(), stCollectionUnitBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_单位账户封存.getSubType(), BusinessType.Collection, tokenContext.getUserInfo().getYWWD()),
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
                            if (next.equals(CollectionBusinessStatus.办结.getName())) {
                                doUnitAcctSeal(YWLSH);
                            }
                        }
                    });
            checkParams(stCollectionUnitBusinessDetails);
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.封存.getName(), "修改");
        }
        return new PostUnitAcctSealedSubmitRes() {{
            this.setStatus("success");
        }};
    }

    private void checkParams(StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails) {
        if (stCollectionUnitBusinessDetails.getExtension() == null || stCollectionUnitBusinessDetails.getUnitInformationActionVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        if (StringUtil.isEmpty(stCollectionUnitBusinessDetails.getDwzh())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "单位账号");
        }
        if (StringUtil.isEmpty(stCollectionUnitBusinessDetails.getExtension().getFchxhyy())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "封存原因");
        }
    }


}

