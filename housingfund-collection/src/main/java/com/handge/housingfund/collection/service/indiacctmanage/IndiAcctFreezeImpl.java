package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctFreeze;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Business_In_Process;
import static com.handge.housingfund.database.enums.BusinessSubType.归集_冻结个人账户;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by 向超 on 2017/8/2.
 * (value = "action.freeze")
 */
@Service
public class IndiAcctFreezeImpl extends IndiAcctActionImpl implements IndiAcctFreeze {

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
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";


    @Override
    public PageRes<ListOperationAcctsResRes> getAcctAcionInfo(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH,
                                                              String CZMC, String CZYY, String KSSJ,String JSSJ ,String pageNo, String pageSize) {
        return super.getAcctAcionInfo(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY, KSSJ, JSSJ ,pageNo, pageSize);
    }

    @Override
    public PageResNew<ListOperationAcctsResRes> getAcctAcionInfoNew(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH, String CZMC, String CZYY, String KSSJ, String JSSJ, String marker, String pageSize, String action) {
        return super.getAcctAcionInfoNew(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY, KSSJ, JSSJ, marker, pageSize, action);
    }

    @Override
    public AddIndiAcctActionRes addAcctAction(TokenContext tokenContext, String grzh, IndiAcctActionPost addIndiAcctAction) {
        System.out.println("冻结个人账户创建");
        //检查上传资料
        if (addIndiAcctAction.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人账户冻结.getCode(), addIndiAcctAction.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }

        paramCheck(addIndiAcctAction);

        String czmc = addIndiAcctAction.getCZMC();
        if (!czmc.equals(CollectionBusinessType.冻结.getCode())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作名称不匹配");
        }
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();
        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grzh", grzh);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人账号不存在");
        } else if (!Arrays.asList(PersonAccountStatus.正常.getCode(), PersonAccountStatus.封存.getCode()).contains(commonPerson.getCollectionPersonalAccount().getGrzhzt())) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "不能办理冻结业务");
        }
        if (!commonPerson.getUnit().getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }
        if (StringUtil.isEmpty(commonPerson.getExtension().getSfdj())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "是否冻结为空");
        }
        if (commonPerson.getExtension().getSfdj().equals("02")) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前账户已冻结，不能再办理冻结业务");
        } else if (!commonPerson.getExtension().getSfdj().equals("01")) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "是否冻结字段异常");
        }

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
        CCollectionIndividualAccountActionVice collectionIndividualAccountActionVice = new CCollectionIndividualAccountActionVice();


        collectionIndividualAccountActionVice.setGrzh(grzh);// 个人账号
        collectionIndividualAccountActionVice.setDwzh(commonPerson.getUnit().getDwzh());// 单位账号
        collectionIndividualAccountActionVice.setBlzl(addIndiAcctAction.getBLZL());// 办理资料
        collectionIndividualAccountActionVice.setCzyy(addIndiAcctAction.getCZYY());// 操作原因
        collectionIndividualAccountActionVice.setBeiZhu(addIndiAcctAction.getBeiZhu());// 备注
        collectionIndividualAccountActionVice.setSlsj(new Date());// 受理时间-------------等待审核阶段传入
        collectionIndividualAccountActionVice.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        collectionIndividualAccountActionVice.setYwwd(tokenContext.getUserInfo().getYWWD());// 业务网点
        collectionIndividualAccountActionVice.setDjje(commonPerson.getCollectionPersonalAccount().getGrzhye());// 冻结金额
        collectionIndividualAccountActionVice.setCreated_at(new Date());
        collectionIndividualAccountActionVice.setCzmc(addIndiAcctAction.getCZMC());// 操作名称
        // 设置业务拓展表信息
        collectionPersonalBusinessDetailsExtension.setCzmc(czmc);// 操作名称
        collectionPersonalBusinessDetailsExtension.setDjje(commonPerson.getCollectionPersonalAccount().getGrzhye());// 冻结金额
        collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        collectionPersonalBusinessDetailsExtension.setCzyy(addIndiAcctAction.getCZYY());// 操作原因
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
        collectionPersonalBusinessDetailsExtension.setYwwd(network);// 业务网点
        collectionPersonalBusinessDetailsExtension.setSlsj(new Date());
        collectionPersonalBusinessDetailsExtension.setBeizhu(addIndiAcctAction.getBeiZhu());//备注
        collectionPersonalBusinessDetailsExtension.setJbrxm(addIndiAcctAction.getJBRXM()); //经办人姓名
        collectionPersonalBusinessDetailsExtension.setJbrzjhm(addIndiAcctAction.getJBRZJHM()); //经办人证件号码
        collectionPersonalBusinessDetailsExtension.setJbrzjlx(addIndiAcctAction.getJBRZJLX()); //经办人类型
        collectionPersonalBusinessDetailsExtension.setBlzl(addIndiAcctAction.getBLZL());//办理资料
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        if (!Arrays.asList("0", "1").contains(addIndiAcctAction.getCZLX()))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型异常");
        // 设置业务信息拓展表表信息
        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
        collectionPersonalBusinessDetails.setGrzh(grzh);
        collectionPersonalBusinessDetails.setGjhtqywlx(czmc);
        collectionPersonalBusinessDetails.setPerson(commonPerson);
        collectionPersonalBusinessDetails.setUnit(commonPerson.getUnit());
        collectionPersonalBusinessDetails.setCreated_at(new Date());

        collectionPersonalBusinessDetails.setIndividualAccountActionVice(collectionIndividualAccountActionVice);
        collectionIndividualAccountActionVice.setGrywmx(collectionPersonalBusinessDetails);

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.通过.getEvent());

                    this.put("1", Events.提交.getEvent());

                }}.get(addIndiAcctAction.getCZLX()), new TaskEntity(stCollectionPersonalBusinessDetails.getYwlsh(), stCollectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        stCollectionPersonalBusinessDetails.getExtension().getCzy(), stCollectionPersonalBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_冻结个人账户.getSubType(), BusinessType.Collection, stCollectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
                new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!StringUtil.notEmpty(next) || e != null) {
                            return;
                        }
                        if (succeed) {
                            CCollectionPersonalBusinessDetailsExtension cectionPersonalBusinessDetailsExtension = stCollectionPersonalBusinessDetails.getExtension();
                            cectionPersonalBusinessDetailsExtension.setStep(next);
                            if (StringUtil.isIntoReview(next, null))
                                cectionPersonalBusinessDetailsExtension.setDdsj(new Date());
                            instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName()))
                                doAcctAction(tokenContext, stCollectionPersonalBusinessDetails.getYwlsh());
                        }
                    }
                });
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetailsData = instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("individualAccountActionVice.grzh", grzh);
                this.put("individualAccountActionVice.czmc", CollectionBusinessType.冻结.getCode());
                this.put("individualAccountActionVice.dwzh", commonPerson.getUnit().getDwzh());
                this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("individualAccountActionVice.deleted", false);
            }
        }).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.ne("ywlsh", stCollectionPersonalBusinessDetails.getYwlsh()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetailsData != null) {
            throw new ErrorException(Business_In_Process);
        }

         saveAuditHistory.saveNormalBusiness(stCollectionPersonalBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.冻结.getName(), "新建");

        return new AddIndiAcctActionRes() {{
            this.setStatus("success");
            this.setYWLSH(stCollectionPersonalBusinessDetails.getYwlsh());
        }};
    }

    @Override
    public ReIndiAcctActionRes reAcctAction(TokenContext tokenContext, String UID, IndiAcctActionPut reIndiAcctAction) {

        //办理资料验证
        if (reIndiAcctAction.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人账户冻结.getCode(), reIndiAcctAction.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        CCollectionIndividualAccountActionVice collectionIndividualAccountActionVice = instance(
                this.collectionIndividualAccountActionViceDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("grywmx.ywlsh", UID);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (collectionIndividualAccountActionVice == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if (!tokenContext.getUserInfo().getCZY().equals(collectionIndividualAccountActionVice.getGrywmx().getExtension().getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied);
        }
        String czmc = reIndiAcctAction.getCZMC();
        if (!czmc.equals(CollectionBusinessType.冻结.getCode())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作名称不匹配");
        }
        collectionIndividualAccountActionVice.setUpdated_at(new Date());
        collectionIndividualAccountActionVice.setCzmc(czmc);// 操作名称(01:开户；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移)
        collectionIndividualAccountActionVice.setCzyy(reIndiAcctAction.getCZYY());// 操作原因
        //collectionIndividualAccountActionVice.setDwzh(reIndiAcctAction.getDWZH());// 单位账号
        //collectionIndividualAccountActionVice.setGrzh(reIndiAcctAction.getGRZH());// 个人账号
        collectionIndividualAccountActionVice.setBlzl(reIndiAcctAction.getBLZL());// 办理资料
        collectionIndividualAccountActionVice.setYwwd(tokenContext.getUserInfo().getYWWD());// 业务网点
        collectionIndividualAccountActionVice.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        collectionIndividualAccountActionVice.setBeiZhu(reIndiAcctAction.getBeiZhu());// 备注
        collectionIndividualAccountActionViceDAO.update(collectionIndividualAccountActionVice);// 更新副表

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
        CCollectionPersonalBusinessDetailsExtension businessDetailsExtension = collectionPersonalBusinessDetails
                .getExtension();
        businessDetailsExtension.setCzyy(reIndiAcctAction.getCZYY());// 更新操作原因
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
        businessDetailsExtension.setYwwd(network);// 更新业务网点
        businessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());// 更新操作员
        businessDetailsExtension.setBlzl(reIndiAcctAction.getBLZL());// 更新办理资料
        businessDetailsExtension.setBeizhu(reIndiAcctAction.getBeiZhu());//更新备注
        businessDetailsExtension.setJbrxm(reIndiAcctAction.getJBRXM()); //经办人姓名
        businessDetailsExtension.setJbrzjhm(reIndiAcctAction.getJBRZJHM()); //经办人证件号码
        businessDetailsExtension.setJbrzjlx(reIndiAcctAction.getJBRZJLX()); //经办人类型

        // ------->swagger没有传
        if (!Arrays.asList("0", "1").contains(reIndiAcctAction.getCZLX())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型不匹配");
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(reIndiAcctAction.getCZLX()), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        collectionPersonalBusinessDetails.getExtension().getCzy(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), 归集_冻结个人账户.getSubType(), BusinessType.Collection, collectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
                new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!StringUtil.notEmpty(next) || e != null) {
                            return;
                        }
                        if (succeed) {
                            businessDetailsExtension.setStep(next);

                            collectionPersonalBusinessDetails.setExtension(businessDetailsExtension);

                            if (StringUtil.isIntoReview(next, null)) businessDetailsExtension.setDdsj(new Date());

                            instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName())) doAcctAction(tokenContext, UID);
                        }
                    }
                });
        if (reIndiAcctAction.getCZLX().equals("1"))
            saveAuditHistory.saveNormalBusiness(UID, tokenContext, CollectionBusinessType.冻结.getName(), "修改");
        return new ReIndiAcctActionRes() {{
            this.setStatus("success");
            this.setYWLSH(UID);
        }};
    }

    @Override
    public GetIndiAcctActionRes showAcctAction(String UID) {
        return super.showAcctAction(UID);
    }

    @Override
    public AutoIndiAcctActionRes AutoIndiAcctAction(TokenContext tokenContext, String GRZH) {
        return super.AutoIndiAcctAction(tokenContext, GRZH);
    }

    @Override
    public CommonResponses headAcctAction(TokenContext tokenContext, String UID) {
        return super.headAcctAction(tokenContext, UID);
    }

    @Override
    public void doAcctAction(TokenContext tokenContext, String YWLSH) {
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
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

        StCollectionPersonalAccount stCollectionPersonalAccount = DAOBuilder.instance(collectionPersonalAccountDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("grzh", collectionPersonalBusinessDetails.getGrzh());
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        if (stCollectionPersonalAccount == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的个人账户");
        }
        String czmc = collectionPersonalBusinessDetails.getExtension().getCzmc();

        if (czmc.equals(CollectionBusinessType.冻结.getCode()) && (stCollectionPersonalAccount.getGrzhzt().equals(PersonAccountStatus.正常.getCode()) || stCollectionPersonalAccount.getGrzhzt().equals(PersonAccountStatus.封存.getCode()))) {// 冻结
            collectionPersonalBusinessDetails.getPerson().getExtension().setSfdj("02");//是否冻结
            collectionPersonalBusinessDetails.getExtension().setDjje(stCollectionPersonalAccount.getGrzhye());
        } else {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH);
        }
        collectionPersonalBusinessDetails.getExtension().setBjsj(new Date());//设置办结时间
        DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails)
                .save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        saveAuditHistory.saveNormalBusiness(YWLSH, CollectionBusinessType.冻结.getName(), "办结");

    }

    @Override
    public SubmitIndiAcctFreezeRes submitIndiAcctAction(TokenContext tokenContext, List<String> YWLSHJH) {
        for (String YWLSH : YWLSHJH) {
            if (YWLSH == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS);
            }
            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywlsh", YWLSH);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (collectionPersonalBusinessDetails == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应业务");
            }
            if (!tokenContext.getUserInfo().getCZY().equals(collectionPersonalBusinessDetails.getExtension().getCzy())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            }
            //办理资料验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.个人账户冻结.getCode(), collectionPersonalBusinessDetails.getExtension().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            StateMachineUtils.updateState(this.iStateMachineService, Events.通过.getEvent(), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                            tokenContext.getUserInfo().getCZY(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), 归集_冻结个人账户.getSubType(), BusinessType.Collection, tokenContext.getUserInfo().getYWWD()),
                    new StateMachineUtils.StateChangeHandler() {
                        @Override
                        public void onStateChange(boolean succeed, String next, Exception e) {

                            if (e != null) {
                                throw new ErrorException(e);
                            }

                            if (!succeed || !StringUtil.notEmpty(next) || e != null) {
                                return;
                            }

                            collectionPersonalBusinessDetails.getExtension().setStep(next);

                            if (StringUtil.isIntoReview(next, null))
                                collectionPersonalBusinessDetails.getExtension().setDdsj(new Date());

                            DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {

                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.办结.getName())) doAcctAction(tokenContext, YWLSH);
                        }
                    });
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.冻结.getName(), "修改");
        }
        return new SubmitIndiAcctFreezeRes() {{
            this.setStatus("success");
        }};

    }
    private void paramCheck(IndiAcctActionPost addIndiAcctAction) {

        if(!StringUtil.notEmpty(addIndiAcctAction.getCZYY())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作原因");
        }
        if(!StringUtil.notEmpty(addIndiAcctAction.getCZLX())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作类型");
        }
        if(!StringUtil.notEmpty(addIndiAcctAction.getCZMC())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作名称");
        }
    }
}
