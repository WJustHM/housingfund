package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.StFinanceRecordingVoucher;

import java.util.List;

public interface IStFinanceRecordingVoucherDAO extends IBaseDAO<StFinanceRecordingVoucher> {


    /**
     * 获取借贷合计
     *
     * @return
     */
    List<Object[]> getJDHJ();


    /**
     * 获取凭证借贷合计
     *
     * @param KSSJ
     * @param JSSJ
     * @return
     */
    List<Object[]> getVoucherHJ(String KSSJ, String JSSJ);

    /**
     * 更新凭证状态
     *
     * @param KSSJ
     * @param JSSJ
     */
    void updateVoucher(String KSSJ, String JSSJ);

}