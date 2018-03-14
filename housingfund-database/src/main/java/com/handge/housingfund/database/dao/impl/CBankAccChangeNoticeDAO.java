package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICBankAccChangeNoticeDAO;
import com.handge.housingfund.database.entities.CBankAccChangeNotice;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by gxy on 17-8-16.
 */
@SuppressWarnings("JpaQlInspection")
@Repository
public class CBankAccChangeNoticeDAO extends BaseDAO<CBankAccChangeNotice> implements ICBankAccChangeNoticeDAO {
    public BigDecimal getAmt(String acct, Date firstDay, Date lastDay) {
        String sql = "SELECT balance FROM CBankAccChangeNotice WHERE created_at = ( SELECT MAX(created_at) FROM CBankAccChangeNotice WHERE created_at < :lastDay AND created_at > :firstDay AND acct = :account) AND acct = :account";
        List amts = getSession().createQuery(sql).setParameter("firstDay", firstDay).setParameter("lastDay", lastDay).setParameter("account", acct).getResultList();

        return amts.size() > 0 ? (BigDecimal) amts.get(0) : null;
    }
}
