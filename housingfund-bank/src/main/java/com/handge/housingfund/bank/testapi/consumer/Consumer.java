package com.handge.housingfund.bank.testapi.consumer;

import com.handge.housingfund.bank.testapi.bank.BankDao;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Created by xuefei on 17-6-18.
 */
@SuppressWarnings("Duplicates")
public class Consumer {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"consumer.xml"});
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"META-INF/spring/application-context.xml"});
        context.start();

        IBank iBank = (IBank) context.getBean("iBank");
//        ITransfer iTransfer = (ITransfer) context.getBean("iTransfer");
//        IReSendNotice iReSendNotice = (IReSendNotice) context.getBean(IReSendNotice.class);
        try {
//            BankDao.send(iBank);
//            BankDao.sendTransfer(iTransfer);
//            BankDao.reSendNotice(iReSendNotice);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        String[] ywlsh = new String[]{
//                "021711166005691",
//                "021711096003997",
//                "021711146005472",
//                "021711176010586",
//                "021711176010588",
//                "021711176010600",
//                "021711176010598",
//                "021711076005375",
//                "021711106005165",
//                "021711176010337",
//                "021711176010589",
//                "021711176010595",
//                "021711156006968",
//                "021711156006969",
//                "021711176010404",
//                "021711156006915",
//                "021711166005828",
//                "021711176010448",
//                "021711016000763",
//                "021711176010387",
//                "021711176010287",
//                "021711106005149",
//                "021711156006860",
//                "021711166005832",
//                "021711166005914",
//                "021711166005915",
//                "021711176010429"
//        };
//
//        IReset iReset = (IReset) context.getBean("iReset");
//        for (String lsh : ywlsh) {
//            System.out.println(lsh);
//            iReset.reset(lsh);
//        }

//        ISMSCommon ismsCommon = (ISMSCommon) context.getBean("iSMSCommon");

        //--QCloudSMS
//        ismsCommon.sendSingleSMS("18683258415", "【五舟汉云】您的登陆验证码为5555，有效期60s，请勿泄漏。");
//        ismsCommon.sendSingleSMSWithTemp("17628289250", 74389, new ArrayList<String>(){{this.add("6556");this.add("60s");}});
//        ismsCommon.sendMultiSMSWithTemp(new ArrayList<String>(){{this.add("15708444776");this.add("18683258415");}}, 74389, new ArrayList<String>(){{this.add("6556");this.add("60s");}});

        //--FlagInfoSMS
//        ismsCommon.sendSingleSMS("18683258415", "你有一项编号为78965的事务需要处理。");
//        ismsCommon.sendMultiSMS(new ArrayList<String>(){{this.add("18683258415");this.add("17628289250");}}, "您的验证码为：123456");

        ISMSCommon ismsCommon = (ISMSCommon) context.getBean("iSMSCommon");
        ismsCommon.sendSingleSMSWithTemp("18286721207", SMSTemp.年度结息.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                    this.add("999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.个人账户信息变更.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.异地转移接续转出.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                    this.add("遵义市");
                    this.add("999999.99");
                    this.add("999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.异地转移接续转入.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                    this.add("遵义市");
                    this.add("999999.99");
                    this.add("9999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.汇缴.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                    this.add("9999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.汇缴逾期催缴.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.提取.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                    this.add("完全丧失劳动能力，并与单位终止劳动关系");
                    this.add("999999.99");
                    this.add("99999.99");
                    this.add("9999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.提前还款业务发起时.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("999999.99");
                    this.add("07");
                    this.add("07");
                    this.add("01");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.提前还款扣款后.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("9999999.99");
                    this.add("999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.委扣余额充足.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.委扣余额不足.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.非委扣.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.正常还款后.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("07");
                    this.add("01");
                    this.add("99999.99");
                    this.add("9999.99");
                    this.add("9999.99");
                    this.add("999");
                    this.add("999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.贷款结清.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("2017");
                    this.add("07");
                    this.add("01");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.贷款信息变更.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("07");
                    this.add("01");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.贷款逾期催收.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("6556");
                    this.add("999");
                    this.add("999999.99");
                }}
        );
        ismsCommon.sendSingleSMSWithTemp("18683258415", SMSTemp.贷款发放.getCode(),
                new ArrayList<String>() {{
                    this.add("李科长");
                    this.add("999999.99");
                    this.add("01");
                    this.add("99999.99");
                }}
        );
//        ismsCommon.getbalance();
//        ismsCommon.sendMultiSMSWithTemp(new ArrayList<String>(){{this.add("18683258415");this.add("11111111111");}}, 1, new ArrayList<String>(){{this.add("6556");this.add("60s");}});
    }
}

