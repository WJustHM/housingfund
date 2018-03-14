package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-24.
 */

/**
 * 前端审核交互对象
 */
@XmlRootElement(name = "AuditInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditInfo implements Serializable{

    private String EVENT;   //审核结果
    private String NOTE;    //备注
    private String YYYJ;    //原因意见
    private String QZSJ;    //签章数据

    public AuditInfo(String EVENT, String NOTE, String YYYJ, String QZSJ) {
        this.EVENT = EVENT;
        this.NOTE = NOTE;
        this.YYYJ = YYYJ;
        this.QZSJ = QZSJ;
    }

    public AuditInfo() {

    }

    public String getYYYJ() {
        return YYYJ;
    }

    public void setYYYJ(String YYYJ) {
        this.YYYJ = YYYJ;
    }

    public String getQZSJ() {
        return QZSJ;
    }

    public void setQZSJ(String QZSJ) {
        this.QZSJ = QZSJ;
    }

    public String getEvent() {
        return EVENT;
    }

    public void setEvent(String event) {
        this.EVENT = event;
    }

    public String getNote() {
        return NOTE;
    }

    public void setNote(String note) {
        this.NOTE = note;
    }

    @Override
    public String toString() {
        return "AuditInfo{" +
                "EVENT='" + EVENT + '\'' +
                ", NOTE='" + NOTE + '\'' +
                ", YYYJ='" + YYYJ + '\'' +
                ", QZSJ='" + QZSJ + '\'' +
                '}';
    }
}
