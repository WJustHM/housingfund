package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "PersonRadixPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRadixPost implements Serializable {


    private static final long serialVersionUID = -7921818487286471981L;
    private String JBRZJLX;  //经办人证件类型

    private String FSRS;  //调整人数

    private String JBRXM;  //经办人姓名

    private String YWWD;  //业务网点

    private String JBRZJHM;  //经办人证件号码

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String CZLX;//操作类型

    private String SXNY;

    private String DWJCBL;

    private String GRJCBL;

    private ArrayList<PersonRadixPostJCJSTZXX> JCJSTZXX;  //缴存基数调整信息

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

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

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


    public ArrayList<PersonRadixPostJCJSTZXX> getJCJSTZXX() {

        return this.JCJSTZXX;

    }


    public void setJCJSTZXX(ArrayList<PersonRadixPostJCJSTZXX> JCJSTZXX) {

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
}