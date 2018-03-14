package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "PersonRadixPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRadixPut  implements Serializable{

    private static final long serialVersionUID = -5775918096302012912L;
    private String JBRZJLX;  //经办人证件类型

    private String JBRXM;  //经办人姓名

    private String FSRS;  //发生人数

    private String YWWD;  //业务网点

    private String JBRZJHM;  //经办人证件号码

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String CZLX;

    private String SXNY;

    private String DWJCBL;

    private String GRJCBL;

    private ArrayList<PersonRadixPutJCJSTZXX> JCJSTZXX;  //缴存基数调整信息

    private String BLZL;  //办理资料

    public String getSXNY() {
        return SXNY;
    }

    public void setSXNY(String SXNY) {
        this.SXNY = SXNY;
    }

    public String getJBRZJLX() {

        return this.JBRZJLX;

    }

    public String getCZLX() {
        return CZLX;
    }

    public void setCZLX(String CZLX) {
        this.CZLX = CZLX;
    }

    public void setJBRZJLX(String JBRZJLX) {

        this.JBRZJLX = JBRZJLX;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getFSRS() {

        return this.FSRS;

    }


    public void setFSRS(String FSRS) {

        this.FSRS = FSRS;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getJBRZJHM() {

        return this.JBRZJHM;

    }


    public void setJBRZJHM(String JBRZJHM) {

        this.JBRZJHM = JBRZJHM;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public ArrayList<PersonRadixPutJCJSTZXX> getJCJSTZXX() {

        return this.JCJSTZXX;

    }


    public void setJCJSTZXX(ArrayList<PersonRadixPutJCJSTZXX> JCJSTZXX) {

        this.JCJSTZXX = JCJSTZXX;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }

    public String getDWJCBL() {
        return DWJCBL;
    }

    public void setDWJCBL(String DWJCBL) {
        this.DWJCBL = DWJCBL;
    }

    public String getGRJCBL() {
        return GRJCBL;
    }

    public void setGRJCBL(String GRJCBL) {
        this.GRJCBL = GRJCBL;
    }

    public String toString() {

        return "PersonRadixPut{" +

                "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
                "JBRXM='" + this.JBRXM + '\'' + "," +
                "FSRS='" + this.FSRS + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
                "DWZH='" + this.DWZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "JCJSTZXX='" + this.JCJSTZXX + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' +

                "}";

    }
}