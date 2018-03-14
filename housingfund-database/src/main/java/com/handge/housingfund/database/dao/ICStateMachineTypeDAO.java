package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CStateMachineType;

/**
 * Created by 凡 on 2017/8/5.
 */
public interface ICStateMachineTypeDAO extends IBaseDAO<CStateMachineType>{
    CStateMachineType getByStep(String step);
}
