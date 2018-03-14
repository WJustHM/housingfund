package com.handge.housingfund.common.service.collection.service.unitinfomanage;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.unit.AutoUnitAcctActionRes;
import com.handge.housingfund.common.service.collection.model.unit.GetUnitAcctInfoDetail;

/**
 * Created by Liujuhao on 2017/7/21.
 */
public interface UnitAcctCommon {


    /**
     * 获取（销户、启封、封存）操作时所需的单位关键信息
     * @param DWZH 单位账号
     * @return
     */
    public AutoUnitAcctActionRes getUnitAcctActionAuto(final String DWZH);

    /**
     * 获取根据单位账号获取单位信息
     * @param DWZH
     * @return
     */
    public GetUnitAcctInfoDetail getUnitInfoAuto(TokenContext tokenContext,final String DWZH);
}
