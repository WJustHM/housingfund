package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanyi on 2017/9/30.
 */
@XmlRootElement(name = "会计科目")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectBaseModel implements Serializable {

    private static final long serialVersionUID = 7913979159893387327L;
    private String id;
    //科目编号
    private String KMBH;

    //科目名称
    private String KMMC;

    //上级科目
    private String SJKM;

    //下级会计科目
    private ArrayList<SubjectBaseModel> subjectBaseModels;


    public SubjectBaseModel() {
    }

    public SubjectBaseModel(String KMBH, String KMMC, String SJKM, ArrayList<SubjectBaseModel> subjectBaseModels) {
        this.KMBH = KMBH;
        this.KMMC = KMMC;
        this.SJKM = SJKM;
        this.subjectBaseModels = subjectBaseModels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKMBH() {
        return KMBH;
    }

    public void setKMBH(String KMBH) {
        this.KMBH = KMBH;
    }

    public String getKMMC() {
        return KMMC;
    }

    public void setKMMC(String KMMC) {
        this.KMMC = KMMC;
    }

    public String getSJKM() {
        return SJKM;
    }

    public void setSJKM(String SJKM) {
        this.SJKM = SJKM;
    }

    public ArrayList<SubjectBaseModel> getSubjectBaseModels() {
        return subjectBaseModels;
    }

    public void setSubjectBaseModels(ArrayList<SubjectBaseModel> subjectBaseModels) {
        this.subjectBaseModels = subjectBaseModels;
    }

    @Override
    public String toString() {
        return "SubjectBaseModel{" +
                "id='" + id + '\'' +
                ", KMBH='" + KMBH + '\'' +
                ", KMMC='" + KMMC + '\'' +
                ", SJKM='" + SJKM + '\'' +
                ", subjectBaseModels=" + subjectBaseModels +
                '}';
    }
}
