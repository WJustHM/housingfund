package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/8/14.
 */

/**
 * （批量）审核的前端传入对象
 */
@XmlRootElement(name = "BatchReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchReviewInfo implements Serializable {

    private AuditInfo auditInfo;

    private ArrayList<String> ids;

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "BatchReviewInfo{" +
                "auditInfo=" + auditInfo +
                ", ids=" + ids +
                '}';
    }
}
