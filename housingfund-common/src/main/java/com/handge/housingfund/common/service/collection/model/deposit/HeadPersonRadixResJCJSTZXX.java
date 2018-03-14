package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadPersonRadixResJCJSTZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadPersonRadixResJCJSTZXX    implements Serializable,Comparable<HeadPersonRadixResJCJSTZXX>{

    private static final long serialVersionUID = -3326150701243556476L;
    private  String    JZNY;  //缴至年月

   private  String    TZQJCJS;  //调整前缴存基数

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   private  String    TZHJCJS;  //调整后缴存基数

   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getTZQJCJS(){

       return this.TZQJCJS;   

   }


   public  void setTZQJCJS(String  TZQJCJS){

       this.TZQJCJS = TZQJCJS;   

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


   public  String getTZHJCJS(){

       return this.TZHJCJS;   

   }


   public  void setTZHJCJS(String  TZHJCJS){

       this.TZHJCJS = TZHJCJS;   

   }


   public String toString(){ 

       return "HeadPersonRadixResJCJSTZXX{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "TZQJCJS='" + this.TZQJCJS + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "TZHJCJS='" + this.TZHJCJS + '\'' + 

      "}";

  }

    @Override
    public int compareTo(HeadPersonRadixResJCJSTZXX o) {
       return  this.GRZH.compareTo(o.getGRZH());

    }
}