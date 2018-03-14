package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.loan.model.DetailsBackRepayment;
import com.handge.housingfund.common.service.loan.model.OvderDueRecord;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.StHousingBusinessDetails;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface IStHousingBusinessDetailsDAO extends IBaseDAO<StHousingBusinessDetails> {

    public void deleteId(String id);

    String getPch(String pch);

    String getJzph(String pch);

    void deleteBackLoanDuction(Session session, String id);

    List<StHousingBusinessDetails> getList();

    void updateBatch(List<StHousingBusinessDetails> gBusinessDetails, String ywlsh, String id);

    BigInteger getBuinessCount(String dkzh, BigDecimal dqqc);

    List<OvderDueRecord> searchRecord();

    List<DetailsBackRepayment> waitStatus(String dkzh, BigDecimal dqqc);

    void updateDetails(String id);

    String doesBusiness(String dkzh, BigDecimal dqqc);

    BigInteger searchOverdueBuness(String dkzh, String sjqc);

    /**
     * 查询贷款回收总金额
     * @param isYuQi 是否只查询逾期数据
     * @param ks
     * @param js
     * @return
     */
    BigDecimal getLoanBackTotal(boolean isYuQi, Date ks, Date js);

    BigInteger getLoanBackCount(boolean isYuQi, Date ks, Date js);

    StHousingBusinessDetails getByYWLSH(String YWLSH);

}