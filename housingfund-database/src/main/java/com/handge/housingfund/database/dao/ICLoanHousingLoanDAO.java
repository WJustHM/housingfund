package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CLoanHousingLoan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/14.
 */
public interface ICLoanHousingLoanDAO extends IBaseDAO<CLoanHousingLoan> {


    public BigDecimal getLoanDkffeTotal(Date ks, Date js);

    public Long getLoanDkffeCount(Date ks, Date js);

}
