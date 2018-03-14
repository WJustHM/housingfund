package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitInformationBasicViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitInformationBasicVice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Funnyboy on 2017/7/10.
 */
@Repository
public class CCollectionUnitInformationBasicViceDAO extends BaseDAO<CCollectionUnitInformationBasicVice>
        implements ICCollectionUnitInformationBasicViceDAO {


    @Override
    public List<CCollectionUnitInformationBasicVice> getUnitSetDoingByName(String dwmc) {
        String sql = "select result from CCollectionUnitInformationBasicVice result " +
                "where result.dwywmx.deleted = false " +
                "and result.dwywmx.ywmxlx = '03' " +
                "and result.dwywmx.ywmxlx.cCollectionUnitBusinessDetailsExtension.step <> '办结' " +
                "and result.dwmc = :dwmc ";
        List<CCollectionUnitInformationBasicVice> list = getSession().createQuery(sql, CCollectionUnitInformationBasicVice.class)
                .setParameter("dwmc", dwmc)
                .list();
        return list;
    }

    @Override
    public CCollectionUnitInformationBasicVice getUnitBaseBus(String ywlsh) {
        String sql = "select result from CCollectionUnitInformationBasicVice result " +
                "where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionUnitInformationBasicVice result = getSession().createQuery(sql, CCollectionUnitInformationBasicVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }
}
