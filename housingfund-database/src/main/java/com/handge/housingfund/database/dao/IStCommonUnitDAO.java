package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StCommonUnit;

import java.math.BigDecimal;
import java.util.List;

public interface IStCommonUnitDAO extends IBaseDAO<StCommonUnit> {

    StCommonUnit getUnit(String dwzh);

    /**
     * 传入单位账号计算出单位的单位账户余额（用于更新账户信息）
     */
    BigDecimal getUnitAmountCount(String dwzh);

    List<StCommonUnit> getUnitByName(String dwmc);

    Object[] getUnitSumBalances(String khwd, String dwzh, String dwmc, String jzny, String sfwft);

    List<Object[]> getSMSPayCall(String yyyyMM);
}