package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionIndividualAccountTransferViceDAO;
import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferVice;
import com.handge.housingfund.database.entities.StCommonUnit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Liujuhao on 2017/7/10.
 */

@Repository
public class CCollectionIndividualAccountTransferViceDAO extends BaseDAO<CCollectionIndividualAccountTransferVice>
        implements ICCollectionIndividualAccountTransferViceDAO {
	@Override
    public CCollectionIndividualAccountTransferVice getByYWLSH(String ywlsh) {
        //noinspection JpaQlInspection
        String sql = "select result from CCollectionIndividualAccountTransferVice result where result.dwywmx.ywlsh  = :ywlsh";
        CCollectionIndividualAccountTransferVice result = (CCollectionIndividualAccountTransferVice)getSession().createQuery(sql)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    @Override
    public void deleteByYWDJH(String ywlsh) {
        CCollectionIndividualAccountTransferVice c = getByYWLSH(ywlsh);
        this.delete(c);
    }

    @Override
    public List<CCollectionIndividualAccountTransferVice> getList(String zcdwzh, String zhuangTai) {
        Criteria criteria = getSession().createCriteria(CCollectionIndividualAccountTransferVice.class);
        criteria.createAlias("dwywmx","dwywmx");
        criteria.createAlias("dwywmx.cCollectionUnitBusinessDetailsExtension","extenstion");
        if(null != zcdwzh){
            criteria.add(Restrictions.eq("dwywmx.dwzh",zcdwzh));
        }
        if(null != zhuangTai){
            criteria.add(Restrictions.eq("extenstion.step",zhuangTai));
        }
        return criteria.list();
    }

    @Override
    public Object[] getUnitRefleshMsg(StCommonUnit value) {



        return new Object[0];
    }
}
