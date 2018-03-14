package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CBankAccChangeNotice;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gxy on 17-8-16.
 */
public interface ICBankAccChangeNoticeDAO extends IBaseDAO<CBankAccChangeNotice> {
    BigDecimal getAmt(String acct, Date firstDay, Date lastDay);
}
