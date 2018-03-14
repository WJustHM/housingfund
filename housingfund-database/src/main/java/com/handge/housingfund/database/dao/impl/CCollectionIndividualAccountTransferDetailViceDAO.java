package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionIndividualAccountTransferDetailViceDAO;
import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferDetailVice;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 向超 on 2017/7/10.
 */
@Repository
public class CCollectionIndividualAccountTransferDetailViceDAO extends BaseDAO<CCollectionIndividualAccountTransferDetailVice> implements ICCollectionIndividualAccountTransferDetailViceDAO {
    @SuppressWarnings({ "unchecked", "deprecation" })
	@Override
    public List<CCollectionIndividualAccountTransferDetailVice> getByYWLSH(String ywlsh) {
        List<CCollectionIndividualAccountTransferDetailVice> list  = getSession().createCriteria(CCollectionIndividualAccountTransferDetailVice.class)
                .add(Restrictions.eq("ywlsh",ywlsh))
                .add(Restrictions.eq("deleted",false))
                .list();
        return list;
    }

}
