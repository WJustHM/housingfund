package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/13.
 * 描述
 */
@XmlRootElement(name = "CommonReturn")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonReturn implements Serializable {
    /**
     * @param taskId 业务流水号
     * @param status 操作结果（0成功 1失败）
     */
    private String taskId;
    private String status;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CommonReturn{" +
                "taskId='" + taskId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
