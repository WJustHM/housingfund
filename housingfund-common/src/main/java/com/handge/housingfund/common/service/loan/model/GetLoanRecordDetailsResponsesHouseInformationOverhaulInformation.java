package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation  implements Serializable {

    private String FXHCS;  //翻修后层数

    private String BLZL;  //提交资料

    private String GRSKYHZH;  //个人收款银行账号

    private String PZJGWH;  //批准机关文号

    private String KHHYHMC;  //开户行银行名称

    private String YJZMJ;  //原建筑面积

    private String FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）

    private String JHJGRQ;  //计划竣工日期

    private String JHFXFY;  //计划翻修费用

    private String FWZLDZ;  //房屋坐落地址

    private String JHKGRQ;  //计划开工日期

    private String YHKHM;  //银行开户名

    private String YBDCZH;  //原不动产证号

    private String DXGCYS;  //大修工程预算

    private String TDSYZH;  //土地使用证号

    private String FWZJBGJGMCJBH;   //房屋质检报告机关名称及编号

    public String getYBDCZH() {
        return YBDCZH;
    }

    public void setYBDCZH(String YBDCZH) {
        this.YBDCZH = YBDCZH;
    }

    public String getDXGCYS() {
        return DXGCYS;
    }

    public void setDXGCYS(String DXGCYS) {
        this.DXGCYS = DXGCYS;
    }

    public String getTDSYZH() {
        return TDSYZH;
    }

    public void setTDSYZH(String TDSYZH) {
        this.TDSYZH = TDSYZH;
    }

    public String getFWZJBGJGMCJBH() {
        return FWZJBGJGMCJBH;
    }

    public void setFWZJBGJGMCJBH(String FWZJBGJGMCJBH) {
        this.FWZJBGJGMCJBH = FWZJBGJGMCJBH;
    }

    public String getFXHCS() {

        return this.FXHCS;

    }


    public void setFXHCS(String FXHCS) {

        this.FXHCS = FXHCS;

    }


    public String getTJZL() {

        return this.BLZL;

    }


    public void setTJZL(String TJZL) {

        this.BLZL = TJZL;

    }


    public String getGRSKYHZH() {

        return this.GRSKYHZH;

    }


    public void setGRSKYHZH(String GRSKYHZH) {

        this.GRSKYHZH = GRSKYHZH;

    }


    public String getPZJGWH() {

        return this.PZJGWH;

    }


    public void setPZJGWH(String PZJGWH) {

        this.PZJGWH = PZJGWH;

    }


    public String getKHHYHMC() {

        return this.KHHYHMC;

    }


    public void setKHHYHMC(String KHHYHMC) {

        this.KHHYHMC = KHHYHMC;

    }


    public String getYJZMJ() {

        return this.YJZMJ;

    }


    public void setYJZMJ(String YJZMJ) {

        this.YJZMJ = YJZMJ;

    }


    public String getFWXZ() {

        return this.FWXZ;

    }


    public void setFWXZ(String FWXZ) {

        this.FWXZ = FWXZ;

    }


    public String getJHJGRQ() {

        return this.JHJGRQ;

    }


    public void setJHJGRQ(String JHJGRQ) {

        this.JHJGRQ = JHJGRQ;

    }


    public String getJHFXFY() {

        return this.JHFXFY;

    }


    public void setJHFXFY(String JHFXFY) {

        this.JHFXFY = JHFXFY;

    }


    public String getFWZLDZ() {

        return this.FWZLDZ;

    }


    public void setFWZLDZ(String FWZLDZ) {

        this.FWZLDZ = FWZLDZ;

    }


    public String getJHKGRQ() {

        return this.JHKGRQ;

    }


    public void setJHKGRQ(String JHKGRQ) {

        this.JHKGRQ = JHKGRQ;

    }


    public String getYHKHM() {

        return this.YHKHM;

    }


    public void setYHKHM(String YHKHM) {

        this.YHKHM = YHKHM;

    }

    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation{" +
                "FXHCS='" + FXHCS + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", GRSKYHZH='" + GRSKYHZH + '\'' +
                ", PZJGWH='" + PZJGWH + '\'' +
                ", KHHYHMC='" + KHHYHMC + '\'' +
                ", YJZMJ='" + YJZMJ + '\'' +
                ", FWXZ='" + FWXZ + '\'' +
                ", JHJGRQ='" + JHJGRQ + '\'' +
                ", JHFXFY='" + JHFXFY + '\'' +
                ", FWZLDZ='" + FWZLDZ + '\'' +
                ", JHKGRQ='" + JHKGRQ + '\'' +
                ", YHKHM='" + YHKHM + '\'' +
                ", YBDCZH='" + YBDCZH + '\'' +
                ", DXGCYS='" + DXGCYS + '\'' +
                ", TDSYZH='" + TDSYZH + '\'' +
                ", FWZJBGJGMCJBH='" + FWZJBGJGMCJBH + '\'' +
                '}';
    }
}