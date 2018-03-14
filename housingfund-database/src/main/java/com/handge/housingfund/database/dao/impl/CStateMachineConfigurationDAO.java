package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class CStateMachineConfigurationDAO extends BaseDAO<CStateMachineConfiguration>
        implements ICStateMachineConfigurationDAO {

    @Override
    public List<CStateMachineConfiguration> getConfig(BusinessType type, String subType, String status, String event) {
        String sql = "select result from  CStateMachineConfiguration result " +
                "where result.type = :type " +
                "and result.subType = :subType " +
                "and result.source = :status " +
                "and result.event = :event ";
        List<CStateMachineConfiguration> result = getSession().createQuery(sql, CStateMachineConfiguration.class)
                .setParameter("type", type)
                .setParameter("subType", subType)
                .setParameter("status", status)
                .setParameter("event", event)
                .list();
        return result;
    }

    @Override
    public CStateMachineConfiguration getDefinedConfig(String ywwd, String type, String subType, String source, String event) {
        String sql = "select result from  CStateMachineConfiguration result " +
                "where result.type = :type " +
                "and result.subType = :subType " +
                "and result.source = :source " +
                "and result.ywwd = :ywwd " +
                "and result.event = :event ";
        CStateMachineConfiguration result = getSession().createQuery(sql, CStateMachineConfiguration.class)
                .setParameter("type", type)
                .setParameter("subType", subType)
                .setParameter("source", source)
                .setParameter("ywwd", ywwd)
                .setParameter("event", event).getSingleResult();
        return result;
    }

/*    @Override
    public List<String> getSources(String role, String type, String subModule, String subType, String ywwd) {
        String sql = "select distinct result.source from CStateMachineConfiguration result " +
                "where ( result.role like :role1 " +
                "or result.role like :role2 " +
                "or result.role = :role3 ) " +
                "and result.type = :type " +
                "and result.subModule = :subModule " +
                "and result.subType = :subType " +
                "and result.workstation = :ywwd " +
                "and result.isAudit > 0 ";

        List<String> sources = getSession().createQuery(sql, String.class)
                .setParameter("subType", subType)
                .setParameter("role1", "%," + role + "%")
                .setParameter("role2", "%" + role + ",%")
                .setParameter("role3", "%" + role + "%")
                .setParameter("ywwd", ywwd)
                .list();

        return sources;
    }*/


    public ArrayList<String> getReviewSources(String role, BusinessType type, String subModule, Collection subTypes, String ywwd) {

        Criteria criteria = this.getSession().createCriteria(CStateMachineConfiguration.class);

        criteria.add(Restrictions.gt("isAudit", "0"));

        criteria.add(Restrictions.eq("flag", true));

        criteria.add(Restrictions.eq("deleted", false));

        if (null != role) {
            criteria.add(Restrictions.or(
                    Restrictions.like("role", "%," + role),
                    Restrictions.like("role", role + ",%"),
                    Restrictions.like("role", "%," + role + ",%"),
                    Restrictions.eq("role", role)));
        }
        if (null != type) {
            criteria.add(Restrictions.eq("type", type));
        }
        if (null != subModule) {
            criteria.add(Restrictions.like("subType", subModule + "%"));
        }
        if (null != subTypes) {
            criteria.add(Restrictions.in("subType", subTypes));
        }
        if (null != ywwd) {
            criteria.add(Restrictions.eq("workstation", ywwd));
        }

        List<CStateMachineConfiguration> entities = criteria.list();

        ArrayList<String> sources = new ArrayList<>();

        for (CStateMachineConfiguration entity : entities) {
            sources.add(entity.getSource());
        }
        sources = new ArrayList<>(new HashSet<>(sources));
        return sources;
    }

    @Override
    public List<String> getTargets(BusinessType type, String subType, String ywwd, String role) {
        String sql = "select distinct result.target from CStateMachineConfiguration result " +
                "where result.subType = :subType " +
                "and ( result.role like :role1 " +
                "or result.role like :role2 " +
                "or result.role like :role4 " +
                "or result.role = :role3 ) " +
                "and result.workstation = :ywwd " +
                "and result.type = :type " +
                "and result.isAudit > 0";

        List<String> targets = getSession().createQuery(sql, String.class)
                .setParameter("subType", subType)
                .setParameter("role1", "%," + role)
                .setParameter("role2", role + ",%")
                .setParameter("role3", role)
                .setParameter("role4", "%," + role + ",%")
                .setParameter("ywwd", ywwd)
                .setParameter("type", type)
                .list();
        return targets;
    }

    @Override
    public boolean isSpecialReview(String source, String subType, String role, String ywwd) {

        Criteria criteria = this.getSession().createCriteria(CStateMachineConfiguration.class);

        criteria.add(Restrictions.gt("isAudit", "0"));

        criteria.add(Restrictions.eq("flag", true));

        criteria.add(Restrictions.eq("deleted", false));

        criteria.add(Restrictions.eq("source", source));

        criteria.add(Restrictions.eq("event", Events.特审.getEvent()));

        if (null != role) {
            criteria.add(Restrictions.or(
                    Restrictions.like("role", "%," + role),
                    Restrictions.like("role", role + ",%"),
                    Restrictions.like("role", "%," + role + ",%"),
                    Restrictions.eq("role", role)));
        }

        if (null != subType) {
            criteria.add(Restrictions.eq("subType", subType));
        }
        if (null != ywwd) {
            criteria.add(Restrictions.eq("workstation", ywwd));
        }

        int num = criteria.list().size();

        return (num > 0);
    }

    @Override
    public List<String> getSpecialReviewSource(String subType) {
        String hql = "select distinct result.source from CStateMachineConfiguration result " +
                "where result.subType = :subType " +
                "and result.event = '特审' " +
                "and result.isAudit > 0" +
                "and result.deleted = false " +
                "and result.flag = true";

        List<String> sources = getSession().createQuery(hql, String.class)
                .setParameter("subType", subType)
                .list();

        return sources;
    }

    @Deprecated
    @Override
    public boolean isSpecialReview(String state) {
        String hql = "select distinct result.source from CStateMachineConfiguration result " +
                "where result.source = :state " +
                "and result.event = '特审' " +
                "and result.subType = :subType " +
                "and result.isAudit > 0";

        int num = getSession().createQuery(hql, String.class)
                .setParameter("state", state)
                .list().size();

        return (num == 1);
    }

    public String getStepByRevoke(String role, BusinessType type, String subType, String ywwd, String target) {

        String hql = "select distinct result.source from CStateMachineConfiguration result " +
                "where result.role = :role " +
                "and result.type = :type " +
                "and result.subType = :subType " +
                "and result.workstation = :ywwd " +
                "and result.target = :target " +
                "and result.flag = true " +
                "and result.deleted = false";

        String step = getSession().createQuery(hql, String.class)
                .setParameter("role", role)
                .setParameter("type", type)
                .setParameter("subType", subType)
                .setParameter("ywwd", ywwd)
                .setParameter("target", target)
                .getSingleResult();

        return step;
    }

    @Override
    public ArrayList<String> getSubTypesByAuth(String ywwd, String role, BusinessType type, String prefix) {

        Criteria criteria = this.getSession().createCriteria(CStateMachineConfiguration.class);

        criteria.add(Restrictions.gt("isAudit", "0"));

        criteria.add(Restrictions.eq("flag", true));

        criteria.add(Restrictions.eq("deleted", false));

        criteria.add(Restrictions.or(
                Restrictions.eq("event", "通过"),
                Restrictions.eq("event", "特审")));

        if (null != role) {
            criteria.add(Restrictions.or(
                    Restrictions.like("role", "%," + role),
                    Restrictions.like("role", role + ",%"),
                    Restrictions.like("role", "%," + role + ",%"),
                    Restrictions.eq("role", role)));
        }
        if (null != type) {
            criteria.add(Restrictions.eq("type", type));
        }
        if (null != prefix) {
            criteria.add(Restrictions.like("subType", prefix + "%"));
        }
        if (null != ywwd) {
            criteria.add(Restrictions.eq("workstation", ywwd));
        }

        List<CStateMachineConfiguration> entities = criteria.list();

        ArrayList<String> subtypes = new ArrayList<>();

        for (CStateMachineConfiguration entity : entities) {
            subtypes.add(entity.getSubType());
        }
        subtypes = new ArrayList<>(new HashSet<>(subtypes));
        return subtypes;
    }

    @Override
    public boolean checkIsPermission(String ywwd, String role, String subType, String source) {

        String hql = "select conf.id from CStateMachineConfiguration conf " +
                "where conf.workstation = :ywwd and (conf.role = :role or conf.role like :role1 or conf.role like :role2 or conf.role like :role3) and conf.subType = :subType and conf.source = :source and conf.event in ('通过','特审') " +
                "and conf.deleted = false and conf.flag = true and conf.isAudit > 0 ";

        List<String> result = getSession().createQuery(hql, String.class)
                .setParameter("role", role)
                .setParameter("role1", role + ",%")
                .setParameter("role2", "%," + role)
                .setParameter("role3", "%," + role + ",%")
                .setParameter("subType", subType)
                .setParameter("ywwd", ywwd)
                .setParameter("source", source)
                .getResultList();

        if (result == null || result.size() <= 0 )
            return false;
        return true;
    }
}
