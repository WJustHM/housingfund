package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionIndividualAccountActionVice;

import java.util.Date;
import java.util.List;

/**
 * Created by Funnyboy on 2017/7/10.
 */
public interface ICCollectionIndividualAccountActionViceDAO extends IBaseDAO<CCollectionIndividualAccountActionVice> {
    CCollectionIndividualAccountActionVice getByYWLSH(String ywlsh);

    List<CCollectionIndividualAccountActionVice> getSealBusBefore(String dwzh, String qcyf, Date lastqcbjsj);

    List<CCollectionIndividualAccountActionVice> getSealBusAfter(String dwzh, String qcyf, Date lastqcbjsj);

    List<CCollectionIndividualAccountActionVice> getSealBus(String dwzh, String qcyf);

    CCollectionIndividualAccountActionVice getNearlySeal(String grzh, String zcdw);
}
