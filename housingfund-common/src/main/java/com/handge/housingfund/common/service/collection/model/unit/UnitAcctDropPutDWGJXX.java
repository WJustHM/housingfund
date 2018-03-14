package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "UnitAcctDropPutDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctDropPutDWGJXX  implements Serializable{

    private String FCHXHYY;  //封存或销户原因

    private String YWWD;  //业务网点 

    private String BLZL;  //办理资料

    private String CZY;  //操作员 

    private String CZLX;  //操作类型 

    private String BeiZhu;  //备注 

    public String getFCHXHYY() {

        return this.FCHXHYY;

    }


    public void setFCHXHYY(String FCHXHYY) {

        this.FCHXHYY = FCHXHYY;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getCZLX() {

        return this.CZLX;

    }


    public void setCZLX(String CZLX) {

        this.CZLX = CZLX;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String toString() {

        return "UnitAcctDropPutDWGJXX{" +

                "FCHXHYY='" + this.FCHXHYY + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "CZLX='" + this.CZLX + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' +

                "}";

    }
}