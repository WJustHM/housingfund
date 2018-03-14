package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.model.OvderDueRecord;
import com.handge.housingfund.common.service.util.CommLoanAlgorithm;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.database.dao.ICAccountEmployeeDAO;
import com.handge.housingfund.database.dao.IStHousingOverdueRegistrationDAO;
import com.handge.housingfund.database.entities.StHousingOverdueRegistration;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class StHousingOverdueRegistrationDAO extends BaseDAO<StHousingOverdueRegistration>
        implements IStHousingOverdueRegistrationDAO {

    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;

    @Override
    public void batchUpdate(List<StHousingOverdueRegistration> stHousingOverdueRegistration) {
        for (StHousingOverdueRegistration overdueRegistration : stHousingOverdueRegistration) {
            this.getSession().update(overdueRegistration);
        }
        this.getSession().flush();
        this.getSession().clear();
    }

    @Override
    public List<StHousingOverdueRegistration> getByDKZH(String dkzh) {
        return getSession().createCriteria(StHousingOverdueRegistration.class)
                .add(Restrictions.eq("dkzh", dkzh))
                .add(Restrictions.eq("deleted", false))
                .list();
    }

    @Override
    public List<StHousingOverdueRegistration> getList() {
        return getSession().createCriteria(StHousingOverdueRegistration.class).add(Restrictions.eq("deleted", false)).list();
    }

    @Override
    public List<OvderDueRecord> searchRecord() {
        String sql2 = "SELECT over.id,over.DKZH,over.YQQC,due.YWZT FROM st_housing_overdue_registration over " +
                "INNER JOIN c_housing_overdue_registration_extension due ON over.extenstion=due.id  WHERE over.deleted=0 AND due.YWZT <> '已入账'";

        List<Object[]> list = iAccountEmployeeDAO.list(sql2, new HashMap<String, Object>());
        List<OvderDueRecord> record = new ArrayList<>();
        OvderDueRecord over = null;
        for (Object[] reco : list) {
            over = new OvderDueRecord((String) reco[0], (String) reco[1], (BigDecimal) reco[2], (String) reco[3]);
            record.add(over);
        }
        return record;
    }

    @Override
    public HashMap<String, BigInteger> count() {
        String sql = "SELECT over.dkzh,count(*) FROM st_housing_overdue_registration over INNER JOIN c_housing_overdue_registration_extension pe" +
                " ON  over.extenstion=pe.id " +
                " WHERE" +
                " pe.YWZT<>'已入账'  group by over.dkzh ";
        List<Object[]> list = getSession().createNativeQuery(sql).list();
        HashMap<String, BigInteger> map = new HashMap<>();
        for (Object[] dkzh : list) {
            map.put((String) dkzh[0], (BigInteger) dkzh[1]);
        }
        return map;
    }

    @Override
    public BigDecimal getLoanBackTotal(Date ks, Date js) {
        Session session = getSession();
//        String sqlex = "SELECT DKZH,count(*) from st_housing_overdue_registration WHERE  created_at <= '" + js + "'  GROUP BY DKZH";
//        String sql = "SELECT DKZH,YQBJ,YQLX,YQQC from st_housing_overdue_registration over inner join c_housing_overdue_registration_extension pe WHERE over.extenstion=pe.id WHERE pe.YWZT='待入账'";
//
//        List<Object[]> obj = session.createNativeQuery(sqlex).list();
//        List<Object[]> list = session.createNativeQuery(sql).list();
        BigDecimal total = BigDecimal.ZERO;
//        for (Object[] oo : obj) {
//            if (((BigInteger) oo[1]).compareTo(new BigInteger("2")) == 1 && ((BigInteger) oo[1]).compareTo(new BigInteger("6")) == -1) {
//                for (Object[] yqxx : list) {
//                    String dkzh = (String) yqxx[0];
//                    if (dkzh.equals((String) oo[0])) {
//                        String account = "SELECT pe.DKXFFRQ,acc.DKLL,acc.LLFDBL from st_housing_personal_account acc inner join c_loan_housing_personal_account_extension pe on acc.extenstion=pe.id  WHERE" +
//                                " acc.DKZH=" + dkzh;
//                        List<Object[]> acco = getSession().createNativeQuery(account).list();
//                        if (acco.size() > 0) {
//                            Date dkffrq = (java.sql.Timestamp) acco.get(0)[0];
//                            BigDecimal dkll = (BigDecimal) acco.get(0)[1];
//                            BigDecimal llfdbl = (BigDecimal) acco.get(0)[2];
//                            BigDecimal yqbj = (BigDecimal) yqxx[1];
//                            BigDecimal yqlx = (BigDecimal) yqxx[2];
//                            BigDecimal yqqc = (BigDecimal) yqxx[3];
//                            BigDecimal ll = CommLoanAlgorithm.lendingRate(dkll, llfdbl);
//                            BigDecimal fx = CommLoanAlgorithm.overdueFX(yqbj, yqlx, ll, dkffrq, yqqc.intValue(), new Date());
//                            total = total.add(yqbj.add(yqlx).add(fx));
//                        }
//                    }
//                }
//            } else if (((BigInteger) oo[1]).compareTo(new BigInteger("6")) > 0) {
//                for (Object[] yqxx : list) {
//                    String dkzh = (String) yqxx[0];
//                    if (dkzh.equals((String) oo[0])) {
//                        String account = "SELECT pe.DKXFFRQ,acc.DKLL,acc.LLFDBL,acc.DKYE from st_housing_personal_account acc inner join c_loan_housing_personal_account_extension pe on acc.extenstion=pe.id  WHERE" +
//                                " acc.DKZH=" + dkzh;
//                        List<Object[]> acco = getSession().createNativeQuery(account).list();
//                        if (acco.size() > 0) {
//                            Date dkffrq = (java.sql.Timestamp) acco.get(0)[0];
//                            BigDecimal dkll = (BigDecimal) acco.get(0)[1];
//                            BigDecimal llfdbl = (BigDecimal) acco.get(0)[2];
//                            BigDecimal yqbj = (BigDecimal) yqxx[1];
//                            BigDecimal yqlx = (BigDecimal) yqxx[2];
//                            BigDecimal dkye = (BigDecimal) acco.get(0)[3];
//                            BigDecimal yqqc = (BigDecimal) yqxx[3];
//                            BigDecimal ll = CommLoanAlgorithm.lendingRate(dkll, llfdbl);
//                            BigDecimal fx = CommLoanAlgorithm.overdueFX(yqbj, yqlx, ll, dkffrq, yqqc.intValue(), new Date());
//                            total = total.add(yqlx).add(fx).add(dkye);
//                        }
//                        break;
//                    }
//                }
//            }
//        }

        String sqlaccount = "SELECT tmp.DKZH  FROM ( SELECT st.DKZH AS DKZH, count( st.DKZH ) AS count  FROM" +
                " st_housing_overdue_registration st INNER JOIN c_housing_overdue_registration_extension c ON st.extenstion = c.id  AND c.YWZT <> '已入账'  " +
                " WHERE DATE_FORMAT( st.created_at, '%Y-%m' ) >= DATE_FORMAT( DATE_SUB( :js , INTERVAL 3 MONTH ), '%Y-%m' )  AND DATE_FORMAT( st.created_at, '%Y-%m' ) <  DATE_FORMAT( :js , '%Y-%m') " +
                " GROUP BY st.DKZH  ) tmp WHERE tmp.count >= 3 AND tmp.count < 6 ";
        List<String> aaccount = session.createNativeQuery(sqlaccount)
                .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss")).list();
        for (String acc : aaccount) {
            String sqlst = "SELECT" +
                    " st.YQBJ,st.YQLX,st.YQQC " +
                    " FROM" +
                    " st_housing_overdue_registration st INNER JOIN c_housing_overdue_registration_extension c ON st.extenstion = c.id  AND c.YWZT <> '已入账' " +
                    " WHERE DATE_FORMAT( st.created_at, '%Y-%m' ) >= DATE_FORMAT( DATE_SUB( :js , INTERVAL 3 MONTH ), '%Y-%m' )  AND DATE_FORMAT( st.created_at, '%Y-%m' ) <  DATE_FORMAT( :js , '%Y-%m') " +
                    " AND st.DKZH = '" + acc + "'";
            String account = "SELECT pe.DKXFFRQ,acc.DKLL,acc.LLFDBL from st_housing_personal_account acc inner join c_loan_housing_personal_account_extension pe on acc.extenstion=pe.id  WHERE" +
                    " acc.DKZH=" + acc;
            List<Object[]> details = session.createNativeQuery(sqlst)
                    .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss")).list();
            List<Object[]> peracc = session.createNativeQuery(account).list();
            for (Object[] stdetails : details) {
                if (peracc.size() > 0) {
                    Date dkffrq = (java.sql.Timestamp) peracc.get(0)[0];
                    BigDecimal dkll = (BigDecimal) peracc.get(0)[1];
                    BigDecimal llfdbl = (BigDecimal) peracc.get(0)[2];
                    BigDecimal yqbj = (BigDecimal) stdetails[0];
                    BigDecimal yqlx = (BigDecimal) stdetails[1];
                    BigDecimal yqqc = (BigDecimal) stdetails[2];
                    BigDecimal ll = CommLoanAlgorithm.lendingRate(dkll, llfdbl);
                    BigDecimal fx = CommLoanAlgorithm.overdueFX(yqbj, yqlx, ll, dkffrq, yqqc.intValue(), new Date());
                    total = total.add(yqbj.add(yqlx).add(fx));
                }
            }
        }

        String sqlaccount6 = "SELECT tmp.DKZH  FROM ( SELECT st.DKZH AS DKZH, count( st.DKZH ) AS count  FROM" +
                " st_housing_overdue_registration st INNER JOIN c_housing_overdue_registration_extension c ON st.extenstion = c.id  AND c.YWZT <>  '已入账'  " +
                " WHERE DATE_FORMAT( st.created_at, '%Y-%m' ) >= DATE_FORMAT( DATE_SUB( :js , INTERVAL 6 MONTH ), '%Y-%m' )  AND DATE_FORMAT( st.created_at, '%Y-%m' ) <  DATE_FORMAT( :js , '%Y-%m') " +
                " GROUP BY st.DKZH  ) tmp WHERE tmp.count >= 6";
        List<String> aaccount6 = session.createNativeQuery(sqlaccount6)
                .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss")).list();

        for (String acc : aaccount6) {
            String sqlst = "SELECT" +
                    " st.YQBJ,st.YQLX,st.YQQC " +
                    " FROM" +
                    " st_housing_overdue_registration st INNER JOIN c_housing_overdue_registration_extension c ON st.extenstion = c.id  AND c.YWZT <> '已入账' " +
                    " WHERE DATE_FORMAT( st.created_at, '%Y-%m' ) >= DATE_FORMAT( DATE_SUB( :js , INTERVAL 6 MONTH ), '%Y-%m' )  AND DATE_FORMAT( st.created_at, '%Y-%m' ) <  DATE_FORMAT( :js , '%Y-%m') " +
                    " AND st.DKZH = '" + acc + "'";
            String account = "SELECT pe.DKXFFRQ,acc.DKLL,acc.LLFDBL,acc.DKYE from st_housing_personal_account acc inner join c_loan_housing_personal_account_extension pe on acc.extenstion=pe.id  WHERE" +
                    " acc.DKZH=" + acc;
            List<Object[]> details = session.createNativeQuery(sqlst)
                    .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss")).list();
            List<Object[]> peracc = session.createNativeQuery(account).list();
            BigDecimal tottalmon = BigDecimal.ZERO;
            for (Object[] stdetails : details) {
                if (peracc.size() > 0) {
                    Date dkffrq = (java.sql.Timestamp) peracc.get(0)[0];
                    BigDecimal dkll = (BigDecimal) peracc.get(0)[1];
                    BigDecimal llfdbl = (BigDecimal) peracc.get(0)[2];
                    BigDecimal dkye = (BigDecimal) peracc.get(0)[3];
                    BigDecimal yqbj = (BigDecimal) stdetails[0];
                    BigDecimal yqlx = (BigDecimal) stdetails[1];
                    BigDecimal yqqc = (BigDecimal) stdetails[2];
                    BigDecimal ll = CommLoanAlgorithm.lendingRate(dkll, llfdbl);
                    BigDecimal fx = CommLoanAlgorithm.overdueFX(yqbj, yqlx, ll, dkffrq, yqqc.intValue(), new Date());
                    tottalmon = tottalmon.add(dkye.add(fx));
                    break;
                }
            }
            total = total.add(tottalmon);
        }

        return total;
    }

    @Override
    public BigInteger overdueCount(Date stime) {

        String jzsj = DateUtil.date2Str(stime, "yyyy-MM-dd HH:mm:ss");

        String sql = "SELECT count(*)  from st_housing_overdue_registration de inner join c_housing_overdue_registration_extension" +
                " ex on de.extenstion=ex.id  WHERE de.deleted='0' and  de.created_at <= '" + jzsj + "'";

        String sql2 = "SELECT DISTINCT COUNT(DISTINCT de.DKZH) FROM st_housing_overdue_registration de " +
                "INNER JOIN c_housing_overdue_registration_extension ex ON de.extenstion = ex.id " +
                "WHERE ex.YWZT <> '已入账' AND de.created_at <= '" + jzsj + "'";

        BigInteger count = (BigInteger) getSession().createNativeQuery(sql2).getSingleResult();
        return count;
    }

}
