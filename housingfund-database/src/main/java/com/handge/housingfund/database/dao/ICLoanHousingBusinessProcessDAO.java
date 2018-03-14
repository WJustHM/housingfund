package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.model.HousingCompanyReviewResHousingCompanyReview;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public interface ICLoanHousingBusinessProcessDAO extends IBaseDAO<CLoanHousingBusinessProcess>{

    public ArrayList<HousingCompanyReviewResHousingCompanyReview> getReviewListByFKGS(PageRes pageRes, String FKGS, String STEP, String CZNR);

    public CLoanHousingBusinessProcess getByYWLSH(String YWLSH);

    public String getLoanBuniess(String ywlsh);

    public void updateYwlsh(String id,String YWLSH);

    public List<Object> countRepament();

    public CLoanHousingBusinessProcess getByYWLSHJKRZJHM(String YWLSH,String JKRZJHM);
}
