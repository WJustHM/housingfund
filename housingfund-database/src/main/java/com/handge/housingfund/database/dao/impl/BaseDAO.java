package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.util.RedisCache;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.*;

@SuppressWarnings("Duplicates")
@Repository
public class BaseDAO<T> extends HibernateDaoSupport implements IBaseDAO<T> {

    static Logger logger = LogManager.getLogger(BaseDAO.class);

    protected Class<T> entityClass;
    RedisCache redisCache = RedisCache.getRedisCacheInstance();
    int expires = 600;

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Class getEntityClass() {

        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return entityClass;
    }

    public String save(Common common) {
        if (common.getCreated_at() == null)
            common.setCreated_at(new Date());
        String id = (String) this.getSession().save(common);
        this.getSession().flush();
        this.getSession().refresh(common);
        return id;
    }

    @SuppressWarnings("unchecked")
    public T refresh(Common common) {
        this.getSession().refresh(common);
        return (T) common;
    }

    public void saveOrUpdate(Common common) {
        common.setUpdated_at(new Date());
        this.getSession().saveOrUpdate(common);
    }

    @SuppressWarnings("unchecked")
    public T get(String id) {
        T t = (T) this.getSession().get(getEntityClass(), id);
        return t;
    }

    public boolean contains(Common common) {
        return this.getSession().contains(common);
    }

    public void delete(Common common) {
        common.setDeleted(true);
        common.setDeleted_at(new Date());
        this.getSession().update(common);
    }

    public void deleteForever(Common common) {
        this.getSession().delete(common);
        this.getSession().flush();
        this.getSession().clear();

    }

    public void update(Common common) {
        common.setUpdated_at(new Date());
        this.getSession().update(common);
        this.getSession().flush();
        this.getSession().refresh(common);
    }

    public Session getSession() {
        return this.currentSession();
    }

    @SuppressWarnings("deprecation")
    protected Criteria getCriteria(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                                   ListDeleted listDeleted, SearchOption searchOption) {
        Criteria criteria = this.getSession().createCriteria(getEntityClass());
        if (filters != null) {
            Iterator<String> iterator = filters.keySet().iterator();
            List<String> relation = new ArrayList<String>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String[] splits = key.split("\\.");
                if (splits.length == 1) {
                    if (filters.get(key) == null) {

                        criteria.add(Restrictions.isNull(key));

                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(key, (Collection) filters.get(key)));

                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String) {

                        criteria.add(Restrictions.like(key, "%" + filters.get(key) + "%"));

                    } else {

                        criteria.add(Restrictions.eq(key, filters.get(key)));
                    }

                } else if (splits.length == 2) {
                    if (!relation.contains(splits[0])) {
                        criteria.createAlias(splits[0], splits[0]);
                        relation.add(splits[0]);
                    }
                    if (filters.get(key) == null) {
                        criteria.add(Restrictions.isNull(key));
                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(key, (Collection) filters.get(key)));
                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String) {

                        criteria.add(Restrictions.like(key, "%" + filters.get(key) + "%"));

                    } else {
                        criteria.add(Restrictions.eq(key, filters.get(key)));
                    }
                } else {
                    if (!relation.contains(splits[0])) {
                        criteria.createAlias(splits[0], splits[0]);
                        relation.add(splits[0]);
                    }
                    if (!relation.contains(splits[0] + "." + splits[1])) {
                        criteria.createAlias(splits[0] + "." + splits[1], splits[1]);
                        relation.add(splits[0] + "." + splits[1]);
                    }
                    for (int i = 2; i < splits.length; i++) {
                        if (!relation.contains(splits[i - 2] + "." + splits[i - 1])) {
                            criteria.createAlias(splits[i - 2] + "." + splits[i - 1], splits[i - 1]);
                            relation.add(splits[i - 2] + "." + splits[i - 1]);
                        }
                    }
                    if (filters.get(key) == null) {

                        criteria.add(Restrictions.isNull(splits[splits.length - 2] + "." + splits[splits.length - 1]));

                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                (Collection) filters.get(key)));

                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String)

                        criteria.add(Restrictions.like(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                "%" + filters.get(key) + "%"));

                    else {
                        criteria.add(Restrictions.eq(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                filters.get(key)));
                    }
                }
            }
        }
        if (listDeleted == ListDeleted.DELETED) {
            criteria.add(Restrictions.eq("deleted", true));
        }
        if (listDeleted == null || listDeleted == ListDeleted.NOTDELETED) {
            criteria.add(Restrictions.eq("deleted", false));
        }
        if (start != null) {
            criteria.add(Restrictions.ge("created_at", start));
        }
        if (end != null) {
            criteria.add(Restrictions.le("created_at", end));
        }
        if (orderby != null && order != null) {
            if (order == Order.ASC) {
                criteria.addOrder(org.hibernate.criterion.Order.asc(orderby));
            } else {
                criteria.addOrder(org.hibernate.criterion.Order.desc(orderby));
            }
        } else {
            criteria.addOrder(org.hibernate.criterion.Order.asc("created_at"));
        }
        return criteria;
    }

    @SuppressWarnings("unchecked")
    public List<T> list(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                        ListDeleted listDeleted, SearchOption searchOption) {
        List<T> result = new ArrayList<T>();
        result = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption).list();
        return result;
    }

    public Long count(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                      ListDeleted listDeleted, SearchOption searchOption) {
        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
        criteria.setProjection(Projections.rowCount());
        String queryString = criteria.toString();
        long count = 0;
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            String md5 = DigestUtils.md5Hex(queryString);
            if (redis.exists(md5)) {
                count = new Long(redis.get(md5));
                redis.close();
            } else {
                count = (Long) criteria.uniqueResult();
                redis.set(md5, String.valueOf(count));
                redis.set(md5 + ":data", String.valueOf(count));
                redis.expire(md5, expires);
                redis.expire(md5 + ":data", expires);
                redis.close();
            }
        } catch (Exception e) {
            count = (Long) criteria.uniqueResult();
            logger.error(e.getMessage());
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    public PageResults<T> listWithPage(HashMap<String, Object> filters, Date start, Date end, String orderby,
                                       Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo, int pageSize) {
        PageResults<T> retValue = new PageResults<T>();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);
        Long count = count(filters, start, end, orderby, order, listDeleted, searchOption);
        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();
        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
        List<T> itemList = criteria.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
        if (itemList == null) {
            itemList = new ArrayList<T>();
        }
        retValue.setResults(itemList);

        return retValue;
    }

    @SuppressWarnings("deprecation")
    protected Criteria getCriteria(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                                   ListDeleted listDeleted, SearchOption searchOption, CriteriaExtension extension) {
        Criteria criteria = this.getSession().createCriteria(getEntityClass());
        if (filters != null) {
            Iterator<String> iterator = filters.keySet().iterator();
            List<String> relation = new ArrayList<String>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String[] splits = key.split("\\.");
                if (splits.length == 1) {
                    if (filters.get(key) == null) {

                        criteria.add(Restrictions.isNull(key));

                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(key, (Collection) filters.get(key)));

                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String) {

                        criteria.add(Restrictions.like(key, "%" + filters.get(key) + "%"));

                    } else {

                        criteria.add(Restrictions.eq(key, filters.get(key)));
                    }

                } else if (splits.length == 2) {
                    if (!relation.contains(splits[0])) {
                        criteria.createAlias(splits[0], splits[0]);
                        relation.add(splits[0]);
                    }
                    if (filters.get(key) == null) {
                        criteria.add(Restrictions.isNull(key));
                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(key, (Collection) filters.get(key)));
                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String) {

                        criteria.add(Restrictions.like(key, "%" + filters.get(key) + "%"));

                    } else {
                        criteria.add(Restrictions.eq(key, filters.get(key)));
                    }
                } else {
                    if (!relation.contains(splits[0])) {
                        criteria.createAlias(splits[0], splits[0]);
                        relation.add(splits[0]);
                    }
                    if (!relation.contains(splits[0] + "." + splits[1])) {
                        criteria.createAlias(splits[0] + "." + splits[1], splits[1]);
                        relation.add(splits[0] + "." + splits[1]);
                    }
                    for (int i = 2; i < splits.length; i++) {
                        if (!relation.contains(splits[i - 2] + "." + splits[i - 1])) {
                            criteria.createAlias(splits[i - 2] + "." + splits[i - 1], splits[i - 1]);
                            relation.add(splits[i - 2] + "." + splits[i - 1]);
                        }
                    }
                    if (filters.get(key) == null) {

                        criteria.add(Restrictions.isNull(splits[splits.length - 2] + "." + splits[splits.length - 1]));

                    } else if (filters.get(key) instanceof Collection) {

                        criteria.add(Restrictions.in(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                (Collection) filters.get(key)));

                    } else if (searchOption != null && searchOption == SearchOption.FUZZY
                            && filters.get(key) instanceof String)

                        criteria.add(Restrictions.like(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                "%" + filters.get(key) + "%"));

                    else {
                        criteria.add(Restrictions.eq(splits[splits.length - 2] + "." + splits[splits.length - 1],
                                filters.get(key)));
                    }
                }
            }
        }
        if (listDeleted == ListDeleted.DELETED) {
            criteria.add(Restrictions.eq("deleted", true));
        }
        if (listDeleted == null || listDeleted == ListDeleted.NOTDELETED) {
            criteria.add(Restrictions.eq("deleted", false));
        }
        if (start != null) {
            criteria.add(Restrictions.ge("created_at", start));
        }
        if (end != null) {
            criteria.add(Restrictions.le("created_at", end));
        }
        if (orderby != null && order != null) {
            if (order == Order.ASC) {
                criteria.addOrder(org.hibernate.criterion.Order.asc(orderby));
            } else {
                criteria.addOrder(org.hibernate.criterion.Order.desc(orderby));
            }
        } else {
            criteria.addOrder(org.hibernate.criterion.Order.asc("created_at"));
        }

        extension.extend(criteria);

        return criteria;
    }

    @SuppressWarnings("unchecked")
    public List<T> list(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                        ListDeleted listDeleted, SearchOption searchOption, CriteriaExtension extension) {
        List<T> result = new ArrayList<T>();
        result = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension).list();
        return result;
    }

    public Long count(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order,
                      ListDeleted listDeleted, SearchOption searchOption, CriteriaExtension extension) {
        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
        criteria.setProjection(Projections.rowCount());
        String queryString = criteria.toString();
        long count = 0;
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            String md5 = DigestUtils.md5Hex(queryString);
            if (redis.exists(md5)) {
                count = new Long(redis.get(md5));
                redis.close();
            } else {
                count = (Long) criteria.uniqueResult();
                redis.setex(md5, expires, String.valueOf(count));
                redis.setex(md5 + ":data", expires, String.valueOf(count));
                redis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            count = (Long) criteria.uniqueResult();
        }
        return count;
    }

    @Autowired
    public void setSessionFactoryOverride(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public PageResults<T> listWithPage(HashMap<String, Object> filters, Date start, Date end, String orderby,
                                       Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo, int pageSize,
                                       CriteriaExtension extension) {
        PageResults<T> retValue = new PageResults<T>();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);
        Long count = count(filters, start, end, orderby, order, listDeleted, searchOption, extension);
        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();
        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
        List<T> itemList = criteria.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
        if (itemList == null) {
            itemList = new ArrayList<T>();
        }
        retValue.setResults(itemList);

        return retValue;
    }

    @Override
    public PageResults<T> listWithMarker(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, String marker, int pageSize, ListAction action) throws Exception {
        PageResults<T> retValue = new PageResults<T>();
        List<T> itemList = null;
        Criteria criteria = null;
        switch (action){
            case First:
                orderby="created_at";
                order=Order.DESC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
                criteria.addOrder(org.hibernate.criterion.Order.desc("id"));
                itemList = criteria.setMaxResults(pageSize).list();
                break;
            case Last:
                orderby="created_at";
                order=Order.ASC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
                criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
                itemList = criteria.setMaxResults(pageSize).list();
                Collections.reverse(itemList);
                break;
            case Previous:
                if(marker==null){
                    throw new Exception("marker不能为空");
                }
                orderby="created_at";
                order=Order.ASC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
                criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
                criteria.add(Restrictions.gt("id", marker));
                itemList = criteria.setMaxResults(pageSize).list();
                Collections.reverse(itemList);
                if (itemList.size() == 0) {
                    retValue.setTag("F");
//                    throw new Exception("当前页已经是第一页");
                }
                break;
            case Next:
                if(marker==null){
                    throw new Exception("marker不能为空");
                }
                orderby="created_at";
                order=Order.DESC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);
                criteria.addOrder(org.hibernate.criterion.Order.desc("id"));
                criteria.add(Restrictions.lt("id", marker));
                itemList = criteria.setMaxResults(pageSize).list();
                if (itemList.size() == 0) {
                    retValue.setTag("L");
//                    throw new Exception("当前页已经是最后一页");
                }
                break;
            default:
                if(marker==null){
                    throw new Exception("action错误");
                }
        }
        retValue.setResults(itemList);
        retValue.setPageSize(pageSize);
        return retValue;
    }

    @Override
    public PageResults<T> listWithMarker(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, String marker, int pageSize, ListAction action, CriteriaExtension extension)  throws Exception{
        PageResults<T> retValue = new PageResults<T>();
        List<T> itemList = null;
        Criteria criteria = null;
        switch (action){
            case First:
                orderby="created_at";
                order=Order.DESC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
                criteria.addOrder(org.hibernate.criterion.Order.desc("id"));
                itemList = criteria.setMaxResults(pageSize).list();
                break;
            case Last:
                orderby = "created_at";
                order=Order.ASC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
                criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
                itemList = criteria.setMaxResults(pageSize).list();
                Collections.reverse(itemList);
                break;
            case Previous:
                if(marker==null){
                    throw new Exception("marker不能为空");
                }
                orderby="created_at";
                order=Order.ASC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
                criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
                criteria.add(Restrictions.gt("id", marker));
                itemList = criteria.setMaxResults(pageSize).list();
                Collections.reverse(itemList);
                if (itemList.size() == 0) {
                    retValue.setTag("F");
//                    throw new Exception("当前页已经是第一页");
                }
                break;
            case Next:
                if(marker==null){
                    throw new Exception("marker不能为空");
                }
                orderby="created_at";
                order=Order.DESC;
                criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption, extension);
                criteria.addOrder(org.hibernate.criterion.Order.desc("id"));
                criteria.add(Restrictions.lt("id", marker));
                itemList = criteria.setMaxResults(pageSize).list();
                if (itemList.size() == 0) {
                    retValue.setTag("L");
                    Collections.reverse(itemList);
//                    throw new Exception("当前页已经是最后一页");
                }
                break;
            default:
                if(marker==null){
                    throw new Exception("action错误");
                }
        }
        retValue.setResults(itemList);
        retValue.setPageSize(pageSize);
        return retValue;
    }
    public List<Object[]> list(String sql, Map<String,Object> params){
        Session session = this.getSession();
        NativeQuery<Object[]> query = session.createNativeQuery(sql);
        if(params!=null&&!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList();
    }

    public PageResults<Object[]> listWithPage(String sql, Map<String,Object> params, String countSQL, Map<String,Object> countParams,int pageNo, int pageSize){
        PageResults<Object[]> retValue = new PageResults<Object[]>();
        Session session = this.getSession();
        Long count = new Long(0);
        NativeQuery countQuery = session.createNativeQuery(countSQL);
        if(countParams!=null&&!countParams.isEmpty()) {
            for (String key : countParams.keySet()) {
                countQuery.setParameter(key, countParams.get(key));
            }
        }
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            String md5 = DigestUtils.md5Hex(countQuery.getQueryString());
            if (redis.exists(md5)) {
                count = new Long(redis.get(md5));
            } else {
                count = Long.valueOf(countQuery.getSingleResult().toString());
                redis.set(md5, String.valueOf(count));
                redis.set(md5 + ":data", String.valueOf(count));
                redis.expire(md5, expires);
                redis.expire(md5 + ":data", expires);
                redis.close();
            }
        } catch (Exception e) {
            count = Long.valueOf(countQuery.getSingleResult().toString());
            logger.error(e.getMessage());
        }

        int currentPage = pageNo > 1? pageNo : 1;
        if (pageSize>0){
            sql = sql + " LIMIT " + String.valueOf((currentPage - 1) * pageSize) + ", " + String.valueOf(pageSize);
        }
        NativeQuery query = session.createNativeQuery(sql);
        if(params!=null&&!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);
        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();
        List<Object[]> itemList = query.getResultList();
        if (itemList == null) {
            itemList = new ArrayList<Object[]>();
        }
        retValue.setResults(itemList);
        return retValue;
    }

    public Object[] get(String sql, Map<String,Object> params){
        Session session = this.getSession();
        Query query = session.createNativeQuery(sql);
        if(params!=null&&!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return (Object[])query.getSingleResult();
    }


}
