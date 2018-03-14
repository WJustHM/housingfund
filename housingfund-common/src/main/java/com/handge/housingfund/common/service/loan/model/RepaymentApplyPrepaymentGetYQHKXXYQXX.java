package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentGetYQHKXXYQXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentGetYQHKXXYQXX  implements Serializable {

    private String YQBJ;  //逾期本金（元）

    private String YQLX;  //逾期利息（元）

    private String YQFX;  //逾期罚息（元）

    private String XZQC;  //选择期次

    private String YQKZJ;  //逾期期次

    public String getYQBJ() {

        return this.YQBJ;

    }


    public void setYQBJ(String YQBJ) {

        this.YQBJ = YQBJ;

    }


    public String getYQLX() {

        return this.YQLX;

    }


    public void setYQLX(String YQLX) {

        this.YQLX = YQLX;

    }


    public String getYQFX() {

        return this.YQFX;

    }


    public void setYQFX(String YQFX) {

        this.YQFX = YQFX;

    }


    public String getXZQC() {

        return this.XZQC;

    }


    public void setXZQC(String XZQC) {

        this.XZQC = XZQC;

    }


    public String getYQKZJ() {

        return this.YQKZJ;

    }


    public void setYQKZJ(String YQKZJ) {

        this.YQKZJ = YQKZJ;

    }


    public String toString() {

        return "RepaymentApplyPrepaymentGetYQHKXXYQXX{" +

                "YQBJ='" + this.YQBJ + '\'' + "," +
                "YQLX='" + this.YQLX + '\'' + "," +
                "YQFX='" + this.YQFX + '\'' + "," +
                "XZQC='" + this.XZQC + '\'' + "," +
                "YQKZJ='" + this.YQKZJ + '\'' +

                "}";

    }
}