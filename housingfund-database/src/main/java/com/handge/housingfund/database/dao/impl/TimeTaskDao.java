package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICTimeTaskDao;
import com.handge.housingfund.database.entities.CTimeTask;
import org.springframework.stereotype.Repository;

@Repository
public class TimeTaskDao extends BaseDAO<CTimeTask> implements ICTimeTaskDao{
}
