package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/8/17.
 */
@XmlRootElement(name = "UploadFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadFile implements Serializable {
    private static final long serialVersionUID = 6334118856949936908L;
    //模块名称（01：归集 02：提取 03：贷款）
    private String modle;
    //业务名称（01：单位开户 02：单位信息变更 03：单位账户注销 04：单位账号封存 05：单位账号启封 06：清册确认 07：汇缴申请 08：单位补缴 09：单位错缴 10：单位缓缴 11：单位缴存比例调整 12：个人缴存基数调整 13：个人账户设立 14：个人信息变更 15：合并个人账户 16：个人账户封存、启封 17：异地转移接续（只有转出） 18：个人账户冻结、解冻 19：个人账户内部转移
    // 01：购买住房 02：建造、翻建 03：大修住房 04：离休、退休 05：完全丧失劳动能力、并与单位终止劳动合同 06：出（国）境定居 07：偿还购房贷款本息（含公积金贷款） 08：租房 09：死亡 10：大病医疗
    // 01：房开公司申请/变更 02：楼盘项目申请/变更 03：贷款发放-申请人 04：贷款发放-共同借款人 05：贷款发放-房屋信息-购买（商品房）（期房操作流程） 06：贷款发放-房屋信息-购买（二手房）（现房操作流程） 07：贷款发放-房屋信息-自建、翻建（现房操作流程） 08：贷款发放-房屋信息-大修（现房操作流程） 09：贷款发放-担保信息 10：贷款发放-其他资料 11：划拨之前 12：借款合同变更 13：贷款合同变更 14：签订合同
    private String business;
    private String certificateName;
    private Boolean required;

    public UploadFile(String modle, String business, String certificateName, Boolean required) {
        this.modle = modle;
        this.business = business;
        this.certificateName = certificateName;
        this.required = required;
    }

    public UploadFile() {
    }

    public String getModle() {
        return modle;
    }

    public void setModle(String modle) {
        this.modle = modle;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
