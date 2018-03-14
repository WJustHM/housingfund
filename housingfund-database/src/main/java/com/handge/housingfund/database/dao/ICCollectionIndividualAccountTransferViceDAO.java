package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferVice;
import com.handge.housingfund.database.entities.StCommonUnit;

import java.util.List;

/**
 * Created by Liujuhao on 2017/7/10.
 */
public interface ICCollectionIndividualAccountTransferViceDAO extends IBaseDAO<CCollectionIndividualAccountTransferVice> {

    /**
     * 内部转移业务
     * 根据业务流水号得到单位部分的办理信息
     * @param ywlsh
     * @return
     */
    CCollectionIndividualAccountTransferVice getByYWLSH(String ywlsh);

    void deleteByYWDJH(String ywlsh);

    List<CCollectionIndividualAccountTransferVice> getList(String zcdwzh, String zhuangTai);

    Object[] getUnitRefleshMsg(StCommonUnit value);
}
