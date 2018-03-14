package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransOutInfoIn;
import com.handge.housingfund.common.service.bank.bean.transfer.TransInApplCancelIn;
import com.handge.housingfund.common.service.collection.enumeration.AllochthonousStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.service.trader.IAllochthounousBackCall;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Liujuhao on 2017/12/8.
 */

@SuppressWarnings({"SpellCheckingInspection", "SpringJavaAutowiredMembersInspection", "unused"})
public class AllochthounousBackCall implements IAllochthounousBackCall {
    private static Logger logger = LogManager.getLogger(AllochthounousBackCall.class);
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

    @Autowired
    private IStCommonPersonDAO commonPersonDAO;

    @Autowired
    private ICAccountNetworkDAO accountNetworkDAO;

    @Autowired
    private IVoucherManagerService voucherManagerService;

    @Autowired
    private ICBankCenterInfoDAO icBankCenterInfoDAO;

    @Autowired
    private ISMSCommon ismsCommon;


    @Override
    public void transferOutInputCallBack(SingleTransInApplIn singleTransOutInfoIn) {

        //region //必要参数声明&关系配置


        StCollectionPersonalBusinessDetails exist_collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("allochthounousTransferVice.LXHBH", singleTransOutInfoIn.getConNum());
            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (exist_collectionPersonalBusinessDetails != null && !CollectionBusinessStatus.联系函审核通过.getCode().equals(exist_collectionPersonalBusinessDetails.getAllochthounousTransferVice().getLXHZT())) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process, "转出联系函编号：" + singleTransOutInfoIn.getConNum());
        }

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = exist_collectionPersonalBusinessDetails != null ? exist_collectionPersonalBusinessDetails : new StCollectionPersonalBusinessDetails();


        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension() != null ? collectionPersonalBusinessDetails.getExtension() : new CCollectionPersonalBusinessDetailsExtension();
        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice() != null ? collectionPersonalBusinessDetails.getAllochthounousTransferVice() : new CCollectionAllochthounousTransferVice();
        collectionPersonalBusinessDetails.setAllochthounousTransferVice(collectionAllochthounousTransferVice);
        collectionAllochthounousTransferVice.setGrywmx(collectionPersonalBusinessDetails);

        List<CCollectionAllochthounousTransferProcess> list_process = (collectionAllochthounousTransferVice.getProcesses() != null && collectionAllochthounousTransferVice.getProcesses().size() != 0) ? collectionAllochthounousTransferVice.getProcesses() : new ArrayList<CCollectionAllochthounousTransferProcess>();
        collectionAllochthounousTransferVice.setProcesses(list_process);

        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.eq("zjhm", singleTransOutInfoIn.getIdNum()));

                criteria.createAlias("collectionPersonalAccount", "collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }

        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        String networkId = (commonPerson == null || commonPerson.getUnit() == null || commonPerson.getUnit().getExtension() == null || commonPerson.getUnit().getExtension().getKhwd() == null) ? "1" : commonPerson.getUnit().getExtension().getKhwd();

        CAccountNetwork network = DAOBuilder.instance(accountNetworkDAO).UID(networkId).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        //region //字段填充
        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.外部转出.getCode());
        collectionPersonalBusinessDetails.setPerson(commonPerson);
        collectionPersonalBusinessDetails.setUnit(commonPerson == null ? null : commonPerson.getUnit());
        collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.外部转出.getCode());
        collectionPersonalBusinessDetailsExtension.setYwwd(network);
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.联系函审核通过.getName());

        if (exist_collectionPersonalBusinessDetails == null) {

            CCollectionAllochthounousTransferProcess collectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

            collectionAllochthounousTransferProcess.setCaoZuo("转入方复核联系函");
            collectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));
            collectionAllochthounousTransferProcess.setCZRY(singleTransOutInfoIn.getCenterHeadIn().getOperNo());
            collectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransOutInfoIn.getCenterHeadIn().getSendDate() + singleTransOutInfoIn.getCenterHeadIn().getSendTime()));
            collectionAllochthounousTransferProcess.setCZYJ(singleTransOutInfoIn.getTranRstMsg(/* 反馈信息*/));
            collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.联系函复核通过.name());
            collectionAllochthounousTransferProcess.setTransferVice(collectionAllochthounousTransferVice);
            list_process.add(collectionAllochthounousTransferProcess);
        }
        collectionAllochthounousTransferVice.setLXHBH(singleTransOutInfoIn.getConNum(/* 原联系函编号(required), 要求使用转入申请的联系函编号*/));
        /**
         * 交易类型(required)
         * 0-新增信息转出
         * 1-修改转出信息
         * 2-转入确认办结
         * 0新增是用于新增的转移接续【转出发起】
         * 1修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】
         * 2转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】
         */

        collectionAllochthounousTransferVice.setZGXM(singleTransOutInfoIn.getEmpName(/* 职工姓名(required) */));
        collectionAllochthounousTransferVice.setZJLX(singleTransOutInfoIn.getDocType(/* 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他 */));
        collectionAllochthounousTransferVice.setZJHM(singleTransOutInfoIn.getIdNum(/* 职工证件号码(required) */));
        collectionAllochthounousTransferVice.setZRZJZH(singleTransOutInfoIn.getTranRefAcctNo()/* 转入公积金中心资金账号(required), 转入公积金中心资金接收账户的账号 */);
        collectionAllochthounousTransferVice.setZRZJZHHM(singleTransOutInfoIn.getTranRefAcctName()/* 转入公积金中心资金账号户名(required), 转入公积金中心资金接收账户户名 */);
        collectionAllochthounousTransferVice.setZRZJZHYHMC(singleTransOutInfoIn.getTranBank()/* 公积金中心委托业务办理银行, 公积金中心委托银行办理异地转移接续业务时填写, 参见银行代码表 */);
        collectionAllochthounousTransferVice.setLXDHHCZ(singleTransOutInfoIn.getTrenCenInfo()/* 转入公积金中心联系方式(required) */);
        collectionAllochthounousTransferVice.setLXDSCRQ(DateUtil.safeStr2Date("yyyyMMdd", singleTransOutInfoIn.getGenDate())/* 联系单生成日期(required), 格式: YYYYMMDD*/);
        collectionAllochthounousTransferVice.setZCJGBH(singleTransOutInfoIn.getTranOutUnitNo()/*转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码*/);
        collectionAllochthounousTransferVice.setZCGJJZXMC(singleTransOutInfoIn.getTranCenName()/* 转出公积金中心名称(required)*/);
        collectionAllochthounousTransferVice.setYGZDWMC(singleTransOutInfoIn.getSocUnitName()/*原工作单位名称(required)*/);
        collectionAllochthounousTransferVice.setYGRZFGJJZH(singleTransOutInfoIn.getSocRefAcctNo()/*原住房公积金账号(required)*/);
        collectionAllochthounousTransferVice.setZRJGBH(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo());
        collectionAllochthounousTransferVice.setZRGJJZXMC(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));

        //collectionAllochthounousTransferVice.setZYJE(StringUtil.safeBigDecimal(singleTransOutInfoIn.getTranAmt( /* 转移金额(required), 转移金额=本金金额+利息金额 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */)));
        //collectionAllochthounousTransferVice.setYZHBJJE(StringUtil.safeBigDecimal(singleTransOutInfoIn.getOrAcctAmt(/* 本金金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空 */)));
        //collectionAllochthounousTransferVice.setBNDLX(StringUtil.safeBigDecimal(singleTransOutInfoIn.getYinter(/* 利息金额(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)));
        //collectionAllochthounousTransferVice.setKHRQ(DateUtil.safeStr2Date("YYYYMM",singleTransOutInfoIn.getOAcctDate(/* 开户日期(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)));
        //collectionAllochthounousTransferVice.setJZNY(singleTransOutInfoIn.get(/* 缴至年月(required), 格式: YYYYMM 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/));
        //collectionAllochthounousTransferVice.setLXJC(StringUtil.safeBoolean(singleTransOutInfoIn.getSixMconFlag(/*缴至月份之前6个月是否连续缴存(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)));
        //collectionAllochthounousTransferVice.setGJJDKCS(StringUtil.safeBigDecimal(singleTransOutInfoIn.getLoanNum(/*在转出地使用住房公积金贷款次数(required), 当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)).intValue());
        //collectionAllochthounousTransferVice.setJQDK(StringUtil.safeBoolean(singleTransOutInfoIn.getLoanEndFlag(/*是否有未结清的公积金贷款(required), 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)));
        //collectionAllochthounousTransferVice.setSFCZPTPD(StringUtil.safeBoolean(singleTransOutInfoIn.getFrLoanFlag(/*是否存在骗提骗贷行为, 1：是 2：否  当交易类型=‘0’, 反馈信息代码=‘1’时, 可为空*/)));

        //singleTransOutInfoIn.getTranRstCode(/*反馈信息代码, 0-已完成 1-失败 2-处理中 当交易类型为2-反馈结果时, 该项必填, 其他可为空*/);

        collectionAllochthounousTransferVice.setFKXX(singleTransOutInfoIn.getTranRstMsg(/* 反馈信息*/));
        collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.联系函复核通过.getCode());
        //collectionAllochthounousTransferVice.setYWLXDH(singleTransOutInfoIn.getContPhone(/*业务办理联系电话(required), 固定电话：区号-电话号码 手机：11位号码*/));

        //singleTransOutInfoIn.getBak1(/*备用*/);

        //singleTransOutInfoIn.getBak2(/*备用*/);

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });
    }

    @Override
    public void transferOutConfirmCallBack(SingleTransOutInfoIn singleTransOutInfoIn) {

        //region //必要参数查询&正确性验证
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("allochthounousTransferVice.LXHBH", singleTransOutInfoIn.getOrConNum());
            this.put("gjhtqywlx", CollectionBusinessType.外部转出.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (collectionPersonalBusinessDetails == null || collectionPersonalBusinessDetails.getAllochthounousTransferVice() == null || collectionPersonalBusinessDetails.getExtension() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

        CCollectionAllochthounousTransferVice collectionAllochthounousTransferVice = collectionPersonalBusinessDetails.getAllochthounousTransferVice();

        List<CCollectionAllochthounousTransferProcess> list_process = collectionAllochthounousTransferVice.getProcesses();

        if ("0".equals(singleTransOutInfoIn.getTranRstCode())) {

            String lxhzt = collectionAllochthounousTransferVice.getLXHZT();

            CCollectionAllochthounousTransferProcess collectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

            if (AllochthonousStatus.账户信息复核通过.getCode().equals(lxhzt)) {
                collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.正常办结.getName());
                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.正常办结.getCode());
                collectionAllochthounousTransferProcess.setCaoZuo("转入方发起正常办结");
                collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.正常办结.name());
            }
            else if (AllochthonousStatus.协商中.getCode().equals(lxhzt)) {
                collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.协商后业务办结.getName());
                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.协商后业务办结.getCode());
                collectionAllochthounousTransferProcess.setCaoZuo("转入方发起协商办结");
                collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.协商后业务办结.name());
            }
            else if (AllochthonousStatus.转出审核不通过.getCode().equals(lxhzt)) {
                collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.转出失败业务办结.getName());
                collectionAllochthounousTransferVice.setLXHZT(AllochthonousStatus.转出失败业务办结.getCode());
                collectionAllochthounousTransferProcess.setCaoZuo("转入方发起失败办结");
                collectionAllochthounousTransferProcess.setCZHZT(AllochthonousStatus.转出失败业务办结.name());
            }
            //endregion

            //region //字段填充
            collectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));
            collectionAllochthounousTransferProcess.setCZRY(singleTransOutInfoIn.getCenterHeadIn().getOperNo());
            collectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransOutInfoIn.getCenterHeadIn().getSendDate() + singleTransOutInfoIn.getCenterHeadIn().getSendTime()));
            collectionAllochthounousTransferProcess.setCZYJ(singleTransOutInfoIn.getTranRstMsg());

            list_process.add(collectionAllochthounousTransferProcess);
        }

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });
    }

    public void transferOutPayCallBack(AccChangeNotice accChangeNotice) {

        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();

        if (accChangeNoticeFile == null) {
            return;
        }

        String BDCSeqNo = accChangeNoticeFile.getNo();
        String ReceiveDate = accChangeNoticeFile.getDate();
        String summary = accChangeNoticeFile.getSummary();

        if (!DateUtil.isFollowFormat(ReceiveDate, "yyyyMMdd", false)) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "交易日期");
        }

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", BDCSeqNo);

        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        if (collectionPersonalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转出地已转账.getName());

        DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        collectionPersonalBusinessDetails.setFse(accChangeNotice.getAccChangeNoticeFile().getAmt().subtract(collectionPersonalBusinessDetails.getFslxe()).multiply(new BigDecimal("-1")));

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("cCollectionUnitBusinessDetailsExtension.beizhu", collectionPersonalBusinessDetails.getYwlsh());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        collectionUnitBusinessDetails.setFse(collectionPersonalBusinessDetails.getFse());
        collectionUnitBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转出地已转账.getName());

        collectionPersonalBusinessDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionPersonalBusinessDetails.getUnit().getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails.getFse()));
        DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        //region //销户

        StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();

        StCollectionPersonalAccount collectionPersonalAccount = commonPerson.getCollectionPersonalAccount();
        BigDecimal GRZHYE = BigDecimal.ZERO.add(collectionPersonalAccount.getGrzhye());

        collectionPersonalAccount.setGrzhye(BigDecimal.ZERO);
        collectionPersonalAccount.setGrzhzt(PersonAccountStatus.外部转出销户.getCode());
        collectionPersonalAccount.setXhrq(new Date());
        collectionPersonalAccount.setXhyy(CollectionBusinessType.外部转出.getCode());

        DAOBuilder.instance(this.commonPersonDAO).entity(commonPerson).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });


        //endregion
        //region //财务入账

        accChangeNoticeFile.setSummary("外部转出-" + collectionPersonalBusinessDetails.getPerson().getXingMing());

        String acc_summary = accChangeNoticeFile.getSummary();
        int djsl = 2;
        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

        VoucherAmount jfsj = new VoucherAmount();
        jfsj.setZhaiYao(summary);
        jfsj.setJinE(collectionPersonalBusinessDetails.getFse().abs());
        JFSJ.add(jfsj);

        VoucherAmount jfsj1 = new VoucherAmount();
        jfsj1.setZhaiYao(summary);
        jfsj1.setJinE(collectionPersonalBusinessDetails.getFslxe().abs());
        JFSJ.add(jfsj1);

        VoucherAmount dfsj = new VoucherAmount();
        dfsj.setJinE(collectionPersonalBusinessDetails.getFse().abs().add(collectionPersonalBusinessDetails.getFslxe().abs()));
        dfsj.setZhaiYao(summary);
        DFSJ.add(dfsj);

        this.voucherManagerService.addVoucher("毕节市住房公积金管理中心",
                collectionPersonalBusinessDetails.getExtension().getCzy(),
                collectionPersonalBusinessDetails.getExtension().getCzy(), "", "",
                VoucherBusinessType.外部转出.getCode(),
                VoucherBusinessType.外部转出.getCode(),
                collectionPersonalBusinessDetails.getYwlsh(),
                JFSJ,DFSJ,djsl+"",
                accChangeNotice, null
        );
        try{
            Calendar c = Calendar.getInstance();
            ismsCommon.sendSingleSMSWithTemp(commonPerson.getSjhm(), SMSTemp.异地转移接续转出.getCode(),
                    new ArrayList<String>() {{
                        this.add(commonPerson.getXingMing());
                        this.add(String.valueOf(c.get(Calendar.MONTH)+1));
                        this.add(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
                        this.add(collectionPersonalBusinessDetails.getAllochthounousTransferVice().getZRGJJZXMC());
                        this.add(collectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSE().abs().toString());
                        this.add(collectionPersonalBusinessDetails.getAllochthounousTransferVice().getFSLXE().abs().toString());
                    }}
            );
        }catch (Exception e){
            logger.info("异地转移接续转出短信发送失败");
        }

        //endregion
    }

    @Autowired
    IStCollectionPersonalBusinessDetailsDAO personalBusinessDetailsDAO;

    /**
     * 转入撤销-回调
     *
     * @param transInApplCancelIn TransInApplCancelIn对象
     * @throws Exception
     */
    @Override
    public void sendCancelNotice(TransInApplCancelIn transInApplCancelIn) {

        if (transInApplCancelIn == null || transInApplCancelIn.getOrConNum() == null) {
            throw new ErrorException();
        }

        StCollectionPersonalBusinessDetails personalBusinessDetails = DAOBuilder.instance(personalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("allochthounousTransferVice.LXHBH", transInApplCancelIn.getOrConNum());
            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.外部转出.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务");
        }

        // TODO: 2017/12/8 撤销办结操作
        personalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.转入撤销业务办结.getCode());
        personalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转入撤销业务办结.getName());
        personalBusinessDetails.getExtension().setBjsj(new Date());

        CCollectionAllochthounousTransferProcess transferProcess = new CCollectionAllochthounousTransferProcess();
        transferProcess.setCaoZuo("转入方撤销业务");

        transferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(transInApplCancelIn.getCenterHeadIn().getTxUnitNo()));
        transferProcess.setCZRY(transInApplCancelIn.getCenterHeadIn().getOperNo());
        transferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", transInApplCancelIn.getCenterHeadIn().getSendDate() + transInApplCancelIn.getCenterHeadIn().getSendTime()));//
        transferProcess.setCZYJ(transInApplCancelIn.getRemark());
        transferProcess.setCZHZT(AllochthonousStatus.转入撤销业务办结.getCode());
        transferProcess.setTransferVice(personalBusinessDetails.getAllochthounousTransferVice());
        personalBusinessDetails.getAllochthounousTransferVice().getProcesses().add(transferProcess);


        personalBusinessDetailsDAO.save(personalBusinessDetails);
    }


    /**
     * 回调函数-转移接续转入
     *
     * @param singleTransInApplIn
     */
    @Override
    public void transferIntoCall(SingleTransInApplIn singleTransInApplIn) {
        if (singleTransInApplIn.getTxFunc().equals("1") && singleTransInApplIn.getTranRstCode().equals("0")) {

            StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("allochthounousTransferVice.LXHBH", singleTransInApplIn.getConNum());
                this.put("gjhtqywlx", CollectionBusinessType.外部转入.getCode());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses() == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
            }
            stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.联系函确认接收.getName());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.联系函确认接收.getCode());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setFKXX(singleTransInApplIn.getTranRstMsg());
            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("转出方确认接受联系函");
            cCollectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransInApplIn.getCenterHeadIn().getTxUnitNo()));//操作机构
            cCollectionAllochthounousTransferProcess.setCZRY(singleTransInApplIn.getCenterHeadIn().getOperNo());//操作员
            cCollectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransInApplIn.getCenterHeadIn().getSendDate() + singleTransInApplIn.getCenterHeadIn().getSendTime()));
            cCollectionAllochthounousTransferProcess.setCZYJ(singleTransInApplIn.getTranRstMsg());
            cCollectionAllochthounousTransferProcess.setCZHZT("联系函确认接收");
            cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
            DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
    }

    /**
     * 单笔转出信息发送/接收(BDC905).
     *
     * @param singleTransOutInfoIn
     */
    @Override
    public void transferOutCall(SingleTransOutInfoIn singleTransOutInfoIn) {
        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("allochthounousTransferVice.LXHBH", singleTransOutInfoIn.getOrConNum());
            this.put("gjhtqywlx", CollectionBusinessType.外部转入.getCode());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }

        if (!singleTransOutInfoIn.getTranRstCode().equals("1") && singleTransOutInfoIn.getTxFunc().equals("0")) {
            stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.账户信息审核通过.getName());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.账户信息复核通过.getCode());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setFKXX(singleTransOutInfoIn.getTranRstMsg());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setZYJE(StringUtil.notEmpty(singleTransOutInfoIn.getTranAmt()) ? new BigDecimal(singleTransOutInfoIn.getTranAmt()) : new BigDecimal("0"));
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setFSLXE(StringUtil.notEmpty(singleTransOutInfoIn.getYinter()) ? new BigDecimal(singleTransOutInfoIn.getYinter()) : new BigDecimal("0"));
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setFSE(StringUtil.notEmpty(singleTransOutInfoIn.getOrAcctAmt()) ? new BigDecimal(singleTransOutInfoIn.getOrAcctAmt()) : new BigDecimal("0"));

            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("转出方复核账户信息通过");
            cCollectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));//操作机构
            cCollectionAllochthounousTransferProcess.setCZRY(singleTransOutInfoIn.getCenterHeadIn().getOperNo());//操作员
            cCollectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransOutInfoIn.getCenterHeadIn().getSendDate() + singleTransOutInfoIn.getCenterHeadIn().getSendTime()));
            cCollectionAllochthounousTransferProcess.setCZYJ(singleTransOutInfoIn.getTranRstMsg());
            cCollectionAllochthounousTransferProcess.setCZHZT("账户信息复核通过");
            cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        }
        if (singleTransOutInfoIn.getTranRstCode().equals("1") && singleTransOutInfoIn.getTxFunc().equals("0")) {
            stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转出审核不通过.getName());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.转出审核不通过.getCode());
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setFKXX(singleTransOutInfoIn.getTranRstMsg());

            List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses();
            CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
            cCollectionAllochthounousTransferProcess.setCaoZuo("转出审核不通过");
            cCollectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));//操作机构
            cCollectionAllochthounousTransferProcess.setCZRY(singleTransOutInfoIn.getCenterHeadIn().getOperNo());//操作员
            cCollectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransOutInfoIn.getCenterHeadIn().getSendDate() + singleTransOutInfoIn.getCenterHeadIn().getSendTime()));
            cCollectionAllochthounousTransferProcess.setCZYJ(singleTransOutInfoIn.getTranRstMsg());
            cCollectionAllochthounousTransferProcess.setCZHZT("转出审核不通过");
            cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
            cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        }
        DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }

    @Override
    public void transferOutArrange(SingleTransOutInfoIn singleTransOutInfoIn) {

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("allochthounousTransferVice.LXHBH", singleTransOutInfoIn.getOrConNum());
            this.put("gjhtqywlx", CollectionBusinessType.外部转出.getCode());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的联系函编号");
        }

        stCollectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.协商中.getName());
        stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.协商中.getCode());

        List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().getProcesses();
        CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
        cCollectionAllochthounousTransferProcess.setCaoZuo("转入方发起协商操作");
        cCollectionAllochthounousTransferProcess.setCZJG(icBankCenterInfoDAO.getCenterNameByNum(singleTransOutInfoIn.getCenterHeadIn().getTxUnitNo()));//操作机构
        cCollectionAllochthounousTransferProcess.setCZRY(singleTransOutInfoIn.getCenterHeadIn().getOperNo());//操作员
        cCollectionAllochthounousTransferProcess.setCZSJ(DateUtil.safeStr2Date("yyyyMMddHHmmss", singleTransOutInfoIn.getCenterHeadIn().getSendDate() + singleTransOutInfoIn.getCenterHeadIn().getSendTime()));
        cCollectionAllochthounousTransferProcess.setCZYJ(singleTransOutInfoIn.getTranRstMsg());
        cCollectionAllochthounousTransferProcess.setCZHZT(CollectionBusinessStatus.协商中.getName());
        cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
        cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }

}
