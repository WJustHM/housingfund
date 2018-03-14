package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.hibernate.Criteria;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBaseDAO<T> {
    public abstract String save(Common common);

    public abstract T get(String id);

    public T refresh(Common common);

    public abstract void delete(Common common);

    public abstract void update(Common common);

    public abstract PageResults<T> listWithPage(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo, int pageSize);

    public abstract List<T> list(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption);

    public abstract Long count(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption);

    public abstract void deleteForever(Common common);

    public interface CriteriaExtension {

        public abstract void extend(Criteria criteria);

    }

    public abstract List<T> list(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, CriteriaExtension extension);

    public abstract Long count(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, CriteriaExtension extension);

    public abstract PageResults<T> listWithPage(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo, int pageSize, CriteriaExtension extension);

    public abstract PageResults<T> listWithMarker(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, String marker, int pageSize, ListAction action) throws Exception;

    public abstract PageResults<T> listWithMarker(HashMap<String, Object> filters, Date start, Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, String marker, int pageSize, ListAction action, CriteriaExtension extension)  throws Exception;

    public abstract List<Object[]> list(String sql, Map<String,Object> params);

    public abstract PageResults<Object[]> listWithPage(String sql, Map<String,Object> params, String countSQL, Map<String,Object> countParams,int pageNo, int pageSize);

    public abstract Object[] get(String sql, Map<String,Object> params);

}
