package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class StCollectionUnitBusinessDetailsDAO extends BaseDAO<StCollectionUnitBusinessDetails>
        implements IStCollectionUnitBusinessDetailsDAO {

    @Override
    public PageResults<StCollectionUnitBusinessDetails> getReviewedList(HashMap<String, Object> filters, Date start,
                                                                        Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption,
                                                                        int pageNo, int pageSize) {


        PageResults<StCollectionUnitBusinessDetails> retValue = new PageResults<StCollectionUnitBusinessDetails>();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);

        Criteria criteria = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);

        boolean flag = true;

        if (filters != null) {

            Iterator<String> iterator = filters.keySet().iterator();

            while (iterator.hasNext()) {

                String key = iterator.next();

                if (key.split("\\.")[0].equals("cCollectionUnitBusinessDetailsExtension")) {

                    flag = false;
                }
            }
        }
        if (flag) {
            criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");
        }
//		criteria.createAlias("cCollectionUnitBusinessDetailsExtension", "extension");

        criteria.add(Restrictions.or(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywzt", "03"),
                Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywzt", "04")));

        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();

        retValue.setTotalCount(count.intValue());
        retValue.resetPageNo();

        Criteria criteria1 = this.getCriteria(filters, start, end, orderby, order, listDeleted, searchOption);

//		criteria1.createAlias("cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");

        criteria1.add(Restrictions.or(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywzt", "03"),
                Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywzt", "04")));

        List<StCollectionUnitBusinessDetails> itemList = criteria1.setFirstResult((currentPage - 1) * pageSize)
                .setMaxResults(pageSize).list();

        if (itemList == null) {
            itemList = new ArrayList<StCollectionUnitBusinessDetails>();
        }
        retValue.setResults(itemList);

        return retValue;
    }

    @Override
    public StCollectionUnitBusinessDetails getByYwlsh(String ywlsh) {
        String sql = "select result from StCollectionUnitBusinessDetails result where result.ywlsh = :ywlsh";
        StCollectionUnitBusinessDetails result = getSession().createQuery
                (sql, StCollectionUnitBusinessDetails.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result;
    }

    /**
     * 单位在途验证，只要存在未办结的业务即认为在途，大多数业务可满足，不满足的业务需另写
     */
    @Override
    public boolean isExistDoingUnitBus(String dwzh, String ywlx) {
        String sql = "select result from StCollectionUnitBusinessDetails result " +
                "where result.dwzh = :dwzh " +
                "and result.cCollectionUnitBusinessDetailsExtension.czmc = :ywlx " +
                "and result.cCollectionUnitBusinessDetailsExtension.step <> '办结' " +
                "and result.deleted = false ";
        List<StCollectionUnitBusinessDetails> list = getSession().createQuery(sql, StCollectionUnitBusinessDetails.class)
                .setParameter("dwzh", dwzh)
                .setParameter("ywlx", ywlx)
                .list();
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCouldModifyUnitBus(String ywlsh) {
        String sql = "select result.ywlsh from StCollectionUnitBusinessDetails result " +
                "where result.ywlsh = :ywlsh " +
                "and result.deleted = false " +
                "and ( result.cCollectionUnitBusinessDetailsExtension.step = '办结' " +
                " or result.cCollectionUnitBusinessDetailsExtension.step = '审核不通过') ";
        String result = getSession().createQuery(sql, String.class)
                .setParameter("ywlsh", ywlsh)
                .uniqueResult();
        return result != null;
    }

    @Override
    public List<StCollectionUnitBusinessDetails> getBusEnd(String dwzh, String ywmxlx) {
        String sql = "select result from StCollectionUnitBusinessDetails result where " +
                "result.dwzh = :dwzh " +
                "and result.ywmxlx = :ywmxlx " +
                "and result.cCollectionUnitBusinessDetailsExtension.step in ('办结','已入账','已入账分摊') ";
        List<StCollectionUnitBusinessDetails> list = getSession().createQuery(sql, StCollectionUnitBusinessDetails.class)
                .setParameter("dwzh", dwzh)
                .setParameter("ywmxlx", ywmxlx)
                .list();
        return list;
    }

    //获取全市各地区归集总额
    @Override
    public List<Map> getCityCollection(int year) {

        Date left = DateUtil.safeStr2Date("yyyy-MM-dd", year + "-01-01");
        Date right = DateUtil.safeStr2Date("yyyy-MM-dd", year + 1 + "-01-01");

        String leftStr = year + "-01-01";
        String rightStr = year + 1 + "-01-01";

/*        String sql_old = "select new map(wk.MingCheng as area, Month(t.jzrq) as month, sum(t.fse) as fse) " +
                "from StCollectionUnitBusinessDetails t " +
                "left join CCollectionUnitBusinessDetailsExtension ex on t.cCollectionUnitBusinessDetailsExtension = ex.id " +
                "left join CAccountNetwork wk on ex.ywwd = wk.id " +
                "where " +
                "t.jzrq >= :left and t.jzrq < :right and t.ywmxlx in ('01','02') " +
                "group by wk.MingCheng, Month(t.jzrq)";

        List<Map> result = getSession().createQuery(sql_old, Map.class)
//					.setParameter("year", year)
                .setParameter("left", left)
                .setParameter("right", right)
                .list();

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("赫章县管理部", new BigDecimal("6918477.53"));
        map.put("百管委管理部", new BigDecimal("922885.61"));
        map.put("市直管理部", new BigDecimal("17930212.41"));
        map.put("七星关区管理部", new BigDecimal("10733901.58"));
        map.put("大方县管理部", new BigDecimal("9569232.21"));
        map.put("黔西县管理部", new BigDecimal("9423812.83"));
        map.put("金沙县管理部", new BigDecimal("10153970.88"));
        map.put("织金县管理部", new BigDecimal("10153970.88"));
        map.put("纳雍县管理部", new BigDecimal("7381287.58"));
        map.put("威宁县管理部", new BigDecimal("13173898.57"));

        if (year == 2017) {
            for (Map map2 : result) {
                if (Integer.valueOf("6").equals(map2.get("month"))) {
                    Set<Map.Entry<String, BigDecimal>> entries = map.entrySet();
                    for (Map.Entry<String, BigDecimal> entry : entries) {
                        if (map2.get("area").toString().equals(entry.getKey())) {
                            BigDecimal fse = (BigDecimal) map2.get("fse");
                            map2.put("fse", fse.add(entry.getValue()));
                        }
                    }
                }
            }
        }*/

        String sql1 = "SELECT n.MingCheng as area,p.month as month,p.fse as fse from " +
                "c_account_network n " +
                "inner join ( " +
                "SELECT ex.YWWD as area, month(sd.jzrq) as month, sum(sd.fse) as fse " +
                "FROM st_collection_unit_business_details sd  " +
                "JOIN c_collection_unit_remittance_vice cv ON cv.DWYWMX = sd.id " +
                "JOIN c_collection_unit_business_details_extension ex ON sd.extenstion = ex.id " +
                "WHERE " +
                "sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :left " +
                "AND sd.jzrq <  :right " +
                "AND cv.HJFS = '01' " +
                "GROUP BY ex.YWWD, month(sd.jzrq))as p on n.id = p.area";

        List<Map> hj_res = getSession().createNativeQuery(sql1)
                .setParameter("left", leftStr)
                .setParameter("right", rightStr)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        String sql2 = "SELECT " +
                "n.MingCheng AS area, " +
                "p.month AS month, " +
                "p.fse AS fse " +
                "FROM c_account_network n " +
                "INNER JOIN( " +
                "SELECT  " +
                "ex.YWWD as area, MONTH(sd.JZRQ) as month, SUM(sd.FSE) as fse " +
                "FROM " +
                "st_collection_unit_business_details sd " +
                "JOIN c_collection_unit_payback_vice cv ON cv.DWYWMX = sd.id " +
                "JOIN c_collection_unit_business_details_extension ex ON sd.extenstion = ex.id " +
                "WHERE " +
                "sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :left " +
                "AND sd.jzrq < :right " +
                "AND cv.BJFS = '01' " +
                "GROUP BY " +
                "ex.YWWD, " +
                "MONTH (sd.jzrq)) as p ON n.id = p.area";

        List<Map> bj_res = getSession().createNativeQuery(sql2)
                .setParameter("left", leftStr)
                .setParameter("right", rightStr)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        String left_jx = year + "-06-01";
        String right_jx = year + "-08-01";

        String sql3 = "SELECT " +
                "n.MingCheng AS area, " +
                "p.month AS month, " +
                "p.fse AS fse " +
                "FROM c_account_network n " +
                "INNER JOIN ( " +
                "SELECT " +
                "ex.ywwd AS area, " +
                "MONTH (a.jzrq) AS month, " +
                "sum(a.fse) AS fse " +
                "FROM " +
                "st_collection_personal_business_details a " +
                "JOIN c_collection_personal_business_details_extension ex ON a.extension = ex.id " +
                "WHERE " +
                "a.GJHTQYWLX = '09' " +
                "AND a.jzrq >= :left " +
                "AND a.jzrq < :right " +
                "AND a.deleted = 0 " +
                "GROUP BY " +
                "ex.ywwd, " +
                "MONTH (a.jzrq)) as p ON p.area = n.id;";
        List<Map> jx_res = getSession().createNativeQuery(sql3)
                .setParameter("left", left_jx)
                .setParameter("right", right_jx)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        String sql4 = "SELECT " +
                "n.MingCheng AS area, " +
                "p.month AS month, " +
                "p.fse AS fse " +
                "FROM c_account_network n " +
                "INNER JOIN ( " +
                "SELECT " +
                "ex.KHWD AS area, " +
                "MONTH (fr.created_at) AS month, " +
                "sum(FSE) AS fse " +
                "FROM " +
                "c_finance_record_unit fr " +
                "JOIN st_common_unit unit ON fr.DWZH = unit.DWZH " +
                "JOIN c_common_unit_extension ex ON unit.extension = ex.id " +
                "WHERE " +
                "ZJLY IN ('汇补缴', '暂收分摊') " +
                "AND fr.created_at >= :left " +
                "AND fr.created_at < :right " +
                "GROUP BY " +
                "ex.KHWD, " +
                "MONTH (fr.created_at)) as p ON p.area = n.id";

        List<Map> wft_res = getSession().createNativeQuery(sql4)
                .setParameter("left", leftStr)
                .setParameter("right", rightStr)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        List<Map> final_res = new ArrayList<>();

        String sql = "select wd.MingCheng from CAccountNetwork  wd " +
                "where wd.id not in ('0') " +
                "and wd.deleted = false ";

        List<String> ywwds = getSession().createQuery(sql, String.class)
                .list();

        for (int i = 1; i < 13; i++) {
            for (String ywwd : ywwds) {
                Map final_map = new HashMap();
                final_map.put("month", i);
                final_map.put("area", ywwd);
                final_map.put("fse", BigDecimal.ZERO);
                for (Map hj_map : hj_res) {
                    if (i == (int)hj_map.get("month") && ywwd.equals(hj_map.get("area"))) {
                        final_map.put("fse", ((BigDecimal) final_map.getOrDefault("fse", BigDecimal.ZERO)).add((BigDecimal) hj_map.get("fse")));
                    }
                }
                for (Map bj_map : bj_res) {
                    if (i == (int)bj_map.get("month") && ywwd.equals(bj_map.get("area"))) {
                        final_map.put("fse", ((BigDecimal) final_map.getOrDefault("fse", BigDecimal.ZERO)).add((BigDecimal) bj_map.get("fse")));
                    }
                }
                for (Map jx_map : jx_res) {
                    if (i == (int)jx_map.get("month") && ywwd.equals(jx_map.get("area"))) {
                        final_map.put("fse", ((BigDecimal) final_map.getOrDefault("fse", BigDecimal.ZERO)).add((BigDecimal) jx_map.get("fse")));
                    }
                }
                for (Map wft_map : wft_res) {
                    if (i == (int)wft_map.get("month") && ywwd.equals(wft_map.get("area"))) {
                        final_map.put("fse", ((BigDecimal) final_map.getOrDefault("fse", BigDecimal.ZERO)).add((BigDecimal) wft_map.get("fse")));
                    }
                }
                final_res.add(final_map);
            }
        }

        return final_res;
    }

    //获取全市各地区提取总额
    @Override
    public List<Map> getCityWithdrawl(int year) {
        String sql = "select new map(t.cCollectionUnitBusinessDetailsExtension.ywwd.MingCheng as area, Month(t.jzrq) as month, abs(sum(t.fse)) + abs(sum(t.fslxe)) as fse) " +
                "from StCollectionUnitBusinessDetails t " +
                "where Year(t.jzrq) = :year and t.ywmxlx in ('11','12') " +
                "group by t.cCollectionUnitBusinessDetailsExtension.ywwd.MingCheng, Month(t.jzrq)";

        Date left = DateUtil.safeStr2Date("yyyy-MM-dd", year + "-01-01");
        Date right = DateUtil.safeStr2Date("yyyy-MM-dd", year + 1 + "-01-01");

        String sql2 = "select new map(wk.MingCheng as area, Month(t.jzrq) as month, (abs(sum(t.fse))) as fse) " +
                "from StCollectionPersonalBusinessDetails t " +
                "left join CCollectionPersonalBusinessDetailsExtension ex on t.cCollectionPersonalBusinessDetailsExtension = ex.id " +
                "left join CAccountNetwork wk on ex.ywwd = wk.id " +
                "where " +
                "t.jzrq >= :left and t.jzrq < :right and t.gjhtqywlx in ('11','12') and ex.step = :ywzt and t.deleted = false " +
                "group by wk.MingCheng, Month(t.jzrq)";

        List<Map> result = getSession().createQuery(sql2, Map.class)
//                .setParameter("year", year)
                .setParameter("ywzt", CollectionBusinessStatus.已入账.getName())
                .setParameter("left", left)
                .setParameter("right", right)
                .list();

        return result;
    }

    //获取全市各地区贷款总额
    @Override
    public List<Map> getCityLoan(int year) {

        String sql = "select new map(wk.MingCheng as area,Month(t.jzrq) as month, sum(t.fse) as fse) "+
                     "from StHousingBusinessDetails t " +
                     "inner join CLoanHousingBusinessProcess p on t.grywmx = p.id " +
                     "left join CAccountNetwork wk on p.ywwd = wk.id " +
                     "where " +
                     "t.jzrq >= :start and t.jzrq < :end and t.dkywmxlx = '01' "+
                     "group by wk.MingCheng,Month(t.jzrq)";


        List<Map> result = getSession().createQuery(sql, Map.class)
//                .setParameter("year", year)
//                .setParameter("ywzt", LoanBussinessStatus.已入账.getName())
                .setParameter("start", DateUtil.safeStr2Date("yyyy-MM-dd hh:mm:ss",(year)+"-01-01 00:00:00")).setParameter("end",DateUtil.safeStr2Date("yyyy-MM-dd hh:mm:ss",(year+1)+"-01-01 00:00:00"))
                .list();

        return result;
    }

    //获取全市各地区还款总额
    @Override
    public List<Map> getCityRepament(int year) {

        String sql = "select new map(t.grywmx.ywwd.MingCheng as area, Month(t.jzrq) as month, sum(t.fse) as fse) " +
                "from StHousingBusinessDetails t " +
                "where Year(t.jzrq) = :year and t.dkywmxlx  in ('03','02','04','06') " +
                "group by t.grywmx.ywwd.MingCheng, Month(t.jzrq)";

        Date left = DateUtil.safeStr2Date("yyyy-MM-dd", year + "-01-01");
        Date right = DateUtil.safeStr2Date("yyyy-MM-dd", year + 1 + "-01-01");

        String sql2 = "select new map(wk.MingCheng as area, Month(t.jzrq) as month, sum(t.bjje) as fse) " +
                "from StHousingBusinessDetails t " +
                "inner join CHousingBusinessDetailsExtension p on t.cHousingBusinessDetailsExtension = p.id " +
                "left join CAccountNetwork wk on p.ywwd = wk.id " +
                "where " +
                "t.jzrq >= :left and t.jzrq < :right and t.dkywmxlx in ('03','02','04','05','06') and p.ywzt = :ywzt " +
                "group by wk.MingCheng, Month(t.jzrq)";

        List<Map> result = getSession().createQuery(sql2, Map.class)
//                .setParameter("year", year)
                .setParameter("ywzt", LoanBussinessStatus.已入账.getName())
                .setParameter("left", left)
                .setParameter("right", right)
                .list();

        return result;
    }

    //获取全市各地区暂收未分摊
    public List<Map> getCityZSWFT() {

        String sql = "SELECT " +
                "\tue.KHWD, " +
                "\tSUM(ae.ZSYE) " +
                "FROM " +
                "\tst_common_unit u " +
                "INNER JOIN c_common_unit_extension ue ON u.extension = ue.id " +
                "INNER JOIN st_collection_unit_account acct ON u.CollectionUnitAccount = acct.id " +
                "INNER JOIN c_collection_unit_account_extension ae ON acct.extenstion = ae.id " +
                "GROUP BY " +
                "\tue.KHWD";

        String hql =
                "select new map (ue.khwd as khwd, sum(ae.zsye) as zsye) " +
                        "from StCommonUnit u " +
                        "inner join CCommonUnitExtension ue on u.cCommonUnitExtension = ue.id " +
                        "inner join StCollectionUnitAccount acct on u.collectionUnitAccount = acct.id " +
                        "inner join CCollectionUnitAccountExtension ae on acct.cCollectionUnitAccountExtension = ae.id " +
                        "group by ue.khwd";

        List<Map> result = getSession().createQuery(hql, Map.class).list();

        return result;
    }

    @Override
    public List<String> getYWWDs() {

        String sql = "select wd.MingCheng from CAccountNetwork  wd " +
                "where wd.id not in ('0') " +
                "and wd.deleted = false ";

        List<String> result = getSession().createQuery(sql, String.class)
                .list();

        return result;
    }

    @Override
    public String savePerRadix(StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String id = null;
        try {
            session.save(stCollectionUnitBusinessDetails);
            session.flush();
            session.refresh(stCollectionUnitBusinessDetails);
            id = stCollectionUnitBusinessDetails.getId();
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            session.close();
        }
        return id;
    }

    @Override
    public List<String> detailsList(String dwzh) {
        String sql = "SELECT  pe.STEP from st_collection_unit_business_details details  INNER JOIN  c_collection_unit_business_details_extension pe" +
                " ON details.extenstion=pe.id WHERE details.DWZH=" + dwzh + " AND pe.CZMC='10' AND details.deleted=0";
        List<String> list = getSession().createNativeQuery(sql).list();
        return list;
    }

    //下面是报表数据

    @Override
    public BigInteger getBQSJDWS(String cxny) {
        String sql = "select count(1) from  " +
                "( " +
                "SELECT DISTINCT dwzh from st_collection_unit_business_details a " +
                "join c_collection_unit_business_details_extension b on a.extenstion = b.id " +
                "where a.ywmxlx in ('01','02') " +
                "and a.jzrq >= :start and a.jzrq < :end " +
                "and b.STEP = '已入账分摊' " +
                ") c ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQSJZGRS(String cxny) {
        String sql = "select COUNT(1) from " +
                "( " +
                "select DISTINCT grzh from st_collection_personal_business_details a " +
                "join c_collection_personal_business_details_extension b on a.extension = b.id " +
                "where a.GJHTQYWLX in ('01','02') " +
                "and a.jzrq >= :start and a.jzrq < :end " +
                "and b.STEP = '办结' " +
                ") c ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }


    @Override
    public BigDecimal getBQSJCE(String cxny) {
//        直接汇缴：
        String sql = "SELECT " +
                "sum(sd.fse) " +
                "FROM " +
                "st_collection_unit_business_details sd " +
                "JOIN c_collection_unit_remittance_vice cv ON cv.DWYWMX = sd.id " +
                "WHERE " +
                "sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :start " +
                "AND sd.jzrq <= :end " +
                "AND cv.HJFS = '01' ";
        BigDecimal result = (BigDecimal) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        BigDecimal fse = result == null ? BigDecimal.ZERO : result;
//        直接补缴：
        String sql4 = "SELECT " +
                "sum(sd.fse) " +
                "FROM " +
                "st_collection_unit_business_details sd " +
                "JOIN c_collection_unit_payback_vice cv ON cv.DWYWMX = sd.id " +
                "WHERE " +
                "sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :start " +
                "AND sd.jzrq <= :end " +
                "AND cv.BJFS = '01'";
        BigDecimal result4 = (BigDecimal) getSession().createSQLQuery(sql4)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        BigDecimal fse4 = result4 == null ? BigDecimal.ZERO : result4;


        //本期实缴额需要加上结息的金额
        String sql2 = "select sum(a.fse) from " +
                "st_collection_personal_business_details a " +
                "where a.GJHTQYWLX = '09' " +
                "and a.jzrq >= :start " +
                "and a.jzrq <= :end " +
                "and a.deleted = 0";
        Object jxhj = getSession().createSQLQuery(sql2)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .uniqueResult();

        //未分摊增加额
        String sql3 = "SELECT " +
                "sum(FSE) " +
                "FROM " +
                "c_finance_record_unit " +
                "WHERE " +
                "ZJLY IN ('汇补缴', '暂收分摊') " +
                "AND created_at >= :start " +
                "AND created_at <= :end " +
                "AND deleted=0 ";
        Object wft = getSession().createSQLQuery(sql3)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .uniqueResult();

        BigDecimal nzjx = jxhj == null ? BigDecimal.ZERO : (BigDecimal) jxhj;
        BigDecimal wfte = wft == null ? BigDecimal.ZERO : (BigDecimal) wft;
        return fse.add(nzjx).add(wfte).add(fse4);
    }

    @Override
    public BigDecimal getBQGRTQE(String cxny) {
        String sql = "select sum(-fse) from st_collection_personal_business_details a " +
                "join c_collection_personal_business_details_extension b on a.extension = b.id " +
                "where a.GJHTQYWLX in ('11','12') " +
                "and b.STEP = '已入账' " +
                "and a.deleted = 0 " +
                "and a.jzrq >= :start and a.jzrq < :end ";

        String kssj = DateUtil.date2Str(getStart(cxny), "yyyy-MM-dd");
        String jssj = DateUtil.date2Str(getEnd(cxny), "yyyy-MM-dd");
        BigDecimal result = (BigDecimal) getSession().createSQLQuery(sql)
                .setParameter("start", kssj)
                .setParameter("end", jssj)
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQDWZHKHS(String cxny) {
        String sql = "select count(1) from st_common_unit unit " +
                "where unit.DWKHRQ >= :start and unit.DWKHRQ < :end ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQDWZHXHS(String cxny) {
        String sql = "select count(1) from st_collection_unit_business_details a " +
                "join c_collection_unit_business_details_extension b on a.extenstion = b.id " +
                "where ywmxlx = '72' and b.bjsj >= :start and b.bjsj < :end ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQGRZHKHS(String cxny) {
        String sql = "select count(1) from st_collection_personal_account acct " +
                "where acct.KHRQ >= :start " +
                "and acct.KHRQ < :end ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQGRZHXHS(String cxny) {
        String sql = "select count(1) from st_collection_personal_account" +
                " where xhrq >= :start " +
                "and xhrq < :end ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQLJTQZGS(String cxny) {
        String sql = "select count(1) from ( " +
                "select distinct grzh from st_collection_personal_business_details a " +
                "join c_collection_personal_business_details_extension b on a.extension = b.id " +
                "where a.GJHTQYWLX in ('11','12') and b.step = '已入账' " +
                "and a.jzrq < :end " +
                "and a.deleted = 0 " +
                ") c ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQMDWZHS(String cxny) {
        String sql = "select count(1) from st_collection_unit_account where created_at < :end and dwzhzt != '04' ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQMGRZHS(String cxny) {
        String sql = "select count(1) from st_collection_personal_account where created_at < :end and grzhzt in ('01','02')";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQMFCZHF(String cxny) {
        String sql = "select count(1) from st_collection_personal_account where grzhzt = '02' and xhrq is null";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .list().get(0);
        return result;
    }

    @Override
    public BigInteger getBQMLJGRZHXHS(String cxny) {
        String sql = "select count(1) from st_collection_personal_account where " +
                "xhrq < :end ";
        BigInteger result = (BigInteger) getSession().createSQLQuery(sql)
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        return result;
    }

    @Override
    public String[] getUnitClassificationInformation(String cxny, String code) {

        /*String sql = "select sum(bus.fse) from st_collection_unit_business_details bus   " +
                "join st_common_unit a on bus.dwzh = a.dwzh " +
                "join c_common_unit_extension c on a.extension = c.id " +
                "where bus.jzrq >= :start and bus.jzrq < :end " +
                "and bus.ywmxlx in ('01','02') ";*/
        String sql = "SELECT sum(r.FSE) FROM c_finance_record_unit r " +
                "join st_common_unit a on r.dwzh = a.dwzh " +
                "join c_common_unit_extension c on a.extension = c.id " +
                "WHERE r.ZJLY IN ('汇补缴', '暂收分摊') " +
                "AND r.created_at >= :start " +
                "AND r.created_at <= :end " +
                "AND r.deleted=0 ";

        String sql2 = "SELECT sum(sd.fse) " +
                "FROM st_collection_unit_business_details sd " +
                "JOIN c_collection_unit_payback_vice cv ON cv.DWYWMX = sd.id  " +
                "join st_common_unit a on sd.dwzh = a.dwzh  " +
                "join c_common_unit_extension c on a.extension = c.id " +
                "WHERE sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :start " +
                "AND sd.jzrq <= :end " +
                "AND cv.BJFS = '01'";

        String sql3 = "SELECT sum(sd.fse) " +
                "FROM st_collection_unit_business_details sd " +
                "JOIN c_collection_unit_remittance_vice cv ON cv.DWYWMX = sd.id " +
                "join st_common_unit a on sd.dwzh = a.dwzh " +
                "join c_common_unit_extension c on a.extension = c.id " +
                "WHERE sd.ywmxlx IN ('01', '02') " +
                "AND sd.jzrq >= :start " +
                "AND sd.jzrq <= :end " +
                "AND cv.HJFS = '01' ";

        String sql4 = "";
        if ("100".equals(code)) {

        } else if ("101".equals(code)) {
            sql4 = "and c.dwlb in ('1','2') ";
        } else if ("102".equals(code)) {
            sql4 = "and c.dwlb = '3' ";
        } else if ("106".equals(code)) {
            sql4 = "and c.dwlb in ('4','5') ";
        } else if ("107".equals(code)) {
            sql4 = "and c.dwlb = '6' ";
        } else {
            return new String[]{"0", "0"};
        }

        sql = sql + sql4;
        sql2 = sql2 + sql4;
        sql3 = sql3 + sql4;

        String grsql = "SELECT" +
                " count( tmp.grzh )" +
                " FROM" +
                " (" +
                " SELECT" +
                " scpbd.GRZH AS grzh" +
                " FROM" +
                " st_collection_personal_business_details scpbd" +
                " INNER JOIN st_common_unit scu ON scu.id = scpbd.Unit" +
                " JOIN c_common_unit_extension c ON scu.extension = c.id" +
                " WHERE" +
                " scpbd.created_at >= :start" +
                " AND scpbd.created_at < :end" +
                " AND scpbd.deleted = 0" +
                " AND scpbd.GJHTQYWLX IN ( '01', '02' ) " +
                sql4 +
                " GROUP BY " +
                " scpbd.GRZH" +
                ") tmp";
        //财务暂收分摊分类
        BigDecimal result = (BigDecimal) getSession().createSQLQuery(sql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        if (result == null) {
            result = BigDecimal.ZERO;
        }
        //补缴转账分类
        BigDecimal result2 = (BigDecimal) getSession().createSQLQuery(sql2)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        if (result2 == null) {
            result2 = BigDecimal.ZERO;
        }
        //汇缴转账分类
        BigDecimal result3 = (BigDecimal) getSession().createSQLQuery(sql3)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        if (result3 == null) {
            result3 = BigDecimal.ZERO;
        }

        result = result.add(result2).add(result3);

        BigInteger result4 = (BigInteger) getSession().createSQLQuery(grsql)
                .setParameter("start", getStart(cxny))
                .setParameter("end", getEnd(cxny))
                .list().get(0);
        if (result4 == null) {
            result4 = BigInteger.ZERO;
        }

        return new String[]{String.valueOf(result4), String.valueOf(result)};
    }

    //工具方法
    private Date getEnd(String cxny) {
        String nextMonth = getNextMonth(cxny);
        return parseToDate(nextMonth, "yyyy-MM");
        //parseToString(end,"yyyy-MM-dd HH:mm:ss");
    }

    private Date getStart(String cxny) {
        return parseToDate(cxny, "yyyy-MM");
    }

    public String getNextMonth(String repeatDate) {
        String lastMonth = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        int year = Integer.parseInt(repeatDate.substring(0, 4));
        String monthsString = repeatDate.substring(5, 7);
        int month;
        if ("0".equals(monthsString.substring(0, 1))) {
            month = Integer.parseInt(monthsString.substring(1, 2));
        } else {
            month = Integer.parseInt(monthsString.substring(0, 2));
        }
        cal.set(year, month, Calendar.DATE);
        lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }

    public String parseToString(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public Date parseToDate(String date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new ErrorException("时间格式错误，时间格式应为为：" + format);
        }
    }

    private String removeLine(String str) {
        return str.replace("-", "");
    }
}
