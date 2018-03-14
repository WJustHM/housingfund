package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoIndiAcctActionResGRZHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoIndiAcctActionResGRZHXX  implements Serializable{

   private  String    GRJCBL;  //个人缴存比例

   private  String    GRJCJS;  //个人缴存基数 

   private  String    GRZHSNJZYE;  //个人账户上年结转余额

   private  String    GRZHYE;  //个人账户余额

   private  String    Xingming;  //姓名 

   private  String    GRJZNY;  //个人缴至年月 

   private  String    ZJHM;  //证件号码 

   private  String    GRZHDNGJYE;  //个人账户当年归集余额

   private  String    GRZHZT;  //个人账户状态 

   private  String    ZJLX;  //证件类型 

   private  String    DWJCBL;  //单位缴存比例

   private  String    GRZH;  //个人账号

   private  String    KHRQ;  //开户日期

    private String    GJJSCHJNY;//公积金首次汇缴年月

    public String getGJJSCHJNY() {
        return GJJSCHJNY;
    }

    public void setGJJSCHJNY(String GJJSCHJNY) {
        this.GJJSCHJNY = GJJSCHJNY;
    }

    public String getKHRQ() {
        return KHRQ;
    }

    public void setKHRQ(String KHRQ) {
        this.KHRQ = KHRQ;
    }

    public  String getGRJCBL(){

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){

       this.GRJCBL = GRJCBL;   

   }


   public  String getGRJCJS(){ 

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){ 

       this.GRJCJS = GRJCJS;   

   }


   public  String getGRZHSNJZYE(){

       return this.GRZHSNJZYE;   

   }


   public  void setGRZHSNJZYE(String  GRZHSNJZYE){

       this.GRZHSNJZYE = GRZHSNJZYE;   

   }


   public  String getGRZHYE(){

       return this.GRZHYE;   

   }


   public  void setGRZHYE(String  GRZHYE){

       this.GRZHYE = GRZHYE;   

   }


   public  String getXingming(){ 

       return this.Xingming;   

   }


   public  void setXingming(String  Xingming){ 

       this.Xingming = Xingming;   

   }


   public  String getGRJZNY(){ 

       return this.GRJZNY;   

   }


   public  void setGRJZNY(String  GRJZNY){ 

       this.GRJZNY = GRJZNY;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGRZHDNGJYE(){

       return this.GRZHDNGJYE;   

   }


   public  void setGRZHDNGJYE(String  GRZHDNGJYE){

       this.GRZHDNGJYE = GRZHDNGJYE;   

   }


   public  String getGRZHZT(){ 

       return this.GRZHZT;   

   }


   public  void setGRZHZT(String  GRZHZT){ 

       this.GRZHZT = GRZHZT;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

   }


   public  String getDWJCBL(){

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){

       this.DWJCBL = DWJCBL;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public String toString(){ 

       return "AutoIndiAcctActionResGRZHXX{" +  
 
              "GRJCBL='" + this.GRJCBL + '\'' + "," +
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "GRZHSNJZYE='" + this.GRZHSNJZYE + '\'' + "," +
              "GRZHYE='" + this.GRZHYE + '\'' + "," +
              "Xingming='" + this.Xingming + '\'' + "," +
              "GRJZNY='" + this.GRJZNY + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "GRZHDNGJYE='" + this.GRZHDNGJYE + '\'' + "," +
              "GRZHZT='" + this.GRZHZT + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "DWJCBL='" + this.DWJCBL + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' +
              "KHRQ='" + this.KHRQ + '\'' +

               "}";

  } 
}