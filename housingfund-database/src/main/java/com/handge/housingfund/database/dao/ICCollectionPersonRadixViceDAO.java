package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.collection.model.deposit.GetPersonRadixBeforeResJCJSTZXX;
import com.handge.housingfund.database.entities.CCollectionPersonRadixDetailVice;
import com.handge.housingfund.database.entities.CCollectionPersonRadixVice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ICCollectionPersonRadixViceDAO extends IBaseDAO<CCollectionPersonRadixVice> {
    List<CCollectionPersonRadixVice> getRadixList(String dwzh, String grzh, Date hbjny);

    /**
     * 得到该人办理的基数调整业务，生效月份为汇补缴年月，取办理时间最晚的一条
     */
    CCollectionPersonRadixDetailVice getRadixLast(String dwzh, String grzh, String hbjny);

    /**
     * 得到该人办理基数调整业务，生效月份在汇补缴年月之后的第一条
     */
    CCollectionPersonRadixDetailVice getRadixAfter(String dwzh, String grzh, String hbjny);

    CCollectionPersonRadixVice getPersonRadix(String ywlsh);


    List<CCollectionPersonRadixVice> getPersonRadix(String dwzh,String jzny,Date bjsj);

    ArrayList<GetPersonRadixBeforeResJCJSTZXX> getRadixs(String dwzh);

    List<Object[]> getPersonRadixDetail(String id);

    /**
     * 基数调整，根据业务，更新个人的缴存基数、缴存额信息
     * @param ywlsh
     */
    void doFinal(String ywlsh);

    /**
     * 基数业务，更新办结时间
     */
    void updateBJSJ(String ywlsh);
}
