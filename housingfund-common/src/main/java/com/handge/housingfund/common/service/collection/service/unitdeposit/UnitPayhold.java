package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.AddIndiAcctActionRes;
import com.handge.housingfund.common.service.collection.model.unit.ReUnitAcctDropRes;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/7/18.
 */

// TODO: 2017/7/18 单位缓缴（杨凡）
public interface UnitPayhold {

    public PageRes<ListUnitPayHoldResRes> getUnitPayholdInfo(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String CZY, String YWWD,String KSSJ, String JSSJ, String pageNo, String pageSize);//获取缓缴业务记录列表

    public PageResNew<ListUnitPayHoldResRes> getUnitPayholdInfoNew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String CZY, String YWWD, String KSSJ, String JSSJ, String marker, String pageSize,String action);//获取缓缴业务记录列表

    public ReUnitDepositRatioRes addUnitPayhold(TokenContext tokenContext, UnitPayHoldPost unitPayHoldPost);//提交缓缴申请

    public ReUnitAcctDropRes reUnitPayhold(TokenContext tokenContext,String YWLSH, UnitPayHoldPut unitPayHoldPut);//单位缓缴修改

    public GetUnitPayHoldRes showUnitPayhold(String YWLSH);//获取缓缴申请详情

    public CommonResponses headUnitPayhold(String YWLSH);//缓缴回执单

    public void doUnitPayhold(String YWLSH);//审核通过后操作

    public AddIndiAcctActionRes submitUnitPayHold(TokenContext tokenContext,ArrayList<String> YWLSHs);//批量提交

    public void doUpdateUnitState(String ywlsh);//缓缴结束时间到了，更新单位账户状态

}
