package com.handge.housingfund.common.service.collection.service.unitinfomanage;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.List;

/*
 * Created by Liujuhao on 2017/7/17.
 */
public interface UnitAcctAlter {

    /**
     *  单位变更新建
     * @param body  单位缴存登记等信息对象
     */
    public AddUnitAcctSetRes addUnitAccountAlter(TokenContext tokenContext, UnitAcctAlterPost body);

    /**
     * 单位变更详情
     * @param YWLSH 业务流水号
     */
    public GetUnitAcctAlterRes getUnitAccountAlter(TokenContext tokenContext,String YWLSH);

    /**
     * 单位变更修改
     * @param YWLSH 业务流水号
     * @param t 单位变更等信息对象
     */
    public AddUnitAcctSetRes reUnitAccountAlter(TokenContext tokenContext,String YWLSH, UnitAcctAlterPut t);

    /**
     * 单位变更列表
     * @param DWZH 单位账号
     * @param DWMC 单位名称
     * @param DWLB 单位类型
     * @param ZhuangTai 状态
     */
    public PageRes<ListUnitAcctAlterResRes> showUnitAccountsAlter(TokenContext tokenContext,String DWZH, String DWMC, String DWLB, String ZhuangTai, String page, String pagesize,String KSSJ,String JSSJ);


    /**
     * 单位变更列表
     * @param DWZH 单位账号
     * @param DWMC 单位名称
     * @param DWLB 单位类型
     * @param ZhuangTai 状态
     */
    public PageResNew<ListUnitAcctAlterResRes> showUnitAccountsAlter(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai, String marker, String action, String pagesize, String KSSJ, String JSSJ);

    /**
     * 单位账户变更回执单
     * @param YWLSH
     */
    public CommonResponses headUnitAcctsAlterReceipt(TokenContext tokenContext, String YWLSH);

    /**
     * 审核通过后变更
     * @param YWLSH
     * @return
     */
    public void doUnitAcctAlter(TokenContext tokenContext,String YWLSH);

    /**
     * 提交新建状态的业务
     * @param YWLSHs
     * @return
     */
    public PostUnitAcctAlterSubmitRes submitUnitAccountAlter(TokenContext tokenContext,List<String> YWLSHs);
}
