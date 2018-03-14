package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/8/29.
 */

/**
 * （批量）是否正在审核的验证，前端传入对象
 */
@XmlRootElement(name = "BatchReviewCheck")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchReviewCheck {

    private String ywmk;                  //业务模块

    private String shybh;                 //审核员编号

    private ArrayList<String> ids;        //业务流水号集合

    public String getYwmk() {
        return ywmk;
    }

    public void setYwmk(String ywmk) {
        this.ywmk = ywmk;
    }

    public String getShybh() {
        return shybh;
    }

    public void setShybh(String shybh) {
        this.shybh = shybh;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "BatchReviewCheck{" +
                "ywmk='" + ywmk + '\'' +
                ", shybh='" + shybh + '\'' +
                ", ids=" + ids +
                '}';
    }
}
