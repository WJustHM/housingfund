package com.handge.housingfund.finance.utils;

import com.handge.housingfund.common.service.finance.model.SubjectsBalance;
import com.handge.housingfund.database.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyi on 2017/9/13.
 */
public class FinanceComputeHelper {

    /**
     * 查找指定科目
     *
     * @param stFinanceSubjects
     * @param KMBH
     * @return
     */
    public static StFinanceSubjects searchSubjects(List<StFinanceSubjects> stFinanceSubjects, String KMBH) {
        for (StFinanceSubjects s : stFinanceSubjects) {
            if (KMBH.equals(s.getKmbh())) {
                return s;
            }
        }
        return null;
    }

    public static List<StFinanceSubjects> filterSubjects(List<StFinanceSubjects> stFinanceSubjects, int KMJB) {
        List<StFinanceSubjects> res = new ArrayList<>();
        for (StFinanceSubjects s : stFinanceSubjects) {
            if (s.getKmjb().intValue() == KMJB) {
                res.add(s);
            }
        }
        return res;
    }

    public static List<StFinanceSubjects> filterSubjects(List<StFinanceSubjects> stFinanceSubjects, String KMBH, int KMJB) {
        List<StFinanceSubjects> res = new ArrayList<>();
        for (StFinanceSubjects s : stFinanceSubjects) {
            if (s.getKmjb().intValue() == KMJB && s.getKmbh().indexOf(KMBH) == 0) {
                res.add(s);
            }
        }
        return res;
    }

    /**
     * 查找指定科目余额信息
     *
     * @param financeSubjectsBalances
     * @param KMBH
     * @return
     */
    public static CFinanceSubjectsBalance searchSubjectsBalance(List<CFinanceSubjectsBalance> financeSubjectsBalances, String KMBH) {
        for (CFinanceSubjectsBalance s : financeSubjectsBalances) {
            if (KMBH.equals(s.getKmbh())) {
                return s;
            }
        }
        return null;
    }

    /**
     * 查找指定科目余额信息
     *
     * @param financeSubjectsBalances
     * @param KMBH
     * @return
     */
    public static List<CFinanceSubjectsBalance> searchSubjectsBalanceLike(List<CFinanceSubjectsBalance> financeSubjectsBalances, String KMBH) {
        List<CFinanceSubjectsBalance> res = new ArrayList<>();
        for (CFinanceSubjectsBalance s : financeSubjectsBalances) {
            if (s.getKmbh().indexOf(KMBH) == 0) {
                res.add(s);
            }
        }
        return res;
    }

    /**
     * 查找指定科目余额信息
     *
     * @param financeSubjectsBalances
     * @param KMBH
     * @return
     */
    public static BigDecimal searchSubjectsBalanceNotDb(List<SubjectsBalance> financeSubjectsBalances, String KMBH) {
        for (SubjectsBalance s : financeSubjectsBalances) {
            if (KMBH.equals(s.getKMBH())) {
                return s.getBYYE() != null ? new BigDecimal(s.getBYYE()) : BigDecimal.ZERO;
            }
        }
        return null;
    }

    /**
     * 查找指定科目余额信息
     *
     * @param financeSubjectsBalances
     * @param KMBH
     * @return
     */
    public static List<CFinanceSubjectsBalance> searchSubjectsBalances(List<CFinanceSubjectsBalance> financeSubjectsBalances, String KMBH) {
        List<CFinanceSubjectsBalance> res = new ArrayList<>();
        for (CFinanceSubjectsBalance s : financeSubjectsBalances) {
            if (KMBH.equals(s.getKmbh())) {
                res.add(s);
            }
        }
        return res;
    }

    /**
     * 查找指定科目季度余额信息
     *
     * @param cFinanceSubjectsBalanceQuarters
     * @param KMBH
     * @return
     */
    public static List<CFinanceSubjectsBalanceQuarter> searchSubjectsBalanceQuarters(List<CFinanceSubjectsBalanceQuarter> cFinanceSubjectsBalanceQuarters, String KMBH) {
        List<CFinanceSubjectsBalanceQuarter> res = new ArrayList<>();
        for (CFinanceSubjectsBalanceQuarter c : cFinanceSubjectsBalanceQuarters) {
            if (KMBH.equals(c.getKmbh())) {
                res.add(c);
            }
        }
        return res;
    }

    /**
     * 查找指定科目季度余额信息
     *
     * @param cFinanceSubjectsBalanceQuarters
     * @param KMBH
     * @return
     */
    public static CFinanceSubjectsBalanceQuarter searchSubjectsBalanceQuarter(List<CFinanceSubjectsBalanceQuarter> cFinanceSubjectsBalanceQuarters, String KMBH) {
        for (CFinanceSubjectsBalanceQuarter c : cFinanceSubjectsBalanceQuarters) {
            if (KMBH.equals(c.getKmbh())) {
                return c;
            }
        }
        return null;
    }

    /**
     * 查找指定科目年度余额信息
     *
     * @param cFinanceSubjectsBalanceYears
     * @param KMBH
     * @return
     */
    public static CFinanceSubjectsBalanceYear searchSubjectsBalanceYear(List<CFinanceSubjectsBalanceYear> cFinanceSubjectsBalanceYears, String KMBH) {
        for (CFinanceSubjectsBalanceYear c : cFinanceSubjectsBalanceYears) {
            if (KMBH.equals(c.getKmbh())) {
                return c;
            }
        }
        return null;
    }

    /**
     * 获取指定级别科目余额信息
     *
     * @param cFinanceSubjectsBalanceList
     * @param KMBH
     * @param KMJB
     * @return
     */
    public static List<CFinanceSubjectsBalance> filterBalance(List<CFinanceSubjectsBalance> cFinanceSubjectsBalanceList, String KMBH, int KMJB) {
        List<CFinanceSubjectsBalance> res = new ArrayList<>();
        for (CFinanceSubjectsBalance c : cFinanceSubjectsBalanceList) {
            if (c.getKmjb().intValue() == KMJB && c.getKmbh().indexOf(KMBH) == 0) {
                res.add(c);
            }
        }
        return res;
    }

    /**
     * 搜索记账凭证信息
     *
     * @param stFinanceRecordingVouchers
     * @param KMBH                       科目编号
     * @return
     */
    public static List<StFinanceRecordingVoucher> searchRecordingVoucher(List<StFinanceRecordingVoucher> stFinanceRecordingVouchers, String KMBH) {
        List<StFinanceRecordingVoucher> res = new ArrayList<>();
        for (StFinanceRecordingVoucher voucher : stFinanceRecordingVouchers) {
            if (KMBH.equals(voucher.getKmbh())) {
                res.add(voucher);
            }
        }
        return res;
    }

}
