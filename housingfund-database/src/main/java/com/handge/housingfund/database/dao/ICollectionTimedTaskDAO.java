package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionTimedTask;

import java.util.List;
/**
 * Created by 凡 on 2017/9/11.
 */
public interface ICollectionTimedTaskDAO extends IBaseDAO<CCollectionTimedTask>{
   List<CCollectionTimedTask> getCollectionTask(String zxsj);

   /**
    * CAS的方式更新数据，如果返回更新行数
    */
   int updateByCAS(CCollectionTimedTask task);
}
