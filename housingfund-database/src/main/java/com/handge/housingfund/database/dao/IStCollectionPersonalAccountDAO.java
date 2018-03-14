package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StCollectionPersonalAccount;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface IStCollectionPersonalAccountDAO extends IBaseDAO<StCollectionPersonalAccount> {

    StCollectionPersonalAccount getByGrzh(String grzh);

    BigDecimal getGrzhyeHj();

}