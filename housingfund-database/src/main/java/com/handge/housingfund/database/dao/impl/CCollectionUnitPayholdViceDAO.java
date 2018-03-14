package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitPayholdViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitPayholdVice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CCollectionUnitPayholdViceDAO extends BaseDAO<CCollectionUnitPayholdVice>
        implements ICCollectionUnitPayholdViceDAO {
    @Override
    public CCollectionUnitPayholdVice getNearlyPayhold(String dwzh) {
        String sql = "select result from CCollectionUnitPayholdVice result " +
                "where result.dwywmx.dwzh =:dwzh " +
                "and result.dwywmx.deleted = false " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '办结' " +
                "order by result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj desc ";
        List<CCollectionUnitPayholdVice> list = getSession().createQuery(sql, CCollectionUnitPayholdVice.class)
                .setParameter("dwzh", dwzh)
                .list();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
