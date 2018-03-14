package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/10.
 */
@XmlRootElement(name = "RepaymentGetInfomationList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentGetInfomationList  implements Serializable {
    private static final long serialVersionUID = 5570361633850097547L;
    private String YQQC;
    private String YQBJ;
    private String YQLX;
    private String YQFX;

    public String getYQQC() {
        return YQQC;
    }

    public void setYQQC(String YQQC) {
        this.YQQC = YQQC;
    }

    public String getYQBJ() {
        return YQBJ;
    }

    public void setYQBJ(String YQBJ) {
        this.YQBJ = YQBJ;
    }

    public String getYQLX() {
        return YQLX;
    }

    public void setYQLX(String YQLX) {
        this.YQLX = YQLX;
    }

    public String getYQFX() {
        return YQFX;
    }

    public void setYQFX(String YQFX) {
        this.YQFX = YQFX;
    }

}
