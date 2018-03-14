package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;


@XmlRootElement(name = "ListEmployeeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListEmployeeRes implements Serializable ,Comparable<ListEmployeeRes> {

    private String XingMing;  //姓名
    private String GRZH;  //个人账号
    private String ZJHM;  //证件号码
    private String GRZHZT;  //个人账户状态
    private String GRJCJS;  //个人缴存基数
    private String GRYJCE;  //个人月缴存额
    private String DWYJCE;  //单位月缴存额
    private String YJCE;  //月缴存额
    private String JZNY;  //缴至年月
    private String GRZHYE;  //个人账户余额
    private String SFDK;        //是否贷款

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getGRZHZT() {
        return GRZHZT;
    }

    public void setGRZHZT(String GRZHZT) {
        this.GRZHZT = GRZHZT;
    }

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public String getDWYJCE() {
        return DWYJCE;
    }

    public void setDWYJCE(String DWYJCE) {
        this.DWYJCE = DWYJCE;
    }

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    public String getSFDK() {
        return SFDK;
    }

    public void setSFDK(String SFDK) {
        this.SFDK = SFDK;
    }

    @Override
    public String toString() {
        return "ListEmployeeRes{" +
                "XingMing='" + XingMing + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", GRZHZT='" + GRZHZT + '\'' +
                ", GRJCJS='" + GRJCJS + '\'' +
                ", GRYJCE='" + GRYJCE + '\'' +
                ", DWYJCE='" + DWYJCE + '\'' +
                ", YJCE='" + YJCE + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                ", SFDK='" + SFDK + '\'' +
                '}';
    }
    @Override
    public int compareTo(ListEmployeeRes o) {
        String grzhzt = this.getGRZHZT();
        if(grzhzt.equals(o.getGRZHZT())){
            return 0;
        }
        if("01".equals(grzhzt)){
            return -1;
        }
        if("02".equals(grzhzt)){
            return 1;
        }
        return 1;
    }
    public static class GrzhztCompator implements Comparator<ListEmployeeRes> {
        @Override
        public int compare(ListEmployeeRes listEmployeeRes, ListEmployeeRes listEmployeeRes2) {
            return listEmployeeRes.getGRZHZT().compareTo(listEmployeeRes2.getGRZHZT());
        }
    }
    public ListEmployeeRes() {

    }
    public ListEmployeeRes(Object[] obj) {
        this.setGRZH(obj[0].toString());
        this.setXingMing(obj[1].toString());
        this.setZJHM(obj[2].toString());
        this.setGRZHZT(obj[3].toString());
        if(obj[4]!=null){
            this.setGRJCJS(obj[4].toString());
        }
        if(obj[5]!=null){
            this.setGRYJCE(obj[5].toString());
        }
        if(obj[6]!=null){
            this.setDWYJCE(obj[6].toString());
        }
        if(obj[7]!=null){
            this.setJZNY(obj[7].toString());
        }
        if(obj[8]!=null){
            this.setGRZHYE(obj[8].toString());
        }
        if(obj[9]!=null){
            this.setSFDK("是");
        }else{
            this.setSFDK("否");
        }
        BigDecimal dwyjce = new BigDecimal(this.getDWYJCE()!=null?this.getDWYJCE():"0.00");
        BigDecimal gryjce = new BigDecimal(this.getGRYJCE()!=null?this.getGRYJCE():"0.00");
        this.setYJCE(dwyjce.add(gryjce).toString());

    }
}