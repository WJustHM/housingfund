package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;
import com.handge.housingfund.common.service.others.IReadExcelUtilsService;
import com.handge.housingfund.common.service.others.model.MonitorDataRes;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.RedisCache;
import com.handge.housingfund.server.controllers.ResUtils;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.JedisCluster;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by sjw on 2017/10/23.
 */
@Controller
@Path("/other")
public class ReadExcelUtilsController {
    @Autowired
    private IReadExcelUtilsService readExcelUtilsService;
    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    @GET
    @Path("/UnitAcctExcel/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getImportUnitAcctInfo(final @Context HttpRequest request,final @PathParam("id") String id) {
        System.out.println("导入单位开户数据");
        TokenContext  tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            ImportExcelRes res = this.readExcelUtilsService.getImportUnitAcctInfo(tokenContext, id);
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(res).build());

        } catch (ErrorException e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/IndiAcctExcel/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response getImportIndiAcctInfo(final @Context HttpRequest request,final @PathParam("id") String id) {
        System.out.println("导入个人开户数据");
        TokenContext  tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(this.readExcelUtilsService.getImportIndiAcctInfo(tokenContext,id)).build());
        } catch (ErrorException e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/IndiAcctExcelMonitor/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response IndiAcctExcelMonitor(final @Context HttpRequest request,final @PathParam("id") String id) {
        System.out.println("开户监控");
        TokenContext  tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            MonitorDataRes monitorDataRes = new MonitorDataRes();
            String TotalNum = "IndiAcct_TotalNum"+id;
            String SucNum = "IndiAcct_SucNum"+id;
            String Mes = "IndiAcct_Mes"+id;
            monitorDataRes.setTotal_num(redis.get(TotalNum));
            monitorDataRes.setSuc_num(redis.get(SucNum));
            monitorDataRes.setMes(redis.get(Mes));
            redis.close();
            try {
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(monitorDataRes).build());
            } catch (ErrorException e) {
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
            }
        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getMessage()).build());
        }
    }
    @GET
    @Path("/PersonRadixExcel/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response getImportPersonRadix(final @Context HttpRequest request,final @PathParam("id") String id) {
        System.out.println("导入个人基数调整数据");
        TokenContext  tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(this.readExcelUtilsService.getImportPersonRadix(tokenContext,id)).build());

        } catch (ErrorException e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }
    @GET
    @Path("/PersonRadixMonitor/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response PersonRadixMonitor(final @Context HttpRequest request,final @PathParam("id") String id) {
        System.out.println("个人基数调整监控");
        TokenContext  tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            MonitorDataRes monitorDataRes = new MonitorDataRes();
            String TotalNum = "PerRadix_TotalNum"+id;
            String SucNum = "PerRadix_SucNum"+id;
            String Mes = "PerRadix_Mes"+id;
            monitorDataRes.setTotal_num(redis.get(TotalNum));
            monitorDataRes.setSuc_num(redis.get(SucNum));
            monitorDataRes.setMes(redis.get(Mes));
            redis.close();
            try {
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(monitorDataRes).build());
            } catch (ErrorException e) {
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
            }
        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getMessage()).build());
        }
    }
}
