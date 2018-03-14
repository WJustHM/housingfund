package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.model.HousingCompanyReviewResHousingCompanyReview;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.ICLoanHousingBusinessProcessDAO;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class CLoanHousingBusinessProcessDAO extends BaseDAO<CLoanHousingBusinessProcess> implements ICLoanHousingBusinessProcessDAO {

    @Override
    public ArrayList<HousingCompanyReviewResHousingCompanyReview> getReviewListByFKGS(PageRes pageRes, String FKGS, String STEP, String CZNR) {

        try {

            String hql = "select process.ywlsh,process.czy,process.ywwd,company.fkgs,company.sjflb,process.loanHousingCompanyVice.fkgs,process.loanHousingCompanyVice.sjflb from CLoanHousingBusinessProcess as process, CLoanHousingCompanyBasic as company " +
                    "where process.step = :step " +
                    "and ((process.cznr = :bg " +
                    "and process.loanHousingCompanyVice.fkgszh = company.fkgszh " +
                    "and company.fkgs like :fkgs) " +
                    "or (process.cznr = :sl " +
                    "and process.loanHousingCompanyVice.fkgs like :fkgs))";

            List<Object[]> resuts = getSession().createQuery(hql)
                    .setParameter("step", STEP)
                    .setParameter("bg", LoanBusinessType.房开变更.getCode())
                    .setParameter("sl", LoanBusinessType.新建房开.getCode())
                    .setParameter("fkgs", "%" + FKGS + "%")
                    .setFirstResult((pageRes.getCurrentPage() - 1) * pageRes.getPageSize())
                    .setMaxResults(pageRes.getPageSize())
                    .list();

            pageRes.setTotalCount(resuts.size());
            pageRes.setNextPageNo(pageRes.getCurrentPage() + 1);
            pageRes.setPageCount((resuts.size() / pageRes.getPageSize()) + 1);

            ArrayList<HousingCompanyReviewResHousingCompanyReview> reviews = new ArrayList<>();
            for (Object[] result : resuts) {
                HousingCompanyReviewResHousingCompanyReview review = new HousingCompanyReviewResHousingCompanyReview();
                review.setYWLSH(result[0] == null ? null : (String) result[0]);
                review.setCZY(result[1] == null ? null : (String) result[1]);
                review.setYWWD(result[2] == null ? null : (String) result[2]);
                if (result[3] != null && result[4] != null) {
                    review.setFKGS((String) result[3]);
                    review.setSJFLB((String) result[4]);
                } else {
                    if (result[5] != null && result[6] != null) {
                        review.setFKGS((String) result[5]);
                        review.setSJFLB((String) result[6]);
                    }
                }
                reviews.add(review);
            }

            return reviews;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public CLoanHousingBusinessProcess getByYWLSH(String YWLSH) {
        List<CLoanHousingBusinessProcess> list = getSession().createCriteria(CLoanHousingBusinessProcess.class)
                .add(Restrictions.eq("ywlsh", YWLSH))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getLoanBuniess(String ywlsh) {
        List<CLoanHousingBusinessProcess> list = getSession().createCriteria(CLoanHousingBusinessProcess.class)
                .add(Restrictions.eq("ywlsh", ywlsh))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) {
            return list.get(0).getCznr();
        }
        return null;
    }

    @Override
    public void updateYwlsh(String id, String YWLSH) {
        String sql = "UPDATE st_housing_business_details details" +
                " INNER JOIN" +
                " c_loan_housing_business_process pro ON" +
                " details.grywmx = :id" +
                " SET" +
                " details.YWLSH = :ywlsh";

        getSession().createNativeQuery(sql)
                .setParameter("ywlsh", YWLSH)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Object> countRepament() {
//        String sql="SELECT count(*) from c_loan_housing_business_process pro WHERE pro.CZNR in ('03','06') and pro.STEP = '扣款已发送' and pro.DKZH ='"+dkzh+"'";
        String sql2 = "SELECT pro.DKZH from c_loan_housing_business_process pro WHERE pro.CZNR in ('03','06') and pro.STEP = '扣款已发送'";
//        return (BigInteger) getSession().createNativeQuery(sql)
//                .list().get(0);
        List<Object> list = getSession().createNativeQuery(sql2).list();
        return list;
    }

    @Override
    public CLoanHousingBusinessProcess getByYWLSHJKRZJHM(String YWLSH, String JKRZJHM) {
        List<CLoanHousingBusinessProcess> list = getSession().createCriteria(CLoanHousingBusinessProcess.class)
                .add(Restrictions.eq("ywlsh", YWLSH))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) return list.get(0);
        for (CLoanHousingBusinessProcess pro : list) {
            if (null != pro.getLoanHousingPersonInformationVice() && pro.getLoanHousingPersonInformationVice().getJkrzjhm().equals(JKRZJHM)) {
                return pro;
            }
        }
        return null;
    }
}
