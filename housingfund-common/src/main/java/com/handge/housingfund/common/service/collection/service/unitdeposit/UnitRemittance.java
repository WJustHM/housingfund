package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkFlow;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * 单位汇缴 yangfan
 */
public interface UnitRemittance extends WorkFlow{

    /**
     *汇缴申请时，根据账号+汇补缴年月询清册信息
     */
    public ListRemittanceInventoryRes getUnitRemittanceInventory(TokenContext tokenContext,String dwzh, String hbjny);


    /**
     * 查询单位汇缴业务记录列表
     */
    public PageRes<ListUnitRemittanceResRes> getUnitRemittanceList(TokenContext tokenContext,String dwmc, String dwzh, String ywzt, String czy,String yhmc,String ywwd, String kssj, String jssj, String ywpch, String pageNumber, String pageSize);

    /**
     *新建汇缴申请
     * // model缺少 操作类型
     */
    public AddUnitRemittanceRes postUnitRemittance(TokenContext tokenContext,UnitRemittancePost unitRemittancePost);

    /**
     * 根据单位账号获取汇缴申请的单位信息
     */
    public AutoUnitRemittanceRes getUnitRemittanceAuto(TokenContext tokenContext,String dwzh);

    /**
     *根据ywlsh得到汇缴申请详情
     */
    public GetUnitRemittanceRes getUnitRemittance(TokenContext tokenContext,String ywlsh);

    /**
     * 汇缴申请修改
     */
    public ReUnitRemittanceRes putUnitRemittance(TokenContext tokenContext,String ywlsh, UnitRemittancePut unitRemittancePut);

    /**
     * 汇缴信息回执单
     */
    public CommonResponses headUnitRemittance(String ywlsh);

    /**
     *汇缴办结时处理
     */
    public void doFinal(String ywlsh);

    /**
     * 汇缴提交成功，向资金交易平台发送消息

     public void sendRemittance(String ywlsh) throws Exception;
     */

    /**
     * 由清册业务发起，直接生成汇缴记录(新建状态)
     */
    public void saveRemittance(String ywlsh);

    /**
     *缴存通知单
     */
    CommonResponses headUnitRemittanceNotice(String ywlsh);

    /**
     * 根据个人账号查询连续缴存月数
     * 封装在id里面
     */
    CommonResponses getConsecutiveDepositMonths(String grzh);

    /**
     * 批量进行汇缴
     */
    void saveRemittance3(TokenContext tokenContext, String ywpch);

    void getBatchPaymentNotice(String ywlsh);

    BusCommonRetrun accountRecord(String ywlsh, String code);

    void doFail(String ywlsh,String sbyy);

    CommonResponses revokeRemittance(String ywlsh);

    void remittanceAgain(String dwzh, String jkfs);

    void remittancePaymentNotice(String ywlsh, AccChangeNotice accChangeNotice,String id);

    PageResNew<ListUnitRemittanceResRes> getUnitRemittanceList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy,String yhmc,String ywwd, String kssj, String jssj, String ywpch, String marker, String pageSize, String action);

    void doBeforehandSealCheck(String ywlsh);
}
