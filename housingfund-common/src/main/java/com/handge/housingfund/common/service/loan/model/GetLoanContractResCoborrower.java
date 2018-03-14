package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/8/10.
 */

@XmlRootElement(name = "GetLoanContractResCoborrower")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanContractResCoborrower  implements Serializable {

    private String CDGX;  //参贷关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他）

    private String HKSZD;  //户口所在地

    private String BLZL;  //提交资料

    private String GTJKRGJJZH;  //共同借款人公积金账号

    private String SJHM;  //手机号码

    private String GDDHHM;  //固定电话号码

    private String JCD;  //缴存地

    private String GTJKRZJHM;  //共同借款人证件号码

    private String GTJKRZJLX;  //共同借款人证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）

    private String YSR;  //月收入

    private String GTJKRXM;  //共同借款人姓名

    private CoborrowerUnitInformation DWXX; //单位信息

    private CoborrowerAccountInformation GJJZHXX;   //公积金账户信息

    public String getCDGX() {
        return CDGX;
    }

    public void setCDGX(String CDGX) {
        this.CDGX = CDGX;
    }

    public String getHKSZD() {
        return HKSZD;
    }

    public void setHKSZD(String HKSZD) {
        this.HKSZD = HKSZD;
    }

    public String getTJZL() {
        return BLZL;
    }

    public void setTJZL(String TJZL) {
        this.BLZL = TJZL;
    }

    public String getGTJKRGJJZH() {
        return GTJKRGJJZH;
    }

    public void setGTJKRGJJZH(String GTJKRGJJZH) {
        this.GTJKRGJJZH = GTJKRGJJZH;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getGDDHHM() {
        return GDDHHM;
    }

    public void setGDDHHM(String GDDHHM) {
        this.GDDHHM = GDDHHM;
    }

    public String getJCD() {
        return JCD;
    }

    public void setJCD(String JCD) {
        this.JCD = JCD;
    }

    public String getGTJKRZJHM() {
        return GTJKRZJHM;
    }

    public void setGTJKRZJHM(String GTJKRZJHM) {
        this.GTJKRZJHM = GTJKRZJHM;
    }

    public String getGTJKRZJLX() {
        return GTJKRZJLX;
    }

    public void setGTJKRZJLX(String GTJKRZJLX) {
        this.GTJKRZJLX = GTJKRZJLX;
    }

    public String getYSR() {
        return YSR;
    }

    public void setYSR(String YSR) {
        this.YSR = YSR;
    }

    public String getGTJKRXM() {
        return GTJKRXM;
    }

    public void setGTJKRXM(String GTJKRXM) {
        this.GTJKRXM = GTJKRXM;
    }

    public CoborrowerUnitInformation getDWXX() {
        return DWXX;
    }

    public void setDWXX(CoborrowerUnitInformation DWXX) {
        this.DWXX = DWXX;
    }

    public CoborrowerAccountInformation getGJJZHXX() {
        return GJJZHXX;
    }

    public void setGJJZHXX(CoborrowerAccountInformation GJJZHXX) {
        this.GJJZHXX = GJJZHXX;
    }

    @Override
    public String toString() {
        return "GetLoanContractResCoborrower{" +
                "CDGX='" + CDGX + '\'' +
                ", HKSZD='" + HKSZD + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", GTJKRGJJZH='" + GTJKRGJJZH + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", GDDHHM='" + GDDHHM + '\'' +
                ", JCD='" + JCD + '\'' +
                ", GTJKRZJHM='" + GTJKRZJHM + '\'' +
                ", GTJKRZJLX='" + GTJKRZJLX + '\'' +
                ", YSR='" + YSR + '\'' +
                ", GTJKRXM='" + GTJKRXM + '\'' +
                ", DWXX=" + DWXX +
                ", GJJZHXX=" + GJJZHXX +
                '}';
    }
}
