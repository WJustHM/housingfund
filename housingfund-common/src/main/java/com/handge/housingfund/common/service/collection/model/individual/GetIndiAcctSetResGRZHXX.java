package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctSetResGRZHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctSetResGRZHXX  implements Serializable{

   private  String    GRJCJS;  //个人缴存基数

   private  String    GJJSCHJNY;  //公积金首次汇缴年月 

   private  String    GRCKZHKHYHMC;  //个人存款账户开户银行名称

   private  String    GRYJCE;  //个人月缴存额

   private  String    YJCE;  //月缴存额

   private  String    GRZH;  //个人账号 

   private  String    GRJCBL;  //个人缴存比例

   private  String    DWYJCE;  //单位月缴存额

   private  String    KHRQ;  //开户日期 

   private  String    GRZHZT;  //个人账户状态 

   private  String    DWJCBL;  //单位缴存比例

   private  String    GRCKZHKHYHDM;  //个人存款账户开户银行代码 

   private  String    GRCKZHHM;  //个人存款账户号码 

   public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public  String getGJJSCHJNY(){ 

       return this.GJJSCHJNY;   

   }


   public  void setGJJSCHJNY(String  GJJSCHJNY){ 

       this.GJJSCHJNY = GJJSCHJNY;   

   }


   public  String getGRCKZHKHYHMC(){

       return this.GRCKZHKHYHMC;

   }


   public  void setGRCKZHKHYHMC(String  GRCKZHKHYHMC){

       this.GRCKZHKHYHMC = GRCKZHKHYHMC;

   }


   public  String getGRYJCE(){

       return this.GRYJCE;   

   }


   public  void setGRYJCE(String  GRYJCE){

       this.GRYJCE = GRYJCE;   

   }


   public  String getYJCE(){

       return this.YJCE;   

   }


   public  void setYJCE(String  YJCE){

       this.YJCE = YJCE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getGRJCBL(){

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){

       this.GRJCBL = GRJCBL;   

   }


   public  String getDWYJCE(){

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){

       this.DWYJCE = DWYJCE;   

   }


   public  String getKHRQ(){ 

       return this.KHRQ;   

   }


   public  void setKHRQ(String  KHRQ){ 

       this.KHRQ = KHRQ;   

   }


   public  String getGRZHZT(){ 

       return this.GRZHZT;   

   }


   public  void setGRZHZT(String  GRZHZT){ 

       this.GRZHZT = GRZHZT;   

   }


   public  String getDWJCBL(){

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){

       this.DWJCBL = DWJCBL;   

   }


   public  String getGRCKZHKHYHDM(){ 

       return this.GRCKZHKHYHDM;   

   }


   public  void setGRCKZHKHYHDM(String  GRCKZHKHYHDM){ 

       this.GRCKZHKHYHDM = GRCKZHKHYHDM;   

   }


   public  String getGRCKZHHM(){ 

       return this.GRCKZHHM;   

   }


   public  void setGRCKZHHM(String  GRCKZHHM){ 

       this.GRCKZHHM = GRCKZHHM;   

   }


   public String toString(){ 

       return "GetIndiAcctSetResGRZHXX{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "GJJSCHJNY='" + this.GJJSCHJNY + '\'' + "," +
              "GRCKZHKHYHMC='" + this.GRCKZHKHYHMC + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + "," +
              "YJCE='" + this.YJCE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "GRJCBL='" + this.GRJCBL + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "KHRQ='" + this.KHRQ + '\'' + "," +
              "GRZHZT='" + this.GRZHZT + '\'' + "," +
              "DWJCBL='" + this.DWJCBL + '\'' + "," +
              "GRCKZHKHYHDM='" + this.GRCKZHKHYHDM + '\'' + "," +
              "GRCKZHHM='" + this.GRCKZHHM + '\'' + 

      "}";

  } 
}