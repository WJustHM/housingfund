package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;

import javax.ws.rs.QueryParam;

public interface ISignContractListApi<T> {


    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public T getSignContractList(TokenContext tokenContext,String YWZT,String JE, final String JKRXM, final String JKRZJHM, final String DKYT, String YHMC,String YWWD, String pageSize, String page, String KSSJ, String JSSJ);


    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public T getSignContractList(TokenContext tokenContext, final String JKRXM, final String JKRZJHM, final String DKYT,String YHMC,String YWWD, String pageSize, String marker,String action,String KSSJ,String JSSJ);


    /**
     * 自动获取合同信息
     *
     * @param YWLSH 业务流水号
     **/
    public T getContractAuto(TokenContext tokenContext,String YWLSH);


    /**
     * 打印 合同pdf
     *
     * @param YWLSH 业务流水号
     **/
    public T printContractPdf(TokenContext tokenContext,String YWLSH);
}