package com.handge.housingfund.common.service.others;

import java.util.ArrayList;

/**
 * Created by tanyi on 2018/1/8.
 * 短信平台接口,提供给各服务
 */
public interface ISMSCommon {
    //单发短信
   void sendSingleSMS(String phone, String content);

    //使用模板单发短信
   void sendSingleSMSWithTemp(String phone, int temp, ArrayList<String> params);

    //群发短信
   void sendMultiSMS(ArrayList<String> phones, String content);

    //使用模板群发短信
   void sendMultiSMSWithTemp(ArrayList<String> phones, int temp, ArrayList<String> params);

   //查询余额
   void getbalance();
}
