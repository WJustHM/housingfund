package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/8/17.
 */
@XmlRootElement(name = "GetIndiAcctsInfoDetailsResJCXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctsInfoDetailsResJCXX  implements Serializable{

    private  String  DWMC;  //单位名称

    private  String  DWZH;  //单位账号

    private  String  GRJCJS;  //个人缴存基数（元）

    private  String  GRJCBL;  //个人缴存比例

    private  String  DWJCBL;  //单位缴存比例

    private  String  GRYJCE; //个人月缴存额（元）

    private  String  DWYJCE; //单位月缴存额（元）

    private  String  YJCE; //月缴存额（元）

    private  String  DWZHZT; //单位账号状态

    private  String  JZNY; //缴至年月

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    public String getGRJCBL() {
        return GRJCBL;
    }

    public void setGRJCBL(String GRJCBL) {
        this.GRJCBL = GRJCBL;
    }

    public String getDWJCBL() {
        return DWJCBL;
    }

    public void setDWJCBL(String DWJCBL) {
        this.DWJCBL = DWJCBL;
    }

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public String getDWYJCE() {
        return DWYJCE;
    }

    public void setDWYJCE(String DWYJCE) {
        this.DWYJCE = DWYJCE;
    }

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

    public String getDWZHZT() {
        return DWZHZT;
    }

    public void setDWZHZT(String DWZHZT) {
        this.DWZHZT = DWZHZT;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String toString(){

        return "GetIndiAcctsInfoDetailsResJCXX{" +
                "DWMC='" + this.DWMC + '\'' + "," +
                "DWZH='" + this.DWZH + '\'' + "," +
                "GRJCJS='" + this.GRJCJS + '\'' + "," +
                "GRJCBL='" + this.GRJCBL + '\'' + "," +
                "DWJCBL='" + this.DWJCBL + '\'' + "," +
                "GRYJCE='" + this.GRYJCE + '\'' + "," +
                "DWYJCE='" + this.DWYJCE + '\'' + "," +
                "DWZHZT='" + this.DWZHZT + '\'' +
                "JZNY='" + this.JZNY + '\'' +
        "}";
    }

}
