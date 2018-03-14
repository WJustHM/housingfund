package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitRemittanceViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitRemittanceVice;
import com.handge.housingfund.database.entities.StCommonPerson;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Repository
public class CCollectionUnitRemittanceViceDAO extends BaseDAO<CCollectionUnitRemittanceVice>
        implements ICCollectionUnitRemittanceViceDAO {
    @Override
    public CCollectionUnitRemittanceVice getByYwlsh(String ywlsh) {
        String sql = "select result from CCollectionUnitRemittanceVice result where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionUnitRemittanceVice result = getSession().createQuery(sql, CCollectionUnitRemittanceVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    @Override
    public CCollectionUnitRemittanceVice getByQcqrdh(String qcqrdh) {
        String sql = "select result from CCollectionUnitRemittanceVice result where result.qcqrdh = :qcqrdh ";
        CCollectionUnitRemittanceVice result = getSession().createQuery(sql, CCollectionUnitRemittanceVice.class)
                .setParameter("qcqrdh", qcqrdh)
                .uniqueResult();
        return result;
    }


    @Override
    public CCollectionUnitRemittanceVice getRemittance(String dwzh, String hbjny) {
        String sql = "select result from CCollectionUnitRemittanceVice result where result.dwywmx.dwzh = :dwzh and result.dwywmx.hbjny = :hbjny";
        CCollectionUnitRemittanceVice result = getSession().createQuery(sql,CCollectionUnitRemittanceVice.class)
                .setParameter("dwzh",dwzh)
                .setParameter("hbjny",hbjny)
                .uniqueResult();
        return result;
    }

    @Override
    public boolean checkIsExistDoing(String dwzh) {
        String sql = "select result from StCollectionUnitBusinessDetails result " +
                "where result.cCollectionUnitBusinessDetailsExtension.step <> '已入账分摊' " +
                "and result.cCollectionUnitBusinessDetailsExtension.czmc = '01' " +
                "and result.deleted = false " +
                "and result.dwzh = :dwzh ";
        List list = getSession().createQuery(sql)
                .setParameter("dwzh", dwzh)
                .list();
        return !(list == null || list.size() == 0);
    }

    @Override
    public BigDecimal getComputeDwzhye(String dwzh) {
        String sql = "select sum(result.collectionPersonalAccount.grzhye) from StCommonPerson result " +
                "where result.unit.dwzh = :dwzh " +
                "and result.collectionPersonalAccount.grzhzt in ('01','02','06','99')";
        BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
                .setParameter("dwzh", dwzh)
                .uniqueResult();
        return result;
    }

    /**
     * 获取个人的连续缴存月数,当前只有汇缴(补缴错缴暂不支持)
     */
    @Override
    public List<String> getConsecutiveDepositMonths(String grzh) {
        /*String sql = "select remittance.dwywmx.hbjny from CCollectionUnitRemittanceVice remittance , " +
                "CCollectionUnitDepositInventoryDetailVice inventoryDetail " +
                "where remittance.qcqrdh = inventoryDetail.inventory.qcqrdh " +
                "and inventoryDetail.grzh = :grzh " +
                "and remittance.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '已入账分摊' " +
                "order by remittance.dwywmx.hbjny desc ";*/
        String sql2 ="select result.cCollectionPersonalBusinessDetailsExtension.fsny from StCollectionPersonalBusinessDetails result " +
                "where result.gjhtqywlx = '01' " +
                "and result.grzh = :grzh " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "order by result.cCollectionPersonalBusinessDetailsExtension.fsny desc";
        List<String> hbjnys = getSession().createQuery(sql2, String.class)
                .setParameter("grzh", grzh)
                .list();
        Iterator<String> iterator = hbjnys.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if(next == null || next.length() != 6){
                iterator.remove();
            }
        }

        return hbjnys;
    }

    @Override
    public String getYWLSHIsExistDoing(String dwzh) {
        String sql = "select result from CCollectionUnitRemittanceVice result " +
                "where result.dwywmx.dwzh = :dwzh " +
                "and result.dwywmx.ywmxlx = '01' " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step <> '已入账分摊' " +
                "and result.dwywmx.deleted = false ";
        List<CCollectionUnitRemittanceVice> result = getSession().createQuery(sql, CCollectionUnitRemittanceVice.class)
                .setParameter("dwzh", dwzh)
                .list();
        if(result != null && result.size() >0){
            return result.get(0).getDwywmx().getYwlsh();
        }
        return null;
    }

    @Override
    public void flushAndClear() {
        getSession().flush();
        getSession().clear();
    }



    @Override
    public List<StCommonPerson> getListNoInCurrentUnitOfInventory(String qcqrdh,String dwzh) {
        String sql = "select result from StCommonPerson result ," +
                "CCollectionUnitDepositInventoryDetailVice inventoryDetail " +
                "join inventoryDetail.inventory inventory " +
                "where result.grzh = inventoryDetail.grzh " +
                "and inventory.qcqrdh = :qcqrdh " +
                "and result.unit.dwzh <> :dwzh ";
        List<StCommonPerson> list = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("qcqrdh", qcqrdh)
                .setParameter("dwzh", dwzh)
                .list();
        return list;
    }

    @Override
    public boolean checkIsExistDoing(String grzh,String dwzh) {
        String sql = "select a.id from c_collection_unit_deposit_inventory_detail_vice a " +
                "join c_collection_unit_deposit_inventory_vice b on a.inventory_id = b.id " +
                "join c_collection_unit_remittance_vice c on b.qcqrdh = c.qcqrdh " +
                "join st_collection_unit_business_details d on c.dwywmx = d.id " +
                "join c_collection_unit_business_details_extension e on d.extenstion = e.id " +
                "where a.grzh = :grzh " +
                "and e.step in ('待入账','入账失败','新建','待确认') " +
                "and a.grzhzt = '01' " +
                "and c.deleted = 0 " +
                "and d.dwzh = :dwzh ";
        List list = getSession().createSQLQuery(sql)
                .setParameter("grzh", grzh)
                .setParameter("dwzh", dwzh)
                .list();

        return list.size() > 0;
    }

}
