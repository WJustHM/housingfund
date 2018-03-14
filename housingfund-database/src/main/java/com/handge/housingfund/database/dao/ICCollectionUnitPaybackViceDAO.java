package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitPaybackVice;
import com.handge.housingfund.database.entities.StCommonPerson;

import java.util.Date;
import java.util.List;


public interface ICCollectionUnitPaybackViceDAO extends IBaseDAO<CCollectionUnitPaybackVice> {

    CCollectionUnitPaybackVice getByYwlsh(String ywlsh);

    List<StCommonPerson> getIndiAcctSetAfterSlsj(String dwzh, Date hbjnyDate, Date slsj);

    boolean isExistPayBack(String dwzh, String grzh, String hbjny);

    boolean isExistPayBack(String grzh, String createMonth);

    boolean isExistDoing(String dwzh);

    CCollectionUnitPaybackVice getPaybackNew(String dwzh, String date);

    boolean isAlreadyExistPayBack(String grzh, String sxny);

    /**
     *
     */
    boolean checkIsExistDoing(String grzh,String dwzh);

}
