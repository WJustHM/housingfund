package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.configure.Configure;
import com.tienon.io.tcp.RefbdcTcpClient;
import com.tienon.util.RefbdcUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;

/**
 * 发送器,负责发送报文到住建部以及接收返回报文
 * Created by gxy on 17-6-20.
 */
@SuppressWarnings("Duplicates")
public class Sender {
    private static Logger logger = LogManager.getLogger(Sender.class);
    private byte[] currentSessionKey;
    private Configuration cfg = Configure.getInstance().getConfiguration("bank");

    public Sender() throws Exception {
    }

    /**
     * 接口探测
     *
     * @param xml 请求xml
     * @return response
     * @throws Exception 异常
     */
    public String interfaceCheck(String xml) throws Exception {
        RefbdcTcpClient cc = new RefbdcTcpClient(
                cfg.getString("serverIp"),
                cfg.getInt("serverPort"),
                cfg.getString("nodeNo"),
                cfg.getInt("timeout")
        );

        //接口探测
        byte[] ret = cc.login(xml.getBytes());
        String response = new String(ret);
        logger.info("回复报文:\n" + response);
        return response;
    }

    /**
     * 签到,获取SessionKey
     *
     * @param xml 请求xml
     * @return response
     * @throws Exception 异常
     */
    public String login(String xml) throws Exception {
        RefbdcTcpClient cc = new RefbdcTcpClient(
                cfg.getString("serverIp"),
                cfg.getInt("serverPort"),
                cfg.getString("nodeNo"),
                cfg.getInt("timeout")
        );

        //签到
        byte[] ret = cc.login(xml.getBytes());
        String response = new String(ret, "GBK");
        logger.info("签到回复报文:\n" + response);

        String key = getSessionKey(response);
        byte[] sessionKey;
        try {
            sessionKey = RefbdcUtil.decryptSessionKey(cfg.getString("publicKey"), key);
        } catch (Exception e) {
            throw new Exception("获取sessionKey明文异常. " + LogUtil.getTrace(e));
        }
        if (sessionKey == null) {
            throw new Exception("sessionKey为空");
        }
        currentSessionKey = sessionKey;

        //打印sessionkey
        StringBuffer strbuf = new StringBuffer();
        for (byte aSessionKey : sessionKey) {
            strbuf.append(aSessionKey).append("|");
        }
        logger.info("SessionKey:" + strbuf.toString());

        //持久化sessionKey
        FileUtil.write(cfg.getString("sessionKey"), sessionKey);

        return response;
    }

    /**
     * 业务
     *
     * @param reqXml 请求xml
     * @return respXml 响应xml
     * @throws Exception 异常
     */
    public String invoke(String reqXml) throws Exception {
        RefbdcTcpClient cc = new RefbdcTcpClient(
                cfg.getString("serverIp"),
                cfg.getInt("serverPort"),
                cfg.getString("nodeNo"),
                cfg.getInt("timeout")
        );

        //获取持久化的sessionKey
        byte[] sessionKey = null;
        try {
            sessionKey = FileUtil.read(cfg.getString("sessionKey"));
        } catch (Exception e) {
            FileUtil.createFile(cfg.getString("sessionKey"));
        }
        if (sessionKey == null || sessionKey.length <= 0) {
            String xml;
            try {
                xml = new CreateLoginXml().getLoginXml();
                login(xml);
            } catch (Exception e) {
                throw new Exception("签到出错. " + LogUtil.getTrace(e));
            }
        } else {
            currentSessionKey = sessionKey;

            //打印sessionkey
            StringBuffer strbuf = new StringBuffer();
            for (byte aSessionKey : sessionKey) {
                strbuf.append(aSessionKey).append("|");
            }
            logger.info("SessionKey:" + strbuf.toString());
        }

        //发送交易业务
        byte[] request;
        try {
            request = RefbdcUtil.encryptMsg(reqXml.getBytes("GBK"), currentSessionKey);
        } catch (Exception e) {
            throw new Exception("加密报文异常. " + LogUtil.getTrace(e));
        }

        byte[] ret = cc.invokeFront(request);

        byte[] response;
        try {
            response = RefbdcUtil.decryptMsg(ret, currentSessionKey);
        } catch (Exception e) {
            throw new Exception("解密报文异常. " + LogUtil.getTrace(e));
        }
        return new String(response, "GBK");
    }

    /**
     * 获取sessionKey
     *
     * @param xmlMsg 响应xml
     * @return sessionKey
     * @throws DocumentException 异常
     */
    private static String getSessionKey(String xmlMsg) throws Exception {
        SAXReader reader = new SAXReader();
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlMsg.getBytes());
        Document doc = null;
        try {
            doc = reader.read(bais);
        } catch (DocumentException e) {
            throw new Exception("解析XML异常. " + LogUtil.getTrace(e));
        }
        Element root = doc.getRootElement();
        Element body = root.element("body");
        Element field = body.element("field");

        return field.getText();
    }

    //模拟，直接发送到bankserver
    public String invoke2(String reqXml) throws Exception {
        RefbdcTcpClient cc = new RefbdcTcpClient(
                cfg.getString("centerIp"),
                cfg.getInt("centerPort"),
                cfg.getString("nodeNo"),
                cfg.getInt("timeout")
        );

        //获取持久化的sessionKey
        byte[] sessionKey = null;
        try {
            sessionKey = FileUtil.read(cfg.getString("sessionKey"));
        } catch (Exception e) {
            FileUtil.createFile(cfg.getString("sessionKey"));
        }
        if (sessionKey == null || sessionKey.length <= 0) {
            String xml;
            try {
                xml = new CreateLoginXml().getLoginXml();
            } catch (Exception e) {
                throw new Exception("构造签到报文出错. " + LogUtil.getTrace(e));
            }
            login(xml);
        } else {
            currentSessionKey = sessionKey;

            //打印sessionkey
            StringBuffer strbuf = new StringBuffer();
            for (byte aSessionKey : sessionKey) {
                strbuf.append(aSessionKey).append("|");
            }
            logger.info("SessionKey:" + strbuf.toString());
        }

        //发送交易业务
        byte[] request;
        try {
            request = RefbdcUtil.encryptMsg(reqXml.getBytes("GBK"), currentSessionKey);
        } catch (Exception e) {
            throw new Exception("加密报文异常. " + LogUtil.getTrace(e));
        }

        byte[] ret = cc.invokeFront(request);

        byte[] response;
        try {
            response = RefbdcUtil.decryptMsg(ret, currentSessionKey);
        } catch (Exception e) {
            throw new Exception("解密报文异常. " + LogUtil.getTrace(e));
        }
        return new String(response, "GBK");
    }
}
