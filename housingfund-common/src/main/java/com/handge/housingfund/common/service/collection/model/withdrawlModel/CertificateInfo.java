package com.handge.housingfund.common.service.collection.model.withdrawlModel;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URL;

/**
 * Created by xuefei_wang on 17-6-21.
 */
@XmlRootElement(name = "certificate")
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificateInfo implements Serializable {
    /**
     * taskId 业务流水号
     * name 姓名
     * type 类型
     * url 资料URL地址
     */
    @XmlElement(name = "TaskId")
    private String taskId;
    @XmlElement(name = "NAME")
    private String name;
    @XmlElement(name = "TYPE")
    private String type;
    @XmlElement(name = "URL")
    private URL url;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "CertificateInfo{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url=" + url +
                '}';
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
