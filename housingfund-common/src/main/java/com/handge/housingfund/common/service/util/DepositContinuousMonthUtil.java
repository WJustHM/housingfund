package com.handge.housingfund.common.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DepositContinuousMonthUtil {

    public static List<String> getMonths(List<String> MonthList) throws ParseException {
        List list = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String sdf = "yyyy-MM";
        String first = DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,MonthList.get(0), sdf);
        String last = null;
        for (int i=0;i<MonthList.size();i++){
            Date date_time1 = simpleDateFormat.parse(MonthList.get(i));
            long ts1 = date_time1.getTime();
            if(MonthList.size()<=(i+1)){
                break;
            }
            Date date_time2 = simpleDateFormat.parse(MonthList.get(i+1));
            long ts2 = date_time2.getTime();
            int n =(int)((ts1-ts2)/1000/3600/24);
            if (n>=28&&n<=31){
                last = DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,MonthList.get(i+1), sdf);
            }else {
                break;
            }
        }
        if(last!=null&&first!=null){
            list.add(last);
            list.add(first);
        }
        return list;
    }

}
