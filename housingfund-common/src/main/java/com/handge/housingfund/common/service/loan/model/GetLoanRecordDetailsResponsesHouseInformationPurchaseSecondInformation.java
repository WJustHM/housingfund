package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation  implements Serializable {

    private String LPMC;    //楼盘名称

    private String FWJZMJ;  //房屋建筑面积

    private String GRSKYHZH;  //个人收款银行账号

    private String FWZL;  //房屋坐落

    private String FWTNMJ;  //房屋套内面积

    private String FWXS;  //房屋形式（0：在建房  1：现房）

    private String FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）

    private BigDecimal FWZJ;  //房屋总价

    private String KHYHMC;  //开户银行名称

    private String FWJGRQ;  //房屋竣工日期

    private String FWJG;  //房屋结构（0：框架 1：砖混 2：其他）

    private BigDecimal HTJE;  //合同金额

    private String YHKHM;  //银行开户名

    private String GFHTBH;  //购房合同编号

    private BigDecimal DanJia;  //单价

    private String LXFS;  //联系方式

    private String SFRMC;  //售房人名称

    private BigDecimal SFK;  //首付款

    private String YFK; //已付款

    private String BLZL;

    public String getLPMC() {
        return LPMC;
    }

    public void setLPMC(String LPMC) {
        this.LPMC = LPMC;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public String getFWJZMJ() {

        return this.FWJZMJ;

    }


    public void setFWJZMJ(String FWJZMJ) {

        this.FWJZMJ = FWJZMJ;

    }


    public String getGRSKYHZH() {

        return this.GRSKYHZH;

    }


    public void setGRSKYHZH(String GRSKYHZH) {

        this.GRSKYHZH = GRSKYHZH;

    }


    public String getFWZL() {

        return this.FWZL;

    }


    public void setFWZL(String FWZL) {

        this.FWZL = FWZL;

    }


    public String getFWTNMJ() {

        return this.FWTNMJ;

    }


    public void setFWTNMJ(String FWTNMJ) {

        this.FWTNMJ = FWTNMJ;

    }


    public String getFWXS() {

        return this.FWXS;

    }


    public void setFWXS(String FWXS) {

        this.FWXS = FWXS;

    }


    public String getFWXZ() {

        return this.FWXZ;

    }


    public void setFWXZ(String FWXZ) {

        this.FWXZ = FWXZ;

    }


    public BigDecimal getFWZJ() {

        return this.FWZJ;

    }


    public void setFWZJ(BigDecimal FWZJ) {

        this.FWZJ = FWZJ;

    }


    public String getKHYHMC() {

        return this.KHYHMC;

    }


    public void setKHYHMC(String KHYHMC) {

        this.KHYHMC = KHYHMC;

    }


    public String getFWJGRQ() {

        return this.FWJGRQ;

    }


    public void setFWJGRQ(String FWJGRQ) {

        this.FWJGRQ = FWJGRQ;

    }


    public String getFWJG() {

        return this.FWJG;

    }


    public void setFWJG(String FWJG) {

        this.FWJG = FWJG;

    }


    public BigDecimal getHTJE() {

        return this.HTJE;

    }


    public void setHTJE(BigDecimal HTJE) {

        this.HTJE = HTJE;

    }


    public String getYHKHM() {

        return this.YHKHM;

    }


    public void setYHKHM(String YHKHM) {

        this.YHKHM = YHKHM;

    }


    public String getGFHTBH() {

        return this.GFHTBH;

    }


    public void setGFHTBH(String GFHTBH) {

        this.GFHTBH = GFHTBH;

    }


    public BigDecimal getDanJia() {

        return this.DanJia;

    }


    public void setDanJia(BigDecimal DanJia) {

        this.DanJia = DanJia;

    }


    public String getLXFS() {

        return this.LXFS;

    }


    public void setLXFS(String LXFS) {

        this.LXFS = LXFS;

    }


    public String getSFRMC() {

        return this.SFRMC;

    }


    public void setSFRMC(String SFRMC) {

        this.SFRMC = SFRMC;

    }


    public BigDecimal getSFK() {

        return this.SFK;

    }


    public void setSFK(BigDecimal SFK) {

        this.SFK = SFK;

    }

    public String getYFK() {
        return YFK;
    }

    public void setYFK(String YFK) {
        this.YFK = YFK;
    }

    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation{" +
                "FWJZMJ='" + FWJZMJ + '\'' +
                ", GRSKYHZH='" + GRSKYHZH + '\'' +
                ", FWZL='" + FWZL + '\'' +
                ", FWTNMJ='" + FWTNMJ + '\'' +
                ", FWXS='" + FWXS + '\'' +
                ", FWXZ='" + FWXZ + '\'' +
                ", FWZJ=" + FWZJ +
                ", KHYHMC='" + KHYHMC + '\'' +
                ", FWJGRQ='" + FWJGRQ + '\'' +
                ", FWJG='" + FWJG + '\'' +
                ", HTJE=" + HTJE +
                ", YHKHM='" + YHKHM + '\'' +
                ", GFHTBH='" + GFHTBH + '\'' +
                ", DanJia=" + DanJia +
                ", LXFS='" + LXFS + '\'' +
                ", SFRMC='" + SFRMC + '\'' +
                ", SFK=" + SFK +
                ", YFK='" + YFK + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}