package com.handge.housingfund.account.util;

import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.configuration2.Configuration;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by tanyi on 2017/7/12.
 */
public class EmailUtil {

    private static String emailAcount;
    private static String emailPwd;
    private static String sender;
    private static String emailHost;
    private static String emailPort;

    static {
        Configuration config = Configure.getInstance().getConfiguration("email");
        emailAcount = config.getString("emailAcount");
        emailPwd = config.getString("emailPwd");
        sender = config.getString("sender");
        emailHost = config.getString("emailHost");
        emailPort = config.getString("emailPort");
        Assert.notNull(emailAcount, "emailAcount不能为空，email.properties配置文件");
        Assert.notNull(emailPwd, "emailPwd不能为空，请检查redis.properties配置文件");
        Assert.notNull(sender, "sender不能为空，请检查redis.properties配置文件");
        Assert.notNull(emailHost, "emailHost不能为空，请检查redis.properties配置文件");
        Assert.notNull(emailPort, "emailPort不能为空，请检查redis.properties配置文件");
    }


    /**
     * 发送邮件
     *
     * @param email   接收人邮件
     * @param name    接收人姓名
     * @param subject 主题
     * @param content 内容
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public static void sendEmail(String email, String name, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", emailHost);
        properties.setProperty("mail.smtp.port", emailPort);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", emailPort);
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(false);
        MimeMessage message = createMessage(session, email, name, subject, content);
        Transport transport = session.getTransport();
        transport.connect(emailAcount, emailPwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 生成邮件信息
     *
     * @param session
     * @param receivemail
     * @param name
     * @param subject
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private static MimeMessage createMessage(Session session, String receivemail, String name, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAcount, sender, "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receivemail, name, "UTF-8"));
        message.setSubject(subject, "UTF-8");
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    public static String createEmailContent(String username, String url, String time) {
        String temp = "<style>\n" +
                "        *{\n" +
                "            margin: 0;padding: 0;\n" +
                "        }\n" +
                "        a{\n" +
                "            text-decoration: none;\n" +
                "            color: #356fa8;\n" +
                "        }  \n" +
                "        a:hover {\n" +
                "            text-decoration: none;\n" +
                "        } \n" +
                "        img {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            border: none;\n" +
                "        }\n" +
                "        .bjgjj-mail{\n" +
                "            font-size: 14px;\n" +
                "            font-family: '微软雅黑';\n" +
                "            width: 80%;\n" +
                "            margin:20px auto 20px;\n" +
                "            border:1px solid #c6c6c6;\n" +
                "            border-radius: 2px;\n" +
                "        }\n" +
                "        .bjgjj-mailTitle{\n" +
                "            width: 100%;\n" +
                "            background: #356fa8;\n" +
                "            border-radius: 2px;\n" +
                "        }\n" +
                "        .bjgjj-mailTitle span{\n" +
                "            color: #fff;\n" +
                "            line-height: 42px;\n" +
                "            padding-left: 30px;\n" +
                "        }\n" +
                "        .bjgjj-mailTitle span img{\n" +
                "            vertical-align: middle;\n" +
                "            margin-top: -6px; \n" +
                "            margin-right: 10px;\n" +
                "        }\n" +
                "        .bjgjj-mailInner{\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "        .bjgjj-mailInner p{\n" +
                "            line-height: 35px;\n" +
                "        }\n" +
                "        .bjgjj-main{\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .bjgjj-main a{\n" +
                "            display: inline-block;\n" +
                "            margin: 10px 0;\n" +
                "            word-wrap:break-word; \n" +
                "            width: 100%; \n" +
                "        }\n" +
                "        .bjgjj-footer{\n" +
                "            text-align: right;\n" +
                "            padding-top: 50px;\n" +
                "        }\n" +
                "        @media screen and (max-width: 1280px){\n" +
                "            .bjgjj-mail{\n" +
                "                font-size: 10px;\n" +
                "                width: 96%;\n" +
                "            }\n" +
                "            .bjgjj-mailInner{\n" +
                "                margin: 5px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "        <div class=\"bjgjj-mail\">\n" +
                "            <div class=\"bjgjj-mailTitle\">\n" +
                "                <span><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAACXBIWXMAAAsSAAALEgHS3X78AAABCElEQVQ4y62V0Y2DMBBEX1D+QwmUQAdJB3clpISUcCVQgkugBJdACXRwTgVzP4vOWHYwiJFWQt7lYRbvcJHEQXVAsFip4bh+gF9gANpVRtJWPCy6ZD3oX1Ocz0FaSS8rTBUk9ZK+M7nJ7iW3q6CygtW5Qn5MoU9ty9luSpoltQuwV536wsNnW1/1dKoAzlY7Jn18pt+FQtNzGqJX99GJcBYrqNvx6l10xHySX0F9BXCKTkep/rFAr8C9YnpmwFfWcgXewG2j7mvP/DbAxDkKZ0PfMacB3AnQMedSXscVUgdbLroNI/mkV26i4vnfCx5yHpwudJWtCDbe1EBjX3VmIjHIm4G0n/4Wf/aPL9kGI47RAAAAAElFTkSuQmCC\">毕节市住房公积金密码重置邮件</span>\n" +
                "            </div>\n" +
                "            <div class=\"bjgjj-mailInner\">\n" +
                "                <p>尊敬的用户" + username + "，您好：</p>\n" +
                "                <p>    感谢使用住房公积金系统，您发起了重置密码服务。</p>\n" +
                "                <div class=\"bjgjj-main\">\n" +
                "                    <p>点击下面的链接，跳转到重置密码页面，若链接无法点击，请直接复制到浏览器地址栏打开：</p>\n" +
                "                    <a href=\"javascript:void(0)\" href=\"" + url + "\">" + url + "</a>\n" +
                "                    <p>如果您未提交申请，请忽略此邮件，为此给您带来的不便我们深表歉意。</p>\n" +
                "                </div>\n" +
                "                <div class=\"bjgjj-footer\">\n" +
                "                    <p>毕节市住房公积金</p>\n" +
                "                    <p>" + time + "</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "</div>\n";
        return temp;
    }

}
