package com.handge.housingfund.common.service.collection.service.unitinfomanage;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.List;

/**
 * Created by Liujuhao on 2017/7/17.
 */
public interface UnitAcctUnseal{

    /**
     *  单位账号启封新建
     * @param unitUnsealedPost  单位账号启封等信息对象
     * @return
     */
    UnitAcctUnsealedRes addUnitAcctUnsealed(TokenContext tokenContext,UnitAcctUnsealedPost unitUnsealedPost);

    /**
     * 单位账号启封修改
     * @param YWLSH 业务流水号
     * @param unitUnsealedPut 单位账号启封等信息对象
     * @return
     */
    ReUnitAcctUnsealedRes reUnitAcctUnsealed(TokenContext tokenContext, String YWLSH, UnitAcctUnsealedPut unitUnsealedPut);

    /**
     *  单位账号启封详情
     * @param YWLSH  业务流水号
     * @return
     */
    GetUnitAcctUnsealedRes getUnitAcctUnsealed(String YWLSH);

    /**
     * 单位账号启封列表
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageRes<ListUnitAcctUnsealedResRes> showUnitAcctsUnsealed(TokenContext tokenContext,String DWZH, String DWMC, String DWLB, String ZhuangTai,String KSSJ,String JSSJ, String page, String pagesize);

    /**
     * 单位账号启封列表
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageResNew<ListUnitAcctUnsealedResRes> showUnitAcctsUnsealedNew(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pageSize, String action);

    /**
     * 单位账户启封回执单
     * @param YWLSH
     * @return
     */
    CommonResponses headUnitAcctsUnsealedReceipt(TokenContext tokenContext,String YWLSH);

    /**
     * 审核通过后单位启封操作
     * @param YWLSH
     * @return
     */
    void doUnitAcctUnseal(String YWLSH);

    /**
     * 提交新建状态的业务
     * @param YWLSHs
     * @return
     */
    PostUnitAcctUnsealedSubmitRes submitUnitAcctUnseal(TokenContext tokenContext,List<String> YWLSHs);
}
