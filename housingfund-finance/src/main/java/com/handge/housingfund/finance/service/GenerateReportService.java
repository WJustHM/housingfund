package com.handge.housingfund.finance.service;


import com.handge.housingfund.database.dao.ICFinanceAccountPeriodDAO;
import com.handge.housingfund.database.dao.ICFinanceSubjectsBalanceQuarterDAO;
import com.handge.housingfund.database.dao.ICFinanceSubjectsBalanceYearDAO;
import com.handge.housingfund.database.entities.CFinanceAccountPeriod;
import com.handge.housingfund.database.entities.CFinanceSubjectsBalance;
import com.handge.housingfund.database.entities.CFinanceSubjectsBalanceQuarter;
import com.handge.housingfund.database.entities.CFinanceSubjectsBalanceYear;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xuefei_wang on 17-9-11.
 */


/**
 * notice GenerateReportService hanpend after
 *
 *
 */
@Service
public class GenerateReportService {

    public static Hashtable<String,List<String>>  quarterToMonth = new Hashtable<>();

    static {
        quarterToMonth.put("03",Arrays.asList("01","02","03"));
        quarterToMonth.put("06",Arrays.asList("04","05","06"));
        quarterToMonth.put("09",Arrays.asList("07","08","09"));
        quarterToMonth.put("12",Arrays.asList("10","11","12"));
    }

    public static final String  END_MONTH = "12";

    @Autowired
    ICFinanceAccountPeriodDAO financeAccountPeriodDAO;

    @Autowired
    ICFinanceSubjectsBalanceQuarterDAO financeSubjectsBalanceQuarterDAO;

    @Autowired
    ICFinanceSubjectsBalanceYearDAO financeSubjectsBalanceYearDAO;


    public  void  generateIncomExpenseReportToDB(String period){
        Set<String> specialMonths = quarterToMonth.keySet();
        Hashtable<String,CFinanceSubjectsBalanceQuarter> tempQuarter = new Hashtable<>();
        Hashtable<String,CFinanceSubjectsBalanceYear> tempYear = new Hashtable<>();
        if (period == null || period.length() !=6 || !StringUtils.isNumeric(period)) return;
        String year = period.substring(4);
        String month = period.substring(4,6);
        if (specialMonths.contains(month)){
            List<String> monthes = quarterToMonth.get(month);
            for (String m : monthes){
                HashMap filter = new HashMap();
                filter.put("kjnd",year);
                filter.put("kjqj",String.format("%s%s",year,m));
                filter.put("sfjs",true);
                List<CFinanceAccountPeriod> periods = financeAccountPeriodDAO.list(filter, null, null, null, null, null, null);
                if (periods.isEmpty() || (period.length() != 1)) return;
                CFinanceAccountPeriod fap = periods.get(0);
                List<CFinanceSubjectsBalance> fapsb = fap.getcFinanceSubjectsBalances();
                for (CFinanceSubjectsBalance fsb : fapsb){
                    CFinanceSubjectsBalanceQuarter fsbq = tempQuarter.getOrDefault(fsb.getKmbh(), new CFinanceSubjectsBalanceQuarter(fsb.getKmmc(),
                            fsb.getKmbh(),
                            new BigDecimal(0),
                            new BigDecimal(0),
                            new BigDecimal(0),
                            new BigDecimal(0),
                            fsb.getKmjb(),fsb.getKmyefx(),year,String.valueOf((Integer.valueOf(month) / 3) +1)));
                    fsbq.setBjjs(fsbq.getBjjs().add(fsb.getByjs()));
                    fsbq.setBjzj(fsbq.getBjzj().add(fsb.getByzj()));
                    if (Integer.valueOf(m) / 3 == 1) {
                        fsbq.setSjye(fsb.getSyye());
                    }
                    if (Integer.valueOf(m) / 3 == 0) {
                        fsbq.setBjye(fsb.getByye());
                    }
                    tempQuarter.put(fsb.getKmbh(),fsbq);
                }
            }

            if (month.equals(END_MONTH)){
                HashMap filter = new HashMap();
                filter.put("kjnd",year);
                filter.put("sfjs",true);
                List<CFinanceAccountPeriod> periods = financeAccountPeriodDAO.list(filter, null, null, null, null, null, null);
                if (periods.size() != 12) return;
                for (CFinanceAccountPeriod fap : periods){
                    List<CFinanceSubjectsBalance> periodDatas = fap.getcFinanceSubjectsBalances();
                    int m = Integer.valueOf(fap.getKjqj().substring(4,6));
                    for (CFinanceSubjectsBalance perioddata : periodDatas){
                        CFinanceSubjectsBalanceYear  yeardata = tempYear.getOrDefault(perioddata.getKmbh() , new CFinanceSubjectsBalanceYear(
                                perioddata.getKmbh(),
                                perioddata.getKmmc(),
                                new BigDecimal(0),
                                new BigDecimal(0),
                                new BigDecimal(0),
                                new BigDecimal(0),
                                perioddata.getKmjb(),perioddata.getKmyefx(),year));
                        yeardata.setBnjs(yeardata.getBnjs().add(perioddata.getByjs()));
                        yeardata.setBnzj(yeardata.getBnzj().add(perioddata.getByzj()));
                        if (m == 1) {
                            yeardata.setSnye(yeardata.getSnye().add(perioddata.getSyye()));
                        }
                        if (m == 12) {
                            yeardata.setBnye(yeardata.getBnye().add(perioddata.getByye()));
                        }
                        tempYear.put(perioddata.getKmbh(),yeardata);
                    }
                }

            }
        }
        flush(tempQuarter,tempYear);
    }

    public void flush(Hashtable<String,CFinanceSubjectsBalanceQuarter> tempQuarter,Hashtable<String,CFinanceSubjectsBalanceYear> tempYear){
        if (tempQuarter.size() != 0 ){
            Set<String> keys = tempQuarter.keySet();
            for (String k :  keys){
                CFinanceSubjectsBalanceQuarter quarter = tempQuarter.get(k);
                // 上季度余额　+　本季度增加　　＝　　本季度余额　+　本季度减少
                boolean isBalance = (quarter.getSjye().add(quarter.getBjzj()).equals(quarter.getBjye().add(quarter.getBjjs())));
                quarter.setVerify(isBalance);
                financeSubjectsBalanceQuarterDAO.save(quarter);
            }
        }
        if (tempYear.size() != 0){
            Set<String> keys = tempYear.keySet();
            for (String k :  keys){
                CFinanceSubjectsBalanceYear year = tempYear.get(k);
                // 上年度余额　+　本年度增加　　＝　　本年度余额　+　本年度减少
                boolean isBalance = (year.getSnye().add(year.getBnzj()).equals(year.getBnye().add(year.getBnjs())));
                year.setVerify(isBalance);
                financeSubjectsBalanceYearDAO.save(year);
            }
        }
    }
}
