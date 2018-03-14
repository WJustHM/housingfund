package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitPayholdVice;

public interface ICCollectionUnitPayholdViceDAO extends IBaseDAO<CCollectionUnitPayholdVice> {
    CCollectionUnitPayholdVice getNearlyPayhold(String dwzh);
}
