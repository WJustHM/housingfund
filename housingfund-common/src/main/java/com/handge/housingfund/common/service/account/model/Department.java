package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gxy on 17-7-17.
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.FIELD)
public class Department implements Serializable {

    private String id;//id

    private String MingCheng;//名称

    private String WangDian_id;//网点ID

    private String FZR;//负责人

    private String LXDH;//联系电话

    private List<String> BMCY;//成员列表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMingCheng() {
        return MingCheng;
    }

    public void setMingCheng(String mingCheng) {
        MingCheng = mingCheng;
    }

    public String getWangDian_id() {
        return WangDian_id;
    }

    public void setWangDian_id(String wangDian_id) {
        WangDian_id = wangDian_id;
    }

    public String getFZR() {
        return FZR;
    }

    public void setFZR(String FZR) {
        this.FZR = FZR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public List<String> getBMCY() {
        return BMCY;
    }

    public void setBMCY(List<String> BMCY) {
        this.BMCY = BMCY;
    }
}
