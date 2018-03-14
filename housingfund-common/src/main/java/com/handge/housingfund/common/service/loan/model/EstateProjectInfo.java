package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectInfo  implements Serializable {

    private EstateProjectInfoLDXX LDXX;  //楼栋信息

    private String BLZL;  //办理资料

    private String CZY;   //操作员

    private String YWWD; //业务网点

    private String BEIZHU; //备注

    public String getBEIZHU() {
        return BEIZHU;
    }

    public void setBEIZHU(String BEIZHU) {
        this.BEIZHU = BEIZHU;
    }

    private EstateProjectInfoLPXX LPXX;  //楼盘信息

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



    public EstateProjectInfoLDXX getLDXX() {

        return this.LDXX;

    }


    public void setLDXX(EstateProjectInfoLDXX LDXX) {

        this.LDXX = LDXX;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    public EstateProjectInfoLPXX getLPXX() {

        return this.LPXX;

    }


    public void setLPXX(EstateProjectInfoLPXX LPXX) {

        this.LPXX = LPXX;

    }


    public String toString() {

        return "EstateProjectInfo{" +

                "LDXX='" + this.LDXX + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "LPXX='" + this.LPXX + '\'' +

                "}";

    }
}