package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;

import javax.ws.rs.core.Response;

/**
 * Created by xuefei_wang on 17-9-7.
 */
public interface IFinanceReportAPI {

    Response getBalanceReportByMonth(TokenContext tokenContext, String period);

    Response getBalanceReportByQuarter(TokenContext tokenContext, int year, int quarter);

    Response getBalanceReportByYear(TokenContext tokenContext, int year);

    /**
     * 增值收益表　按月计量
     *
     * @param tokenContext
     * @param period
     * @return
     */
    Response getIncomexpenseReportByMonth(TokenContext tokenContext, String period);

    /**
     * 增值收益表　按季度计量
     *
     * @param tokenContext
     * @param year
     * @param quarter
     * @return
     */
    Response getIncomexpenseReportByQuarter(TokenContext tokenContext, int year, int quarter);

    /**
     * 增值收益表 按年计量
     *
     * @param tokenContext
     * @param year
     * @return
     */
    Response getIncomexpenseReportByYear(TokenContext tokenContext, int year);

    /**
     * 增值收益分配表 按月计量
     *
     * @param tokenContext
     * @param year
     * @return
     */
    Response getIncomallocationReport(TokenContext tokenContext, int year);

    /**
     * 获取住房公积金缴存使用情况
     *
     * @param tokenContext
     * @param CXNY
     * @return
     */
    Response gethousingfundDepositResport(TokenContext tokenContext, String CXNY);

    /**
     * 获取住房公积金贷款发放和账户存款余额变动情况统计表
     *
     * @param tokenContext
     * @param CXRQ         查询日期 2017-09-13
     * @return
     */
    Response gethousingfundLoanBalance(TokenContext tokenContext, String CXRQ);

    /**
     * @param tokenContext
     * @param CXNY
     * @return
     */
    Response getHousingfundBankBalance(TokenContext tokenContext, String CXNY);

    Response getHousingLoanReport(TokenContext tokenContext, String CXNY);

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx
     * @param begin
     * @param end
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, int pageNo, int pageSize);

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx
     * @param begin
     * @param end
     * @return
     */
    Response getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, String marker, String action, String pageSize);

    /**
     * 获取银行结算流水信息
     *
     * @param id
     * @return
     */
    Response getSettlementDayBook(String id);

    /**
     * 对账
     *
     * @param date
     * @return
     */
    Response getReconciliation(String date);

    /**
     * 获取银行存款日记账列表
     *
     * @param yhzhhm
     * @param jzrq
     * @param rzrq
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getDepositJournal(String yhzhhm, String jzrq, String rzrq, int pageNo, int pageSize);

    /**
     * 获取定期存款明细列表
     *
     * @param yhzhhm   专户号码
     * @param ckqx     存款期限
     * @param begin    存入日期开始
     * @param end      存入日期结束
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getFixedDepositDetails(String yhzhhm, String ckqx, String begin, String end, int pageNo, int pageSize);

    /**
     * 获取定期存款明细
     *
     * @param dqckbh 定期存款编号
     * @return
     */
    Response getSingleFixedDepositDetails(String dqckbh);

    /**
     * 获取全市归集贷款统计表
     *
     * @param type 统计类型
     * @param year 年份
     * @return
     */
    Response getCityLoanCollection(String type, String year);

    /**
     * 获取全市各区暂收未分摊总额
     * @param tokenContext
     * @param CXSJ 查询年月
     * @return
     */
    Response getCityZSWFT(TokenContext tokenContext,String CXSJ);

    Response getDepositWithdrawlClassifyReport(TokenContext tokenContext,String CXNY);
}
