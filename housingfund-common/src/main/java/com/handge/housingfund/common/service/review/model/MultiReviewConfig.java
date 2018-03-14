package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Liujuhao on 2017/9/18.
 */

/**
 * 多级审核的配置信息
 */
@XmlRootElement(name = "MultiReviewConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiReviewConfig implements Serializable{

    private static final long serialVersionUID = 8155895450134734333L;

    private HashSet<String> LSSHYBH;   //历史审核员编号（集合）

    private String DQSHY;    //当前审核员

    private String SCSHY;   //上次审核员

    private String DQXM;    //当前（审核员）姓名

    private String SHJB;   //审核级别（0：普审，1：特审）

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }

    public String getSHJB() {
        return SHJB;
    }

    public void setSHJB(String SHJB) {
        this.SHJB = SHJB;
    }

    public HashSet<String> getLSSHYBH() {
        return LSSHYBH;
    }

    public void setLSSHYBH(HashSet<String> LSSHYBH) {
        this.LSSHYBH = LSSHYBH;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    @Override
    public String toString() {
        return "MultiReviewConfig{" +
                "LSSHYBH=" + LSSHYBH +
                ", DQSHY='" + DQSHY + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SHJB='" + SHJB + '\'' +
                '}';
    }
}
