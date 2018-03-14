package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionUnitPaybackViceDAO;
import com.handge.housingfund.database.entities.CCollectionUnitPaybackVice;
import com.handge.housingfund.database.entities.StCommonPerson;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CCollectionUnitPaybackViceDAO extends BaseDAO<CCollectionUnitPaybackVice>
        implements ICCollectionUnitPaybackViceDAO {

    @Override
    public CCollectionUnitPaybackVice getByYwlsh(String ywlsh) {
        String sql = "select result from CCollectionUnitPaybackVice result where result.dwywmx.ywlsh = :ywlsh ";
        CCollectionUnitPaybackVice result = getSession().createQuery(sql, CCollectionUnitPaybackVice.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    /**
     * 根据单位账号、汇补缴年月、清册受理时间，得到清册生成后的人员信息，gjjschjny（公积金首次汇缴年月）小于等于汇补缴年月
     * 以此生成补缴数据
     */
    public List<StCommonPerson> getIndiAcctSetAfterSlsj(String dwzh, Date hbjnyDate, Date slsj) {
        String sql ="select result from  StCommonPerson result, " +
                        "CCollectionIndividualAccountBasicVice acctSet join acctSet.grywmx.cCollectionPersonalBusinessDetailsExtension extension " +
                        "where result.grzh = acctSet.grzh and acctSet.dwzh = :dwzh and acctSet.gjjschjny <= :hbjny " +
                        "and extension.step ='已入账分摊' and extension.czmc = '03' and extension.slsj > :slsj ";
        List<StCommonPerson> result = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("dwzh",dwzh)
                .setParameter("hbjny",hbjnyDate)
                .setParameter("slsj",slsj)
                .list();
        return result;
    }

    /**
     * 查看员工在该单位该月是否已存在补缴(指定单位)
     */
    @Override
    public boolean isExistPayBack(String dwzh, String grzh, String hbjny) {
        String sql = "select result from StCommonPerson result, " +
                        "CCollectionUnitPaybackVice payback " +
                        "join payback.bjmx bjmx " +
                        "join payback.dwywmx dwywmx " +
                        "join dwywmx.cCollectionUnitBusinessDetailsExtension extension " +
                    "where result.grzh = bjmx.grzh " +
                        "and bjmx.grzh =:grzh " +
                        "and dwywmx.dwzh = :dwzh " +
                        "and dwywmx.hbjny = :hbjny " +
                        "and extension.step = '已入账分摊'";
        StCommonPerson person = getSession().createQuery(sql, StCommonPerson.class)
                .setParameter("dwzh",dwzh)
                .setParameter("hbjny",hbjny)
                .setParameter("grzh",grzh)
                .uniqueResult();
        return null != person;
    }

    /**
     * 查看员工该月是否已存在补缴（任意单位）
     *  //TODO 如果在其他单位下存在未已入账分摊的补缴，如何处理
     *  这里限制：只要没删除，只要存在无论是否已入账分摊，就不会产生第二份补缴数据
     */
    @Override
    public boolean isExistPayBack(String grzh, String hbjny) {
        String sql = "select result from CCollectionUnitPaybackVice result inner join result.bjmx bjmx " +
                "where result.dwywmx.hbjny = :hbjny " +
                "and result.dwywmx.deleted = false " +
                "and bjmx.grzh = :grzh";
        List<CCollectionUnitPaybackVice> list = getSession().createQuery(sql, CCollectionUnitPaybackVice.class)
                .setParameter("grzh", grzh)
                .setParameter("hbjny", hbjny)
                .list();
        if(list != null && list.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistDoing(String dwzh) {
        String sql = "select result from CCollectionUnitPaybackVice result " +
                "where result.dwywmx.cCollectionUnitBusinessDetailsExtension.step not in ('初始状态','待确认','已入账分摊') " +
                "and result.dwywmx.deleted = false " +
                "and result.dwywmx.dwzh = :dwzh ";
        List<CCollectionUnitPaybackVice> list = getSession().createQuery(sql, CCollectionUnitPaybackVice.class)
                .setParameter("dwzh", dwzh)
                .list();
        if(list != null && list.size() > 0){
            return true;
        }
        return false;


    }

    /**
     * 查询是否存在
     */
    @Override
    public CCollectionUnitPaybackVice getPaybackNew(String dwzh, String hbjny) {
        String sql = "select result from CCollectionUnitPaybackVice result " +
                "where result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '待确认' " +
                "and result.dwywmx.deleted = false " +
                "and result.dwywmx.dwzh = :dwzh and result.dwywmx.hbjny = :hbjny ";
        CCollectionUnitPaybackVice result = getSession().createQuery(sql, CCollectionUnitPaybackVice.class)
                .setParameter("dwzh", dwzh)
                .setParameter("hbjny", hbjny)
                .uniqueResult();
        return result;
    }

    @Override
    public boolean isAlreadyExistPayBack(String grzh, String hbjny) {
        String sql = "select result from CCollectionUnitPaybackVice result inner join result.bjmx bjmx " +
                "where bjmx.grzh = :grzh " +
                "and result.dwywmx.hbjny = :hbjny " +
                "and result.dwywmx.deleted = false " +
                "and result.dwywmx.cCollectionUnitBusinessDetailsExtension.step = '已入账分摊' ";
        CCollectionUnitPaybackVice result = getSession().createQuery(sql, CCollectionUnitPaybackVice.class)
                .setParameter("grzh", grzh)
                .setParameter("hbjny", hbjny)
                .uniqueResult();
        return result != null;
    }

    @Override
    public boolean checkIsExistDoing(String grzh, String dwzh) {
        String sql = "select a.id from c_collection_unit_payback_detail_vice a " +
                "join c_collection_unit_payback_vice b on a.BJMX = b.id " +
                "join st_collection_unit_business_details c on b.dwywmx = c.id " +
                "join c_collection_unit_business_details_extension d on c.extenstion = d.id " +
                "where a.grzh = :grzh " +
                "and d.step in ('待入账','入账失败','新建','待确认') " +
                "and c.deleted = 0 " +
                "and c.dwzh = :dwzh " ;
        List list = getSession().createSQLQuery(sql)
                .setParameter("grzh", grzh)
                .setParameter("dwzh", dwzh)
                .list();

        return list.size() > 0;
    }
}
