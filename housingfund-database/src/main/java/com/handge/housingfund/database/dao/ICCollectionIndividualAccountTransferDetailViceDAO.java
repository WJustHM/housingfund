package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferDetailVice;

import java.util.List;

/**
 * Created by 向超 on 2017/7/10.
 */
public interface ICCollectionIndividualAccountTransferDetailViceDAO extends IBaseDAO<CCollectionIndividualAccountTransferDetailVice>{
    /**
     * 内部转移业务
     * 根据业务流水号得到个人部分的办理信息，只会显示正常的数据（删除标识为false）
     * @param ywlsh
     * @return
     */
    List<CCollectionIndividualAccountTransferDetailVice> getByYWLSH(String ywlsh);

}
