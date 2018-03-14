package com.handge.housingfund.common.service.ca.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/6/29.
 */
@XmlRootElement
public class UnitInfo implements Serializable {
    /**
     * 登记日期
     */
    @XmlElement
    public String dwdjrq;
    /**
     * 单位公积金账号
     */
    @XmlElement
    public String dwgjjzh = "";
    /**
     * 单位名称
     */
    @XmlElement
    public String dwmc = "";
    /**
     * 单位地址
     */
    @XmlElement
    public String dwdz;
    /**
     * 单位组织机构代码
     */
    @XmlElement
    public String dwzzjgdm = "";
    /**
     * 专办员姓名
     */
    @XmlElement
    public String dwlxrxm = "";
    /**
     * 专办员身份证号码
     */
    @XmlElement
    public String dwlxrzjhm = "";
    /**
     * 专办员电话（手机号码）
     */
    @XmlElement
    public String dwlxrdh;

    public UnitInfo() {
    }

    public UnitInfo(String dwdjrq, String dwgjjzh, String dwmc, String dwdz, String dwzzjgdm, String dwlxrxm, String dwlxrzjhm, String dwlxrdh) {
        this.dwdjrq = dwdjrq;
        this.dwgjjzh = dwgjjzh;
        this.dwmc = dwmc;
        this.dwdz = dwdz;
        this.dwzzjgdm = dwzzjgdm;
        this.dwlxrxm = dwlxrxm;
        this.dwlxrzjhm = dwlxrzjhm;
        this.dwlxrdh = dwlxrdh;
    }

    /**
     * @return 登记日期
     */
    @XmlTransient
    public String getDwdjrq() {
        return dwdjrq;
    }

    /**
     * @param dwdjrq 登记日期
     */
    public void setDwdjrq(String dwdjrq) {
        this.dwdjrq = dwdjrq;
    }

    /**
     * @return 单位公积金账号
     */
    @XmlTransient
    public String getDwgjjzh() {
        return dwgjjzh;
    }

    /**
     * @param dwgjjzh 单位公积金账号
     */
    public void setDwgjjzh(String dwgjjzh) {
        this.dwgjjzh = dwgjjzh;
    }

    /**
     * @return 单位名称
     */
    @XmlTransient
    public String getDwmc() {
        return dwmc;
    }

    /**
     * @param dwmc 单位名称
     */
    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    /**
     * @return 单位地址
     */
    @XmlTransient
    public String getDwdz() {
        return dwdz;
    }

    /**
     * @param dwdz 单位地址
     */
    public void setDwdz(String dwdz) {
        this.dwdz = dwdz;
    }

    /**
     * @return 单位组织机构代码
     */
    @XmlTransient
    public String getDwzzjgdm() {
        return dwzzjgdm;
    }

    /**
     * @param dwzzjgdm 单位组织机构代码
     */
    public void setDwzzjgdm(String dwzzjgdm) {
        this.dwzzjgdm = dwzzjgdm;
    }

    /**
     * @return 专办员姓名
     */
    @XmlTransient
    public String getDwlxrxm() {
        return dwlxrxm;
    }

    /**
     * @param dwlxrxm 专办员姓名
     */
    public void setDwlxrxm(String dwlxrxm) {
        this.dwlxrxm = dwlxrxm;
    }

    /**
     * @return 专办员身份证号码
     */
    @XmlTransient
    public String getDwlxrzjhm() {
        return dwlxrzjhm;
    }

    /**
     * @param dwlxrzjhm 专办员身份证号码
     */
    public void setDwlxrzjhm(String dwlxrzjhm) {
        this.dwlxrzjhm = dwlxrzjhm;
    }

    /**
     * @return 专办员电话
     */
    @XmlTransient
    public String getDwlxrdh() {
        return dwlxrdh;
    }

    /**
     * @param dwlxrdh 专办员电话
     */
    public void setDwlxrdh(String dwlxrdh) {
        this.dwlxrdh = dwlxrdh;
    }

    @Override
    public String toString() {
        return "UnitInfo{" +
                "dwdjrq='" + dwdjrq + '\'' +
                ", dwgjjzh='" + dwgjjzh + '\'' +
                ", dwmc='" + dwmc + '\'' +
                ", dwdz='" + dwdz + '\'' +
                ", dwzzjgdm='" + dwzzjgdm + '\'' +
                ", dwlxrxm='" + dwlxrxm + '\'' +
                ", dwlxrzjhm='" + dwlxrzjhm + '\'' +
                ", dwlxrdh='" + dwlxrdh + '\'' +
                '}';
    }
}
