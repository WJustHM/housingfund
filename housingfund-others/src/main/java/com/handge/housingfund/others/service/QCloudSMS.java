//package com.handge.housingfund.others.service;
//
//import com.github.qcloudsms.SmsMultiSender;
//import com.github.qcloudsms.SmsMultiSenderResult;
//import com.github.qcloudsms.SmsSingleSender;
//import com.github.qcloudsms.SmsSingleSenderResult;
//import com.handge.housingfund.common.configure.Configure;
//import com.handge.housingfund.common.service.others.IQCloudSMS;
//import com.handge.housingfund.common.service.util.LogUtil;
//import org.apache.commons.configuration2.Configuration;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// * Created by tanyi on 2018/1/8.
// */
//public class QCloudSMS implements IQCloudSMS {
//
//    private static Logger logger = LogManager.getLogger(QCloudSMS.class);
//
//    private static Configuration cfg = Configure.getInstance().getConfiguration("sms");
//
//    private static final int appId = cfg.getInt("appId");
//
//    private static final String appKey = cfg.getString("appKey");
//
//    //-----------------------QCloudSMS接口-----------------------------
//    @Override
//    public boolean sendSingleTemp(String phone, int temp, ArrayList<String> params) {
//        try {
//            SmsSingleSender sender = new SmsSingleSender(appId,appKey);
//
//            SmsSingleSenderResult result = sender.sendWithParam("86", phone, temp, params, "", "", "");
//            logger.info(result);
//
//            return 0 == result.result;
//        } catch (Exception e) {
//            logger.error(LogUtil.getTrace(e));
//            return false;
//        }
//    }
//
//    @Override
//    public boolean sendSingle(String phone, String content) {
//        try {
//            SmsSingleSender sender = new SmsSingleSender(appId, appKey);
//
//            SmsSingleSenderResult result = sender.send(0, "86", phone, content, "", "");
//            logger.info(result);
//
//            return 0 == result.result;
//        } catch (Exception e) {
//            logger.error(LogUtil.getTrace(e));
//            return false;
//        }
//    }
//
//    @Override
//    public boolean sendMultiTemp(ArrayList<String> phones, int temp, ArrayList<String> params) {
//        try {
//            SmsMultiSender multiSender = new SmsMultiSender(appId, appKey);
//
//            SmsMultiSenderResult multiSenderResult = multiSender.sendWithParam("86", phones, temp, params, "", "", "");
//            logger.info(multiSenderResult);
//
//            return 0 == multiSenderResult.result;
//        } catch (Exception e) {
//            logger.error(LogUtil.getTrace(e));
//            return false;
//        }
//    }
//
//    @Override
//    public boolean sendMulti(ArrayList<String> phones, String content) {
//        try {
//            SmsMultiSender multiSender = new SmsMultiSender(appId, appKey);
//
//            SmsMultiSenderResult multiSenderResult = multiSender.send(0, "86", phones, content, "", "");
//            logger.info(multiSenderResult);
//
//            return 0 == multiSenderResult.result;
//        } catch (Exception e) {
//            logger.error(LogUtil.getTrace(e));
//            return false;
//        }
//    }
//
//    //-----------------------公积金系统接口-----------------------------
//    @Override
//    public void sendSingleSMS(String phone, String content) {
//        logger.info("号码： " + phone + "， 内容： " + content);
//        boolean isSuccess = false;
//        int times = 0;
//
//        while (!isSuccess) {
//            isSuccess = sendSingle(phone, content);
//            times++;
//            if (times == 5) break;
//        }
//    }
//
//    @Override
//    public void sendSingleSMSWithTemp(String phone, int temp, ArrayList<String> params) {
//        logger.info("号码： " + phone + "， 内容： " + Arrays.toString(params.toArray()));
//        boolean isSuccess = false;
//        int times = 0;
//
//        while (!isSuccess) {
//            isSuccess = sendSingleTemp(phone, temp, params);
//            times++;
//            if (times == 5) break;
//        }
//    }
//
//    @Override
//    public void sendMultiSMS(ArrayList<String> phones, String content) {
//        logger.info("号码： " + Arrays.toString(phones.toArray()) + "， 内容： " + content);
//        boolean isSuccess = false;
//        int times = 0;
//
//        while (!isSuccess) {
//            isSuccess = sendMulti(phones, content);
//            times++;
//            if (times == 5) break;
//        }
//    }
//
//    @Override
//    public void sendMultiSMSWithTemp(ArrayList<String> phones, int temp, ArrayList<String> params) {
//        logger.info("号码： " + Arrays.toString(phones.toArray()) + "， 内容： " + Arrays.toString(params.toArray()));
//        boolean isSuccess = false;
//        int times = 0;
//
//        while (!isSuccess) {
//            isSuccess = sendMultiTemp(phones, temp, params);
//            times++;
//            if (times == 5) break;
//        }
//    }
//}
