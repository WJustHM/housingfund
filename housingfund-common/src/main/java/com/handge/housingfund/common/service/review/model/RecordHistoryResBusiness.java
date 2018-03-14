package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/8/31.
 */

/**
 * 业务历史记录查询的返回结果（最外层）
 */
@XmlRootElement(name = "RecordHistoryResBusiness")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordHistoryResBusiness implements Serializable{

    private String YWLSH;

    private String YWLX;

    private ArrayList<RecordHistoryResBusinessReviews> ReviewsRecords;

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public ArrayList<RecordHistoryResBusinessReviews> getReviewsRecords() {
        return ReviewsRecords;
    }

    public void setReviewsRecords(ArrayList<RecordHistoryResBusinessReviews> reviewsRecords) {
        ReviewsRecords = reviewsRecords;
    }

    @Override
    public String toString() {
        return "RecordHistoryResBusiness{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", ReviewsRecords=" + ReviewsRecords +
                '}';
    }
}
