package com.handge.housingfund.common.service.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by gxy on 17-8-11.
 */
public class LogUtil {
    /**
     * 获取异常信息
     * @param t
     * @return 异常信息
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
