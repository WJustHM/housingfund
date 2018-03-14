package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadUnitPayCallReceiptResCJJL")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayCallReceiptResCJJL implements Serializable {

    private String FSE;  //发生额

    private Integer FSRS;  //发生人数

    private String ZDCJ;  //自动催缴

    private String CJR;  //催缴人

    private String YHJNY;  //应汇缴年月

    private String CJFS;  //催缴方式

    private String CJSJ;  //催缴时间

    private String CZY;   //操作员


    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }


    public String getFSE() {

        return this.FSE;

    }


    public void setFSE(String FSE) {

        this.FSE = FSE;

    }


    public Integer getFSRS() {

        return this.FSRS;

    }


    public void setFSRS(Integer FSRS) {

        this.FSRS = FSRS;

    }


    public String getZDCJ() {

        return this.ZDCJ;

    }


    public void setZDCJ(String ZDCJ) {

        this.ZDCJ = ZDCJ;

    }


    public String getCJR() {

        return this.CJR;

    }


    public void setCJR(String CJR) {

        this.CJR = CJR;

    }


    public String getYHJNY() {

        return this.YHJNY;

    }


    public void setYHJNY(String YHJNY) {

        this.YHJNY = YHJNY;

    }


    public String getCJFS() {

        return this.CJFS;

    }


    public void setCJFS(String CJFS) {

        this.CJFS = CJFS;

    }


    public String getCJSJ() {

        return this.CJSJ;

    }


    public void setCJSJ(String CJSJ) {

        this.CJSJ = CJSJ;

    }


    public String toString() {

        return "HeadUnitPayCallReceiptResCJJL{" +

            "FSE='" + this.FSE + '\'' + "," +
            "FSRS='" + this.FSRS + '\'' + "," +
            "ZDCJ='" + this.ZDCJ + '\'' + "," +
            "CJR='" + this.CJR + '\'' + "," +
            "YHJNY='" + this.YHJNY + '\'' + "," +
            "CJFS='" + this.CJFS + '\'' + "," +
            "CJSJ='" + this.CJSJ + '\'' +
            "CZY='" + this.CZY + '\'' +
            "}";

    }
}