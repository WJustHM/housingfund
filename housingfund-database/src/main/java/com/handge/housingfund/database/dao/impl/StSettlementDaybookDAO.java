package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.IStSettlementDaybookDAO;
import com.handge.housingfund.database.entities.StSettlementDaybook;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StSettlementDaybookDAO extends BaseDAO<StSettlementDaybook> implements IStSettlementDaybookDAO {

    @Override
    public StSettlementSpecialBankAccount getSettlementDaybook(String yhhb, String zhxz) {
        String sql = "select result from StSettlementSpecialBankAccount result " +
                "where result.yhdm = :yhhb " +
                "and result.zhxz = :zhxz ";
        List<StSettlementSpecialBankAccount> list = getSession().createQuery(sql, StSettlementSpecialBankAccount.class)
                .setParameter("yhhb", yhhb)
                .setParameter("zhxz", zhxz)
                .list();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
