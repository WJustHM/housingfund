package com.handge.housingfund.server.controllers.review.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.enumeration.BusinessDetailsModule;
import com.handge.housingfund.common.service.review.IBaseReview;
import com.handge.housingfund.common.service.review.model.BatchReviewCheck;
import com.handge.housingfund.common.service.review.model.BatchReviewInfo;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.review.api.IReviewAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Liujuhao on 2017/9/25.
 */

@Component
public class ReviewApi implements IReviewAPI<Response> {

    @Autowired
    @Qualifier(value = "review.collection")
    private IBaseReview collection;

    @Autowired
    @Qualifier(value = "review.loanl")
    private IBaseReview loan;

    @Autowired
    @Qualifier(value = "review.finance")
    private IBaseReview finance;


    @Override
    public Response getReviewList(TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String pageNo, String module, String type,
                                  String DWMC, String XingMing, String CZY, String GRZH, String TQYY, String ZJHM, String ZRZXMC, String ZCZXMC, String DWZH,
                                  String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
                                  String KHYHMC, String ZHHM, String YWMC) {

        HashMap conditions = new HashMap<String, String>() {
            {
                this.put("YWLX", YWLX);
                this.put("KSSJ", KSSJ);
                this.put("JSSJ", JSSJ);
                this.put("pageSize", pageSize);
                this.put("pageNo", pageNo);
                this.put("DWMC", DWMC);
                this.put("XingMing", XingMing);
                this.put("CZY", CZY);
                this.put("GRZH", GRZH);
                this.put("TQYY", TQYY);
                this.put("ZJHM", ZJHM);
                this.put("ZRZXMC", ZRZXMC);
                this.put("ZCZXMC", ZCZXMC);
                this.put("DWZH", DWZH);
                this.put("JKRXM", JKRXM);
                this.put("FKGS", FKGS);
                this.put("LPMC", LPMC);
                this.put("HKLX", HKLX);
                this.put("JKHTBH", JKHTBH);
                this.put("KHYHMC", KHYHMC);
                this.put("ZHHM", ZHHM);
                this.put("YWMC", YWMC);
            }
        };

        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.getReviewInfo(tokenContext, conditions, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.getReviewInfo(tokenContext, conditions, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.getReviewInfo(tokenContext, conditions, module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getReviewedList(TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String pageNo, String module, String type, String ZhuangTai,
                                    String DWMC, String XingMing, String CZY, String GRZH, String TQYY, String ZJHM, String ZRZXMC, String ZCZXMC, String DWZH,
                                    String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
                                    String KHYHMC, String ZHHM, String YWMC) {

        HashMap conditions = new HashMap<String, String>() {
            {
                this.put("YWLX", YWLX);
                this.put("KSSJ", KSSJ);
                this.put("JSSJ", JSSJ);
                this.put("pageSize", pageSize);
                this.put("pageNo", pageNo);
                this.put("ZhuangTai", ZhuangTai);
                this.put("DWMC", DWMC);
                this.put("XingMing", XingMing);
                this.put("CZY", CZY);
                this.put("GRZH", GRZH);
                this.put("TQYY", TQYY);
                this.put("ZJHM", ZJHM);
                this.put("ZRZXMC", ZRZXMC);
                this.put("ZCZXMC", ZCZXMC);
                this.put("DWZH", DWZH);
                this.put("JKRXM", JKRXM);
                this.put("FKGS", FKGS);
                this.put("LPMC", LPMC);
                this.put("HKLX", HKLX);
                this.put("JKHTBH", JKHTBH);
                this.put("KHYHMC", KHYHMC);
                this.put("ZHHM", ZHHM);
                this.put("YWMC", YWMC);
            }
        };
        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.getReviewedInfo(tokenContext, conditions, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.getReviewedInfo(tokenContext, conditions, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.getReviewedInfo(tokenContext, conditions, module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response postBusinessReview(TokenContext tokenContext, BatchReviewInfo batchReviewInfo, String module, String type) {

        if (batchReviewInfo.getIds() == null || batchReviewInfo.getAuditInfo() == null || batchReviewInfo.getAuditInfo().getEvent() == null) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号或审核信息");
        }

        if (StringUtil.isEmpty(batchReviewInfo.getAuditInfo().getYYYJ())) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "原因(意见)不能为空");
        }

        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.postBusinessReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(),
                        batchReviewInfo.getAuditInfo().getEvent().equals("01"), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.postBusinessReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(),
                        batchReviewInfo.getAuditInfo().getEvent().equals("01"), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.postBusinessReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(),
                        batchReviewInfo.getAuditInfo().getEvent().equals("01"), module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response checkIsReviewing(TokenContext tokenContext, BatchReviewCheck batchReviewCheck, String module, String type) {

        if (batchReviewCheck.getIds() == null) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }
        if (batchReviewCheck.getShybh() == null) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "审核员编号");
        }

        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.checkIsReviewing(tokenContext, batchReviewCheck.getIds(), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.checkIsReviewing(tokenContext, batchReviewCheck.getIds(), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.checkIsReviewing(tokenContext, batchReviewCheck.getIds(), module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }

    @Override
    public Response postSpecialReview(TokenContext tokenContext, BatchReviewInfo batchReviewInfo, String module, String type) {

        System.out.println("特审操作");

        if (batchReviewInfo.getIds() == null || batchReviewInfo.getAuditInfo() == null) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号或审核信息");
        }

        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.postSpecialReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.postSpecialReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(), module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.postSpecialReview(tokenContext, batchReviewInfo.getIds(), batchReviewInfo.getAuditInfo(), module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getReviewList(TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String marker, String action, String module, String type,
                                  String DWMC, String XingMing, String CZY, String GRZH, String TQYY,
                                  String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
                                  String KHYHMC, String ZHHM, String YWMC) {

        if (!Arrays.asList("1", "2", "3", "4").contains(action)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "非法的列表操作");
        }

        HashMap conditions = new HashMap<String, String>() {
            {
                this.put("YWLX", YWLX);
                this.put("KSSJ", KSSJ);
                this.put("JSSJ", JSSJ);
                this.put("pageSize", pageSize);
                this.put("marker", marker);
                this.put("DWMC", DWMC);
                this.put("XingMing", XingMing);
                this.put("CZY", CZY);
                this.put("GRZH", GRZH);
                this.put("TQYY", TQYY);
                this.put("JKRXM", JKRXM);
                this.put("FKGS", FKGS);
                this.put("LPMC", LPMC);
                this.put("HKLX", HKLX);
                this.put("JKHTBH", JKHTBH);
                this.put("KHYHMC", KHYHMC);
                this.put("ZHHM", ZHHM);
                this.put("YWMC", YWMC);
            }
        };

        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.getReviewInfo(tokenContext, conditions, action, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.getReviewInfo(tokenContext, conditions, action, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.getReviewInfo(tokenContext, conditions, action, module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getReviewedList(TokenContext tokenContext, String YWLX, String KSSJ, String JSSJ, String pageSize, String marker, String action, String module, String type, String ZhuangTai,
                                    String DWMC, String XingMing, String CZY, String GRZH, String TQYY,
                                    String JKRXM, String FKGS, String LPMC, String HKLX, String JKHTBH,
                                    String KHYHMC, String ZHHM, String YWMC) {

        if (!Arrays.asList("1", "2", "3", "4").contains(action)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "非法的列表操作");
        }

        HashMap conditions = new HashMap<String, String>() {
            {
                this.put("YWLX", YWLX);
                this.put("KSSJ", KSSJ);
                this.put("JSSJ", JSSJ);
                this.put("pageSize", pageSize);
                this.put("marker", marker);
                this.put("ZhuangTai", ZhuangTai);
                this.put("DWMC", DWMC);
                this.put("XingMing", XingMing);
                this.put("CZY", CZY);
                this.put("GRZH", GRZH);
                this.put("TQYY", TQYY);
                this.put("JKRXM", JKRXM);
                this.put("FKGS", FKGS);
                this.put("LPMC", LPMC);
                this.put("HKLX", HKLX);
                this.put("JKHTBH", JKHTBH);
                this.put("KHYHMC", KHYHMC);
                this.put("ZHHM", ZHHM);
                this.put("YWMC", YWMC);
            }
        };
        try {

            if (Arrays.asList(
                    BusinessDetailsModule.Collection_person.getCode(),
                    BusinessDetailsModule.Collection_unit.getCode()).contains(module)) {

                return Response.status(200).entity(this.collection.getReviewedInfo(tokenContext, conditions, action, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Loan_person.getCode(),
                    BusinessDetailsModule.Loan_project.getCode()).contains(module)) {

                return Response.status(200).entity(this.loan.getReviewedInfo(tokenContext, conditions, action, module, type)).build();

            } else if (Arrays.asList(
                    BusinessDetailsModule.Finance_common.getCode(),
                    BusinessDetailsModule.Finance_currentToPeriodic.getCode(),
                    BusinessDetailsModule.Finance_periodicWithdraw.getCode()).contains(module)) {

                return Response.status(200).entity(this.finance.getReviewedInfo(tokenContext, conditions, action, module, type)).build();

            } else {

                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "模块编号");
            }

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
}
