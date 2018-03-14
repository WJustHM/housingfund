package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadIndiAcctActionRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadIndiAcctActionRes implements Serializable {

    private String GRJCJS;  //个人缴存基数

    private String GRYJCE;  //个人月缴存额

    private String ZJLX;  //证件类型

    private String GRZH;  //个人账号

    private String RiQi;  //日期

    private String JZNY;  //缴至年月

    private String DWYJCE;  //单位月缴存额

    private String ZJHM;  //证件号码

    private String BeiZhu;  //备注（封存、启封、冻结、解冻）

    private String GRZHDNGJYE;  //个人账户当年归集余额（元)

    private String GRZHZT;  //个人账户状态

    private String DWZH;  //单位账号

    private String DJJE;  //冻结金额（冻结/解冻）

    private String DWMC;  //单位名称

    private String YWLSH;  //业务流水号

    private String GRZHYE;  //个人账户余额（元）

    private String GRZHSNJZYE;  //个人账户上年结转余额（元）

    private String YJCE;  //月缴存额

    private String SXNY;  //生效年月

    private String CZMC;  //操作名称(05:封存;04:启封;14:冻结;15:解冻;)

    private String GRJCBL;  //个人缴存比例

    private String GRZJNY;  //个人缴至年月

    private String DWJCBL;  //单位缴存比例

    private String CZYY;  //操作（封存、启封、冻结、解冻、托管）原因

    private String XingMing;  //姓名

    private String DWJBR; //单位经办人

    private String CZY; //操作员

    private String SHR; //审核人

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    private String YWWD; //业务网点

    public String getDWJBR() {
        return DWJBR;
    }

    public void setDWJBR(String DWJBR) {
        this.DWJBR = DWJBR;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String getGRJCJS() {

        return this.GRJCJS;

    }


    public void setGRJCJS(String GRJCJS) {

        this.GRJCJS = GRJCJS;

    }


    public String getGRYJCE() {

        return this.GRYJCE;

    }


    public void setGRYJCE(String GRYJCE) {

        this.GRYJCE = GRYJCE;

    }


    public String getZJLX() {

        return this.ZJLX;

    }


    public void setZJLX(String ZJLX) {

        this.ZJLX = ZJLX;

    }


    public String getGRZH() {

        return this.GRZH;

    }


    public void setGRZH(String GRZH) {

        this.GRZH = GRZH;

    }


    public String getRiQi() {

        return this.RiQi;

    }


    public void setRiQi(String RiQi) {

        this.RiQi = RiQi;

    }


    public String getJZNY() {

        return this.JZNY;

    }


    public void setJZNY(String JZNY) {

        this.JZNY = JZNY;

    }


    public String getDWYJCE() {

        return this.DWYJCE;

    }


    public void setDWYJCE(String DWYJCE) {

        this.DWYJCE = DWYJCE;

    }


    public String getZJHM() {

        return this.ZJHM;

    }


    public void setZJHM(String ZJHM) {

        this.ZJHM = ZJHM;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getGRZHDNGJYE() {

        return this.GRZHDNGJYE;

    }


    public void setGRZHDNGJYE(String GRZHDNGJYE) {

        this.GRZHDNGJYE = GRZHDNGJYE;

    }


    public String getGRZHZT() {

        return this.GRZHZT;

    }


    public void setGRZHZT(String GRZHZT) {

        this.GRZHZT = GRZHZT;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }


    public String getDJJE() {

        return this.DJJE;

    }


    public void setDJJE(String DJJE) {

        this.DJJE = DJJE;

    }


    public String getDWMC() {

        return this.DWMC;

    }


    public void setDWMC(String DWMC) {

        this.DWMC = DWMC;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getGRZHYE() {

        return this.GRZHYE;

    }


    public void setGRZHYE(String GRZHYE) {

        this.GRZHYE = GRZHYE;

    }


    public String getGRZHSNJZYE() {

        return this.GRZHSNJZYE;

    }


    public void setGRZHSNJZYE(String GRZHSNJZYE) {

        this.GRZHSNJZYE = GRZHSNJZYE;

    }


    public String getYJCE() {

        return this.YJCE;

    }


    public void setYJCE(String YJCE) {

        this.YJCE = YJCE;

    }


    public String getSXNY() {

        return this.SXNY;

    }


    public void setSXNY(String SXNY) {

        this.SXNY = SXNY;

    }


    public String getCZMC() {

        return this.CZMC;

    }


    public void setCZMC(String CZMC) {

        this.CZMC = CZMC;

    }


    public String getGRJCBL() {

        return this.GRJCBL;

    }


    public void setGRJCBL(String GRJCBL) {

        this.GRJCBL = GRJCBL;

    }


    public String getGRZJNY() {

        return this.GRZJNY;

    }


    public void setGRZJNY(String GRZJNY) {

        this.GRZJNY = GRZJNY;

    }


    public String getDWJCBL() {

        return this.DWJCBL;

    }


    public void setDWJCBL(String DWJCBL) {

        this.DWJCBL = DWJCBL;

    }


    public String getCZYY() {

        return this.CZYY;

    }


    public void setCZYY(String CZYY) {

        this.CZYY = CZYY;

    }


    public String getXingMing() {

        return this.XingMing;

    }


    public void setXingMing(String XingMing) {

        this.XingMing = XingMing;

    }


    public String toString() {

        return "HeadIndiAcctActionRes{" +

            "GRJCJS='" + this.GRJCJS + '\'' + "," +
            "GRYJCE='" + this.GRYJCE + '\'' + "," +
            "ZJLX='" + this.ZJLX + '\'' + "," +
            "GRZH='" + this.GRZH + '\'' + "," +
            "RiQi='" + this.RiQi + '\'' + "," +
            "JZNY='" + this.JZNY + '\'' + "," +
            "DWYJCE='" + this.DWYJCE + '\'' + "," +
            "ZJHM='" + this.ZJHM + '\'' + "," +
            "BeiZhu='" + this.BeiZhu + '\'' + "," +
            "GRZHDNGJYE='" + this.GRZHDNGJYE + '\'' + "," +
            "GRZHZT='" + this.GRZHZT + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "DJJE='" + this.DJJE + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "GRZHYE='" + this.GRZHYE + '\'' + "," +
            "GRZHSNJZYE='" + this.GRZHSNJZYE + '\'' + "," +
            "YJCE='" + this.YJCE + '\'' + "," +
            "SXNY='" + this.SXNY + '\'' + "," +
            "CZMC='" + this.CZMC + '\'' + "," +
            "GRJCBL='" + this.GRJCBL + '\'' + "," +
            "GRZJNY='" + this.GRZJNY + '\'' + "," +
            "DWJCBL='" + this.DWJCBL + '\'' + "," +
            "CZYY='" + this.CZYY + '\'' + "," +
            "XingMing='" + this.XingMing + '\'' +
            "CZY='" + this.CZY + '\'' +
            "DWJBR='" + this.DWJBR + '\'' +
            "SHR='" + this.SHR + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "}";

    }
}