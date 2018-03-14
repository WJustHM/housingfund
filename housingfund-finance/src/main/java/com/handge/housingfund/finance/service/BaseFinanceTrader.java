package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentOut;
import com.handge.housingfund.common.service.bank.bean.center.SingleTransferAccountIn;
import com.handge.housingfund.common.service.bank.bean.center.SingleTransferAccountOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.enums.AcctClassEnums;
import com.handge.housingfund.common.service.bank.enums.BankClassEnums;
import com.handge.housingfund.common.service.bank.enums.BusTypeEnums;
import com.handge.housingfund.common.service.bank.enums.SettleTypeEnums;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.SettlementHandler;
import com.handge.housingfund.database.dao.ICBankBankInfoDAO;
import com.handge.housingfund.database.entities.CBankBankInfo;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuefei_wang on 17-10-18.
 * <p>
 * 需要走结算平台业务：
 * <p>
 * 支付住房公积金归集手续费，2c92941e5e2e0a99015e310cb5b00038，026   f
 * 支付个人贷款手续费，2c92941e5e2e0a99015e310cb5b00039，027 f
 * 支付项目贷款手续费，2c92941e5e2e0a99015e310cb5b0003a，028 f
 * 同行转账，2c92941e5e2e0a99015e310ddd7f003e
 * 跨行转账，2c92941e5e2e0a99015e310ddd7f003f
 * 支付补息利息支出，2c92941e5e2e0a99015e310bc2820035，032 f
 * 支付专项应付款，2c92941f5ec31709015ec34700890005，041 f
 * 其他支付，2c92941e5e2e0a99015e310bc2820034，031 f
 */
@SuppressWarnings("Duplicates")
@Service
public class BaseFinanceTrader {

    @Autowired
    IBank iBank;

    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    @Autowired
    ICBankBankInfoDAO bankBankInfoDAO;

    public void handleZFZFGJJGJSXF(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                      SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception)  {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.手续费支出.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleGRDKSXF(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                 SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);

        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());

        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.手续费支出.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleXMDKSXF(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                 SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception){
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.手续费支出.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleTHZZ(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                              SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn();
        singleTransferAccountIn.setCenterHeadIn(centerHeadIn);
        singleTransferAccountIn.setSettleType(SettleTypeEnums.同行.getCode()); // ??
        singleTransferAccountIn.setBusType(BusTypeEnums.资金划转.getCode());  // ??? 18:资金划转
        singleTransferAccountIn.setDeAcctNo(content.get("FFYHZH").toString());
        singleTransferAccountIn.setDeAcctName(content.get("FFYHHM").toString());
        singleTransferAccountIn.setDeAcctClass(AcctClassEnums.对公.getCode());  //??? 付方帐号类别 2:对公
        singleTransferAccountIn.setCrAcctNo(content.get("SFYHZH").toString());
        singleTransferAccountIn.setCrAcctName(content.get("SFYHHM").toString());
        singleTransferAccountIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??  收方帐号类别 2:对公
        singleTransferAccountIn.setCrBankClass(BankClassEnums.本行.getCode());
        singleTransferAccountIn.setCrBankName(content.get("YHMC").toString()); // ?? 中国银行交易需要输入
        singleTransferAccountIn.setCrChgNo(ff.getChgno());
        singleTransferAccountIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singleTransferAccountIn.setRefSeqNo(centerHeadIn.getSendSeqNo());
        singleTransferAccountIn.setSummary(centerHeadIn.getSendSeqNo());
        singleTransferAccountIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SingleTransferAccountOut singleTransferAccountOut = iBank.sendMsg(singleTransferAccountIn);

            settlementHandler.setCenterHeadOut(singleTransferAccountOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleKHZZ(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                              SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception){
        String settleType = ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode();
        SingleTransferAccountIn singleTransferAccountIn = new SingleTransferAccountIn();
        singleTransferAccountIn.setCenterHeadIn(centerHeadIn);
        singleTransferAccountIn.setSettleType(settleType);
        singleTransferAccountIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singleTransferAccountIn.setBusType(BusTypeEnums.资金划转.getCode());  // ???
        singleTransferAccountIn.setDeAcctNo(content.get("FFYHZH").toString());
        singleTransferAccountIn.setDeAcctName(content.get("FFYHHM").toString());
        singleTransferAccountIn.setDeAcctClass(AcctClassEnums.对公.getCode());  //??? 对公账户？
        singleTransferAccountIn.setCrAcctNo(content.get("SFYHZH").toString());
        singleTransferAccountIn.setCrAcctName(content.get("SFYHHM").toString());
        singleTransferAccountIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??　对公账户?
        singleTransferAccountIn.setCrBankClass(BankClassEnums.跨行.getCode());
        singleTransferAccountIn.setCrBankName(content.get("SFYHMC").toString()); // ?? 收方银行行名
        singleTransferAccountIn.setCrChgNo(ff.getChgno());
        singleTransferAccountIn.setRefSeqNo(centerHeadIn.getSendSeqNo());
        singleTransferAccountIn.setSummary(centerHeadIn.getSendSeqNo());
        singleTransferAccountIn.setRemark(centerHeadIn.getSendSeqNo());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SingleTransferAccountOut singleTransferAccountOut = iBank.sendMsg(singleTransferAccountIn);

            settlementHandler.setCenterHeadOut(singleTransferAccountOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleZFBXLXZC(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                  SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setSettleType(SettleTypeEnums.同行.getCode());
        singlePaymentIn.setBusType(BusTypeEnums.其他支付.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());

        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    //支付廉租房补充资金
    public void handleZFLZBCZJ(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                  SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setSettleType(SettleTypeEnums.同行.getCode());
        singlePaymentIn.setBusType(BusTypeEnums.公租房投资.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());

        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleZFZXYFK(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                 SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.支付管理费用.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleZFQTYFK(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                                 SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception){
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.其他支付.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public void handleOther(Map<String, Object> content, CenterHeadIn centerHeadIn, CenterAccountInfo ff,
                               SettlementHandler.Success success, SettlementHandler.Fail fail, SettlementHandler.ManualProcess process, SettlementHandler.SendException exception) {
        SinglePaymentIn singlePaymentIn = new SinglePaymentIn();
        singlePaymentIn.setCenterHeadIn(centerHeadIn);
        String ffyhmc = content.get("FFYHMC").toString();
        String sfyhmc = content.get("SFYHMC").toString();
        boolean s = bankSimilar(ffyhmc, sfyhmc);
        String bankclass = s ? BankClassEnums.本行.getCode() : BankClassEnums.跨行.getCode();
        String settleType = s ? SettleTypeEnums.同行.getCode() : (ff.getKHSFSS() ? SettleTypeEnums.跨行实时.getCode() : SettleTypeEnums.跨行非实时.getCode());
        singlePaymentIn.setCrChgNo(ff.getChgno());
        singlePaymentIn.setSettleType(settleType);
        singlePaymentIn.setBusType(BusTypeEnums.其他支付.getCode());
        singlePaymentIn.setDeAcctNo(content.get("FFYHZH").toString());
        singlePaymentIn.setDeAcctName(content.get("FFYHHM").toString());
        singlePaymentIn.setDeAcctClass(AcctClassEnums.对公.getCode());
        singlePaymentIn.setAmt(new BigDecimal(content.get("FSE").toString()));
        singlePaymentIn.setCrAcctNo(content.get("SFYHZH").toString());
        singlePaymentIn.setCrAcctName(content.get("SFYHHM").toString());
        singlePaymentIn.setCrAcctClass(AcctClassEnums.对公.getCode()); // ??
        singlePaymentIn.setCrBankClass(bankclass); // ---->判定同行还是跨行
        singlePaymentIn.setCrBankName(content.get("SFYHMC").toString()); // ??
        singlePaymentIn.setRefSeqNo1(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setSummary(centerHeadIn.getSendSeqNo());
        singlePaymentIn.setRemark(content.getOrDefault("BZ", "无备注").toString());
        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(success)
                    .setFail(fail)
                    .setManualProcess(process);

            SinglePaymentOut singlePaymentOut = iBank.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(exception).handleException(e);
        }
    }

    public boolean bankSimilar(String bankone, String banktwo) {

        CBankBankInfo bank1 = DAOBuilder.instance(bankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bank_name", bankone);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        CBankBankInfo bank2 = DAOBuilder.instance(bankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bank_name", banktwo);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (bank1 == null) {
            throw new ErrorException(bankone + " 　非本公积金中心签约银行");
        }
        if (bank2 == null) {
            throw new ErrorException(banktwo + " 　非本公积金中心签约银行");
        }

        return bank1.getNode().equals(bank2.getNode());
    }

}
