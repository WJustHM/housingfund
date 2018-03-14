package com.handge.housingfund.server.controllers.finance.api.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.finance.IFinanceAsync;
import com.handge.housingfund.common.service.finance.IFinanceReportService;
import com.handge.housingfund.common.service.finance.model.ReconciliationModel;
import com.handge.housingfund.common.service.finance.model.ReportTable;
import com.handge.housingfund.common.service.loan.IBankCallService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IFinanceReportAPI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tanyi on 2017/9/7.
 */
@Component
public class FinanceReportAPI implements IFinanceReportAPI {

    Calendar c = Calendar.getInstance();

    @Autowired
    private IFinanceReportService financeReportService;

    @Autowired
    private IBankCallService bankCallService;

    @Autowired
    private IFinanceAsync iFinanceAsync;

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    public static Gson gson = new Gson();

    /**
     * @param tokenContext
     * @param period       会计期间
     * @return
     */
    @Override
    public Response getBalanceReportByMonth(TokenContext tokenContext, String period) {
        if (period == null || period.isEmpty()) {
            period = DateUtil.getPreviousMonth();
        }
        if (period.length() != 6 || !StringUtils.isNumeric(period)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(period).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetBalanceReportByMonth(tokenContext, period);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }

    }

    /**
     * @param tokenContext
     * @param year
     * @param quarter      　季度
     * @return
     */
    @Override
    public Response getBalanceReportByQuarter(TokenContext tokenContext, int year, int quarter) {
        if (year < 1990 || year > 2099) {
            return Response.status(Response.Status.BAD_REQUEST).entity(year).build();
        }
        if (quarter < 1 || quarter > 4) {
            return Response.status(Response.Status.BAD_REQUEST).entity(quarter).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetBalanceReportByQuarter(tokenContext, year, quarter);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }


    /**
     * @param tokenContext
     * @param year
     * @return
     */
    @Override
    public Response getBalanceReportByYear(TokenContext tokenContext, int year) {
        if (year < 1990 || year > 2099) {
            return Response.status(Response.Status.BAD_REQUEST).entity(year).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetBalanceReportByYear(tokenContext, year);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }

    /**
     * 增值收益表　按月计量
     *
     * @param tokenContext
     * @param period       　季度
     * @return
     */
    @Override
    public Response getIncomexpenseReportByMonth(TokenContext tokenContext, String period) {
        if (period == null || period.isEmpty()) {
            period = DateUtil.getPreviousMonth();
        }
        if (period.length() != 6 || !StringUtils.isNumeric(period)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(period).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetIncomexpenseReportByMonth(tokenContext, period);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }

    /**
     * 增值收益表　按季度计量
     *
     * @param tokenContext
     * @param year
     * @param quarter
     * @return
     */
    @Override
    public Response getIncomexpenseReportByQuarter(TokenContext tokenContext, int year, int quarter) {
        if (year < 1990 || year > 2099) {
            return Response.status(Response.Status.BAD_REQUEST).entity(year).build();
        }
        if (quarter < 1 || quarter > 4) {
            return Response.status(Response.Status.BAD_REQUEST).entity(quarter).build();
        }

        try {
            ReportTable reportTable = this.financeReportService.createOrGetIncomexpenseReportByQuarter(tokenContext, year, quarter);
            System.out.println(reportTable.toString());
            return Response.status(200).entity(reportTable).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }

    /**
     * @param tokenContext
     * @param year
     * @return
     */
    @Override
    public Response getIncomexpenseReportByYear(TokenContext tokenContext, int year) {
        if (year < 1990 || year > 2099) {
            return Response.status(Response.Status.BAD_REQUEST).entity(year).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetIncomexpenseReportByYear(tokenContext, year);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }

    /**
     * 增值收益分配表 按月计量
     *
     * @param tokenContext
     * @param year
     * @return
     */
    @Override
    public Response getIncomallocationReport(TokenContext tokenContext, int year) {
        if (year < 1990 || year > 2099) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("查询年不存在");
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
        try {
            ReportTable reportTable = financeReportService.createOrGetIncomallocationReport(tokenContext, year);
            return Response.ok(reportTable).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setCode(e.getCode());
                this.setMsg(e.getMsg());
            }}).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
            }}).build();
        }
    }


    @Override
    public Response gethousingfundDepositResport(TokenContext tokenContext, String CXNY) {
        try {
            return Response.status(200).entity(financeReportService.addhousingfundDepositResport(tokenContext, CXNY)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response gethousingfundLoanBalance(TokenContext context, String CXRQ) {
        try {
            return Response.status(200).entity(financeReportService.addhousingfundLoanBalance(context, CXRQ)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getHousingfundBankBalance(TokenContext tokenContext, String CXNY) {
        if (!StringUtil.notEmpty(CXNY)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("查询年月不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(financeReportService.checkHousingfundBankBalance(tokenContext, CXNY)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getHousingLoanReport(TokenContext tokenContext, String CXNY) {
        if (!StringUtil.notEmpty(CXNY)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("查询年月不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(financeReportService.addHousingLoanReport(tokenContext, CXNY)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    //----------------------银行结算流水---------------------
    @Override
    public Response getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(financeReportService.getSettlementDayBookList(zjywlx, yhmc, yhzh, begin, end, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, String marker, String action, String pageSize) {

        try {
            int pagesize = pageSize == null ? 30 : Integer.parseInt(pageSize);
            return Response.status(200).entity(financeReportService.getSettlementDayBookList(zjywlx, yhmc, yhzh, begin, end, marker, action, pagesize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    @Override
    public Response getSettlementDayBook(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("结算流水id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(financeReportService.getSettlementDayBook(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getReconciliation(String date) {
        if (!StringUtil.notEmpty(date)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "日期不能为空");
        }
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            if (redis.exists("dz_" + date)) {
                String data = redis.get("dz_" + date);
                List<ReconciliationModel> res = gson.fromJson(data, new TypeToken<List<ReconciliationModel>>() {
                }.getType());
                redis.close();
                return Response.status(200).entity(res).build();
            } else {
                if (redis.exists("dz_state_" + date)) {
                    redis.close();
                    return Response.status(200).entity(new ArrayList<>()).build();//计算中
                } else {
                    redis.setex("dz_state_" + date, 60, "1");
                    redis.close();
                    iFinanceAsync.getReconciliationAsync(date);
                    return Response.status(200).entity(new ArrayList<>()).build();
                }
            }
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        } catch (Exception e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMessage());
                this.setCode(ReturnEnumeration.Parameter_MISS.getCode());
            }}).build();
        }
    }

    //----------------------银行结算流水----------------------
    //---------------------银行存款日记账---------------------
    @Override
    public Response getDepositJournal(String yhzhhm, String jzrq, String rzrq, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(financeReportService.getDepositJournal(yhzhhm, jzrq, rzrq, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    //---------------------银行存款日记账---------------------
    //---------------------定期存款明细信息---------------------
    @Override
    public Response getFixedDepositDetails(String yhzhhm, String ckqx, String begin, String end, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(financeReportService.getFixedDepositDetails(yhzhhm, ckqx, begin, end, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSingleFixedDepositDetails(String dqckbh) {
        if (!StringUtil.notEmpty(dqckbh)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("定期存款编号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(financeReportService.getSingleFixedDepositDetails(dqckbh)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
    //---------------------定期存款明细信息---------------------

    //---------------------全市归集贷款统计表---------------------
    @Override
    public Response getCityLoanCollection(String type, String year) {

        if (!StringUtil.notEmpty(type) || !Arrays.asList("GJ", "TQ", "FK", "HK").contains(type)) {
            {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "统计类型");
            }
        }

        if (!StringUtil.notEmpty(year)) {
            {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "年份");
            }
        }

        try {
            return Response.status(200).entity(financeReportService.getCityLoanCollection(type, year)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    //---------------------全市归集贷款统计表---------------------


    //-----------------获取全市各区暂收未分摊总额-----------------
    public Response getCityZSWFT(TokenContext tokenContext, String CXSJ){
        if (!StringUtil.notEmpty(CXSJ)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "查询日期");
        }
//        if(!StringUtil.matchRegex("2017-17","\\d{4}")){
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "请确保查询日期形如2017");
//        }
        try {
            return Response.status(200).entity(financeReportService.getCityZSWFT(tokenContext,CXSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    //---------------------查询归集提取分类----------------------

    @Override
    public Response getDepositWithdrawlClassifyReport(TokenContext tokenContext, String CXNY) {
        if(StringUtil.isEmpty(CXNY)){
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "查询年月");
        }
        try {
            return Response.status(200).entity(financeReportService.getHousingfundDepositWithdrawlClassifyResport(tokenContext,CXNY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
}
