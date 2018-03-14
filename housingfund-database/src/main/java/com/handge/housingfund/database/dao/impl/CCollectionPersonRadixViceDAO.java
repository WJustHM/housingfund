package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.collection.model.deposit.GetPersonRadixBeforeResJCJSTZXX;
import com.handge.housingfund.database.dao.ICCollectionPersonRadixViceDAO;
import com.handge.housingfund.database.entities.CCollectionPersonRadixDetailVice;
import com.handge.housingfund.database.entities.CCollectionPersonRadixVice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CCollectionPersonRadixViceDAO extends BaseDAO<CCollectionPersonRadixVice>
        implements ICCollectionPersonRadixViceDAO {
    @Override
    public List<CCollectionPersonRadixVice> getRadixList(String dwzh, String grzh, Date hbjny) {
        String sql = "select result from CCollectionPersonRadixVice result inner join result.jstzxq jstzxq " +
                "where jstzxq.grzh = :grzh and result.dwywmx.dwzh = :dwzh and result.sxny >= :hbjny " +
                "order by result.sxny asc, result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj asc ";
        List<CCollectionPersonRadixVice> result = getSession().createQuery(sql,CCollectionPersonRadixVice.class)
                .setParameter("dwzh",dwzh)
                .setParameter("grzh",grzh)
                .setParameter("hbjny",hbjny)
                .list();
        return result;
    }

    @Override
    public CCollectionPersonRadixDetailVice getRadixLast(String dwzh, String grzh, String hbjny) {
        String sql = "select result from CCollectionPersonRadixDetailVice result ," +
                " CCollectionPersonRadixVice vice inner join vice.jstzxq jstzxq " +
                "where result.id = jstzxq.id " +
                "and vice.sxny = :hbjny " +
                "and result.grzh = :grzh " +
                "and vice.dwywmx.dwzh = :dwzh " +
                "and vice.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '办结' " +
                "order by vice.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj desc ";
        List<CCollectionPersonRadixDetailVice> result = getSession().createQuery(sql,CCollectionPersonRadixDetailVice.class)
                .setParameter("dwzh",dwzh)
                .setParameter("grzh",grzh)
                .setParameter("hbjny",hbjny)
                .list();
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public CCollectionPersonRadixDetailVice getRadixAfter(String dwzh, String grzh, String hbjny) {
        String sql = "select result from CCollectionPersonRadixDetailVice result ," +
                " CCollectionPersonRadixVice vice inner join vice.jstzxq jstzxq " +
                "where result.id = jstzxq.id " +
                "and vice.sxny+0 > :hbjny " +
                "and result.grzh = :grzh " +
                "and vice.dwywmx.dwzh = :dwzh " +
                "and vice.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '办结' " +
                "order by vice.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj asc ";
        List<CCollectionPersonRadixDetailVice> result = getSession().createQuery(sql,CCollectionPersonRadixDetailVice.class)
                .setParameter("dwzh",dwzh)
                .setParameter("grzh",grzh)
                .setParameter("hbjny",Integer.parseInt(hbjny))
                .list();
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public CCollectionPersonRadixVice getPersonRadix(String ywlsh) {
        String sql = "select result from CCollectionPersonRadixVice result where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionPersonRadixVice result = getSession().createQuery(sql, CCollectionPersonRadixVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    @Override
    public List<CCollectionPersonRadixVice> getPersonRadix(String dwzh,String jzny ,Date bjsj) {
        String sql = "select result from CCollectionPersonRadixVice result " +
                "where result.dwywmx.dwzh = :dwzh " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '办结' " ;
        String sql2 = "and (result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj >= :bjsj " +
                "or (result.dwywmx.cCollectionUnitBusinessDetailsExtension.sxny > :jzny " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj < :bjsj )) ";
        String sql3 = "order by result.dwywmx.cCollectionUnitBusinessDetailsExtension.bjsj asc ";

        List<CCollectionPersonRadixVice> result = null;
        if(bjsj == null){
            sql = sql + sql3;
            result = getSession().createQuery(sql, CCollectionPersonRadixVice.class)
                    .setParameter("dwzh", dwzh)
                    .list();
        }else{
            sql = sql + sql2 + sql3;
            result = getSession().createQuery(sql, CCollectionPersonRadixVice.class)
                    .setParameter("dwzh", dwzh)
                    .setParameter("jzny", jzny)
                    .setParameter("bjsj",bjsj)
                    .list();
        }
        return result;
    }

    /**
     * @param dwzh
     * @return
     */
    @Override
    public ArrayList<GetPersonRadixBeforeResJCJSTZXX> getRadixs(String dwzh) {
        String sql = "select result.grzh,result.xingMing,personalAccount.grjcjs,personalAccount.dwyjce+personalAccount.gryjce,extension.grjzny,result.zjhm,personalAccount.gryjce,personalAccount.dwyjce " +
                "from " +
                "StCommonPerson result " +
                "join result.collectionPersonalAccount personalAccount " +
                "join result.cCommonPersonExtension extension " +
                "where result.unit.dwzh = :dwzh " +
                "and personalAccount.grzhzt in ('01') ";
        List<Object[]> result = getSession().createQuery(sql,Object[].class)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<GetPersonRadixBeforeResJCJSTZXX> list = new ArrayList<>();
        for(Object[] obj: result){
            list.add(new GetPersonRadixBeforeResJCJSTZXX(obj));
        }
        return list;
    }

    @Override
    public List<Object[]> getPersonRadixDetail(String id) {
        String sql = "select b.GRZH,b.XingMing,b.ZJHM,c.GRYJCE+c.DWYJCE,d.GRJZNY,a.TZQGRJS,a.TZHGRJS,c.GRYJCE,c.DWYJCE " +
                "from c_collection_person_radix_detail_vice a " +
                "join st_common_person b on a.grzh = b.grzh " +
                "join st_collection_personal_account c on b.PersonalAccount = c.id " +
                "join c_common_person_extension d on b.extension = d.id " +
                "where a.JSTZXQ = :id ";
        List<Object[]> result = getSession().createSQLQuery(sql)
                .setParameter("id", id)
                .list();
        return result;
    }

    @Override
    public void doFinal(String ywlsh) {
        String sql = "update  " +
                "st_collection_personal_account a  " +
                "join st_common_person p on p.PersonalAccount = a.id  " +
                "join c_collection_person_radix_detail_vice b on a.grzh = b.grzh " +
                "join c_collection_person_radix_vice c on b.jstzxq = c.id  " +
                "join st_collection_unit_business_details d on c.dwywmx = d.id  " +
                "join st_common_unit e on p.Unit = e.id " +
                "join st_collection_unit_account f on f.id = e.CollectionUnitAccount  " +
                "set " +
                "a.grjcjs = b.tzhgrjs, " +
                "a.gryjce = round(b.tzhgrjs * f.grjcbl,2), " +
                "a.dwyjce = round(b.tzhgrjs * f.dwjcbl,2)  " +
                "where d.ywlsh = :ywlsh " +
                "and e.dwzh = d.dwzh " +
                "and  b.tzhgrjs != 0  " +
                "and b.tzhgrjs is not null ";
        int count = getSession().createSQLQuery(sql)
                .setParameter("ywlsh", ywlsh)
                .executeUpdate();
    }

    @Override
    public void updateBJSJ(String ywlsh) {
        String sql = "update st_collection_unit_business_details a " +
                "JOIN  c_collection_unit_business_details_extension b on a.extenstion = b.id " +
                "set b.bjsj = now() " +
                "where ywlsh = :ywlsh ";
        int i = getSession().createSQLQuery(sql)
                .setParameter("ywlsh", ywlsh)
                .executeUpdate();
    }

}
