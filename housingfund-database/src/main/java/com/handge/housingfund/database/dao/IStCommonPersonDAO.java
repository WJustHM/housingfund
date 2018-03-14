package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.collection.model.deposit.AutoRemittanceInventoryResQCXX;
import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.StCollectionPersonalAccount;
import com.handge.housingfund.database.entities.StCommonPerson;

import java.util.ArrayList;
import java.util.List;

public interface IStCommonPersonDAO extends IBaseDAO<StCommonPerson> {

    StCommonPerson getByGrzh(String grzh);

    /**
     * 根据单位账号得到单位下正常缴存员工的列表（个人账户状态包括正常、冻结）
     * @param dwzh
     * @return
     */
    List<StCommonPerson> getListByDwzhNormalDeposit(String dwzh);

    ArrayList<AutoRemittanceInventoryResQCXX> getListByDwzhNormalDeposit2(String dwzh);

    /**
     * 根据单位账号得到单位下所有员工的列表(个人账户状态除开销户类型)
     * @param dwzh
     * @return
     */
    List<StCommonPerson> getListByDwzh(String dwzh);

    /**
     * 根据个人账户状态查询单位下的人数
     */
    long getUnitPersonCount(String dwzh, String grzhzt);

    /**
     * 根据员工证件号码、证件类型，查询个人信息
     */
    StCommonPerson getPerson(String zjhm,String zjlx);

    /**
     * 根据个人姓名、证件号码、个人存款证件号码查询个人信息
     */
    List<StCommonPerson> getPerson(String xingming, String zjhm, String grckzhhm);

    void updateNormal(Common person);

    List<StCollectionPersonalAccount> getPersonalAccounts(String dwzh);

    List<StCollectionPersonalAccount> getPersonsAccountsSorted(String dwzh);

    List<StCommonPerson> getPersonsAccountsSorted2(String dwzh);

    public ArrayList<ListEmployeeRes> getEmployeeInfo(String dwzh);

    Object[] getPersonSealMsgForTQ(String grzh);

}
