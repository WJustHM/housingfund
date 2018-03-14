package com.handge.housingfund.finance.service;


import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.entities.Common;

/**
 * Created by Administrator on 2017/8/30.
 */
public interface IUpdateSFSYService <K extends Common, T extends IBaseDAO<K>> {

    /**
     * 更新数据库表SFSY（是否使用）字段
     * @param entity
     * @param DAOClasz
     * @param id
     */
    public void updateSFSY(K entity, T DAOClasz, String id);

}
