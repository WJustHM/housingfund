package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitInformationBasicVice;

import java.util.List;

/**
 * Created by lian on 2017/7/10.
 */
public interface ICCollectionUnitInformationBasicViceDAO extends  IBaseDAO<CCollectionUnitInformationBasicVice>{
    List<CCollectionUnitInformationBasicVice> getUnitSetDoingByName(String dwmc);

    CCollectionUnitInformationBasicVice getUnitBaseBus(String ywlsh);
}
