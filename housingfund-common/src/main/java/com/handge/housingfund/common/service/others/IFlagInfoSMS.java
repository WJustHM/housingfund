package com.handge.housingfund.common.service.others;

import java.util.ArrayList;

/**
 * Created by gxy on 18-1-24.
 */
public interface IFlagInfoSMS extends ISMSCommon {
    /**
     * 单发短信
     *
     * @param phone   手机号码
     * @param content 内容
     * @return
     */
    boolean sendSingle(String phone, String content);

    /**
     * 群发短信
     *
     * @param phones  手机号码
     * @param content 内容
     * @return
     */
    boolean sendMulti(ArrayList<String> phones, String content);

    /**
     * 上行回复内容查询接口
     */
    void reply();

    /**
     * 上行回复内容确认接口
     * @param id 最后一条信息的id
     */
    void replyConfirm(String id);

    /**
     * 回执接口,查询短信发送状态
     */
    void report();

    /**
     * 查询余额接口
     */
    void getBalance();
}
