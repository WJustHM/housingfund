package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;

/**
 * 增强安全
 */
public class EnhancedSecurity implements Serializable {
    private static final long serialVersionUID = 7974322381917929127L;
    /**
     * 安全机具设备标识(required)
     */
    private String KeyData;
    /**
     * 签名信息(required)
     */
    private String Signature;

    public EnhancedSecurity() {
    }

    public EnhancedSecurity(String keyData, String signature) {
        KeyData = keyData;
        Signature = signature;
    }

    public String getKeyData() {
        return KeyData;
    }

    public void setKeyData(String keyData) {
        KeyData = keyData;
    }

    @Override
    public String toString() {
        return "EnhancedSecurity{" +
                "KeyData='" + KeyData + '\'' +
                ", Signature='" + Signature + '\'' +
                '}';
    }
}
