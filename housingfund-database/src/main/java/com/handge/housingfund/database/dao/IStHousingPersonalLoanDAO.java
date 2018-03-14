package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StHousingPersonalLoan;

public interface IStHousingPersonalLoanDAO extends IBaseDAO<StHousingPersonalLoan> {

    StHousingPersonalLoan getByDKZH(String dkzh);
}