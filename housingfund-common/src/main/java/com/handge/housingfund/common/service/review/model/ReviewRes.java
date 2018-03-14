package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/8/7.
 */

/**
 *  （批量）审核时的结果返回
 */
@XmlRootElement(name = "ReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewRes implements Serializable{

    private String Status;  //状态（success成功，fail失败）

    private String YWLSH;  //业务流水号

    private ArrayList<String> CGYWLSH; // 成功业务流水号集合

    private ArrayList<String> SBYWLSH; //失败业务流水号集合

    public ArrayList<String> getCGYWLSH() {
        return CGYWLSH;
    }

    public void setCGYWLSH(ArrayList<String> CGYWLSH) {
        this.CGYWLSH = CGYWLSH;
    }

    public ArrayList<String> getSBYWLSH() {
        return SBYWLSH;
    }

    public void setSBYWLSH(ArrayList<String> SBYWLSH) {
        this.SBYWLSH = SBYWLSH;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    @Override
    public String toString() {
        return "ReviewRes{" +
                "Status='" + Status + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", CGYWLSH=" + CGYWLSH +
                ", SBYWLSH=" + SBYWLSH +
                '}';
    }
}
