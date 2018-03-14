package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.collection.model.deposit.GetUnitDepositDetailRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositDeatilResRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositResRes;

/**
 * Created by Liujuhao on 2017/7/18.
 */

// TODO: 2017/7/18 单位缴存信息（练隆森）
public interface UnitDepositInfo {


    public GetUnitDepositDetailRes getUnitDepositDetail(TokenContext tokenContext, final  String DWZH);

    public PageRes<ListUnitDepositResRes> getUnitDepositListRes(TokenContext tokenContext, final String DWMC, final String DWZH, final String pageSize, final String page, final boolean GLWD, final boolean GLXH, String khwd, String SFYWFTYE,String JZNY);

    public PageRes<ListUnitDepositDeatilResRes> getUnitDepositInfoList(TokenContext tokenContext, String DWZH, String date, String pageSize, String page);
}
