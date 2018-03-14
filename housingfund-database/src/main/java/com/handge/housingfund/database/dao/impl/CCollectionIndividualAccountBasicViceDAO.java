package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionIndividualAccountBasicViceDAO;
import com.handge.housingfund.database.entities.CCollectionIndividualAccountBasicVice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Funnyboy on 2017/7/10.
 */
@Repository
public class CCollectionIndividualAccountBasicViceDAO extends BaseDAO<CCollectionIndividualAccountBasicVice> implements ICCollectionIndividualAccountBasicViceDAO {
    @Override
    public List<CCollectionIndividualAccountBasicVice> getAfter(String dwzh,Date bjsj) {
        String sql = "select result from CCollectionIndividualAccountBasicVice result " +
                "where result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and result.dwzh = :dwzh " +
                "and result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj";
        List<CCollectionIndividualAccountBasicVice> result = getSession().createQuery(sql, CCollectionIndividualAccountBasicVice.class)
                .setParameter("bjsj", bjsj)
                .setParameter("dwzh", dwzh)
                .list();
        return result;
    }

    @Override
    public CCollectionIndividualAccountBasicVice getByYwlsh(String ywlsh) {
        String sql = "select result from CCollectionIndividualAccountBasicVice result " +
                "where result.grywmx.ywlsh = :ywlsh ";
        CCollectionIndividualAccountBasicVice result = getSession().createQuery(sql, CCollectionIndividualAccountBasicVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;

    }

    @Override
    public void saveNoflush(CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice) {
        getSession().save(collectionIndividualAccountBasicVice);
    }
}
