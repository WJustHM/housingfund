package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstatePut")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstatePut  implements Serializable {

    private EstatePutLDXX LDXX;  //楼栋信息

    private String BLZL;  //办理资料

    private EstatePutLPXX LPXX;  //楼盘信息

    private String YWWD;//业务网点

    private String CZY;//操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public EstatePutLDXX getLDXX() {

        return this.LDXX;

    }


    public void setLDXX(EstatePutLDXX LDXX) {

        this.LDXX = LDXX;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    public EstatePutLPXX getLPXX() {

        return this.LPXX;

    }


    public void setLPXX(EstatePutLPXX LPXX) {

        this.LPXX = LPXX;

    }


    public String toString() {

        return "EstatePut{" +

                "LDXX='" + this.LDXX + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "LPXX='" + this.LPXX + '\'' +

                "}";

    }
}