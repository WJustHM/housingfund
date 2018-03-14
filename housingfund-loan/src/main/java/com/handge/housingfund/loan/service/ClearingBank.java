package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.LoanDeductionIn;
import com.handge.housingfund.common.service.bank.bean.center.SingleCollectionIn;
import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.enums.BusTypeEnums;
import com.handge.housingfund.common.service.loan.IClearingBank;
import com.handge.housingfund.common.service.util.ErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/9/17.
 */
@SuppressWarnings("Duplicates")
@Component
public class ClearingBank implements IClearingBank {
    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    /**
     * 单笔收款
     */
    @Override
    public <T> T sendSingleMessage(String ywlsh, String hkzh, String jkrxm, String yhmc, BigDecimal totalMoney) throws Exception {
        SingleCollectionIn singleCollectionIn = null;
        ArrayList<CenterAccountInfo> specialAccount;
        try {
            specialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(yhmc);
        }catch (ErrorException e){
            throw new Exception("single"+e.getMsg());
        }
        for (CenterAccountInfo specialAccou : specialAccount) {
            if (specialAccou.getZHXZ().equals("01")) {
                CenterHeadIn centerHeadIn = new CenterHeadIn();
                centerHeadIn.setOperNo("admin");//操作员
                centerHeadIn.setSendSeqNo(ywlsh);
                centerHeadIn.setReceiveNode(specialAccou.getNode()); //银行 接收节点号
                centerHeadIn.setCustNo(specialAccou.getKHBH());

                singleCollectionIn = new SingleCollectionIn();
                singleCollectionIn.setCenterHeadIn(centerHeadIn);
                singleCollectionIn.setSettleType("1");//判断，一般都是同行s
                singleCollectionIn.setBusType(BusTypeEnums.汇补缴.getCode()); //业务类型代码
                //公积金中心账户
                singleCollectionIn.setCrAcctNo(specialAccou.getYHZHHM());//收款公积金账户账号
                singleCollectionIn.setCrAcctName(specialAccou.getYHZHMC());//收款公积金账户户名
                singleCollectionIn.setCrAcctClass("2");//收方账户类别 对公 s
                //付款方账号
                singleCollectionIn.setDeAcctNo(hkzh);//付方
                singleCollectionIn.setDeAcctName(jkrxm);//付方
                singleCollectionIn.setDeAcctClass("1");//付方 gs xg
                singleCollectionIn.setDeBankClass("0");//本行 s
                //还款金额
                singleCollectionIn.setAmt(totalMoney);//还款金额
                //singleCollectionIn.setDeBankName("中国银行");//款行必须输入
                //singleCollectionIn.setDeChgNo("104362004010");//跨行必须输入
                singleCollectionIn.setSummary(yhmc + ":" + jkrxm + "单笔收款");
                singleCollectionIn.setRemark(yhmc + ":" + jkrxm + "单笔收款");
            }
        }
        return (T) singleCollectionIn;
    }

    @Override
    public <T> T sendOverdueMessage(String ywlsh, String hkzh, String jkrxm, String yhmc, BigDecimal totalMoney) throws Exception {
        SingleCollectionIn singleCollectionIn = null;
        ArrayList<CenterAccountInfo> specialAccount;
        try {
            specialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(yhmc);
        }catch (ErrorException e){
            throw new Exception("single"+e.getMsg());
        }
        for (CenterAccountInfo specialAccou : specialAccount) {
            if (specialAccou.getZHXZ().equals("01")) {
                CenterHeadIn centerHeadIn = new CenterHeadIn();
                centerHeadIn.setOperNo("admin");//操作员
                centerHeadIn.setSendSeqNo(ywlsh);
                centerHeadIn.setReceiveNode(specialAccou.getNode()); //银行 接收节点号
                centerHeadIn.setCustNo(specialAccou.getKHBH());

                singleCollectionIn = new SingleCollectionIn();
                singleCollectionIn.setCenterHeadIn(centerHeadIn);
                singleCollectionIn.setSettleType("1");//判断，一般都是同行s
                singleCollectionIn.setBusType(BusTypeEnums.逾期还款.getCode()); //业务类型代码
                //公积金中心账户
                singleCollectionIn.setCrAcctNo(specialAccou.getYHZHHM());//收款公积金账户账号
                singleCollectionIn.setCrAcctName(specialAccou.getYHZHMC());//收款公积金账户户名
                singleCollectionIn.setCrAcctClass("2");//收方账户类别 对公 s
                //付款方账号
                singleCollectionIn.setDeAcctNo(hkzh);//付方
                singleCollectionIn.setDeAcctName(jkrxm);//付方
                singleCollectionIn.setDeAcctClass("1");//付方 gs xg
                singleCollectionIn.setDeBankClass("0");//本行 s
                //还款金额
                singleCollectionIn.setAmt(totalMoney);//还款金额
                //singleCollectionIn.setDeBankName("中国银行");//款行必须输入
                //singleCollectionIn.setDeChgNo("104362004010");//跨行必须输入
                singleCollectionIn.setSummary(yhmc + ":" + jkrxm + "单笔收款");
                singleCollectionIn.setRemark(yhmc + ":" + jkrxm + "单笔收款");
            }
        }
        return (T) singleCollectionIn;
    }

    /**
     * 贷款收款
     */
    @Override
    public <T> T sendLoaneMessage(String ywlsh, String yhmc, BigDecimal totalMoney, int totalNubmer) throws Exception {

        LoanDeductionIn loandeductionIn = null;
        ArrayList<CenterAccountInfo> specialAccount;
        try {
            specialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(yhmc);
        }catch (ErrorException e){
            throw new Exception("single"+e.getMsg());
        }
        for (CenterAccountInfo specialAccou : specialAccount) {
            if (specialAccou.getZHXZ().equals("01")) {
                CenterHeadIn centerHeadIn = new CenterHeadIn();
                centerHeadIn.setOperNo("admin");//操作员
                centerHeadIn.setSendSeqNo(ywlsh);
                centerHeadIn.setReceiveNode(specialAccou.getNode()); //银行
                centerHeadIn.setCustNo(specialAccou.getKHBH());

                loandeductionIn = new LoanDeductionIn();
                loandeductionIn.setCenterHeadIn(centerHeadIn);
                if(specialAccou.getNode().equals("104000")){
                    loandeductionIn.setFileType("3");// s
                }else {
                    loandeductionIn.setFileType("1");// s
                }
                loandeductionIn.setCrAcctNo(specialAccou.getYHZHHM());
                loandeductionIn.setCrAcctName(specialAccou.getYHZHMC());
                loandeductionIn.setCrAcctClass("2");// s
                loandeductionIn.setBatchTotalNum(totalNubmer-1);
                loandeductionIn.setBatchTotalAmt(totalMoney);//还款金额
                if("105000".equals(specialAccou.getNode())){
                    loandeductionIn.setBatchPrjNo(specialAccou.getPLXMBH());
                }
            }
        }
        return (T) loandeductionIn;
    }

    /**
     * 回查
     */
    @Override
    public TransactionResultQueryIn transResultQuery(String ywlsh, String yhmc) throws Exception {
        TransactionResultQueryIn transactionResultQueryIn = null;
        ArrayList<CenterAccountInfo> specialAccount;
        try {
            specialAccount = iSettlementSpecialBankAccountManageService.getSpecialAccount(yhmc);
        }catch (ErrorException e){
            throw new Exception("single"+e.getMsg());
        }
        for (CenterAccountInfo specialAccou : specialAccount) {
            if (specialAccou.getZHXZ().equals("01")) {
                CenterHeadIn centerHeadIn = new CenterHeadIn();
                centerHeadIn.setOperNo("admin");//操作员
                centerHeadIn.setReceiveNode(specialAccou.getNode()); //交行
                centerHeadIn.setCustNo(specialAccou.getKHBH());

                transactionResultQueryIn = new TransactionResultQueryIn();
                transactionResultQueryIn.setCenterHeadIn(centerHeadIn);
                transactionResultQueryIn.setTxSeqNo(ywlsh);
            }
        }
        return transactionResultQueryIn;

    }
}
