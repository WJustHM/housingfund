package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICLoanHousingLoanDAO;
import com.handge.housingfund.database.entities.CLoanHousingLoan;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/14.
 */
@Repository
public class CLoanHousingLoanDAO extends BaseDAO<CLoanHousingLoan> implements ICLoanHousingLoanDAO {
    @Override
    public BigDecimal getLoanDkffeTotal(Date ks, Date js) {

        String sql = "SELECT SUM(DKFFE) s FROM c_loan_housing_loan WHERE created_at>=:ks AND created_at<=:js AND state=1 AND deleted=0";
      
        String sql2 = "select sum(t.fse) " +
                "from StHousingBusinessDetails t " +
                "where " +
                "t.dkywmxlx = '01' and t.jzrq >= :ks and t.jzrq <= :js";

        Object o = getSession().createQuery(sql2)
                .setParameter("ks", ks)
                .setParameter("js", js)
                .list().get(0);

        return o != null ? (BigDecimal) o : BigDecimal.ZERO;
    }

    @Override
    public Long getLoanDkffeCount(Date ks, Date js) {
        String sql = "SELECT COUNT(*) FROM c_loan_housing_loan WHERE created_at>=:ks AND created_at<=:js AND state=1 AND deleted=0";

        String sql2 = "select count(t) " +
                "from StHousingBusinessDetails t " +
                "left join CLoanHousingBusinessProcess p on t.grywmx = p.id " +
                "where " +
                "t.jzrq >= :ks and t.jzrq <= :js and t.dkywmxlx = '01' and p.step = :ywzt";

        String sql3 = "SELECT count(1) FROM c_loan_housing_person_information_basic basic " +
                "INNER JOIN st_housing_personal_account account ON basic.personalAccount = account.id " +
                "WHERE account.dkffrq >= :ks AND account.dkffrq < :js";

        BigInteger o = (BigInteger)getSession().createSQLQuery(sql3)
                .setParameter("ks", DateUtil.date2Str(ks, "yyyy-MM-dd HH:mm:ss"))
                .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss"))
                .list().get(0);

        return o != null ? o.longValue() : 0;
    }
}
