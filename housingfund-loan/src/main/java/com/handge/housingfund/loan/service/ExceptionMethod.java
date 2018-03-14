package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryIn;
import com.handge.housingfund.common.service.bank.bean.center.TransactionResultQueryOut;
import com.handge.housingfund.common.service.bank.enums.BankExceptionEnums;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.loan.IClearingBank;
import com.handge.housingfund.common.service.loan.IExceptionMethod;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.LogUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CHousingBusinessDetailsExtension;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import com.handge.housingfund.database.entities.StHousingOverdueRegistration;
import com.handge.housingfund.database.enums.TaskBusinessStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Funnyboy on 2017/9/17.
 */
@SuppressWarnings("Duplicates")
@Component
public class ExceptionMethod implements IExceptionMethod {
    private static Logger logger = LogManager.getLogger(LoanTaskService.class);
    //个人贷款业务流程表
    @Autowired
    private ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    //个人贷款账户
    @Autowired
    private IStHousingPersonalAccountDAO stHousingPersonalAccount;
    //借款人公积金账号
    @Autowired
    private IStCollectionPersonalAccountDAO stCollectionPersonalAccountDAO;
    //个人贷款业务明细
    @Autowired
    private IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;
    //逾期记录
    @Autowired
    private IStHousingOverdueRegistrationDAO stHousingOverdueRegistrationDAO;
    //银行
    @Autowired
    private IBank iBank;
    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IClearingBank iclearingBank;


    @Override
    public void exceptionSelf(Exception e, String id, String ywlsh, String yhmc) {
        /**
         * 三种可能：
         * 1. 发送报文出错，直接抛出回滚
         * 2. 回复报文出错，回查接口, 线下
         * 3. 返回失败，线下处理，修正数据
         * */
        String msg = e.getMessage();
        if (msg != null) {
            //失败处理
            CLoanHousingBusinessProcess cnHousingBusinessProcess = cloanHousingBusinessProcess.get(id);
            if (msg.contains("入账失败") || msg.contains("数据缺失") || msg.contains(BankExceptionEnums.发送请求出错.getDesc()) || msg.contains(BankExceptionEnums.转换发送报文出错.getDesc()) || msg.contains("发送发流水号失败")) {
                //下载
                List<StHousingBusinessDetails> stHousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails hingBusinessDetails1 : stHousingBusinessDetails) {
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setRgcl("0");
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                }
                cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
            } else if (msg.contains(BankExceptionEnums.接收回复出错.getDesc()) || msg.contains(BankExceptionEnums.转换回复报文出错.getDesc())) {//返回报文出错,需要调用查询接口（2）
                //需要调用查询接口，判断业务是否接受，如果没有，抛出异常,否则不作操作
                try {
                    TransactionResultQueryIn transactionResultQueryIn = iclearingBank.transResultQuery(ywlsh, yhmc);
                    TransactionResultQueryOut transactionResultQueryOut = iBank.sendMsg(transactionResultQueryIn);
                    if ("1".equals(transactionResultQueryOut.getOldTxStatus()) || "2".equals(transactionResultQueryOut.getOldTxStatus())
                            || "1".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {//报文发送成功，查看业务接收了，操作失败还是成功，失败直接抛出回滚,成功不做处理
                        throw new Exception(transactionResultQueryOut.getCenterHeadOut().getRtnMessage());
                    }
                } catch (Exception ex) {
                    /**
                     * 三种可能：
                     * 1. 发送报文出错，修改业务状态，线下处理，进去
                     * 2. 回复报文出错，修改业务状态，线下处理，进去
                     * 3. 业务处理失败，线下，进去
                     * */
                    //处理报文异常
                    List<StHousingBusinessDetails> ousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                    }
                    cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
                }
            } else{
                logger.error(LogUtil.getTrace(e));
                throw new ErrorException(msg);
            }
        }
        System.out.println("djj");
    }


    //还款申请
    @Override
    public void exceptionAbnormal(Exception e, String id, String ywlsh, String yhmc) {
        /**
         * 三种可能：
         * 1. 发送报文出错，直接抛出回滚
         * 2. 回复报文出错，回查接口, 线下
         * 3. 返回失败，线下处理，修正数据
         * */
        String msg = e.getMessage();
        if (msg != null) {
            //失败处理
            CLoanHousingBusinessProcess cnHousingBusinessProcess = cloanHousingBusinessProcess.get(id);
            if (msg.contains("入账失败") || msg.contains(BankExceptionEnums.发送请求出错.getDesc()) || msg.contains(BankExceptionEnums.转换发送报文出错.getDesc()) || msg.contains("发送发流水号失败")) {
                //下载
                List<StHousingBusinessDetails> stHousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails hingBusinessDetails1 : stHousingBusinessDetails) {
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setRgcl("0");
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                    CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(hingBusinessDetails1.getYwlsh());
                    //原来的
                    byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(byYWLSH);
                }
                //新的
                cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
                return;
            } else if (msg.contains(BankExceptionEnums.接收回复出错.getDesc()) || msg.contains(BankExceptionEnums.转换回复报文出错.getDesc())) {//返回报文出错,需要调用查询接口（2）
                //需要调用查询接口，判断业务是否接受，如果没有，抛出异常,否则不作操作
                try {
                    TransactionResultQueryIn transactionResultQueryIn = iclearingBank.transResultQuery(ywlsh, yhmc);
                    TransactionResultQueryOut transactionResultQueryOut = iBank.sendMsg(transactionResultQueryIn);
                    if ("1".equals(transactionResultQueryOut.getOldTxStatus()) || "2".equals(transactionResultQueryOut.getOldTxStatus())
                            || "1".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {//报文发送成功，查看业务接收了，操作失败还是成功，失败直接抛出回滚,成功不做处理
                        throw new Exception(transactionResultQueryOut.getCenterHeadOut().getRtnMessage());
                    }
                } catch (Exception ex) {
                    /**
                     * 三种可能：
                     * 1. 发送报文出错，修改业务状态，线下处理，进去
                     * 2. 回复报文出错，修改业务状态，线下处理，进去
                     * 3. 业务处理失败，线下，进去
                     * */
                    //处理报文异常
                    List<StHousingBusinessDetails> ousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(ex.getMessage());//失败原因
                        ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                        //原来的
                        CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(ngBusinessDetails.getYwlsh());
                        byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                        cloanHousingBusinessProcess.update(byYWLSH);
                    }
                    //新的
                    cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
                    return;
                }
            } else if (msg.contains("single")) {
                List<StHousingBusinessDetails> ousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");
                    //原来的
                    CLoanHousingBusinessProcess byYWLSH = cloanHousingBusinessProcess.getByYWLSH(ngBusinessDetails.getYwlsh());
                    byYWLSH.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(byYWLSH);
                }
                //新的
                cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
                return;
            }
        } else {
            throw new ErrorException(e.getMessage());
        }
    }


    @Override
    public void exceptionBatch(Exception e, String id) {
        /**
         * 三种可能：
         * 1. 发送报文出错，直接抛出回滚
         * 2. 回复报文出错，回查接口, 线下
         * 3. 返回失败，线下处理，修正数据
         * */
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("入账失败") || msg.contains("single") || msg.contains(BankExceptionEnums.发送请求出错.getDesc()) || msg.contains(BankExceptionEnums.转换发送报文出错.getDesc())) {
                CLoanHousingBusinessProcess cnHousingBusinessProcess = cloanHousingBusinessProcess.get(id);
                List<StHousingBusinessDetails> stHousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails hingBusinessDetails1 : stHousingBusinessDetails) {
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setRgcl("0");//重扣划
                }
                cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
            } else if (msg.contains(BankExceptionEnums.接收回复出错.getDesc()) || msg.contains(BankExceptionEnums.转换回复报文出错.getDesc())) {
                CLoanHousingBusinessProcess housingBusinessProcess = cloanHousingBusinessProcess.get(id);
                List<StHousingBusinessDetails> ousingBusinessDetails = housingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因
                    ngBusinessDetails.getcHousingBusinessDetailsExtension().setRgcl("0");//重扣划
                }
                housingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(housingBusinessProcess);
            } else {
                throw new ErrorException(e.getMessage());
            }
        }

    }
    //不能往外抛
    @Override
    public void exceptionOverdueAutomatic(Exception ex, String id, String ywlsh, String khyhzhmc) {
        /**
         * 三种可能：
         * 1. 发送报文出错，直接抛出回滚
         * 2. 回复报文出错，回查接口, 线下
         * 3. 返回失败，线下处理，修正数据
         * */
        String msg = ex.getMessage();
        if (msg != null) {
            //失败处理
            CLoanHousingBusinessProcess cnHousingBusinessProcess = cloanHousingBusinessProcess.get(id);
            if (msg.contains("入账失败") || msg.contains(BankExceptionEnums.发送请求出错.getDesc()) || msg.contains(BankExceptionEnums.转换发送报文出错.getDesc()) || msg.contains("发送发流水号失败")) {
                //下载
                List<StHousingBusinessDetails> stHousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                for (StHousingBusinessDetails hingBusinessDetails1 : stHousingBusinessDetails) {
                    List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                        this.put("dkzh", hingBusinessDetails1.getDkzh());
                        this.put("yqqc", hingBusinessDetails1.getDqqc());
                        this.put("cHousingBusinessDetailsExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                    }}, null, null, null, null, null, null);
                    if (stHousingOverdueRegistrations.size() < 0) continue;
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setRgcl("0");
                    hingBusinessDetails1.getcHousingBusinessDetailsExtension().setSbyy(msg);//失败原因

                    stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                    stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                }
                cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
            } else if (msg.contains(BankExceptionEnums.接收回复出错.getDesc()) || msg.contains(BankExceptionEnums.转换回复报文出错.getDesc())) {//返回报文出错,需要调用查询接口（2）
                //需要调用查询接口，判断业务是否接受，如果没有，抛出异常,否则不作操作
                try {
                    TransactionResultQueryIn transactionResultQueryIn = iclearingBank.transResultQuery(ywlsh, khyhzhmc);
                    TransactionResultQueryOut transactionResultQueryOut = iBank.sendMsg(transactionResultQueryIn);
                    if ("1".equals(transactionResultQueryOut.getOldTxStatus()) || "2".equals(transactionResultQueryOut.getOldTxStatus())
                            || "1".equals(transactionResultQueryOut.getCenterHeadOut().getTxStatus())) {//报文发送成功，查看业务接收了，操作失败还是成功，失败直接抛出回滚,成功不做处理
                        throw new Exception(transactionResultQueryOut.getCenterHeadOut().getRtnMessage());
                    }
                } catch (Exception e) {
                    /**
                     * 三种可能：
                     * 1. 发送报文出错，修改业务状态，线下处理，进去
                     * 2. 回复报文出错，修改业务状态，线下处理，进去
                     * 3. 业务处理失败，线下，进去
                     * */
                    //处理报文异常
                    List<StHousingBusinessDetails> ousingBusinessDetails = cnHousingBusinessProcess.getStHousingBusinessDetails();
                    for (StHousingBusinessDetails ngBusinessDetails : ousingBusinessDetails) {
                        List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
                            this.put("dkzh", ngBusinessDetails.getDkzh());
                            this.put("yqqc", ngBusinessDetails.getDqqc());
                            this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
                        }}, null, null, null, null, null, null);
                        if (stHousingOverdueRegistrations.size() < 0) continue;
                        CHousingBusinessDetailsExtension singBusinessDetailsExtension = ngBusinessDetails.getcHousingBusinessDetailsExtension();
                        singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
                        singBusinessDetailsExtension.setSbyy(e.getMessage());//失败原因
                        singBusinessDetailsExtension.setRgcl("0");

                        stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
                        stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
                    }
                    cnHousingBusinessProcess.setStep(LoanBussinessStatus.入账失败.getName());
                    cloanHousingBusinessProcess.update(cnHousingBusinessProcess);
                }
            }else{
                logger.error(LogUtil.getTrace(ex));
                throw new ErrorException(msg);
            }
        }
    }

    //不能往外抛
    @Override
    public void exceptionOverdueAutomaticSingle(Exception ex, String id) {
        StHousingBusinessDetails stHousingBusinessDetails = stHousingBusinessDetailsDAO.get(id);
        CHousingBusinessDetailsExtension singBusinessDetailsExtension = stHousingBusinessDetails.getcHousingBusinessDetailsExtension();
        singBusinessDetailsExtension.setYwzt(TaskBusinessStatus.入账失败.getName());
        singBusinessDetailsExtension.setSbyy(ex.getMessage());//失败原因
        singBusinessDetailsExtension.setRgcl("0");
        List<StHousingOverdueRegistration> stHousingOverdueRegistrations = stHousingOverdueRegistrationDAO.list(new HashMap<String, Object>() {{
            this.put("dkzh", stHousingBusinessDetails.getDkzh());
            this.put("yqqc", stHousingBusinessDetails.getDqqc());
            this.put("cHousingOverdueRegistrationExtension.ywzt", LoanBussinessStatus.扣款已发送.getName());
        }}, null, null, null, null, null, null);
        stHousingOverdueRegistrations.get(0).getcHousingOverdueRegistrationExtension().setYwzt(TaskBusinessStatus.入账失败.getName());
        stHousingOverdueRegistrationDAO.update(stHousingOverdueRegistrations.get(0));
        stHousingBusinessDetailsDAO.update(stHousingBusinessDetails);
    }
}