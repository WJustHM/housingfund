package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成测试head
 * Created by gxy on 17-7-3.
 */
public class CenterHead {
    static String random = String.valueOf(Math.random()).substring(2,11);
    public static String[] getDatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        String datetime = format.format(new Date());
        String[] datetimes = datetime.split("\\s+");
        return datetimes;
    }

    /**
     * 中
     */
    public static CenterHeadIn getBOCHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn BOCHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "104000",
                "嘻嘻嘻"
        );
        BOCHead.setCustNo("GZ20057305");
        return BOCHead;
    }

    /**
     * 农
     */
    public static CenterHeadIn getABCHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn ABCHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "103000",
                "522400000000000000007"
        );
        ABCHead.setCustNo("GZ20057305");
        return ABCHead;
    }

    /**
     * 工
     */
    public static CenterHeadIn getICBCHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn ICBCHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "102000",
                "522400000000000000007"
        );
        ICBCHead.setCustNo("GZ20057305");
        return ICBCHead;
    }

    /**
     * 建
     */
    public static CenterHeadIn getCCBHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn CCBHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "105000",
                "522400000000000000007"
        );
        CCBHead.setCustNo("GZ20057305");
        return CCBHead;
    }

    /**
     * 交
     */
    public static CenterHeadIn getBOCOMHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn BOCOMHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "301000",
                "522400000000000000007"
        );
        BOCOMHead.setCustNo("GZ20057305");
        return BOCOMHead;
    }
    /**
     * 贵州银行
     */
    public static CenterHeadIn getGZBCHead(String txCode) {
        String[] datetime = getDatetime();
        CenterHeadIn BOCOMHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                "313024",
                "522400000000000000007"
        );
        BOCOMHead.setCustNo("GZBC522400707001");
        return BOCOMHead;
    }

    /**
     * 转移接续
     */
    public static CenterHeadIn getTransHead(String txCode, String receiveNode) {
        String[] datetime = getDatetime();
        CenterHeadIn BOCOMHead = new CenterHeadIn(
                datetime[0],
                datetime[1],
                "52240" + datetime[0].substring(2) + random,
                "522400000000000",
                "C52240",
                txCode,
                receiveNode,
                "522400000000000000007"
        );

        return BOCOMHead;
    }
}
