package com.handge.housingfund.common.service.ca;

import com.handge.housingfund.common.service.ca.model.UnitInfo;

/**
 * Created by tanyi on 2017/6/23.
 */
public interface CaService {

    /**
     * P1验证签名
     *
     * @param CertBase64 证书文件
     * @param indata     原文
     * @param SignDate   签名结果
     */
    boolean VerifySignP1(String CertBase64, String indata, String SignDate);

    /**
     * P7验证签名
     *
     * @param CertBase64 证书文件
     * @param indata     原文
     * @param SignDate   签名结果
     */
    boolean VerifySignP7(String CertBase64, String indata, String SignDate);

    /**
     * 获取单位信息
     *
     * @param dwgjjzh 单位公积金账号
     * @return
     */
    public UnitInfo GetGJJCompanyInfo(String dwgjjzh);

}
