package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICollectionTimedTaskDAO;
import com.handge.housingfund.database.entities.CCollectionTimedTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 凡 on 2017/9/11.
 */
@Repository
public class CollectionTimedTaskDAO extends BaseDAO<CCollectionTimedTask> implements ICollectionTimedTaskDAO {


    /**
     * //TODO 加入生效年月
     * @param zxsj
     */
    @Override
    public List<CCollectionTimedTask> getCollectionTask(String zxsj) {

        String sql = "select result from CCollectionTimedTask result " +
                "where result.zxzt = '00' and result.zxsj <= :zxsj " +
                "order by result.created_at asc ";
        List<CCollectionTimedTask> list = getSession().createQuery(sql, CCollectionTimedTask.class)
                .setParameter("zxsj",zxsj)
                .list();

        return list;
    }

    @Override
    public int updateByCAS(CCollectionTimedTask task) {
        String sql = "update c_collection_timed_task set " +
                "zxcs = zxcs + 1 , zxzt = '01' " +
                "where ywlsh = :ywlsh and zxzt = '00' ";
        int i = getSession().createNativeQuery(sql)
                .setParameter("ywlsh", task.getYwlsh())
                .executeUpdate();
        return i;
    }
}
