package com.handge.housingfund.common.service.util;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created by 向超 on 2017/8/31.
 */
public class Message {
    public static String sendMessage(String phoneNumber,String messageContent){
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn/");
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
        NameValuePair[] data = { new NameValuePair("Uid", "xiangchao123"), // 注册的用户名
                new NameValuePair("Key", "add5c3729b9d3a2d244b"), // 注册成功后,登录网站使用的密钥
                new NameValuePair("smsMob", phoneNumber), // 手机号码
                new NameValuePair("smsText", messageContent) };//设置短信内容

        post.setRequestBody(data);
        String result = "";
        try {
            client.executeMethod(post);
            Header[] headers = post.getResponseHeaders();
            int statusCode = post.getStatusCode();
            System.out.println("statusCode:" + statusCode);
            for (Header h : headers) {
                System.out.println(h.toString());
            }
            result = new String(post.getResponseBodyAsString().getBytes(
                    "gbk"));
            System.out.println(result);
            post.releaseConnection();
        }catch (Exception e){
            System.out.println("发送短信失败");
            e.printStackTrace();
        }
        return result;
    }
}
