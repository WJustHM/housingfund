package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferNewVice;

import java.util.Date;
import java.util.List;

/**
 * 个人开户，转移账户情况时，记录转移前个人账户信息
 */
public interface ICCollectionIndividualAccountTransferNewViceDAO extends IBaseDAO<CCollectionIndividualAccountTransferNewVice> {

    List<Object[]> getTransferOutBus(String dwzh, Date bjsj);

}
