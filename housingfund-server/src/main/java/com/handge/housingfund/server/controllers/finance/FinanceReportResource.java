package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IFinanceReportAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by xuefei_wang on 17-9-7.
 */

@Path("/finance/report/")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class FinanceReportResource {

    @Autowired
    IFinanceReportAPI financeReportAPI;


    /**
     * 资产负债表　　按月计量
     *
     * @param request
     * @param period  　会计期间　ex : 201708
     * @return
     * @author xuefei_wang
     */
    @Path("balance/month")
    @GET
    public Response getBalanceReportByMonth(final @Context HttpRequest request,
                                            final @QueryParam("period") String period) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        Response response = financeReportAPI.getBalanceReportByMonth(tokenContext, period);
        return ResUtils.wrapEntityIfNeeded(response);
    }

    /**
     * 资产负债表　按季度计量
     *
     * @param request
     * @param year    年度　ex: 2017
     * @param quarter 季度　 {1,2,3,4}
     * @return
     * @author xuefei_wang
     */
    @Path("balance/quarter")
    @GET
    public Response getBalanceReportByQuarter(final @Context HttpRequest request,
                                              final @QueryParam("year") int year,
                                              final @QueryParam("quarter") int quarter) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        Response response = financeReportAPI.getBalanceReportByQuarter(tokenContext, year, quarter);
        return ResUtils.wrapEntityIfNeeded(response);
    }

    /**
     * 资产负债表　　按年计量
     *
     * @param request
     * @param year    　年度 ex: 2017
     * @return
     * @author xuefei_wang
     */
    @Path("balance/year")
    @GET
    public Response getBalanceReportByYear(final @Context HttpRequest request,
                                           final @QueryParam("year") int year) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        Response response = financeReportAPI.getBalanceReportByYear(tokenContext, year);
        return ResUtils.wrapEntityIfNeeded(response);
    }


    /**
     * 增值收益表　按月计量
     *
     * @param request
     * @param period  　会计期间　ex: 2017
     * @return
     * @author xuefei_wang
     */
    @Path("incomexpense/month")
    @GET
    public Response getIncomexpenseReportByMonth(final @Context HttpRequest request,
                                                 final @QueryParam("period") String period) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        Response response = financeReportAPI.getIncomexpenseReportByMonth(tokenContext, period);
        return ResUtils.wrapEntityIfNeeded(response);
    }

    /**
     * 增值收益表　按季度计量
     *
     * @param request
     * @param year    　　年度 ex: 2017
     * @param quarter 季度　{1,2,3,4}
     * @return
     * @author xuefei_wang
     */
    @Path("incomexpense/quarter")
    @GET
    public Response getIncomexpenseReportByQuarter(final @Context HttpRequest request,
                                                   final @QueryParam("year") int year,
                                                   final @QueryParam("quarter") int quarter) {
        System.out.println("增值收益表　按季度计量");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);

        try {
            return ResUtils.wrapEntityIfNeeded(financeReportAPI.getIncomexpenseReportByQuarter(tokenContext, year, quarter));
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }
    }

    /**
     * 增值收益表 按年计量
     *
     * @param request
     * @param year    　年度　ex: 2017
     * @return
     * @author xuefei_wang
     */
    @Path("incomexpense/year")
    @GET
    public Response getIncomexpenseReportByYear(final @Context HttpRequest request,
                                                final @QueryParam("year") int year) {
        System.out.println("增值收益表　按年计量");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            return ResUtils.wrapEntityIfNeeded(financeReportAPI.getIncomexpenseReportByYear(tokenContext, year));
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }
    }

    /**
     * 增值收益分配表 按月计量
     *
     * @param request
     * @param year  会计期间 ex: 201709
     * @return
     * @author xuefei_wang
     */

    @Path("incomallocation")
    @GET
    public Response getIncomallocationReport(final @Context HttpRequest request,
                                             final @QueryParam("year") int year) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        Response response = financeReportAPI.getIncomallocationReport(tokenContext, year);
        return ResUtils.wrapEntityIfNeeded(response);
    }

    //-----------------------------------------------------------------------------

    @Path("housingfundDeposit")
    public Response getIncomallocationResport(final @Context HttpRequest request) {
        return null;
    }

    /**
     * @param request
     * @param CXNY    查询年月 yyyy-MM
     * @return
     */
    @Path("housingfundDepositResport")
    @GET
    public Response gethousingfundDepositResport(final @Context HttpRequest request, final @QueryParam("CXNY") String CXNY) {
        System.out.println("获取住房公积金缴存使用情况");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.gethousingfundDepositResport(tokenContext, CXNY));
    }

    /**
     * 获取住房公积金贷款发放和账户存款余额变动情况统计表
     *
     * @param request
     * @param CXRQ    查询日期yyyy-MM-dd
     * @return
     */
    @Path("housingfundLoanBalance")
    @GET
    public Response gethousingfundLoanBalance(final @Context HttpRequest request, final @QueryParam("CXRQ") String CXRQ) {
        System.out.println("获取住房公积金贷款发放和账户存款余额变动情况统计表");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.gethousingfundLoanBalance(tokenContext, CXRQ));
    }

    /**
     * 获取住房公积金银行存款情况表
     *
     * @param request
     * @param CXNY    查询年月 yyyy-MM
     * @return
     */
    @Path("housingfundBankBalance")
    @GET
    public Response getHousingfundBankBalance(final @Context HttpRequest request, final @QueryParam("CXNY") String CXNY) {
        System.out.println("获取住房公积金银行存款情况表");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getHousingfundBankBalance(tokenContext, CXNY));
    }

    /**
     * 获取住房公积金存贷款报备表
     *
     * @param CXNY 查询年月 yyyy-MM
     * @return
     */
    @Path("housingfundLoanReport")
    @GET
    public Response getHousingLoanReport(final @Context HttpRequest request, final @QueryParam("CXNY") String CXNY) {
        System.out.println("获取住房公积金存贷款报备表");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getHousingLoanReport(tokenContext, CXNY));
    }


    //----------------------银行结算流水----------------------

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx   资金业务类型
     * @param yhmc     银行名称
     * @param yhzh     银行账号
     * @param begin    结算发生日期-开始日期 yyyyMMdd
     * @param end      结算发生日期-结束日期 yyyyMMdd
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    @GET
    @Path("settlementdaybook")
    public Response getSettlementDayBookList(final @QueryParam("ZJYWLX") String zjywlx,
                                             final @QueryParam("YHMC") String yhmc,
                                             final @QueryParam("YHZH") String yhzh,
                                             final @QueryParam("BEGIN") String begin,
                                             final @QueryParam("END") String end,
                                             final @QueryParam("pageNo") int pageNo,
                                             final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取银行结算流水信息列表");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getSettlementDayBookList(zjywlx, yhmc, yhzh, begin, end, pageNo, pageSize));
    }

    /**
     * 获取银行结算流水信息列表
     *
     * @param zjywlx   资金业务类型
     * @param yhmc     银行名称
     * @param yhzh     银行账号
     * @param begin    结算发生日期-开始日期 yyyyMMdd
     * @param end      结算发生日期-结束日期 yyyyMMdd
     * @return
     */
    @GET
    @Path("settlementdaybook/new/notice")
    public Response getSettlementDayBookList(final @QueryParam("ZJYWLX") String zjywlx,
                                             final @QueryParam("YHMC") String yhmc,
                                             final @QueryParam("YHZH") String yhzh,
                                             final @QueryParam("BEGIN") String begin,
                                             final @QueryParam("END") String end,
                                             final @QueryParam("marker") String marker,
                                             final @QueryParam("action") String action,
                                             final @QueryParam("pageSize") String pageSize) {
        System.out.println("获取银行结算流水信息列表");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getSettlementDayBookList(zjywlx, yhmc, yhzh, begin, end, marker, action, pageSize));
    }

    @GET
    @Path("settlementdaybook/{id}")
    public Response getSettlementDayBook(final @PathParam("id") String id) {
        System.out.println("获取银行结算流水信息");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getSettlementDayBook(id));
    }

    @GET
    @Path("reconciliation")
    public Response getReconciliation(final @QueryParam("date") String date) {
        System.out.println("对账");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getReconciliation(date));
    }

    //----------------------银行结算流水----------------------
    //---------------------银行存款日记账---------------------

    /**
     * 获取银行存款日记账列表
     *
     * @param yhzhhm   银行专户号码
     * @param jzrq     记账日期 YYYYMMDD
     * @param rzrq     入账日期 YYYYMMDD
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    @GET
    @Path("depositjournal")
    public Response getDepositJournal(final @QueryParam("YHZHHM") String yhzhhm,
                                      final @QueryParam("JZRQ") String jzrq,
                                      final @QueryParam("RZRQ") String rzrq,
                                      final @QueryParam("pageNo") int pageNo,
                                      final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取银行存款日记账列表");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getDepositJournal(yhzhhm, jzrq, rzrq, pageNo, pageSize));
    }
    //---------------------银行存款日记账---------------------
    //---------------------定期存款明细信息--------------------

    /**
     * @param yhzhhm   银行专户号码
     * @param ckqx     存款期限
     * @param begin    存入日期-开始日期 yyyyMMdd
     * @param end      存入日期-开始日期 yyyyMMdd
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    @GET
    @Path("fixedDepositDetails")
    public Response getFixedDepositDetails(final @QueryParam("YHZHHM") String yhzhhm,
                                           final @QueryParam("CKQX") String ckqx,
                                           final @QueryParam("BEGIN") String begin,
                                           final @QueryParam("END") String end,
                                           final @QueryParam("pageNo") int pageNo,
                                           final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取定期存款明细列表");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getFixedDepositDetails(yhzhhm, ckqx, begin, end, pageNo, pageSize));
    }

    @GET
    @Path("fixedDepositDetails/{DQCKBH}")
    public Response getSingleFixedDepositDetails(final @PathParam("DQCKBH") String dqckbh) {
        System.out.println("获取定期存款明细");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getSingleFixedDepositDetails(dqckbh));
    }
    //---------------------定期存款明细信息--------------------
    //---------------------全市归集贷款统计表---------------------
    @GET
    @Path("cityLoanCollection")
    public Response getCityLoanCollection(final @QueryParam("TYPE") String type, final @QueryParam("YEAR") String year) {
        System.out.println("获取全市归集贷款统计表");
        return ResUtils.wrapEntityIfNeeded(financeReportAPI.getCityLoanCollection(type, year));
    }
    //---------------------全市归集贷款统计表---------------------

    @GET
    @Path("depositWithdrawlClassify")
    public Response getDepositWithdrawlClassify(final @Context HttpRequest request, final @QueryParam("CXNY") String CXNY){
        System.out.println("住房缴存提取分类统计");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return  ResUtils.wrapEntityIfNeeded(financeReportAPI.getDepositWithdrawlClassifyReport(tokenContext,CXNY)) ;
    }

    //-----------------获取全市各区暂收未分摊总额-----------------
    @GET
    @Path("CityZSWFT")
    public Response getCityZSWFT(final @Context HttpRequest request, final @QueryParam("CXSJ") String CXSJ){
        System.out.println("获取全市各区暂收未分摊总额");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return  ResUtils.wrapEntityIfNeeded(financeReportAPI.getCityZSWFT(tokenContext,CXSJ)) ;
    }
}
