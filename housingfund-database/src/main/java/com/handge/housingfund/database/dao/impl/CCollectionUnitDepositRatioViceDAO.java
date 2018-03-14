package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitDepositRatioViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitDepositRatioVice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CCollectionUnitDepositRatioViceDAO extends BaseDAO<CCollectionUnitDepositRatioVice>
        implements ICCollectionUnitDepositRatioViceDAO {
    @Override
    public CCollectionUnitDepositRatioVice getDepositRatio(String ywlsh) {
        String sql = "select result from CCollectionUnitDepositRatioVice result where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionUnitDepositRatioVice result = getSession().createQuery(sql, CCollectionUnitDepositRatioVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    /**
     * 需要查询：
     * 1.清册不存在时查出所有业务
     * 2.清册存在时，查出清册办结时间前 【生效月份大于缴至年月的】 与 【清册办结时间后】 的业务
     */
    @Override
    public List<CCollectionUnitDepositRatioVice> getDepositRatio(String dwzh,String jzny, Date bjsj) {
        String sql = "select result from CCollectionUnitDepositRatioVice result " +
                "where result.dwywmx.dwzh = :dwzh " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '办结' ";
        String sql2 = "and (result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj >= :bjsj " +
                "or (result.dwywmx.cCollectionUnitBusinessDetailsExtension.sxny > :jzny " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj < :bjsj )) ";
        String sql3 = "order by result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj asc ";
        List<CCollectionUnitDepositRatioVice> result = null;
        if(bjsj == null){
            sql = sql + sql3;
            result = getSession().createQuery(sql, CCollectionUnitDepositRatioVice.class)
                    .setParameter("dwzh", dwzh)
                    .list();
        }else{
            sql = sql + sql2 + sql3;
            result = getSession().createQuery(sql, CCollectionUnitDepositRatioVice.class)
                    .setParameter("dwzh", dwzh)
                    .setParameter("jzny", jzny)
                    .setParameter("bjsj", bjsj)
                    .list();

        }
        return result;

    }

    @Override
    public void doFinalUpdatePerson(String ywlsh) {
        String sql = "update  " +
                "st_collection_personal_account a  " +
                "join st_common_person p on p.PersonalAccount = a.id  " +
                "join st_common_unit e on p.unit = e.id  " +
                "join st_collection_unit_account f on f.id = e.CollectionUnitAccount " +
                "join st_collection_unit_business_details d  on d.dwzh = e.dwzh  " +
                "join c_collection_unit_deposit_ratio_vice c on c.dwywmx = d.id  " +
                "set " +
                "a.gryjce = round(a.grjcjs * c.tzhgrbl,2), " +
                "a.dwyjce = round(a.grjcjs * c.tzhdwbl,2)  " +
                "where d.ywlsh = :ywlsh";
        int count = getSession().createSQLQuery(sql)
                .setParameter("ywlsh", ywlsh)
                .executeUpdate();
    }

}
