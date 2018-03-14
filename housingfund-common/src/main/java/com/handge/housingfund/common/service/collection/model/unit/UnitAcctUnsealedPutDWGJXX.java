package com.handge.housingfund.common.service.collection.model.unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "UnitAcctUnsealedPutDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctUnsealedPutDWGJXX  implements Serializable{

    private String YWWD;  //业务网点

    private String DWQFYY;  //启封原因

    private String BLZL;  //办理资料

    private String CZY;  //操作员

    private String BeiZhu;  //备注

    private String CZLX;  //操作类型

    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getDWQFYY() {

        return this.DWQFYY;

    }


    public void setDWQFYY(String DWQFYY) {

        this.DWQFYY = DWQFYY;

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


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getCZLX() {

        return this.CZLX;

    }


    public void setCZLX(String CZLX) {

        this.CZLX = CZLX;

    }


    public String toString() {

        return "UnitAcctUnsealedPutDWGJXX{" +

                "YWWD='" + this.YWWD + '\'' + "," +
                "DWQFYY='" + this.DWQFYY + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' + "," +
                "CZLX='" + this.CZLX + '\'' +

                "}";

    }
}