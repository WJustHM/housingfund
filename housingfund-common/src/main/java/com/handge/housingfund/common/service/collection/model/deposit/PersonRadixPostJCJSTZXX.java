package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "PersonRadixPostJCJSTZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRadixPostJCJSTZXX implements Serializable {


    private static final long serialVersionUID = 1742503573580242086L;
    private String TZHGRJCJS;  //调整后个人缴存基数

    private String TZQGRJCJS;  //调整前个人缴存基数

    private String GRZH;  //个人账号

    private String DWYJCE;

    private String GRYJCE;

    private String YCJE;

    private String ZJHM;

    @Override
    public String toString() {
        return "PersonRadixPostJCJSTZXX{" +
                "TZHGRJCJS='" + TZHGRJCJS + '\'' +
                ", TZQGRJCJS='" + TZQGRJCJS + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", DWYJCE='" + DWYJCE + '\'' +
                ", GRYJCE='" + GRYJCE + '\'' +
                ", YCJE='" + YCJE + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                '}';
    }

    public String getDWYJCE() {
        return DWYJCE;
    }

    public void setDWYJCE(String DWYJCE) {
        this.DWYJCE = DWYJCE;
    }

    public String getYCJE() {
        return YCJE;
    }

    public void setYCJE(String YCJE) {
        this.YCJE = YCJE;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public String getTZHGRJCJS() {

        return this.TZHGRJCJS;

    }


    public void setTZHGRJCJS(String TZHGRJCJS) {

        this.TZHGRJCJS = TZHGRJCJS;

    }


    public String getTZQGRJCJS() {

        return this.TZQGRJCJS;

    }


    public void setTZQGRJCJS(String TZQGRJCJS) {

        this.TZQGRJCJS = TZQGRJCJS;

    }


    public String getGRZH() {

        return this.GRZH;

    }


    public void setGRZH(String GRZH) {

        this.GRZH = GRZH;

    }


}