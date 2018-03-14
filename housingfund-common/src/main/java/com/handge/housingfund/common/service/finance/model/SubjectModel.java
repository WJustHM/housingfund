package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-8-15.
 */
@XmlRootElement(name = "会计科目")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectModel extends SubjectBaseModel implements Serializable {

    private static final long serialVersionUID = -5806919625387064016L;

    //科目属性
    private String KMSX;

    //自定义科目属性
    private String TYPE;

    //余款流向
    private String KMYEFX;

    //科目级别
    private int KMJB;

    //科目性质
    private String KMXZ;

    //科目简码
    private String KMJM;

    //赤字控制
    private String CZKZ;

    //备注
    private String BEIZHU;

    //是否已使用
    private boolean SFYSY;

    //是否默认
    private boolean SFMR;

    public SubjectModel() {
    }

    public SubjectModel(String KMBH, String KMMC, String SJKM, ArrayList<SubjectBaseModel> subjectBaseModels,
                        String KMSX, String TYPE, String KMYEFX, int KMJB, String KMXZ, String KMJM,
                        String CZKZ, String BEIZHU, boolean SFYSY, boolean SFMR) {
        super(KMBH, KMMC, SJKM, subjectBaseModels);
        this.KMSX = KMSX;
        this.TYPE = TYPE;
        this.KMYEFX = KMYEFX;
        this.KMJB = KMJB;
        this.KMXZ = KMXZ;
        this.KMJM = KMJM;
        this.CZKZ = CZKZ;
        this.BEIZHU = BEIZHU;
        this.SFYSY = SFYSY;
        this.SFMR = SFMR;
    }

    public String getKMSX() {
        return KMSX;
    }

    public void setKMSX(String KMSX) {
        this.KMSX = KMSX;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getKMYEFX() {
        return KMYEFX;
    }

    public void setKMYEFX(String KMYEFX) {
        this.KMYEFX = KMYEFX;
    }

    public int getKMJB() {
        return KMJB;
    }

    public void setKMJB(int KMJB) {
        this.KMJB = KMJB;
    }

    public String getKMXZ() {
        return KMXZ;
    }

    public void setKMXZ(String KMXZ) {
        this.KMXZ = KMXZ;
    }

    public String getKMJM() {
        return KMJM;
    }

    public void setKMJM(String KMJM) {
        this.KMJM = KMJM;
    }

    public String getCZKZ() {
        return CZKZ;
    }

    public void setCZKZ(String CZKZ) {
        this.CZKZ = CZKZ;
    }

    public String getBEIZHU() {
        return BEIZHU;
    }

    public void setBEIZHU(String BEIZHU) {
        this.BEIZHU = BEIZHU;
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
        return "SubjectModel{" +
                "KMSX='" + KMSX + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", KMYEFX='" + KMYEFX + '\'' +
                ", KMJB=" + KMJB +
                ", KMXZ='" + KMXZ + '\'' +
                ", KMJM='" + KMJM + '\'' +
                ", CZKZ='" + CZKZ + '\'' +
                ", BEIZHU='" + BEIZHU + '\'' +
                ", SFYSY=" + SFYSY +
                ", SFMR=" + SFMR +
                '}';
    }
}
