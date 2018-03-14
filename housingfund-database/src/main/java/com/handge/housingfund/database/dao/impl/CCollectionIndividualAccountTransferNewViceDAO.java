package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionIndividualAccountTransferNewViceDAO;
import com.handge.housingfund.database.entities.CCollectionIndividualAccountTransferNewVice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 个人开户，转移账户情况时，记录转移前个人账户信息
 */
@Repository
public class CCollectionIndividualAccountTransferNewViceDAO extends BaseDAO<CCollectionIndividualAccountTransferNewVice>
        implements ICCollectionIndividualAccountTransferNewViceDAO {

    @Override
    public List<Object[]> getTransferOutBus(String dwzh, Date bjsj) {
        String sql = "select result.grzh,result.sxny,result.ygrjcjs,result.ydwyjce,result.ygryjce from CCollectionIndividualAccountTransferNewVice result " +
                "where result.zcdw.dwzh = :dwzh " +
                "and result.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '06' ";
        String sql2 = "and result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj ";
        String sql3 = "and result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' ";
        List<Object[]> list = null;
        if(bjsj == null){
            sql = sql + sql3;
            list = getSession().createQuery(sql)
                    .setParameter("dwzh", dwzh)
                    .list();
        }else {
            sql = sql + sql2 + sql3;
            list = getSession().createQuery(sql)
                    .setParameter("dwzh", dwzh)
                    .setParameter("bjsj", bjsj)
                    .list();
        }
        return list;
    }

}
