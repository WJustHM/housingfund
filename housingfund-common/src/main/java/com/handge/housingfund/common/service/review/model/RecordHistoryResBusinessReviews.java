package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/8/31.
 */

/**
 * 业务历史记录查询的返回结果（详情层）
 */
@XmlRootElement(name = "RecordHistoryResBusinessReviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordHistoryResBusinessReviews implements Serializable{

    private String YYYJ;

    private String CZY; //

    private String YWWD;    //

    private String ZhiWu;

    private String CZQD;    //

    private Date SHSJ;  //审核受理时间

    private String CZNR;    //

    public String getYYYJ() {
        return YYYJ;
    }

    public void setYYYJ(String YYYJ) {
        this.YYYJ = YYYJ;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getZhiWu() {
        return ZhiWu;
    }

    public void setZhiWu(String zhiWu) {
        ZhiWu = zhiWu;
    }

    public String getCZQD() {
        return CZQD;
    }

    public void setCZQD(String CZQD) {
        this.CZQD = CZQD;
    }

    public Date getSHSJ() {
        return SHSJ;
    }

    public void setSHSJ(Date SHSJ) {
        this.SHSJ = SHSJ;
    }

    public String getCZNR() {
        return CZNR;
    }

    public void setCZNR(String CZNR) {
        this.CZNR = CZNR;
    }

    @Override
    public String toString() {
        return "RecordHistoryResBusinessReviews{" +
                "YYYJ='" + YYYJ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", ZhiWu='" + ZhiWu + '\'' +
                ", CZQD='" + CZQD + '\'' +
                ", SHSJ=" + SHSJ +
                ", CZNR='" + CZNR + '\'' +
                '}';
    }
}
