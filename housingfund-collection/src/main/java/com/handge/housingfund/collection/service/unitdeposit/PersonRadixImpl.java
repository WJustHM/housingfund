package com.handge.housingfund.collection.service.unitdeposit;

import com.google.gson.Gson;
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
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.CommonMessage;
import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.InventoryMessage;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelErrorListRes;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;
import com.handge.housingfund.common.service.collection.model.individual.PersonRadixExcelRes;
import com.handge.housingfund.common.service.collection.model.unit.AutoUnitAcctActionRes;
import com.handge.housingfund.common.service.collection.service.unitdeposit.PersonRadix;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctCommon;
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
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Funnyboy on 2017/7/26.
 */

@SuppressWarnings("Duplicates")
@Component
public class PersonRadixImpl implements PersonRadix {
    @Autowired
    private IStCommonUnitDAO common_unit;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collection_unit_business_details;
    @Autowired
    private IStCommonPersonDAO common_person;
    @Autowired
    private IStCollectionPersonalAccountDAO collection_person_account;
    @Autowired
    private ICCollectionPersonRadixViceDAO collectionPersonRadixVice_dao;
    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO collectionUnitDepositInventoryViceDAO;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    IStCollectionUnitAccountDAO istCollectionUnitAccountDAO;
    @Autowired
    private ICCollectionPersonRadixViceDAO personRadixDao;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private com.handge.housingfund.statemachineV2.IStateMachineService iStateMachineService;
    @Autowired
    private ICollectionTimedTaskDAO timedTaskDAO;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    IUploadImagesService iUploadImagesService;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private UnitAcctCommon unitAcctCommon;
    @Autowired
    private IStCommonPolicyDAO istCommonPolicyDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private UnitDepositInventory unitDepositInventory;

    public final static Gson gson = new Gson();
    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    //时间格式转换
    private final SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat simpleall = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat simpleM = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat simpleData = new SimpleDateFormat("yyyyMM");

    /**
     * 列表获取信息
     */
    @Override
    public final PageRes<ListPersonRadixResRes> getPersonRadix(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String pageNumber, String pageSize) {

        HashMap<String, Object> search_map = new HashMap<>();
        search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
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
                !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ),
                !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ),
                "created_at", Order.DESC, null, null,
                Integer.parseInt(pageNumber),
                Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
//                            criteria.createAlias("unit", "unit");
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
        PageRes<ListPersonRadixResRes> pageres = new PageRes<>();
        ArrayList<ListPersonRadixResRes> listPersonRadixResRes = new ArrayList<>();
        ListPersonRadixResRes personRadixResRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {

            personRadixResRes = new ListPersonRadixResRes();
            try {
                personRadixResRes.setYWZT(collectionUnitBusinessDetails.getExtension().getStep());
                personRadixResRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());
                personRadixResRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
                personRadixResRes.setSLSJ(simpleall.format(collectionUnitBusinessDetails.getExtension().getSlsj()));
            } catch (Exception e) {
            }
            try {
                personRadixResRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());
                personRadixResRes.setJZNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())));
            } catch (Exception e) {
                List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
                    if (StringUtil.notEmpty(collectionUnitBusinessDetails.getDwzh()))
                        this.put("dwzh", collectionUnitBusinessDetails.getDwzh());
                }}, null, null, null, null, null, null);
                if (stCommonUnit.size() > 0) {
                    personRadixResRes.setDWMC(stCommonUnit.get(0).getDwmc());
                    try {
                        personRadixResRes.setJZNY(stCommonUnit.get(0).getCollectionUnitAccount().getJzny() == null ? null : simpleM.format(simpleData.parse(stCommonUnit.get(0).getCollectionUnitAccount().getJzny())));
                    } catch (Exception ex) {
                    }
                }

            }
            personRadixResRes.setYWLSH(collectionUnitBusinessDetails.getYwlsh());
            personRadixResRes.setDWZH(collectionUnitBusinessDetails.getDwzh());
            listPersonRadixResRes.add(personRadixResRes);
        }
        pageres.setResults(listPersonRadixResRes);
        pageres.setCurrentPage(stCollectionUnitBusinessDetailsPage.getCurrentPage());
        pageres.setNextPageNo(stCollectionUnitBusinessDetailsPage.getPageNo());
        pageres.setPageSize(stCollectionUnitBusinessDetailsPage.getPageSize());
        pageres.setTotalCount(stCollectionUnitBusinessDetailsPage.getTotalCount());
        pageres.setPageCount(stCollectionUnitBusinessDetailsPage.getPageCount());

        return pageres;
    }


    //政策信息
    public final BigDecimal ZCZDJS(BigDecimal grjcjs, BigDecimal grjcbl, BigDecimal dwjcbl, BigDecimal jcsx, BigDecimal jcxx) {
        BigDecimal yjcjs = grjcjs.multiply(grjcbl).add(grjcjs.multiply(dwjcbl));
        BigDecimal zdbl = BigDecimal.ZERO;
        if (yjcjs.compareTo(BigDecimal.ZERO) >= 0) {
            if (yjcjs.compareTo(jcsx) > 0) {
                zdbl = jcsx.divide(grjcbl.add(dwjcbl), 10, BigDecimal.ROUND_HALF_UP);
            }
        }
        return zdbl;
    }

    public final BigDecimal ZCZXJS(BigDecimal grjcjs, BigDecimal grjcbl, BigDecimal dwjcbl, BigDecimal jcsx, BigDecimal jcxx) {
        BigDecimal yjcjs = grjcjs.multiply(grjcbl).add(grjcjs.multiply(dwjcbl));
        BigDecimal zxbl = BigDecimal.ZERO;
        if (yjcjs.compareTo(BigDecimal.ZERO) >= 0) {
            if (yjcjs.compareTo(jcxx) < 0) {
                zxbl = jcxx.divide(grjcbl.add(dwjcbl), 10, BigDecimal.ROUND_HALF_UP);
            }
        }
        return zxbl;
    }

    /**
     * 添加业务
     */
    @Override
    public final CommonResponses addPersonRadix(TokenContext tokenContext, PersonRadixPost body) {
        if ("1".equals(body.getCZLX())) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人基数调整.getCode(), body.getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "上传资料");
            }
        }
        //数据验证
        ObjectAttributeCheck.checkObjects(new HashMap<String, String>() {{
            this.put("单位账号", body.getDWZH());
            this.put("生效年月", body.getSXNY());
        }});

        List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(body.getDWZH())) this.put("dwzh", body.getDWZH());
        }}, null, null, null, null, null, null);
        if (stCommonUnit.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号不存在");
        if (stCommonUnit.get(0).getExtension() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位必要信息丢失，请联系管理员");
        if (UnitAccountStatus.销户.getCode().equals(stCommonUnit.get(0).getCollectionUnitAccount().getDwzhzt()))
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "该账户处于销户状态");

        if (!tokenContext.getUserInfo().getYWWD().equals(stCommonUnit.get(0).getExtension().getKhwd()))
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");

//        List<StCollectionUnitBusinessDetails> unitBusinessDetails = collection_unit_business_details.list(new HashMap<String, Object>() {{
//            this.put("dwzh", body.getDWZH());
//            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
//        }}, null, null, null, null, null, null);
        List<String> detailsList = collection_unit_business_details.detailsList(body.getDWZH());
        for(String  details:detailsList){
            if (!CollectionBusinessStatus.办结.getName().equals(details)) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process, "存在尚未办结的基数调整业务");
            }
        }

        if ("1".equals(body.getCZLX())) {
            try {
                String dwyhjny = BusUtils.getDWYHJNY(body.getDWZH());
                if (simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime() || simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                    if (simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
                    } else if ((simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime())) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
                    }
                }
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = null;
        try {
            //第一步、业务明细表和拓展表
            stCollectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();
            stCollectionUnitBusinessDetails.setDwzh(body.getDWZH());//单位账号
            stCollectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());
            //拓展表
            CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
            collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
            collectionUnitBusinessDetailsExtension.setJbrxm(body.getJBRXM());//证件姓名
            collectionUnitBusinessDetailsExtension.setJbrzjlx(body.getJBRZJLX());//证件类型
            collectionUnitBusinessDetailsExtension.setJbrzjhm(body.getJBRZJHM());//证件号码
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
            collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
            collectionUnitBusinessDetailsExtension.setYwwd(network);//业务网点
            collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
            collectionUnitBusinessDetailsExtension.setSlsj(new Date());//受理时间
            collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.基数调整.getCode());
            collectionUnitBusinessDetailsExtension.setSxny(body.getSXNY().replace("-", ""));

            //第二步、副表入库
            Set<CCollectionPersonRadixDetailVice> collectionPersonRadixDetailViceset = new HashSet<>();//副表明细
            CCollectionPersonRadixDetailVice collectionPersonRadixDetailVice = null;
            ArrayList<PersonRadixPostJCJSTZXX> personRadixPostJCJSTZXX = body.getJCJSTZXX();//外部获取的
            int i = 0;
            if (personRadixPostJCJSTZXX != null) {
                //个人账号验证是否存在
                if ("1".equals(body.getCZLX())) {
                    StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
                    StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
                    BigDecimal zd = BigDecimal.ZERO;
                    BigDecimal zx = BigDecimal.ZERO;
                    boolean firstone = true;
                    boolean firsttwo = true;
                    BigDecimal tzhgrmin = new BigDecimal(100000000);
                    BigDecimal tzhgrmax = BigDecimal.ZERO;
                    for (PersonRadixPostJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
                        if (StringUtil.notEmpty(personRadixPost.getTZHGRJCJS()) && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(BigDecimal.ZERO) >= 0) {
                            if (firstone && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmin) < 0) {
                                tzhgrmin = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                firstone = false;
                            } else {
                                if (new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmin) < 0) {
                                    tzhgrmin = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                }
                            }
                            if (firsttwo && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmax) > 0) {
                                tzhgrmax = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                firsttwo = false;
                            } else {
                                if (new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmax) > 0) {
                                    tzhgrmax = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                }
                            }
                        }
                    }
                    if (tzhgrmin.compareTo(new BigDecimal(100000000)) < 0) {
                        zx = ZCZXJS(tzhgrmin, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }
                    if (tzhgrmax.compareTo(BigDecimal.ZERO) > 0) {
                        zd = ZCZDJS(tzhgrmax, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }

                    if (zd.compareTo(BigDecimal.ZERO) > 0 && zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整区间：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP) + " - " + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zd.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不大于：" + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不小于：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                }

                //入库，字段是否为空
                for (PersonRadixPostJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
                    collectionPersonRadixDetailVice = new CCollectionPersonRadixDetailVice();
                    collectionPersonRadixDetailVice.setGrzh(personRadixPost.getGRZH());//个人账户
                    collectionPersonRadixDetailVice.setTzqgrjs(new BigDecimal(personRadixPost.getTZQGRJCJS()));//前缴存基数
                    if (personRadixPost.getTZHGRJCJS() == null || "".equals(personRadixPost.getTZHGRJCJS().toString().trim())) {
                        collectionPersonRadixDetailVice.setTzhgrjs(null);//后缴存基数
                    } else {
                        collectionPersonRadixDetailVice.setTzhgrjs(new BigDecimal(personRadixPost.getTZHGRJCJS()));//后缴存基数
                        i++;
                    }
                    collectionPersonRadixDetailViceset.add(collectionPersonRadixDetailVice);
                }
            }
            CCollectionPersonRadixVice collectionPersonRadixVice = new CCollectionPersonRadixVice();//副表
            collectionPersonRadixVice.setFsrs(Long.parseLong(String.valueOf(i)));
            collectionPersonRadixVice.setJstzxq(collectionPersonRadixDetailViceset);//发生人数关联的对对象集合
            collectionPersonRadixVice.setDwjcbl(new BigDecimal(body.getDWJCBL()));
            collectionPersonRadixVice.setGrjcbl(new BigDecimal(body.getGRJCBL()));
            //添加关联
            stCollectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);//业务拓展表
            stCollectionUnitBusinessDetails.setPersonRadixVice(collectionPersonRadixVice);//个人基数副表
            stCollectionUnitBusinessDetails.setUnit(stCommonUnit.get(0));//单位信息
            collectionPersonRadixVice.setDwywmx(stCollectionUnitBusinessDetails);//副表单位业务明细关联，应该放在最下
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        //保存数据
        String ID = collection_unit_business_details.save(stCollectionUnitBusinessDetails);
        //添加状态机
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collection_unit_business_details.get(ID);
        iSaveAuditHistory.saveNormalBusiness(collectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.基数调整.getName(), "新建");
        StateMachineUtils.updateState(iStateMachineService, body.getCZLX().equals("0") ? Events.通过.getEvent() : Events.提交.getEvent(),
            new TaskEntity() {{
                this.setOperator(tokenContext.getUserInfo().getCZY());
                this.setStatus(collectionUnitBusinessDetails.getExtension().getStep());
                this.setTaskId(collectionUnitBusinessDetails.getYwlsh());
                this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                this.setSubtype(BusinessSubType.归集_个人基数调整.getSubType());
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
                    if (StringUtil.isIntoReview(next, null))
                        collectionUnitBusinessDetails.getExtension().setDdsj(new Date());
                    if (succeed) {
                        if(body.getCZLX().equals("1") && CollectionBusinessStatus.办结.getName().equals(next)){
                            doPersonRadix(tokenContext,collectionUnitBusinessDetails.getYwlsh());
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
     * 修改业务
     */
    @Override
    public final CommonResponses rePersonRadix(TokenContext tokenContext, String YWLSH, PersonRadixPut body) {
        if ("1".equals(body.getCZLX())) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.个人基数调整.getCode(), body.getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "上传资料");
            }
        }
        //数据验证
        ObjectAttributeCheck.checkObjects(new HashMap<String, String>() {{
            this.put("单位账号", body.getDWZH());
            this.put("生效年月", body.getSXNY());
        }});

        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
        }}, null, null, null, null, null, null);

        if (stCollectionUnitBusinessDetails.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务(" + YWLSH + ")不存在");

        if (!tokenContext.getUserInfo().getCZY().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getCzy()) ||
            !tokenContext.getUserInfo().getYWWD().equals(stCollectionUnitBusinessDetails.get(0).getExtension().getYwwd().getId()))
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能修改");

        List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(body.getDWZH())) this.put("dwzh", body.getDWZH());
        }}, null, null, null, null, null, null);
        if (stCommonUnit.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号不存在");

        if (!stCollectionUnitBusinessDetails.get(0).getDwzh().equals(body.getDWZH()))
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "单位账号不匹配：" + body.getDWZH() + "、" + stCollectionUnitBusinessDetails.get(0).getDwzh());
        if ("1".equals(body.getCZLX())) {
            String dwyhjny = BusUtils.getDWYHJNY(body.getDWZH());
            try {
                if (simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime() || simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                    if (simpleM.parse(body.getSXNY()).getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
                    } else if ((simpleM.parse(body.getSXNY()).getTime() < simpleM.parse(dwyhjny).getTime())) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
                    }
                }
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }

        //业务记录
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            //第一步、业务明细表拓展表
            collectionUnitBusinessDetails.setDwzh(body.getDWZH());//单位账号
            //拓展表
            CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = null;
            try {
                collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();
                collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
                collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
                collectionUnitBusinessDetailsExtension.setSxny(body.getSXNY().replace("-", ""));
            } catch (Exception e) {
                collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
                collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
                collectionUnitBusinessDetailsExtension.setJbrxm(body.getJBRXM());//证件姓名
                collectionUnitBusinessDetailsExtension.setJbrzjlx(body.getJBRZJLX());//证件类型
                collectionUnitBusinessDetailsExtension.setJbrzjhm(body.getJBRZJHM());//证件号码
                collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
                collectionUnitBusinessDetailsExtension.setSxny(body.getSXNY().replace("-", ""));
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
                collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
                collectionUnitBusinessDetailsExtension.setSlsj(new Date());//受理时间
                collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.基数调整.getCode());
            }
            //第二步、业务副表(一个副表，一个明细副表)
            //永久删除
            CCollectionPersonRadixVice collectionPersonRadixVice = collectionUnitBusinessDetails.getPersonRadixVice();
            if (collectionPersonRadixVice.getJstzxq() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "必要数据丢失，请联系管理员");
            if (collectionPersonRadixVice.getJstzxq().size() != body.getJCJSTZXX().size()) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务发生人数不匹配");
            }

            int total = 0;
            ArrayList<PersonRadixPutJCJSTZXX> personRadixPostJCJSTZXX = body.getJCJSTZXX();
            if (personRadixPostJCJSTZXX != null) {
                //个人账号验证
//                for (PersonRadixPutJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
//                    if (personRadixPost.getGRZH() != null) {
//                        if (collection_person_account.getByGrzh(personRadixPost.getGRZH()) == null)
//                            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人账号(" + personRadixPost.getGRZH() + ")不存在");
//                    }
//                }

                if ("1".equals(body.getCZLX())) {
                    StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
                    StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
                    BigDecimal zd = BigDecimal.ZERO;
                    BigDecimal zx = BigDecimal.ZERO;
                    boolean firstone = true;
                    boolean firsttwo = true;
                    BigDecimal tzhgrmin = new BigDecimal(100000000);
                    BigDecimal tzhgrmax = BigDecimal.ZERO;
                    for (PersonRadixPutJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
                        if (StringUtil.notEmpty(personRadixPost.getTZHGRJCJS()) && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(BigDecimal.ZERO) >= 0) {
                            if (firstone && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmin) < 0) {
                                tzhgrmin = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                firstone = false;
                            } else {
                                if (new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmin) < 0) {
                                    tzhgrmin = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                }
                            }
                            if (firsttwo && new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmax) > 0) {
                                tzhgrmax = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                firsttwo = false;
                            } else {
                                if (new BigDecimal(personRadixPost.getTZHGRJCJS()).compareTo(tzhgrmax) > 0) {
                                    tzhgrmax = new BigDecimal(personRadixPost.getTZHGRJCJS());
                                }
                            }
                        }
                    }
                    if (tzhgrmin.compareTo(new BigDecimal(100000000)) < 0) {
                        zx = ZCZXJS(tzhgrmin, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }
                    if (tzhgrmax.compareTo(BigDecimal.ZERO) > 0) {
                        zd = ZCZDJS(tzhgrmax, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }

                    if (zd.compareTo(BigDecimal.ZERO) > 0 && zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整区间：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP) + " - " + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zd.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不大于：" + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不小于：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                }
                //入库
                for (PersonRadixPutJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
                    for (CCollectionPersonRadixDetailVice cllectionPersonRadixDetailVices : collectionPersonRadixVice.getJstzxq()) {
                        if (cllectionPersonRadixDetailVices.getGrzh().equals(personRadixPost.getGRZH())) {
//                            collectionPersonRadixDetailVice.setTzqgrjs(new BigDecimal(personRadixPost.getTZQGRJCJS()));//前缴存基数
                            if (personRadixPost.getTZHGRJCJS() == null || "".equals(personRadixPost.getTZHGRJCJS().toString().trim())) {
                                cllectionPersonRadixDetailVices.setTzhgrjs(null);//后缴存基数
                            } else {
                                cllectionPersonRadixDetailVices.setTzhgrjs(new BigDecimal(personRadixPost.getTZHGRJCJS()));//后缴存基数

                                total++;
                            }
                        }
                    }
                }
            }

            collectionPersonRadixVice.setFsrs(Long.parseLong(String.valueOf(total)));
            collectionPersonRadixVice.setDwjcbl(new BigDecimal(body.getDWJCBL()));
            collectionPersonRadixVice.setGrjcbl(new BigDecimal(body.getGRJCBL()));
            //关联
            collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);
            collectionPersonRadixVice.setDwywmx(collectionUnitBusinessDetails);
            if ("1".equals(body.getCZLX())) {
                iSaveAuditHistory.saveNormalBusiness(collectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.基数调整.getName(), "修改");
            }
            //状态机
            StateMachineUtils.updateState(iStateMachineService, body.getCZLX().equals("0") ? Events.保存.getEvent() : Events.通过.getEvent(),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(collectionUnitBusinessDetails.getExtension().getStep());
                    this.setTaskId(collectionUnitBusinessDetails.getYwlsh());
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.归集_个人基数调整.getSubType());
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
                                collectionUnitBusinessDetails.getExtension().setBjsj(new Date());
                                doPersonRadix(tokenContext,collectionUnitBusinessDetails.getYwlsh());
                            }
                            collectionUnitBusinessDetails.getExtension().setStep(next);
                            collection_unit_business_details.update(collectionUnitBusinessDetails);

                        }
                    }
                });
        }

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    /**
     * 详情
     */
    @Override
    public final GetPersonRadixRes showPersonRadix(TokenContext tokenContext, String YWLSH) {
        CCollectionPersonRadixVice personRadix = personRadixDao.getPersonRadix(YWLSH);

        GetPersonRadixRes result = new GetPersonRadixRes();

        StCollectionUnitBusinessDetails dwywmx = personRadix.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        //关键信息
        GetPersonRadixResDWGJXX gjxx = new GetPersonRadixResDWGJXX();   //单位关键信息
        gjxx.setYWLSH(dwywmx.getYwlsh());
        gjxx.setDWZH(dwywmx.getDwzh());
        gjxx.setDWJBR(extension.getJbrxm());
        gjxx.setZJHM(extension.getJbrzjhm());
        gjxx.setZJLX(extension.getJbrzjlx());
        result.setBLZL(extension.getBlzl());//办理资料
        result.setCZY(extension.getCzy());//操作员
        result.setYWWD(extension.getYwwd().getMingCheng());//业务网点
        result.setSXNY(DateUtil.str2str(extension.getSxny(), 6));

        result.setDWYHJNY(BusUtils.getDWYHJNY(dwywmx.getDwzh()));
        gjxx.setFSRS(personRadix.getFsrs() + "");//发生人数

        gjxx.setDWMC(dwywmx.getUnit().getDwmc());//单位名称

        //缴存基数调整信息
        result.setDWJCBL(ComUtils.moneyFormat(personRadix.getDwjcbl()));

        result.setGRJCBL(ComUtils.moneyFormat(personRadix.getGrjcbl()));

        ArrayList<GetPersonRadixResJCJSTZXX> list = getPersonRadixDetail(personRadix);
        BigDecimal gryjcehj = new BigDecimal("0.00");
        BigDecimal dwyjcehj = new BigDecimal("0.00");
        BigDecimal yjcehj = new BigDecimal("0.00");
        for(GetPersonRadixResJCJSTZXX v : list){
            if(v.getGRYJCE()!=null){
                gryjcehj = gryjcehj.add(new BigDecimal(v.getGRYJCE()));
            }
            if(v.getDWYJCE()!=null){
                dwyjcehj = dwyjcehj.add(new BigDecimal(v.getDWYJCE()));
            }
            if(v.getYJCE()!=null){
                yjcehj = yjcehj.add(new BigDecimal(v.getYJCE()));
            }

        }
        result.setGRYJCEHJ(gryjcehj.toString());
        result.setDWYJCEHJ(dwyjcehj.toString());
        result.setYJCEHJ(yjcehj.toString());
        result.setDWGJXX(gjxx);
        result.setJCJSTZXX(list);

        return result;
    }

    private ArrayList<GetPersonRadixResJCJSTZXX> getPersonRadixDetail(CCollectionPersonRadixVice personRadix ) {
        ArrayList<GetPersonRadixResJCJSTZXX> result = new ArrayList<>();
        StCommonUnit dwxx = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("dwzh", personRadix.getDwywmx().getDwzh());
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<Object[]> personRadixDetail = personRadixDao.getPersonRadixDetail(personRadix.getId());
        for(Object[] obj : personRadixDetail){
            GetPersonRadixResJCJSTZXX detailView = new GetPersonRadixResJCJSTZXX();

            detailView.setGRZH((String)obj[0]);//个人账户
            detailView.setXingMing((String)obj[1]);//姓名
            detailView.setZJHM((String)obj[2]);//证件号码

            detailView.setJZNY(ComUtils.parseToYYYYMM2((String)obj[4]));//缴至年月

            BigDecimal tzqjcjs = (BigDecimal) obj[5];
            BigDecimal tzhjcjs = (BigDecimal) obj[6];
            detailView.setTZQGRJCJS(ComUtils.moneyFormat(tzqjcjs));//调整前缴存基数
            detailView.setTZHGRJCJS(ComUtils.moneyFormat(tzhjcjs));//调整后缴存基数

            BigDecimal jsView = (tzhjcjs == null) ? tzqjcjs : tzhjcjs;  //非空的时候，用调整后的基数计算，空的时候用调整前的基数计算
            BigDecimal gryjce = personRadix.getGrjcbl().multiply(jsView).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal dwyjce = personRadix.getDwjcbl().multiply(jsView).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal yjce = gryjce.add(dwyjce);

            detailView.setGRYJCE(ComUtils.moneyFormat(gryjce));
            detailView.setDWYJCE(ComUtils.moneyFormat(dwyjce));
            detailView.setYJCE(ComUtils.moneyFormat(yjce));

            result.add(detailView);

        }
        return result;
    }

    /**
     * 查询2
     */
    @Override
    public final GetPersonRadixBeforeRes autoPersonRadix(TokenContext tokenContext, String DWZH) {
        List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("dwzh", DWZH);
        }}, null, null, null, null, null, null);

        if (stCommonUnit.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号(" + DWZH + ")不存在");
        }
        List<StCommonPerson> stCommonPerson = common_person.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DWZH)) this.put("unit.dwzh", DWZH);
        }}, null, null, null, null, null, null);

        if (stCommonPerson.size() == 0) throw new ErrorException(ReturnEnumeration.Data_MISS, "该单位中没有个人信息");

        GetPersonRadixBeforeRes getPersonRadixBeforeRes = new GetPersonRadixBeforeRes();
        getPersonRadixBeforeRes.setDWZH(stCommonUnit.get(0).getDwzh());//单位账户
        getPersonRadixBeforeRes.setDWMC(stCommonUnit.get(0).getDwmc());//单位名称
        getPersonRadixBeforeRes.setJBRXM(stCommonUnit.get(0).getJbrxm());//经办人姓名
        getPersonRadixBeforeRes.setJBRZJLX(stCommonUnit.get(0).getJbrzjlx());//经办人类型
        getPersonRadixBeforeRes.setJBRZJHM(stCommonUnit.get(0).getJbrzjhm());//经办人号码
        getPersonRadixBeforeRes.setDWJCBL(stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl().toString());
        getPersonRadixBeforeRes.setGRJCBL(stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl().toString());
//        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = new ArrayList<>();
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = personRadixDao.getRadixs( DWZH);
        BigDecimal dwyjcehj = new BigDecimal("0.00");
        BigDecimal gryjcehj = new BigDecimal("0.00");
        BigDecimal yjcehj =  new BigDecimal("0.00");
        for (GetPersonRadixBeforeResJCJSTZXX l:list) {
            if(l.getGRYJCE()!=null){
                gryjcehj = gryjcehj.add(new BigDecimal(l.getGRYJCE()));
            }
            if(l.getDWYJCE()!=null){
                dwyjcehj = dwyjcehj.add(new BigDecimal(l.getDWYJCE()));
            }
            if(l.getYJCE()!=null){
                yjcehj = yjcehj.add(new BigDecimal(l.getYJCE()));
            }

        }
        getPersonRadixBeforeRes.setDWYHJNY(BusUtils.getDWYHJNY(DWZH));
        getPersonRadixBeforeRes.setJCJSTZXX(list);//集合
        getPersonRadixBeforeRes.setGRYJCEHJ(gryjcehj.toString());
        getPersonRadixBeforeRes.setDWYJCEHJ(dwyjcehj.toString());
        getPersonRadixBeforeRes.setYJCEHJ(yjcehj.toString());
        return getPersonRadixBeforeRes;
    }

    /**
     * 回执单
     */
    @Override
    public final CommonResponses headPersonRadix(TokenContext tokenContext, String YWLSH) {
        List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
        }}, null, null, "created_at", null, null, null);

        if (stCollectionUnitBusinessDetails.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务(" + YWLSH + ")不存在");
        }
        String step = null;
        try {
            step = stCollectionUnitBusinessDetails.get(0).getExtension().getStep();
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务状态");
        }

        if (step == null || !step.equals(CollectionBusinessStatus.办结.getName()))
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "不能打印回执单");

        HeadPersonRadixRes getPersonRadixRes = null;
        HeadPersonRadixResDWGJXX getPersonRadixResDWGJXX = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {
            getPersonRadixRes = new HeadPersonRadixRes();
            //关键信息
            getPersonRadixResDWGJXX = new HeadPersonRadixResDWGJXX();
            try {
                getPersonRadixResDWGJXX.setYWLSH(collectionUnitBusinessDetails.getYwlsh());//业务流水号
                getPersonRadixResDWGJXX.setTZSJ(simple.format(new Date()));//时间
                getPersonRadixResDWGJXX.setDWZH(collectionUnitBusinessDetails.getDwzh());//单位账号
                getPersonRadixResDWGJXX.setFSRS(collectionUnitBusinessDetails.getPersonRadixVice().getFsrs() + "");//发生人数
                getPersonRadixResDWGJXX.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());//单位名称
                getPersonRadixResDWGJXX.setDWJBR(collectionUnitBusinessDetails.getExtension().getJbrxm());//姓名
                getPersonRadixResDWGJXX.setJBRZJHM(collectionUnitBusinessDetails.getExtension().getJbrzjhm());//号码
                getPersonRadixResDWGJXX.setJBRZJLX(collectionUnitBusinessDetails.getExtension().getJbrzjlx());//类型
                getPersonRadixResDWGJXX.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());//操作员
                getPersonRadixResDWGJXX.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
            } catch (Exception e) {
                if (collectionUnitBusinessDetails.getExtension() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "拓展信息丢失，不能打印回执单");
                if (collectionUnitBusinessDetails.getUnit() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息丢失，不能打印回执单");
                throw new ErrorException(e);
            }

            //缴存基数调整信息
            ArrayList<HeadPersonRadixResJCJSTZXX> list = new ArrayList<>();
            HeadPersonRadixResJCJSTZXX getPersonRadixResJCJSTZXX = null;
            try {
                if (collectionUnitBusinessDetails.getPersonRadixVice() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务必要数据为空，请联系管理员");
                Set<CCollectionPersonRadixDetailVice> cCollectionPersonRadixDetailVice = collectionUnitBusinessDetails.getPersonRadixVice().getJstzxq();
                if (cCollectionPersonRadixDetailVice != null) {
                    for (CCollectionPersonRadixDetailVice personRadixDetailVice : cCollectionPersonRadixDetailVice) {
                        if (personRadixDetailVice.getTzhgrjs() == null || "".equals(personRadixDetailVice.getTzhgrjs().toString().trim()))
                            continue;
                        getPersonRadixResJCJSTZXX = new HeadPersonRadixResJCJSTZXX();
                        List<StCommonPerson> stCommonPerson = common_person.list(new HashMap<String, Object>() {{
                            this.put("grzh", personRadixDetailVice.getGrzh());
                        }}, null, null, "created_at", null, null, null);
                        if (stCommonPerson.size() == 0) {
                            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人账户(" + personRadixDetailVice.getGrzh() + ")不存在");
                        }
                        getPersonRadixResJCJSTZXX.setGRZH(personRadixDetailVice.getGrzh());//个人账户
                        getPersonRadixResJCJSTZXX.setXingMing(stCommonPerson.get(0).getXingMing());//姓名
                        try {
                            getPersonRadixResJCJSTZXX.setJZNY(stCommonPerson.get(0).getUnit().getCollectionUnitAccount().getJzny() + "");//缴至年月
                            getPersonRadixResJCJSTZXX.setTZHJCJS(personRadixDetailVice.getTzhgrjs() + "");//后缴存基数
                            getPersonRadixResJCJSTZXX.setTZQJCJS(personRadixDetailVice.getTzqgrjs() + "");//前缴存基数
                        } catch (NullPointerException e) {
                            if (stCommonPerson.get(0).getUnit() == null)
                                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息丢失，不能打印回执单");
                            if (stCommonPerson.get(0).getUnit().getCollectionUnitAccount() == null)
                                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号信息丢失，不能打印回执单");
                            throw new ErrorException(e);
                        }
                        list.add(getPersonRadixResJCJSTZXX);
                    }
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }
            Collections.sort(list);
            getPersonRadixRes.setDWGJXX(getPersonRadixResDWGJXX);
            getPersonRadixRes.setJCJSTZXX(list);
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
            getPersonRadixRes.setSHR(cAuditHistory.getCzy());
        }
        String id = pdfService.getPersonRadixPdf(getPersonRadixRes);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 办结
     * 根据生效年月：直接办理/生效月份为未来则使用定时任务
     */
    @Override
    public final void doPersonRadix(TokenContext tokenContext, String ywlsh) {
        //参数检查
        if (!StringUtil.notEmpty(ywlsh)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }
        iSaveAuditHistory.saveNormalBusiness(ywlsh, tokenContext, CollectionBusinessType.基数调整.getName(), "办结");

        //基数调整，根据业务流水号，更新个人的缴存基数、缴存额信息
        personRadixDao.doFinal(ywlsh);
        //更新业务办结时间 （人数多的单位会因为详情信息效率低，因为对象结构不好）
        personRadixDao.updateBJSJ(ywlsh);

    }

    /**
     * 正常办结时更新个人账户
     */
    private void doRadixNomal(String ywlsh) {
        CCollectionPersonRadixVice personRadix = personRadixDao.getPersonRadix(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = personRadix.getDwywmx();
        String dwzh = dwywmx.getDwzh();
        StCommonUnit unit = common_unit.getUnit(dwzh);
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        BigDecimal grjcbl = unitAccount.getGrjcbl();
        BigDecimal dwjcbl = unitAccount.getDwjcbl();

        Set<CCollectionPersonRadixDetailVice> set = personRadix.getJstzxq();
        List<CCollectionPersonRadixDetailVice> detailList = new ArrayList<>(set);
        //移除不调整的人员
        removeNotAdjust(detailList);
        //按个人账号排序
        Collections.sort(detailList);
        //得到单位下的职工账户信息，按个人账号排序
        List<StCollectionPersonalAccount> persons = common_person.getPersonalAccounts(dwzh);
        Collections.sort(persons);
        //双排序检索
        int i=0,j=0;
        int size = detailList.size();
        int size2 = persons.size();
        while(i< size && j< size2){
            CCollectionPersonRadixDetailVice radixDetail = detailList.get(i);
            StCollectionPersonalAccount personAccount = persons.get(j);
            String grzh = radixDetail.getGrzh();
            if(grzh.compareTo(personAccount.getGrzh()) == 0){
                updatePersonAcount(personAccount,radixDetail.getTzhgrjs(),grjcbl,dwjcbl);
                i++;j++;
            }else if(grzh.compareTo(personAccount.getGrzh()) < 0){
                i++;
            }else{
                j++;
            }
        }
    }

    private void updatePersonAcount(StCollectionPersonalAccount personAccount, BigDecimal newGrjcjs, BigDecimal grjcbl, BigDecimal dwjcbl) {
        personAccount.setGrjcjs(newGrjcjs);
        personAccount.setGryjce(BusUtils.computeDeposit(newGrjcjs, grjcbl));
        personAccount.setDwyjce(BusUtils.computeDeposit(newGrjcjs, dwjcbl));
        commonPersonDAO.updateNormal(personAccount);
    }

    private void removeNotAdjust(List<CCollectionPersonRadixDetailVice> list) {
        Iterator<CCollectionPersonRadixDetailVice> iterator = list.iterator();
        while(iterator.hasNext()){
            CCollectionPersonRadixDetailVice detail = iterator.next();
            BigDecimal tzhgrjs = detail.getTzhgrjs();
            if(tzhgrjs == null || BigDecimal.ZERO.equals(tzhgrjs)){
                iterator.remove();
            }
        }
    }

    /**
     * 添加定时任务
     */
    private void doCreateRadixTask(String ywlsh, String sxny) {
        CCollectionTimedTask task = new CCollectionTimedTask();
        task.setYwlsh(ywlsh);
        task.setZxzt("00");
        task.setYwlx(CollectionBusinessType.基数调整.getCode());
        task.setYwms("基数调整业务");
        task.setZxcs(0);
        task.setZxsj(sxny);
        timedTaskDAO.save(task);
    }


    /**
     * 批量
     */
    @Override
    public final CommonResponses batchSubmit(TokenContext tokenContext, BatchSubmission body) {
        if (body != null) {
            for (String fixinfo : body.getYWLSHJH()) {
                //业务信息记录
                List<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = collection_unit_business_details.list(
                    new HashMap<String, Object>() {{
                        if (StringUtil.notEmpty(fixinfo)) this.put("ywlsh", fixinfo);
                        this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
                    }}, null, null, null, null, null, null);
                if (stCollectionUnitBusinessDetails.size() > 0) {
                    final StCollectionUnitBusinessDetails unitBusinessDetails = stCollectionUnitBusinessDetails.get(0);
                    if (!tokenContext.getUserInfo().getCZY().equals(unitBusinessDetails.getExtension().getCzy()) ||
                        !tokenContext.getUserInfo().getYWWD().equals(unitBusinessDetails.getExtension().getYwwd().getId()))
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + fixinfo + ")不是由您受理的，不能提交");
                    if (unitBusinessDetails.getExtension() == null)
                        throw new ErrorException("业务(" + fixinfo + ")数据拓展信息丢失");
                    if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                        UploadFileBusinessType.个人基数调整.getCode(), unitBusinessDetails.getExtension().getBlzl())) {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "上传资料");
                    }
                    //bug修改 杨凡 2017-11-18 增加 生效年月大于等于应汇缴年月的限制
                    checkSxny(unitBusinessDetails);

                    StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails1 = stCollectionUnitBusinessDetails.get(0);

                    List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
                        this.put("dwzh", stCollectionUnitBusinessDetails1.getDwzh());
                    }}, null, null, null, null, null, null);
                    if (stCommonUnit.size() == 0)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号不存在");

                    StCommonPolicy yjcesx = istCommonPolicyDAO.get("YJCESX");
                    StCommonPolicy yjcexx = istCommonPolicyDAO.get("YJCEXX");
                    BigDecimal zd = BigDecimal.ZERO;
                    BigDecimal zx = BigDecimal.ZERO;
                    boolean firstone = true;
                    boolean firsttwo = true;
                    BigDecimal tzhgrmin = new BigDecimal(100000000);
                    BigDecimal tzhgrmax = BigDecimal.ZERO;
                    for (CCollectionPersonRadixDetailVice personRadixPost : stCollectionUnitBusinessDetails1.getPersonRadixVice().getJstzxq()) {
                        if (personRadixPost.getTzhgrjs() != null && personRadixPost.getTzhgrjs().compareTo(BigDecimal.ZERO) >= 0) {
                            if (firstone && personRadixPost.getTzhgrjs().compareTo(tzhgrmin) < 0) {
                                tzhgrmin = personRadixPost.getTzhgrjs();
                                firstone = false;
                            } else {
                                if (personRadixPost.getTzhgrjs().compareTo(tzhgrmin) < 0) {
                                    tzhgrmin = personRadixPost.getTzhgrjs();
                                }
                            }
                            if (firsttwo && personRadixPost.getTzhgrjs().compareTo(tzhgrmax) > 0) {
                                tzhgrmax = personRadixPost.getTzhgrjs();
                                firsttwo = false;
                            } else {
                                if (personRadixPost.getTzhgrjs().compareTo(tzhgrmax) > 0) {
                                    tzhgrmax = personRadixPost.getTzhgrjs();
                                }
                            }
                        }
                    }

                    if (tzhgrmin.compareTo(new BigDecimal(100000000)) < 0) {
                        zx = ZCZXJS(tzhgrmin, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }
                    if (tzhgrmax.compareTo(BigDecimal.ZERO) > 0) {
                        zd = ZCZDJS(tzhgrmax, stCommonUnit.get(0).getCollectionUnitAccount().getGrjcbl(), stCommonUnit.get(0).getCollectionUnitAccount().getDwjcbl(), yjcesx.getYjcesx(), yjcexx.getYjcexx());
                    }
                    if (zd.compareTo(BigDecimal.ZERO) > 0 && zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整区间：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP) + " - " + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zd.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不大于：" + zd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else if (zx.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴存基数调整应不小于：" + zx.setScale(2, BigDecimal.ROUND_HALF_UP));
                    }


                    iSaveAuditHistory.saveNormalBusiness(unitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.基数调整.getName(), "修改");
                    StateMachineUtils.updateState(iStateMachineService, Events.通过.getEvent(),
                        new TaskEntity() {{
                            this.setOperator(tokenContext.getUserInfo().getCZY());
                            this.setStatus(unitBusinessDetails.getExtension().getStep());
                            this.setTaskId(unitBusinessDetails.getYwlsh());
                            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                            this.setSubtype(BusinessSubType.归集_个人基数调整.getSubType());
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
                                    unitBusinessDetails.getExtension().setDdsj(new Date());
                                }
                                if (succeed) {
                                    if(CollectionBusinessStatus.办结.getName().equals(next)){
                                        unitBusinessDetails.getExtension().setBjsj(new Date());
                                        doPersonRadix(tokenContext,unitBusinessDetails.getYwlsh());
                                    }
                                    unitBusinessDetails.getExtension().setStep(next);
                                    collection_unit_business_details.update(unitBusinessDetails);
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

    private void checkSxny(StCollectionUnitBusinessDetails dwywmx) {
        String dwzh = dwywmx.getDwzh();
        String dwyhjnyStr = BusUtils.getDWYHJNY(dwzh);
        String sxnyStr = dwywmx.getExtension().getSxny();
        Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
        Date dwyhjny = ComUtils.parseToDate(dwyhjnyStr, "yyyy-MM");
        if (sxny.getTime() < dwyhjny.getTime() || sxny.getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
            if (sxny.getTime() > DateUtil.dateStringtoStringDate(new Date(), "yyyy-MM").getTime()) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "调整日期不能大于当前时间" + simpleM.format(new Date()));
            } else if ((sxny.getTime() < dwyhjny.getTime())) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + simpleM.format(dwyhjny));
            }
// else {
//                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月小于等于当前时间" + dwyhjny + " - " + simpleM.format(new Date()));
//            }
        }
//        if (sxny.compareTo(dwyhjny) < 0) {
//            throw new ErrorException(dwywmx.getYwlsh() + "：生效年月应大于等于单位的应汇缴年月！");
//        }
    }

    @Override
    public final void doRadixTask(String ywlsh) {
//        doRadixNomal(ywlsh);
    }


    /**
     * 批量导入个人基数调整
     */

    public CommonMessage batchExcelData(TokenContext tokenContext, PersonRadixPost body, List<StCommonUnit> stCommonUnit, CAccountNetwork network) {
//        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
//            UploadFileBusinessType.个人基数调整.getCode(), body.getBLZL())) {
//            throw new ErrorException(ReturnEnumeration.Data_MISS, "上传资料");
//        }
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = null;
        CommonMessage commonMessage = new CommonMessage();
        try {
            //第一步、业务明细表和拓展表
            stCollectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();
            stCollectionUnitBusinessDetails.setDwzh(body.getDWZH());//单位账号
            stCollectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());
            //拓展表
            CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
            collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
            StCommonUnit unit = stCommonUnit.get(0);
            collectionUnitBusinessDetailsExtension.setJbrxm(unit.getJbrxm());//证件姓名
            collectionUnitBusinessDetailsExtension.setJbrzjlx(unit.getJbrzjlx());//证件类型
            collectionUnitBusinessDetailsExtension.setJbrzjhm(unit.getJbrzjhm());//证件号码

            collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());//操作员
            collectionUnitBusinessDetailsExtension.setYwwd(network);//业务网点
            collectionUnitBusinessDetailsExtension.setBlzl(body.getBLZL());//办理资料
            collectionUnitBusinessDetailsExtension.setSlsj(new Date());//受理时间
            collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.基数调整.getCode());
            collectionUnitBusinessDetailsExtension.setSxny(body.getSXNY().replace("-", ""));

            //第二步、副表入库
            Set<CCollectionPersonRadixDetailVice> collectionPersonRadixDetailViceset = new HashSet<>();//副表明细
            CCollectionPersonRadixDetailVice collectionPersonRadixDetailVice = null;
            ArrayList<PersonRadixPostJCJSTZXX> personRadixPostJCJSTZXX = body.getJCJSTZXX();//外部获取的
            int i = 0;
            if (personRadixPostJCJSTZXX != null) {

                //入库，字段是否为空
                for (PersonRadixPostJCJSTZXX personRadixPost : personRadixPostJCJSTZXX) {
                    //个人账号验证是否存在
                    String grzh = personRadixPost.getGRZH();
                    StCollectionPersonalAccount person = collection_person_account.getByGrzh(grzh);
                    AssertUtils.notEmpty(person,"个人信息(" + personRadixPost.getGRZH() + ")不存在");

                    collectionPersonRadixDetailVice = new CCollectionPersonRadixDetailVice();
                    collectionPersonRadixDetailVice.setGrzh(grzh);//个人账户
                    collectionPersonRadixDetailVice.setTzqgrjs(new BigDecimal(personRadixPost.getTZQGRJCJS()));//前缴存基数    //person.getGrjcjs()
                    if (personRadixPost.getTZHGRJCJS() == null || "".equals(personRadixPost.getTZHGRJCJS().toString().trim())) {
                        collectionPersonRadixDetailVice.setTzhgrjs(null);//后缴存基数
                    } else {
                        collectionPersonRadixDetailVice.setTzhgrjs(new BigDecimal(personRadixPost.getTZHGRJCJS()));//后缴存基数
                        i++;
                    }
                    collectionPersonRadixDetailViceset.add(collectionPersonRadixDetailVice);
                }
            }
            CCollectionPersonRadixVice collectionPersonRadixVice = new CCollectionPersonRadixVice();//副表
            collectionPersonRadixVice.setFsrs(Long.parseLong(String.valueOf(i)));
            collectionPersonRadixVice.setJstzxq(collectionPersonRadixDetailViceset);//发生人数关联的对对象集合
            //添加关联
            stCollectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);//业务拓展表
            stCollectionUnitBusinessDetails.setPersonRadixVice(collectionPersonRadixVice);//个人基数副表
            stCollectionUnitBusinessDetails.setUnit(unit);//单位信息
            collectionPersonRadixVice.setDwywmx(stCollectionUnitBusinessDetails);//副表单位业务明细关联，应该放在最下

            Date date = checkSxny(unit,body.getSXNY());
            InventoryMessage inventory2 = unitDepositInventory.getInventory2(unit.getDwzh(), date);

            collectionPersonRadixVice.setDwjcbl(new BigDecimal(inventory2.getDWJCBL()).divide(new BigDecimal("100")));
            collectionPersonRadixVice.setGrjcbl(new BigDecimal(inventory2.getGRJCBL()).divide(new BigDecimal("100")));

        } catch (Exception e) {
            commonMessage.setCode("错误原因:");
            commonMessage.setMessage(e.getMessage());
            return commonMessage;
        }
        //保存数据
        //    collection_unit_business_details.saveNoflush(stCollectionUnitBusinessDetails);

        stCollectionUnitBusinessDetails.getExtension().setStep("新建");
        String ID = collection_unit_business_details.savePerRadix(stCollectionUnitBusinessDetails);
        //添加状态机
//        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collection_unit_business_details.get(ID);
//        iSaveAuditHistory.saveNormalBusiness(collectionUnitBusinessDetails.getYwlsh(), tokenContext, CollectionBusinessType.基数调整.getName(), "新建");
        commonMessage.setMessage("Success");
        commonMessage.setCode("01");
        return commonMessage;
    }

    public final ImportExcelRes saveImportRadix(String id,TokenContext tokenContext, Map<Integer, Map<Integer, Object>> map) {
        HashMap<String, String> KeyValue = new HashMap<>();
        ArrayList arrayList = new ArrayList();
        ArrayList indiAcctSetPostList = new ArrayList();
        ImportExcelRes importExcelRes = new ImportExcelRes();
        ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
        SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM");
        KeyValue.put("DWMC", String.valueOf(map.get(2).get(1)).trim());
        KeyValue.put("DWZH", String.valueOf(map.get(2).get(5)).replace(" ", ""));
        String SXNY = map.get(2).get(8).toString().trim();
        if (SXNY.equals("") || SXNY == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "生效年月数据缺失");
        }else{
            /*Date d = null;
            try {
                d = ymd.parse(SXNY);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            KeyValue.put("SXNY", SXNY);
        }
        //数据验证
        //ObjectAttributeCheck.checkObject(body);
        List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(KeyValue.get("DWZH"))) this.put("dwzh", KeyValue.get("DWZH"));
        }}, null, null, null, null, null, null);
        if (stCommonUnit.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位账号不存在");
        if (stCommonUnit.get(0).getExtension() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位必要信息丢失，请联系管理员");
        if (UnitAccountStatus.销户.getCode().equals(stCommonUnit.get(0).getCollectionUnitAccount().getDwzhzt()))
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "该账户处于销户状态");

        if (!tokenContext.getUserInfo().getYWWD().equals(stCommonUnit.get(0).getExtension().getKhwd()))
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");

        List<StCollectionUnitBusinessDetails> unitBusinessDetails = collection_unit_business_details.list(new HashMap<String, Object>() {{
            this.put("dwzh", KeyValue.get("DWZH"));
            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
        }}, null, null, null, null, null, null);
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
        for (StCollectionUnitBusinessDetails unitBusinessDetailsbbb : unitBusinessDetails) {
            try {
                if (!CollectionBusinessStatus.办结.getName().equals(unitBusinessDetailsbbb.getExtension().getStep())) {
                    throw new ErrorException(ReturnEnumeration.Business_In_Process, "存在尚未办结的基数调整业务");
                }
            } catch (Exception e) {
                if (unitBusinessDetailsbbb.getExtension() == null)
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "该业务(" + unitBusinessDetailsbbb.getYwlsh() + ")必要信息丢失，请联系管理员");
                throw new ErrorException(e);
            }
        }

        String dwyhjny = BusUtils.getDWYHJNY(KeyValue.get("DWZH"));
        try {
            if (simpleM.parse(KeyValue.get("SXNY")).getTime() < simpleM.parse(dwyhjny).getTime()) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效年月应大于等于应汇缴年月" + dwyhjny);
            }
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
        Integer F = 0;
        Integer c = 0;
        PersonRadixPost personRadixPost = new PersonRadixPost();
        String grzh = null;
        for (int i = 4; i <= map.size(); i++) {
            String v = String.valueOf(map.get(i).get(0));
            if (v == null || v.length() <= 0) {
                c = 1;
                break;
            }
            if ((map.get(i).get(0) == null || map.get(i).get(0).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "序号数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }

            if ((map.get(i).get(1) == null || map.get(i).get(1).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "个人账号数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            grzh = map.get(i).get(1).toString().trim();
            String finalGrzh = grzh;
            StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grzh", finalGrzh);

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }

            });
            if(commonPerson==null){
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "个人账号不存在");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            /*if(!commonPerson.getUnit().getDwzh().equals(KeyValue.get("DWZH"))){
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "个人账号不属于该单位");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }*/
            if ((map.get(i).get(2) == null || map.get(i).get(2).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "姓名数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(3) == null || map.get(i).get(3).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "证件号码数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(4) == null || map.get(i).get(4).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "单位月缴存额数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(5) == null || map.get(i).get(5).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "个人月缴存额数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
            if ((map.get(i).get(6) == null || map.get(i).get(6).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "月缴存额数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
//            if ((map.get(i).get(5) == null || map.get(i).get(5).equals(""))) {
//                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "缴至年月数据缺失");
//                arrayList.add(excelErrorListRes);
//                F++;
//                break;
//            }
            if ((map.get(i).get(7) == null || map.get(i).get(7).equals(""))) {
                excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("第" + (i + 1) + "行", "调整前缴存基数数据缺失");
                arrayList.add(excelErrorListRes);
                F++;
                break;
            }
        }
        if (c == 1 && F == 0) {
            excelErrorListRes = ExcelUtil.ReturnExcelErrorlist("数据", "正在导入");
            arrayList.add(excelErrorListRes);
        }
        importExcelRes.setSuccess_num("0");
        importExcelRes.setFail_num(F.toString());
        importExcelRes.setImportExcelErrorListRes(arrayList);
        if(F==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    asyncaddPersonRadix(id,tokenContext,map,stCommonUnit,network);
                }
            }).start();
        }
        return importExcelRes;
    }
    public void asyncaddPersonRadix(String id, TokenContext tokenContext, Map<Integer, Map<Integer, Object>> map,List<StCommonUnit> stCommonUnit,CAccountNetwork network){
        HashMap<String, String> KeyValue = new HashMap<>();
        ArrayList indiAcctSetPostList = new ArrayList();
        SimpleDateFormat ym = new SimpleDateFormat("yyyy/MM");
        KeyValue.put("DWMC", String.valueOf(map.get(2).get(1)));
        KeyValue.put("DWZH", String.valueOf(map.get(2).get(5)));
        //String str = map.get(2).get(8).toString(); ym.format(new Date(str))
        KeyValue.put("SXNY", map.get(2).get(8).toString());
        String TotalNum = "PerRadix_TotalNum"+id;
        String SucNum = "PerRadix_SucNum"+id;
        String Mes = "PerRadix_Mes"+id;
        Integer S = 0;
        PersonRadixPost personRadixPost = new PersonRadixPost();
        JedisCluster redis = null;
        try {
            redis = redisCache.getJedisCluster();
            for (int i = 4; i <= map.size(); i++) {
                String v = String.valueOf(map.get(i).get(0));
                if (v == null || v.length() <= 0) {
                    break;
                }
                KeyValue.put("XuHao", String.valueOf(map.get(i).get(0)));
                KeyValue.put("GRZH", String.valueOf(map.get(i).get(1)));
                KeyValue.put("XingMing", String.valueOf(map.get(i).get(2)));
                KeyValue.put("ZJHM", String.valueOf(map.get(i).get(3)));
                KeyValue.put("DWYJCE", String.valueOf(map.get(i).get(4)));
                KeyValue.put("GRYJCE", String.valueOf(map.get(i).get(5)));
                KeyValue.put("YJCE", String.valueOf(map.get(i).get(6)));
//                KeyValue.put("JZNY", String.valueOf(map.get(i).get(5)));
                KeyValue.put("TZQJCJS", String.valueOf(map.get(i).get(7)));
                KeyValue.put("TZHJCJS", String.valueOf(map.get(i).get(8)));
                PersonRadixPostJCJSTZXX JCJSTZXX = new PersonRadixPostJCJSTZXX();
                personRadixPost.setDWZH(KeyValue.get("DWZH").replace(" ", ""));
                personRadixPost.setCZLX("0");
                personRadixPost.setSXNY(KeyValue.get("SXNY").trim().replace(" ", ""));
                JCJSTZXX.setGRZH(KeyValue.get("GRZH").replace(" ", ""));
                JCJSTZXX.setDWYJCE(KeyValue.get("DWYJCE").replace(" ", ""));
                JCJSTZXX.setGRYJCE(KeyValue.get("GRYJCE").replace(" ", ""));
                JCJSTZXX.setYCJE(KeyValue.get("YJCE").replace(" ", ""));
                JCJSTZXX.setTZQGRJCJS(KeyValue.get("TZQJCJS").replace(" ", ""));
                JCJSTZXX.setTZHGRJCJS(KeyValue.get("TZHJCJS").replace(" ", ""));
                indiAcctSetPostList.add(JCJSTZXX);
                personRadixPost.setJCJSTZXX(indiAcctSetPostList);
                S++;
            }
            if (S != 0) {
                ArrayList<Map> list = new ArrayList();
                ArrayList<Map> list_arr = new ArrayList();
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("id", "011201");
                map1.put("name", "缴存基数变更证明");
                map1.put("modle", "01");
                map1.put("business", "12");
                map1.put("required", "true");
                map1.put("operation", "个人基数调整");
                map1.put("data", list_arr);
                list.add(map1);
                String blzl = gson.toJson(list);
                personRadixPost.setBLZL(blzl);
                redis.setex(TotalNum,3600,"0");
                redis.setex(SucNum,3600,"0");
                redis.setex(Mes,3600,"正在导入");
                CommonMessage result = batchExcelData(tokenContext, personRadixPost, stCommonUnit, network);
                if (result != null && result.getMessage().equals("Success") && result.getCode() != null) {
                    redis.setex(SucNum,3600,"1");
                    redis.setex(Mes,3600,"导入成功");
                    redis.setex(TotalNum,3600,"1");
                    redis.close();
                }else{
                    redis.setex(Mes,3600,"导入失败");
                }
            }
        } catch (Exception e) {
            redis.setex(Mes,3600,"导入失败");
        }
    }
    @Override
    public PageResNew<ListPersonRadixResRes> getPersonRadixnew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pageSize, String action) {
        HashMap<String, Object> search_map = new HashMap<>();
        search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.基数调整.getCode());
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
            stCollectionUnitBusinessDetailsPage = collection_unit_business_details.listWithMarker(search_map, !StringUtil.notEmpty(KSSJ) ? null : simpleall.parse(KSSJ),
                !StringUtil.notEmpty(JSSJ) ? null : simpleall.parse(JSSJ),
                "created_at", Order.DESC, null, null, marker, Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
//                            criteria.createAlias("unit", "unit");
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
        PageResNew<ListPersonRadixResRes> pageres = new PageResNew<>();
        ArrayList<ListPersonRadixResRes> listPersonRadixResRes = new ArrayList<>();
        ListPersonRadixResRes personRadixResRes = null;
        for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : stCollectionUnitBusinessDetails) {

            personRadixResRes = new ListPersonRadixResRes();
            try {
                personRadixResRes.setId(collectionUnitBusinessDetails.getId());
                personRadixResRes.setYWZT(collectionUnitBusinessDetails.getExtension().getStep());
                personRadixResRes.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());
                personRadixResRes.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
                personRadixResRes.setSLSJ(simpleall.format(collectionUnitBusinessDetails.getExtension().getSlsj()));
            } catch (Exception e) {
            }
            try {
                personRadixResRes.setDWMC(collectionUnitBusinessDetails.getUnit().getDwmc());
                personRadixResRes.setJZNY(simpleM.format(simpleData.parse(collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().getJzny())));
            } catch (Exception e) {
                List<StCommonUnit> stCommonUnit = common_unit.list(new HashMap<String, Object>() {{
                    if (StringUtil.notEmpty(collectionUnitBusinessDetails.getDwzh()))
                        this.put("dwzh", collectionUnitBusinessDetails.getDwzh());
                }}, null, null, null, null, null, null);
                if (stCommonUnit.size() > 0) {
                    personRadixResRes.setDWMC(stCommonUnit.get(0).getDwmc());
                    try {
                        personRadixResRes.setJZNY(stCommonUnit.get(0).getCollectionUnitAccount().getJzny() == null ? null : simpleM.format(simpleData.parse(stCommonUnit.get(0).getCollectionUnitAccount().getJzny())));
                    } catch (Exception ex) {
                    }
                }

            }
            personRadixResRes.setYWLSH(collectionUnitBusinessDetails.getYwlsh());
            personRadixResRes.setDWZH(collectionUnitBusinessDetails.getDwzh());
            listPersonRadixResRes.add(personRadixResRes);
        }
        pageres.setResults(action,listPersonRadixResRes);
        return pageres;

    }

    //获取个人基数调整数据
    public final PersonRadixExcelRes getPersonRadixdata(TokenContext tokenContext, String DWZH,String sxny) {
//        HashMap<String,String> hashMap = new HashMap<>();
        PersonRadixExcelRes personRadixExcelRes = new PersonRadixExcelRes();
//        GetPersonRadixBeforeRes getPersonRadixBeforeRes = autoPersonRadix(tokenContext, DWZH);
        AssertUtils.isTrue(!ComUtils.isEmpty(DWZH),"请先选择单位！");
        StCommonUnit unit = commonUnitDAO.getUnit(DWZH);
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = getPersonRadixList(unit,DWZH,sxny);

        AutoUnitAcctActionRes getUnitAcctActionAuto = unitAcctCommon.getUnitAcctActionAuto(DWZH);
        personRadixExcelRes.setDWGJXX(getUnitAcctActionAuto.getDWGJXX());
        personRadixExcelRes.setSXNY(sxny);
        personRadixExcelRes.setJCJSTZXX(list);

        return personRadixExcelRes;
    }

    @Override
    public GetPersonRadixBeforeRes autoPersonRadix(TokenContext tokenContext, String dwzh, String sxny) {
        AssertUtils.isTrue(!ComUtils.isEmpty(dwzh),"请先选择单位！");
        StCommonUnit unit = commonUnitDAO.getUnit(dwzh);
        Date date = checkSxny(unit,sxny);

        InventoryMessage inventory2 = unitDepositInventory.getInventory2(dwzh, date);
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = getPersonRadixList(inventory2);

        GetPersonRadixBeforeRes getPersonRadixBeforeRes = new GetPersonRadixBeforeRes();
        getPersonRadixBeforeRes.setDWZH(unit.getDwzh());//单位账户
        getPersonRadixBeforeRes.setDWMC(unit.getDwmc());//单位名称
        getPersonRadixBeforeRes.setJBRXM(unit.getJbrxm());//经办人姓名
        getPersonRadixBeforeRes.setJBRZJLX(unit.getJbrzjlx());//经办人类型
        getPersonRadixBeforeRes.setJBRZJHM(unit.getJbrzjhm());//经办人号码
        getPersonRadixBeforeRes.setDWJCBL(ComUtils.divMoney(inventory2.getDWJCBL(),"100"));      //取当月清册里面的
        getPersonRadixBeforeRes.setGRJCBL(ComUtils.divMoney(inventory2.getGRJCBL(),"100"));      //取当月清册里面的


        BigDecimal dwyjcehj = BigDecimal.ZERO;
        BigDecimal gryjcehj = BigDecimal.ZERO;
        BigDecimal yjcehj =  BigDecimal.ZERO;
        for (GetPersonRadixBeforeResJCJSTZXX row : list) {
            gryjcehj = gryjcehj.add(new BigDecimal(row.getGRYJCE()));
            dwyjcehj = dwyjcehj.add(new BigDecimal(row.getDWYJCE()));
            yjcehj = yjcehj.add(new BigDecimal(row.getYJCE()));
        }


        getPersonRadixBeforeRes.setJCJSTZXX(list);//集合
        getPersonRadixBeforeRes.setGRYJCEHJ(gryjcehj.toString());
        getPersonRadixBeforeRes.setDWYJCEHJ(dwyjcehj.toString());
        getPersonRadixBeforeRes.setYJCEHJ(yjcehj.toString());

        String sxny2 = ComUtils.parseToString(date, "yyyy-MM");
        getPersonRadixBeforeRes.setSXNY(sxny2);
        getPersonRadixBeforeRes.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));

        return getPersonRadixBeforeRes;
    }

    private ArrayList<GetPersonRadixBeforeResJCJSTZXX> getPersonRadixList(StCommonUnit unit,String dwzh, String sxny) {
        Date date = checkSxny(unit,sxny);

        InventoryMessage inventory2 = unitDepositInventory.getInventory2(dwzh, date);
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> result = getPersonRadixList(inventory2);
        return result;
    }

    private ArrayList<GetPersonRadixBeforeResJCJSTZXX> getPersonRadixList(InventoryMessage inventory2) {
        ArrayList<InventoryDetail> qcxq = inventory2.getQCXQ();
        AssertUtils.isTrue(qcxq == null || qcxq.size() != 0,"该单位中没有个人信息");
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = getRadixsDetail(qcxq);
        return list;
    }

    private Date checkSxny(StCommonUnit unit, String SXNY) {
        String dwyhjny = BusUtils.getDWYHJNY(unit);
        Date yhjny = ComUtils.parseToDate(dwyhjny, "yyyy-MM");
        if(ComUtils.isEmpty(SXNY)){
            return yhjny;
        }else{
            Date sxny = ComUtils.parseToDate(SXNY, "yyyy-MM");
            AssertUtils.isTrue(sxny.compareTo(yhjny) >= 0,"生效年月应大于等于单位的应汇缴年月！");
            return sxny;
        }
    }

    /**
     * 排除封存的人，只显示正常缴存的人员
     */
    private ArrayList<GetPersonRadixBeforeResJCJSTZXX> getRadixsDetail(ArrayList<InventoryDetail> qcxq) {
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> result = new ArrayList<>();
        for(InventoryDetail detail : qcxq){
            if("01".equals(detail.getGRZHZT())){    //排除封存的人，只显示正常缴存的人员
                GetPersonRadixBeforeResJCJSTZXX view = new GetPersonRadixBeforeResJCJSTZXX();
                view.setXingMing(detail.getXingMing());
                view.setGRZH(detail.getGRZH());
                view.setZJHM(detail.getZJHM());
                view.setTZQGRJCJS(detail.getGRJCJS());
                view.setGRYJCE(detail.getGRYJCE());
                view.setDWYJCE(detail.getDWYJCE());
                view.setYJCE(detail.getYJCE());
                view.setJZNY("");   //待处理
                result.add(view);
            }
        }
        return result;
    }
}
