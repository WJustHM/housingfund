package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/10/11.
 */
@XmlRootElement(name = "CommonDistrict")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonDistrict implements Serializable {
    private static final long serialVersionUID = 4673363965436851981L;
    protected String id;
    protected String parent;
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public CommonDistrict(String id, String parent, String name) {
        this.id = id;
        this.parent = parent;
        this.name = name;
    }
}
