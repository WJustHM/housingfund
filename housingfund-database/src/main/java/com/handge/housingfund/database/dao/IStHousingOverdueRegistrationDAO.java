package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.loan.model.OvderDueRecord;
import com.handge.housingfund.database.entities.StHousingOverdueRegistration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IStHousingOverdueRegistrationDAO extends IBaseDAO<StHousingOverdueRegistration> {

    void batchUpdate(List<StHousingOverdueRegistration> stHousingOverdueRegistration);

    List<StHousingOverdueRegistration> getByDKZH(String dkzh);

    List<StHousingOverdueRegistration>  getList();

    List<OvderDueRecord>  searchRecord();

    HashMap<String,BigInteger> count();

    BigDecimal getLoanBackTotal(Date ks, Date js);

    BigInteger overdueCount(Date stime);
}