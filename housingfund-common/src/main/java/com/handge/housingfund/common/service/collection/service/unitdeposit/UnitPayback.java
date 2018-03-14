package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkFlow;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.Map;

/**
 * 单位补缴
 */
public interface UnitPayback extends WorkFlow {

    /**
     * 获取补缴记录列表
     */
    public PageRes<ListPayBackResRes> getUnitPayBackList(TokenContext tokenContext,String dwmc, String dwzh, String ywzt, String czy, String ywwd,String kssj, String jssj, String pageNo, String pageSize);

    /**
     * 新增补缴申请
     */
    public AddUnitPayBackRes postUnitPayBack(TokenContext tokenContext, UnitPayBackPost unitPayBackPost);

    /**
     * 根据单位/汇补缴年月获取补缴信息（申请时生成数据）
     */
    public AutoUnitPayBackRes getUnitPayBackAuto(TokenContext tokenContext,String dwzh, String hbjny);

    /**
     *修改补缴信息
     */
    public ReUnitPayBackRes putUnitPayBack(TokenContext tokenContext,String ywlsh ,UnitPayBackPut unitPayBackPut);

    /**
     *获取补缴信息详情
     */
    public GetUnitPayBackRes getUnitPayBack(TokenContext tokenContext,String ywlsh);

    /**
     *获取补缴回执单
     */
    public CommonResponses HeadUnitPayBack(String ywlsh);

    /**
     *公积金补缴通知单
     */
    public CommonResponses headUnitPayBackNotice(String ywlsh);

    /**
     * 补缴办结时处理
     */
    public void doFinal(String ywlsh);

    /**
     * 检查启封数据，可能生成补缴数据
     */
    public void checkUnseal(String ywlsh, Map map);

    public void checkPersonAccSet(String ywlsh,Map map);

    void getPaymentNotice(String ywlsh);

    void doFail(String ywlsh,String sbyy);

    BusCommonRetrun accountRecord(String ywlsh, String code);

    void payBackckPaymentNotice(String ywlsh, AccChangeNotice accChangeNotice, String id);

    CommonResponses postPayBackTemporary(TokenContext tokenContext,String ywlsh);

    PageResNew<ListPayBackResRes> getUnitPayBackList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String ywwd,String kssj, String jssj, String marker, String pageSize, String action);

}
