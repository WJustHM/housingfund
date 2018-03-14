package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StSettlementDaybook;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;

public interface IStSettlementDaybookDAO extends IBaseDAO<StSettlementDaybook> {

    StSettlementSpecialBankAccount getSettlementDaybook(String yhhb, String zhxz);
}