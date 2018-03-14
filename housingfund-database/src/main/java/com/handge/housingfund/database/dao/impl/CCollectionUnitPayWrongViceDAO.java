package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitPayWrongViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitPayWrongVice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CCollectionUnitPayWrongViceDAO extends BaseDAO<CCollectionUnitPayWrongVice>
        implements ICCollectionUnitPayWrongViceDAO {
    @Override
    public boolean isAlreadyGenerated(String grzh, Date jcgzny) {
        String sql = "select result from CCollectionUnitPayWrongVice result inner join result.cjxq cjxq " +
                "where cjxq.grzh = :grzh and result.jcgzny = :jcgzny ";
        CCollectionUnitPayWrongVice result = getSession().createQuery(sql, CCollectionUnitPayWrongVice.class)
                .setParameter("grzh", grzh)
                .setParameter("jcgzny", jcgzny)
                .uniqueResult();
        return result != null;
    }

    @Override
    public boolean isAlreadyExistPayWrong(String grzh, Date jcgzny) {
        String sql = "select result from CCollectionUnitPayWrongVice result inner join result.cjxq cjxq " +
                "where cjxq.grzh = :grzh " +
                "and result.jcgzny = :jcgzny " +
                "and result.dwywmx.deleted = false " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '已入账' ";
        CCollectionUnitPayWrongVice result = getSession().createQuery(sql, CCollectionUnitPayWrongVice.class)
                .setParameter("grzh", grzh)
                .setParameter("jcgzny", jcgzny)
                .uniqueResult();
        return result != null;
    }

    @Override
    public boolean isExistDoing(String dwzh, Date jcgzny) {
        String sql = "select result from CCollectionUnitPayWrongVice result " +
                "where result.jcgzny = :jcgzny and result.dwywmx.dwzh = :dwzh " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step not in ('新建','已入账') ";
        List<CCollectionUnitPayWrongVice> result = getSession().createQuery(sql, CCollectionUnitPayWrongVice.class)
                .setParameter("dwzh", dwzh)
                .setParameter("jcgzny", jcgzny)
                .list();
        if(result != null && result.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public CCollectionUnitPayWrongVice getUnitPayWrongNewStatus(String dwzh, Date jcgzny) {
        String sql = "select result from CCollectionUnitPayWrongVice result " +
                "where result.jcgzny = :jcgzny " +
                "and result.dwywmx.dwzh = :dwzh " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '新建' " +
                "and result.dwywmx.deleted = false ";
        CCollectionUnitPayWrongVice result = getSession().createQuery(sql, CCollectionUnitPayWrongVice.class)
                .setParameter("dwzh", dwzh)
                .setParameter("jcgzny", jcgzny)
                .uniqueResult();
        return result;
    }

    @Override
    public CCollectionUnitPayWrongVice getByYwlsh(String ywlsh) {
        String sql = "select result from CCollectionUnitPayWrongVice result " +
                "where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionUnitPayWrongVice result = getSession().createQuery(sql, CCollectionUnitPayWrongVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

}