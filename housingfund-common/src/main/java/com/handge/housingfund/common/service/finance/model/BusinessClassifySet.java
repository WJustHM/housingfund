package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
@XmlRootElement(name = "日常业务分类管理")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessClassifySet implements Serializable {

    private String id;
    //分类名称
    private String YWMC;
    //是否日常
    private Boolean SFRC;
    //创建人
    private String CJR;
    //创建时间
    private String CJSJ;
    //动作
    private String action;
    //日常业务
    private ArrayList<BusinessSet> RCYW;

    public BusinessClassifySet() {
    }

    public BusinessClassifySet(String id, String YWMC, Boolean SFRC, String CJR, String CJSJ, String action, ArrayList<BusinessSet> RCYW) {
        this.id = id;
        this.YWMC = YWMC;
        this.SFRC = SFRC;
        this.CJR = CJR;
        this.CJSJ = CJSJ;
        this.action = action;
        this.RCYW = RCYW;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYWMC() {
        return YWMC;
    }

    public void setYWMC(String YWMC) {
        this.YWMC = YWMC;
    }

    public Boolean getSFRC() {
        return SFRC;
    }

    public void setSFRC(Boolean SFRC) {
        this.SFRC = SFRC;
    }

    public String getCJR() {
        return CJR;
    }

    public void setCJR(String CJR) {
        this.CJR = CJR;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public ArrayList<BusinessSet> getRCYW() {
        return RCYW;
    }

    public void setRCYW(ArrayList<BusinessSet> RCYW) {
        this.RCYW = RCYW;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "BusinessClassifySet{" +
                "id='" + id + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", SFRC=" + SFRC +
                ", CJR='" + CJR + '\'' +
                ", CJSJ='" + CJSJ + '\'' +
                ", action='" + action + '\'' +
                ", RCYW=" + RCYW +
                '}';
    }
}
