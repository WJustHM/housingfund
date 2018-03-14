package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.BaseFinanceModel;
import com.handge.housingfund.common.service.finance.model.ContainerKS;
import com.handge.housingfund.common.service.finance.model.ContainerKSV;
import com.handge.housingfund.common.service.review.model.AuditInfo;

import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by xuefei_wang on 17-8-24.
 */
public interface IFinanceBaseApi {

    Response searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi,String BeiZhu, int pageNo, int pageSize);

    Response searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi, String marker, String action, String pageSize);

    Response deleteJobs(TokenContext tokenContext, ContainerKS<String> idsContainerK);

    Response submitJobs(TokenContext tokenContext, ContainerKS<String> idsContainerK);

    Response updateJob(TokenContext tokenContext, String ywlsh, BaseFinanceModel jobInfo, boolean submit);

    Response getJob(TokenContext tokenContext, String ywlsh);

    Response create(TokenContext tokenContext, BaseFinanceModel jobInfo, boolean submit);

    Response searcheAuditJobs(TokenContext tokenContext, String type, String operator, Date begin, Date end, boolean complete, int pageNo, int pageSize);

    Response getAuditJob(TokenContext tokenContext, String ywlsh);

    Response auditJobs(TokenContext tokenContext, ContainerKSV<String, AuditInfo> containerAudit);

    Response revokeJobs(TokenContext tokenContext, ContainerKS<String> idsContainer);

//    Response getSubjectTrades(TokenContext tokenContext, String subjectId, Date begin, Date end);
//
//    Response getSubjectTradesByMonth(TokenContext tokenContext, String subjectId, String period);
//
//    Response getSubjectTradesByYear(TokenContext tokenContext, String subjectId, String year);

}
