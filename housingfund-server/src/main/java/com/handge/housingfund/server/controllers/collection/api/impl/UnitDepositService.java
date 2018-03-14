package com.handge.housingfund.server.controllers.collection.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.businessdetail.IndiBusiness;
import com.handge.housingfund.common.service.collection.service.businessdetail.UnitBusiness;
import com.handge.housingfund.common.service.collection.service.unitdeposit.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.UnitDepositApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;


@Component
public class UnitDepositService implements UnitDepositApi<Response> {

    @Autowired
    private PersonRadix personRadix;
    @Autowired
    private UnitDepositRatio unitDepositRatio;
    @Autowired
    private UnitDepositInfo unitDepositInfo;
    @Autowired
    private UnitRemittance unitRemittance;
    @Autowired
    private UnitDepositInventory unitDepositInventory;
    @Autowired
    private UnitPayback unitPayback;
    @Autowired
    private UnitPayWrong unitPayWrong;
    @Autowired
    private UnitPayCall unitPayCall;
    @Autowired
    private UnitPayhold unitPayhold;
    @Autowired
    private UnitBusiness unitBusiness;
    @Autowired
    private IndiBusiness indiBusiness;



    /**
     * 根据账号查询清册信息（生成清册数据，从公共基础表读取）
     *
     * @param DWZH 单位账号
     **/
    public Response getRemittanceInventoryPersonalList(TokenContext tokenContext, final String DWZH) {

        System.out.println("根据账号查询清册信息（生成清册数据，从公共基础表读取）");

        if (StringUtil.isEmpty(DWZH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitDepositInventory.getUnitRemittanceInventoryAuto(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取催缴历史记录
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitPayCallHistory(TokenContext tokenContext, final String YWLSH) {

        System.out.println("获取催缴历史记录");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayCall.getUnitPayCallHistoryInfo(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 提交催缴
     *
     * @param unitPayCallPosts
     **/
    public Response postUnitPayCall(TokenContext tokenContext, UnitPayCallPost unitPayCallPosts) {

        System.out.println("提交催缴");

        if (unitPayCallPosts == null || unitPayCallPosts.getBatchSubmission() == null || unitPayCallPosts.getBatchSubmission().getYWLSHJH() == null) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "表单数据");
        }

        for (String ywlsh : unitPayCallPosts.getBatchSubmission().getYWLSHJH()) {
            if (!StringUtil.notEmpty(ywlsh)) {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
            }
        }
        try {

            return Response.status(200).entity(this.unitPayCall.addUnitPayCall(tokenContext, unitPayCallPosts)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缴存比例调整修改 //todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitDepositRatio(TokenContext tokenContext, final String YWLSH, final UnitDepositRatioPut body) {

        System.out.println("单位缴存比例调整修改");

        //参数检查
        if (body == null) return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "表单数据");
        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        try {
            if (!body.getCZLX().equals("0") && !body.getCZLX().equals("1")) {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型有误(0-保存、1-提交)");
            }
        } catch (Exception e) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "操作类型");
        }
        try {

            return Response.status(200).entity(this.unitDepositRatio.reDepositRatio(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缴存比例调整详情 //todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitDepositRatio(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位缴存比例调整详情");

        //参数检查
        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Data_MISS, "业务流水号");
        try {

            return Response.status(200).entity(this.unitDepositRatio.showDepositRatio(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 汇缴申请修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitRemittance(TokenContext tokenContext, final String YWLSH, final UnitRemittancePut body) {

        System.out.println("汇缴申请修改");

        try {

            return Response.status(200).entity(this.unitRemittance.putUnitRemittance(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 汇缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitRemittance(TokenContext tokenContext, final String YWLSH) {

        System.out.println("汇缴申请详情");

        try {

            return Response.status(200).entity(this.unitRemittance.getUnitRemittance(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位汇缴-缴存回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitRemittanceReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位汇缴-缴存回执单");

        try {

            return Response.status(200).entity(this.unitRemittance.headUnitRemittance(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 修改错缴申请
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitPayWrong(TokenContext tokenContext, final String YWLSH, final UnitPayWrongPut body) {

        System.out.println("修改错缴申请");

        try {

            return Response.status(200).entity(this.unitPayWrong.putUnitPayWrong(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取错缴详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitPayWrong(TokenContext tokenContext, final String YWLSH) {

        System.out.println("获取错缴详情");

        try {

            return Response.status(200).entity(this.unitPayWrong.getUnitPayWrong(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 申请单位缴存比例调整时，先获取单位相关信息// TODO djj
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitDepositRatioAuto(TokenContext tokenContext, final String DWZH) {

        System.out.println("申请单位缴存比例调整时，先获取单位相关信息");

        //参数检查
        if (!StringUtil.notEmpty(DWZH)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.unitDepositRatio.autoDepositRatio(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建汇缴申请
     **/
    public Response postUnitRemittance(TokenContext tokenContext, final UnitRemittancePost body) {

        System.out.println("新建汇缴申请");

        try {

            return Response.status(200).entity(this.unitRemittance.postUnitRemittance(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取补缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    public Response getUnitPayBackList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWZT, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, String pageNo, String pageSize) {

        System.out.println("获取补缴记录");

        try {

            return Response.status(200).entity(this.unitPayback.getUnitPayBackList(tokenContext, DWMC, DWZH, YWZT, CZY, YWWD, KSSJ, JSSJ, pageNo, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取单位补缴申请时的单位信息
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitPayBackAuto(TokenContext tokenContext, final String DWZH, final String HBJNY) {

        System.out.println("获取单位补缴申请时的单位信息");

        if (StringUtil.isEmpty(DWZH) || StringUtil.isEmpty(HBJNY)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayback.getUnitPayBackAuto(tokenContext, DWZH, HBJNY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建补缴申请
     **/
    public Response postUnitPayBack(TokenContext tokenContext, final UnitPayBackPost body) {

        System.out.println("新建补缴申请");

        try {

            return Response.status(200).entity(this.unitPayback.postUnitPayBack(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 查看单位缴存信息（从列表进入）
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitDepositDetail(TokenContext tokenContext, final String DWZH) {

        System.out.println("查看单位缴存信息（从列表进入）");


        try {

            return Response.status(200).entity(this.unitDepositInfo.getUnitDepositDetail(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 个人缴存基数调整列表、搜索 //todo djj
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public Response getPersonRadixList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNumber, final String pageSize) {

        System.out.println("个人缴存基数调整列表、搜索");

        if (!StringUtil.notEmpty(pageNumber) || !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码");
        try {

            return Response.status(200).entity(this.personRadix.getPersonRadix(tokenContext, DWMC, DWZH, ZhuangTai, KSSJ, JSSJ, pageNumber, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }

    @Override
    public Response getPersonRadixListnew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pageSize, String action) {

        System.out.println("个人缴存基数调整列表、搜索");

        if ( !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码");
        try {

            return Response.status(200).entity(this.personRadix.getPersonRadixnew(tokenContext, DWMC, DWZH, ZhuangTai, KSSJ, JSSJ, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 个人缴存基数调整申请 // todo djj
     **/
    public Response postUnitCardinalNumber(TokenContext tokenContext, final PersonRadixPost body) {

        System.out.println("个人缴存基数调整申请");

        if (body == null) return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "表单数据");
        try {
            if (!body.getCZLX().equals("0") && !body.getCZLX().equals("1")) {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型有误(0-保存、1-提交)");
            }
        } catch (Exception e) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "操作类型");
        }
        try {

            return Response.status(200).entity(this.personRadix.addPersonRadix(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 根据单位账号获取个人缴存基数调整前详情
     *
     * @param DWZH 单位账号
     **/
    public Response getPersonRadixBeforeDetail(TokenContext tokenContext, final String DWZH, final String SXNY) {

        System.out.println("根据单位账号获取个人缴存基数调整前详情");

        if (!StringUtil.notEmpty(DWZH)) return ResUtils.buildParametersFormatErrorResponse();

        try {

            return Response.status(200).entity(this.personRadix.autoPersonRadix(tokenContext, DWZH,SXNY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }


    /**
     * 根据单位账号获取汇缴申请的单位信息
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitRemittanceAuto(TokenContext tokenContext, final String DWZH) {

        System.out.println("根据单位账号获取汇缴申请的单位信息");

        if (StringUtil.isEmpty(DWZH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitRemittance.getUnitRemittanceAuto(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 新建错缴更正申请
     **/
    public Response postUnitPayWrong(TokenContext tokenContext, final UnitPayWrongPost body) {

        System.out.println("新建错缴更正申请");

        try {

            return Response.status(200).entity(this.unitPayWrong.addUnitPayWrong(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 公积金补缴通知单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitPayBackNotice(TokenContext tokenContext, final String YWLSH) {

        System.out.println("公积金补缴通知单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayback.headUnitPayBackNotice(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取汇缴清册信息（获取清册数据，从清册业务表读取）
     *
     * @param DWZH 单位账号
     **/
    public Response getUnitInventoryList(TokenContext tokenContext, final String DWZH) {

        System.out.println("获取汇缴清册信息（获取清册数据，从清册业务表读取）");

        if (StringUtil.isEmpty(DWZH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitDepositInventory.getUnitRemittanceInventoryAuto(tokenContext, DWZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 打印清册确认单
     **/
    public Response headRemittanceInventory(TokenContext tokenContext, final String dwzh,final String qcny) {
        System.out.println("打印清册确认单");
        if (StringUtil.isEmpty(dwzh) || StringUtil.isEmpty(qcny)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(this.unitDepositInventory.getReceipt(tokenContext,dwzh,qcny)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 打印清册回执单
     *
     * @param QCQRDH 清册确认单号
     **/
    public Response headRemittanceInventory(TokenContext tokenContext, final String QCQRDH) {

        System.out.println("打印清册回执单");

        if (StringUtil.isEmpty(QCQRDH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitDepositInventory.getReceipt(QCQRDH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 获取多月的清册信息
     **/
    public Response getInventoryBatchList(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String YWLSH) {
        System.out.println("获取多月的清册信息");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getInventoryBatchList(tokenContext,dwzh, qcsjq, qcsjz,YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }
    /**
     * 清册受理提交
     */
    public Response postInventoryBatch(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String jkfs, String cxqc) {
        System.out.println("清册受理提交信息");
        try {
            return Response.status(200).entity(this.unitDepositInventory.addInventoryBatch(tokenContext,dwzh,qcsjq,qcsjz,jkfs,cxqc)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 获取单位该月的最新清册详情
     */
    public Response getLatestInventoryOfMonth(TokenContext tokenContext,String ywlsh) {
        System.out.println("获取单位该月的最新清册详情");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getLatestInventoryOfMonth(tokenContext, ywlsh)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }
    /**
     * 获取单位该月的最初的汇缴清册详情
     */
    public Response getFirstInventoryOfMonth(TokenContext tokenContext,String ywlsh) {
        System.out.println("获取单位该月的最新清册详情");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getFirstInventoryOfMonth(tokenContext, ywlsh)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    public Response getInventoryDetail(String dwzh, String qcyf, String xingMing, String pageNumber, String pageSize){
        System.out.println("生成单位该月的清册详情的详情..");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getInventoryDetail(dwzh,qcyf,xingMing,pageNumber,pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 获取单位清册初始化数据
     */
    public Response getUnitInventoryInit(TokenContext tokenContext,String dwzh) {
        System.out.println("获取单位清册初始化数据");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getUnitInventoryInit(tokenContext, dwzh)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getUnitPayBackList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String ywwd,String kssj, String jssj, String marker, String pageSize, String action) {
        System.out.println("获取补缴记录");

        try {

            return Response.status(200).entity(this.unitPayback.getUnitPayBackList(tokenContext, dwmc, dwzh, ywzt, czy, ywwd, kssj, jssj, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    @Override
    public Response getUnitPayWrongList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String marker, String pageSize, String action) {
        System.out.println("获取错缴更正业务记录列表");
        try {
            return Response.status(200).entity(this.unitPayWrong.getUnitPayWrongList(tokenContext, dwmc, dwzh, ywzt, czy, kssj, jssj, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getUnitRemittanceList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy,String yhmc,String ywwd, String kssj, String jssj, String ywpch, String marker, String pageSize, String action) {
        System.out.println("单位汇缴业务记录");
        try {
            return Response.status(200).entity(this.unitRemittance.getUnitRemittanceList(tokenContext, dwmc, dwzh, ywzt, czy,yhmc,ywwd, kssj, jssj,ywpch, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getUnitPayCallList(TokenContext tokenContext, String dwmc, String dwzh, String marker, String pageSize, String kssj, String jssj, String action) {
        System.out.println("催缴记录");
        try {
            return Response.status(200).entity(this.unitPayCall.getUnitPayCallInfo(tokenContext,dwmc, dwzh, kssj, jssj, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getInventoryList(TokenContext tokenContext, String dwmc, String dwzh, String czy, String ywwd, String kssj, String jssj, String marker, String pageSize, String action) {
        System.out.println("缴存清册列表");
        try {
            return Response.status(200).entity(this.unitDepositInventory.getUnitInventoryInfo(tokenContext, dwmc, dwzh, czy, ywwd, kssj, jssj, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 提交缓缴申请
     **/
    public Response postUnitPayHold(TokenContext tokenContext, final UnitPayHoldPost body) {

        System.out.println("提交缓缴申请");

        try {

            return Response.status(200).entity(this.unitPayhold.addUnitPayhold(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 缓缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitPayHoldReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("缓缴回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayhold.headUnitPayhold(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 批量提交缓缴
     *
     * @param ywlshs
     * @return
     */
    public Response submitUnitPayHold(TokenContext tokenContext, BatchSubmission ywlshs) {

        System.out.println("批量提交缓缴");

        if (ywlshs == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayhold.submitUnitPayHold(tokenContext,ywlshs.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取错缴更正业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public Response getUnitPayWrongList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY, final String KSSJ, final String JSSJ, final String page, final String pageSize) {

        System.out.println("获取错缴更正业务记录列表");

        try {

            return Response.status(200).entity(this.unitPayWrong.getUnitPayWrongList(tokenContext, DWMC, DWZH, ZhuangTai, CZY, KSSJ, JSSJ, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 错缴业务回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitPayWrongReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("错缴业务回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayWrong.headUnitPayWrong(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 根据单位账号和更正缴至年月,获取单位错缴信息
     *
     * @param DWZH 单位账号
     * @param JZNY 更正缴至年月
     **/
    public Response autoGetPayWrong(TokenContext tokenContext, final String DWZH, final String JZNY) {

        System.out.println("错缴业务信息");

        if (StringUtil.isEmpty(DWZH) || StringUtil.isEmpty(JZNY)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayWrong.autoGetPayWrong(DWZH, JZNY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 汇缴清册详情
     *
     * @param QCQRDH 清册确认单号
     **/
    public Response getUnitRemittanceInventoryDetail(TokenContext tokenContext, final String QCQRDH) {

        System.out.println("汇缴清册详情");

        if (StringUtil.isEmpty(QCQRDH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitDepositInventory.getUnitRemittanceInventoryDetail(tokenContext, QCQRDH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位汇缴业务记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    public Response getUnitRemittanceList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWZT, final String CZY,final String YHMC,final String YWWD, final String KSSJ, final String JSSJ, final String YWPCH, final String pageNo, final String pageSize) {

        System.out.println("单位汇缴业务记录");

        try {

            return Response.status(200).entity(this.unitRemittance.getUnitRemittanceList(tokenContext, DWMC, DWZH, YWZT, CZY,YHMC,YWWD, KSSJ, JSSJ,YWPCH, pageNo, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 催缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     **/
    public Response getUnitPayCallList(TokenContext tokenContext, final String DWMC, final String DWZH, final String page, final String pageSize,final String kssj,final String jssj) {

        System.out.println("催缴记录");

        try {

            return Response.status(200).entity(this.unitPayCall.getUnitPayCallInfo(tokenContext,DWMC, DWZH, page, pageSize, kssj, jssj)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取催缴详情
     *
     * @param YWLSH 业务流水号
     **/
//    public Response getUnitPayCallDetail(TokenContext tokenContext,final String YWLSH) {
//
//        System.out.println("获取催缴详情（回执单）");
//
////        return Response.status(200).entity(new GetUnitPayCallDetailRes() {{
////
////
////        }}).build();
//
//        try{
//
//            return Response.status(200).entity(this.unitPayCall.headUnitPayCall(YWLSH)).build();
//
//        }catch(ErrorException e){
//
//            return Response.status(200).entity(e.getError()).build();
//
//        }
////    }


    /**
     * 催缴回执单（打印催缴记录）
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitPayCallReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("催缴回执单（打印催缴记录）");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayCall.headUnitPayCall(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY       操作员
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public Response getUnitPayHoldList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, final String page, final String pageSize) {

        System.out.println("获取缓缴业务记录列表");

        try {

            return Response.status(200).entity(this.unitPayhold.getUnitPayholdInfo(tokenContext,DWMC, DWZH, ZhuangTai, CZY, YWWD,KSSJ, JSSJ, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY       操作员
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public Response getUnitPayHoldListNew(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, final String marker, final String pageSize,String action) {

        System.out.println("获取缓缴业务记录列表");

        try {

            return Response.status(200).entity(this.unitPayhold.getUnitPayholdInfoNew(tokenContext,DWMC, DWZH, ZhuangTai, CZY, YWWD,KSSJ, JSSJ, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 缴存列表信息
     *  @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param KHWD
     **/
    public Response getUnitDepositListRes(TokenContext tokenContext, final String DWMC, final String DWZH, final String pageSize, final String page, final boolean GLWD, final boolean GLXH, String KHWD,String SFYWFTYE,String JZNY) {

        System.out.println("缴存列表信息");

        try {

            return Response.status(200).entity(this.unitDepositInfo.getUnitDepositListRes(tokenContext, DWMC, DWZH, pageSize, page, GLWD, GLXH, KHWD, SFYWFTYE,JZNY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位缴存信息列表
     *
     * @param date 年限
     * @param DWZH 单位账号
     **/
    public Response getUnitDepositInfoList(TokenContext tokenContext, String DWZH, String date,final String pageSize,final String page) {

        System.out.println("单位缴存信息列表");

        try {

            return Response.status(200).entity(this.unitDepositInfo.getUnitDepositInfoList(tokenContext, DWZH, date, pageSize, page)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缓缴修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitPayHold(TokenContext tokenContext, final String YWLSH, final UnitPayHoldPut body) {

        System.out.println("单位缓缴修改");

        try {

            return Response.status(200).entity(this.unitPayhold.reUnitPayhold(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取缓缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitPayHold(TokenContext tokenContext, final String YWLSH) {

        System.out.println("获取缓缴申请详情");

        try {

            return Response.status(200).entity(this.unitPayhold.showUnitPayhold(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缴存比例调整列表、搜索
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间起点
     * @param JSSJ      受理时间终点
     **/
    public Response getUnitDepositRatioList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNumber, final String pageSize) {

        System.out.println("单位缴存比例调整列表、搜索");

        if (!StringUtil.notEmpty(pageNumber) || !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码");

        try {

            return Response.status(200).entity(this.unitDepositRatio.getDepositRatioInfo(tokenContext, DWMC, DWZH, ZhuangTai, KSSJ, JSSJ, pageNumber, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    @Override
    public Response getUnitDepositRatioListnew(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String kssj, String jssj, String marker, String pageSize, String action) {
        System.out.println("单位缴存比例调整列表、搜索");

        if ( !StringUtil.notEmpty(pageSize))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "页码");

        try {

            return Response.status(200).entity(this.unitDepositRatio.getDepositRatioInfonew(tokenContext, dwmc, dwzh, ywzt, kssj, jssj, marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 缴存清册列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param CZY  操作员
     * @param KSSJ 受理时间开始(开始时间）
     * @param JSSJ 受理时间结束(结束时间）
     **/
    public Response getInventoryList(TokenContext tokenContext, final String DWMC, final String DWZH, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, String pageNo, String pageSize) {

        System.out.println("缴存清册列表");

        try {

            return Response.status(200).entity(this.unitDepositInventory.getUnitInventoryInfo(tokenContext, DWMC, DWZH, CZY,YWWD, KSSJ, JSSJ, pageNo, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 清册确认
     **/
    public Response postInventoryConfirm(TokenContext tokenContext, final InventoryConfirmPost body) {

        System.out.println("清册确认");

        try {

            return Response.status(200).entity(this.unitDepositInventory.postUnitDepositInventory(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 个人缴存基数调整修改//todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response putPersonRadix(TokenContext tokenContext, final String YWLSH, final PersonRadixPut body) {

        System.out.println("个人缴存基数调整修改");
        if (body == null) return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "表单数据");

        try {
            if (!body.getCZLX().equals("0") && !body.getCZLX().equals("1")) {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型有误(0-保存、1-提交)");
            }
        } catch (Exception e) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "操作类型");
        }

        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        try {

            return Response.status(200).entity(this.personRadix.rePersonRadix(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 个人缴存基数调整详情//todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response getPersonRadix(TokenContext tokenContext, final String YWLSH) {

        System.out.println("个人缴存基数调整详情");

        if (!StringUtil.notEmpty(YWLSH))
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        try {

            return Response.status(200).entity(this.personRadix.showPersonRadix(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 公积金补缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitPayBackReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("公积金补缴回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayback.HeadUnitPayBack(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缴存比例调整申请
     **/
    public Response postUnitDepositRatio(TokenContext tokenContext, final PostListUnitDepositRatioPost body) {

        System.out.println("单位缴存比例调整申请");
        if (body == null) return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "表单数据");

        try {
            if (!body.getCZLX().equals("0") && !body.getCZLX().equals("1")) {
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型(0-保存、1-提交)");
            }
        } catch (Exception e) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "操作类型");
        }
        try {
            return Response.status(200).entity(this.unitDepositRatio.addDepositRatio(tokenContext, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 单位缴存比例调整回执单//todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitDepositRatioReceipt(TokenContext tokenContext, final String YWLSH) {

        System.out.println("单位缴存比例调整回执单");

        //参数检查
        if (!StringUtil.notEmpty(YWLSH)) {

            return ResUtils.buildParametersFormatErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitDepositRatio.headDepositRatio(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 单位
     */
    @Override
    public Response unitSubmitBatch(TokenContext tokenContext, BatchSubmission body) {

        System.out.println("批量提交单位");

        try {

            return Response.status(200).entity(this.unitDepositRatio.batchSubmit(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 个人
     */
    @Override
    public Response personSubmitBatch(TokenContext tokenContext, BatchSubmission body) {

        System.out.println("批量提交个人");

        try {

            return Response.status(200).entity(this.personRadix.batchSubmit(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 个人缴存基数调整回执单//todo djj
     *
     * @param YWLSH 业务流水号
     **/
    public Response headPersonRadix(TokenContext tokenContext, final String YWLSH) {

        System.out.println("个人缴存基数调整回执单");

        //参数检查
        if (!StringUtil.notEmpty(YWLSH)) return ResUtils.buildParametersFormatErrorResponse();
        try {

            return Response.status(200).entity(this.personRadix.headPersonRadix(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 修改补缴
     *
     * @param YWLSH 业务流水号
     **/
    public Response putUnitPayBack(TokenContext tokenContext, final String YWLSH, final UnitPayBackPut body) {

        System.out.println("修改补缴");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayback.putUnitPayBack(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 获取单位补缴详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getUnitPayBack(TokenContext tokenContext, final String YWLSH) {

        System.out.println("获取单位补缴详情");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.unitPayback.getUnitPayBack(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    public Response postPayBackTemporary(TokenContext tokenContext, final String YWLSH) {
        System.out.println("补缴重新发起");
        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(this.unitPayback.postPayBackTemporary(tokenContext, YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }



    /**
     * 单位业务明细列表 // todo djj
     *
     * @param DWMC   单位名称
     * @param DWZH   单位账号
     * @param YWMXLX 业务明细类型
     **/
    public Response getUnitBusnissDetailList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWMXLX, final String pageNumber, final String pageSize,String start,String end) {

        System.out.println("单位业务明细列表");

        if (StringUtil.notEmpty(YWMXLX) && !Arrays.asList(
                CollectionBusinessType.所有.getCode(),
                CollectionBusinessType.变更.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.销户.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.催缴.getCode(),
                CollectionBusinessType.汇缴.getCode(),
                CollectionBusinessType.补缴.getCode(),
                CollectionBusinessType.内部转移.getCode(),
                CollectionBusinessType.外部转出.getCode(),
                CollectionBusinessType.外部转入.getCode(),
                CollectionBusinessType.年终结息.getCode(),
                CollectionBusinessType.基数调整.getCode(),
                CollectionBusinessType.比例调整.getCode(),
                CollectionBusinessType.部分提取.getCode(),
                CollectionBusinessType.缓缴处理.getCode(),
                CollectionBusinessType.销户提取.getCode(),
                CollectionBusinessType.其他.getCode(),
                CollectionBusinessType.合并.getCode(),
                CollectionBusinessType.错缴更正.getCode(),
                CollectionBusinessType.单位清册.getCode()).contains(YWMXLX)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Business_Type_NOT_MATCH, "请检查业务类型编码");
        }

        try {
            return Response.status(200).entity(this.unitBusiness.showUnitBusinessDetails(tokenContext,DWMC, DWZH, YWMXLX, pageNumber, pageSize,start,end)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }

    @Override
    public Response getUnitBusnissDetailListnew(TokenContext tokenContext, String DWMC, String DWZH, String YWMXLX, String marker, String pageSize, String start, String end, String action) {

        System.out.println("单位业务明细列表");

        if (StringUtil.notEmpty(YWMXLX) && !Arrays.asList(
                CollectionBusinessType.所有.getCode(),
                CollectionBusinessType.变更.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.销户.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.催缴.getCode(),
                CollectionBusinessType.汇缴.getCode(),
                CollectionBusinessType.补缴.getCode(),
                CollectionBusinessType.内部转移.getCode(),
                CollectionBusinessType.外部转出.getCode(),
                CollectionBusinessType.外部转入.getCode(),
                CollectionBusinessType.年终结息.getCode(),
                CollectionBusinessType.基数调整.getCode(),
                CollectionBusinessType.比例调整.getCode(),
                CollectionBusinessType.部分提取.getCode(),
                CollectionBusinessType.缓缴处理.getCode(),
                CollectionBusinessType.销户提取.getCode(),
                CollectionBusinessType.其他.getCode(),
                CollectionBusinessType.合并.getCode(),
                CollectionBusinessType.错缴更正.getCode(),
                CollectionBusinessType.单位清册.getCode()).contains(YWMXLX)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Business_Type_NOT_MATCH, "请检查业务类型编码");
        }

        try {
            return Response.status(200).entity(this.unitBusiness.showUnitBusinessDetailsnew(tokenContext,DWMC, DWZH, YWMXLX, marker, pageSize,start,end,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }

    }

    /**
     * 个人业务明细列表//todo djj
     *
     * @param XingMing 姓名
     * @param GRZH     个人账号
     * @param YWMXLX   业务明细类型
     **/
    public Response getIndiBusnissDetailList(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String pageNumber, final String pageSize,String start,String end,String ZJHM) {

        System.out.println("个人业务明细列表");

        if (StringUtil.notEmpty(YWMXLX) && !Arrays.asList(
                CollectionBusinessType.所有.getCode(),
                CollectionBusinessType.变更.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.合并.getCode(),
                CollectionBusinessType.解冻.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.汇缴.getCode(),
                CollectionBusinessType.补缴.getCode(),
                CollectionBusinessType.错缴更正.getCode(),
                CollectionBusinessType.内部转移.getCode(),
                CollectionBusinessType.部分提取.getCode(),
                CollectionBusinessType.外部转出.getCode(),
                CollectionBusinessType.外部转入.getCode(),
                CollectionBusinessType.年终结息.getCode(),
                CollectionBusinessType.销户提取.getCode(),
                CollectionBusinessType.其他.getCode()).contains(YWMXLX)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Business_Type_NOT_MATCH, "请检查业务类型编码");
        }
        try {

            return Response.status(200).entity(this.indiBusiness.showIndiBusinessDetails(tokenContext,XingMing, GRZH, YWMXLX, pageNumber, pageSize,start,end,ZJHM)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    @Override
    public Response getIndiBusnissDetailListnew(TokenContext tokenContext, String XingMing, String GRZH, String YWMXLX, String marker, String pageSize, String start, String end, String action) {

        System.out.println("个人业务明细列表new");
        try {

            return Response.status(200).entity(this.indiBusiness.showIndiBusinessDetailsnew(tokenContext,XingMing, GRZH, YWMXLX, marker, pageSize,start,end,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


    /**
     * 汇缴申请时，根据账号+汇补缴年月查询清册信息
     *
     * @param dwzh
     * @param hbjny
     * @return
     */
    @Override
    public Response UnitRemittanceInventory(TokenContext tokenContext, String dwzh, String hbjny) {

        System.out.println("获取单位汇缴信息（清册部分）");

        try {

            return Response.status(200).entity(this.unitRemittance.getUnitRemittanceInventory(tokenContext, dwzh, hbjny)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }

    /**
     * 汇缴通知单
     *
     * @param ywlsh
     * @return
     */
    @Override
    public Response headUnitRemittanceNotice(TokenContext tokenContext, String ywlsh) {
        System.out.println("获取单位汇缴通知单）");

        try {

            return Response.status(200).entity(this.unitRemittance.headUnitRemittanceNotice(ywlsh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();

        }
    }


}