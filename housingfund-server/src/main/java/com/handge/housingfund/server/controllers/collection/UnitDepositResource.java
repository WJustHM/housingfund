package com.handge.housingfund.server.controllers.collection;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.UnitDepositApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/collection")
public class UnitDepositResource {

    @Autowired
    private UnitDepositApi<Response> service;


    /**
     * 根据账号查询清册信息（生成清册数据，从公共基础表读取）
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitRemittanceInventory/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getRemittanceInventoryPersonalList(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("根据账号查询清册信息（生成清册数据，从公共基础表读取）");

        return ResUtils.wrapEntityIfNeeded(this.service.getRemittanceInventoryPersonalList(tokenContext, DWZH));
    }

    /**
     * 根据账号查询清册信息（生成清册数据，从公共基础表读取）
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitRemittanceInventory/{DWZH}/{HBJNY}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getRemittanceInventoryPersonalList(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH, final @PathParam("HBJNY") String HBJNY) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("根据账号查询清册信息（生成清册数据，从公共基础表读取）");

        return ResUtils.wrapEntityIfNeeded(this.service.UnitRemittanceInventory(tokenContext, DWZH, HBJNY));
    }


    /**
     * 获取催缴历史记录
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayCallOps/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayCallHistory(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取催缴历史记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayCallHistory(tokenContext, YWLSH));
    }


    /**
     * 提交催缴
     *
     * @param unitpaycallpost
     **/
    @Path("/UnitPayCallOps")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitPayCall(final @Context HttpRequest request, final RequestWrapper<UnitPayCallPost> unitpaycallpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("提交催缴");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitPayCall(tokenContext, unitpaycallpost == null ? null : unitpaycallpost.getReq()));
    }


    /**
     * 单位缴存比例调整修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitDepositRatio/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitDepositRatio(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitDepositRatioPut> unitdepositratioput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitDepositRatio(tokenContext, YWLSH, unitdepositratioput == null ? null : unitdepositratioput.getReq()));
    }


    /**
     * 单位缴存比例调整详情 //
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitDepositRatio/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositRatio(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositRatio(tokenContext, YWLSH));
    }


    /**
     * 汇缴申请修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitRemittance/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitRemittance(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitRemittancePut> unitremittanceput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("汇缴申请修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitRemittance(tokenContext, YWLSH, unitremittanceput == null ? null : unitremittanceput.getReq()));
    }


    /**
     * 汇缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitRemittance/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitRemittance(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("汇缴申请详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitRemittance(tokenContext, YWLSH));
    }


    /**
     * 单位汇缴-缴存回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitRemittance/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitRemittanceReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位汇缴-缴存回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitRemittanceReceipt(tokenContext, YWLSH));
    }


    /**
     * 修改错缴申请
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayWrong/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitPayWrong(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitPayWrongPut> unitpaywrongput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("修改错缴申请");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitPayWrong(tokenContext, YWLSH, unitpaywrongput == null ? null : unitpaywrongput.getReq()));
    }


    /**
     * 获取错缴详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayWrong/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayWrong(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取错缴详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayWrong(tokenContext, YWLSH));
    }


    /**
     * 申请单位缴存比例调整时，先获取单位相关信息
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitDepositRatioAuto/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositRatioAuto(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("申请单位缴存比例调整时，先获取单位相关信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositRatioAuto(tokenContext, DWZH));
    }


    /**
     * 新建汇缴申请
     **/
    @Path("/UnitRemittance")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitRemittance(final @Context HttpRequest request, final RequestWrapper<UnitRemittancePost> unitremittancepost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新建汇缴申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitRemittance(tokenContext, unitremittancepost == null ? null : unitremittancepost.getReq()));
    }


    /**
     * 获取补缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/UnitPayBackList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitListPayBackRes(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD,
                                          final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ,
                                          final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取补缴记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayBackList(tokenContext, DWMC, DWZH, YWZT, CZY,YWWD, KSSJ, JSSJ, pageNo, pageSize));
    }

    @Path("/UnitPayBackList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitListPayBackRes(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD,
                                          final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ,
                                          final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,
                                          final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取补缴记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayBackList(tokenContext, DWMC, DWZH, YWZT, CZY, YWWD, KSSJ, JSSJ, marker, pageSize, action));
    }

    /**
     * 获取单位补缴申请时的单位信息
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitPayBackAuto/{DWZH}/{HBJNY}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayBackAuto(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH, final @PathParam("HBJNY") String HBJNY) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取单位补缴申请时的单位信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayBackAuto(tokenContext, DWZH, HBJNY));
    }


    /**
     * 新建补缴申请
     **/
    @Path("/UnitPayBack")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitPayBack(final @Context HttpRequest request, final RequestWrapper<UnitPayBackPost> unitpaybackpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新建补缴申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitPayBack(tokenContext, unitpaybackpost == null ? null : unitpaybackpost.getReq()));
    }


    /**
     * 查看单位缴存信息（从列表进入）
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitDepositDetail/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositDetail(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("查看单位缴存信息（从列表进入）");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositDetail(tokenContext, DWZH));
    }


    /**
     * 个人缴存基数调整列表、搜索
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/PersonRadixList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonRadixList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                       final @QueryParam("YWZT") String YWZT, final @QueryParam("KSSJ") String KSSJ,
                                       final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo,
                                       final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整列表、搜索");

        return ResUtils.wrapEntityIfNeeded(this.service.getPersonRadixList(tokenContext, DWMC, DWZH, YWZT, KSSJ, JSSJ, pageNo, pageSize));
    }


    /**
     * 个人缴存基数调整列表、搜索
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/PersonRadixList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonRadixListnew(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("YWZT") String YWZT, final @QueryParam("KSSJ") String KSSJ,
                                          final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker,
                                          final @QueryParam("pageSize") String pageSize, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整列表、搜索");

        return ResUtils.wrapEntityIfNeeded(this.service.getPersonRadixListnew(tokenContext, DWMC, DWZH, YWZT, KSSJ, JSSJ, marker, pageSize, action));
    }


    /**
     * 个人缴存基数调整申请
     **/
    @Path("/PersonRadix")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitCardinalNumber(final @Context HttpRequest request, final RequestWrapper<PersonRadixPost> personradixpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitCardinalNumber(tokenContext, personradixpost == null ? null : personradixpost.getReq()));
    }


    /**
     * 根据单位账号获取个人缴存基数调整前详情
     *
     * @param DWZH 单位账号
     **/
    @Path("/PersonRadix")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonRadixBeforeDetail(final @Context HttpRequest request, final @QueryParam("DWZH") String DWZH
            , final @QueryParam("SXNY") String SXNY) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("根据单位账号获取个人缴存基数调整前详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getPersonRadixBeforeDetail(tokenContext, DWZH,SXNY));
    }


    /**
     * 根据单位账号获取汇缴申请的单位信息
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitRemittanceAuto/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitRemittanceAuto(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("根据单位账号获取汇缴申请的单位信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitRemittanceAuto(tokenContext, DWZH));
    }


    /**
     * 新建错缴更正申请
     **/
    @Path("/UnitPayWrong")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitPayWrong(final @Context HttpRequest request, final RequestWrapper<UnitPayWrongPost> unitpaywrongpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新建错缴更正申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitPayWrong(tokenContext, unitpaywrongpost == null ? null : unitpaywrongpost.getReq()));
    }


    /**
     * 公积金补缴通知单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayBackNotice/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitPayBackNotice(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("公积金补缴通知单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitPayBackNotice(tokenContext, YWLSH));
    }

    /**
     * 公积金汇缴通知单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitRemittanceNotice/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitRemittanceNotice(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("公积金汇缴通知单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitRemittanceNotice(tokenContext, YWLSH));
    }


    /**
     * 获取汇缴清册信息（获取清册数据，从清册业务表读取）
     *
     * @param DWZH 单位账号
     **/
    @Path("/UnitRemittanceInventoryAuto/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitInventoryList(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取汇缴清册信息（获取清册数据，从清册业务表读取）");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitInventoryList(tokenContext, DWZH));
    }

    /**
     * 打印清册回执单
     **/
    @Path("/UnitRemittanceInventoryDetail/receipt/{DWZH}/{QCNY}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headRemittanceInventory(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH,
                                            final @PathParam("QCNY") String QCNY) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("打印清册回执单");
        return ResUtils.wrapEntityIfNeeded(this.service.headRemittanceInventory(tokenContext, DWZH, QCNY));
    }

    /**
     * 打印清册回执单
     *
     * @param QCQRDH 清册确认单号
     **/
    @Path("/UnitRemittanceInventoryDetail/receipt/{QCQRDH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headRemittanceInventory(final @Context HttpRequest request, final @PathParam("QCQRDH") String QCQRDH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("打印清册回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headRemittanceInventory(tokenContext, QCQRDH));
    }


    /**
     * 提交缓缴申请
     **/
    @Path("/UnitPayHold")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitPayHold(final @Context HttpRequest request, final RequestWrapper<UnitPayHoldPost> unitpayholdpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("提交缓缴申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitPayHold(tokenContext, unitpayholdpost == null ? null : unitpayholdpost.getReq()));
    }


    /**
     * 缓缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayHold/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitPayHoldReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("缓缴回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitPayHoldReceipt(tokenContext, YWLSH));
    }

    /**
     * 批量提交缓缴
     *
     * @param body 业务流水号集合
     * @return
     */
    @Path("/UnitPayHold/submitUnitPayHold")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitUnitPayHold(final @Context HttpRequest request, final RequestWrapper<BatchSubmission> body) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("批量提交缓缴");

        return ResUtils.wrapEntityIfNeeded(this.service.submitUnitPayHold(tokenContext, body == null ? null : body.getReq()));
    }


    /**
     * 获取错缴更正业务记录列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/UnitPayWrongList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayWrongList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH, final @QueryParam("YWZT") String YWZT,
                                        final @QueryParam("CZY") String CZY, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取错缴更正业务记录列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayWrongList(tokenContext, DWMC, DWZH, YWZT, CZY, KSSJ, JSSJ, pageNo, pageSize));
    }

    @Path("/UnitPayWrongList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayWrongList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH, final @QueryParam("YWZT") String YWZT,
                                        final @QueryParam("CZY") String CZY, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,
                                        final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取错缴更正业务记录列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayWrongList(tokenContext, DWMC, DWZH, YWZT, CZY, KSSJ, JSSJ, marker, pageSize, action));
    }

    /**
     * 错缴业务回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayWrong/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitPayWrongReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("错缴业务回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitPayWrongReceipt(tokenContext, YWLSH));
    }

    /**
     * 根据单位账号和更正缴至年月,获取单位错缴信息
     */
    @Path("/UnitPayWrong/{DWZH}/{GRJZNY}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response autoGetPayWrong(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH, final @PathParam("GRJZNY") String GRJZNY) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("根据单位账号和更正缴至年月,获取单位错缴信息");

        return ResUtils.wrapEntityIfNeeded(this.service.autoGetPayWrong(tokenContext, DWZH, GRJZNY));
    }


    /**
     * 汇缴清册详情
     *
     * @param QCQRDH 清册确认单号
     **/
    @Path("/UnitRemittanceInventoryDetail/{QCQRDH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitRemittanceInventoryDetail(final @Context HttpRequest request, final @PathParam("QCQRDH") String QCQRDH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("汇缴清册详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitRemittanceInventoryDetail(tokenContext, QCQRDH));
    }


    /**
     * 单位汇缴业务记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/UnitRemittanceList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitRemittanceList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YHMC") String YHMC, final @QueryParam("YWWD") String YWWD,
                                          final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("YWPCH") String YWPCH,
                                          final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位汇缴业务记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitRemittanceList(tokenContext, DWMC, DWZH, YWZT, CZY, YHMC, YWWD, KSSJ, JSSJ, YWPCH, pageNo, pageSize));
    }

    @Path("/UnitRemittanceList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitRemittanceList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YHMC") String YHMC, final @QueryParam("YWWD") String YWWD,
                                          final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("YWPCH") String YWPCH,
                                          final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,
                                          final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位汇缴业务记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitRemittanceList(tokenContext, DWMC, DWZH, YWZT, CZY, YHMC, YWWD, KSSJ, JSSJ, YWPCH, marker, pageSize, action));
    }

    /**
     * 催缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     **/
    @Path("/UnitPayCallList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayCallList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC,
                                       final @QueryParam("DWZH") String DWZH, final @QueryParam("pageNo") String pageNo,
                                       final @QueryParam("pageSize") String pageSize, final @QueryParam("KSSJ") String KSSJ,
                                       final @QueryParam("JSSJ") String JSSJ) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("催缴记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayCallList(tokenContext, DWMC, DWZH, pageNo, pageSize, KSSJ, JSSJ));
    }

    @Path("/UnitPayCallList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayCallList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC,
                                       final @QueryParam("DWZH") String DWZH, final @QueryParam("marker") String marker,
                                       final @QueryParam("pageSize") String pageSize, final @QueryParam("KSSJ") String KSSJ,
                                       final @QueryParam("JSSJ") String JSSJ, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("催缴记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayCallList(tokenContext, DWMC, DWZH, marker, pageSize, KSSJ, JSSJ, action));
    }

//    /**
//     *获取催缴详情
//     *@param YWLSH 业务流水号
//     **/
//    @Path("/UnitPayCallDetail/{YWLSH}")
//    @GET
//    @Produces("application/json; charset=utf-8")
//    public Response getUnitPayCallDetail(final @Context  HttpRequest request,final @PathParam("YWLSH") String YWLSH){
//
//        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
//System.out.println("获取催缴详情");
//
//        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayCallDetail(tokenContext, YWLSH )) ;
//    }


    /**
     * 催缴回执单（打印催缴记录）
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayCallDetail/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitPayCallReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("催缴回执单（打印催缴记录）");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitPayCallReceipt(tokenContext, YWLSH));
    }


    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/UnitPayHoldList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayHoldList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH, final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取缓缴业务记录列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayHoldList(tokenContext, DWMC, DWZH, YWZT, CZY,YWWD, KSSJ, JSSJ, pageNo, pageSize));
    }

    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    @Path("/UnitPayHoldList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayHoldListNew(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH, final @QueryParam("YWZT") String YWZT, final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取缓缴业务记录列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayHoldListNew(tokenContext, DWMC, DWZH, YWZT, CZY, YWWD,KSSJ, JSSJ, marker, pageSize, action));
    }


    /**
     * 缴存列表信息
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param GLWD 是否根据网点筛选
     * @param GLXH 是否筛选已销户单位
     **/
    @Path("/UnitDepositList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositListRes(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page,
                                          final @QueryParam("GLWD") boolean GLWD, final @QueryParam("GLXH") boolean GLXH, final @QueryParam("KHWD") String KHWD,
                                          final @QueryParam("SFYWFTYE") String SFYWFTYE,final @QueryParam("JZNY") String JZNY) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("缴存列表信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositListRes(tokenContext, DWMC, DWZH, pageSize, page, GLWD, GLXH, KHWD,SFYWFTYE,JZNY));
    }

    /**
     * 单位缴存列表信息
     *
     * @param CXNX 查询年限
     * @param DWZH 单位账号
     **/
    @Path("/UnitDepositList/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositListRes(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH, final @QueryParam("CXNX") String CXNX, final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String page, String A) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("缴存列表信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositInfoList(tokenContext, DWZH, CXNX, pageSize, page));
    }


    /**
     * 单位缓缴修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayHold/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitPayHold(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitPayHoldPut> unitpayholdput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缓缴修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitPayHold(tokenContext, YWLSH, unitpayholdput == null ? null : unitpayholdput.getReq()));
    }


    /**
     * 获取缓缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayHold/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayHold(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取缓缴申请详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayHold(tokenContext, YWLSH));
    }


    /**
     * 单位缴存比例调整列表、搜索
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ 受理时间起点
     * @param JSSJ 受理时间终点
     **/
    @Path("/UnitDepositRatioList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositRatioList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                            final @QueryParam("YWZT") String YWZT, final @QueryParam("KSSJ") String KSSJ,
                                            final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo,
                                            final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整列表、搜索");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositRatioList(tokenContext, DWMC, DWZH, YWZT, KSSJ, JSSJ, pageNo, pageSize));
    }

    /**
     * 单位缴存比例调整列表、搜索
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ 受理时间起点
     * @param JSSJ 受理时间终点
     **/
    @Path("/UnitDepositRatioList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitDepositRatioList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                            final @QueryParam("YWZT") String YWZT, final @QueryParam("KSSJ") String KSSJ,
                                            final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker,
                                            final @QueryParam("pageSize") String pageSize, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整列表、搜索");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitDepositRatioListnew(tokenContext, DWMC, DWZH, YWZT, KSSJ, JSSJ, marker, pageSize, action));
    }


    /**
     * 缴存清册列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param CZY  操作员
     * @param KSSJ 受理时间开始(开始时间）
     * @param JSSJ 受理时间结束(结束时间）
     **/
    @Path("/UnitInventory")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getInventoryList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                     final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD, final @QueryParam("KSSJ") String KSSJ,
                                     final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo,
                                     final @QueryParam("pageSize") String pageSize) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("缴存清册列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getInventoryList(tokenContext, DWMC, DWZH, CZY, YWWD, KSSJ, JSSJ, pageNo, pageSize));
    }

    @Path("/UnitInventory/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getInventoryList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                     final @QueryParam("CZY") String CZY, final @QueryParam("YWWD") String YWWD ,final @QueryParam("KSSJ") String KSSJ,
                                     final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker,
                                     final @QueryParam("pageSize") String pageSize, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("缴存清册列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getInventoryList(tokenContext, DWMC, DWZH, CZY, YWWD, KSSJ, JSSJ, marker, pageSize, action));
    }


    /**
     * 清册确认
     **/
    @Path("/UnitInventory")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postInventoryConfirm(final @Context HttpRequest request, final RequestWrapper<InventoryConfirmPost> inventoryconfirmpost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("清册确认");

        return ResUtils.wrapEntityIfNeeded(this.service.postInventoryConfirm(tokenContext, inventoryconfirmpost == null ? null : inventoryconfirmpost.getReq()));
    }


    /**
     * 个人缴存基数调整修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/PersonRadix/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPersonRadix(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<PersonRadixPut> personradixput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putPersonRadix(tokenContext, YWLSH, personradixput == null ? null : personradixput.getReq()));
    }


    /**
     * 个人缴存基数调整详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/PersonRadix/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonRadix(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getPersonRadix(tokenContext, YWLSH));
    }


    /**
     * 公积金补缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayBack/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitPayBackReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("公积金补缴回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitPayBackReceipt(tokenContext, YWLSH));
    }


    /**
     * 单位缴存比例调整申请
     **/
    @Path("/UnitDepositRatio")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitDepositRatio(final @Context HttpRequest request, final RequestWrapper<PostListUnitDepositRatioPost> postlistunitdepositratiopost) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitDepositRatio(tokenContext, postlistunitdepositratiopost == null ? null : postlistunitdepositratiopost.getReq()));
    }


    /**
     * 单位缴存比例调整回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitDepositRatio/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitDepositRatioReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位缴存比例调整回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitDepositRatioReceipt(tokenContext, YWLSH));
    }

    /**
     * 批量提交
     **/
    @Path("/UnitDepositRatioSubmit")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitBatchUnit(final @Context HttpRequest request, final RequestWrapper<BatchSubmission> body) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("批量单位");

        return ResUtils.wrapEntityIfNeeded(this.service.unitSubmitBatch(tokenContext, body == null ? null : body.getReq()));
    }


    /**
     * 批量提交
     **/
    @Path("/PersonRadixSubmit")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitBatchPerson(final @Context HttpRequest request, final RequestWrapper<BatchSubmission> body) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("批量个人");

        return ResUtils.wrapEntityIfNeeded(this.service.personSubmitBatch(tokenContext, body == null ? null : body.getReq()));
    }


    /**
     * 个人缴存基数调整回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/PersonRadix/receipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headPersonRadix(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人缴存基数调整回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headPersonRadix(tokenContext, YWLSH));
    }


    /**
     * 修改补缴
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayBack/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitPayBack(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitPayBackPut> unitpaybackput) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("修改补缴");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitPayBack(tokenContext, YWLSH, unitpaybackput == null ? null : unitpaybackput.getReq()));
    }


    /**
     * 获取单位补缴详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/UnitPayBack/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitPayBack(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取单位补缴详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitPayBack(tokenContext, YWLSH));
    }

    /**
     * 单位补缴重新使用暂收来进行缴款
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/PayBackTemporary/{YWLSH}")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayBackTemporary(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位补缴重新使用暂收来进行缴款");

        return ResUtils.wrapEntityIfNeeded(this.service.postPayBackTemporary(tokenContext, YWLSH));
    }

    /**
     * 单位业务明细列表
     *
     * @param DWMC   单位名称
     * @param DWZH   单位账号
     * @param YWMXLX 业务明细类型
     **/
    @Path("/UnitBusniessDetailList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitBusnissDetailList(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                             final @QueryParam("YWMXLX") String YWMXLX,
                                             final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize
            , final @QueryParam("start") String start, final @QueryParam("end") String end) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位业务明细列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitBusnissDetailList(tokenContext, DWMC, DWZH, YWMXLX, pageNo, pageSize, start, end));
    }

    /**
     * 单位业务明细列表
     *
     * @param DWMC   单位名称
     * @param DWZH   单位账号
     * @param YWMXLX 业务明细类型
     **/
    @Path("/UnitBusniessDetailList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitBusnissDetailListnew(final @Context HttpRequest request, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWZH") String DWZH,
                                                final @QueryParam("YWMXLX") String YWMXLX,
                                                final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize
            , final @QueryParam("start") String start, final @QueryParam("end") String end, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单位业务明细列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitBusnissDetailListnew(tokenContext, DWMC, DWZH, YWMXLX, marker, pageSize, start, end, action));
    }

    /**
     * 个人业务明细列表
     *
     * @param XingMing 姓名
     * @param GRZH     个人账号
     * @param YWMXLX   业务明细类型
     **/
    @Path("/IndiBusniessDetailList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getIndiBusnissDetailList(final @Context HttpRequest request, final @QueryParam("XingMing") String XingMing, final @QueryParam("GRZH") String GRZH,
                                             final @QueryParam("YWMXLX") String YWMXLX,
                                             final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize, final @QueryParam("start") String start,
                                             final @QueryParam("end") String end
            , final @QueryParam("ZJHM")String ZJHM) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人业务明细列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getIndiBusnissDetailList(tokenContext, XingMing, GRZH, YWMXLX, pageNo, pageSize, start, end,ZJHM));
    }

    /**
     * 个人业务明细列表
     *
     * @param XingMing 姓名
     * @param GRZH     个人账号
     * @param YWMXLX   业务明细类型
     **/
    @Path("/IndiBusniessDetailList/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getIndiBusnissDetailListnew(final @Context HttpRequest request, final @QueryParam("XingMing") String XingMing, final @QueryParam("GRZH") String GRZH,
                                                final @QueryParam("YWMXLX") String YWMXLX,
                                                final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize, final @QueryParam("start") String start,
                                                final @QueryParam("end") String end, final @QueryParam("action") String action) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("个人业务明细列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getIndiBusnissDetailListnew(tokenContext, XingMing, GRZH, YWMXLX, marker, pageSize, start, end, action));
    }

    /**
     * 获取多月的清册信息
     *
     * @param DWZH 单位账号
     **/
    @Path("/InventoryBatchList")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getInventoryBatchList(final @Context HttpRequest request, final @QueryParam("DWZH") String DWZH,
                                          final @QueryParam("QCSJQ") String QCSJQ, final @QueryParam("QCSJZ") String QCSJZ,
                                          final @QueryParam("YWLSH") String YWLSH) {

        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取多月的清册信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getInventoryBatchList(tokenContext, DWZH, QCSJQ, QCSJZ, YWLSH));
    }

    /**
     * 清册受理提交
     *
     * @param dwzh  单位账号
     * @param qcsjq 清册年月起
     * @param qcsjq 清册年月止
     * @param jkfs  缴款方式
     **/
    @Path("/InventorySubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postInventoryBatch(final @Context HttpRequest request, final @QueryParam("DWZH") String dwzh,
                                       final @QueryParam("QCSJQ") String qcsjq, final @QueryParam("QCSJZ") String qcsjz,
                                       final @QueryParam("JKFS") String jkfs, final @QueryParam("CXQC") String CXQC) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("清册受理提交");
        return ResUtils.wrapEntityIfNeeded(this.service.postInventoryBatch(tokenContext, dwzh, qcsjq, qcsjz, jkfs, CXQC));
    }

    /**
     * 获取单位该月的最新清册详情
     *
     * @param request
     * @param YWLSH   业务流水号
     **/
    @Path("/LatestInventoryOfMonth/{QCQRDH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getLatestInventoryOfMonth(final @Context HttpRequest request, final @PathParam("QCQRDH") String YWLSH) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取单位该月的最新清册详情");
        return ResUtils.wrapEntityIfNeeded(this.service.getLatestInventoryOfMonth(tokenContext, YWLSH));
    }

    /**
     * 获取单位该月的最初的汇缴清册详情
     *
     * @param request
     * @param YWLSH   业务流水号
     **/
    @Path("/FirstInventoryOfMonth/{QCQRDH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getFirstInventoryOfMonth(final @Context HttpRequest request, final @PathParam("QCQRDH") String YWLSH) {
        System.out.println("获取单位该月的最初的汇缴清册详情");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getFirstInventoryOfMonth(tokenContext, YWLSH));
    }

    /**
     * 获取单位该月的最初的汇缴清册详情
     *
     * @param request
     **/
    @Path("/InventoryDetail/")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getInventoryDetail(final @Context HttpRequest request, final @QueryParam("DWZH") String DWZH,
                                       final @QueryParam("QCNY") String QCNY, final @QueryParam("XingMing") String XingMing,
                                       final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("生成单位该月的清册详情的详情..");
        return ResUtils.wrapEntityIfNeeded(this.service.getInventoryDetail(DWZH, QCNY, XingMing, pageNo, pageSize));
    }


    /**
     * 获取单位该月的最初的汇缴清册详情
     **/
    @Path("/UnitInventoryInit/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitInventoryInit(final @Context HttpRequest request, final @PathParam("DWZH") String DWZH) {
        System.out.println("获取单位该月的最初的汇缴清册详情");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getUnitInventoryInit(tokenContext, DWZH));
    }
}