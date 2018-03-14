package com.handge.housingfund.collection.service.unitdeposit;

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
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositRatio;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Funnyboy on 2017/7/25.
 */

@Service
public class UnitDepositRatioImpl implements UnitDepositRatio {

    //单位信息
    @Autowired
    private IStCommonUnitDAO common_unit;
    //单位业务明细
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collection_unit_business_details;
    //单位账号
    @Autowired
    private IStCollectionUnitAccountDAO collection_unit_account_dao;
    @Autowired
    private ICCollectionUnitDepositRatioViceDAO depositRatioDao;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private com.handge.housingfund.statemachineV2.IStateMachineService iStateMachineService;
    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO collectionUnitDepositInventoryViceDAO;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    IUploadImagesService iUploadImagesService;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private IStCommonPolicyDAO istCommonPolicyDAO;

    //时间格式转换
    private final SimpleDateFormat simpleall = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat simpleM = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat simpleData = new SimpleDateFormat("yyyyMM");

    public UnitDepositRatioImpl() {
    }

    /**
     * 列表信息
     */
    @Override
    public final PageRes<ListUnitDepositRatioResRes> getDepositRatioInfo(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String pageNumber, String pageSize) {
        //格式检查以及条件查询

        HashMap<String, Object> search_map = new HashMap<>();
        search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        if (StringUtil.notEmpty(ZhuangTai)) {
            if (ZhuangTai.equals(CollectionBusinessStatus.新建.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.新建.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待审核.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.审核不通过.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.审核不通过.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.办结.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.待入账.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待入账.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.已入账分摊.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.已入账分摊.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.已入账.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.已入账.getName());
            }
        }
        PageResults<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsPage = null;
        try {
            stCollectionUnitBusinessDetailsPage = collection_unit_business_details.listWithPage(search_map,
                    !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ)
                    , !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ), "created_at", Order.DESC, null, null,
                    Integer.parseInt(pageNumber), Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            // criteria.createAlias("unit", "unit");
                            if (StringUtil.notEmpty(DWMC)) {
                                criteria.add(Restrictions.like(CriteriaUtils.addAlias(criteria, "unit.dwmc"), "%" + DWMC + "%"));
                            }
                            if (StringUtil.notEmpty(DWZH)) {
                                criteria.add(Restrictions.like(CriteriaUtils.addAlias(criteria, "dwzh"), "%" + DWZH + "%"));
                            }
                        }
                    });
        } catch (NumberFormatException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = stCollectionUnitBusinessDetailsPage.getResults();
        PageRes<ListUnitDepositRatioResRes> pageres = new PageRes<>();
        ArrayList<ListUnitDepositRatioResRes> list = new ArrayList<>();
        ListUnitDepositRatioResRes listUnitDepositRatioResRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            listUnitDepositRatioResRes = new ListUnitDepositRatioResRes();
            //完整
            listUnitDepositRatioResRes.setYWLSH(collectionUnitBusinessDetails.getYwlsh());//业务流水号
            listUnitDepositRatioResRes.setDWZH(collectionUnitBusinessDetails.getDwzh());//单位账号
            StCommonUnit stCommonUnit = common_unit.getUnit(collectionUnitBusinessDetails.getDwzh());
            if (stCommonUnit != null) {
                listUnitDepositRatioResRes.setDWJCBL(stCommonUnit.getCollectionUnitAccount().getDwjcbl() != null ? stCommonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal(100)).toString() : null);
                listUnitDepositRatioResRes.setGRJCBL(stCommonUnit.getCollectionUnitAccount().getGrjcbl() != null ? stCommonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal(100)).toString() : null);
            }
            try {
                listUnitDepositRatioResRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称
                listUnitDepositRatioResRes.setJZNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())) + "");//缴至年月
            } catch (Exception e) {
                if (stCommonUnit != null) {
                    listUnitDepositRatioResRes.setDWMC(stCommonUnit.getDwmc());//单位名称
                    try {
                        listUnitDepositRatioResRes.setJZNY(stCommonUnit.getCollectionUnitAccount().getJzny());//缴至年月
                    } catch (Exception ex) {
                    }
                }
            }
            try {
                listUnitDepositRatioResRes.setYWZT(collectionUnitBusinessDetails.getExtension().getStep());//业务状态
                listUnitDepositRatioResRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
                listUnitDepositRatioResRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//业务网点
                listUnitDepositRatioResRes.setSLSJ(simpleall.format(collectionUnitBusinessDetails.getExtension().getSlsj()));//受理时间
            } catch (Exception e) {
            }
            list.add(listUnitDepositRatioResRes);

        }
        pageres.setResults(list);
        pageres.setCurrentPage(stCollectionUnitBusinessDetailsPage.getCurrentPage());
        pageres.setNextPageNo(stCollectionUnitBusinessDetailsPage.getPageNo());
        pageres.setPageSize(stCollectionUnitBusinessDetailsPage.getPageSize());
        pageres.setTotalCount(stCollectionUnitBusinessDetailsPage.getTotalCount());
        pageres.setPageCount(stCollectionUnitBusinessDetailsPage.getPageCount());

        return pageres;
    }

    //政策信息
    public final BigDecimal ZCZDBL(BigDecimal grjcjs, BigDecimal grjcbl, BigDecimal dwjcbl, BigDecimal jcsx, BigDecimal jcxx) {
        BigDecimal yjcjs = grjcjs.multiply(grjcbl.divide(new BigDecimal(100))).add(grjcjs.multiply(dwjcbl.divide(new BigDecimal(100))));
        BigDecimal zdbl = null;
        if (yjcjs.compareTo(BigDecimal.ZERO) > 0) {
            if (yjcjs.compareTo(jcsx) > 0) {
                zdbl = jcsx.divide(grjcjs, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
        }
        return zdbl;
    }

    public final BigDecimal ZCZXBL(BigDecimal grjcjs, BigDecimal grjcbl, BigDecimal dwjcbl, BigDecimal jcsx, BigDecimal jcxx) {
        BigDecimal yjcjs = grjcjs.multiply(grjcbl.divide(new BigDecimal(100))).add(grjcjs.multiply(dwjcbl.divide(new BigDecimal(100))));
        BigDecimal zxbl = null;
        if (yjcjs.compareTo(BigDecimal.ZERO) > 0) {
            if (yjcjs.compareTo(jcxx) < 0) {
                zxbl = jcxx.divide(grjcjs, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
        }
        return zxbl;
    }

    /**
     * 添加缴存比例调整业务
     * 1. 入单位业务明细表和拓展表0保存1提交
     * 2. 入业务副表
     */
    @Override
    public final CommonResponses addDepositRatio(TokenContext tokenContext, PostListUnitDepositRatioPost body) {
        if ("1".equals(body.getCZLX())) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.单位比例调整.getCode(), body.getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
            }
        }
        //数据验证
        ObjectAttributeCheck.checkDataType(new HashMap<String, String>() {{
            this.put("调整后单位缴存比例", body.getTZHDWJCBL());
            this.put("调整后个人缴存比例", body.getTZHGRJCBL());
        }});

        StCommonUnit secommunit = common_unit.getUnit(body.getDWZH());
        if (secommunit == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "账号不存在 ");
        if (secommunit.getCollectionUnitAccount() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息关联单位账号信息为空,请查看单位信息详情");
        if (UnitAccountStatus.销户.getCode().equals(secommunit.getCollectionUnitAccount().getDwzhzt()))
            throw new ErrorException("此账号处于销户状态");
        if (!tokenContext.getUserInfo().getYWWD().equals(secommunit.getExtension().getKhwd()))
            throw new ErrorException("该业务不在当前网点受理范围内");

        if (StringUtil.isEmpty(body.getSXNY()))
            throw new ErrorException(ReturnEnumeration.Data_MISS, "生效年月不能为空");

        List<StCollectionUnitBusinessDetails> unitBusinessDetails = collection_unit_business_details.list(new HashMap<String, Object>() {{
            this.put("dwzh", body.getDWZH());
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
        }}, null, null, null, null, null, null);

        for (StCollectionUnitBusinessDetails unitBusiness : unitBusinessDetails) {
            if (!CollectionBusinessStatus.办结.getName().equals(unitBusiness.getExtension().getStep()))
                throw new ErrorException("存在此单位比例缴存调整业务尚未办理");
        }
        if ("1".equals(body.getCZLX())) {
            StCommonPolicy dwjcblsx = istCommonPolicyDAO.get("DWJCBLSX");
            StCommonPolicy dwjcblxx = istCommonPolicyDAO.get("DWJCBLXX");
            StCommonPolicy grjcblsx = istCommonPolicyDAO.get("GRJCBLSX");
            StCommonPolicy grjcblxx = istCommonPolicyDAO.get("GRJCBLXX");
            double grsx = Double.parseDouble(grjcblsx.getGrjcblsx().toString());
            double grxx = Double.parseDouble(grjcblxx.getGrjcblxx().toString());
            double dwsx = Double.parseDouble(dwjcblsx.getDwjcblsx().toString());
            double dwxx = Double.parseDouble(dwjcblxx.getDwjcblxx().toString());
            double dw = Double.parseDouble(body.getTZHDWJCBL()) / 100;
            double gr = Double.parseDouble(body.getTZHGRJCBL()) / 100;
            if (dwxx > dw || dwsx < dw) throw new ErrorException("单位调整比例区间:" + dwxx * 100 + "% - " + dwsx * 100 + "%");
            if (grxx > gr || grsx < gr) throw new ErrorException("个人调整比例区间:" + grxx * 100 + "% - " + grsx * 100 + "%");

            try {
                String dwyhjny = BusUtils.getDWYHJNY(body.getDWZH());
                if (simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime() || simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                    if (simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
                    } else if ((simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime())) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
                    }
// else {
//                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月小于等于当前时间" + dwyhjny + " - " + simpleM.format(new Date()));
//                    }
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }

            StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
            StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
            List<StCommonPerson> listByDwzhNormalDeposit = commonPersonDAO.list(new HashMap<String, Object>() {{
                this.put("unit.dwzh", body.getDWZH());
                this.put("collectionPersonalAccount.grzhzt", PersonAccountStatus.正常.getCode());
            }}, null, null, null, null, null, null);
            BigDecimal zxbl = BigDecimal.ZERO;
            BigDecimal zdbl = BigDecimal.ZERO;
            boolean firstone = true;
            boolean firsttwo = true;
            for (StCommonPerson deposit : listByDwzhNormalDeposit) {
                if (firstone) {
                    BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zd != null) {
                        zdbl = zd;
                        firstone = false;
                    }
                } else {
                    BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zd != null && zdbl.compareTo(BigDecimal.ZERO) > 0) {
                        if (zdbl.compareTo(zd) > 0) {
                            zdbl = zd;
                        }
                    }
                }
                if (firsttwo) {
                    BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zx != null) {
                        zxbl = zx;
                        firsttwo = false;
                    }
                } else {
                    BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zx != null && zxbl.compareTo(BigDecimal.ZERO) > 0) {
                        if (zxbl.compareTo(zx) < 0) {
                            zxbl = zx;
                        }
                    }
                }
            }
            if (zxbl.compareTo(BigDecimal.ZERO) > 0 & zdbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和区间：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "% - " + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            } else if (zdbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能超过：" + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            } else if (zxbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能低于：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            }
        }
        //第一步、业务明细表和拓展表
        String id = null;
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();
        stCollectionUnitBusinessDetails.setDwzh(body.getDWZH());//单位账号
        stCollectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());
        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
        collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        collectionUnitBusinessDetailsExtension.setSlsj(new Date());//受理时间
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
        collectionUnitBusinessDetailsExtension.setJbrxm(body.getJBRXM());//经办人姓名
        collectionUnitBusinessDetailsExtension.setJbrzjhm(body.getJBRZJHM());//经办人证件号码
        collectionUnitBusinessDetailsExtension.setJbrzjlx(body.getJBRZJLX());//经办人证件类型
        collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.比例调整.getCode());


        //第二步、副表信息添加
        try {
            collectionUnitBusinessDetailsExtension.setSxny(simpleData.format(simpleM.parse(body.getSXNY()))); //待定
            CCollectionUnitDepositRatioVice collectionUnitDepositRatioVice = new CCollectionUnitDepositRatioVice();
            collectionUnitDepositRatioVice.setTzqdwbl(secommunit.getCollectionUnitAccount().getDwjcbl());//调整前单位比例
            collectionUnitDepositRatioVice.setTzhdwbl(new BigDecimal(body.getTZHDWJCBL()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后单位比例
            collectionUnitDepositRatioVice.setTzqgrbl(secommunit.getCollectionUnitAccount().getGrjcbl());//调整前个人比例
            collectionUnitDepositRatioVice.setTzhgrbl(new BigDecimal(body.getTZHGRJCBL()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后个人比例
            collectionUnitDepositRatioVice.setDwywmx(stCollectionUnitBusinessDetails);//副表的业务表关联
            stCollectionUnitBusinessDetails.setUnitDepositRatioVice(collectionUnitDepositRatioVice);//业务副表
            stCollectionUnitBusinessDetails.setUnit(secommunit);//单位信息
            stCollectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);//业务拓展表
            id = collection_unit_business_details.save(stCollectionUnitBusinessDetails);
        } catch (Exception e) {
            if (secommunit.getCollectionUnitAccount() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号信息丢失");
            throw new ErrorException(e);
        }

        //状态机
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collection_unit_business_details.get(id);
        iSaveAuditHistory.saveNormalBusiness(collectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.比例调整.getName(), "新建");
        StateMachineUtils.updateState(iStateMachineService, body.getCZLX().equals("0") ? Events.通过.getEvent() : Events.提交.getEvent(),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(collectionUnitBusinessDetails.getExtension().getStep());
                    this.setTaskId(collectionUnitBusinessDetails.getYwlsh());
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.归集_单位缴存比例调整.getSubType());
                    this.setType(BusinessType.Collection);
                    this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }
                        if (StringUtil.isIntoReview(next, null)) {
                            collectionUnitBusinessDetails.getExtension().setDdsj(new Date());
                        }
                        if (succeed) {
                            if(body.getCZLX().equals("1") && CollectionBusinessStatus.办结.getName().equals(next) ){
                                doActionDepositRatio(tokenContext,collectionUnitBusinessDetails.getYwlsh());
                                collectionUnitBusinessDetails.getExtension().setBjsj(new Date());
                            }
                            collectionUnitBusinessDetails.getExtension().setStep(next);
                            collection_unit_business_details.update(collectionUnitBusinessDetails);
                        }
                    }
                });

        return new CommonResponses() {{
            this.setId(collectionUnitBusinessDetails.getYwlsh());
            this.setState("success");
        }};
    }

    /**
     * 修改缴存比例调整业务
     */
    @Override
    public final CommonResponses reDepositRatio(TokenContext tokenContext, String YWLSH, UnitDepositRatioPut body) {
        if ("1".equals(body.getCZLX())) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                    UploadFileBusinessType.单位比例调整.getCode(), body.getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
            }
        }
        //数据验证
        ObjectAttributeCheck.checkDataType(new HashMap<String, String>() {{
            this.put("调整后单位缴存比例", body.getTZHDWJCBL());
            this.put("调整后个人缴存比例", body.getTZHGRJCBL());
        }});
        if (body.getDWZH() == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号不能为空");

        if (StringUtil.isEmpty(body.getSXNY()))
            throw new ErrorException(ReturnEnumeration.Data_MISS, "生效时间不能为空");
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                    this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
                }}, null, null, null, null, null, null);
        if (stCollectionUnitBusinessDetails.size() <= 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");

        if (!tokenContext.getUserInfo().getCZY().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getCzy()) ||
                !tokenContext.getUserInfo().getYWWD().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getYwwd().getId()))
            throw new ErrorException("操作员|业务网点不匹配");

        if (!body.getDWZH().equals(stCollectionUnitBusinessDetails.get(0).getDwzh()))
            throw new ErrorException("业务账号不能修改");
        if ("1".equals(body.getCZLX())) {
            StCommonPolicy dwjcblsx = istCommonPolicyDAO.get("DWJCBLSX");
            StCommonPolicy dwjcblxx = istCommonPolicyDAO.get("DWJCBLXX");
            StCommonPolicy grjcblsx = istCommonPolicyDAO.get("GRJCBLSX");
            StCommonPolicy grjcblxx = istCommonPolicyDAO.get("GRJCBLXX");
            double grsx = Double.parseDouble(grjcblsx.getGrjcblsx().toString());
            double grxx = Double.parseDouble(grjcblxx.getGrjcblxx().toString());
            double dwsx = Double.parseDouble(dwjcblsx.getDwjcblsx().toString());
            double dwxx = Double.parseDouble(dwjcblxx.getDwjcblxx().toString());
            double dw = Double.parseDouble(body.getTZHDWJCBL()) / 100;
            double gr = Double.parseDouble(body.getTZHGRJCBL()) / 100;
            if (dwxx > dw || dwsx < dw) throw new ErrorException("单位调整比例区间:" + dwxx * 100 + "% - " + dwsx * 100 + "%");
            if (grxx > gr || grsx < gr) throw new ErrorException("个人调整比例区间:" + grxx * 100 + "% - " + grsx * 100 + "%");

            try {
                String dwyhjny = BusUtils.getDWYHJNY(body.getDWZH());
                if (simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime() || simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                    if (simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
                    } else if ((simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime())) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
                    }
// else {
//                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月小于等于当前时间" + dwyhjny + " - " + simpleM.format(new Date()));
//                    }
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }

            StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
            StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
            List<StCommonPerson> listByDwzhNormalDeposit = commonPersonDAO.list(new HashMap<String, Object>() {{
                this.put("unit.dwzh", body.getDWZH());
                this.put("collectionPersonalAccount.grzhzt", PersonAccountStatus.正常.getCode());
            }}, null, null, null, null, null, null);
            BigDecimal zxbl = BigDecimal.ZERO;
            BigDecimal zdbl = BigDecimal.ZERO;
            boolean firstone = true;
            boolean firsttwo = true;
            for (StCommonPerson deposit : listByDwzhNormalDeposit) {
                if (firstone) {
                    BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zd != null) {
                        zdbl = zd;
                        firstone = false;
                    }
                } else {
                    BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zd != null && zdbl.compareTo(BigDecimal.ZERO) > 0) {
                        if (zdbl.compareTo(zd) > 0) {
                            zdbl = zd;
                        }
                    }
                }
                if (firsttwo) {
                    BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zx != null) {
                        zxbl = zx;
                        firsttwo = false;
                    }
                } else {
                    BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), new BigDecimal(body.getTZHGRJCBL()), new BigDecimal(body.getTZHDWJCBL()), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    if (zx != null && zxbl.compareTo(BigDecimal.ZERO) > 0) {
                        if (zxbl.compareTo(zx) < 0) {
                            zxbl = zx;
                        }
                    }
                }
            }
            if (zxbl.compareTo(BigDecimal.ZERO) > 0 & zdbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和区间：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "% - " + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            } else if (zdbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能超过：" + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            } else if (zxbl.compareTo(BigDecimal.ZERO) > 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能低于：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            }
        }
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            List<StCommonUnit> stCommonUnit = null;
            try {
                //第一步、修改业务表明细表和拓展表
                //业务表
                //拓展表
                CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();
                if (collectionUnitBusinessDetailsExtension != null) {
                    collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
                    collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()); //操作员
                    collectionUnitBusinessDetailsExtension.setSxny(simpleData.format(simpleM.parse(body.getSXNY()))); //待定
                } else {
                    collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
                    collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
                    collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
                    collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
                    collectionUnitBusinessDetailsExtension.setSlsj(new Date());//受理时间
                    collectionUnitBusinessDetailsExtension.setSxny(simpleData.format(simpleM.parse(body.getSXNY()))); //待定
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
                    collectionUnitBusinessDetailsExtension.setJbrxm(body.getJBRXM());//经办人姓名
                    collectionUnitBusinessDetailsExtension.setJbrzjhm(body.getJBRZJHM());//经办人证件号码
                    collectionUnitBusinessDetailsExtension.setJbrzjlx(body.getJBRZJLX());//经办人证件类型
                    collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.比例调整.getCode());
                }

                //第二步、副表信息
                stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
                    this.put("dwzh", body.getDWZH());
                }}, null, null, null, null, null, null);
                if (stCommonUnit.size() == 0) throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息丢失");
                if (stCommonUnit.get(0).getCollectionUnitAccount() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号信息丢失");

                CCollectionUnitDepositRatioVice collectionUnitDepositRatioVice = collectionUnitBusinessDetails.getUnitDepositRatioVice();
                if (collectionUnitDepositRatioVice != null) {
//                    collectionUnitDepositRatioVice.setTzqdwbl(stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl());//调整前单位比例
                    collectionUnitDepositRatioVice.setTzhdwbl(new BigDecimal(body.getTZHDWJCBL().toString()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后单位比例
//                    collectionUnitDepositRatioVice.setTzqgrbl(stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl());//调整前个人比例
                    collectionUnitDepositRatioVice.setTzhgrbl(new BigDecimal(body.getTZHGRJCBL().toString()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后个人比例
                } else {
                    collectionUnitDepositRatioVice = new CCollectionUnitDepositRatioVice();
                    collectionUnitDepositRatioVice.setTzqdwbl(stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl());//调整前单位比例
                    collectionUnitDepositRatioVice.setTzhdwbl(new BigDecimal(body.getTZHDWJCBL().toString()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后单位比例
                    collectionUnitDepositRatioVice.setTzqgrbl(stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl());//调整前个人比例
                    collectionUnitDepositRatioVice.setTzhgrbl(new BigDecimal(body.getTZHGRJCBL().toString()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP));//调整后个人比例
                }

                if ("1".equals(body.getCZLX())) {
                    iSaveAuditHistory.saveNormalBusiness(collectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.比例调整.getName(), "修改");
                }
                //状态机
                StateMachineUtils.updateState(iStateMachineService, body.getCZLX().equals("0") ? Events.保存.getEvent() : Events.通过.getEvent(),
                        new TaskEntity() {{
                            this.setOperator(tokenContext.getUserInfo().getCZY());
                            this.setStatus(collectionUnitBusinessDetails.getExtension().getStep());
                            this.setTaskId(collectionUnitBusinessDetails.getYwlsh());
                            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                            this.setSubtype(BusinessSubType.归集_单位缴存比例调整.getSubType());
                            this.setType(BusinessType.Collection);
                            this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                        }}, new StateMachineUtils.StateChangeHandler() {
                            @Override
                            public void onStateChange(boolean succeed, String next, Exception e) {
                                if (e != null) {
                                    throw new ErrorException(e);
                                }
                                if (!succeed || next == null) {
                                    return;
                                }
                                if (StringUtil.isIntoReview(next, null)) {
                                    collectionUnitBusinessDetails.getExtension().setDdsj(new Date());
                                }
                                if (succeed) {
                                    if(body.getCZLX().equals("1") && CollectionBusinessStatus.办结.getName().equals(next)){
                                        doActionDepositRatio(tokenContext,YWLSH);
                                        collectionUnitBusinessDetails.getExtension().setBjsj(new Date());
                                    }
                                    collectionUnitBusinessDetails.getExtension().setStep(next);
                                    collection_unit_business_details.update(collectionUnitBusinessDetails);

                                }
                            }
                        });
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        }

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    /**
     * 显示业务详情
     */
    @Override
    public final GetUnitDepositRatioRes showDepositRatio(TokenContext tokenContext, String YWLSH) {
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                    this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
                }}, null, null, null, null, null, null);
        if (stCollectionUnitBusinessDetails.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");
        }
        if (stCollectionUnitBusinessDetails.get(0).getUnit() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息丢失");

        GetUnitDepositRatioRes getUnitDepositDetailRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            getUnitDepositDetailRes = new GetUnitDepositRatioRes();
            //完整
            try {
                getUnitDepositDetailRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称
                try {
                    getUnitDepositDetailRes.setJZNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())));//缴至年月
                } catch (Exception e) {
                }
                CCollectionUnitDepositRatioVice unitDepositRatioVice = collectionUnitBusinessDetails.getUnitDepositRatioVice();
                if (unitDepositRatioVice != null) {
                    getUnitDepositDetailRes.setTZQDWJCBL(unitDepositRatioVice.getTzqdwbl().multiply(new BigDecimal(100)) + "");//调整前单位缴存比例
                    getUnitDepositDetailRes.setTZHDWJCBL(unitDepositRatioVice.getTzhdwbl().multiply(new BigDecimal(100)) + "");//调整后单位缴存比例
                    getUnitDepositDetailRes.setTZQGRJCBL(unitDepositRatioVice.getTzqgrbl().multiply(new BigDecimal(100)) + "");//调整前个人缴存比例
                    getUnitDepositDetailRes.setTZHGRJCBL(unitDepositRatioVice.getTzhgrbl().multiply(new BigDecimal(100)) + "");//调整后个人缴存比例
                }
                try {
                    getUnitDepositDetailRes.setSXNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getExtension().getSxny())));//生效年月
                } catch (Exception e) {
                }
                try {
                    getUnitDepositDetailRes.setBLZL(collectionUnitBusinessDetails.getExtension().getBlzl());//办理资料
                    getUnitDepositDetailRes.setJBRXM(collectionUnitBusinessDetails.getExtension().getJbrxm());//经办人姓名
                    getUnitDepositDetailRes.setJBRZJHM(collectionUnitBusinessDetails.getExtension().getJbrzjhm());//证件号码
                    getUnitDepositDetailRes.setJBRZJLX(collectionUnitBusinessDetails.getExtension().getJbrzjlx());//证件类型
                    getUnitDepositDetailRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
                    getUnitDepositDetailRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//业务网点
                } catch (Exception e) {
                }
                getUnitDepositDetailRes.setYWLSH(YWLSH);//业务流水号
                getUnitDepositDetailRes.setDWZH(collectionUnitBusinessDetails.getDwzh());//单位账户
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        }
        return getUnitDepositDetailRes;
    }

    /**
     * 先根据单位账号获取单位信息，配合添加业务一起使用
     */
    @Override
    public final AutoUnitDepositRatioRes autoDepositRatio(TokenContext tokenContext, String DWZH) {
        List<StCommonUnit> stCommonUnit = common_unit.list(
                new HashMap<String, Object>() {{
                    this.put("dwzh", DWZH);
                }}, null, null, null, null, null, null);
        if (stCommonUnit.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不存在");
        }
        AutoUnitDepositRatioRes autoUnitDepositRatioRes = null;
        try {
            autoUnitDepositRatioRes = new AutoUnitDepositRatioRes();
            autoUnitDepositRatioRes.setDWMC(stCommonUnit.get(0).getDwmc());//单位名称
            autoUnitDepositRatioRes.setDWZH(stCommonUnit.get(0).getDwzh());//单位账号
            autoUnitDepositRatioRes.setJBRXM(stCommonUnit.get(0).getJbrxm());//经办人姓名
            autoUnitDepositRatioRes.setJBRZJLX(stCommonUnit.get(0).getJbrzjlx());//经办人证件类型
            autoUnitDepositRatioRes.setJBRZJHM(stCommonUnit.get(0).getJbrzjhm());//经办人证件号码
            autoUnitDepositRatioRes.setTZQDWJCBL(stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal(100)) + "");//调整前单位缴存比例
            autoUnitDepositRatioRes.setTZQGRJCBL(stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal(100)) + "");//调整前个人缴存比例
            if (!ComUtils.isEmpty(stCommonUnit.get(0).getCollectionUnitAccount().getJzny())) {
                autoUnitDepositRatioRes.setJZNY(simpleM.format(simpleData.parse(stCommonUnit.get(0).getCollectionUnitAccount().getJzny())));//缴至年月
            }
        } catch (Exception e) {
            if (stCommonUnit.get(0).getCollectionUnitAccount() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息关联单位账号信息丢失");
            throw new ErrorException(e);
        }
        return autoUnitDepositRatioRes;
    }

    /**
     * 回执单业务
     */
    @Override
    public final CommonResponses headDepositRatio(TokenContext tokenContext, String YWLSH) {

        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(
                new HashMap<String, Object>() {{
                    if (StringUtil.notEmpty(YWLSH)) this.put("ywlsh", YWLSH);
                    this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
                }}, null, null, null, null, null, null);
        if (stCollectionUnitBusinessDetails.size() <= 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");
        }

        String step = null;
        try {
            step = stCollectionUnitBusinessDetails.get(0).getExtension().getStep();
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "拓展信息丢失,请修改业务数据");
        }
        if (step == null || !step.equals("办结"))
            throw new ErrorException("业务尚未办结，业务还不能打印回执单");

        HeadUnitDepositRatioReceiptRes getUnitDepositDetailRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            getUnitDepositDetailRes = new HeadUnitDepositRatioReceiptRes();
            //完整
            try {
                getUnitDepositDetailRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称
                getUnitDepositDetailRes.setYWLSH(YWLSH);//业务流水号
                getUnitDepositDetailRes.setTZQDWJCBL(collectionUnitBusinessDetails.getUnitDepositRatioVice().getTzqdwbl().multiply(new BigDecimal(100)) + "");//调整前单位缴存比例
                getUnitDepositDetailRes.setTZHDWJCBL(collectionUnitBusinessDetails.getUnitDepositRatioVice().getTzhdwbl().multiply(new BigDecimal(100)) + "");//调整后单位缴存比例
                getUnitDepositDetailRes.setTZQGRJCBL(collectionUnitBusinessDetails.getUnitDepositRatioVice().getTzqgrbl().multiply(new BigDecimal(100)) + "");//调整前个人缴存比例
                getUnitDepositDetailRes.setTZHGRJCBL(collectionUnitBusinessDetails.getUnitDepositRatioVice().getTzhgrbl().multiply(new BigDecimal(100)) + "");//调整后个人缴存比例
                getUnitDepositDetailRes.setSXNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getExtension().getSxny())));//生效年月
                getUnitDepositDetailRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
                getUnitDepositDetailRes.setJBRXM(collectionUnitBusinessDetails.getExtension().getJbrxm());//经办人姓名
                getUnitDepositDetailRes.setJBRZJHM(collectionUnitBusinessDetails.getExtension().getJbrzjhm());//证件号码
                getUnitDepositDetailRes.setJBRZJLX(collectionUnitBusinessDetails.getExtension().getJbrzjlx());//证件类型
                getUnitDepositDetailRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
                getUnitDepositDetailRes.setDWZH(collectionUnitBusinessDetails.getDwzh());//单位账号
            } catch (Exception e) {
                if (collectionUnitBusinessDetails.getUnit() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息丢失");
                if (collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号信息丢失");
                if (collectionUnitBusinessDetails.getExtension() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务拓展信息丢失");
                throw new ErrorException(e);
            }
        }
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
            getUnitDepositDetailRes.setSHR(cAuditHistory.getCzy());
        }
        String id = pdfService.getUnitDepositRatioReceiptPdf(getUnitDepositDetailRes);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 调整单位缴存比例参数（办结操作）
     */
    @Override
    public final void doActionDepositRatio(TokenContext tokenContext, String ywlsh) {
        //参数检查
        if (!StringUtil.notEmpty(ywlsh))
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "业务流水号为空");
        //业务信息记录
        CCollectionUnitDepositRatioVice depositRatio = this.depositRatioDao.getDepositRatio(ywlsh);
        if (depositRatio == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");
        }
        iSaveAuditHistory.saveNormalBusiness(ywlsh, tokenContext, CollectionBusinessType.比例调整.getName(), "办结");
        Date sxny = ComUtils.parseToDate(depositRatio.getDwywmx().getExtension().getSxny(), "yyyyMM");
        Date nowDate = ComUtils.getfirstDayOfMonth(new Date());
        if (sxny.compareTo(nowDate) <= 0) {
            doRatioNomal(ywlsh);
        } else {
            doCreateRatioTask(ywlsh, depositRatio.getSxny());
        }
        StCollectionUnitBusinessDetails dwywmx = depositRatio.getDwywmx();
        dwywmx.getExtension().setBjsj(new Date());//办结时间
        collection_unit_business_details.update(dwywmx);

    }

    private void doCreateRatioTask(String ywlsh, String sxny) {
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx("75");
        task.setYwms("比例调整业务");
        task.setZxcs(0);
        task.setZxsj(sxny);
        timedTaskDAO.save(task);
    }

    private void doRatioNomal(String ywlsh) {
        CCollectionUnitDepositRatioVice depositRatio = depositRatioDao.getDepositRatio(ywlsh);
        String dwzh = depositRatio.getDwywmx().getDwzh();
        BigDecimal dwbl = depositRatio.getTzhdwbl();
        BigDecimal grbl = depositRatio.getTzhgrbl();

        //更新单位缴存比例
        StCollectionUnitAccount unitAccount = collection_unit_account_dao.getUnitAccount(dwzh);
        unitAccount.setDwjcbl(dwbl);
        unitAccount.setGrjcbl(grbl);
        collection_unit_account_dao.update(unitAccount);

        //更新单位下每个人的缴存额
        depositRatioDao.doFinalUpdatePerson(ywlsh);

    }

    /**
     * 缴存比例调整 0 提交 1 撤回
     * 批量提交
     */
    @Override
    public final CommonResponses batchSubmit(TokenContext tokenContext, BatchSubmission body) {
        if (body != null) {
            for (String fixinfo : body.getYWLSHJH()) {
                //业务信息记录
                List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(
                        new HashMap<String, Object>() {{
                            if (StringUtil.notEmpty(fixinfo)) this.put("ywlsh", fixinfo);
                            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
                        }}, null, null, null, null, null, null);
                if (stCollectionUnitBusinessDetails.size() > 0) {
                    if (!tokenContext.getUserInfo().getCZY().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getCzy()) ||
                            !tokenContext.getUserInfo().getYWWD().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getYwwd().getId()))
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + fixinfo + ")不是由您受理的，不能提交");
                    if (stCollectionUnitBusinessDetails.get(0).getExtension() == null)
                        throw new ErrorException("业务数据拓展信息丢失：" + fixinfo);
                    if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                            UploadFileBusinessType.单位比例调整.getCode(), stCollectionUnitBusinessDetails.get(0).getExtension().getBlzl())) {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
                    }

                    StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails1 = stCollectionUnitBusinessDetails.get(0);
                    try {
                        String dwyhjny = BusUtils.getDWYHJNY(stCollectionUnitBusinessDetails1.getDwzh());
                        if (simpleData.parse(stCollectionUnitBusinessDetails1.getExtension().getSxny()).getTime() < simpleM.parse(dwyhjny).getTime() || simpleData.parse(stCollectionUnitBusinessDetails1.getExtension().getSxny()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                            if (simpleData.parse(stCollectionUnitBusinessDetails1.getExtension().getSxny()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
                            } else if ((simpleData.parse(stCollectionUnitBusinessDetails1.getExtension().getSxny()).getTime() < simpleM.parse(dwyhjny).getTime())) {
                                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
                            }
// else {
//                                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月小于等于当前时间" + dwyhjny + " - " + simpleM.format(new Date()));
//                            }
                        }
                    } catch (Exception e) {
                        throw new ErrorException(e);
                    }

                    BigDecimal tzhdwbl = stCollectionUnitBusinessDetails1.getUnitDepositRatioVice().getTzhdwbl();
                    BigDecimal tzhgrbl = stCollectionUnitBusinessDetails1.getUnitDepositRatioVice().getTzhgrbl();
                    StCommonPolicy dwjcblsx = istCommonPolicyDAO.get("DWJCBLSX");
                    StCommonPolicy dwjcblxx = istCommonPolicyDAO.get("DWJCBLXX");
                    StCommonPolicy grjcblsx = istCommonPolicyDAO.get("GRJCBLSX");
                    StCommonPolicy grjcblxx = istCommonPolicyDAO.get("GRJCBLXX");
                    double grsx = Double.parseDouble(grjcblsx.getGrjcblsx().toString());
                    double grxx = Double.parseDouble(grjcblxx.getGrjcblxx().toString());
                    double dwsx = Double.parseDouble(dwjcblsx.getDwjcblsx().toString());
                    double dwxx = Double.parseDouble(dwjcblxx.getDwjcblxx().toString());
                    double dw = Double.parseDouble(tzhdwbl.toString());
                    double gr = Double.parseDouble(tzhgrbl.toString());
                    if (dwxx > dw || dwsx < dw)
                        throw new ErrorException("单位调整比例区间:" + dwxx * 100 + "% - " + dwsx * 100 + "%");
                    if (grxx > gr || grsx < gr)
                        throw new ErrorException("个人调整比例区间:" + grxx * 100 + "% - " + grsx * 100 + "%");

                    try {
                        String dwyhjny = BusUtils.getDWYHJNY(stCollectionUnitBusinessDetails1.getDwzh());
                        if (simpleData.parse(stCollectionUnitBusinessDetails1.getExtension().getSxny()).getTime() < simpleM.parse(dwyhjny).getTime()) {
                            throw new ErrorException("生效年月要大于等于应汇缴年月：" + dwyhjny);
                        }
                    } catch (Exception e) {
                        throw new ErrorException(e);
                    }

                    StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
                    StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
                    List<StCommonPerson> listByDwzhNormalDeposit = commonPersonDAO.list(new HashMap<String, Object>() {{
                        this.put("unit.dwzh", stCollectionUnitBusinessDetails1.getDwzh());
                        this.put("collectionPersonalAccount.grzhzt", PersonAccountStatus.正常.getCode());
                    }}, null, null, null, null, null, null);
                    BigDecimal zxbl = BigDecimal.ZERO;
                    BigDecimal zdbl = BigDecimal.ZERO;
                    boolean firstone = true;
                    boolean firsttwo = true;
                    for (StCommonPerson deposit : listByDwzhNormalDeposit) {
                        if (firstone) {
                            BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), tzhgrbl.multiply(BigDecimal.valueOf(100)), tzhdwbl.multiply(BigDecimal.valueOf(100)), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                            if (zd != null) {
                                zdbl = zd;
                                firstone = false;
                            }
                        } else {
                            BigDecimal zd = ZCZDBL(deposit.getCollectionPersonalAccount().getGrjcjs(), tzhgrbl.multiply(BigDecimal.valueOf(100)), tzhdwbl.multiply(BigDecimal.valueOf(100)), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                            if (zd != null && zdbl.compareTo(BigDecimal.ZERO) > 0) {
                                if (zdbl.compareTo(zd) > 0) {
                                    zdbl = zd;
                                }
                            }
                        }
                        if (firsttwo) {
                            BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), tzhgrbl.multiply(BigDecimal.valueOf(100)), tzhdwbl.multiply(BigDecimal.valueOf(100)), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                            if (zx != null) {
                                zxbl = zx;
                                firsttwo = false;
                            }
                        } else {
                            BigDecimal zx = ZCZXBL(deposit.getCollectionPersonalAccount().getGrjcjs(), tzhgrbl.multiply(BigDecimal.valueOf(100)), tzhdwbl.multiply(BigDecimal.valueOf(100)), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                            if (zx != null && zxbl.compareTo(BigDecimal.ZERO) > 0) {
                                if (zxbl.compareTo(zx) < 0) {
                                    zxbl = zx;
                                }
                            }
                        }
                    }
                    if (zxbl.compareTo(BigDecimal.ZERO) > 0 & zdbl.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和区间：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "% - " + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                    } else if (zdbl.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能超过：" + zdbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                    } else if (zxbl.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "根据缴存额度限制，单位缴存比例与个人缴存比例之和不能低于：" + zxbl.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                    }


                    iSaveAuditHistory.saveNormalBusiness(stCollectionUnitBusinessDetails.get(0).getYwlsh(), tokenContext, CollectionBusinessType.比例调整.getName(), "修改");
                    StateMachineUtils.updateState(iStateMachineService, Events.通过.getEvent(),
                            new TaskEntity() {{
                                this.setOperator(stCollectionUnitBusinessDetails.get(0).getExtension().getCzy());
                                this.setStatus(stCollectionUnitBusinessDetails.get(0).getExtension().getStep());
                                this.setTaskId(stCollectionUnitBusinessDetails.get(0).getYwlsh());
                                this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                                this.setSubtype(BusinessSubType.归集_单位缴存比例调整.getSubType());
                                this.setType(BusinessType.Collection);
                                this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                            }}, new StateMachineUtils.StateChangeHandler() {
                                @Override
                                public void onStateChange(boolean succeed, String next, Exception e) {
                                    if (e != null) {
                                        throw new ErrorException(e);
                                    }
                                    if (!succeed || next == null) {
                                        return;
                                    }
                                    if (StringUtil.isIntoReview(next, null)) {
                                        stCollectionUnitBusinessDetails.get(0).getExtension().setDdsj(new Date());
                                    }
                                    if (succeed) {
                                        if(CollectionBusinessStatus.办结.getName().equals(next)){
                                            doActionDepositRatio(tokenContext,fixinfo);
                                            stCollectionUnitBusinessDetails.get(0).getExtension().setBjsj(new Date());
                                        }
                                        stCollectionUnitBusinessDetails.get(0).getExtension().setStep(next);
                                        collection_unit_business_details.update(stCollectionUnitBusinessDetails.get(0));

                                    }
                                }
                            });
                }
            }
        }

        return new CommonResponses() {{
            this.setState("success");
            this.setId("");
        }};
    }

    @Override
    public final void doRatioTask(String ywlsh) {
        doRatioNomal(ywlsh);
    }

    @Override
    public PageResNew<ListUnitDepositRatioResRes> getDepositRatioInfonew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pageSize, String action) {
        //格式检查以及条件查询

        HashMap<String, Object> search_map = new HashMap<>();
        search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.比例调整.getCode());
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        if (StringUtil.notEmpty(ZhuangTai)) {
            if (ZhuangTai.equals(CollectionBusinessStatus.新建.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.新建.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待审核.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.审核不通过.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.审核不通过.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.办结.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.待入账.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.待入账.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.已入账分摊.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.已入账分摊.getName());
            } else if (ZhuangTai.equals(CollectionBusinessStatus.已入账.getName())) {
                search_map.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.已入账.getName());
            }
        }
        PageResults<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetailsPage = null;
        try {
            stCollectionUnitBusinessDetailsPage = collection_unit_business_details.listWithMarker(search_map, !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ)
                    , !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ), "created_at", Order.DESC, null, null, marker, Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            // criteria.createAlias("unit", "unit");
                            if (StringUtil.notEmpty(DWMC)) {
                                criteria.add(Restrictions.like(CriteriaUtils.addAlias(criteria, "unit.dwmc"), "%" + DWMC + "%"));
                            }
                            if (StringUtil.notEmpty(DWZH)) {
                                criteria.add(Restrictions.like(CriteriaUtils.addAlias(criteria, "dwzh"), "%" + DWZH + "%"));
                            }
                        }
                    });
        } catch (NumberFormatException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = stCollectionUnitBusinessDetailsPage.getResults();
        PageResNew<ListUnitDepositRatioResRes> pageres = new PageResNew<>();
        ArrayList<ListUnitDepositRatioResRes> list = new ArrayList<>();
        ListUnitDepositRatioResRes listUnitDepositRatioResRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            listUnitDepositRatioResRes = new ListUnitDepositRatioResRes();
            //完整
            listUnitDepositRatioResRes.setId(collectionUnitBusinessDetails.getId());
            listUnitDepositRatioResRes.setYWLSH(collectionUnitBusinessDetails.getYwlsh());//业务流水号
            listUnitDepositRatioResRes.setDWZH(collectionUnitBusinessDetails.getDwzh());//单位账号
            StCommonUnit stCommonUnit = common_unit.getUnit(collectionUnitBusinessDetails.getDwzh());
            if (stCommonUnit != null) {
                listUnitDepositRatioResRes.setDWJCBL(stCommonUnit.getCollectionUnitAccount().getDwjcbl() != null ? stCommonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal(100)).toString() : null);
                listUnitDepositRatioResRes.setGRJCBL(stCommonUnit.getCollectionUnitAccount().getGrjcbl() != null ? stCommonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal(100)).toString() : null);
            }
            try {
                listUnitDepositRatioResRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称
                listUnitDepositRatioResRes.setJZNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())) + "");//缴至年月
            } catch (Exception e) {
                if (stCommonUnit != null) {
                    listUnitDepositRatioResRes.setDWMC(stCommonUnit.getDwmc());//单位名称
                    try {
                        listUnitDepositRatioResRes.setJZNY(stCommonUnit.getCollectionUnitAccount().getJzny());//缴至年月
                    } catch (Exception ex) {
                    }
                }
            }
            try {
                listUnitDepositRatioResRes.setYWZT(collectionUnitBusinessDetails.getExtension().getStep());//业务状态
                listUnitDepositRatioResRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
                listUnitDepositRatioResRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());//业务网点
                listUnitDepositRatioResRes.setSLSJ(simpleall.format(collectionUnitBusinessDetails.getExtension().getSlsj()));//受理时间
            } catch (Exception e) {
            }
            list.add(listUnitDepositRatioResRes);

        }
        pageres.setResults(action,list);
        return pageres;
    }


}
