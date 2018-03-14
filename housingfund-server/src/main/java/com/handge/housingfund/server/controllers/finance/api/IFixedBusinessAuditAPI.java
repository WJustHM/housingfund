package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.ReviewInfo;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/14.
 */
public interface IFixedBusinessAuditAPI {

    Response getAuditRecords(String khyhmc, String acctNo, String ywlx, String audit, String begin, String end, int pageNo, int pageSize);

    Response getAuditRecord(TokenContext tokenContext,String id, String ywlx);

    Response auditActived2Fixed(String id, ReviewInfo reviewInfo);

    Response auditFixedDraw(String id, ReviewInfo reviewInfo);
}
