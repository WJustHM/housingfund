package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StHousingPersonalAccount;

import java.math.BigDecimal;
import java.util.Date;

public interface IStHousingPersonalAccountDAO extends IBaseDAO<StHousingPersonalAccount> {

    StHousingPersonalAccount  getByDkzh(String dkzh);


void createOverAccountTemp();


void insertOverdue(String ID,BigDecimal DQJHHKJE,BigDecimal DQJHGHBJ,BigDecimal DQJHGHLX,BigDecimal DQYHJE,
                BigDecimal DQYHLX,BigDecimal DQYHBJ,BigDecimal LJYQQS,BigDecimal YQBJZE,BigDecimal YQLXZE);

void updateTemp();

void updateOver(String ID, String DKZHZT, BigDecimal DQJHHKJE, BigDecimal DQJHGHBJ, BigDecimal DQJHGHLX, BigDecimal DQYHJE
        , BigDecimal DQYHBJ , BigDecimal DQYHLX, BigDecimal LJYQQS, BigDecimal YQBJZE, BigDecimal YQLXZE,BigDecimal DQQC,BigDecimal DKYEZCBJ);

    void updateDecution(String ID, String DKZHZT,BigDecimal DKYE, BigDecimal DQJHHKJE, BigDecimal DQJHGHBJ, BigDecimal DQJHGHLX, BigDecimal DQYHJE
            , BigDecimal DQYHBJ , BigDecimal DQYHLX, BigDecimal HSLXZE, Date DKJJRQ, BigDecimal HSBJZE, BigDecimal DQQC, BigDecimal DKYEZCBJ);

    void flush();
}