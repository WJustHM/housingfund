package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/28.
 */
@XmlRootElement(name = "日常业务分类管理")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessSet implements Serializable {

    private static final long serialVersionUID = -4179598046487702522L;
    private String id;
    //业务名称
    private String YWMC;
    //创建人
    private String CJR;
    //创建时间
    private String CJSJ;
    //动作
    private String action;
    //所属分类
    private BusinessClassifySet SSFL;
    //资金业务类型编码
    private String ZJYWLXBM;
    //资金业务类型
    private String ZJYWLX;
    //是否已使用
    private boolean SFYSY;
    //是否默认
    private boolean SFMR;

    public BusinessSet() {
    }

    public BusinessSet(String id, String YWMC, String CJR, String CJSJ, String action, BusinessClassifySet SSFL, String ZJYWLXBM, String ZJYWLX,boolean SFYSY,boolean SFMR) {
        this.id = id;
        this.YWMC = YWMC;
        this.CJR = CJR;
        this.CJSJ = CJSJ;
        this.action = action;
        this.SSFL = SSFL;
        this.ZJYWLXBM = ZJYWLXBM;
        this.ZJYWLX = ZJYWLX;
        this.SFYSY = SFYSY;
        this.SFMR = SFMR;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BusinessClassifySet getSSFL() {
        return SSFL;
    }

    public void setSSFL(BusinessClassifySet SSFL) {
        this.SSFL = SSFL;
    }

    public String getZJYWLXBM() {
        return ZJYWLXBM;
    }

    public void setZJYWLXBM(String ZJYWLXBM) {
        this.ZJYWLXBM = ZJYWLXBM;
    }

    public String getZJYWLX() {
        return ZJYWLX;
    }

    public void setZJYWLX(String ZJYWLX) {
        this.ZJYWLX = ZJYWLX;
    }

    public boolean isSFYSY() {
        return SFYSY;
    }

    public void setSFYSY(boolean SFYSY) {
        this.SFYSY = SFYSY;
    }

    public boolean isSFMR() {
        return SFMR;
    }

    public void setSFMR(boolean SFMR) {
        this.SFMR = SFMR;
    }

    @Override
    public String toString() {
        return "BusinessSet{" +
                "id='" + id + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", CJR='" + CJR + '\'' +
                ", CJSJ='" + CJSJ + '\'' +
                ", action='" + action + '\'' +
                ", SSFL=" + SSFL +
                ", ZJYWLXBM='" + ZJYWLXBM + '\'' +
                ", ZJYWLX='" + ZJYWLX + '\'' +
                ", SFYSY=" + SFYSY +
                ", SFMR=" + SFMR +
                '}';
    }
}
