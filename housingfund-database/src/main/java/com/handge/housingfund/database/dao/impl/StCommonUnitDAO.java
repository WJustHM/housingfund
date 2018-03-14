package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.StCommonUnit;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Repository
public class StCommonUnitDAO extends BaseDAO<StCommonUnit> implements IStCommonUnitDAO {

    @Override
    public StCommonUnit getUnit(String dwzh) {
        List<StCommonUnit> list = getSession().createCriteria(StCommonUnit.class).add(Restrictions.eq("dwzh", dwzh)).list();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 计算单位账户余额
     */
    @Override
    public BigDecimal getUnitAmountCount(String dwzh) {
        String sql = "select sum(result.collectionPersonalAccount.grzhye) from StCommonPerson result " +
                "where result.unit.dwzh = :dwzh ";
        BigDecimal amountCount = getSession().createQuery(sql, BigDecimal.class)
                .setParameter("dwzh", dwzh)
                .uniqueResult();
        return amountCount;
    }

    @Override
    public List<StCommonUnit> getUnitByName(String dwmc) {
        String sql = "select result from StCommonUnit result " +
                "where result.dwmc = :dwmc " +
                "and result.deleted = false ";
        List<StCommonUnit> list = getSession().createQuery(sql, StCommonUnit.class)
                .setParameter("dwmc", dwmc)
                .list();

        return list;
    }

    @Override
    public Object[] getUnitSumBalances(String khwd, String dwzh, String dwmc, String jzny, String sfwft) {
        String sql = "SELECT sum(scua.DWZHYE),sum(ccuae.ZSYE) FROM st_collection_unit_account scua " +
                "INNER JOIN c_collection_unit_account_extension ccuae ON ccuae.id = scua.extenstion " +
                "inner join st_common_unit scu on scu.DWZH = scua.DWZH " +
                "inner join c_common_unit_extension ccue on ccue.id = scu.extension " +
                "inner join c_account_network can on can.id = ccue.KHWD " +
                "where scua.deleted = 0 ";

        HashMap<String,Object> params = new HashMap<String,Object>();
        if (StringUtil.notEmpty(khwd)) {
            sql += " and ccue.KHWD = :khwd";
            params.put("khwd",khwd);
        }
        if (StringUtil.notEmpty(dwzh)) {
            sql += " and scu.DWZH like :dwzh";
            params.put("dwzh","%" + dwzh + "%");
        }
        if (StringUtil.notEmpty(dwmc)) {
            sql += " and scu.DWMC like :dwmc";
            params.put("dwmc","%" + dwmc + "%");
        }
        if (StringUtil.notEmpty(jzny)) {
            String jznyNew = jzny.replace("-","");
            sql += " and scua.JZNY = :jzny";
            params.put("jzny",jznyNew);
        }
        if (StringUtil.notEmpty(sfwft) && !"00".equals(sfwft)) {
            if ("01".equals(sfwft)) {
                sql += " and ccuae.ZSYE > 0";
            } else if ("02".equals(sfwft)) {
                sql += " and ccuae.ZSYE = 0";
            }
        }
        Object[] result = this.get(sql,params);
        return result;
    }

    @Override
    public List<Object[]> getSMSPayCall(String month) {
        String sql = "select a.jbrxm,a.dwzh,b.jzny,a.jbrsjhm " +
                "from st_common_unit a " +
                "join st_collection_unit_account b on a.CollectionUnitAccount = b.id " +
                "where " +
                "a.jbrsjhm != '13800000000' " +
                "and b.jzny < :month " +
                "and a.jbrsjhm is not null";
        List<Object[]> list= (List<Object[]>)getSession().createSQLQuery(sql)
                .setParameter("month",month)
                .list();
        return list;
    }
}
