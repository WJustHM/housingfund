package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.BaseFinanceModel;
import com.handge.housingfund.common.service.finance.model.ContainerKS;
import com.handge.housingfund.common.service.finance.model.ContainerKSV;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IFinanceBaseApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by xuefei_wang on 17-8-22.
 * 日常财务处理
 */
@Path("/finance/base")
@Controller
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class FinanceBaseResource {

    @Autowired
    IFinanceBaseApi financeBaseApi;

    /**
     * ok  搜索日常任务
     *
     * @param businessName
     * @param status
     * @param operator
     * @param begin
     * @param end
     * @return ContainerK<BaseFinanceModel>
     */
    @Path("")
    @GET
    public Response searchJobs(final @Context HttpRequest request,
                               final @QueryParam("businessName") String businessName,
                               final @QueryParam("status") String status,
                               final @QueryParam("operator") String operator,
                               final @QueryParam("begin") String begin,
                               final @QueryParam("end") String end,
                               final @QueryParam("HeJi") String HeJi,
                               final @QueryParam("BeiZhu") String BeiZhu,
                               final @QueryParam("pageNo") @DefaultValue("1") int pageNo,
                               final @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            Date dateBegin = (begin == null ? null : DateUtil.str2Date("yyyy-MM-dd", begin));
            Date dateEnd = (end == null ? null : DateUtil.str2Date("yyyy-MM-ddHH:mm:ss", end + "23:59:59"));
            return ResUtils.wrapEntityIfNeeded(financeBaseApi.searchJobs(tokenContext, businessName, status, operator, dateBegin, dateEnd, HeJi, BeiZhu, pageNo, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(e.getStackTrace()).build();
        }
    }

    /**
     * ok  搜索日常任务
     *
     * @param businessName
     * @param status
     * @param operator
     * @param begin
     * @param end
     * @return ContainerK<BaseFinanceModel>
     */
    @Path("/new")
    @GET
    public Response searchJobs(final @Context HttpRequest request,
                               final @QueryParam("businessName") String businessName,
                               final @QueryParam("status") String status,
                               final @QueryParam("operator") String operator,
                               final @QueryParam("begin") String begin,
                               final @QueryParam("end") String end,
                               final @QueryParam("HeJi") String HeJi,
                               final @QueryParam("marker") String marker,
                               final @QueryParam("action") String action,
                               final @QueryParam("pageSize") String pageSize) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            Date dateBegin = (begin == null ? null : DateUtil.str2Date("yyyy-MM-dd", begin));
            Date dateEnd = (end == null ? null : DateUtil.str2Date("yyyy-MM-ddHH:mm:ss", end + "23:59:59"));
            return ResUtils.wrapEntityIfNeeded(financeBaseApi.searchJobs(tokenContext, businessName, status, operator, dateBegin, dateEnd, HeJi, marker, action, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(e.getStackTrace()).build();
        }
    }


    /**
     * @param request
     * @param containerStrings
     * @return ContainerK<ContainerKV<String,ReturnStatus>>
     */
    @Path("")
    @DELETE
    public Response deleteJobs(final @Context HttpRequest request,
                               final RequestWrapper<ContainerKS<String>> containerStrings) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKS<String> idsConstainer = containerStrings.getReq();
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.deleteJobs(tokenContext, idsConstainer));
    }


    /**
     * 1217090100001  1217090100002  1217090100004
     *
     * @param request
     * @param ywlsh
     * @return ContainerK<ContainerKV<String,ReturnStatus>>
     */
    @DELETE
    @Path("/{ywlsh}")
    public Response deleteJobs(final @Context HttpRequest request,
                               final @PathParam("ywlsh") String ywlsh) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKS<String> idsConstainer = new ContainerKS<>();
        idsConstainer.addK(ywlsh);
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.deleteJobs(tokenContext, idsConstainer));
    }


    /**
     * ok　创建日常任务
     *
     * @param jobInfo
     * @return ContainerKV<String,String>
     */
    @Path("")
    @POST
    public Response createJob(final @Context HttpRequest request,
                              final @QueryParam("submit") @DefaultValue("false") boolean submit,
                              final RequestWrapper<BaseFinanceModel> jobInfo) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        BaseFinanceModel baseFinanceModel = jobInfo.getReq();
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.create(tokenContext, baseFinanceModel, submit));
    }

    /**
     * ok　更型一个业务
     *
     * @param ywlsh
     * @param jobInfo
     * @return ContainerK<BaseFinanceModel>
     */
    @POST
    @Path("/{ywlsh}")
    public Response updateJob(final @Context HttpRequest request,
                              final @PathParam("ywlsh") String ywlsh,
                              final @QueryParam("submit") @DefaultValue("false") boolean submit,
                              final RequestWrapper<BaseFinanceModel> jobInfo) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        BaseFinanceModel baseFinanceModel = jobInfo.getReq();
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.updateJob(tokenContext, ywlsh, baseFinanceModel, submit));

    }


    /**
     * 　ok　提交一批日常业务
     *
     * @param
     * @return ContainerK<ContainerKV<String,String>>
     */
    @Path("")
    @PUT
    public Response submitJobs(final @Context HttpRequest request,
                               final RequestWrapper<ContainerKS<String>> idContainer
    ) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKS<String> idsContainer = idContainer.getReq();
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.submitJobs(tokenContext, idsContainer));
    }


    /**
     * 　ok　提交一个日常业务
     *
     * @param
     * @return ContainerK<ContainerKV<String,ReturnStatus>>
     */
    @PUT
    @Path("/{ywlsh}")
    public Response submitJob(final @Context HttpRequest request,
                              final @PathParam("ywlsh") String ywlsh
    ) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKS<String> idsContainer = new ContainerKS<String>();
        idsContainer.addK(ywlsh);
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.submitJobs(tokenContext, idsContainer));
    }

    @POST
    @Path("/revoke/{ywlsh}")
    public Response revokeJob(final @Context HttpRequest request,
                              final @PathParam("ywlsh") String ywlsh) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKS<String> idsContainer = new ContainerKS<String>();
        idsContainer.addK(ywlsh);
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.revokeJobs(tokenContext, idsContainer));
    }

    /**
     * ok　获取业务的详情
     *
     * @param ywlsh
     * @return ContainerK<BaseFinanceModel>
     */
    @GET
    @Path("/{ywlsh}")
    public Response getJob(final @Context HttpRequest request,
                           final @PathParam("ywlsh") String ywlsh) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.getJob(tokenContext, ywlsh));
    }

    /**
     * 　ok　获取当前用户的审核作业
     *
     * @param businessName
     * @param operator
     * @param begin
     * @param end
     * @param complete     是否是完成的作业
     * @return ContainerK<BaseFinanceModel>
     */
    @GET
    @Path("/audit/jobs")
    public Response searchAuditJobs(final @Context HttpRequest request,
                                    final @QueryParam("businessName") String businessName,
                                    final @QueryParam("operator") String operator,
                                    final @QueryParam("begin") String begin,
                                    final @QueryParam("end") String end,
                                    final @QueryParam("complete") boolean complete,
                                    final @QueryParam("pageNo") @DefaultValue("1") int pageNo,
                                    final @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            Date dateBegin = (begin == null ? null : DateUtil.str2Date("yyyy-MM-dd", begin));
            Date dateEnd = (end == null ? null : DateUtil.str2Date("yyyy-MM-dd", end));
            return ResUtils.wrapEntityIfNeeded(financeBaseApi.searcheAuditJobs(tokenContext, businessName, operator, dateBegin, dateEnd, complete, pageNo, pageSize));
        } catch (Exception e) {
            return Response.status(200).entity(e).build();
        }

    }


    /**
     * ok　获取审核作业的详情
     *
     * @param ywlsh
     * @return ContainerK<BaseFinanceModel>
     */
    @GET
    @Path("/audit/jobs/{ywlsh}")
    public Response getAuditJob(final @Context HttpRequest request,
                                final @PathParam("ywlsh") String ywlsh) throws Exception {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.getAuditJob(tokenContext, ywlsh));
    }


    /**
     * 　批量审核
     *
     * @param request
     * @param containerKV
     * @return ContainerKSV<String,String>
     */
    @PUT
    @Path("/audit/jobs")
    public Response auditJobs(final @Context HttpRequest request,
                              final RequestWrapper<ContainerKSV<String, AuditInfo>> containerKV) throws Exception {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKSV<String, AuditInfo> containerAudit = containerKV.getReq();
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.auditJobs(tokenContext, containerAudit));
    }


    /**
     * ok　审核作业
     *
     * @param ywlsh
     * @param auditInfo
     * @return
     */
    @PUT
    @Path("/audit/jobs/{ywlsh}")
    public Response auditJob(final @Context HttpRequest request,
                             final @PathParam("ywlsh") String ywlsh,
                             final RequestWrapper<AuditInfo> auditInfo) throws Exception {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        ContainerKSV<String, AuditInfo> containerAudit = new ContainerKSV<String, AuditInfo>();
        containerAudit.addK(ywlsh);
        containerAudit.setV(auditInfo.getReq());
        return ResUtils.wrapEntityIfNeeded(financeBaseApi.auditJobs(tokenContext, containerAudit));
    }

}





