package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
@XmlRootElement(name = "AccountEmployeeDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountEmployeeDetail extends Employee implements Serializable {

    private String id;

    private String zhangaho;//员工账号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZhangaho() {
        return zhangaho;
    }

    public void setZhangaho(String zhangaho) {
        this.zhangaho = zhangaho;
    }
}
