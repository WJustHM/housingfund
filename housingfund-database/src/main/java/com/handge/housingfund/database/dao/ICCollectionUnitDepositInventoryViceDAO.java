package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.deposit.AutoRemittanceInventoryResQCXX;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryBatchVice;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryDetailVice;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryVice;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ICCollectionUnitDepositInventoryViceDAO extends IBaseDAO<CCollectionUnitDepositInventoryVice> {

    CCollectionUnitDepositInventoryVice getUnitInventoryMsg(String dwzh, String qcny);

    CCollectionUnitDepositInventoryVice getUnitInventoryMsg(String qcqrdh);

    CCollectionUnitDepositInventoryVice getNearLyInventory(String dwzh);

    boolean isExistInventory(String grzh, String qcny);

    CCollectionUnitDepositInventoryVice getByYwlsh(String ywlsh);

    /**
     * 查看清册类业务（开户、启封、封存）是否存在在途业务
     */
    List<StCollectionPersonalBusinessDetails> checkInventoryIsDoing(String dwzh);

    ArrayList<AutoRemittanceInventoryResQCXX> getFirstInventoryOfUnit(String dwzh);

    ArrayList<AutoRemittanceInventoryResQCXX> getNearLyInventory2(String ywlsh);

    ArrayList<AutoRemittanceInventoryResQCXX> getPersonAccSetAfterInventory(String dwzh, Date bjsj);

    List<Object[]> getSealBus(String dwzh, String qcny);

    void saveInventory(CCollectionUnitDepositInventoryVice saveMsg);


    ArrayList<InventoryDetail> getFirstInventoryOfUnit3(String dwzh);

    ArrayList<InventoryDetail> getNearLyInventory3(String ywlsh);

    ArrayList<InventoryDetail> getPersonAccSetOf(String dwzh, String qcyf, Date bjsj);

    List<Object[]> getSealBus3(String dwzh, String qcny);

    List<Object[]> getSealAfter(String dwzh, String qcny, Date bjsj);

    CCollectionUnitDepositInventoryBatchVice getBatchInventory(String ywpch);

    /**
     * 单位首次汇缴时，获取开户的人员信息
     */
    ArrayList<InventoryDetail> getSchjqc(String dwzh, String qcny);

    /**
     * 获取上一次清册办结时间之前，个人开户首次汇缴时间为当前清册月份的人
     */
    ArrayList<InventoryDetail> getPersonAccSetBeforeBJSJ(String dwzh, String nearLyqcny,String qcyfStr, Date bjsj);

    /**
     * 获取上一次清册办结时间之后，小于等于当前清册月份的人
     */
    ArrayList<InventoryDetail> getPersonAccSetAfterBJSJ(String dwzh, String qcyfStr, Date bjsj);

    ArrayList<InventoryDetail> getTransferInBus(String dwzh,String qcyf, Date bjsj);

    CCollectionUnitDepositInventoryVice getInventory(String dwzh, String qcyf);

    void saveBatch(CCollectionUnitDepositInventoryBatchVice inventoryBatch);

    List<CCollectionUnitDepositInventoryDetailVice> getQcxq(CCollectionUnitDepositInventoryVice inventory);

    ArrayList<InventoryDetail> getListByDwzhNormalDeposit3(String dwzh, String dqyf);

    ArrayList<InventoryDetail> getInventoryDetailList(String id);

    List<Object[]> getTransfer(String dwzh, String qcyfStr, Date bjsj);

    List<Object[]> getTransfer2(String dwzh, String qcyfStr, Date bjsj,String jzny);

    List<Object[]> getTransferAndSeal(String dwzh, String qcyfStr, Date bjsj, String jzny);
}
