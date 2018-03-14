package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICBankCenterInfoDAO;
import com.handge.housingfund.database.entities.CBankCenterInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gxy on 17-12-7.
 */
@Repository
public class CBankCenterInfoDAO extends BaseDAO<CBankCenterInfo> implements ICBankCenterInfoDAO {

    @Override
    public String getCenterNameByNum(String number) {

        String hql;

        String name = "未知";

        if (number.length() == 6) {
            hql = "select result.unit_name from CBankCenterInfo result " +
                    "where result.node = :number " +
                    "and result.deleted = false ";
        } else if (number.length() == 15) {
            hql = "select result.unit_name from CBankCenterInfo result " +
                    "where result.unit_no = :number " +
                    "and result.deleted = false ";
        } else
            return name;

        name = getSession().createQuery(hql, String.class)
                .setParameter("number", number)
                .uniqueResult();
        return name;
    }

    @Override
    public String getCenterNodeByNum(String number) {

        String hql = "select result.node from CBankCenterInfo result " +
                "where result.unit_no = :number " +
                "and result.deleted = false ";

        String node = getSession().createQuery(hql, String.class)
                .setParameter("number", number)
                .uniqueResult();
        
        if (node == null) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有找到对应的节点号，请检查机构编号是否正确");
        }

        return node;
    }

}
