package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.ReconciliationBase;

import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-10-25.
 */
public interface IReconciliationAPI {
    /**
     * 获取余额调节列表
     * @param yhmc       银行名称
     * @param zhhm       专户号码
     * @param tjny       调节年月
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getReconciliationList(String yhmc, String zhhm, String tjny, int pageNo, int pageSize);
    /**
     * 获取余额调节详情
     * @param id
     * @return
     */
    Response getReconciliation(String id);
    /**
     * 新增余额调节
     * @param reconciliation
     * @return
     */
    Response addReconciliation(TokenContext tokenContext,ReconciliationBase reconciliation);
    /**
     * 修改余额调节
     * @param id
     * @param reconciliation
     * @return
     */
    Response updateReconciliation(TokenContext tokenContext,String id, ReconciliationBase reconciliation);
    /**
     * 删除余额调节
     * @param id
     * @return
     */
    Response delReconciliation(String id);

    /**
     * 获取余额调节余额
     * @param node
     * @param khbh
     * @param yhzh
     * @param tjny
     * @return
     */
    Response getBalance(String node, String khbh, String yhzh, String tjny);
}
