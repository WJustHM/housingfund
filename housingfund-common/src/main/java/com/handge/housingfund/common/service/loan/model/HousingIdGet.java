package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingIdGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingIdGet  implements Serializable {

    private String ZCZJ;  //注册资金

    private String ZZDJ;  //资质等级（0：一级 1：二级 2：三级 3：四级）

    private String YWLSH;  //业务流水号

    private String FRDBZJLX;  //法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）

    private HousingIdGetManagerInformation managerInformation;  //经办人信息

    private String LXR;  //联系人

    // private String SFRKHYHMC;  //售房人开户银行名称

    private String LXDH;  //联系电话

    private String FRDBZJHM;  //法人代表证件号码

    private String ZZJGDM;  //组织机构代码

    private String ZCDZ;  //注册地址

    // private String SFRKHYHKHM;  //售房人开户银行开户名

    private String FRDB;  //法人代表

    // private String SFRZHHM;  //售房人账户号码

    private ArrayList<HousingCompanyInfoSale> housingCompanyInfoSales;

    private String BeiZhu;  //备注

    private String DWDZ;  //单位地址

    private String FKGS;  //房开公司

    private String SJFLB;  //售建房类别（0：开发商 1：个人 2：其他）

    private String BLZL;  //办理资料

    private String BZJZH; //保证金账户

    private String BZJZHKHH; //保证金账户开户行

    private String BZJKHM; //保证金开户名

    private String FKGSZH;//房开公司账号

    private ArrayList<String> DELTA;//变更字段

    public ArrayList<String> getDELTA() {
        return DELTA;
    }


    public void setDELTA(ArrayList<String> DELTA) {
        this.DELTA = DELTA;
    }

    public String getFKGSZH() {
        return FKGSZH;
    }

    public void setFKGSZH(String FKGSZH) {
        this.FKGSZH = FKGSZH;
    }

    public HousingIdGetManagerInformation getManagerInformation() {
        return managerInformation;
    }

    public void setManagerInformation(HousingIdGetManagerInformation managerInformation) {
        this.managerInformation = managerInformation;
    }

    public ArrayList<HousingCompanyInfoSale> getHousingCompanyInfoSales() {
        return housingCompanyInfoSales;
    }

    public void setHousingCompanyInfoSales(ArrayList<HousingCompanyInfoSale> housingCompanyInfoSales) {
        this.housingCompanyInfoSales = housingCompanyInfoSales;
    }

    public String getBZJZH() {
        return BZJZH;
    }

    public void setBZJZH(String BZJZH) {
        this.BZJZH = BZJZH;
    }

    public String getBZJKHH() {
        return BZJZHKHH;
    }

    public void setBZJKHH(String BZJKHH) {
        this.BZJZHKHH = BZJKHH;
    }

    public String getBZJKHM() {
        return BZJKHM;
    }

    public void setBZJKHM(String BZJKHM) {
        this.BZJKHM = BZJKHM;
    }

    public String getZCZJ() {

        return this.ZCZJ;

    }


    public void setZCZJ(String ZCZJ) {

        this.ZCZJ = ZCZJ;

    }



    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }








    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }





    public String getLXDH() {

        return this.LXDH;

    }


    public void setLXDH(String LXDH) {

        this.LXDH = LXDH;

    }


    public String getFRDBZJHM() {

        return this.FRDBZJHM;

    }


    public void setFRDBZJHM(String FRDBZJHM) {

        this.FRDBZJHM = FRDBZJHM;

    }


    public String getZZJGDM() {

        return this.ZZJGDM;

    }


    public void setZZJGDM(String ZZJGDM) {

        this.ZZJGDM = ZZJGDM;

    }


    public String getZCDZ() {

        return this.ZCDZ;

    }


    public void setZCDZ(String ZCDZ) {

        this.ZCDZ = ZCDZ;

    }





    public String getFRDB() {

        return this.FRDB;

    }


    public void setFRDB(String FRDB) {

        this.FRDB = FRDB;

    }





    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getDWDZ() {

        return this.DWDZ;

    }


    public void setDWDZ(String DWDZ) {

        this.DWDZ = DWDZ;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }

    public String getZZDJ() {
        return ZZDJ;
    }

    public void setZZDJ(String ZZDJ) {
        this.ZZDJ = ZZDJ;
    }

    public String getFRDBZJLX() {
        return FRDBZJLX;
    }

    public void setFRDBZJLX(String FRDBZJLX) {
        this.FRDBZJLX = FRDBZJLX;
    }

    public String getSJFLB() {
        return SJFLB;
    }

    public void setSJFLB(String SJFLB) {
        this.SJFLB = SJFLB;
    }

    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }

    @Override
    public String toString() {
        return "HousingIdGet{" +
                "ZCZJ='" + ZCZJ + '\'' +
                ", ZZDJ='" + ZZDJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", FRDBZJLX='" + FRDBZJLX + '\'' +
                ", managerInformation=" + managerInformation +
                ", LXR='" + LXR + '\'' +
                ", LXDH='" + LXDH + '\'' +
                ", FRDBZJHM='" + FRDBZJHM + '\'' +
                ", ZZJGDM='" + ZZJGDM + '\'' +
                ", ZCDZ='" + ZCDZ + '\'' +
                ", FRDB='" + FRDB + '\'' +
                ", housingCompanyInfoSales=" + housingCompanyInfoSales +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", DWDZ='" + DWDZ + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", SJFLB='" + SJFLB + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", BZJZH='" + BZJZH + '\'' +
                ", FKGSZH='" + FKGSZH + '\'' +
                ", BZJZHKHH='" + BZJZHKHH + '\'' +
                ", BZJKHM='" + BZJKHM + '\'' +
                '}';
    }
}