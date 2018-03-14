package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.finance.model.WithdrawlReportResult;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCommonPersonDAO;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StCommonPerson;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@SuppressWarnings({"JpaQlInspection", "SqlDialectInspection", "SqlNoDataSourceInspection"})
@Repository
public class StCollectionPersonalBusinessDetailsDAO extends BaseDAO<StCollectionPersonalBusinessDetails>
        implements IStCollectionPersonalBusinessDetailsDAO {

    @Autowired
    private IStCommonPersonDAO commonPersonDAO;

    public PageResults<StCollectionPersonalBusinessDetails> getReviewedList(HashMap<String, Object> filters, Date start,
                                                                            Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption,
                                                                            int pageNo, int pageSize) {

        PageResults<StCollectionPersonalBusinessDetails> retValue = new PageResults<StCollectionPersonalBusinessDetails>();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);

        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);

        boolean flag = true;

        if (filters != null) {

            Iterator<String> iterator = filters.keySet().iterator();

            while (iterator.hasNext()) {

                String key = iterator.next();

                if (key.split("\\.")[0].equals("cCollectionPersonalBusinessDetailsExtension")) {

                    flag = false;
                }
            }
        }
        if (flag) {
            criteria.createAlias("cCollectionPersonalBusinessDetailsExtension",
                    "cCollectionPersonalBusinessDetailsExtension");
        }
        criteria.add(Restrictions.or(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywzt", "03"),
                Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywzt", "04")));

        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();

        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();

        Criteria criteria1 = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);

//        criteria1.createAlias("cCollectionPersonalBusinessDetailsExtension", "cCollectionPersonalBusinessDetailsExtension");

        criteria1.add(Restrictions.or(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywzt", "03"),
                Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywzt", "04")));

        List<StCollectionPersonalBusinessDetails> itemList = criteria1.setFirstResult((currentPage - 1) * pageSize)
                .setMaxResults(pageSize).list();

        if (itemList == null) {
            itemList = new ArrayList<StCollectionPersonalBusinessDetails>();
        }
        retValue.setResults(itemList);

        return retValue;
    }

    @SuppressWarnings("unchecked")
    public PageResults<StCollectionPersonalBusinessDetails> listByCZMC(HashMap<String, Object> filters, Date start,
                                                                       Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo,
                                                                       int pageSize, List<String> czmcList) {
        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);

        boolean flag = true;

        if (filters != null) {

            Iterator<String> iterator = filters.keySet().iterator();

            while (iterator.hasNext()) {

                String key = iterator.next();

                if (key.split("\\.")[0].equals("cCollectionPersonalBusinessDetailsExtension")) {

                    flag = false;
                }
            }
        }
        if (flag) {
            criteria.createAlias("cCollectionPersonalBusinessDetailsExtension",
                    "cCollectionPersonalBusinessDetailsExtension");
        }
        Disjunction dis = Restrictions.disjunction();
        for (String czmc : czmcList) {
            dis.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", czmc));
        }
        criteria.add(dis);
        PageResults<StCollectionPersonalBusinessDetails> retValue = new PageResults<StCollectionPersonalBusinessDetails>();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);

        List<StCollectionPersonalBusinessDetails> itemList = criteria.setFirstResult((currentPage - 1) * pageSize)
                .setMaxResults(pageSize).list();
        if (itemList == null) {
            itemList = new ArrayList<StCollectionPersonalBusinessDetails>();
        }
        retValue.setResults(itemList);

        Criteria criteria1 = criteria;
        criteria1.setProjection(Projections.rowCount());
        Long count = (Long) criteria1.uniqueResult();
        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();

        return retValue;
    }

    @Override
    public StCollectionPersonalBusinessDetails getByYwlsh(String ywlsh) {
        String sql = "select result from StCollectionPersonalBusinessDetails result where result.ywlsh = :ywlsh";
        StCollectionPersonalBusinessDetails result = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    /**
     * 单位在途验证，只要存在未办结的业务即认为在途，大多数业务可满足，不满足的业务需另写
     */
    @Override
    public boolean isExistDoingPersonBus(String grzh, String ywlx) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.grzh = :grzh " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc = :ywlx " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step <> '办结' " +
                "and result.deleted = false ";
        List<StCollectionPersonalBusinessDetails> list = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("grzh", grzh)
                .setParameter("ywlx", ywlx)
                .list();
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCouldModifyPersonBus(String ywlsh) {
        String sql = "select result.ywlsh from StCollectionPersonalBusinessDetails result " +
                "where result.ywlsh = :ywlsh " +
                "and result.deleted = false " +
                "and ( result.cCollectionPersonalBusinessDetailsExtension.step = '新建' " +
                " or result.cCollectionPersonalBusinessDetailsExtension.step = '审核不通过') ";
        String result = getSession().createQuery(sql, String.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result != null;
    }

    @Override
    public void updateGRZH(String GRZH_current, String GRZH_original, StCommonPerson stCommonPerson) {

        if (!stCommonPerson.getGrzh().equals(GRZH_current)) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH);
        }


        String YWLSH = (String) this.getSession().createNativeQuery("" +
                "select " +
                "res.ywlsh  from st_collection_personal_business_details res " +
                "where res.grzh = :grzh " +
                "and   res.gjhtqywlx = '05' " +
                "order by res.created_at desc " +
                "limit 1").setParameter("grzh",GRZH_original).uniqueResult();

        StCommonPerson commonPerson_current = this.commonPersonDAO.getByGrzh(GRZH_current);
        StCommonPerson commonPerson_original = this.commonPersonDAO.getByGrzh(GRZH_original);

        if(commonPerson_current.getUnit()!=null&&commonPerson_original.getUnit()!=null&&!commonPerson_current.getUnit().getDwzh().equals(commonPerson_original.getUnit().getDwzh())){

            YWLSH = null;
        }

        for(String tableName : Arrays.asList(
                "c_collection_individual_account_basic_vice",
                "c_collection_individual_account_action_vice",
                "c_collection_individual_account_merge_vice",
                "c_collection_individual_account_transfer_new_vice"                 ,
                "c_collection_withdrawl_vice"
        )){

            String sql = "" +
                    "update "+tableName+" "+"vice  \n" +
                    "inner join st_collection_personal_business_details details on vice.grywmx = details.id \n" +
                    "set   details.grzh = :GRZH , details.Person = :person, vice.grzh = :GRZH \n " +
                    "where details.GRZH = :grzh \n" + (
                    YWLSH != null ? ("and details.ywlsh <> '"+YWLSH+"'" ): "");

            this.getSession().createNativeQuery(sql).setParameter("GRZH",GRZH_current).setParameter("grzh",GRZH_original).setParameter("person",stCommonPerson.getId()).executeUpdate();

        }

        this.getSession().createNativeQuery("" +
                "update st_collection_personal_business_details details " +
                "set details.grzh = :GRZH , details.Person = :person "+
                "where details.GRZH = :grzh \n" + (
                YWLSH != null ? ("and details.ywlsh <> '"+YWLSH+"'" ): "")).setParameter("GRZH",GRZH_current).setParameter("grzh",GRZH_original).setParameter("person",stCommonPerson.getId()).executeUpdate();
    }


    public BigDecimal getTotalBusinessFSE(Date date) {


        String sql = "select sum(details.fse) FROM StCollectionPersonalBusinessDetails details  where " +
                "details.created_at >= '" + DateUtil.date2Str(date, "yyyy-MM-dd 00:00:00") + "' and " +
                "details.created_at <= '" + DateUtil.date2Str(date, "yyyy-MM-dd 23:59:59") + "' and " +
                "details.deleted = 0";

        BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
                .uniqueResult();

        return result;
    }

    public BigDecimal getTotalBusinessFSEBetweenDate(Date start, Date end) {


        String sql = "select sum(details.fse) FROM StCollectionPersonalBusinessDetails details  where " +
                "details.created_at >= '" + DateUtil.date2Str(start, "yyyy-MM-dd 00:00:00") + "' and " +
                "details.created_at <= '" + DateUtil.date2Str(end, "yyyy-MM-dd 23:59:59") + "' and " +
                "details.deleted = 0";

        BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
                .uniqueResult();

        return result;
    }

    public BigDecimal getSumZHYE() {


        String sql = "select sum(account.dwzhye) FROM StCollectionUnitAccount account";

        BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
                .uniqueResult();

        return result;
    }

    @Override
    public void saveNormal(StCollectionPersonalBusinessDetails personalBusiness) {
        getSession().save(personalBusiness);
    }

    @Override
    public List<StCollectionPersonalBusinessDetails> getPersonDeposits(String grzh) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.grzh =:grzh " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc in ('01','02','73') " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "order by result.cCollectionPersonalBusinessDetailsExtension.bjsj desc ";
        List<StCollectionPersonalBusinessDetails> list = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("grzh", grzh)
                .list();
        return list;
    }

    @Override
    public List<StCollectionPersonalBusinessDetails> getPersonDepositsAfter(String grzh, String sxnyStr) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.grzh =:grzh " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc in ('01','02','73') " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
                "and result.cCollectionPersonalBusinessDetailsExtension.fsny < :sxny " +
                "order by result.cCollectionPersonalBusinessDetailsExtension.fsny desc, " +
                "result.cCollectionPersonalBusinessDetailsExtension.bjsj desc ";
        List<StCollectionPersonalBusinessDetails> list = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("grzh", grzh)
                .setParameter("sxny", sxnyStr)
                .list();
        return list;
    }

    @Override
    public List<StCollectionPersonalBusinessDetails> getPersonDepositsChange(String grzh) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.grzh = :grzh " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc in ('01','02','73','09','11','12') " +
                "and result.cCollectionPersonalBusinessDetailsExtension.step in ('已入账','已入账分摊','办结') " +
                "order by result.jzrq desc,result.cCollectionPersonalBusinessDetailsExtension.fsny desc ";
        List<StCollectionPersonalBusinessDetails> list = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("grzh", grzh)
                .list();
        return list;
    }

    @Override
    public BigDecimal getGrywmxHj() {
        String sql = "select sum(res.fse) FROM StCollectionPersonalBusinessDetails res where deleted=0 and JZRQ IS NOT NULL";
        BigDecimal result = getSession().createQuery(sql, BigDecimal.class)
                .uniqueResult();
        return result;
    }

    @Override
    public boolean hasWithdrawl(String grzh) {
        String sql = "select result from StCollectionPersonalBusinessDetails result " +
                "where result.grzh = :grzh  " +
                "and result.cCollectionPersonalBusinessDetailsExtension.czmc in ('11','12') " +
                "and result.deleted = '0'"+
                "and result.cCollectionPersonalBusinessDetailsExtension.step not in ('已入账','已作废') ";
        List<StCollectionPersonalBusinessDetails> list = getSession().createQuery(sql, StCollectionPersonalBusinessDetails.class)
                .setParameter("grzh", grzh)
                .list();
        return list.size() > 0;
    }

    @Override
    public ArrayList<WithdrawlReportResult> getWithdrawlReport(Date kssj, Date jssj) {
        ArrayList<WithdrawlReportResult> withdrawlReportResults = new ArrayList<>();

        String start = DateUtil.date2Str(kssj, "yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.date2Str(jssj, "yyyy-MM-dd HH:mm:ss");

        String sql = "SELECT " +
                "count(DISTINCT de.GRZH) as num, SUM(ABS(de.FSE)) as amt, de.TQYY as tqyy " +
                "FROM " +
                "st_collection_personal_business_details de " +
                "INNER JOIN c_collection_personal_business_details_extension ex ON de.extension = ex.id " +
                "WHERE " +
                "de.GJHTQYWLX IN ('11', '12') " +
                "AND ex.STEP = '已入账' " +
                "AND de.deleted = '0'" +
                "AND de.JZRQ >='" + start + "' " +
                "AND de.JZRQ <='" + end + "' " +
                "AND de.TQYY IN ('01','02','03','04','05','06','07','08','09','99') " +
                "GROUP BY de.TQYY";

        List<Object[]> list = getSession().createNativeQuery(sql).list();

        for (Object[] result : list) {
            WithdrawlReportResult withdrawlReportResult = new WithdrawlReportResult();
            withdrawlReportResult.setShuLiang(String.valueOf((BigInteger) result[0]));
            withdrawlReportResult.setJinE(((BigDecimal) result[1]).toPlainString());
            withdrawlReportResult.setCode((String) result[2]);

            withdrawlReportResults.add(withdrawlReportResult);
        }

        return withdrawlReportResults;
    }

    @Override
    public List<String> getYwlshListByPchByYwlsh(String ywlsh) {

        String sql =
                "SELECT d1.YWLSH FROM st_collection_personal_business_details d1 " +
                "INNER JOIN c_collection_personal_business_details_extension e1 ON d1.extension = e1.id " +
                "WHERE e1.PCH = ( SELECT e2.PCH FROM st_collection_personal_business_details d2 " +
                "INNER JOIN c_collection_personal_business_details_extension e2 ON d2.extension = e2.id " +
                "WHERE d2.YWLSH = '" + ywlsh +"' ) AND d1.deleted = false";

        List<String> ywlshList = getSession().createNativeQuery(sql).list();

//        for (Object o : ywlshList) {
//            System.out.println(o);
//        }

        return ywlshList;
    }

    @Override
    public Object[] getPersonDepositSUM(String grzh, String fsny) {
        String sql = "select v.grzh,sum(v.dwfse) dwfsehj,sum(v.grfse) grfsehj,sum(v.fse) fsehj from  " +
                "( " +
                "select a.grzh, " +
                "(case  " +
                "when b.czmc in ('01','02') then b.dwfse  " +
                "when b.czmc in ('73') then -b.dwfse " +
                "end ) dwfse, " +
                "(case  " +
                "when b.czmc in ('01','02') then b.grfse   " +
                "when b.czmc in ('73') then -b.grfse " +
                "end) grfse, " +
                "a.fse " +
                "from st_collection_personal_business_details a  " +
                "join c_collection_personal_business_details_extension b on a.extension = b.id  " +
                "where a.grzh = :grzh  " +
                "and b.fsny = :fsny " +
                "and b.czmc in ('01','02','73') " +
                ") v ";
        Object[] result = (Object[])getSession().createSQLQuery(sql)
                .setParameter("grzh", grzh)
                .setParameter("fsny", fsny)
                .uniqueResult();

        return result;
    }

    public List<String> getDepositContinuousMonth(String grzh){
        String sql = "select distinct b.fsny " +
                "from st_collection_personal_business_details a " +
                "join c_collection_personal_business_details_extension b on a.extension = b.id " +
                "where a.grzh ='" + grzh +"'" +
                "and a.gjhtqywlx in ('01','02') " +
                "order by b.fsny desc ";
        List<String> MonthList = getSession().createNativeQuery(sql).list();
        return MonthList;
    }

    @Override
    public void updateDQYE(String id, BigDecimal dqye) {
        String sql = "update c_collection_personal_business_details_extension set dqye = :dqye where id = :id ";
        int i = getSession().createSQLQuery(sql)
                .setParameter("id", id)
                .setParameter("dqye", dqye)
                .executeUpdate();
    }

}
