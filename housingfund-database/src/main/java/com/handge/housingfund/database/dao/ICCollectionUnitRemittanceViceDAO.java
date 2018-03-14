package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CCollectionUnitRemittanceVice;
import com.handge.housingfund.database.entities.StCommonPerson;

import java.math.BigDecimal;
import java.util.List;

/**
 *单位汇缴
 */
public interface ICCollectionUnitRemittanceViceDAO extends IBaseDAO<CCollectionUnitRemittanceVice> {
    CCollectionUnitRemittanceVice getByYwlsh(String ywlsh);

    CCollectionUnitRemittanceVice getByQcqrdh(String qcqrdh);

    /**
     * 根据单位账号和汇补缴年月查询汇缴信息
     */
    CCollectionUnitRemittanceVice getRemittance(String dwzh, String hbjny);

    /**
     * 单位在途业务验证
     */
    boolean checkIsExistDoing(String dwzh);

    BigDecimal getComputeDwzhye(String dwzh);

    /**
     * 查询该人的连续缴存月数
     * 从上个月开始往前计算
     */
    List<String> getConsecutiveDepositMonths(String grzh);

    /**
     * 该单位是否存在未办结的汇缴业务
     */
    String getYWLSHIsExistDoing(String dwzh);


    void flushAndClear();

    List<StCommonPerson> getListNoInCurrentUnitOfInventory(String qcqrdh, String dwzh);


    /**
     *
     */
    boolean checkIsExistDoing(String grzh,String dwzh);

}
