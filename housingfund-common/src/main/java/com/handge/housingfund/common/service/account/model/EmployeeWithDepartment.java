package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/7/31.
 */
@XmlRootElement(name = "EmployeeWithDepartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeWithDepartment implements Serializable {

    private String id;

    private String xingMing;

    private String zhangHao;

    private String xingBie;

    private String yhtx;

    private Date rzsj;

    private String xueLi;

    private String youXiang;

    private String lxdh;

    private String qqh;

    private String wxh;

    private Department cAccountDepartment;

    private Network cAccountNetwork;

    protected Date created_at;

    protected Date updated_at;

    protected Date deleted_at;

    protected boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXingMing() {
        return xingMing;
    }

    public void setXingMing(String xingMing) {
        this.xingMing = xingMing;
    }

    public String getZhangHao() {
        return zhangHao;
    }

    public void setZhangHao(String zhangHao) {
        this.zhangHao = zhangHao;
    }

    public String getXingBie() {
        return xingBie;
    }

    public void setXingBie(String xingBie) {
        this.xingBie = xingBie;
    }

    public String getYhtx() {
        return yhtx;
    }

    public void setYhtx(String yhtx) {
        this.yhtx = yhtx;
    }

    public Date getRzsj() {
        return rzsj;
    }

    public void setRzsj(Date rzsj) {
        this.rzsj = rzsj;
    }

    public String getXueLi() {
        return xueLi;
    }

    public void setXueLi(String xueLi) {
        this.xueLi = xueLi;
    }

    public String getYouXiang() {
        return youXiang;
    }

    public void setYouXiang(String youXiang) {
        this.youXiang = youXiang;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getQqh() {
        return qqh;
    }

    public void setQqh(String qqh) {
        this.qqh = qqh;
    }

    public String getWxh() {
        return wxh;
    }

    public void setWxh(String wxh) {
        this.wxh = wxh;
    }

    public Department getcAccountDepartment() {
        return cAccountDepartment;
    }

    public void setcAccountDepartment(Department cAccountDepartment) {
        this.cAccountDepartment = cAccountDepartment;
    }

    public Network getcAccountNetwork() {
        return cAccountNetwork;
    }

    public void setcAccountNetwork(Network cAccountNetwork) {
        this.cAccountNetwork = cAccountNetwork;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
