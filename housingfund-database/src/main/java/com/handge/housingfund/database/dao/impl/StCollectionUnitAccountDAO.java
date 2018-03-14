package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.IStCollectionUnitAccountDAO;
import com.handge.housingfund.database.entities.StCollectionUnitAccount;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class StCollectionUnitAccountDAO extends BaseDAO<StCollectionUnitAccount>
		implements IStCollectionUnitAccountDAO {

	@Override
	public StCollectionUnitAccount getUnitAccount(String DWZH) {
		String sql = "select result from StCollectionUnitAccount result " +
				"where result.dwzh = :dwzh " +
				"and result.deleted = false";
		StCollectionUnitAccount unitAccount = getSession().createQuery(sql, StCollectionUnitAccount.class)
				.setParameter("dwzh", DWZH)
				.uniqueResult();
		return unitAccount;
	}

	@Override
	public BigDecimal getDwzhyeHJ() {
		String sql = "select sum(res.dwzhye) FROM StCollectionUnitAccount res ";
		BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
				.uniqueResult();
		return result;
	}
}
