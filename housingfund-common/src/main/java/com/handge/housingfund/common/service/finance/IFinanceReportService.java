package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.util.ErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by tanyi on 2017/9/7.
 */
public interface IFinanceReportService {

    /**
     * 资产负债表　　按月计量
     *
     * @param tokenContext
     * @param period
     * @return
     * @throws ExecutionException
     */
    ReportTable createOrGetBalanceReportByMonth(TokenContext tokenContext, String period) throws ErrorException, Exception;

    /**
     * 资产负债表　　按季计量
     *
     * @param tokenContext
     * @param year
     * @param quarter
     * @return
     * @throws ExecutionException
     */
    ReportTable createOrGetBalanceReportByQuarter(TokenContext tokenContext, int year, int quarter) throws ErrorException, Exception;

    /**
     * 资产负债表　　按年计量
     *
     * @param tokenContext
     * @param year
     * @return
     * @throws ExecutionException
     */
    ReportTable createOrGetBalanceReportByYear(TokenContext tokenContext, int year) throws ErrorException, Exception;

    /**
     * 增值收益表　按月计量
     *
     * @param tokenContext
     * @param period
     * @return
     * @throws ExecutionException
     */
    ReportTable createOrGetIncomexpenseReportByMonth(TokenContext tokenContext, String period) throws ErrorException, Exception;

    /**
     * 增值收益表　按季度计量
     *
     * @param tokenContext
     * @param year
     * @param quarter
     * @return
     */
    ReportTable createOrGetIncomexpenseReportByQuarter(TokenContext tokenContext, int year, int quarter) throws Exception;

    /**
     * 增值收益表 按年计量
     *
     * @param tokenContext
     * @param year
     * @return
     */
    ReportTable createOrGetIncomexpenseReportByYear(TokenContext tokenContext, int year) throws Exception;

    /**
     * 增值收益分配表
     *
     * @param tokenContext
     * @param year
     * @return
     */
    ReportTable createOrGetIncomallocationReport(TokenContext tokenContext, int year) throws Exception;

    /**
     * 获取住房公积金缴存使用情况
     *
     * @param CXNY 查询年月
     * @return
     */
    HousingfundDeposit addhousingfundDepositResport(TokenContext tokenContext, String CXNY);

    /**
     * 获取住房公积金贷款发放和账户存款余额变动情况统计表
     *
     * @param CXRQ 查询日期 2017-09-13
     * @return
     */
    HousingfundLoanBalance addhousingfundLoanBalance(TokenContext tokenContext, String CXRQ);

    /**
     * 获取住房公积金银行存款情况表
     *
     * @param CXNY 查询年月
     * @return
     */
    HousingfundBankDeposit checkHousingfundBankBalance(TokenContext tokenContext, String CXNY);


    /**
     * 获取住房公积金存贷款报备表
     *
     * @param CXNY 查询年月
     * @return
     */
    HousingfundLoanReport addHousingLoanReport(TokenContext tokenContext, String CXNY);

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx   资金业务类型
     * @param begin    结算发生日期开始
     * @param end      结算发生日期结束
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<SettlementDayBook> getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, int pageNo, int pageSize);

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx 资金业务类型
     * @param begin  结算发生日期开始
     * @param end    结算发生日期结束
     * @return
     */
    PageResNew<SettlementDayBook> getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, String marker, String action, int pageSize);

    /**
     * 获取银行结算流水信息
     *
     * @param id
     * @return
     */
    SettlementDayBook getSettlementDayBook(String id);

    /**
     * 添加银行结算流水信息
     *
     * @param settlementDayBook
     * @return
     */
    SettlementDayBook addSettlementDayBook(SettlementDayBook settlementDayBook);

    /**
     * 获取银行存款日记账列表
     *
     * @param yhzhhm   专户号码
     * @param jzrq     记账日期
     * @param rzrq     入账日期
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<DepositJournal> getDepositJournal(String yhzhhm, String jzrq, String rzrq, int pageNo, int pageSize);

    /**
     * 添加银行存款日记账
     *
     * @param depositJournal
     * @return
     */
    DepositJournal addDepositJournal(DepositJournal depositJournal);

    /**
     * 更新银行存款日记账
     *
     * @param depositJournal
     * @return
     */
    void updateDepositJournal(String ywlsh, DepositJournal depositJournal);

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
    PageRes<FixedDepositDetails> getFixedDepositDetails(String yhzhhm, String ckqx, String begin, String end, int pageNo, int pageSize);

    /**
     * 获取定期存款明细
     *
     * @param dqckbh 定期存款编号
     * @return
     */
    FixedDepositDetails getSingleFixedDepositDetails(String dqckbh);

    /**
     * 获取全市归集贷款统计表
     *
     * @param type 统计类型
     * @param year 年份
     */
    ReportModel getCityLoanCollection(String type, String year);

    /**
     * 获取全市各区暂收未分摊总额
     * @param CXSJ
     * @return
     */
    ReportModel getCityZSWFT(TokenContext tokenContext, String CXSJ);


    /**
     * 获取全市各区暂收未分摊总额定时任务
     *
     */
    void saveZSWFTTimeTask();


    /**
     * 住房公积金缴存、提取分类情况
     *
     * @param CXNY 查询年月
     * @return
     */
    HousingfundDepositWithdrawlClassifyReport getHousingfundDepositWithdrawlClassifyResport(TokenContext tokenContext, String CXNY);
}
