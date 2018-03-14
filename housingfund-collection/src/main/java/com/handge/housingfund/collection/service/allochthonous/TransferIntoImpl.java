package com.handge.housingfund.collection.service.allochthonous;

import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransOutInfoIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransOutInfoOut;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.collection.allochthonous.model.*;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferFinalInterface;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferIntoInterface;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Data_MISS;
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;

@Component
public class TransferIntoImpl implements TransferIntoInterface {
    private static Logger logger = LogManager.getLogger(TransferIntoImpl.class);
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;

    @Autowired
    private ICCollectionAllochthounousTransferViceDAO icCollectionAllochthounousTransferViceDAO;

    @Autowired
    private ICCollectionAllochthounousTransferProcessDAO icCollectionAllochthounousTransferProcessDAO;

    @Autowired
    private IStateMachineService iStateMachineService;

    @Autowired
    private ISaveAuditHistory saveAuditHistory;

    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Autowired
    private ITransfer iTransfer;

    @Autowired
    private IStCommonPersonDAO commonPersonDAO;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

    @Autowired
    @Qualifier(value = "final.final")
    private TransferFinalInterface transferFinalInterface;

    @Autowired
    private ICBankCenterInfoDAO icBankCenterInfoDAO;

    @Autowired
    private ISMSCommon ismsCommon;

    /**
     * 办理转移-转移接续转入
     *
     * @param LXHBH     联系函编号
     * @param ZGXM      职工姓名
     * @param ZJHM      证件号码
     * @param zhuangTai 状态 （0：账户信息审核通过 1：账户信息审核不通过 2：协商中 3：系统处理中 4：正常办结 5：账户信息已录）
     * @param ZCZXMC    转出中心名称
     */
    public HandleTransferListModle handleTransferList(TokenContext tokenContext, final String LXHBH, final String ZGXM, final String ZJHM, final String zhuangTai, final String ZCZXMC, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize) {
        System.out.println("办理转移-转移接续转入");
        PageRes pageRes = new PageRes();
        int pageno = 1;
        int pagesize = 30;
        try {
            if (StringUtil.notEmpty(pageNo)) pageno = Integer.parseInt(pageNo);
            if (StringUtil.notEmpty(pageSize)) pagesize = Integer.parseInt(pageSize);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码或每页条数");
        }
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(LXHBH)) this.put("allochthounousTransferVice.LXHBH", LXHBH);
            if (StringUtil.notEmpty(ZGXM)) this.put("allochthounousTransferVice.ZGXM", ZGXM);
            if (StringUtil.notEmpty(ZJHM)) this.put("allochthounousTransferVice.ZJHM", ZJHM);
            if (StringUtil.notEmpty(ZCZXMC)) this.put("allochthounousTransferVice.ZCGJJZXMC", ZCZXMC);
        }}).betweenDate(timeTransform(KSSJ, JSSJ)[0], timeTransform(KSSJ, JSSJ)[1]).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
                if (StringUtil.notEmpty(zhuangTai) && !CollectionBusinessStatus.所有.getName().equals(zhuangTai)) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step", zhuangTai));
                }
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转入.getCode()));

                if (CollectionBusinessStatus.所有.getName().equals(zhuangTai)){
                        criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(
                        CollectionBusinessStatus.正常办结.getName(), CollectionBusinessStatus.协商后业务办结.getName(),
                        CollectionBusinessStatus.转入撤销业务办结.getName(), CollectionBusinessStatus.转出失败业务办结.getName(),
                        CollectionBusinessStatus.联系函审核通过.getName(), CollectionBusinessStatus.联系函确认接收.getName(),
                        CollectionBusinessStatus.协商中.getName(), CollectionBusinessStatus.账户信息审核通过.getName(),
                        CollectionBusinessStatus.转出审核不通过.getName())));
                }
            }
        }).pageOption(pageRes, pagesize, pageno).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (false) {
            return ModelUtils.randomModel(HandleTransferListModle.class);
        }

        return new HandleTransferListModle() {
            {
                this.setNextPageNo(pageRes.getNextPageNo() + ""/*下一页*/);
                this.setCurrentPage(pageRes.getCurrentPage() + ""/*当前页码*/);
                this.setPageSize(pageRes.getPageSize() + ""/*当前页码数据条数*/);
                this.setTotalCount(pageRes.getTotalCount() + ""/*总条数*/);
                this.setPageCount(pageRes.getPageCount() + ""/*总页数*/);
                this.setResults(new ArrayList<HandleTransferListModleResults>() {
                    {
                        for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 : stCollectionPersonalBusinessDetails) {
                            this.add(new HandleTransferListModleResults() {
                                {
                                    this.setYWLSH(stCollectionPersonalBusinessDetails1.getYwlsh()/*业务流水号*/);
                                    this.setLXHBH(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getLXHBH()/*联系函编号*/);
                                    this.setZGXM(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZGXM()/*职工姓名*/);
                                    this.setZJHM(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZJHM()/*证件号码*/);
                                    this.setZhuangTai(stCollectionPersonalBusinessDetails1.getExtension().getStep()/*状态*/);
                                    this.setZCFKXX(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getFKXX()/*转出反馈信息*/);
                                    this.setZCGJJZXMC(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZCGJJZXMC()/*转出公积金中心名称*/);
                                    this.setZYJE(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZYJE() == null ? null : stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZYJE().toString()/*转移金额*/);
                                    this.setXGZDWMC(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getXGZDWMC()/*现工作单位名称*/);
                                    this.setXGRZFGJJZH(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getXZFGJJZH()/*现个人住房公积金账号*/);
                                    this.setLXDSCRQ(DateUtil.date2Str1(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getLXDSCRQ(), "yyyy-MM-dd"/*联系单生成日期*/));
                                }
                            });
                        }
                    }
                }/**/);
            }
        };
    }

    @Override
    public TransferIntoBusinessDetailsModle transferIntoBusinessDetails(TokenContext tokenContext, String YWLSH) {
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        return  new TransferIntoBusinessDetailsModle() {{
            for (CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess : stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses()) {
                this.add(new TransferIntoBusinessDetailsModleInner() {{
                    this.setLXHBH(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHBH()/*联系函编号*/);
                    this.setCaoZuo(cCollectionAllochthounousTransferProcess.getCaoZuo()/*操作*/);
                    this.setCZJG(cCollectionAllochthounousTransferProcess.getCZJG()/*操作机构*/);
                    this.setCZRY(cCollectionAllochthounousTransferProcess.getCZRY()/*操作人员*/);
                    this.setCZSJ(DateUtil.date2Str(cCollectionAllochthounousTransferProcess.getCZSJ(), "yyyy-MM-dd HH:mm:ss")/*操作时间*/);
                    this.setCZYJ(cCollectionAllochthounousTransferProcess.getCZYJ()/*操作意见*/);
                    this.setCZHZT(cCollectionAllochthounousTransferProcess.getCZHZT()/*操作后状态*/);
                }});
            }
        }};
    }


    /**
     * 联系函详情-转移接续转入
     *
     * @param YWLSH 业务流水号
     */
    public TransferIntoDetailsModle transferIntoDetails(TokenContext tokenContext, final String YWLSH) {
        System.out.println("联系函详情-转移接续转入");

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        CCollectionAllochthounousTransferVice cCollectionAllochthounousTransferVice = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice();


        if (false) {
            return ModelUtils.randomModel(TransferIntoDetailsModle.class);
        }

        return new TransferIntoDetailsModle() {
            {
                {
                    this.setYWLSH(YWLSH);//业务流水号

                    this.setLXHBH(cCollectionAllochthounousTransferVice.getLXHBH());//联系函编号

                    this.setLXDSCRQ(DateUtil.date2Str(cCollectionAllochthounousTransferVice.getLXDSCRQ(), "yyyy-MM-dd"));//联系单生成日期

                    this.setTurnOutMsg(new TransferIntoDetailsModleTurnOutMsg() {{
                        this.setZCJGBH(cCollectionAllochthounousTransferVice.getZCJGBH());//转出机构编号

                        this.setZCGJJZXMC(cCollectionAllochthounousTransferVice.getZCGJJZXMC());//转出中心名称

                        this.setYGRZFGJJZH(cCollectionAllochthounousTransferVice.getYGRZFGJJZH());//原个人住房公积金账号

                        this.setYGZDWMC(cCollectionAllochthounousTransferVice.getYGZDWMC());//原工作单位名称
                    }});//

                    this.setWorkerMsg(new TransferIntoDetailsModleWorkerMsg() {{
                        this.setZJLX(cCollectionAllochthounousTransferVice.getZJLX());//证件类型

                        this.setZJHM(cCollectionAllochthounousTransferVice.getZJHM());//证件号码

                        this.setZGXM(cCollectionAllochthounousTransferVice.getZGXM());//职工姓名

                        this.setSJHM(cCollectionAllochthounousTransferVice.getSJHM());//手机号码

                        this.setXGRZFGJJZH(cCollectionAllochthounousTransferVice.getXZFGJJZH());//现个人住房公积金账号

                        this.setXGZDWMC(cCollectionAllochthounousTransferVice.getXGZDWMC());//现工作单位名称
                    }});//

                    this.setTurnIntoMsg(new TransferIntoDetailsModleTurnIntoMsg() {{
                        this.setZRGJJZXZJZHSSYHMC(cCollectionAllochthounousTransferVice.getZRZJZHYHMC());//转入公积金中心资金账户所属银行名称

                        this.setZRGJJZXZJZH(cCollectionAllochthounousTransferVice.getZRZJZH());//转入公积金中心资金账号

                        this.setZRGJJZXZJZHHM(cCollectionAllochthounousTransferVice.getZRZJZHHM());//转入公积金中心资金账号户名

                        this.setLXDHHCZ(cCollectionAllochthounousTransferVice.getLXDHHCZ());//联系电话/传真
                    }});//

                    this.setBLZL(StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getBLZL()) ? cCollectionAllochthounousTransferVice.getBLZL() : "");//办理资料
                }
            }
        };
    }

    @Override
    public CommonResponses transferIntoFinish(TokenContext tokenContext, String YWLSH, TransferIntoFinishModle body) {
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        //判断员工是否存在
        StCommonPerson stCommonPerson = DAOBuilder.instance(commonPersonDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("zjhm", stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
                criteria.add(Restrictions.not( Restrictions.in("collectionPersonalAccount.grzhzt",PersonAccountStatus.合并销户.getCode(),PersonAccountStatus.外部转出销户.getCode(),PersonAccountStatus.提取销户.getCode())));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCommonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该员工不存在或已销户");
        }
        List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = new ArrayList<>();
        CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

        cCollectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
        cCollectionAllochthounousTransferProcess.setCZRY(stCollectionPersonalBusinessDetails.getExtension().getCzy());
        cCollectionAllochthounousTransferProcess.setCZSJ(new Date());
        cCollectionAllochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
        cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());

        SingleTransOutInfoIn singleTransOutInfoIn = new SingleTransOutInfoIn();
        singleTransOutInfoIn.setOrConNum(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHBH());
        CenterHeadIn centerHeadIn = new CenterHeadIn(YWLSH,
                icBankCenterInfoDAO.getCenterNodeByNum(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZCJGBH()),
                tokenContext.getUserInfo().getCZY());
        String step = stCollectionPersonalBusinessDetails.getExtension().getStep();
        if (step.equals(CollectionBusinessStatus.账户信息审核通过.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.账户信息复核通过.getCode())) {

            cCollectionAllochthounousTransferProcess.setCaoZuo("转入方发起正常办结操作");
            cCollectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.正常办结.getName());
        }
        else if (step.equals(CollectionBusinessStatus.转出审核不通过.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.转出审核不通过.getCode())) {

            cCollectionAllochthounousTransferProcess.setCaoZuo("转入方发起失败办结操作");
            cCollectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.转出失败业务办结.getName());
        }
        else if (step.equals(CollectionBusinessStatus.协商中.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.协商中.getCode())) {

            cCollectionAllochthounousTransferProcess.setCaoZuo("转入方发起协商办结操作");
            cCollectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.协商后业务办结.getName());
        }
        else {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "当前业务状态或联系单状态不允许办结");
        }
        cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setProcesses(cCollectionAllochthounousTransferProcesses);

        singleTransOutInfoIn.setTxFunc("2");
        singleTransOutInfoIn.setTranRstCode("0");
        singleTransOutInfoIn.setCenterHeadIn(centerHeadIn);
        singleTransOutInfoIn.setOrConNum(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHBH());
        singleTransOutInfoIn.setTranAmt(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZYJE() == null ? null : stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZYJE() + "");
        singleTransOutInfoIn.setOrAcctAmt(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE() == null ? null : stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE() + "");
        singleTransOutInfoIn.setYinter(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE() == null ? null : stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE() + "");

        SingleTransOutInfoOut singleTransOutInfoOut = null;
        try {
            singleTransOutInfoOut = iTransfer.sendMsg(singleTransOutInfoIn);
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        if ("0".equals(singleTransOutInfoOut.getCenterHeadOut().getTxStatus())) {
            if (step.equals(CollectionBusinessStatus.账户信息审核通过.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.账户信息复核通过.getCode())) {
                stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.正常办结.getName());
                stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.正常办结.getCode());
            }
            if (step.equals(CollectionBusinessStatus.转出审核不通过.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.转出审核不通过.getCode())) {
                stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转出失败业务办结.getName());
                stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.转出失败业务办结.getCode());
            }
            if (step.equals(CollectionBusinessStatus.协商中.getName()) && stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT().equals(AllochthonousStatus.协商中.getCode())) {
                stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.协商后业务办结.getName());
                stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.协商后业务办结.getCode());
            }
            if (step.equals(CollectionBusinessStatus.账户信息审核通过.getName()) || step.equals(CollectionBusinessStatus.协商中.getName())) {
                stCollectionPersonalBusinessDetails.setFse(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE()==null?new BigDecimal("0"):stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE());
                stCollectionPersonalBusinessDetails.setFslxe(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE()==null?new BigDecimal("0"):stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE());
                //直接做一笔单位外部转入
                doDirectUnitTransfer(tokenContext,stCollectionPersonalBusinessDetails,stCommonPerson.getUnit().getDwzh());

                stCommonPerson.getCollectionPersonalAccount().setGrzhye(stCommonPerson.getCollectionPersonalAccount().getGrzhye().add(new BigDecimal(singleTransOutInfoIn.getTranAmt())));
                stCommonPerson.getUnit().getCollectionUnitAccount().setDwzhye(stCommonPerson.getUnit().getCollectionUnitAccount().getDwzhye().add(new BigDecimal(singleTransOutInfoIn.getTranAmt())));
                DAOBuilder.instance(commonPersonDAO).entity(stCommonPerson).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                try{
                Calendar c = Calendar.getInstance();
                ismsCommon.sendSingleSMSWithTemp(stCommonPerson.getSjhm(), SMSTemp.异地转移接续转入.getCode(),
                        new ArrayList<String>() {{
                            this.add(stCommonPerson.getXingMing());
                            this.add(String.valueOf(c.get(Calendar.MONTH)+1));
                            this.add(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
                            this.add(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZCGJJZXMC());
                            this.add(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZYJE().abs().toString());
                            this.add(stCommonPerson.getCollectionPersonalAccount().getGrzhye().toString());
                        }}
                );
                }catch (Exception e){
                    logger.info("异地转移接续转入短信发送失败");
                }
                }
            DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            return new CommonResponses() {{
                this.setStatus("success");
                this.setYWLSH(YWLSH);
            }};
        }

        return new CommonResponses() {{
            this.setStatus("fail");
            this.setYWLSH(YWLSH);
        }};
    }

    /**
     * 申请受理-转移接续转入
     *
     * @param LXHBH  联系函编号
     * @param ZGXM   职工姓名
     * @param ZJHM   证件号码
     * @param ZCZXMC 转出中心名称
     */
    public TransferIntoListModle transferIntoList(TokenContext tokenContext, final String LXHBH, final String ZGXM, final String ZJHM, final String ZCZXMC,final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNo, final String pageSize) {
        System.out.println("申请受理-转移接续转入");
        PageRes pageRes = new PageRes();
        int pageno = 1;
        int pagesize = 30;
        try {
            if (StringUtil.notEmpty(pageNo)) pageno = Integer.parseInt(pageNo);
            if (StringUtil.notEmpty(pageSize)) pagesize = Integer.parseInt(pageSize);
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码或每页条数");
        }
        List<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(LXHBH)) this.put("allochthounousTransferVice.LXHBH", LXHBH);
            if (StringUtil.notEmpty(ZGXM)) this.put("allochthounousTransferVice.ZGXM", ZGXM);
            if (StringUtil.notEmpty(ZJHM)) this.put("allochthounousTransferVice.ZJHM", ZJHM);
            if (StringUtil.notEmpty(ZCZXMC)) this.put("allochthounousTransferVice.ZCGJJZXMC", ZCZXMC);
        }}).betweenDate(timeTransform(KSSJ, JSSJ)[0], timeTransform(KSSJ, JSSJ)[1]).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");
                if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
                criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转入.getCode()));
                if(StringUtil.notEmpty(ZhuangTai)&&ZhuangTai.equals(CollectionBusinessStatus.所有.getName())) {
                    criteria.add(Restrictions.or(
                            Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.新建.getName(),
                                    CollectionBusinessStatus.审核不通过.getName()),
                            Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step",
                                    CollectionBusinessStatus.待某人审核.getName())));
                }
                if(StringUtil.notEmpty(ZhuangTai)&&!ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())&&!ZhuangTai.equals(CollectionBusinessStatus.所有.getName())) {
                    criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.step",ZhuangTai));
                } else if(CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)) {
                    criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.待某人审核.getName()));
                }
            }
        }).pageOption(pageRes, pagesize, pageno).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (false) {
            return ModelUtils.randomModel(TransferIntoListModle.class);
        }

        return new TransferIntoListModle() {
            {
                ArrayList<TransferIntoListModleResults> arrayList = new ArrayList<>();
                for (StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 : stCollectionPersonalBusinessDetails) {
                    TransferIntoListModleResults transferIntoListModleResults = new TransferIntoListModleResults() {{
                        this.setYWLSH(stCollectionPersonalBusinessDetails1.getYwlsh()/*业务流水号*/);
                        this.setLXHBH(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getLXHBH()/*联系函编号*/);
                        this.setZGXM(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZGXM()/*职工姓名*/);
                        this.setZJHM(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZJHM()/*证件号码*/);
                        this.setSJHM(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getSJHM()/*手机号码*/);
                        this.setZCGJJZXMC(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getZCGJJZXMC()/*转出中心名称*/);
                        this.setYGRZFGJJZH(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getYGRZFGJJZH()/*原个人住房公积金账号*/);
                        this.setYDWMC(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getYGZDWMC()/*原工作单位名称*/);
                        this.setXGRZFGJJZH(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getXZFGJJZH()/*现个人住房公积金账号*/);
                        this.setXDWMC(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getXGZDWMC()/*现单位名称*/);
                        this.setLXDSCRQ(DateUtil.date2Str(stCollectionPersonalBusinessDetails1.getAllochthounousTransferVice().getLXDSCRQ(), "yyyy-MM-dd")/*联系单生成日期*/);
                        this.setZhuangTai(stCollectionPersonalBusinessDetails1.getExtension().getStep()/*状态 (0：联系函新建 1：联系函已录未审 2：联系函审核失败 3：联系函审核通过 4：确认接收联系函 5：驳回联系函)*/);
                    }};
                    arrayList.add(transferIntoListModleResults);
                }
                this.setResults(arrayList);

                this.setCurrentPage(pageRes.getCurrentPage() + "");

                this.setNextPageNo(pageRes.getNextPageNo() + "");

                this.setPageCount(pageRes.getPageCount() + "");

                this.setTotalCount(pageRes.getTotalCount() + "");

                this.setPageSize(pageRes.getPageSize() + "");
            }
        };
    }

    //向转出方发起协商
    @Override
    public CommonResponses transferIntoNegotiation(TokenContext tokenContext, String YWLSH, TransferIntoNegotiationModle body) {
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if(!CollectionBusinessStatus.账户信息审核通过.getName().equals(stCollectionPersonalBusinessDetails.getExtension().getStep())){
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH,"只有账户信息审核通过的状态下才允许协商");
        }
        SingleTransOutInfoIn singleTransOutInfoIn = new SingleTransOutInfoIn();
        CenterHeadIn centerHeadIn = new CenterHeadIn(YWLSH,
                icBankCenterInfoDAO.getCenterNodeByNum(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getZCJGBH()),
                tokenContext.getUserInfo().getCZY());
        singleTransOutInfoIn.setOrConNum(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHBH());
        singleTransOutInfoIn.setCenterHeadIn(centerHeadIn);
        singleTransOutInfoIn.setTxFunc("1");
        singleTransOutInfoIn.setTranRstCode(null);
        SingleTransOutInfoOut singleTransOutInfoOut = null;
        try {
            singleTransOutInfoOut = iTransfer.sendMsg(singleTransOutInfoIn);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        if (singleTransOutInfoOut != null && singleTransOutInfoOut.getCenterHeadOut().getTxStatus().equals("0")) {
            stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.协商中.getName());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.协商中.getCode());

            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("向转出方发起协商");
            cCollectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");//操作机构
            cCollectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());//操作员
            cCollectionAllochthounousTransferProcess.setCZSJ(new Date());
            cCollectionAllochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
            cCollectionAllochthounousTransferProcess.setCZHZT("协商中");
            cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        }else {
            return new CommonResponses(){{
                this.setStatus("Fail");
                this.setYWLSH(YWLSH);
            }};
        }
        DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new CommonResponses(){{
            this.setStatus("success");
            this.setYWLSH(YWLSH);
        }};
    }


    /**
     * 录入联系函-转移接续转入
     *
     * @param type 类型（0：保存 1：提交）
     * @param body
     */
    public CommonResponses transferIntoPost(TokenContext tokenContext, final String type, final TransferIntoPostModle body) {

        System.out.println("录入联系函-转移接续转入");

        if (type.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.异地转移接续转入.getCode(), body.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        StCommonPerson stCommonPerson = DAOBuilder.instance(commonPersonDAO).searchFilter(new HashMap<String,Object>(){{
            this.put("zjhm",body.getWorkerMsg().getZJHM());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        final StCommonUnit unit = stCommonPerson.getUnit();

        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }
        if(stCommonPerson != null && stCommonPerson.getCollectionPersonalAccount() != null && stCommonPerson.getCollectionPersonalAccount().getXhrq() != null) {
               throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH,"当前账户已销户，不能办理转移接续");
        }
        CCollectionAllochthounousTransferVice cCollectionAllochthounousTransferVice = new CCollectionAllochthounousTransferVice();
        cCollectionAllochthounousTransferVice.setZCJGBH(body.getTurnOutMsg().getZCJGBH());//转出机构编号
        cCollectionAllochthounousTransferVice.setZCGJJZXMC(body.getTurnOutMsg().getZCGJJZXMC());//*转出中心名称：
        cCollectionAllochthounousTransferVice.setYGRZFGJJZH(body.getTurnOutMsg().getYGRZFGJJZH());//*原个人住房公积金账号：
        cCollectionAllochthounousTransferVice.setYGZDWMC(body.getTurnOutMsg().getYGZDWMC());//*原工作单位名称：
        cCollectionAllochthounousTransferVice.setZJLX(body.getWorkerMsg().getZJLX());//*证件类型：
        cCollectionAllochthounousTransferVice.setZJHM(body.getWorkerMsg().getZJHM());//*证件号码：
        cCollectionAllochthounousTransferVice.setZGXM(body.getWorkerMsg().getZGXM());//职工姓名：
        cCollectionAllochthounousTransferVice.setSJHM(body.getWorkerMsg().getSJHM());//手机号码：
        cCollectionAllochthounousTransferVice.setXZFGJJZH(body.getWorkerMsg().getXGRZFGJJZH());//现个人住房公积金账号：
        cCollectionAllochthounousTransferVice.setXGZDWMC(body.getWorkerMsg().getXGZDWMC());//现工作单位名称：
        cCollectionAllochthounousTransferVice.setZRZJZHYHMC(body.getTurnIntoMsg().getZRGJJZXZJZHSSYHMC());//*转入公积金中心资金账户所属银行名称：
        cCollectionAllochthounousTransferVice.setZRZJZH(body.getTurnIntoMsg().getZRGJJZXZJZH());//*转入公积金中心资金账号：
        cCollectionAllochthounousTransferVice.setZRZJZHHM(body.getTurnIntoMsg().getZRGJJZXZJZHHM());//转入公积金中心资金账号户名：
        cCollectionAllochthounousTransferVice.setLXDHHCZ(body.getTurnIntoMsg().getLXDHHCZ());//*联系电话/传真：
        cCollectionAllochthounousTransferVice.setLXDSCRQ(new Date());//联系单生成日期
        cCollectionAllochthounousTransferVice.setBLZL(body.getBLZL());//办理资料
        cCollectionAllochthounousTransferVice.setZRJGBH("C52240");//转入机构编号
        cCollectionAllochthounousTransferVice.setZRGJJZXMC("毕节市住房公积金管理中心");//转入机构名称

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();
        stCollectionPersonalBusinessDetails.setAllochthounousTransferVice(cCollectionAllochthounousTransferVice);
        stCollectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.外部转入.getCode());
        cCollectionAllochthounousTransferVice.setGrywmx(stCollectionPersonalBusinessDetails);
        CCollectionPersonalBusinessDetailsExtension cCollectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
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
        cCollectionPersonalBusinessDetailsExtension.setYwwd(network);
        cCollectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());
        cCollectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.初始状态.getName());
        cCollectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.外部转入.getCode());
        stCollectionPersonalBusinessDetails.setExtension(cCollectionPersonalBusinessDetailsExtension);
        if (type.equals("1")) {
            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = new ArrayList<>();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("转入方录入联系函");
            cCollectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
            cCollectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
            cCollectionAllochthounousTransferProcess.setCZSJ(new Date());
            cCollectionAllochthounousTransferProcess.setCZYJ(null);
            cCollectionAllochthounousTransferProcess.setCZHZT("联系函已录未核");
            cCollectionAllochthounousTransferProcess.setTransferVice(cCollectionAllochthounousTransferVice);
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setProcesses(cCollectionAllochthounousTransferProcesses);
        }
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(stCollectionPersonalBusinessDetails1.getYwlsh(), tokenContext, CollectionBusinessType.外部转入.getName(), "新建");
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.通过.getEvent());

                    this.put("1", Events.提交.getEvent());

                }}.get(type), new TaskEntity(stCollectionPersonalBusinessDetails1.getYwlsh(), stCollectionPersonalBusinessDetails1.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        tokenContext.getUserInfo().getCZY(), stCollectionPersonalBusinessDetails1.getExtension().getBeizhu(), BusinessSubType.归集_转入个人接续.getSubType(), BusinessType.Collection, stCollectionPersonalBusinessDetails1.getExtension().getYwwd().getId()),
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
                            CCollectionPersonalBusinessDetailsExtension cectionPersonalBusinessDetailsExtension = stCollectionPersonalBusinessDetails1.getExtension();
                            cectionPersonalBusinessDetailsExtension.setStep(next);

                            stCollectionPersonalBusinessDetails1.setExtension(cectionPersonalBusinessDetailsExtension);
                            if (StringUtil.isIntoReview(next, null))
                                stCollectionPersonalBusinessDetails1.getExtension().setDdsj(new Date());

                            instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails1).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.联系函审核通过.getName())) {
                                transferFinalInterface.intoFinal(tokenContext, stCollectionPersonalBusinessDetails1.getYwlsh());
                            }
                        }

                    }
                });
        //在途验证
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetail = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("allochthounousTransferVice.ZJHM", body.getWorkerMsg().getZJHM());
            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转入.getCode());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.协商后业务办结.getName(), CollectionBusinessStatus.转入撤销业务办结.getName(),
                        CollectionBusinessStatus.正常办结.getName(), CollectionBusinessStatus.转出失败业务办结.getName()))));
                criteria.add(Restrictions.ne("ywlsh", stCollectionPersonalBusinessDetails1.getYwlsh()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetail != null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process);
        }
        if (type.equals("1")) {
            checkParam(stCollectionPersonalBusinessDetails1);//必传参数检验
        }
        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        return new CommonResponses() {
            {
                this.setStatus("success");
                this.setYWLSH(stCollectionPersonalBusinessDetails1.getYwlsh());
            }
        };
    }

    /**
     * 申请修改-转移接续转入
     *
     * @param type 类型（0：保存 1：提交）
     * @param body
     */
    public CommonResponses transferIntoPut(TokenContext tokenContext, final String type, final TransferIntoPutModle body) {
        System.out.println("申请修改-转移接续转入");
        if (type.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.异地转移接续转入.getCode(), body.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        StCommonPerson stCommonPerson = DAOBuilder.instance(commonPersonDAO).searchFilter(new HashMap<String,Object>(){{
            this.put("zjhm",body.getWorkerMsg().getZJHM());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if(stCommonPerson != null && stCommonPerson.getCollectionPersonalAccount() != null && stCommonPerson.getCollectionPersonalAccount().getXhrq() != null) {
            throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH,"当前账户已销户，不能办理转移接续");
        }
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", body.getYWLSH());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if (!tokenContext.getUserInfo().getCZY().equals(stCollectionPersonalBusinessDetails.getExtension().getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied,"该业务("+body.getYWLSH()+")不是由您受理的，不能进行修改操作");
        }
        CCollectionAllochthounousTransferVice cCollectionAllochthounousTransferVice = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice();
        cCollectionAllochthounousTransferVice.setZCJGBH(body.getTurnOutMsg().getZCJGBH());//转出机构编号
        cCollectionAllochthounousTransferVice.setZCGJJZXMC(body.getTurnOutMsg().getZCGJJZXMC());//*转出中心名称：
        cCollectionAllochthounousTransferVice.setYGRZFGJJZH(body.getTurnOutMsg().getYGRZFGJJZH());//*原个人住房公积金账号：
        cCollectionAllochthounousTransferVice.setYGZDWMC(body.getTurnOutMsg().getYGZDWMC());//*原工作单位名称：
        cCollectionAllochthounousTransferVice.setZJLX(body.getWorkerMsg().getZJLX());//*证件类型：
        cCollectionAllochthounousTransferVice.setZJHM(body.getWorkerMsg().getZJHM());//*证件号码：
        cCollectionAllochthounousTransferVice.setZGXM(body.getWorkerMsg().getZGXM());//职工姓名：
        cCollectionAllochthounousTransferVice.setSJHM(body.getWorkerMsg().getSJHM());//手机号码：
        cCollectionAllochthounousTransferVice.setXZFGJJZH(body.getWorkerMsg().getXGRZFGJJZH());//现个人住房公积金账号：
        cCollectionAllochthounousTransferVice.setXGZDWMC(body.getWorkerMsg().getXGZDWMC());//现工作单位名称：
        cCollectionAllochthounousTransferVice.setZRZJZHYHMC(body.getTurnIntoMsg().getZRGJJZXZJZHSSYHMC());//*转入公积金中心资金账户所属银行名称：
        cCollectionAllochthounousTransferVice.setZRZJZH(body.getTurnIntoMsg().getZRGJJZXZJZH());//*转入公积金中心资金账号：
        cCollectionAllochthounousTransferVice.setZRZJZHHM(body.getTurnIntoMsg().getZRGJJZXZJZHHM());//转入公积金中心资金账号户名：
        cCollectionAllochthounousTransferVice.setLXDHHCZ(body.getTurnIntoMsg().getLXDHHCZ());//*联系电话/传真：
        cCollectionAllochthounousTransferVice.setBLZL(body.getBLZL());//办理资料
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
        if (type.equals("1")) {
            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = new ArrayList<>();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("转入方录入联系函");
            cCollectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
            cCollectionAllochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
            cCollectionAllochthounousTransferProcess.setCZSJ(new Date());
            cCollectionAllochthounousTransferProcess.setCZYJ(null);
            cCollectionAllochthounousTransferProcess.setCZHZT("联系函已录未核");
            cCollectionAllochthounousTransferProcess.setTransferVice(cCollectionAllochthounousTransferVice);
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setProcesses(cCollectionAllochthounousTransferProcesses);
        }
        stCollectionPersonalBusinessDetails.getExtension().setYwwd(network);

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails1 = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (type.equals("1")) {
            checkParam(stCollectionPersonalBusinessDetails1);
            saveAuditHistory.saveNormalBusiness(body.getYWLSH(), tokenContext, CollectionBusinessType.外部转入.getName(), "修改");
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

                    this.put("0", Events.保存.getEvent());

                    this.put("1", Events.通过.getEvent());

                }}.get(type), new TaskEntity(stCollectionPersonalBusinessDetails1.getYwlsh(), stCollectionPersonalBusinessDetails1.getExtension().getStep(), new HashSet<String>(tokenContext.getRoleList()),
                        tokenContext.getUserInfo().getCZY(), stCollectionPersonalBusinessDetails1.getExtension().getBeizhu(), BusinessSubType.归集_转入个人接续.getSubType(), BusinessType.Collection, stCollectionPersonalBusinessDetails1.getExtension().getYwwd().getId()),
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
                            stCollectionPersonalBusinessDetails1.getExtension().setStep(next);

                            if (StringUtil.isIntoReview(next, null))
                                stCollectionPersonalBusinessDetails1.getExtension().setDdsj(new Date());

                            instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails1).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (next.equals(CollectionBusinessStatus.联系函审核通过.getName()))
                                transferFinalInterface.intoFinal(tokenContext, stCollectionPersonalBusinessDetails1.getYwlsh());
                        }
                    }
                });
        //在途验证
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetail = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("allochthounousTransferVice.ZJHM", body.getWorkerMsg().getZJHM());
            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转入.getCode());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.协商后业务办结.getName(), CollectionBusinessStatus.转入撤销业务办结.getName(),
                        CollectionBusinessStatus.正常办结.getName(), CollectionBusinessStatus.转出失败业务办结.getName()))));
                criteria.add(Restrictions.ne("ywlsh", stCollectionPersonalBusinessDetails1.getYwlsh()));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetail != null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process);
        }

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        return new CommonResponses() {
            {
                this.setMsg("success");
                this.setStatus("200");
            }
        };
    }
    //直接做一笔单位外部转入业务
    private void doDirectUnitTransfer(TokenContext tokenContext,StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails,String dwzh){
        StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();
        stCollectionUnitBusinessDetails.setFse(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE()==null?new BigDecimal("0"):stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE());
        stCollectionUnitBusinessDetails.setFslxe(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE()==null?new BigDecimal("0"):stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE());
        stCollectionUnitBusinessDetails.setFsrs(new BigDecimal("0"));
        stCollectionUnitBusinessDetails.setDwzh(dwzh);
        stCollectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.外部转入.getCode());
        stCollectionUnitBusinessDetails.setJzrq(new Date());
        stCollectionUnitBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
        CCollectionUnitBusinessDetailsExtension cCollectionUnitBusinessDetailsExtension =new CCollectionUnitBusinessDetailsExtension();
        cCollectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.外部转入.getCode());
        cCollectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());
        cCollectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());
        cCollectionUnitBusinessDetailsExtension.setYwwd(cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()));
        cCollectionUnitBusinessDetailsExtension.setBeizhu(stCollectionPersonalBusinessDetails.getYwlsh());
        cCollectionUnitBusinessDetailsExtension.setSlsj(new Date());
        stCollectionUnitBusinessDetails.setExtension(cCollectionUnitBusinessDetailsExtension);
        DAOBuilder.instance(collectionUnitBusinessDetailsDAO).entity(stCollectionUnitBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {//插入单位明细表
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }


    private void checkParam(StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails) {
        if (stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS);
        }
        CCollectionAllochthounousTransferVice cCollectionAllochthounousTransferVice = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice();
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZCJGBH())) {
            throw new ErrorException(Data_MISS, "转出机构编号");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZCGJJZXMC())) {
            throw new ErrorException(Data_MISS, "转出公积金中心名称");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getYGRZFGJJZH())) {
            throw new ErrorException(Data_MISS, "原个人住房公积金账号");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getYGZDWMC())) {
            throw new ErrorException(Data_MISS, "原工作单位名称");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZJLX())) {
            throw new ErrorException(Data_MISS, "证件类型");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZJHM())) {
            throw new ErrorException(Data_MISS, "证件号码");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZRZJZHYHMC())) {
            throw new ErrorException(Data_MISS, "转入公积金中心资金账户所属银行名称");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getZRZJZH())) {
            throw new ErrorException(Data_MISS, "转入公积金中心资金账号");
        }
        if (!StringUtil.notEmpty(cCollectionAllochthounousTransferVice.getLXDHHCZ())) {
            throw new ErrorException(Data_MISS, "联系电话/传真");
        }
    }
}
