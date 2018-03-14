package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.bank.bean.center.BDC122Summary;
import com.handge.housingfund.common.service.bank.bean.center.FixedAccBalanceQueryOut;
import com.handge.housingfund.common.service.finance.*;
import com.handge.housingfund.common.service.finance.model.TimedTaskInfo;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.FinanceComputeHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuefei_wang on 17-9-28.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda"})
@Component
public class TimedFinanceTask implements ITimedFinanceTask {
    @Autowired
    IAccountBookService iAccountBookService;
    @Autowired
    IFinanceReportService iFinanceReportService;
    @Autowired
    IStSettlementSpecialBankAccountDAO iStSettlementSpecialBankAccountDAO;
    @Autowired
    private ICFinanceAccountPeriodDAO icFinanceAccountPeriodDAO;
    @Autowired
    IFinanceTrader iFinanceTrader;
    @Autowired
    private IVoucherManagerService iVoucherManagerService;

    @Autowired
    private IStCommonPersonDAO commonPersonDAO;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    private IStFinanceSubjectsDAO stFinanceSubjectsDAO;

    @Autowired
    private IStFinanceRecordingVoucherDAO financeRecordingVoucherDAO;

    @Override
    public void checkVoucher() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String KJQJ = format.format(calendar.getTime());
        iVoucherManagerService.checkoutVoucher("自动", KJQJ);
    }

    /**
     * 每月月初第一天任何时候　结算上月职工公积金利息，并生成结算凭证
     *
     * @return {@link TimedTaskInfo}
     */
    @Override
    public void checkoutMonthInterest() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        // 月末
        Date end = DateUtils.addDays(new Date(year - 1900, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)), 1);

        // 月首
        Date start = new Date(year - 1900, month, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        BigDecimal sumMonthInterest = BigDecimal.ZERO;

        BigDecimal zhye = this.collectionPersonalBusinessDetailsDAO.getSumZHYE();

        BigDecimal delta = this.collectionPersonalBusinessDetailsDAO.getTotalBusinessFSEBetweenDate(start, new Date());


        BigDecimal totoalFse = zhye.subtract(delta);
        System.err.println("当前账户余额为:" + zhye);
        System.err.println("开始时间:" + start + "到当前时间:" + new Date() + "的发生额为" + delta);
        System.err.println("开始时的账户余额为:" + totoalFse);

        for (Date date = start; date.getTime() < end.getTime(); date = DateUtils.addDays(date, 1)) {

            BigDecimal FSE = this.collectionPersonalBusinessDetailsDAO.getTotalBusinessFSE(date);

            totoalFse = totoalFse.add(FSE == null ? BigDecimal.ZERO : FSE);

            sumMonthInterest = sumMonthInterest.add(totoalFse.multiply(new BigDecimal("0.00004167")));

        }

        System.err.println("最终利息为:" + sumMonthInterest);

        VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                "", "", VoucherBusinessType.计提公积金利息.getCode(), VoucherBusinessType.计提公积金利息.getCode(),
                "", "计提公积金利息（月末）", sumMonthInterest, null, null);
        if (voucherRes.getJZPZH() == null) {
            throw new ErrorException(voucherRes.getMSG());
        }
        System.out.println(voucherRes.getJZPZH());
    }


    /**
     * 每年6月30日　23：59　触发
     * 日常财务 :结算截止到6月30日，金额为中心所有缴存职工截止到6月30日时的金额总和．生成结算凭证
     *
     * @param sumYearInterest 年度结算利息和
     * @return {@link TimedTaskInfo}
     * @author xuefei_wang
     */
    @Override
    public TimedTaskInfo checkoutYearInterest(BigDecimal sumYearInterest) {

        VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                "", "", VoucherBusinessType.结算公积金利息.getCode(), VoucherBusinessType.结算公积金利息.getCode(),
                "", "结算公积金利息", sumYearInterest, null, null);

        if (voucherRes.getJZPZH() == null) {
            throw new ErrorException(voucherRes.getMSG());
        }
        return new TimedTaskInfo(voucherRes.getJZPZH());
    }

    /**
     * 每年1月1日　05：00　触发
     * 日常财务 :　计算“增值收益”科目的余额．生成结算凭证
     *
     * @return {@link TimedTaskInfo}
     * @author xuefei_wang
     */
    @Override
    public TimedTaskInfo checkoutBenefits() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        String kjqj = String.format("%s%s", calendar.get(Calendar.YEAR), "12");

        //获取上月所有科目余额信息
        List<CFinanceSubjectsBalance> balances = getBalance(kjqj);
        balances = computBalance(balances);
        CFinanceSubjectsBalance YWSR = FinanceComputeHelper.searchSubjectsBalance(balances, "311");//增值收益科目余额信息
        if (YWSR == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "科目余额信息不存在");
        }

        VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                "", "", VoucherBusinessType.年度结转增值收益.getCode(), VoucherBusinessType.年度结转增值收益.getCode(),
                "", "年度结转增值收益", YWSR.getByye(), null, null);

        if (voucherRes.getJZPZH() == null) {
            throw new ErrorException(voucherRes.getMSG());
        }
        return new TimedTaskInfo(voucherRes.getJZPZH());
    }


    @Override
    public void setBusinessEndIncomeAndExpenditure() {
        //region 查询上月会计期间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String KJQJ = format.format(calendar.getTime());

        List<CFinanceSubjectsBalance> balances = getBalance(KJQJ);

        List<VoucherAmount> SRJFSJ = new ArrayList<>();
        List<VoucherAmount> SRDFSJ = new ArrayList<>();

        List<CFinanceSubjectsBalance> srbalances = FinanceComputeHelper.searchSubjectsBalanceLike(balances, "401");
        BigDecimal srtotal = BigDecimal.ZERO;
        for (CFinanceSubjectsBalance balance : srbalances) {
            if (balance.getByzj().compareTo(BigDecimal.ZERO) > 0) {
                SRJFSJ.add(new VoucherAmount() {{
                    this.setZhaiYao("期末业务收入结转（月末）");
                    this.setJinE(balance.getByzj());
                    this.setRemark(balance.getKmmc());
                }});
                srtotal = srtotal.add(balance.getByzj());
            }
        }
        BigDecimal finalSrtotal = srtotal;
        if (SRJFSJ.size() <= 0) {
            SRJFSJ.add(new VoucherAmount() {{
                this.setZhaiYao("期末业务收入结转（月末）");
                this.setJinE(finalSrtotal);
                this.setRemark("业务收入");
            }});
        }
        SRDFSJ.add(new VoucherAmount() {{
            this.setZhaiYao("期末业务收入结转（月末）");
            this.setJinE(finalSrtotal);
            this.setRemark("增值收益");
        }});
        VoucherRes voucherRes1 = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                "", "", VoucherBusinessType.期末结转业务收入.getCode(), VoucherBusinessType.期末结转业务收入.getCode(),
                "", SRJFSJ, SRDFSJ, "1", "", "");


        List<VoucherAmount> ZCJFSJ = new ArrayList<>();
        List<VoucherAmount> ZCDFSJ = new ArrayList<>();

        List<CFinanceSubjectsBalance> zcbalances = FinanceComputeHelper.searchSubjectsBalanceLike(balances, "411");
        BigDecimal zctotal = BigDecimal.ZERO;
        for (CFinanceSubjectsBalance balance : zcbalances) {
            if (balance.getByzj().compareTo(BigDecimal.ZERO) > 0) {
                ZCDFSJ.add(new VoucherAmount() {{
                    this.setZhaiYao("期末业务支出结转（月末）");
                    this.setJinE(balance.getByzj());
                    this.setRemark(balance.getKmmc());
                }});
                zctotal = zctotal.add(balance.getByzj());
            }
        }
        BigDecimal finalZctotal = zctotal;
        if (ZCDFSJ.size() <= 0) {
            ZCDFSJ.add(new VoucherAmount() {{
                this.setZhaiYao("期末业务支出结转（月末）");
                this.setJinE(finalZctotal);
                this.setRemark("业务支出");
            }});
        }
        ZCJFSJ.add(new VoucherAmount() {{
            this.setZhaiYao("期末业务支出结转（月末）");
            this.setJinE(finalZctotal);
            this.setRemark("增值收益");
        }});

        VoucherRes voucherRes2 = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动",
                "", "", VoucherBusinessType.期末结转业务支出.getCode(), VoucherBusinessType.期末结转业务支出.getCode(),
                "", ZCJFSJ, ZCDFSJ, "1", "", "");

        //endregion
        if (voucherRes1.getJZPZH() == null) {
            if (voucherRes2.getJZPZH() == null) {
                throw new ErrorException(voucherRes1.getMSG() + "|" + voucherRes2.getMSG());
            }
            throw new ErrorException(voucherRes1.getMSG());
        }
        if (voucherRes2.getJZPZH() == null) {
            throw new ErrorException(voucherRes2.getMSG());
        }
    }

    /**
     * 新增会计期间
     * 每年1月1日,00：00点触发
     */
    @Override
    public void addAccountPeriod() {
        iAccountBookService.addAccountPeriod();
    }

    /**
     * 新增住房公积金银行存款
     * 每月1日,00：00点触发
     */
    @Override
    public void addHousingfundBankBalance() {
        String CXNY = null;
        try {
            CXNY = DateUtil.date2Str(DateUtil.str2Date("yyyyMM", DateUtil.getPreviousMonth()), "yyyy-MM");
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        iFinanceReportService.checkHousingfundBankBalance(null, CXNY);
    }

    /**
     * 更新定期余额
     * 每天01:00触发
     */
    @Override
    public void updateFixedBalance() {
        List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccountList = DAOBuilder.instance(iStSettlementSpecialBankAccountDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        for (StSettlementSpecialBankAccount specialBankAccount : stSettlementSpecialBankAccountList) {
            iFinanceTrader.getFixedBalance("admin", specialBankAccount.getYhzhhm(), "", specialBankAccount.getYhmc(), true);
        }
    }

    /**
     * 计提定期利息收入
     * 每月1日,00：00点触发
     */
    @Override
    public void fixedIntIncome() {
        List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccountList = DAOBuilder.instance(iStSettlementSpecialBankAccountDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //公积金存款定期利息收入
        BigDecimal ckzhFixedIntIncome = new BigDecimal("0.00");
        //增值收益定期利息收入
        BigDecimal syzhFixedIntIncome = new BigDecimal("0.00");

        FixedAccBalanceQueryOut fixedAccBalanceQueryOut = null;
        for (StSettlementSpecialBankAccount specialBankAccount : stSettlementSpecialBankAccountList) {
            if ("01".equals(specialBankAccount.getZhxz())) {
                fixedAccBalanceQueryOut = iFinanceTrader.getFixedBalance("admin", specialBankAccount.getYhzhhm(), "", specialBankAccount.getYhmc(), false);
                if (fixedAccBalanceQueryOut != null && "0".equals(fixedAccBalanceQueryOut.getCenterHeadOut().getTxStatus())) {
                    for (BDC122Summary bdc122Summary : fixedAccBalanceQueryOut.getSUMMARY()) {
                        if (!"1".equals(bdc122Summary.getAcctStatus())) {
                            ckzhFixedIntIncome = ckzhFixedIntIncome.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()).divide(new BigDecimal(360), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(30)));
                        }
                    }
                }
            }
            if ("03".equals(specialBankAccount.getZhxz())) {
                fixedAccBalanceQueryOut = iFinanceTrader.getFixedBalance("admin", specialBankAccount.getYhzhhm(), "", specialBankAccount.getYhmc(), false);
                if (fixedAccBalanceQueryOut != null && "0".equals(fixedAccBalanceQueryOut.getCenterHeadOut().getTxStatus())) {
                    for (BDC122Summary bdc122Summary : fixedAccBalanceQueryOut.getSUMMARY()) {
                        if (!"1".equals(bdc122Summary.getAcctStatus())) {
                            ckzhFixedIntIncome = ckzhFixedIntIncome.add(bdc122Summary.getDrawAmt().multiply(bdc122Summary.getInterestRate()).divide(new BigDecimal(360), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(30)));
                        }
                    }
                }
            }
        }

        VoucherRes voucherRes1 = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "", "",
                VoucherBusinessType.计提公积金存款定期利息收入.getCode(), VoucherBusinessType.计提公积金存款定期利息收入.getCode(), "",
                "计提公积金存款定期利息收入", ckzhFixedIntIncome.setScale(2, BigDecimal.ROUND_HALF_UP), null, null);

        VoucherRes voucherRes2 = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "", "",
                VoucherBusinessType.计提定期增值收益利息收入.getCode(), VoucherBusinessType.计提定期增值收益利息收入.getCode(), "",
                "计提定期增值收益利息收入", syzhFixedIntIncome.setScale(2, BigDecimal.ROUND_HALF_UP), null, null);
        if (voucherRes1.getJZPZH() == null) {
            if (voucherRes2.getJZPZH() == null) {
                throw new ErrorException(voucherRes1.getMSG() + "、" + voucherRes2.getMSG());
            }
            throw new ErrorException(voucherRes1.getMSG());
        }
        if (voucherRes2.getJZPZH() == null) {
            throw new ErrorException(voucherRes2.getMSG());
        }
    }

    /**
     * 通过会计期间查询当月科目之和
     *
     * @param KJQJ
     * @return
     */
    private List<CFinanceSubjectsBalance> getBalance(String KJQJ) {
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
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有会计期间");
        }
        if (cFinanceAccountPeriod.isSfjs()) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "会计期间已经结账");
        }
        //endregion
        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<String> kmbhlist = new ArrayList<>();
        for (StFinanceSubjects s : stFinanceSubjects) {
            kmbhlist.add(s.getKmbh());
        }
        Collections.sort(kmbhlist);
        List<CFinanceSubjectsBalance> cFinanceSubjectsBalances = new ArrayList<>();
        List<CFinanceSubjectsBalance> oldbalances = null;
        try {
            oldbalances = getOldPeriod(KJQJ, "yyyyMM");
        } catch (ParseException e) {
            throw new ErrorException(e);
        }

        Date start = cFinanceAccountPeriod.getQsrq();
        Date end = new Date(cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000);//精确到到最后一天最后一秒

        List<StFinanceRecordingVoucher> vouchers = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(start, end).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (String s : kmbhlist) {
            CFinanceSubjectsBalance cFinanceSubjectsBalance = new CFinanceSubjectsBalance();
            CFinanceSubjectsBalance balance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, s);
            if (balance == null) {
                cFinanceSubjectsBalance.setSyye(new BigDecimal("0.00"));
            } else {
                cFinanceSubjectsBalance.setSyye(balance.getByye());
            }

            StFinanceSubjects subject = FinanceComputeHelper.searchSubjects(stFinanceSubjects, s);

            if (subject == null) {
                continue;
            }

            List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = FinanceComputeHelper.searchRecordingVoucher(vouchers, s);

            BigDecimal JFJE = new BigDecimal("0.00");
            BigDecimal DFJE = new BigDecimal("0.00");

            for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                if ("02".equals(subject.getKmyefx())) {
                    JFJE = JFJE.add(stFinanceRecordingVoucher.getJffse());
                    DFJE = DFJE.add(stFinanceRecordingVoucher.getDffse());
                } else {
                    JFJE = JFJE.add(stFinanceRecordingVoucher.getDffse());
                    DFJE = DFJE.add(stFinanceRecordingVoucher.getJffse());
                }
            }
            JFJE = JFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
            DFJE = DFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
            cFinanceSubjectsBalance.setByzj(JFJE);
            cFinanceSubjectsBalance.setByjs(DFJE);

//            BigDecimal SYYE = cFinanceSubjectsBalance.getSyye();
//            BigDecimal BYYE = SYYE.add(JFJE).subtract(DFJE);
//
//            BYYE = BYYE.setScale(2, BigDecimal.ROUND_HALF_UP);
//            cFinanceSubjectsBalance.setByye(BYYE);

            cFinanceSubjectsBalance.setKmbh(subject.getKmbh());
            cFinanceSubjectsBalance.setKmmc(subject.getKmmc());
            cFinanceSubjectsBalance.setKmjb(subject.getKmjb());
            cFinanceSubjectsBalance.setKmyefx(subject.getKmyefx());
            cFinanceSubjectsBalances.add(cFinanceSubjectsBalance);
        }
        return cFinanceSubjectsBalances;
    }

    /**
     * 获取上月会计期间中的所有科目余额信息
     *
     * @param KJQJ   当前时间
     * @param Format 时间格式
     * @return
     * @throws ParseException
     */
    private List<CFinanceSubjectsBalance> getOldPeriod(String KJQJ, String Format) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.str2Date(Format, KJQJ));
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

    /**
     * 计算余额信息
     *
     * @param subjectsBalanceList
     * @return
     */
    private List<CFinanceSubjectsBalance> computBalance(List<CFinanceSubjectsBalance> subjectsBalanceList) {
        List<CFinanceSubjectsBalance> res = new ArrayList<>();
        for (CFinanceSubjectsBalance c : subjectsBalanceList) {
            switch (c.getKmjb().intValue()) {
                case 1:
                    List<CFinanceSubjectsBalance> balances2 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 2);
                    List<CFinanceSubjectsBalance> balances3 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 3);
                    BigDecimal SYYE = c.getSyye();
                    BigDecimal BYZJ = c.getByzj();
                    BigDecimal BYJS = c.getByjs();
                    for (CFinanceSubjectsBalance balance : balances2) {
                        BYZJ = BYZJ.add(balance.getByzj());
                        BYJS = BYJS.add(balance.getByjs());
                    }
                    for (CFinanceSubjectsBalance b : balances3) {
                        BYZJ = BYZJ.add(b.getByzj());
                        BYJS = BYJS.add(b.getByjs());
                    }
                    BigDecimal BYYE = SYYE.add(BYZJ).subtract(BYJS);
                    SYYE = SYYE.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYZJ = BYZJ.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYJS = BYJS.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYYE = BYYE.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setSyye(SYYE);
                    c.setByzj(BYZJ);
                    c.setByjs(BYJS);
                    c.setByye(BYYE);
                    res.add(c);
                    break;
                case 2:
                    List<CFinanceSubjectsBalance> balances23 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 3);
                    BigDecimal SYYE2 = c.getSyye();
                    BigDecimal BYZJ2 = c.getByzj();
                    BigDecimal BYJS2 = c.getByjs();
                    for (CFinanceSubjectsBalance b : balances23) {
                        BYZJ2 = BYZJ2.add(b.getByzj());
                        BYJS2 = BYJS2.add(b.getByjs());
                    }
                    BigDecimal BYYE2 = SYYE2.add(BYZJ2).subtract(BYJS2);

                    SYYE2 = SYYE2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYZJ2 = BYZJ2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYJS2 = BYJS2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYYE2 = BYYE2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setSyye(SYYE2);
                    c.setByzj(BYZJ2);
                    c.setByjs(BYJS2);
                    c.setByye(BYYE2);
                    res.add(c);
                    break;
                case 3:
                    BigDecimal SYYE3 = c.getSyye();
                    BigDecimal BYZJ3 = c.getByzj();
                    BigDecimal BYJS3 = c.getByjs();

                    BigDecimal BYYE3 = SYYE3.add(BYZJ3).subtract(BYJS3);

                    BYYE3 = BYYE3.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setByye(BYYE3);
                    res.add(c);
                    break;
                default:
                    break;
            }
        }
        return res;
    }


}
