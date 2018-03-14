package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.Reconciliation;
import com.handge.housingfund.common.service.finance.model.ReconciliationBase;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * Created by gxy on 17-10-25.
 */
public interface IReconciliationService {
    /**
     * 获取余额调节列表
     *
     * @param yhmc     银行名称
     * @param zhhm     专户号码
     * @param tjny     调节年月
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<Reconciliation> getReconciliationList(String yhmc, String zhhm, String tjny, int pageNo, int pageSize);

    /**
     * 获取余额调节详情
     *
     * @param id
     * @return
     */
    Reconciliation getReconciliation(String id);

    /**
     * 新增余额调节
     *
     * @param reconciliation
     * @return
     */
    CommonResponses addReconciliation(TokenContext tokenContext, ReconciliationBase reconciliation);

    /**
     * 修改余额调节
     *
     * @param id
     * @param reconciliation
     * @return
     */
    CommonResponses updateReconciliation(TokenContext tokenContext, String id, ReconciliationBase reconciliation);

    /**
     * 删除余额调节
     *
     * @param id
     * @return
     */
    boolean delReconciliation(String id);

    /**
     * 获取余额调节期末余额
     * @param node
     * @param khbh
     * @param yhzh
     * @param tjny
     * @return
     */
    Reconciliation getReconciliationInitBalance(String node, String khbh, String yhzh, String tjny);
}
