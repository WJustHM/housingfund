package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.FSE;
import com.handge.housingfund.common.service.finance.model.VoucherBatchPdfPost;
import com.handge.housingfund.common.service.finance.model.VoucherModel;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IVoucherAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-8-16.
 */

@Path("/finance/voucher")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class VoucherResource {

    @Autowired
    IVoucherAPI iVoucherAPI;

    @Path("")
    @GET
    public Response getVoucherList(final @QueryParam("MBBH") String mbbh,
                                   final @QueryParam("YWMC") String ywmc,
                                   final @QueryParam("KM") String km,
                                   final @DefaultValue("1") @QueryParam("pageNo") int pageNo,
                                   final @DefaultValue("50") @QueryParam("pageSize") int pageSize) {
        System.out.println("获取业务凭证列表");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherList(mbbh, ywmc, km, pageNo, pageSize));
    }

    @GET
    @Path("/{id}")
    public Response getVoucher(final @PathParam("id") String id) {
        System.out.println("获取业务凭证详情");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucher(id));
    }

    @GET
    @Path("/byywid/{ywid}")
    public Response getVoucherByYWID(final @PathParam("ywid") String ywid) {
        System.out.println("获取业务凭证详情");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherByYWID(ywid));
    }

    @Path("")
    @POST
    public Response addVoucher(final RequestWrapper<VoucherModel> voucherModel) {
        System.out.println("新增业务凭证");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.addVoucher(voucherModel == null ? null : voucherModel.getReq()));
    }

    @PUT
    @Path("/{id}")
    public Response updateVoucher(final @PathParam("id") String id, final RequestWrapper<VoucherModel> voucherModel) {
        System.out.println("修改业务凭证");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.updateVoucher(id, voucherModel == null ? null : voucherModel.getReq()));
    }

    @DELETE
    @Path("/{id}")
    public Response delVoucher(final @PathParam("id") String id) {
        System.out.println("删除业务凭证");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.deleteVoucher(id));
    }

    @Path("")
    @DELETE
    public Response delVouchers(final RequestWrapper<ArrayList<String>> delList) {
        System.out.println("删除业务凭证");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.delVouchers(delList == null ? null : delList.getReq()));
    }

    /**
     * @param HRHM     汇入户名
     * @param state    状态 (0：所有，1：已分配，2：未分配)
     * @param HRSJKS   汇入时间开始
     * @param HRSJJS   汇入时间结束
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GET
    @Path("/temporaryRecord/list")
    public Response getTemporaryRecordList(final @QueryParam("HRHM") String HRHM, final @QueryParam("state") int state,
                                           final @QueryParam("HRSJKS") String HRSJKS, final @QueryParam("HRSJJS") String HRSJJS,
                                           final @QueryParam("FSE") String FSE, final @QueryParam("JZPZH") String JZPZH,
                                           final @QueryParam("YHMC") String YHMC, final @QueryParam("ZHHM") String ZHHM,
                                           final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo) {
        System.out.println("获取暂收列表");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getTemporaryRecordList(HRHM, state, HRSJKS, HRSJJS, FSE, JZPZH, YHMC, ZHHM, pageSize, pageNo));
    }

    /**
     * @param HRHM     汇入户名
     * @param state    状态 (0：所有，1：已分配，2：未分配)
     * @param HRSJKS   汇入时间开始
     * @param HRSJJS   汇入时间结束
     * @param markercesu
     * @param action
     * @param pageSize
     * @return
     */
    @GET
    @Path("/temporaryRecord/list/new")
    public Response getTemporaryRecordList(final @QueryParam("HRHM") String HRHM, final @QueryParam("state") int state,
                                           final @QueryParam("HRSJKS") String HRSJKS, final @QueryParam("HRSJJS") String HRSJJS,
                                           final @QueryParam("FSE") String FSE, final @QueryParam("JZPZH") String JZPZH,
                                           final @QueryParam("YHMC") String YHMC, final @QueryParam("ZHHM") String ZHHM,
                                           final @QueryParam("marker") String marker, final @QueryParam("action") String action,
                                           final @QueryParam("pageSize") String pageSize) {
        System.out.println("获取暂收列表");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getTemporaryRecordList(HRHM, state, HRSJKS, HRSJJS, FSE, JZPZH, YHMC, ZHHM, marker, action, pageSize));
    }


    /**
     * @param PZRQKS   凭证日期开始
     * @param PZRQJS   凭证日期结束
     * @param PZH      凭证号
     * @param YWLX     业务类型
     * @param YWMC     业务名称
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return
     */
    @GET
    @Path("/managerlist")
    public Response getVoucherManagerList(final @QueryParam("PZRQKS") String PZRQKS,
                                          final @QueryParam("PZRQJS") String PZRQJS,
                                          final @QueryParam("PZH") String PZH,
                                          final @QueryParam("YWLX") String YWLX,
                                          final @QueryParam("YWMC") String YWMC,
                                          final @QueryParam("ZhaiYao") String ZhaiYao,
                                          final @QueryParam("FSE") String FSE,
                                          final @QueryParam("pageNo") String pageNo,
                                          final @QueryParam("pageSize") String pageSize) {
        System.out.println("获取业务凭证管理列表");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherManagerList(PZRQKS, PZRQJS, PZH, YWLX, YWMC, ZhaiYao, FSE, pageSize, pageNo));
    }

    /**
     * @param PZRQKS 凭证日期开始
     * @param PZRQJS 凭证日期结束
     * @param PZH    凭证号
     * @param YWLX   业务类型
     * @param YWMC   业务名称
     * @return
     */
    @GET
    @Path("/managerlist/new")
    public Response getVoucherManagerList(final @QueryParam("PZRQKS") String PZRQKS,
                                          final @QueryParam("PZRQJS") String PZRQJS,
                                          final @QueryParam("PZH") String PZH,
                                          final @QueryParam("YWLX") String YWLX,
                                          final @QueryParam("YWMC") String YWMC,
                                          final @QueryParam("FSE") String FSE,
                                          final @QueryParam("ZhaiYao") String ZhaiYao,
                                          final @QueryParam("marker") String marker,
                                          final @QueryParam("action") String action,
                                          final @QueryParam("pageSize") String pageSize) {
        System.out.println("获取业务凭证管理列表");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherManagerList(PZRQKS, PZRQJS, PZH, YWLX, YWMC, ZhaiYao, FSE, marker, action, pageSize));
    }

    /**
     * @param JZPZH 凭证号
     * @return
     */
    @GET
    @Path("/details/managerdetails")
    public Response getVoucherManagerDetail(final @QueryParam("JZPZH") String JZPZH) {
        System.out.println("获取业务凭证管理详情");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherManagerDetail(JZPZH));
    }

    /**
     * @param JZPZH 凭证号
     * @return
     */
    @GET
    @Path("/managerdetails/pdf")
    public Response getVoucherManagerDetailpdf(final @QueryParam("JZPZH") String JZPZH) {
        System.out.println("获取业务凭证管理pdf");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getVoucherManagerDetailpdf(JZPZH));
    }

    /**
     * @param YWLSH 业务流水号
     * @param CZNR  操作内容
     * @return
     */
    @GET
    @Path("/businessdetails")
    public Response getBusinessDetail(final @QueryParam("YWLSH") String YWLSH,
                                      final @QueryParam("CZNR") String CZNR) {
        System.out.println("获取业务凭证管理业务详情");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getBusinessDetail(YWLSH, CZNR));
    }

    /**
     * @param KJQJ 会计期间 eg:201708
     * @return
     */
    @GET
    @Path("/checkVoucher")
    public Response checkVoucher(final @Context HttpRequest request, final @QueryParam("KJQJ") String KJQJ) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("结账");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.checkVoucher(tokenContext, KJQJ));
    }

    /**
     * @param JZRQ 截止日期
     * @return
     */
    @GET
    @Path("/getSubjectsCollect")
    public Response getSubjectsCollect(final @QueryParam("JZRQ") String JZRQ) {
        System.out.println("获取科目汇总");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getSubjectsCollect(JZRQ));
    }

    /**
     * @param KMBH    科目名称
     * @param KJQJKS  会计期间开始 eg:201701
     * @param KJQJJS  会计期间结束 eg:201704
     * @param BHWJZPZ 是否包含未结账凭证
     * @return
     */
    @GET
    @Path("/booksDetails")
    public Response getBooksDetails(final @QueryParam("KMBH") String KMBH,
                                    final @QueryParam("KJQJKS") String KJQJKS,
                                    final @QueryParam("KJQJJS") String KJQJJS,
                                    final @QueryParam("BHWJZPZ") boolean BHWJZPZ) {
        System.out.println("获取明细账");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getBooksDetails(KMBH, KJQJKS, KJQJJS, BHWJZPZ));
    }

    /**
     * 总账
     *
     * @param KMBH
     * @param KJQJKS
     * @param KJQJJS
     * @param BHWJZPZ
     * @return
     */
    @GET
    @Path("/booksGeneral")
    public Response getBooksGeneral(final @QueryParam("KMBH") String KMBH,
                                    final @QueryParam("KJQJKS") String KJQJKS,
                                    final @QueryParam("KJQJJS") String KJQJJS,
                                    final @QueryParam("BHWJZPZ") boolean BHWJZPZ) {
        System.out.println("总账");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.getBooksGeneral(KMBH, KJQJKS, KJQJJS, BHWJZPZ));
    }

    /**
     * 批量凭证pdf
     *
     * @param voucherBatchPdfPost
     * @return
     */
    @POST
    @Path("/batchVocherPdf")
    public Response batchVoucherPdf(final RequestWrapper<VoucherBatchPdfPost> voucherBatchPdfPost) {
        System.out.println("批量凭证pdf");
        return ResUtils.wrapEntityIfNeeded(iVoucherAPI.batchVoucherPdf(voucherBatchPdfPost == null ? null : voucherBatchPdfPost.getReq()));
    }

}
