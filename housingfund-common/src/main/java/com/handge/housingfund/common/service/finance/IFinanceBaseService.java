package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.BaseFinanceModel;
import com.handge.housingfund.common.service.finance.model.ContainerKS;
import com.handge.housingfund.common.service.finance.model.ContainerKSV;
import com.handge.housingfund.common.service.finance.model.ContainerKV;
import com.handge.housingfund.common.service.review.model.AuditInfo;

import java.util.Date;

/**
 * Created by xuefei_wang on 17-8-24.
 *
 * @see com.handge.housingfund.finance.service.FinanceBaseService
 */
public interface IFinanceBaseService {

    ContainerKS<BaseFinanceModel> searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi,String BeiZhu, int pageNo, int pageSize);

    ContainerKS<BaseFinanceModel> searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi, String marker, String action, int pageSize);

    ContainerKS<ContainerKSV<String, String>> deleteJobs(TokenContext tokenContext, ContainerKS<String> idsContainer);

    ContainerKS<ContainerKSV<String, String>> submitJobs(TokenContext tokenContext, ContainerKS<String> idsContainer);

    ContainerKV<String, String> updateJob(TokenContext tokenContext, String ywlsh, BaseFinanceModel jobInfo, boolean submit);

    BaseFinanceModel getJob(TokenContext tokenContext, String ywlsh);

    ContainerKS<BaseFinanceModel> searcheAuditJobs(TokenContext tokenContext, String type, String operator, Date begin, Date end, boolean complete, int pageNo, int pageSize);

    BaseFinanceModel getAuditJob(TokenContext tokenContext, String ywlsh);

    ContainerKS<ContainerKSV<String, String>> auditJobs(TokenContext tokenContext, ContainerKSV<String, AuditInfo> containerAudit);

    ContainerKV<String, Object> createTask(TokenContext tokenContext, BaseFinanceModel jobInfo, boolean submit);

    boolean financeJobFinished(String ywlsh);

    boolean financeJobFinished(String ywlsh, TokenContext tokenContext);

    void savaToBase(String ywlsh, String jzpzh);

    void updateStatus(String ywlsh, String status, String sbyy);

    ContainerKV revokeJobs(TokenContext tokenContext, ContainerKS<String> idsContainer);

}
