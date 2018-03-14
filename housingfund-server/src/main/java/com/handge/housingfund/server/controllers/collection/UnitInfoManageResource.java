package com.handge.housingfund.server.controllers.collection;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.UnitInfoManageApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/collection")
public class UnitInfoManageResource {

    @Autowired
    private UnitInfoManageApi<Response> service;

    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsSealedInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsSealedInfo(@Context HttpRequest httpRequest, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("单位封存列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsSealedInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai,KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsSealedInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsSealedInfoNew(@Context HttpRequest httpRequest, final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("单位封存列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsSealedInfoNew((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai,KSSJ,JSSJ, marker, pageSize,action));
    }


    /**
     * 单位账号启封修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctUnsealed/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitAcctUnsealed(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitAcctUnsealedPut> unitacctunsealedput) {

        System.out.println("单位账号启封修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitAcctUnsealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, unitacctunsealedput == null ? null : unitacctunsealedput.getReq()));
    }


    /**
     * 单位账号启封详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctUnsealed/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctUnsealed(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("单位账号启封详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctUnsealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsAlterInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsAlterInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB,
                                          final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai,
                                          final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位变更列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsAlterInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, pageNo, pageSize,KSSJ,JSSJ));
    }

    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsAlterInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsAlterInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB,
                                          final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai,
                                          final @QueryParam("marker")String marker,final @QueryParam("action") String action, final @QueryParam("pageSize") String pageSize,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位变更列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsAlterInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, marker,action, pageSize,KSSJ,JSSJ));
    }


    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsUnsealedInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsUnsealedInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位启封列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsUnsealedInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, pageNo, pageSize,KSSJ,JSSJ));
    }
    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsUnsealedInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsUnsealedInfoNew(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("marker") String pageNo, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位启封列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsUnsealedInfoNew((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, pageNo, pageSize,action,KSSJ,JSSJ));
    }



    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    @Path("/unitAcctsInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsInfo(@Context HttpRequest httpRequest,
                                     final @QueryParam("SFXH") String SFXH,final @QueryParam("DWZH") String DWZH, final @QueryParam("DWMC") String DWMC,
                                     final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZHZT") String DWZHZT,
                                     final @QueryParam("YWWD") String YWWD,
                                     final @QueryParam("START") String startTime, final @QueryParam("END") String endTime,
                                     final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize,final @QueryParam("timestamp") String timestamp) {

        System.out.println("查询单位账户信息列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),SFXH,DWZH, DWMC, DWLB, DWZHZT,YWWD,startTime,endTime, pageNo, pageSize));
    }

    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    @Path("/unitAcctsInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsInfo(@Context HttpRequest httpRequest,
                                     final @QueryParam("SFXH") String SFXH,final @QueryParam("DWZH") String DWZH, final @QueryParam("DWMC") String DWMC,
                                     final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZHZT") String DWZHZT,
                                     final @QueryParam("YWWD") String YWWD,
                                     final @QueryParam("START") String startTime, final @QueryParam("END") String endTime,
                                     final @QueryParam("marker")String marker,final @QueryParam("action") String action, final @QueryParam("pageSize") String pageSize,final @QueryParam("timestamp") String timestamp) {

        System.out.println("查询单位账户信息列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),SFXH,DWZH, DWMC, DWLB, DWZHZT,YWWD,startTime,endTime, marker,action, pageSize));
    }

    @Path("/unitAccts/employee")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEmployeeList(@Context HttpRequest httpRequest, @QueryParam("DWZH") String DWZH, @QueryParam("XingMing") String XingMing, @QueryParam("page") String page, @QueryParam("pageSize") String pagesize){
        System.out.println("查询单位员工列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getEmployeeList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWZH,XingMing,page,pagesize));
    }
    /**
     * 单位账号封存修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctSealed/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitAcctSealed(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitAcctSealedPut> unitacctsealedput) {

        System.out.println("单位账号封存修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitAcctSealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, unitacctsealedput == null ? null : unitacctsealedput.getReq()));
    }


    /**
     * 单位账号封存详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctSealed/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctSealed(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("单位账号封存详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctSealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 单位开户信息登记和变更回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitBasic/receipt")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitBasicReceipt(@Context HttpRequest httpRequest,final @QueryParam("YWLSH") String YWLSH, final @QueryParam("YWLX") String YWLX) {

        System.out.println("单位开户信息登记和变更回执单");


        return ResUtils.wrapEntityIfNeeded(this.service.headUnitBasicReceipt((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, YWLX));


    }


    /**
     * 单位开户修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctSet/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitAcctSet(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitAcctSetPut> unitacctsetput) {

        System.out.println("单位开户修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, unitacctsetput == null ? null : unitacctsetput.getReq()));
    }


    /**
     * 单位开户详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctSet/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctSet(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("单位开户详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsSetInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsSetInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB,
                                        final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("pageNo") String pageNo,
                                        final @QueryParam("pageSize") String pageSize,
                                        final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位开户列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsSetInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, ZhuangTai, pageNo, pageSize,KSSJ,JSSJ));
    }
    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsSetInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsSetInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB,
                                        final @QueryParam("ZhuangTai") String ZhuangTai, final @QueryParam("marker")String marker,final @QueryParam("action") String action,
                                        final @QueryParam("pageSize") String pageSize,
                                        final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

        System.out.println("单位开户列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsSetInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, ZhuangTai, marker,action, pageSize,KSSJ,JSSJ));
    }


    /**
     * 新建单位账号启封
     **/
    @Path("/unitAcctUnsealed")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctUnsealed(@Context HttpRequest httpRequest,final RequestWrapper<UnitAcctUnsealedPost> unitacctunsealedpost) {

        System.out.println("新建单位账号启封");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctUnsealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),unitacctunsealedpost == null ? null : unitacctunsealedpost.getReq()));
    }


    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsDropInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsDropInfo(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("单位销户列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsDropInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    @Path("/unitAcctsDropInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctsDropInfoNew(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("DWLB") String DWLB, final @QueryParam("DWZH") String DWZH, final @QueryParam("ZhuangTai") String ZhuangTai,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("单位销户列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctsDropInfoNew((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, DWLB, DWZH, ZhuangTai, KSSJ,JSSJ, marker, pageSize,action));
    }

    /**
     * 单位账户变更修改
     *
     * @param YWLSH
     **/
    @Path("/unitAcctAlter/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitAcctAlter(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitAcctAlterPut> unitacctalterput) {

        System.out.println("单位账户变更修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, unitacctalterput == null ? null : unitacctalterput.getReq()));
    }


    /**
     * 单位账户变更详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctAlter/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctAlter(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("单位账户变更详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 新建单位账号封存
     **/
    @Path("/unitAcctSealed")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctSealed(@Context HttpRequest httpRequest,final RequestWrapper<UnitAcctSealedPost> unitacctsealedpost) {

        System.out.println("新建单位账号封存");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctSealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),unitacctsealedpost == null ? null : unitacctsealedpost.getReq()));
    }


    /**
     * 打印单位开户信息销户、启封、封存回执单
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctAction/receipt/{YWLSH}/{YWLX}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response headUnitUnsealed(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final @PathParam("YWLX") String YWLX) {

        System.out.println("打印单位开户信息销户、启封、封存回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.headUnitUnsealed((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, YWLX));
    }


    /**
     * 获取单位账号（销户、启封、封存）申请关键信息
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctActionAuto/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctKeyword(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("获取单位账号（销户、启封、封存）申请关键信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctKeyword((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 新建单位开户登记
     **/
    @Path("/unitAcctSet")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctSet(@Context HttpRequest httpRequest,final RequestWrapper<UnitAcctSetPost> unitacctsetpost) {

        System.out.println("新建单位开户登记");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),unitacctsetpost == null ? null : unitacctsetpost.getReq()));
    }


    /**
     * 单位账号销户修改
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctDrop/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUnitAcctDrop(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final RequestWrapper<UnitAcctDropPut> unitacctdropput) {

        System.out.println("单位账号销户修改");

        return ResUtils.wrapEntityIfNeeded(this.service.putUnitAcctDrop((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, unitacctdropput == null ? null : unitacctdropput.getReq()));
    }


    /**
     * 单位账号销户详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/unitAcctDrop/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctDrop(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

        System.out.println("单位账号销户详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctDrop((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
    }


    /**
     * 新建单位账户变更
     **/
    @Path("/unitAcctAlter")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctAlter(@Context HttpRequest httpRequest,final RequestWrapper<UnitAcctAlterPost> unitacctalterpost) {

        System.out.println("新建单位账户变更");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),unitacctalterpost == null ? null : unitacctalterpost.getReq()));
    }


    /**
     * 新建单位账号销户
     **/
    @Path("/unitAcctDrop")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctDrop(@Context HttpRequest httpRequest,final RequestWrapper<UnitAcctDropPost> unitacctdroppost) {

        System.out.println("新建单位账号销户");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctDrop((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),unitacctdroppost == null ? null : unitacctdroppost.getReq()));
    }



    /**
     * 根据单位账号, 获取单位开户信息
     *
     * @param DWZH 单位账号
     **/
    @Path("/unitAcctsInfo/{DWZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitAcctDetailInfo(@Context HttpRequest httpRequest,final @PathParam("DWZH") String DWZH) {

        System.out.println("根据单位账号, 获取单位开户信息");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitAcctDetailInfo((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWZH));
    }


    /**
     * 新建单位开户提交
     **/
    @Path("/unitAcctSetSubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctSetSubmit(@Context HttpRequest httpRequest,final RequestWrapper<PostUnitAcctSetSubmit> postunitacctsetsubmit) {

        System.out.println("新建单位开户提交");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctSetSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),postunitacctsetsubmit == null ? null : postunitacctsetsubmit.getReq()));
    }

    /**
     * 新建单位时名称验证
     **/
    @Path("/unitNameCheckMessage")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getUnitNameCheckMessage(@QueryParam("DWMC") String DWMC){
        System.out.println("单位开户名称验证");

        return ResUtils.wrapEntityIfNeeded(this.service.getUnitNameCheckMessage(DWMC));

    }


    /**
     * 单位变更提交
     **/
    @Path("/unitAcctAlterSubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctAlterSubmit(@Context HttpRequest httpRequest,final RequestWrapper<PostUnitAcctAlterSubmit> postunitacctaltersubmit) {

        System.out.println("单位变更提交");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctAlterSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),postunitacctaltersubmit == null ? null : postunitacctaltersubmit.getReq()));
    }


    /**
     * 新建单位账号销户提交
     **/
    @Path("/unitAcctDropSubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctDropSubmit(@Context HttpRequest httpRequest,final RequestWrapper<PostUnitAcctDropSubmit> postunitacctdropsubmit) {

        System.out.println("新建单位账号销户提交");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctDropSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),postunitacctdropsubmit == null ? null : postunitacctdropsubmit.getReq()));
    }


    /**
     * 单位账号启封提交
     **/
    @Path("/unitAcctUnsealedSubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctUnsealedSubmit(@Context HttpRequest httpRequest,final RequestWrapper<PostUnitAcctUnsealedSubmit> postunitacctunsealedsubmit) {

        System.out.println("单位账号启封提交");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctUnsealedSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),postunitacctunsealedsubmit == null ? null : postunitacctunsealedsubmit.getReq()));
    }


    /**
     * 单位账号封存提交
     **/
    @Path("/unitAcctSealedSubmit")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUnitAcctSealedSubmit(@Context HttpRequest httpRequest,final RequestWrapper<PostUnitAcctSealedSubmit> postunitacctsealedsubmit) {

        System.out.println("单位账号封存提交");

        return ResUtils.wrapEntityIfNeeded(this.service.postUnitAcctSealedSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),postunitacctsealedsubmit == null ? null : postunitacctsealedsubmit.getReq()));
    }
}