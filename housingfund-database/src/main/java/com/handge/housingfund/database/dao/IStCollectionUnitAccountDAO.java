package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StCollectionUnitAccount;

import java.math.BigDecimal;

public interface IStCollectionUnitAccountDAO extends IBaseDAO<StCollectionUnitAccount> {

    StCollectionUnitAccount getUnitAccount(String DWZH);

    BigDecimal getDwzhyeHJ();

}