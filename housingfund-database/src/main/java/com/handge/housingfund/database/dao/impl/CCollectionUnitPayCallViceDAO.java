package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitPayCallViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitPayCallDetailVice;
import com.handge.housingfund.database.entities.CCollectionUnitPayCallVice;
import com.handge.housingfund.database.entities.StCommonUnit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CCollectionUnitPayCallViceDAO extends BaseDAO<CCollectionUnitPayCallVice>
        implements ICCollectionUnitPayCallViceDAO {

    @Override
    public List<String> getUnitNeedCall(String nowDate) {
        String sql = "select result.dwzh from StCollectionUnitAccount result where result.jzny+0 < :nowDate and result.dwzhzt = '01' ";
        List<String> units = getSession().createQuery(sql, String.class)
                .setParameter("nowDate", Integer.parseInt(nowDate))
                .list();

        return units;
    }

    @Override
    public CCollectionUnitPayCallVice getNearlyPayCall(String dwzh) {
        String sql = "select result from CCollectionUnitPayCallVice result " +
                "where result.dwywmx.dwzh = :dwzh " +
                "order by result.yjny desc ";
        List<CCollectionUnitPayCallVice> result = getSession().createQuery(sql, CCollectionUnitPayCallVice.class)
                .setParameter("dwzh", dwzh)
                .list();
        if(result != null && result.size()>0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public CCollectionUnitPayCallVice getPayCall(String ywlsh) {
        String sql = "select result from CCollectionUnitPayCallVice result where result.dwywmx.ywlsh = :ywlsh";
        CCollectionUnitPayCallVice result = getSession().createQuery(sql, CCollectionUnitPayCallVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    @Override
    public List<CCollectionUnitPayCallDetailVice> getPayCallDetail(String ywlsh) {
        String sql = "select result from CCollectionUnitPayCallDetailVice result , " +
                "CCollectionUnitPayCallVice paycall inner join paycall.cjxq cjxq " +
                "where result.id = cjxq.id " +
                "and paycall.dwywmx.ywlsh = :ywlsh " +
                "order by result.created_at desc ";
        List<CCollectionUnitPayCallDetailVice> list = getSession().createQuery(sql, CCollectionUnitPayCallDetailVice.class)
                .setParameter("ywlsh", ywlsh)
                .list();
        return list;
    }

    @Override
    public Object[] getDeposit(StCommonUnit unit) {
        String sql = "select count(person.id), sum(account.gryjce) + sum(account.dwyjce) " +
                "from StCommonPerson person " +
                "join person.collectionPersonalAccount account " +
                "where person.unit = :unit " +
                "and account.grzhzt = '01' ";
        Object[] result = getSession().createQuery(sql, Object[].class)
                .setParameter("unit", unit)
                .uniqueResult();
        return result;
    }

    @Override
    public List<StCommonUnit> getUnitNeedCall2(String nowDate) {
        String sql = "select result from StCommonUnit result " +
                "join fetch result.collectionUnitAccount account " +
                "where account.jzny < :nowDate " +
                "and account.dwzhzt = '01' " +
                "order by result.dwzh asc ";
        List<StCommonUnit> units = getSession().createQuery(sql, StCommonUnit.class)
                .setParameter("nowDate", nowDate)
                .list();
        //units = units.subList(100,120);
        return units;
    }

    public List<Object[]> getPayCallMag(){
        String sql = "select a.dwzh ,MAX(c.yjny) " +
                "from st_common_unit a " +
                "LEFT JOIN st_collection_unit_business_details b on b.Unit = a.id " +
                "LEFT JOIN c_collection_unit_paycall_vice c on c.dwywmx = b.id " +
                "GROUP BY a.dwzh " +
                "order by a.dwzh asc;";
        List<Object[]> list = getSession().createSQLQuery(sql)
                .list();
        return list;
    }

    @Override
    public void saveNomal(CCollectionUnitPayCallVice payCall) {
        getSession().save(payCall);
    }

    @Override
    public void flush() {
        getSession().flush();
    }
}
