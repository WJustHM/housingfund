package com.handge.housingfund.common.service.others;

import java.util.ArrayList;

/**
 * Created by tanyi on 2018/1/8.
 * 腾讯短信平台接口
 */
public interface IQCloudSMS extends ISMSCommon{

    /**
     * 指定模板单发短信
     *
     * @param phone  手机号码
     * @param temp   模板编号
     * @param params 参数
     * @return
     */
    boolean sendSingleTemp(String phone, int temp, ArrayList<String> params);

    /**
     * 单发短信
     *
     * @param phone   手机号码
     * @param content 内容
     * @return
     */
    boolean sendSingle(String phone, String content);

    /**
     * 指定模板群发短信
     *
     * @param phones 手机号码
     * @param temp   模板编号
     * @param params 参数
     * @return
     */
    boolean sendMultiTemp(ArrayList<String> phones, int temp, ArrayList<String> params);

    /**
     * 群发短信
     *
     * @param phones  手机号码
     * @param content 内容
     * @return
     */
    boolean sendMulti(ArrayList<String> phones, String content);

}
