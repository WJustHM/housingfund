package com.handge.housingfund.common.service.others.model;

import java.io.Serializable;

/**
 * Created by tanyi on 2017/8/15.
 */
public class Review implements Serializable {

    private static final long serialVersionUID = -2920809514268554357L;

    private String action = "";//操作

    private String CZY = "";//操作员及职务

    private String YWWD = "";//业务网点

    private String CZQD = "";//操作渠道

    private String SLRYJ = "";//受理人意见

    private String SLR = "";//受理人

    private String SPSJ = "";//审批时间

    private int type = 0;//审批级别（0：新建，1：一级审批，2：二级审批，3：三级审批）

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getCZQD() {
        return CZQD;
    }

    public void setCZQD(String CZQD) {
        this.CZQD = CZQD;
    }

    public String getSLRYJ() {
        return SLRYJ;
    }

    public void setSLRYJ(String SLRYJ) {
        this.SLRYJ = SLRYJ;
    }

    public String getSLR() {
        return SLR;
    }

    public void setSLR(String SLR) {
        this.SLR = SLR;
    }

    public String getSPSJ() {
        return SPSJ;
    }

    public void setSPSJ(String SPSJ) {
        this.SPSJ = SPSJ;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
