package com.handge.housingfund.others.webservice.utils;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;


public class ClientHandler implements SOAPHandler<SOAPMessageContext> {

    public boolean handleMessage(SOAPMessageContext ctx) {
        //客户端发出请求前，添加表头信息
        if ((Boolean) ctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
            try {
                SOAPMessage msg = ctx.getMessage();
                SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
                SOAPHeader hdr = env.getHeader();
                if (hdr == null) hdr = env.addHeader();

                //添加认证信息头
                QName name = new QName("http://ws.szca.com.cn/", "authentication");
                SOAPHeaderElement header = hdr.addHeaderElement(name);

                SOAPElement authCode = header.addChildElement("authCode");
                authCode.addTextNode("bb8fdeda6d4f4bbcb7caa66117b3fd0b93035f89c34dc05217fd265a9bb02a98e85415ef");
                msg.saveChanges();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Set<QName> getHeaders() {
        return null;
    }

    public void close(MessageContext context) {
    }

    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

}