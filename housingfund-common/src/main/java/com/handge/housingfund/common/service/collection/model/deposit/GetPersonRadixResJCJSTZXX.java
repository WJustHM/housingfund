package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetPersonRadixResJCJSTZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPersonRadixResJCJSTZXX  implements Serializable{

    private static final long serialVersionUID = 8873740889261789332L;
    private  String    JZNY;  //缴至年月

   private  String TZQGRJCJS;  //调整前缴存基数

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   private  String TZHGRJCJS;  //调整后缴存基数

    private String ZJHM;

    private String GRYJCE; //个人月缴存

    private String DWYJCE; //单位月缴存

    private String YJCE; // 月缴存

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public  String getJZNY(){

       return this.JZNY;   

   }


    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public  void setJZNY(String  JZNY){

       this.JZNY = JZNY;   

   }


   public  String getTZQGRJCJS(){

       return this.TZQGRJCJS;

   }


   public  void setTZQGRJCJS(String TZQGRJCJS){

       this.TZQGRJCJS = TZQGRJCJS;

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getTZHGRJCJS(){

       return this.TZHGRJCJS;

   }


   public  void setTZHGRJCJS(String TZHGRJCJS){

       this.TZHGRJCJS = TZHGRJCJS;

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

    public String toString(){

       return "GetPersonRadixResJCJSTZXX{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "TZQGRJCJS='" + this.TZQGRJCJS + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "TZHGRJCJS='" + this.TZHGRJCJS + '\'' +
               "GRYJCE='" + this.GRYJCE + '\'' +
               "DWYJCE='" + this.DWYJCE + '\'' +
               "YJCE='" + this.YJCE + '\'' +

      "}";

  } 
}