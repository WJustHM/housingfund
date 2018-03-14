package com.handge.housingfund.others.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.others.IFlagInfoSMS;
import com.handge.housingfund.common.service.sms.SmsStub;
import com.handge.housingfund.common.service.util.LogUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gxy on 18-1-24.
 */
@SuppressWarnings("Duplicates")
public class FlagInfoSMS implements IFlagInfoSMS {

    private static Logger logger = LogManager.getLogger(FlagInfoSMS.class);

    private static Configuration cfg = Configure.getInstance().getConfiguration("sms");

    private static final String id = cfg.getString("id");

    private static final String userName = cfg.getString("userName");

    private static final String password = cfg.getString("password");

    private static final ArrayList<String> tempList = new ArrayList<>();

    static {
        Iterator<String> keys = cfg.getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.contains("temp")) tempList.add(cfg.getString(key));
        }
    }

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //region-----------------------FlagInfo接口-----------------------------
    @Override
    public boolean sendSingle(String phone, String content) {
        try {
            SmsStub stub = new SmsStub("https://api.ums86.com:9600/sms_hb/services/Sms?wsdl");

            SmsStub.Sms sms0 = new SmsStub.Sms();
            sms0.setIn0(id);//企业编号
            sms0.setIn1(userName);//登录名
            sms0.setIn2(password);//密码
            sms0.setIn3(content);//短信内容
            sms0.setIn4(phone);//手机号码
            sms0.setIn5("000" + format.format(new Date()));//流水号
            sms0.setIn6("");//预约发送时间，格式:yyyyMMddHHmmss
            sms0.setIn7("1");//提交时检测方式

            SmsStub.SmsResponse resp = stub.sms(sms0);
            stub.cleanup();//使用完后cleanup
            logger.info(resp.getOut());

            String respOut = resp.getOut();
            HashMap<String, String> respMap = str2map(respOut);
            logger.info("respOut2Map: " + respMap);
        } catch (RemoteException e) {
            logger.error(LogUtil.getTrace(e));
        }

        return true;
    }

    @Override
    public boolean sendMulti(ArrayList<String> phones, String content) {
        String phoneS = StringUtils.join(phones.toArray(), ",");

        return sendSingle(phoneS, content);
    }

    @Override
    public void reply() {
        try {
            SmsStub stub = new SmsStub("https://api.ums86.com:9600/sms_hb/services/Sms?wsdl");

            SmsStub.ReplyRequest replyRequest = new SmsStub.ReplyRequest();
            replyRequest.setIn0(id);//企业编号
            replyRequest.setIn1(userName);//登录名
            replyRequest.setIn2(password);//密码

            SmsStub.ReplyResponse resp = stub.reply(replyRequest);
            stub.cleanup();//使用完后cleanup
            System.out.println(resp.getResult());
            SmsStub.Reply[] replys = resp.getReplys();

            if (replys != null) {
                for (SmsStub.Reply reply : replys) {
                    System.out.println(reply.getMdn() + "," + reply.getContent());
                }
            }

            replyConfirm(resp.getId());
        } catch (RemoteException e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    @Override
    public void replyConfirm(String lastId) {
        try {
            SmsStub stub = new SmsStub("https://api.ums86.com:9600/sms_hb/services/Sms?wsdl");

            SmsStub.ReplyConfirmRequest confirm = new SmsStub.ReplyConfirmRequest();
            confirm.setIn0(id);//企业编号
            confirm.setIn1(userName);//登录名
            confirm.setIn2(password);//密码
            confirm.setIn4(lastId);

            SmsStub.ReplyConfirmResponse resp = stub.replyConfirm(confirm);
            stub.cleanup();//使用完后cleanup
            System.out.println(resp.getResult());
        } catch (RemoteException e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    @Override
    public void report() {
        try {
            SmsStub stub = new SmsStub("https://api.ums86.com:9600/sms_hb/services/Sms?wsdl");

            SmsStub.Report report = new SmsStub.Report();
            report.setIn0(id);//企业编号
            report.setIn1(userName);//登录名
            report.setIn2(password);//密码

            SmsStub.ReportResponse resp = stub.report(report);
            stub.cleanup();//使用完后cleanup
            System.out.println(resp.getOut());
        } catch (RemoteException e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    @Override
    public void getBalance() {
        try {
            SmsStub stub = new SmsStub("https://api.ums86.com:9600/sms_hb/services/Sms?wsdl");

            SmsStub.SearchSmsNumRequest searchSmsNumRequest = new SmsStub.SearchSmsNumRequest();
			searchSmsNumRequest.setIn0(id);//企业编号
			searchSmsNumRequest.setIn1(userName);//登录名
			searchSmsNumRequest.setIn2(password);//密码

			SmsStub.SearchSmsNumResponse resp= stub.searchSmsNum(searchSmsNumRequest);
            stub.cleanup();//使用完后cleanup
			System.out.println(resp.getResult());
			System.out.println(resp.getNumber());
        } catch (RemoteException e) {
            logger.error(LogUtil.getTrace(e));
        }
    }

    private HashMap<String, String> str2map(String respOut) {
        HashMap<String, String> respMap = new HashMap<>();

        String[] strs = respOut.split("&");
        for (String str : strs) {
            String[] subStr = str.split("=", -1);
            respMap.put(subStr[0], subStr[1]);
        }

        return respMap;
    }
    //endregion

    //region-----------------------公积金系统接口-----------------------------
    @Override
    public void sendSingleSMS(String phone, String content) {
        logger.info("号码： " + phone + "， 内容： " + content);
        boolean isSuccess = false;
        int times = 0;

        while (!isSuccess) {
            isSuccess = sendSingle(phone, content);

            if (!isSuccess) {
                times++;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (times == 2) break;
            }
        }
    }

    @Override
    public void sendSingleSMSWithTemp(String phone, int temp, ArrayList<String> params) {
        logger.info("号码： " + phone + "， 内容： " + Arrays.toString(params.toArray()));
        String content = temp2content(temp, params);

        sendSingleSMS(phone, content);
    }

    @Override
    public void sendMultiSMS(ArrayList<String> phones, String content) {
        logger.info("号码： " + Arrays.toString(phones.toArray()) + "， 内容： " + content);
        boolean isSuccess = false;
        int times = 0;

        while (!isSuccess) {
            isSuccess = sendMulti(phones, content);

            if (!isSuccess) {
                times++;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (times == 2) break;
            }
        }
    }

    @Override
    public void sendMultiSMSWithTemp(ArrayList<String> phones, int temp, ArrayList<String> params) {
        logger.info("号码： " + Arrays.toString(phones.toArray()) + "， 内容： " + Arrays.toString(params.toArray()));
        String content = temp2content(temp, params);

        sendMultiSMS(phones, content);
    }

    @Override
    public void getbalance() {
        getBalance();
    }
    //endregion

    private String temp2content(int temp, ArrayList<String> params) {
        String content = tempList.get(temp - 1);
        for (String param : params) {
            content = content.replaceFirst("xx", param);
        }

        return content;
    }
}
