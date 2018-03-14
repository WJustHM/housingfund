package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.deposit.AutoRemittanceInventoryResQCXX;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICCollectionUnitDepositInventoryViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryBatchVice;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryDetailVice;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryVice;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CCollectionUnitDepositInventoryViceDAO extends BaseDAO<CCollectionUnitDepositInventoryVice>
        implements ICCollectionUnitDepositInventoryViceDAO {

    @Override
    public CCollectionUnitDepositInventoryVice getUnitInventoryMsg(String dwzh, String qcny) {
        String sql = "select result from CCollectionUnitDepositInventoryVice result where result.dwywmx.dwzh = :dwzh and result.qcny = :qcny ";
        List<CCollectionUnitDepositInventoryVice> list = getSession().createQuery(sql, CCollectionUnitDepositInventoryVice.class)
                .setParameter("dwzh", dwzh)
                .setParameter("qcny", qcny)
                .list();
        if(list !=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public CCollectionUnitDepositInventoryVice getUnitInventoryMsg(String qcqrdh) {
        String sql = "select result from CCollectionUnitDepositInventoryVice result where result.qcqrdh = :qcqrdh";
        CCollectionUnitDepositInventoryVice result = (CCollectionUnitDepositInventoryVice) getSession().createQuery(sql)
                .setParameter("qcqrdh",qcqrdh)
                .uniqueResult();
        return result;
    }

    /**
     *取到单位最近的一次清册记录
     */
    @Override
    public CCollectionUnitDepositInventoryVice getNearLyInventory(String dwzh) {
        String sql = "select result from CCollectionUnitDepositInventoryVice result " +
                        "where result.dwywmx.dwzh = :dwzh " +   //and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '已入账分摊'
                        "order by result.qcny desc";
        List<CCollectionUnitDepositInventoryVice> result = getSession().createQuery(sql, CCollectionUnitDepositInventoryVice.class)
                .setParameter("dwzh", dwzh)
                .list();
        if(result != null && result.size() > 0){
            Collections.sort(result);
            return result.get(result.size()-1);
        }
        return null;
    }

    @Override
    public boolean isExistInventory(String grzh, String qcny) {
        String sql = "select result from CCollectionUnitDepositInventoryDetailVice result " +
                "where result.grzh = :grzh " +
                "and result.inventory.qcny = :qcny " +
                "and result.grzhzt = '01' " ;
        List<CCollectionUnitDepositInventoryDetailVice> result = getSession().createQuery(sql, CCollectionUnitDepositInventoryDetailVice.class)
                .setParameter("grzh", grzh)
                .setParameter("qcny", qcny)
                .list();
        if(result == null || result.size() == 0){
            return false;
        }else if(result.size() == 1){
            return true;
        }else{
            throw new ErrorException("职工："+grzh+"，在"+qcny+"存在"+result.size()+"条缴存清册信息，需对数据进行处理！");
        }
    }

    @Override
    public CCollectionUnitDepositInventoryVice getByYwlsh(String ywlsh) {
        String sql = "select result from CCollectionUnitDepositInventoryVice result where result.dwywmx.ywlsh = :ywlsh";
        CCollectionUnitDepositInventoryVice result = getSession().createQuery(sql,CCollectionUnitDepositInventoryVice.class)
                .setParameter("ywlsh",ywlsh)
                .uniqueResult();
        return result;
    }

    /**
     * 查看清册类业务（开户、启封、封存）是否存在在途业务
     */
    @Override
    public List<StCollectionPersonalBusinessDetails> checkInventoryIsDoing(String dwzh) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.unit.dwzh = :dwzh " +
                "and result.deleted = false " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step <> '办结' " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc in ('03','04','05') ";
        List<StCollectionPersonalBusinessDetails> result = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("dwzh", dwzh)
                .list();
        return result;
    }

    /**
     * 查询单位和个人的首次汇缴年月相同列表
     */
    @Override
    public ArrayList<AutoRemittanceInventoryResQCXX> getFirstInventoryOfUnit(String dwzh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "StCommonUnit unit inner join unit.cCommonUnitExtension commonUnitExtension " +
                "where " +
                "unit.dwzh = :dwzh " +
                "and commonUnitExtension.dwschjny = person.cCommonPersonExtension.gjjschjny " +
                "and person.unit.id = unit.id ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<AutoRemittanceInventoryResQCXX> list = new ArrayList<AutoRemittanceInventoryResQCXX>();
        for(Object[] obj: result){
            AutoRemittanceInventoryResQCXX inventoryDetail = new AutoRemittanceInventoryResQCXX(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public ArrayList<AutoRemittanceInventoryResQCXX> getNearLyInventory2(String ywlsh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionUnitDepositInventoryVice inventory inner join inventory.qcxq inventoryDetail " +
                //CCollectionUnitDepositInventoryDetailVice inventoryDetail2  +
                "where " +
                "inventory.dwywmx.ywlsh = :ywlsh " +
                //and inventoryDetail.id = inventoryDetail2.id +
                "and inventoryDetail.grzh = person.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("ywlsh", ywlsh)
                .list();
        ArrayList<AutoRemittanceInventoryResQCXX> list = new ArrayList<AutoRemittanceInventoryResQCXX>();
        for(Object[] obj: result){
            AutoRemittanceInventoryResQCXX inventoryDetail = new AutoRemittanceInventoryResQCXX(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public ArrayList<AutoRemittanceInventoryResQCXX> getPersonAccSetAfterInventory(String dwzh, Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountBasicVice accSet " +
                "where " +
                "accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and accSet.dwzh = :dwzh " +
                "and accSet.grywmx.gjhtqywlx = '03' " +
                "and accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj " +
                "and person.grzh = accSet.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("bjsj", bjsj)
                .list();
        ArrayList<AutoRemittanceInventoryResQCXX> list = new ArrayList<AutoRemittanceInventoryResQCXX>();
        for(Object[] obj: result){
            AutoRemittanceInventoryResQCXX inventoryDetail = new AutoRemittanceInventoryResQCXX(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public List<Object[]> getSealBus(String dwzh, String qcny) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce,seal.grywmx.gjhtqywlx " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountActionVice seal " +
                "where " +
                "person.grzh = seal.grzh " +
                "and seal.dwzh = :dwzh and seal.sxny = :qcny " +
                "and seal.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "order by seal.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";

        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("qcny", qcny)
                .list();
        return result;
    }

    /**
     * 清册业务保存
     * 1、清册业务主体保存
     * 2、清册业务详情批量保存
     */
    @Override
    public void saveInventory(CCollectionUnitDepositInventoryVice saveMsg) {
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = saveMsg.getQcxq();
        saveMsg.setQcxq(new HashSet<>());
        //保存主体信息
        getSession().save(saveMsg);
        getSession().flush();
        getSession().refresh(saveMsg);
        String ywlsh = saveMsg.getDwywmx().getYwlsh();
        saveMsg.setQcqrdh(ywlsh);
        //清册业务批次号，取该批次的第一个月业务流水号
        CCollectionUnitDepositInventoryBatchVice qcpc = saveMsg.getQcpc();
        if(StringUtil.isEmpty(qcpc.getYwpch())){
            qcpc.setYwpch(ywlsh);
            save(qcpc);
        }
        getSession().save(saveMsg);

        int i = 0;
        for(CCollectionUnitDepositInventoryDetailVice detail: qcxq){
            detail.setQcqrdh(ywlsh);
            detail.setInventory(saveMsg);
            //保存详情信息
            getSession().save(detail);
            if(++i % 30 == 0){
                getSession().flush();
                //getSession().clear();
            }
        }
        //更新详情的清册确认单号
        getSession().flush();
        //getSession().clear();
    }

    @Override
    public ArrayList<InventoryDetail> getFirstInventoryOfUnit3(String dwzh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce, personalAccount.grzhzt ,personalAccount.grzhye " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "StCommonUnit unit inner join unit.cCommonUnitExtension commonUnitExtension " +
                "where " +
                "unit.dwzh = :dwzh " +
                "and commonUnitExtension.dwschjny = person.cCommonPersonExtension.gjjschjny " +
                "and person.unit.id = unit.id ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public ArrayList<InventoryDetail> getNearLyInventory3(String ywlsh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,inventoryDetail.grzhzt ,personalAccount.grzhye " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionUnitDepositInventoryVice inventory inner join inventory.qcxq inventoryDetail " +
                "where " +
                "inventory.dwywmx.ywlsh = :ywlsh " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and inventoryDetail.grzh = person.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("ywlsh", ywlsh)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;

    }

    /**
     * 找到比清册月份
     */
    @Override
    public ArrayList<InventoryDetail> getPersonAccSetOf(String dwzh, String qcyf, Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce , '01' , 0 " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountBasicVice accSet " +
                "where " +
                "accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and accSet.dwzh = :dwzh " +
                "and accSet.grywmx.gjhtqywlx = '03' " +
                "and accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj " +
                "and person.cCommonPersonExtension.gjjschjny+0 <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = accSet.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("qcyf", Integer.parseInt(qcyf))
                .setParameter("bjsj", bjsj)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public List<Object[]> getSealBus3(String dwzh, String qcny) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,personalAccount.grzhzt ,personalAccount.grzhye , " +
                "seal.grywmx.gjhtqywlx , seal.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountActionVice seal " +
                "where " +
                "person.grzh = seal.grzh " +
                "and seal.dwzh = :dwzh " +
                "and seal.sxny+0 <= :qcny " +
                "and seal.czmc in ('04','05') " +
                "and seal.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "order by seal.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";

        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("qcny", Integer.parseInt(qcny))
                .list();
        return result;

    }

    @Override
    public List<Object[]> getSealAfter(String dwzh, String qcny , Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,personalAccount.grzhzt ,personalAccount.grzhye ,seal.grywmx.gjhtqywlx " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountActionVice seal " +
                "where " +
                "person.grzh = seal.grzh " +
                "and seal.dwzh = :dwzh " +
                "and seal.sxny+0 <= :qcny " +
                "and seal.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and seal.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj " +
                "order by seal.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";

        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("qcny", Integer.parseInt(qcny))
                .setParameter("bjsj", bjsj)
                .list();
        return result;
    }

    @Override
    public CCollectionUnitDepositInventoryBatchVice getBatchInventory(String ywpch) {
        String sql = "select result from CCollectionUnitDepositInventoryBatchVice result " +
                "where result.ywpch = :ywpch " +
                "and result.deleted = false ";
        CCollectionUnitDepositInventoryBatchVice result = getSession().createQuery(sql, CCollectionUnitDepositInventoryBatchVice.class)
                .setParameter("ywpch", ywpch)
                .uniqueResult();
        return result;
    }

    @Override
    public ArrayList<InventoryDetail> getSchjqc(String dwzh, String qcny) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,'01' ,personalAccount.grzhye " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountBasicVice accSet " +
                "where " +
                "accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and accSet.dwzh = :dwzh " +
                "and accSet.grywmx.gjhtqywlx = '03' " +
                "and person.cCommonPersonExtension.gjjschjny+0 <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = accSet.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("qcyf", Integer.parseInt(qcny))
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }


    @Override
    public ArrayList<InventoryDetail> getPersonAccSetBeforeBJSJ(String dwzh, String nearLyqcny,String qcyfStr, Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,'01' ,0 " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountBasicVice accSet " +
                "where " +
                "accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and accSet.dwzh = :dwzh " +
                "and accSet.grywmx.gjhtqywlx = '03' " +
                "and accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj < :bjsj " +
                "and person.cCommonPersonExtension.gjjschjny  <= :qcyf " +
                "and person.cCommonPersonExtension.gjjschjny > :nearLyqcny " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = accSet.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("nearLyqcny",nearLyqcny)
                .setParameter("qcyf", qcyfStr)
                .setParameter("bjsj",bjsj)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public ArrayList<InventoryDetail> getPersonAccSetAfterBJSJ(String dwzh, String qcyfStr, Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,'01' , 0 " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountBasicVice accSet " +
                "where " +
                "accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and accSet.dwzh = :dwzh " +
                "and accSet.grywmx.gjhtqywlx = '03' " +
                "and accSet.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj >= :bjsj " +
                "and person.cCommonPersonExtension.gjjschjny+0  <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = accSet.grzh ";
        List<Object[]> result = getSession().createQuery(sql, Object[].class)
                .setParameter("qcyf", Integer.parseInt(qcyfStr))
                .setParameter("bjsj",bjsj)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public ArrayList<InventoryDetail> getTransferInBus(String dwzh, String qcyf, Date bjsj) {
        String sql = "select distinct person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,personalAccount.grzhzt ,personalAccount.grzhye " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountTransferNewVice transfer " +
                "where " +
                "transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and transfer.zrdw.dwzh = :dwzh " +
                "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '06' ";
        String sql2 = "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj ";
        String sql3 = "and person.cCommonPersonExtension.gjjschjny+0  <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = transfer.grzh ";
        List<Object[]> result = null;
        if(bjsj != null){
            sql = sql + sql2 + sql3;
            result = getSession().createQuery(sql, Object[].class)
                    .setParameter("qcyf", Integer.parseInt(qcyf))
                    .setParameter("bjsj",bjsj)
                    .setParameter("dwzh", dwzh)
                    .list();
        }else{
            sql = sql + sql3;
            result = getSession().createQuery(sql, Object[].class)
                    .setParameter("qcyf", Integer.parseInt(qcyf))
                    .setParameter("dwzh", dwzh)
                    .list();
        }
        ArrayList<InventoryDetail> list = new ArrayList<InventoryDetail>();
        for(Object[] obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            list.add(inventoryDetail);
        }
        return list;
    }

    @Override
    public CCollectionUnitDepositInventoryVice getInventory(String dwzh, String qcyf) {
        String sql = "select result from CCollectionUnitDepositInventoryVice result " +
                "where result.dwywmx.dwzh = :dwzh " +
                "and result.qcny = :qcny";
        CCollectionUnitDepositInventoryVice result = getSession().createQuery(sql, CCollectionUnitDepositInventoryVice.class)
                .setParameter("dwzh", dwzh)
                .setParameter("qcny", qcyf)
                .uniqueResult();
        return result;
    }

    @Override
    public void saveBatch(CCollectionUnitDepositInventoryBatchVice inventoryBatch) {
        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();
        List<CCollectionUnitDepositInventoryVice> list = new ArrayList<>(qclb);
        Collections.sort(list);
        for(CCollectionUnitDepositInventoryVice  inventory : list){
            this.saveInventory(inventory);
        }
    }

    @Override
    public List<CCollectionUnitDepositInventoryDetailVice> getQcxq(CCollectionUnitDepositInventoryVice inventory) {
        String sql = "select result from CCollectionUnitDepositInventoryDetailVice result " +
                "where result.inventory = :inventory ";
        List<CCollectionUnitDepositInventoryDetailVice> list = getSession().createQuery(sql, CCollectionUnitDepositInventoryDetailVice.class)
                .setParameter("inventory", inventory)
                .list();
        return list;
    }

    /**
     * 排除个人账户状态不存在
     */
    @Override
    public ArrayList<InventoryDetail> getListByDwzhNormalDeposit3(String dwzh,String dqyf) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,personalAccount.grzhzt ,personalAccount.grzhye " +
                "from StCommonPerson person " +
                "inner join person.collectionPersonalAccount personalAccount " +
                "where person.unit.dwzh = :dwzh "+
                "and  person.deleted = false  " +
                "and ( person.collectionPersonalAccount.grzhzt = '01' " +
                "or  person.collectionPersonalAccount.grzhzt = '02') " +
                "and person.cCommonPersonExtension.gjjschjny <= :dqyf ";   //gjjschjny经检测没有为空的数据
        List<Object[]> result = getSession().createQuery(sql,Object[].class)
                .setParameter("dwzh", dwzh)
                .setParameter("dqyf", dqyf)
                .list();
        ArrayList<InventoryDetail> result2 = new ArrayList<>();
        for(Object[]  obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            result2.add(inventoryDetail);
        }

        return result2;
    }

    @Override
    public ArrayList<InventoryDetail> getInventoryDetailList(String id) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,inventoryDetail.grjcjs , " +
                "inventoryDetail.dwyjce ,inventoryDetail.gryjce ,inventoryDetail.grzhzt ,inventoryDetail.grzhye " +
                "from " +
                "StCommonPerson person , " +
                "CCollectionUnitDepositInventoryDetailVice inventoryDetail join inventoryDetail.inventory inventory " +
                "where " +
                "inventory.id = :id " +
                "and inventoryDetail.grzh = person.grzh ";
        List<Object[]> result = getSession().createQuery(sql,Object[].class)
                .setParameter("id", id)
                .list();
        ArrayList<InventoryDetail> result2 = new ArrayList<>();
        for(Object[]  obj: result){
            InventoryDetail inventoryDetail = new InventoryDetail(obj);
            result2.add(inventoryDetail);
        }
        return result2;
    }

    @Override
    public List<Object[]> getTransfer(String dwzh, String qcyf, Date bjsj) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,personalAccount.grzhzt ,personalAccount.grzhye, " +
                "transfer.zrdw.dwzh,transfer.zcdw.dwzh, " +
                "transfer.sxny,transfer.ygrjcjs,transfer.ydwyjce,transfer.ygryjce " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountTransferNewVice transfer " +
                "where " +
                "transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '06' " +
                "and transfer.zrdw.dwzh <> transfer.zcdw.dwzh " +
                "and " +
                "( " +
                "(transfer.zrdw.dwzh = :dwzh " +
                "and person.cCommonPersonExtension.gjjschjny <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = transfer.grzh ) " +
                "or " +
                "transfer.zcdw.dwzh = :dwzh " +
                ") ";
        String sql2 = "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :bjsj ";
        String sql3 = "order by transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";

        sql = bjsj != null ? sql + sql2 + sql3 : sql + sql3;
        Query<Object[]> query = getSession().createQuery(sql, Object[].class)
                .setParameter("qcyf", qcyf)
                .setParameter("dwzh", dwzh);

        query = bjsj != null ?query.setParameter("bjsj",bjsj) : query;

        List<Object[]> list = query.list();
        return list;
    }

    public List<Object[]> getTransfer2(String dwzh, String qcyf, Date bjsj,String jzny) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce ,'01' ,personalAccount.grzhye, " +
                "'01',transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj " +
                "from " +
                "StCommonPerson person inner join person.collectionPersonalAccount personalAccount, " +
                "CCollectionIndividualAccountTransferNewVice transfer " +
                "where " +
                "transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and transfer.zrdw.dwzh = :dwzh " +
                "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '06' " +
                "and person.cCommonPersonExtension.gjjschjny <= :qcyf " +
                "and (personalAccount.grzhzt = '01' or personalAccount.grzhzt = '02') " +
                "and person.grzh = transfer.grzh ";
        String sql2 = "and(transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj >= :bjsj " +
                "or (transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj < :bjsj  " +
                "and person.cCommonPersonExtension.gjjschjny > :jzny))";
        String sql3 = "order by transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";
        sql = bjsj != null ? sql + sql2 + sql3  : sql + sql3;
        Query<Object[]> query = getSession().createQuery(sql, Object[].class)
                .setParameter("qcyf", qcyf)
                .setParameter("dwzh", dwzh);
        query = bjsj != null ?query.setParameter("bjsj",bjsj).setParameter("jzny",jzny) : query;

        List<Object[]> list1 = query.list();

        String sql4 = "select transfer.grzh,transfer.sxny,'',transfer.ygrjcjs , " +
                "transfer.ydwyjce,transfer.ygryjce,'',0, " +
                "'02',transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj " +
                "from CCollectionIndividualAccountTransferNewVice transfer " +
                "where transfer.zcdw.dwzh = :dwzh " +
                "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '06' " +
                "and transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' ";
        String sql5 = "and(transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj >= :bjsj " +
                "or (transfer.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj < :bjsj " +
                "and transfer.sxny > :jzny))";

        sql4 = bjsj != null ? sql4 + sql5 +sql3 : sql4 +sql3;
        Query<Object[]> query2 = getSession().createQuery(sql4, Object[].class)
                .setParameter("dwzh", dwzh);
        query2 = bjsj != null ? query2.setParameter("bjsj",bjsj).setParameter("jzny",jzny) : query2;

        List<Object[]> list2 = query2.list();

        //根据办结时间排序
        list1 = sortByBJSJ(list1,list2);

        return list1;
    }

    private List<Object[]> sortByBJSJ(List<Object[]> list1,List<Object[]> list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        List<Object[]> result = new ArrayList(size1 + size2);
        int i=0,j=0;
        while(i< size1 || j< size2){
            //边界验证
            if(i== size1){
                result.addAll(list2.subList(j, size2));
                break;
            }else if(j == size2){
                result.addAll(list1.subList(i, size1));
                break;
            }
            //
            Date date1 = (Date)list1.get(i)[9];
            Date date2 = (Date)list2.get(j)[9];
            if(date1.compareTo(date2) <= 0){
                result.add(list1.get(i++));
            }else{
                result.add(list2.get(j++));
            }
        }
        return result;
    }

    @Override
    public List<Object[]> getTransferAndSeal(String dwzh, String qcyf, Date bjsj,String jzny) {

        List<Object[]> transfer = getTransfer2(dwzh, qcyf, bjsj, jzny);

        List<Object[]> sealBus = getSealBus3(dwzh, qcyf);

        List<Object[]> object = sortByBJSJ(transfer, sealBus);

        return object;
    }
}
