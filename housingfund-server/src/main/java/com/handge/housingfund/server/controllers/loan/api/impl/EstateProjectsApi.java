package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.EstateProjectRecords;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectsApi;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class EstateProjectsApi implements IEstateProjectsApi<Response> {


    /**
     * 停用/启用房开公司
     *
     * @param LPBH  楼盘编号
     * @param state (1：启用，2：停用)
     **/
    public Response putEstateProjectsstate(final String LPBH, final String state) {

        System.out.println("停用/启用房开公司");
        if (!StringUtil.notEmpty(LPBH) || !StringUtil.notEmpty(state)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(state)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 删除楼盘项目信息
     *
     * @param TYPE 1：申请受理 ,2:变更受理
     **/
    public Response deleteEstateId(final String TYPE, final DelList body) {

        System.out.println("删除楼盘项目信息");
        if (!StringUtil.notEmpty(TYPE) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(TYPE)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 受理记录
     *
     * @param TYPE      1：申请受理 ,2:变更受理
     * @param LPMC      楼盘名称
     * @param ZhuangTai 状态(00所有，01新建，02待审核，03审核不通过，04办结)
     **/
    public Response getEstatRecords(final String TYPE, final String LPMC, final String ZhuangTai) {

        System.out.println("受理记录");
        if (!StringUtil.notEmpty(TYPE) || !StringUtil.notEmpty(ZhuangTai)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(TYPE) || !Arrays.asList("00", "01", "02", "03", "04").contains(TYPE)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new EstateProjectRecords() {{


        }}).build();
    }


}