package com.handge.housingfund.common.service.collection.service.unitinfomanage;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.CommonMessage;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.List;
import java.util.Map;

/*
 * Created by Liujuhao on 2017/7/17.
 */
public interface UnitAcctSet {

    /**
     *  单位开户登记
     * @param t  单位缴存登记等信息对象
     * @return
     */
    public AddUnitAcctSetRes addUnitAccountSet(TokenContext tokenContext, UnitAcctSetPost t);

    /**
     * 单位开户详情
     * @param YWLSH 业务流水号
     * @return
     */
    public GetUnitAcctSetRes getUnitAccountSet(TokenContext tokenContext,String YWLSH);

    /**
     * 单位开户修改
     * @param YWLSH 业务流水号
     * @param t 单位开户等信息对象
     * @return
     */
    public AddUnitAcctSetRes reUnitAccountSet(TokenContext tokenContext,String YWLSH, UnitAcctSetPut t);

    /**
     * 单位开户列表
     * @param DWMC 单位名称
     * @param DWLB 单位类型
     * @param ZhuangTai 状态
     * @return
     */
    public PageRes<ListUnitAcctSetResRes> showUnitAccountsSet(TokenContext tokenContext,String DWMC, String DWLB, String ZhuangTai, String page, String pagesize,String KSSJ,String JSSJ);


    /**
     * 单位开户列表
     * @param DWMC 单位名称
     * @param DWLB 单位类型
     * @param ZhuangTai 状态
     * @return
     */
    public PageResNew<ListUnitAcctSetResRes> showUnitAccountsSet(TokenContext tokenContext, String DWMC, String DWLB, String ZhuangTai, String marker, String action, String pagesize, String KSSJ, String JSSJ);

    /**
     * 单位账户开户回执单
     * @param YWLSH
     * @return
     */
    public CommonResponses headUnitAcctsSetReceipt(TokenContext tokenContext, String YWLSH);

    /**
     * 审核通过后开户
     * @param YWLSH
     * @return
     */
    public void doUnitAcctSet(TokenContext tokenContext,String YWLSH);

    /**
     * 提交新建状态的业务
     * @param YWLSHs
     * @return
     */
    public PostUnitAcctSetSubmitRes submitUnitAccountSet(TokenContext tokenContext,List<String> YWLSHs);


    /**
     * 单位名称检查，申请时，查询数据库中是否已有相同的单位名称存在
     * @param dwmc
     * @return
     */
    public CommonMessage getUnitNameCheckMessage(String dwmc);

    public CommonMessage addImportAcctInfo(TokenContext tokenContext, Map<Integer, Map<Integer, Object>> map);


}
