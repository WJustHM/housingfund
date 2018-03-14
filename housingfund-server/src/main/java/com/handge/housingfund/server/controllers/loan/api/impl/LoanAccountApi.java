package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ILoanAccountService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanRiskStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.ILoanAccountApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.Arrays;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class LoanAccountApi implements ILoanAccountApi<Response> {

    // TODO: 2017/8/9 贷款账户查询--9个API（刘基豪）

    @Autowired
    private ILoanAccountService service;

    /**
     * 贷款账户查询
     *
     * @param DKZH   贷款账号
     * @param JKRXM  借款人姓名
     * @param status 状态（0:正常 1:逾期 2:呆账 3:转出 4:结清）
     **/
    public Response getHousingFundAccount(final String DKZH, final String JKRXM, final String status, final String DKFXDJ, final String pageSize, final String page,
                                          final String YWWD, final String SWTYH, final String ZJHM) {

        System.out.println("贷款账户查询");

        if (StringUtil.notEmpty(status) && !Arrays.asList(
                LoanAccountType.待签合同.getCode(),
                LoanAccountType.待放款.getCode(),
                LoanAccountType.正常.getCode(),
                LoanAccountType.已结清.getCode(),
                LoanAccountType.呆账.getCode(),
                LoanAccountType.待确认.getCode(),
                LoanAccountType.已作废.getCode(),
                LoanAccountType.逾期.getCode()).contains(status)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "贷款账户状态");
        }

        if (!StringUtil.notEmpty(pageSize) || !StringUtil.notEmpty(page)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码或每页条数");
        }

        if (StringUtil.notEmpty(DKFXDJ) && !Arrays.asList(
                LoanRiskStatus.所有.getCode(),
                LoanRiskStatus.正常.getCode(),
                LoanRiskStatus.关注.getCode(),
                LoanRiskStatus.其他.getCode(),
                LoanRiskStatus.可疑.getCode(),
                LoanRiskStatus.损失.getCode(),
                LoanRiskStatus.次级.getCode()).contains(DKFXDJ)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "贷款风险等级");
        }

        try {

            return Response.status(200).entity(this.service.getHousingFundAccount(DKZH, JKRXM, status, DKFXDJ, pageSize, page, YWWD, SWTYH, ZJHM)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 贷款账户业务清单
     *
     * @param DKZH 贷款账号
     **/
    public Response getHousingfundAccountBusinessList(final String DKZH, final String pageSize, final String page) {

        System.out.println("贷款账户业务清单");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        if (!StringUtil.notEmpty(pageSize) || !StringUtil.notEmpty(page)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码或每页条数");
        }

        try {

            return Response.status(200).entity(this.service.getHousingfundAccountBusinessList(DKZH, pageSize, page)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 贷款账户逾期信息
     *
     * @param DKZH 贷款账号
     **/
    public Response getHousingfundAccountOverdueList(final String DKZH, final String pageSize, final String page) {

        System.out.println("贷款账户逾期信息");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        if (!StringUtil.notEmpty(pageSize) || !StringUtil.notEmpty(page)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码或每页条数");
        }

        try {

            return Response.status(200).entity(this.service.getHousingfundAccountOverdueList(DKZH, pageSize, page)).build();

        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 提前还款申请书(待定)
     *
     * @param DKZH 贷款账号
     **/
    public Response postOverdueApply(final String DKZH) {

        System.out.println("提前还款通知书(待定)");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

//        return Response.status(200).entity(this.service).build();
        return null;
    }


    /**
     * 还款记录
     *
     * @param DKZH 贷款账号
     **/
    public Response getHousingRecordList(final String DKZH, final String pageSize, final String page) {

        System.out.println("还款记录");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {

            return Response.status(200).entity(this.service.getHousingRecordList(DKZH, pageSize, page)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 打印还款记录
     *
     * @param DKZH 贷款账号
     **/
    public Response getHousingRecordPintList(final String DKZH) {

        System.out.println("打印还款记录");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {
            return Response.status(200).entity(this.service.getHousingRecordPintList(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 还款计划
     *
     * @param DKZH 贷款账号
     **/
    public Response getHousingfundPlanList(final String DKZH) {

        System.out.println("还款计划");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {

            return Response.status(200).entity(this.service.getHousingfundPlanList(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 贷款账户详情
     *
     * @param DKZH 贷款账户
     **/
    public Response getHousingDkzh(final String DKZH) {

        System.out.println("贷款账户详情");

        if (!StringUtil.notEmpty(DKZH)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {
            return Response.status(200).entity(this.service.getHousingDkzh(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
//
//    @Override
//    public Response putbadDebts(Integer DKZH, String status) {
//        return null;
//    }

    /**
     * 单独修改状态（转为呆账）
     *
     * @param DKZH 贷款账号
     **/
    public Response putbadDebts(final String DKZH) {

        System.out.println("单独修改状态（转为呆账）");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {
            return Response.status(200).entity(this.service.putBadDebts(DKZH, null)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号
     *
     * @param DKZH
     * @return
     */
    @Override
    public Response getLoanAccountBorrowerInfo(String DKZH) {

        System.out.println("根据贷款账号，获借款人的姓名、证件类型、证件号码、贷款账号");

        if (!StringUtil.notEmpty(DKZH)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {

            return Response.status(200).entity(this.service.getLoanAccountBorrowerInfo(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response putLoanRiskAssessment(String DKZH, String DKFXDJ) {

        System.out.println("贷款风险评估修改");

        if (!StringUtil.notEmpty(DKZH) || !StringUtil.notEmpty(DKFXDJ)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号或贷款风险等级");
        }

        if (!Arrays.asList(
                LoanRiskStatus.正常.getCode(),
                LoanRiskStatus.关注.getCode(),
                LoanRiskStatus.其他.getCode(),
                LoanRiskStatus.可疑.getCode(),
                LoanRiskStatus.损失.getCode(),
                LoanRiskStatus.次级.getCode()).contains(DKFXDJ)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "贷款风险等级");
        }
        try {

            return Response.status(200).entity(this.service.putRiskAssessment(DKZH, DKFXDJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    @Override
    public Response headSquareReceipt(String DKZH) {
        System.out.println("贷款账号已结清回执单");

        if (!StringUtil.notEmpty(DKZH)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }

        try {
            return Response.status(200).entity(this.service.getSquareReceipt(DKZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    @Override
    public Response HeadHousingfundPlan(TokenContext tokenContext,String DKZH, String HKRQS, String HKRQE) {

        System.out.println("还款计划回执单");

        if (!StringUtil.notEmpty(DKZH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        }
        if(!StringUtil.notEmpty(HKRQS) || !StringUtil.notEmpty(HKRQE) ){
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "查询时间缺失");
        }
        if(!DateUtil.isFollowFormat(HKRQS,"yyyy-MM" , false)){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "开始时间格式不正确");
        }
        if(!DateUtil.isFollowFormat(HKRQE,"yyyy-MM" , false)){
           throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束时间格式不正确");
        }
        int result = HKRQS.compareTo(HKRQE);
        if (result >0){
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能大于结束月份");
        }
        try {
            return Response.status(200).entity(this.service.getHousingfundPlan(tokenContext,DKZH,HKRQS,HKRQE)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

}