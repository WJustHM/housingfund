package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.center.LoginIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import org.apache.commons.configuration2.Configuration;
import org.dom4j.DocumentException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建签到Xml
 * Created by gxy on 17-6-21.
 */
public class CreateLoginXml {
    private static String[] getDatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        String datetime = format.format(new Date());
        String[] datetimes = datetime.split("\\s+");
        return datetimes;
    }

    public String getLoginXml() throws JAXBException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, DocumentException {
        String[] datetime = getDatetime();
        Configuration cfg =  Configure.getInstance().getConfiguration("bank");
        String sendNode = cfg.getString("nodeNo");
        String txUnitNo = cfg.getString("txUnitNo");
        String operNo = cfg.getString("operNo");
        String custNo = cfg.getString("custNo");
        CenterHeadIn centerHeadIn = new CenterHeadIn(
                datetime[0],
                datetime[1],
                sendNode.substring(1) + datetime[0].substring(2) + "999999999",
                txUnitNo,
                sendNode,
                "BDC001",
                "105000",
                operNo
        );
        centerHeadIn.setCustNo(custNo);
        LoginIn loginIn = new LoginIn(centerHeadIn);
        Message message = new ParmBean2XMLBean().transfer(loginIn);
        String xml = JAXBUtil.toXML(message);
        System.out.println("签到报文: \n" + JAXBUtil.formatXml(xml, "send"));
        return xml;
    }
}
