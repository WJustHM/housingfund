package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyi on 2017/12/10.
 */
@XmlRootElement(name = "对账结果")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReconciliationModel implements Serializable {

    private static final long serialVersionUID = 4540213809993933068L;

    private String SSX;  //试算项

    private String SSJG; //试算结果

    private boolean SFPH;//是否平衡

    private List<String> Msgs;//错误信息

    public ReconciliationModel() {
    }

    public ReconciliationModel(String SSX, String SSJG, boolean SFPH, List<String> msgs) {
        this.SSX = SSX;
        this.SSJG = SSJG;
        this.SFPH = SFPH;
        Msgs = msgs;
    }

    public String getSSX() {
        return SSX;
    }

    public void setSSX(String SSX) {
        this.SSX = SSX;
    }

    public String getSSJG() {
        return SSJG;
    }

    public void setSSJG(String SSJG) {
        this.SSJG = SSJG;
    }

    public boolean isSFPH() {
        return SFPH;
    }

    public void setSFPH(boolean SFPH) {
        this.SFPH = SFPH;
    }

    public List<String> getMsgs() {
        return Msgs;
    }

    public void setMsgs(List<String> msgs) {
        Msgs = msgs;
    }

    @Override
    public String toString() {
        return "ReconciliationModel{" +
                "SSX='" + SSX + '\'' +
                ", SSJG='" + SSJG + '\'' +
                ", SFPH=" + SFPH +
                ", Msgs=" + Msgs +
                '}';
    }
}
