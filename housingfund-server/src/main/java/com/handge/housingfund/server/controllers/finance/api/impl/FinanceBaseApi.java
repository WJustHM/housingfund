package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.IFinanceBaseService;
import com.handge.housingfund.common.service.finance.model.BaseFinanceModel;
import com.handge.housingfund.common.service.finance.model.ContainerKS;
import com.handge.housingfund.common.service.finance.model.ContainerKSV;
import com.handge.housingfund.common.service.finance.model.ContainerKV;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.finance.api.IFinanceBaseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-24.
 */

@Component
public class FinanceBaseApi implements IFinanceBaseApi {

    @Autowired
    IFinanceBaseService financeBaseService;

    @Override
    public Response create(TokenContext tokenContext, BaseFinanceModel jobInfo, boolean submit) {
//        FinanceType modelType = FinanceBusinessTypeDictionary.dictionary.getOrDefault(jobInfo.getYwmc().trim(), FinanceType.QTYW);
        ContainerKV<String, Object> createInfo = null;
        try {
            createInfo = financeBaseService.createTask(tokenContext, jobInfo, submit);
//            switch (modelType) {
//                case SDZS:
//                    createInfo = financeBaseService.doSDZH(tokenContext, jobInfo);
//                    break;
//                case KHZZ:
//                    createInfo = financeBaseService.doKHZZ(tokenContext, jobInfo);
//                    break;
//                case THZZ:
//                    createInfo = financeBaseService.doTHZZ(tokenContext, jobInfo);
//                    break;
//                case JTSXF:
//                    createInfo = financeBaseService.doJTSXF(tokenContext, jobInfo);
//                    break;
//                case ZFSXF:
//                    createInfo = financeBaseService.doZFSXF(tokenContext, jobInfo);
//                    break;
//                case SDYHLX:
//                    createInfo = financeBaseService.doSDYHLX(tokenContext, jobInfo);
//                    break;
//                case ZZSYFP:
//                    createInfo = financeBaseService.doZZSYFP(tokenContext, jobInfo);
//                    break;
//                case JSGJJLX:
//                    createInfo = financeBaseService.doJSGJJLX(tokenContext, jobInfo);
//                    break;
//                case JTGJJLX:
//                    createInfo = financeBaseService.doJTGJJLX(tokenContext, jobInfo);
//                    break;
//                case ZFQTYFK:
//                    createInfo = financeBaseService.doZFQTYFK(tokenContext, jobInfo);
//                    break;
//                case NDJZZZSY:
//                    createInfo = financeBaseService.doNDJZZZSY(tokenContext, jobInfo);
//                    break;
//                case QMJZYWSR:
//                    createInfo = financeBaseService.doQMJZYWSR(tokenContext, jobInfo);
//                    break;
//                case QMJZYWZC:
//                    createInfo = financeBaseService.doQMJZYWZC(tokenContext, jobInfo);
//                    break;
//                case ZFBXDKLX:
//                    createInfo = financeBaseService.doZFBXDKLX(tokenContext, jobInfo);
//                    break;
//                case QTYW:
//                    createInfo = financeBaseService.doQTYW(tokenContext, jobInfo);
//                    break;
//            }
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
        return Response.ok(createInfo).build();
    }

    @Override
    public Response searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end,String HeJi,String BeiZhu, int pageNo, int pageSize) {
        try {
            ContainerKS<BaseFinanceModel> containerK = financeBaseService.searchJobs(tokenContext, businessName, status, operator, begin, end,HeJi,BeiZhu, pageNo, pageSize);
            return Response.ok(containerK).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }

    }

    @Override
    public Response searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end,String HeJi, String marker, String action, String pageSize) {
        try {
            int pagesize = pageSize == null ? 30 : Integer.parseInt(pageSize);
            ContainerKS<BaseFinanceModel> containerK = financeBaseService.searchJobs(tokenContext, businessName, status, operator, begin, end,HeJi,  marker, action, pagesize);
            return Response.ok(containerK).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }

    }


    @Override
    public Response submitJobs(TokenContext tokenContext, ContainerKS<String> idsContainerK) {
        try {
            ContainerKS<ContainerKSV<String, String>> containerKS = financeBaseService.submitJobs(tokenContext, idsContainerK);
            ContainerKS<ContainerKSV<String, String>> finalContainer = new ContainerKS<>();
            List<ContainerKSV<String, String>> containerKSVs = containerKS.getList();
            for (ContainerKSV c : containerKSVs) {
                if (c.getList().size() != 0) finalContainer.addK(c);
            }

            return Response.status(200).entity(finalContainer).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    @Override
    public Response deleteJobs(TokenContext tokenContext, ContainerKS<String> idsContainerK) {
        try {
            ContainerKS<ContainerKSV<String, String>> containerKS = financeBaseService.deleteJobs(tokenContext, idsContainerK);
            ContainerKS<ContainerKSV<String, String>> finalContainer = new ContainerKS<>();
            List<ContainerKSV<String, String>> containerKSVs = containerKS.getList();
            for (ContainerKSV c : containerKSVs) {
                if (c.getList().size() != 0) finalContainer.addK(c);
            }

            return Response.ok(finalContainer).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * @param tokenContext
     * @param ywlsh
     * @param jobInfo
     * @return ["ywlsh","msg"]
     */
    @Override
    public Response updateJob(TokenContext tokenContext, String ywlsh, BaseFinanceModel jobInfo, boolean submit) {
        try {
            ContainerKV<String, String> containerKV = financeBaseService.updateJob(tokenContext, ywlsh, jobInfo, submit);
            return Response.ok(containerKV).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getJob(TokenContext tokenContext, String ywlsh) {
        try {
            BaseFinanceModel baseFinanceModel = financeBaseService.getJob(tokenContext, ywlsh);
            return Response.ok(baseFinanceModel).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    @Override
    public Response searcheAuditJobs(TokenContext tokenContext, String businessName, String operator, Date begin, Date end, boolean complete, int pageNo, int pageSize) {
        try {
            ContainerKS<BaseFinanceModel> containerKS = financeBaseService.searcheAuditJobs(tokenContext, businessName, operator, begin, end, complete, pageNo, pageSize);
            return Response.ok(containerKS).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    @Override
    public Response getAuditJob(TokenContext tokenContext, String ywlsh) {
        try {
            BaseFinanceModel baseFinanceModel = financeBaseService.getAuditJob(tokenContext, ywlsh);
            return Response.ok(baseFinanceModel).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }

    }

    /**
     * @param tokenContext
     * @param containerAudit
     * @return [ <"ssss,sddwd",msg>,<"ssss",msg>,<"ssss",msg> ]
     */
    @Override
    public Response auditJobs(TokenContext tokenContext, ContainerKSV<String, AuditInfo> containerAudit) {
        try {
            ContainerKS<ContainerKSV<String, String>> containerKSV = financeBaseService.auditJobs(tokenContext, containerAudit);
            ContainerKS<ContainerKSV<String, String>> finalContainer = new ContainerKS<>();
            List<ContainerKSV<String, String>> containerKSVs = containerKSV.getList();
            for (ContainerKSV c : containerKSVs) {
                if (c.getList().size() != 0) finalContainer.addK(c);
            }
            return Response.ok(finalContainer).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response revokeJobs(TokenContext tokenContext, ContainerKS<String> idsContainer) {
        try {
            ContainerKV value = financeBaseService.revokeJobs(tokenContext, idsContainer);

            return Response.ok(value).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
//
//    @Override
//    public Response getSubjectTrades(TokenContext tokenContext, String subjectId, Date begin, Date end) {
//            try {
//                String value = financeBaseService.getSubjectTrades(tokenContext,subjectId,begin,end);
//                return Response.ok(value).build();
//            } catch (ErrorException e){
//                return Response.status(200).entity(new Error() {{
//                    this.setMsg(e.getMsg());
//                    this.setCode(e.getCode());
//                }}).build();
//            }
//    }
//
//    @Override
//    public Response getSubjectTradesByMonth(TokenContext tokenContext, String subjectId, String period) {
//        try {
//            String value = financeBaseService.getSubjectTradesByMonth(tokenContext,subjectId,period);
//            return Response.ok(value).build();
//        } catch (ErrorException e){
//            return Response.status(200).entity(new Error() {{
//                this.setMsg(e.getMsg());
//                this.setCode(e.getCode());
//            }}).build();
//        }
//    }
//
//    @Override
//    public Response getSubjectTradesByYear(TokenContext tokenContext, String subjectId, String year) {
//        try {
//            String value = financeBaseService.getSubjectTradesByYear(tokenContext,subjectId,year);
//            return Response.ok(value).build();
//        } catch (ErrorException e){
//            return Response.status(200).entity(new Error() {{
//                this.setMsg(e.getMsg());
//                this.setCode(e.getCode());
//            }}).build();
//        }
//    }
//
//

}
