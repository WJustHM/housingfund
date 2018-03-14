package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.loan.model.Error;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("FieldCanBeLocal")
public class ErrorException extends RuntimeException implements Serializable {

    private static boolean DEBUG = false;

    private String Code;

    private String Msg;

    public ErrorException() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public ErrorException(String code, String msg) {
        Code = code;
        Msg = msg;
    }

    public ErrorException(String msg) {
        this.Code = ReturnEnumeration.Program_UNKNOW_ERROR.getCode();
        this.Msg = msg;
    }


    public ErrorException(ReturnEnumeration enumeration, String extension) {
        this.Code = enumeration.getCode();
        this.Msg = enumeration.getMessage() + extension;
    }

    public ErrorException(ReturnEnumeration enumeration) {
        this.Code = enumeration.getCode();
        this.Msg = enumeration.getMessage();
    }

    public ErrorException(Exception e) {

        if (e instanceof ErrorException) {

            this.Code = ((ErrorException) e).getCode();

            this.Msg = ((ErrorException) e).getMsg();

            return;
        }
        this.Code = ReturnEnumeration.Program_UNKNOW_ERROR.getCode();

        Throwable cause;

        Throwable throwable = e;

        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }

        this.Msg = ErrorException.analysisError(e);// ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + throwable.getMessage();
    }

    public ErrorException(Exception e, String msg) {

        if (e instanceof ErrorException) {

            this.Code = ((ErrorException) e).getCode();

            this.Msg = msg + ((ErrorException) e).getMsg();

            return;
        }
        this.Code = ReturnEnumeration.Program_UNKNOW_ERROR.getCode();

        Throwable cause;

        Throwable throwable = e;

        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }

        this.Msg = msg + ErrorException.analysisError(e); //ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + msg + throwable.getMessage();
    }

    public Error getError() {

        Error error = new Error();

        error.setCode(Code);

        error.setMsg(Msg);

        return error;

    }

    @Override
    public String getMessage() {


        return this.getMsg();
    }


    private static String analysisError(Exception e) {

        Throwable cause;

        Throwable throwable = e;

        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }

        if (throwable.getMessage() == null && e instanceof NullPointerException) {

            return "必要数据为空 " +
                    (DEBUG ? (ErrorException.getSupportClass(e).getFileName() + " " + ErrorException.getSupportClass(e).getMethodName() + " " + ErrorException.getSupportClass(e).getLineNumber() + "行") : "");
        }


        if (ErrorException.getSupportClass(e).getClassName().equals("AssertUtils.java") && ErrorException.getSupportClass(e).getClassName().equals("notEmpty")) {

            return "必要数据为空 " + throwable.getMessage();
        }

        Matcher matcher;
        if (throwable == null || throwable.getMessage() == null) {
            return "未知错误 " + e.getClass();
        }
        if ((matcher = Pattern.compile("(.*)too long(.*)'(.*)'(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据长度过长:" + DataBaseMap.columnMap.get(matcher.group(3));
        }

        if ((matcher = Pattern.compile("(.*)Forbid consumer(.*)com.handge.housingfund.(.*) from (.*)").matcher(throwable.getMessage())).matches()) {

            return "服务连接错误 请检查检查" + matcher.group(3) + "服务是否打开";
        }

        if ((matcher = Pattern.compile("(.*)channel is closed(.*)com.handge.housingfund.(.*)?(.*)").matcher(throwable.getMessage())).matches()) {

            return "服务连接错误 请检查检查" + matcher.group(3) + "服务是否打开";
        }

        if ((matcher = Pattern.compile("(.*)Unknown(.*)column(.*)'(.*)_(.*)_(.*)'(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据库字段缺失:" + DataBaseMap.columnMap.get(matcher.group(5) + "_" + matcher.group(6)) + "请更新数据库";
        }

        if ((matcher = Pattern.compile("(.*)Unknown(.*)column(.*)'(.*)_(.*)'(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据库字段缺失:" + DataBaseMap.columnMap.get(matcher.group(5)) + "请更新数据库";
        }

        if ((matcher = Pattern.compile("(.*)Column(.*)'(.*)_(.*)_(.*)'cannot be null(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据异常:" + DataBaseMap.columnMap.get(matcher.group(4) + "_" + matcher.group(5)) + "不允许为空";
        }

        if ((matcher = Pattern.compile("(.*)Column(.*)'(.*)_(.*)'cannot be null(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据异常:" + DataBaseMap.columnMap.get(matcher.group(4)) + "不允许为空";
        }

        if ((matcher = Pattern.compile("(.*)Data truncation(.*)'(.*)'(.*)").matcher(throwable.getMessage())).matches()) {

            return "数据不合法:" + DataBaseMap.columnMap.get(matcher.group(3));
        }

        return "未知错误:" + throwable.getMessage() + "\n" +
                (DEBUG ? (ErrorException.getSupportClass(e).getFileName() + " " + ErrorException.getSupportClass(e).getMethodName() + " " + ErrorException.getSupportClass(e).getLineNumber() + "行") : "");
    }

    private static StackTraceElement getSupportClass(Exception e) {

        for (StackTraceElement stackTraceElement : e.getStackTrace()) {

            if (stackTraceElement.getClassName().startsWith("com.handge.housingfund") && Pattern.compile("(.*)(account|bank|collection|finance|loan|others|review|statemachine|task)(.*)").matcher(stackTraceElement.getClassName()).matches()) {

                return stackTraceElement;
            }
        }

        return e.getStackTrace().length == 0 ? new StackTraceElement("", "", "", 0) : (e.getStackTrace())[0];
    }

}
