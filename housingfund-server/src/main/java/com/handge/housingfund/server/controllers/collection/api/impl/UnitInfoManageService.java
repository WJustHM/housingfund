package com.handge.housingfund.server.controllers.collection.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.UnitInfoManageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;


@SuppressWarnings("Duplicates")
@Component
public class UnitInfoManageService implements UnitInfoManageApi<Response> {

    @Autowired
    private UnitAcctSet unitAcctSet;

    @Autowired
    private UnitAcctAlter unitAcctAlter;

    @Autowired
    private UnitAcctSeal unitAcctSeal;

    @Autowired
    private UnitAcctUnseal unitAcctUnseal;

    @Autowired
    private UnitAcctDrop unitAcctDrop;

    @Autowired
    private UnitAcctInfo unitAcctInfo;

    @Autowired
    private UnitAcctCommon unitAcctCommon;

    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsSealedInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String page, final String pageSize) {

        System.out.println("单位封存列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSeal.showUnitAcctsSealed(tokenContext,DWZH, DWMC, DWLB, ZhuangTai,KSSJ,JSSJ, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsSealedInfoNew(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String marker, final String pageSize,final String action) {

        System.out.println("单位封存列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSeal.showUnitAcctsSealedNew(tokenContext,DWZH, DWMC, DWLB, ZhuangTai,KSSJ,JSSJ, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账号启封修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitAcctUnsealed(TokenContext tokenContext, final String YWLSH, final UnitAcctUnsealedPut body) {

        System.out.println("单位账号启封修改");

        if (YWLSH == null || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctUnseal.reUnitAcctUnsealed(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账号启封详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctUnsealed(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位账号启封详情");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctUnseal.getUnitAcctUnsealed(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsAlterInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,
                                          String page, String pageSize,String KSSJ,String JSSJ) {

        System.out.println("单位变更列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctAlter.showUnitAccountsAlter(tokenContext, DWZH, DWMC, DWLB, ZhuangTai, page, pageSize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsAlterInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,
                                          String marker,String action, String pageSize,String KSSJ,String JSSJ) {

        System.out.println("单位变更列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctAlter.showUnitAccountsAlter(tokenContext, DWZH, DWMC, DWLB, ZhuangTai, marker,action, pageSize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsUnsealedInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai, final String page, final String pageSize,String KSSJ,String JSSJ) {

        System.out.println("单位启封列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctUnseal.showUnitAcctsUnsealed(tokenContext,DWZH, DWMC, DWLB, ZhuangTai, KSSJ, JSSJ, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsUnsealedInfoNew(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai, final String marker, final String pageSize, String listAction, String KSSJ, String JSSJ) {

        System.out.println("单位启封列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctUnseal.showUnitAcctsUnsealedNew(tokenContext,DWZH, DWMC, DWLB, ZhuangTai, KSSJ, JSSJ, marker, pageSize,listAction)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    public Response getUnitAcctsInfo(TokenContext tokenContext, final String SFXH ,final String DWZH, final String DWMC, final String DWLB, final String DWZHZT,final String YWWD,
                                     String startTime, String endTime, String page, String pageSize) {

        System.out.println("查询单位账户信息列表");

        if (DWZHZT == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctInfo.getUnitAcctsInfo(tokenContext, SFXH,DWMC, DWZH, DWLB, DWZHZT, YWWD,startTime, endTime, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }



    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    public Response getUnitAcctsInfo(TokenContext tokenContext, final String SFXH ,final String DWZH, final String DWMC, final String DWLB, final String DWZHZT,final String YWWD,
                                     String startTime, String endTime, String marker,String action, String pageSize) {

        System.out.println("查询单位账户信息列表");

        if (DWZHZT == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctInfo.getUnitAcctsInfo(tokenContext, SFXH,DWMC, DWZH, DWLB, DWZHZT, YWWD,startTime, endTime, marker,action, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    public Response getEmployeeList(TokenContext tokenContext, String DWZH, String XingMing, String page, String pagesize) {

        System.out.println("查询单位员工列表");


        try {

            return Response.status(200).entity(this.unitAcctInfo.getEmployeeList(tokenContext,DWZH,XingMing,page,pagesize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }
    /**
     * 单位账号封存修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitAcctSealed(TokenContext tokenContext, final String YWLSH, final UnitAcctSealedPut body) {

        System.out.println("单位账号封存修改");

        if (YWLSH == null || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSeal.reUnitAcctSealed(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账号封存详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctSealed(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位账号封存详情");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSeal.getUnitAcctSealed(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位开户信息登记和变更回执单
     *
     * @param YWLSH 业务流水号
     * @param YWLX  业务类型（）
     **/
    public Response headUnitBasicReceipt(TokenContext tokenContext, final String YWLSH, final String YWLX) {

        System.out.println("单位开户信息登记和变更回执单");

        if (YWLSH == null || !Arrays.asList("03", "70").contains(YWLX)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Data_NOT_MATCH, "业务类型");
        }

        try {

            if (YWLX.equals("03")) {

                return Response.status(200).entity(this.unitAcctSet.headUnitAcctsSetReceipt(tokenContext, YWLSH)).build();
            }

            if (YWLX.equals("70")) {

                return Response.status(200).entity(this.unitAcctAlter.headUnitAcctsAlterReceipt(tokenContext, YWLSH)).build();
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

        return null;
    }


    /**
     * 单位开户修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitAcctSet(TokenContext tokenContext, final String YWLSH, final UnitAcctSetPut body) {

        System.out.println("单位开户修改");

        if (YWLSH == null || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSet.reUnitAccountSet(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位开户详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctSet(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位开户详情");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSet.getUnitAccountSet(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String ZhuangTai, String page,
                                        String pageSize,String KSSJ,String JSSJ) {

        System.out.println("单位开户列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSet.showUnitAccountsSet(tokenContext, DWMC, DWLB, ZhuangTai, page, pageSize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String ZhuangTai, String marker,String action,
                                        String pageSize,String KSSJ,String JSSJ) {

        System.out.println("单位开户列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSet.showUnitAccountsSet(tokenContext, DWMC, DWLB, ZhuangTai, marker,action, pageSize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }
    /**
     * 新建单位账号启封
     **/
    public Response postUnitAcctUnsealed(TokenContext tokenContext, final UnitAcctUnsealedPost body) {

        System.out.println("新建单位账号启封");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctUnseal.addUnitAcctUnsealed(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }




    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsDropInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String page, final String pageSize) {

        System.out.println("单位销户列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctDrop.showUnitAcctDrop(tokenContext,DWZH, DWMC, DWLB, ZhuangTai, KSSJ, JSSJ, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public Response getUnitAcctsDropInfoNew(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String marker, final String pageSize,String action) {

        System.out.println("单位销户列表");

        if (ZhuangTai == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctDrop.showUnitAcctDropNew(tokenContext,DWZH, DWMC, DWLB, ZhuangTai, KSSJ, JSSJ, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账户变更修改
     *
     * @param YWLSH
     **/
    public Response putUnitAcctAlter(TokenContext tokenContext, final String YWLSH, final UnitAcctAlterPut body) {

        System.out.println("单位账户变更修改");

        if (YWLSH == null || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctAlter.reUnitAccountAlter(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账户变更详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctAlter(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位账户变更详情");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctAlter.getUnitAccountAlter(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建单位账号封存
     **/
    public Response postUnitAcctSealed(TokenContext tokenContext, final UnitAcctSealedPost body) {

        System.out.println("新建单位账号封存");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSeal.addUnitAcctSealed(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 打印单位开户信息销户、启封、封存回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitUnsealed(TokenContext tokenContext, final String YWLSH, final String YWLX) {

        System.out.println("打印单位开户信息销户、启封、封存回执单");

        if (YWLSH == null || !Arrays.asList(
                CollectionBusinessType.销户.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.封存.getCode()).contains(YWLX)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if (YWLX.equals(CollectionBusinessType.销户.getCode())) {
            try {

                return Response.status(200).entity(this.unitAcctDrop.headUnitAcctsDropReceipt(YWLSH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();

            }
        }
        if (YWLX.equals(CollectionBusinessType.封存.getCode())) {
            try {

                return Response.status(200).entity(this.unitAcctSeal.headUnitAcctsSealedReceipt(tokenContext,YWLSH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();

            }
        }
        if (YWLX.equals(CollectionBusinessType.启封.getCode())) {
            try {

                return Response.status(200).entity(this.unitAcctUnseal.headUnitAcctsUnsealedReceipt(tokenContext,YWLSH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();

            }
        }
        return null;
    }


    /**
     * 获取单位账号（销户、启封、封存）申请关键信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctKeyword(TokenContext tokenContext, final String YWLSH) {

        System.out.println("获取单位账号（销户、启封、封存）申请关键信息");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctCommon.getUnitAcctActionAuto(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }


    /**
     * 新建单位开户登记
     **/
    public Response postUnitAcctSet(TokenContext tokenContext, final UnitAcctSetPost body) {

        System.out.println("新建单位开户登记");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctSet.addUnitAccountSet(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账号销户修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitAcctDrop(TokenContext tokenContext, final String YWLSH, final UnitAcctDropPut body) {

        System.out.println("单位账号销户修改");

        if (YWLSH == null || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctDrop.reUnitAcctDrop(tokenContext, body, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位账号销户详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitAcctDrop(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位账号销户详情");

        if (YWLSH == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctDrop.getUnitAcctDrop(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建单位账户变更
     **/
    public Response postUnitAcctAlter(TokenContext tokenContext, final UnitAcctAlterPost body) {

        System.out.println("新建单位账户变更");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctAlter.addUnitAccountAlter(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建单位账号销户
     **/
    public Response postUnitAcctDrop(TokenContext tokenContext, final UnitAcctDropPost body) {

        System.out.println("新建单位账号销户");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitAcctDrop.addUnitAcctDrop(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 根据单位账号, 获取单位开户信息
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitAcctDetailInfo(TokenContext tokenContext, final String DWZH) {

        System.out.println("根据单位账号, 获取单位开户信息");

//        return Response.status(200).entity(new GetUnitAcctInfoDetail() {{
//
//
//        }}).build();
        try {

            return Response.status(200).entity(this.unitAcctCommon.getUnitInfoAuto(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 新建单位开户提交
     **/
    public Response postUnitAcctSetSubmit(TokenContext tokenContext, final PostUnitAcctSetSubmit body) {

        System.out.println("新建单位开户提交");

        try {

            return Response.status(200).entity(this.unitAcctSet.submitUnitAccountSet(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }

    /**
     * 新建单位时名称验证
     **/
    public Response getUnitNameCheckMessage(String dwmc) {

        System.out.println("新建单位时名称验证");

        try {

            return Response.status(200).entity(this.unitAcctSet.getUnitNameCheckMessage(dwmc)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }



    /**
     * 单位变更提交
     **/
    public Response postUnitAcctAlterSubmit(TokenContext tokenContext, final PostUnitAcctAlterSubmit body) {

        System.out.println("单位变更提交");

        try {

            return Response.status(200).entity(this.unitAcctAlter.submitUnitAccountAlter(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 新建单位账号销户提交
     **/
    public Response postUnitAcctDropSubmit(TokenContext tokenContext, final PostUnitAcctDropSubmit body) {

        System.out.println("新建单位账号销户提交");

        try {

            return Response.status(200).entity(this.unitAcctDrop.submitUnitAcctDrop(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位账号启封提交
     **/
    public Response postUnitAcctUnsealedSubmit(TokenContext tokenContext, final PostUnitAcctUnsealedSubmit body) {

        System.out.println("单位账号启封提交");

        try {

            return Response.status(200).entity(this.unitAcctUnseal.submitUnitAcctUnseal(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位账号封存提交
     **/
    public Response postUnitAcctSealedSubmit(TokenContext tokenContext, final PostUnitAcctSealedSubmit body) {

        System.out.println("单位账号封存提交");

        try {

            return Response.status(200).entity(this.unitAcctSeal.submitUnitAcctSeal(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

}