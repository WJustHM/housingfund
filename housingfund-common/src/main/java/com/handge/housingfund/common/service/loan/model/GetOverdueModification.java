package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/9/20.
 */
@XmlRootElement(name = "GetOverdueModification")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetOverdueModification implements Serializable {

    private static final long serialVersionUID = -2715517715044705379L;
    private  String JKRXM;
    private  String YQKZJ;
    private ArrayList<GetOverdueModificationList> getOverdueModificationList;

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getYQKZJ() {
        return YQKZJ;
    }

    public void setYQKZJ(String YQKZJ) {
        this.YQKZJ = YQKZJ;
    }

    public ArrayList<GetOverdueModificationList> getGetOverdueModificationList() {
        return getOverdueModificationList;
    }

    public void setGetOverdueModificationList(ArrayList<GetOverdueModificationList> getOverdueModificationList) {
        this.getOverdueModificationList = getOverdueModificationList;
    }
}
