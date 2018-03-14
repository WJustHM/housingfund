package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.collection.model.deposit.AutoRemittanceInventoryResQCXX;
import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;
import com.handge.housingfund.database.dao.IStCommonPersonDAO;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.StCollectionPersonalAccount;
import com.handge.housingfund.database.entities.StCommonPerson;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StCommonPersonDAO extends BaseDAO<StCommonPerson> implements IStCommonPersonDAO {

    @Override
    public StCommonPerson getByGrzh(String grzh) {
        List<StCommonPerson> list  = getSession().createCriteria(StCommonPerson.class)
                .add(Restrictions.eq("grzh",grzh))
                .add(Restrictions.eq("deleted",false))
                .list();
        if(list.size() == 1){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据单位账号得到单位下正常缴存员工的列表（个人账户状态包括正常01、冻结06）
     * // TODO 对象导航的方式查询效率低下!
     */
    @Override
    public List<StCommonPerson> getListByDwzhNormalDeposit(String dwzh) {
        String sql = "select person from  StCommonPerson person " +
                        "where person.unit.dwzh = :dwzh " +
                        "and  person.deleted = false  " +
                        "and ( person.collectionPersonalAccount.grzhzt = '01' " +
                            "or  person.collectionPersonalAccount.grzhzt = '06') ";
        List<StCommonPerson> result = (List<StCommonPerson>) getSession().createQuery(sql)
                .setParameter("dwzh",dwzh)
                .list();
        return result;
    }

    /**
     *
     */
    @Override
    public ArrayList<AutoRemittanceInventoryResQCXX> getListByDwzhNormalDeposit2(String dwzh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm ,personalAccount.grjcjs , " +
                "personalAccount.dwyjce ,personalAccount.gryjce " +
                "from StCommonPerson person " +
                "inner join person.collectionPersonalAccount personalAccount " +
                "where person.unit.dwzh = :dwzh "+
                "and  person.deleted = false  " +
                "and ( person.collectionPersonalAccount.grzhzt = '01' " +
                "or  person.collectionPersonalAccount.grzhzt = '06') ";

        List<Object[]> result = getSession().createQuery(sql,Object[].class)
                .setParameter("dwzh", dwzh)
                .list();
        ArrayList<AutoRemittanceInventoryResQCXX> result2 = new ArrayList<AutoRemittanceInventoryResQCXX>();
        for(Object[]  obj: result){
            AutoRemittanceInventoryResQCXX inventoryDetail = new AutoRemittanceInventoryResQCXX(obj);
            result2.add(inventoryDetail);
        }
        return result2;
    }

    /**
     * 根据单位账号得到单位下所有员工的列表(个人账户状态除开销户类型)
     */
    @Override
    public List<StCommonPerson> getListByDwzh(String dwzh) {
        String sql = "select result from StCommonPerson result " +
                "where result.unit.dwzh = :dwzh" ;
        List<StCommonPerson> result = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("dwzh", dwzh)
                .list();
        return result;
    }

    /**
     * 根据个人账户状态得到单位下的个人计数
     * 00表示所有,其他为正常的账户状态
     */
    public long getUnitPersonCount(String dwzh, String grzhzt){
        String sql = "select count(result) from StCommonPerson result " +
                "where result.unit.dwzh = :dwzh " +
                "and result.collectionPersonalAccount.grzhzt = :grzhzt";
        Long count = getSession().createQuery(sql, Long.class)
                .setParameter("dwzh", dwzh)
                .setParameter("grzhzt", grzhzt)
                .uniqueResult();
        return count;
    }

    @Override
    public StCommonPerson getPerson(String zjhm, String zjlx) {
        String sql = "select result from StCommonPerson result " +
                "where result.zjlx = :zjlx " +
                "and result.zjhm = :zjhm " +
                "and result.deleted = false ";
        StCommonPerson person = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("zjhm",zjhm)
                .setParameter("zjlx",zjlx)
                .uniqueResult();
        return person;
    }

    @Override
    public List<StCommonPerson> getPerson(String xingMing, String zjhm, String grckzhhm) {
        String sql = "select result from StCommonPerson result " +
                "where result.xingMing = :xingMing " +
                "and result.zjhm = :zjhm " +
                "and result.collectionPersonalAccount.grckzhhm = :grckzhhm " +
                "and result.deleted = false " +
                "and result.collectionPersonalAccount.grzhzt not in ('03','04','05')";
        List<StCommonPerson> list = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("xingMing", xingMing)
                .setParameter("zjhm", zjhm)
                .setParameter("grckzhhm", grckzhhm)
                .list();
        return list;
    }

    @Override
    public void updateNormal(Common person) {
        getSession().update(person);
    }

    /**
     * 得到单位下的个人账户信息列表
     */
    @Override
    public List<StCollectionPersonalAccount> getPersonalAccounts(String dwzh) {
        String sql = "select result from StCollectionPersonalAccount result, " +
                "StCommonPerson person join person.collectionPersonalAccount personAccount " +
                "where person.unit.dwzh = :dwzh and result.id = personAccount.id ";
        List<StCollectionPersonalAccount> list = getSession().createQuery(sql, StCollectionPersonalAccount.class)
                .setParameter("dwzh",dwzh)
                .list();
        return list;
    }

    @Override
    public List<StCollectionPersonalAccount> getPersonsAccountsSorted(String dwzh) {
        String sql = "select result from StCollectionPersonalAccount result, " +
                "StCommonPerson person join person.collectionPersonalAccount personAccount " +
                "where person.unit.dwzh = :dwzh and result.id = personAccount.id ";
        List<StCollectionPersonalAccount> list = getSession().createQuery(sql, StCollectionPersonalAccount.class)
                .setParameter("dwzh",dwzh)
                .list();
        return list;

    }

    @Override
    public List<StCommonPerson> getPersonsAccountsSorted2(String dwzh) {
        String sql = "select result from StCommonPerson result " +
                "join fetch result.collectionPersonalAccount  " +
                "where result.unit.dwzh = :dwzh ";
        List<StCommonPerson> list = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("dwzh",dwzh)
                .list();
        return list;

    }

    /**
     * 根据单位账号得到单位下所有员工的列表(个人账户状态除开销户类型)
     */
    @Override
    public ArrayList<ListEmployeeRes> getEmployeeInfo(String dwzh) {
        String sql = "select person.grzh ,person.xingMing ,person.zjhm,personalAccount.grzhzt ,personalAccount.grjcjs ,personalAccount.gryjce ,personalAccount.dwyjce ,extension.grjzny, personalAccount.grzhye,cl.id from StCommonPerson person " +
                " inner join person.collectionPersonalAccount personalAccount " +
                " inner join person.cCommonPersonExtension extension " +
                " left join  CLoanHousingPersonInformationBasic cl " +
                " on (person.zjhm=cl.jkrzjhm " +
                " and cl.deleted = false " +
                " and cl.loanContract is not null " +
                " and cl.personalAccount is not null " +
                " and cl.dkzhzt not in ('3','8') )" +
                " where person.unit.dwzh = :dwzh" ;
        List<Object[]> result = getSession().createQuery(sql,Object[].class)
                .setParameter("dwzh", dwzh)
                .list();
       ArrayList<ListEmployeeRes> arrayList = new ArrayList();
        for(Object[] obj: result){
            ListEmployeeRes listEmployeeRes1 = new ListEmployeeRes(obj);
            arrayList.add(listEmployeeRes1);
        }
        return arrayList;
    }

    /**
     * 封存后未缴到应缴年月，提取时设置提示
     */
    @Override
    public Object[] getPersonSealMsgForTQ(String grzh) {

        String sql = "select b.grzh,e.grjzny,e.gjjschjny,a.sxny " +
                "from c_collection_individual_account_action_vice a  " +
                "join st_collection_personal_business_details b on a.grywmx = b.id  " +
                "join c_collection_personal_business_details_extension c on b.extension = c.id  " +
                "join st_common_person d on b.grzh = d.grzh  " +
                "join c_common_person_extension e  on  d.extension = e.id  " +
                "where b.grzh = :grzh  " +
                "and c.czmc = '05' " +
                "and c.step = '办结'" +
                "order by c.bjsj desc " +
                "limit 1";

        Object[] result = (Object[])getSession().createSQLQuery(sql)
                .setParameter("grzh", grzh)
                .uniqueResult();

        return result;
    }
}
