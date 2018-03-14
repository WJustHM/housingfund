package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.AssertUtils;
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
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSeal;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayWrong;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.handge.housingfund.database.enums.BusinessSubType.归集_个人账户封存;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by 向超 on 2017/8/2.
 * (value = "action.seal")
 */
@SuppressWarnings("Duplicates")
@Service
public class IndiAcctSealImpl extends IndiAcctActionImpl implements IndiAcctSeal {

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
    private UnitPayWrong unitPayWrong;
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;
    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryDAO;
    @Autowired
    private ICCollectionUnitPayWrongViceDAO payWrongDAO;
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
        return super.getAcctAcionInfo(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,  KSSJ, JSSJ ,pageNo, pageSize);
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

        System.out.println("封存个人账户创建");

        //参数检查
        checkParam(grzh, addIndiAcctAction);
        //业务验证
        sealPostCheck(grzh, addIndiAcctAction);

        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);

        String czmc = addIndiAcctAction.getCZMC();

        CCollectionPersonalBusinessDetailsExtension extension = new CCollectionPersonalBusinessDetailsExtension();
        CCollectionIndividualAccountActionVice sealVice = new CCollectionIndividualAccountActionVice();
        //在途验证
        final StCommonUnit unit = commonPerson.getUnit();

        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }

        // 设置副表信息
        sealVice.setGrzh(grzh);// 个人账号
        sealVice.setDwzh(unit.getDwzh());// 单位账号
        sealVice.setBlzl(addIndiAcctAction.getBLZL());// 办理资料--------->swagger
        // 没传
        sealVice.setSxny(DateUtil.str2str(addIndiAcctAction.getSXNY(), -1));//生效年月
        sealVice.setCzyy(addIndiAcctAction.getCZYY());// 操作原因
        sealVice.setBeiZhu(addIndiAcctAction.getBeiZhu());// 备注
        sealVice.setSlsj(new Date());// 受理时间-------------等待审核阶段传入
        sealVice.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        sealVice.setYwwd(tokenContext.getUserInfo().getYWWD());// 业务网点
        sealVice.setCreated_at(new Date());// 受理时间
        sealVice.setCzmc(addIndiAcctAction.getCZMC());// 操作名称

        // 设置业务拓展表信息
        extension.setCzmc(czmc);// 操作名称
        extension.setSlsj(new Date());
        // 不一致
        extension.setCzy(tokenContext.getUserInfo().getCZY());// 操作员
        extension.setCzyy(addIndiAcctAction.getCZYY());// 操作原因
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());

        extension.setYwwd(network);// 业务网点
        extension.setBeizhu(addIndiAcctAction.getBeiZhu());//备注
        extension.setJbrxm(unit.getJbrxm()); //经办人姓名
        extension.setJbrzjhm(unit.getJbrzjhm()); //经办人证件号码
        extension.setJbrzjlx(unit.getJbrzjlx()); //经办人类型
        extension.setBlzl(addIndiAcctAction.getBLZL());//办理资料
        extension.setStep(CollectionBusinessStatus.初始状态.getName());
        if (!Arrays.asList("0", "1").contains(addIndiAcctAction.getCZLX()))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型异常");

        // 设置业务信息拓展表表信息
        StCollectionPersonalBusinessDetails grywmx = new StCollectionPersonalBusinessDetails();
        grywmx.setExtension(extension);
        grywmx.setGrzh(grzh);
        grywmx.setGjhtqywlx(czmc);
        grywmx.setPerson(commonPerson);
        grywmx.setUnit(unit);
        grywmx.setCreated_at(new Date());
        grywmx.setIndividualAccountActionVice(sealVice);
        sealVice.setGrywmx(grywmx);

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = instance(collectionPersonalBusinessDetailsDAO).entity(grywmx).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.通过.getEvent());

                    this.put("1", Events.提交.getEvent());

                }}.get(addIndiAcctAction.getCZLX()), new TaskEntity(stCollectionPersonalBusinessDetails.getYwlsh(), stCollectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        stCollectionPersonalBusinessDetails.getExtension().getCzy(), stCollectionPersonalBusinessDetails.getExtension().getBeizhu(), BusinessSubType.归集_个人账户封存.getSubType(), BusinessType.Collection, stCollectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
                new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                            throw new ErrorException(e);
                        }
                        if (!StringUtil.notEmpty(next) || e != null) {
                            return;
                        }
                        if (succeed) {
                            CCollectionPersonalBusinessDetailsExtension cectionPersonalBusinessDetailsExtension = stCollectionPersonalBusinessDetails.getExtension();
                            cectionPersonalBusinessDetailsExtension.setStep(next);

                            stCollectionPersonalBusinessDetails.setExtension(cectionPersonalBusinessDetailsExtension);

                            if (StringUtil.isIntoReview(next, null)) grywmx.getExtension().setDdsj(new Date());

                            instance(collectionPersonalBusinessDetailsDAO).entity(grywmx).save(new DAOBuilder.ErrorHandler() {
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
        if (addIndiAcctAction.getCZLX().equals("1")) {
            checkParams(grywmx);
        }
        saveAuditHistory.saveNormalBusiness(stCollectionPersonalBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.封存.getName(), "新建");
        return new AddIndiAcctActionRes() {{
            this.setStatus("success");
            this.setYWLSH(stCollectionPersonalBusinessDetails.getYwlsh());
        }};
    }

    /**
     * 封存业务办理验证
     * 1、
     * 2、
     */
    private void sealPostCheck(String grzh, IndiAcctActionPost addIndiAcctAction) {

        //1、验证个人账号
        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);
        AssertUtils.notEmpty(commonPerson, "个人账号不存在");

        //2、验证个人账户状态
        String grzhzt = commonPerson.getCollectionPersonalAccount().getGrzhzt();
        AssertUtils.isTrue(PersonAccountStatus.正常.getCode().equals(grzhzt), "当前办理的职工个人账户状态应为正常！");

        //3、封存业务，操作名称验证
        String czmc = addIndiAcctAction.getCZMC();
        AssertUtils.isTrue(czmc.equals(CollectionBusinessType.封存.getCode()), "操作名称不匹配，当前办理封存业务");

        //4、在途验证
        StCollectionPersonalBusinessDetails sealDoing = getSealDoing(grzh);
        AssertUtils.isEmpty(sealDoing, "已正在办理封存业务！");

        //5、验证是否存在正在缴存的业务
        String dwzh = commonPerson.getUnit().getDwzh();
        BusUtils.checkRemittanceDoing(dwzh);

        //6、办理资料验证
        if (addIndiAcctAction.getCZLX().equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人账户封存.getCode(), addIndiAcctAction.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料缺失");
        }

        //7、关联业务验证,主要是错缴,对比单位的应汇缴年月
        String sxny = addIndiAcctAction.getSXNY();
        relationBusCheck(grzh, sxny);

    }

    private void relationBusCheck(String grzh, String sxnystr) {
        StCommonPerson commonPerson = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = commonPerson.getUnit();
        String dwzh = unit.getDwzh();
        String dwyhjny = BusUtils.getDWYHJNY(dwzh);
        Date yhjny = ComUtils.parseToDate(dwyhjny, "yyyy-MM");
        Date sxny = ComUtils.parseToDate(sxnystr, "yyyy-MM");

        //验证生效年月是否大于等于单位的首次汇缴年月
        String dwschjnyStr = unit.getExtension().getDwschjny();
        Date dwschjny = ComUtils.parseToDate(dwschjnyStr, "yyyyMM");
        boolean b = dwschjny.compareTo(sxny) <= 0;
        AssertUtils.isTrue(b, "生效年月不能小于单位的首次汇缴年月！");

        //验证生效年月是否大于等于个人的首次汇缴年月
        String gjjschjnyStr = commonPerson.getExtension().getGjjschjny();
        Date grschjny = ComUtils.parseToDate(gjjschjnyStr, "yyyyMM");
        boolean b1 = grschjny.compareTo(sxny) <= 0;
        AssertUtils.isTrue(b1, "生效年月不能小于个人的首次汇缴年月！");

        //生效月份小于应汇缴年月时，将发生错缴，验证错缴是否合法
        /*Date start = sxny;
        while (start.compareTo(yhjny) < 0) {
            String createMonthStr =  ComUtils.parseToString(start,"yyyyMM");
            //查看该人该月是否存在清册，清册上无该人数据或清册上个人账户状态为封存，则不产生错缴数据
            //1、验证：该人该月是否已汇缴
            boolean isExistInventory = inventoryDAO.isExistInventory(grzh, createMonthStr);
            AssertUtils.isTrue(isExistInventory,"当前职工在"+createMonthStr+"无汇缴记录，不能产生该月的错缴！");

            //2、验证：该人该月是否已经产生过错缴
            boolean isExistPayBack = payWrongDAO.isAlreadyExistPayWrong(grzh, start);
            AssertUtils.isTrue(!isExistPayBack,"当前职工在"+createMonthStr+"已存在错缴记录,不能再次产生错缴！");

            start = ComUtils.getNextMonth(start);
        }*/

        //错缴金额验证,当前业务
        if(sxny.compareTo(yhjny) < 0){
            //提取业务在途验证
            withdrawlBusCheck(commonPerson);

            StCollectionPersonalAccount personalAccount = commonPerson.getCollectionPersonalAccount();
            BigDecimal grzhye = personalAccount.getGrzhye();
            //查询
            BigDecimal cjje = BigDecimal.ZERO;
            List<StCollectionPersonalBusinessDetails> personDeposits = collectionPersonalBusinessDetailsDAO.getPersonDeposits(grzh);
            for(StCollectionPersonalBusinessDetails grywmx : personDeposits){
                CCollectionPersonalBusinessDetailsExtension extension = grywmx.getExtension();
                String fsnyStr = extension.getFsny();
                BigDecimal fse = grywmx.getFse();
                Date fsny = ComUtils.parseToDate(fsnyStr, "yyyyMM");
                if("01".equals(extension.getCzmc())){
                    if(sxny.compareTo(fsny) <= 0){
                        cjje = cjje.add(fse);
                    }
                }

            }
            if(grzhye.compareTo(cjje) < 0){
                throw new ErrorException("封存将产生错缴,将发生的错缴金额:"+cjje+",个人账户余额不足！");
            }
        }
    }

    private void withdrawlBusCheck(StCommonPerson commonPerson) {
        boolean flag = collectionPersonalBusinessDetailsDAO.hasWithdrawl(commonPerson.getGrzh());   //true 表示职工有正在办理的提取业务
        AssertUtils.isTrue(!flag,"当前职工正在办理提取业务！");
    }

    private StCollectionPersonalBusinessDetails getSealDoing(String grzh) {
        return instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("individualAccountActionVice.grzh", grzh);
                this.put("individualAccountActionVice.czmc", CollectionBusinessType.封存.getCode());
                this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.待审核.getName(), CollectionBusinessStatus.审核不通过.getName()));
                this.put("individualAccountActionVice.deleted", false);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }

    private void checkParam(String grzh, IndiAcctActionPost addIndiAcctAction) {

        //检查参数是否为空
        if (addIndiAcctAction.getCZLX().equals("1")) {

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
            //检查参数是否合法
            try {
                new SimpleDateFormat(formatNY).parse(addIndiAcctAction.getSXNY());
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月格式不对");
            }
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
                UploadFileBusinessType.个人账户封存.getCode(), reIndiAcctAction.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料缺失");
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
        if (!czmc.equals(CollectionBusinessType.封存.getCode())) {
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
        businessDetailsExtension.setJbrzjhm(reIndiAcctAction.getJBRZJHM()); //经办人证件号码
        businessDetailsExtension.setJbrzjlx(reIndiAcctAction.getJBRZJLX()); //经办人类型
        businessDetailsExtension.setBeizhu(reIndiAcctAction.getBeiZhu());//更新备注
        // ------->swagger没有传


        if (!Arrays.asList("0", "1").contains(reIndiAcctAction.getCZLX())) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型不匹配");
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(reIndiAcctAction.getCZLX()), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        collectionPersonalBusinessDetails.getExtension().getCzy(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), 归集_个人账户封存.getSubType(), BusinessType.Collection, collectionPersonalBusinessDetails.getExtension().getYwwd().getId()),
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
        if (reIndiAcctAction.getCZLX().equals("1")) {
            checkParams(collectionPersonalBusinessDetails);
            saveAuditHistory.saveNormalBusiness(UID, tokenContext, CollectionBusinessType.封存.getName(), "修改");
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
     * 封存可能产生错缴数据，对已缴月份进行封存时，错缴生成需满足：该人该月在该单位下，该人该月该单位为其缴过款;未缴过款不产生错缴
     * 三种情况：
     * 1、对未清册的月份的修改，正常处理
     * 2、对未来月份的修改,定时任务
     * 3、对缴过款月份的修改，来产生错缴，只允许进行一次，不改变当前状态，只生产错缴数据
     */
    @Override
    public void doAcctAction(TokenContext tokenContext, String ywlsh) {
        CCollectionIndividualAccountActionVice seal = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        StCollectionPersonalBusinessDetails personalBusiness = seal.getGrywmx();
        if (personalBusiness == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务流水号对应业务：" + ywlsh + "，对应的业务不存在！");
        }
        String grzh = personalBusiness.getGrzh();
        String sxnyStr = seal.getSxny();
        Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
        //获取封存信息
        ComMessage comMessage = sealCheck(grzh, sxnyStr);
        String code = comMessage.getCode();
        if ("01".equals(code)) {
            doSealNomal(grzh);
        }
        if ("02".equals(code)) {
            doSealTimeTask(ywlsh, sxnyStr);
        }
        if ("03".equals(code)) {
            //checkAddIsPayWrong(grzh, sxny);
            String newjzny = ComUtils.getPreMonth(sxnyStr,"yyyyMM");
            doSealNomal(grzh,newjzny);
            doCreatePayWrong(tokenContext,ywlsh);
        }
        //更新办结时间
        personalBusiness.getExtension().setBjsj(new Date());//设置办结时间
        collectionPersonalBusinessDetailsDAO.save(personalBusiness);
        String dwzh = personalBusiness.getUnit().getDwzh();
        BusUtils.refreshUnitAcount(dwzh);
        saveAuditHistory.saveNormalBusiness(ywlsh, CollectionBusinessType.封存.getName(), "办结");
    }

    /**
     * 产生错缴
     */
    private void doCreatePayWrong(TokenContext tokenContext, String ywlsh) {
        unitPayWrong.checkSealCreateWrong(tokenContext,ywlsh);
    }

    /**
     * 产生定时
     */
    private void doSealTimeTask(String ywlsh, String sxny) {
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx("05");
        task.setYwms("封存业务");
        task.setZxcs(0);
        task.setZxsj(sxny);
        timedTaskDAO.save(task);
    }

    /**
     * 正常封存，更新数据
     */
    private void doSealNomal(String grzh) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
        personalAccount.setGrzhzt(PersonAccountStatus.封存.getCode());    //封存
        commonPersonDAO.save(person);
    }

    private void doSealNomal(String grzh,String jzny) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        CCommonPersonExtension extension = person.getExtension();
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
        personalAccount.setGrzhzt(PersonAccountStatus.封存.getCode());    //封存
        extension.setGrjzny(jzny);
        commonPersonDAO.save(person);
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
            if (collectionPersonalBusinessDetails == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应业务");
            }
            if (!tokenContext.getUserInfo().getCZY().equals(collectionPersonalBusinessDetails.getExtension().getCzy())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            }
            checkParams(collectionPersonalBusinessDetails);
            //办理资料验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.个人账户封存.getCode(), collectionPersonalBusinessDetails.getExtension().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料缺失");
            }
            StateMachineUtils.updateState(this.iStateMachineService, Events.通过.getEvent(), new TaskEntity(collectionPersonalBusinessDetails.getYwlsh(), collectionPersonalBusinessDetails.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                            tokenContext.getUserInfo().getCZY(), collectionPersonalBusinessDetails.getExtension().getBeizhu(), 归集_个人账户封存.getSubType(), BusinessType.Collection, tokenContext.getUserInfo().getYWWD()),
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
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.封存.getName(), "修改");
        }
        return new SubmitIndiAcctFreezeRes() {{
            this.setStatus("success");
        }};

    }


    public boolean checkAddIsPayWrong(String grzh, Date sxny) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        String dwzh = unit.getDwzh();
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        if (nearLyInventory != null) {
            Date qcny = ComUtils.parseToDate(nearLyInventory.getQcny(), "yyyyMM");
            if (sxny.compareTo(qcny) <= 0) {
                //将产生错缴，查看是否已产生过错缴，若已产生过就报错
                boolean isAlreadyGenerated = payWrongDAO.isAlreadyExistPayWrong(grzh, sxny);
                if (isAlreadyGenerated) {
                    throw new ErrorException("个人账号：" + grzh + "，在该月已有过错缴记录，不能再次办理！");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 定时任务执行
     * 封存业务，更改账户状态
     */
    @Override
    public void doSealTask(String ywlsh) {
        CCollectionIndividualAccountActionVice seal = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        String grzh = seal.getGrywmx().getGrzh();
        doSealNomal(grzh);
        String dwzh = seal.getGrywmx().getUnit().getDwzh();
        BusUtils.refreshUnitAcount(dwzh);
    }

    @Override
    public void doSealDirect(String grzh, String sxny) {

        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        String dwzh = unit.getDwzh();
        CCollectionIndividualAccountActionVice seal = new CCollectionIndividualAccountActionVice();
        seal.setGrzh(grzh);
        seal.setCzmc(CollectionBusinessType.封存.getCode());
        seal.setSxny(sxny);
        seal.setDwzh(dwzh);

        StCollectionPersonalBusinessDetails grywmx = new StCollectionPersonalBusinessDetails();
        grywmx.setGrzh(grzh);
        grywmx.setGjhtqywlx(CollectionBusinessType.封存.getCode());
        grywmx.setPerson(person);
        grywmx.setUnit(unit);
        grywmx.setIndividualAccountActionVice(seal);

        CCollectionPersonalBusinessDetailsExtension extention = new CCollectionPersonalBusinessDetailsExtension();
        extention.setStep(CollectionBusinessStatus.办结.getName());
        extention.setCzmc(CollectionBusinessType.封存.getCode());    //封存
        extention.setBjsj(new Date());
        grywmx.setExtension(extention);

        seal.setGrywmx(grywmx);

        collectionIndividualAccountActionViceDAO.save(seal);
    }

    @Override
    public ComMessage getPersonSealMsgForTQ(String grzh) {
        Object[] obj = commonPersonDAO.getPersonSealMsgForTQ(grzh);
        ComMessage msg = new ComMessage();
        if(obj == null){
            return new ComMessage("02","");
        }
        String grjzny = (String)obj[1];
        String gjjschjny = (String)obj[2];
        String sxny = (String)obj[3];

        Date gryhjny = getGRYHJNY(grjzny,gjjschjny);
        Date sxnyDate = ComUtils.parseToDate(sxny,"yyyyMM");
        if(sxnyDate.compareTo(gryhjny) > 0){
            Date lastMonth = ComUtils.getLastMonth(sxnyDate);
            String lastStr = ComUtils.parseToString(lastMonth,"yyyy年MM月");
            msg.setCode("01");
            msg.setMessage("当前职工公积金未缴至封存年月");
        }else{
            msg.setCode("02");
            msg.setMessage("");
        }
        return msg;
    }

    private Date getGRYHJNY(String grjzny, String gjjschjny) {
        if(!ComUtils.isEmpty(grjzny)){
            return ComUtils.getNextMonth(ComUtils.parseToDate(grjzny,"yyyyMM"));
        }else{
            return ComUtils.parseToDate(gjjschjny,"yyyyMM");
        }
    }

}
