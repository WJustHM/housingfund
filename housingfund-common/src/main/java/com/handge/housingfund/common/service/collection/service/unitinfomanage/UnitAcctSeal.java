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
public interface UnitAcctSeal {

    /**
     * 单位账号封存
     *
     * @param unitSealedPost 单位账号封存等信息对象
     * @return
     */
    AddUnitAcctSealedRes addUnitAcctSealed(TokenContext tokenContext,UnitAcctSealedPost unitSealedPost);

    /**
     * 单位账号封存修改
     *
     * @param YWLSH
     * @param unitSealedPut
     * @return
     */
    ReUnitAcctSealedRes reUnitAcctSealed(TokenContext tokenContext, String YWLSH, UnitAcctSealedPut unitSealedPut);

    /**
     * 单位账号封存详情
     *
     * @param YWLSH 业务流水号
     * @return
     */
    GetUnitAcctSealedRes getUnitAcctSealed(String YWLSH);

    /**
     * 单位账号封存列表
     *
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageRes<ListUnitAcctSealedResRes> showUnitAcctsSealed(TokenContext tokenContext,String DWZH, String DWMC, String DWLB, String ZhuangTai,String KSSJ,String JSSJ, String marker, String pagesize);

    /**
     * 单位账号封存列表
     *
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageResNew<ListUnitAcctSealedResRes> showUnitAcctsSealedNew(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pagesize, String action);

    /**
     * 单位账户封存回执单
     *
     * @param YWLSH
     * @return
     */
    CommonResponses headUnitAcctsSealedReceipt(TokenContext tokenContext,String YWLSH);

    /**
     * 审核通过后单位账户封存操作
     *
     * @param YWLSH
     * @return
     */
    public void doUnitAcctSeal(String YWLSH);


    /**
     * 提交新建状态的业务
     * @param YWLSHs
     * @return
     */
    public PostUnitAcctSealedSubmitRes submitUnitAcctSeal(TokenContext tokenContext,List<String> YWLSHs);
}
