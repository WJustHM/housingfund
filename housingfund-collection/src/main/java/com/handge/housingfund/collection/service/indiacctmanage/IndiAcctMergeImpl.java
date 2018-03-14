package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.ResponseUtils;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctMerge;
import com.handge.housingfund.common.service.enumeration.ErrorEnumeration;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ResponseEntity;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Liujuhao on 2017/7/4.
 */

@Component
@Deprecated
public class IndiAcctMergeImpl implements IndiAcctMerge {

    //业务流水号不一样了，通过外键关系查询得到
    private Logger logger = LogManager.getLogger(IndiAcctMergeImpl.class);
    // 个人账户信息副表
    @Autowired
    private ICCollectionIndividualAccountMergeViceDAO collection_individual_account_merge_vice;
    // 个人业务明细
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collection_personal_business_details;
    // 个人信息
    @Autowired
    private IStCommonPersonDAO common_person;
    // 归集个人账户
    @Autowired
    private IStCollectionPersonalAccountDAO collection_Personal_Account;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;


//    @Autowired
//    private IStateMachineService iStateMachineService;

    private HashMap<String, Object> search_map = null;

    /**
     * 获取个人账户合并信息列表   不再检查swagger待测
     * //TODO   联表查询空对象问题
     *
     * @return 个人账户合并信息列表
     * 1.根据姓名、证件号码、个人账号、单位名称、状态参数，查询“个人账户信息”、“个人缴存信息”相关的表；
     * 2.异常处理，统一待定
     * 3.封装数据库返回数据
     * 4.返回结果(查询合并业务表信息,)
     */
    @Override
    public ResponseEntity getMergeInfo(final String DWMC, final String ZhuangTai, final String XingMing, final String ZJHM, final String GRZH, final String CZMC) {
        //参数格式检查\根据业务类型，获取该业务下的账户列表
        if (CZMC.equals("") || CZMC == null) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Parameter_MISS);
        }

        //格式检查以及条件查询
        search_map = new HashMap<>();
        if (GRZH != null) {
            search_map.put("grzh", GRZH);
        }
        if (DWMC != null) {
            search_map.put("unit.dwmc", DWMC);
        }
        if (XingMing != null) {
            search_map.put("person.xingMing", XingMing);
        }
        if (ZJHM != null) {
            search_map.put("person.zjhm", ZJHM);
        }
        if (ZhuangTai != null) {
            search_map.put("cCollectionPersonalBusinessDetailsExtension.ywzt", ZhuangTai);
        }

        // TODO: 2017/7/28 有分页隐患（杜俊杰）
        List<StCollectionPersonalBusinessDetails> collectionPersonalBusinessDetails = collection_personal_business_details.list(
                search_map, null, null, null, null, null, null);
        ListOperationAcctsResRes indiAcctMergeInfoRes = null;
        ArrayList<ListOperationAcctsResRes> indiAcctMergeInfo = new ArrayList<>();
        for (StCollectionPersonalBusinessDetails collectionBusiness : collectionPersonalBusinessDetails) {
            indiAcctMergeInfoRes = new ListOperationAcctsResRes();
            //TODO 对象待定
            indiAcctMergeInfoRes.setYWLSH(collectionBusiness.getYwlsh());//业务流水号
            indiAcctMergeInfoRes.setGRZH(collectionBusiness.getGrzh());//个人账号
            try {
                indiAcctMergeInfoRes.setXingming(collectionBusiness.getPerson().getXingMing());//姓名
                indiAcctMergeInfoRes.setZJHM(collectionBusiness.getPerson().getZjhm());//证件号码
                indiAcctMergeInfoRes.setGRZHYE(collectionBusiness.getPerson().getCollectionPersonalAccount().getGrzhye()+"");
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_NOT_MATCH);
            }
            try {
                indiAcctMergeInfoRes.setJZNY(collectionBusiness.getUnit().getCollectionUnitAccount().getJzny().toString());
                indiAcctMergeInfoRes.setDWMC(collectionBusiness.getUnit().getDwmc());//单位名称
            } catch (NullPointerException e) {
                logger.debug(e.getMessage());
            }
            indiAcctMergeInfoRes.setZhuangTai(collectionBusiness.getExtension().getStep());//状态
            if (collectionBusiness.getExtension().getSlsj() != null) {
                indiAcctMergeInfoRes.setSLSJ(collectionBusiness.getExtension().getSlsj().toString());//受理时间
            }
            indiAcctMergeInfo.add(indiAcctMergeInfoRes);
        }
        ListOperationAcctsRes data = new ListOperationAcctsRes();
        data.setRes(indiAcctMergeInfo);
        return ResponseUtils.buildCommonEntityResponse(data);
    }

    /**
     * 新增个人账户合并     google advance  swagger
     *  2017
     *
     * @param addIndiAcctMerge 封装了个人账户合并信息的对象
     * @return 成功/失败
     * 1.入副表
     * 2.入业务表和业务拓展表（办结之后再入基础信息表）
     */
    @Override
    public ResponseEntity addAcctMerge(IndiAcctMergePost addIndiAcctMerge) {
        //字段检查  0保存（新建）1提交（待审核）
        if (addIndiAcctMerge.getCZLX() == null ||
                addIndiAcctMerge.getBLZH() == null ||
                addIndiAcctMerge.getHBZHList() == null ||
                (!addIndiAcctMerge.getCZLX().equals("0") && !addIndiAcctMerge.getCZLX().equals("1"))) {
            return ResponseUtils.buildParametersErrorResponse();
        }

        Date date = new Date();
        List<StCommonPerson> comm_persons = common_person.list(new HashMap<String, Object>() {{
            this.put("grzh", addIndiAcctMerge.getBLZH());
        }}, null, null, null, null, null, null);//(个人账号)

        if (comm_persons.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"个人账号不存在");
        }

        //一 、  入副表
        CCollectionIndividualAccountMergeVice mergevice = new CCollectionIndividualAccountMergeVice();
        mergevice.setBlzh(addIndiAcctMerge.getBLZH());//保留账号
        mergevice.setBlzl(addIndiAcctMerge.getBLZL());//办理资料
        mergevice.setCzmc(CollectionBusinessType.合并.getCode());//操作名称（71合并）
        mergevice.setCzy(addIndiAcctMerge.getCZY());//操作员
        try {
            mergevice.setDwzh(comm_persons.get(0).getUnit().getDwzh());//单位账号
        } catch (NullPointerException e) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"缺少关联的单位账号");
        }
        mergevice.setGrzh(addIndiAcctMerge.getBLZH());//个人账号
        StringBuffer sb = new StringBuffer();
        for (String ll : addIndiAcctMerge.getHBZHList()) {
            sb.append(ll + ":");
        }
        mergevice.setHbzh(sb.replace(sb.length() - 1, sb.length(), "").toString());//合并账号
        mergevice.setSlsj(date);//受理时间
        mergevice.setXingMing(addIndiAcctMerge.getXingMing());//姓名
        mergevice.setYwwd(addIndiAcctMerge.getYWWD());//业务网点
        mergevice.setZjhm(addIndiAcctMerge.getZJHM());//证件号码
        mergevice.setZjlx(addIndiAcctMerge.getZJLX());//证件类型

        //二、 入业务表和业务拓展表   不再检查字段720
        CCollectionPersonalBusinessDetailsExtension extension = new CCollectionPersonalBusinessDetailsExtension();
        extension.setBlzl(addIndiAcctMerge.getBLZL());//办理资料
        extension.setCzmc(CollectionBusinessType.合并.getCode());//操作名称
        extension.setCzy(addIndiAcctMerge.getCZY());//操作员
        extension.setJbrxm(addIndiAcctMerge.getJBRXM());//JBRXM   缴存信息中获取，通过单位账号
        extension.setJbrzjhm(addIndiAcctMerge.getJBRZJHM());//JBRZJHM
        extension.setJbrzjlx(addIndiAcctMerge.getJBRZJLX());//JBRZJLX
        extension.setSlsj(date);//受理时间
//        extension.setYwwd(addIndiAcctMerge.getYWWD());//业务网点
        //TODO ZHJX

        //入个人业务明细表（主表）  不再检查字段
        StCollectionPersonalBusinessDetails buniess = new StCollectionPersonalBusinessDetails();
        buniess.setGrzh(addIndiAcctMerge.getBLZH());//保留账号
        //外键关系
        buniess.setExtension(extension);//添加副表对象
        buniess.setPerson(comm_persons.get(0));//个人账号外键信息
        buniess.setUnit(comm_persons.get(0).getUnit());//单位账号外键信息
        mergevice.setGrywmx(buniess);
        buniess.setIndividualAccountMergeVice(mergevice);
        //入库操作
        String ID = collection_personal_business_details.save(buniess);

        //修改step状态
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = collection_personal_business_details.list(new HashMap<String, Object>() {{
            this.put("id", ID);
        }}, null, null, null, null, null, null);

//        StateMachineUtils.Operation opretaion = null;
//        if ("0".equals(addIndiAcctMerge.getCZLX())) {
//            opretaion = StateMachineUtils.Operation.合并个人账户_初始状态_保存;
//        } else {
//            opretaion = StateMachineUtils.Operation.合并个人账户_初始状态_提交;
//        }
        for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : stCollectionPersonalBusinessDetails) {

//            StateMachineUtils.updateState(this.iStateMachineService, collectionPersonalBusinessDetails.getYwlsh(), opretaion, new StateMachineUtils.StateChangeHandler() {
//                @Override
//                public void onStateChange(boolean succeed, String next, Exception e) {
//                    if (succeed) {
//                        CCollectionPersonalBusinessDetailsExtension cectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();
//                        cectionPersonalBusinessDetailsExtension.setStep(next);
//                        if ("0".equals(addIndiAcctMerge.getCZLX())) {
//                            extension.setYwzt(CollectionBusinessStatus.新建.getCode());//业务表状态
//                        } else {
//                            extension.setYwzt(CollectionBusinessStatus.待审核.getCode());
//                        }
//                        collectionPersonalBusinessDetails.setExtension(cectionPersonalBusinessDetailsExtension);
//                        try {
//                            collection_personal_business_details.update(collectionPersonalBusinessDetails);
//                        } catch (Exception ex) {
//                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                        }
//                    }
//                }
//            });
        }

        return ResponseUtils.buildCommonEntityResponse(new AddIndiAcctMergeRes() {{
            this.setStatus("success");
        }});
    }

    /**
     * 修改个人账户合并    swagger正常，不再测试
     *  2017
     *
     * @param YWLSH           业务流水号
     * @param reIndiAcctMerge 封装了个人账户合并修改信息的对象
     * @return 成功/失败
     * 1.入副表
     * 2.入业务表和业务拓展表（办结之后再入基础信息表）
     */
    @Override
    public ResponseEntity reAcctMerge(String YWLSH, IndiAcctMergePut reIndiAcctMerge) {
        //参数为空判断
        if (YWLSH == null || reIndiAcctMerge.getCZLX() == null ||
                reIndiAcctMerge.getBLZH() == null ||
                reIndiAcctMerge.getHBZHList() == null ||
                (!reIndiAcctMerge.getCZLX().equals("0") && !reIndiAcctMerge.getCZLX().equals("1"))) {
            return ResponseUtils.buildParametersFormatErrorResponse();
        }

        //二、存入业务明细以及拓展表
        List<StCollectionPersonalBusinessDetails> collectionPersonalBusiness = collection_personal_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                }}, null, null, null, null, null, null);
        if (collectionPersonalBusiness.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"不存在此业务记录");
        }

        List<StCommonPerson> common_persons = common_person.list(new HashMap<String, Object>() {{
            this.put("grzh", reIndiAcctMerge.getBLZH());
        }}, null, null, null, null, null, null);

        if (common_persons.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"个人账号不存在");
        }

//        StateMachineUtils.Operation opretaion = null;
//        if ("0".equals(reIndiAcctMerge.getCZLX())) {
//            opretaion = StateMachineUtils.Operation.合并个人账户_新建_保存;
//        } else {
//            opretaion = StateMachineUtils.Operation.合并个人账户_新建_提交;
//        }
        //个人业务表
        for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : collectionPersonalBusiness) {
            //副表
            try {
                CCollectionIndividualAccountMergeVice collectionIndividualAccountMergeVice = collectionPersonalBusinessDetails.getIndividualAccountMergeVice();
                collectionIndividualAccountMergeVice.setBlzh(reIndiAcctMerge.getBLZH());//保留账号
                collectionIndividualAccountMergeVice.setCzy(reIndiAcctMerge.getCZY()); //操作员
                collectionIndividualAccountMergeVice.setYwwd(reIndiAcctMerge.getYWWD());//业务网点
                StringBuilder sb = new StringBuilder();
                for (String ll : reIndiAcctMerge.getHBZHList()) {
                    sb.append(ll + ":");
                }
                collectionIndividualAccountMergeVice.setHbzh(sb.replace(sb.length() - 1, sb.length(), "").toString());//合并账号
                collectionIndividualAccountMergeVice.setBlzl(reIndiAcctMerge.getBLZL());//办理资料

                //拓展表
                CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();
                collectionPersonalBusinessDetailsExtension.setJbrxm(reIndiAcctMerge.getJBRXM());//JBRXM
                collectionPersonalBusinessDetailsExtension.setJbrzjlx(reIndiAcctMerge.getJBRZJLX());//JBRZJLX
                collectionPersonalBusinessDetailsExtension.setJbrzjhm(reIndiAcctMerge.getJBRZJHM());//JBRZJHM
                collectionPersonalBusinessDetailsExtension.setBlzl(reIndiAcctMerge.getBLZL());//办理资料
                collectionPersonalBusinessDetailsExtension.setCzy(reIndiAcctMerge.getCZY());//操作员
//                collectionPersonalBusinessDetailsExtension.setYwwd(reIndiAcctMerge.getYWWD());//业务网店

                //业务明细表
                collectionPersonalBusinessDetails.setGrzh(reIndiAcctMerge.getBLZH());//保留账号
                collectionPersonalBusinessDetails.setPerson(common_persons.get(0));
                collectionPersonalBusinessDetails.setUnit(common_persons.get(0).getUnit());

                //关联关系
                collectionIndividualAccountMergeVice.setGrywmx(collectionPersonalBusinessDetails);//副表插入业务记录
                collectionPersonalBusinessDetails.setIndividualAccountMergeVice(collectionIndividualAccountMergeVice);//添加副表信息

                //入库
//                StateMachineUtils.updateState(this.iStateMachineService, YWLSH, opretaion, new StateMachineUtils.StateChangeHandler() {
//                    @Override
//                    public void onStateChange(boolean succeed, String next, Exception e) {
//                        if (succeed) {
//
//                            collectionPersonalBusinessDetailsExtension.setStep(next);//step
//                            if ("0".equals(reIndiAcctMerge.getCZLX())) {
//                                collectionPersonalBusinessDetailsExtension.setYwzt(CollectionBusinessStatus.新建.getCode());//业务表状态
//                            } else {
//                                collectionPersonalBusinessDetailsExtension.setYwzt(CollectionBusinessStatus.待审核.getCode());
//                            }
//                            collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
//                            try {
//                                collection_personal_business_details.update(collectionPersonalBusinessDetails);
//                            } catch (Exception ex) {
//                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                            }
//                        }
//                    }
//                });
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"个人账号不存在关联的单位账号");
            }
        }


        return ResponseUtils.buildCommonEntityResponse(new ReIndiAcctMergeRes() {{
            this.setStatus("200");
            this.setYWLSH(YWLSH);
        }});
    }

    /**
     * 查看个人账户合并详情   已测试-正常  不再测试，swagger正常
     *  2017
     *
     * @param YWLSH 业务流水号YWLSH
     * @return 封装了个人账户合并详情的对象
     * 1. 业务明细表贯通
     */
    @Override
    public ResponseEntity showAcctMerge(String YWLSH) {
        //参数检查
        if (YWLSH == null) {
            return ResponseUtils.buildParametersFormatErrorResponse();
        }

        //业务明细
        List<StCollectionPersonalBusinessDetails> buniessdetails = this.collection_personal_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                }}, null, null, null, null, null, null);

        if (buniessdetails.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"不存在此业务记录");
        }

        //一、保留账号字段信息 BLZH&HBZH
        GetIndiAcctMergeResBLZHXX showIndiAcctMergeBLZHXX = null;//BLZH
        ArrayList<GetIndiAcctMergeResFCZHXX> list = new ArrayList<>();//合并账号的集合
        GetIndiAcctMergeRes showIndiAcctMerge = null;
        for (StCollectionPersonalBusinessDetails bu : buniessdetails) {
            showIndiAcctMergeBLZHXX = new GetIndiAcctMergeResBLZHXX();
            try {
                showIndiAcctMergeBLZHXX.setHuMing(bu.getPerson().getXingMing());//户名
                showIndiAcctMergeBLZHXX.setGRZH(bu.getPerson().getGrzh());//个人账号
                showIndiAcctMergeBLZHXX.setZHZT(bu.getPerson().getCollectionPersonalAccount().getGrzhzt()); //个人账户状态
                showIndiAcctMergeBLZHXX.setYHZH(bu.getPerson().getCollectionPersonalAccount().getGrckzhhm()); //银行账号
                showIndiAcctMergeBLZHXX.setZHYEHJ(bu.getPerson().getCollectionPersonalAccount().getGrzhye() + ""); //个人账户余额
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }
            try {
                showIndiAcctMergeBLZHXX.setDWZH(bu.getUnit().getDwzh()); //单位账号
                showIndiAcctMergeBLZHXX.setDWMC(bu.getUnit().getDwmc()); //单位名称
                showIndiAcctMergeBLZHXX.setJZNY(bu.getUnit().getCollectionUnitAccount().getJzny() + "");//缴至年月，根据单位账号去单位账户信息里面
                //TODO 账户结息
                //TODO 账户本金
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }

            //合并账户
            GetIndiAcctMergeResFCZHXX showIndiAcctMergeHBZHXX = null;//每次值会被覆盖，不用再循环里重新初始化
            List<StCommonPerson> com = null;
            try {
                CCollectionIndividualAccountMergeVice mergevi = bu.getIndividualAccountMergeVice();
                if (mergevi.getHbzh() != null) {
                    for (String ss : mergevi.getHbzh().split("\\:")) {
                        com = common_person.list(new HashMap<String, Object>() {{
                            this.put("grzh", ss);
                        }}, null, null, null, null, null, null);
                        if (com.size() <= 0) {
                            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
                        }
                        showIndiAcctMergeHBZHXX = new GetIndiAcctMergeResFCZHXX();
                        showIndiAcctMergeHBZHXX.setHuMing(com.get(0).getXingMing()); //户名
                        showIndiAcctMergeHBZHXX.setGRZH(com.get(0).getGrzh());//个人账号
                        try {
                            showIndiAcctMergeHBZHXX.setDWZH(com.get(0).getUnit().getDwzh());//单位账号
                            showIndiAcctMergeHBZHXX.setDWMC(com.get(0).getUnit().getDwmc());//单位名称
                            showIndiAcctMergeHBZHXX.setJZNY(com.get(0).getUnit().getCollectionUnitAccount().getJzny() + "");//缴至年月，单位信息
                        } catch (NullPointerException e) {
                            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
                        }
                        try {
                            //TODO 账号结息
                            //TODO 账户本金
                            showIndiAcctMergeHBZHXX.setZHZT(com.get(0).getCollectionPersonalAccount().getGrzhzt());//账户状态
                            showIndiAcctMergeHBZHXX.setYHZH(com.get(0).getCollectionPersonalAccount().getGrckzhhm());//银行账户
                            showIndiAcctMergeHBZHXX.setZHYEHJ(com.get(0).getCollectionPersonalAccount().getGrzhye() + "");//账号余额合计
                        } catch (NullPointerException e) {
                            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
                        }
                        list.add(showIndiAcctMergeHBZHXX);
                    }
                }
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }
            //三、返回最终结果
            showIndiAcctMerge = new GetIndiAcctMergeRes();
            try {
                showIndiAcctMerge.setZJHM(bu.getIndividualAccountMergeVice().getZjhm());//证件号码
                showIndiAcctMerge.setZJLX(bu.getIndividualAccountMergeVice().getZjlx());//证件类型
                showIndiAcctMerge.setXingMing(bu.getIndividualAccountMergeVice().getXingMing());//姓名
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }
            try {
                showIndiAcctMerge.setJBRZJLX(bu.getExtension().getJbrzjlx());//经办人证件类型
                showIndiAcctMerge.setJBRXM(bu.getExtension().getJbrxm());//经办人姓名
//                showIndiAcctMerge.setYWWD(bu.getExtension().getYwwd());//业务网点
                showIndiAcctMerge.setJBRZJHM(bu.getExtension().getJbrzjhm());//经办人证件号码
                showIndiAcctMerge.setCZY(bu.getExtension().getCzy());///操作员
                showIndiAcctMerge.setBLZL(bu.getExtension().getBlzl());//办理资料
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }
            showIndiAcctMerge.setFCZHXX(list);//封存账号信息
            showIndiAcctMerge.setBLZHXX(showIndiAcctMergeBLZHXX);//保留账号信息
        }

        return ResponseUtils.buildCommonEntityResponse(showIndiAcctMerge);
    }

    /**
     * 合并个人账户（2）查询业务   已测试-正常 不再测试
     *  2017
     */
    @Override
    public ResponseEntity AutoIndiAcctMerge(String XingMing, String ZJLX, String ZJHM) {

        //参数检查
        if (XingMing == null || ZJHM == null || ZJLX == null) {
            return ResponseUtils.buildParametersErrorResponse();
        }

        //1 查询个人信息表根据姓名和个人账户
        AutoIndiAcctMergeRes autoIndiAcctMergerRes = new AutoIndiAcctMergeRes();
        ArrayList<AutoIndiAcctMergeResRes> list = new ArrayList<>();
        AutoIndiAcctMergeResRes infolist = null;
        List<StCommonPerson> commomPersons = common_person.list(new HashMap<String, Object>() {{
            this.put("xingMing", XingMing);
            this.put("zjlx", ZJLX);
            this.put("zjhm", ZJHM);
        }}, null, null, null, null, null, null);

        if (commomPersons.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
        }

        for (StCommonPerson com : commomPersons) {
            infolist = new AutoIndiAcctMergeResRes();
            infolist.setGRZH(com.getGrzh());  //个人账户
            infolist.setHuMing(com.getXingMing());  //户名
            try {
                infolist.setDWZH(com.getUnit().getDwzh());   //单位账号
                infolist.setDWMC(com.getUnit().getDwmc()); //单位名称
                infolist.setJZNY(com.getUnit().getCollectionUnitAccount().getJzny() + "");//缴至年月，去个人信息表中，再到单位信息，再到单位账号信息查询
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }

            try {
                infolist.setYHZH(com.getCollectionPersonalAccount().getGrckzhhm()); //银行账号
                infolist.setZHZT(com.getCollectionPersonalAccount().getGrzhzt()); //个人账户状态
                infolist.setZHBJ(com.getCollectionPersonalAccount().getGrzhye() + "");//账户本金,个人账户余额
                //TODO 账户结息 (具体业务再用)
                infolist.setZHYE(com.getCollectionPersonalAccount().getGrzhye() + ""); //个人账户余额
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }
            list.add(infolist);
        }
        autoIndiAcctMergerRes.setRes(list);
        return ResponseUtils.buildCommonEntityResponse(autoIndiAcctMergerRes);
    }

    /**
     * 打印回执单，和详情一样
     *  2017
     */
    @Override
    public ResponseEntity headAcctMerge(String YWLSH) {
        //参数检查
        if (YWLSH == null || YWLSH.equals("")) {
            return ResponseUtils.buildParametersFormatErrorResponse();
        }

        //业务明细
        List<StCollectionPersonalBusinessDetails> buniessdetails = this.collection_personal_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                }}, null, null, null, null, null, null);

        if (buniessdetails.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"不存在此业务记录");
        }

        //一、保留账号字段信息   720完毕
        HeadIndiAcctMergeResBLZHXX showIndiAcctMergeBLZHXX = null;
        HeadIndiAcctMergeRes showIndiAcctMerge = null;
        for (StCollectionPersonalBusinessDetails bu : buniessdetails) {
            showIndiAcctMergeBLZHXX = new HeadIndiAcctMergeResBLZHXX();
            try {
                showIndiAcctMergeBLZHXX.setHuMing(bu.getPerson().getXingMing());//户名
                showIndiAcctMergeBLZHXX.setGRZH(bu.getPerson().getGrzh());//个人账号
                showIndiAcctMergeBLZHXX.setZHZT(bu.getPerson().getCollectionPersonalAccount().getGrzhzt()); //个人账户状态
                showIndiAcctMergeBLZHXX.setYHZH(bu.getPerson().getCollectionPersonalAccount().getGrckzhhm()); //银行账号
                showIndiAcctMergeBLZHXX.setZHYEHJ(bu.getPerson().getCollectionPersonalAccount().getGrzhye().toString()); //个人账户余额
            } catch (NullPointerException e) {
                logger.debug(e.getMessage());
            }
            try {
                showIndiAcctMergeBLZHXX.setDWZH(bu.getUnit().getDwzh()); //单位账号
                showIndiAcctMergeBLZHXX.setDWMC(bu.getUnit().getDwmc()); //单位名称
                showIndiAcctMergeBLZHXX.setJZNY(bu.getUnit().getCollectionUnitAccount().getJzny().toString());//缴至年月，根据单位账号去单位账户信息里面
                //TODO 账户结息，基础表里没有
                //TODO 账户本金，基础表里没有
            } catch (NullPointerException e) {
                logger.debug(e.getMessage());
            }

            //封存账号信息
            HeadIndiAcctMergeResFCZHXX showIndiAcctMergeHBZHXX = null;//每次值会被覆盖，不用再循环里重新初始化
            ArrayList<HeadIndiAcctMergeResFCZHXX> list = new ArrayList<>();//合并账号的集合
            List<StCommonPerson> com = null;
            try {
                CCollectionIndividualAccountMergeVice mergevi = bu.getIndividualAccountMergeVice();
                if (mergevi.getHbzh() != null) {
                    for (String ss : mergevi.getHbzh().split("\\:")) {
                        com = common_person.list(new HashMap<String, Object>() {{
                            this.put("grzh", ss);
                        }}, null, null, null, null, null, null);
                        if (com.size() <= 0) {
                            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
                        }
                        showIndiAcctMergeHBZHXX = new HeadIndiAcctMergeResFCZHXX();
                        showIndiAcctMergeHBZHXX.setHuMing(com.get(0).getXingMing()); //户名
                        showIndiAcctMergeHBZHXX.setGRZH(com.get(0).getGrzh());//个人账号
                        try {
                            showIndiAcctMergeHBZHXX.setDWZH(com.get(0).getUnit().getDwzh());//单位账号
                            showIndiAcctMergeHBZHXX.setDWMC(com.get(0).getUnit().getDwmc());//单位名称
                            showIndiAcctMergeHBZHXX.setJZNY(com.get(0).getUnit().getCollectionUnitAccount().getJzny().toString());//缴至年月，单位信息
                        } catch (NullPointerException e) {
                            logger.debug(e.getMessage());
                        }
                        try {
                            //TODO 账号结息
                            //TODO 账户本金
                            showIndiAcctMergeHBZHXX.setZHZT(com.get(0).getCollectionPersonalAccount().getGrzhzt());//账户状态
                            showIndiAcctMergeHBZHXX.setYHZH(com.get(0).getCollectionPersonalAccount().getGrckzhhm());//银行账户
                            showIndiAcctMergeHBZHXX.setZHYEHJ(com.get(0).getCollectionPersonalAccount().getGrzhye().toString());//账号余额合计
                        } catch (NullPointerException e) {
                            logger.debug(e.getMessage());
                        }
                        list.add(showIndiAcctMergeHBZHXX);
                    }
                }
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            }

            //三、返回最终结果
            showIndiAcctMerge = new HeadIndiAcctMergeRes();
            try {
                showIndiAcctMerge.setZJHM(bu.getIndividualAccountMergeVice().getZjhm());//证件号码
                showIndiAcctMerge.setZJLX(bu.getIndividualAccountMergeVice().getZjlx());//证件类型
                showIndiAcctMerge.setXingMing(bu.getIndividualAccountMergeVice().getXingMing());//姓名
            } catch (NullPointerException e) {
                logger.debug(e.getMessage());
            }
            try {
                showIndiAcctMerge.setZJHM(bu.getExtension().getJbrzjhm());//经办人证件号码
                showIndiAcctMerge.setCZY(bu.getExtension().getCzy());///操作员
            } catch (NullPointerException e) {
                logger.debug(e.getMessage());
            }
            showIndiAcctMerge.setFCZHXX(list);//封存账号信息
            showIndiAcctMerge.setBLZHXX(showIndiAcctMergeBLZHXX);//保留账号信息
            showIndiAcctMerge.setRiQi(new Date().toString());//日期
        }

        return ResponseUtils.buildCommonEntityResponse(showIndiAcctMerge);
    }

    /**
     * 1. 改变合并账户状态（封存）
     * 2. 移动账户余额
     * // TODO: 2017/7/24 实现（杜俊杰）
     */
    @Override
    public ResponseEntity doAcctMerge(String YWLSH) {
        //参数检查
        if (YWLSH == null || YWLSH.equals("")) {
            return ResponseUtils.buildParametersFormatErrorResponse();
        }
        //业务明细
        List<StCollectionPersonalBusinessDetails> buniessdetails = this.collection_personal_business_details.list(
                new HashMap<String, Object>() {{
                    this.put("ywlsh", YWLSH);
                }}, null, null, null, null, null, null);
        if (buniessdetails.size() <= 0) {
            return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
        }
        //第一步，转移封存账号的钱,更改账户信息
        for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails : buniessdetails) {
            //第二部，改变被合并账号的状态，以及账户余额转移到保存账户里
            String[] HBZH = stCollectionPersonalBusinessDetails.getIndividualAccountMergeVice().getHbzh().split("\\:");
            BigDecimal HBZHMoney = new BigDecimal("0");
            for (String str : HBZH) {
                List<StCollectionPersonalAccount> stCommonPersonAcct = collection_Personal_Account.list(
                        new HashMap<String, Object>() {{
                            this.put("grzh", str);
                        }}, null, null, null, null, null, null);
                if (stCommonPersonAcct.size() <= 0) {
                    return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS,"个人账号信息不存在");
                }
                StCollectionPersonalAccount stCollectionPersonalAccountmove = stCommonPersonAcct.get(0);
                HBZHMoney = HBZHMoney.add(stCommonPersonAcct.get(0).getGrzhye());
                stCollectionPersonalAccountmove.setGrzhzt("02");
                stCollectionPersonalAccountmove.setGrzhye(new BigDecimal(0));
                try {
                    collection_Personal_Account.update(stCollectionPersonalAccountmove);
                } catch (Exception ex) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }

            //更新保留的个人账户余额
            StCollectionPersonalAccount stCollectionPersonalAccount = null;
            try {
                stCollectionPersonalAccount = stCollectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount();
                BigDecimal InitMoney = stCollectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrzhye();
                InitMoney = InitMoney.add(HBZHMoney);
                stCollectionPersonalAccount.setGrzhye(InitMoney);
                stCollectionPersonalAccount.setGrzhzt("01");

                collection_Personal_Account.update(stCollectionPersonalAccount);
            } catch (NullPointerException e) {
                return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Data_MISS);
            } catch (Exception ex) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }


            //状态刘基豪修改
//            //第二步、更改业务表状态
//            //业务拓展表
//            CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = null;
//            //副表
//            CCollectionIndividualAccountMergeVice collectionIndividualAccountMergeVice = null;
//            try {
//                collectionPersonalBusinessDetailsExtension = stCollectionPersonalBusinessDetails.getExtension();
//                collectionIndividualAccountMergeVice = stCollectionPersonalBusinessDetails.getIndividualAccountMergeVice();
//
//                stCollectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
//
//                stCollectionPersonalBusinessDetails.setIndividualAccountMergeVice(collectionIndividualAccountMergeVice);
//            } catch (NullPointerException e) {
//                return ResponseUtils.buildCommonErrorResponse(ReturnEnumeration.Data_MISS);
//            }
            try {
                collection_personal_business_details.update(stCollectionPersonalBusinessDetails);
            } catch (Exception ex) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        return ResponseUtils.buildCommonEntityResponse(new ReIndiAcctMergeRes() {{
            this.setYWLSH(YWLSH);
            this.setStatus("success");
        }});
    }

    public void doAccountMerge(String sourceDwzh,String desDwzh){

        StCommonUnit source_unit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("dwzh",sourceDwzh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        StCommonUnit des_unit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("dwzh",desDwzh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });



    }
}
