package com.handge.housingfund.bank.server;

import com.handge.housingfund.bank.server.service.CenterImpl;
import com.handge.housingfund.bank.server.service.CenterTransferImpl;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.bean.head.SysHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckIn;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckOut;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import com.handge.housingfund.common.service.util.*;
import com.tienon.util.ByteArrayUtil;
import com.tienon.util.RefbdcUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * 公积金中心作为服务端, 接收住建部发送过来的消息
 */
@Service
@Component
public class CRFCenterServer {

    @Autowired
    private ICenter iCenter;

    @Autowired
    private ICenterTransfer iCenterTransfer;

    private static Logger logger = LogManager.getLogger(CRFCenterServer.class);

    private static Configuration cfg = Configure.getInstance().getConfiguration("bank");

    private static Selector roller = null;

    private static final int port = cfg.getInt("centerPort");

    private static final String host = cfg.getString("centerIp");

    private CRFCenterServer() throws IOException {
    }

    private void init() throws IOException {
        roller = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(host, port));
        serverChannel.configureBlocking(false);
        serverChannel.register(roller, SelectionKey.OP_ACCEPT);
    }

    void start() throws Exception {
        init();
        while (roller.select() > 0) {
            Set<SelectionKey> keySets = roller.selectedKeys();
            Iterator iter = keySets.iterator();
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                iter.remove();
                actionHandler(key);
            }
        }
    }

    private void actionHandler(SelectionKey key) throws Exception {
        if (key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
//            socketChannel.configureBlocking(false);
//            socketChannel.register(roller, SelectionKey.OP_READ);
//        } else if(key.isReadable()) {
//            SocketChannel socketChannel = (SocketChannel) key.channel();
            TcpSocketIO tcpIO = new TcpSocketIO(socketChannel);
            ByteBuffer headerBuf = ByteBuffer.allocate(16);
            ByteBuffer lenbuf = ByteBuffer.allocate(4);
            byte[] reqMsg;
            String xmlMsg = "";
            //获取持久化的sessionKey
            byte[] sessionKey = FileUtil.read(cfg.getString("sessionKey"));
            //接收消息
            try {
                byte[] buf = tcpIO.read(0, 16);
                if (buf == null) {
                    this.close(socketChannel);
                    return;
                }
                headerBuf = ByteBuffer.wrap(buf);

                lenbuf = ByteBuffer.wrap(tcpIO.read(0, 4));
                int contentLength = (int) ByteArrayUtil.NET2DATA(lenbuf.array());
                ByteBuffer reqBuffer = ByteBuffer.wrap(tcpIO.read(0, contentLength));
                reqMsg = reqBuffer.array();
                String headStr = new String(headerBuf.array());
                if (headStr.charAt(6) == '1') {
                    //签到,不需要解密
                    xmlMsg = new String(reqMsg,"GBK");
//                  scheme(xmlMsg);
                } else {
                    //业务,需要解密
                    byte[] decReq = RefbdcUtil.decryptMsg(reqMsg, sessionKey);
                    xmlMsg = new String(decReq,"GBK");
//                  scheme(xmlMsg);
                }
                logger.info("从住建部收到的报文:\n" + JAXBUtil.formatXml(xmlMsg, "notice"));
            } catch (Exception e) {
                logger.error("reqXml: \n" + xmlMsg);
                logger.error(LogUtil.getTrace(e));
//                this.close(socketChannel);
            }

            try {
                String respXml = null;
                byte[] resp = null;

                //判断属于哪个接口
                String txCode = getTxCode(xmlMsg);
                if ("SYS600".equals(txCode)) {
                    respXml = transferAndSend(xmlMsg, "InterfaceCheckOut", txCode);
                    resp = respXml.getBytes("GBK");
                } else if("BDC901".equals(txCode)) {
                    respXml = transferAndSend(xmlMsg, "SingleTransInApplIn", txCode);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                } else if("BDC903".equals(txCode)) {
                    respXml = transferAndSend(xmlMsg, "TransInApplCancelIn", txCode);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                } else if("BDC904".equals(txCode)) {
                    respXml = transferAndSend(xmlMsg, "ApplScheduleQueryIn", txCode);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                } else if("BDC905".equals(txCode)) {
                    respXml = transferAndSend(xmlMsg, "SingleTransOutInfoIn", txCode);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                } else if ("SBDC100".equals(txCode)){
                    respXml = transferAndSend(xmlMsg, "AccChangeNoticeOut", txCode);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                } else {
                    CenterHeadOut centerHeadOut = CenterTransferImpl.getCenterHeadOut("1", "", txCode, "");
                    Object respObj = new SingleTransInApplOut(centerHeadOut);

                    Message message = new ParmBean2XMLBean().transfer(respObj);
                    respXml = JAXBUtil.toXML(message);
                    resp = RefbdcUtil.encryptMsg(respXml.getBytes("GBK"), sessionKey);
                }

                //清空缓冲区
                headerBuf.clear();
                lenbuf.clear();

                //回复消息
                byte[] header = new byte[16];
                byte[] nodeNoByte = ("C52240" + "0").getBytes();
                System.arraycopy(nodeNoByte, 0, header, 0, nodeNoByte.length);
                headerBuf = ByteBuffer.wrap(header);
                byte[] buf = new byte[4];
                ByteArrayUtil.DATA2NET(resp.length, buf);
                lenbuf = ByteBuffer.wrap(buf);
                ByteBuffer requestBuffer = ByteBuffer.wrap(resp);

                socketChannel.write(headerBuf);
                socketChannel.write(lenbuf);
                socketChannel.write(requestBuffer);
            } catch (Exception e) {
                logger.error(LogUtil.getTrace(e));
                this.close(socketChannel);
//                throw new Exception("回复消息出错." + e.getMessage());
            }
            this.close(socketChannel);
        }
    }

    /**
     * 对象和xml报文的相互转换,并与住建部进行通信
     *
     * @param reqXml 请求xml
     * @return respXml
     * @throws Exception 异常
     * @parm returnType
     */
    private String transferAndSend(String reqXml, String returnType, String txCode) throws Exception {
        Object reqObj;
        Object respObj;
        String respXml;

        try {
            Message reqMsg = JAXBUtil.createInstanceFromXML(reqXml);
            reqObj = new XMLBean2ParmBean().transfer(reqMsg, returnType);
        } catch (Exception e) {
            logger.error("reqXml: \n" + reqXml);
            logger.error(LogUtil.getTrace(e));

            if ("InterfaceCheckOut".equals(returnType) || "AccChangeNoticeOut".equals(returnType)) {
                SysHeadIn sysHeadIn = CenterImpl.getSysHeadIn("1", "", txCode, "");
                respObj = new InterfaceCheckIn(sysHeadIn);
            } else if ("SingleTransInApplIn".equals(returnType)) {  //单笔转入申请
                CenterHeadOut centerHeadOut = CenterTransferImpl.getCenterHeadOut("1", "", txCode, "");
                respObj = new SingleTransInApplOut(centerHeadOut);
            } else if ("TransInApplCancelIn".equals(returnType)) {  //转入申请取消
                CenterHeadOut centerHeadOut = CenterTransferImpl.getCenterHeadOut("1", "", txCode, "");
                respObj = new SingleTransInApplOut(centerHeadOut);
            } else if ("ApplScheduleQueryIn".equals(returnType)) {  //申请进度查询
                CenterHeadOut centerHeadOut = CenterTransferImpl.getCenterHeadOut("1", "", txCode, "");
                respObj = new SingleTransInApplOut(centerHeadOut);
            } else {  //单笔转出信息
                CenterHeadOut centerHeadOut = CenterTransferImpl.getCenterHeadOut("1", "", txCode, "");
                respObj = new SingleTransInApplOut(centerHeadOut);
            }

            Message message = new ParmBean2XMLBean().transfer(respObj);
            return JAXBUtil.toXML(message);
        }

        if ("InterfaceCheckOut".equals(returnType)) {
            InterfaceCheckOut interfaceCheckOut = (InterfaceCheckOut) reqObj;
            logger.info("从住建部收到的数据:\n" + interfaceCheckOut);

            //调用方法,将接口探测消息推送到业务层
            respObj = iCenter.recMsg(interfaceCheckOut);
        } else if ("SingleTransInApplIn".equals(returnType)) {  //单笔转入申请
            SingleTransInApplIn singleTransInApplIn = (SingleTransInApplIn) reqObj;
            logger.info("收到单笔转入申请:\n" + singleTransInApplIn);

            respObj = iCenterTransfer.recMsg(singleTransInApplIn);
        } else if ("TransInApplCancelIn".equals(returnType)) {  //转入申请取消
            TransInApplCancelIn transInApplCancelIn = (TransInApplCancelIn) reqObj;
            logger.info("收到转入申请取消:\n" + transInApplCancelIn);

            respObj = iCenterTransfer.recMsg(transInApplCancelIn);
        } else if ("ApplScheduleQueryIn".equals(returnType)) {  //申请进度查询
            ApplScheduleQueryIn applScheduleQueryIn = (ApplScheduleQueryIn) reqObj;
            logger.info("收到申请进度查询:\n" + applScheduleQueryIn);

            respObj = iCenterTransfer.recMsg(applScheduleQueryIn);
        } else if ("SingleTransOutInfoIn".equals(returnType)) {  //单笔转出信息
            SingleTransOutInfoIn singleTransOutInfoIn = (SingleTransOutInfoIn) reqObj;
            logger.info("收到单笔转出信息:\n" + singleTransOutInfoIn);

            respObj = iCenterTransfer.recMsg(singleTransOutInfoIn);
        } else {
            AccChangeNoticeOut accChangeNoticeOut = (AccChangeNoticeOut) reqObj;
            logger.info("收到到账通知:\n" + accChangeNoticeOut);

            //调用方法,将资金变动通知入库并推送到业务层
            respObj = iCenter.recMsg(accChangeNoticeOut);
        }

        try {
            Message message = new ParmBean2XMLBean().transfer(respObj);
            respXml = JAXBUtil.toXML(message);
            logger.info("发送到住建部的报文:\n" + JAXBUtil.formatXml(respXml, "answer"));
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            throw e;
        }

        return respXml;
    }

    /**
     * 获取交易码
     *
     * @param xmlMsg xml
     * @return TxCode
     * @throws DocumentException 异常
     */
    private String getTxCode(String xmlMsg) throws DocumentException {
        SAXReader reader = new SAXReader();
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlMsg.getBytes());
        Document doc = reader.read(bais);
        Element root = doc.getRootElement();
        Element head = root.element("head");
        String TxCode = "";
        for (Iterator iter = head.elementIterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next(); // 获取标签对象
            // 获取该标签对象的属性
            Attribute attr = element.attribute("name");
            if (null != attr) {
                String attrVal = attr.getValue();
//                String attrName = attr.getName();
                if (attrVal.equals("TxCode")) {
                    TxCode = element.getText();
                }
//                System.out.println(attrName + ": " + attrVal);
            }
        }

        logger.info("TxCode: " + TxCode);
        return TxCode;
    }

    private void close(SocketChannel sc) throws Exception {
        if (sc != null) {
            try {
                sc.close();
            } catch (IOException var3) {
                var3.printStackTrace();
                throw new Exception("关闭连接出错." + var3.getMessage());
            }
        }

    }

}
