package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.model.DetailsBackRepayment;
import com.handge.housingfund.common.service.loan.model.OvderDueRecord;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAccountEmployeeDAO;
import com.handge.housingfund.database.dao.IStHousingBusinessDetailsDAO;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class StHousingBusinessDetailsDAO extends BaseDAO<StHousingBusinessDetails>
        implements IStHousingBusinessDetailsDAO {
    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;
    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void deleteId(String id) {
        StHousingBusinessDetails stHousingBusinessDetails = getSession().get(StHousingBusinessDetails.class, id);
        stHousingBusinessDetails.getGrywmx().getStHousingBusinessDetails().remove(stHousingBusinessDetails);
        stHousingBusinessDetails.setGrywmx(null);
        this.getSession().delete(stHousingBusinessDetails);
        this.getSession().flush();
    }

    @Override
    public String getPch(String pch) {
        String sql = "SELECT pe.pch from st_housing_business_details details ,c_housing_business_details_extension pe where" +
                " details.extenstion=pe.id and pe.PCH = :pch AND details.deleted=0";
        List<String> str = getSession().createNativeQuery(sql).setParameter("pch", pch).list();
        if (str.size() != 0) return str.get(0);
        return null;
    }

    @Override
    public String getJzph(String pch) {

        String sql = "select process.JZPZH from st_housing_business_details details , c_loan_housing_business_process  process,c_housing_business_details_extension pe" +
                " where details.grywmx=process.id  and pe.id=details.extenstion AND pe.PCH =:pch";
        List<String> pzh = getSession().createNativeQuery(sql).setParameter("pch",pch).list();
        if (pzh.size() > 0) return pzh.get(0);
        return null;
    }

    @Override
    public void deleteBackLoanDuction(Session session, String id) {
        String sql = "DELETE details,ex FROM st_housing_business_details details INNER JOIN c_housing_business_details_extension ex ON  details.extenstion=ex.id" +
                " WHERE" +
                " details.id =:id";
        session.createNativeQuery(sql).setParameter("id", id).executeUpdate();
    }

    @Override
    public List<StHousingBusinessDetails> getList() {
        return getSession().createCriteria(StHousingBusinessDetails.class)
                .add(Restrictions.eq("dkywmxlx", LoanBusinessType.正常还款.getCode()))
                .add(Restrictions.eq("deleted", false)).list();
    }

    @Override
    public void updateBatch(List<StHousingBusinessDetails> gBusinessDetails, String ywlsh, String id) {
        try {
            String sqlde = "INSERT INTO st_housing_business_details(id,created_at,deleted,deleted_at,updated_at,DKZH,YWLSH,DKYWMXLX,YWFSRQ,DKYHDM," +
                    "FSE,BJJE,LXJE,FXJE,DQQC,ZCZYQBJJE,YQZZCBJJE,JZRQ,extenstion,grywmx) VALUES ";

            String sqlex = "INSERT INTO c_housing_business_details_extension(id,created_at,deleted,deleted_at,updated_at,YWZT,SBYY,RGCL,XQFSE,XQBJJE,XQLXJE,XQDKYE,JKRXM" +
                    ",HKZH,ZHKHYHMC,ZHKHYHDM,YWWD,PCH,HKXH) VALUES ";
            StringBuilder sbde = new StringBuilder();
            StringBuilder sbex = new StringBuilder();
            sbde.append(sqlde);
            sbex.append(sqlex);
            int number = 0;
            for (StHousingBusinessDetails deatisl : gBusinessDetails) {
                number++;
                String details = UUID.randomUUID().toString().replaceAll("-", "");
                String extension = UUID.randomUUID().toString().replaceAll("-", "");
                if (number == (gBusinessDetails.size())) {
                    sbde.append("(" + "'" + details + "'" + "," + "'" + new Timestamp(new Date().getTime()) + "'" + "," + 0 + "," + null
                            + "," + null + "," + "'" + deatisl.getDkzh() + "'" + "," + "'" + ywlsh + "'" + "," + "'" + deatisl.getDkywmxlx() + "'" + "," + "'" + new Timestamp(deatisl.getYwfsrq().getTime()) + "'"
                            + "," + "'" + deatisl.getDkyhdm() + "'" + "," + deatisl.getFse() + "," + deatisl.getBjje() + "," + deatisl.getLxje() + "," + deatisl.getFxje()
                            + "," + deatisl.getDqqc() + "," + BigDecimal.ZERO + "," + BigDecimal.ZERO + "," + null
                            + "," + "'" + extension + "'" + "," + "'" + id + "'" + ")");

                    sbex.append("(" + "'" + extension + "'" + "," + "'" + new Timestamp(new Date().getTime()) + "'" + "," + 0 + "," + null
                            + "," + null + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getYwzt() + "'"
                            + "," + null + "," + null
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqfse()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqbjje()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqlxje()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqdkye()
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getJkrxm() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getHkzh() + "'" + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getZhkhyhmc() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getZhkhyhdm() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getYwwd() + "'"
                            + "," + null
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getHkxh() + "'"+ ")");
                } else {

                    sbde.append("(" + "'" + details + "'" + "," + "'" + new Timestamp(new Date().getTime()) + "'" + "," + 0 + "," + null
                            + "," + null + "," + "'" + deatisl.getDkzh() + "'" + "," + "'" + ywlsh + "'" + "," + "'" + deatisl.getDkywmxlx() + "'" + "," + "'" + new Timestamp(deatisl.getYwfsrq().getTime()) + "'"
                            + "," + "'" + deatisl.getDkyhdm() + "'" + "," + deatisl.getFse() + "," + deatisl.getBjje() + "," + deatisl.getLxje() + "," + deatisl.getFxje()
                            + "," + deatisl.getDqqc() + "," + BigDecimal.ZERO + "," + BigDecimal.ZERO + "," + null
                            + "," + "'" + extension + "'" + "," + "'" + id + "'" + "),");

                    sbex.append("(" + "'" + extension + "'" + "," + "'" + new Timestamp(new Date().getTime()) + "'" + "," + 0 + "," + null
                            + "," + null + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getYwzt() + "'"
                            + "," + null + "," + null
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqfse()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqbjje()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqlxje()
                            + "," + deatisl.getcHousingBusinessDetailsExtension().getXqdkye()
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getJkrxm() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getHkzh() + "'" + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getZhkhyhmc() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getZhkhyhdm() + "'"
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getYwwd() + "'"
                            + "," + null
                            + "," + "'" + deatisl.getcHousingBusinessDetailsExtension().getHkxh() + "'"+ "),");
                }
            }
            String s = sbde.toString();
            String s1 = sbex.toString();
            getSession().createNativeQuery(sbde.toString()).executeUpdate();
            getSession().createNativeQuery(sbex.toString()).executeUpdate();
        } catch (Exception e) {
            throw new ErrorException(e);
        }

    }

    @Override
    public BigInteger getBuinessCount(String dkzh, BigDecimal dqqc) {
        String sql = "SELECT COUNT(*) FROM st_housing_business_details" +
                " WHERE" +
                " DKZH = :dkzh" +
                " AND DQQC = :dqqc" +
                " AND DKYWMXLX = :dkywmxlx" +
                " AND deleted =0";

        return (BigInteger) getSession().createNativeQuery(sql)
                .setParameter("dkzh", dkzh)
                .setParameter("dqqc", dqqc)
                .setParameter("dkywmxlx", LoanBusinessType.正常还款.getCode())
                .list().get(0);


    }

    @Override
    public List<OvderDueRecord> searchRecord() {
//        String sql2 = "SELECT over.id,over.DKZH,over.DQQC,due.YWZT FROM st_housing_business_details over " +
//                "INNER JOIN c_housing_business_details_extension due ON over.extenstion=due.id  WHERE over.deleted=0 AND over.dkywmxlx='02'";

        String sql2="SELECT over.id, over.DKZH, over.DQQC,due.YWZT FROM st_housing_business_details over INNER JOIN c_housing_business_details_extension due ON " +
                " over.extenstion = due.id JOIN ( SELECT de.DKZH, max( de.DQQC ) AS DQQC  FROM st_housing_business_details de INNER JOIN c_housing_business_details_extension ex ON " +
                " de.extenstion = ex.id  WHERE de.DKYWMXLX = '02'  AND de.deleted = 0 GROUP BY de.DKZH  ) AS a ON a.DKZH = over.DKZH  AND a.DQQC = over.DQQC " +
                " WHERE over.DKYWMXLX = '02' AND over.deleted = 0";

        List<Object[]> list = iAccountEmployeeDAO.list(sql2, new HashMap<String, Object>());
        List<OvderDueRecord> record = new ArrayList<>();
        OvderDueRecord over = null;
        for (Object[] reco : list) {
            over = new OvderDueRecord((String) reco[0], (String) reco[1], (BigDecimal) reco[2], (String) reco[3]);
            record.add(over);
        }
        return record;
    }

    @Override
    public List<DetailsBackRepayment> waitStatus(String dkzh, BigDecimal dqqc) {
        String sql = "SELECT  details.id,details.FSE,details.LXJE,details.BJJE,details.YWLSH  FROM st_housing_business_details details INNER JOIN c_housing_business_details_extension bex" +
                " ON bex.id=details.extenstion WHERE" +
                " details.DKZH =:dkzh" +
                " AND details.DQQC =:dqqc" +
                " AND details.DKYWMXLX='02'" +
                " AND  bex.YWZT='待入账'";
        HashMap<String, Object> map = new HashMap<>();
        map.put("dkzh", dkzh);
        map.put("dqqc", dqqc);
        List<Object[]> list = iAccountEmployeeDAO.list(sql, map);
        List<DetailsBackRepayment> record = new ArrayList<>();
        DetailsBackRepayment over = null;
        for (Object[] reco : list) {
            over = new DetailsBackRepayment((String) reco[0], (BigDecimal) reco[1], (BigDecimal) reco[2], (BigDecimal) reco[3], (String) reco[4]);
            record.add(over);
        }

        return record;
    }

    @Override
    public void updateDetails(String id) {
        String sql = "UPDATE st_housing_business_details details INNER JOIN c_housing_business_details_extension bex ON bex.id=details.extenstion " +
                " SET" +
                " details.JZRQ=NOW() " +
                " AND bex.YWZT='扣款已发送'" +
                " AND details.grywmx = :id" +
                " AND details.YWFSRQ=NOW()";
        getSession().createNativeQuery(sql).setParameter("id", id).executeUpdate();
    }

    @Override
    public String doesBusiness(String dkzh, BigDecimal dqqc) {
        String sql = "select  pe.YWZT FROM st_housing_business_details details  INNER JOIN c_housing_business_details_extension pe ON pe.id=details.extenstion" +
                " WHERE " +
                " details.deleted='0'" +
                " AND details.DKZH =:dkzh" +
                " AND details.DQQC =:dqqc" +
                " AND details.DKYWMXLX='02'";
//                " AND pe.YWZT  <> '已入账' AND pe.YWZT <> '逾期'";


        List list = getSession().createNativeQuery(sql)
                .setParameter("dkzh", dkzh)
                .setParameter("dqqc", dqqc).list();
        if (list.size() == 0) return null;
        else {
            return (String) list.get(0);
        }
    }

    @Override
    public BigInteger searchOverdueBuness(String dkzh, String sjqc) {

        String sql = "SELECT * from st_housing_business_details details INNER JOIN c_housing_business_details_extension" +
                " pe ON details.extenstion=pe.id" +
                " WHERE details.DKYWMXLX='04'" +
                "  AND details.DKZH =:dkzh" +
                " AND details.DQQC =:dqqc";
        return null;
    }

    @Override
    public BigDecimal getLoanBackTotal(boolean isYuQi, Date ks, Date js) {
        String sql = "SELECT SUM(sd.BJJE) FROM st_housing_business_details sd RIGHT JOIN c_housing_business_details_extension ce ON sd.extenstion=ce.id " +
                " WHERE sd.JZRQ>=:ks AND sd.JZRQ<=:js AND sd.deleted=0 AND ce.YWZT='已入账' AND " + (isYuQi ? "sd.DKYWMXLX='04'" : "sd.DKYWMXLX IN ('02','03','04','06')");
        Object o = getSession().createNativeQuery(sql)
                .setParameter("ks", DateUtil.date2Str(ks, "yyyy-MM-dd HH:mm:ss"))
                .setParameter("js", DateUtil.date2Str(js, "yyyy-MM-dd HH:mm:ss"))
                .list().get(0);

        return o != null ? (BigDecimal) o : BigDecimal.ZERO;
    }

    @Override
    public BigInteger getLoanBackCount(boolean isYuQi, Date ks, Date js) {
        String sql = "SELECT COUNT(*) FROM st_housing_business_details sd RIGHT JOIN c_housing_business_details_extension ce ON sd.extenstion=ce.id " +
                " WHERE sd.JZRQ>=:ks AND sd.JZRQ<=:js AND sd.deleted=0 AND ce.YWZT='已入账' AND " + (isYuQi ? "sd.DKYWMXLX='03'" : "sd.DKYWMXLX<>'01'");
        Object o = getSession().createNativeQuery(sql)
                .setParameter("ks", ks)
                .setParameter("js", js)
                .list().get(0);

        return o != null ? (BigInteger) o : BigInteger.ZERO;
    }

    @Override
    public StHousingBusinessDetails getByYWLSH(String YWLSH) {
        List<StHousingBusinessDetails> list = getSession().createCriteria(StHousingBusinessDetails.class)
                .add(Restrictions.eq("ywlsh", YWLSH))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }


}
