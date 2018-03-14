package com.handge.housingfund.common.service.util;


import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * JAXB工具类,负责将XMLBean和XML的相互转换
 * Created by gxy on 17-6-20.
 */
public class JAXBUtil {
    private static Logger logger = LogManager.getLogger(JAXBUtil.class);

//    private static CompositeConfiguration cfg = PropertiesUtil.getConfiguration("bank.properties");
    /**
     * 转换XML
     * @param message
     * @return XML
     * @throws JAXBException
     */
    public static String toXML(Message message) throws JAXBException {
        //writer，用于保存XML内容
        StringWriter writer = new StringWriter();
        writer.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n");
        //获取一个关于Customer类的 JAXB 对象
        JAXBContext context = JAXBContext.newInstance(Message.class);
        //由  Jaxbcontext 得到一个Marshaller
        Marshaller marshaller = context.createMarshaller();
        //设置为格式化输出，就是XML自动格式化
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        //使用marshaller将对象输出到writer
        marshaller.marshal(message, writer);
        //writer.toString()，将所有写入的内容转成String
        return writer.toString();
    }

    /**
     * 转换成XMLBean
     * @param xml
     * @return XMLBean
     * @throws JAXBException
     */
    public static Message createInstanceFromXML(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
        //marshaller是类到XML的转化，那么 unmashaller是XML到类的转化。
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Message message = (Message) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        return message;
    }

    //格式化XML字符串
    public static String formatXml(String str, String type) throws DocumentException, IOException {
        Configuration cfg =   Configure.getInstance().getConfiguration("bank");
        Document document;
        document = DocumentHelper.parseText(str);
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("GBK");

        //返回字符串
        StringWriter strWriter = new StringWriter();
        //返回字符串
        XMLWriter xmlWriter = new XMLWriter(strWriter, format);
        // 将document写入到输出流
        xmlWriter.write(document);
        xmlWriter.close();

        //写入文件
        String fileName = "";
        Element root = document.getRootElement();
        List node = root.element("head").elements("field");
        for (Object aNode : node) {
            Element field = (Element) aNode;
            if ("SendSeqNo".equals(field.attribute("name").getValue())){
                if ("send".equals(type))
                    fileName = cfg.getString("filePath") + field.getText() + "-send.xml";
                else
                    fileName = cfg.getString("filePath") + field.getText() + "-notice.xml";
            }
            if ("ReceiveSeqNo".equals(field.attribute("name").getValue()) && "answer".equals(type))
                fileName = cfg.getString("filePath") + field.getText() + "-answer.xml";
            if ("BDCSeqNo".equals(field.attribute("name").getValue()) && "receive".equals(type))
                fileName = cfg.getString("filePath") + field.getText() + "-receive.xml";
        }

        FileUtil.mkdirs(cfg.getString("filePath"));
        FileWriter fileWriter = new FileWriter(fileName);
        //写入文件
        xmlWriter = new XMLWriter(fileWriter, format);
        // 将document写入到文件流
        xmlWriter.write(document);
        xmlWriter.close();
        logger.info("XML报文已存入文件" + fileName);

        return strWriter.toString();
    }
}
