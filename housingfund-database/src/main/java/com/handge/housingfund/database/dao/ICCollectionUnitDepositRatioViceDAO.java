package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitDepositRatioVice;

import java.util.Date;
import java.util.List;

public interface ICCollectionUnitDepositRatioViceDAO extends IBaseDAO<CCollectionUnitDepositRatioVice> {

    CCollectionUnitDepositRatioVice getDepositRatio(String ywlsh);

    List<CCollectionUnitDepositRatioVice> getDepositRatio(String dwzh,String jzny, Date bjsj);

    void doFinalUpdatePerson(String ywlsh);
}
