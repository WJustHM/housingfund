package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctUnseal;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayback;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
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
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Business_In_Process;
import static com.handge.housingfund.database.utils.DAOBuilder.ErrorHandler;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by 向超 on 2017/8/2.
 * 个人账号解封
 * (value = "action.unseal")
 */
@Service
public class IndiAcctUnsealImpl extends IndiAcctActionImpl implements IndiAcctUnseal {

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
    private UnitPayback unitPayback;
    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryDAO;
    @Autowired
    private ICCollectionUnitPaybackViceDAO paybackViceDAO;
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";

    private static String formatNY = "yyyy-MM";

    private static String formatNYR = "yyyy-MM-dd HH:mm";

    @Override
    public PageRes<ListOperationAcctsResRes> getAcctAcionInfo(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH, String CZMC, String CZYY,String KSSJ,String JSSJ , String pageNo, String pageSize) {
        return super.getAcctAcionInfo(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY, KSSJ, JSSJ , pageNo, pageSize);
    }

    @Override
    public PageResNew<ListOperationAcctsResRes> getAcctAcionInfoNew(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH, String CZMC, String CZYY, String KSSJ, String JSSJ, String marker, String pageSize, String action) {
        return super.getAcctAcionInfoNew(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY, KSSJ, JSSJ, marker, pageSize, action);
    }

    /*
          completed 已测试  lian
          日期格式未统一
          status 应该返回 成功或者失败 不应该是200
        */
    @Override
    public AddIndiAcctActionRes addAcctAction(TokenContext tokenContext, String grzh, IndiAcctActionPost addIndiAcctAction) {

        System.out.println("启封个人账户创建");
        //办理资料验证
        if (addIndiAcctAction.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人账户启封.getCode(), addIndiAcctAction.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        //参数检查
        if (addIndiAcctAction.getCZLX().equals("1")) {
            paramCheck(addIndiAcctAction);
        }
        //启封业务验证
        unSealPostCheck(grzh, addIndiAcctAction);

        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = commonPerson.getUnit();

        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }

        String czmc = addIndiAcctAction.getCZMC();

        CCollectionIndividualAccountActionVice collectionIndividualAccountActionVice = new CCollectionIndividualAccountActionVice();
        // 设置副表信息
        // String ywlsh = Generator.generateYWLSH() + 1;
        // collectionIndividualAccountActionVice.setYwlsh(ywlsh);//业务流水号 自动生成
        collectionIndividualAccountActionVice.setGrzh(grzh);// 个人账号
        collectionIndividualAccountActionVice.setDwzh(unit.getDwzh());// 单位账号
        // collectionIndividualAccountActionVice.setStep("待审核");// 审核状态
        // --------------->等待状态机
        collectionIndividualAccountActionVice.setBlzl(addIndiAcctAction.getBLZL());// 办理资料--------->swagger
        // 没传
        collectionIndividualAccountActionVice.setSxny(DateUtil.str2str(addIndiAcctAction.getSXNY(), -1));// 生效年月
        collectionIndividualAccountActionVice.setCzyy(addIndiAcctAction.getCZYY());// 操作原因
        collectionIndividualAccountActionVice.setBeiZhu(addIndiAcctAction.getBeiZhu());// 备注
        collectionIndividualAccountActionVice.setSlsj(new Date());// 受理时间-------------等待审核阶段传入
        collectionIndividualAccountActionVice.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        collectionIndividualAccountActionVice.setYwwd(tokenContext.getUserInfo().getYWWD());// 业务网点
//        collectionIndividualAccountActionVice.setDjje(commonPerson.getCollectionPersonalAccount().getGrzhye());// 冻结金额
        collectionIndividualAccountActionVice.setCreated_at(new Date());// 受理时间
        collectionIndividualAccountActionVice.setCzmc(addIndiAcctAction.getCZMC());// 操作名称
        // 设置业务拓展表信息
        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
        collectionPersonalBusinessDetailsExtension.setCzmc(czmc);// 操作名称
//        collectionPersonalBusinessDetailsExtension.setDjje(commonPerson.getCollectionPersonalAccount().getGrzhye());// 冻结金额
        // 数据库字段为ZHJX
        // 不一致
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
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        collectionPersonalBusinessDetailsExtension.setJbrxm(unit.getJbrxm());
        collectionPersonalBusinessDetailsExtension.setJbrzjhm(unit.getJbrzjhm());
        collectionPersonalBusinessDetailsExtension.setJbrzjlx(unit.getJbrzjlx());
        collectionPersonalBusinessDetailsExtension.setBeizhu(addIndiAcctAction.getBeiZhu());
        collectionPersonalBusinessDetailsExtension.setBlzl(addIndiAcctAction.getBLZL());//办理资料

        //存储角色信息，用于可能产生的补缴 2017-10-30 add 杨凡
        collectionPersonalBusinessDetailsExtension.setBlr(ComUtils.roleListToString(tokenContext));

        if (!Arrays.asList("0", "1").contains(addIndiAcctAction.getCZLX()))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型异常");
        // 设置业务信息拓展表表信息
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();
        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
        // collectionPersonalBusinessDetails.setYwlsh(ywlsh);
        collectionPersonalBusinessDetails.setGrzh(grzh);
        collectionPersonalBusinessDetails.setGjhtqywlx(czmc);
        collectionPersonalBusinessDetails.setPerson(commonPerson);
        collectionPersonalBusinessDetails.setUnit(unit);
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
                        stCollectionPersonalBusinessDetails.getExtension().getCzy(), stCollectionPersonalBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_个人账户启封.getSubType(), BusinessType.Collection, stCollectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
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

                            stCollectionPersonalBusinessDetails.setExtension(cectionPersonalBusinessDetailsExtension);

                            if (StringUtil.isIntoReview(next, null))
                                collectionPersonalBusinessDetails.getExtension().setDdsj(new Date());

                            instance(collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new ErrorHandler() {
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
        saveAuditHistory.saveNormalBusiness(stCollectionPersonalBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.启封.getName(), "新建");
        return new AddIndiAcctActionRes() {{
            this.setStatus("success");
            this.setYWLSH(stCollectionPersonalBusinessDetails.getYwlsh());
        }};
    }

    private void unSealPostCheck(String grzh, IndiAcctActionPost addIndiAcctAction) {

        // 杨凡 2017-09-04 封存增加补缴相关验证
        //增加验证，补缴相关,看该月是否会产生补缴，若产生补缴看是否是第一次产生
        Date sxny = ComUtils.parseToDate(addIndiAcctAction.getSXNY(), "yyyy-MM");
        //flag 为true，表示只产生补缴数据，不改变个人账户状态
        //boolean flag = checkAddIsPayBack(grzh, sxny);
        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);
        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人账号不存在");
        }
        if (!commonPerson.getCollectionPersonalAccount().getGrzhzt().equals(PersonAccountStatus.封存.getCode())) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前账户状态不为封存，不能办理启封业务");
        }

        final StCommonUnit unit = commonPerson.getUnit();

        if(commonPerson.getUnit().getCollectionUnitAccount().getDwzhzt().equals(UnitAccountStatus.封存.getCode())){
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "当前单位账号状态为封存");
        }

        String czmc = addIndiAcctAction.getCZMC();
        if (!czmc.equals(CollectionBusinessType.启封.getCode())) {
            throw new ErrorException(ReturnEnumeration.Business_Type_NOT_MATCH);
        }
        String sxnyStr = ComUtils.parseToString(sxny, "yyyyMM");
        ComMessage comMessage = sealCheck(grzh, sxnyStr);
        if ("05".equals(comMessage.getCode())) {
            throw new ErrorException(comMessage.getMessage());
        }
        String dwzh = commonPerson.getUnit().getDwzh();
        //检查单位是否正在汇缴中,存在则抛出运行时异常
        BusUtils.checkRemittanceDoing(dwzh);

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetailsQFData = instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("individualAccountActionVice.grzh", grzh);
                this.put("individualAccountActionVice.czmc", CollectionBusinessType.启封.getCode());
                this.put("individualAccountActionVice.dwzh", unit.getDwzh());
                this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("individualAccountActionVice.deleted", false);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetailsQFData != null) {
            throw new ErrorException(Business_In_Process);
        }

        //生效月份小于应汇缴年月时，将发生错缴，验证错缴是否合法
        String dwyhjny = BusUtils.getDWYHJNY(dwzh);

        //生效月份不能小于个人的缴至年月
        String grjznyStr = commonPerson.getExtension().getGrjzny();
        if(!ComUtils.isEmpty(grjznyStr)){
            Date grjzny = ComUtils.parseToDate(grjznyStr, "yyyyMM");
            if(sxny.compareTo(grjzny)<=0){
                throw new ErrorException("启封的生效年月，应大于个人的缴至年月："+grjznyStr);
            }
        }

        /*Date yhjny = ComUtils.parseToDate(dwyhjny, "yyyy-MM");
        Date start = sxny;
        while (start.compareTo(yhjny) < 0) {
            String createMonthStr =  ComUtils.parseToString(start,"yyyyMM");
            //查看该人该月是否存在清册，清册上无该人数据或清册上个人账户状态为封存，则不产生错缴数据
            //1、验证：该人该月是否已汇缴
            boolean notExistInventory = !(inventoryDAO.isExistInventory(grzh, createMonthStr));
            AssertUtils.isTrue(notExistInventory,"当前职工在"+createMonthStr+"已有汇缴记录，不能产生该月的补缴！");

            //2、验证：该人该月是否已经产生补缴
            boolean notExistPayBack = !(paybackViceDAO.isExistPayBack(grzh, createMonthStr));
            AssertUtils.isTrue(notExistPayBack,"当前职工在"+createMonthStr+"已存在补缴记录，不能再次产生补缴！");

            start = ComUtils.getNextMonth(start);
        }*/
    }

    private void paramCheck(IndiAcctActionPost addIndiAcctAction) {

        if(!StringUtil.notEmpty(addIndiAcctAction.getCZYY())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作原因");
        }
        if(!StringUtil.notEmpty(addIndiAcctAction.getSXNY())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"生效年月");
        }
        if(!StringUtil.notEmpty(addIndiAcctAction.getCZLX())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作类型");
        }
        if(!StringUtil.notEmpty(addIndiAcctAction.getCZMC())){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"操作名称");
        }
    }

    /*
      completed 已测试  lian
      前端传的操作名称跟数据库的操作名称不一致 不能直接赋值
      状态机异常捕获
    */
    @Override
    public ReIndiAcctActionRes reAcctAction(TokenContext tokenContext, String UID, IndiAcctActionPut reIndiAcctAction) {
        //办理资料验证
        if (reIndiAcctAction.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人账户启封.getCode(), reIndiAcctAction.getBLZL())) {
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
        if (!czmc.equals(CollectionBusinessType.启封.getCode())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作名称不匹配");
        }
        collectionIndividualAccountActionVice.setUpdated_at(new Date());
        collectionIndividualAccountActionVice.setCzmc(reIndiAcctAction.getCZMC());// 操作名称(01:开户；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移)
        collectionIndividualAccountActionVice.setCzyy(reIndiAcctAction.getCZYY());// 操作原因
        //collectionIndividualAccountActionVice.setDwzh(reIndiAcctAction.getDWZH());// 单位账号
        //collectionIndividualAccountActionVice.setGrzh(reIndiAcctAction.getGRZH());// 个人账号
        collectionIndividualAccountActionVice.setBlzl(reIndiAcctAction.getBLZL());// 办理资料
        collectionIndividualAccountActionVice.setYwwd(tokenContext.getUserInfo().getYWWD());// 业务网点
        collectionIndividualAccountActionVice.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        collectionIndividualAccountActionVice.setSxny(ComUtils.parseToYYYYMM(reIndiAcctAction.getSXNY()));

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
        // ------->swagger没有传
        if (!Arrays.asList("0", "1").contains(reIndiAcctAction.getCZLX())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型不匹配");
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(reIndiAcctAction.getCZLX()), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        collectionPersonalBusinessDetails.getExtension().getCzy(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_个人账户启封.getSubType(), BusinessType.Collection, collectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
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

                            if (StringUtil.isIntoReview(next, null))
                                collectionPersonalBusinessDetails.getExtension().setDdsj(new Date());

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
        if (reIndiAcctAction.getCZLX().equals("1")){
            if(StringUtil.isEmpty(reIndiAcctAction.getCZYY())) throw new ErrorException(ReturnEnumeration.Data_MISS,"启封原因");
            saveAuditHistory.saveNormalBusiness(UID, tokenContext, CollectionBusinessType.启封.getName(), "修改");
        }
        return new ReIndiAcctActionRes() {{
            this.setStatus("success");
            this.setYWLSH(UID);
        }};
    }

    /*
      completed 已测试  lian
      办理资料查不到
      单位信息里面多了一个业务流水号(swagger问题)
      生效年月查不到
      个人缴至年月查不到
      备注查不到
    */
    @Override
    public GetIndiAcctActionRes showAcctAction(String UID) {
        return super.showAcctAction(UID);
    }

    /*
      completed 已测试  lian
      操作人员查不到
      日期格式未统一
    */
    @Override
    public AutoIndiAcctActionRes AutoIndiAcctAction(TokenContext tokenContext, String GRZH) {
        return super.AutoIndiAcctAction(tokenContext, GRZH);
    }

    /*
      completed 已测试  lian
      日期格式不统一
      备注查不到
      月缴存额查不到
      操作名称查不到
      生效年月查不到
      个人缴至年月查不到
    */
    @Override
    public CommonResponses headAcctAction(TokenContext tokenContext, String UID) {
        return super.headAcctAction(tokenContext, UID);
    }

    /**
     * 杨凡 2017-8-14 关联业务：
     * 个人账户启封办结操作,可能生成补缴数据
     */
    @Override
    public void doAcctAction(TokenContext tokenContext, String ywlsh) {
        CCollectionIndividualAccountActionVice unSeal = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        StCollectionPersonalBusinessDetails personalBusiness = unSeal.getGrywmx();
        if (personalBusiness == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号对应业务：" + ywlsh + "，对应的业务不存在！");
        }
        String grzh = personalBusiness.getGrzh();
        String sxnyStr = unSeal.getSxny();
        Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
        //获取启封信息
        ComMessage comMessage = sealCheck(grzh, sxnyStr);
        String code = comMessage.getCode();
        if ("01".equals(code)) {
            doUnSealNomal(grzh);
        }
        if ("02".equals(code)) {
            doUnSealTimeTask(ywlsh, sxnyStr);
        }
        if ("03".equals(code)) {
            doUnSealNomal(grzh);
            //checkAddIsPayBack(grzh, sxny);
            Map map = new HashMap();
            map.put("ywwd",personalBusiness.getExtension().getYwwd());
            map.put("czy",personalBusiness.getExtension().getCzy());
            map.put("bjyy","启封补缴");
            map.put("token",tokenContext);
            map.put("roleStr",personalBusiness.getExtension().getBlr());
            doCreatePayBack(ywlsh,map);
        }
        //更新办结时间
        personalBusiness.getExtension().setBjsj(new Date());//设置办结时间
        collectionPersonalBusinessDetailsDAO.save(personalBusiness);
        String dwzh = personalBusiness.getUnit().getDwzh();
        BusUtils.refreshUnitAcount(dwzh);
        saveAuditHistory.saveNormalBusiness(ywlsh, CollectionBusinessType.启封.getName(), "办结");
    }

    private void doUnSealNomal(String grzh) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
        personalAccount.setGrzhzt(PersonAccountStatus.正常.getCode());    //qq启封
        commonPersonDAO.save(person);

    }

    private void doUnSealTimeTask(String ywlsh, String sxny) {
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx("04");
        task.setYwms("启封业务");
        task.setZxcs(0);
        task.setZxsj(sxny);
        timedTaskDAO.save(task);
    }

    private void doCreatePayBack(String ywlsh,Map map) {
        unitPayback.checkUnseal(ywlsh,map);
    }

    /*
      completed 已测试  lian
      正常
    */
    @Override
    public SubmitIndiAcctFreezeRes submitIndiAcctAction(TokenContext tokenContext, List<String> YWLSHJH) {
        ArrayList<Exception> exceptions = new ArrayList<>();
        for (String YWLSH : YWLSHJH) {
            if (!StringUtil.notEmpty(YWLSH)) {
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
            //办理资料验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.个人账户启封.getCode(), collectionPersonalBusinessDetails.getExtension().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            if (collectionPersonalBusinessDetails == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应业务");
            }
            if (!tokenContext.getUserInfo().getCZY().equals(collectionPersonalBusinessDetails.getExtension().getCzy())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            }
            StateMachineUtils.updateState(this.iStateMachineService, Events.通过.getEvent(), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                            tokenContext.getUserInfo().getCZY(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_个人账户启封.getSubType(), BusinessType.Collection, tokenContext.getUserInfo().getYWWD()),
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
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.启封.getName(), "修改");
            if(StringUtil.isEmpty(collectionPersonalBusinessDetails.getIndividualAccountActionVice().getCzyy())) throw new ErrorException(ReturnEnumeration.Data_MISS,"启封原因");
        }
        return new SubmitIndiAcctFreezeRes() {{
            this.setStatus("success");
        }};
    }

    /**
     * 新增时验证是否补缴，若产生补缴看是否是第一次产生
     */
    public boolean checkAddIsPayBack(String grzh, Date sxny) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        String dwzh = unit.getDwzh();
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        if (nearLyInventory != null) {
            Date qcny = ComUtils.parseToDate(nearLyInventory.getQcny(), "yyyyMM");
            if (sxny.compareTo(qcny) <= 0) {
                //将产生补缴，查看是否已产生过补缴，若已产生过就报错
                String sxnyStr = ComUtils.parseToString(sxny, "yyyyMM");
                boolean isAlreadyGenerated = paybackViceDAO.isAlreadyExistPayBack(grzh, sxnyStr);
                if (isAlreadyGenerated) {
                    throw new ErrorException("个人账号：" + grzh + "，在该月已有过补缴记录，不能再次办理！");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void doUnsealTask(String ywlsh) {
        CCollectionIndividualAccountActionVice seal = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        String grzh = seal.getGrywmx().getGrzh();
        doUnSealNomal(grzh);
        String dwzh = seal.getGrywmx().getUnit().getDwzh();
        BusUtils.refreshUnitAcount(dwzh);
    }

    @Override
    public void douUnSealDirect(String grzh, String sxny) {

        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        String dwzh = unit.getDwzh();
        CCollectionIndividualAccountActionVice seal = new CCollectionIndividualAccountActionVice();
        seal.setGrzh(grzh);
        seal.setDwzh(dwzh);
        seal.setCzmc("04");
        seal.setSxny(sxny);

        StCollectionPersonalBusinessDetails grywmx = new StCollectionPersonalBusinessDetails();
        grywmx.setGrzh(grzh);
        grywmx.setGjhtqywlx("04");
        grywmx.setPerson(person);
        grywmx.setUnit(unit);
        grywmx.setIndividualAccountActionVice(seal);

        CCollectionPersonalBusinessDetailsExtension extention = new CCollectionPersonalBusinessDetailsExtension();
        extention.setStep("办结");
        extention.setCzmc("04");    //启封
        extention.setBjsj(new Date());
        grywmx.setExtension(extention);

        seal.setGrywmx(grywmx);

        collectionIndividualAccountActionViceDAO.save(seal);
    }

}
