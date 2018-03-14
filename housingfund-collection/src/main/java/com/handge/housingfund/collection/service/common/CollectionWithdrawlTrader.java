package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.enums.*;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.WithDrawalReason;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionWithdrawlTrader;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.LogUtil;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAuditHistoryDAO;
import com.handge.housingfund.database.dao.ICCollectionWithdrawlBusinessExtensionDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStSettlementSpecialBankAccountDAO;
import com.handge.housingfund.database.entities.CAuditHistory;
import com.handge.housingfund.database.entities.CCollectionWithdrawlBusinessExtension;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/25.
 * 描述
 */
public class CollectionWithdrawlTrader implements ICollectionWithdrawlTrader {
    private static Logger logger = LogManager.getLogger(CollectionWithdrawlTrader.class);
    @Autowired
    private WithdrawlTasks withdrawlTasks;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    //结算平台
    @Autowired
    private IBank iBank;
    @Autowired
    private IStSettlementSpecialBankAccountDAO settlementSpecialBankAccountDAO;
    @Autowired
    private ISettlementSpecialBankAccountManageService accountManageService;
    @Autowired
    private IBankInfoService iBankInfoService;
    //财务
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private ICAuditHistoryDAO iAuditHistoryDAO;
    @Autowired
    private ICCollectionWithdrawlBusinessExtensionDAO withdrawlBusinessExtensionDAO;

    private static String df = "yyyy-MM-dd HH:mm:ss";
    @Override
    public void sendWithdrawlNotice(AccChangeNotice accChangeNotice) {
        System.out.println("账户变动通知：提取");
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }
        String ywlsh = accChangeNoticeFile.getNo();

        //调用财务生成凭证接口
        StCollectionPersonalBusinessDetails details = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });

        try {
            VoucherRes voucherRes;
            //审核人，该条记录审核通过的操作员
            List<CAuditHistory> cAuditHistory = DAOBuilder.instance(iAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("ywlsh", ywlsh);
                    this.put("shjg", "01");
                }
            }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
//                    exceptions.add(e);
                }
            });
            if (CollectionBusinessType.部分提取.getCode().equals(details.getGjhtqywlx())) {

                voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", details.getExtension().getCzy(), cAuditHistory.size() == 0 ? details.getExtension().getCzy() : cAuditHistory.get(0).getCzy(), "", "管理员",
                        VoucherBusinessType.部分提取.getCode(), VoucherBusinessType.部分提取.getCode(), accChangeNotice, null);

            } else {
                //region 销户提取生成记账凭证

                String summary = accChangeNoticeFile.getSummary();
                int djsl = 2;
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

                VoucherAmount jfsj = new VoucherAmount();
                jfsj.setZhaiYao(summary);
                jfsj.setJinE(details.getWithdrawlVice().getFse().abs());
                JFSJ.add(jfsj);

                VoucherAmount jfsj1 = new VoucherAmount();
                jfsj1.setZhaiYao(summary);
                jfsj1.setJinE(details.getExtension().getXhtqfslxe().abs());
                JFSJ.add(jfsj1);

                VoucherAmount dfsj = new VoucherAmount();
                dfsj.setJinE(details.getWithdrawlVice().getFse().abs().add(details.getExtension().getXhtqfslxe().abs()));
                dfsj.setZhaiYao(summary);
                DFSJ.add(dfsj);

                voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", details.getExtension().getCzy(),
                        cAuditHistory.size() == 0 ? details.getExtension().getCzy() : cAuditHistory.get(0).getCzy(), "", "管理员",
                        VoucherBusinessType.销户提取.getCode(), VoucherBusinessType.销户提取.getCode(),
                        ywlsh, JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, null);
                //endregion
            }

            if (!CollectionBusinessStatus.已入账.getName().equals(details.getExtension().getStep())) {
                if (StringUtil.isEmpty(voucherRes.getJZPZH())) {
                    details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                    details.getExtension().setSbyy( StringUtil.notEmpty(details.getExtension().getSbyy()) ? details.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) +voucherRes.getMSG()
                    :DateUtil.date2Str(new Date(),df) +voucherRes.getMSG() );
                } else {
                    //提取办结操作
                    withdrawlTasks.doFinal(ywlsh);
                    details.getExtension().setJzpzh(voucherRes.getJZPZH());
                }
                collectionPersonalBusinessDetailsDAO.update(details);
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            details.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
            details.getExtension().setSbyy(StringUtil.notEmpty(details.getExtension().getSbyy())
                    ? details.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) + e.getMessage() :
                    DateUtil.date2Str(new Date(),df) + e.getMessage());
            collectionPersonalBusinessDetailsDAO.update(details);
        }
    }

    @Override
    public void sendFailedWithdrawlNotice(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            return;
        }
        String ywlsh = accChangeNoticeFile.getNo();
        withdrawlTasks.doRecoverWithdrawl(ywlsh);
    }

    /**
     * 封装数据发往结算平台（单笔付款）
     *
     * @param YWLSH
     */
    // TODO: 2017/8/31 字段未完善
    public CommonReturn sendMsg(String YWLSH) {

       CommonReturn commonReturn = new CommonReturn();
        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = collectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH);

        CCollectionWithdrawlBusinessExtension withdrawlBussinessExtension = instance(withdrawlBusinessExtensionDAO).searchFilter(new HashMap<String,Object>(){{
            this.put("stCollectionPersonalBusinessDetails",collectionPersonalBusinessDetails);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });

        //个人存款账户开户银行名称
        String indiBankName = withdrawlBussinessExtension != null &&  StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) ?
                withdrawlBussinessExtension.getSkyhmc():collectionPersonalBusinessDetails.getExtension().getGrckzhkhyhmc() ;
        //个人存款账户开户银行代码
        String indiBankCode = "";
        // region 头部信息
        CenterHeadIn head = new CenterHeadIn();
        head.setSendSeqNo(YWLSH);//发送方流水号
        //接收方节点号(公积金中心银行节点号)
        String receiveNode = "";
        //客户编号
        String custNo = "";
        //付方账户，根据个人存款账户所属的受托银行查找公积金中心对应银行的公积金账号
        String deAcctNo = "";
        //付方户名
        String deAcctName = "";
        //银行代码
        String yhdm = "";
        //联行号
        String chgNo;
        try {
            chgNo = iBankInfoService.getBankInfo(indiBankName).getChgno();
        } catch (ErrorException e) {
            collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
            collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy())
                    ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) +e.getMsg()
            : DateUtil.date2Str(new Date(),df) +e.getMsg());
            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
            commonReturn.setStatus(e.getMsg());
            return commonReturn;
        }

        HashMap<String, String> reasonMap = new HashMap<>();
        reasonMap.put(WithDrawalReason.REASON_1.getCode(), WithDrawalReason.REASON_1.getName());
        reasonMap.put(WithDrawalReason.REASON_2.getCode(), WithDrawalReason.REASON_2.getName());
        reasonMap.put(WithDrawalReason.REASON_3.getCode(), WithDrawalReason.REASON_3.getName());
        reasonMap.put(WithDrawalReason.REASON_4.getCode(), WithDrawalReason.REASON_4.getName());
        reasonMap.put(WithDrawalReason.REASON_5.getCode(), WithDrawalReason.REASON_5.getName());
        reasonMap.put(WithDrawalReason.REASON_6.getCode(), WithDrawalReason.REASON_6.getName());
        reasonMap.put(WithDrawalReason.REASON_7.getCode(), WithDrawalReason.REASON_7.getName());
        reasonMap.put(WithDrawalReason.REASON_8.getCode(), WithDrawalReason.REASON_8.getName());
        reasonMap.put(WithDrawalReason.REASON_9.getCode(), WithDrawalReason.REASON_9.getName());
        reasonMap.put(WithDrawalReason.REASON_10.getCode(), WithDrawalReason.REASON_10.getName());

        ArrayList<CenterAccountInfo> infos = new ArrayList<>();
        try {
            infos = accountManageService.getSpecialAccount(indiBankName);
        } catch (ErrorException ignored) {
        }

        boolean hasCKZH = false; //是否有存款专户,没有就默认使用建行存款专户
        for (CenterAccountInfo info : infos) {
            if ("01".equals(info.getZHXZ())) {
                receiveNode = info.getNode();
                indiBankCode = info.getCode();
                custNo = info.getKHBH();
                deAcctNo = info.getYHZHHM();
                deAcctName = info.getYHZHMC();
                hasCKZH = true;
                yhdm = info.getCode();
            }
        }
        head.setCustNo(custNo);
        head.setReceiveNode(receiveNode);
        head.setOperNo("522400000000000000007");       //操作员编号
        //region 结束

        //region 拼接报文体
        //结算模式 1:同行2:跨行实时3:跨行非实时
        if (!hasCKZH) {
            StSettlementSpecialBankAccount settlementSpecialBankAccount = DAOBuilder.instance(settlementSpecialBankAccountDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("yhdm", "105");
                this.put("zhxz", "01");
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                }
            });

            if (settlementSpecialBankAccount == null) {
                collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy())
                        ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) + "未找到用于此提取的中心银行账号"
                : DateUtil.date2Str(new Date(),df) + "未找到用于此提取的中心银行账号" );
                collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                commonReturn.setStatus("未找到用于此提取的中心银行账号");
                return commonReturn;
            } else {
                head.setCustNo(settlementSpecialBankAccount.getcBankContract().getKhbh());
                head.setReceiveNode(settlementSpecialBankAccount.getcBankContract().getNode());
                deAcctName = settlementSpecialBankAccount.getYhzhmc();
                deAcctNo = settlementSpecialBankAccount.getYhzhhm();
                yhdm = settlementSpecialBankAccount.getcBankContract().getYhdm();
            }
        }
        //保存公积金中心的账户代码，用于提取列表的过滤
        collectionPersonalBusinessDetails.getExtension().setJbrxm(yhdm);

        String settleType;
        if (!StringUtil.notEmpty(indiBankCode)) {
            settleType = SettleTypeEnums.跨行实时.getCode();
        } else {
            settleType = SettleTypeEnums.同行.getCode();
        }
        //业务类型 02:部分提取 03:销户提取
        String busType;
        if (CollectionBusinessType.销户提取.getCode().equals(collectionPersonalBusinessDetails.getExtension().getCzmc())) {
            busType = BusTypeEnums.销户提取.getCode();
        } else {
            busType = BusTypeEnums.部分提取.getCode();
        }

        //金额
        BigDecimal amt;
        if (CollectionBusinessType.销户提取.getCode().equals(collectionPersonalBusinessDetails.getExtension().getCzmc())) {
            if (collectionPersonalBusinessDetails.getExtension().getXhtqfslxe() == null) {
                collectionPersonalBusinessDetails.getExtension().setXhtqfslxe(new BigDecimal(0));
            }
            amt = collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs().add(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe());
        } else {
            amt = collectionPersonalBusinessDetails.getWithdrawlVice().getFse().abs();
        }
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn(
                head,
                settleType,
//                busType,
                BusTypeEnums.部分提取.getCode(),
                deAcctNo,//付方账号
                deAcctName,//付方户名
                AcctClassEnums.对公.getCode(),//付方账户类别(1:对私 2:对公)
//                collectionPersonalBusinessDetails.getFse().abs(),
                amt,
                withdrawlBussinessExtension != null && StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) && Arrays.asList(WithDrawalReason.REASON_8.getCode(),WithDrawalReason.REASON_10.getCode())
                        .contains(collectionPersonalBusinessDetails.getTqyy())? withdrawlBussinessExtension.getSkyhzhhm():collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm()   ,
                withdrawlBussinessExtension != null &&  StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) && Arrays.asList(WithDrawalReason.REASON_8.getCode(),WithDrawalReason.REASON_10.getCode())
                        .contains(collectionPersonalBusinessDetails.getTqyy()) ?  withdrawlBussinessExtension.getSkyhhm() : collectionPersonalBusinessDetails.getExtension().getHuming() ,//收方账户
                AcctClassEnums.对私.getCode(),//收方账户类别(1:对私 2:对公)
                (settleType.equals(SettleTypeEnums.同行.getCode())) ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode(),//收方账户行别（0:本行,1:跨行）,
                amt,
                YWLSH,//业务明细流水号1
                collectionPersonalBusinessDetails.getPerson().getXingMing() + reasonMap.get(collectionPersonalBusinessDetails.getTqyy()) + " 提取公积金",
                "公积金提取");

        //设置跨行必输
        if (!settleType.equals(SettleTypeEnums.同行.getCode())) {
            singlePaymentIn.setCrBankName( withdrawlBussinessExtension != null &&  StringUtil.notEmpty(withdrawlBussinessExtension.getSkyhmc()) && Arrays.asList(WithDrawalReason.REASON_8.getCode(),WithDrawalReason.REASON_10.getCode())
                    .contains(collectionPersonalBusinessDetails.getTqyy())? withdrawlBussinessExtension.getSkyhmc()
                    : collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhkhyhmc() );//收方行名
            singlePaymentIn.setCrChgNo(chgNo);//收方联行号
        }
        //中国银行必输
        if (indiBankCode.equals("104")) {
            singlePaymentIn.setCrChgNo("104100000004");//收方联行号
        }
        //销户提取时必输
        if (collectionPersonalBusinessDetails.getExtension().getCzmc().equals(CollectionBusinessType.销户提取.getCode())) {
            singlePaymentIn.setSummary(collectionPersonalBusinessDetails.getPerson().getXingMing() + reasonMap.get(collectionPersonalBusinessDetails.getTqyy()) + " 销户提取");
            if (WithDrawalReason.REASON_5.equals(collectionPersonalBusinessDetails.getTqyy())) {
                singlePaymentIn.setSummary(collectionPersonalBusinessDetails.getPerson().getXingMing() + reasonMap.get(collectionPersonalBusinessDetails.getTqyy()) + " 外部转出");
            }

//            singlePaymentIn.setDeIntAcctNo(deAcctNo);//付息户账号 = 付方账号
//            singlePaymentIn.setDeIntAcctName(deAcctName);//付息户名 = 付方户名
//            singlePaymentIn.setDeIntAcctClass(AcctClassEnums.对公.getCode());//付息户类别(1:对私 2:对公)
//            singlePaymentIn.setRefSeqNo2("");//业务明细流水号2
//            singlePaymentIn.setIntAmt(collectionPersonalBusinessDetails.getExtension().getXhtqfslxe() == null ? new BigDecimal(0) : collectionPersonalBusinessDetails.getExtension().getXhtqfslxe());
//            singlePaymentIn.setIntAmt(new BigDecimal(0));
//            singlePaymentIn.setDeIntCrAcct(collectionPersonalBusinessDetails.getPerson().getCollectionPersonalAccount().getGrckzhhm());
        }

        //region结束
        System.out.println("发往结算平台数据：" + singlePaymentIn);
        SinglePaymentOut singlePaymentOut = null;
        String returnMsg;
        try {
            singlePaymentOut = iBank.sendMsg(singlePaymentIn);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Arrays.asList(BankExceptionEnums.转换发送报文出错.getDesc(), BankExceptionEnums.发送请求出错.getDesc()).contains(msg)) {
                collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                collectionPersonalBusinessDetails.getExtension().setSbyy( StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy())
                        ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) + " 发送结算平台的业务数据异常，请作废并重新办理该业务" :
                        DateUtil.date2Str(new Date(),df) + " 发送结算平台的业务数据异常，请作废并重新办理该业务"  );
                collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                returnMsg = msg;
                commonReturn.setStatus(returnMsg);
                return commonReturn;
            } else {//非正常失败，需手动处理
                collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
                collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy())
                        ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) + "读取结算平台返回数据出错，请手动验证"
                        : DateUtil.date2Str(new Date(),df) + "读取结算平台返回数据出错，请手动验证");
                collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
                returnMsg = msg;
                commonReturn.setStatus(returnMsg);
                return commonReturn;
//                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, msg + " : 请手动处理");
            }
        }
        System.out.println("结算平台返回数据：" + singlePaymentOut);
        if (!singlePaymentOut.getCenterHeadOut().getTxStatus().equals("0")) {
            //若交易失败(如：账户余额不足等正常情况)
            collectionPersonalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.入账失败.getName());
            collectionPersonalBusinessDetails.getExtension().setSbyy(StringUtil.notEmpty(collectionPersonalBusinessDetails.getExtension().getSbyy())
                    ? collectionPersonalBusinessDetails.getExtension().getSbyy() + DateUtil.date2Str(new Date(),df) + singlePaymentOut.getCenterHeadOut().getRtnMessage() + "请手动验证" :
                    DateUtil.date2Str(new Date(),df) + singlePaymentOut.getCenterHeadOut().getRtnMessage() + "请手动验证"  );
            collectionPersonalBusinessDetailsDAO.update(collectionPersonalBusinessDetails);
            returnMsg = singlePaymentOut.getCenterHeadOut().getRtnMessage();
            commonReturn.setStatus(returnMsg);
            return commonReturn;
//            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "银行处理失败, " + singlePaymentOut.getCenterHeadOut().getRtnMessage() +"，请手动处理" );
        }
        returnMsg = "success";
        commonReturn.setStatus(returnMsg);
        return commonReturn;
    }
}
