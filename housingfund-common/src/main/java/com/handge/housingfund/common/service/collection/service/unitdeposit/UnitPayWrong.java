package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * 单位错缴（杨凡）
 */
public interface UnitPayWrong {

    /**
     *获取错缴更正业务记录列表
     * require
     */
    public PageRes<ListUnitPayWrongResRes> getUnitPayWrongList(TokenContext tokenContext,String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String page, String pagesize);

    /**
     *新建错缴更正申请
     * require
     */
    public AddUnitPayWrongPostRes addUnitPayWrong(TokenContext tokenContext, UnitPayWrongPost unitPayWrongPost);

    /**
     *修改错缴申请
     * 可以不要
     */
    public ReUnitPayWrongPutRes putUnitPayWrong(TokenContext tokenContext,String ywlsh, UnitPayWrongPut unitPayWrongPut);

    /**
     *获取错缴申请详情
     * require
     */
    public GetUnitPayWrongRes getUnitPayWrong(TokenContext tokenContext,String ywlsh);

    /**
     * 错缴回执单
     * require
     */
    public CommonResponses headUnitPayWrong(String ywlsh);

    /**
     * 获取错缴申请详情
     * 不要该API
     */
    public HeadUnitPayWrongReceiptRes autoGetPayWrong(String dwzh,String jcgzny);

    /**
     * 审核通过后错缴操作
     */
    public void doUnitPayWrong(String ywlsh);

    /**
     * 封存业务检查，可能产生错缴
     */
    void checkSealCreateWrong(TokenContext tokenContext, String ywlsh);

    /**
     * 错缴业务办结
     */
    void doFinal(String ywlsh);

    PageResNew<ListUnitPayWrongResRes> getUnitPayWrongList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String marker, String pageSize, String action);
}
