package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.FixedBusinessAudit;
import com.handge.housingfund.common.service.review.model.ReviewInfo;

/**
 * Created by Administrator on 2017/9/14.
 */
public interface IFixedBusinessAuditService {

    /**
     * 获取定期业务审核列表
     *
     * @param khyhmc   开户银行名称
     * @param acctNo   账号
     * @param ywlx     业务类型 09:活期转定期 10:定期支取 11:活期转通知存款 12:通知存款支取
     * @param audit    是否已审核标志，区分待处理和已处理 0:待审核 1:已审核
     * @param begin    到达时间-开始
     * @param end      到达时间-结束
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    PageRes<FixedBusinessAudit> getAuditRecords(String khyhmc, String acctNo, String ywlx, String audit, String begin, String end, int pageNo, int pageSize);

    /**
     * 审核活期转定期
     *
     * @param id
     * @param reviewInfo
     */
    void auditActived2Fixed(String id, ReviewInfo reviewInfo);

    /**
     * 审核活期转定期
     *
     * @param id
     * @param reviewInfo
     */
    void auditFixedDraw(String id, ReviewInfo reviewInfo);
}
