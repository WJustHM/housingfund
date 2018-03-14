package com.handge.housingfund.server.controllers.others;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IExportExcelService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.ResUtils;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by sjw on 2017/11/9.
 */
@Controller
@Path("/other")
public class ExportExcelController {
    @Autowired
    private IExportExcelService exportExcelService;

    @GET
    @Path("/ExportPersonRadixExcel/{DWZH}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response exportPersonRadixExcel(final @Context HttpRequest request, final @PathParam("DWZH") String dwzh
            , final @QueryParam("SXNY") String sxny) {
        System.out.println("生成个人基数调整excel");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonResponses res = this.exportExcelService.getExceldata(tokenContext, dwzh,sxny);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/InventoryToExcel/{DWZH}/{QCNY}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response InventoryToExcel(final @Context HttpRequest request, final @PathParam("DWZH") String dwzh, final @PathParam("QCNY") String qcny) {
        System.out.println("生成清册确认单excel");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonResponses res = this.exportExcelService.getInventoryData(tokenContext, dwzh,qcny);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/employeeListToExcel/{DWZH}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeeListToExcel(final @Context HttpRequest request, final @PathParam("DWZH") String dwzh) {
        System.out.println("生成公司职工列表excel");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonResponses res = this.exportExcelService.getemployeeListData(tokenContext, dwzh);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/AnnualRportToExcel")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnualReportData(final @Context HttpRequest request) {
        System.out.println("生成年报excel");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonResponses res = this.exportExcelService.getAnnualReportData(tokenContext);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/paymentHistoryToExcel/{DKZH}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPaymentHistoryData(final @Context HttpRequest request,final @PathParam("DKZH") String dkzh, final @QueryParam("HKRQS") String hkrqs,  final @QueryParam("HKRQE") String hkrqe) {
        System.out.println("生成还款记录excel");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonResponses res = this.exportExcelService.getPaymentHistoryData(tokenContext,dkzh,hkrqs,hkrqe);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }

}
