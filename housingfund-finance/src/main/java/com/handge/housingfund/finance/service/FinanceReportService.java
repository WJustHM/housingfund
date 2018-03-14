package com.handge.housingfund.finance.service;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.IPolicyService;
import com.handge.housingfund.common.service.account.enums.RateEnum;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.center.BDC122Summary;
import com.handge.housingfund.common.service.bank.bean.center.FixedAccBalanceQueryOut;
import com.handge.housingfund.common.service.finance.IAccountBookService;
import com.handge.housingfund.common.service.finance.IFinanceReportService;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.finance.model.enums.FinanceReportType;
import com.handge.housingfund.common.service.finance.model.enums.Subject2Account;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.DepositPeriodTransfer;
import com.handge.housingfund.finance.utils.FinanceComputeHelper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by xuefei_wang on 2017/9/7.
 * <p>
 * <p>
 * 会计期间　201708
 * 季度期间　2017$1
 * <p>
 * 年度　　　2017
 */
@Component
public class FinanceReportService implements IFinanceReportService {

    Cache<String, CFinanceSubjectsBalance> cache = CacheBuilder.newBuilder().expireAfterAccess(1l, TimeUnit.HOURS).build();

    @Autowired
    private IStSettlementDaybookDAO iStSettlementDaybookDAO;

    @Autowired
    private IStFinanceRecordingVoucherDAO financeRecordingVoucherDAO;

    @Autowired
    private IStFinanceBankDepositJournalDAO iStFinanceBankDepositJournalDAO;

    @Autowired
    private ICFinanceAccountPeriodDAO icFinanceAccountPeriodDAO;

    @Autowired
    private ICLoanHousingLoanDAO loanHousingLoanDAO;

    @Autowired
    private IStHousingBusinessDetailsDAO stHousingBusinessDetailsDAO;

    @Autowired
    private IStHousingPersonalLoanDAO stHousingPersonalLoanDAO;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icLoanHousingPersonInformationBasicDAO;


    @Autowired
    private IStCollectionUnitBusinessDetailsDAO stCollectionUnitBusinessDetailsDAO;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO stCollectionPersonalBusinessDetailsDAO;

    @Autowired
    private ICLoanHousingLoanDAO icLoanHousingLoanDAO;

    @Autowired
    ICFinanceSubjectsBalanceQuarterDAO financeSubjectsBalanceQuarterDAO;

    @Autowired
    ICFinanceSubjectsBalanceYearDAO financeSubjectsBalanceYearDAO;

    @Autowired
    IStFinanceTimeDepositDAO iStFinanceTimeDepositDAO;

    @Autowired
    IStHousingOverdueRegistrationDAO istHousingOverdueRegistrationDAO;

    @Autowired
    ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;

    @Autowired
    private ICBankContractDAO icBankContractDAO;

    @Autowired
    IAccountBookService iAccountBookService;

    @Autowired
    IFinanceTrader iFinanceTrader;

    @Autowired
    IPolicyService iPolicyService;

    @Autowired
    ICBankNodeInfoDAO icBankNodeInfoDAO;

    @Autowired
    ICFinanceReportDAO icFinanceReportDAO;

    @Autowired
    ICAccountNetworkDAO icAccountNetworkDAO;

    @Autowired
    IVoucherManagerService iVoucherManagerService;

    @Autowired
    IStCollectionPersonalAccountDAO iStCollectionPersonalAccountDAO;

    @Autowired
    IStCollectionUnitAccountDAO iStCollectionUnitAccountDAO;

    @Autowired
    IStCollectionPersonalBusinessDetailsDAO iStCollectionPersonalBusinessDetailsDAO;

    @Autowired
    IStHousingOverdueRegistrationDAO iStHousingOverdueRegistrationDAO;


    public static Gson gson = new Gson();

    /**
     * @param tokenContext
     * @param period       example "201708"
     * @return
     * @author xuefei_wang
     */
    @Override
    public ReportTable createOrGetBalanceReportByMonth(TokenContext tokenContext, String period) throws Exception {
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金资产负债表_月, period);
        if (reportTable == null) {
            reportTable = createBalanceReport(tokenContext, period, "住房会01表附表");
            saveReportTable(FinanceReportType.住房公积金资产负债表_月, period, reportTable);
        }
        return reportTable;
    }

    /**
     * @param tokenContext
     * @param year
     * @param quarter
     * @return
     * @author xuefei_wang
     */
    @Override
    public ReportTable createOrGetBalanceReportByQuarter(TokenContext tokenContext, int year, int quarter) throws Exception {
        String period = String.format("%s%02d", year, quarter * 3);
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        String reportPeriod = String.format("%s$%d", year, quarter);
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金资产负债表_季度, reportPeriod);
        if (reportTable == null) {
            reportTable = createOrGetBalanceReportByMonth(tokenContext, period);
            reportTable.setPeriod(year + "年第" + quarter + "季度");
            saveReportTable(FinanceReportType.住房公积金资产负债表_季度, reportPeriod, reportTable);
        }
        return reportTable;
    }

    /**
     * @param tokenContext
     * @param year
     * @return
     * @author xuefei_wang
     */
    @Override
    public ReportTable createOrGetBalanceReportByYear(TokenContext tokenContext, int year) throws Exception {
        String period = String.format("%s%s", year, 12);
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        String reportPeriod = String.valueOf(year);
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金资产负债表_年度, reportPeriod);
        if (reportTable == null) {
            reportTable = createOrGetBalanceReportByMonth(tokenContext, period);
            reportTable.setPeriod(reportPeriod + "年度");
            saveReportTable(FinanceReportType.住房公积金资产负债表_年度, reportPeriod, reportTable);
        }
        return reportTable;
    }


    /**
     * 增值收益月表
     *
     * @param tokenContext
     * @param period
     * @return
     * @author xuefei_wang
     */
    @Override
    public ReportTable createOrGetIncomexpenseReportByMonth(TokenContext tokenContext, String period) throws Exception {
        String reportPeriod = period;
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金增值收益表_月, reportPeriod);
        if (reportTable == null) {
            reportTable = comeputeIncomexpenseReportByMonth(tokenContext, period);
            saveReportTable(FinanceReportType.住房公积金增值收益表_月, reportPeriod, reportTable);
        }
        return reportTable;
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
    public ReportTable createOrGetIncomexpenseReportByQuarter(TokenContext tokenContext, int year, int quarter) throws Exception {
        String period = String.format("%s%02d", year, quarter * 3);
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        String reportPeriod = String.format("%s$%d", year, quarter);
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金增值收益表_季度, reportPeriod);
        if (reportTable == null) {
            reportTable = comeputeIncomexpenseReportByQuarter(tokenContext, year, quarter);
            reportTable.setPeriod(year + "年第" + quarter + "季度");
            saveReportTable(FinanceReportType.住房公积金增值收益表_季度, reportPeriod, reportTable);
        }
        return reportTable;

    }

    /**
     * 增值收益表 按年计量
     *
     * @param tokenContext
     * @param year
     * @return
     */
    @Override
    public ReportTable createOrGetIncomexpenseReportByYear(TokenContext tokenContext, int year) throws Exception {
        String period = String.format("%s%s", year, 12);
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        String reportPeriod = String.valueOf(year);
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金增值收益表_年度, reportPeriod);
        if (reportTable == null) {
            reportTable = comeputeIncomexpenseReportByYear(tokenContext, year);
            reportTable.setPeriod(reportPeriod);
            saveReportTable(FinanceReportType.住房公积金增值收益表_年度, reportPeriod, reportTable);
        }
        return reportTable;

    }


    private ReportTable createBalanceReport(TokenContext tokenContext, String period, String reportName) throws Exception {
        String begain = period.substring(0, 4) + "01";
        if (cache.size() == 0) {
            loadBalanceCacheFromDataBase(period, begain);
        }
        AccountBookModel accountBookModel = iAccountBookService.getAccountBookList();
        String CWZG;
        if (accountBookModel != null) {
            CWZG = accountBookModel.getKJZG();
        } else {
            CWZG = "账套未设置财务主管";
        }
        ReportTable reportTable = new ReportTable();
        reportTable.setReportName(reportName);
        reportTable.setDw("元");
        reportTable.setOperator(tokenContext.getUserInfo().getCZY());
        reportTable.setCrateTime(DateUtil.getNowDateTime());
        reportTable.setPeriod(period);
        reportTable.setFinanceUnit(icAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()).getMingCheng());
        reportTable.setFinanceManager(CWZG);
        List<ReportTable.Table> tables = new ArrayList<>();
        try {
            ReportTable.Table tableZICHAN = new ReportTable.Table("资产");

            BigDecimal zc1_nc = getBalanceData(SubjectEnum.住房公积金存款.getId(), begain).getSyye();
            BigDecimal zc1_qm = getBalanceData(SubjectEnum.住房公积金存款.getId(), period).getByye();

            BigDecimal zc2_nc = getBalanceData(SubjectEnum.增值收益存款.getId(), begain).getSyye();
            BigDecimal zc2_qm = getBalanceData(SubjectEnum.增值收益存款.getId(), period).getByye();

            BigDecimal zc3_nc = getBalanceData(SubjectEnum.应收利息.getId(), begain).getSyye();
            BigDecimal zc3_qm = getBalanceData(SubjectEnum.应收利息.getId(), period).getByye();

            BigDecimal zc4_nc = getBalanceData(SubjectEnum.其他应收款.getId(), begain).getSyye();
            BigDecimal zc4_qm = getBalanceData(SubjectEnum.其他应收款.getId(), period).getByye();

            BigDecimal zc5_nc = getBalanceData(SubjectEnum.委托贷款.getId(), begain).getSyye();
            BigDecimal zc5_qm = getBalanceData(SubjectEnum.委托贷款.getId(), period).getByye();

            BigDecimal zc6_nc = getBalanceData(SubjectEnum.逾期贷款.getId(), begain).getSyye();
            BigDecimal zc6_qm = getBalanceData(SubjectEnum.逾期贷款.getId(), period).getByye();

            BigDecimal zc7_nc = getBalanceData(SubjectEnum.国家债券.getId(), begain).getSyye();
            BigDecimal zc7_qm = getBalanceData(SubjectEnum.国家债券.getId(), period).getByye();

            BigDecimal sumzcNC = zc1_nc.add(zc2_nc).add(zc3_nc).add(zc4_nc).add(zc5_nc).add(zc6_nc).add(zc7_nc);
            BigDecimal sumzcQM = zc1_qm.add(zc2_qm).add(zc3_qm).add(zc4_qm).add(zc5_qm).add(zc6_qm).add(zc7_qm);
            tableZICHAN.addRow(wrapMap("xm", "住房公积金存款", "hc", "1", "nc", zc1_nc.toString(), "qm", zc1_qm.toString()))
                    .addRow(wrapMap("xm", "增值收益存款", "hc", "2", "nc", zc2_nc.toString(), "qm", zc2_qm.toString()))
                    .addRow(wrapMap("xm", "应收利息", "hc", "5", "nc", zc3_nc.toString(), "qm", zc3_qm.toString()))
                    .addRow(wrapMap("xm", "其他应收款", "hc", "7", "nc", zc4_nc.toString(), "qm", zc4_qm.toString()))
                    .addRow(wrapMap("xm", "委托贷款", "hc", "8", "nc", zc5_nc.toString(), "qm", zc5_qm.toString()))
                    .addRow(wrapMap("xm", "逾期贷款", "hc", "9", "nc", zc6_nc.toString(), "qm", zc6_qm.toString()))
                    .addRow(wrapMap("xm", "国家债券", "hc", "12", "nc", zc7_nc.toString(), "qm", zc7_qm.toString()))
                    .addRow(wrapMap("xm", "资产合计", "hc", "15", "nc", sumzcNC.toString(), "qm", sumzcQM.toString()));

            // 负债表
            ReportTable.Table tableFUZHAI = new ReportTable.Table("负债");

            BigDecimal fz1_nc = getBalanceData(SubjectEnum.住房公积金.getId(), begain).getSyye();
            BigDecimal fz1_qm = getBalanceData(SubjectEnum.住房公积金.getId(), period).getByye();

            BigDecimal fz2_nc = getBalanceData(SubjectEnum.应付利息.getId(), begain).getSyye();
            BigDecimal fz2_qm = getBalanceData(SubjectEnum.应付利息.getId(), period).getByye();

            BigDecimal fz3_nc = getBalanceData(SubjectEnum.其他应付款.getId(), begain).getSyye();
            BigDecimal fz3_qm = getBalanceData(SubjectEnum.其他应付款.getId(), period).getByye();

            BigDecimal fz4_nc = getBalanceData(SubjectEnum.专项应付款.getId(), begain).getSyye();
            BigDecimal fz4_qm = getBalanceData(SubjectEnum.专项应付款.getId(), period).getByye();

            BigDecimal fz5_nc = getBalanceData(SubjectEnum.城市廉租住房建设补充资金.getId(), begain).getSyye();
            BigDecimal fz5_qm = getBalanceData(SubjectEnum.城市廉租住房建设补充资金.getId(), period).getByye();

            BigDecimal sumfzNC = fz1_nc.add(fz2_nc).add(fz3_nc).add(fz4_nc).add(fz5_nc);
            BigDecimal sumfzQM = fz1_qm.add(fz2_qm).add(fz3_qm).add(fz4_qm).add(fz5_qm);

            tableFUZHAI.addRow(wrapMap("xm", "住房公积金", "hc", "16", "nc", fz1_nc.toString(), "qm", fz1_qm.toString()))
                    .addRow(wrapMap("xm", "应付利息", "hc", "19", "nc", fz2_nc.toString(), "qm", fz2_qm.toString()))
                    .addRow(wrapMap("xm", "其他应付款", "hc", "21", "nc", fz3_nc.toString(), "qm", fz3_qm.toString()))
                    .addRow(wrapMap("xm", "专项应付款", "hc", "22", "nc", fz4_nc.toString(), "qm", fz4_qm.toString()))
                    .addRow(wrapMap("xm", "城市廉租住房建设补充资金", "hc", "23", "nc", fz5_nc.toString(), "qm", fz5_qm.toString()))
                    .addRow(wrapMap("xm", "负债合计", "hc", "26", "nc", sumfzNC.toString(), "qm", sumfzQM.toString()));

            //　净资产表
            ReportTable.Table tableJINGZICHAN = new ReportTable.Table("净资产");

            BigDecimal jzc1_nc = getBalanceData(SubjectEnum.贷款风险准备.getId(), begain).getSyye();
            BigDecimal jzc1_qm = getBalanceData(SubjectEnum.贷款风险准备.getId(), period).getByye();

            BigDecimal jzc2_nc = getBalanceData(SubjectEnum.待分配增值收益.getId(), begain).getSyye().add(
                    getBalanceData(SubjectEnum.增值收益.getId(), begain).getSyye()
            );
            BigDecimal jzc2_qm = getBalanceData(SubjectEnum.待分配增值收益.getId(), period).getByye().add(
                    getBalanceData(SubjectEnum.增值收益.getId(), period).getByye()
            );

            BigDecimal sumjzcNC = jzc1_nc.add(jzc2_nc);
            BigDecimal sumjzcQM = jzc1_qm.add(jzc2_qm);

            tableJINGZICHAN.addRow(wrapMap("xm", "贷款风险准备", "hc", "27", "nc", jzc1_nc.toString(), "qm", jzc1_qm.toString()))
                    .addRow(wrapMap("xm", "待分配增值收益", "hc", "28", "nc", jzc2_nc.toString(), "qm", jzc2_qm.toString()))
                    .addRow(wrapMap("xm", "净资产合计", "hc", "29", "nc", sumjzcNC.toString(), "qm", sumjzcQM.toString()));

            ReportTable.Table summary = new ReportTable.Table("合计");
            summary.addRow(wrapMap("xm", "负债净资产合计", "hc", "30", "nc", sumfzNC.add(sumjzcNC).toString(), "qm", sumfzQM.add(sumjzcQM).toString()));
            tables.add(tableZICHAN);
            tables.add(tableFUZHAI);
            tables.add(tableJINGZICHAN);
            tables.add(summary);
            reportTable.setTable(tables);
        } catch (ErrorException e) {
            throw e;
        } finally {
            cache.invalidateAll();
        }

        return reportTable;
    }


    public boolean test(String period) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("kjqj", period);
        filter.put("sfjs", true);
        List<CFinanceAccountPeriod> periods = icFinanceAccountPeriodDAO.list(filter, null, null, null, null, null, null);
        if (periods == null || periods.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * @param subjectId
     * @param period
     * @return
     * @author xuefei_wang
     */
    private CFinanceSubjectsBalance getBalanceData(String subjectId, String period) throws Exception {
        String key = String.format("%s_%s", subjectId, period);
        CFinanceSubjectsBalance fsb = cache.get(key, new Callable<CFinanceSubjectsBalance>() {
            @Override
            public CFinanceSubjectsBalance call() throws Exception {
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("kjqj", period);
                filter.put("sfjs", true);
                List<CFinanceAccountPeriod> periods = icFinanceAccountPeriodDAO.list(filter, null, null, null, null, null, null);
                if (periods.size() == 0 || periods.get(0).getcFinanceSubjectsBalances().size() == 0) {
//                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "会计期间未结账");
                    return new CFinanceSubjectsBalance();
                }
                if (periods.size() == 1) {
                    List<CFinanceSubjectsBalance> subjectsBalances = periods.get(0).getcFinanceSubjectsBalances();
                    for (CFinanceSubjectsBalance cFinanceSubjectsBalance : subjectsBalances) {
                        if (cFinanceSubjectsBalance.getKmbh().equals(subjectId)) {
                            return cFinanceSubjectsBalance;
                        }
                    }
                } else {
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "会计期间(" + period + ")存在多条数据，无法确认有效值");
                }

                return null;
            }
        });
        return fsb;
    }

    /**
     * @param period
     * @param begain
     */
    public void loadBalanceCacheFromDataBase(String period, String begain) throws Exception {

        List<CFinanceAccountPeriod> periods = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", Arrays.asList(period, begain));
            }
        }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (periods.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "会计期间(" + period + ")未全部结帐，请先结算");
        }
        for (CFinanceAccountPeriod fap : periods) {
            List<CFinanceSubjectsBalance> subjectBalances = fap.getcFinanceSubjectsBalances();
            for (CFinanceSubjectsBalance fsb : subjectBalances) {
                String key = String.format("%s_%s", fsb.getKmbh(), period);
                cache.put(key, fsb);
            }
        }
    }

    private ReportTable getReportTable(FinanceReportType reportType, String reportPeriod) {
        CFinanceReport financeReport = DAOBuilder.instance(icFinanceReportDAO).searchFilter(new HashMap<String, Object>() {{
//            this.put("name", reportType.getName());
            this.put("bblx", reportType.getType());
            this.put("bbqj", reportPeriod);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (financeReport == null) {
            return null;
        }
        String reportData = financeReport.getBbsj();
        if (reportData == null) {
            return null;
        }
        return gson.fromJson(reportData, ReportTable.class);
    }

    private void saveReportTable(FinanceReportType reportType, String reportPeriod, ReportTable reportTable) {
        CFinanceReport cFinanceReport = new CFinanceReport();
        cFinanceReport.setName(reportType.getName());
        cFinanceReport.setBblx(reportType.getType());
        cFinanceReport.setBbqj(reportPeriod);
        cFinanceReport.setBbsj(gson.toJson(reportTable));
        DAOBuilder.instance(icFinanceReportDAO).entity(cFinanceReport).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }


    private ReportTable comeputeIncomexpenseReportByMonth(TokenContext tokenContext, String period) throws Exception {
        List<String> rangePeriod = Arrays.asList(period);
        List<String> yearRangePeriod = getYearRangePeriod(period.substring(0, 4));
        List<String> range = yearRangePeriod.subList(0, yearRangePeriod.indexOf(period) + 1);
        ReportTable reportTable = new ReportTable();
        reportTable.setOperator(tokenContext.getUserInfo().getCZY());
        reportTable.setCrateTime(DateUtil.getNowDateTime());
        reportTable.setPeriod(period);
        reportTable.setFinanceUnit(icAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()).getMingCheng());
        reportTable.setReportName(FinanceReportType.住房公积金增值收益表_月.getName());
        List<ReportTable.Table> tables = new ArrayList<>();

        ReportTable.Table income = new ReportTable.Table("业务收入");

        BigDecimal sr2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, rangePeriod);
        BigDecimal sr2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, range);

        BigDecimal sr3_bq = calculatPeriodSum2(SubjectEnum.增值收益利息收入, rangePeriod);
        BigDecimal sr3_lj = calculatPeriodSum2(SubjectEnum.增值收益利息收入, range);


        BigDecimal sr4_bq = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, rangePeriod);
        BigDecimal sr4_lj = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, range);

        BigDecimal sr5_bq = calculatPeriodSum2(SubjectEnum.国家债券利息收入, rangePeriod);
        BigDecimal sr5_lj = calculatPeriodSum2(SubjectEnum.国家债券利息收入, range);

        BigDecimal sr6_bq = calculatPeriodSum2(SubjectEnum.其他收入, rangePeriod);
        BigDecimal sr6_lj = calculatPeriodSum2(SubjectEnum.其他收入, range);

        BigDecimal sr1_bq = sr2_bq.add(sr3_bq).add(sr4_bq).add(sr5_bq).add(sr6_bq);
        BigDecimal sr1_lj = sr2_lj.add(sr3_lj).add(sr4_lj).add(sr5_lj).add(sr6_lj);

        income.addRow(wrapMap("xm", "一. 业务收入", "no", "1", "bq", sr1_bq.toString(), "lj", sr1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息收入", "no", "2", "bq", sr2_bq.toString(), "lj", sr2_lj.toString()))
                .addRow(wrapMap("xm", "增值收益利息收入", "no", "3", "bq", sr3_bq.toString(), "lj", sr3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款利息收入", "no", "4", "bq", sr4_bq.toString(), "lj", sr4_lj.toString()))
                .addRow(wrapMap("xm", "国家债券利息收入", "no", "5", "bq", sr5_bq.toString(), "lj", sr5_lj.toString()))
                .addRow(wrapMap("xm", "其它收入", "no", "6", "bq", sr6_bq.toString(), "lj", sr6_lj.toString()));

        ReportTable.Table expense = new ReportTable.Table("业务支出");

        BigDecimal zc2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, rangePeriod);
        BigDecimal zc2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, range);

        BigDecimal zc3_bq = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, rangePeriod);
        BigDecimal zc3_lj = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, range);


        BigDecimal zc4_bq = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, rangePeriod);
        BigDecimal zc4_lj = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, range);

        BigDecimal zc1_bq = zc2_bq.add(zc3_bq).add(zc4_bq);
        BigDecimal zc1_lj = zc2_lj.add(zc3_lj).add(zc4_lj);

        expense.addRow(wrapMap("xm", "二.业务支出", "no", "1", "bq", zc1_bq.toString(), "lj", zc1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息支出", "no", "2", "bq", zc2_bq.toString(), "lj", zc2_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金归集手续费支出", "no", "3", "bq", zc3_bq.toString(), "lj", zc3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款手续费支出", "no", "4", "bq", zc4_bq.toString(), "lj", zc4_lj.toString()));

        ReportTable.Table puregeIncome = new ReportTable.Table("增值收益")
                .addRow(wrapMap("xm", "三、增值收益", "no", "1", "bq", sr1_bq.subtract(zc1_bq).toString(), "lj", sr1_lj.subtract(zc1_lj).toString()));
        tables.add(income);
        tables.add(expense);
        tables.add(puregeIncome);
        reportTable.setTable(tables);
        return reportTable;
    }

    private List<String> getYearRangePeriod(String year) {
        List<String> months = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        return months.stream().map(m -> String.format("%s%s", year, m)).collect(Collectors.toList());
    }

    private ReportTable comeputeIncomexpenseReportByQuarter(TokenContext tokenContext, int year, int quarter) throws Exception {
        List<String> rangePeriod = new ArrayList<>();
        List<String> yearRangePeriod = getYearRangePeriod(String.valueOf(year));
        List<String> range = yearRangePeriod.subList(0, yearRangePeriod.indexOf(String.format("%s%02d", year, quarter * 3)) + 1);

        switch (quarter) {
            case 1:
                rangePeriod.addAll(Arrays.asList(String.valueOf(year) + "01", String.valueOf(year) + "02", String.valueOf(year) + "03"));
                break;
            case 2:
                rangePeriod.addAll(Arrays.asList(String.valueOf(year) + "04", String.valueOf(year) + "05", String.valueOf(year) + "06"));
                break;
            case 3:
                rangePeriod.addAll(Arrays.asList(String.valueOf(year) + "07", String.valueOf(year) + "08", String.valueOf(year) + "09"));
                break;
            case 4:
                rangePeriod.addAll(Arrays.asList(String.valueOf(year) + "10", String.valueOf(year) + "11", String.valueOf(year) + "12"));
                break;
        }
        ReportTable reportTable = new ReportTable();
        reportTable.setOperator(tokenContext.getUserInfo().getCZY());
        reportTable.setCrateTime(DateUtil.getNowDateTime());
        reportTable.setPeriod(String.format("%s$%s", year, quarter));
        reportTable.setFinanceUnit(icAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()).getMingCheng());
        reportTable.setReportName(FinanceReportType.住房公积金增值收益表_季度.getName());
        List<ReportTable.Table> tables = new ArrayList<>();

        ReportTable.Table income = new ReportTable.Table("业务收入");

        BigDecimal sr2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, rangePeriod);
        BigDecimal sr2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, range);

        BigDecimal sr3_bq = calculatPeriodSum2(SubjectEnum.增值收益利息收入, rangePeriod);
        BigDecimal sr3_lj = calculatPeriodSum2(SubjectEnum.增值收益利息收入, range);


        BigDecimal sr4_bq = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, rangePeriod);
        BigDecimal sr4_lj = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, range);

        BigDecimal sr5_bq = calculatPeriodSum2(SubjectEnum.国家债券利息收入, rangePeriod);
        BigDecimal sr5_lj = calculatPeriodSum2(SubjectEnum.国家债券利息收入, range);

        BigDecimal sr6_bq = calculatPeriodSum2(SubjectEnum.其他收入, rangePeriod);
        BigDecimal sr6_lj = calculatPeriodSum2(SubjectEnum.其他收入, range);

        BigDecimal sr1_bq = sr2_bq.add(sr3_bq).add(sr4_bq).add(sr5_bq).add(sr6_bq);
        BigDecimal sr1_lj = sr2_lj.add(sr3_lj).add(sr4_lj).add(sr5_lj).add(sr6_lj);

        income.addRow(wrapMap("xm", "一. 业务收入", "no", "1", "bq", sr1_bq.toString(), "lj", sr1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息收入", "no", "2", "bq", sr2_bq.toString(), "lj", sr2_lj.toString()))
                .addRow(wrapMap("xm", "增值收益利息收入", "no", "3", "bq", sr3_bq.toString(), "lj", sr3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款利息收入", "no", "4", "bq", sr4_bq.toString(), "lj", sr4_lj.toString()))
                .addRow(wrapMap("xm", "国家债券利息收入", "no", "5", "bq", sr5_bq.toString(), "lj", sr5_lj.toString()))
                .addRow(wrapMap("xm", "其它收入", "no", "6", "bq", sr6_bq.toString(), "lj", sr6_lj.toString()));

        ReportTable.Table expense = new ReportTable.Table("业务支出");

        BigDecimal zc2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, rangePeriod);
        BigDecimal zc2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, range);

        BigDecimal zc3_bq = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, rangePeriod);
        BigDecimal zc3_lj = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, range);


        BigDecimal zc4_bq = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, rangePeriod);
        BigDecimal zc4_lj = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, range);

        BigDecimal zc1_bq = zc2_bq.add(zc3_bq).add(zc4_bq);
        BigDecimal zc1_lj = zc2_lj.add(zc3_lj).add(zc4_lj);

        expense.addRow(wrapMap("xm", "二.业务支出", "no", "1", "bq", zc1_bq.toString(), "lj", zc1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息支出", "no", "2", "bq", zc2_bq.toString(), "lj", zc2_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金归集手续费支出", "no", "3", "bq", zc3_bq.toString(), "lj", zc3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款手续费支出", "no", "4", "bq", zc4_bq.toString(), "lj", zc4_lj.toString()));

        ReportTable.Table puregeIncome = new ReportTable.Table("增值收益")
                .addRow(wrapMap("xm", "三、增值收益", "no", "1", "bq", sr1_bq.subtract(zc1_bq).toString(), "lj", sr1_lj.subtract(zc1_lj).toString()));
        tables.add(income);
        tables.add(expense);
        tables.add(puregeIncome);
        reportTable.setTable(tables);
        return reportTable;
    }

    private ReportTable comeputeIncomexpenseReportByYear(TokenContext tokenContext, int year) throws Exception {
        ReportTable reportTable = new ReportTable();
        reportTable.setOperator(tokenContext.getUserInfo().getCZY());
        reportTable.setCrateTime(DateUtil.getNowDateTime());
        reportTable.setPeriod(String.format("%s", year));
        reportTable.setFinanceUnit(icAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()).getMingCheng());
        reportTable.setReportName(FinanceReportType.住房公积金增值收益表_年度.getName());
        List<ReportTable.Table> tables = new ArrayList<>();
        List<String> rangePeriod = getYearRangePeriod(String.valueOf(year));
        List<String> range = getYearRangePeriod(String.valueOf(year - 1));
        ReportTable.Table income = new ReportTable.Table("业务收入");

        BigDecimal sr2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, rangePeriod);
        BigDecimal sr2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息收入, range);

        BigDecimal sr3_bq = calculatPeriodSum2(SubjectEnum.增值收益利息收入, rangePeriod);
        BigDecimal sr3_lj = calculatPeriodSum2(SubjectEnum.增值收益利息收入, range);


        BigDecimal sr4_bq = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, rangePeriod);
        BigDecimal sr4_lj = calculatPeriodSum2(SubjectEnum.委托贷款利息收入, range);

        BigDecimal sr5_bq = calculatPeriodSum2(SubjectEnum.国家债券利息收入, rangePeriod);
        BigDecimal sr5_lj = calculatPeriodSum2(SubjectEnum.国家债券利息收入, range);

        BigDecimal sr6_bq = calculatPeriodSum2(SubjectEnum.其他收入, rangePeriod);
        BigDecimal sr6_lj = calculatPeriodSum2(SubjectEnum.其他收入, range);

        BigDecimal sr1_bq = sr2_bq.add(sr3_bq).add(sr4_bq).add(sr5_bq).add(sr6_bq);
        BigDecimal sr1_lj = sr2_lj.add(sr3_lj).add(sr4_lj).add(sr5_lj).add(sr6_lj);

        income.addRow(wrapMap("xm", "一. 业务收入", "no", "1", "bq", sr1_bq.toString(), "lj", sr1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息收入", "no", "2", "bq", sr2_bq.toString(), "lj", sr2_lj.toString()))
                .addRow(wrapMap("xm", "增值收益利息收入", "no", "3", "bq", sr3_bq.toString(), "lj", sr3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款利息收入", "no", "4", "bq", sr4_bq.toString(), "lj", sr4_lj.toString()))
                .addRow(wrapMap("xm", "国家债券利息收入", "no", "5", "bq", sr5_bq.toString(), "lj", sr5_lj.toString()))
                .addRow(wrapMap("xm", "其它收入", "no", "6", "bq", sr6_bq.toString(), "lj", sr6_lj.toString()));

        ReportTable.Table expense = new ReportTable.Table("业务支出");

        BigDecimal zc2_bq = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, rangePeriod);
        BigDecimal zc2_lj = calculatPeriodSum2(SubjectEnum.住房公积金利息支出, range);

        BigDecimal zc3_bq = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, rangePeriod);
        BigDecimal zc3_lj = calculatPeriodSum2(SubjectEnum.住房公积金归集手续费支出, range);


        BigDecimal zc4_bq = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, rangePeriod);
        BigDecimal zc4_lj = calculatPeriodSum2(SubjectEnum.委托贷款手续费支出, range);

        BigDecimal zc1_bq = zc2_bq.add(zc3_bq).add(zc4_bq);
        BigDecimal zc1_lj = zc2_lj.add(zc3_lj).add(zc4_lj);

        expense.addRow(wrapMap("xm", "二.业务支出", "no", "1", "bq", zc1_bq.toString(), "lj", zc1_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金利息支出", "no", "2", "bq", zc2_bq.toString(), "lj", zc2_lj.toString()))
                .addRow(wrapMap("xm", "住房公积金归集手续费支出", "no", "3", "bq", zc3_bq.toString(), "lj", zc3_lj.toString()))
                .addRow(wrapMap("xm", "委托贷款手续费支出", "no", "4", "bq", zc4_bq.toString(), "lj", zc4_lj.toString()));

        ReportTable.Table puregeIncome = new ReportTable.Table("增值收益")
                .addRow(wrapMap("xm", "三、增值收益", "no", "1", "bq", sr1_bq.subtract(zc1_bq).toString(), "lj", sr1_lj.subtract(zc1_lj).toString()));
        tables.add(income);
        tables.add(expense);
        tables.add(puregeIncome);
        reportTable.setTable(tables);
        return reportTable;
    }

    /**
     * 增值收益分配表
     *
     * @param tokenContext
     * @param year
     * @return
     * @author xuefei_wang
     */
    @Override
    public ReportTable createOrGetIncomallocationReport(TokenContext tokenContext, int year) throws Exception {
        String reportPeriod = String.valueOf(year);
        String period = String.format("%s%s", year, 12);
        if (!test(period)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该报表期间未全部结帐，请先结算");
        }
        ReportTable reportTable = getReportTable(FinanceReportType.住房公积金增值收益分配表_年, reportPeriod);
        if (reportTable == null) {
            reportTable = comeputeIncomallocationReport(tokenContext, year);
            saveReportTable(FinanceReportType.住房公积金增值收益分配表_年, reportPeriod, reportTable);
        }
        return reportTable;
    }

    //获取会计期间指定科目本月余额
    private BigDecimal calculatPeriodSum(SubjectEnum subject, List<String> ranges) throws Exception {
        BigDecimal sum = BigDecimal.ZERO;
        for (String period : ranges) {
            CFinanceSubjectsBalance balance = getBalanceData(subject.getId(), period);
            BigDecimal blancePeriod = balance.getByye().abs();
            sum = sum.add(blancePeriod);
        }
        return sum;
    }
    //获取会计期间指定科目本月增加
    private BigDecimal calculatPeriodSum2(SubjectEnum subject, List<String> ranges) throws Exception {
        BigDecimal sum = BigDecimal.ZERO;
        for (String period : ranges) {
            CFinanceSubjectsBalance balance = getBalanceData(subject.getId(), period);
            BigDecimal blancePeriod = balance.getByzj().abs();
            sum = sum.add(blancePeriod);
        }
        return sum;
    }


    private ReportTable comeputeIncomallocationReport(TokenContext tokenContext, int year) throws Exception {
        ReportTable reportTable = new ReportTable();
        reportTable.setOperator(tokenContext.getUserInfo().getCZY());
        reportTable.setCrateTime(DateUtil.getNowDateTime());
        reportTable.setPeriod(String.format("%s", year));
        reportTable.setFinanceUnit(icAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD()).getMingCheng());
        reportTable.setReportName(FinanceReportType.住房公积金增值收益分配表_年.getName());

        ReportTable lastIncomallocationReportTable = getReportTable(FinanceReportType.住房公积金增值收益分配表_年, String.valueOf(year - 1));
        List<String> rangeYear = getYearRangePeriod(String.valueOf(year));
        boolean isNotNull = (lastIncomallocationReportTable != null);

        BigDecimal h1_bnsj = calculatPeriodSum2(SubjectEnum.业务收入, rangeYear).subtract(calculatPeriodSum2(SubjectEnum.业务支出, rangeYear));
        BigDecimal h1_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "1", "bnsj")) : BigDecimal.ZERO);

        BigDecimal h2_bnsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "7", "bnsj")) : BigDecimal.ZERO);
        BigDecimal h2_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "7", "snsj")) : BigDecimal.ZERO);

        BigDecimal h3_bnsj = h1_bnsj.add(h2_bnsj);
        BigDecimal h3_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "3", "bnsj")) : BigDecimal.ZERO);

        BigDecimal h4_bnsj = calculatPeriodSum(SubjectEnum.增值收益分配_提取贷款风险准备金, rangeYear);
        BigDecimal h4_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "4", "bnsj")) : BigDecimal.ZERO);

        BigDecimal h5_bnsj = calculatPeriodSum(SubjectEnum.增值收益分配_提取公积金中心管理费用, rangeYear);
        BigDecimal h5_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "5", "bnsj")) : BigDecimal.ZERO);

        BigDecimal h6_bnsj = calculatPeriodSum(SubjectEnum.增值收益分配_城市廉租住房补充资金, rangeYear);
        BigDecimal h6_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "6", "bnsj")) : BigDecimal.ZERO);

        BigDecimal h7_bnsj = h3_bnsj.subtract(h4_bnsj).subtract(h5_bnsj).subtract(h6_bnsj);
        BigDecimal h7_snsj = (isNotNull ? new BigDecimal(getReportItemValue(lastIncomallocationReportTable, "7", "bnsj")) : BigDecimal.ZERO);

        List<ReportTable.Table> tables = new ArrayList<>();
        ReportTable.Table incomallocation = new ReportTable.Table("增值收益分配");

        incomallocation
                .addRow(wrapMap("xm", "一．增值收益", "hc", "1", "bnsj", String.valueOf(h1_bnsj), "snsj", String.valueOf(h1_snsj)))
                .addRow(wrapMap("xm", "年初弥补损失", "hc", "2", "bnsj", String.valueOf(h2_bnsj), "snsj", String.valueOf(h2_snsj)))
                .addRow(wrapMap("xm", "二．可供分配的增值收益", "hc", "3", "bnsj", String.valueOf(h3_bnsj), "snsj", String.valueOf(h3_snsj)))
                .addRow(wrapMap("xm", "提取贷款风险准备", "hc", "4", "bnsj", String.valueOf(h4_bnsj), "snsj", String.valueOf(h4_snsj)))
                .addRow(wrapMap("xm", "提取公积金管理费用", "hc", "5", "bnsj", String.valueOf(h5_bnsj), "snsj", String.valueOf(h5_snsj)))
                .addRow(wrapMap("xm", "城市廉租房建设补充资金", "hc", "6", "bnsj", String.valueOf(h6_bnsj), "snsj", String.valueOf(h6_snsj)))
                .addRow(wrapMap("xm", "三．年末未弥补损失", "hc", "7", "bnsj", String.valueOf(h7_bnsj), "snsj", String.valueOf(h7_snsj)));
        tables.add(incomallocation);
        reportTable.setTable(tables);
        return reportTable;

    }

    private String getReportItemValue(ReportTable reportTable, String no, String item) {
        List<ReportTable.Table> tables = reportTable.getTable();
        for (ReportTable.Table table : tables) {
            List<HashMap<String, String>> rows = table.getRows();
            for (HashMap<String, String> row : rows) {
                if (row.get("hc").equals(no)) {
                    return new BigDecimal(row.get(item)).toString();
                }
            }
        }
        return String.valueOf(BigDecimal.ZERO);
    }

    /**
     * @param datas
     * @return
     * @author xuefei_wang
     */
    private HashMap<String, String> wrapMap(String... datas) {
        HashMap<String, String> wrap = new HashMap<>();
        int len = datas.length;
        for (int i = 0; i < len; i = i + 2) {
            wrap.put(datas[i], datas[i + 1]);
        }
        return wrap;
    }


    /**
     * @param CXNY
     * @return
     * @author tanyi
     */
    @Override
    public HousingfundDeposit addhousingfundDepositResport(TokenContext tokenContext, String CXNY) {
        if (!StringUtil.notEmpty(CXNY)) {
            CXNY = DateUtil.date2Str(new Date(), "yyyy-MM");
        }
        HousingfundDeposit res = new HousingfundDeposit();
        res.setBCRQ(DateUtil.date2Str(new Date(), "yyyy-MM-dd"));
        res.setBiaoHao("建金 3-1 表");
        res.setDi("地");
        res.setJGMC(tokenContext.getUserInfo().getYWWDMC());
        res.setPZJG("国家统计局");
        res.setPZWH("国统制【2015】73号");
        res.setSheng("省");
        res.setShiJian(CXNY);
        res.setTBR("");
        res.setXZHQDM("520500");
        res.setYXQZ("2016年8月");
        res.setZDJG("住房和城乡建设部");
        String KJQJ = null;
        try {
            KJQJ = DateUtil.date2Str(DateUtil.str2Date("yyyy-MM", CXNY), "yyyyMM");
        } catch (ParseException e) {
            throw new ErrorException(e);
        }

        String finalKJQJ = KJQJ;
        CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", finalKJQJ);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cFinanceAccountPeriod == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间不存在");
        }
        ArrayList<HousingfundDepositDetail> deposits = new ArrayList<>();

        Date ksdate = cFinanceAccountPeriod.getQsrq();
        Date jsdate = new Date(cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000);

        boolean isend = new Date().getTime() <= jsdate.getTime();

        List<CHousingfundDepositDetail> housingfundDepositDetails = cFinanceAccountPeriod.getcHousingfundDepositDetails();
        if (housingfundDepositDetails == null || housingfundDepositDetails.size() <= 0 || isend) {

            housingfundDepositDetails = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(DateUtil.str2Date("yyyy-MM", CXNY));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
            calendar.add(Calendar.MONTH, -1);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            String KJQJUP = format.format(calendar.getTime());

            CFinanceAccountPeriod cFinanceAccountPeriodold = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", KJQJUP);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<CHousingfundDepositDetail> details;
            if (cFinanceAccountPeriodold != null) {
                details = cFinanceAccountPeriodold.getcHousingfundDepositDetails();
                if (details == null) {
                    details = new ArrayList<>();
                }
            } else {
                details = new ArrayList<>();
            }

            System.out.println("----STEP_1 PASSED");

            housingfundDepositDetails.add(getDepositDetail("一、住房公积金缴存和提取", "一、", "-", "-", "-"));

            //region 住房公积金缴存和提取
            // 114=103+108；115=104+109；
            // 116=105+108-109=114-115；118=106+110-111；119=107+112-113。
            BigInteger BQSJDWS = stCollectionUnitBusinessDetailsDAO.getBQSJDWS(CXNY);   //本期实缴单位数 101
            BigInteger BQSJZGRS = stCollectionUnitBusinessDetailsDAO.getBQSJZGRS(CXNY);   //本期实缴职工人数 102

            //上期末信息
            CHousingfundDepositDetail SQMJCZE103 = searchCDetail(details, "114");
            BigDecimal SQMJCZE = BigDecimal.ZERO;
            if (SQMJCZE103 != null) {
                SQMJCZE = new BigDecimal(SQMJCZE103.getShuliang());//	上期末缴存总额 103
            }
            CHousingfundDepositDetail SQMGRTQZE104 = searchCDetail(details, "115");
            BigDecimal SQMGRTQZE = BigDecimal.ZERO;
            if (SQMGRTQZE104 != null) {
                SQMGRTQZE = new BigDecimal(SQMGRTQZE104.getShuliang());//	上期末个人提取总额 104
            }
            CHousingfundDepositDetail SQMJCYE105 = searchCDetail(details, "116");
            BigDecimal SQMJCYE = BigDecimal.ZERO;
            if (SQMJCYE105 != null) {
                SQMJCYE = new BigDecimal(SQMJCYE105.getShuliang());//	上期末缴存余额 105
            }
            CHousingfundDepositDetail SQMDWZHS106 = searchCDetail(details, "118");
            BigDecimal SQMDWZHS = BigDecimal.ZERO;
            if (SQMDWZHS106 != null) {
                SQMDWZHS = new BigDecimal(SQMDWZHS106.getShuliang());//	上期末单位账户数 106
            }
            CHousingfundDepositDetail SQMGRZHS107 = searchCDetail(details, "119");
            BigDecimal SQMGRZHS = BigDecimal.ZERO;
            if (SQMGRZHS107 != null) {
                SQMGRZHS = new BigDecimal(SQMGRZHS107.getShuliang());//	上期末个人账户数 107
            }

            System.out.println("----STEP_2_1 PASSED");

            BigDecimal BQSJCE = changeWy(stCollectionUnitBusinessDetailsDAO.getBQSJCE(CXNY));     //本期实缴存额 108
            BigDecimal BQGRTQE = changeWy(stCollectionUnitBusinessDetailsDAO.getBQGRTQE(CXNY));//本期个人提取额 109

            BigInteger BQDWZHKHS = stCollectionUnitBusinessDetailsDAO.getBQDWZHKHS(CXNY);//本期单位账户开户数 110
            BigInteger BQDWZHXHS = stCollectionUnitBusinessDetailsDAO.getBQDWZHXHS(CXNY);//本期单位账户销户数 111
            BigInteger BQGRZHKHS = stCollectionUnitBusinessDetailsDAO.getBQGRZHKHS(CXNY);//本期个人账户开户数 112
            BigInteger BQGRZHXHS = stCollectionUnitBusinessDetailsDAO.getBQGRZHXHS(CXNY);//本期个人账户销户数 113

            BigDecimal BQMJCZE = SQMJCZE.add(fillNull(BQSJCE));//本期末缴存总额 114
            BigDecimal BQMGRTQZE = SQMGRTQZE.add(fillNull(BQGRTQE));//本期末个人提取总额 115
            BigDecimal BQMJCYE = BQMJCZE.subtract(fillNull(BQMGRTQZE));//本期末缴存余额 116

            BigInteger BQLJTQZGS = stCollectionUnitBusinessDetailsDAO.getBQLJTQZGS(CXNY);//本期末累计提取职工数 117
            BigInteger BQMDWZHS;//本期末单位账户数 118
            BigInteger BQMGRZHS;//本期末个人账户数 119
            BigInteger BQMFCZHF = stCollectionUnitBusinessDetailsDAO.getBQMFCZHF(CXNY);//本期末封存账户数 (个人) 120
            BigInteger BQMLJGRZHXHS = stCollectionUnitBusinessDetailsDAO.getBQMLJGRZHXHS(CXNY);//本期末累计个人账户销户数 121

            housingfundDepositDetails.add(getDepositDetail("本期实缴单位数", "", String.valueOf(BQSJDWS), "个", "101"));//StCollectionUnitAccount
            housingfundDepositDetails.add(getDepositDetail("本期实缴职工人数", "", String.valueOf(BQSJZGRS), "人", "102"));//
            housingfundDepositDetails.add(getDepositDetail("上期末缴存总额", "", String.valueOf(SQMJCZE), "万元", "103"));
            housingfundDepositDetails.add(getDepositDetail("上期末个人提取总额", "", String.valueOf(SQMGRTQZE), "万元", "104"));

            housingfundDepositDetails.add(getDepositDetail("上期末缴存余额", "", String.valueOf(SQMJCYE), "万元", "105"));
            housingfundDepositDetails.add(getDepositDetail("上期末单位账户数", "", String.valueOf(SQMDWZHS), "个", "106"));
            housingfundDepositDetails.add(getDepositDetail("上期末个人账户数", "", String.valueOf(SQMGRZHS), "户", "107"));
            housingfundDepositDetails.add(getDepositDetail("本期实缴存额", "", String.valueOf(BQSJCE), "万元", "108"));
            housingfundDepositDetails.add(getDepositDetail("本期个人提取额", "", String.valueOf(BQGRTQE), "万元", "109"));
            housingfundDepositDetails.add(getDepositDetail("本期单位账户开户数", "", String.valueOf(fillterNull(BQDWZHKHS)), "个", "110"));
            housingfundDepositDetails.add(getDepositDetail("本期单位账户销户数", "", String.valueOf(fillterNull(BQDWZHXHS)), "个", "111"));
            housingfundDepositDetails.add(getDepositDetail("本期个人账户开户数", "", String.valueOf(fillterNull(BQGRZHKHS)), "户", "112"));
            housingfundDepositDetails.add(getDepositDetail("本期个人账户销户数", "", String.valueOf(fillterNull(BQGRZHXHS)), "户", "113"));

            housingfundDepositDetails.add(getDepositDetail("本期末缴存总额", "", String.valueOf(BQMJCZE), "万元", "114"));

            housingfundDepositDetails.add(getDepositDetail("本期末个人提取总额", "", String.valueOf(BQMGRTQZE), "万元", "115"));
            housingfundDepositDetails.add(getDepositDetail("本期末缴存余额", "", String.valueOf(BQMJCYE), "万元", "116"));
            housingfundDepositDetails.add(getDepositDetail("本期末累计提取职工数", "", String.valueOf(fillterNull(BQLJTQZGS)), "人", "117"));

            BQMDWZHS = SQMDWZHS.add(new BigDecimal(BQDWZHKHS)).subtract(new BigDecimal(BQDWZHXHS)).toBigInteger();
            housingfundDepositDetails.add(getDepositDetail("本期末单位账户数", "", String.valueOf(BQMDWZHS), "个", "118"));

            BQMGRZHS = SQMGRZHS.add(new BigDecimal(BQGRZHKHS)).subtract(new BigDecimal(BQGRZHXHS)).toBigInteger();
            housingfundDepositDetails.add(getDepositDetail("本期末个人账户数", "", String.valueOf(BQMGRZHS), "户", "119"));
            housingfundDepositDetails.add(getDepositDetail("     其中：本期末封存账户数", "", String.valueOf(fillterNull(BQMFCZHF)), "户", "120"));
            housingfundDepositDetails.add(getDepositDetail("本期末累计个人账户销户数", "", String.valueOf(fillterNull(BQMLJGRZHXHS)), "户", "121"));

            System.out.println("----STEP_2_2 PASSED");

            //region 住房公积金个人住房贷款

            housingfundDepositDetails.add(getDepositDetail("二、住房公积金个人住房贷款", "二、", "-", "-", "-"));

            CHousingfundDepositDetail detail201 = searchCDetail(details, "206");
            String shuliang201 = "0.00";
            if (detail201 != null) {
                shuliang201 = String.valueOf(detail201.getShuliang());
            }
            housingfundDepositDetails.add(getDepositDetail("上期末贷款总额", "", shuliang201, "万元", "201"));

            CHousingfundDepositDetail detail202 = searchCDetail(details, "207");
            String shuliang202 = "0.00";
            if (detail202 != null) {
                shuliang202 = String.valueOf(detail202.getShuliang());
            }
            housingfundDepositDetails.add(getDepositDetail("上期末贷款余额", "", shuliang202, "万元", "202"));

            CHousingfundDepositDetail detail203 = searchCDetail(details, "210");
            String shuliang203 = "0";
            if (detail203 != null) {
                shuliang203 = detail203.getShuliang();
            }
            housingfundDepositDetails.add(getDepositDetail("上期末累计放贷笔数", "", shuliang203, "笔", "203"));

            System.out.println("----STEP_3_1 PASSED");

            BigDecimal shuliang204 = loanHousingLoanDAO.getLoanDkffeTotal(ksdate, jsdate);
            shuliang204 = shuliang204.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);

            System.out.println("----STEP_3_1_1 PASSED");

            housingfundDepositDetails.add(getDepositDetail("本期发放额", "", String.valueOf(shuliang204), "万元", "204"));

            BigDecimal shuliang205 = stHousingBusinessDetailsDAO.getLoanBackTotal(false, ksdate, jsdate);

            System.out.println("----STEP_3_1_2 PASSED");

            shuliang205 = shuliang205.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
            housingfundDepositDetails.add(getDepositDetail("本期回收额", "", String.valueOf(shuliang205), "万元", "205"));

            System.out.println("----STEP_3_1_3 PASSED");

            BigDecimal shuliang206 = new BigDecimal(shuliang201).add(shuliang204).setScale(2, BigDecimal.ROUND_HALF_UP);
            housingfundDepositDetails.add(getDepositDetail("本期末贷款总额", "", String.valueOf(shuliang206), "万元", "206"));

            System.out.println("----STEP_3_1_4 PASSED");

            BigDecimal shuliang207 = new BigDecimal(shuliang202).add(shuliang204).subtract(shuliang205).setScale(2, BigDecimal.ROUND_HALF_UP);
            housingfundDepositDetails.add(getDepositDetail("本期末贷款余额", "", String.valueOf(shuliang207), "万元", "207"));

            System.out.println("----STEP_3_1_5 PASSED");

            BigDecimal shuliang208 = iStHousingOverdueRegistrationDAO.getLoanBackTotal(ksdate, jsdate);

            System.out.println("----STEP_3_1_6 PASSED");

            shuliang208 = shuliang208.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
            housingfundDepositDetails.add(getDepositDetail("     其中：逾期贷款额", "", String.valueOf(shuliang208), "万元", "208"));

            System.out.println("----STEP_3_1_7 PASSED");

            Long shuliang209 = loanHousingLoanDAO.getLoanDkffeCount(ksdate, jsdate);
            housingfundDepositDetails.add(getDepositDetail("本期放贷笔数", "", String.valueOf(shuliang209), "笔", "209"));

            System.out.println("----STEP_3_1_8 PASSED");

            BigDecimal shuliang210 = new BigDecimal(shuliang203).add(new BigDecimal(shuliang209));
            housingfundDepositDetails.add(getDepositDetail("本期末累计放贷笔数", "", String.valueOf(shuliang210), "笔", "210"));

            System.out.println("----STEP_3_1_9 PASSED");

            // TODO: 2018/2/5 改为直接算出
            BigInteger shuliang211 = icLoanHousingPersonInformationBasicDAO.accountOverCount(jsdate);
            housingfundDepositDetails.add(getDepositDetail("本期末存量贷款笔数", "", String.valueOf(shuliang211), "笔", "211"));

            System.out.println("----STEP_3_1_10 PASSED");

            // TODO: 2018/2/5 改为直接算出
            BigInteger shuliang212 = iStHousingOverdueRegistrationDAO.overdueCount(jsdate);
            housingfundDepositDetails.add(getDepositDetail("     其中：逾期贷款笔数", "", String.valueOf(shuliang212), "笔", "212"));

            System.out.println("----STEP_3_1_11 PASSED");

            System.out.println("----STEP_3_2 PASSED");

            //endregion

            //region 住房公积金试点项目贷款

            housingfundDepositDetails.add(getDepositDetail("三、住房公积金试点项目贷款", "三、", "-", "-", "-"));

            housingfundDepositDetails.add(getDepositDetail("上期末贷款余额", "", "0.00", "", "301"));

            housingfundDepositDetails.add(getDepositDetail("本期发放额", "", "0.00", "", "302"));

            housingfundDepositDetails.add(getDepositDetail("本期回收额", "", "0.00", "", "303"));

            housingfundDepositDetails.add(getDepositDetail("本期末贷款余额", "", "0.00", "", "304"));

            //endregion

            //region 住房公积金购买国债
            housingfundDepositDetails.add(getDepositDetail("四、住房公积金购买国债", "四、", "-", "-", "-"));

            housingfundDepositDetails.add(getDepositDetail("上期末国债余额", "", "0.00", "万元", "401"));

            housingfundDepositDetails.add(getDepositDetail("     其中：证券交易所市场", "", "0.00", "万元", "402"));

            housingfundDepositDetails.add(getDepositDetail("         银行间市场", "", "0.00", "万元", "403"));

            housingfundDepositDetails.add(getDepositDetail("         银行柜台", "", "0.00", "万元", "404"));

            housingfundDepositDetails.add(getDepositDetail("本期购买国债额", "", "0.00", "万元", "405"));

            housingfundDepositDetails.add(getDepositDetail("     其中：银行间市场", "", "0.00", "万元", "406"));

            housingfundDepositDetails.add(getDepositDetail("         银行柜台", "", "0.00", "万元", "407"));

            housingfundDepositDetails.add(getDepositDetail("本期兑付、转让、收回额", "", "0.00", "万元", "408"));

            housingfundDepositDetails.add(getDepositDetail("本期末国债余额", "", "0.00", "万元", "409"));

            housingfundDepositDetails.add(getDepositDetail("     其中：证券交易所市场", "", "0.00", "万元", "410"));

            housingfundDepositDetails.add(getDepositDetail("         银行间市场", "", "0.00", "万元", "411"));

            housingfundDepositDetails.add(getDepositDetail("         银行柜台", "", "0.00", "万元", "412"));
            //endregion

            System.out.println("----STEP_4 PASSED");

            if (!isend) {
                cFinanceAccountPeriod.setcHousingfundDepositDetails(housingfundDepositDetails);
                DAOBuilder.instance(icFinanceAccountPeriodDAO).entity(cFinanceAccountPeriod).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
            }
        }

        System.out.println("----STEP_5 PASSED");

        for (CHousingfundDepositDetail detail : housingfundDepositDetails) {
            HousingfundDepositDetail depositDetail = new HousingfundDepositDetail();
            depositDetail.setZBMC(detail.getZbmc());
            depositDetail.setXuHao(String.valueOf(detail.getXuhao()));
            depositDetail.setShuLiang(String.valueOf(detail.getShuliang()));
            depositDetail.setJLDW(detail.getJldw());
            depositDetail.setDaiMa(detail.getDaima());
            deposits.add(depositDetail);
        }

        System.out.println("----STEP_6 PASSED");

        res.setList(deposits);

        return res;
    }

    private BigDecimal changeWy(BigDecimal sqmjcze) {
        sqmjcze = fillNull(sqmjcze);
        return sqmjcze.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal fillNull(BigDecimal value) {
        if (value == null)
            return BigDecimal.ZERO;
        return value;
    }

    private BigInteger fillterNull(BigInteger value) {
        if (value == null)
            return BigInteger.ZERO;
        return value;
    }

    private BigInteger filterNull2(CHousingfundDepositDetail value) {
        if (value == null)
            return BigInteger.ZERO;
        return new BigInteger(value.getShuliang());
    }

    private BigDecimal filterNull(CHousingfundDepositDetail value) {
        if (value == null)
            return BigDecimal.ZERO;
        return new BigDecimal(value.getShuliang());
    }

    @Override
    public HousingfundLoanBalance addhousingfundLoanBalance(TokenContext tokenContext, String CXRQ) {
        if (!StringUtil.notEmpty(CXRQ)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "查询日期");
        }
        CFinanceReport report = DAOBuilder.instance(icFinanceReportDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bblx", FinanceReportType.住房公积金贷款发放和账户余额变动情况统计表.getType());
            this.put("bbqj", CXRQ);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        Date cxrq;
        Date begin;
        try {
            cxrq = DateUtil.str2Date("yyyy-MM-dd", CXRQ);
            begin = DateUtil.str2Date("yyyy-MM-dd", CXRQ.substring(0,8)+"01");
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
        Date jscxrq = new Date(cxrq.getTime() + 24 * 60 * 60 * 1000 - 1);
        Date finalCxrq = cxrq;
        boolean isEnd = new Date().getTime() <= jscxrq.getTime();
        if (isEnd) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "因时间未到，未生成此报表");

        HousingfundLoanBalance res = new HousingfundLoanBalance();
        if (report != null) {
            res = gson.fromJson(report.getBbsj(), HousingfundLoanBalance.class);
        } else {

            List<CLoanHousingLoan> cLoanHousingLoans = DAOBuilder.instance(icLoanHousingLoanDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("state", 1);
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.between("dkffrq", finalCxrq, jscxrq));
                }
            }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            // 当日个贷审批发放额
            BigDecimal drgdspffe = BigDecimal.ZERO;
            for (CLoanHousingLoan loan : cLoanHousingLoans) {
                drgdspffe = drgdspffe.add(new BigDecimal(loan.getDkffe()));
            }
            drgdspffe = drgdspffe.divide(new BigDecimal("10000"), 0, BigDecimal.ROUND_HALF_UP);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cxrq);
            calendar.add(Calendar.MONTH, -1);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            String KJQJUP = format.format(calendar.getTime());

            CFinanceAccountPeriod cFinanceAccountPeriodold = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", KJQJUP);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            List<CFinanceSubjectsBalance> oldbalances;
            if (cFinanceAccountPeriodold != null) {
                if (!cFinanceAccountPeriodold.isSfjs()) {
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "上月未结账");
                }
                oldbalances = cFinanceAccountPeriodold.getcFinanceSubjectsBalances();
                if (oldbalances == null || oldbalances.size() <= 0) {
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "上月未结账");
                }
            } else {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "上月会计期间不存在");
            }
            CFinanceSubjectsBalance ckbalance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, "101");//住房公积金存款
            List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO)
                    .betweenDate(begin, jscxrq)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.add(Restrictions.like("kmbh", "101%"));
                        }
                    })
                    .getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
            BigDecimal JFJE = BigDecimal.ZERO;
            BigDecimal DFJE = BigDecimal.ZERO;
            for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                JFJE = JFJE.add(stFinanceRecordingVoucher.getJffse());
                DFJE = DFJE.add(stFinanceRecordingVoucher.getDffse());
            }
            // 住房公积金专户存款余额
            BigDecimal CKBYYE = ckbalance != null ? ckbalance.getByye().add(JFJE).subtract(DFJE) : BigDecimal.ZERO;

            CFinanceSubjectsBalance sybalance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, "102");//增值收益存款
            List<StFinanceRecordingVoucher> systFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO)
                    .betweenDate(begin, jscxrq)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.add(Restrictions.like("kmbh", "102%"));
                        }
                    })
                    .getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
            BigDecimal SYJFJE = BigDecimal.ZERO;
            BigDecimal SYDFJE = BigDecimal.ZERO;
            for (StFinanceRecordingVoucher stFinanceRecordingVoucher : systFinanceRecordingVouchers) {
                SYJFJE = SYJFJE.add(stFinanceRecordingVoucher.getJffse());
                SYDFJE = SYDFJE.add(stFinanceRecordingVoucher.getDffse());
            }
            // 增值收益专户存款余额
            BigDecimal SYBYYE = sybalance != null ? sybalance.getByye().add(SYJFJE).subtract(SYDFJE) : BigDecimal.ZERO;
            BigDecimal hj = CKBYYE.add(SYBYYE);

            CKBYYE = CKBYYE.divide(new BigDecimal("10000"), 0, BigDecimal.ROUND_HALF_UP);
            SYBYYE = SYBYYE.divide(new BigDecimal("10000"), 0, BigDecimal.ROUND_HALF_UP);
            hj = hj.divide(new BigDecimal("10000"), 0, BigDecimal.ROUND_HALF_UP);
            res.setBZDW(tokenContext.getUserInfo().getYWWDMC());
            res.setDRGDSPFFE(String.valueOf(drgdspffe));
            res.setRiQi(CXRQ);
            res.setYEHJ(String.valueOf(hj));
            res.setZFGJJZHCKYE(String.valueOf(CKBYYE));
            res.setZZSYZHCKYE(String.valueOf(SYBYYE));
            report = new CFinanceReport();
            report.setBblx(FinanceReportType.住房公积金贷款发放和账户余额变动情况统计表.getType());
            report.setBbqj(CXRQ);
            report.setBbsj(gson.toJson(res));
            report.setName("住房公积金贷款发放和账户余额变动情况统计表");
            DAOBuilder.instance(icFinanceReportDAO).entity(report).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
        return res;
    }

    /**
     * 获取住房公积金银行存款情况表
     *
     * @param CXNY
     * @return
     */
    @Override
    public HousingfundBankDeposit checkHousingfundBankBalance(TokenContext tokenContext, String CXNY) {
        String KJQJ;
        try {
            KJQJ = DateUtil.date2Str(DateUtil.str2Date("yyyy-MM", CXNY), "yyyyMM");
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "查询年月格式不正确");
        }
        HousingfundBankDeposit res = new HousingfundBankDeposit();
        String finalKJQJ = KJQJ;
        CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", finalKJQJ);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cFinanceAccountPeriod == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间不存在");
        }
        if (!cFinanceAccountPeriod.isSfjs()) {
            throw new ErrorException("该月未结账");
        }

        ArrayList<HousingBankDepositDetail> deposits = new ArrayList<>();

        res.setBCRQ(DateUtil.date2Str(new Date(), "yyyy-MM-dd"));
        res.setBiaoHao("建金 3-1 表");
        res.setDi("地");
        res.setJGMC(tokenContext != null ? tokenContext.getUserInfo().getYWWDMC() : "毕节市住房公积金管理中心");
        res.setPZJG("国家统计局");
        res.setPZWH("国统制【2015】73号");
        res.setSheng("省");
        res.setShiJian(CXNY);
        res.setTBR("");
        res.setXZHQDM("520500");
        res.setYXQZ("2016年8月");
        res.setZDJG("住房和城乡建设部");

        List<CHousingfundBankDepositDetail> list = cFinanceAccountPeriod.getcHousingfundBankDepositDetails();
        if (list == null || list.size() <= 0) {
            Date jssj = new Date(cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000);
            boolean isEnd = new Date().getTime() <= jssj.getTime();
            if (isEnd) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "因时间未到，未生成此报表");
            list = addHousingfundBankBalance(KJQJ);
        }
        for (CHousingfundBankDepositDetail detail : list) {
            HousingBankDepositDetail depositDetail = new HousingBankDepositDetail();
            depositDetail.setCKJRJG(detail.getCkjrjg());
            depositDetail.setLiLv(String.valueOf(detail.getLilv().setScale(4, BigDecimal.ROUND_HALF_UP)));
            depositDetail.setCKLX(detail.getCklx());
            depositDetail.setZHXZ(detail.getZhxz());
            depositDetail.setZJYE(String.valueOf(detail.getZjye()));
            deposits.add(depositDetail);
        }
        res.setList(deposits);
        return res;
    }

    private List<CHousingfundBankDepositDetail> addHousingfundBankBalance(String KJQJ) {

        CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", KJQJ);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceAccountPeriod == null) {
            iAccountBookService.addAccountPeriod();
            cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", KJQJ);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }

        List<CBankContract> cBankContracts = DAOBuilder.instance(icBankContractDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CFinanceSubjectsBalance> subjectsBalances = cFinanceAccountPeriod.getcFinanceSubjectsBalances();

        List<CHousingfundBankDepositDetail> cHousingfundBankDepositDetails = new ArrayList<>();
        List<CHousingfundBankDepositSum> cHousingfundBankDepositSums = new ArrayList<>();

        //存款专户
        BigDecimal ckzhActivedBalance = new BigDecimal("0.00");
        BigDecimal ckzhActivedRate = new BigDecimal("0.0000");
        BigDecimal ckzhXDBalance = new BigDecimal("0.00");
        BigDecimal ckzhXDRate = new BigDecimal("0.0000");
        BigDecimal ckzhNotice1DBalance = new BigDecimal("0.00");
        BigDecimal ckzhNotice1DRate = new BigDecimal("0.0000");
        BigDecimal ckzhNotice7DBalance = new BigDecimal("0.00");
        BigDecimal ckzhNotice7DRate = new BigDecimal("0.0000");
        BigDecimal ckzhNoticeBalance = new BigDecimal("0.00");
        BigDecimal ckzhNoticeRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed3MBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed3MRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed6MBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed6MRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed1YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed1YRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed2YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed2YRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed3YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed3YRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixed5YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixed5YRate = new BigDecimal("0.0000");
        BigDecimal ckzhXYBalance = new BigDecimal("0.00");
        BigDecimal ckzhXYRate = new BigDecimal("0.0000");
        BigDecimal ckzhOtherBalance = new BigDecimal("0.00");
        BigDecimal ckzhOtherRate = new BigDecimal("0.0000");
        BigDecimal ckzhSubtotalBalance = new BigDecimal("0.00");
        BigDecimal ckzhSubtotalRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixedLess1YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixedLess1YRate = new BigDecimal("0.0000");
        BigDecimal ckzhFixedMore1YBalance = new BigDecimal("0.00");
        BigDecimal ckzhFixedMore1YRate = new BigDecimal("0.0000");

        //增值收益专户
        BigDecimal syzhActivedBalance = new BigDecimal("0.00");
        BigDecimal syzhActivedRate = new BigDecimal("0.0000");
        BigDecimal syzhXDBalance = new BigDecimal("0.00");
        BigDecimal syzhXDRate = new BigDecimal("0.0000");
        BigDecimal syzhNotice1DBalance = new BigDecimal("0.00");
        BigDecimal syzhNotice1DRate = new BigDecimal("0.0000");
        BigDecimal syzhNotice7DBalance = new BigDecimal("0.00");
        BigDecimal syzhNotice7DRate = new BigDecimal("0.0000");
        BigDecimal syzhNoticeBalance = new BigDecimal("0.00");
        BigDecimal syzhNoticeRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed3MBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed3MRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed6MBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed6MRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed1YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed1YRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed2YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed2YRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed3YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed3YRate = new BigDecimal("0.0000");
        BigDecimal syzhFixed5YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixed5YRate = new BigDecimal("0.0000");
        BigDecimal syzhXYBalance = new BigDecimal("0.00");
        BigDecimal syzhXYRate = new BigDecimal("0.0000");
        BigDecimal syzhOtherBalance = new BigDecimal("0.00");
        BigDecimal syzhOtherRate = new BigDecimal("0.0000");
        BigDecimal syzhSubtotalBalance = new BigDecimal("0.00");
        BigDecimal syzhSubtotalRate = new BigDecimal("0.0000");
        BigDecimal syzhFixedLess1YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixedLess1YRate = new BigDecimal("0.0000");
        BigDecimal syzhFixedMore1YBalance = new BigDecimal("0.00");
        BigDecimal syzhFixedMore1YRate = new BigDecimal("0.0000");

        BigDecimal activedLiLv = iPolicyService.getPolicyRate(RateEnum.活期存款利率.getCode());
        if (activedLiLv == null) activedLiLv = new BigDecimal("0.0000");
        BigDecimal xdLiLv = iPolicyService.getPolicyRate(RateEnum.协定存款利率.getCode());
        if (xdLiLv == null) xdLiLv = new BigDecimal("0.0000");

        for (CBankContract bankContract : cBankContracts) {
            CBankNodeInfo cBankNodeInfo = DAOBuilder.instance(icBankNodeInfoDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("node", bankContract.getNode());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            String yhmc = null;
            if (cBankNodeInfo != null) yhmc = cBankNodeInfo.getBank_name();

            List<StSettlementSpecialBankAccount> bankAccounts = bankContract.getStSettlementSpecialBankAccounts();

            for (StSettlementSpecialBankAccount specialBankAccount : bankAccounts) {
                if ("0".equals(specialBankAccount.getcSettlementSpecialBankAccountExtension().getStatus())) {
//                    ActivedBalance balance = iFinanceTrader.getActivedBalance(specialBankAccount.getYhzhhm());
//                    FixedAccBalanceQueryOut fixedBalance = iFinanceTrader.getFixedBalance("admin", specialBankAccount.getYhzhhm(), "", specialBankAccount.getYhzhmc(), false);

                    BigDecimal xdckje = bankContract.getXdckje();
                    if (xdckje == null) xdckje = BigDecimal.ZERO;

                    BigDecimal activedBalance = BigDecimal.ZERO;
                    BigDecimal xiedingBalance = BigDecimal.ZERO;
                    BigDecimal fixed1Balance = BigDecimal.ZERO;
                    BigDecimal fixed2Balance = BigDecimal.ZERO;

                    BigDecimal fixed1Rate = new BigDecimal("0.0000");
                    BigDecimal fixed2Rate = new BigDecimal("0.0000");

                    BigDecimal fixed1RateSum = new BigDecimal("0.0000");
                    BigDecimal fixed2RateSum = new BigDecimal("0.0000");

                    if (subjectsBalances.size() > 0) {
                        for (CFinanceSubjectsBalance cFinanceSubjectsBalance : subjectsBalances) {
                            if (Subject2Account.getSubjectByAccount(specialBankAccount.getYhzhhm(), "活").equals(cFinanceSubjectsBalance.getKmbh())) {
                                BigDecimal balance = cFinanceSubjectsBalance.getByye();
                                if (balance.compareTo(xdckje) <= 0) {
                                    activedBalance = balance;
                                } else {
                                    activedBalance = xdckje;
                                    xiedingBalance = balance.subtract(xdckje);
                                }

                                if ("01".equals(specialBankAccount.getZhxz()) || "02".equals(specialBankAccount.getZhxz())) {
                                    ckzhActivedBalance = ckzhActivedBalance.add(activedBalance);
                                    ckzhActivedRate = ckzhActivedRate.add(activedLiLv.multiply(activedBalance));

                                    ckzhXDBalance = ckzhXDBalance.add(xiedingBalance);
                                    ckzhXDRate = ckzhXDRate.add(xdLiLv.multiply(xiedingBalance));
                                }

                                if ("03".equals(specialBankAccount.getZhxz())) {
                                    syzhActivedBalance = syzhActivedBalance.add(activedBalance);
                                    syzhActivedRate = syzhActivedRate.add(activedLiLv.multiply(activedBalance));

                                    syzhXDBalance = syzhXDBalance.add(xiedingBalance);
                                    syzhXDRate = syzhXDRate.add(xdLiLv.multiply(xiedingBalance));
                                }
                            }

                            if (Subject2Account.getSubjectByAccount(specialBankAccount.getYhzhhm(), "定").equals(cFinanceSubjectsBalance.getKmbh())) {
                                fixed1Balance = cFinanceSubjectsBalance.getByye();
                                if ("01".equals(specialBankAccount.getZhxz()) || "02".equals(specialBankAccount.getZhxz())) {
                                    ckzhFixedLess1YBalance = ckzhFixedLess1YBalance.add(fixed1Balance);
                                }

                                if ("03".equals(specialBankAccount.getZhxz())) {
                                    syzhFixedLess1YBalance = syzhFixedLess1YBalance.add(fixed1Balance);
                                }
                            }
                        }
                    }
//                    List<BDC122Summary> bdc122SummaryList = fixedBalance != null ? fixedBalance.getSUMMARY() : new ArrayList<>();
//                    for (BDC122Summary bdc122Summary : bdc122SummaryList) {
//                        if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("360")) <= 0) {
//                            fixed1Balance = fixed1Balance.add(bdc122Summary.getDrawAmt());
//                            fixed1RateSum = fixed1RateSum.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixedLess1YBalance = ckzhFixedLess1YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixedLess1YRate = ckzhFixedLess1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixedLess1YBalance = syzhFixedLess1YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixedLess1YRate = syzhFixedLess1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else {
//                            fixed2Balance = fixed2Balance.add(bdc122Summary.getDrawAmt());
//                            fixed2RateSum = fixed2RateSum.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixedMore1YBalance = ckzhFixedMore1YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixedMore1YRate = ckzhFixedMore1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixedMore1YBalance = syzhFixedMore1YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixedMore1YRate = syzhFixedMore1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        }
//
//                        if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("90")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed3MBalance = ckzhFixed3MBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed3MRate = ckzhFixed3MRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed3MBalance = syzhFixed3MBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed3MRate = syzhFixed3MRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("180")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed6MBalance = ckzhFixed6MBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed6MRate = ckzhFixed6MRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed6MBalance = syzhFixed6MBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed6MRate = syzhFixed6MRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("360")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed1YBalance = ckzhFixed1YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed1YRate = ckzhFixed1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed1YBalance = syzhFixed1YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed1YRate = syzhFixed1YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("720")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed2YBalance = ckzhFixed2YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed2YRate = ckzhFixed2YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed2YBalance = syzhFixed2YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed2YRate = syzhFixed2YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("1080")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed3YBalance = ckzhFixed3YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed3YRate = ckzhFixed3YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed3YBalance = syzhFixed3YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed3YRate = syzhFixed3YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        } else if (bdc122Summary.getDepositPeriod().compareTo(new BigDecimal("1800")) == 0) {
//                            if ("01".equals(specialBankAccount.getZhxz())) {
//                                ckzhFixed5YBalance = ckzhFixed5YBalance.add(bdc122Summary.getDrawAmt());
//                                ckzhFixed5YRate = ckzhFixed5YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//
//                            if ("03".equals(specialBankAccount.getZhxz())) {
//                                syzhFixed5YBalance = syzhFixed5YBalance.add(bdc122Summary.getDrawAmt());
//                                syzhFixed5YRate = syzhFixed5YRate.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()));
//                            }
//                        }
//                    }
//
//                    if (fixed1Balance.compareTo(BigDecimal.ZERO) != 0)
//                        fixed1Rate = fixed1RateSum.divide(fixed1Balance, 4, BigDecimal.ROUND_HALF_UP).abs();
//                    if (fixed2Balance.compareTo(BigDecimal.ZERO) != 0)
//                        fixed2Rate = fixed2RateSum.divide(fixed2Balance, 4, BigDecimal.ROUND_HALF_UP).abs();

                    cHousingfundBankDepositDetails.add(getBankDepositDetail(yhmc != null ? yhmc : specialBankAccount.getYhmc(), specialBankAccount.getZhxz(), "活期", activedBalance.setScale(2, BigDecimal.ROUND_HALF_UP), activedLiLv.setScale(4, BigDecimal.ROUND_HALF_UP)));
                    cHousingfundBankDepositDetails.add(getBankDepositDetail(yhmc != null ? yhmc : specialBankAccount.getYhmc(), specialBankAccount.getZhxz(), "协定", xiedingBalance.setScale(2, BigDecimal.ROUND_HALF_UP), xdLiLv.setScale(4, BigDecimal.ROUND_HALF_UP)));
                    cHousingfundBankDepositDetails.add(getBankDepositDetail(yhmc != null ? yhmc : specialBankAccount.getYhmc(), specialBankAccount.getZhxz(), "定期一年以内", fixed1Balance.setScale(2, BigDecimal.ROUND_HALF_UP), fixed1Rate.setScale(4, BigDecimal.ROUND_HALF_UP)));
                    cHousingfundBankDepositDetails.add(getBankDepositDetail(yhmc != null ? yhmc : specialBankAccount.getYhmc(), specialBankAccount.getZhxz(), "定期一年以上", fixed2Balance.setScale(2, BigDecimal.ROUND_HALF_UP), fixed2Rate.setScale(4, BigDecimal.ROUND_HALF_UP)));
                }
            }
        }

        if (ckzhActivedBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhActivedRate = ckzhActivedRate.divide(ckzhActivedBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhXDBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhXDRate = ckzhXDRate.divide(ckzhXDBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed3MBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed3MRate = ckzhFixed3MRate.divide(ckzhFixed3MBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed6MBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed6MRate = ckzhFixed6MRate.divide(ckzhFixed6MBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed1YBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed1YRate = ckzhFixed1YRate.divide(ckzhFixed1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed2YBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed2YRate = ckzhFixed2YRate.divide(ckzhFixed2YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed3YBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed3YRate = ckzhFixed3YRate.divide(ckzhFixed3YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixed5YBalance.compareTo(new BigDecimal("0.00")) != 0)
            ckzhFixed5YRate = ckzhFixed5YRate.divide(ckzhFixed5YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        if (syzhActivedBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhActivedRate = syzhActivedRate.divide(syzhActivedBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhXDBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhXDRate = syzhXDRate.divide(syzhXDBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed3MBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed3MRate = syzhFixed3MRate.divide(syzhFixed3MBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed6MBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed6MRate = syzhFixed6MRate.divide(syzhFixed6MBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed1YBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed1YRate = syzhFixed1YRate.divide(syzhFixed1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed2YBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed2YRate = syzhFixed2YRate.divide(syzhFixed2YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed3YBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed3YRate = syzhFixed3YRate.divide(syzhFixed3YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixed5YBalance.compareTo(new BigDecimal("0.00")) != 0)
            syzhFixed5YRate = syzhFixed5YRate.divide(syzhFixed5YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        ckzhSubtotalBalance = ckzhSubtotalBalance.add(ckzhActivedBalance)
                .add(ckzhXDBalance)
                .add(ckzhNotice1DBalance)
                .add(ckzhNotice7DBalance)
//                .add(ckzhFixed3MBalance)
//                .add(ckzhFixed6MBalance)
//                .add(ckzhFixed1YBalance)
//                .add(ckzhFixed2YBalance)
//                .add(ckzhFixed3YBalance)
//                .add(ckzhFixed5YBalance)
                .add(ckzhFixedLess1YBalance)
                .add(ckzhFixedMore1YBalance)
                .add(ckzhXYBalance)
                .add(ckzhOtherBalance);
        ckzhSubtotalRate = ckzhSubtotalRate.add(ckzhActivedBalance.multiply(ckzhActivedRate))
                .add(ckzhXDBalance.multiply(ckzhXDRate))
                .add(ckzhNotice1DBalance.multiply(ckzhNotice1DRate))
                .add(ckzhNotice7DBalance.multiply(ckzhNotice7DRate))
                .add(ckzhFixed3MBalance.multiply(ckzhFixed3MRate))
                .add(ckzhFixed6MBalance.multiply(ckzhFixed6MRate))
                .add(ckzhFixed1YBalance.multiply(ckzhFixed1YRate))
                .add(ckzhFixed2YBalance.multiply(ckzhFixed2YRate))
                .add(ckzhFixed3YBalance.multiply(ckzhFixed3YRate))
                .add(ckzhFixed5YBalance.multiply(ckzhFixed5YRate))
                .add(ckzhXYBalance.multiply(ckzhXYRate))
                .add(ckzhOtherBalance.multiply(ckzhOtherRate));

        if (ckzhSubtotalBalance.compareTo(BigDecimal.ZERO) != 0)
            ckzhSubtotalRate = ckzhSubtotalRate.divide(ckzhSubtotalBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        syzhSubtotalBalance = syzhSubtotalBalance.add(syzhActivedBalance)
                .add(syzhXDBalance)
                .add(syzhNotice1DBalance)
                .add(syzhNotice7DBalance)
//                .add(syzhFixed3MBalance)
//                .add(syzhFixed6MBalance)
//                .add(syzhFixed1YBalance)
//                .add(syzhFixed2YBalance)
//                .add(syzhFixed3YBalance)
//                .add(syzhFixed5YBalance)
                .add(syzhFixedLess1YBalance)
                .add(syzhFixedMore1YBalance)
                .add(syzhXYBalance)
                .add(syzhOtherBalance);
        syzhSubtotalRate = syzhSubtotalRate.add(syzhActivedBalance.multiply(syzhActivedRate))
                .add(syzhXDBalance.multiply(syzhXDRate))
                .add(syzhNotice1DBalance.multiply(syzhNotice1DRate))
                .add(syzhNotice7DBalance.multiply(syzhNotice7DRate))
                .add(syzhFixed3MBalance.multiply(syzhFixed3MRate))
                .add(syzhFixed6MBalance.multiply(syzhFixed6MRate))
                .add(syzhFixed1YBalance.multiply(syzhFixed1YRate))
                .add(syzhFixed2YBalance.multiply(syzhFixed2YRate))
                .add(syzhFixed3YBalance.multiply(syzhFixed3YRate))
                .add(syzhFixed5YBalance.multiply(syzhFixed5YRate))
                .add(syzhXYBalance.multiply(syzhXYRate))
                .add(syzhOtherBalance.multiply(syzhOtherRate));

        if (syzhSubtotalBalance.compareTo(BigDecimal.ZERO) != 0)
            syzhSubtotalRate = syzhSubtotalRate.divide(syzhSubtotalBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        if (ckzhFixedLess1YBalance.compareTo(BigDecimal.ZERO) != 0)
            ckzhFixedLess1YRate = ckzhFixedLess1YRate.divide(ckzhFixedLess1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (ckzhFixedMore1YBalance.compareTo(BigDecimal.ZERO) != 0)
            ckzhFixedMore1YRate = ckzhFixedMore1YRate.divide(ckzhFixedMore1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        if (syzhFixedLess1YBalance.compareTo(BigDecimal.ZERO) != 0)
            syzhFixedLess1YRate = syzhFixedLess1YRate.divide(syzhFixedLess1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();
        if (syzhFixedMore1YBalance.compareTo(BigDecimal.ZERO) != 0)
            syzhFixedMore1YRate = syzhFixedMore1YRate.divide(syzhFixedMore1YBalance, 4, BigDecimal.ROUND_HALF_UP).abs();

        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "活期", ckzhActivedBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhActivedRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "协定", ckzhXDBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhXDRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "通知(一天)", ckzhNotice1DBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhNotice1DRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "通知(七天)", ckzhNotice7DBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhNotice7DRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "通知", ckzhNoticeBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhNoticeRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(三个月)", ckzhFixed3MBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed3MRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(半年)", ckzhFixed6MBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed6MRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(一年)", ckzhFixed1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(二年)", ckzhFixed2YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed2YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(三年)", ckzhFixed3YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed3YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期(五年)", ckzhFixed5YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixed5YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期一年以内", ckzhFixedLess1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixedLess1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "定期一年以上", ckzhFixedMore1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhFixedMore1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "协议", ckzhXYBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhXYRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "其他", ckzhOtherBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhOtherRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("01", "小计", ckzhSubtotalBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhSubtotalRate.setScale(4, BigDecimal.ROUND_HALF_UP)));

        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "活期", syzhActivedBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhActivedRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "协定", syzhXDBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhXDRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "通知(一天)", syzhNotice1DBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhNotice1DRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "通知(七天)", syzhNotice7DBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhNotice7DRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "通知", syzhNoticeBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhNoticeRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(三个月)", syzhFixed3MBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed3MRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(半年)", syzhFixed6MBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed6MRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(一年)", syzhFixed1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(二年)", syzhFixed2YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed2YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(三年)", syzhFixed3YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed3YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期(五年)", syzhFixed5YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixed5YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期一年以内", syzhFixedLess1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixedLess1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "定期一年以上", syzhFixedMore1YBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhFixedMore1YRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "协议", syzhXYBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhXYRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "其他", syzhOtherBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhOtherRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositSums.add(getHousingfundBankDepositSum("03", "小计", syzhSubtotalBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhSubtotalRate.setScale(4, BigDecimal.ROUND_HALF_UP)));

        //住房公积金存款情况表
        cHousingfundBankDepositDetails.add(getBankDepositDetail("合计", "01", "-", ckzhSubtotalBalance.setScale(2, BigDecimal.ROUND_HALF_UP), ckzhSubtotalRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cHousingfundBankDepositDetails.add(getBankDepositDetail("合计", "03", "-", syzhSubtotalBalance.setScale(2, BigDecimal.ROUND_HALF_UP), syzhSubtotalRate.setScale(4, BigDecimal.ROUND_HALF_UP)));
        cFinanceAccountPeriod.setcHousingfundBankDepositDetails(cHousingfundBankDepositDetails);
        //住房公积金存款情况表附表(汇总表)
        cFinanceAccountPeriod.setcHousingfundBankDepositSums(cHousingfundBankDepositSums);

        DAOBuilder.instance(icFinanceAccountPeriodDAO).entity(cFinanceAccountPeriod).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return cHousingfundBankDepositDetails;
    }

    @Override
    public HousingfundLoanReport addHousingLoanReport(TokenContext tokenContext, String CXNY) {

        String KJQJ;
        try {
            KJQJ = DateUtil.date2Str(DateUtil.str2Date("yyyy-MM", CXNY), "yyyyMM");
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "查询年月格式不正确");
        }

        HousingfundLoanReport res = new HousingfundLoanReport();
        CFinanceReport report = DAOBuilder.instance(icFinanceReportDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bblx", FinanceReportType.住房公积金存贷款报备表.getType());
            this.put("bbqj", CXNY);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (report != null) {
            res = gson.fromJson(report.getBbsj(), HousingfundLoanReport.class);
        } else {
            res.setBCRQ(DateUtil.date2Str(new Date(), "yyyy-MM-dd"));
            res.setBiaoHao("建金 3-1 表");
            res.setDi("地");
            res.setJGMC(tokenContext.getUserInfo().getYWWDMC());
            res.setPZJG("国家统计局");
            res.setPZWH("国统制【2015】73号");
            res.setSheng("省");
            res.setShiJian(CXNY);
            res.setTBR("");
            res.setXZHQDM("520500");
            res.setYXQZ("2016年8月");
            res.setZDJG("住房和城乡建设部");

            String finalKJQJ = KJQJ;
            CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", finalKJQJ);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cFinanceAccountPeriod == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间");
            }

            Date jsdate = new Date(cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000);

            boolean isend = new Date().getTime() <= jsdate.getTime();
            if (isend) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "年月条件不符合，未生成此报表");

            String lastKJQJ;

            if (DateUtil.getMonth(CXNY) > 6) {
                int year = DateUtil.getYear(DateUtil.date2Str(new Date(), "yyyyMMdd"));
                lastKJQJ = String.valueOf(year) + "06";
            } else {
                int year = DateUtil.getYear(DateUtil.date2Str(new Date(), "yyyyMMdd"));
                lastKJQJ = String.valueOf(year - 1) + "06";
            }

            CFinanceAccountPeriod lastCFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", lastKJQJ);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            //小计
            BigDecimal XJ1 = BigDecimal.ZERO;
            BigDecimal XJ1LiLv = null;
            BigDecimal XJ3 = BigDecimal.ZERO;
            BigDecimal XJ4 = BigDecimal.ZERO;
            BigDecimal XJ5 = BigDecimal.ZERO;

            List<CHousingfundBankDepositSum> cHousingfundBankDepositSums = cFinanceAccountPeriod.getcHousingfundBankDepositSums();
            List<CFinanceSubjectsBalance> cFinanceSubjectsBalances = cFinanceAccountPeriod.getcFinanceSubjectsBalances();
            List<CFinanceSubjectsBalance> lastCFinanceSubjectsBalances = lastCFinanceAccountPeriod != null ? lastCFinanceAccountPeriod.getcFinanceSubjectsBalances() : new ArrayList<>();

            //1、个人住房公积金
            ArrayList<HousingfundLoanReportDetail> GRZFGJJ = new ArrayList<>();

            //2、其他
            ArrayList<HousingfundLoanReportDetail> QT = new ArrayList<>();
            BigDecimal otherBalance = BigDecimal.ZERO;

            if (cFinanceSubjectsBalances.size() > 0) {
                for (CFinanceSubjectsBalance cFinanceSubjectsBalance : cFinanceSubjectsBalances) {
                    if ("201".equals(cFinanceSubjectsBalance.getKmbh())) {
                        HashMap<String, BigDecimal> rateMap = iPolicyService.getPolicyRateById("GRZFGJJCKLL");
                        BigDecimal snjz = BigDecimal.ZERO;
                        BigDecimal snjzLiLv = rateMap.getOrDefault("snjz", new BigDecimal("0.0000")).setScale(4, BigDecimal.ROUND_HALF_UP);
                        BigDecimal dngj = BigDecimal.ZERO;
                        BigDecimal dngjLiLv = rateMap.getOrDefault("dngj", new BigDecimal("0.0000")).setScale(4, BigDecimal.ROUND_HALF_UP);

                        if (lastCFinanceSubjectsBalances.size() > 0) {
                            for (CFinanceSubjectsBalance lastCFinanceSubjectsBalance : lastCFinanceSubjectsBalances) {
                                if ("201".equals(lastCFinanceSubjectsBalance.getKmbh())) {
                                    snjz = lastCFinanceSubjectsBalance.getByye();
                                    dngj = cFinanceSubjectsBalance.getByye().subtract(snjz);
                                }
                            }
                        } else {
                            dngj = cFinanceSubjectsBalance.getByye();
                        }

                        if (snjz.compareTo(BigDecimal.ZERO) == 0) {
                            snjzLiLv = new BigDecimal("0.0000");
                            XJ1LiLv = dngjLiLv;
                        }
                        if (dngj.compareTo(BigDecimal.ZERO) == 0) {
                            dngjLiLv = new BigDecimal("0.0000");
                            XJ1LiLv = snjzLiLv;
                        }
                        if (snjz.compareTo(BigDecimal.ZERO) == 0 && dngj.compareTo(BigDecimal.ZERO) == 0)
                            XJ1LiLv = new BigDecimal("0.0000");
                        if (snjz.compareTo(BigDecimal.ZERO) != 0 && dngj.compareTo(BigDecimal.ZERO) != 0) {
                            if (cFinanceSubjectsBalance.getByye().compareTo(BigDecimal.ZERO) != 0) {
                                XJ1LiLv = snjz.multiply(snjzLiLv).add(dngj.multiply(dngjLiLv)).divide(cFinanceSubjectsBalance.getByye(), 4, RoundingMode.HALF_UP).abs();
                            }
                        }

                        GRZFGJJ.add(getReportDetail("其中：当年归集", dngj.toPlainString(), dngjLiLv.toPlainString()));
                        GRZFGJJ.add(getReportDetail("上年结转", snjz.toPlainString(), snjzLiLv.toPlainString()));
                        GRZFGJJ.add(getReportDetail("小计", cFinanceSubjectsBalance.getByye().toPlainString(), XJ1LiLv != null ? XJ1LiLv.toPlainString() : "-"));
                        XJ1 = cFinanceSubjectsBalance.getByye();
                    }
                    if ("211".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("214".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("219".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("301".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("311".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("321".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("40105".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.add(cFinanceSubjectsBalance.getByye());
                    if ("111".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.subtract(cFinanceSubjectsBalance.getByye());
                    if ("119".equals(cFinanceSubjectsBalance.getKmbh()))
                        otherBalance = otherBalance.subtract(cFinanceSubjectsBalance.getByye());
                }

                QT.add(getReportDetail("", otherBalance.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "-"));
            } else {
                GRZFGJJ.add(getReportDetail("其中：当年归集", "0.00", "0.0000"));
                GRZFGJJ.add(getReportDetail("上年结转", "0.00", "0.0000"));
                GRZFGJJ.add(getReportDetail("小计", "0.00", "-"));

                QT.add(getReportDetail("", "0.00", "-"));
            }

            res.setGRZFGJJ(GRZFGJJ);
            res.setQT(QT);

            //余额合计1
            ArrayList<HousingfundLoanReportDetail> YEHJ1 = new ArrayList<>();
            BigDecimal YEHJ1Balance = BigDecimal.ZERO;
            YEHJ1Balance = XJ1.add(otherBalance);
            YEHJ1.add(getReportDetail("", YEHJ1Balance.toPlainString(), XJ1LiLv != null ? XJ1LiLv.toPlainString() : "-"));
            res.setYEHJ1(YEHJ1);

            //3、住房公积金存款专户
            ArrayList<HousingfundLoanReportDetail> ZFGJJCKZH = new ArrayList<>();
            //4、住房公积金增值收益专户
            ArrayList<HousingfundLoanReportDetail> ZFGJJZZSYZH = new ArrayList<>();

            if (cHousingfundBankDepositSums.size() > 0) {
                for (CHousingfundBankDepositSum cHousingfundBankDepositSum : cHousingfundBankDepositSums) {
                    String yuE = cHousingfundBankDepositSum.getZjye().toPlainString();
                    String liLv = cHousingfundBankDepositSum.getLilv().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
                    if ("01".equals(cHousingfundBankDepositSum.getZhxz())) {
                        if ("活期".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("其中：活期", yuE, liLv));
                        if ("协定".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("协定", yuE, liLv));
                        if ("通知".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("通知", yuE, liLv));
                        if ("定期一年以内".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("一年以内（含）定期", yuE, liLv));
                        if ("定期一年以上".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("一年以上定期", yuE, liLv));
                        if ("其他".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJCKZH.add(getReportDetail("其他", yuE, liLv));
                        if ("小计".equals(cHousingfundBankDepositSum.getCklx())) {
                            ZFGJJCKZH.add(getReportDetail("余额调节", "0.00", "-"));
                            ZFGJJCKZH.add(getReportDetail("小计", yuE, liLv));
                            XJ3 = cHousingfundBankDepositSum.getZjye();
                        }
                    }

                    if ("03".equals(cHousingfundBankDepositSum.getZhxz())) {
                        if ("活期".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("其中：活期", yuE, liLv));
                        if ("协定".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("协定", yuE, liLv));
                        if ("通知".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("通知", yuE, liLv));
                        if ("定期一年以内".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("一年以内（含）定期", yuE, liLv));
                        if ("定期一年以上".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("一年以上定期", yuE, liLv));
                        if ("其他".equals(cHousingfundBankDepositSum.getCklx()))
                            ZFGJJZZSYZH.add(getReportDetail("其他", yuE, liLv));
                        if ("小计".equals(cHousingfundBankDepositSum.getCklx())) {
                            ZFGJJZZSYZH.add(getReportDetail("余额调节", "0.00", "-"));
                            ZFGJJZZSYZH.add(getReportDetail("小计", yuE, liLv));
                            XJ4 = cHousingfundBankDepositSum.getZjye();
                        }
                    }
                }
            } else {
                ZFGJJCKZH.add(getReportDetail("其中：活期", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("协定", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("通知", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("一年以内（含）定期", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("一年以上定期", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("其他", "0.00", "0.0000"));
                ZFGJJCKZH.add(getReportDetail("余额调节", "0.00", "-"));
                ZFGJJCKZH.add(getReportDetail("小计", "0.00", "0.0000"));

                ZFGJJZZSYZH.add(getReportDetail("其中：活期", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("协定", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("通知", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("一年以内（含）定期", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("一年以上定期", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("其他", "0.00", "0.0000"));
                ZFGJJZZSYZH.add(getReportDetail("余额调节", "0.00", "-"));
                ZFGJJZZSYZH.add(getReportDetail("小计", "0.00", "0.0000"));
            }

            res.setZFGJJCKZH(ZFGJJCKZH);
            res.setZFGJJZZSYZH(ZFGJJZZSYZH);

            //5、个人委托贷款
            ArrayList<HousingfundLoanReportDetail> GRWTDK = new ArrayList<>();
            BigDecimal less5Y = icLoanHousingPersonInformationBasicDAO.getPersonLoanBalanceLess5Y(CXNY);
            BigDecimal more5Y = icLoanHousingPersonInformationBasicDAO.getPersonLoanBalanceMore5Y(CXNY);
            BigDecimal less5YLiLv = iPolicyService.getPolicyRate(RateEnum.五年以下公积金贷款利率.getCode());
            BigDecimal more5YLiLv = iPolicyService.getPolicyRate(RateEnum.五年以上公积金贷款利率.getCode());
            if (less5Y == null) {
                less5Y = BigDecimal.ZERO;
                less5YLiLv = new BigDecimal("0.0000");
            }
            if (more5Y == null) {
                more5Y = BigDecimal.ZERO;
                more5YLiLv = new BigDecimal("0.0000");
            }
            XJ5 = less5Y.add(more5Y);
            BigDecimal XJ5LiLv = new BigDecimal("0.0000");
            if (less5YLiLv != null && more5YLiLv != null) {
                XJ5LiLv = XJ5.compareTo(BigDecimal.ZERO) != 0 ? less5Y.multiply(less5YLiLv).add(more5Y.multiply(more5YLiLv)).divide(XJ5, 4, BigDecimal.ROUND_HALF_UP).abs() : new BigDecimal("0.0000");
            }

            if (cFinanceSubjectsBalances.size() > 0) {
                for (CFinanceSubjectsBalance cFinanceSubjectsBalance : cFinanceSubjectsBalances) {
                    if ("12101".equals(cFinanceSubjectsBalance.getKmbh())) {
                        XJ5 = cFinanceSubjectsBalance.getByye();
                        more5Y = XJ5.subtract(less5Y);
                    }
                }
            }

            GRWTDK.add(getReportDetail("其中：五年（含）以下", less5Y.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), less5YLiLv != null ? less5YLiLv.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : "0.0000"));
            GRWTDK.add(getReportDetail("五年以上", more5Y.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), more5YLiLv != null ? more5YLiLv.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : "0.0000"));
            GRWTDK.add(getReportDetail("小计", XJ5.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), XJ5LiLv.toPlainString()));
            res.setGRWTDK(GRWTDK);

            //6、国债投资
            ArrayList<HousingfundLoanReportDetail> GZTZ = new ArrayList<>();
            GZTZ.add(getReportDetail("", "0.00", "0.0000"));
            res.setGZTZ(GZTZ);

            //7、其他投资（项目贷款）
            ArrayList<HousingfundLoanReportDetail> QTTZ = new ArrayList<>();
            QTTZ.add(getReportDetail("", "0.00", "0.0000"));
            res.setQTTZ(QTTZ);

            //余额合计（3+4+5+6+7）
            ArrayList<HousingfundLoanReportDetail> YEHJ2 = new ArrayList<>();
            BigDecimal YEHJ2Balance = BigDecimal.ZERO;
            YEHJ2Balance = YEHJ2Balance.add(XJ3).add(XJ4).add(XJ5);
            YEHJ2.add(getReportDetail("", YEHJ2Balance.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "-"));
            res.setYEHJ2(YEHJ2);

            report = new CFinanceReport();
            report.setBblx(FinanceReportType.住房公积金存贷款报备表.getType());
            report.setBbqj(CXNY);
            report.setBbsj(gson.toJson(res));
            report.setName(FinanceReportType.住房公积金存贷款报备表.getName());
            DAOBuilder.instance(icFinanceReportDAO).entity(report).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
        return res;
    }

    /**
     * @param ZBMC
     * @param XuHao
     * @param ShuLiang
     * @param JLDW
     * @param DaiMa
     * @return
     * @author tanyi
     */
    private CHousingfundDepositDetail getDepositDetail(String ZBMC, String XuHao, String ShuLiang, String JLDW, String DaiMa) {
        CHousingfundDepositDetail detail = new CHousingfundDepositDetail();
        detail.setZbmc(ZBMC);
        detail.setXuhao(XuHao);
        detail.setShuliang(ShuLiang);
        detail.setJldw(JLDW);
        detail.setDaima(DaiMa);
        return detail;
    }

    private HousingfundLoanReportDetail getReportDetail(String XM, String YE, String LX) {
        HousingfundLoanReportDetail detail = new HousingfundLoanReportDetail();
        detail.setXiangMu(XM);
        detail.setLiLv(LX);
        detail.setYuE(YE);
        return detail;
    }

    /**
     * @param CKJRJG
     * @param ZHXZ
     * @param ZHLX
     * @param ZJYE
     * @param LiLv
     * @return
     */
    private CHousingfundBankDepositDetail getBankDepositDetail(String CKJRJG, String ZHXZ, String ZHLX, BigDecimal ZJYE, BigDecimal LiLv) {
        CHousingfundBankDepositDetail detail = new CHousingfundBankDepositDetail();
        detail.setCkjrjg(CKJRJG);
        detail.setLilv(LiLv);
        detail.setCklx(ZHLX);
        detail.setZhxz(ZHXZ);
        detail.setZjye(ZJYE);
        return detail;
    }

    /**
     * @param zhxz
     * @param cklx
     * @param zjye
     * @param lilv
     * @return
     */
    private CHousingfundBankDepositSum getHousingfundBankDepositSum(String zhxz, String cklx, BigDecimal zjye, BigDecimal lilv) {
        return new CHousingfundBankDepositSum(zhxz, cklx, zjye, lilv);
    }

    /**
     * @param details
     * @param daima
     * @return
     * @author tanyi
     */
    private CHousingfundDepositDetail searchCDetail(List<CHousingfundDepositDetail> details, String daima) {
        for (CHousingfundDepositDetail detail : details) {
            if (detail.getDaima().equals(daima)) {
                return detail;
            }
        }
        return null;
    }

    //------------------银行结算流水-------------------
    @Override
    public PageRes<SettlementDayBook> getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zjywlx) && !"所有".equals(zjywlx)) filter.put("zjywlx", zjywlx);
        if (StringUtil.notEmpty(yhzh)) {
            filter.put("skzhhm", yhzh);
        } else {
            if (StringUtil.notEmpty(yhmc)) {
                CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).searchFilter(new HashMap<String, Object>() {{
                    this.put("yhmc", yhmc);
                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                List<String> accounts = new ArrayList<>();
                for (StSettlementSpecialBankAccount acct : cBankContract.getStSettlementSpecialBankAccounts()) {
                    accounts.add(acct.getYhzhhm());
                }
                filter.put("skzhhm", accounts);
            }
        }

        PageRes pageRes = new PageRes();

        List<StSettlementDaybook> stSettlementDaybooks = DAOBuilder.instance(iStSettlementDaybookDAO)
                .searchFilter(filter)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        Date beginDate;
                        Date endDate;
                        if (StringUtil.notEmpty(begin)) {
                            try {
                                beginDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", begin);
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结算发生日期格式有误");
                            }
                            criteria.add(Restrictions.ge("jsfsrq", beginDate));
                            System.out.println(beginDate);
                        }
                        if (StringUtil.notEmpty(end)) {
                            try {
                                endDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", end);
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结算发生日期格式有误");
                            }
                            criteria.add(Restrictions.le("jsfsrq", endDate));
                            System.out.println(endDate);
                        }
                    }
                })
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        PageRes<SettlementDayBook> settlementDayBookPageRes = new PageRes<>();
        settlementDayBookPageRes.setCurrentPage(pageRes.getCurrentPage());
        settlementDayBookPageRes.setNextPageNo(pageRes.getNextPageNo());
        settlementDayBookPageRes.setPageCount(pageRes.getPageCount());
        settlementDayBookPageRes.setPageSize(pageRes.getPageSize());
        settlementDayBookPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<SettlementDayBook> settlementDayBooks = new ArrayList<>();

        for (StSettlementDaybook stSettlementDaybook : stSettlementDaybooks) {
            SettlementDayBook settlementDayBook = new SettlementDayBook();
            settlementDayBook.setFKYHDM(stSettlementDaybook.getFkyhdm());
            settlementDayBook.setFKZHHM(stSettlementDaybook.getFkzhhm());
            settlementDayBook.setFKZHMC(stSettlementDaybook.getFkzhmc());
            settlementDayBook.setFSE(stSettlementDaybook.getFse() != null ? stSettlementDaybook.getFse().toPlainString() : null);
            settlementDayBook.setId(stSettlementDaybook.getId());
            settlementDayBook.setJSFSRQ(DateUtil.date2Str(stSettlementDaybook.getJsfsrq(), "yyyy-MM-dd"));
            settlementDayBook.setJSYHDM(stSettlementDaybook.getJsyhdm());
            settlementDayBook.setSKYHDM(stSettlementDaybook.getSkyhdm());
            settlementDayBook.setSKZHHM(stSettlementDaybook.getSkzhhm());
            settlementDayBook.setSKZHMC(stSettlementDaybook.getSkzhmc());
            settlementDayBook.setYHJSLSH(stSettlementDaybook.getYhjslsh());
            settlementDayBook.setYWLSH(stSettlementDaybook.getYwlsh());
            settlementDayBook.setYWPZHM(stSettlementDaybook.getYwpzhm());
            settlementDayBook.setZHAIYAO(stSettlementDaybook.getZhaiYao());
            settlementDayBook.setZJYWLX(stSettlementDaybook.getZjywlx());

            settlementDayBooks.add(settlementDayBook);
        }

        settlementDayBookPageRes.setResults(settlementDayBooks);
        return settlementDayBookPageRes;
    }

    @Override
    public PageResNew<SettlementDayBook> getSettlementDayBookList(String zjywlx, String yhmc, String yhzh, String begin, String end, String marker, String action, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zjywlx) && !"所有".equals(zjywlx)) filter.put("zjywlx", zjywlx);
        if (StringUtil.notEmpty(yhzh)) {
            filter.put("skzhhm", yhzh);
        } else {
            if (StringUtil.notEmpty(yhmc)) {
                CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).searchFilter(new HashMap<String, Object>() {{
                    this.put("yhmc", yhmc);
                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                List<String> accounts = new ArrayList<>();
                for (StSettlementSpecialBankAccount acct : cBankContract.getStSettlementSpecialBankAccounts()) {
                    accounts.add(acct.getYhzhhm());
                }
                filter.put("skzhhm", accounts);
            }
        }
        List<StSettlementDaybook> stSettlementDaybooks = DAOBuilder.instance(iStSettlementDaybookDAO)
                .searchFilter(filter)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        Date beginDate;
                        Date endDate;
                        if (StringUtil.notEmpty(begin)) {
                            try {
                                beginDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", begin);
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结算发生日期格式有误");
                            }
                            criteria.add(Restrictions.ge("jsfsrq", beginDate));
                            System.out.println(beginDate);
                        }
                        if (StringUtil.notEmpty(end)) {
                            try {
                                endDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", end);
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结算发生日期格式有误");
                            }
                            criteria.add(Restrictions.le("jsfsrq", endDate));
                            System.out.println(endDate);
                        }
                    }
                })
                .pageOption(marker, action, pageSize).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        PageResNew<SettlementDayBook> settlementDayBookPageRes = new PageResNew<>();

        ArrayList<SettlementDayBook> settlementDayBooks = new ArrayList<>();

        for (StSettlementDaybook stSettlementDaybook : stSettlementDaybooks) {
            SettlementDayBook settlementDayBook = new SettlementDayBook();
            settlementDayBook.setFKYHDM(stSettlementDaybook.getFkyhdm());
            settlementDayBook.setFKZHHM(stSettlementDaybook.getFkzhhm());
            settlementDayBook.setFKZHMC(stSettlementDaybook.getFkzhmc());
            settlementDayBook.setFSE(stSettlementDaybook.getFse() != null ? stSettlementDaybook.getFse().toPlainString() : null);
            settlementDayBook.setId(stSettlementDaybook.getId());
            settlementDayBook.setJSFSRQ(DateUtil.date2Str(stSettlementDaybook.getJsfsrq(), "yyyy-MM-dd"));
            settlementDayBook.setJSYHDM(stSettlementDaybook.getJsyhdm());
            settlementDayBook.setSKYHDM(stSettlementDaybook.getSkyhdm());
            settlementDayBook.setSKZHHM(stSettlementDaybook.getSkzhhm());
            settlementDayBook.setSKZHMC(stSettlementDaybook.getSkzhmc());
            settlementDayBook.setYHJSLSH(stSettlementDaybook.getYhjslsh());
            settlementDayBook.setYWLSH(stSettlementDaybook.getYwlsh());
            settlementDayBook.setYWPZHM(stSettlementDaybook.getYwpzhm());
            settlementDayBook.setZHAIYAO(stSettlementDaybook.getZhaiYao());
            settlementDayBook.setZJYWLX(stSettlementDaybook.getZjywlx());

            settlementDayBooks.add(settlementDayBook);
        }
        settlementDayBookPageRes.setResults(action, settlementDayBooks);
        return settlementDayBookPageRes;
    }

    @Override
    public SettlementDayBook getSettlementDayBook(String id) {
        StSettlementDaybook stSettlementDaybook = DAOBuilder.instance(iStSettlementDaybookDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (stSettlementDaybook == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        SettlementDayBook settlementDayBook = new SettlementDayBook();
        settlementDayBook.setFKYHDM(stSettlementDaybook.getFkyhdm());
        settlementDayBook.setFKZHHM(stSettlementDaybook.getFkzhhm());
        settlementDayBook.setFKZHMC(stSettlementDaybook.getFkzhmc());
        settlementDayBook.setFSE(stSettlementDaybook.getFse() != null ? stSettlementDaybook.getFse().toPlainString() : null);
        settlementDayBook.setId(stSettlementDaybook.getId());
        settlementDayBook.setJSFSRQ(DateUtil.date2Str(stSettlementDaybook.getJsfsrq(), "yyyy-MM-dd"));
        settlementDayBook.setJSYHDM(stSettlementDaybook.getJsyhdm());
        settlementDayBook.setSKYHDM(stSettlementDaybook.getSkyhdm());
        settlementDayBook.setSKZHHM(stSettlementDaybook.getSkzhhm());
        settlementDayBook.setSKZHMC(stSettlementDaybook.getSkzhmc());
        settlementDayBook.setYHJSLSH(stSettlementDaybook.getYhjslsh());
        settlementDayBook.setYWLSH(stSettlementDaybook.getYwlsh());
        settlementDayBook.setYWPZHM(stSettlementDaybook.getYwpzhm());
        settlementDayBook.setZHAIYAO(stSettlementDaybook.getZhaiYao());
        settlementDayBook.setZJYWLX(stSettlementDaybook.getZjywlx());

        return settlementDayBook;
    }

    @Override
    public SettlementDayBook addSettlementDayBook(SettlementDayBook settlementDayBook) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("yhjslsh", settlementDayBook.getYHJSLSH());
        filter.put("fkzhhm", settlementDayBook.getFKZHHM());
        filter.put("skzhhm", settlementDayBook.getSKZHHM());
        StSettlementDaybook isExist = DAOBuilder.instance(iStSettlementDaybookDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                System.err.println(e.getMessage());
            }
        });

        if (isExist != null) return null;

        StSettlementDaybook stSettlementDaybook = new StSettlementDaybook();
        stSettlementDaybook.setFkyhdm(settlementDayBook.getFKYHDM());
        stSettlementDaybook.setFkzhhm(settlementDayBook.getFKZHHM());
        stSettlementDaybook.setFkzhmc(settlementDayBook.getFKZHMC());
        stSettlementDaybook.setFse(new BigDecimal(settlementDayBook.getFSE()));
        try {
            stSettlementDaybook.setJsfsrq(DateUtil.str2Date("yyyyMMdd", settlementDayBook.getJSFSRQ()));
        } catch (ParseException e) {
            stSettlementDaybook.setJsfsrq(new Date());
            System.out.println(e.getMessage());
        }
        stSettlementDaybook.setJsyhdm(settlementDayBook.getJSYHDM());
        stSettlementDaybook.setSkyhdm(settlementDayBook.getSKYHDM());
        stSettlementDaybook.setSkzhhm(settlementDayBook.getSKZHHM());
        stSettlementDaybook.setSkzhmc(settlementDayBook.getSKZHMC());
        stSettlementDaybook.setYhjslsh(settlementDayBook.getYHJSLSH());
        stSettlementDaybook.setYwlsh(settlementDayBook.getYWLSH());
        stSettlementDaybook.setYwpzhm(settlementDayBook.getYWPZHM());
        stSettlementDaybook.setZhaiYao(settlementDayBook.getZHAIYAO());
        stSettlementDaybook.setZjywlx(settlementDayBook.getZJYWLX());

        String id = DAOBuilder.instance(iStSettlementDaybookDAO).entity(stSettlementDaybook).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        settlementDayBook.setId(id);

        return settlementDayBook;
    }

    //---------------------银行结算流水---------------------
    //---------------------银行存款日记账---------------------
    @Override
    public PageRes<DepositJournal> getDepositJournal(String yhzhhm, String jzrq, String rzrq, int pageNo, int pageSize) {
        Date jzrqDate;
        Date rzrqDate;
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhzhhm)) filter.put("yhzhhm", yhzhhm);

        if (StringUtil.notEmpty(jzrq)) {
            try {
                jzrqDate = DateUtil.str2Date("yyyy-MM-dd", jzrq);
                filter.put("jzrq", jzrqDate);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "记账日期格式有误");
            }
        }
        if (StringUtil.notEmpty(rzrq)) {
            try {
                rzrqDate = DateUtil.str2Date("yyyy-MM-dd", rzrq);
                filter.put("rzrq", rzrqDate);
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "入账日期格式有误");
            }
        }

        PageRes pageRes = new PageRes();

        List<StFinanceBankDepositJournal> stFinanceBankDepositJournals = DAOBuilder.instance(iStFinanceBankDepositJournalDAO).searchFilter(filter)
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        PageRes<DepositJournal> depositJournalPageRes = new PageRes<>();
        depositJournalPageRes.setCurrentPage(pageRes.getCurrentPage());
        depositJournalPageRes.setNextPageNo(pageRes.getNextPageNo());
        depositJournalPageRes.setPageCount(pageRes.getPageCount());
        depositJournalPageRes.setPageSize(pageRes.getPageSize());
        depositJournalPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<DepositJournal> depositJournals = new ArrayList<>();
        for (StFinanceBankDepositJournal stFinanceBankDepositJournal : stFinanceBankDepositJournals) {
            DepositJournal depositJournal = new DepositJournal();
            depositJournal.setCNLSH(stFinanceBankDepositJournal.getCnlsh());
            depositJournal.setCZBS(stFinanceBankDepositJournal.getCzbs());
            depositJournal.setDFFSE(stFinanceBankDepositJournal.getDffse() != null ? stFinanceBankDepositJournal.getDffse().toPlainString() : null);
            depositJournal.setId(stFinanceBankDepositJournal.getId());
            depositJournal.setJFFSE(stFinanceBankDepositJournal.getJffse() != null ? stFinanceBankDepositJournal.getJffse().toPlainString() : null);
            depositJournal.setJZPZH(stFinanceBankDepositJournal.getJzpzh());
            depositJournal.setJZRQ(DateUtil.date2Str(stFinanceBankDepositJournal.getJzrq(), "yyyy-MM-dd"));
            depositJournal.setPZSSNY(stFinanceBankDepositJournal.getPzssny());
            depositJournal.setRZRQ(DateUtil.date2Str(stFinanceBankDepositJournal.getRzrq(), "yyyy-MM-dd"));
            depositJournal.setRZZT(stFinanceBankDepositJournal.getRzzt());
            depositJournal.setYEJDFX(stFinanceBankDepositJournal.getYejdfx());
            depositJournal.setYHJSLSH(stFinanceBankDepositJournal.getYhjslsh());
            depositJournal.setYHZHHM(stFinanceBankDepositJournal.getYhzhhm());
            depositJournal.setYuE(stFinanceBankDepositJournal.getYuE() != null ? stFinanceBankDepositJournal.getYuE().toPlainString() : null);
            depositJournal.setZHAIYAO(stFinanceBankDepositJournal.getZhaiYao());
            depositJournal.setZJYWLX(stFinanceBankDepositJournal.getZjywlx());

            depositJournals.add(depositJournal);
        }

        depositJournalPageRes.setResults(depositJournals);
        return depositJournalPageRes;
    }

    @Override
    public DepositJournal addDepositJournal(DepositJournal depositJournal) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("yhjslsh", depositJournal.getYHJSLSH());
        filter.put("yhzhhm", depositJournal.getYHZHHM());
        StFinanceBankDepositJournal isExist = DAOBuilder.instance(iStFinanceBankDepositJournalDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                System.err.println(e.getMessage());
            }
        });

        if (isExist != null) return null;

        StFinanceBankDepositJournal stFinanceBankDepositJournal = new StFinanceBankDepositJournal();
        stFinanceBankDepositJournal.setCnlsh(depositJournal.getCNLSH());
        stFinanceBankDepositJournal.setCzbs(depositJournal.getCZBS());
        stFinanceBankDepositJournal.setDffse(new BigDecimal(depositJournal.getDFFSE()));
        stFinanceBankDepositJournal.setJffse(new BigDecimal(depositJournal.getJFFSE()));
        stFinanceBankDepositJournal.setJzpzh(depositJournal.getJZPZH());
        try {
            stFinanceBankDepositJournal.setJzrq(DateUtil.str2Date("yyyyMMdd", depositJournal.getJZRQ()));
            stFinanceBankDepositJournal.setRzrq(DateUtil.str2Date("yyyyMMdd", depositJournal.getRZRQ()));
        } catch (ParseException e) {
            stFinanceBankDepositJournal.setJzrq(new Date());
            stFinanceBankDepositJournal.setRzrq(new Date());
            System.err.println(e.getMessage());
        }
        stFinanceBankDepositJournal.setPzssny(depositJournal.getPZSSNY());
        stFinanceBankDepositJournal.setRzzt(depositJournal.getRZZT());
        stFinanceBankDepositJournal.setYejdfx(depositJournal.getYEJDFX());
        stFinanceBankDepositJournal.setYhjslsh(depositJournal.getYHJSLSH());
        stFinanceBankDepositJournal.setYhzhhm(depositJournal.getYHZHHM());
        stFinanceBankDepositJournal.setYuE(new BigDecimal(depositJournal.getYuE()));
        stFinanceBankDepositJournal.setZhaiYao(depositJournal.getZHAIYAO());
        stFinanceBankDepositJournal.setZjywlx(depositJournal.getZJYWLX());

        String id = DAOBuilder.instance(iStFinanceBankDepositJournalDAO).entity(stFinanceBankDepositJournal).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        depositJournal.setId(id);

        return depositJournal;
    }

    @Override
    public void updateDepositJournal(String ywlsh, DepositJournal depositJournal) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("cnlsh", ywlsh);

        StFinanceBankDepositJournal stFinanceBankDepositJournal = DAOBuilder.instance(iStFinanceBankDepositJournalDAO).searchFilter(filter)
                .getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        if (stFinanceBankDepositJournal == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有流水为" + ywlsh + "的相关信息");
        }

        stFinanceBankDepositJournal.setCnlsh(depositJournal.getCNLSH());
        stFinanceBankDepositJournal.setCzbs(depositJournal.getCZBS());
        stFinanceBankDepositJournal.setDffse(new BigDecimal(depositJournal.getDFFSE()));
        stFinanceBankDepositJournal.setJffse(new BigDecimal(depositJournal.getJFFSE()));
        stFinanceBankDepositJournal.setJzpzh(depositJournal.getJZPZH());
//        stFinanceBankDepositJournal.setJzrq(depositJournal.getJZRQ());
        stFinanceBankDepositJournal.setPzssny(depositJournal.getPZSSNY());
        stFinanceBankDepositJournal.setRzrq(new Date());
        stFinanceBankDepositJournal.setRzzt(depositJournal.getRZZT());
        stFinanceBankDepositJournal.setYejdfx(depositJournal.getYEJDFX());
        stFinanceBankDepositJournal.setYhjslsh(depositJournal.getYHJSLSH());
        stFinanceBankDepositJournal.setYhzhhm(depositJournal.getYHZHHM());
        stFinanceBankDepositJournal.setYuE(new BigDecimal(depositJournal.getYuE()));
        stFinanceBankDepositJournal.setZhaiYao(depositJournal.getZHAIYAO());
        stFinanceBankDepositJournal.setZjywlx(depositJournal.getZJYWLX());

        DAOBuilder.instance(iStFinanceBankDepositJournalDAO).entity(stFinanceBankDepositJournal).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }

    /**
     * 获取上月会计期间中的所有科目余额信息
     *
     * @param date 当前时间
     * @return
     * @throws ParseException
     */
    private List<CFinanceSubjectsBalance> getOldPeriod(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String KJQJUP = format.format(calendar.getTime());

        CFinanceAccountPeriod cFinanceAccountPeriodold = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", KJQJUP);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<CFinanceSubjectsBalance> oldbalances;
        if (cFinanceAccountPeriodold != null) {
            if (!cFinanceAccountPeriodold.isSfjs()) {
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "上月未结账");
            }
            oldbalances = cFinanceAccountPeriodold.getcFinanceSubjectsBalances();
            if (oldbalances == null || oldbalances.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "上月未结账");
            }
        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "上月会计期间不存在");
        }
        return oldbalances;
    }

    //----------------------银行存款日记账---------------------
    //---------------------定期存款明细信息---------------------
    @Override
    public PageRes<FixedDepositDetails> getFixedDepositDetails(String yhzhhm, String ckqx, String begin, String end, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhzhhm)) filter.put("zhhm", yhzhhm);
        if (StringUtil.notEmpty(ckqx)) {
            BigDecimal deposit_period = DepositPeriodTransfer.flagToDays(ckqx);
            filter.put("ckqx", deposit_period);
        }

        Date beginDate;
        Date endDate;
        try {
            beginDate = DateUtil.str2Date("yyyy-MM-dd", begin);
            endDate = DateUtil.str2Date("yyyy-MM-dd", end);
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "存入日期格式有误");
        }

        PageRes pageRes = new PageRes();

        List<StFinanceTimeDeposit> stFinanceTimeDeposits = DAOBuilder.instance(iStFinanceTimeDepositDAO).searchFilter(filter)
                .pageOption(pageRes, pageSize, pageNo)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        if (beginDate != null) {
                            criteria.add(Restrictions.ge("crrq", beginDate));
                        }
                        if (endDate != null) {
                            criteria.add(Restrictions.le("dqrq", endDate));
                        }
                    }
                })
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        PageRes<FixedDepositDetails> fixedDepositDetailsPageRes = new PageRes<>();
        fixedDepositDetailsPageRes.setCurrentPage(pageRes.getCurrentPage());
        fixedDepositDetailsPageRes.setNextPageNo(pageRes.getNextPageNo());
        fixedDepositDetailsPageRes.setPageCount(pageRes.getPageCount());
        fixedDepositDetailsPageRes.setPageSize(pageRes.getPageSize());
        fixedDepositDetailsPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<FixedDepositDetails> fixedDepositDetailsArrayList = new ArrayList<>();

        for (StFinanceTimeDeposit stFinanceTimeDeposit : stFinanceTimeDeposits) {
            FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
            fixedDepositDetails.setBJJE(stFinanceTimeDeposit.getBjje() != null ? stFinanceTimeDeposit.getBjje().toPlainString() : null);
            fixedDepositDetails.setCKQX(stFinanceTimeDeposit.getCkqx() != null ? DepositPeriodTransfer.daysToFlag(stFinanceTimeDeposit.getCkqx()) : null);
            fixedDepositDetails.setCRRQ(DateUtil.date2Str(stFinanceTimeDeposit.getCrrq(), "yyyy-MM-dd"));
            fixedDepositDetails.setDQCKBH(stFinanceTimeDeposit.getDqckbh());
            fixedDepositDetails.setDQRQ(DateUtil.date2Str(stFinanceTimeDeposit.getDqrq(), "yyyy-MM-dd"));
            fixedDepositDetails.setId(stFinanceTimeDeposit.getId());
            fixedDepositDetails.setKHYHMC(stFinanceTimeDeposit.getKhyhmc());
            fixedDepositDetails.setLILV(stFinanceTimeDeposit.getLiLv() != null ? stFinanceTimeDeposit.getLiLv().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
            fixedDepositDetails.setLXSR(stFinanceTimeDeposit.getLxsr() != null ? stFinanceTimeDeposit.getLxsr().toPlainString() : null);
            fixedDepositDetails.setQKRQ(DateUtil.date2Str(stFinanceTimeDeposit.getQkrq(), "yyyy-MM-dd"));
            fixedDepositDetails.setZHHM(stFinanceTimeDeposit.getZhhm());
            fixedDepositDetails.setZHMC(stFinanceTimeDeposit.getZhmc());
            fixedDepositDetails.setZQQK(stFinanceTimeDeposit.getZqqk());

            fixedDepositDetailsArrayList.add(fixedDepositDetails);
        }

        fixedDepositDetailsPageRes.setResults(fixedDepositDetailsArrayList);
        return fixedDepositDetailsPageRes;
    }

    public FixedDepositDetails getSingleFixedDepositDetails(String dqckbh) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("dqckbh", dqckbh);

        StFinanceTimeDeposit stFinanceTimeDeposit = DAOBuilder.instance(iStFinanceTimeDepositDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (stFinanceTimeDeposit == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有定期存款编号为" + dqckbh + "的相关信息");
        }

        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("dqckbh", dqckbh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
        fixedDepositDetails.setBJJE(stFinanceTimeDeposit.getBjje() != null ? stFinanceTimeDeposit.getBjje().toPlainString() : null);
        fixedDepositDetails.setCKQX(stFinanceTimeDeposit.getCkqx() != null ? DepositPeriodTransfer.daysToFlag(stFinanceTimeDeposit.getCkqx()) : null);
        fixedDepositDetails.setCRRQ(DateUtil.date2Str(stFinanceTimeDeposit.getCrrq(), "yyyy-MM-dd"));
        fixedDepositDetails.setDQCKBH(stFinanceTimeDeposit.getDqckbh());
        fixedDepositDetails.setDQRQ(DateUtil.date2Str(stFinanceTimeDeposit.getDqrq(), "yyyy-MM-dd"));
        fixedDepositDetails.setId(stFinanceTimeDeposit.getId());
        fixedDepositDetails.setKHYHMC(stFinanceTimeDeposit.getKhyhmc());
        fixedDepositDetails.setLILV(stFinanceTimeDeposit.getLiLv() != null ? stFinanceTimeDeposit.getLiLv().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
        fixedDepositDetails.setLXSR(stFinanceTimeDeposit.getLxsr() != null ? stFinanceTimeDeposit.getLxsr().toPlainString() : null);
        fixedDepositDetails.setQKRQ(DateUtil.date2Str(stFinanceTimeDeposit.getQkrq(), "yyyy-MM-dd"));
        fixedDepositDetails.setZHHM(stFinanceTimeDeposit.getZhhm());
        fixedDepositDetails.setZHMC(stFinanceTimeDeposit.getZhmc());
        fixedDepositDetails.setZQQK(stFinanceTimeDeposit.getZqqk());
        if (cFinanceFixedDraw != null) {
            fixedDepositDetails.setCZY(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy());
            fixedDepositDetails.setSLRQ(DateUtil.date2Str(cFinanceFixedDraw.getCreated_at(), "yyyy-MM-dd"));
        }

        return fixedDepositDetails;
    }

    //---------------------定期存款明细信息---------------------
    //---------------------全市归集贷款统计表---------------------
    @Override
    public ReportModel getCityLoanCollection(String type, String year) {

        ReportModel reportModel = new ReportModel();

        HashMap<String, HashMap<String, BigDecimal>> results = new HashMap<>();

        List<Map> result = new ArrayList<>();

        if ("GJ".equals(type)) {
            result = stCollectionUnitBusinessDetailsDAO.getCityCollection(Integer.parseInt(year));
        } else if ("TQ".equals(type)) {
            result = stCollectionUnitBusinessDetailsDAO.getCityWithdrawl(Integer.parseInt(year));
        } else if ("FK".equals(type)) {
            result = stCollectionUnitBusinessDetailsDAO.getCityLoan(Integer.parseInt(year));
        } else if ("HK".equals(type)) {
            result = stCollectionUnitBusinessDetailsDAO.getCityRepament(Integer.parseInt(year));
        }

        List<String> ywwds = stCollectionUnitBusinessDetailsDAO.getYWWDs();

        for (String month : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")) {
            HashMap<String, BigDecimal> areas = new HashMap<>();
            for (String area : ywwds) {
                areas.put(area, new BigDecimal(BigInteger.ZERO));
            }
            areas.put("sum", new BigDecimal(BigInteger.ZERO));
            results.put(month, areas);
        }

        for (Map colct : result) {
            String area = (String) colct.get("area");
            String month = String.valueOf(colct.get("month"));
            BigDecimal fse = ((BigDecimal) colct.get("fse")).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP); //以“万元”为单位

            if (!results.containsKey(month)) {
                HashMap<String, BigDecimal> areas = new HashMap<>();
                results.put(month, areas);
            }
            results.get(month).put(area, fse);
            results.get(month).put("sum", results.get(month).getOrDefault("sum", BigDecimal.ZERO).add((BigDecimal) colct.get("fse")));
        }

        final BigDecimal[] total = {BigDecimal.ZERO};
        results.forEach((k,v) -> { v.forEach((k2,v2) -> {
            if ("sum".equals(k2)) {
                total[0] = total[0].add(v2);
                results.get(k).put("sum", v2.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP));
            }
                });}
        );

        reportModel.setTotal(total[0].divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP));
        reportModel.setResults(results);

        return reportModel;
    }

    //---------------------全市归集贷款统计表---------------------

    //-----------------获取全市各区暂收未分摊总额-----------------
    public ReportModel getCityZSWFT(TokenContext tokenContext, String CXSJ) {

        ReportModel reportModel = new ReportModel();

        if (Integer.parseInt(DateUtil.date2Str(new Date(), "yyyy")) < Integer.parseInt(CXSJ)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "因时间未到，未生成此报表");
        }
        List<CFinanceReport> reportList = DAOBuilder.instance(icFinanceReportDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bblx", FinanceReportType.住房公积金全市暂收未分摊统计表.getType());
        }}).orderOption("bbqj", Order.ASC).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.like("bbqj", CXSJ + "%"));
            }
        }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        HashMap<String, HashMap<String, BigDecimal>> hashMapWFTs = new HashMap<>();

        List<String> khwds = stCollectionUnitBusinessDetailsDAO.getYWWDs();
        HashMap<String, BigDecimal> ywwdMap = getYWWDMap(khwds);
        //1.初始化返回结构及缺省值
        for (int j = 1; j < 13; j++) {
            hashMapWFTs.put(String.valueOf(j), ywwdMap);       //组装数据
        }
        //2.获取当月前未分摊
        for (CFinanceReport cFinanceReport : reportList) {
            HashMap<String, BigDecimal> hashMap = new HashMap<>();
            hashMap = gson.fromJson(cFinanceReport.getBbsj(), HashMap.class);
            String bbqj = cFinanceReport.getBbqj().substring(4);
            hashMapWFTs.put(bbqj.startsWith("0") ? bbqj.substring(1) : bbqj, hashMap);
        }
        //3.查询当月未分摊
        if (Integer.parseInt(DateUtil.date2Str(new Date(), "yyyy")) == Integer.parseInt(CXSJ)) {
            List<Map> listMap = stCollectionUnitBusinessDetailsDAO.getCityZSWFT();
            HashMap<String, BigDecimal> hashMap = new HashMap<>();
            for (Map map : listMap) {
                String khwd = (String) map.get("khwd");//开户网点
                BigDecimal zsye = ((BigDecimal) map.get("zsye")).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);//暂收余额
                String kwwdName = icAccountNetworkDAO.get(khwd).getMingCheng();
                khwds.add(kwwdName);
                hashMap.put(kwwdName, zsye);
            }
            String Month = DateUtil.date2Str(new Date(), "MM");
            hashMapWFTs.put(Month.startsWith("0") ? Month.substring(1) : Month, hashMap);
        }
        reportModel.setResults(hashMapWFTs);
        return reportModel;
    }
    //-----------------获取全市各区暂收未分摊总额-----------------

    private HashMap<String, BigDecimal> getYWWDMap(List<String> khwds) {
        HashMap<String, BigDecimal> hashMapOther = new HashMap<>();
        for (String khwd : khwds) {
            hashMapOther.put(khwd, BigDecimal.ZERO);
        }
        return hashMapOther;
    }

    //-----------------获取全市各区暂收未分摊总额定时任务----------------- 触发时间 每月月末 23:59
    public void saveZSWFTTimeTask() {
        HashMap<String, BigDecimal> hashMap = new HashMap<>();
        List<Map> listMap = stCollectionUnitBusinessDetailsDAO.getCityZSWFT();
        for (Map map : listMap) {
            String khwd = (String) map.get("khwd");//开户网点
            BigDecimal zsye = ((BigDecimal) map.get("zsye")).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);//暂收余额
            hashMap.put(icAccountNetworkDAO.get(khwd).getMingCheng(), zsye);
        }
        CFinanceReport cFinanceReport = new CFinanceReport();
        cFinanceReport.setBblx(FinanceReportType.住房公积金全市暂收未分摊统计表.getType());//报表类型
        cFinanceReport.setBbqj(DateUtil.date2Str(new Date(), "yyyyMM"));//报表期间
        cFinanceReport.setBbsj(gson.toJson(hashMap));//报表数据
        cFinanceReport.setName(FinanceReportType.住房公积金全市暂收未分摊统计表.getName());//报表名字

        DAOBuilder.instance(icFinanceReportDAO).entity(cFinanceReport).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }
    //-----------------获取全市各区暂收未分摊总额定时任务-----------------

    @Override
    public HousingfundDepositWithdrawlClassifyReport getHousingfundDepositWithdrawlClassifyResport(TokenContext tokenContext, String CXNY) {
//        addhousingfundDepositResport
        if (!StringUtil.notEmpty(CXNY)) {
            CXNY = DateUtil.date2Str(new Date(), "yyyy-MM");
        }
        String KJQJ = null;
        try {
            KJQJ = DateUtil.date2Str(DateUtil.str2Date("yyyy-MM", CXNY), "yyyyMM");
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }

        String finalKJQJ = KJQJ;
        CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", finalKJQJ);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cFinanceAccountPeriod == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间不存在");
        }
        Date ksdate = cFinanceAccountPeriod.getQsrq();
        Date jsdate = new Date(cFinanceAccountPeriod.getJiezrq().getTime()+ 86399 * 1000);

        //region 代码映射
        HashMap<String, String> map = new HashMap<>();
        map.put("01", "211");
        map.put("02", "211");
        map.put("06", "212");
        map.put("07", "213");
        map.put("", "214");
        map.put("03", "221");
        map.put("04", "222");
        map.put("05", "223");
        map.put("08", "224");
        map.put("09", "225");
        map.put("99", "225");
        //endregion
        ArrayList<HousingfundDepositWithdrawlClassifyReportDetail> reportDetails = new ArrayList<HousingfundDepositWithdrawlClassifyReportDetail>();
        //缴存
        HousingfundDepositWithdrawlClassifyReportDetail reportDetail1 = new HousingfundDepositWithdrawlClassifyReportDetail();
        reportDetail1.setZBMC("一、缴存");
        reportDetail1.setDaiMa("—");
        reportDetail1.setShuLiang("—");
        reportDetail1.setJinE("—");
        reportDetails.add(reportDetail1);

        reportDetails.add(getDepositReportDetails(CXNY, "合计", "100", "00"));   //合计 100
        reportDetails.add(getDepositReportDetails(CXNY, "国家机关、事业单位", "101", "1"));   //国家机关 101
        reportDetails.add(getDepositReportDetails(CXNY, "国有企业", "102", "2"));   //国有企业 102
        reportDetails.add(getDepositReportDetails(CXNY, "城镇集体企业", "103", "3"));   //城镇集体企业 103
        reportDetails.add(getDepositReportDetails(CXNY, "外商投资企业", "104", ""));   //外商投资企业 104
        reportDetails.add(getDepositReportDetails(CXNY, "城镇私营企业及其他城镇企业", "105", ""));   //城镇私营企业及其他城镇企业 105
        reportDetails.add(getDepositReportDetails(CXNY, "民办非企业单位、社会团体", "106", ""));   //民办非企业单位、社会团体 106
        reportDetails.add(getDepositReportDetails(CXNY, "其他", "107", ""));   //其他 107

        //
        HousingfundDepositWithdrawlClassifyReportDetail housingfundDepositWithdrawlClassifyReportDetail1 = new HousingfundDepositWithdrawlClassifyReportDetail();
        housingfundDepositWithdrawlClassifyReportDetail1.setZBMC("二、提取");
        housingfundDepositWithdrawlClassifyReportDetail1.setDaiMa("—");
        housingfundDepositWithdrawlClassifyReportDetail1.setShuLiang("—");
        housingfundDepositWithdrawlClassifyReportDetail1.setJinE("—");
        reportDetails.add(housingfundDepositWithdrawlClassifyReportDetail1);


        ArrayList<WithdrawlReportResult> withdrawlReportResult = stCollectionPersonalBusinessDetailsDAO.getWithdrawlReport(ksdate, jsdate);

        HashMap<String, HousingfundDepositWithdrawlClassifyReportDetail> map2 = new HashMap<>();
        HousingfundDepositWithdrawlClassifyReportDetail detail1 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail1.setZBMC("购买、建造、翻修、大修自住住房");
        detail1.setDaiMa("211");
        detail1.setShuLiang("0");
        detail1.setJinE("0");
        reportDetails.add(detail1);
        map2.put("211", detail1);

        HousingfundDepositWithdrawlClassifyReportDetail detail2 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail2.setZBMC("偿还购房贷款本息");
        detail2.setDaiMa("212");
        detail2.setShuLiang("0");
        detail2.setJinE("0");
        reportDetails.add(detail2);
        map2.put("212", detail2);

        HousingfundDepositWithdrawlClassifyReportDetail detail3 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail3.setZBMC("租赁住房");
        detail3.setDaiMa("213");
        detail3.setShuLiang("0");
        detail3.setJinE("0");
        reportDetails.add(detail3);
        map2.put("213", detail3);

        HousingfundDepositWithdrawlClassifyReportDetail detail4 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail4.setZBMC("离休、退休");
        detail4.setDaiMa("221");
        detail4.setShuLiang("0");
        detail4.setJinE("0");
        reportDetails.add(detail4);
        map2.put("221", detail4);

        HousingfundDepositWithdrawlClassifyReportDetail detail5 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail5.setZBMC("完全丧失劳动能力，并与单位终止劳动关系");
        detail5.setDaiMa("222");
        detail5.setShuLiang("0");
        detail5.setJinE("0");
        reportDetails.add(detail5);
        map2.put("222", detail5);

        HousingfundDepositWithdrawlClassifyReportDetail detail6 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail6.setZBMC("出境定居");
        detail6.setDaiMa("223");
        detail6.setShuLiang("0");
        detail6.setJinE("0");
        reportDetails.add(detail6);
        map2.put("223", detail6);

        HousingfundDepositWithdrawlClassifyReportDetail detail7 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail7.setZBMC("死亡或宣告死亡");
        detail7.setDaiMa("224");
        detail7.setShuLiang("0");
        detail7.setJinE("0");
        reportDetails.add(detail7);
        map2.put("224", detail7);

        HousingfundDepositWithdrawlClassifyReportDetail detail8 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail8.setZBMC("其他非住房消费");
        detail8.setDaiMa("225");
        detail8.setShuLiang("0");
        detail8.setJinE("0");
        reportDetails.add(detail8);
        map2.put("225", detail8);

        HousingfundDepositWithdrawlClassifyReportDetail detail9 = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail9.setZBMC("其他住房消费");
        detail9.setDaiMa("214");
        detail9.setShuLiang("0");
        detail9.setJinE("0");
        reportDetails.add(detail9);
        map2.put("214", detail9);

        String JinE1 = "";
        for (WithdrawlReportResult withdrawlReportResult1 : withdrawlReportResult) {
            HousingfundDepositWithdrawlClassifyReportDetail housingfundDepositWithdrawlClassifyReportDetail2 = map2.get(map.get(withdrawlReportResult1.getCode()));
//            housingfundDepositWithdrawlClassifyReportDetail2.setZBMC(reasonMap.get(withdrawlReportResult1.getCode()));
//            housingfundDepositWithdrawlClassifyReportDetail2.setDaiMa(map.get(withdrawlReportResult1.getCode()));
            housingfundDepositWithdrawlClassifyReportDetail2.setShuLiang(withdrawlReportResult1.getShuLiang());
            housingfundDepositWithdrawlClassifyReportDetail2.setJinE(new BigDecimal(housingfundDepositWithdrawlClassifyReportDetail2.getJinE())
                    .add(new BigDecimal(withdrawlReportResult1.getJinE()).divide(new BigDecimal(10000))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }

        HousingfundDepositWithdrawlClassifyReport report = new HousingfundDepositWithdrawlClassifyReport();
        report.setSheng("省");
        report.setDi("地");
        report.setXZHQDM("520500");
        report.setJGMC("毕节市住房公积金管理中心");
        report.setBiaoHao("建金 3-3表");
        report.setZDJG("住房和城乡建设部");
        report.setPZJG("国家统计局");
        report.setYXQZ("2016年8月");
        report.setJLDW("人、万元");
        report.setList(reportDetails);
        return report;
    }

    private HousingfundDepositWithdrawlClassifyReportDetail getDepositReportDetails(String CXNY, String dwlbmc, String code, String dwlb) {

        String[] obj = stCollectionUnitBusinessDetailsDAO.getUnitClassificationInformation(CXNY, code);

        HousingfundDepositWithdrawlClassifyReportDetail detail = new HousingfundDepositWithdrawlClassifyReportDetail();
        detail.setZBMC(dwlbmc);
        detail.setDaiMa(code);
        detail.setShuLiang(obj[0]);
        String je = obj[1] == null ? "0" : obj[1];
        detail.setJinE(String.valueOf(new BigDecimal(je).divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP)));
        return detail;

    }
}
