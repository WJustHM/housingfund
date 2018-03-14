package com.handge.housingfund.server.controllers.collection.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.service.common.CommonOps;
import com.handge.housingfund.common.service.enumeration.BusinessDetailsModule;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.CommonApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Liujuhao on 2017/8/29.
 */

@Service
public class CollectionCommonService implements CommonApi<Response> {

    @Autowired
    private CommonOps commonOps;

    /**
     * 删除，归集业务（目前只支持归集）
     *
     * @param YWLSHs 业务流水号集合
     * @param YWMK   业务模块(01个人，02单位)
     **/
    @Override
    public Response deleteOperation(TokenContext tokenContext, final List<String> YWLSHs, final String YWMK) {

        System.out.println("删除，归集业务");

        if (YWLSHs == null) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        if (!StringUtil.notEmpty(YWMK) || !Arrays.asList(BusinessDetailsModule.Collection_person.getCode(), BusinessDetailsModule.Collection_unit.getCode()).contains(YWMK)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "业务模块有误(01-个人、02-单位)");
        }

        try {
            return Response.status(200).entity(this.commonOps.deleteOperation(tokenContext, YWLSHs, YWMK)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 撤回，归集业务（目前只支持归集）
     *
     * @param YWLSH 业务流水号
     * @param YWMK  业务模块(01个人，02单位)
     **/
    @Override
    public Response revokeOperation(TokenContext tokenContext, final String YWLSH, final String YWMK) {

        System.out.println("撤回，归集业务");

        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        if (!StringUtil.notEmpty(YWMK) || !Arrays.asList(BusinessDetailsModule.Collection_person.getCode(), BusinessDetailsModule.Collection_unit.getCode()).contains(YWMK)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "业务模块有误(01-个人、02-单位)");
        }

        try {

            return Response.status(200).entity(this.commonOps.revokeOperation(tokenContext, YWLSH, YWMK)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getRecordHistory(String ZhangHao, String YWMK, String pageSize, String pageNo) {

        System.out.println("查询，个人/单位账户历史记录");

        if (!StringUtil.notEmpty(ZhangHao) || !StringUtil.notEmpty(YWMK)/* || !StringUtil.notEmpty(pageSize) || !StringUtil.notEmpty(pageNo)*/) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "账号或业务模块");
        }

        if (!Arrays.asList(BusinessDetailsModule.Collection_person.getCode(), BusinessDetailsModule.Collection_unit.getCode()).contains(YWMK)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "业务模块有误(01-个人、02-单位)");
        }

        try{

            return Response.status(200).entity(this.commonOps.recordHistory(ZhangHao, YWMK, pageSize, pageNo)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getReviewInfos(String YWLSH) {

        System.out.println("查询审核史记录");


        try{

            return Response.status(200).entity(this.commonOps.getReviewInfos(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response accountRecord(String ywlsh, String ywlx, String code) {
        try{
            return Response.status(200).entity(this.commonOps.accountRecord(ywlsh,ywlx,code)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

}
