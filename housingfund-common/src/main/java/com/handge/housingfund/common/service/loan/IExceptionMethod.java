package com.handge.housingfund.common.service.loan;

/**
 * Created by Funnyboy on 2017/9/17.
 */
public interface IExceptionMethod {
    //正常,接收任何异常
    void exceptionSelf(Exception e, String id, String ywlsh,String yhmc);
    //还款申请
    void exceptionAbnormal(Exception e, String id, String ywlsh,String yhmc);
    //结算平台的批量查询问题反馈处理
    void exceptionBatch(Exception e, String id);
    //接收任何异常
    void exceptionOverdueAutomatic(Exception ex, String id, String ywlsh, String khyhzhmc);
    //接收任何异常
    void exceptionOverdueAutomaticSingle(Exception ex,String id);
}
