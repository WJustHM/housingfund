package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.loan.ILoanRecordService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanRecordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class LoanRecordApi implements ILoanRecordApi<Response> {

    // TODO: 2017/8/9 贷款记录--2个API（刘基豪），新添加3个

    @Autowired
    private ILoanRecordService service;

    /**
     * 贷款详情页面
     *
     * @param DKZH 贷款账号
     **/
    public Response getLoanRecordDetails(final String DKZH) {

        if (!StringUtil.notEmpty(DKZH)) {

            return Response.status(200).entity(new ErrorException(ReturnEnumeration.Parameter_MISS, "贷款账号")).build();

        }

        System.out.println("贷款详情页面");

        try {

            return Response.status(200).entity(this.service.getLoanDetails(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 贷款记录列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public Response getLoanRecordList(final String JKRXM, final String JKRZJHM, final String DKYT, final String pageSize, final String page, final String Module,
                                      final String YWWD, final String SWTYH, final String DKZH, final String HTJE) {

        System.out.println("贷款记录列表");

        if (StringUtil.notEmpty(DKYT) && !Arrays.asList("0", "1", "2", "3").contains(DKYT)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "贷款用途");

        }

        if (!StringUtil.notEmpty(pageSize)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "每页条数");

        }

        if (!StringUtil.notEmpty(page)) {

           return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码");

        }

        if (!Arrays.asList(ReviewSubModule.归集_提取.getCode(), ReviewSubModule.贷款_放款.getCode()).contains(Module)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "未知的业务模块编码");
        }

        try {

            return Response.status(200).entity(this.service.listLoanRecord(JKRXM, JKRZJHM, DKYT, pageSize, page, Module, YWWD, SWTYH, DKZH, HTJE)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getLoanRecordHistory(String DKZH, String pageSize, String pageNo) {

        System.out.println("查询，贷款业务的历史记录（审核流程）");

        if (!StringUtil.notEmpty(DKZH)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS,"贷款账号");
        }

        try {
            return Response.status(200).entity(this.service.getLoanRecordHistory(DKZH, pageSize, pageNo)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }


}