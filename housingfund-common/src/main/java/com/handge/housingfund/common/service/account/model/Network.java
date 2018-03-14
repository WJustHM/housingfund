package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-7-15.
 */
@XmlRootElement(name = "Network")
@XmlAccessorType(XmlAccessType.FIELD)
public class Network implements Serializable {

    private String id;

    private String MingCheng;//名称

    private String BLSJ;//办理时间

    private String FZR;//负责人

    private String LXDH;//联系电话

    private String DiQu;//地区

    private String XXDZ;//详细地址

    private String JingDu;//经度

    private String WeiDu;//维度

    private String SJWD;//上级网点

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMingCheng() {
        return MingCheng;
    }

    public void setMingCheng(String mingCheng) {
        MingCheng = mingCheng;
    }

    public String getBLSJ() {
        return BLSJ;
    }

    public void setBLSJ(String BLSJ) {
        this.BLSJ = BLSJ;
    }

    public String getFZR() {
        return FZR;
    }

    public void setFZR(String FZR) {
        this.FZR = FZR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getDiQu() {
        return DiQu;
    }

    public void setDiQu(String diQu) {
        DiQu = diQu;
    }

    public String getXXDZ() {
        return XXDZ;
    }

    public void setXXDZ(String XXDZ) {
        this.XXDZ = XXDZ;
    }

    public String getJingDu() {
        return JingDu;
    }

    public void setJingDu(String jingDu) {
        JingDu = jingDu;
    }

    public String getWeiDu() {
        return WeiDu;
    }

    public void setWeiDu(String weiDu) {
        WeiDu = weiDu;
    }

    public String getSJWD() {
        return SJWD;
    }

    public void setSJWD(String SJWD) {
        this.SJWD = SJWD;
    }
}
