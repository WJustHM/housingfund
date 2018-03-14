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
public interface UnitAcctDrop {

    /**
     *  单位账号销户
     * @param t  单位账号销户等信息对象
     * @return
     */
    AddUnitAcctDropRes addUnitAcctDrop(TokenContext tokenContext,UnitAcctDropPost t);

    /**
     * 单位账号销户修改
     * @param YWLSH 业务流水号
     * @param t 单位账号销户等信息对象
     * @return
     */
    ReUnitAcctDropRes reUnitAcctDrop(TokenContext tokenContext, UnitAcctDropPut t, String YWLSH);

    /**
     *  单位账号销户详情
     * @param YWLSH  业务流水号
     * @return
     */
    GetUnitAcctDropRes getUnitAcctDrop(String YWLSH);

    /**
     * 单位账号销户列表
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageRes<ListUnitAcctDropResRes> showUnitAcctDrop(TokenContext tokenContext,String DWZH, String DWMC, String DWLB, String ZhuangTai,String KSSJ,String JSSJ, String page, String pagesize);

    /**
     * 单位账号销户列表
     * @param DWZH
     * @param DWMC
     * @param DWLB
     * @param ZhuangTai
     * @return
     */
    PageResNew<ListUnitAcctDropResRes> showUnitAcctDropNew(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai, String KSSJ, String JSSJ, String page, String pagesize, String action);

    /**
     * 单位账户销户回执单
     * @param YWLSH
     * @return
     */
    CommonResponses headUnitAcctsDropReceipt(String YWLSH);

    /**
     * 审核通过后单位销户操作
     * @param YWLSH
     * @return
     */
    void doUnitAcctDrop(String YWLSH);

    /**
     * 提交新建状态的业务
     * @param YWLSHs
     * @return
     */
    PostUnitAcctDropSubmitRes submitUnitAcctDrop(TokenContext tokenContext,List<String> YWLSHs);

}
