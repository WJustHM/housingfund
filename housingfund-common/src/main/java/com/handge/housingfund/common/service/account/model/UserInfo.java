package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-9-5.
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 7486070975759607463L;

    private String GRZH;

    private String GRXM;

    private String DWMC;

    private String DWZH;

    private String FKGS;

    private String FKGSZH;

    private String YWWD;//业务网点ID

    private String YWWDMC;//业务网点名称

    private String CZY;

    public String getYWWDMC() {
        return YWWDMC;
    }

    public void setYWWDMC(String yWWDMC) {
        YWWDMC = yWWDMC;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String cZY) {
        CZY = cZY;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String gRZH) {
        GRZH = gRZH;
    }

    public String getGRXM() {
        return GRXM;
    }

    public void setGRXM(String gRXM) {
        GRXM = gRXM;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String dWMC) {
        DWMC = dWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String dWZH) {
        DWZH = dWZH;
    }

    public String getFKGS() {
        return FKGS;
    }

    public void setFKGS(String fKGS) {
        FKGS = fKGS;
    }

    public String getFKGSZH() {
        return FKGSZH;
    }

    public void setFKGSZH(String fKGSZH) {
        FKGSZH = fKGSZH;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String yWWD) {
        YWWD = yWWD;
    }

}
