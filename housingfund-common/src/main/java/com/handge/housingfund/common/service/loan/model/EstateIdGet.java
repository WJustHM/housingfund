package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateIdGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateIdGet  implements Serializable {

    private String ID;   //回执单Id

    private String CZY;   //操作员

    private String BLR;  //办理人

    private String YWWD; //业务网点

    private String SHR;  // 审核人

    private String TZRQ;  //填制日期

    private String YZM;  //验证码

    private String BLRQZ;  //办理人签字

    private String JZZJE;  //建筑总金额

    private String HQTDDJ;  //获取土地单价

    private String YWLSH;  //业务流水号

    private EstateIdGetManagerInformation managerInformation;  //经办人信息

    private String YSXKZH;  //预售许可证号

    private String LXR;  //联系人

    private String LXDH;  //联系电话

    private EstateIdGetLDXX LDXX;  //楼栋信息

    private String HQTDZJ;  //获取土地总价

    private String AJXYRQ;  //按揭协议日期

    private String BZJBL;  //保证金比例

    private String LPMC;  //楼盘名称

    private String LPBH; // 楼盘编号

    private String BeiZhu;  //备注

    private String FKGS;  //房开公司

    private String LPDZ;  //楼盘地址

    private String JZZMJ;  //建筑总面积

    private String BLZL;  //办理资料

    private ArrayList<String> DELTA;//变更字段

    public ArrayList<String> getDELTA() {
        return DELTA;
    }

    public void setDELTA(ArrayList<String> DELTA) {
        this.DELTA = DELTA;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getBLR() {
        return BLR;
    }

    public void setBLR(String BLR) {
        this.BLR = BLR;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getYZM() {
        return YZM;
    }

    public void setYZM(String YZM) {
        this.YZM = YZM;
    }

    public String getBLRQZ() {
        return BLRQZ;
    }

    public void setBLRQZ(String BLRQZ) {
        this.BLRQZ = BLRQZ;
    }

    public EstateIdGetManagerInformation getManagerInformation() {
        return managerInformation;
    }

    public void setManagerInformation(EstateIdGetManagerInformation managerInformation) {
        this.managerInformation = managerInformation;
    }

    public String getLPBH() {
        return LPBH;
    }

    public void setLPBH(String LPBH) {
        this.LPBH = LPBH;
    }

    public String getJZZJE() {

        return this.JZZJE;

    }


    public void setJZZJE(String JZZJE) {

        this.JZZJE = JZZJE;

    }


    public String getHQTDDJ() {

        return this.HQTDDJ;

    }


    public void setHQTDDJ(String HQTDDJ) {

        this.HQTDDJ = HQTDDJ;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public EstateIdGetManagerInformation getmanagerInformation() {

        return this.managerInformation;

    }


    public void setmanagerInformation(EstateIdGetManagerInformation managerInformation) {

        this.managerInformation = managerInformation;

    }


    public String getYSXKZH() {

        return this.YSXKZH;

    }


    public void setYSXKZH(String YSXKZH) {

        this.YSXKZH = YSXKZH;

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


    public EstateIdGetLDXX getLDXX() {

        return this.LDXX;

    }


    public void setLDXX(EstateIdGetLDXX LDXX) {

        this.LDXX = LDXX;

    }


    public String getHQTDZJ() {

        return this.HQTDZJ;

    }


    public void setHQTDZJ(String HQTDZJ) {

        this.HQTDZJ = HQTDZJ;

    }


    public String getAJXYRQ() {

        return this.AJXYRQ;

    }


    public void setAJXYRQ(String AJXYRQ) {

        this.AJXYRQ = AJXYRQ;

    }


    public String getBZJBL() {

        return this.BZJBL;

    }


    public void setBZJBL(String BZJBL) {

        this.BZJBL = BZJBL;

    }


    public String getLPMC() {

        return this.LPMC;

    }


    public void setLPMC(String LPMC) {

        this.LPMC = LPMC;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }


    public String getLPDZ() {

        return this.LPDZ;

    }


    public void setLPDZ(String LPDZ) {

        this.LPDZ = LPDZ;

    }


    public String getJZZMJ() {

        return this.JZZMJ;

    }


    public void setJZZMJ(String JZZMJ) {

        this.JZZMJ = JZZMJ;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    @Override
    public String toString() {
        return "EstateIdGet{" +
                "ID='" + ID + '\'' +
                "CZY='" + CZY + '\'' +
                ", BLR='" + BLR + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SHR='" + SHR + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", YZM='" + YZM + '\'' +
                ", BLRQZ='" + BLRQZ + '\'' +
                ", JZZJE='" + JZZJE + '\'' +
                ", HQTDDJ='" + HQTDDJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", managerInformation=" + managerInformation +
                ", YSXKZH='" + YSXKZH + '\'' +
                ", LXR='" + LXR + '\'' +
                ", LXDH='" + LXDH + '\'' +
                ", LDXX=" + LDXX +
                ", HQTDZJ='" + HQTDZJ + '\'' +
                ", AJXYRQ='" + AJXYRQ + '\'' +
                ", BZJBL='" + BZJBL + '\'' +
                ", LPMC='" + LPMC + '\'' +
                ", LPBH='" + LPBH + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", LPDZ='" + LPDZ + '\'' +
                ", JZZMJ='" + JZZMJ + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}