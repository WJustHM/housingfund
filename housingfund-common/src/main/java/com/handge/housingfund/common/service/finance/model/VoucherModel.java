package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-15.
 */
@XmlRootElement(name = "业务凭据")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherModel implements Serializable {

    private String id;
    //业务名称
    private String YWMC;
    //业务名称ID
    private String YWMCID;
    //模板编号
    private String MBBH;
    //借方科目([id:123,KMMC:name,KMBH:科目编号,KMYEFX:科目余额方向])
    private String JFKM;
    //贷方科目([id:123,KMMC:name,KMBH:科目编号,KMYEFX:科目余额方向])
    private String DFKM;
    //备注
    private String BEIZHU;
    //创建人
    private String CJR;
    //创建时间
    private String CJSJ;
    //是否已使用
    private boolean SFYSY;

    public VoucherModel() {
    }

    public VoucherModel(String id, String YWMC, String YWMCID, String MBBH, String JFKM, String DFKM, String BEIZHU, String CJR, String CJSJ, boolean SFYSY) {
        this.id = id;
        this.YWMC = YWMC;
        this.YWMCID = YWMCID;
        this.MBBH = MBBH;
        this.JFKM = JFKM;
        this.DFKM = DFKM;
        this.BEIZHU = BEIZHU;
        this.CJR = CJR;
        this.CJSJ = CJSJ;
        this.SFYSY = SFYSY;
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

    public String getYWMCID() {
        return YWMCID;
    }

    public void setYWMCID(String YWMCID) {
        this.YWMCID = YWMCID;
    }

    public String getMBBH() {
        return MBBH;
    }

    public void setMBBH(String MBBH) {
        this.MBBH = MBBH;
    }

    public String getJFKM() {
        return JFKM;
    }

    public void setJFKM(String JFKM) {
        this.JFKM = JFKM;
    }

    public String getDFKM() {
        return DFKM;
    }

    public void setDFKM(String DFKM) {
        this.DFKM = DFKM;
    }

    public String getBEIZHU() {
        return BEIZHU;
    }

    public void setBEIZHU(String BEIZHU) {
        this.BEIZHU = BEIZHU;
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

    public boolean isSFYSY() {
        return SFYSY;
    }

    public void setSFYSY(boolean SFYSY) {
        this.SFYSY = SFYSY;
    }

    @Override
    public String toString() {
        return "VoucherModel{" +
                "id='" + id + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", YWMCID='" + YWMCID + '\'' +
                ", MBBH='" + MBBH + '\'' +
                ", JFKM='" + JFKM + '\'' +
                ", DFKM='" + DFKM + '\'' +
                ", BEIZHU='" + BEIZHU + '\'' +
                ", CJR='" + CJR + '\'' +
                ", CJSJ='" + CJSJ + '\'' +
                ", SFYSY=" + SFYSY +
                '}';
    }
}
