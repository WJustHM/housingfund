package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentCalculationPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentCalculationPost  implements Serializable {

    private String JSFS;  //计算方式

    private String GJJ;  //公积金

    private RepaymentCalculationPostAJCSJS AJCSJS;  //按揭成数计算

    private String SYX;  //商业性

    private String SYLL;  //商业利率

    private String GJJLL;  //公积金利率

    private String DKLB;  //贷款类别

    private RepaymentCalculationPostDKZEJS DKZEJS;  //贷款总额计算

    private String AJNX;  //按揭年数

    private String LL;  //利率

    private String DKHKFS;  //贷款还款方式

    public String getJSFS() {

        return this.JSFS;

    }


    public void setJSFS(String JSFS) {

        this.JSFS = JSFS;

    }


    public String getGJJ() {

        return this.GJJ;

    }


    public void setGJJ(String GJJ) {

        this.GJJ = GJJ;

    }


    public RepaymentCalculationPostAJCSJS getAJCSJS() {

        return this.AJCSJS;

    }


    public void setAJCSJS(RepaymentCalculationPostAJCSJS AJCSJS) {

        this.AJCSJS = AJCSJS;

    }


    public String getSYX() {

        return this.SYX;

    }


    public void setSYX(String SYX) {

        this.SYX = SYX;

    }


    public String getSYLL() {

        return this.SYLL;

    }


    public void setSYLL(String SYLL) {

        this.SYLL = SYLL;

    }


    public String getGJJLL() {

        return this.GJJLL;

    }


    public void setGJJLL(String GJJLL) {

        this.GJJLL = GJJLL;

    }


    public String getDKLB() {

        return this.DKLB;

    }


    public void setDKLB(String DKLB) {

        this.DKLB = DKLB;

    }


    public RepaymentCalculationPostDKZEJS getDKZEJS() {

        return this.DKZEJS;

    }


    public void setDKZEJS(RepaymentCalculationPostDKZEJS DKZEJS) {

        this.DKZEJS = DKZEJS;

    }


    public String getAJNX() {

        return this.AJNX;

    }


    public void setAJNX(String AJNX) {

        this.AJNX = AJNX;

    }


    public String getLL() {

        return this.LL;

    }


    public void setLL(String LL) {

        this.LL = LL;

    }


    public String getDKHKFS() {

        return this.DKHKFS;

    }


    public void setDKHKFS(String DKHKFS) {

        this.DKHKFS = DKHKFS;

    }


    public String toString() {

        return "RepaymentCalculationPost{" +

                "JSFS='" + this.JSFS + '\'' + "," +
                "GJJ='" + this.GJJ + '\'' + "," +
                "AJCSJS='" + this.AJCSJS + '\'' + "," +
                "SYX='" + this.SYX + '\'' + "," +
                "SYLL='" + this.SYLL + '\'' + "," +
                "GJJLL='" + this.GJJLL + '\'' + "," +
                "DKLB='" + this.DKLB + '\'' + "," +
                "DKZEJS='" + this.DKZEJS + '\'' + "," +
                "AJNX='" + this.AJNX + '\'' + "," +
                "LL='" + this.LL + '\'' + "," +
                "DKHKFS='" + this.DKHKFS + '\'' +

                "}";

    }
}