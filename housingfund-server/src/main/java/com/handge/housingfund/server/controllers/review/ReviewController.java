package com.handge.housingfund.server.controllers.review;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.BatchReviewCheck;
import com.handge.housingfund.common.service.review.model.BatchReviewInfo;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.review.api.IReviewAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Liujuhao on 2017/9/25.
 */

@Path("/review")
@Controller
public class ReviewController {

    @Autowired
    private IReviewAPI<Response> service;

    /**
     * 获取未审核列表
     *
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/list/{module}/{type}")
    public Response getReviewList(@Context HttpRequest httpRequest,
                                  /**归集**/
                                  final @QueryParam("DWMC") String DWMC,
                                  final @QueryParam("XingMing") String XingMing,
                                  final @QueryParam("CZY") String CZY,
                                  final @QueryParam("GRZH") String GRZH,
                                  final @QueryParam("TQYY") String TQYY,
                                  final @QueryParam("ZJHM") String ZJHM,
                                  final @QueryParam("ZRGJJZXMC") String ZRZXMC,
                                  final @QueryParam("ZCGJJZXMC") String ZCZXMC,
                                  final @QueryParam("DWZH") String DWZH,
                                  /**贷款**/
                                  final @QueryParam("JKRXM") String JKRXM,
                                  final @QueryParam("FKGS") String FKGS,
                                  final @QueryParam("LPMC") String LPMC,
                                  final @QueryParam("HKLX") String HKLX,
                                  final @QueryParam("JKHTBH") String JKHTBH,
                                  /**财务**/
                                  final @QueryParam("KHYHMC") String KHYHMC,
                                  final @QueryParam("ZHHM") String ZHHM,
                                  final @QueryParam("YWMC") String YWMC,
                                  /**公共**/
                                  final @QueryParam("YWLX") String YWLX,
                                  final @QueryParam("pageSize") String pageSize,
                                  final @QueryParam("pageNo") String pageNo,
                                  final @QueryParam("KSSJ") String KSSJ,
                                  final @QueryParam("JSSJ") String JSSJ,
                                  final @PathParam("module") String module,
                                  final @PathParam("type") String type) {

        System.out.println("获取未审核列表");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.getReviewList(
                tokenContext, YWLX, KSSJ, JSSJ, pageSize, pageNo, module, type,
                DWMC, XingMing, CZY, GRZH, TQYY,ZJHM, ZRZXMC, ZCZXMC, DWZH,
                JKRXM, FKGS, LPMC, HKLX, JKHTBH,
                KHYHMC, ZHHM, YWMC));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/list/new/{module}/{type}")
    public Response getReviewList(@Context HttpRequest httpRequest,
                                  /**归集**/
                                  final @QueryParam("DWMC") String DWMC,
                                  final @QueryParam("XingMing") String XingMing,
                                  final @QueryParam("CZY") String CZY,
                                  final @QueryParam("GRZH") String GRZH,
                                  final @QueryParam("TQYY") String TQYY,
                                  /**贷款**/
                                  final @QueryParam("JKRXM") String JKRXM,
                                  final @QueryParam("FKGS") String FKGS,
                                  final @QueryParam("LPMC") String LPMC,
                                  final @QueryParam("HKLX") String HKLX,
                                  final @QueryParam("JKHTBH") String JKHTBH,
                                  /**财务**/
                                  final @QueryParam("KHYHMC") String KHYHMC,
                                  final @QueryParam("ZHHM") String ZHHM,
                                  final @QueryParam("YWMC") String YWMC,
                                  /**公共**/
                                  final @QueryParam("YWLX") String YWLX,
                                  final @QueryParam("pageSize") String pageSize,
                                  final @QueryParam("marker") String marker,
                                  final @QueryParam("action") String action,
                                  final @QueryParam("KSSJ") String KSSJ,
                                  final @QueryParam("JSSJ") String JSSJ,
                                  final @PathParam("module") String module,
                                  final @PathParam("type") String type) {

        System.out.println("获取未审核列表");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.getReviewList(
                tokenContext, YWLX, KSSJ, JSSJ, pageSize, marker, action, module, type,
                DWMC, XingMing, CZY, GRZH, TQYY,
                JKRXM, FKGS, LPMC, HKLX, JKHTBH,
                KHYHMC, ZHHM, YWMC));
    }

    /**
     * 获取已审核列表
     *
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/listed/{module}/{type}")
    public Response getReviewedList(@Context HttpRequest httpRequest,
                                    /**归集**/
                                    final @QueryParam("DWMC") String DWMC,
                                    final @QueryParam("XingMing") String XingMing,
                                    final @QueryParam("CZY") String CZY,
                                    final @QueryParam("GRZH") String GRZH,
                                    final @QueryParam("TQYY") String TQYY,
                                    final @QueryParam("ZJHM") String ZJHM,
                                    final @QueryParam("ZRGJJZXMC") String ZRZXMC,
                                    final @QueryParam("ZCGJJZXMC") String ZCZXMC,
                                    final @QueryParam("DWZH") String DWZH,
                                    /**贷款**/
                                    final @QueryParam("JKRXM") String JKRXM,
                                    final @QueryParam("FKGS") String FKGS,
                                    final @QueryParam("LPMC") String LPMC,
                                    final @QueryParam("HKLX") String HKLX,
                                    final @QueryParam("JKHTBH") String JKHTBH,
                                    /**财务**/
                                    final @QueryParam("KHYHMC") String KHYHMC,
                                    final @QueryParam("ZHHM") String ZHHM,
                                    final @QueryParam("YWMC") String YWMC,
                                    /**公共**/
                                    final @QueryParam("ZhuangTai") String ZhuangTai,
                                    final @QueryParam("YWLX") String YWLX,
                                    final @QueryParam("pageSize") String pageSize,
                                    final @QueryParam("pageNo") String pageNo,
                                    final @QueryParam("KSSJ") String KSSJ,
                                    final @QueryParam("JSSJ") String JSSJ,
                                    final @PathParam("module") String module,
                                    final @PathParam("type") String type) {

        System.out.println("获取已审核列表");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.getReviewedList(
                tokenContext, YWLX, KSSJ, JSSJ, pageSize, pageNo, module, type, ZhuangTai,
                DWMC, XingMing, CZY, GRZH, TQYY, ZJHM, ZRZXMC,ZCZXMC, DWZH,
                JKRXM, FKGS, LPMC, HKLX, JKHTBH,
                KHYHMC, ZHHM, YWMC));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/listed/new/{module}/{type}")
    public Response getReviewedList(@Context HttpRequest httpRequest,
                                    /**归集**/
                                    final @QueryParam("DWMC") String DWMC,
                                    final @QueryParam("XingMing") String XingMing,
                                    final @QueryParam("CZY") String CZY,
                                    final @QueryParam("GRZH") String GRZH,
                                    final @QueryParam("TQYY") String TQYY,
                                    /**贷款**/
                                    final @QueryParam("JKRXM") String JKRXM,
                                    final @QueryParam("FKGS") String FKGS,
                                    final @QueryParam("LPMC") String LPMC,
                                    final @QueryParam("HKLX") String HKLX,
                                    final @QueryParam("JKHTBH") String JKHTBH,
                                    /**财务**/
                                    final @QueryParam("KHYHMC") String KHYHMC,
                                    final @QueryParam("ZHHM") String ZHHM,
                                    final @QueryParam("YWMC") String YWMC,
                                    /**公共**/
                                    final @QueryParam("ZhuangTai") String ZhuangTai,
                                    final @QueryParam("YWLX") String YWLX,
                                    final @QueryParam("pageSize") String pageSize,
                                    final @QueryParam("marker") String marker,
                                    final @QueryParam("action") String action,
                                    final @QueryParam("KSSJ") String KSSJ,
                                    final @QueryParam("JSSJ") String JSSJ,
                                    final @PathParam("module") String module,
                                    final @PathParam("type") String type) {

        System.out.println("获取已审核列表");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.getReviewedList(
                tokenContext, YWLX, KSSJ, JSSJ, pageSize, marker, action, module, type, ZhuangTai,
                DWMC, XingMing, CZY, GRZH, TQYY,
                JKRXM, FKGS, LPMC, HKLX, JKHTBH,
                KHYHMC, ZHHM, YWMC));
    }

    /**
     * 审核操作
     *
     * @return
     */
    @POST
    @Path("/do/{module}/{type}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postBusinessReview(@Context HttpRequest httpRequest, final RequestWrapper<BatchReviewInfo> batchReviewInfo,
                                       @PathParam("module") String module, @PathParam("type") String type) {

        System.out.println("审核操作");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.postBusinessReview(tokenContext, batchReviewInfo == null ? null : batchReviewInfo.getReq(), module, type));
    }

    /**
     * 审核在途验证
     *
     * @param httpRequest
     * @param batchReviewCheckRequest
     * @param module
     * @param type
     * @return
     */
    @POST
    @Path("/check/{module}/{type}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCheckIsReviewing(@Context HttpRequest httpRequest, final RequestWrapper<BatchReviewCheck> batchReviewCheckRequest,
                                         @PathParam("module") String module, @PathParam("type") String type) {

        System.out.println("检查业务是否正在审核中");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.checkIsReviewing(tokenContext, batchReviewCheckRequest == null ? null : batchReviewCheckRequest.getReq(), module, type));
    }

    /**
     * 特审操作
     *
     * @param httpRequest
     * @param batchReviewInfo
     * @return
     */
    @POST
    @Path("/special/{module}/{type}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postSpecialReview(@Context HttpRequest httpRequest, final RequestWrapper<BatchReviewInfo> batchReviewInfo,
                                      @PathParam("module") String module, @PathParam("type") String type) {

        System.out.println("特审操作");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.postSpecialReview(tokenContext, batchReviewInfo == null ? null : batchReviewInfo.getReq(), module, type));
    }
}
