package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionIndividualAccountBasicVice;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Funnyboy on 2017/7/10.
 */
@Transactional
public interface ICCollectionIndividualAccountBasicViceDAO extends IBaseDAO<CCollectionIndividualAccountBasicVice> {
    List<CCollectionIndividualAccountBasicVice> getAfter(String dwzh,Date bjsj);

    CCollectionIndividualAccountBasicVice getByYwlsh(String ywlsh);

    void saveNoflush(CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice);
}
