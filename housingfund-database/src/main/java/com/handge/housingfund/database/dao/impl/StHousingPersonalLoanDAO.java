package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.IStHousingPersonalLoanDAO;
import com.handge.housingfund.database.entities.StHousingPersonalLoan;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StHousingPersonalLoanDAO extends BaseDAO<StHousingPersonalLoan> implements IStHousingPersonalLoanDAO {

    @Override
    public StHousingPersonalLoan getByDKZH(String dkzh) {
        List<StHousingPersonalLoan> list  = getSession().createCriteria(StHousingPersonalLoan.class)
                .add(Restrictions.eq("dkzh",dkzh))
                .add(Restrictions.eq("deleted",false))
                .list();
        if(list.size() == 1){
            return list.get(0);
        }
        return null;
    }
}
