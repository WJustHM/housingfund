package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitPayCallDetailVice;
import com.handge.housingfund.database.entities.CCollectionUnitPayCallVice;
import com.handge.housingfund.database.entities.StCommonUnit;

import java.util.List;

public interface ICCollectionUnitPayCallViceDAO extends IBaseDAO<CCollectionUnitPayCallVice> {

    List<String> getUnitNeedCall(String nowDate);

    CCollectionUnitPayCallVice getNearlyPayCall(String dwzh);

    CCollectionUnitPayCallVice getPayCall(String ywlsh);

    /**
     * 有序排序
     */
    List<CCollectionUnitPayCallDetailVice> getPayCallDetail(String ywlsh);

    Object[] getDeposit(StCommonUnit unit);

    List<StCommonUnit> getUnitNeedCall2(String nowDate);

    public List<Object[]> getPayCallMag();

    void saveNomal(CCollectionUnitPayCallVice payCall);

    void flush();
}
