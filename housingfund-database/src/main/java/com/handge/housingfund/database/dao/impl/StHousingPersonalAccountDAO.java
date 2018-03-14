package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IStHousingPersonalAccountDAO;
import com.handge.housingfund.database.entities.StHousingPersonalAccount;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class StHousingPersonalAccountDAO extends BaseDAO<StHousingPersonalAccount>
        implements IStHousingPersonalAccountDAO {


    @Override
    public StHousingPersonalAccount getByDkzh(String dkzh) {
        List<StHousingPersonalAccount> list = getSession().createCriteria(StHousingPersonalAccount.class)
                .add(Restrictions.eq("dkzh", dkzh))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void createOverAccountTemp() {
        String sql = "CREATE TEMPORARY TABLE accountOver(id VARCHAR(32) primary key,DKZHZT VARCHAR(2),YHQS decimal(4,0),DQJHHKJE decimal(18,2),DQJHGHBJ decimal(18,2),DQJHGHLX decimal(18,2),DQYHJE decimal(18,2)" +
                ",DQYHLX decimal(18,2),DQYHBJ decimal(18,2),LJYQQS decimal(18,2),YQBJZE decimal(18,2),YQLXZE decimal(18,2),DQQC decimal(4,0))";
        getSession().createNativeQuery(sql);
    }

    @Override
    public void insertOverdue(String ID, BigDecimal DQJHHKJE, BigDecimal DQJHGHBJ, BigDecimal DQJHGHLX, BigDecimal DQYHJE
            , BigDecimal DQYHLX, BigDecimal DQYHBJ, BigDecimal LJYQQS, BigDecimal YQBJZE, BigDecimal YQLXZE) {
//		String sql="INSERT accountOver(" +
//				", id = :id" +
//				", DQJHHKJE = :dqjhhkje " +
//				", DQJHGHBJ = :dqjhghbj " +
//				", DQJHGHLX = :dqjhghlx " +
//				", DQYHJE = :dqyhje " +
//				", DQYHLX = :dqyhlx " +
//				", DQYHBJ = :dqyhbj " +
//				", LJYQQS = :ljyqqs " +
//				", YQBJZE = :yqbjze " +
//				", YQLXZE = :yqlxze " +
//				"SELECT id,DQJHHKJE, DQJHGHBJ, DQJHGHLX, DQYHJE, DQYHLX, DQYHBJ, LJYQQS, YQBJZE, YQLXZE FROM st_housing_personal_account ";
//		getSession().createNativeQuery(sql)
//				.setParameter("id",ID)
//				.setParameter("dqjhhkje",DQJHHKJE)
//				.setParameter("dqjhghbj",DQJHGHBJ)
//				.setParameter("dqjhghlx",DQJHGHLX)
//				.setParameter("dqyhje",DQYHJE)
//				.setParameter("dqyhlx",DQYHLX)
//				.setParameter("dqyhbj",DQYHBJ)
//				.setParameter("ljyqqs",LJYQQS)
//				.setParameter("yqbjze",YQBJZE)
//				.setParameter("yqlxze",YQLXZE)
//				.executeUpdate();

        String sql = "INSERT accountOver(" +
                " id" +
                ", DQJHHKJE" +
                ", DQJHGHBJ" +
                ", DQJHGHLX" +
                ", DQYHJE" +
                ", DQYHLX" +
                ", DQYHBJ" +
                ", LJYQQS" +
                ", YQBJZE" +
                ", YQLXZE)" +
                " values( ?,?, ?, ?, ?, ?, ?, ?, ?,? )";
        getSession().createNativeQuery(sql)
                .setParameter(1, ID)
                .setParameter(2, DQJHHKJE)
                .setParameter(3, DQJHGHBJ)
                .setParameter(4, DQJHGHLX)
                .setParameter(5, DQYHJE)
                .setParameter(6, DQYHLX)
                .setParameter(7, DQYHBJ)
                .setParameter(8, LJYQQS)
                .setParameter(9, YQBJZE)
                .setParameter(10, YQLXZE)
                .executeUpdate();
    }

    @Override
    public void updateTemp() {
        String sql = "UPDATE c_loan_housing_person_information_basic as basic,st_housing_personal_account as account,accountOver over SET " +
                "basic.DKZHZT=2,account.DQJHHKJE=over.DQJHHKJE  WHERE over.id=basic.id AND basic.personalAccount=account.id";
        getSession().createNativeQuery(sql);
    }

    @Override
    public void updateOver(String ID, String DKZHZT,BigDecimal DQJHHKJE, BigDecimal DQJHGHBJ, BigDecimal DQJHGHLX, BigDecimal DQYHJE
            , BigDecimal DQYHBJ , BigDecimal DQYHLX, BigDecimal LJYQQS, BigDecimal YQBJZE, BigDecimal YQLXZE,BigDecimal DQQC,BigDecimal DKYEZCBJ) {
        String sql = "UPDATE " +
                " c_loan_housing_person_information_basic basic" +
                " INNER JOIN st_housing_personal_account account ON basic.personalAccount=account.id" +
                " INNER JOIN c_loan_housing_personal_account_extension exten ON account.extenstion=exten.id" +
                " SET " +
                " basic.DKZHZT = :dkzhzt " +
                ", account.DQJHHKJE  = :dqjhhkje " +
                ", account.DQJHGHBJ  = :dqjhghbj " +
                ", account.DQJHGHLX  = :dqjhghlx " +
                ", account.DQYHJE= :dqyhje " +
                ", account.DQYHBJ= :dqyhbj " +
                ", account.DQYHLX= :dqyhlx " +
                ", account.LJYQQS= :ljyqqs " +
                ", account.YQBJZE= :yqbjze " +
                ", account.YQLXZE= :yqlxze " +
                ", exten.DQQC= :dqqc " +
                ", exten.DKYEZCBJ= :dkyezcbj " +
                ", account.updated_at = now() " +
                ", basic.updated_at = now() " +
                ", basic.created_at = now() " +
                "WHERE basic.id = :id ";
        getSession().createSQLQuery(sql)
                .setParameter("id", ID)
                .setParameter("dkzhzt",DKZHZT)
                .setParameter("dqjhhkje", DQJHHKJE)
                .setParameter("dqjhghlx", DQJHGHLX)
                .setParameter("dqjhghbj", DQJHGHBJ)
                .setParameter("dqyhje", DQYHJE)
                .setParameter("dqyhbj", DQYHBJ)
                .setParameter("dqyhlx", DQYHLX)
                .setParameter("ljyqqs", LJYQQS)
                .setParameter("yqbjze", YQBJZE)
                .setParameter("yqlxze", YQLXZE)
                .setParameter("dqqc", DQQC)
                .setParameter("dkyezcbj", DKYEZCBJ)
                .executeUpdate();
    }

    @Override
    public void updateDecution(String ID, String DKZHZT,BigDecimal DKYE, BigDecimal DQJHHKJE, BigDecimal DQJHGHBJ, BigDecimal DQJHGHLX, BigDecimal DQYHJE
            , BigDecimal DQYHBJ, BigDecimal DQYHLX, BigDecimal HSLXZE, Date DKJJRQ, BigDecimal HSBJZE, BigDecimal DQQC, BigDecimal DKYEZCBJ) {

        String sqljq = "UPDATE " +
                " c_loan_housing_person_information_basic basic" +
                " INNER JOIN st_housing_personal_account account ON basic.personalAccount=account.id" +
                " INNER JOIN c_loan_housing_personal_account_extension exten ON account.extenstion=exten.id" +
                " SET " +
                " basic.DKZHZT = :dkzhzt " +
                ", account.DQJHHKJE  = :dqjhhkje " +
                ", account.DQJHGHBJ  = :dqjhghbj " +
                ", account.DQJHGHLX  = :dqjhghlx " +
                ", account.DQYHJE= :dqyhje " +
                ", account.DQYHBJ= :dqyhbj " +
                ", account.DQYHLX= :dqyhlx" +
                ", account.HSLXZE= :hslxze " +
                (DKJJRQ!=null&&!DKJJRQ.equals("") ?  ", account.DKJQRQ= :dkjjrq ":" ") +
                ", account.HSBJZE= :hsbjze " +
                ", exten.DQQC= :dqqc " +
                ", exten.DKYEZCBJ= :dkyezcbj " +
                ", account.updated_at = now() " +
                ", basic.updated_at = now() " +
                ", basic.created_at = now() " +
                "WHERE basic.id = :id ";
        NativeQuery sqlQuery = getSession().createSQLQuery(sqljq);

        sqlQuery.setParameter("id", ID);
        sqlQuery.setParameter("dkzhzt",DKZHZT);
        sqlQuery.setParameter("dqjhhkje", DQJHHKJE);
        sqlQuery.setParameter("dqjhghlx", DQJHGHLX);
        sqlQuery.setParameter("dqjhghbj", DQJHGHBJ);
        sqlQuery.setParameter("dqyhje", DQYHJE);
        sqlQuery.setParameter("dqyhbj", DQYHBJ);
        sqlQuery.setParameter("dqyhlx", DQYHLX);
        sqlQuery.setParameter("hslxze", HSLXZE);
        if(DKJJRQ!=null&&!DKJJRQ.equals("")) {
            sqlQuery.setParameter("dkjjrq", DKJJRQ);
        }
        sqlQuery.setParameter("hsbjze", HSBJZE);
        sqlQuery.setParameter("dqqc", DQQC);
        sqlQuery.setParameter("dkyezcbj", DKYEZCBJ);
        sqlQuery.executeUpdate();
    }

    @Override
    public void flush() {
        getSession().flush();
        getSession().clear();
    }


}
