package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "InnerTransferPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class InnerTransferPost implements Serializable {

    private String DWZH;//现单位账号

    private String GJJSCHJNY;//公积金首次汇缴年月

    private String ZJHM;//证件号码

    private String XingMing;//姓名

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    private String GRCKZHHM;//个人存款账户号码

    private String GRJCJS;//个人缴存基数
    public String getGRCKZHHM() {
        return GRCKZHHM;
    }

    public void setGRCKZHHM(String GRCKZHHM) {
        this.GRCKZHHM = GRCKZHHM;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public String getGJJSCHJNY() {
        return GJJSCHJNY;
    }

    public void setGJJSCHJNY(String GJJSCHJNY) {
        this.GJJSCHJNY = GJJSCHJNY;
    }

    @Override
    public String toString() {
        return "InnerTransferPost{" +
                "DWZH='" + DWZH + '\'' +
                ", GJJSCHJNY='" + GJJSCHJNY + '\'' +
                '}';
    }
}