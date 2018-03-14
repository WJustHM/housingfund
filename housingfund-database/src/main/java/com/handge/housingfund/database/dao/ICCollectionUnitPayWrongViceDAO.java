package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitPayWrongVice;

import java.util.Date;

public interface ICCollectionUnitPayWrongViceDAO extends IBaseDAO<CCollectionUnitPayWrongVice> {
    /**
     * 该人是否已经在该月产生错缴数据
     */
    boolean isAlreadyGenerated(String grzh, Date gzny);

    /**
     * 该人是否已经在该月产生错缴数据（已办结的）
     */
    boolean isAlreadyExistPayWrong(String grzh, Date gzny);

    /**
     * 是否存在已受理，未办结的错缴数据
     */
    boolean isExistDoing(String dwzh, Date cjgzny);

    CCollectionUnitPayWrongVice getUnitPayWrongNewStatus(String dwzh, Date cjgzny);

    CCollectionUnitPayWrongVice getByYwlsh(String ywlsh);
}
