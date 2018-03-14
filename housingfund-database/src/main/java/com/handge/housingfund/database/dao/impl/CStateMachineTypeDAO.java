package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICStateMachineTypeDAO;
import com.handge.housingfund.database.entities.CStateMachineType;
import org.springframework.stereotype.Repository;

/**
 * Created by 凡 on 2017/8/5.
 */
@Repository
public class CStateMachineTypeDAO extends BaseDAO<CStateMachineType> implements ICStateMachineTypeDAO {

    @Override
    public CStateMachineType getByStep(String step) {
        String sql = "select result from CStateMachineType result where result.step = :step";
        CStateMachineType result = getSession().createQuery(sql, CStateMachineType.class)
                .setParameter("step", step)
                .uniqueResult();
        return result;
    }
}
