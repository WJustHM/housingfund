package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.AccChangeNoticeSMWJ;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IAccChangeNoticeAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/12.
 */
@Path("/finance/notice")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AccChangeNoticeResource {
    @Autowired
    IAccChangeNoticeAPI iAccChangeNoticeAPI;

    /**
     * 获取账号变动通知列表
     *
     * @param zhhm     专户号码
     * @param sfzz     是否已做账: 0:是,1:否,null:自动对账业务
     * @param summary  摘要
     * @param begin    交易日期,开始时间格式:YYYYMMDD
     * @param end      交易日期,开始时间格式:YYYYMMDD
     * @param FSE      金额
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("")
    @GET
    public Response getAccChangeNotices(final @QueryParam("ZHHM") String zhhm,
                                        final @QueryParam("SFZZ") String sfzz,
                                        final @QueryParam("SUMMARY") String summary,
                                        final @QueryParam("BEGIN") String begin,
                                        final @QueryParam("END") String end,
                                        final @QueryParam("FSE") String FSE,
                                        final @QueryParam("DSZH") String DSZH,
                                        final @QueryParam("DSHM") String DSHM,
                                        final @QueryParam("pageNo") int pageNo,
                                        final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取账号变动通知列表");
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.getAccChangeNotices(zhhm, sfzz, summary, begin, end, FSE, DSZH, DSHM, pageNo, pageSize));
    }

    /**
     * 获取账号变动通知列表
     *
     * @param zhhm    专户号码
     * @param sfzz    是否已做账: 0:是,1:否,null:自动对账业务
     * @param summary 摘要
     * @param begin   交易日期,开始时间格式:YYYYMMDD
     * @param end     交易日期,开始时间格式:YYYYMMDD
     * @return
     */
    @Path("/new/notice")
    @GET
    public Response getAccChangeNotices(final @QueryParam("ZHHM") String zhhm,
                                        final @QueryParam("SFZZ") String sfzz,
                                        final @QueryParam("SUMMARY") String summary,
                                        final @QueryParam("BEGIN") String begin,
                                        final @QueryParam("END") String end,
                                        final @QueryParam("FSE") String FSE,
                                        final @QueryParam("DSZH") String DSZH,
                                        final @QueryParam("DSHM") String DSHM,
                                        final @QueryParam("marker") String marker,
                                        final @QueryParam("action") String action,
                                        final @QueryParam("pageSize") String pageSize) {
        System.out.println("获取账号变动通知列表");
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.getAccChangeNotices(zhhm, sfzz, summary, begin, end, FSE, DSZH, DSHM, marker, action, pageSize));
    }

    @Path("/bydwzh/{dwzh}")
    @GET
    public Response getAccChangeNotices(final @PathParam("dwzh") String DWZH,
                                        final @QueryParam("ZJLY") String ZJLY,
                                        final @QueryParam("FSE") String FSE,
                                        final @QueryParam("JZPZH") String JZPZH,
                                        final @QueryParam("KSSJ") String begin,
                                        final @QueryParam("JSSJ") String end,
                                        final @QueryParam("pageNo") int pageNo,
                                        final @QueryParam("pageSize") int pageSize) {
        System.out.println("通过单位账号获取账号变动通知列表");
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.getAccChangeNoticesByDWZH(DWZH, ZJLY, FSE, JZPZH, begin, end, pageNo, pageSize));
    }

    @GET
    @Path("/{id}")
    public Response getAccChangeNotice(final @PathParam("id") String id) {
        System.out.println("获取账号变动通知");
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.getAccChangeNotice(id));
    }

    @PUT
    @Path("/{id}")
    public Response putAccChangeNotice(final @Context HttpRequest request, final @PathParam("id") String id, final RequestWrapper<AccChangeNoticeSMWJ> file) {
        System.out.println("修改账号变动通知");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.putAccChangeNotice(tokenContext, id, file.getReq()));
    }

    @GET
    @Path("/book/compareBooks")
    public Response getCompareBooks(final @QueryParam("NODE") String node,
                                    final @QueryParam("KHBH") String khbh,
                                    final @QueryParam("YHZH") String yhzh,
                                    final @QueryParam("BEGIN") String begin,
                                    final @QueryParam("END") String end) {
        System.out.println("获取对账单");
        return ResUtils.wrapEntityIfNeeded(iAccChangeNoticeAPI.getCompareBooks(node, khbh, yhzh, begin, end));
    }
}
