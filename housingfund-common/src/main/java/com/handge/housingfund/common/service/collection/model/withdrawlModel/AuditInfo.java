package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-6-27.
 */

@XmlRootElement(name = "AuditInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditInfo implements Serializable {
    /**
     * status 审核结果（01通过/02不通过）
     * Reason 原因
     */
    private String status ;
    private String reason;

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "AuditInfo{" +
                "status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
