package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentPostYQHKXXYQXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentPostYQHKXXYQXX  implements Serializable {

    private static final long serialVersionUID = -1854416475774314009L;

    private String YQBJ;  //逾期本金（元）

    private String YQLX;  //逾期利息（元）

    private String YQFX;  //逾期罚息（元）

    private String YQQC;  //逾期期次

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

    public String getYQQC() {
        return YQQC;
    }

    public void setYQQC(String YQQC) {
        this.YQQC = YQQC;
    }

    @Override
    public String toString() {
        return "RepaymentApplyPrepaymentPostYQHKXXYQXX{" +
                "YQBJ='" + YQBJ + '\'' +
                ", YQLX='" + YQLX + '\'' +
                ", YQFX='" + YQFX + '\'' +
                ", YQQC='" + YQQC + '\'' +
                '}';
    }
}