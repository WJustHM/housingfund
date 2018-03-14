package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.IStCollectionPersonalAccountDAO;
import com.handge.housingfund.database.entities.StCollectionPersonalAccount;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class StCollectionPersonalAccountDAO extends BaseDAO<StCollectionPersonalAccount>
		implements IStCollectionPersonalAccountDAO {

	@Override
	public StCollectionPersonalAccount getByGrzh(String grzh) {
		List<StCollectionPersonalAccount> list  = getSession().createCriteria(StCollectionPersonalAccount.class)
				.add(Restrictions.eq("grzh",grzh))
				.add(Restrictions.eq("deleted",false))
				.list();
		if(list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	@Override
	public BigDecimal getGrzhyeHj() {
		String sql = "select sum(res.grzhye) FROM StCollectionPersonalAccount res ";
		BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
				.uniqueResult();
		return result;
	}
}
