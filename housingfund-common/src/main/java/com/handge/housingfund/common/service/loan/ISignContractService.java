package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.SignContractPost;
import com.handge.housingfund.common.service.loan.model.SignContractResponse;


public interface ISignContractService {
    /**
     * 贷款合同（待定）
     *
     * @param YWLSH 业务流水号
     **/
    public CommonResponses postPurchaseContract(TokenContext tokenContext,final String status , final String YWLSH, final SignContractPost body);

    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public PageRes<SignContractResponse> getSignContractList(TokenContext tokenContext,String YWZT,String JE,final String JKRXM, final String JKRZJHM, final String DKYT, String YHMC,String YWWD,String pageSize, String page,String KSSJ,String JSSJ);


    /**
     * 签订合同列表
     *  @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public PageResNew<SignContractResponse> getSignContractList(TokenContext tokenContext, final String JKRXM, final String JKRZJHM, final String DKYT, String YHMC,String YWWD, String pageSize, String marker, String action, String KSSJ, String JSSJ);

    /**
     * 自动获取合同信息
     *
     * @param YWLSH 业务流水号
     **/
    public SignContractPost getContractAuto(TokenContext tokenContext,String YWLSH);


    /**
     * 打印 合同pdf
     *
     * @param YWLSH 业务流水号
     **/
    public CommonResponses printContractPdf(TokenContext tokenContext, String YWLSH);


}
