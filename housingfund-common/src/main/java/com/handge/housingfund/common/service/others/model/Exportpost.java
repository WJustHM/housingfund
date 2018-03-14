package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sjw on 2017/10/23.
 */
@XmlRootElement(name = "Exportpost")
@XmlAccessorType(XmlAccessType.FIELD)
public class Exportpost {
    private static final long serialVersionUID = 4673363965436851921L;
    protected String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
