package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.loan.model.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Funnyboy on 2017/8/7.
 */
public final class CommLoanAlgorithm {

    private final static SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat ny = new SimpleDateFormat("yyyy-MM");
    /**
     * 本期本息：等额本息4/本金5
     *
     * @param DKFFE:合同贷款金额
     * @param DKQS:贷款期数
     * @param DKHKFS：贷款还款方式
     * @param DKLL:贷款利率(年)
     * @param DQQC：当期期次     //
     */
    public static BigDecimal currentBX(BigDecimal DKFFE, int DKQS, String DKHKFS, BigDecimal DKLL, int DQQC) {
        if (DKFFE == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放额为空");
        if (DKLL == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款利率为空");
        if (DKQS <= 0) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数不小于0");
        if (!StringUtil.notEmpty(DKHKFS) || (!DKHKFS.equals("02") && !DKHKFS.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");
        if (DQQC == 0) return BigDecimal.ZERO;
        try {
            BigDecimal ZFJE = null;
            BigDecimal DKFFENEW = new BigDecimal(DKFFE.doubleValue());
            BigDecimal DKLLNEW = new BigDecimal(DKLL.doubleValue());
            BigDecimal MYBJ = DKFFENEW.divide(new BigDecimal(DKQS), 10, BigDecimal.ROUND_HALF_UP);
            if (DKHKFS.equals("02")) {//本金
                ZFJE = MYBJ.add((DKFFENEW.subtract((MYBJ.multiply((new BigDecimal(DQQC).subtract(new BigDecimal(1)))))).multiply(DKLLNEW).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)));
            }
            if (DKHKFS.equals("01")) {//本息
                ZFJE = DKFFENEW.multiply(DKLLNEW.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply((new BigDecimal(String.valueOf(Math.pow(BigDecimal.valueOf(1).
                        add(DKLLNEW.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).doubleValue(), new BigDecimal(DKQS).doubleValue()))))).divide((new BigDecimal(String.valueOf(Math.pow(new BigDecimal(1).
                        add(DKLLNEW.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).doubleValue(), new BigDecimal(DKQS).doubleValue()))).subtract(new BigDecimal(1))), 10, BigDecimal.ROUND_HALF_UP);
            }
            return ZFJE.setScale(6, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 逾期利息（本期利息/等额本金、等额本息）(一期，循环调用得到总的逾期利息)
     * DKFFE:合同贷款金额
     * YQQC:逾期期次
     * DKHKFS：贷款还款方式
     * DKLL:贷款利率
     * DKQS：贷款期数
     * //
     */
    public final  static BigDecimal overdueThisPeriodLX(BigDecimal DKFFE, int DQQC, String DKHKFS, BigDecimal DKLL, int DKQS) {
        if (DKFFE == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放额为空");
        if (DKLL == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款利率为空");
        if (DKQS <= 0) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数不小于0");
        if (!StringUtil.notEmpty(DKHKFS) || (!DKHKFS.equals("02") && !DKHKFS.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");
        try {
            if (DQQC == 0) return BigDecimal.ZERO;
            BigDecimal DKFFENEW = new BigDecimal(DKFFE.doubleValue());
            BigDecimal DKLLNEW = new BigDecimal(DKLL.doubleValue());
            BigDecimal MYBJ = DKFFENEW.divide(new BigDecimal(DKQS), 10, BigDecimal.ROUND_HALF_UP);
            BigDecimal YQLX = null;
            if (DKHKFS.equals("02")) {
                //本金
                //逾期利息=（贷款额－已还本金）×月利率(每月利息)
                YQLX = (DKFFENEW.subtract(MYBJ.multiply(new BigDecimal(DQQC - 1)))).multiply(DKLLNEW).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP);
            } else if (DKHKFS.equals("01")) {
                //等额本息
                //月均还款  b＝a×i×（1＋i）^n÷〔（1＋i）^n－1〕(n死的)
                BigDecimal yjhk = currentBX(DKFFENEW, DKQS, DKHKFS, DKLLNEW, DQQC);
                YQLX = (DKFFENEW.multiply(DKLLNEW).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP).subtract(yjhk)).multiply(
                        new BigDecimal(1).add((DKLLNEW.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP))).pow(new BigDecimal(DQQC).subtract(new BigDecimal(1)).intValue())
                ).add(yjhk);
            }
            return YQLX.setScale(6, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 等额本息：剩余利息
     */
    public final  static BigDecimal residualInterestPrincipal(int dqqc, BigDecimal dkffe, BigDecimal dkll, int dkqs, String dkhkfs) {
        BigDecimal MQLX = BigDecimal.ZERO;
        for (int i = dqqc; i <= dkqs; i++) {
            MQLX = MQLX.add(overdueThisPeriodLX(dkffe, i, dkhkfs, dkll, dkqs));
        }
        return MQLX;
    }

    /**
     * 对应的某一期
     * 逾期罚息：逾期本息×罚息利率（贷款年利率/360*1.5）×逾期天数(循环调用得到总的逾期罚息，逾期天数通过数据库带调用计算得到)
     * YQBJ:逾期本金
     * YQLX：逾期利息(等额本金，等额本息)
     * DKNLV：贷款年利率
     * YQTS:逾期天数
     */
    public final  static BigDecimal overdueFX(BigDecimal YQBJ, BigDecimal YQLX, BigDecimal DKNLV, Date DKFFRQ, int YQQC, Date YDKKRQ) {
        if (YQBJ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "逾期本金为空");
        if (YQLX == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "逾期利息为空");
        if (DKNLV == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款年利率不小于0");
        if (YQQC <= 0) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "逾期期次不小于0");
        if (DKFFRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放日期不能为空");
        if (YDKKRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期不能为空");

        BigDecimal YQBJNEW = new BigDecimal(YQBJ.doubleValue());
        BigDecimal YQLXNEW = new BigDecimal(YQLX.doubleValue());
        BigDecimal DKNLVNEW = new BigDecimal(DKNLV.doubleValue());
        int LXDay = interestDays(DKFFRQ, YQQC, YDKKRQ);
        return YQBJNEW.add(YQLXNEW).multiply(DKNLVNEW.divide(new BigDecimal(36000), 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1.5))).multiply(new BigDecimal(LXDay)).setScale(6, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * 罚息天数（正的）
     * YDKKRQ:约定扣款日期
     * YQQC:当期期次
     * DKSQ:贷款期数
     * DKFFRQ:YYYYMMDD
     * dj
     */
    public final  static int interestDays(Date DKFFRQ, int YQQC, Date YDKKRQ) {
        if (DKFFRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放日期不能为空");
        if (YDKKRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期不能为空");
        if (YQQC <= 0) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "逾期期次期数不能小于0");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DKFFRQ);
        int day = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//每月还款日
        calendar.add(Calendar.MONTH, YQQC);
        int month = calendar.get(Calendar.MONTH) + 1;//放款月
        int year = calendar.get(Calendar.YEAR);//当期年份
        calendar.setTime(YDKKRQ);
        int days = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//约定还款日
        int months = calendar.get(Calendar.MONTH);//约定扣款月
        int years = calendar.get(Calendar.YEAR);//扣款年份
        int subyear = years - year;
        int submonth = months - month;
        int subday = 30 - day + days;
        int LXDay = subyear * 12 * 30 + submonth * 30 + subday;
        if (LXDay < 0) LXDay = 0;
        return LXDay;

    }

    /**
     * 提前部分还款
     * 提前还款金额＝本次还款金额－（罚息＋逾期本息＋本期本息）
     */
    public final  static BigDecimal prepaymentKAmount(BigDecimal BCHKJE, BigDecimal BQBX) {
        if (BCHKJE == null) throw new ErrorException("BCHKJE为空");
        if (BQBX == null) throw new ErrorException("BQBX为空");
        try {
            return new BigDecimal(BCHKJE.doubleValue()).subtract(new BigDecimal(BQBX.doubleValue())).setScale(6, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    @Deprecated
    public final  static int repaymentInterestdays(Date DKFFRQ, Date YDKKRQ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DKFFRQ);
        int days = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//每月还款日
        calendar.setTime(YDKKRQ);
        int day = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//每月还款日
        int tqts = 0;
        if (days >= day) {
            tqts = days - day;
        } else {
            tqts = 30 - day + days;
        }
        return tqts;

    }

    /**
     * 提前部分还款 负的
     * 提前还本金额=提前还款金额÷（1＋贷款月利率×计息天数÷30天）
     */
    public final  static BigDecimal prepaymentBAmount(BigDecimal TQHKJE, BigDecimal DKLL, int JXTS) {
        return new BigDecimal(TQHKJE.doubleValue()).divide(new BigDecimal(1).add(new BigDecimal(DKLL.doubleValue()).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(JXTS)).divide(new BigDecimal(30), 10, BigDecimal.ROUND_HALF_UP)), 10, BigDecimal.ROUND_HALF_UP).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计息天数(负的)(提前还款、后面的部分)
     */
    public final  static int beforeInterestDays(Date DKFFRQ, int DQQC, Date YDKKRQ) {
        if (DKFFRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放日期不能为空");
        if (YDKKRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期不能为空");
        //为0 没问题
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DKFFRQ);
            int days = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//每月还款日
            calendar.add(Calendar.MONTH, DQQC);
            calendar.setTime(YDKKRQ);//约定扣款日期
            int day = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);
            int actday = 0;
            if (day <= days) {
                actday = days - day;
            } else {
                actday = 30 - day + days;
            }
            return -actday;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 按照天计算利息
     */
    @Deprecated
    public final  static BigDecimal beforeInterestDaysall(BigDecimal DKFFE, int DKQS, String DKHKFS, BigDecimal DKLL, Date DKFFRQ, Date DQSJ) throws ParseException {
        if (!StringUtil.notEmpty(DKHKFS) || (!DKHKFS.equals("02") && !DKHKFS.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");
        LinkedList<HousingfundAccountPlanGetInformation> plan = repaymentPlan(DKFFE, DKQS, DKHKFS, DKLL, sim.format(DKFFRQ));
        DQSJ = sim.parse(sim.format(DQSJ));
        DKFFRQ = sim.parse(sim.format(DKFFRQ));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DKFFRQ);
        int yueday = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//约定天
        BigDecimal total = BigDecimal.ZERO;
        int qs = 0;
        BigDecimal ye = BigDecimal.ZERO;
        for (int i = 1; i <= DKQS; i++) {
            long beforeTime = calendar.getTime().getTime();
            calendar.add(Calendar.MONTH, 1);
            if (beforeTime < DQSJ.getTime() && DQSJ.getTime() <= calendar.getTime().getTime()) {
                qs = i;
                ye = ye.add(new BigDecimal(plan.get(i - 1).getDKYE()).add(new BigDecimal(plan.get(i - 1).getHKBJJE())));
                break;
            }
            total = total.add(new BigDecimal(plan.get(i - 1).getHKLXJE()));
        }
        calendar.setTime(DQSJ);//当前时间
        int nowday = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//天数
        if (nowday > yueday) {
            total = total.add(ye.multiply(DKLL.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(30), 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(nowday - yueday))));
        } else {
            total = total.add(ye.multiply(DKLL.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(30), 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(30 - yueday + nowday))));
        }

        return total.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     * */
    public final  static int currentQS(Date DKFSRQ, Date YDKKRQ) {
        if (DKFSRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放日期不能为空");
        if (YDKKRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期不能为空");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DKFSRQ);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(YDKKRQ);
        int days = calendar.get(Calendar.DATE);
        int months = calendar.get(Calendar.MONTH) + 1;
        int years = calendar.get(Calendar.YEAR);
        int subyear = years - year;
        int submonth = months - month;
        int total = subyear * 12 + submonth;
        if (day >= days) {
            total = total - 1;
        }
        return total + 1;

    }

    /**
     * 等额本金：剩余利息 (正常还款)
     */
    @Deprecated
    public final  static BigDecimal residualInterest(int dqqc, BigDecimal dkll, String dkhkfs, int dkqs, BigDecimal dkffe) {

        BigDecimal total = BigDecimal.ZERO;
        for (int i = dqqc; i <= dkqs; i++) {
            //期初余额
            BigDecimal dkye = loanBalanceBefore(i, dkll, dkhkfs, dkqs, dkffe);
            if (dqqc == 0) continue;
            total = total.add(dkye.multiply(new BigDecimal(dkll.doubleValue()).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)));
        }
        return total.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 贷款余额，扣前余额（期初余额）
     */
    public final  static BigDecimal loanBalanceBefore(int dqqc, BigDecimal dkll, String dkhkfs, int dkqs, BigDecimal dkffe) {
        if (dqqc == 0) return dkffe;
        BigDecimal dkye = new BigDecimal(dkffe.doubleValue());
        for (int i = 1; i <= dkqs; i++) {
            BigDecimal bjje = principalAmount(dkffe, dkqs, dkll, dkhkfs, i);
            if (dqqc == i) {
                return dkye;
            }
            dkye = dkye.subtract(bjje);
        }
        return dkye.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 贷款余额，扣后余额（期末余额）
     */
    public final  static BigDecimal loanBalanceAfter(int dqqc, BigDecimal dkll, String dkhkfs, int dkqs, BigDecimal dkffe) {
        if (dqqc == 0) return dkffe;
        BigDecimal dkye = new BigDecimal(dkffe.doubleValue());
        for (int i = 1; i <= dkqs; i++) {
            BigDecimal bjje = principalAmount(dkffe, dkqs, dkll, dkhkfs, i);
            if (dqqc == i) {
                dkye = dkye.subtract(bjje);
                if (dkye.doubleValue() <= BigDecimal.ZERO.doubleValue()) dkye = BigDecimal.ZERO;
                return dkye;
            }
            dkye = dkye.subtract(bjje);
        }
        return dkye.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 新首月计息天数
     */
    public final  static int firstMonthInterestDays(Date DKFFRQ, Date YDKKRQ) {
        if (DKFFRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放日期不能为空");
        if (YDKKRQ == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定扣款日期不能为空");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DKFFRQ);
        int day = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//原来天数
        calendar.setTime(YDKKRQ);
        int days = calendar.get(Calendar.DATE) > 30 ? 30 : calendar.get(Calendar.DATE);//约定扣款日期
        int actday = 0;
        if (days > day) {
            actday = 30 - days + day;
        } else {
            actday = day - days;
        }
        return actday + 30;

    }

    /**
     * 等额本息/等额本金:节约利息
     */
    public final  static BigDecimal savingLX(BigDecimal BCHKJE, BigDecimal DKLL, String DKHKFS, int syqc) {
        BigDecimal BCHKJENEW = new BigDecimal(BCHKJE.doubleValue());
        BigDecimal DKYE = BCHKJENEW;
        BigDecimal DKLLNEW = new BigDecimal(DKLL.doubleValue());
        BigDecimal lxze = BigDecimal.ZERO;
        for (int i = 1; i <= syqc; i++) {
            BigDecimal bjje = principalAmount(BCHKJE, syqc, DKLL, DKHKFS, i);
            lxze = lxze.add(DKYE.multiply(DKLLNEW.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)));
            DKYE = DKYE.subtract(bjje);
        }
        return lxze;
    }

    /**
     * 等额本息：新月还款额
     */
    public final  static BigDecimal newCrescentRepayment(BigDecimal sybj, BigDecimal dkll, int syqs) {
        //a×i×（1＋i）^n÷〔（1＋i）^n－1〕
        return new BigDecimal(sybj.doubleValue()).multiply(new BigDecimal(dkll.doubleValue()).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)).multiply((new BigDecimal(1)
                .add(new BigDecimal(dkll.doubleValue()).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP))
                .pow(syqs))).divide(((new BigDecimal(1).add(new BigDecimal(dkll.doubleValue())
                .divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP))).pow(syqs).subtract(new BigDecimal(1))), 10, BigDecimal.ROUND_HALF_UP).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 每月本金金额(等额本息、等额本金)
     */
    public final  static BigDecimal principalAmount(BigDecimal dkffe, int dkqs, BigDecimal dkll, String dkhkfs, int dqqc) {

        if (dkffe == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款发放额为空");
        if (dkll == null) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款利率为空");
        if (dkqs <= 0) throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数不小于0");
        if (!StringUtil.notEmpty(dkhkfs) || (dkhkfs.equals("02") && dkhkfs.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");
        try {
            BigDecimal bqbx = currentBX(dkffe, dkqs, dkhkfs, dkll, dqqc);//本期本息
            BigDecimal mylx = overdueThisPeriodLX(dkffe, dqqc, dkhkfs, dkll, dkqs);//逾期利息
            BigDecimal bjje = bqbx.subtract(mylx);
            return bjje.setScale(6, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 当前时间获取期数: 正常还款时间计算
     */
    public final  static CurrentPeriodRange theTimePeriod(Date dkffrq, int dkqs, Date dqsj) {
        try {
            dqsj = sim.parse(sim.format(dqsj));
            CurrentPeriodRange currentPeriodRange = new CurrentPeriodRange();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dkffrq);
            for (int i = 1; i <= dkqs; i++) {
                long beforeTime = calendar.getTime().getTime();
                calendar.add(Calendar.MONTH, 1);
                if (beforeTime < dqsj.getTime() && dqsj.getTime() <= calendar.getTime().getTime()) {
                    currentPeriodRange.setBeforeTime(new Date(beforeTime));
                    currentPeriodRange.setCurrentPeriod(i);
                    currentPeriodRange.setAfterTime(new Date(calendar.getTime().getTime()));
                    break;
                }
            }
            return currentPeriodRange;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 还款计划（本金、本息）
     * over
     */
    public final  static LinkedList<HousingfundAccountPlanGetInformation> repaymentPlan(BigDecimal DKFFE, int DKQS, String DKHKFS, BigDecimal DKLL, String DKFFRQ) throws ParseException {
        if (!StringUtil.notEmpty(DKHKFS) || (!DKHKFS.equals("02") && !DKHKFS.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sim.parse(DKFFRQ));
        int sourceDay= calendar.get(Calendar.DAY_OF_MONTH);
        LinkedList<HousingfundAccountPlanGetInformation> information = new LinkedList<>();
        HousingfundAccountPlanGetInformation housingfundAccountPlanGetInformation = null;
        BigDecimal dkye = DKFFE;
        if (dkye.compareTo(new BigDecimal(0)) == 0) return information;
        boolean first=true;
        BigDecimal sqbj=null;
        for (int i = 1; i <= DKQS; i++) {
            housingfundAccountPlanGetInformation = new HousingfundAccountPlanGetInformation();
            calendar.add(Calendar.MONTH, 1);
            int newDay= calendar.get(Calendar.DAY_OF_MONTH);
            int maxDay=calendar.getActualMaximum(Calendar.DATE);
            if(newDay>sourceDay){
                calendar.set(Calendar.DAY_OF_MONTH,-(newDay-sourceDay));
            }else if(newDay<sourceDay&&newDay<maxDay&&maxDay<=sourceDay){
                calendar.add(Calendar.DAY_OF_MONTH,maxDay-newDay);
            }
            BigDecimal bqbx = currentBX(DKFFE, DKQS, DKHKFS, DKLL, i).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal mylx = overdueThisPeriodLX(DKFFE, i, DKHKFS, DKLL, DKQS).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bjje = bqbx.subtract(mylx);
            if(!first) {
                dkye = dkye.subtract(sqbj);
            }else{
                first=false;
            }
            sqbj=bjje;

            housingfundAccountPlanGetInformation.setHKQC(String.valueOf(i));//还款期次
            housingfundAccountPlanGetInformation.setHKRQ(sim.format(calendar.getTime()) + "");//还款日期
            housingfundAccountPlanGetInformation.setFSE(bqbx.toString());//本期本息
            housingfundAccountPlanGetInformation.setHKBJJE(bjje.toString());//本金金额
            housingfundAccountPlanGetInformation.setHKLXJE(mylx.toString());//利息
            housingfundAccountPlanGetInformation.setDKYE(dkye.toString());//贷款余额
            information.add(housingfundAccountPlanGetInformation);
        }
        return information;
    }

    public final  static Date periodOfafterTime(Date dkffrq, int dqqc) {
        try {
            dkffrq = sim.parse(sim.format(dkffrq));
            Calendar cal = Calendar.getInstance();
            cal.setTime(dkffrq);
            cal.add(Calendar.MONTH, dqqc);
            Date calafter = cal.getTime();
            return sim.parse(sim.format(calafter));
        } catch (Exception e) {
            throw new ErrorException(e);
        }

    }

    public final  static Date settlementDate(Date dkffrq, int dkqs) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dkffrq);
        cal.add(Calendar.MONTH, dkqs);
        return cal.getTime();
    }


    public final  static CurrentBeforeAfterime burrentBeforeAfterTime(Date dkffrq, int dqqc) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dkffrq);
            calendar.add(Calendar.MONTH, dqqc);
            long dqsj = DateUtil.dateStringtoStringDate(calendar.getTime()).getTime();
            long dqsj7 = dqsj + 604800000;
            calendar.add(Calendar.MONTH, -1);
            long sqsj7 = DateUtil.dateStringtoStringDate(calendar.getTime()).getTime() + 604800000;
            calendar.add(Calendar.MONTH, 2);
            long xqsj = DateUtil.dateStringtoStringDate(calendar.getTime()).getTime();
            return new CurrentBeforeAfterime(dqsj, dqsj7, sqsj7, xqsj);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public final  static void currentRepamentTime(Date dkffrq, Date ydkkrq, int dqqc) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dkffrq);
            calendar.add(Calendar.MONTH, dqqc);
            long day = calendar.getTimeInMillis();
            calendar.add(Calendar.DATE, 7);
            long dayafter7 = calendar.getTimeInMillis();
            calendar.setTime(ydkkrq);
            long days = calendar.getTimeInMillis();
            if (days >= day && days <= dayafter7) {
                throw new ErrorException("正常还款时区不能办理提前还款业务，请" + ((dayafter7 - days) / 86400000 + 1) + "日后来办理此业务");
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public final  static SettleAccounts settleAccounts(BigDecimal dkye, BigDecimal bqbj, BigDecimal dkll, BigDecimal bqbx, int jxts) {
        BigDecimal tqhbje = dkye.subtract(bqbj);
        BigDecimal tqhklx = tqhbje.multiply(dkll.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP))
                .multiply(new BigDecimal(jxts)).divide(new BigDecimal(30), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal tqjqdkze = bqbx.add(tqhbje).add(tqhklx);
        return new SettleAccounts() {{
            this.setTQHBJE(tqhbje);
            this.setTQHKLX(tqhklx);
            this.setTQJQHKZE(tqjqdkze);
        }};
    }

    public final  static SettlePartialRepayments settlePartialRepayments(BigDecimal bchkje, BigDecimal bqbx, BigDecimal dkll, int jxts) {
        BigDecimal tqhkje = new BigDecimal(bchkje.doubleValue()).subtract(new BigDecimal(bqbx.doubleValue())).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal tqhbje = new BigDecimal(tqhkje.doubleValue()).divide(new BigDecimal(1).add(new BigDecimal(dkll.doubleValue()).divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(jxts)).divide(new BigDecimal(30), 10, BigDecimal.ROUND_HALF_UP)), 10, BigDecimal.ROUND_HALF_UP).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal tqhklx = tqhkje.subtract(tqhbje);
        return new SettlePartialRepayments() {{
            this.setTQHKJE(tqhkje);
            this.setTQHBJE(tqhbje);
            this.setTQHKLX(tqhklx);
        }};
    }

    public final  static BigDecimal lendingRate(BigDecimal dkll, BigDecimal llfdbl) {
        if(llfdbl.compareTo(BigDecimal.ZERO)==0){
            return dkll;
        }else{
            return dkll.multiply(llfdbl);
        }
    }


    public final  static boolean checkTime(Date ydkkrq) {
        String[] split = sim.format(ydkkrq).split("-");
        Calendar instance = Calendar.getInstance();
        int month = instance.get(Calendar.MONTH) + 1;
        int days = instance.get(Calendar.DATE);
        int currentMonthDay = getCurrentMonthDay(instance);

        int currentMonthDay1 = Integer.parseInt(split[1]);
        int months = Integer.parseInt(split[2]);
        if (month == months) {
            if (days == currentMonthDay1 || currentMonthDay1 >= currentMonthDay) {
                return true;
            }
        }
        return false;
    }

    public final  static int getCurrentMonthDay(Calendar a) {
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public final  static int sea() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sim.parse("2017-10-31"));
            calendar.add(Calendar.MONTH, 1);
            System.out.println(sim.format(calendar.getTime()));
        } catch (ParseException e) {

        }
        return 1;
    }
    /**
     * 还款计划（本金、本息）时间范围搜索
     *
     */
    public final  static LinkedList<HousingfundAccountPlanGetInformation> repaymentPlanTimeFilter(BigDecimal DKFFE, int DKQS, String DKHKFS, BigDecimal DKLL, String DKFFRQ,String HKRQS,String HKRQE) throws ParseException {
        if (!StringUtil.notEmpty(DKHKFS) || (!DKHKFS.equals("02") && !DKHKFS.equals("01")))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款还款方式为空,或者02本金01本息");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sim.parse(DKFFRQ));
        LinkedList<HousingfundAccountPlanGetInformation> information = new LinkedList<>();
        HousingfundAccountPlanGetInformation housingfundAccountPlanGetInformation = null;
        BigDecimal dkye = DKFFE;
        if (dkye.compareTo(new BigDecimal(0)) == 0) return information;
        boolean first=true;
        BigDecimal sqbj=null;
        for (int i = 1; i <= DKQS; i++) {
            housingfundAccountPlanGetInformation = new HousingfundAccountPlanGetInformation();
            calendar.add(Calendar.MONTH, 1);
            BigDecimal bqbx = currentBX(DKFFE, DKQS, DKHKFS, DKLL, i).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal mylx = overdueThisPeriodLX(DKFFE, i, DKHKFS, DKLL, DKQS).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bjje = bqbx.subtract(mylx);
            if(!first) {
                dkye = dkye.subtract(sqbj);
            }else{
                first=false;
            }
            sqbj=bjje;

            housingfundAccountPlanGetInformation.setHKQC(String.valueOf(i));//还款期次
            String HKRQ = ny.format(calendar.getTime());
            if(HKRQ!=null && HKRQS!=null && HKRQE!=null){
             int res_l = HKRQ.compareTo(HKRQS);
             int res_r = HKRQ.compareTo(HKRQE);
                if (res_l==0){//"a=b"
                }else if(res_l < 0){ //"a<b"
                    continue;
                }else if(res_l>0 && res_r<=0){ //"a>b"
                }else if(res_l>0 && res_r>0){
                    continue;
                }
            }
            housingfundAccountPlanGetInformation.setHKRQ(sim.format(calendar.getTime()) + "");//还款日期
            housingfundAccountPlanGetInformation.setFSE(bqbx.toString());//本期本息
            housingfundAccountPlanGetInformation.setHKBJJE(bjje.toString());//本金金额
            housingfundAccountPlanGetInformation.setHKLXJE(mylx.toString());//利息
            housingfundAccountPlanGetInformation.setDKYE(dkye.toString());//贷款余额
            information.add(housingfundAccountPlanGetInformation);
        }
        return information;
    }
}
