package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.IRepaymentService;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPost;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IRepaymentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class RepaymentApi implements IRepaymentApi<Response> {

    @Autowired
    private IRepaymentService repaymentService;

    /**
     * 还款repaymentApplyReceipt
     * //TODO djj
     *
     * @param DKZH  贷款账号
     * @param JKRXM 借款人姓名
     **/
    public Response getHousingRepaymentApplyList(TokenContext tokenContext, final String DKZH, final String JKRXM, String pageSize, String page,String KSSJ,String JSSJ,String YHDM,String ZJHM) {

        System.out.println("还款repaymentApplyReceipt");

        if (!StringUtil.notEmpty(page) || !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "页码不能为空");
        try {
            return Response.status(200).entity(this.repaymentService.getHousingRepaymentApplyList(tokenContext, DKZH, JKRXM, pageSize, page,KSSJ,JSSJ,YHDM,ZJHM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getHousingRepaymentApplyListnew(TokenContext tokenContext, String dkzh, String jkrxm, String pageSize, String marker, String kssj, String jssj, String action,String YHDM) {
        System.out.println("还款repaymentApplyReceipt");

        if (!StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "页码不能为空");
        try {
            return Response.status(200).entity(this.repaymentService.getHousingRepaymentApplyListnew(tokenContext, dkzh, jkrxm, pageSize, marker,kssj,jssj,action,YHDM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 新增还款申请
     * //TODO djj
     *
     * @param action (0保存 1提交)
     **/
    public Response postRepayment(TokenContext tokenContext, final String action, final RepaymentApplyPrepaymentPost body) {

        //逾期还款9  提前部分还款8   提前结清贷款0
        if ((!StringUtil.notEmpty(body.getHKLX())) || (!body.getHKLX().equals("04") && !body.getHKLX().equals("03") && !body.getHKLX().equals("06"))) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "还款类型有误(04-逾期还款、03-提前部分、06-结清贷款)");
        }
        System.out.println("新增还款申请");

        try {
            return Response.status(200).entity(this.repaymentService.postRepayment(tokenContext, action, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 修改还款申请
     * //TODO completed over
     *
     * @param action (0保存 1提交)
     * @param YWLSH  业务流水号
     **/
    public Response putRepayment(TokenContext tokenContext, final String action, final String YWLSH, final RepaymentApplyPrepaymentPost body) {

        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if ((!StringUtil.notEmpty(body.getHKLX())) || (!body.getHKLX().equals("04") && !body.getHKLX().equals("03") && !body.getHKLX().equals("06"))) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "还款类型有误(04-逾期还款、03-提前部分、06-结清贷款)");
        }
        System.out.println("修改还款申请");

        try {
            return Response.status(200).entity(this.repaymentService.putRepayment(tokenContext, action, YWLSH, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 删除申请受理
     * //TODO djj
     **/
    public Response deleteHousingRepaymentApply(TokenContext tokenContext, final DelList body) {

        System.out.println("删除申请受理");

//        return Response.status(200).entity(new CommonResponses() {{
//
//
//        }}).build();
//        return Response.status(200).entity(this.repaymentService.deleteHousingRepaymentApply(tokenContext,body)).build();
        return null;
    }


    /**
     * 获取还款申请详情
     * //TODO djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response getPerpaymentDetails(TokenContext tokenContext, final String YWLSH) {

        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }
        System.out.println("获取还款申请详情");

//        return Response.status(200).entity(new RepaymentApplyPrepaymentPost() {{
//        }}).build();
        try {
            return Response.status(200).entity(this.repaymentService.getPerpaymentDetails(tokenContext, YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 提交与撤回还款申请
     * //TODO djj
     *
     * @param status 状态（0：提交 1：撤回 ）
     **/
//    @Deprecated
    public Response putEstateStatusSubmit(TokenContext tokenContext, final BatchSubmission body, final String status, String ywlx) {

        System.out.println("提交与撤回还款申请");

        if (!StringUtil.notEmpty(status) || (!"0".equals(status) && !"1".equals(status) && !"2".equals(status)))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "状态编码有误(0-提交、1-撤回、2-作废)");

        try {
            return Response.status(200).entity(this.repaymentService.batchSubmit(tokenContext, body, status, ywlx)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 打印还款申请回执
     * //TODO completed over
     *
     * @param YWLSH 业务流水号
     * @return
     */
    public Response printRepaymentReceipt(TokenContext tokenContext, String YWLSH) {

        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

//        return Response.status(200).entity(new RepaymentApplyReceipt() {
//        }).build();
        try {
            return Response.status(200).entity(this.repaymentService.printRepaymentReceipt(tokenContext, YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response backRepaymentInfo(TokenContext tokenContext, String hklx, String DKZH, String YDKKRQ, String BCHKJE) {

        if (!StringUtil.notEmpty(DKZH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "贷款账号");
        if (!StringUtil.notEmpty(hklx))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "还款类型有误(04-逾期还款、03-提前部分、06-结清贷款)");
        //逾期还款9  提前部分还款8   提前结清贷款0
        if (!"04".equals(hklx) && !"03".equals(hklx) && !"06".equals(hklx)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "还款类型有误(04-逾期还款、03-提前部分、06-结清贷款)");
        }
        if (!StringUtil.notEmpty(YDKKRQ))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "约定还款日期");

        try {
            return Response.status(200).entity(this.repaymentService.backRepaymentInfo(tokenContext, hklx, DKZH, YDKKRQ, BCHKJE,"","")).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 办结操作
     */
    @Override
    public Response doAction(TokenContext tokenContext, String YWLSH) {
        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        try {
//            return Response.status(200).entity(this.repaymentService.doAction(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
        return null;
    }

    /**
     * 查询失败
     */
    @Override
    public Response getFailedBuinessInfo(TokenContext tokenContext, String JKRXM, String DKZH, String stime, String etime, String pageSize, String page,String YHDM,String ZJHM,String HKYWLX,String YWWD) {

        try {
            return Response.status(200).entity(this.repaymentService.getFailedBuinessInfo(tokenContext, JKRXM, DKZH, stime, etime, pageSize, page,YHDM,ZJHM,HKYWLX,YWWD)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getFailedBuinessInfonew(TokenContext tokenContext, String JKRXM, String DKZH, String stime, String etime, String pageSize, String marker, String action,String YHDM) {
        try {
            return Response.status(200).entity(this.repaymentService.getFailedBuinessInfonew(tokenContext, JKRXM, DKZH, stime, etime, pageSize, marker,action,YHDM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 审核扣划记录
     */
    @Override
    public Response putFailedBuinessSubmit(TokenContext tokenContext, String ID, String CZLX) {

        try {
            return Response.status(200).entity(this.repaymentService.putFailedBuinessSubmit(tokenContext, ID, CZLX)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getOverdueModification(TokenContext tokenContext, String ywlsh, String dkzh, String ydkkrq) {

        try {
            return Response.status(200).entity(this.repaymentService.getOverdueModification(tokenContext, ywlsh, dkzh, ydkkrq)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}